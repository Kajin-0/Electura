package com.example.domain

import com.example.model.AnswerKind
import com.example.model.CircuitProblem
import com.example.model.ComplexNum
import kotlin.math.abs
import kotlin.math.max
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

        return when (problem.answerKind) {
            AnswerKind.REAL_SCALAR -> evaluateReal(problem, trimmed, hintsUsed, currentStreak)
            AnswerKind.COMPLEX_RECT_OR_POLAR -> evaluateComplex(problem, trimmed, hintsUsed, currentStreak)
        }
    }

    private fun evaluateReal(
        problem: CircuitProblem,
        raw: String,
        hintsUsed: Int,
        currentStreak: Int
    ): EvaluationResult {
        val expected = problem.expectedReal ?: return EvaluationResult.InvalidFormat("Missing expected answer.")
        val parsed = parseRealExpression(raw)
            ?: return EvaluationResult.InvalidFormat("Could not parse number. Try a decimal (e.g. 0.707) or fraction (e.g. 3/4).")

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
            ?: return EvaluationResult.InvalidFormat("Try rectangular (e.g. 8 - j6) or polar (e.g. 10∠-36.87°).")

        val tol = max(problem.tolerance * expected.magnitude, 0.003)
        val distance = sqrt((parsed.re - expected.re) * (parsed.re - expected.re) + (parsed.im - expected.im) * (parsed.im - expected.im))
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
        val cleaned = raw
            .replace("°", "")
            .replace("deg", "", ignoreCase = true)
            .replace(" ", "")
            .replace("−", "-")
            .lowercase()

        // Check polar notation: R∠theta, R<theta, R@theta, RangleTheta
        val polarSeparators = listOf("∠", "<", "@", "angle")
        for (sep in polarSeparators) {
            if (cleaned.contains(sep)) {
                val parts = cleaned.split(sep)
                if (parts.size == 2) {
                    val mag = parseRealExpression(parts[0]) ?: return null
                    val deg = parseRealExpression(parts[1]) ?: return null
                    return ComplexNum.fromPolarDeg(mag, deg)
                }
                return null
            }
        }

        // Pure real number
        parseRealExpression(cleaned)?.let { return ComplexNum(it, 0.0) }

        // Rectangular notation: normalize 'i', 'e12', 'e_12' to 'j'
        val rect = cleaned
            .replace("e_12", "j")
            .replace("e12", "j")
            .replace('i', 'j')
        if (!rect.contains('j')) return null

        // Tokenize signed terms like "+8", "-j6", "+6j", "-j"
        val terms = mutableListOf<String>()
        var current = StringBuilder()
        for (idx in rect.indices) {
            val ch = rect[idx]
            if ((ch == '+' || ch == '-') && idx > 0 && rect[idx - 1] != 'e') {
                if (current.isNotEmpty()) {
                    terms.add(current.toString())
                    current = StringBuilder()
                }
            }
            current.append(ch)
        }
        if (current.isNotEmpty()) terms.add(current.toString())

        var re = 0.0
        var im = 0.0
        for (term in terms) {
            if (term.contains('j')) {
                val withoutJ = term.replace("j", "")
                val coeff = when (withoutJ) {
                    "", "+" -> 1.0
                    "-" -> -1.0
                    else -> parseRealExpression(withoutJ) ?: return null
                }
                im += coeff
            } else {
                val valRe = parseRealExpression(term) ?: return null
                re += valRe
            }
        }
        return ComplexNum(re, im)
    }

    fun parseRealExpression(raw: String): Double? {
        val s = raw.trim()
            .replace(" ", "")
            .replace("−", "-")
            .replace("π", Math.PI.toString())
            .replace("pi", Math.PI.toString(), ignoreCase = true)
            .replace("ω", "")
        if (s.isEmpty()) return null

        // Handle simple division like 1/sqrt(2) or 3/4
        if (s.contains('/')) {
            val parts = s.split('/')
            if (parts.size != 2) return null
            val num = parseSimpleAtom(parts[0]) ?: return null
            val den = parseSimpleAtom(parts[1]) ?: return null
            if (abs(den) < 1e-12) return null
            return num / den
        }

        return parseSimpleAtom(s)
    }

    private fun parseSimpleAtom(raw: String): Double? {
        if (raw.isEmpty()) return null
        val sign = if (raw.startsWith("-")) -1.0 else 1.0
        val body = raw.removePrefix("-").removePrefix("+")
        if (body.startsWith("sqrt(") && body.endsWith(")")) {
            val inner = body.substring(5, body.length - 1).toDoubleOrNull() ?: return null
            if (inner < 0.0) return null
            return sign * sqrt(inner)
        }
        if (body.startsWith("√")) {
            val inner = body.removePrefix("√").removePrefix("(").removeSuffix(")").toDoubleOrNull() ?: return null
            if (inner < 0.0) return null
            return sign * sqrt(inner)
        }
        return raw.toDoubleOrNull()
    }
}
