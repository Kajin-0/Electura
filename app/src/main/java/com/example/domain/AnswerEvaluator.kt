package com.example.domain

import com.example.model.AnswerKind
import com.example.model.CircuitProblem
import com.example.model.ComplexNum
import com.example.model.ProblemCategory
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.cosh
import kotlin.math.exp
import kotlin.math.hypot
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sinh
import kotlin.math.sqrt
import kotlin.math.tan

sealed class EvaluationResult {
    data class Correct(val parsedDisplay: String, val xpEarned: Int, val note: String) : EvaluationResult()
    data class Incorrect(val parsedDisplay: String, val diagnosticTip: String) : EvaluationResult()
    data class InvalidFormat(val reason: String) : EvaluationResult()
}

object AnswerEvaluator {

    private enum class UnitFamily {
        CAPACITANCE,
        INDUCTANCE,
        RESISTANCE,
        CONDUCTANCE,
        VOLTAGE,
        CURRENT,
        TIME,
        FREQUENCY,
        ANGULAR_FREQ,
        REAL_POWER,
        APPARENT_POWER,
        CURRENT_SLEW,
        ANGLE,
        DECIBEL,
        GAIN_RATIO
    }

    private data class UnitSpec(
        val canonical: String,
        val family: UnitFamily,
        val scaleToBase: Double,
        val aliases: List<String>,
        val caseSensitive: Boolean = false
    )

    private val unitSpecs: List<UnitSpec> = listOf(
        // Angle
        UnitSpec("°", UnitFamily.ANGLE, 1.0, listOf("degrees", "degree", "deg", "°")),
        UnitSpec("rad", UnitFamily.ANGLE, 180.0 / Math.PI, listOf("radians", "radian", "rad")),

        // Angular frequency & frequency
        UnitSpec("Mrad/s", UnitFamily.ANGULAR_FREQ, 1e6, listOf("Mrad/s", "mrad/s")),
        UnitSpec("krad/s", UnitFamily.ANGULAR_FREQ, 1e3, listOf("krad/s")),
        UnitSpec("rad/s", UnitFamily.ANGULAR_FREQ, 1.0, listOf("rad/s")),
        UnitSpec("MHz", UnitFamily.FREQUENCY, 1e6, listOf("MHz")),
        UnitSpec("kHz", UnitFamily.FREQUENCY, 1e3, listOf("kHz")),
        UnitSpec("Hz", UnitFamily.FREQUENCY, 1.0, listOf("Hz", "hz")),

        // Capacitance
        UnitSpec("pF", UnitFamily.CAPACITANCE, 1e-12, listOf("pF", "pf")),
        UnitSpec("nF", UnitFamily.CAPACITANCE, 1e-9, listOf("nF", "nf")),
        UnitSpec("μF", UnitFamily.CAPACITANCE, 1e-6, listOf("μF", "uF", "μf", "uf")),
        UnitSpec("mF", UnitFamily.CAPACITANCE, 1e-3, listOf("mF"), caseSensitive = true),
        UnitSpec("F", UnitFamily.CAPACITANCE, 1.0, listOf("F"), caseSensitive = true),

        // Inductance
        UnitSpec("nH", UnitFamily.INDUCTANCE, 1e-9, listOf("nH")),
        UnitSpec("μH", UnitFamily.INDUCTANCE, 1e-6, listOf("μH", "uH")),
        UnitSpec("mH", UnitFamily.INDUCTANCE, 1e-3, listOf("mH")),
        UnitSpec("H", UnitFamily.INDUCTANCE, 1.0, listOf("H"), caseSensitive = true),

        // Resistance / Impedance
        UnitSpec("mΩ", UnitFamily.RESISTANCE, 1e-3, listOf("mohms", "mohm", "mΩ"), caseSensitive = true),
        UnitSpec("MΩ", UnitFamily.RESISTANCE, 1e6, listOf("Mohms", "Mohm", "MΩ"), caseSensitive = true),
        UnitSpec("kΩ", UnitFamily.RESISTANCE, 1e3, listOf("kohms", "kohm", "kΩ", "KΩ")),
        UnitSpec("Ω", UnitFamily.RESISTANCE, 1.0, listOf("ohms", "ohm", "Ω")),

        // Conductance
        UnitSpec("μS", UnitFamily.CONDUCTANCE, 1e-6, listOf("μS", "uS"), caseSensitive = true),
        UnitSpec("mS", UnitFamily.CONDUCTANCE, 1e-3, listOf("mS"), caseSensitive = true),
        UnitSpec("S", UnitFamily.CONDUCTANCE, 1.0, listOf("siemens", "S"), caseSensitive = true),

        // Time
        UnitSpec("μs", UnitFamily.TIME, 1e-6, listOf("μs", "us")),
        UnitSpec("ms", UnitFamily.TIME, 1e-3, listOf("ms"), caseSensitive = true),
        UnitSpec("s", UnitFamily.TIME, 1.0, listOf("seconds", "second", "secs", "sec", "s"), caseSensitive = true),

        // Slew
        UnitSpec("kA/s", UnitFamily.CURRENT_SLEW, 1e3, listOf("kA/s")),
        UnitSpec("mA/s", UnitFamily.CURRENT_SLEW, 1e-3, listOf("mA/s")),
        UnitSpec("A/s", UnitFamily.CURRENT_SLEW, 1.0, listOf("A/s", "a/s")),

        // Apparent & real power
        UnitSpec("kVA", UnitFamily.APPARENT_POWER, 1e3, listOf("kVA", "kva")),
        UnitSpec("VA", UnitFamily.APPARENT_POWER, 1.0, listOf("VA", "va")),
        UnitSpec("MW", UnitFamily.REAL_POWER, 1e6, listOf("MW"), caseSensitive = true),
        UnitSpec("kW", UnitFamily.REAL_POWER, 1e3, listOf("kW", "kw")),
        UnitSpec("mW", UnitFamily.REAL_POWER, 1e-3, listOf("mW"), caseSensitive = true),
        UnitSpec("W", UnitFamily.REAL_POWER, 1.0, listOf("watts", "watt", "W")),

        // Gain & dB
        UnitSpec("V/V", UnitFamily.GAIN_RATIO, 1.0, listOf("V/V", "v/v")),
        UnitSpec("dB", UnitFamily.DECIBEL, 1.0, listOf("dB", "db")),

        // Voltage & Current
        UnitSpec("kV", UnitFamily.VOLTAGE, 1e3, listOf("kV", "kv")),
        UnitSpec("mV", UnitFamily.VOLTAGE, 1e-3, listOf("mV"), caseSensitive = true),
        UnitSpec("V", UnitFamily.VOLTAGE, 1.0, listOf("volts", "volt", "V")),
        UnitSpec("kA", UnitFamily.CURRENT, 1e3, listOf("kA"), caseSensitive = true),
        UnitSpec("mA", UnitFamily.CURRENT, 1e-3, listOf("mA"), caseSensitive = true),
        UnitSpec("μA", UnitFamily.CURRENT, 1e-6, listOf("μA", "uA"), caseSensitive = true),
        UnitSpec("A", UnitFamily.CURRENT, 1.0, listOf("amps", "amp", "A"))
    )

    private sealed class UnitMatchResult {
        data class Clean(val cleanedInput: String) : UnitMatchResult()
        data class Mismatch(val warningReason: String) : UnitMatchResult()
    }

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

        val unitCleaned = when (val inspected = inspectTrailingUnit(problem, trimmed)) {
            is UnitMatchResult.Clean -> inspected.cleanedInput
            is UnitMatchResult.Mismatch -> return EvaluationResult.InvalidFormat(inspected.warningReason)
        }

        if (unitCleaned.isEmpty()) {
            return EvaluationResult.InvalidFormat("Enter a numeric value before submitting.")
        }

        return when (problem.answerKind) {
            AnswerKind.REAL_SCALAR -> evaluateReal(problem, unitCleaned, hintsUsed, currentStreak)
            AnswerKind.COMPLEX_RECT_OR_POLAR -> evaluateComplex(problem, unitCleaned, hintsUsed, currentStreak)
        }
    }

    private fun findSpecForExpectedUnit(unit: String): UnitSpec? {
        if (unit.isBlank()) return null
        return unitSpecs.firstOrNull { spec ->
            spec.aliases.any { it == unit }
        } ?: unitSpecs.firstOrNull { spec ->
            !spec.caseSensitive && spec.aliases.any { it.equals(unit, ignoreCase = true) }
        }
    }

    private fun inspectTrailingUnit(problem: CircuitProblem, trimmed: String): UnitMatchResult {
        val expectedUnit = problem.unit.trim()
        if (expectedUnit.isBlank()) return UnitMatchResult.Clean(trimmed)

        val expectedSpec = findSpecForExpectedUnit(expectedUnit)

        // 1. Check exact / alias match for the expected unit first (preserving existing lenient aliases)
        val exactAliases = when (expectedUnit) {
            "Ω" -> listOf("ohms", "ohm", "Ω")
            "°", "deg" -> listOf("degrees", "degree", "deg", "°")
            "μF" -> listOf("μF", "uF")
            "μH" -> listOf("μH", "uH")
            else -> listOf(expectedUnit)
        }.sortedByDescending { it.length }

        val exactIgnoreCase = !(expectedSpec?.caseSensitive ?: false)
        for (alias in exactAliases) {
            val stripped = stripSuffixAtBoundary(trimmed, alias, ignoreCase = exactIgnoreCase)
            if (stripped != null) {
                return UnitMatchResult.Clean(stripped)
            }
        }

        // 2. Check other unit specs for non-punitive conversion warnings or unit mismatches
        val candidateSpecs = unitSpecs
            .flatMap { spec -> spec.aliases.map { alias -> Triple(spec, alias, spec.caseSensitive) } }
            .sortedByDescending { it.second.length }

        for ((foundSpec, alias, caseSensitive) in candidateSpecs) {
            // Never intercept angle suffixes ("rad", "deg", "°") as trailing problem units unless expected unit is itself an angle!
            if (foundSpec.family == UnitFamily.ANGLE && expectedSpec?.family != UnitFamily.ANGLE) {
                continue
            }
            val ignoreCase = !caseSensitive
            val prefix = stripSuffixAtBoundary(trimmed, alias, ignoreCase = ignoreCase) ?: continue
            if (prefix.isEmpty()) continue

            if (expectedSpec != null && foundSpec.family == expectedSpec.family) {
                val factor = foundSpec.scaleToBase / expectedSpec.scaleToBase
                val conversionNote = buildConversionRatioText(foundSpec, expectedSpec, factor)
                val convertedMatches = doesConvertedValueMatch(problem, prefix, factor)
                val msg = if (convertedMatches != null) {
                    "Entered value is in ${foundSpec.canonical}, while this problem expects $expectedUnit ($conversionNote). Your converted answer ($convertedMatches $expectedUnit) matches — enter the value in $expectedUnit."
                } else {
                    "Unit mismatch: you entered ${foundSpec.canonical}, but this problem asks for $expectedUnit ($conversionNote). Convert to $expectedUnit to submit (streak preserved)."
                }
                return UnitMatchResult.Mismatch(msg)
            }

            if (expectedSpec != null) {
                return UnitMatchResult.Mismatch(
                    "Unit mismatch: entered '${foundSpec.canonical}', but this problem expects '$expectedUnit' (streak preserved)."
                )
            }
        }

        return UnitMatchResult.Clean(trimmed)
    }

    private fun stripSuffixAtBoundary(text: String, suffix: String, ignoreCase: Boolean): String? {
        if (!text.endsWith(suffix, ignoreCase = ignoreCase) || text.length <= suffix.length) return null
        val prefix = text.dropLast(suffix.length)
        val lastChar = prefix.last()
        return if (lastChar.isDigit() || lastChar == ')' || lastChar == '°' || lastChar.isWhitespace()) {
            prefix.trimEnd()
        } else {
            null
        }
    }

    private fun buildConversionRatioText(found: UnitSpec, expected: UnitSpec, factor: Double): String {
        return when {
            found.family == UnitFamily.ANGLE && found.canonical == "rad" -> "1 rad = 180/π °"
            found.family == UnitFamily.ANGLE && expected.canonical == "rad" -> "180° = π rad"
            factor >= 1.0 -> "1 ${found.canonical} = ${ComplexNum.formatClean(factor)} ${expected.canonical}"
            else -> "1 ${expected.canonical} = ${ComplexNum.formatClean(1.0 / factor)} ${found.canonical}"
        }
    }

    private fun doesConvertedValueMatch(problem: CircuitProblem, prefixExpr: String, factor: Double): String? {
        return when (problem.answerKind) {
            AnswerKind.REAL_SCALAR -> {
                val expected = problem.expectedReal ?: return null
                val parsed = parseRealExpression(prefixExpr) ?: return null
                val converted = parsed * factor
                val tol = max(problem.tolerance * abs(expected), 0.002)
                if (abs(converted - expected) <= tol) ComplexNum.formatClean(converted) else null
            }
            AnswerKind.COMPLEX_RECT_OR_POLAR -> {
                val expected = problem.expectedComplex ?: return null
                val parsed = parseComplex(prefixExpr) ?: return null
                val converted = ComplexNum(parsed.re * factor, parsed.im * factor)
                val tol = max(problem.tolerance * expected.magnitude, 0.003)
                if (hypot(converted.re - expected.re, converted.im - expected.im) <= tol) {
                    if (problem.category == ProblemCategory.GEOMETRIC_ALGEBRA) converted.formatBivector() else converted.formatRect()
                } else null
            }
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
            ?: return EvaluationResult.InvalidFormat("Could not parse number. Try a decimal (0.707), fraction (3/4), or math expr (2*cos(60°), e^(-pi/2)).")

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
            val tip = diagnoseRealMistake(problem, parsed, expected, tol)
            EvaluationResult.Incorrect(parsedDisplay = parsedStr, diagnosticTip = tip)
        }
    }

    private fun diagnoseRealMistake(
        problem: CircuitProblem,
        parsed: Double,
        expected: Double,
        tol: Double
    ): String {
        val nonzero = abs(expected) > 1e-9
        return when {
            nonzero && abs(parsed + expected) <= tol ->
                "Magnitude matches, check your sign (+/-)."
            nonzero && abs(parsed - Math.toRadians(expected)) <= max(tol * Math.PI / 180.0, 0.002) &&
                (problem.unit == "°" || problem.unit == "deg" || abs(expected) >= 10.0) ->
                "Value matches radians instead of degrees — multiply by 180/π to convert to degrees."
            nonzero && abs(parsed - Math.toDegrees(expected)) <= max(tol * 180.0 / Math.PI, 0.05) &&
                abs(expected) < 10.0 ->
                "Value matches degrees instead of radians — check degree vs radian conversion."
            nonzero && (abs(parsed * 1000.0 - expected) <= tol || abs(parsed / 1000.0 - expected) <= tol) ->
                "Off by 10³ — check your unit prefix (milli / kilo scaling)."
            nonzero && (abs(parsed * 1e6 - expected) <= tol || abs(parsed / 1e6 - expected) <= tol) ->
                "Off by 10⁶ — check micro (10⁻⁶) vs mega (10⁶) prefix scaling."
            nonzero && (abs(parsed - 2.0 * expected) <= 2.0 * tol || abs(parsed * 2.0 - expected) <= tol) ->
                "Off by a factor of 2 — check a missing or extra factor of 2 (e.g. 2M, R/2L, or 2√(L/C))."
            nonzero && (abs(parsed - expected * sqrt(2.0)) <= 1.5 * tol || abs(parsed * sqrt(2.0) - expected) <= 1.5 * tol) ->
                "Off by a factor of √2 — check RMS vs peak conversion (V_rms = V_pk / √2)."
            nonzero && (abs(parsed - expected * sqrt(3.0)) <= 2.0 * tol || abs(parsed * sqrt(3.0) - expected) <= 2.0 * tol) ->
                "Off by a factor of √3 — check 3-phase line vs phase scaling or 30°/60° geometry."
            nonzero && abs(parsed) > 1e-9 && abs((1.0 / parsed) - expected) <= tol ->
                "Value is the reciprocal (1/x) of the expected answer — check numerator vs denominator."
            abs(parsed - expected) < max(tol * 4, 0.05) ->
                "Very close! Check intermediate rounding or retain another significant digit."
            else -> when (problem.category) {
                ProblemCategory.GEOMETRIC_ALGEBRA ->
                    "Value is off from expected scalar or norm. Check Clifford dot (·) vs wedge (∧) product signs."
                ProblemCategory.COMPLEX_ANALYSIS ->
                    "Value is off from expected result. Check enclosed poles and residue signs."
                ProblemCategory.POWER ->
                    "Power calculation mismatch. Check pf = cos(θ), RMS factors, and reactive Q compensation."
                ProblemCategory.RESONANCE ->
                    "Resonant parameter mismatch. Check Q = (1/R)√(L/C) (series) vs R√(C/L) (parallel) and ω₀ = 1/√(LC)."
                ProblemCategory.ADMITTANCE ->
                    "Admittance/LCR mismatch. Remember G = Re(1/Z), B = Im(1/Z), and D = R_s/|X_s|."
                ProblemCategory.LAPLACE ->
                    "Transform/transient mismatch. Double-check initial conditions, time constants τ, and pole locations."
                ProblemCategory.IMPEDANCE ->
                    "Impedance calculation mismatch. Try revealing a hint or checking ωL vs 1/(ωC)."
            }
        }
    }

    private fun evaluateComplex(
        problem: CircuitProblem,
        raw: String,
        hintsUsed: Int,
        currentStreak: Int
    ): EvaluationResult {
        val expected = problem.expectedComplex ?: return EvaluationResult.InvalidFormat("Missing expected complex answer.")
        val isGa = problem.category == ProblemCategory.GEOMETRIC_ALGEBRA
        val parsed = parseComplex(raw)
            ?: return EvaluationResult.InvalidFormat(
                if (isGa) "Try multivector (8 + 6e12), rectangular (8 + j6), or polar (10∠36.87°)."
                else "Try rectangular (8 - j6), polar (10∠pi/4 rad), or trig (4*cos(60°) + j4*sin(60°))."
            )

        val tol = max(problem.tolerance * expected.magnitude, 0.003)
        val distance = hypot(parsed.re - expected.re, parsed.im - expected.im)
        val primaryRect = if (isGa) parsed.formatBivector() else parsed.formatRect()
        val parsedStr = if (abs(parsed.im) > 1e-6) "$primaryRect (${parsed.formatPolar()})" else primaryRect

        return if (distance <= tol) {
            val xp = computeXp(problem.difficulty.xpReward, hintsUsed, currentStreak)
            val verifiedNote = when (problem.category) {
                ProblemCategory.GEOMETRIC_ALGEBRA -> "Cl₂,₀ multivector verified (${parsed.formatBivector()})."
                ProblemCategory.COMPLEX_ANALYSIS -> "Complex value verified (${parsed.formatPolar()})."
                else -> "Complex phasor verified (${parsed.formatPolar()})."
            }
            EvaluationResult.Correct(
                parsedDisplay = parsedStr,
                xpEarned = xp,
                note = verifiedNote
            )
        } else {
            val tip = diagnoseComplexMistake(problem, parsed, expected, tol, isGa)
            EvaluationResult.Incorrect(parsedDisplay = parsedStr, diagnosticTip = tip)
        }
    }

    private fun diagnoseComplexMistake(
        problem: CircuitProblem,
        parsed: ComplexNum,
        expected: ComplexNum,
        tol: Double,
        isGa: Boolean
    ): String {
        val magTol = max(problem.tolerance * expected.magnitude, 0.004)
        val parsedMagSq = parsed.re * parsed.re + parsed.im * parsed.im
        val recip = if (parsedMagSq > 1e-12) ComplexNum(parsed.re / parsedMagSq, -parsed.im / parsedMagSq) else null
        val radInterpretedAsDeg = ComplexNum.fromPolarDeg(expected.magnitude, expected.angleRad)

        return when {
            abs(parsed.magnitude - expected.magnitude) <= magTol && abs(parsed.im + expected.im) <= tol ->
                if (isGa) "Norm matches! Check the orientation sign of your e12 bivector component."
                else "Magnitude matches! Check the sign of your imaginary / phase component."
            expected.magnitude > 1e-6 && abs(parsed.re + expected.re) <= tol && abs(parsed.im + expected.im) <= tol ->
                "Magnitude matches, check your overall sign (+/- or 180° phase flip)."
            abs(parsed.re - expected.im) <= tol && abs(parsed.im - expected.re) <= tol ->
                if (isGa) "Scalar grade-0 and bivector e12 components appear swapped."
                else "Real and imaginary components appear swapped."
            recip != null && hypot(recip.re - expected.re, recip.im - expected.im) <= tol ->
                "Entered value is the complex reciprocal (1/z) of the target — verify Z vs Y = 1/Z."
            expected.magnitude > 1e-6 && (
                hypot(parsed.re * 1000.0 - expected.re, parsed.im * 1000.0 - expected.im) <= tol ||
                    hypot(parsed.re / 1000.0 - expected.re, parsed.im / 1000.0 - expected.im) <= tol
                ) ->
                "Vector components are scaled by 10³ — check milli / kilo unit scaling."
            expected.magnitude > 1e-6 && (
                hypot(parsed.re * 2.0 - expected.re, parsed.im * 2.0 - expected.im) <= 2.0 * tol ||
                    hypot(parsed.re / 2.0 - expected.re, parsed.im / 2.0 - expected.im) <= tol
                ) ->
                "Off by a factor of 2 — check a missing or extra factor of 2."
            expected.magnitude > 1e-6 && (
                hypot(parsed.re * sqrt(2.0) - expected.re, parsed.im * sqrt(2.0) - expected.im) <= 1.5 * tol ||
                    hypot(parsed.re / sqrt(2.0) - expected.re, parsed.im / sqrt(2.0) - expected.im) <= 1.5 * tol
                ) ->
                "Off by a factor of √2 — check RMS vs peak phasor scaling."
            expected.magnitude > 1e-6 && (
                hypot(parsed.re * sqrt(3.0) - expected.re, parsed.im * sqrt(3.0) - expected.im) <= 2.0 * tol ||
                    hypot(parsed.re / sqrt(3.0) - expected.re, parsed.im / sqrt(3.0) - expected.im) <= 2.0 * tol
                ) ->
                "Off by a factor of √3 — check 3-phase line vs phase scaling."
            abs(parsed.magnitude - expected.magnitude) <= magTol -> {
                if (abs(expected.angleDeg) > 1.0 &&
                    hypot(parsed.re - radInterpretedAsDeg.re, parsed.im - radInterpretedAsDeg.im) <= tol
                ) {
                    "Magnitude matches! Did you enter radians without writing 'rad'? Add 'rad' after the angle or convert to degrees."
                } else if (isGa) {
                    "Norm (${ComplexNum.formatClean(parsed.magnitude)}) is right — check your e12 bivector angle."
                } else {
                    "Magnitude (${ComplexNum.formatClean(parsed.magnitude)}) is right — check your phase angle."
                }
            }
            else -> when (problem.category) {
                ProblemCategory.GEOMETRIC_ALGEBRA ->
                    "Multivector mismatch. Check Clifford product signs (e₁₂² = −1, e₂∧e₁ = −e₁₂)."
                ProblemCategory.COMPLEX_ANALYSIS ->
                    "Complex mismatch. Check pole residue arithmetic or j² = −1 simplification."
                ProblemCategory.POWER ->
                    "Complex power mismatch. Remember S = V·I* conjugates current I before multiplying."
                else ->
                    "Phasor mismatch. Verify reactance signs (+jωL vs -j/ωC)."
            }
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

        val tokens = tokenize(normalized, allowImaginary = true) ?: return null
        val parser = ExprParser(tokens, allowImaginary = true)
        val result = parser.parseFullExpression() ?: return null
        if (!result.re.isFinite() || !result.im.isFinite()) return null
        return result
    }

    fun parseRealExpression(raw: String, degreeFactor: Double = Math.PI / 180.0): Double? {
        val normalized = normalizeInput(raw)
        if (normalized.isEmpty()) return null

        val tokens = tokenize(normalized, allowImaginary = true) ?: return null
        val parser = ExprParser(tokens, allowImaginary = true, degreeFactor = degreeFactor)
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

    private sealed class Tok {
        data class Num(val v: Double) : Tok()
        data class Const(val name: String) : Tok() // "pi", "e", "j"
        data class Func(val name: String) : Tok()
        data class Op(val op: Char) : Tok()        // '+', '-', '*', '/', '^'
        data object LParen : Tok()
        data object RParen : Tok()
        data object Comma : Tok()
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
                ',' -> {
                    out.add(Tok.Comma)
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
                // Alphanumeric tokens: GA bivector e12 / e_12, atan2, log10
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
                if (input.startsWith("atan2", i, ignoreCase = true) && (i + 5 == n || !input[i + 5].isLetterOrDigit())) {
                    out.add(Tok.Func("atan2"))
                    i += 5
                    continue
                }
                if (input.startsWith("log10", i, ignoreCase = true) && (i + 5 == n || !input[i + 5].isLetterOrDigit())) {
                    out.add(Tok.Func("log"))
                    i += 5
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
                    "sqrt", "exp", "ln", "log",
                    "sin", "cos", "tan", "atan", "arctan",
                    "abs", "arg", "conj", "re", "real", "im", "imag" -> out.add(Tok.Func(word))
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

        private fun peek(offset: Int = 0): Tok? = tokens.getOrNull(pos + offset)
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

        private fun isBareNumPair(prev: Tok?, next: Tok?): Boolean =
            prev is Tok.Num && next is Tok.Num

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
                        val rhs = parseDivisorFactor() ?: return null
                        acc = divComplex(acc, rhs) ?: return null
                    }
                    canStartTerm(t) && canEndTerm(tokens.getOrNull(pos - 1)) -> {
                        if (isBareNumPair(tokens[pos - 1], t)) break
                        val rhs = parseFactor() ?: return null
                        acc = mulComplex(acc, rhs)
                    }
                    else -> break
                }
            }
            return acc
        }

        /**
         * Parses a divisor after '/' allowing tightly juxtaposed implicit factors like `1/j10` or `1/2pi`
         * to bind together as the denominator before outer '*' or '/' operators apply.
         */
        private fun parseDivisorFactor(): ComplexNum? {
            val t = peek()
            if (t is Tok.Op && (t.op == '+' || t.op == '-')) {
                consume()
                val inner = parseDivisorFactor() ?: return null
                return if (t.op == '-') ComplexNum(-inner.re, -inner.im) else inner
            }
            var acc = parsePolar() ?: return null
            while (true) {
                val next = peek()
                if (canStartTerm(next) && canEndTerm(tokens.getOrNull(pos - 1)) && !isBareNumPair(tokens[pos - 1], next)) {
                    val rhs = parsePolar() ?: return null
                    acc = mulComplex(acc, rhs)
                } else {
                    break
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
            var lhs = parsePower(polarDegMode = false) ?: return null
            if (peek() == Tok.Polar) {
                if (!allowImaginary) return null
                if (abs(lhs.im) > 1e-9) return null
                consume()
                val angleDeg = parsePolarAngleDeg() ?: return null
                lhs = ComplexNum.fromPolarDeg(lhs.re, angleDeg)
            }
            return lhs
        }

        /**
         * Parses inline polar angle expressions such as:
         * - `30°` / `-36.87°` / `90 deg`
         * - `pi/4 rad` / `-pi/2 rad` (including within outer sums like `10∠pi/4 rad + 5`)
         * - `(30° + 15°)` / `(pi/4 rad)` / `(pi/4) rad`
         * - `atan2(8, 6)*180/pi`
         * while terminating right after explicit angle units (`°`/`deg`/`rad`) or before multiplying another polar phasor.
         */
        private fun parsePolarAngleDeg(): Double? {
            if (peek() == Tok.LParen) {
                consume()
                val inner = parsePolarAngleExpr() ?: return null
                if (peek() != Tok.RParen) return null
                consume()
                val angleDeg = when (peek()) {
                    Tok.DegSuffix -> {
                        consume()
                        inner
                    }
                    Tok.RadSuffix -> {
                        consume()
                        inner * (180.0 / Math.PI)
                    }
                    else -> inner
                }
                return if (angleDeg.isFinite()) angleDeg else null
            }

            val first = parseAngleAtom() ?: return null
            var acc = first.first
            var unitClosed = first.second

            while (!unitClosed) {
                val next = peek()
                when {
                    next is Tok.Op && (next.op == '*' || next.op == '/') -> {
                        if (nextFactorContainsPolarOrImaginary(startOffset = 1)) break
                        consume()
                        val rhs = parseAngleAtom() ?: return null
                        acc = if (next.op == '*') {
                            mulComplex(acc, rhs.first)
                        } else {
                            divComplex(acc, rhs.first) ?: return null
                        }
                        unitClosed = rhs.second
                    }
                    canStartTerm(next) && canEndTerm(tokens.getOrNull(pos - 1)) &&
                        !isBareNumPair(tokens[pos - 1], next) -> {
                        val rhs = parseAngleAtom() ?: return null
                        acc = mulComplex(acc, rhs.first)
                        unitClosed = rhs.second
                    }
                    else -> break
                }
            }

            if (abs(acc.im) > 1e-9 || !acc.re.isFinite()) return null
            var resultDeg = acc.re
            if (!unitClosed) {
                when (peek()) {
                    Tok.DegSuffix -> consume()
                    Tok.RadSuffix -> {
                        consume()
                        resultDeg *= (180.0 / Math.PI)
                    }
                    else -> Unit
                }
            }
            return if (resultDeg.isFinite()) resultDeg else null
        }

        private fun nextFactorContainsPolarOrImaginary(startOffset: Int): Boolean {
            var depth = 0
            var off = startOffset
            while (true) {
                val t = peek(off) ?: return false
                when (t) {
                    Tok.LParen -> depth++
                    Tok.RParen -> if (depth == 0) return false else depth--
                    Tok.Comma -> if (depth == 0) return false
                    Tok.Polar -> return true
                    is Tok.Const -> if (t.name == "j") return true
                    is Tok.Op -> if (depth == 0 && (t.op == '+' || t.op == '-' || t.op == '*' || t.op == '/')) {
                        if (off > startOffset) return false
                    }
                    else -> Unit
                }
                off++
            }
        }

        private fun parsePolarAngleExpr(): Double? {
            var acc = parsePolarAngleMulTerm() ?: return null
            while (true) {
                val t = peek()
                if (t is Tok.Op && (t.op == '+' || t.op == '-')) {
                    consume()
                    val rhs = parsePolarAngleMulTerm() ?: return null
                    acc = if (t.op == '+') acc + rhs else acc - rhs
                } else {
                    break
                }
            }
            val finalDeg = when (peek()) {
                Tok.DegSuffix -> {
                    consume()
                    acc
                }
                Tok.RadSuffix -> {
                    consume()
                    acc * (180.0 / Math.PI)
                }
                else -> acc
            }
            return if (finalDeg.isFinite()) finalDeg else null
        }

        private fun parsePolarAngleMulTerm(): Double? {
            var acc = parseAngleAtom()?.first ?: return null
            while (true) {
                val t = peek()
                when {
                    t is Tok.Op && t.op == '*' -> {
                        consume()
                        val rhs = parseAngleAtom()?.first ?: return null
                        acc = mulComplex(acc, rhs)
                    }
                    t is Tok.Op && t.op == '/' -> {
                        consume()
                        val rhs = parseAngleAtom()?.first ?: return null
                        acc = divComplex(acc, rhs) ?: return null
                    }
                    canStartTerm(t) && canEndTerm(tokens.getOrNull(pos - 1)) && !isBareNumPair(tokens[pos - 1], t) -> {
                        val rhs = parseAngleAtom()?.first ?: return null
                        acc = mulComplex(acc, rhs)
                    }
                    else -> break
                }
            }
            if (abs(acc.im) > 1e-9 || !acc.re.isFinite()) return null
            var deg = acc.re
            when (peek()) {
                Tok.DegSuffix -> consume()
                Tok.RadSuffix -> {
                    consume()
                    deg *= (180.0 / Math.PI)
                }
                else -> Unit
            }
            return deg
        }

        private fun parseAngleAtom(): Pair<ComplexNum, Boolean>? {
            val t = peek()
            if (t is Tok.Op && (t.op == '+' || t.op == '-')) {
                consume()
                val inner = parseAngleAtom() ?: return null
                val signed = if (t.op == '-') ComplexNum(-inner.first.re, -inner.first.im) else inner.first
                return signed to inner.second
            }
            val value = parsePower(polarDegMode = true) ?: return null
            return if (peek() == Tok.DegSuffix) {
                consume()
                value to true
            } else {
                value to false
            }
        }

        private fun parsePower(polarDegMode: Boolean = false): ComplexNum? {
            var base = parsePostfix(polarDegMode) ?: return null
            if (peek() == Tok.Op('^')) {
                consume()
                val expVal = parseFactor() ?: return null
                base = powComplex(base, expVal) ?: return null
                if (!polarDegMode) {
                    while (peek() == Tok.DegSuffix) {
                        consume()
                        if (degreeFactor != 1.0) {
                            base = ComplexNum(base.re * degreeFactor, base.im * degreeFactor)
                        }
                    }
                }
            }
            return base
        }

        private fun parsePostfix(polarDegMode: Boolean): ComplexNum? {
            var v = parsePrimary(polarDegMode) ?: return null
            if (!polarDegMode) {
                while (peek() == Tok.DegSuffix) {
                    consume()
                    if (degreeFactor != 1.0) {
                        v = ComplexNum(v.re * degreeFactor, v.im * degreeFactor)
                    }
                }
            }
            return v
        }

        private fun parsePrimary(polarDegMode: Boolean): ComplexNum? {
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
                    val inner = if (polarDegMode) {
                        val deg = parsePolarAngleExpr() ?: return null
                        ComplexNum(deg, 0.0)
                    } else {
                        parseExpr() ?: return null
                    }
                    if (peek() != Tok.RParen) return null
                    consume()
                    inner
                }
                is Tok.SqrtSym -> {
                    consume()
                    val inner = parsePrimary(polarDegMode = false) ?: return null
                    sqrtComplex(inner)
                }
                is Tok.Func -> {
                    consume()
                    if (peek() != Tok.LParen) return null
                    consume()
                    val arg1 = parseExpr() ?: return null
                    if (t.name == "atan2") {
                        if (peek() != Tok.Comma) return null
                        consume()
                        val arg2 = parseExpr() ?: return null
                        if (peek() != Tok.RParen) return null
                        consume()
                        if (abs(arg1.im) > 1e-9 || abs(arg2.im) > 1e-9) return null
                        val ans = atan2(arg1.re, arg2.re)
                        return if (ans.isFinite()) ComplexNum(ans, 0.0) else null
                    }
                    if (peek() != Tok.RParen) return null
                    consume()
                    evaluateUnaryFunction(t.name, arg1)
                }
                else -> null
            }
        }

        private fun evaluateUnaryFunction(name: String, arg: ComplexNum): ComplexNum? {
            val res = when (name) {
                "sqrt" -> sqrtComplex(arg)
                "exp" -> expComplex(arg)
                "ln" -> lnComplex(arg)
                "log" -> {
                    if (abs(arg.im) > 1e-9 || arg.re <= 0.0) return null
                    ComplexNum(log10(arg.re), 0.0)
                }
                "sin" -> sinComplex(arg)
                "cos" -> cosComplex(arg)
                "tan" -> tanComplex(arg)
                "atan", "arctan" -> {
                    if (abs(arg.im) > 1e-9) return null
                    ComplexNum(atan(arg.re), 0.0)
                }
                "abs" -> ComplexNum(arg.magnitude, 0.0)
                "arg" -> {
                    if (arg.magnitude < 1e-18) return null
                    ComplexNum(arg.angleRad, 0.0)
                }
                "conj" -> ComplexNum(arg.re, -arg.im)
                "re", "real" -> ComplexNum(arg.re, 0.0)
                "im", "imag" -> ComplexNum(arg.im, 0.0)
                else -> null
            } ?: return null
            return if (res.re.isFinite() && res.im.isFinite()) res else null
        }

        private fun sinComplex(z: ComplexNum): ComplexNum? {
            if (abs(z.im) < 1e-12) return ComplexNum(sin(z.re), 0.0)
            if (!allowImaginary) return null
            return ComplexNum(sin(z.re) * cosh(z.im), cos(z.re) * sinh(z.im))
        }

        private fun cosComplex(z: ComplexNum): ComplexNum? {
            if (abs(z.im) < 1e-12) return ComplexNum(cos(z.re), 0.0)
            if (!allowImaginary) return null
            return ComplexNum(cos(z.re) * cosh(z.im), -sin(z.re) * sinh(z.im))
        }

        private fun tanComplex(z: ComplexNum): ComplexNum? {
            if (abs(z.im) < 1e-12) {
                if (abs(cos(z.re)) < 1e-12) return null
                return ComplexNum(tan(z.re), 0.0)
            }
            if (!allowImaginary) return null
            val s = sinComplex(z) ?: return null
            val c = cosComplex(z) ?: return null
            return divComplex(s, c)
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
