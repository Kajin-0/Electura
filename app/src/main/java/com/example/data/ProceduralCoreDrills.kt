package com.example.data

import com.example.model.AnswerKind
import com.example.model.CircuitProblem
import com.example.model.CircuitTopology
import com.example.model.ComplexNum
import com.example.model.Difficulty
import com.example.model.ProblemCategory
import kotlin.random.Random

private var drillSequence = 0

internal fun nextDrillSequence(): Int = ++drillSequence

internal fun pickTemplateIndex(
    templates: List<Pair<String, Difficulty>>,
    preferredDifficulty: Difficulty?,
    excludedTitles: Set<String>
): Int {
    val freshDiffMatches = if (preferredDifficulty != null) {
        templates.indices.filter { templates[it].second == preferredDifficulty && templates[it].first !in excludedTitles }
    } else emptyList()
    if (freshDiffMatches.isNotEmpty()) return freshDiffMatches.random()

    val allDiffMatches = if (preferredDifficulty != null) {
        templates.indices.filter { templates[it].second == preferredDifficulty }
    } else emptyList()
    if (allDiffMatches.isNotEmpty()) return allDiffMatches.random()

    val freshAny = templates.indices.filter { templates[it].first !in excludedTitles }
    if (freshAny.isNotEmpty()) return freshAny.random()

    return Random.nextInt(templates.size)
}

internal fun generateImpedanceDrill(
    drillId: String,
    drillNum: Int,
    preferredDifficulty: Difficulty?,
    excluded: Set<String>
): CircuitProblem = when (
    pickTemplateIndex(
        listOf(
            "Procedural RL Impedance" to Difficulty.INTERMEDIATE,
            "Procedural RC Impedance" to Difficulty.INTERMEDIATE,
            "Procedural Series RLC Impedance" to Difficulty.INTERMEDIATE,
            "Procedural Transformer Impedance" to Difficulty.MASTERY,
            "Procedural Coupled Inductors" to Difficulty.ADVANCED,
            "Procedural Sine–Cosine Phase" to Difficulty.FOUNDATIONS,
            "Procedural Complex Current Divider" to Difficulty.INTERMEDIATE,
            "Procedural AC Thévenin V_th" to Difficulty.MASTERY,
            "Procedural Multi-Freq RL RMS" to Difficulty.INTERMEDIATE
        ),
        preferredDifficulty,
        excluded
    )
) {
    0 -> {
        val pyth = listOf(
            Triple(3, 4, 5), Triple(6, 8, 10), Triple(5, 12, 13),
            Triple(8, 15, 17), Triple(7, 24, 25), Triple(9, 40, 41)
        ).random()
        val scale = listOf(1, 2, 5, 10).random()
        val omega = listOf(50, 100, 200, 250, 500, 1000).random()
        val r = (pyth.first * scale).toDouble()
        val x = (pyth.second * scale).toDouble()
        val hyp = pyth.third * scale
        val l = x / omega
        val lStr = ComplexNum.formatClean(l)
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural RL Impedance",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RL,
            prompt = "Find total series impedance Z = R + jωL (in Ohms) for:",
            parametersLine1 = "R = ${r.toInt()}Ω, L = ${lStr}H",
            parametersLine2 = "ω = $omega rad/s",
            methodTitle = "PHASOR DRILL",
            methodNote = "Compute X_L = ωL and express Z in rectangular or polar form.",
            inputHint = "Enter Z (e.g. ${r.toInt()} + j${x.toInt()})",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(r, x),
            unit = "Ω",
            hints = listOf(
                "X_L = ω × L = $omega × $lStr.",
                "Combine real R and imaginary +jX_L."
            ),
            workedSteps = listOf(
                "1. X_L = $omega × $lStr = ${x.toInt()} Ω.",
                "2. Z = ${r.toInt()} + j${x.toInt()} Ω (|Z| = $hyp Ω)."
            ),
            isProcedural = true
        )
    }
    1 -> {
        val pyth = listOf(
            Triple(3, 4, 5), Triple(6, 8, 10), Triple(5, 12, 13),
            Triple(8, 15, 17), Triple(7, 24, 25), Triple(9, 40, 41)
        ).random()
        val scale = listOf(1, 2, 5, 10).random()
        val omega = listOf(50, 100, 200, 250, 500, 1000).random()
        val r = (pyth.first * scale).toDouble()
        val x = (pyth.second * scale).toDouble()
        val hyp = pyth.third * scale
        val cMicro = 1_000_000.0 / (omega * x)
        val cStr = ComplexNum.formatClean(cMicro)
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural RC Impedance",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RC,
            prompt = "Find total series impedance Z = R − j/(ωC) (in Ohms) for:",
            parametersLine1 = "R = ${r.toInt()}Ω, C = ${cStr}μF",
            parametersLine2 = "ω = $omega rad/s",
            methodTitle = "PHASOR DRILL",
            methodNote = "Compute X_C = 1/(ωC) and remember capacitive reactance is −jX_C.",
            inputHint = "Enter Z (e.g. ${r.toInt()} - j${x.toInt()})",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(r, -x),
            unit = "Ω",
            hints = listOf(
                "X_C = 1 / ($omega × ${cStr}×10⁻⁶).",
                "Capacitive impedance carries a negative sign: R − jX_C."
            ),
            workedSteps = listOf(
                "1. X_C = 1 / (ωC) = ${x.toInt()} Ω.",
                "2. Z = ${r.toInt()} − j${x.toInt()} Ω (|Z| = $hyp Ω)."
            ),
            isProcedural = true
        )
    }
    2 -> {
        val r = listOf(10, 15, 20, 30, 40, 50).random()
        val omega = listOf(50, 100, 200, 500).random()
        val xl = listOf(20, 40, 50, 60, 80).random()
        val xc = listOf(10, 20, 25, 50, 100).filter { it != xl }.random()
        val lMh = (xl / omega.toDouble()) * 1000.0
        val cUf = 1_000_000.0 / (omega * xc)
        val lMhStr = ComplexNum.formatClean(lMh)
        val cUfStr = ComplexNum.formatClean(cUf)
        val xNet = (xl - xc).toDouble()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Series RLC Impedance",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Find net series RLC impedance Z = R + j(X_L − X_C) (in Ohms) at ω = $omega rad/s:",
            parametersLine1 = "R = ${r}Ω, L = ${lMhStr}mH, C = ${cUfStr}μF",
            parametersLine2 = "ω = $omega rad/s",
            methodTitle = "SERIES RLC DRILL",
            methodNote = "Subtract capacitive reactance X_C = 1/(ωC) from inductive reactance X_L = ωL.",
            inputHint = "Enter Z (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(r.toDouble(), xNet),
            unit = "Ω",
            hints = listOf(
                "X_L = $omega × ${lMhStr}mH = $xl Ω; X_C = 1 / ($omega × ${cUfStr}μF) = $xc Ω.",
                "Net reactance X = X_L − X_C = ${xNet.toInt()} Ω."
            ),
            workedSteps = listOf(
                "1. X_L = $xl Ω, X_C = $xc Ω ⇒ X_net = ${xNet.toInt()} Ω.",
                "2. Z = ${ComplexNum(r.toDouble(), xNet).formatRect()} Ω."
            ),
            isProcedural = true
        )
    }
    3 -> {
        val ratios = listOf(Pair(2, 1), Pair(3, 1), Pair(4, 1), Pair(1, 2), Pair(1, 4))
        val ratio = ratios.random()
        val aSq = (ratio.first.toDouble() / ratio.second.toDouble()).let { it * it }
        val rSec = if (ratio.first < ratio.second) listOf(16, 32, 48).random() else listOf(2, 3, 4, 5, 6).random()
        val xMag = if (ratio.first < ratio.second) listOf(16, 32, 48).random() else listOf(2, 3, 4, 5).random()
        val xSignMul = if (Random.nextBoolean()) 1 else -1
        val xSec = xMag * xSignMul
        val zInRe = aSq * rSec
        val zInIm = aSq * xSec
        val reverseAsk = Random.nextDouble() < 0.35
        val xSecStr = if (xSec >= 0) "+ j$xSec" else "− j${-xSec}"
        if (reverseAsk) {
            val zInStr = ComplexNum(zInRe, zInIm).formatRect()
            CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural Transformer Impedance",
                category = ProblemCategory.IMPEDANCE,
                difficulty = Difficulty.MASTERY,
                topology = CircuitTopology.COMPLEX_LOAD,
                prompt = "An ideal N₁:N₂ = ${ratio.first}:${ratio.second} transformer shows primary Z_in. Find secondary Z_L (in Ω):",
                parametersLine1 = "Turns Ratio N₁ : N₂ = ${ratio.first} : ${ratio.second}",
                parametersLine2 = "Measured Primary Z_in = ($zInStr) Ω",
                methodTitle = "INVERSE REFLECTION DRILL",
                methodNote = "Divide measured primary impedance Z_in by a² = (N₁/N₂)² to recover secondary load Z_L.",
                inputHint = "Enter Z_L in Ω (A + jB)",
                answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
                expectedComplex = ComplexNum(rSec.toDouble(), xSec.toDouble()),
                unit = "Ω",
                hints = listOf(
                    "a = ${ratio.first}/${ratio.second}, so a² = ${ComplexNum.formatClean(aSq)}.",
                    "Z_L = Z_in / ${ComplexNum.formatClean(aSq)}."
                ),
                workedSteps = listOf(
                    "1. a² = (${ratio.first}/${ratio.second})² = ${ComplexNum.formatClean(aSq)}.",
                    "2. Z_L = ($zInStr) / ${ComplexNum.formatClean(aSq)} = ($rSec $xSecStr) Ω."
                ),
                isProcedural = true
            )
        } else {
            CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural Transformer Impedance",
                category = ProblemCategory.IMPEDANCE,
                difficulty = Difficulty.MASTERY,
                topology = CircuitTopology.COMPLEX_LOAD,
                prompt = "An ideal transformer with turns ratio N₁:N₂ = ${ratio.first}:${ratio.second} feeds Z_L. Find reflected Z_in:",
                parametersLine1 = "Turns Ratio N₁ : N₂ = ${ratio.first} : ${ratio.second}",
                parametersLine2 = "Secondary Load Z_L = ($rSec $xSecStr) Ω",
                methodTitle = "IMPEDANCE REFLECTION DRILL",
                methodNote = "Scale secondary impedance Z_L by a² = (N₁/N₂)².",
                inputHint = "Enter Z_in in Ω (A + jB)",
                answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
                expectedComplex = ComplexNum(zInRe, zInIm),
                unit = "Ω",
                hints = listOf(
                    "Turns ratio a = ${ratio.first}/${ratio.second}, so a² = ${ComplexNum.formatClean(aSq)}.",
                    "Z_in = ${ComplexNum.formatClean(aSq)} × ($rSec $xSecStr) Ω."
                ),
                workedSteps = listOf(
                    "1. Impedance scaling factor a² = (${ratio.first}/${ratio.second})² = ${ComplexNum.formatClean(aSq)}.",
                    "2. Z_in = ${ComplexNum(zInRe, zInIm).formatRect()} Ω."
                ),
                isProcedural = true
            )
        }
    }
    4 -> {
        val r = listOf(10, 15, 20, 30).random()
        val omega = listOf(100, 200, 500, 1000).random()
        val l1Mh = listOf(30, 40, 50, 60).random()
        val l2Mh = listOf(20, 30, 40, 50).random()
        val mMh = listOf(5, 10, 15).random()
        val aiding = Random.nextBoolean()
        val lEqMh = if (aiding) l1Mh + l2Mh + 2 * mMh else l1Mh + l2Mh - 2 * mMh
        val xL = omega * (lEqMh / 1000.0)
        val modeLabel = if (aiding) "series aiding" else "series opposing"
        val signSym = if (aiding) "+" else "−"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Coupled Inductors",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.SERIES_RL,
            prompt = "Find total series impedance Z (in Ω) of R with two coupled coils ($modeLabel) at ω = $omega rad/s:",
            parametersLine1 = "R = ${r}Ω, L₁ = ${l1Mh}mH, L₂ = ${l2Mh}mH",
            parametersLine2 = "M = ${mMh}mH ($modeLabel), ω = $omega rad/s",
            methodTitle = "COUPLED INDUCTANCE DRILL",
            methodNote = "Compute L_eq = L₁ + L₂ $signSym 2M ($modeLabel), convert mH to H, then X_L = ωL_eq.",
            inputHint = "Enter Z in Ω (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(r.toDouble(), xL),
            unit = "Ω",
            hints = listOf(
                "L_eq = $l1Mh + $l2Mh $signSym 2($mMh) = $lEqMh mH.",
                "X_L = $omega × ${ComplexNum.formatClean(lEqMh / 1000.0)} = ${ComplexNum.formatClean(xL)} Ω."
            ),
            workedSteps = listOf(
                "1. L_eq = $l1Mh + $l2Mh $signSym ${2 * mMh} = $lEqMh mH = ${ComplexNum.formatClean(lEqMh / 1000.0)} H.",
                "2. X_L = $omega × ${ComplexNum.formatClean(lEqMh / 1000.0)} = ${ComplexNum.formatClean(xL)} Ω ⇒ Z = ${ComplexNum(r.toDouble(), xL).formatRect()} Ω."
            ),
            isProcedural = true
        )
    }
    5 -> {
        val thetaV = listOf(-15, 0, 15, 30, 45, 60).random()
        val thetaISin = listOf(30, 45, 60, 75, 90, 105).filter { (thetaV - (it - 90)) != 0 }.random()
        val thetaICos = thetaISin - 90
        val delta = (thetaV - thetaICos).toDouble()
        val tvStr = if (thetaV >= 0) "+ ${thetaV}°" else "− ${-thetaV}°"
        val tcStr = if (thetaICos >= 0) "+ ${thetaICos}°" else "− ${-thetaICos}°"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Sine–Cosine Phase",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.SERIES_RL,
            prompt = "Convert i(t) to cosine form and compute the impedance phase angle θ = θ_v − θ_i (in degrees):",
            parametersLine1 = "v(t) = 120 cos(377t $tvStr) V",
            parametersLine2 = "i(t) = 5 sin(377t + ${thetaISin}°) A",
            methodTitle = "PHASE ALIGNMENT DRILL",
            methodNote = "Shift sine by −90° to express both signals as cosines before computing θ_v − θ_i.",
            inputHint = "Enter θ in degrees",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = delta,
            unit = "°",
            hints = listOf(
                "i(t) = 5 cos(377t $tcStr) A, so θ_i = ${thetaICos}°.",
                "θ = ${thetaV}° − (${thetaICos}°)."
            ),
            workedSteps = listOf(
                "1. Convert sine: sin(377t + ${thetaISin}°) = cos(377t $tcStr) ⇒ θ_i = ${thetaICos}°.",
                "2. θ = ${thetaV}° − (${thetaICos}°) = ${delta.toInt()}°."
            ),
            isProcedural = true
        )
    }
    6 -> {
        val r1 = listOf(3, 4, 5, 6).random()
        val r2 = 10 - r1
        val x = listOf(2, 3, 4, 5, 6).random()
        val scale = listOf(1, 2, 3).random()
        val iSrc = 10 * scale
        val i1Re = (scale * r2).toDouble()
        val i1Im = -(scale * x).toDouble()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Complex Current Divider",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "Current I_s = $iSrc∠0° A splits between parallel Z₁ and Z₂. Find complex branch current I₁ (in A):",
            parametersLine1 = "I_s = $iSrc∠0° A,   Z₁ = ($r1 + j$x) Ω",
            parametersLine2 = "Z₂ = ($r2 − j$x) Ω  (parallel with Z₁)",
            methodTitle = "CURRENT DIVIDER DRILL",
            methodNote = "Add Z₁ + Z₂ (notice reactive terms cancel to 10 Ω), then compute I_s · Z₂ / (Z₁ + Z₂).",
            inputHint = "Enter I₁ in A (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(i1Re, i1Im),
            unit = "A",
            hints = listOf(
                "Z₁ + Z₂ = ($r1 + $r2) + j($x − $x) = 10 Ω.",
                "I₁ = ($iSrc / 10) × ($r2 − j$x) = $scale × ($r2 − j$x)."
            ),
            workedSteps = listOf(
                "1. Z₁ + Z₂ = 10 + j0 Ω.",
                "2. I₁ = $iSrc · ($r2 − j$x) / 10 = ${ComplexNum(i1Re, i1Im).formatRect()} A."
            ),
            isProcedural = true
        )
    }
    7 -> {
        val pick = listOf(
            Triple(3, 4, 25),
            Triple(4, 3, 25),
            Triple(6, 8, 50),
            Triple(8, 6, 50),
            Triple(5, 12, 169),
            Triple(12, 5, 169)
        ).random()
        val r = pick.first
        val xMag = pick.second
        val vS = pick.third
        val inductiveShunt = Random.nextBoolean()
        val den = (r * r + xMag * xMag).toDouble()
        val sign = if (inductiveShunt) 1 else -1
        val vTh = ComplexNum(vS * xMag * xMag / den, sign * vS * r * xMag / den)
        val z2Str = if (inductiveShunt) "+j$xMag Ω (inductive shunt L)" else "−j$xMag Ω (capacitive shunt C)"
        val zSumStr = if (inductiveShunt) "$r + j$xMag" else "$r − j$xMag"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural AC Thévenin V_th",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.SERIES_RC,
            prompt = "Find the open-circuit Thévenin voltage V_th (in Volts) across shunt branch Z₂:",
            parametersLine1 = "V_s = $vS∠0° V,   Z₁ = R = $r Ω",
            parametersLine2 = "Z₂ = $z2Str",
            methodTitle = "THÉVENIN DIVIDER DRILL",
            methodNote = "Compute complex voltage divider V_th = V_s · Z₂ / (Z₁ + Z₂) and rationalize the denominator.",
            inputHint = "Enter V_th in V (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = vTh,
            unit = "V",
            hints = listOf(
                "V_th = $vS × (${if (inductiveShunt) "+j$xMag" else "−j$xMag"}) / ($zSumStr).",
                "|$zSumStr|² = ${den.toInt()}."
            ),
            workedSteps = listOf(
                "1. V_th = $vS(${if (inductiveShunt) "+j$xMag" else "−j$xMag"}) / ($zSumStr).",
                "2. V_th = ${vTh.formatRect()} V (${vTh.formatPolar()})."
            ),
            isProcedural = true
        )
    }
    else -> {
        val scale = listOf(1, 2).random()
        val pick = listOf(
            listOf(1, 2, 2, 3),
            listOf(2, 3, 6, 7),
            listOf(3, 4, 12, 13),
            listOf(1, 4, 8, 9),
            listOf(4, 4, 7, 9)
        ).random()
        val r = 12 * scale
        val lMh = 25 * scale
        val v0 = pick[0] * 12 * scale
        val v1Rms = pick[1] * 13 * scale
        val v2Rms = pick[2] * 20 * scale
        val iTot = pick[3].toDouble()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Multi-Freq RL RMS",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RL,
            prompt = "A multi-frequency voltage v(t) drives series RL load (R = $r Ω, L = $lMh mH). Find total RMS current I_rms (in A):",
            parametersLine1 = "v(t) = $v0 + ${v1Rms}√2 cos(200t) + ${v2Rms}√2 sin(640t) V",
            parametersLine2 = "R = $r Ω,  L = $lMh mH   (I_rms = √(I₀² + I₁² + I₂²))",
            methodTitle = "SUPERPOSITION RMS DRILL",
            methodNote = "Solve |I_k| = V_k,rms / |R + jω_k L| separately at ω = 0, 200, and 640 rad/s, then combine in RMS.",
            inputHint = "Enter I_rms in Amps",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = iTot,
            unit = "A",
            hints = listOf(
                "I₀ = $v0 / $r = ${pick[0]} A; |Z(200)| = ${13 * scale} Ω ⇒ I₁ = ${pick[1]} A.",
                "|Z(640)| = ${20 * scale} Ω ⇒ I₂ = ${pick[2]} A."
            ),
            workedSteps = listOf(
                "1. I₀ = ${pick[0]} A, I₁ = ${pick[1]} A, I₂ = ${pick[2]} A.",
                "2. I_rms = √(${pick[0]}² + ${pick[1]}² + ${pick[2]}²) = ${pick[3]} A."
            ),
            isProcedural = true
        )
    }
}

internal fun generateAdmittanceDrill(
    drillId: String,
    drillNum: Int,
    preferredDifficulty: Difficulty?,
    excluded: Set<String>
): CircuitProblem = when (
    pickTemplateIndex(
        listOf(
            "Procedural Complex Admittance" to Difficulty.INTERMEDIATE,
            "Procedural LCR Loss Factor D" to Difficulty.INTERMEDIATE,
            "Procedural LCR Series → Parallel R_p" to Difficulty.MASTERY,
            "Procedural LCR Parallel C_p" to Difficulty.MASTERY,
            "Procedural Parallel G + jB" to Difficulty.FOUNDATIONS,
            "Procedural Capacitor SRF" to Difficulty.ADVANCED,
            "Procedural Millman Nodal Voltage" to Difficulty.INTERMEDIATE,
            "Procedural Leakage R_p from D" to Difficulty.MASTERY
        ),
        preferredDifficulty,
        excluded
    )
) {
    0 -> {
        val pyth = listOf(Triple(3, 4, 5), Triple(6, 8, 10), Triple(8, 6, 10)).random()
        val scale = listOf(1, 2).random()
        val isCapacitive = Random.nextBoolean()
        val r = pyth.first * scale.toDouble()
        val x = pyth.second * scale.toDouble() * (if (isCapacitive) -1 else 1)
        val magSq = r * r + x * x
        val g = r / magSq
        val b = -x / magSq
        val zSign = if (x >= 0) "+ j${x.toInt()}" else "− j${(-x).toInt()}"
        val conjSign = if (x >= 0) "− j${x.toInt()}" else "+ j${(-x).toInt()}"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Complex Admittance",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "Convert series impedance Z to complex admittance Y = G + jB (in Siemens):",
            parametersLine1 = "Z = (${r.toInt()} $zSign) Ω",
            parametersLine2 = "Y = 1 / Z = G + jB",
            methodTitle = "ADMITTANCE DRILL",
            methodNote = "Multiply numerator and denominator by the conjugate (${r.toInt()} $conjSign).",
            inputHint = "Enter Y in S (G + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(g, b),
            unit = "S",
            hints = listOf(
                "|Z|² = ${r.toInt()}² + ${pyth.second * scale}² = ${magSq.toInt()}.",
                "Y = (${r.toInt()} $conjSign) / ${magSq.toInt()}."
            ),
            workedSteps = listOf(
                "1. |Z|² = ${r.toInt()}² + ${pyth.second * scale}² = ${magSq.toInt()}.",
                "2. Y = (${r.toInt()} $conjSign) / ${magSq.toInt()} = ${ComplexNum(g, b).formatRect()} S."
            ),
            isProcedural = true
        )
    }
    1 -> {
        val xc = listOf(100, 200, 250, 400, 500, 1000).random()
        val dTarget = listOf(0.01, 0.02, 0.04, 0.05, 0.08, 0.1).random()
        val esr = xc * dTarget
        val esrStr = ComplexNum.formatClean(esr)
        val dStr = ComplexNum.formatClean(dTarget)
        val mode = Random.nextInt(3)
        when (mode) {
            0 -> CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural LCR Loss Factor D",
                category = ProblemCategory.ADMITTANCE,
                difficulty = Difficulty.INTERMEDIATE,
                topology = CircuitTopology.SERIES_RC,
                prompt = "An LCR meter measures a capacitor's ESR and reactance |X_C|. Compute dissipation factor D:",
                parametersLine1 = "ESR = $esrStr Ω,   |X_C| = $xc Ω",
                parametersLine2 = "D = tan(δ) = ESR / |X_C|",
                methodTitle = "LCR LOSS DRILL",
                methodNote = "Divide equivalent series resistance ESR by capacitive reactance magnitude |X_C|.",
                inputHint = "Enter D (e.g. 0.02)",
                answerKind = AnswerKind.REAL_SCALAR,
                expectedReal = dTarget,
                unit = "",
                hints = listOf("D = ESR / |X_C| = $esrStr / $xc.", "Express as a dimensionless decimal."),
                workedSteps = listOf("1. D = ESR / |X_C| = $esrStr / $xc = $dStr."),
                isProcedural = true
            )
            1 -> CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural LCR Loss Factor D",
                category = ProblemCategory.ADMITTANCE,
                difficulty = Difficulty.INTERMEDIATE,
                topology = CircuitTopology.SERIES_RC,
                prompt = "An LCR meter reports capacitor dissipation factor D and |X_C|. Find equivalent series resistance ESR (in Ω):",
                parametersLine1 = "D = $dStr,   |X_C| = $xc Ω",
                parametersLine2 = "ESR = D · |X_C|",
                methodTitle = "LCR ESR EXTRACTION",
                methodNote = "Multiply dissipation factor D = tan(δ) by |X_C|.",
                inputHint = "Enter ESR in Ω",
                answerKind = AnswerKind.REAL_SCALAR,
                expectedReal = esr,
                unit = "Ω",
                hints = listOf("ESR = D × |X_C| = $dStr × $xc.", "Compute product in Ohms."),
                workedSteps = listOf("1. ESR = $dStr × $xc = $esrStr Ω."),
                isProcedural = true
            )
            else -> {
                val qTarget = 1.0 / dTarget
                CircuitProblem(
                    id = drillId,
                    code = "Drill #$drillNum",
                    title = "Procedural LCR Loss Factor D",
                    category = ProblemCategory.ADMITTANCE,
                    difficulty = Difficulty.INTERMEDIATE,
                    topology = CircuitTopology.SERIES_RC,
                    prompt = "An LCR meter measures ESR and capacitive reactance |X_C|. Find component Quality Factor Q = 1 / D:",
                    parametersLine1 = "ESR = $esrStr Ω,   |X_C| = $xc Ω",
                    parametersLine2 = "Q = |X_C| / ESR = 1 / D",
                    methodTitle = "LCR Q-FACTOR DRILL",
                    methodNote = "Divide reactive magnitude |X_C| by ESR (or invert dissipation factor D).",
                    inputHint = "Enter Q-factor",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = qTarget,
                    unit = "",
                    hints = listOf("Q = |X_C| / ESR = $xc / $esrStr.", "Note D = $dStr, so Q = 1 / D."),
                    workedSteps = listOf("1. Q = $xc / $esrStr = ${ComplexNum.formatClean(qTarget)}."),
                    isProcedural = true
                )
            }
        }
    }
    2 -> {
        val rs = listOf(1, 2, 3, 4, 5, 8, 10).random()
        val q = listOf(2, 3, 4, 5, 6, 8, 10, 12).random()
        val xs = rs * q
        val isInductive = Random.nextBoolean()
        val rp = rs * (1 + q * q).toDouble()
        val xLabel = if (isInductive) "+ j$xs" else "− j$xs"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural LCR Series → Parallel R_p",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "An LCR meter in series mode reads Z_s = R_s $xLabel Ω. Convert to equivalent parallel resistance R_p (in Ω):",
            parametersLine1 = "Series Readout Z_s = ($rs $xLabel) Ω",
            parametersLine2 = "Q = |X_s| / R_s = $q,   R_p = R_s · (1 + Q²)",
            methodTitle = "LCR EQUIVALENCE DRILL",
            methodNote = "Compute Q = |X_s| / R_s and apply R_p = R_s(1 + Q²).",
            inputHint = "Enter R_p in Ω",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = rp,
            unit = "Ω",
            hints = listOf(
                "Q = $xs / $rs = $q, so 1 + Q² = ${1 + q * q}.",
                "R_p = $rs × ${1 + q * q}."
            ),
            workedSteps = listOf(
                "1. Q = $xs / $rs = $q ⇒ 1 + Q² = ${1 + q * q}.",
                "2. R_p = $rs × ${1 + q * q} = ${rp.toInt()} Ω."
            ),
            isProcedural = true
        )
    }
    3 -> {
        val gP = listOf(0.01, 0.02, 0.04, 0.05).random()
        val bP = listOf(0.02, 0.04, 0.05, 0.08, 0.1).random()
        val omega = listOf(500, 1000, 2000).random()
        val cUf = (bP / omega.toDouble()) * 1_000_000.0
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural LCR Parallel C_p",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "An LCR meter in C_p–G mode reports admittance Y at ω = $omega rad/s. Find parallel C_p (in μF):",
            parametersLine1 = "Y = (${ComplexNum.formatClean(gP)} + j${ComplexNum.formatClean(bP)}) S",
            parametersLine2 = "ω = $omega rad/s",
            methodTitle = "LCR PARALLEL SUSCEPTANCE",
            methodNote = "Use C_p = B_p / ω and multiply Farads by 10⁶ to convert to μF.",
            inputHint = "Enter C_p in μF",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = cUf,
            unit = "μF",
            hints = listOf(
                "Extract capacitive susceptance B_p = Im(Y) = ${ComplexNum.formatClean(bP)} S.",
                "C_p = ${ComplexNum.formatClean(bP)} / $omega F = ${ComplexNum.formatClean(cUf)} μF."
            ),
            workedSteps = listOf(
                "1. B_p = ωC_p = ${ComplexNum.formatClean(bP)} S.",
                "2. C_p = (${ComplexNum.formatClean(bP)} / $omega) × 10⁶ = ${ComplexNum.formatClean(cUf)} μF."
            ),
            isProcedural = true
        )
    }
    4 -> {
        val r = listOf(10, 20, 25, 40, 50).random()
        val omega = listOf(500, 1000, 2000).random()
        val bTarget = listOf(0.01, 0.02, 0.04, 0.05, 0.08, 0.1).random()
        val cUf = (bTarget / omega.toDouble()) * 1_000_000.0
        val gTarget = 1.0 / r.toDouble()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Parallel G + jB",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "Find total parallel admittance Y = 1/R + jωC (in Siemens) at ω = $omega rad/s:",
            parametersLine1 = "R = ${r}Ω   ||   C = ${ComplexNum.formatClean(cUf)} μF",
            parametersLine2 = "ω = $omega rad/s",
            methodTitle = "PARALLEL ADMITTANCE DRILL",
            methodNote = "Sum conductance G = 1/R and positive capacitive susceptance +jωC.",
            inputHint = "Enter Y in S (G + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(gTarget, bTarget),
            unit = "S",
            hints = listOf(
                "G = 1 / $r = ${ComplexNum.formatClean(gTarget)} S.",
                "B_C = $omega × (${ComplexNum.formatClean(cUf)}×10⁻⁶) = ${ComplexNum.formatClean(bTarget)} S."
            ),
            workedSteps = listOf(
                "1. G = 1 / $r = ${ComplexNum.formatClean(gTarget)} S; B_C = ${ComplexNum.formatClean(bTarget)} S.",
                "2. Y = ${ComplexNum(gTarget, bTarget).formatRect()} S."
            ),
            isProcedural = true
        )
    }
    5 -> {
        val srfCombos = listOf(
            Triple(10, 10, 100),
            Triple(40, 10, 50),
            Triple(25, 16, 50),
            Triple(25, 25, 40),
            Triple(16, 100, 25),
            Triple(4, 100, 50),
            Triple(10, 40, 50),
            Triple(16, 25, 50),
            Triple(5, 20, 100),
            Triple(8, 50, 50),
            Triple(2, 50, 100),
            Triple(25, 64, 25),
            Triple(25, 100, 20),
            Triple(40, 40, 25)
        )
        val pick = srfCombos.random()
        val lVal = pick.first
        val cVal = pick.second
        val srfMrad = pick.third.toDouble()
        val useUhPf = Random.nextBoolean()
        val lUnit = if (useUhPf) "μH" else "nH"
        val cUnit = if (useUhPf) "pF" else "nF"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Capacitor SRF",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "A reactive component has series L = $lVal $lUnit and C = $cVal $cUnit. Find ω_SRF = 1/√(L·C) (in Mrad/s):",
            parametersLine1 = "C = $cVal $cUnit,   L_s = $lVal $lUnit",
            parametersLine2 = "Self-resonance when ω·L_s = 1/(ω·C)",
            methodTitle = "SELF-RESONANCE DRILL",
            methodNote = "Convert L ($lUnit) and C ($cUnit), compute L·C (10⁻¹⁸ s² scale), take 1/√(L·C), and divide by 10⁶.",
            inputHint = "Enter ω_SRF in Mrad/s",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = srfMrad,
            unit = "Mrad/s",
            hints = listOf(
                "L·C = ${lVal * cVal} × 10⁻¹⁸ s².",
                "Take square root and invert, then convert rad/s to Mrad/s (÷ 10⁶)."
            ),
            workedSteps = listOf(
                "1. √(L·C) = √(${lVal * cVal}) × 10⁻⁹ s.",
                "2. ω_SRF = 1 / √(L·C) = ${srfMrad.toInt()} × 10⁶ rad/s = ${srfMrad.toInt()} Mrad/s."
            ),
            isProcedural = true
        )
    }
    6 -> {
        val pick = listOf(
            listOf(20.0, 0.2, 0.1, 20.0, 0.2, -0.1, 0.6),
            listOf(30.0, 0.2, 0.1, 20.0, 0.2, -0.1, 0.6),
            listOf(20.0, 0.3, 0.2, 10.0, 0.3, -0.2, 0.4),
            listOf(40.0, 0.1, 0.15, 20.0, 0.1, -0.15, 0.3),
            listOf(25.0, 0.2, 0.2, 15.0, 0.2, -0.2, 0.1)
        ).random()
        val v1 = pick[0]; val g1 = pick[1]; val b1 = pick[2]
        val v2 = pick[3]; val g2 = pick[4]; val b2 = pick[5]
        val g3 = pick[6]
        val sumG = g1 + g2 + g3
        val iRe = v1 * g1 - v2 * b2
        val iIm = v1 * b1 + v2 * g2
        val vNode = ComplexNum(iRe / sumG, iIm / sumG)
        val y1Str = ComplexNum(g1, b1).formatRect()
        val y2Str = ComplexNum(g2, b2).formatRect()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Millman Nodal Voltage",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.PARALLEL_RLC,
            prompt = "Three parallel AC branches share a common node to ground. Use Millman's theorem to find V_node (in Volts):",
            parametersLine1 = "V₁ = ${v1.toInt()}∠0° V via Y₁ = ($y1Str) S;   V₂ = ${v2.toInt()}∠90° V via Y₂ = ($y2Str) S",
            parametersLine2 = "Shunt Y₃ = $g3 S (V₃ = 0 V)   (V_node = Σ(V_k Y_k) / Σ Y_k)",
            methodTitle = "MILLMAN NODAL DRILL",
            methodNote = "Sum short-circuit branch currents Σ(V_k·Y_k) and divide by total nodal admittance ΣY_k.",
            inputHint = "Enter V_node in V (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = vNode,
            unit = "V",
            hints = listOf(
                "ΣY_k = ${ComplexNum.formatClean(sumG)} S.",
                "ΣV_k Y_k = ${ComplexNum(iRe, iIm).formatRect()} A."
            ),
            workedSteps = listOf(
                "1. ΣY_k = ${ComplexNum.formatClean(sumG)} S, ΣV_k Y_k = ${ComplexNum(iRe, iIm).formatRect()} A.",
                "2. V_node = ${vNode.formatRect()} V."
            ),
            isProcedural = true
        )
    }
    else -> {
        val (cUf, omega, dVal) = listOf(
            Triple(0.5, 2000, 0.005),
            Triple(0.25, 2000, 0.005),
            Triple(1.0, 1000, 0.002),
            Triple(0.4, 5000, 0.004),
            Triple(0.5, 4000, 0.005),
            Triple(0.2, 5000, 0.008)
        ).random()
        val bp = omega * cUf * 1e-6
        val gp = dVal * bp
        val rpK = 1.0 / (gp * 1000.0)
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Leakage R_p from D",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "A film capacitor modeled as parallel C_p ∥ R_p has loss tangent D at ω = $omega rad/s. Find parallel leakage R_p (in kΩ):",
            parametersLine1 = "C_p = $cUf μF,   ω = $omega rad/s",
            parametersLine2 = "D = G_p / (ω·C_p) = $dVal",
            methodTitle = "PARALLEL LOSS DRILL",
            methodNote = "Compute B_p = ω·C_p, then G_p = D·B_p and R_p = 1 / G_p.",
            inputHint = "Enter R_p in kΩ",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = rpK,
            unit = "kΩ",
            hints = listOf(
                "B_p = $omega × $cUf μF = ${ComplexNum.formatClean(bp * 1000.0)} mS.",
                "R_p = 1 / (ω·C_p·D) in Ohms, then ÷ 1000 for kΩ."
            ),
            workedSteps = listOf(
                "1. G_p = D·ω·C_p = ${ComplexNum.formatClean(gp * 1e6)} μS.",
                "2. R_p = 1 / G_p = ${ComplexNum.formatClean(rpK)} kΩ."
            ),
            isProcedural = true
        )
    }
}

internal fun generateResonanceDrill(
    drillId: String,
    drillNum: Int,
    preferredDifficulty: Difficulty?,
    excluded: Set<String>
): CircuitProblem = when (
    pickTemplateIndex(
        listOf(
            "Procedural Resonance Q" to Difficulty.INTERMEDIATE,
            "Procedural Resonant Frequency ω₀" to Difficulty.FOUNDATIONS,
            "Procedural −3 dB Bandwidth" to Difficulty.INTERMEDIATE,
            "Procedural Resonant Voltage Gain" to Difficulty.MASTERY,
            "Procedural Lossy Tank Z_tank" to Difficulty.ADVANCED,
            "Procedural L-Match Network" to Difficulty.ADVANCED,
            "Procedural Tank Current Magnification" to Difficulty.INTERMEDIATE,
            "Procedural Off-Resonance Detuning" to Difficulty.MASTERY
        ),
        preferredDifficulty,
        excluded
    )
) {
    0 -> {
        val r = listOf(5, 10, 20, 25).random()
        val qTarget = listOf(4, 5, 8, 10, 12, 15).random()
        val x0 = r * qTarget.toDouble()
        val omega0 = listOf(100, 200, 500, 1000).random()
        val lMh = (x0 / omega0) * 1000.0
        val cUf = 1_000_000.0 / (omega0 * x0)
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Resonance Q",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Calculate the quality factor Q at resonance for the series RLC:",
            parametersLine1 = "R = ${r}Ω, L = ${ComplexNum.formatClean(lMh)}mH",
            parametersLine2 = "C = ${ComplexNum.formatClean(cUf)}μF (ω₀ = $omega0 rad/s)",
            methodTitle = "Q-FACTOR DRILL",
            methodNote = "Use Q = ω₀L / R = (1/R)√(L/C).",
            inputHint = "Enter Q-factor",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = qTarget.toDouble(),
            unit = "",
            hints = listOf(
                "At ω₀ = $omega0 rad/s, Q = ω₀·L / R.",
                "Convert mH to Henries (×10⁻³) before multiplying by ω₀."
            ),
            workedSteps = listOf(
                "1. X_L0 = ω₀L = $omega0 × ${ComplexNum.formatClean(lMh / 1000.0)} = ${ComplexNum.formatClean(x0)} Ω.",
                "2. Q = X_L0 / R = ${ComplexNum.formatClean(x0)} / $r = $qTarget."
            ),
            isProcedural = true
        )
    }
    1 -> {
        val a = listOf(1, 2, 4, 5).random()
        val b = listOf(1, 2, 5).random()
        val lMh = 10.0 * a
        val cUf = 10_000.0 / (a * b * b)
        val omega0 = 100.0 * b
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Resonant Frequency ω₀",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Find the resonant angular frequency ω₀ = 1/√(LC) (in rad/s) for the LC tank:",
            parametersLine1 = "L = ${ComplexNum.formatClean(lMh)} mH,   C = ${ComplexNum.formatClean(cUf)} μF",
            parametersLine2 = "ω₀ = 1 / √(L · C)",
            methodTitle = "RESONANCE FREQUENCY DRILL",
            methodNote = "Convert L to H (×10⁻³) and C to F (×10⁻⁶), multiply L·C, and take 1/√(LC).",
            inputHint = "Enter ω₀ in rad/s",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = omega0,
            unit = "rad/s",
            hints = listOf(
                "L·C = ${ComplexNum.formatClean(1e-4 / (b * b))} s².",
                "√(LC) = ${ComplexNum.formatClean(1e-2 / b)} s."
            ),
            workedSteps = listOf(
                "1. L·C = (${ComplexNum.formatClean(lMh)}×10⁻³) × (${ComplexNum.formatClean(cUf)}×10⁻⁶) = 1 / ${(omega0 * omega0).toInt()} s².",
                "2. ω₀ = 1 / √(LC) = ${omega0.toInt()} rad/s."
            ),
            isProcedural = true
        )
    }
    2 -> {
        val q = listOf(5, 8, 10, 20, 25, 40, 50).random()
        val bwPool = listOf(20, 40, 50, 100, 200, 250, 500)
        val bw = bwPool.random()
        val f0 = bw * q
        val mode = Random.nextInt(3)
        when (mode) {
            0 -> CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural −3 dB Bandwidth",
                category = ProblemCategory.RESONANCE,
                difficulty = Difficulty.INTERMEDIATE,
                topology = CircuitTopology.PARALLEL_RLC,
                prompt = "Calculate the half-power (−3 dB) bandwidth BW = f₀ / Q (in Hz) for the tuned resonator:",
                parametersLine1 = "Center Frequency f₀ = $f0 Hz",
                parametersLine2 = "Quality Factor Q = $q",
                methodTitle = "BANDWIDTH DRILL",
                methodNote = "Divide center resonant frequency f₀ by quality factor Q.",
                inputHint = "Enter BW in Hz",
                answerKind = AnswerKind.REAL_SCALAR,
                expectedReal = bw.toDouble(),
                unit = "Hz",
                hints = listOf("Half-power bandwidth BW = f₂ − f₁ = f₀ / Q.", "Compute $f0 / $q."),
                workedSteps = listOf("1. BW = f₀ / Q = $f0 / $q = $bw Hz."),
                isProcedural = true
            )
            1 -> CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural −3 dB Bandwidth",
                category = ProblemCategory.RESONANCE,
                difficulty = Difficulty.INTERMEDIATE,
                topology = CircuitTopology.PARALLEL_RLC,
                prompt = "A resonator tuned to f₀ = $f0 Hz has −3 dB bandwidth BW = $bw Hz. Find Quality Factor Q:",
                parametersLine1 = "Center Frequency f₀ = $f0 Hz",
                parametersLine2 = "−3 dB Bandwidth BW = $bw Hz",
                methodTitle = "Q FROM BANDWIDTH",
                methodNote = "Divide center frequency f₀ by passband width BW.",
                inputHint = "Enter Q-factor",
                answerKind = AnswerKind.REAL_SCALAR,
                expectedReal = q.toDouble(),
                unit = "",
                hints = listOf("Q = f₀ / BW = $f0 / $bw.", "Higher selectivity means larger Q."),
                workedSteps = listOf("1. Q = $f0 / $bw = $q."),
                isProcedural = true
            )
            else -> CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural −3 dB Bandwidth",
                category = ProblemCategory.RESONANCE,
                difficulty = Difficulty.INTERMEDIATE,
                topology = CircuitTopology.PARALLEL_RLC,
                prompt = "A bandpass filter has Q = $q and −3 dB bandwidth BW = $bw Hz. Find center frequency f₀ (in Hz):",
                parametersLine1 = "Quality Factor Q = $q",
                parametersLine2 = "−3 dB Bandwidth BW = $bw Hz",
                methodTitle = "CENTER FREQUENCY DRILL",
                methodNote = "Multiply half-power bandwidth BW by quality factor Q.",
                inputHint = "Enter f₀ in Hz",
                answerKind = AnswerKind.REAL_SCALAR,
                expectedReal = f0.toDouble(),
                unit = "Hz",
                hints = listOf("Rearrange Q = f₀ / BW ⇒ f₀ = BW × Q.", "Compute $bw × $q."),
                workedSteps = listOf("1. f₀ = $bw × $q = $f0 Hz."),
                isProcedural = true
            )
        }
    }
    3 -> {
        val r = listOf(5, 10, 20).random()
        val q = listOf(5, 8, 10, 12, 15).random()
        val xC0 = r * q
        val vs = listOf(5, 10, 12, 24).random()
        val vcMag = (q * vs).toDouble()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Resonant Voltage Gain",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "At series resonance (X_L = X_C = ${xC0}Ω), find the magnified capacitor voltage |V_C| (in V):",
            parametersLine1 = "V_s = $vs V (RMS),   R = ${r}Ω",
            parametersLine2 = "X_L0 = X_C0 = ${xC0}Ω",
            methodTitle = "Q-MAGNIFICATION DRILL",
            methodNote = "Compute Q = X_C0 / R (or I = V_s / R) and solve |V_C| = Q · V_s.",
            inputHint = "Enter |V_C| in Volts",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = vcMag,
            unit = "V",
            hints = listOf(
                "Quality factor Q = X_C0 / R = $xC0 / $r = $q.",
                "At resonance, |V_C| = Q × V_s = $q × $vs."
            ),
            workedSteps = listOf(
                "1. Series resonant current I = $vs / $r = ${ComplexNum.formatClean(vs.toDouble() / r)} A (and Q = $q).",
                "2. |V_C| = I · X_C0 = Q · V_s = ${vcMag.toInt()} V."
            ),
            isProcedural = true
        )
    }
    4 -> {
        val rs = listOf(2, 4, 5, 10).random()
        val qCoil = listOf(2, 3, 4, 5, 6).random()
        val xL = rs * qCoil
        val zTank = rs * (1 + qCoil * qCoil).toDouble()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Lossy Tank Z_tank",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.PARALLEL_RLC,
            prompt = "A lossy coil (R_s = ${rs}Ω, X_L = ${xL}Ω) is resonated at unity pf by parallel C. Find Z_tank:",
            parametersLine1 = "Lossy Coil Branch: R_s = ${rs}Ω, X_L = ${xL}Ω",
            parametersLine2 = "Shunted by lossless C (Q_coil = $qCoil)",
            methodTitle = "LOSSY TANK DRILL",
            methodNote = "Compute Q_coil = X_L / R_s and parallel equivalent Z_tank = R_s(1 + Q_coil²).",
            inputHint = "Enter Z_tank in Ω",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = zTank,
            unit = "Ω",
            hints = listOf(
                "Q_coil = $xL / $rs = $qCoil.",
                "Z_tank = $rs × (1 + $qCoil²) = ${zTank.toInt()} Ω."
            ),
            workedSteps = listOf(
                "1. Q_coil = $xL / $rs = $qCoil.",
                "2. Z_tank = $rs(1 + ${qCoil * qCoil}) = ${zTank.toInt()} Ω."
            ),
            isProcedural = true
        )
    }
    5 -> {
        val q = listOf(2, 3, 4, 5, 6).random()
        val rS = listOf(5, 10, 15, 20, 25, 40, 50).random()
        val rL = rS * (1 + q * q)
        val xL = (q * rS).toDouble()
        val xC = rL.toDouble() / q.toDouble()
        val askShuntC = Random.nextBoolean()
        if (askShuntC) {
            CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural L-Match Network",
                category = ProblemCategory.RESONANCE,
                difficulty = Difficulty.ADVANCED,
                topology = CircuitTopology.PARALLEL_RLC,
                prompt = "Match R_s = ${rS}Ω up to R_L = ${rL}Ω using series +jX_L and shunt −jX_C across R_L. Find shunt |X_C| (in Ω):",
                parametersLine1 = "R_s = $rS Ω  →  Load R_L = $rL Ω",
                parametersLine2 = "Q = √(R_L / R_s − 1),   X_C = R_L / Q",
                methodTitle = "L-MATCH SHUNT DRILL",
                methodNote = "Compute Q = √(R_L/R_s − 1) and solve parallel shunt capacitive reactance X_C = R_L / Q.",
                inputHint = "Enter X_C in Ω",
                answerKind = AnswerKind.REAL_SCALAR,
                expectedReal = xC,
                unit = "Ω",
                hints = listOf(
                    "Q = √($rL / $rS − 1) = √${q * q} = $q.",
                    "X_C = $rL / $q = ${ComplexNum.formatClean(xC)} Ω."
                ),
                workedSteps = listOf(
                    "1. Transformation factor Q = √($rL/$rS − 1) = $q.",
                    "2. Shunt capacitive reactance X_C = $rL / $q = ${ComplexNum.formatClean(xC)} Ω."
                ),
                isProcedural = true
            )
        } else {
            CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural L-Match Network",
                category = ProblemCategory.RESONANCE,
                difficulty = Difficulty.ADVANCED,
                topology = CircuitTopology.PARALLEL_RLC,
                prompt = "Match R_s = ${rS}Ω up to R_L = ${rL}Ω using series +jX_L and shunt −jX_C across R_L. Find X_L (in Ω):",
                parametersLine1 = "R_s = $rS Ω  →  Load R_L = $rL Ω",
                parametersLine2 = "Q = √(R_L / R_s − 1),   X_L = Q · R_s",
                methodTitle = "L-MATCH NETWORK DRILL",
                methodNote = "Compute Q = √(R_L/R_s − 1) and solve series inductive reactance X_L = Q · R_s.",
                inputHint = "Enter X_L in Ω",
                answerKind = AnswerKind.REAL_SCALAR,
                expectedReal = xL,
                unit = "Ω",
                hints = listOf(
                    "Q = √($rL / $rS − 1) = √${q * q} = $q.",
                    "X_L = $q × $rS = ${xL.toInt()} Ω."
                ),
                workedSteps = listOf(
                    "1. Transformation factor Q = √($rL/$rS − 1) = $q.",
                    "2. Required series reactance X_L = $q × $rS = ${xL.toInt()} Ω."
                ),
                isProcedural = true
            )
        }
    }
    6 -> {
        val iSource = listOf(2, 4, 5, 10).random()
        val qTank = listOf(3, 4, 5, 6, 8, 10, 12).random()
        val xL = listOf(10, 20, 25, 40, 50).random()
        val r = qTank * xL
        val vTank = iSource * r
        val iInductor = (iSource * qTank).toDouble()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Tank Current Magnification",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.PARALLEL_RLC,
            prompt = "A parallel RLC tank at resonance ω₀ is driven by current source I_s. Find circulating inductor current |I_L| (in Amps):",
            parametersLine1 = "I_s = $iSource A rms,   R = $r Ω",
            parametersLine2 = "X_L = X_C = $xL Ω at resonance ω₀",
            methodTitle = "PARALLEL CURRENT MAGNIFICATION",
            methodNote = "At parallel resonance reactive branch currents cancel, so V_tank = I_s·R and |I_L| = V_tank / X_L = Q_p·I_s.",
            inputHint = "Enter |I_L| in Amps",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = iInductor,
            unit = "A",
            hints = listOf(
                "Tank voltage V_tank = I_s × R = $iSource × $r = $vTank V (or Q_p = R / X_L = $qTank).",
                "|I_L| = V_tank / X_L = $vTank / $xL."
            ),
            workedSteps = listOf(
                "1. Reactive currents cancel at ω₀, leaving V_tank = I_s·R = $vTank V.",
                "2. Inductor current |I_L| = $vTank / $xL = ${iInductor.toInt()} A (= $qTank × $iSource)."
            ),
            isProcedural = true
        )
    }
    else -> {
        val r = listOf(40, 80, 120).random()
        val q = listOf(50, 100).random()
        val f0Khz = listOf(40.0, 80.0, 100.0).random()
        val ratios = listOf(
            Triple(3, 4, 5),
            Triple(12, 5, 13),
            Triple(15, 8, 17)
        )
        val tri = ratios.random()
        val twoQDelta = tri.first.toDouble() / tri.second.toDouble()
        val delta = twoQDelta / (2.0 * q)
        val fDriveKhz = f0Khz * (1.0 + delta)
        val zMag = r.toDouble() * (tri.third.toDouble() / tri.second.toDouble())
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Off-Resonance Detuning",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "A high-Q series RLC circuit (R = ${r}Ω, Q = $q at f₀ = ${ComplexNum.formatClean(f0Khz)} kHz) is driven at f = ${ComplexNum.formatClean(fDriveKhz)} kHz. Find |Z| (in Ω):",
            parametersLine1 = "R = $r Ω,   Q = $q,   f₀ = ${ComplexNum.formatClean(f0Khz)} kHz",
            parametersLine2 = "Drive f = ${ComplexNum.formatClean(fDriveKhz)} kHz  (δ = Δf/f₀,  |Z| ≈ R√(1 + (2Qδ)²))",
            methodTitle = "NARROWBAND DETUNING DRILL",
            methodNote = "Compute fractional detuning δ = (f − f₀)/f₀ and normalized detuning 2Qδ, then evaluate |Z| ≈ R√(1 + (2Qδ)²).",
            inputHint = "Enter |Z| in Ω",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = zMag,
            unit = "Ω",
            hints = listOf(
                "Fractional detuning δ = (${ComplexNum.formatClean(fDriveKhz)} − ${ComplexNum.formatClean(f0Khz)}) / ${ComplexNum.formatClean(f0Khz)} = ${ComplexNum.formatClean(delta)}.",
                "Normalized detuning 2Qδ = 2 × $q × ${ComplexNum.formatClean(delta)} = ${ComplexNum.formatClean(twoQDelta)}."
            ),
            workedSteps = listOf(
                "1. δ = Δf / f₀ = ${ComplexNum.formatClean(delta)}, so 2Qδ = ${ComplexNum.formatClean(twoQDelta)}.",
                "2. |Z| = $r × √(1 + ${ComplexNum.formatClean(twoQDelta)}²) = ${ComplexNum.formatClean(zMag)} Ω."
            ),
            isProcedural = true
        )
    }
}

internal fun generatePowerDrill(
    drillId: String,
    drillNum: Int,
    preferredDifficulty: Difficulty?,
    excluded: Set<String>
): CircuitProblem = when (
    pickTemplateIndex(
        listOf(
            "Procedural Power Factor" to Difficulty.FOUNDATIONS,
            "Procedural Complex Power S = V·I*" to Difficulty.INTERMEDIATE,
            "Procedural Max Power Transfer" to Difficulty.MASTERY,
            "Procedural 3-Phase Δ ↔ Y" to Difficulty.INTERMEDIATE,
            "Procedural 3-Phase Line Power" to Difficulty.ADVANCED,
            "Procedural Multi-Freq RMS" to Difficulty.FOUNDATIONS,
            "Procedural Partial PFC (kVAR)" to Difficulty.MASTERY,
            "Procedural Distortion Power Factor" to Difficulty.MASTERY
        ),
        preferredDifficulty,
        excluded
    )
) {
    0 -> {
        val triples = listOf(
            Triple(3, 4, 5), Triple(4, 3, 5), Triple(6, 8, 10),
            Triple(8, 6, 10), Triple(5, 12, 13), Triple(12, 5, 13),
            Triple(7, 24, 25), Triple(8, 15, 17)
        )
        val t = triples.random()
        val scale = listOf(50, 100, 150, 200, 250, 500).random()
        val vRms = listOf(120, 208, 240, 480).random()
        val qSign = if (Random.nextBoolean()) 1 else -1
        val p = t.first * scale
        val q = t.second * scale * qSign
        val sMag = t.third * scale
        val pf = p.toDouble() / sMag.toDouble()
        val qLabel = if (q >= 0) "+ j$q" else "− j${-q}"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Power Factor",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "An AC load absorbs complex power S (${if (q >= 0) "inductive" else "capacitive"}). Calculate its power factor:",
            parametersLine1 = "S = ($p $qLabel) VA",
            parametersLine2 = "V_rms = $vRms V",
            methodTitle = "POWER TRIANGLE DRILL",
            methodNote = "Compute |S| = √(P² + Q²) and divide P / |S|.",
            inputHint = "Enter power factor (e.g. 0.8)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = pf,
            unit = "",
            hints = listOf(
                "|S| = √($p² + ($q)²) = $sMag VA.",
                "pf = $p / $sMag."
            ),
            workedSteps = listOf(
                "1. Apparent power |S| = √($p² + ${t.second * scale}²) = $sMag VA.",
                "2. pf = P / |S| = $p / $sMag = ${ComplexNum.formatClean(pf)}."
            ),
            isProcedural = true
        )
    }
    1 -> {
        val vChoices = listOf(
            Pair(100, 0), Pair(120, 0), Pair(200, 0), Pair(240, 0),
            Pair(0, 100), Pair(80, 60), Pair(60, -80)
        )
        val vPick = vChoices.random()
        val vRe = vPick.first
        val vIm = vPick.second
        val iRe = listOf(2, 3, 4, 5).random()
        val iIm = listOf(-3, -2, -1, 1, 2).random()
        val p = (vRe * iRe + vIm * iIm).toDouble()
        val q = (vIm * iRe - vRe * iIm).toDouble()
        val vStr = ComplexNum(vRe.toDouble(), vIm.toDouble()).formatRect()
        val iSign = if (iIm >= 0) "+ j$iIm" else "− j${-iIm}"
        val iConjStr = "$iRe ${if (-iIm >= 0) "+ j${-iIm}" else "− j$iIm"}"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Complex Power S = V·I*",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "Compute complex power S = V_rms · I_rms* (in VA) for the RMS phasors:",
            parametersLine1 = "V_rms = ($vStr) V",
            parametersLine2 = "I_rms = ($iRe $iSign) A",
            methodTitle = "COMPLEX POWER DRILL",
            methodNote = "Negate the imaginary sign of I_rms to form I_rms*, then multiply by V_rms.",
            inputHint = "Enter S in VA (P + jQ)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(p, q),
            unit = "VA",
            hints = listOf(
                "I_rms* = ($iConjStr) A.",
                "Multiply ($vStr) × ($iConjStr)."
            ),
            workedSteps = listOf(
                "1. Take complex conjugate: I_rms* = ($iConjStr) A.",
                "2. S = ($vStr) · ($iConjStr) = ${ComplexNum(p, q).formatRect()} VA."
            ),
            isProcedural = true
        )
    }
    2 -> {
        val pairs = listOf(Pair(40, 10), Pair(60, 5), Pair(100, 25), Pair(120, 20), Pair(200, 25), Pair(80, 10))
        val pick = pairs.random()
        val vTh = pick.first
        val rTh = pick.second
        val xTh = listOf(4, 8, 12, 15, 20).random()
        val pMax = (vTh * vTh).toDouble() / (4.0 * rTh)
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Max Power Transfer",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "Source V_th with Z_th feeds an optimally matched load Z_L = Z_th*. Find maximum power P_max (in W):",
            parametersLine1 = "V_th = $vTh V (RMS),   Z_th = ($rTh + j$xTh) Ω",
            parametersLine2 = "Matched Load Z_L = ($rTh − j$xTh) Ω",
            methodTitle = "CONJUGATE MATCH DRILL",
            methodNote = "Reactances cancel when Z_L = Z_th*; total resistance is 2R_th and P_max = V_th² / (4 R_th).",
            inputHint = "Enter P_max in Watts",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = pMax,
            unit = "W",
            hints = listOf(
                "Imaginary parts +j$xTh and −j$xTh cancel completely.",
                "P_max = $vTh² / (4 × $rTh)."
            ),
            workedSteps = listOf(
                "1. With Z_L = Z_th*, net loop impedance is 2R_th = ${2 * rTh} Ω.",
                "2. P_max = $vTh² / (4 × $rTh) = ${ComplexNum.formatClean(pMax)} W."
            ),
            isProcedural = true
        )
    }
    3 -> {
        val rY = listOf(3, 4, 5, 6, 8, 10).random()
        val xSign = if (Random.nextBoolean()) 1 else -1
        val xY = listOf(2, 3, 4, 6, 8).random() * xSign
        val rD = 3 * rY
        val xD = 3 * xY
        val dStr = if (xD >= 0) "+ j$xD" else "− j${-xD}"
        val yStr = if (xY >= 0) "+ j$xY" else "− j${-xY}"
        val wyeToDelta = Random.nextBoolean()
        if (wyeToDelta) {
            CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural 3-Phase Δ ↔ Y",
                category = ProblemCategory.POWER,
                difficulty = Difficulty.INTERMEDIATE,
                topology = CircuitTopology.COMPLEX_LOAD,
                prompt = "A balanced 3-phase Y load has phase impedance Z_Y. Find the equivalent delta impedance Z_Δ (in Ω):",
                parametersLine1 = "Z_Y = ($rY $yStr) Ω per phase",
                parametersLine2 = "Balanced 3φ Y → Δ Equivalence",
                methodTitle = "THREE-PHASE Y–Δ DRILL",
                methodNote = "Multiply identical balanced wye leg impedance by 3: Z_Δ = 3 · Z_Y.",
                inputHint = "Enter Z_Δ in Ω (A + jB)",
                answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
                expectedComplex = ComplexNum(rD.toDouble(), xD.toDouble()),
                unit = "Ω",
                hints = listOf(
                    "For balanced equal wye branches, Z_Δ = 3 · Z_Y.",
                    "Multiply ($rY $yStr) by 3."
                ),
                workedSteps = listOf(
                    "1. Z_Δ = 3 · Z_Y = 3($rY $yStr) = ${ComplexNum(rD.toDouble(), xD.toDouble()).formatRect()} Ω."
                ),
                isProcedural = true
            )
        } else {
            CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural 3-Phase Δ ↔ Y",
                category = ProblemCategory.POWER,
                difficulty = Difficulty.INTERMEDIATE,
                topology = CircuitTopology.COMPLEX_LOAD,
                prompt = "A balanced 3-phase Δ load has phase impedance Z_Δ. Find the equivalent wye impedance Z_Y (in Ω):",
                parametersLine1 = "Z_Δ = ($rD $dStr) Ω per phase",
                parametersLine2 = "Balanced 3φ Δ → Y Equivalence",
                methodTitle = "THREE-PHASE Δ–Y DRILL",
                methodNote = "Divide identical balanced delta leg impedance by 3: Z_Y = Z_Δ / 3.",
                inputHint = "Enter Z_Y in Ω (A + jB)",
                answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
                expectedComplex = ComplexNum(rY.toDouble(), xY.toDouble()),
                unit = "Ω",
                hints = listOf(
                    "For balanced equal delta branches, Z_Y = Z_Δ / 3.",
                    "Divide real $rD and imaginary $xD by 3."
                ),
                workedSteps = listOf(
                    "1. Z_Y = Z_Δ / 3 = ($rD $dStr) / 3 = ${ComplexNum(rY.toDouble(), xY.toDouble()).formatRect()} Ω."
                ),
                isProcedural = true
            )
        }
    }
    4 -> {
        val vLine = listOf(200, 400, 480, 600).random()
        val iLine = listOf(10, 20, 25, 30, 40).random()
        val pKw = 0.0015 * vLine * iLine
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural 3-Phase Line Power",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "Find total balanced 3-phase active power P_3φ (in kW) for pf = cos(30°) = √3 / 2:",
            parametersLine1 = "V_L = $vLine V (RMS),   I_L = $iLine A (RMS)",
            parametersLine2 = "pf = √3 / 2 (30° lag)",
            methodTitle = "3-PHASE POWER DRILL",
            methodNote = "Use P_3φ = √3 · V_L · I_L · pf and divide by 1000 for kilowatts.",
            inputHint = "Enter P_3φ in kW",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = pKw,
            unit = "kW",
            hints = listOf(
                "√3 × (√3 / 2) = 1.5.",
                "P_3φ = 1.5 × $vLine × $iLine W."
            ),
            workedSteps = listOf(
                "1. P_3φ = √3 × $vLine × $iLine × (√3 / 2) = 1.5 × ${vLine * iLine} W.",
                "2. Convert to kW: ${vLine * iLine * 1.5 / 1000.0} ⇒ ${ComplexNum.formatClean(pKw)} kW."
            ),
            isProcedural = true
        )
    }
    5 -> {
        val quads = listOf(
            listOf(1, 2, 2, 3),
            listOf(2, 3, 6, 7),
            listOf(3, 4, 12, 13),
            listOf(1, 4, 8, 9),
            listOf(4, 4, 7, 9),
            listOf(2, 5, 14, 15)
        )
        val q = quads.random()
        val scale = listOf(1, 2, 3).random()
        val i0 = q[0] * scale
        val i1 = q[1] * scale
        val i2 = q[2] * scale
        val iTot = (q[3] * scale).toDouble()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Multi-Freq RMS",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "Compute total RMS current I_rms (in A) for this non-sinusoidal DC + AC harmonic waveform:",
            parametersLine1 = "i(t) = $i0 + ${i1}√2 cos(100t) + ${i2}√2 sin(300t) A",
            parametersLine2 = "I_rms = √(I₀² + I₁,rms² + I₃,rms²)",
            methodTitle = "HARMONIC RMS DRILL",
            methodNote = "Divide each sinusoidal peak by √2 to get RMS, then take √(I₀² + I₁² + I₃²).",
            inputHint = "Enter I_rms in A",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = iTot,
            unit = "A",
            hints = listOf(
                "Harmonic RMS terms are $i0 A, $i1 A, and $i2 A.",
                "Square and sum: ${i0 * i0} + ${i1 * i1} + ${i2 * i2} = ${(iTot * iTot).toInt()}."
            ),
            workedSteps = listOf(
                "1. RMS terms: I₀ = $i0 A, I₁ = $i1 A, I₃ = $i2 A.",
                "2. I_rms = √(${i0 * i0} + ${i1 * i1} + ${i2 * i2}) = √${(iTot * iTot).toInt()} = ${iTot.toInt()} A."
            ),
            isProcedural = true
        )
    }
    6 -> {
        val pKw = listOf(12, 24, 36, 48, 60).random()
        val pick = listOf(
            listOf("0.6", "0.8", 4.0 / 3.0, 0.75, "4/3", "3/4"),
            listOf("0.6", "1.0", 4.0 / 3.0, 0.0, "4/3", "0"),
            listOf("0.8", "1.0", 0.75, 0.0, "3/4", "0")
        ).random()
        val pf1 = pick[0] as String
        val pf2 = pick[1] as String
        val tan1 = pick[2] as Double
        val tan2 = pick[3] as Double
        val tan1Str = pick[4] as String
        val tan2Str = pick[5] as String
        val qC = pKw * (tan1 - tan2)
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Partial PFC (kVAR)",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "An inductive load drawing P = $pKw kW at pf₁ = $pf1 lag is corrected to pf₂ = $pf2 lag. Find required capacitor rating Q_C (in kVAR):",
            parametersLine1 = "P = $pKw kW,   Initial pf₁ = $pf1 lagging",
            parametersLine2 = "Target pf₂ = $pf2   (Q_C = P·(tan θ₁ − tan θ₂))",
            methodTitle = "REACTIVE POWER PFC DRILL",
            methodNote = "Convert pf₁ and pf₂ to tan θ = √(1 − pf²)/pf, then compute Q_C = P·(tan θ₁ − tan θ₂).",
            inputHint = "Enter Q_C in kVAR",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = qC,
            unit = "kVAR",
            hints = listOf(
                "tan θ₁ = $tan1Str, tan θ₂ = $tan2Str.",
                "Q_C = $pKw × ($tan1Str − $tan2Str)."
            ),
            workedSteps = listOf(
                "1. Q₁ = $pKw × ($tan1Str) = ${ComplexNum.formatClean(pKw * tan1)} kVAR; Q₂ = ${ComplexNum.formatClean(pKw * tan2)} kVAR.",
                "2. Q_C = ${ComplexNum.formatClean(qC)} kVAR."
            ),
            isProcedural = true
        )
    }
    else -> {
        val pick = listOf(
            listOf(8.0, 6.0, 10.0, 0.8, 36.87),
            listOf(6.0, 8.0, 10.0, 0.8, 36.87),
            listOf(8.0, 6.0, 10.0, 0.6, 53.13),
            listOf(12.0, 5.0, 13.0, 0.8, 36.87),
            listOf(24.0, 7.0, 25.0, 0.8, 36.87)
        ).random()
        val i1 = pick[0]; val i3 = pick[1]; val iRms = pick[2]; val cosTh = pick[3]; val thDeg = pick[4]
        val vRms = listOf(100, 120, 240).random()
        val pfTrue = (i1 / iRms) * cosTh
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Distortion Power Factor",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "A sinusoidal source feeds non-linear harmonic current i(t). Compute the true power factor pf_true = P / (V_rms·I_rms):",
            parametersLine1 = "v(t) = $vRms√2 cos(ωt) V,   i(t) = ${i1.toInt()}√2 cos(ωt − $thDeg°) + ${i3.toInt()}√2 cos(3ωt) A",
            parametersLine2 = "cos($thDeg°) = $cosTh   (pf_true = (I₁ / I_rms)·cos θ₁)",
            methodTitle = "DISTORTION PF DRILL",
            methodNote = "Multiply the fundamental displacement factor cos θ₁ by distortion factor I₁ / I_rms.",
            inputHint = "Enter scalar pf_true",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = pfTrue,
            unit = "",
            hints = listOf(
                "I_rms = √(${i1.toInt()}² + ${i3.toInt()}²) = ${iRms.toInt()} A.",
                "pf_true = (${i1.toInt()} / ${iRms.toInt()}) × $cosTh."
            ),
            workedSteps = listOf(
                "1. I_rms = ${iRms.toInt()} A ⇒ distortion factor I₁/I_rms = ${ComplexNum.formatClean(i1 / iRms)}.",
                "2. pf_true = ${ComplexNum.formatClean(pfTrue)}."
            ),
            isProcedural = true
        )
    }
}
