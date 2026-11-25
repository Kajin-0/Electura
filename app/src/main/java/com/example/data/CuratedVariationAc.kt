package com.example.data

import com.example.model.AnswerKind
import com.example.model.CircuitProblem
import com.example.model.ComplexNum
import com.example.model.ProblemCategory
import com.example.model.ProblemStep
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
    workedSteps: List<String>,
    acContext: String = base.acContext,
    steps: List<ProblemStep> = emptyList()
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
    sourceProblemCode = base.code,
    acContext = acContext,
    steps = steps
)

internal fun generateAcVariation(base: CircuitProblem): CircuitProblem? = when (base.id) {
    // ── IMPEDANCE (16) ──
    "p_100a" -> {
        val omega = listOf(100, 200, 314, 377, 500, 1000, 2000).random()
        val vIsSin = Random.nextBoolean()
        var thetaVCos = listOf(-45, -30, -15, 0, 10, 15, 20, 30, 40, 45, 60).random()
        var thetaICos = listOf(-75, -60, -45, -30, -25, -15, 0, 15, 30).random()
        while (!vIsSin && thetaVCos == 20 && thetaICos == -25) {
            thetaICos = listOf(-75, -60, -45, -30, -15, 0, 15, 30).random()
        }
        val diff = (thetaVCos - thetaICos).toDouble()
        val vm = listOf(60, 80, 100, 120, 150, 200).random()
        val im = listOf(2, 3, 4, 5, 6, 8, 10).random()
        fun fmtPhase(deg: Int): String = when {
            deg > 0 -> "+ ${deg}°"
            deg < 0 -> "− ${-deg}°"
            else -> ""
        }
        val line1: String
        val line2: String
        val hint1: String
        val step1: String
        if (!vIsSin) {
            val iSinAngle = thetaICos + 90
            line1 = "v(t) = $vm cos(${omega}t ${fmtPhase(thetaVCos)}) V".replace("  ", " ").replace(" )", ")")
            line2 = "i(t) = $im sin(${omega}t ${fmtPhase(iSinAngle)}) A".replace("  ", " ").replace(" )", ")")
            hint1 = "sin(${omega}t ${fmtPhase(iSinAngle)}) = cos(${omega}t ${fmtPhase(iSinAngle)} − 90°), so θ_i = ${thetaICos}°.".replace(" )", ")")
            step1 = "1. Convert i(t) to cosine: θ_i = ${iSinAngle}° − 90° = ${thetaICos}°."
        } else {
            val vSinAngle = thetaVCos + 90
            line1 = "v(t) = $vm sin(${omega}t ${fmtPhase(vSinAngle)}) V".replace("  ", " ").replace(" )", ")")
            line2 = "i(t) = $im cos(${omega}t ${fmtPhase(thetaICos)}) A".replace("  ", " ").replace(" )", ")")
            hint1 = "sin(${omega}t ${fmtPhase(vSinAngle)}) = cos(${omega}t ${fmtPhase(vSinAngle)} − 90°), so θ_v = ${thetaVCos}°.".replace(" )", ")")
            step1 = "1. Convert v(t) to cosine: θ_v = ${vSinAngle}° − 90° = ${thetaVCos}°."
        }
        wrapCuratedVariation(
            base = base,
            parametersLine1 = line1,
            parametersLine2 = line2,
            hints = listOf(
                hint1,
                "θ = θ_v − θ_i = ${thetaVCos}° − (${thetaICos}°)."
            ),
            workedSteps = listOf(
                step1,
                "2. Impedance phase θ = θ_v − θ_i = ${thetaVCos}° − (${thetaICos}°) = ${diff.toInt()}°."
            ),
            expectedReal = diff
        )
    }

    "p_101a" -> {
        val (rBase, xBase, zBase) = listOf(
            Triple(3, 4, 5), Triple(4, 3, 5), Triple(5, 12, 13), Triple(12, 5, 13), Triple(8, 15, 17)
        ).random()
        val scale = listOf(1, 2, 3, 4, 5).random()
        val r = rBase * scale
        val xL = xBase * scale
        val zMag = zBase * scale
        val omega = listOf(50, 100, 200, 250, 400, 500).random()
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
        val l1 = listOf(20, 25, 30, 40, 45, 50, 60, 75, 80, 90).random()
        val l2 = listOf(25, 35, 40, 50, 60, 75, 80, 90, 100).random()
        val maxM = minOf(l1, l2) / 2
        val mPool = (5..maxM step 5).toList()
        val m = if (mPool.isNotEmpty()) mPool.random() else 5
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
        val (omega, xC) = listOf(
            50 to 40, 50 to 50, 50 to 80,
            100 to 20, 100 to 25, 100 to 40, 100 to 50,
            200 to 10, 200 to 20, 200 to 25, 200 to 50,
            250 to 16, 250 to 20, 250 to 40,
            500 to 10, 500 to 20, 500 to 25, 500 to 40
        ).random()
        val cUf = 1_000_000.0 / (omega.toDouble() * xC.toDouble())
        val r = listOf(10, 15, 20, 25, 30, 40, 50, 60, 75, 80).random()
        val cStr = ComplexNum.formatClean(cUf)
        val z = ComplexNum(r.toDouble(), -xC.toDouble())
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω, C = ${cStr}μF",
            parametersLine2 = "ω = $omega rad/s",
            hints = listOf(
                "Convert $cStr μF = $cStr × 10⁻⁶ F.",
                "X_C = 1 / ($omega × ${cStr}×10⁻⁶) = $xC Ω, giving Z = $r − j$xC Ω."
            ),
            workedSteps = listOf(
                "1. X_C = 1/(ωC) = $xC Ω.",
                "2. Z = $r − j$xC Ω (${z.formatPolar()})."
            ),
            expectedComplex = z
        )
    }

    "p_106b" -> {
        val rSum = listOf(10, 20, 25, 50).random()
        val r1 = (2..(rSum - 2) step 2).toList().random()
        val r2 = rSum - r1
        val xMag = listOf(2, 3, 4, 5, 6, 8, 10, 12).random()
        val z1Inductive = Random.nextBoolean()
        val x1 = if (z1Inductive) xMag else -xMag
        val x2 = -x1
        val scale = listOf(1, 2, 3, 4).random()
        val iSrc = rSum * scale
        val ans = ComplexNum((scale * r2).toDouble(), (scale * x2).toDouble())
        val z1Str = ComplexNum(r1.toDouble(), x1.toDouble()).formatRect()
        val z2Str = ComplexNum(r2.toDouble(), x2.toDouble()).formatRect()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "I_s = $iSrc∠0° A,   Z₁ = ($z1Str) Ω",
            parametersLine2 = "Z₂ = ($z2Str) Ω  (parallel with Z₁)",
            hints = listOf(
                "Sum the two impedances: Z₁ + Z₂ = $rSum + j0 Ω.",
                "I₁ = $iSrc × ($z2Str) / $rSum = $scale × ($z2Str)."
            ),
            workedSteps = listOf(
                "1. Z₁ + Z₂ = $rSum + j0 Ω.",
                "2. I₁ = $iSrc · ($z2Str) / $rSum = ${ans.formatRect()} A (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_108c" -> {
        val (rBase, xBase, magSqBase) = listOf(
            Triple(1, 1, 2),
            Triple(3, 4, 25),
            Triple(4, 3, 25),
            Triple(1, 2, 5),
            Triple(2, 1, 5)
        ).random()
        val scale = listOf(4, 5, 6, 8, 10, 12, 15, 20).random()
        val r = rBase * scale
        val xC = xBase * scale
        val re = (r.toDouble() * xC * xC) / (r * r + xC * xC)
        val im = -(r.toDouble() * r * xC) / (r * r + xC * xC)
        val ans = ComplexNum(re, im)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω || Z_C = -j${xC}Ω",
            parametersLine2 = "Y = 1/$r + j/$xC",
            hints = listOf(
                "Z_eq = ($r × -j$xC) / ($r − j$xC).",
                "Multiply numerator and denominator by ($r + j$xC); |Z_sum|² = ${r * r + xC * xC}."
            ),
            workedSteps = listOf(
                "1. Admittance Y = 1/$r + j/$xC (${magSqBase * scale * scale} denominator).",
                "2. Z_eq = 1 / Y = ${ans.formatRect()} Ω (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_112d" -> {
        val r = listOf(3, 4, 5, 6, 8, 10, 12, 15, 20, 25).random()
        val omega = listOf(50, 100, 200, 250, 500).random()
        val xL = listOf(4, 5, 8, 10, 12, 16, 20, 25, 40).random()
        val xC = listOf(4, 5, 8, 10, 16, 20, 25, 40).filter { it != xL }.random()
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
        val a = listOf(1, 2, 4, 5, 8, 10).random()
        val b = listOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 12).random()
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
        val (rBase, xBase, denBase) = listOf(
            Triple(1, 1, 2),
            Triple(1, 2, 5),
            Triple(2, 1, 5),
            Triple(3, 4, 25),
            Triple(4, 3, 25),
            Triple(5, 12, 169)
        ).random()
        val scale = listOf(1, 2, 4, 5).random()
        val vMult = listOf(1, 2, 3, 4).random()
        val r = rBase * scale
        val xL = xBase * scale
        val vIn = vMult * denBase * scale
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
        val (rBase, xBase) = listOf(
            1 to 1, 3 to 4, 4 to 3, 1 to 2, 2 to 1
        ).random()
        val scale = listOf(4, 5, 6, 8, 10, 12, 15, 20).random()
        val r = rBase * scale
        val xL = xBase * scale
        val den = (r * r + xL * xL).toDouble()
        val ans = ComplexNum((r * xL * xL) / den, (r * r * xL) / den)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω || Z_L = j${xL}Ω",
            parametersLine2 = "Y = 1/$r − j/$xL S",
            hints = listOf(
                "Z_eq = ($r × j$xL) / ($r + j$xL).",
                "Multiply numerator and denominator by ($r − j$xL); |Z_sum|² = ${den.toInt()}."
            ),
            workedSteps = listOf(
                "1. Z_eq = j${r * xL}($r − j$xL) / ${den.toInt()} = ${ans.formatRect()} Ω (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_124f" -> {
        val rIn = listOf(2, 4, 5, 8, 10, 20, 25).random().toDouble()
        val rF = listOf(10, 20, 25, 40, 50).random().toDouble()
        val xCfK = listOf(10, 20, 25, 40, 50).random().toDouble()
        val omega = listOf(100, 200, 250, 500, 1000).random().toDouble()
        val cNf = 1_000_000.0 / (omega * xCfK)
        val yRe = 1.0 / rF
        val yIm = 1.0 / xCfK
        val den = yRe * yRe + yIm * yIm
        val zfRe = yRe / den
        val zfIm = -yIm / den
        val ans = ComplexNum(-zfRe / rIn, -zfIm / rIn)
        val zf = ComplexNum(zfRe, zfIm)
        val xCfStr = ComplexNum.formatClean(xCfK)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R_in = ${rIn.toInt()} kΩ,   R_f = ${rF.toInt()} kΩ ∥ C_f = ${ComplexNum.formatClean(cNf)} nF",
            parametersLine2 = "ω = ${omega.toInt()} rad/s",
            hints = listOf(
                "|X_Cf| = 1 / (ω·C_f) = $xCfStr kΩ.",
                "Compute parallel Z_f = R_f ∥ (−jX_Cf), then A_v = −Z_f / ${rIn.toInt()} kΩ."
            ),
            workedSteps = listOf(
                "1. X_Cf = −j$xCfStr kΩ.",
                "2. Z_f = ${zf.formatRect()} kΩ.",
                "3. A_v = −Z_f / ${rIn.toInt()} kΩ = ${ans.formatRect()} V/V (${ans.formatPolar()})."
            ),
            expectedComplex = ans,
            steps = listOf(
                ProblemStep(
                    title = "Capacitive Reactance",
                    prompt = "Step 1: Find the feedback capacitor reactance magnitude |X_Cf| = 1 / (ω · C_f) (in kΩ):",
                    methodNote = "Convert C_f from nF to Farads (×10⁻⁹), evaluate 1/(ω·C_f), and divide by 1000 for kΩ.",
                    inputHint = "Enter |X_Cf| in kΩ",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = xCfK,
                    unit = "kΩ",
                    hints = listOf(
                        "1 / (${omega.toInt()} × ${ComplexNum.formatClean(cNf)}×10⁻⁹) = $xCfStr kΩ."
                    ),
                    summaryText = "|X_Cf| = $xCfStr kΩ (Z_Cf = −j$xCfStr kΩ)"
                ),
                ProblemStep(
                    title = "Feedback Impedance",
                    prompt = "Step 2: Compute parallel feedback impedance Z_f = R_f ∥ (−jX_Cf) (in kΩ):",
                    methodNote = "Evaluate Z_f = (R_f · −jX_Cf) / (R_f − jX_Cf).",
                    inputHint = "Enter Z_f in kΩ (A + jB)",
                    answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
                    expectedComplex = zf,
                    unit = "kΩ",
                    hints = listOf(
                        "Z_f = 1 / (1/${rF.toInt()} + j/$xCfStr) = ${zf.formatRect()} kΩ."
                    ),
                    summaryText = "Z_f = ${zf.formatRect()} kΩ"
                )
            )
        )
    }

    "p_128h" -> {
        val (n1, n2) = listOf(2 to 1, 3 to 1, 4 to 1, 5 to 1, 1 to 2, 1 to 4, 3 to 2).random()
        val aSq = (n1.toDouble() / n2) * (n1.toDouble() / n2)
        val step = if (n2 == 2) 4 else if (n2 == 4) 16 else 2
        val rL = (1..6).random() * step
        val xMag = (1..6).random() * step
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
        val r = listOf(2, 4, 5, 6, 8, 10, 12).random()
        val v1 = r * listOf(2, 3, 4, 5, 6, 8, 10).random()
        val iMag = listOf(2, 3, 4, 5, 6, 8).random()
        val iSign = if (Random.nextBoolean()) 1 else -1
        val branchInd = Random.nextBoolean()
        val iS = iMag * iSign
        // KCL: (V_A - V1)/R + V_A/(branchSign*jR) = j*iS  => V_A(1 - branchSign*j) = V1 + j*(R*iS)
        // Multiply by (1 + branchSign*j)/2:
        val bSign = if (branchInd) 1 else -1
        val rhsRe = v1.toDouble()
        val rhsIm = (r * iS).toDouble()
        val ans = ComplexNum((rhsRe - bSign * rhsIm) / 2.0, (rhsIm + bSign * rhsRe) / 2.0)
        val iStr = if (iS >= 0) "j$iS" else "−j${-iS}"
        val zStr = if (branchInd) "j${r}Ω" else "−j${r}Ω"
        val multFactor = if (branchInd) "(1 + j)" else "(1 − j)"
        val divKcl = if (branchInd) "(1 − j)" else "(1 + j)"
        wrapCuratedVariation(
            base = base,
            prompt = "Find node phasor voltage V_A driven by V₁ through R = ${r}Ω plus current source I_s entering node A:",
            parametersLine1 = "V₁ = $v1∠0° V via R = ${r}Ω to A",
            parametersLine2 = "I_s = $iStr A entering A; Z_branch = $zStr (A to GND)",
            hints = listOf(
                "(V_A − $v1)/$r + V_A/($zStr) = $iStr.",
                "Multiply by $r: V_A$divKcl = $v1 ${if (rhsIm >= 0) "+ j${rhsIm.toInt()}" else "− j${(-rhsIm).toInt()}"}."
            ),
            workedSteps = listOf(
                "1. KCL at node A: V_A$divKcl = $v1 ${if (rhsIm >= 0) "+ j${rhsIm.toInt()}" else "− j${(-rhsIm).toInt()}"}.",
                "2. Multiply by $multFactor/2: V_A = ${ans.formatRect()} V (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_132i" -> {
        val r = listOf(5, 10, 15, 20, 25, 30, 40, 50).random()
        val l1 = listOf(40, 50, 60, 80, 100).random()
        val l2 = listOf(30, 40, 50, 60, 80).random()
        val maxM = (minOf(l1, l2) / 2) - 5
        val mPool = (5..maxM step 5).toList()
        val m = if (mPool.isNotEmpty()) mPool.random() else 5
        val omega = listOf(250, 500, 1000, 2000).random()
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
        val (rBase, xBase, magSqBase) = listOf(
            Triple(3, 4, 25), Triple(4, 3, 25), Triple(1, 2, 5), Triple(2, 1, 5), Triple(5, 12, 169), Triple(12, 5, 169)
        ).random()
        val scale = listOf(1, 2).random()
        val r = rBase * scale
        val xSigned = xBase * scale * (if (Random.nextBoolean()) 1 else -1)
        val k = listOf(1, 2, 3, 4, 5).random()
        val magSq = r * r + xSigned * xSigned
        val vTh = k * magSq
        val ans = ComplexNum((k * r).toDouble(), -(k * xSigned).toDouble())
        val zStr = ComplexNum(r.toDouble(), xSigned.toDouble()).formatRect()
        val zConjStr = ComplexNum(r.toDouble(), -xSigned.toDouble()).formatRect()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_th = $vTh∠0° V, Z_th = ($zStr) Ω",
            parametersLine2 = "Solve I_N = V_th / Z_th",
            hints = listOf(
                "Multiply numerator and denominator by Z_th* = $zConjStr.",
                "Denominator |Z_th|² = $r² + (${xSigned})² = $magSq ($magSqBase × ${scale * scale})."
            ),
            workedSteps = listOf(
                "1. I_N = $vTh($zConjStr) / $magSq = $k($zConjStr).",
                "2. I_N = ${ans.formatRect()} A (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_144h" -> {
        val r = listOf(4, 5, 6, 8, 10, 12, 15, 20).random()
        val xMag = listOf(4, 5, 6, 8, 10, 12, 15).random()
        val gain = listOf(2, 3, 4, 5, 6).random()
        val inductive = Random.nextBoolean()
        val xSigned = if (inductive) xMag else -xMag
        val ans = ComplexNum(r.toDouble(), xSigned * (1.0 + gain))
        val zxStr = if (inductive) "+j${xMag}Ω" else "−j${xMag}Ω"
        val symV = if (inductive) "V_L" else "V_C"
        val resIm = xSigned * (1 + gain)
        val resImStr = if (resIm >= 0) "+ j$resIm" else "− j${-resIm}"
        wrapCuratedVariation(
            base = base,
            prompt = "A test source V_t drives series R = ${r}Ω, Z_X = $zxStr, and dependent drop V_d = ${gain}$symV in loop KVL. Find Z_th = V_t/I_t:",
            parametersLine1 = "R = ${r}Ω,   Z_X = $zxStr,   $symV = I_t·Z_X",
            parametersLine2 = "Loop KVL: V_t = I_t·R + $symV + ${gain}$symV",
            hints = listOf(
                "Combine $symV + ${gain}$symV = ${gain + 1}$symV = ${gain + 1}($zxStr · I_t).",
                "V_t = I_t($r $resImStr), so Z_th = V_t / I_t."
            ),
            workedSteps = listOf(
                "1. $symV = I_t($zxStr).",
                "2. KVL: V_t = ($r $resImStr) I_t ⇒ Z_th = ${ans.formatRect()} Ω."
            ),
            expectedComplex = ans
        )
    }

    "p_116f" -> {
        val (rBase, xBase) = listOf(
            3 to 4, 4 to 3, 5 to 12, 12 to 5, 1 to 2, 2 to 1
        ).random()
        val scale = listOf(1, 2, 4).random()
        val vMult = listOf(1, 2, 3).random()
        val r = rBase * scale
        val xMag = xBase * scale
        val vS = vMult * (r * r + xMag * xMag)
        val isInductive = Random.nextBoolean()
        val xSigned = if (isInductive) xMag.toDouble() else -xMag.toDouble()
        // V_th = vS * (j*xSigned) / (r + j*xSigned) = vS * (xSigned^2 + j*r*xSigned) / (r^2 + xSigned^2)
        val den = (r * r + xMag * xMag).toDouble()
        val ans = ComplexNum(vS * xSigned * xSigned / den, vS * r * xSigned / den)
        val z2Label = if (isInductive) "+j$xMag Ω (open-circuit shunt L)" else "−j$xMag Ω (open-circuit shunt C)"
        val z2Short = if (isInductive) "j$xMag" else "−j$xMag"
        val divConj = if (isInductive) "($r − j$xMag)" else "($r + j$xMag)"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_s = $vS∠0° V,   Z₁ = R = $r Ω",
            parametersLine2 = "Z₂ = $z2Label",
            hints = listOf(
                "V_th = $vS × ($z2Short) / ($r ${if (isInductive) "+ j$xMag" else "− j$xMag"}).",
                "Multiply numerator and denominator by $divConj; |Z₁ + Z₂|² = ${den.toInt()}."
            ),
            workedSteps = listOf(
                "1. V_th = $vS($z2Short)$divConj / ${den.toInt()}.",
                "2. V_th = ${ans.formatRect()} V (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_146i" -> {
        val (r2, x2Mag, wM) = listOf(
            Triple(3, 4, 5),
            Triple(3, 4, 10),
            Triple(4, 3, 10),
            Triple(6, 8, 10),
            Triple(5, 12, 13),
            Triple(8, 6, 20),
            Triple(1, 2, 5),
            Triple(2, 1, 10)
        ).random()
        val x2 = if (Random.nextBoolean()) x2Mag else -x2Mag
        val r1 = listOf(2, 3, 4, 5, 6, 8, 10).random()
        val x1 = listOf(4, 6, 8, 10, 12, 14, 16, 18).random()
        val wMSq = (wM * wM).toDouble()
        val den = (r2 * r2 + x2 * x2).toDouble()
        val refRe = wMSq * r2 / den
        val refIm = -wMSq * x2 / den
        val ans = ComplexNum(r1 + refRe, x1 + refIm)
        val ref = ComplexNum(refRe, refIm)
        val z22Str = ComplexNum(r2.toDouble(), x2.toDouble()).formatRect()
        val z22ConjStr = ComplexNum(r2.toDouble(), -x2.toDouble()).formatRect()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Z₁₁ = ($r1 + j$x1) Ω,   Z₂₂ = ($z22Str) Ω",
            parametersLine2 = "Mutual reactance ωM = $wM Ω  (Z_ref = (ωM)² / Z₂₂)",
            hints = listOf(
                "(ωM)² = ${wM * wM}, so Z_ref = ${wM * wM} / ($z22Str) = ${ref.formatRect()} Ω.",
                "Add Z₁₁ = ($r1 + j$x1) Ω."
            ),
            workedSteps = listOf(
                "1. Reflected impedance: Z_ref = ${wM * wM}($z22ConjStr) / ${den.toInt()} = ${ref.formatRect()} Ω.",
                "2. Z_in = ($r1 + j$x1) + (${ref.formatRect()}) = ${ans.formatRect()} Ω."
            ),
            expectedComplex = ans,
            steps = listOf(
                ProblemStep(
                    title = "Reflected Impedance",
                    prompt = "Step 1: Compute the reflected impedance from the secondary loop Z_ref = (ωM)² / Z₂₂ (in Ω):",
                    methodNote = "Square ωM = $wM Ω and divide by Z₂₂ = $z22Str Ω.",
                    inputHint = "Enter Z_ref in Ω (A + jB)",
                    answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
                    expectedComplex = ref,
                    unit = "Ω",
                    hints = listOf(
                        "(ωM)² = ${wM * wM}, |Z₂₂|² = ${den.toInt()}.",
                        "Z_ref = ${wM * wM}($z22ConjStr) / ${den.toInt()}."
                    ),
                    summaryText = "Z_ref = ${ref.formatRect()} Ω"
                )
            )
        )
    }

    "p_114e" -> {
        val scale = listOf(1, 2, 3, 4).random()
        val quad = listOf(
            listOf(1, 2, 2, 3),
            listOf(2, 3, 6, 7),
            listOf(1, 4, 8, 9),
            listOf(4, 4, 7, 9),
            listOf(2, 6, 9, 11),
            listOf(6, 6, 7, 11),
            listOf(3, 4, 12, 13),
            listOf(2, 5, 14, 15)
        ).random()
        val wMult = listOf(1, 2).random()
        val r = 12 * scale
        val lMh = (25 * scale) / wMult.toDouble()
        val w1 = 200 * wMult
        val w2 = 640 * wMult
        val v0 = quad[0] * 12 * scale
        val v1Rms = quad[1] * 13 * scale
        val v2Rms = quad[2] * 20 * scale
        val iTot = quad[3].toDouble()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v(t) = $v0 + ${v1Rms}√2 cos(${w1}t) + ${v2Rms}√2 sin(${w2}t) V",
            parametersLine2 = "Series Load: R = $r Ω,  L = ${ComplexNum.formatClean(lMh)} mH   (I_rms = √(I₀² + I₁² + I₂²))",
            hints = listOf(
                "DC I₀ = $v0 / $r = ${quad[0]} A; at ω₁ = $w1 rad/s, |Z₁| = ${13 * scale} Ω ⇒ I₁ = ${quad[1]} A.",
                "At ω₂ = $w2 rad/s, |Z₂| = ${20 * scale} Ω ⇒ I₂ = ${quad[2]} A."
            ),
            workedSteps = listOf(
                "1. I₀ = ${quad[0]} A, I₁ = ${quad[1]} A, I₂ = ${quad[2]} A.",
                "2. I_rms = √(${quad[0]}² + ${quad[1]}² + ${quad[2]}²) = ${quad[3]} A."
            ),
            expectedReal = iTot
        )
    }

    "p_134j" -> {
        val c2Nf = listOf(40, 50, 80, 100, 120, 150, 200, 250).random()
        val r3 = listOf(200, 400, 500, 600, 800, 1000).random()
        val ratio = listOf(2.0, 2.5, 3.0, 4.0, 5.0, 6.0).random()
        val r4 = (r3 * ratio).toInt()
        val c4Nf = listOf(10, 15, 20, 25, 30, 40, 50).random()
        val cxNf = c2Nf * ratio
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Arm 2: C₂ = $c2Nf nF,   Arm 3: R₃ = $r3 Ω",
            parametersLine2 = "Arm 4: R₄ = $r4 Ω ∥ C₄ = $c4Nf nF   (Z_x = Z₂·Z₃·Y₄)",
            hints = listOf(
                "Balance yields C_x = C₂ · (R₄ / R₃).",
                "Compute $c2Nf × ($r4 / $r3)."
            ),
            workedSteps = listOf(
                "1. C_x = $c2Nf nF × ($r4 / $r3) = ${ComplexNum.formatClean(cxNf)} nF."
            ),
            expectedReal = cxNf
        )
    }

    "p_135k" -> {
        val (r1Base, xmBase, r2Base, aIn, hyp) = listOf(
            listOf(2, 4, 16, 3, 5),
            listOf(1, 12, 36, 5, 13),
            listOf(11, 5, 25, 12, 13),
            listOf(3, 15, 45, 8, 17),
            listOf(11, 8, 16, 15, 17)
        ).random()
        val scale = listOf(1, 2, 3).random()
        val vMult = listOf(1, 2, 3).random()
        val r1 = r1Base * scale
        val xm = xmBase * scale
        val r2 = r2Base * scale
        val vS = vMult * hyp * hyp * scale
        val isIndShared = Random.nextBoolean()
        val zInIm = if (isIndShared) xmBase else -xmBase
        val ans = ComplexNum((vMult * aIn).toDouble(), (-vMult * zInIm).toDouble())
        val zmStr = if (isIndShared) "+j$xm" else "−j$xm"
        val xc2Str = if (isIndShared) "C (−j$xm Ω)" else "L (+j$xm Ω)"
        val z11Str = if (isIndShared) "$r1 + j$xm" else "$r1 − j$xm"
        val xRef = xm * xm / r2
        wrapCuratedVariation(
            base = base,
            prompt = "Two AC loops share reactance Z_m = $zmStr Ω. Loop 1 has V₁ = $vS∠0° V and R₁ = $r1 Ω; Loop 2 is passive with R₂ = $r2 Ω and $xc2Str canceling Z_m. Find Loop 1 current I₁ (in Amps):",
            parametersLine1 = "Loop 1: V₁ = $vS∠0° V, R₁ = $r1 Ω, shared Z_m = $zmStr Ω",
            parametersLine2 = "Loop 2: R₂ = $r2 Ω, ${if (isIndShared) "X_C2 = −j$xm" else "X_L2 = +j$xm"} Ω, shared Z_m = $zmStr Ω  (Z₂₂ = $r2 Ω)",
            hints = listOf(
                "Loop 2 gives I₂ = (Z_m / $r2) I₁; substituting into Loop 1 adds +(ωM)²/$r2 = +$xRef Ω to R₁.",
                "Z_in = ($r1 + $xRef) ${if (isIndShared) "+ j$xm" else "− j$xm"} = ${aIn * scale} ${if (isIndShared) "+ j$xm" else "− j$xm"} Ω."
            ),
            workedSteps = listOf(
                "1. Reflected Loop-2 resistance: X_m²/R₂ = $xm²/$r2 = $xRef Ω ⇒ Z_in = (${z11Str}) + $xRef = ${aIn * scale} ${if (isIndShared) "+ j$xm" else "− j$xm"} Ω.",
                "2. I₁ = $vS / Z_in = ${ans.formatRect()} A (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_136l" -> {
        val (baseAb, baseBc, baseCa, sumRealBase) = listOf(
            listOf(ComplexNum(6.0, 8.0), ComplexNum(10.0, -6.0), ComplexNum(4.0, -2.0), 20.0),
            listOf(ComplexNum(3.0, 4.0), ComplexNum(4.0, -2.0), ComplexNum(3.0, -2.0), 10.0),
            listOf(ComplexNum(4.0, 6.0), ComplexNum(12.0, -4.0), ComplexNum(4.0, -2.0), 20.0)
        ).random()
        val scale = listOf(1, 2, 3, 4, 5).random()
        val conjSign = if (Random.nextBoolean()) 1.0 else -1.0
        fun mkZ(z: ComplexNum) = ComplexNum(z.re * scale, z.im * scale * conjSign)
        val zab = mkZ(baseAb as ComplexNum)
        val zbc = mkZ(baseBc as ComplexNum)
        val zca = mkZ(baseCa as ComplexNum)
        val sumZ = (sumRealBase as Double) * scale
        val branch = (0..2).random()
        val (targetName, pairLabel, zA, zB, formulaStr) = when (branch) {
            0 -> listOf("terminal-a Wye branch Z₁", "Z_ab · Z_ca", zab, zca, "(Z_ab · Z_ca) / (Z_ab + Z_bc + Z_ca)")
            1 -> listOf("terminal-b Wye branch Z₂", "Z_ab · Z_bc", zab, zbc, "(Z_ab · Z_bc) / (Z_ab + Z_bc + Z_ca)")
            else -> listOf("terminal-c Wye branch Z₃", "Z_bc · Z_ca", zbc, zca, "(Z_bc · Z_ca) / (Z_ab + Z_bc + Z_ca)")
        }
        val zaC = zA as ComplexNum; val zbC = zB as ComplexNum
        val prodRe = zaC.re * zbC.re - zaC.im * zbC.im
        val prodIm = zaC.re * zbC.im + zaC.im * zbC.re
        val prodC = ComplexNum(prodRe, prodIm)
        val sumStr = ComplexNum.formatClean(sumZ)
        val ans = ComplexNum(prodRe / sumZ, prodIm / sumZ)
        wrapCuratedVariation(
            base = base,
            prompt = "Transform a complex Delta network (Z_ab, Z_bc, Z_ca) into its equivalent Wye star. Find ${targetName as String} = ${formulaStr as String} (in Ohms):",
            parametersLine1 = "Z_ab = ${zab.formatRect()} Ω,   Z_bc = ${zbc.formatRect()} Ω",
            parametersLine2 = "Z_ca = ${zca.formatRect()} Ω   (ΣZ_Δ = Z_ab + Z_bc + Z_ca)",
            hints = listOf(
                "Loop sum ΣZ_Δ = $sumStr Ω (imaginary parts cancel).",
                "Product = (${zaC.formatRect()})(${zbC.formatRect()}) = ${prodC.formatRect()} Ω²."
            ),
            workedSteps = listOf(
                "1. ΣZ_Δ = $sumStr Ω.",
                "2. Answer = (${prodC.formatRect()}) / $sumStr = ${ans.formatRect()} Ω."
            ),
            expectedComplex = ans,
            steps = listOf(
                ProblemStep(
                    title = "Delta Loop Sum",
                    prompt = "Step 1: Find the total Delta loop impedance sum ΣZ_Δ = Z_ab + Z_bc + Z_ca (in Ω):",
                    methodNote = "Add real and imaginary parts across all three Delta branches.",
                    inputHint = "Enter ΣZ_Δ in Ω",
                    answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
                    expectedComplex = ComplexNum(sumZ, 0.0),
                    unit = "Ω",
                    hints = listOf(
                        "Imaginary parts cancel cleanly; sum the real parts = $sumStr Ω."
                    ),
                    summaryText = "ΣZ_Δ = $sumStr Ω"
                ),
                ProblemStep(
                    title = "Adjacent Branch Product",
                    prompt = "Step 2: Multiply the two adjacent Delta branches ${pairLabel as String} (in Ω²):",
                    methodNote = "Compute (${zaC.formatRect()}) · (${zbC.formatRect()}).",
                    inputHint = "Enter product (A + jB)",
                    answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
                    expectedComplex = prodC,
                    unit = "",
                    hints = listOf(
                        "(${zaC.formatRect()})(${zbC.formatRect()}) = ${prodC.formatRect()}."
                    ),
                    summaryText = "$pairLabel = ${prodC.formatRect()} Ω²"
                )
            )
        )
    }

    // ── ADMITTANCE (18) ──
    "p_501a" -> {
        val (br, bx) = listOf(3 to 4, 4 to 3, 5 to 12, 12 to 5, 8 to 15, 15 to 8).random()
        val scale = listOf(1, 2, 5, 10).random()
        val xSign = if (Random.nextBoolean()) 1 else -1
        val r = br * scale
        val x = bx * scale * xSign
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
        val r = listOf(10, 20, 25, 40, 50, 80, 100).random()
        val (cUf, omega) = listOf(
            10 to 1000, 20 to 500, 25 to 1000, 40 to 1000, 50 to 1000,
            100 to 500, 100 to 1000, 50 to 2000, 25 to 2000, 10 to 5000
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
        val zMag = listOf(20, 25, 40, 50, 60, 75, 80, 100, 120, 150).random()
        val (deg, cosVal) = listOf(36.87 to 0.8, 53.13 to 0.6, 60.0 to 0.5).random()
        val thetaSign = if (Random.nextBoolean()) "+" else "−"
        val rs = zMag * cosVal
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "|Z| = $zMag Ω,  θ = $thetaSign$deg°",
            parametersLine2 = "f_test = 159.15 Hz (ω = 1000 rad/s)",
            hints = listOf(
                "cos($thetaSign$deg°) = cos($deg°) ≈ ${ComplexNum.formatClean(cosVal)}.",
                "R_s = $zMag × ${ComplexNum.formatClean(cosVal)}."
            ),
            workedSteps = listOf(
                "1. R_s = |Z| cos(θ) = $zMag × ${ComplexNum.formatClean(cosVal)} = ${ComplexNum.formatClean(rs)} Ω."
            ),
            expectedReal = rs
        )
    }

    "p_507c" -> {
        val candidates = listOf(5, 10, 20, 25, 40, 50, 80, 100).flatMap { lMh ->
            listOf(100, 200, 250, 400, 500, 800, 1000, 2000).mapNotNull { omega ->
                val xlTimes10 = (omega * lMh) / 100
                if ((omega * lMh) % 1000 == 0 && (omega * lMh / 1000) in setOf(4, 5, 8, 10, 16, 20, 25, 40, 50, 80, 100)) {
                    lMh to omega
                } else if (xlTimes10 == 25 || xlTimes10 == 50) {
                    lMh to omega
                } else null
            }
        }
        val (lMh, omega) = candidates.random()
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
        val validPairs = listOf(1, 2, 4, 5, 8, 10, 20).flatMap { rK ->
            listOf(25, 40, 50, 100, 125, 200, 250, 500).mapNotNull { cNf ->
                if (1_000_000 % (rK * cNf) == 0) rK to cNf else null
            }
        }
        val (rK, cNf) = validPairs.random()
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
        val validPairs = listOf(200, 400, 500, 1000, 2000, 4000).flatMap { omega ->
            listOf(5, 10, 15, 20, 25, 30, 40, 50, 60, 80).mapNotNull { lMh ->
                if ((omega * lMh) % 1000 == 0) omega to lMh else null
            }
        }
        val (omega, lMh) = validPairs.random()
        val rs = listOf(5, 10, 12, 15, 20, 25, 30, 40).random()
        val xs = (omega * lMh) / 1000
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
        val esr = listOf(1, 2, 3, 4, 5, 6, 8, 9, 10, 12).random()
        val xc = listOf(50, 100, 150, 200, 250, 400, 500).random()
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
        val rs = listOf(2, 4, 5, 6, 8, 10, 12, 15, 20).random()
        val q = listOf(2, 3, 4, 5, 6, 8, 10).random()
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
        val (gr, br) = listOf(3 to 4, 4 to 3, 6 to 8, 8 to 6, 5 to 12, 12 to 5).random()
        val scale = listOf(0.05, 0.1, 0.2).random()
        val bSign = if (Random.nextBoolean()) 1.0 else -1.0
        val g = gr * scale
        val b = br * scale * bSign
        val cancelMargin = listOf(0.1, 0.2, 0.3, 0.4, 0.5).random()
        val bc = if (b >= 0) b + cancelMargin else cancelMargin
        val bl = if (b >= 0) -cancelMargin else b - cancelMargin
        val magSq = g * g + b * b
        val ans = ComplexNum(g / magSq, -b / magSq)
        val gStr = ComplexNum.formatClean(g)
        val bcStr = ComplexNum.formatClean(bc)
        val blStr = ComplexNum.formatClean(bl)
        val yNet = ComplexNum(g, b)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "G = $gStr S,  B_C = +$bcStr S",
            parametersLine2 = "B_L = $blStr S",
            hints = listOf(
                "Net susceptance B = $bcStr + ($blStr) = ${ComplexNum.formatClean(b)} S.",
                "|Y|² = ${ComplexNum.formatClean(magSq)}, Z = ($gStr − j(${ComplexNum.formatClean(b)})) / ${ComplexNum.formatClean(magSq)}."
            ),
            workedSteps = listOf(
                "1. Y = ${yNet.formatRect()} S.",
                "2. Z = 1 / Y = ${ans.formatRect()} Ω (${ans.formatPolar()})."
            ),
            expectedComplex = ans,
            steps = listOf(
                ProblemStep(
                    title = "Net Admittance",
                    prompt = "Step 1: Combine the parallel conductance and susceptances into net admittance Y = G + j(B_C + B_L) (in S):",
                    methodNote = "Sum B = B_C + B_L and pair with conductance G.",
                    inputHint = "Enter Y in S (G + jB)",
                    answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
                    expectedComplex = yNet,
                    unit = "S",
                    hints = listOf(
                        "B = $bcStr + ($blStr) = ${ComplexNum.formatClean(b)} S."
                    ),
                    summaryText = "Y = ${yNet.formatRect()} S"
                )
            )
        )
    }

    "p_529i" -> {
        val g = listOf(0.01, 0.02, 0.025, 0.04, 0.05, 0.08).random()
        val omega = listOf(200, 400, 500, 1000, 2000, 4000, 5000).random()
        val cpUf = listOf(5, 10, 20, 25, 40, 50, 80, 100).random()
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
        val pairs = listOf(4, 5, 8, 10, 16, 20, 25, 40, 50, 64, 80, 100).flatMap { l ->
            listOf(4, 5, 10, 16, 20, 25, 40, 50, 80, 100).mapNotNull { c ->
                if (!(l == 40 && c == 10)) l to c else null
            }
        }
        val (lNh, cNf) = pairs.random()
        val esr = listOf(0.02, 0.05, 0.08, 0.1).random()
        val exactW = 1000.0 / sqrt(lNh.toDouble() * cNf.toDouble())
        wrapCuratedVariation(
            base = base,
            prompt = "A capacitor has parasitic ESL = $lNh nH in series with C = $cNf nF and ESR = $esr Ω. Find ω_SRF (in Mrad/s):",
            parametersLine1 = "C = $cNf nF,   ESL (L_s) = $lNh nH",
            parametersLine2 = "ESR = $esr Ω  (series parasitic model)",
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
        val r1 = listOf(200, 250, 400, 500, 800, 1000).random()
        val r2 = listOf(100, 200, 250, 400, 500).random()
        val r3 = listOf(400, 500, 800, 1000, 1200).random()
        val c1Uf = listOf(0.1, 0.2, 0.25, 0.4, 0.5, 0.8, 1.0).random()
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
        val rs = listOf(4, 5, 6, 8, 10, 12, 15, 20).random()
        val lsMh = listOf(10, 15, 20, 25, 30, 40, 50, 60, 80, 100).random()
        val q = listOf(2, 3, 4, 5, 8, 10).random()
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
        val y11Re = listOf(4.0, 5.0, 6.0, 7.0, 8.0, 10.0).random()
        val y11Im = listOf(-3.0, -2.0, 1.0, 2.0, 3.0, 4.0).random()
        val (sumRe, sumIm) = listOf(
            2.0 to 1.0, 1.0 to 2.0, 3.0 to 1.0, 1.0 to 3.0,
            4.0 to 2.0, 2.0 to 4.0, 3.0 to -1.0, 4.0 to -2.0
        ).random()
        val magSq = sumRe * sumRe + sumIm * sumIm
        val kMult = listOf(1, 2).random()
        val negProd = kMult * magSq
        val ylRe = listOf(1.0, 2.0).random()
        val ylIm = listOf(1.0, 2.0, -1.0).random()
        val y22Re = sumRe - ylRe
        val y22Im = sumIm - ylIm
        val fbRe = negProd * sumRe / magSq
        val fbIm = -negProd * sumIm / magSq
        val ans = ComplexNum(y11Re + fbRe, y11Im + fbIm)
        val y11Str = ComplexNum(y11Re, y11Im).formatRect()
        val y22Str = ComplexNum(y22Re, y22Im).formatRect()
        val ylStr = ComplexNum(ylRe, ylIm).formatRect()
        val sumStr = ComplexNum(sumRe, sumIm).formatRect()
        val sumC = ComplexNum(sumRe, sumIm)
        val fbC = ComplexNum(fbRe, fbIm)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "y₁₁ = $y11Str mS,   y₂₂ = $y22Str mS,   y₁₂ = −1 mS,   y₂₁ = ${negProd.toInt()} mS",
            parametersLine2 = "Load Y_L = $ylStr mS   (Y_in = y₁₁ − y₁₂y₂₁ / (y₂₂ + Y_L))",
            hints = listOf(
                "y₂₂ + Y_L = $sumStr mS.",
                "−y₁₂y₂₁ / ($sumStr) = ${negProd.toInt()} / ($sumStr) = ${fbC.formatRect()} mS."
            ),
            workedSteps = listOf(
                "1. y₂₂ + Y_L = $sumStr mS.",
                "2. Reflected term = ${negProd.toInt()} / ($sumStr) = ${fbC.formatRect()} mS.",
                "3. Y_in = ($y11Str) + (${fbC.formatRect()}) = ${ans.formatRect()} mS."
            ),
            expectedComplex = ans,
            steps = listOf(
                ProblemStep(
                    title = "Port-2 Load Sum",
                    prompt = "Step 1: Sum the output and load admittances y₂₂ + Y_L (in mS):",
                    methodNote = "Add real and imaginary parts of y₂₂ and Y_L.",
                    inputHint = "Enter y₂₂ + Y_L in mS (A + jB)",
                    answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
                    expectedComplex = sumC,
                    unit = "mS",
                    hints = listOf(
                        "($y22Str) + ($ylStr) = $sumStr mS."
                    ),
                    summaryText = "y₂₂ + Y_L = $sumStr mS"
                ),
                ProblemStep(
                    title = "Feedback Reflection",
                    prompt = "Step 2: Evaluate the reflected feedback term −y₁₂y₂₁ / (y₂₂ + Y_L) (in mS):",
                    methodNote = "Divide ${negProd.toInt()} by ($sumStr) via conjugate rationalization.",
                    inputHint = "Enter term in mS (A + jB)",
                    answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
                    expectedComplex = fbC,
                    unit = "mS",
                    hints = listOf(
                        "−y₁₂y₂₁ = ${negProd.toInt()}; |y₂₂ + Y_L|² = ${magSq.toInt()}."
                    ),
                    summaryText = "−y₁₂y₂₁/(y₂₂ + Y_L) = ${fbC.formatRect()} mS"
                )
            )
        )
    }

    "p_512d" -> {
        val v1 = listOf(10.0, 15.0, 20.0, 25.0, 30.0, 40.0).random()
        val v2 = listOf(10.0, 15.0, 20.0, 25.0, 30.0).random()
        val (gBase, bMag) = listOf(0.1 to 0.1, 0.2 to 0.1, 0.3 to 0.2, 0.2 to 0.2, 0.4 to 0.2, 0.3 to 0.1).random()
        val bSign = if (Random.nextBoolean()) 1.0 else -1.0
        val g1 = gBase; val g2 = gBase
        val b1 = bMag * bSign; val b2 = -b1
        val g3 = listOf(0.1, 0.2, 0.3, 0.4, 0.5, 0.6).random()
        val sumG = g1 + g2 + g3
        val iRe = v1 * g1 - v2 * b2
        val iIm = v1 * b1 + v2 * g2
        val ans = ComplexNum(iRe / sumG, iIm / sumG)
        val y1Str = ComplexNum(g1, b1).formatRect()
        val y2Str = ComplexNum(g2, b2).formatRect()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V₁ = ${v1.toInt()}∠0° V via Y₁ = ($y1Str) S;   V₂ = ${v2.toInt()}∠90° V via Y₂ = ($y2Str) S",
            parametersLine2 = "Shunt Load Y₃ = $g3 S (V₃ = 0 V)   (V_node = Σ(V_k Y_k) / Σ Y_k)",
            hints = listOf(
                "ΣY_k = ${ComplexNum.formatClean(sumG)} S (susceptances cancel).",
                "ΣV_k Y_k = ${ComplexNum(iRe, iIm).formatRect()} A."
            ),
            workedSteps = listOf(
                "1. ΣY_k = ${ComplexNum.formatClean(sumG)} S.",
                "2. ΣV_k Y_k = ${ComplexNum(iRe, iIm).formatRect()} A ⇒ V_node = ${ans.formatRect()} V."
            ),
            expectedComplex = ans
        )
    }

    "p_531j" -> {
        val cUf = listOf(0.1, 0.2, 0.25, 0.4, 0.5, 1.0, 2.0).random()
        val omega = listOf(1000, 2000, 4000, 5000, 10000).random()
        val dVal = listOf(0.002, 0.004, 0.005, 0.008, 0.01, 0.02).random()
        val bp = omega * cUf * 1e-6
        val gp = dVal * bp
        val rpK = 1.0 / (gp * 1000.0)
        val bpMs = bp * 1000.0
        val gpUs = gp * 1e6
        wrapCuratedVariation(
            base = base,
            prompt = "A film capacitor modeled as parallel C_p ∥ R_p has measured dissipation factor D at ω = $omega rad/s. Find parallel leakage resistance R_p (in kΩ):",
            parametersLine1 = "C_p = $cUf μF,   ω = $omega rad/s",
            parametersLine2 = "Dissipation Factor D = tan δ = $dVal  (parallel model)",
            hints = listOf(
                "B_p = ω·C_p = ${ComplexNum.formatClean(bpMs)} mS.",
                "G_p = D·B_p = ${ComplexNum.formatClean(gpUs)} μS ⇒ R_p = 1/G_p."
            ),
            workedSteps = listOf(
                "1. B_p = $omega × $cUf μF = ${ComplexNum.formatClean(bpMs)} mS.",
                "2. R_p = 1 / (ω·C_p·D) = ${ComplexNum.formatClean(rpK)} kΩ."
            ),
            expectedReal = rpK,
            steps = listOf(
                ProblemStep(
                    title = "Susceptance B_p",
                    prompt = "Step 1: Compute the capacitive susceptance B_p = ω · C_p (in mS):",
                    methodNote = "Multiply ω by C_p (in Farads) and scale by 10³ for millisiemens.",
                    inputHint = "Enter B_p in mS",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = bpMs,
                    unit = "mS",
                    hints = listOf(
                        "$omega × $cUf×10⁻⁶ = ${ComplexNum.formatClean(bpMs)} mS."
                    ),
                    summaryText = "B_p = ${ComplexNum.formatClean(bpMs)} mS"
                ),
                ProblemStep(
                    title = "Leakage Conductance",
                    prompt = "Step 2: Find the parallel leakage conductance G_p = D · B_p (in μS):",
                    methodNote = "Multiply D by B_p (in μS, where 1 mS = 1000 μS).",
                    inputHint = "Enter G_p in μS",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = gpUs,
                    unit = "μS",
                    hints = listOf(
                        "G_p = $dVal × ${ComplexNum.formatClean(bpMs * 1000.0)} μS = ${ComplexNum.formatClean(gpUs)} μS."
                    ),
                    summaryText = "G_p = ${ComplexNum.formatClean(gpUs)} μS"
                )
            )
        )
    }

    "p_532k" -> {
        val (rBase, xBase, numBase) = listOf(
            Triple(3, 4, 160),
            Triple(4, 3, 120)
        ).random()
        val kScale = listOf(1, 2, 4, 5, 8, 10).random()
        val isInd = Random.nextBoolean()
        val r = rBase * kScale
        val x = xBase * kScale
        val magZ = 5 * kScale
        val bShuntMs = (numBase.toDouble() / kScale) * (if (isInd) 1.0 else -1.0)
        val zLoadStr = if (isInd) "$r + j$x" else "$r − j$x"
        val blMs = -bShuntMs
        wrapCuratedVariation(
            base = base,
            prompt = "A complex ${if (isInd) "inductive" else "capacitive"} load Z_L = $zLoadStr Ω is matched with a parallel shunt jB_shunt so Y_in is purely real. Find B_shunt (in mS):",
            parametersLine1 = "Load Z_L = $zLoadStr Ω   (|Z_L| = $magZ Ω)",
            parametersLine2 = "Y_in = 1/Z_L + jB_shunt,   Im(Y_in) = 0",
            hints = listOf(
                "B_L = ${if (isInd) "−$x" else "+$x"} / ${magZ * magZ} S = ${ComplexNum.formatClean(blMs)} mS.",
                "Required cancelling shunt B_shunt = −B_L."
            ),
            workedSteps = listOf(
                "1. Y_L = 1/($zLoadStr) ⇒ B_L = ${ComplexNum.formatClean(blMs)} mS.",
                "2. B_shunt = −B_L = ${ComplexNum.formatClean(bShuntMs)} mS."
            ),
            expectedReal = bShuntMs
        )
    }

    "p_533l" -> {
        val (sumRe, sumIm) = listOf(
            10.0 to 0.0, 20.0 to 0.0, 5.0 to 0.0,
            10.0 to 10.0, 10.0 to -10.0, 5.0 to 5.0
        ).random()
        val magSq = sumRe * sumRe + sumIm * sumIm
        val (aRe, aIm) = listOf(
            -4.0 to 2.0, -3.0 to 1.0, -2.0 to 4.0, -5.0 to -2.0,
            2.0 to 3.0, -3.0 to -3.0, 1.0 to 4.0
        ).random()
        val ans = ComplexNum(aRe, aIm)
        // -y21 = ans * (sumRe + j*sumIm) => y21 = -ans * sum
        val negY21Re = aRe * sumRe - aIm * sumIm
        val negY21Im = aRe * sumIm + aIm * sumRe
        val y21 = ComplexNum(-negY21Re, -negY21Im)
        val ylRe = listOf(2.0, 3.0, 4.0, 6.0).random()
        val ylIm = listOf(-5.0, -3.0, 2.0, 4.0).random()
        val y22 = ComplexNum(sumRe - ylRe, sumIm - ylIm)
        val yLoad = ComplexNum(ylRe, ylIm)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "y₂₁ = ${y21.formatRect()} mS,   y₂₂ = ${y22.formatRect()} mS",
            parametersLine2 = "Load Y_L = ${yLoad.formatRect()} mS   (A_v = −y₂₁ / (y₂₂ + Y_L))",
            hints = listOf(
                "y₂₂ + Y_L = ${ComplexNum(sumRe, sumIm).formatRect()} mS (|y₂₂ + Y_L|² = ${magSq.toInt()}).",
                "A_v = (${ComplexNum(negY21Re, negY21Im).formatRect()}) / (${ComplexNum(sumRe, sumIm).formatRect()})."
            ),
            workedSteps = listOf(
                "1. y₂₂ + Y_L = ${ComplexNum(sumRe, sumIm).formatRect()} mS.",
                "2. A_v = −(${y21.formatRect()}) / (${ComplexNum(sumRe, sumIm).formatRect()}) = ${ans.formatRect()}."
            ),
            expectedComplex = ans
        )
    }

    // ── RESONANCE (16) ──
    "p_201a" -> {
        val (lH, cUf) = listOf(
            0.1 to 10, 0.4 to 10, 0.9 to 10, 0.16 to 10, 0.25 to 10,
            0.1 to 40, 0.4 to 40, 0.04 to 10, 0.2 to 5, 0.45 to 5,
            0.8 to 5, 0.2 to 20, 0.5 to 5, 0.25 to 40
        ).random()
        val r = listOf(4, 5, 8, 10, 15, 20, 25, 40, 50).random()
        val vs = listOf(50, 100, 120, 200).random()
        val q = (1.0 / r) * sqrt(lH / (cUf * 1e-6))
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω, L = ${lH}H, C = ${cUf}μF",
            parametersLine2 = "V_s = $vs V rms (variable ω)",
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
        val bw = listOf(25, 40, 50, 60, 75, 80, 100, 120, 150, 200).random()
        val q = listOf(15, 20, 25, 30, 40, 50, 60, 80, 100).random()
        val f0 = bw * q
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "f₀ = $f0 Hz",
            parametersLine2 = "Quality Factor Q = $q",
            hints = listOf("Q = f₀ / BW ⇒ BW = f₀ / Q.", "Compute $f0 / $q."),
            workedSteps = listOf("1. BW = $f0 / $q = $bw Hz."),
            expectedReal = bw.toDouble()
        )
    }

    "p_204b" -> {
        val (lMh, cUf) = listOf(
            1 to 10, 4 to 10, 9 to 10, 16 to 10, 25 to 10, 36 to 10,
            40 to 10, 100 to 10, 10 to 40, 25 to 40, 5 to 20, 20 to 5,
            8 to 50, 50 to 20, 2 to 50, 4 to 25
        ).random()
        val r = listOf(4, 5, 8, 10, 15, 20).random()
        val w0 = 1.0 / sqrt((lMh * 1e-3) * (cUf * 1e-6))
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "L = ${lMh}mH, C = ${cUf}μF",
            parametersLine2 = "R = ${r}Ω (series)",
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
        val iRes = listOf(2, 3, 4, 5, 6, 8, 10, 12).random()
        val r = listOf(2, 3, 4, 5, 6, 8, 10).random()
        val xC = listOf(30, 40, 45, 50, 60, 75, 80, 100, 120).random()
        val vs = iRes * r
        val vc = (iRes * xC).toDouble()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_s = $vs V rms, R = ${r}Ω",
            parametersLine2 = "X_L = X_C = ${xC}Ω at ω₀",
            hints = listOf(
                "Resonant current I = $vs / $r = $iRes A.",
                "|V_C| = I × $xC."
            ),
            workedSteps = listOf(
                "1. I = $vs / $r = $iRes A.",
                "2. |V_C| = $iRes · $xC = ${vc.toInt()} V."
            ),
            expectedReal = vc
        )
    }

    "p_222g" -> {
        val baseTriples = listOf(
            Triple(3, 4, 5), Triple(4, 3, 5),
            Triple(5, 12, 13), Triple(12, 5, 13),
            Triple(8, 15, 17), Triple(15, 8, 17),
            Triple(7, 24, 25), Triple(24, 7, 25),
            Triple(20, 21, 29), Triple(21, 20, 29)
        )
        val (bHalf, bW0, bHyp) = baseTriples.random()
        val scale = listOf(50, 100, 150, 200, 250).random()
        val halfBw = bHalf * scale
        val w0 = bW0 * scale
        val hyp = bHyp * scale
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
        val r = listOf(200, 250, 400, 500, 800, 1000, 2000).random()
        val cNf = listOf(20, 25, 40, 50, 80, 100, 125, 200, 250).random()
        val lMh = listOf(5, 10, 15, 20, 25, 40).random()
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
        val (lMh, cNf) = listOf(
            10 to 100, 10 to 250, 10 to 400, 10 to 900, 10 to 1000,
            25 to 100, 25 to 400, 40 to 100, 40 to 400, 40 to 1000,
            20 to 200, 20 to 500
        ).random()
        val rK = listOf(1, 2, 3, 4, 5, 6, 8, 10).random()
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
        val rs = listOf(2, 3, 4, 5, 6, 8, 10, 12, 15).random()
        val qCoil = listOf(2, 3, 4, 5, 6, 8, 10).random()
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
            expectedReal = zTank,
            steps = listOf(
                ProblemStep(
                    title = "Coil Q Factor",
                    prompt = "Step 1: Compute the series coil quality factor Q_coil = X_L / R_s:",
                    methodNote = "Divide X_L = $xl Ω by R_s = $rs Ω.",
                    inputHint = "Enter dimensionless Q_coil",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = qCoil.toDouble(),
                    unit = "",
                    hints = listOf(
                        "Q_coil = $xl / $rs = $qCoil."
                    ),
                    summaryText = "Q_coil = $qCoil"
                )
            )
        )
    }

    "p_228h" -> {
        val m = listOf(1, 2, 3, 4).random()
        val n = listOf(1, 2, 3, 4, 5).random()
        val step = listOf(2, 3, 4, 5, 6).random()
        val qMult = listOf(2, 3, 4, 5, 6, 8).random()
        val rTankK = m * step
        val rSrcK = n * step
        val q0 = qMult * (m + n)
        val rEq = (rTankK.toDouble() * rSrcK) / (rTankK + rSrcK)
        val qL = (qMult * n).toDouble()
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
            expectedReal = qL,
            steps = listOf(
                ProblemStep(
                    title = "Loaded Tank Resistance",
                    prompt = "Step 1: Find the effective parallel tank resistance R_eq = R_tank ∥ R_s (in kΩ):",
                    methodNote = "Use R_eq = ($rTankK · $rSrcK) / ($rTankK + $rSrcK).",
                    inputHint = "Enter R_eq in kΩ",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = rEq,
                    unit = "kΩ",
                    hints = listOf(
                        "R_eq = ($rTankK × $rSrcK) / ${rTankK + rSrcK} = ${ComplexNum.formatClean(rEq)} kΩ."
                    ),
                    summaryText = "R_eq = ${ComplexNum.formatClean(rEq)} kΩ"
                )
            )
        )
    }

    "p_230i" -> {
        val rS = listOf(5, 10, 15, 20, 25, 40, 50).random()
        val q = listOf(1, 2, 3, 4, 5, 6).random()
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
        val valid = listOf(1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0).flatMap { iS ->
            listOf(200, 300, 400, 480, 500, 600, 800).flatMap { r ->
                listOf(20, 25, 40, 50, 60, 80, 100).mapNotNull { xL ->
                    if ((iS * r).toInt() % xL == 0) Triple(iS, r, xL) else null
                }
            }
        }
        val (iS, r, xL) = valid.random()
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
        val pairs = listOf(8, 10, 12, 16, 20, 24, 25, 30, 40, 50).flatMap { root ->
            val sq = root * root
            listOf(4, 5, 8, 10, 12, 16, 20, 25, 30, 40, 50, 80, 100).mapNotNull { q1 ->
                val q2 = sq / q1
                if (sq % q1 == 0 && q2 in 4..100) Triple(q1, q2, root) else null
            }
        }
        val (q1, q2, prodSqrt) = pairs.random()
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

    "p_214d" -> {
        val valid = listOf(1000, 2000, 2500, 3000, 4000, 5000, 8000).flatMap { w0 ->
            listOf(10, 15, 20, 24, 30, 32, 40, 45, 50, 60).flatMap { eMj ->
                listOf(1.0, 1.5, 2.0, 2.5, 4.0, 5.0).mapNotNull { pAvg ->
                    val q = (w0 * (eMj * 1e-3)) / pAvg
                    if (q in 10.0..120.0 && kotlin.math.abs(q - kotlin.math.round(q)) < 1e-9) Triple(w0, eMj, pAvg) else null
                }
            }
        }
        val (w0, eMj, pAvg) = valid.random()
        val qVal = w0 * (eMj * 1e-3) / pAvg
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "ω₀ = $w0 rad/s,   E_stored = $eMj mJ",
            parametersLine2 = "P_avg = $pAvg W   (Q = ω₀ · E_stored / P_avg)",
            hints = listOf(
                "E_stored = ${ComplexNum.formatClean(eMj * 1e-3)} J.",
                "Q = ($w0 × ${ComplexNum.formatClean(eMj * 1e-3)}) / $pAvg."
            ),
            workedSteps = listOf(
                "1. Q = $w0 × ${ComplexNum.formatClean(eMj * 1e-3)} / $pAvg = ${ComplexNum.formatClean(qVal)}."
            ),
            expectedReal = qVal
        )
    }

    "p_226h" -> {
        val valid = listOf(
            listOf(4, 3, 5),     // 2Q*delta = 0.75 (3/4)
            listOf(5, 12, 13),   // 2Q*delta = 2.4  (12/5)
            listOf(8, 15, 17),   // 2Q*delta = 1.875 (15/8)
            listOf(20, 21, 29)   // 2Q*delta = 1.05 (21/20)
        ).flatMap { py ->
            val rBase = py[0]; val num = py[1]; val zBase = py[2]
            listOf(2, 3, 4, 5, 6).flatMap { rMult ->
                listOf(20, 25, 40, 50, 80, 100).flatMap { q ->
                    listOf(10, 20, 40, 50).mapNotNull { f0Khz ->
                        val dfNum = num * f0Khz * 1000
                        val dfDen = 2 * rBase * q
                        if (dfNum % dfDen == 0) {
                            listOf(rBase * rMult, q, f0Khz, dfNum / dfDen, zBase * rMult)
                        } else null
                    }
                }
            }
        }
        val pick = valid.random()
        val r = pick[0]; val q = pick[1]; val f0Khz = pick[2]
        val dfSign = if (Random.nextBoolean()) 1 else -1
        val dfHz = pick[3] * dfSign
        val zMag = pick[4]
        val fDriveKhz = f0Khz + dfHz / 1000.0
        val fDriveStr = ComplexNum.formatClean(fDriveKhz)
        val delta = dfHz / (f0Khz * 1000.0)
        val u = 2.0 * q * delta
        val detuneWord = if (dfSign > 0) "above" else "below"
        wrapCuratedVariation(
            base = base,
            prompt = "A high-Q series RLC circuit (R = $r Ω, Q = $q at f₀ = $f0Khz kHz) is driven slightly $detuneWord resonance at f = $fDriveStr kHz. Find impedance magnitude |Z| (in Ω):",
            parametersLine1 = "R = $r Ω,   Q = $q,   f₀ = $f0Khz kHz",
            parametersLine2 = "Drive f = $fDriveStr kHz  (δ = Δf/f₀,  |Z| ≈ R√(1 + (2Qδ)²))",
            hints = listOf(
                "Fractional detuning δ = $dfHz / ${f0Khz * 1000} = ${ComplexNum.formatClean(delta)}.",
                "2Qδ = ${ComplexNum.formatClean(u)}, so |Z| = $r × √(1 + ${ComplexNum.formatClean(u)}²)."
            ),
            workedSteps = listOf(
                "1. δ = $dfHz / ${f0Khz * 1000} = ${ComplexNum.formatClean(delta)} ⇒ 2Qδ = ${ComplexNum.formatClean(u)}.",
                "2. |Z| = $r√(1 + (${ComplexNum.formatClean(u)})²) = $zMag Ω."
            ),
            expectedReal = zMag.toDouble(),
            steps = listOf(
                ProblemStep(
                    title = "Normalized Detuning",
                    prompt = "Step 1: Compute the signed normalized detuning factor 2Qδ = 2Q(f − f₀) / f₀:",
                    methodNote = "Find Δf = f − f₀, compute δ = Δf / f₀, and multiply by 2Q.",
                    inputHint = "Enter scalar 2Qδ",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = u,
                    unit = "",
                    hints = listOf(
                        "δ = $dfHz / ${f0Khz * 1000} = ${ComplexNum.formatClean(delta)}.",
                        "2Qδ = 2 × $q × (${ComplexNum.formatClean(delta)}) = ${ComplexNum.formatClean(u)}."
                    ),
                    summaryText = "2Qδ = ${ComplexNum.formatClean(u)}"
                )
            )
        )
    }

    "p_227i" -> {
        val (f1Base, f2Base, f0Base) = listOf(
            Triple(4, 9, 6), Triple(4, 16, 8), Triple(9, 16, 12),
            Triple(9, 25, 15), Triple(16, 25, 20), Triple(4, 25, 10),
            Triple(16, 36, 24), Triple(25, 36, 30), Triple(9, 49, 21), Triple(16, 49, 28)
        ).random()
        val kScale = listOf(1, 2, 5, 10).random()
        val f1 = f1Base * kScale
        val f2 = f2Base * kScale
        val f0 = f0Base * kScale
        val askF0 = Random.nextBoolean()
        if (askF0) {
            wrapCuratedVariation(
                base = base,
                prompt = "A tuned RLC bandpass filter exhibits lower and upper −3 dB frequencies f₁ = $f1 kHz and f₂ = $f2 kHz. Find exact center resonance f₀ (in kHz):",
                parametersLine1 = "Lower −3 dB: f₁ = $f1 kHz,   Upper −3 dB: f₂ = $f2 kHz",
                parametersLine2 = "Geometric resonance symmetry: f₀ = √(f₁ · f₂)",
                hints = listOf(
                    "Resonance uses the geometric mean √(f₁ · f₂), not (f₁ + f₂)/2.",
                    "Compute √($f1 × $f2) = √${f1 * f2}."
                ),
                workedSteps = listOf(
                    "1. f₀ = √(f₁ · f₂) = √($f1 × $f2) = $f0 kHz."
                ),
                expectedReal = f0.toDouble()
            )
        } else {
            wrapCuratedVariation(
                base = base,
                prompt = "A tuned RLC bandpass filter centered at f₀ = $f0 kHz has upper −3 dB edge f₂ = $f2 kHz. Find its exact lower −3 dB frequency f₁ (in kHz):",
                parametersLine1 = "Center f₀ = $f0 kHz,   Upper −3 dB: f₂ = $f2 kHz",
                parametersLine2 = "Geometric resonance symmetry: f₁ · f₂ = f₀²",
                hints = listOf(
                    "Use geometric symmetry f₁ = f₀² / f₂.",
                    "Compute $f0² / $f2 = ${f0 * f0} / $f2."
                ),
                workedSteps = listOf(
                    "1. f₁ = f₀² / f₂ = ${f0 * f0} / $f2 = $f1 kHz."
                ),
                expectedReal = f1.toDouble()
            )
        }
    }

    "p_228j" -> {
        val (cmNum, c0Den, fpFactor) = listOf(
            Triple(21, 100, 1.1),
            Triple(44, 100, 1.2),
            Triple(69, 100, 1.3),
            Triple(9, 16, 1.25),
            Triple(17, 64, 1.125)
        ).random()
        val fsKhz = listOf(400, 800, 1000, 1600, 2000, 4000).random()
        val c0Pf = c0Den / 10.0
        val cmPf = cmNum / 10.0
        val fpKhz = fsKhz * fpFactor
        val askDelta = Random.nextBoolean()
        if (!askDelta) {
            wrapCuratedVariation(
                base = base,
                prompt = "A piezoelectric crystal resonator has motional series resonance f_s = $fsKhz kHz with motional capacitance C_m in parallel with holder C₀. Find parallel antiresonant frequency f_p (in kHz):",
                parametersLine1 = "f_s = $fsKhz kHz,   C_m = ${ComplexNum.formatClean(cmPf)} pF",
                parametersLine2 = "Holder shunt C₀ = ${ComplexNum.formatClean(c0Pf)} pF   (f_p = f_s √(1 + C_m / C₀))",
                hints = listOf(
                    "C_m / C₀ = ${ComplexNum.formatClean(cmPf)} / ${ComplexNum.formatClean(c0Pf)} = ${ComplexNum.formatClean(cmNum.toDouble() / c0Den)}.",
                    "√(1 + C_m/C₀) = $fpFactor ⇒ f_p = $fsKhz × $fpFactor."
                ),
                workedSteps = listOf(
                    "1. √(1 + C_m / C₀) = $fpFactor.",
                    "2. f_p = $fsKhz × $fpFactor = ${ComplexNum.formatClean(fpKhz)} kHz."
                ),
                expectedReal = fpKhz
            )
        } else {
            val deltaKhz = fpKhz - fsKhz
            wrapCuratedVariation(
                base = base,
                prompt = "A piezoelectric crystal resonator has motional f_s = $fsKhz kHz, C_m = ${ComplexNum.formatClean(cmPf)} pF, and holder C₀ = ${ComplexNum.formatClean(c0Pf)} pF. Find pole-zero separation Δf = f_p − f_s (in kHz):",
                parametersLine1 = "f_s = $fsKhz kHz,   C_m = ${ComplexNum.formatClean(cmPf)} pF,   C₀ = ${ComplexNum.formatClean(c0Pf)} pF",
                parametersLine2 = "f_p = f_s √(1 + C_m / C₀),   Δf = f_p − f_s",
                hints = listOf(
                    "First find f_p = $fsKhz × √(1 + ${ComplexNum.formatClean(cmNum.toDouble() / c0Den)}) = ${ComplexNum.formatClean(fpKhz)} kHz.",
                    "Subtract f_s = $fsKhz kHz."
                ),
                workedSteps = listOf(
                    "1. f_p = $fsKhz × $fpFactor = ${ComplexNum.formatClean(fpKhz)} kHz.",
                    "2. Δf = ${ComplexNum.formatClean(fpKhz)} − $fsKhz = ${ComplexNum.formatClean(deltaKhz)} kHz."
                ),
                expectedReal = deltaKhz
            )
        }
    }

    else -> null
}
