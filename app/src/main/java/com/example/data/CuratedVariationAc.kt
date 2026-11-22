package com.example.data

import com.example.model.AnswerKind
import com.example.model.CircuitProblem
import com.example.model.ComplexNum
import com.example.model.ProblemCategory
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.sqrt
import kotlin.random.Random

internal fun wrapCuratedVariation(
    base: CircuitProblem,
    prompt: String = base.prompt,
    parametersLine1: String,
    parametersLine2: String,
    methodTitle: String = base.methodTitle,
    methodNote: String = base.methodNote,
    inputHint: String = base.inputHint,
    expectedComplex: ComplexNum? = base.expectedComplex,
    expectedReal: Double? = base.expectedReal,
    hints: List<String>,
    workedSteps: List<String>
): CircuitProblem = CircuitProblem(
    id = "var_${base.id}_${nextDrillSequence()}",
    code = "Var · ${base.code}",
    title = base.title,
    category = base.category,
    difficulty = base.difficulty,
    topology = base.topology,
    prompt = prompt,
    parametersLine1 = parametersLine1,
    parametersLine2 = parametersLine2,
    methodTitle = methodTitle,
    methodNote = methodNote,
    inputHint = inputHint,
    answerKind = base.answerKind,
    expectedComplex = expectedComplex,
    expectedReal = expectedReal,
    tolerance = base.tolerance,
    unit = base.unit,
    hints = hints,
    workedSteps = workedSteps,
    isProcedural = true,
    sourceProblemId = base.id,
    sourceProblemCode = base.code
)

internal fun generateAcVariation(base: CircuitProblem): CircuitProblem? = when (base.id) {
    // ── IMPEDANCE (16) ──
    "p_100a" -> {
        val omega = listOf(200, 377, 500, 1000).random()
        val (thetaV, thetaISin) = listOf(
            35 to 80, 15 to 75, 40 to 100, -10 to 50, 30 to 45, 50 to 110
        ).filter { !(it.first == 20 && it.second == 65) }.random()
        val thetaICos = thetaISin - 90
        val diff = (thetaV - thetaICos).toDouble()
        val vSign = if (thetaV >= 0) "+ ${thetaV}°" else "− ${-thetaV}°"
        val icSign = if (thetaICos >= 0) "+ ${thetaICos}°" else "− ${-thetaICos}°"
        val vm = listOf(80, 100, 120, 150).random()
        val im = listOf(2, 4, 5, 8).random()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v(t) = $vm cos(${omega}t $vSign) V",
            parametersLine2 = "i(t) = $im sin(${omega}t + ${thetaISin}°) A",
            hints = listOf(
                "sin(${omega}t + ${thetaISin}°) = cos(${omega}t + ${thetaISin}° − 90°) = cos(${omega}t $icSign), so θ_i = ${thetaICos}°.",
                "θ = θ_v − θ_i = ${thetaV}° − (${thetaICos}°)."
            ),
            workedSteps = listOf(
                "1. Convert i(t) to cosine: sin(${omega}t + ${thetaISin}°) = cos(${omega}t $icSign) ⇒ θ_i = ${thetaICos}°.",
                "2. Impedance phase θ = θ_v − θ_i = ${thetaV}° − (${thetaICos}°) = ${diff.toInt()}°."
            ),
            expectedReal = diff
        )
    }

    "p_101a" -> {
        val (r, xL, zMag) = listOf(
            Triple(3, 4, 5), Triple(8, 6, 10), Triple(5, 12, 13), Triple(12, 16, 20), Triple(15, 20, 25)
        ).random()
        val omega = listOf(50, 100, 200, 250).random()
        val lH = xL.toDouble() / omega.toDouble()
        val lStr = ComplexNum.formatClean(lH)
        val z = ComplexNum(r.toDouble(), xL.toDouble())
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω, L = ${lStr}H",
            parametersLine2 = "ω = $omega rad/s",
            hints = listOf(
                "Inductive impedance is Z_L = jωL.",
                "X_L = $omega × $lStr = $xL Ω, so Z = $r + j$xL Ω (|Z| = $zMag Ω)."
            ),
            workedSteps = listOf(
                "1. X_L = ωL = $omega × $lStr = $xL Ω.",
                "2. Series sum: Z = $r + j$xL Ω (${z.formatPolar()})."
            ),
            expectedComplex = z
        )
    }

    "p_102b" -> {
        val (l1, l2, m) = listOf(
            Triple(30, 50, 10), Triple(45, 75, 20), Triple(50, 80, 15), Triple(25, 35, 12), Triple(60, 90, 25)
        ).random()
        val lEq = (l1 + l2 + 2 * m).toDouble()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "L₁ = $l1 mH,   L₂ = $l2 mH",
            parametersLine2 = "Mutual M = $m mH (series-aiding dots)",
            hints = listOf(
                "For series-aiding coils, mutual flux adds +2M.",
                "L_eq = $l1 + $l2 + 2($m) mH."
            ),
            workedSteps = listOf(
                "1. L_eq = L₁ + L₂ + 2M = $l1 + $l2 + ${2 * m}.",
                "2. L_eq = ${lEq.toInt()} mH."
            ),
            expectedReal = lEq
        )
    }

    "p_104b" -> {
        val (r, xC, omega, cUf) = listOf(
            listOf(30, 30, 100, 333.333),
            listOf(25, 25, 200, 200),
            listOf(50, 50, 100, 200),
            listOf(20, 40, 100, 250),
            listOf(60, 80, 50, 250),
            listOf(15, 20, 500, 100)
        ).filter { !(it[0] == 40 && it[1] == 40) }.random()
        val rD = r.toDouble()
        val xCD = xC.toDouble()
        val omegaI = omega.toInt()
        val cStr = ComplexNum.formatClean(cUf.toDouble())
        val z = ComplexNum(rD, -xCD)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r.toInt()}Ω, C = ${cStr}μF",
            parametersLine2 = "ω = $omegaI rad/s",
            hints = listOf(
                "Convert $cStr μF = $cStr × 10⁻⁶ F.",
                "X_C = 1 / ($omegaI × ${cStr}×10⁻⁶) = ${xC.toInt()} Ω, giving Z = ${r.toInt()} − j${xC.toInt()} Ω."
            ),
            workedSteps = listOf(
                "1. X_C = 1/(ωC) = ${xC.toInt()} Ω.",
                "2. Z = ${r.toInt()} − j${xC.toInt()} Ω (${z.formatPolar()})."
            ),
            expectedComplex = z
        )
    }

    "p_106b" -> {
        val r1 = listOf(2, 4, 6).random()
        val r2 = 10 - r1
        val x = listOf(2, 3, 5, 6).random()
        val scale = listOf(1, 2, 3).random()
        val iSrc = 10 * scale
        val ans = ComplexNum((scale * r2).toDouble(), -(scale * x).toDouble())
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "I_s = $iSrc∠0° A,   Z₁ = ($r1 + j$x) Ω",
            parametersLine2 = "Z₂ = ($r2 − j$x) Ω  (parallel with Z₁)",
            hints = listOf(
                "Sum the two impedances: Z₁ + Z₂ = ($r1 + $r2) + j($x − $x) = 10 Ω.",
                "I₁ = $iSrc × ($r2 − j$x) / 10 = $scale × ($r2 − j$x)."
            ),
            workedSteps = listOf(
                "1. Z₁ + Z₂ = 10 + j0 Ω.",
                "2. I₁ = $iSrc · ($r2 − j$x) / 10 = ${ans.formatRect()} A (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_108c" -> {
        val r = listOf(8, 12, 16, 20, 30).random()
        val xC = r
        val half = r / 2.0
        val ans = ComplexNum(half, -half)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω || Z_C = -j${xC}Ω",
            parametersLine2 = "Y = 1/$r + j/$xC",
            hints = listOf(
                "Z_eq = ($r × -j$xC) / ($r - j$xC) = -j$xC / (1 - j).",
                "Multiply numerator and denominator by (1 + j)."
            ),
            workedSteps = listOf(
                "1. Admittance Y = 1/$r + j/$xC.",
                "2. Z_eq = 1 / Y = ${ComplexNum.formatClean(half)} − j${ComplexNum.formatClean(half)} Ω."
            ),
            expectedComplex = ans
        )
    }

    "p_112d" -> {
        val r = listOf(6, 8, 10, 15, 20).random()
        val omega = listOf(50, 100, 200).random()
        val xL = listOf(12, 16, 20, 24).random()
        val xC = listOf(4, 8, 10, 15, 30).filter { it != xL }.random()
        val lH = xL.toDouble() / omega
        val cMf = 1000.0 / (omega * xC)
        val xNet = (xL - xC).toDouble()
        val ans = ComplexNum(r.toDouble(), xNet)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω, L = ${ComplexNum.formatClean(lH)}H, C = ${ComplexNum.formatClean(cMf)}mF",
            parametersLine2 = "ω = $omega rad/s",
            hints = listOf(
                "X_L = $omega × ${ComplexNum.formatClean(lH)} = $xL Ω.",
                "X_C = 1 / ($omega × ${ComplexNum.formatClean(cMf)}×10⁻³) = $xC Ω, net X = $xL − $xC = ${xNet.toInt()} Ω."
            ),
            workedSteps = listOf(
                "1. X_L = $xL Ω, X_C = $xC Ω.",
                "2. Z = ${ans.formatRect()} Ω (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_124g" -> {
        val (a, b) = listOf(
            3 to 3, 5 to 5, 6 to 6, 8 to 8, 3 to 6, 4 to 8, 5 to 10
        ).random()
        val zThRe = (a * a + b * b) / (2.0 * a)
        val ans = ComplexNum(zThRe, 0.0)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Z₁ = ($a + j$b) Ω   ||   Z₂ = ($a − j$b) Ω",
            parametersLine2 = "Z_th = (Z₁ · Z₂) / (Z₁ + Z₂)",
            inputHint = "Enter Z_th in Ω (e.g. ${ComplexNum.formatClean(zThRe)})",
            hints = listOf(
                "Z₁ · Z₂ = ($a + j$b)($a − j$b) = $a² + $b² = ${a * a + b * b} Ω².",
                "Z₁ + Z₂ = ${2 * a} Ω."
            ),
            workedSteps = listOf(
                "1. Product = ${a * a + b * b}, Sum = ${2 * a}.",
                "2. Z_th = ${a * a + b * b} / ${2 * a} = ${ComplexNum.formatClean(zThRe)} Ω."
            ),
            expectedComplex = ans
        )
    }

    "p_115e" -> {
        val (r, xL, vIn) = listOf(
            Triple(4, 3, 25), Triple(6, 8, 50), Triple(8, 6, 50), Triple(5, 12, 169), Triple(3, 4, 50)
        ).random()
        val den = (r * r + xL * xL).toDouble()
        val re = vIn * xL * xL / den
        val im = vIn * r * xL / den
        val ans = ComplexNum(re, im)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_in = $vIn∠0° V, R = ${r}Ω",
            parametersLine2 = "Z_L = j${xL}Ω (series with R)",
            hints = listOf(
                "V_L = $vIn × (j$xL) / ($r + j$xL).",
                "Multiply by ($r − j$xL); |Z|² = ${den.toInt()}."
            ),
            workedSteps = listOf(
                "1. V_L = j${vIn * xL} / ($r + j$xL) = j${vIn * xL}($r − j$xL) / ${den.toInt()}.",
                "2. V_L = ${ans.formatRect()} V (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_119f" -> {
        val r = listOf(6, 8, 12, 16, 20, 30).random()
        val half = r / 2.0
        val ans = ComplexNum(half, half)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω || Z_L = j${r}Ω",
            parametersLine2 = "Y = 1/$r − j/$r S",
            hints = listOf(
                "Z_eq = ($r × j$r) / ($r + j$r) = j$r / (1 + j).",
                "Multiply numerator and denominator by (1 − j)."
            ),
            workedSteps = listOf(
                "1. Z_eq = j$r(1 − j)/2 = ${ComplexNum.formatClean(half)} + j${ComplexNum.formatClean(half)} Ω (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_124f" -> {
        val (rInK, rFK, cNf, omega) = listOf(
            listOf(5, 10, 200, 500),
            listOf(10, 40, 100, 250),
            listOf(10, 30, 50, 666.667),
            listOf(4, 20, 100, 500),
            listOf(5, 20, 100, 500)
        ).filter { !(it[0] == 10 && it[1] == 20) }.random()
        val rIn = rInK.toDouble()
        val rF = rFK.toDouble()
        val xCfK = 1_000_000.0 / (omega.toDouble() * cNf.toDouble()) // in kOhms because C in nF -> 10^9 / 10^3 = 10^6
        val yRe = 1.0 / rF
        val yIm = 1.0 / xCfK
        val den = yRe * yRe + yIm * yIm
        val zfRe = yRe / den
        val zfIm = -yIm / den
        val ans = ComplexNum(-zfRe / rIn, -zfIm / rIn)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R_in = ${rIn.toInt()} kΩ,   R_f = ${rF.toInt()} kΩ ∥ C_f = ${cNf.toInt()} nF",
            parametersLine2 = "ω = ${ComplexNum.formatClean(omega.toDouble())} rad/s",
            hints = listOf(
                "|X_Cf| = 1 / (ω·C_f) = ${ComplexNum.formatClean(xCfK)} kΩ.",
                "Compute parallel Z_f = R_f ∥ (−jX_Cf), then A_v = −Z_f / ${rIn.toInt()} kΩ."
            ),
            workedSteps = listOf(
                "1. X_Cf = −j${ComplexNum.formatClean(xCfK)} kΩ.",
                "2. Z_f = ${ComplexNum(zfRe, zfIm).formatRect()} kΩ.",
                "3. A_v = −Z_f / ${rIn.toInt()} kΩ = ${ans.formatRect()} V/V (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_128h" -> {
        val (n1, n2) = listOf(3 to 1, 4 to 1, 1 to 2, 5 to 1).random()
        val aSq = (n1.toDouble() / n2) * (n1.toDouble() / n2)
        val rL = if (n1 < n2) listOf(8, 12, 16, 20).random() else listOf(2, 3, 4, 6).random()
        val xMag = if (n1 < n2) listOf(8, 12, 16).random() else listOf(2, 3, 4, 5).random()
        val xL = if (Random.nextBoolean()) xMag else -xMag
        val ans = ComplexNum(aSq * rL, aSq * xL)
        val xSignStr = if (xL >= 0) "+ j$xL" else "− j${-xL}"
        wrapCuratedVariation(
            base = base,
            prompt = "An ideal transformer with turns ratio N₁:N₂ = $n1:$n2 feeds load Z_L. Find primary input impedance Z_in:",
            parametersLine1 = "N₁ : N₂ = $n1 : $n2  (a = N₁/N₂ = ${ComplexNum.formatClean(n1.toDouble() / n2)})",
            parametersLine2 = "Z_L = ($rL $xSignStr) Ω",
            inputHint = "Enter Z_in in Ω (e.g. ${ans.formatRect()})",
            hints = listOf(
                "Turns ratio a = $n1/$n2, so impedance scales by a² = ${ComplexNum.formatClean(aSq)}.",
                "Z_in = ${ComplexNum.formatClean(aSq)} × ($rL $xSignStr) Ω."
            ),
            workedSteps = listOf(
                "1. Scaling factor a² = ($n1 / $n2)² = ${ComplexNum.formatClean(aSq)}.",
                "2. Z_in = ${ans.formatRect()} Ω."
            ),
            expectedComplex = ans
        )
    }

    "p_140k" -> {
        val (r, v1, iS) = listOf(
            Triple(5, 30, 6), Triple(2, 12, 6), Triple(8, 40, 5), Triple(10, 20, 4), Triple(4, 24, 3)
        ).random()
        // KCL: (V_A - V1)/R + V_A/(jR) = j*iS  => V_A(1 - j) = V1 + j(R*iS) => V_A = (V1 + j*R*iS)*(1+j)/2
        val rhsRe = v1.toDouble()
        val rhsIm = (r * iS).toDouble()
        val ans = ComplexNum((rhsRe - rhsIm) / 2.0, (rhsRe + rhsIm) / 2.0)
        wrapCuratedVariation(
            base = base,
            prompt = "Find node phasor voltage V_A driven by V₁ through R = ${r}Ω plus current source I_s entering node A:",
            parametersLine1 = "V₁ = $v1∠0° V via R = ${r}Ω to A",
            parametersLine2 = "I_s = j$iS A entering A; Z_L = j${r}Ω (A to GND)",
            hints = listOf(
                "(V_A − $v1)/$r + V_A/(j$r) = j$iS.",
                "Multiply by $r: V_A(1 − j) = $v1 + j${r * iS}."
            ),
            workedSteps = listOf(
                "1. KCL at node A: V_A(1 − j) = $v1 + j${r * iS}.",
                "2. V_A = ($v1 + j${r * iS})(1 + j)/2 = ${ans.formatRect()} V (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_132i" -> {
        val r = listOf(10, 20, 40, 50).random()
        val l1 = listOf(50, 60, 80).random()
        val l2 = listOf(30, 40, 50).random()
        val m = listOf(10, 15, 20).random()
        val omega = listOf(500, 1000, 2000).random()
        val lEq = l1 + l2 - 2 * m
        val xL = omega * (lEq / 1000.0)
        val ans = ComplexNum(r.toDouble(), xL)
        wrapCuratedVariation(
            base = base,
            prompt = "Two magnetically coupled coils (dots opposing) sit in series with R. Find Z at ω = $omega rad/s:",
            parametersLine1 = "R = ${r}Ω, L₁ = ${l1}mH, L₂ = ${l2}mH",
            parametersLine2 = "M = ${m}mH (series opposing), ω = $omega rad/s",
            hints = listOf(
                "L_eq = ${l1}mH + ${l2}mH − 2(${m}mH) = $lEq mH.",
                "Reactance X_L = $omega × ${ComplexNum.formatClean(lEq / 1000.0)} = ${ComplexNum.formatClean(xL)} Ω."
            ),
            workedSteps = listOf(
                "1. L_eq = $l1 + $l2 − ${2 * m} = $lEq mH = ${ComplexNum.formatClean(lEq / 1000.0)} H.",
                "2. X_L = ${ComplexNum.formatClean(xL)} Ω ⇒ Z = ${ans.formatRect()} Ω (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_136j" -> {
        val (r, x, k) = listOf(
            Triple(3, 4, 4), Triple(4, 3, 2), Triple(6, 8, 1), Triple(8, 6, 2), Triple(5, 12, 1)
        ).random()
        val magSq = r * r + x * x
        val vTh = k * magSq
        val ans = ComplexNum((k * r).toDouble(), -(k * x).toDouble())
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_th = $vTh∠0° V, Z_th = ($r + j$x) Ω",
            parametersLine2 = "Solve I_N = V_th / Z_th",
            hints = listOf(
                "Multiply numerator and denominator by Z_th* = $r − j$x.",
                "Denominator |Z_th|² = $r² + $x² = $magSq."
            ),
            workedSteps = listOf(
                "1. I_N = $vTh($r − j$x) / $magSq = $k($r − j$x).",
                "2. I_N = ${ans.formatRect()} A (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_144h" -> {
        val r = listOf(4, 8, 10, 12).random()
        val xc = listOf(5, 6, 10, 12).random()
        val gain = listOf(3, 4, 5).random()
        val ans = ComplexNum(r.toDouble(), -xc * (1.0 + gain))
        wrapCuratedVariation(
            base = base,
            prompt = "A test source V_t drives series R = ${r}Ω, Z_C = −j${xc}Ω, and dependent drop V_d = ${gain}V_C in loop KVL. Find Z_th = V_t/I_t:",
            parametersLine1 = "R = ${r}Ω,   Z_C = −j${xc}Ω,   V_C = I_t·Z_C",
            parametersLine2 = "Loop KVL: V_t = I_t·R + V_C + ${gain}V_C",
            hints = listOf(
                "Combine V_C + ${gain}V_C = ${gain + 1}V_C = ${gain + 1}(−j$xc I_t).",
                "V_t = I_t($r − j${xc * (gain + 1)}), so Z_th = V_t / I_t."
            ),
            workedSteps = listOf(
                "1. V_C = I_t(−j$xc).",
                "2. KVL: V_t = ($r − j${xc * (gain + 1)}) I_t ⇒ Z_th = ${ans.formatRect()} Ω."
            ),
            expectedComplex = ans
        )
    }

    "p_116f" -> {
        val (r, xc, vS) = listOf(
            Triple(3, 4, 25),
            Triple(4, 3, 25),
            Triple(8, 6, 50),
            Triple(5, 12, 169),
            Triple(12, 5, 169),
            Triple(6, 8, 50)
        ).random()
        // V_th = vS * (-j*xc) / (r - j*xc) = -j*vS*xc*(r + j*xc) / (r^2 + xc^2)
        val den = (r * r + xc * xc).toDouble()
        val ans = ComplexNum(vS * xc * xc / den, -vS * r * xc / den)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_s = $vS∠0° V,   Z₁ = R = $r Ω",
            parametersLine2 = "Z₂ = −j$xc Ω (open-circuit shunt C)",
            hints = listOf(
                "V_th = $vS × (−j$xc) / ($r − j$xc).",
                "Multiply by ($r + j$xc); |$r − j$xc|² = ${den.toInt()}."
            ),
            workedSteps = listOf(
                "1. V_th = −j${vS * xc}($r + j$xc) / ${den.toInt()}.",
                "2. V_th = ${ans.formatRect()} V (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_146i" -> {
        val (r1, x1, r2, x2, wM) = listOf(
            listOf(2, 8, 3, 4, 10),
            listOf(5, 12, 6, 8, 10),
            listOf(3, 9, 4, 3, 5),
            listOf(6, 15, 8, 6, 10),
            listOf(4, 14, 5, 12, 13)
        ).random()
        val wMSq = (wM * wM).toDouble()
        val den = (r2 * r2 + x2 * x2).toDouble()
        val refRe = wMSq * r2 / den
        val refIm = -wMSq * x2 / den
        val ans = ComplexNum(r1 + refRe, x1 + refIm)
        val ref = ComplexNum(refRe, refIm)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Z₁₁ = ($r1 + j$x1) Ω,   Z₂₂ = ($r2 + j$x2) Ω",
            parametersLine2 = "Mutual reactance ωM = $wM Ω  (Z_ref = (ωM)² / Z₂₂)",
            hints = listOf(
                "(ωM)² = ${wM * wM}, so Z_ref = ${wM * wM} / ($r2 + j$x2) = ${ref.formatRect()} Ω.",
                "Add Z₁₁ = ($r1 + j$x1) Ω."
            ),
            workedSteps = listOf(
                "1. Reflected impedance: Z_ref = ${wM * wM}($r2 − j$x2) / ${den.toInt()} = ${ref.formatRect()} Ω.",
                "2. Z_in = ($r1 + j$x1) + (${ref.formatRect()}) = ${ans.formatRect()} Ω."
            ),
            expectedComplex = ans
        )
    }

    // ── ADMITTANCE (12) ──
    "p_501a" -> {
        val (r, x) = listOf(
            3 to 4, 4 to 3, 4 to -3, 6 to 8, 6 to -8, 8 to 6, 8 to -6
        ).random()
        val magSq = (r * r + x * x).toDouble()
        val ans = ComplexNum(r / magSq, -x / magSq)
        val zStr = if (x >= 0) "$r + j$x" else "$r − j${-x}"
        val conjStr = if (x >= 0) "$r − j$x" else "$r + j${-x}"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Z = ($zStr) Ω",
            parametersLine2 = "Y = G + jB = 1 / Z",
            methodNote = "Rationalize 1 / ($zStr) by multiplying by the conjugate ($conjStr).",
            hints = listOf(
                "|Z|² = ${r * r} + ${x * x} = ${magSq.toInt()} Ω².",
                "Y = ($conjStr) / ${magSq.toInt()}."
            ),
            workedSteps = listOf(
                "1. Y = ($conjStr) / ${magSq.toInt()} = ${ans.formatRect()} S (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_504b" -> {
        val (r, cUf, omega) = listOf(
            Triple(10, 100, 1000), Triple(25, 40, 1000), Triple(40, 25, 1000), Triple(50, 50, 1000), Triple(20, 100, 500)
        ).random()
        val g = 1.0 / r
        val b = omega * cUf * 1e-6
        val ans = ComplexNum(g, b)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω || C = ${cUf}μF",
            parametersLine2 = "ω = $omega rad/s",
            hints = listOf(
                "G = 1 / $r = ${ComplexNum.formatClean(g)} S.",
                "B_C = ω·C = $omega × $cUf×10⁻⁶ = ${ComplexNum.formatClean(b)} S."
            ),
            workedSteps = listOf(
                "1. G = ${ComplexNum.formatClean(g)} S, B_C = ${ComplexNum.formatClean(b)} S.",
                "2. Y = ${ans.formatRect()} S (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_510d" -> {
        val (zMag, deg, cosVal) = listOf(
            Triple(50, 36.87, 0.8),
            Triple(100, 60.0, 0.5),
            Triple(40, 60.0, 0.5),
            Triple(60, 53.13, 0.6),
            Triple(80, 36.87, 0.8),
            Triple(25, 53.13, 0.6)
        ).random()
        val rs = zMag * cosVal
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "|Z| = $zMag Ω,  θ = +$deg°",
            parametersLine2 = "f_test = 159.15 Hz (ω = 1000 rad/s)",
            hints = listOf(
                "cos($deg°) ≈ ${ComplexNum.formatClean(cosVal)}.",
                "R_s = $zMag × ${ComplexNum.formatClean(cosVal)}."
            ),
            workedSteps = listOf(
                "1. R_s = |Z| cos(θ) = $zMag × ${ComplexNum.formatClean(cosVal)} = ${ComplexNum.formatClean(rs)} Ω."
            ),
            expectedReal = rs
        )
    }

    "p_507c" -> {
        val (lMh, omega) = listOf(
            50 to 400, 20 to 500, 10 to 500, 25 to 800, 40 to 500, 100 to 200
        ).random()
        val xl = omega * (lMh / 1000.0)
        val bl = -1.0 / xl
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "L = $lMh mH (ideal coil)",
            parametersLine2 = "ω = $omega rad/s",
            hints = listOf(
                "X_L = $omega × ${lMh / 1000.0} = ${ComplexNum.formatClean(xl)} Ω.",
                "Y_L = 1/(j${ComplexNum.formatClean(xl)}) = j(${ComplexNum.formatClean(bl)}) S."
            ),
            workedSteps = listOf(
                "1. X_L = ωL = ${ComplexNum.formatClean(xl)} Ω.",
                "2. B_L = −1/X_L = ${ComplexNum.formatClean(bl)} S."
            ),
            expectedReal = bl
        )
    }

    "p_508c" -> {
        val (rK, cNf) = listOf(
            2 to 250, 4 to 250, 10 to 50, 2 to 500, 1 to 500, 8 to 125
        ).random()
        val omega0 = 1_000_000.0 / (rK * cNf)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R₁ = R₂ = $rK kΩ",
            parametersLine2 = "C₁ = C₂ = $cNf nF  (series & parallel arms)",
            hints = listOf(
                "Convert R = ${rK * 1000} Ω and C = $cNf × 10⁻⁹ F.",
                "ω₀ = 1 / (R·C) = ${ComplexNum.formatClean(omega0)} rad/s."
            ),
            workedSteps = listOf(
                "1. R·C = ${ComplexNum.formatClean(rK * cNf * 1e-6)} s.",
                "2. ω₀ = 1 / (R·C) = ${ComplexNum.formatClean(omega0)} rad/s."
            ),
            expectedReal = omega0
        )
    }

    "p_514e" -> {
        val omega = listOf(500, 1000, 2000).random()
        val lMh = listOf(15, 20, 25, 30, 50, 60).random()
        val rs = listOf(10, 15, 20, 25).random()
        val xs = (omega * lMh / 1000.0).toInt()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Z = ($rs + j$xs) Ω",
            parametersLine2 = "ω = $omega rad/s",
            hints = listOf(
                "Inductive reactance X_s = $xs Ω = ω·L_s.",
                "L_s = $xs / $omega H; multiply by 1000 for mH."
            ),
            workedSteps = listOf(
                "1. X_s = $xs Ω.",
                "2. L_s = $xs / $omega = ${ComplexNum.formatClean(lMh / 1000.0)} H = $lMh mH."
            ),
            expectedReal = lMh.toDouble()
        )
    }

    "p_518f" -> {
        val (esr, xc) = listOf(
            3 to 100, 4 to 200, 5 to 100, 1 to 200, 6 to 150, 8 to 200
        ).random()
        val d = esr.toDouble() / xc
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "ESR (R_s) = $esr Ω",
            parametersLine2 = "|X_C| = $xc Ω at test frequency",
            hints = listOf("D = tan(δ) = ESR / |X_C|.", "Compute $esr / $xc."),
            workedSteps = listOf("1. D = $esr / $xc = ${ComplexNum.formatClean(d)}."),
            expectedReal = d
        )
    }

    "p_522g" -> {
        val (rs, q) = listOf(
            5 to 3, 5 to 4, 8 to 2, 10 to 4, 20 to 2, 12 to 3
        ).random()
        val xs = rs * q
        val rp = (rs * (1 + q * q)).toDouble()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R_s = $rs Ω,  X_s = $xs Ω",
            parametersLine2 = "Quality Factor Q = X_s / R_s = $q",
            hints = listOf(
                "Q = $xs / $rs = $q; use R_p = R_s(1 + Q²).",
                "R_p = $rs × (1 + ${q * q})."
            ),
            workedSteps = listOf(
                "1. Q = $q.",
                "2. R_p = $rs(1 + $q²) = ${rp.toInt()} Ω."
            ),
            expectedReal = rp
        )
    }

    "p_526h" -> {
        val (g, bc, bl) = listOf(
            Triple(0.3, 0.2, -0.6),
            Triple(0.4, 0.5, -0.2),
            Triple(0.6, 1.0, -0.2),
            Triple(0.4, 0.1, -0.4),
            Triple(0.15, 0.3, -0.1)
        ).random()
        val b = bc + bl
        val magSq = g * g + b * b
        val ans = ComplexNum(g / magSq, -b / magSq)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "G = $g S,  B_C = +$bc S",
            parametersLine2 = "B_L = $bl S",
            hints = listOf(
                "Net susceptance B = $bc + ($bl) = ${ComplexNum.formatClean(b)} S.",
                "|Y|² = ${ComplexNum.formatClean(magSq)}, Z = (${ComplexNum.formatClean(g)} − j(${ComplexNum.formatClean(b)})) / ${ComplexNum.formatClean(magSq)}."
            ),
            workedSteps = listOf(
                "1. Y = ${ComplexNum(g, b).formatRect()} S.",
                "2. Z = 1 / Y = ${ans.formatRect()} Ω (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_529i" -> {
        val (g, omega, cpUf) = listOf(
            Triple(0.02, 1000, 40),
            Triple(0.05, 500, 100),
            Triple(0.01, 2000, 25),
            Triple(0.04, 1000, 80),
            Triple(0.02, 2000, 20)
        ).random()
        val bp = omega * cpUf * 1e-6
        wrapCuratedVariation(
            base = base,
            prompt = "An LCR meter in C_p–G parallel mode measures complex admittance Y at ω = $omega rad/s. Find parallel capacitance C_p (in μF):",
            parametersLine1 = "Y = (${ComplexNum.formatClean(g)} + j${ComplexNum.formatClean(bp)}) S",
            parametersLine2 = "Test Frequency ω = $omega rad/s",
            hints = listOf(
                "B_p = Im(Y) = ${ComplexNum.formatClean(bp)} S = ω C_p.",
                "C_p = ${ComplexNum.formatClean(bp)} / $omega F → multiply by 10⁶ for μF."
            ),
            workedSteps = listOf(
                "1. B_p = ${ComplexNum.formatClean(bp)} S.",
                "2. C_p = ${ComplexNum.formatClean(bp)} / $omega = $cpUf μF."
            ),
            expectedReal = cpUf.toDouble()
        )
    }

    "p_528i" -> {
        val (lNh, cNf, wMrad) = listOf(
            Triple(10, 10, 100.0),
            Triple(25, 10, 63.246),
            Triple(25, 40, 31.623),
            Triple(10, 40, 50.0),
            Triple(16, 25, 50.0),
            Triple(50, 20, 31.623),
            Triple(100, 10, 31.623),
            Triple(25, 16, 50.0),
            Triple(4, 25, 100.0)
        ).filter { !(it.first == 40 && it.second == 10) }.random()
        val exactW = 1000.0 / sqrt(lNh.toDouble() * cNf.toDouble()) // 1 / sqrt(nH * nF) = 10^9 rad/s = 1000 Mrad/s
        wrapCuratedVariation(
            base = base,
            prompt = "A capacitor has parasitic ESL = $lNh nH in series with C = $cNf nF and ESR = 0.05 Ω. Find ω_SRF (in Mrad/s):",
            parametersLine1 = "C = $cNf nF,   ESL (L_s) = $lNh nH",
            parametersLine2 = "ESR = 0.05 Ω  (series parasitic model)",
            hints = listOf(
                "L_s·C = $lNh×10⁻⁹ × $cNf×10⁻⁹ = ${lNh * cNf} × 10⁻¹⁸ s².",
                "ω_SRF = 1 / √(L_s C) in rad/s, then divide by 10⁶ for Mrad/s."
            ),
            workedSteps = listOf(
                "1. √(L_s C) = ${ComplexNum.formatClean(sqrt(lNh.toDouble() * cNf))} × 10⁻⁹ s.",
                "2. ω_SRF = ${ComplexNum.formatClean(exactW)} Mrad/s."
            ),
            expectedReal = exactW
        )
    }

    "p_533j" -> {
        val (r2, r3, c1Uf) = listOf(
            Triple(200, 500, 0.5),
            Triple(400, 1000, 0.3),
            Triple(500, 1000, 0.25),
            Triple(250, 800, 0.5),
            Triple(100, 1000, 0.6)
        ).random()
        val r1 = listOf(400, 500, 800).random()
        val lMh = r2.toDouble() * r3 * c1Uf / 1000.0
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Arm 1: R₁ = ${r1}Ω ∥ C₁ = $c1Uf μF",
            parametersLine2 = "Bridge Arms: R₂ = ${r2}Ω, R₃ = ${r3}Ω",
            hints = listOf(
                "At bridge balance, Z_x = R₂·R₃·Y₁ = R₂R₃/R₁ + jωR₂R₃C₁.",
                "L_x = $r2 × $r3 × ${c1Uf}×10⁻⁶ H."
            ),
            workedSteps = listOf(
                "1. L_x = R₂ · R₃ · C₁ = $r2 × $r3 × $c1Uf μF = ${ComplexNum.formatClean(lMh)} mH."
            ),
            expectedReal = lMh
        )
    }

    "p_523g" -> {
        val (rs, lsMh, q) = listOf(
            Triple(10, 20, 2),
            Triple(20, 60, 2),
            Triple(12, 90, 3),
            Triple(8, 32, 4),
            Triple(10, 50, 5)
        ).random()
        val lp = lsMh * (1.0 + 1.0 / (q * q))
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Series mode: R_s = $rs Ω,   L_s = $lsMh mH",
            parametersLine2 = "Quality factor Q = X_s / R_s = $q  (L_p = L_s(1 + 1/Q²))",
            hints = listOf(
                "Q² = ${q * q}, so 1 + 1/Q² = ${ComplexNum.formatClean(1.0 + 1.0 / (q * q))}.",
                "L_p = $lsMh × ${ComplexNum.formatClean(1.0 + 1.0 / (q * q))} mH."
            ),
            workedSteps = listOf(
                "1. 1 + 1/Q² = 1 + 1/${q * q} = ${ComplexNum.formatClean(1.0 + 1.0 / (q * q))}.",
                "2. L_p = $lsMh × ${ComplexNum.formatClean(1.0 + 1.0 / (q * q))} = ${ComplexNum.formatClean(lp)} mH."
            ),
            expectedReal = lp
        )
    }

    "p_535k" -> {
        val (y11Re, y11Im, negProd, sumRe, sumIm) = listOf(
            listOf(6.0, 3.0, 10.0, 3.0, 1.0), // -y12y21 = 10 / (3 + j1) = 3 - j1 -> Yin = 9 + j2
            listOf(5.0, 2.0, 5.0, 2.0, 1.0),  // 5 / (2 + j1) = 2 - j1 -> Yin = 7 + j1
            listOf(4.0, 3.0, 10.0, 1.0, 3.0), // 10 / (1 + j3) = 1 - j3 -> Yin = 5 + j0
            listOf(6.0, -2.0, 20.0, 2.0, 4.0),// 20 / (2 + j4) = 2 - j4 -> Yin = 8 - j6
            listOf(8.0, 4.0, 17.0, 4.0, -1.0) // 17 / (4 - j1) = 4 + j1 -> Yin = 12 + j5
        ).random()
        // Wait: Y_in = y11 - y12*y21 / (y22 + Y_L) = y11 + (-y12*y21) / (sumRe + j*sumIm)
        val y22Re = sumRe - 1.0
        val y22Im = sumIm - 2.0
        val ylRe = 1.0
        val ylIm = 2.0
        val magSq = sumRe * sumRe + sumIm * sumIm
        val fbRe = negProd * sumRe / magSq
        val fbIm = -negProd * sumIm / magSq
        val ans = ComplexNum(y11Re + fbRe, y11Im + fbIm)
        val y11Str = ComplexNum(y11Re, y11Im).formatRect()
        val y22Str = ComplexNum(y22Re, y22Im).formatRect()
        val ylStr = ComplexNum(ylRe, ylIm).formatRect()
        val sumStr = ComplexNum(sumRe, sumIm).formatRect()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "y₁₁ = $y11Str mS,   y₂₂ = $y22Str mS,   y₁₂ = −1 mS,   y₂₁ = ${negProd.toInt()} mS",
            parametersLine2 = "Load Y_L = $ylStr mS   (Y_in = y₁₁ − y₁₂y₂₁ / (y₂₂ + Y_L))",
            hints = listOf(
                "y₂₂ + Y_L = $sumStr mS.",
                "−y₁₂y₂₁ / ($sumStr) = ${negProd.toInt()} / ($sumStr) = ${ComplexNum(fbRe, fbIm).formatRect()} mS."
            ),
            workedSteps = listOf(
                "1. y₂₂ + Y_L = $sumStr mS.",
                "2. Reflected term = ${negProd.toInt()} / ($sumStr) = ${ComplexNum(fbRe, fbIm).formatRect()} mS.",
                "3. Y_in = ($y11Str) + (${ComplexNum(fbRe, fbIm).formatRect()}) = ${ans.formatRect()} mS."
            ),
            expectedComplex = ans
        )
    }

    // ── RESONANCE (10) ──
    "p_201a" -> {
        val (r, lH, cUf) = listOf(
            Triple(5, 0.1, 10),
            Triple(10, 0.4, 10),
            Triple(20, 0.4, 10),
            Triple(10, 0.2, 5),
            Triple(8, 0.16, 10),
            Triple(25, 0.5, 5)
        ).random()
        val q = (1.0 / r) * sqrt(lH / (cUf * 1e-6))
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω, L = ${lH}H, C = ${cUf}μF",
            parametersLine2 = "V_s = 100 V rms (variable ω)",
            hints = listOf(
                "Use Q = (1/R) × √(L/C).",
                "√(L/C) = ${ComplexNum.formatClean(sqrt(lH / (cUf * 1e-6)))} Ω."
            ),
            workedSteps = listOf(
                "1. √(L/C) = ${ComplexNum.formatClean(sqrt(lH / (cUf * 1e-6)))} Ω.",
                "2. Q = (1/$r)√(L/C) = ${ComplexNum.formatClean(q)}."
            ),
            expectedReal = q
        )
    }

    "p_212d" -> {
        val (f0, q) = listOf(
            2000 to 40, 5000 to 50, 1200 to 15, 3000 to 25, 4500 to 30, 8000 to 40
        ).random()
        val bw = f0.toDouble() / q
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "f₀ = $f0 Hz",
            parametersLine2 = "Quality Factor Q = $q",
            hints = listOf("Q = f₀ / BW ⇒ BW = f₀ / Q.", "Compute $f0 / $q."),
            workedSteps = listOf("1. BW = $f0 / $q = ${ComplexNum.formatClean(bw)} Hz."),
            expectedReal = bw
        )
    }

    "p_204b" -> {
        val (lMh, cUf) = listOf(
            10 to 10, 40 to 10, 25 to 40, 16 to 10, 100 to 10, 4 to 10
        ).random()
        val w0 = 1.0 / sqrt((lMh * 1e-3) * (cUf * 1e-6))
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "L = ${lMh}mH, C = ${cUf}μF",
            parametersLine2 = "R = 5Ω (series)",
            hints = listOf(
                "ω₀ = 1 / √(L·C) with L = ${lMh}×10⁻³ H and C = ${cUf}×10⁻⁶ F.",
                "√(L·C) = ${ComplexNum.formatClean(sqrt(lMh * 1e-3 * cUf * 1e-6) * 1000.0)} ms."
            ),
            workedSteps = listOf(
                "1. √(L·C) = ${ComplexNum.formatClean(1.0 / w0)} s.",
                "2. ω₀ = ${ComplexNum.formatClean(w0)} rad/s."
            ),
            expectedReal = w0
        )
    }

    "p_215e" -> {
        val (vs, r, xC) = listOf(
            Triple(20, 4, 40), Triple(15, 5, 80), Triple(24, 6, 50), Triple(18, 3, 45), Triple(30, 5, 75)
        ).random()
        val iRes = vs.toDouble() / r
        val vc = iRes * xC
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_s = $vs V rms, R = ${r}Ω",
            parametersLine2 = "X_L = X_C = ${xC}Ω at ω₀",
            hints = listOf(
                "Resonant current I = $vs / $r = ${ComplexNum.formatClean(iRes)} A.",
                "|V_C| = I × $xC."
            ),
            workedSteps = listOf(
                "1. I = $vs / $r = ${ComplexNum.formatClean(iRes)} A.",
                "2. |V_C| = I · $xC = ${ComplexNum.formatClean(vc)} V."
            ),
            expectedReal = vc
        )
    }

    "p_222g" -> {
        val (halfBw, w0, hyp) = listOf(
            Triple(300, 400, 500),
            Triple(600, 800, 1000),
            Triple(800, 1500, 1700),
            Triple(700, 2400, 2500),
            Triple(900, 1200, 1500)
        ).random()
        val bw = 2 * halfBw
        val w2 = (halfBw + hyp).toDouble()
        wrapCuratedVariation(
            base = base,
            prompt = "An RLC bandpass filter has ω₀ = $w0 rad/s and exact −3 dB bandwidth BW = $bw rad/s. Find upper corner ω₂:",
            parametersLine1 = "ω₀ = $w0 rad/s,   BW = ω₂ − ω₁ = $bw rad/s",
            parametersLine2 = "Exact geometric symmetry: ω₁ · ω₂ = ω₀²",
            hints = listOf(
                "BW / 2 = $halfBw rad/s.",
                "Notice ($halfBw, $w0, $hyp) is a Pythagorean triple: √($halfBw² + $w0²) = $hyp."
            ),
            workedSteps = listOf(
                "1. BW / 2 = $halfBw rad/s.",
                "2. √($halfBw² + $w0²) = $hyp rad/s ⇒ ω₂ = $halfBw + $hyp = ${w2.toInt()} rad/s."
            ),
            expectedReal = w2
        )
    }

    "p_209c" -> {
        val (r, cNf) = listOf(
            400 to 100, 250 to 200, 1000 to 50, 200 to 250, 800 to 125
        ).random()
        val lMh = listOf(10, 20, 25).random()
        val bw = 1.0 / (r * cNf * 1e-9)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω, L = ${lMh}mH",
            parametersLine2 = "C = ${cNf}nF (parallel tank)",
            hints = listOf(
                "In parallel RLC, BW = 1 / (R·C).",
                "R·C = $r × ${cNf}×10⁻⁹ s."
            ),
            workedSteps = listOf(
                "1. BW = 1 / ($r × ${cNf}×10⁻⁹) = ${ComplexNum.formatClean(bw)} rad/s."
            ),
            expectedReal = bw
        )
    }

    "p_219f" -> {
        val (rK, lMh, cNf) = listOf(
            Triple(4, 10, 250),
            Triple(3, 10, 400),
            Triple(2, 10, 1000),
            Triple(5, 25, 100),
            Triple(4, 40, 1000)
        ).random()
        val qp = (rK * 1000.0) * sqrt((cNf * 1e-9) / (lMh * 1e-3))
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${rK}kΩ, L = ${lMh}mH",
            parametersLine2 = "C = ${cNf}nF (parallel)",
            hints = listOf(
                "Compute C / L = (${cNf}×10⁻⁹) / (${lMh}×10⁻³).",
                "Multiply √(C/L) by R = ${rK * 1000} Ω."
            ),
            workedSteps = listOf(
                "1. √(C/L) = ${ComplexNum.formatClean(sqrt((cNf * 1e-9) / (lMh * 1e-3)))}.",
                "2. Q_p = R√(C/L) = ${ComplexNum.formatClean(qp)}."
            ),
            expectedReal = qp
        )
    }

    "p_224g" -> {
        val (rs, qCoil) = listOf(
            4 to 3, 5 to 4, 6 to 2, 10 to 3, 8 to 2
        ).random()
        val xl = rs * qCoil
        val zTank = (rs * (1 + qCoil * qCoil)).toDouble()
        wrapCuratedVariation(
            base = base,
            prompt = "A practical coil (R_s = ${rs}Ω, X_L = ${xl}Ω) is tuned to unity pf by a lossless parallel C. Find tank Z_tank:",
            parametersLine1 = "Coil Branch: R_s = ${rs}Ω, X_L = ${xl}Ω",
            parametersLine2 = "Shunted by C for Parallel Resonance (B_net = 0)",
            hints = listOf(
                "Q_coil = $xl / $rs = $qCoil.",
                "Z_tank = R_p = R_s(1 + Q_coil²) = $rs(1 + $qCoil²)."
            ),
            workedSteps = listOf(
                "1. Q_coil = $xl / $rs = $qCoil.",
                "2. Z_tank = $rs(1 + ${qCoil * qCoil}) = ${zTank.toInt()} Ω."
            ),
            expectedReal = zTank
        )
    }

    "p_228h" -> {
        val (rTankK, q0, rSrcK) = listOf(
            Triple(10, 30, 5), Triple(12, 24, 6), Triple(20, 25, 5), Triple(8, 20, 8), Triple(15, 18, 10)
        ).random()
        val rEq = (rTankK.toDouble() * rSrcK) / (rTankK + rSrcK)
        val qL = q0 * (rEq / rTankK)
        wrapCuratedVariation(
            base = base,
            prompt = "A parallel tank (R_tank = $rTankK kΩ, unloaded Q₀ = $q0) is driven by a source with R_s = $rSrcK kΩ. Find loaded Q_L:",
            parametersLine1 = "R_tank = $rTankK kΩ (unloaded Q₀ = $q0)",
            parametersLine2 = "Norton Source R_s = $rSrcK kΩ across tank",
            hints = listOf(
                "Compute parallel resistance R_eq = ${rTankK}k ∥ ${rSrcK}k = ${ComplexNum.formatClean(rEq)} kΩ.",
                "Q_L = $q0 × (R_eq / $rTankK)."
            ),
            workedSteps = listOf(
                "1. R_eq = ($rTankK × $rSrcK) / (${rTankK + rSrcK}) = ${ComplexNum.formatClean(rEq)} kΩ.",
                "2. Q_L = $q0 × (${ComplexNum.formatClean(rEq)} / $rTankK) = ${ComplexNum.formatClean(qL)}."
            ),
            expectedReal = qL
        )
    }

    "p_230i" -> {
        val (rS, q) = listOf(
            20 to 2, 5 to 3, 10 to 3, 25 to 2, 10 to 4, 50 to 1
        ).random()
        val rL = rS * (1 + q * q)
        val xL = (q * rS).toDouble()
        wrapCuratedVariation(
            base = base,
            prompt = "Match R_s = ${rS}Ω up to R_L = ${rL}Ω using series +jX_L and shunt −jX_C across R_L. Find series X_L (in Ω):",
            parametersLine1 = "Source R_s = $rS Ω  →  Load R_L = $rL Ω",
            parametersLine2 = "Q = √(R_L / R_s − 1),   X_L = Q · R_s",
            hints = listOf(
                "Q = √($rL / $rS − 1) = √${q * q} = $q.",
                "X_L = Q × $rS."
            ),
            workedSteps = listOf(
                "1. Q = √($rL/$rS − 1) = $q.",
                "2. X_L = $q × $rS = ${xL.toInt()} Ω."
            ),
            expectedReal = xL
        )
    }

    "p_216e" -> {
        val (iS, r, xL) = listOf(
            Triple(3.0, 400, 50),
            Triple(1.5, 500, 25),
            Triple(4.0, 300, 60),
            Triple(2.5, 480, 40),
            Triple(2.0, 800, 40)
        ).random()
        val vTank = iS * r
        val iL = vTank / xL
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "I_s = ${ComplexNum.formatClean(iS)} A rms,   R = $r Ω",
            parametersLine2 = "X_L = X_C = $xL Ω at resonance ω₀",
            hints = listOf(
                "Tank voltage V_tank = ${ComplexNum.formatClean(iS)} × $r = ${ComplexNum.formatClean(vTank)} V.",
                "|I_L| = ${ComplexNum.formatClean(vTank)} / $xL."
            ),
            workedSteps = listOf(
                "1. V_tank = I_s · R = ${ComplexNum.formatClean(vTank)} V.",
                "2. |I_L| = ${ComplexNum.formatClean(vTank)} / $xL = ${ComplexNum.formatClean(iL)} A."
            ),
            expectedReal = iL
        )
    }

    "p_232j" -> {
        val (q1, q2, prodSqrt) = listOf(
            Triple(10, 40, 20),
            Triple(25, 4, 10),
            Triple(20, 5, 10),
            Triple(40, 40, 40),
            Triple(16, 4, 8),
            Triple(50, 50, 50)
        ).random()
        val kc = 1.0 / prodSqrt
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Primary Tank Q₁ = $q1,   Secondary Tank Q₂ = $q2",
            parametersLine2 = "Critical coupling condition: k_c = 1 / √(Q₁ · Q₂)",
            hints = listOf(
                "Q₁ · Q₂ = ${q1 * q2} ⇒ √(${q1 * q2}) = $prodSqrt.",
                "k_c = 1 / $prodSqrt."
            ),
            workedSteps = listOf(
                "1. √(Q₁ · Q₂) = √${q1 * q2} = $prodSqrt.",
                "2. k_c = 1 / $prodSqrt = ${ComplexNum.formatClean(kc)}."
            ),
            expectedReal = kc
        )
    }

    else -> null
}
