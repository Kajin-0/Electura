package com.example.model

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

enum class ProblemCategory(val label: String, val tag: String) {
    IMPEDANCE("Impedance", "PHASOR IMPEDANCE"),
    ADMITTANCE("Admittance & LCR", "ADMITTANCE & LCR METER"),
    RESONANCE("Resonance", "RESONANCE & Q-FACTOR"),
    POWER("Power", "COMPLEX POWER"),
    LAPLACE("Laplace", "LCR NETWORK ANALYSIS"),
    COMPLEX_ANALYSIS("Complex Analysis", "COMPLEX ANALYSIS"),
    GEOMETRIC_ALGEBRA("Geometric Algebra", "GEOMETRIC ALGEBRA Cl₂,₀")
}

enum class Difficulty(val label: String, val xpReward: Int) {
    FOUNDATIONS("Foundations", 100),
    INTERMEDIATE("Intermediate", 175),
    MASTERY("Mastery", 250),
    ADVANCED("Advanced", 400)
}

enum class CircuitTopology {
    SERIES_RLC,
    SERIES_RL,
    SERIES_RC,
    PARALLEL_RC,
    PARALLEL_RLC,
    COMPLEX_LOAD,
    LOW_PASS_RC,
    FEEDBACK_LOOP,
    CONTOUR_SPLANE,
    GEOMETRIC_PLANE
}

enum class AnswerKind {
    COMPLEX_RECT_OR_POLAR,
    REAL_SCALAR
}

data class ComplexNum(val re: Double, val im: Double) {
    val magnitude: Double get() = hypot(re, im)
    val angleRad: Double get() = atan2(im, re)
    val angleDeg: Double get() = Math.toDegrees(angleRad)

    fun formatRect(): String {
        val r = formatClean(re)
        val iAbs = formatClean(kotlin.math.abs(im))
        return when {
            kotlin.math.abs(im) < 1e-6 -> r
            kotlin.math.abs(re) < 1e-6 -> if (im >= 0) "j$iAbs" else "-j$iAbs"
            im >= 0 -> "$r + j$iAbs"
            else -> "$r - j$iAbs"
        }
    }

    fun formatPolar(): String {
        return "${formatClean(magnitude)}∠${formatClean(angleDeg)}°"
    }

    companion object {
        fun fromPolarDeg(mag: Double, deg: Double): ComplexNum {
            val rad = Math.toRadians(deg)
            return ComplexNum(mag * cos(rad), mag * sin(rad))
        }

        fun formatClean(v: Double): String {
            val rounded = kotlin.math.round(v * 1000.0) / 1000.0
            val nearInt = kotlin.math.round(rounded)
            return if (kotlin.math.abs(rounded - nearInt) < 1e-4) {
                nearInt.toLong().toString()
            } else {
                String.format(java.util.Locale.US, "%.3f", rounded).trimEnd('0').trimEnd('.')
            }
        }
    }
}

data class CircuitProblem(
    val id: String,
    val code: String,
    val title: String,
    val category: ProblemCategory,
    val difficulty: Difficulty,
    val topology: CircuitTopology,
    val prompt: String,
    val parametersLine1: String,
    val parametersLine2: String,
    val leftMarker: String,
    val rightMarker: String,
    val methodTitle: String,
    val methodNote: String,
    val inputHint: String,
    val answerKind: AnswerKind,
    val expectedComplex: ComplexNum? = null,
    val expectedReal: Double? = null,
    val tolerance: Double = 0.03,
    val unit: String = "",
    val hints: List<String> = emptyList(),
    val workedSteps: List<String> = emptyList(),
    val isProcedural: Boolean = false
) {
    fun expectedDisplay(): String {
        val base = when (answerKind) {
            AnswerKind.COMPLEX_RECT_OR_POLAR -> expectedComplex?.let {
                if (kotlin.math.abs(it.im) > 1e-6) "${it.formatRect()} (${it.formatPolar()})"
                else it.formatRect()
            } ?: "0"
            AnswerKind.REAL_SCALAR -> ComplexNum.formatClean(expectedReal ?: 0.0)
        }
        return if (unit.isNotBlank()) "$base $unit" else base
    }
}

data class AttemptRecord(
    val problemId: String,
    val problemCode: String,
    val problemTitle: String,
    val submittedAnswer: String,
    val isCorrect: Boolean,
    val xpEarned: Int,
    val timestampMillis: Long
)

data class FieldGuideEntry(
    val id: String,
    val tag: String,
    val title: String,
    val formula: String,
    val explanation: String
)

enum class AppTab(val label: String) {
    INDEX("Index"),
    SOLVE("Solve"),
    STATS("Stats"),
    GUIDE("Guide")
}
