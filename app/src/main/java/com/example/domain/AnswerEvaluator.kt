package com.example.domain

import com.example.model.AnswerKind
import com.example.model.CircuitProblem
import com.example.model.ComplexNum
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.hypot
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

sealed class EvaluationResult {
    data class Correct(val parsedDisplay: String, val xpEarned: Int, val note: String) : EvaluationResult()
    data class Incorrect(val parsedDisplay: String, val diagnosticTip: String) : EvaluationResult()
    data class InvalidFormat(val reason: String) : EvaluationResult()
}

object AnswerEvaluator {

    fun evaluate(
        problem: CircuitProblem,
        rawInput: String,
        hintsUsed: Int,
        currentStreak: Int
    ): EvaluationResult {
        val trimmed = rawInput.trim()
        if (trimmed.isEmpty()) {
            return EvaluationResult.InvalidFormat("Enter a value before submitting.")
        }

        val unitCleaned = stripMatchingUnit(trimmed, problem.unit)
        if (unitCleaned.isEmpty()) {
            return EvaluationResult.InvalidFormat("Enter a numeric value before submitting.")
        }

        return when (problem.answerKind) {
            AnswerKind.REAL_SCALAR -> evaluateReal(problem, unitCleaned, hintsUsed, currentStreak)
            AnswerKind.COMPLEX_RECT_OR_POLAR -> evaluateComplex(problem, unitCleaned, hintsUsed, currentStreak)
        }
    }

    private fun stripMatchingUnit(raw: String, unit: String): String {
        val trimmed = raw.trim()
        if (unit.isBlank()) return trimmed

        val aliases = when (unit) {
            "Ω" -> listOf("ohms", "ohm", "Ω")
            "°" -> listOf("degrees", "degree", "deg", "°")
            "μF" -> listOf("μF", "uF")
            "μH" -> listOf("μH", "uH")
            else -> listOf(unit)
        }.sortedByDescending { it.length }

        for (alias in aliases) {
            if (trimmed.endsWith(alias, ignoreCase = true) && trimmed.length > alias.length) {
                val prefix = trimmed.dropLast(alias.length)
                val lastChar = prefix.last()
                // Only strip if the unit follows a digit, ')', '°', or whitespace (never slice another word)
                if (lastChar.isDigit() || lastChar == ')' || lastChar == '°' || lastChar.isWhitespace()) {
                    return prefix.trimEnd()
                }
            }
        }
        return trimmed
    }

    private fun evaluateReal(
        problem: CircuitProblem,
        raw: String,
        hintsUsed: Int,
        currentStreak: Int
    ): EvaluationResult {
        val expected = problem.expectedReal ?: return EvaluationResult.InvalidFormat("Missing expected answer.")
        val parsed = parseRealExpression(raw)
            ?: return EvaluationResult.InvalidFormat("Could not parse number. Try a decimal (e.g. 0.707), fraction (3/4), or expr (e^(-pi/2)).")

        val tol = max(problem.tolerance * abs(expected), 0.002)
        val diff = abs(parsed - expected)
        val parsedStr = ComplexNum.formatClean(parsed)

        return if (diff <= tol) {
            val xp = computeXp(problem.difficulty.xpReward, hintsUsed, currentStreak)
            EvaluationResult.Correct(
                parsedDisplay = parsedStr,
                xpEarned = xp,
                note = "Exact match within engineering tolerance."
            )
        } else {
            val tip = when {
                expected != 0.0 && abs(parsed + expected) <= tol ->
                    "Magnitude matches, check your sign (+/-)."
                expected != 0.0 && abs(parsed * 1000.0 - expected) <= tol * 1000.0 ->
                    "Check your unit prefix (milli / kilo scaling)."
                expected != 0.0 && abs(parsed / 1000.0 - expected) <= tol ->
                    "Check your unit prefix (milli / kilo scaling)."
                abs(parsed - expected) < max(tol * 4, 0.05) ->
                    "Very close! Check rounding or intermediate steps."
                else ->
                    "Value is off from expected result. Try revealing a hint or checking your formula."
            }
            EvaluationResult.Incorrect(parsedDisplay = parsedStr, diagnosticTip = tip)
        }
    }

    private fun evaluateComplex(
        problem: CircuitProblem,
        raw: String,
        hintsUsed: Int,
        currentStreak: Int
    ): EvaluationResult {
        val expected = problem.expectedComplex ?: return EvaluationResult.InvalidFormat("Missing expected complex answer.")
        val parsed = parseComplex(raw)
            ?: return EvaluationResult.InvalidFormat("Try rectangular (8 - j6), polar (10∠-36.87°), or Euler (10e^(j90°)).")

        val tol = max(problem.tolerance * expected.magnitude, 0.003)
        val distance = hypot(parsed.re - expected.re, parsed.im - expected.im)
        val parsedStr = if (abs(parsed.im) > 1e-6) "${parsed.formatRect()} (${parsed.formatPolar()})" else parsed.formatRect()

        return if (distance <= tol) {
            val xp = computeXp(problem.difficulty.xpReward, hintsUsed, currentStreak)
            EvaluationResult.Correct(
                parsedDisplay = parsedStr,
                xpEarned = xp,
                note = "Complex phasor verified (${parsed.formatPolar()})."
            )
        } else {
            val magTol = max(problem.tolerance * expected.magnitude, 0.004)
            val tip = when {
                abs(parsed.magnitude - expected.magnitude) <= magTol && abs(parsed.im + expected.im) <= tol ->
                    "Magnitude matches! Check the sign of your imaginary / phase component."
                abs(parsed.magnitude - expected.magnitude) <= magTol ->
                    "Magnitude (${ComplexNum.formatClean(parsed.magnitude)}) is right — check your phase angle."
                abs(parsed.re - expected.im) <= tol && abs(parsed.im - expected.re) <= tol ->
                    "Real and imaginary components appear swapped."
                else ->
                    "Phasor mismatch. Verify reactance signs (+jωL vs -j/ωC)."
            }
            EvaluationResult.Incorrect(parsedDisplay = parsedStr, diagnosticTip = tip)
        }
    }

    private fun computeXp(baseXp: Int, hintsUsed: Int, currentStreak: Int): Int {
        val hintPenalty = hintsUsed * 25
        val streakBonus = (currentStreak.coerceAtMost(5)) * 15
        return (baseXp - hintPenalty + streakBonus).coerceAtLeast(30)
    }

    fun parseComplex(raw: String): ComplexNum? {
        val normalized = normalizeInput(raw)
        if (normalized.isEmpty()) return null

        // Fast-path clean single polar form (e.g. "10∠pi/4 rad", "10∠-36.87°") so angle expressions with '/' parse cleanly
        parseSingleTopLevelPolar(normalized)?.let { return it }

        val tokens = tokenize(normalized, allowImaginary = true) ?: return null
        val parser = ExprParser(tokens, allowImaginary = true)
        val result = parser.parseFullExpression() ?: return null
        if (!result.re.isFinite() || !result.im.isFinite()) return null
        return result
    }

    fun parseRealExpression(raw: String, degreeFactor: Double = Math.PI / 180.0): Double? {
        val normalized = normalizeInput(raw)
        if (normalized.isEmpty()) return null

        val tokens = tokenize(normalized, allowImaginary = false) ?: return null
        val parser = ExprParser(tokens, allowImaginary = false, degreeFactor = degreeFactor)
        val result = parser.parseFullExpression() ?: return null
        if (!result.re.isFinite() || !result.im.isFinite() || abs(result.im) > 1e-9) return null
        return result.re
    }

    private fun normalizeInput(raw: String): String = raw
        .trim()
        .replace("−", "-")
        .replace("×", "*")
        .replace("·", "*")
        .replace("÷", "/")
        .replace("π", "pi")
        .replace("ω", "")

    private fun parseSingleTopLevelPolar(s: String): ComplexNum? {
        var depth = 0
        var sepStart = -1
        var sepLen = 0
        var sepCount = 0

        var idx = 0
        while (idx < s.length) {
            val ch = s[idx]
            when (ch) {
                '(' -> depth++
                ')' -> depth--
                else -> if (depth == 0) {
                    val matchedLen = when {
                        ch == '∠' || ch == '<' || ch == '@' -> 1
                        s.startsWith("angle", idx, ignoreCase = true) &&
                            (idx == 0 || !s[idx - 1].isLetter()) &&
                            (idx + 5 == s.length || !s[idx + 5].isLetter()) -> 5
                        else -> 0
                    }
                    if (matchedLen > 0) {
                        sepStart = idx
                        sepLen = matchedLen
                        sepCount++
                        idx += matchedLen
                        continue
                    }
                }
            }
            idx++
        }

        if (sepCount != 1) return null

        val magStr = s.substring(0, sepStart).trim()
        var angleStr = s.substring(sepStart + sepLen).trim()
        if (magStr.isEmpty() || angleStr.isEmpty()) return null

        val mag = parseRealExpression(magStr) ?: return null

        var isRadians = false
        val radSuffix = listOf("radians", "radian", "rad").firstOrNull { suffix ->
            angleStr.endsWith(suffix, ignoreCase = true) &&
                angleStr.length > suffix.length &&
                !angleStr[angleStr.length - suffix.length - 1].isLetter()
        }
        val degSuffix = listOf("degrees", "degree", "deg").firstOrNull { suffix ->
            angleStr.endsWith(suffix, ignoreCase = true) &&
                angleStr.length > suffix.length &&
                !angleStr[angleStr.length - suffix.length - 1].isLetter()
        }
        when {
            radSuffix != null -> {
                isRadians = true
                angleStr = angleStr.dropLast(radSuffix.length).trimEnd()
            }
            degSuffix != null -> {
                angleStr = angleStr.dropLast(degSuffix.length).trimEnd()
            }
            angleStr.endsWith("°") -> {
                angleStr = angleStr.dropLast(1).trimEnd()
            }
        }

        val angleVal = parseRealExpression(angleStr, degreeFactor = 1.0) ?: return null
        if (!mag.isFinite() || !angleVal.isFinite()) return null

        val deg = if (isRadians) Math.toDegrees(angleVal) else angleVal
        return ComplexNum.fromPolarDeg(mag, deg)
    }

    private sealed class Tok {
        data class Num(val v: Double) : Tok()
        data class Const(val name: String) : Tok() // "pi", "e", "j"
        data class Func(val name: String) : Tok()  // "sqrt", "exp", "ln", "log"
        data class Op(val op: Char) : Tok()        // '+', '-', '*', '/', '^'
        data object LParen : Tok()
        data object RParen : Tok()
        data object Polar : Tok()
        data object SqrtSym : Tok()
        data object DegSuffix : Tok()
        data object RadSuffix : Tok()
    }

    private fun tokenize(input: String, allowImaginary: Boolean): List<Tok>? {
        val out = mutableListOf<Tok>()
        var i = 0
        val n = input.length

        while (i < n) {
            val ch = input[i]
            if (ch.isWhitespace()) {
                i++
                continue
            }

            when (ch) {
                '+', '-', '*', '/', '^' -> {
                    out.add(Tok.Op(ch))
                    i++
                    continue
                }
                '(' -> {
                    out.add(Tok.LParen)
                    i++
                    continue
                }
                ')' -> {
                    out.add(Tok.RParen)
                    i++
                    continue
                }
                '∠', '<', '@' -> {
                    out.add(Tok.Polar)
                    i++
                    continue
                }
                '√' -> {
                    out.add(Tok.SqrtSym)
                    i++
                    continue
                }
                '°' -> {
                    out.add(Tok.DegSuffix)
                    i++
                    continue
                }
            }

            if (ch.isDigit() || ch == '.') {
                val start = i
                var seenDot = (ch == '.')
                i++
                while (i < n) {
                    val c = input[i]
                    if (c.isDigit()) {
                        i++
                    } else if (c == '.' && !seenDot) {
                        seenDot = true
                        i++
                    } else {
                        break
                    }
                }
                // Scientific notation ONLY when not colliding with GA bivector 'e12' / 'e_12'
                if (i < n && (input[i] == 'e' || input[i] == 'E')) {
                    val isGaBivector = input.startsWith("e_12", i, ignoreCase = true) ||
                        (input.startsWith("e12", i, ignoreCase = true) && (i + 3 == n || !input[i + 3].isDigit()))
                    if (!isGaBivector) {
                        var jIdx = i + 1
                        if (jIdx < n && (input[jIdx] == '+' || input[jIdx] == '-')) jIdx++
                        if (jIdx < n && input[jIdx].isDigit()) {
                            i = jIdx
                            while (i < n && input[i].isDigit()) i++
                        }
                    }
                }
                val num = input.substring(start, i).toDoubleOrNull() ?: return null
                if (!num.isFinite()) return null
                out.add(Tok.Num(num))
                continue
            }

            if (ch.isLetter()) {
                // Check Geometric Algebra bivector e12 / e_12 first
                if (input.startsWith("e_12", i, ignoreCase = true) && (i + 4 == n || !input[i + 4].isLetterOrDigit())) {
                    if (!allowImaginary) return null
                    out.add(Tok.Const("j"))
                    i += 4
                    continue
                }
                if (input.startsWith("e12", i, ignoreCase = true) && (i + 3 == n || !input[i + 3].isLetterOrDigit())) {
                    if (!allowImaginary) return null
                    out.add(Tok.Const("j"))
                    i += 3
                    continue
                }

                val start = i
                while (i < n && input[i].isLetter()) i++
                val word = input.substring(start, i).lowercase()
                when (word) {
                    "pi" -> out.add(Tok.Const("pi"))
                    "e" -> out.add(Tok.Const("e"))
                    "i", "j" -> {
                        if (!allowImaginary) return null
                        out.add(Tok.Const("j"))
                    }
                    "sqrt" -> out.add(Tok.Func("sqrt"))
                    "exp" -> out.add(Tok.Func("exp"))
                    "ln" -> out.add(Tok.Func("ln"))
                    "log" -> out.add(Tok.Func("log"))
                    "angle" -> out.add(Tok.Polar)
                    "deg", "degree", "degrees" -> out.add(Tok.DegSuffix)
                    "rad", "radian", "radians" -> out.add(Tok.RadSuffix)
                    else -> return null
                }
                continue
            }

            return null
        }

        return out
    }

    private class ExprParser(
        private val tokens: List<Tok>,
        private val allowImaginary: Boolean,
        private val degreeFactor: Double = Math.PI / 180.0
    ) {
        private var pos = 0

        private fun peek(): Tok? = tokens.getOrNull(pos)
        private fun consume(): Tok = tokens[pos++]

        fun parseFullExpression(): ComplexNum? {
            if (tokens.isEmpty()) return null
            val v = parseExpr() ?: return null
            if (pos != tokens.size) return null
            return v
        }

        private fun canEndTerm(tok: Tok?): Boolean = when (tok) {
            is Tok.Num, is Tok.Const, Tok.RParen, Tok.DegSuffix, Tok.RadSuffix -> true
            else -> false
        }

        private fun canStartTerm(tok: Tok?): Boolean = when (tok) {
            is Tok.Num, is Tok.Const, is Tok.Func, Tok.LParen, Tok.SqrtSym -> true
            else -> false
        }

        private fun parseExpr(): ComplexNum? {
            var acc = parseTerm() ?: return null
            while (true) {
                when (val t = peek()) {
                    is Tok.Op -> when (t.op) {
                        '+' -> {
                            consume()
                            val rhs = parseTerm() ?: return null
                            acc = ComplexNum(acc.re + rhs.re, acc.im + rhs.im)
                        }
                        '-' -> {
                            consume()
                            val rhs = parseTerm() ?: return null
                            acc = ComplexNum(acc.re - rhs.re, acc.im - rhs.im)
                        }
                        else -> break
                    }
                    else -> break
                }
            }
            return acc
        }

        private fun parseTerm(): ComplexNum? {
            var acc = parseFactor() ?: return null
            while (true) {
                val t = peek()
                when {
                    t is Tok.Op && t.op == '*' -> {
                        consume()
                        val rhs = parseFactor() ?: return null
                        acc = mulComplex(acc, rhs)
                    }
                    t is Tok.Op && t.op == '/' -> {
                        consume()
                        val rhs = parseFactor() ?: return null
                        acc = divComplex(acc, rhs) ?: return null
                    }
                    canStartTerm(t) && canEndTerm(tokens.getOrNull(pos - 1)) -> {
                        // Do not allow two bare numbers with only whitespace ("2 3") to silently multiply
                        if (tokens[pos - 1] is Tok.Num && t is Tok.Num) break
                        val rhs = parseFactor() ?: return null
                        acc = mulComplex(acc, rhs)
                    }
                    else -> break
                }
            }
            return acc
        }

        private fun parseFactor(): ComplexNum? {
            val t = peek()
            if (t is Tok.Op && (t.op == '+' || t.op == '-')) {
                consume()
                val inner = parseFactor() ?: return null
                return if (t.op == '-') ComplexNum(-inner.re, -inner.im) else inner
            }
            return parsePolar()
        }

        private fun parsePolar(): ComplexNum? {
            var lhs = parsePower() ?: return null
            while (peek() == Tok.Polar) {
                if (!allowImaginary) return null
                consume()
                // Allow unary sign in inline polar angles like "10∠-36.87°"
                var sign = 1.0
                while (true) {
                    val t = peek()
                    if (t is Tok.Op && (t.op == '+' || t.op == '-')) {
                        consume()
                        if (t.op == '-') sign *= -1.0
                    } else {
                        break
                    }
                }
                val rhsRaw = parsePowerNoDegScale() ?: return null
                if (abs(rhsRaw.im) > 1e-9 || abs(lhs.im) > 1e-9) return null
                var angleVal = sign * rhsRaw.re
                var isRadians = false
                when (peek()) {
                    Tok.DegSuffix -> consume()
                    Tok.RadSuffix -> {
                        consume()
                        isRadians = true
                    }
                    else -> Unit
                }
                val deg = if (isRadians) Math.toDegrees(angleVal) else angleVal
                lhs = ComplexNum.fromPolarDeg(lhs.re, deg)
            }
            return lhs
        }

        private fun parsePower(): ComplexNum? = parsePowerInternal(scaleDegreeSuffix = true)

        private fun parsePowerNoDegScale(): ComplexNum? = parsePowerInternal(scaleDegreeSuffix = false)

        private fun parsePowerInternal(scaleDegreeSuffix: Boolean): ComplexNum? {
            var base = parsePostfix(scaleDegreeSuffix) ?: return null
            if (peek() == Tok.Op('^')) {
                consume()
                val expVal = parseFactor() ?: return null
                base = powComplex(base, expVal) ?: return null
                while (peek() == Tok.DegSuffix) {
                    consume()
                    if (scaleDegreeSuffix && degreeFactor != 1.0) {
                        base = ComplexNum(base.re * degreeFactor, base.im * degreeFactor)
                    }
                }
            }
            return base
        }

        private fun parsePostfix(scaleDegreeSuffix: Boolean): ComplexNum? {
            var v = parsePrimary() ?: return null
            while (peek() == Tok.DegSuffix) {
                consume()
                if (scaleDegreeSuffix && degreeFactor != 1.0) {
                    v = ComplexNum(v.re * degreeFactor, v.im * degreeFactor)
                }
            }
            return v
        }

        private fun parsePrimary(): ComplexNum? {
            return when (val t = peek()) {
                is Tok.Num -> {
                    consume()
                    ComplexNum(t.v, 0.0)
                }
                is Tok.Const -> {
                    consume()
                    when (t.name) {
                        "pi" -> ComplexNum(Math.PI, 0.0)
                        "e" -> ComplexNum(Math.E, 0.0)
                        "j" -> ComplexNum(0.0, 1.0)
                        else -> null
                    }
                }
                is Tok.LParen -> {
                    consume()
                    val inner = parseExpr() ?: return null
                    if (peek() != Tok.RParen) return null
                    consume()
                    inner
                }
                is Tok.SqrtSym -> {
                    consume()
                    val inner = parsePrimary() ?: return null
                    sqrtComplex(inner)
                }
                is Tok.Func -> {
                    consume()
                    if (peek() != Tok.LParen) return null
                    consume()
                    val arg = parseExpr() ?: return null
                    if (peek() != Tok.RParen) return null
                    consume()
                    when (t.name) {
                        "sqrt" -> sqrtComplex(arg)
                        "exp" -> expComplex(arg)
                        "ln" -> lnComplex(arg)
                        "log" -> {
                            if (abs(arg.im) > 1e-9 || arg.re <= 0.0) return null
                            ComplexNum(log10(arg.re), 0.0)
                        }
                        else -> null
                    }
                }
                else -> null
            }
        }

        private fun mulComplex(a: ComplexNum, b: ComplexNum): ComplexNum = ComplexNum(
            re = a.re * b.re - a.im * b.im,
            im = a.re * b.im + a.im * b.re
        )

        private fun divComplex(a: ComplexNum, b: ComplexNum): ComplexNum? {
            val den = b.re * b.re + b.im * b.im
            if (den < 1e-18) return null
            return ComplexNum(
                re = (a.re * b.re + a.im * b.im) / den,
                im = (a.im * b.re - a.re * b.im) / den
            )
        }

        private fun expComplex(z: ComplexNum): ComplexNum {
            val mag = exp(z.re)
            return ComplexNum(mag * cos(z.im), mag * sin(z.im))
        }

        private fun lnComplex(z: ComplexNum): ComplexNum? {
            val mag = z.magnitude
            if (mag < 1e-18) return null
            return ComplexNum(ln(mag), atan2(z.im, z.re))
        }

        private fun sqrtComplex(z: ComplexNum): ComplexNum? {
            if (abs(z.im) < 1e-12) {
                if (z.re >= 0.0) return ComplexNum(sqrt(z.re), 0.0)
                if (!allowImaginary) return null
                return ComplexNum(0.0, sqrt(-z.re))
            }
            if (!allowImaginary) return null
            val r = z.magnitude
            val re = sqrt((r + z.re) / 2.0)
            val imSign = if (z.im >= 0.0) 1.0 else -1.0
            val im = imSign * sqrt((r - z.re) / 2.0)
            return ComplexNum(re, im)
        }

        private fun powComplex(base: ComplexNum, exponent: ComplexNum): ComplexNum? {
            if (abs(base.im) < 1e-12 && abs(exponent.im) < 1e-12) {
                if (base.re == 0.0 && exponent.re <= 0.0) return null
                if (base.re > 0.0) {
                    val v = base.re.pow(exponent.re)
                    return if (v.isFinite()) ComplexNum(v, 0.0) else null
                }
                val rounded = kotlin.math.round(exponent.re)
                if (abs(exponent.re - rounded) < 1e-12) {
                    val v = base.re.pow(rounded)
                    return if (v.isFinite()) ComplexNum(v, 0.0) else null
                }
            }
            if (base.magnitude < 1e-18) {
                return if (exponent.re > 0.0) ComplexNum(0.0, 0.0) else null
            }
            val lnBase = lnComplex(base) ?: return null
            return expComplex(mulComplex(exponent, lnBase))
        }
    }
}
