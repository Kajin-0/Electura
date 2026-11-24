package com.example.data

import com.example.model.AnswerKind
import com.example.model.CircuitProblem
import com.example.model.CircuitTopology
import com.example.model.ComplexNum
import com.example.model.Difficulty
import com.example.model.ProblemCategory
import kotlin.random.Random

internal fun generateLaplaceDrill(
    drillId: String,
    drillNum: Int,
    preferredDifficulty: Difficulty?,
    excluded: Set<String>
): CircuitProblem = when (
    pickTemplateIndex(
        listOf(
            "Procedural Routh Margin" to Difficulty.MASTERY,
            "Procedural Final Value Theorem" to Difficulty.INTERMEDIATE,
            "Procedural Damping Ratio ζ" to Difficulty.INTERMEDIATE,
            "Procedural Initial Value Theorem" to Difficulty.INTERMEDIATE,
            "Procedural RLC Initial Slope" to Difficulty.ADVANCED,
            "Procedural RC Time Constant τ" to Difficulty.FOUNDATIONS,
            "Procedural Critical Damping R" to Difficulty.ADVANCED,
            "Procedural Underdamped ω_d & t_p" to Difficulty.ADVANCED,
            "Procedural Bandpass Q from H(s)" to Difficulty.INTERMEDIATE,
            "Procedural Steady-State Error" to Difficulty.INTERMEDIATE,
            "Procedural Bode Gain Margin" to Difficulty.MASTERY
        ),
        preferredDifficulty,
        excluded
    )
) {
    0 -> {
        val a2 = Random.nextInt(2, 7)
        val a1 = Random.nextInt(2, 8)
        val kCrit = a2 * a1
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Routh Margin",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.FEEDBACK_LOOP,
            prompt = "Find the critical positive feedback gain K for marginal closed-loop AC oscillation in a 3-pole regulator:",
            parametersLine1 = "1 + G(s)H(s) = 0",
            parametersLine2 = "s³ + ${a2}s² + ${a1}s + K = 0",
            methodTitle = "ROUTH-HURWITZ DRILL",
            methodNote = "Apply the cubic Routh-Hurwitz stability criterion: a₂·a₁ = K_crit.",
            inputHint = "Enter critical gain K",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = kCrit.toDouble(),
            unit = "",
            hints = listOf(
                "For s³ + a₂s² + a₁s + K = 0, the s¹ row vanishes when a₂·a₁ − K = 0.",
                "Compute $a2 × $a1."
            ),
            workedSteps = listOf(
                "1. Identify a₂ = $a2 and a₁ = $a1.",
                "2. Marginal stability occurs at K_crit = a₂·a₁ = $a2 × $a1 = $kCrit."
            ),
            isProcedural = true,
            acContext = "Routh–Hurwitz pinpoints the exact loop gain where an op-amp or switching regulator breaks into sustained AC oscillation."
        )
    }
    1 -> {
        val k = listOf(6, 12, 18, 24).random()
        val z = listOf(2, 3, 4, 5).random()
        val p1 = listOf(1, 2, 3).random()
        val p2 = listOf(2, 4, 6).random()
        val yInf = (k * z).toDouble() / (p1 * p2).toDouble()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Final Value Theorem",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.FEEDBACK_LOOP,
            prompt = "Using the Laplace Final Value Theorem, find the settled steady-state output y(∞) of a switched RLC/regulator stage:",
            parametersLine1 = "Y(s) = $k(s + $z) / [ s(s + $p1)(s + $p2) ]",
            parametersLine2 = "All non-zero poles in open LHP",
            methodTitle = "FINAL VALUE DRILL",
            methodNote = "Cancel the s in the denominator and evaluate the remaining rational expression at s = 0.",
            inputHint = "Enter y(∞)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = yInf,
            unit = "",
            hints = listOf(
                "s·Y(s) = $k(s + $z) / ((s + $p1)(s + $p2)).",
                "Substitute s = 0: ($k × $z) / ($p1 × $p2)."
            ),
            workedSteps = listOf(
                "1. All non-zero poles (−$p1, −$p2) are strictly in the LHP, so FVT applies.",
                "2. y(∞) = ($k × $z) / ($p1 × $p2) = ${ComplexNum.formatClean(yInf)}."
            ),
            isProcedural = true,
            acContext = "Predicts settled output voltage after AC switching transients decay without inverting the full s-domain fraction."
        )
    }
    2 -> {
        val alpha = listOf(2, 3, 4, 6, 8).random()
        val omega0 = listOf(4, 5, 8, 10).random()
        val zeta = alpha.toDouble() / omega0.toDouble()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Damping Ratio ζ",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Find the dimensionless damping ratio ζ = α/ω₀ of the 2nd-order RLC filter characteristic polynomial:",
            parametersLine1 = "s² + ${2 * alpha}s + ${omega0 * omega0} = 0",
            parametersLine2 = "Standard Form: s² + 2ζω₀s + ω₀² = 0",
            methodTitle = "SECOND-ORDER POLES DRILL",
            methodNote = "Identify ω₀ = √c and compute ζ = b / (2ω₀).",
            inputHint = "Enter damping ratio ζ",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = zeta,
            unit = "",
            hints = listOf(
                "ω₀² = ${omega0 * omega0} ⇒ ω₀ = $omega0 rad/s.",
                "2ζω₀ = ${2 * alpha} ⇒ ζ = ${2 * alpha} / (2 × $omega0)."
            ),
            workedSteps = listOf(
                "1. ω₀ = √${omega0 * omega0} = $omega0 rad/s; attenuation α = ${2 * alpha}/2 = $alpha Np/s.",
                "2. ζ = α / ω₀ = $alpha / $omega0 = ${ComplexNum.formatClean(zeta)}."
            ),
            isProcedural = true,
            acContext = "Damping ratio ζ determines whether an RLC AC filter exhibits sharp resonant peaking (ζ < 0.707) or smooth roll-off."
        )
    }
    3 -> {
        val k = listOf(4, 8, 12, 15, 20, 25).random()
        val z = listOf(2, 3, 5, 7).random()
        val p1 = listOf(1, 2, 4).random()
        val p2 = listOf(3, 5, 6).random()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Initial Value Theorem",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.LOW_PASS_RC,
            prompt = "Using the Laplace Initial Value Theorem, find the instantaneous switch-on output f(0⁺) of an AC filter/compensator F(s):",
            parametersLine1 = "F(s) = $k(s + $z) / ((s + $p1)(s + $p2))",
            parametersLine2 = "Strictly proper rational transform",
            methodTitle = "INITIAL VALUE DRILL",
            methodNote = "Multiply F(s) by s and take the ratio of leading highest-power coefficients as s → ∞.",
            inputHint = "Enter f(0+)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = k.toDouble(),
            unit = "",
            hints = listOf(
                "s·F(s) = ($k s² + ${k * z}s) / (s² + ${p1 + p2}s + ${p1 * p2}).",
                "Divide numerator & denominator by s² and let s → ∞."
            ),
            workedSteps = listOf(
                "1. Degree of s·F(s) numerator (s²) equals denominator (s²).",
                "2. Taking leading coefficients: f(0⁺) = $k / 1 = $k."
            ),
            isProcedural = true,
            acContext = "Reveals instantaneous t = 0⁺ step jumps across capacitive feedforward paths before any inductive current can build up."
        )
    }
    4 -> {
        val vs = listOf(12, 24, 30, 48).random()
        val vc0 = listOf(0, 4, 6, 8).random()
        val r = listOf(5, 10, 20).random()
        val l = listOf(0.1, 0.2, 0.25, 0.5).random()
        val diDt = (vs - vc0).toDouble() / l
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural RLC Initial Slope",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "A series RLC with i(0⁻) = 0 and v_C(0⁻) = ${vc0}V switches onto V_s = ${vs}V at t = 0. Find di/dt(0⁺):",
            parametersLine1 = "V_s = $vs V step, R = ${r}Ω, L = $l H",
            parametersLine2 = "i(0⁻) = 0 A, v_C(0⁻) = $vc0 V",
            methodTitle = "RLC INITIAL SLOPE DRILL",
            methodNote = "By state continuity, i(0⁺) = 0 and v_C(0⁺) = $vc0 V, so L·di/dt(0⁺) = V_s − v_C(0⁻).",
            inputHint = "Enter di/dt(0⁺) in A/s",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = diDt,
            unit = "A/s",
            hints = listOf(
                "Zero initial current means resistor drop i(0⁺)R = 0 V.",
                "Compute ($vs − $vc0) / $l."
            ),
            workedSteps = listOf(
                "1. KVL at t = 0⁺: $l · di/dt(0⁺) = $vs − $vc0 = ${vs - vc0} V.",
                "2. di/dt(0⁺) = ${vs - vc0} / $l = ${ComplexNum.formatClean(diDt)} A/s."
            ),
            isProcedural = true,
            acContext = "State continuity at t = 0⁺ determines peak di/dt stress on switches, diodes, and inductors during AC/DC power-up."
        )
    }
    5 -> {
        val rcPairs = listOf(
            Pair(5, 20), Pair(5, 40), Pair(10, 10), Pair(10, 20),
            Pair(10, 25), Pair(10, 50), Pair(20, 25), Pair(25, 20), Pair(40, 25), Pair(50, 20)
        )
        val pair = rcPairs.random()
        val rK = pair.first
        val cUf = pair.second
        val tauSec = (rK * cUf) / 1000.0
        val tauMs = rK * cUf
        val omegaC = 1000.0 / (rK * cUf).toDouble()
        val mode = Random.nextInt(3)
        when (mode) {
            0 -> CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural RC Time Constant τ",
                category = ProblemCategory.LAPLACE,
                difficulty = Difficulty.FOUNDATIONS,
                topology = CircuitTopology.SERIES_RC,
                prompt = "Compute the exponential time constant τ = R·C (in seconds) for the RC network:",
                parametersLine1 = "R = $rK kΩ,   C = $cUf μF",
                parametersLine2 = "τ = R · C",
                methodTitle = "TIME CONSTANT DRILL",
                methodNote = "Multiply R (×10³ Ω) by C (×10⁻⁶ F); kΩ × μF yields milliseconds (×10⁻³ s).",
                inputHint = "Enter τ in seconds",
                answerKind = AnswerKind.REAL_SCALAR,
                expectedReal = tauSec,
                unit = "s",
                hints = listOf("($rK × 10³) × ($cUf × 10⁻⁶) = $tauMs × 10⁻³ s.", "Divide $tauMs by 1000."),
                workedSteps = listOf("1. τ = ($rK×10³) × ($cUf×10⁻⁶) = ${ComplexNum.formatClean(tauSec)} s."),
                isProcedural = true,
                acContext = "Determines the −3 dB AC cutoff frequency ω_c = 1/τ and phase lag of first-order RC low-pass filters."
            )
            1 -> CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural RC Time Constant τ",
                category = ProblemCategory.LAPLACE,
                difficulty = Difficulty.FOUNDATIONS,
                topology = CircuitTopology.SERIES_RC,
                prompt = "An RC stage with R = $rK kΩ has time constant τ = $tauMs ms. Find capacitance C (in μF):",
                parametersLine1 = "R = $rK kΩ,   Time Constant τ = $tauMs ms",
                parametersLine2 = "C = τ / R",
                methodTitle = "CAPACITANCE FROM τ",
                methodNote = "Divide time constant τ (ms) by resistance R (kΩ) — units ms / kΩ equal μF directly!",
                inputHint = "Enter C in μF",
                answerKind = AnswerKind.REAL_SCALAR,
                expectedReal = cUf.toDouble(),
                unit = "μF",
                hints = listOf("τ = ${ComplexNum.formatClean(tauSec)} s and R = ${rK * 1000} Ω.", "C = $tauMs ms / $rK kΩ = $cUf μF."),
                workedSteps = listOf("1. C = τ / R = $tauMs ms / $rK kΩ = $cUf μF."),
                isProcedural = true,
                acContext = "Sizing C for a target τ sets both switching snubber duration and AC filter corner frequency ω_c = 1/τ."
            )
            else -> CircuitProblem(
                id = drillId,
                code = "Drill #$drillNum",
                title = "Procedural RC Time Constant τ",
                category = ProblemCategory.LAPLACE,
                difficulty = Difficulty.FOUNDATIONS,
                topology = CircuitTopology.LOW_PASS_RC,
                prompt = "Find the −3 dB corner angular frequency ω_c = 1 / (R·C) (in rad/s) of this first-order RC filter:",
                parametersLine1 = "R = $rK kΩ,   C = $cUf μF",
                parametersLine2 = "ω_c = 1 / τ = 1 / (R·C)",
                methodTitle = "CUTOFF FROM TIME CONSTANT",
                methodNote = "Compute τ = R·C in seconds, then invert ω_c = 1 / τ.",
                inputHint = "Enter ω_c in rad/s",
                answerKind = AnswerKind.REAL_SCALAR,
                expectedReal = omegaC,
                unit = "rad/s",
                hints = listOf("τ = $rK kΩ × $cUf μF = ${ComplexNum.formatClean(tauSec)} s.", "ω_c = 1 / ${ComplexNum.formatClean(tauSec)}."),
                workedSteps = listOf(
                    "1. τ = R·C = ${ComplexNum.formatClean(tauSec)} s.",
                    "2. ω_c = 1 / τ = ${ComplexNum.formatClean(omegaC)} rad/s."
                ),
                isProcedural = true,
                acContext = "At AC frequency ω = ω_c, output amplitude attenuates by −3 dB (0.707×) with a −45° phase lag."
            )
        }
    }
    6 -> {
        val combos = listOf(
            Triple(10, 100, 10),
            Triple(40, 100, 20),
            Triple(20, 50, 20),
            Triple(90, 100, 30),
            Triple(45, 50, 30),
            Triple(160, 100, 40),
            Triple(25, 10, 50),
            Triple(100, 40, 50)
        )
        val pick = combos.random()
        val lMh = pick.first
        val cUf = pick.second
        val rootZ0 = pick.third
        val rCrit = (2 * rootZ0).toDouble()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Critical Damping R",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Find series resistance R_crit (in Ω) to make this RLC network critically damped (ζ = 1):",
            parametersLine1 = "L = $lMh mH,   C = $cUf μF",
            parametersLine2 = "Critical Damping: R_crit = 2√(L / C)",
            methodTitle = "CRITICAL DAMPING DRILL",
            methodNote = "Convert L (×10⁻³ H) and C (×10⁻⁶ F), divide L/C, take √(L/C), and multiply by 2.",
            inputHint = "Enter R_crit in Ω",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = rCrit,
            unit = "Ω",
            hints = listOf(
                "L / C = ($lMh × 10⁻³) / ($cUf × 10⁻⁶) = ${rootZ0 * rootZ0}.",
                "R_crit = 2 × $rootZ0 = ${rCrit.toInt()} Ω."
            ),
            workedSteps = listOf(
                "1. √(L / C) = √${rootZ0 * rootZ0} = $rootZ0 Ω.",
                "2. R_crit = 2 × $rootZ0 = ${rCrit.toInt()} Ω."
            ),
            isProcedural = true,
            acContext = "Sets the exact damping resistance R_crit = 2√(L/C) needed to eliminate LC filter ringing without wasting excess power."
        )
    }
    7 -> {
        val (w0, zeta, rootComp) = listOf(
            Triple(50, 0.6, 0.8),
            Triple(25, 0.6, 0.8),
            Triple(100, 0.8, 0.6),
            Triple(200, 0.6, 0.8),
            Triple(125, 0.8, 0.6)
        ).random()
        val wd = w0 * rootComp
        val tpMs = 1000.0 * Math.PI / wd
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Underdamped ω_d & t_p",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "An underdamped 2nd-order system has ω₀ = $w0 rad/s and damping ratio ζ = $zeta. Find peak overshoot time t_p (in ms):",
            parametersLine1 = "ω₀ = $w0 rad/s,   ζ = $zeta  (underdamped)",
            parametersLine2 = "ω_d = ω₀√(1 − ζ²),   t_p = π / ω_d",
            methodTitle = "PEAK OVERSHOOT DRILL",
            methodNote = "Compute ω_d = ω₀√(1 − ζ²) and invert t_p = π / ω_d, converting seconds to ms (×1000).",
            inputHint = "Enter t_p in ms",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = tpMs,
            tolerance = 0.03,
            unit = "ms",
            hints = listOf(
                "√(1 − $zeta²) = $rootComp ⇒ ω_d = ${ComplexNum.formatClean(wd)} rad/s.",
                "t_p = 1000π / ${ComplexNum.formatClean(wd)} ms."
            ),
            workedSteps = listOf(
                "1. ω_d = $w0 × $rootComp = ${ComplexNum.formatClean(wd)} rad/s.",
                "2. t_p = π / ${ComplexNum.formatClean(wd)} s ≈ ${ComplexNum.formatClean(tpMs)} ms."
            ),
            isProcedural = true,
            acContext = "Identifies the exact time t_p when an underdamped LC power bus hits peak overshoot voltage during load transients."
        )
    }
    8 -> {
        val (w0, bw, kGain) = listOf(
            Triple(40, 8, 5),
            Triple(30, 5, 3),
            Triple(50, 10, 2),
            Triple(60, 15, 4),
            Triple(80, 10, 5),
            Triple(100, 25, 2)
        ).random()
        val qVal = w0.toDouble() / bw
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Bandpass Q from H(s)",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.FEEDBACK_LOOP,
            prompt = "Extract the quality factor Q of this second-order bandpass filter directly from H(s):",
            parametersLine1 = "H(s) = ${kGain * bw}s / (s² + ${bw}s + ${w0 * w0})",
            parametersLine2 = "Canonical Form: K·(ω₀/Q)s / (s² + (ω₀/Q)s + ω₀²)",
            methodTitle = "BANDPASS H(s) DRILL",
            methodNote = "Read ω₀ = √c and BW = b = ω₀/Q, then divide Q = ω₀ / BW.",
            inputHint = "Enter scalar Q",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = qVal,
            unit = "",
            hints = listOf(
                "ω₀ = √${w0 * w0} = $w0 rad/s.",
                "BW = $bw rad/s ⇒ Q = $w0 / $bw."
            ),
            workedSteps = listOf(
                "1. ω₀ = $w0 rad/s, BW = $bw rad/s.",
                "2. Q = $w0 / $bw = ${ComplexNum.formatClean(qVal)}."
            ),
            isProcedural = true,
            acContext = "Bandwidth BW = ω₀/Q extracted from H(s) directly sets selectivity of IF bandpass filters and resonant AC tanks."
        )
    }
    9 -> {
        val (kGain, pPole) = listOf(20 to 4, 20 to 5, 30 to 6, 40 to 8, 50 to 5).random()
        val kv = kGain.toDouble() / pPole
        val ess = 1.0 / kv
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Steady-State Error",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.FEEDBACK_LOOP,
            prompt = "Find unit-ramp steady-state tracking error e_ss for a unity-feedback Type-1 PLL/power-converter loop G(s):",
            parametersLine1 = "G(s) = $kGain / (s(s + $pPole))",
            parametersLine2 = "Unit ramp r(t) = t·u(t) ⇒ K_v = lim(s→0) s·G(s),  e_ss = 1/K_v",
            methodTitle = "STEADY-STATE ERROR DRILL",
            methodNote = "Evaluate K_v = $kGain / $pPole and invert e_ss = 1 / K_v.",
            inputHint = "Enter e_ss",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = ess,
            unit = "",
            hints = listOf(
                "K_v = $kGain / $pPole = ${ComplexNum.formatClean(kv)}.",
                "e_ss = 1 / ${ComplexNum.formatClean(kv)}."
            ),
            workedSteps = listOf(
                "1. K_v = ${ComplexNum.formatClean(kv)} s⁻¹ ⇒ e_ss = ${ComplexNum.formatClean(ess)}."
            ),
            isProcedural = true,
            acContext = "Type-1 integral compensators eliminate DC step errors and keep ramp/phase-tracking error inversely proportional to K_v."
        )
    }
    else -> {
        val (a, b, wPc) = listOf(Triple(1, 4, 2), Triple(1, 9, 3), Triple(2, 8, 4)).random()
        val denMag = a * b * (a + b)
        val gMag = listOf(0.1, 0.25, 0.5).random()
        val kGain = denMag * gMag
        val gmDb = -20.0 * kotlin.math.log10(gMag)
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Bode Gain Margin",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.FEEDBACK_LOOP,
            prompt = "Find Gain Margin GM (in dB) at −180° phase crossover ω_pc = $wPc rad/s for unity-feedback op-amp/regulator loop G(s):",
            parametersLine1 = "G(s) = ${ComplexNum.formatClean(kGain)} / (s(s + $a)(s + $b))",
            parametersLine2 = "∠G(j$wPc) = −180°,   GM_dB = −20 log₁₀(|G(jω_pc)|)",
            methodTitle = "GAIN MARGIN DRILL",
            methodNote = "At ω_pc = √($a·$b), |G(jω_pc)| = K / (a·b·(a+b)) = $gMag.",
            inputHint = "Enter GM in dB",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = gmDb,
            unit = "dB",
            hints = listOf(
                "|G(j$wPc)| = ${ComplexNum.formatClean(kGain)} / $denMag = $gMag.",
                "GM_dB = −20 log₁₀($gMag)."
            ),
            workedSteps = listOf(
                "1. |G(j$wPc)| = $gMag ⇒ GM_dB = ${ComplexNum.formatClean(gmDb)} dB."
            ),
            isProcedural = true,
            acContext = "Gain margin tells circuit designers how many dB of extra loop gain an amplifier or AC power stage can tolerate before oscillating."
        )
    }
}

internal fun generateComplexAnalysisDrill(
    drillId: String,
    drillNum: Int,
    preferredDifficulty: Difficulty?,
    excluded: Set<String>
): CircuitProblem = when (
    pickTemplateIndex(
        listOf(
            "Procedural Simple Pole Residue" to Difficulty.ADVANCED,
            "Procedural Smith Chart Γ" to Difficulty.ADVANCED,
            "Procedural Cauchy-Riemann Constant" to Difficulty.MASTERY,
            "Procedural Closed Contour Integral" to Difficulty.ADVANCED,
            "Procedural Bilinear Map s → z" to Difficulty.ADVANCED,
            "Procedural Euler Polar-to-Rect" to Difficulty.FOUNDATIONS,
            "Procedural Real-Pole Cover-Up" to Difficulty.INTERMEDIATE,
            "Procedural Inverse Smith Z_L" to Difficulty.MASTERY,
            "Procedural Principal n-th Root" to Difficulty.INTERMEDIATE,
            "Procedural Discrete z = e^(sT)" to Difficulty.INTERMEDIATE,
            "Procedural Unit-Circle Trig Integral" to Difficulty.ADVANCED
        ),
        preferredDifficulty,
        excluded
    )
) {
    0 -> {
        val pA = listOf(1, 2, 3, 4).random()
        val pB = listOf(-1, -2, -3).random()
        val evalAtFirst = Random.nextBoolean()
        val targetPole = if (evalAtFirst) pA else pB
        val otherPole = if (evalAtFirst) pB else pA
        val c = listOf(-6, -4, 2, 4, 6, 8).random()
        val a = listOf(-2, -1, 0, 1, 2, 3).filter { it != -pA && it != -pB }.random()
        val res = (c * (targetPole + a)).toDouble() / (targetPole - otherPole).toDouble()
        val pAStr = if (pA >= 0) "− $pA" else "+ ${-pA}"
        val pBStr = if (pB >= 0) "− $pB" else "+ ${-pB}"
        val numStr = when {
            a == 0 -> "$c·z"
            a > 0 -> "$c(z + $a)"
            else -> "$c(z − ${-a})"
        }
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Simple Pole Residue",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Extract the AC filter/network modal residue Res[f(z), z = $targetPole] at simple pole z₀ = $targetPole:",
            parametersLine1 = "f(z) = $numStr / ((z $pAStr)(z $pBStr))",
            parametersLine2 = "Evaluate at simple pole z₀ = $targetPole",
            methodTitle = "RESIDUE DRILL",
            methodNote = "Cancel the pole factor at z = $targetPole and evaluate the remaining rational expression at z = $targetPole.",
            inputHint = "Enter residue",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = res,
            unit = "",
            hints = listOf(
                "Res[f, $targetPole] = ${c * (targetPole + a)} / ($targetPole − ($otherPole)).",
                "Compute ${c * (targetPole + a)} / ${targetPole - otherPole}."
            ),
            workedSteps = listOf(
                "1. Res[f, $targetPole] = lim(z→$targetPole) (z − ($targetPole))f(z) = $c(${targetPole + a}) / ($targetPole − ($otherPole)).",
                "2. = ${c * (targetPole + a)} / ${targetPole - otherPole} = ${ComplexNum.formatClean(res)}."
            ),
            isProcedural = true,
            acContext = "Pole residues determine the exact modal amplitude of each decaying or oscillatory term in switched AC/digital filter responses."
        )
    }
    1 -> {
        val z0 = listOf(50, 75, 100).random()
        val rMult = listOf(1, 2, 3).random()
        val xMult = listOf(1, 2, -1, -2).random()
        val rL = rMult * z0
        val xL = xMult * z0
        val numRe = (rMult - 1).toDouble()
        val numIm = xMult.toDouble()
        val denRe = (rMult + 1).toDouble()
        val denIm = xMult.toDouble()
        val denSq = denRe * denRe + denIm * denIm
        val gRe = (numRe * denRe + numIm * denIm) / denSq
        val gIm = (numIm * denRe - numRe * denIm) / denSq
        val xSign = if (xL >= 0) "+ j$xL" else "− j${-xL}"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Smith Chart Γ",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Compute the complex reflection coefficient Γ = (Z_L − Z₀)/(Z_L + Z₀) for:",
            parametersLine1 = "Z_L = ($rL $xSign) Ω",
            parametersLine2 = "Line Impedance Z₀ = $z0 Ω",
            methodTitle = "SMITH CHART DRILL",
            methodNote = "Normalize z_L = Z_L/Z₀ first, then evaluate (z_L − 1)/(z_L + 1).",
            inputHint = "Enter Γ (e.g. 0.2 + j0.4 or polar)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(gRe, gIm),
            unit = "",
            hints = listOf(
                "z_L = Z_L / $z0 = $rMult ${if (xMult >= 0) "+ j$xMult" else "− j${-xMult}"}.",
                "Γ = ($numRe ${if (numIm >= 0) "+ j${numIm.toInt()}" else "− j${-numIm.toInt()}"}) / ($denRe ${if (denIm >= 0) "+ j${denIm.toInt()}" else "− j${-denIm.toInt()}"})."
            ),
            workedSteps = listOf(
                "1. Normalize z_L = $rMult ${if (xMult >= 0) "+ j$xMult" else "− j${-xMult}"}.",
                "2. Multiply numerator & denominator by conjugate of (z_L + 1) to get Γ = ${ComplexNum(gRe, gIm).formatRect()}."
            ),
            isProcedural = true,
            acContext = "RF and high-speed AC transmission lines use the Möbius Smith chart Γ = (Z_L − Z₀)/(Z_L + Z₀) to quantify impedance mismatch reflections."
        )
    }
    2 -> {
        val a = Random.nextInt(2, 9)
        val b = Random.nextInt(1, 7)
        val mode = Random.nextInt(3)
        val (uLine, vLine, kTarget, hint1, step1) = when (mode) {
            0 -> listOf(
                "u(x,y) = ${a}x² − ${a}y² + ${b}x",
                "v(x,y) = k·xy + ${b}y",
                (2 * a).toString(),
                "∂u/∂x = ${2 * a}x + $b and ∂v/∂y = kx + $b.",
                "∂u/∂x = ${2 * a}x + $b = ∂v/∂y = kx + $b ⇒ k = ${2 * a}."
            )
            1 -> listOf(
                "u(x,y) = ${a}x² − k·y² + ${b}x",
                "v(x,y) = ${2 * a}xy + ${b}y",
                a.toString(),
                "∂u/∂y = −2ky and −∂v/∂x = −${2 * a}y.",
                "Setting u_y = −v_x gives −2k = −${2 * a} ⇒ k = $a (Laplace u_xx + u_yy = 0)."
            )
            else -> listOf(
                "u(x,y) = ${a}x² − ${a}y² + ${b}x",
                "v(x,y) = ${2 * a}xy + k·y",
                b.toString(),
                "∂u/∂x = ${2 * a}x + $b and ∂v/∂y = ${2 * a}x + k.",
                "Matching constants in ∂u/∂x = ∂v/∂y yields k = $b."
            )
        }
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Cauchy-Riemann Constant",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "In a 2D planar AC conductor/dielectric field f(z) = u(x,y) + j·v(x,y), find constant k so f(z) is analytic:",
            parametersLine1 = uLine,
            parametersLine2 = vLine,
            methodTitle = "CAUCHY-RIEMANN DRILL",
            methodNote = "Differentiate u and v partially and equate ∂u/∂x = ∂v/∂y and ∂u/∂y = −∂v/∂x.",
            inputHint = "Enter constant k",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = kTarget.toDouble(),
            unit = "",
            hints = listOf(hint1, "Solve for real constant k."),
            workedSteps = listOf("1. $step1"),
            isProcedural = true,
            acContext = "Cauchy–Riemann analyticity enforces orthogonal equipotential lines u and AC current/flux streamlines v in 2D PCB traces."
        )
    }
    3 -> {
        val radius = listOf(2, 3, 4).random()
        val bothInside = Random.nextDouble() < 0.4
        val insidePool = (-radius + 1 until radius).filter { it != 0 }
        val z1 = insidePool.random()
        val z2 = if (bothInside) {
            insidePool.filter { it != z1 }.random()
        } else {
            listOf(-radius - 2, -radius - 1, radius + 1, radius + 2).random()
        }
        val c = listOf(3, 4, 6, 8, 12, -6).random()
        val a = listOf(-1, 0, 1, 2).filter { it != -z1 && it != -z2 }.random()
        val integralVal = if (bothInside) {
            c.toDouble()
        } else {
            (c * (z1 + a)).toDouble() / (z1 - z2).toDouble()
        }
        val f1 = if (z1 >= 0) "− $z1" else "+ ${-z1}"
        val f2 = if (z2 >= 0) "− $z2" else "+ ${-z2}"
        val numStr = when {
            a == 0 -> "$c·z"
            a > 0 -> "$c(z + $a)"
            else -> "$c(z − ${-a})"
        }
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Closed Contour Integral",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Sum the enclosed AC/digital filter pole residues via I = (1/j2π) ∮ f(z) dz CCW around the stability circle |z| = $radius:",
            parametersLine1 = "f(z) = $numStr / ((z $f1)(z $f2))",
            parametersLine2 = "Contour C: |z| = $radius (CCW)",
            methodTitle = "CAUCHY RESIDUE ENCLOSURE",
            methodNote = "Sum Res[f, z_k] strictly for all poles with |z_k| < $radius.",
            inputHint = "Enter contour integral value",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = integralVal,
            unit = "",
            hints = listOf(
                if (bothInside) "Both poles z = $z1 and z = $z2 lie inside |z| = $radius."
                else "Pole z = $z1 lies inside |z| = $radius while z = $z2 lies outside.",
                if (bothInside) "Sum Res[f, $z1] + Res[f, $z2] (notice opposing denominators combine!)."
                else "Compute Res[f, $z1] = ${c * (z1 + a)} / ($z1 − ($z2))."
            ),
            workedSteps = if (bothInside) {
                listOf(
                    "1. Both z = $z1 and z = $z2 lie inside |z| = $radius.",
                    "2. Σ Res = ($c(${z1 + a}) − $c(${z2 + a})) / (${z1 - z2}) = ${ComplexNum.formatClean(integralVal)}."
                )
            } else {
                listOf(
                    "1. Only z = $z1 lies inside |z| = $radius.",
                    "2. I = Res[f, $z1] = ${c * (z1 + a)} / (${z1 - z2}) = ${ComplexNum.formatClean(integralVal)}."
                )
            },
            isProcedural = true,
            acContext = "Contour residue sums isolate active filter modes inside a target frequency or stability circle while ignoring outer poles."
        )
    }
    4 -> {
        val wa = listOf(1, 2, 3, 4, 5, -1, -2, -3, -4, -5).random()
        val denom = 1.0 + (wa * wa).toDouble()
        val zRe = (1.0 - (wa * wa).toDouble()) / denom
        val zIm = (2.0 * wa) / denom
        val sStr = if (wa >= 0) "j$wa" else "−j${-wa}"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Bilinear Map s → z",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Map continuous-time s = $sStr to discrete z via bilinear transform z = (1 + s)/(1 − s):",
            parametersLine1 = "z = (1 + s) / (1 − s)",
            parametersLine2 = "Evaluate at s = $sStr",
            methodTitle = "BILINEAR MÖBIUS DRILL",
            methodNote = "Substitute s = $sStr and simplify ((1 + s)²)/(1 − s²).",
            inputHint = "Enter z (A + jB or polar)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(zRe, zIm),
            unit = "",
            hints = listOf(
                "Denominator product (1 − s)(1 + s) = 1 + ${wa * wa} = ${denom.toInt()}.",
                "Numerator (1 + s)² = ${1 - wa * wa} ${if (2 * wa >= 0) "+ j${2 * wa}" else "− j${-2 * wa}"}."
            ),
            workedSteps = listOf(
                "1. z = (${1 - wa * wa} ${if (2 * wa >= 0) "+ j${2 * wa}" else "− j${-2 * wa}"}) / ${denom.toInt()}.",
                "2. z = ${ComplexNum(zRe, zIm).formatRect()} (|z| = 1)."
            ),
            isProcedural = true,
            acContext = "The bilinear Möbius map converts analog RC/RLC filters to digital IIR filters while mapping the entire jω AC axis onto |z| = 1."
        )
    }
    5 -> {
        val r = listOf(2, 4, 6, 8, 10, 12).random()
        val angleChoices = listOf(
            Pair(30, "π/6"),
            Pair(45, "π/4"),
            Pair(60, "π/3"),
            Pair(90, "π/2"),
            Pair(120, "2π/3"),
            Pair(135, "3π/4"),
            Pair(150, "5π/6"),
            Pair(210, "7π/6"),
            Pair(225, "5π/4"),
            Pair(240, "4π/3"),
            Pair(300, "5π/3"),
            Pair(315, "7π/4"),
            Pair(-30, "−π/6"),
            Pair(-45, "−π/4"),
            Pair(-60, "−π/3"),
            Pair(-90, "−π/2"),
            Pair(-120, "−2π/3"),
            Pair(-135, "−3π/4")
        )
        val pick = angleChoices.random()
        val deg = pick.first
        val radLabel = pick.second
        val expected = ComplexNum.fromPolarDeg(r.toDouble(), deg.toDouble())
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Euler Polar-to-Rect",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Convert AC Euler phasor z = $r·e^(j$radLabel) into rectangular form A + jB:",
            parametersLine1 = "z = $r · e^(j$radLabel)   (θ = $deg°)",
            parametersLine2 = "z = r·cos(θ) + j·r·sin(θ)",
            methodTitle = "EULER CONVERSION DRILL",
            methodNote = "Evaluate $r·cos($deg°) for the real part and $r·sin($deg°) for the imaginary part.",
            inputHint = "Enter z (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = expected,
            unit = "",
            hints = listOf(
                "Angle $radLabel radians equals $deg°.",
                "Re(z) = $r cos($deg°) ≈ ${ComplexNum.formatClean(expected.re)}, Im(z) = $r sin($deg°) ≈ ${ComplexNum.formatClean(expected.im)}."
            ),
            workedSteps = listOf(
                "1. θ = $radLabel = $deg°.",
                "2. z = $r(cos $deg° + j sin $deg°) = ${expected.formatRect()}."
            ),
            isProcedural = true,
            acContext = "AC sinusoids A·cos(ωt + θ) map directly to Euler phasors A·e^(jθ) for mesh KVL and nodal KCL."
        )
    }
    6 -> {
        val p = listOf(2, 3, 4, 5, 6).random()
        val m = listOf(-4, -3, -2, 2, 3, 4, 5).random()
        val k = m * p
        val atOrigin = Random.nextBoolean()
        val targetPole = if (atOrigin) 0 else -p
        val res = if (atOrigin) m.toDouble() else (-m).toDouble()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Real-Pole Cover-Up",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Extract the RC/RL transient modal weight Res[F(s), s = $targetPole] via cover-up:",
            parametersLine1 = "F(s) = $k / (s · (s + $p))",
            parametersLine2 = "Evaluate at simple real pole s₀ = $targetPole",
            methodTitle = "SIMPLE REAL-POLE DRILL",
            methodNote = "Cancel the pole factor (s − ($targetPole)) and substitute s = $targetPole into the remainder.",
            inputHint = "Enter scalar residue",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = res,
            unit = "",
            hints = listOf(
                if (atOrigin) "s·F(s) = $k / (s + $p)." else "(s + $p)·F(s) = $k / s.",
                "Evaluate at s = $targetPole."
            ),
            workedSteps = listOf(
                "1. Cover up the pole factor at s = $targetPole.",
                "2. Res[F, $targetPole] = ${ComplexNum.formatClean(res)}."
            ),
            isProcedural = true,
            acContext = "Simple real-pole residues give the exact amplitude of each exponential decay mode A·e^(−t/τ) after AC switching."
        )
    }
    7 -> {
        val pick = listOf(
            listOf(0.4, 0.2, 2.0, 1.0),
            listOf(0.4, -0.2, 2.0, -1.0),
            listOf(0.2, 0.4, 1.0, 1.0),
            listOf(0.2, -0.4, 1.0, -1.0),
            listOf(0.5, 0.5, 1.0, 2.0),
            listOf(-0.2, 0.4, 0.5, 0.5)
        ).random()
        val gRe = pick[0]; val gIm = pick[1]; val znRe = pick[2]; val znIm = pick[3]
        val z0 = listOf(50, 75, 100).random()
        val zLoad = ComplexNum(z0 * znRe, z0 * znIm)
        val gammaStr = ComplexNum(gRe, gIm).formatRect()
        val zNormStr = ComplexNum(znRe, znIm).formatRect()
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Inverse Smith Z_L",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Invert the Smith Chart Möbius map to recover unknown load impedance Z_L (in Ohms) from Γ:",
            parametersLine1 = "Γ = $gammaStr,   Z₀ = $z0 Ω",
            parametersLine2 = "Inverse Möbius: Z_L = Z₀ · (1 + Γ) / (1 − Γ)",
            methodTitle = "INVERSE SMITH DRILL",
            methodNote = "Compute normalized z_L = (1 + Γ)/(1 − Γ), rationalize, and scale by Z₀.",
            inputHint = "Enter Z_L in Ω (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = zLoad,
            unit = "Ω",
            hints = listOf(
                "z_L = (1 + Γ) / (1 − Γ) = $zNormStr.",
                "Multiply by Z₀ = $z0 Ω."
            ),
            workedSteps = listOf(
                "1. z_L = $zNormStr.",
                "2. Z_L = $z0 × ($zNormStr) = ${zLoad.formatRect()} Ω."
            ),
            isProcedural = true,
            acContext = "Inverting the Smith Chart Möbius map lets RF engineers recover exact load impedance R + jX directly from VNA reflection data."
        )
    }
    8 -> {
        val pick = listOf(
            listOf(3, 3, 90, "j27 = 27∠90°"),
            listOf(3, 2, 180, "−8 = 8∠180°"),
            listOf(3, 4, 90, "j64 = 64∠90°"),
            listOf(3, 4, 180, "−64 = 64∠180°"),
            listOf(4, 3, 180, "−81 = 81∠180°"),
            listOf(2, 5, 120, "25∠120°")
        ).random()
        val nRoot = pick[0] as Int
        val rOut = pick[1] as Int
        val thetaDeg = pick[2] as Int
        val zLabel = pick[3] as String
        val outDeg = thetaDeg.toDouble() / nRoot
        val ans = ComplexNum.fromPolarDeg(rOut.toDouble(), outDeg)
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Principal n-th Root",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "In $nRoot-stage AC phase-splitter synthesis where z = w^$nRoot, find principal root w₀ = z^(1/$nRoot) (enter A + jB or R∠θ°):",
            parametersLine1 = "z = $zLabel",
            parametersLine2 = "De Moivre Principal Root (k = 0): w₀ = r^(1/$nRoot) ∠(θ/$nRoot)",
            methodTitle = "DE MOIVRE ROOT DRILL",
            methodNote = "Take the positive $nRoot-th root of r and divide the argument θ by $nRoot.",
            inputHint = "Enter w₀ (e.g. ${rOut}∠${ComplexNum.formatClean(outDeg)}°)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ans,
            unit = "",
            hints = listOf(
                "Modulus r^(1/$nRoot) = $rOut.",
                "Argument = $thetaDeg° / $nRoot = ${ComplexNum.formatClean(outDeg)}°."
            ),
            workedSteps = listOf(
                "1. w₀ = ${rOut}∠${ComplexNum.formatClean(outDeg)}° = ${ans.formatRect()}."
            ),
            isProcedural = true,
            acContext = "Fractional complex roots decompose balanced polyphase sequences and cascaded N-stage AC phase-shift networks."
        )
    }
    9 -> {
        val (omegaStr, tVal, degVal) = listOf(
            Triple("5π", 0.1, 90), Triple("2.5π", 0.1, 45), Triple("10π/3", 0.1, 60)
        ).random()
        val sigma = listOf(1, 2, 3, 4).random()
        val magZ = kotlin.math.exp(-sigma * tVal)
        val ans = ComplexNum.fromPolarDeg(magZ, degVal.toDouble())
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Discrete z = e^(sT)",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Map s-plane pole s = −$sigma + j$omegaStr to discrete z = e^(sT) with T = $tVal s:",
            parametersLine1 = "s = −$sigma + j$omegaStr rad/s,   T = $tVal s",
            parametersLine2 = "z = e^(−σT) ∠ ωT",
            methodTitle = "Z-PLANE MAP DRILL",
            methodNote = "Compute |z| = e^(−σT) and angle ωT rad (= $degVal°).",
            inputHint = "Enter z (rect or polar)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ans,
            tolerance = 0.02,
            unit = "",
            hints = listOf(
                "|z| = e^(−${ComplexNum.formatClean(sigma * tVal)}) ≈ ${ComplexNum.formatClean(magZ)}.",
                "Angle = $degVal°."
            ),
            workedSteps = listOf(
                "1. z = ${ComplexNum.formatClean(magZ)}∠$degVal° = ${ans.formatRect()}."
            ),
            isProcedural = true,
            acContext = "Mapping analog RLC poles via z = e^(sT) bridges continuous AC filter dynamics to sampled digital power controllers."
        )
    }
    else -> {
        val (aVal, bVal, cRoot) = listOf(Triple(5, 3, 4), Triple(5, 4, 3), Triple(13, 5, 12)).random()
        val ans = 2.0 * Math.PI / cRoot
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Unit-Circle Trig Integral",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Evaluate periodic AC cycle integral I = ∫₀²π dθ / ($aVal + $bVal cos θ) (cycle-averaged admittance) via unit-circle residue:",
            parametersLine1 = "I = ∫₀²π dθ / ($aVal + $bVal cos θ)",
            parametersLine2 = "z = e^(jθ) on |z| = 1 ⇒ 2π / √(a² − b²)",
            methodTitle = "TRIG CONTOUR DRILL",
            methodNote = "Apply residue theorem inside |z| < 1: I = 2π / √($aVal² − $bVal²).",
            inputHint = "Enter real I",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = ans,
            tolerance = 0.02,
            unit = "",
            hints = listOf(
                "√($aVal² − $bVal²) = $cRoot.",
                "I = 2π / $cRoot."
            ),
            workedSteps = listOf(
                "1. I = 2π / $cRoot ≈ ${ComplexNum.formatClean(ans)}."
            ),
            isProcedural = true,
            acContext = "Mapping θ ∈ [0, 2π] onto |z| = 1 solves periodic AC cycle-averaging integrals of power, admittance, and ripple via residues."
        )
    }
}

internal fun generateGeometricAlgebraDrill(
    drillId: String,
    drillNum: Int,
    preferredDifficulty: Difficulty?,
    excluded: Set<String>
): CircuitProblem = when (
    pickTemplateIndex(
        listOf(
            "Procedural Cl₂,₀ Geometric Product" to Difficulty.MASTERY,
            "Procedural Cl₂,₀ Multivector Inverse" to Difficulty.ADVANCED,
            "Procedural Cl₃,₀ Power Norm" to Difficulty.ADVANCED,
            "Procedural Fryze Orthogonal Current" to Difficulty.ADVANCED,
            "Procedural Cl₂,₀ Outer Wedge a ∧ b" to Difficulty.FOUNDATIONS,
            "Procedural Cl₂,₀ Inner Product P" to Difficulty.FOUNDATIONS,
            "Procedural Cl₂,₀ Impedance Z" to Difficulty.INTERMEDIATE,
            "Procedural Cl₂,₀ Line Reflection" to Difficulty.INTERMEDIATE,
            "Procedural Fryze Contraction i_⊥" to Difficulty.MASTERY,
            "Procedural GA Wedge Cramer" to Difficulty.INTERMEDIATE,
            "Procedural 3D Cl₃,₀ Rotor" to Difficulty.ADVANCED
        ),
        preferredDifficulty,
        excluded
    )
) {
    0 -> {
        val coords = (-6..6).filter { it != 0 }
        val a1 = coords.random()
        val a2 = coords.random()
        val b1 = coords.random()
        val b2 = coords.random()
        val dot = (a1 * b1 + a2 * b2).toDouble()
        val wedge = (a1 * b2 - a2 * b1).toDouble()
        fun fmtVec(x: Int, y: Int): String {
            val yTerm = if (y >= 0) "+ ${y}e₂" else "− ${-y}e₂"
            return "${x}e₁ $yTerm"
        }
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Cl₂,₀ Geometric Product",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Compute unified AC geometric product ab = a·b + a∧b (representing P + Q·e₁₂ for voltage a and current b; enter α + βe12):",
            parametersLine1 = "a = ${fmtVec(a1, a2)}  (voltage vector)",
            parametersLine2 = "b = ${fmtVec(b1, b2)}  (current vector)",
            methodTitle = "CLIFFORD PRODUCT DRILL",
            methodNote = "Scalar = a₁b₁ + a₂b₂; Bivector = (a₁b₂ − a₂b₁)e₁₂.",
            inputHint = "Enter α + βe12",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(dot, wedge),
            unit = "",
            hints = listOf(
                "a·b = ($a1)×($b1) + ($a2)×($b2) = ${dot.toInt()}.",
                "a∧b = (($a1)×($b2) − ($a2)×($b1))e₁₂ = ${wedge.toInt()}e₁₂."
            ),
            workedSteps = listOf(
                "1. a·b = ${dot.toInt()}; a∧b = ${wedge.toInt()}e₁₂.",
                "2. ab = ${ComplexNum(dot, wedge).formatBivector()}."
            ),
            isProcedural = true,
            acContext = "One geometric product v·i + v∧i simultaneously computes active power P (scalar) and reactive power Q (bivector) in AC circuits."
        )
    }
    1 -> {
        val triples = listOf(
            Triple(3, 4, 5),
            Triple(4, 3, 5),
            Triple(6, 8, 10),
            Triple(5, 12, 13),
            Triple(8, 15, 17)
        )
        val t = triples.random()
        val scale = listOf(1, 2).random()
        val aSign = if (Random.nextBoolean()) 1 else -1
        val bSign = if (Random.nextBoolean()) 1 else -1
        val a = (t.first * scale * aSign).toDouble()
        val b = (t.second * scale * bSign).toDouble()
        val hyp = t.third * scale
        val normSq = (hyp * hyp).toDouble()
        val invRe = a / normSq
        val invIm = -b / normSq
        val mSign = if (b >= 0) "+ ${b.toInt()}e₁₂" else "− ${(-b).toInt()}e₁₂"
        val conjSign = if (b >= 0) "− ${b.toInt()}e₁₂" else "+ ${(-b).toInt()}e₁₂"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Cl₂,₀ Multivector Inverse",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Invert AC impedance multivector M = Z to admittance Y = M⁻¹ = M̃ / (M M̃) in Cl₂,₀ (enter α + βe12 or α + jβ):",
            parametersLine1 = "Impedance M = ${a.toInt()} $mSign",
            parametersLine2 = "Reversion M̃ = ${a.toInt()} $conjSign",
            methodTitle = "MULTIVECTOR INVERSION DRILL",
            methodNote = "Compute M M̃ = α² + β² and divide M̃ by this scalar norm squared.",
            inputHint = "Enter M⁻¹ (e.g. 0.12 - 0.16e12)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(invRe, invIm),
            unit = "",
            hints = listOf(
                "M M̃ = (${a.toInt()})² + (${b.toInt()})² = ${normSq.toInt()}.",
                "M⁻¹ = (${a.toInt()} $conjSign) / ${normSq.toInt()}."
            ),
            workedSteps = listOf(
                "1. M M̃ = ${normSq.toInt()}.",
                "2. M⁻¹ = (${a.toInt()} $conjSign) / ${normSq.toInt()} = ${ComplexNum(invRe, invIm).formatBivector()}."
            ),
            isProcedural = true,
            acContext = "Inverting an impedance multivector Z⁻¹ = Z̃/(Z Z̃) yields AC admittance Y = G + B·e₁₂ for parallel KCL branch analysis."
        )
    }
    2 -> {
        val baseQuads = listOf(
            Pair(listOf(1, 2, 2), 3),
            Pair(listOf(2, 3, 6), 7),
            Pair(listOf(1, 4, 8), 9),
            Pair(listOf(4, 4, 7), 9),
            Pair(listOf(2, 6, 9), 11),
            Pair(listOf(6, 6, 7), 11)
        )
        val pickV = baseQuads.random()
        val pickI = baseQuads.random()
        val scaleV = listOf(5, 10, 20).random()
        val scaleI = listOf(1, 2, 3).random()
        val vComps = pickV.first.shuffled().map { it * scaleV }
        val iComps = pickI.first.shuffled().map { it * scaleI * (if (Random.nextBoolean()) 1 else -1) }
        val vNorm = pickV.second * scaleV
        val iNorm = pickI.second * scaleI
        val normM = (vNorm * iNorm).toDouble()
        fun fmtTerm(coeff: Int, basis: String): String = if (coeff >= 0) "+ ${coeff}$basis" else "− ${-coeff}$basis"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Cl₃,₀ Power Norm",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Compute the apparent geometric power norm ||M|| = ||v||·||i|| in Cl₃,₀ (in VA):",
            parametersLine1 = "v = ${vComps[0]}e₁ + ${vComps[1]}e₂ + ${vComps[2]}e₃ V",
            parametersLine2 = "i = ${iComps[0]}e₁ ${fmtTerm(iComps[1], "e₂")} ${fmtTerm(iComps[2], "e₃")} A",
            methodTitle = "MULTIHARMONIC POWER DRILL",
            methodNote = "In Clifford power theory, ||M|| = √(P² + ||Q||²) equals the product ||v||·||i||.",
            inputHint = "Enter ||M|| in VA",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = normM,
            unit = "VA",
            hints = listOf(
                "||v|| = √(${vComps[0]}² + ${vComps[1]}² + ${vComps[2]}²) = $vNorm V.",
                "||i|| = √((${iComps[0]})² + (${iComps[1]})² + (${iComps[2]})²) = $iNorm A."
            ),
            workedSteps = listOf(
                "1. Compute Euclidean norms: ||v|| = $vNorm V, ||i|| = $iNorm A.",
                "2. ||M|| = ||v|| · ||i|| = $vNorm × $iNorm = ${normM.toInt()} VA."
            ),
            isProcedural = true,
            acContext = "Norm multiplicativity ||v i|| = ||v||·||i|| satisfies IEEE 1459 apparent power S² = P² + Q₁² + D² in non-sinusoidal AC loads."
        )
    }
    3 -> {
        val unitDirs = listOf(Pair(0.6, 0.8), Pair(0.8, 0.6), Pair(-0.6, 0.8), Pair(0.6, -0.8))
        val vHat = unitDirs.random()
        val i1 = listOf(5, 10, 15).random()
        val i2 = listOf(-5, 0, 5, 10).random()
        val wedgeCoeff = i1 * vHat.second - i2 * vHat.first
        val perpMag = kotlin.math.abs(wedgeCoeff)
        val v2Str = if (vHat.second >= 0) "+ ${vHat.second}e₂" else "− ${-vHat.second}e₂"
        val i2Str = when {
            i2 > 0 -> " + ${i2}e₂"
            i2 < 0 -> " − ${-i2}e₂"
            else -> ""
        }
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Fryze Orthogonal Current",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Find the non-active orthogonal current magnitude ||i_⊥|| = ||i ∧ v̂|| in Cl₂,₀ (in A):",
            parametersLine1 = "v̂ = ${vHat.first}e₁ $v2Str  (||v̂|| = 1)",
            parametersLine2 = "i = ${i1}e₁$i2Str A",
            methodTitle = "FRYZE BIVECTOR DRILL",
            methodNote = "Compute ||i ∧ v̂|| = |i₁v̂₂ − i₂v̂₁|.",
            inputHint = "Enter ||i_⊥|| in A",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = perpMag,
            unit = "A",
            hints = listOf(
                "i ∧ v̂ = ($i1 × (${vHat.second}) − ($i2) × (${vHat.first})) e₁₂.",
                "Take absolute value of the e₁₂ coefficient."
            ),
            workedSteps = listOf(
                "1. i ∧ v̂ = ${ComplexNum.formatClean(wedgeCoeff)} e₁₂.",
                "2. ||i_⊥|| = |${ComplexNum.formatClean(wedgeCoeff)}| = ${ComplexNum.formatClean(perpMag)} A."
            ),
            isProcedural = true,
            acContext = "Fryze bivector rejection ||i ∧ v̂|| measures the exact RMS current that passive or active PFC compensators eliminate."
        )
    }
    4 -> {
        val coords = (-6..6).filter { it != 0 }
        val a1 = coords.random()
        val a2 = coords.random()
        val b1 = coords.random()
        val b2 = coords.random()
        val beta = (a1 * b2 - a2 * b1).toDouble()
        fun fmtVec(x: Int, y: Int): String {
            val yTerm = if (y >= 0) "+ ${y}e₂" else "− ${-y}e₂"
            return "${x}e₁ $yTerm"
        }
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Cl₂,₀ Outer Wedge a ∧ b",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "In Cl₂,₀ AC circuit geometry, voltage vector a and current vector b span reactive bivector a ∧ b = β·e₁₂. Find coefficient β:",
            parametersLine1 = "a = ${fmtVec(a1, a2)}  (AC voltage vector)",
            parametersLine2 = "b = ${fmtVec(b1, b2)}  (AC current vector)",
            methodTitle = "WEDGE PRODUCT DRILL",
            methodNote = "Compute β = a₁b₂ − a₂b₁.",
            inputHint = "Enter coefficient β",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = beta,
            unit = "",
            hints = listOf(
                "Cross-multiply vector components: ($a1)×($b2) − ($a2)×($b1).",
                "Remember e₁∧e₁ = e₂∧e₂ = 0 and e₂∧e₁ = −e₁₂."
            ),
            workedSteps = listOf(
                "1. a ∧ b = (($a1)($b2) − ($a2)($b1)) e₁₂.",
                "2. = ${beta.toInt()} e₁₂ ⇒ β = ${beta.toInt()}."
            ),
            isProcedural = true,
            acContext = "The outer wedge v ∧ i directly isolates signed reactive power Q without losing lead/lag orientation between voltage and current."
        )
    }
    5 -> {
        val coords = (-8..8).filter { it != 0 }
        val v1 = coords.random()
        val v2 = coords.random()
        val i1 = coords.random()
        val i2 = coords.random()
        val powerP = (v1 * i1 + v2 * i2).toDouble()
        fun fmtVec(x: Int, y: Int): String {
            val yTerm = if (y >= 0) "+ ${y}e₂" else "− ${-y}e₂"
            return "${x}e₁ $yTerm"
        }
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Cl₂,₀ Inner Product P",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Compute the scalar active power P = v · i (in Watts) in Cl₂,₀:",
            parametersLine1 = "v = ${fmtVec(v1, v2)} V",
            parametersLine2 = "i = ${fmtVec(i1, i2)} A",
            methodTitle = "INNER PRODUCT DRILL",
            methodNote = "Multiply matching basis coefficients (e₁·e₁ = e₂·e₂ = 1, cross terms = 0).",
            inputHint = "Enter P in Watts",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = powerP,
            unit = "W",
            hints = listOf(
                "Compute ($v1 × $i1) + ($v2 × $i2).",
                "Cross terms e₁·e₂ vanish."
            ),
            workedSteps = listOf(
                "1. v · i = ($v1)($i1) + ($v2)($i2) = ${v1 * i1} + ${v2 * i2}.",
                "2. P = ${powerP.toInt()} W."
            ),
            isProcedural = true,
            acContext = "In Geometric Algebra power theory, the inner product v · i extracts true active power P (in Watts) across all AC axes."
        )
    }
    6 -> {
        val r = listOf(4, 6, 8, 10, 12, 15, 20).random()
        val omega = listOf(100, 200, 500, 1000).random()
        val xL = listOf(3, 4, 6, 8, 12, 15, 20).random()
        val lMh = (xL.toDouble() / omega.toDouble()) * 1000.0
        val lMhStr = ComplexNum.formatClean(lMh)
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Cl₂,₀ Impedance Z",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Express series RL impedance as an even Cl₂,₀ multivector Z = R + X_L·e₁₂ (enter A + Be12):",
            parametersLine1 = "R = ${r}Ω,   L = ${lMhStr} mH",
            parametersLine2 = "ω = $omega rad/s",
            methodTitle = "GA IMPEDANCE DRILL",
            methodNote = "Compute X_L = ωL and assign it to the +e₁₂ bivector component.",
            inputHint = "Enter Z in Ω (e.g. $r + ${xL}e12)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(r.toDouble(), xL.toDouble()),
            unit = "Ω",
            hints = listOf(
                "X_L = $omega × (${lMhStr} × 10⁻³) = $xL Ω.",
                "Write Z = $r + ${xL}e₁₂ Ω."
            ),
            workedSteps = listOf(
                "1. X_L = ωL = $xL Ω.",
                "2. Z = $r + ${xL}e₁₂ Ω."
            ),
            isProcedural = true,
            acContext = "Writing Z = R + X_L·e₁₂ places real dissipation in the scalar grade and inductive quadrature reactance in the e₁₂ plane."
        )
    }
    7 -> {
        val (u1, u2, vAmp) = listOf(
            Triple(0.6, 0.8, 25),
            Triple(0.8, 0.6, 25),
            Triple(0.8, 0.6, 50),
            Triple(0.6, -0.8, 25),
            Triple(0.8, -0.6, 50)
        ).random()
        val e2Comp = 2.0 * vAmp * u1 * u2
        val uStr = if (u2 >= 0) "${u1}e₁ + ${u2}e₂" else "${u1}e₁ − ${-u2}e₂"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Cl₂,₀ Line Reflection",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Apply phase-conjugate line reflection v' = û v û of AC phasor v = ${vAmp}e₁ across unit axis û in Cl₂,₀. Find the e₂-component of v':",
            parametersLine1 = "v = ${vAmp}e₁,   û = $uStr  (||û|| = 1)",
            parametersLine2 = "Reflection law: v' = û v û = 2(v·û)û − v",
            methodTitle = "VERSOR REFLECTION DRILL",
            methodNote = "Evaluate v·û = ${vAmp * u1}, then take the e₂-component of 2(v·û)û − v.",
            inputHint = "Enter e₂ coefficient",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = e2Comp,
            unit = "",
            hints = listOf(
                "v·û = ${ComplexNum.formatClean(vAmp * u1)}.",
                "e₂-component = 2 × ${ComplexNum.formatClean(vAmp * u1)} × ($u2)."
            ),
            workedSteps = listOf(
                "1. v' = 2(${ComplexNum.formatClean(vAmp * u1)})($uStr) − ${vAmp}e₁.",
                "2. e₂-component = ${ComplexNum.formatClean(e2Comp)}."
            ),
            isProcedural = true,
            acContext = "Versor reflection û v û mirrors AC phasors across any reference impedance axis, forming the building block of rotor phase shifts."
        )
    }
    8 -> {
        val pick = listOf(
            listOf(0.8, 0.6, 15.0, 5.0),
            listOf(0.6, 0.8, 25.0, 0.0),
            listOf(0.6, 0.8, 10.0, 5.0),
            listOf(0.8, -0.6, 15.0, 5.0),
            listOf(0.8, 0.6, 20.0, -10.0)
        ).random()
        val u1 = pick[0]; val u2 = pick[1]; val i1 = pick[2]; val i2 = pick[3]
        val wedgeB = i1 * u2 - i2 * u1
        val e1Comp = wedgeB * u2
        val uStr = if (u2 >= 0) "${u1}e₁ + ${u2}e₂" else "${u1}e₁ − ${-u2}e₂"
        val iStr = when {
            i2 == 0.0 -> "${i1.toInt()}e₁"
            i2 > 0.0 -> "${i1.toInt()}e₁ + ${i2.toInt()}e₂"
            else -> "${i1.toInt()}e₁ − ${(-i2).toInt()}e₂"
        }
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural Fryze Contraction i_⊥",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Compute orthogonal non-active current i_⊥ = (i ∧ v̂) · v̂ in Cl₂,₀ and enter its e₁-component (in A):",
            parametersLine1 = "v̂ = $uStr   (||v̂|| = 1)",
            parametersLine2 = "i = $iStr A   (e₁₂e₁ = −e₂, e₁₂e₂ = +e₁)",
            methodTitle = "FRYZE CONTRACTION DRILL",
            methodNote = "Find bivector i ∧ v̂ = (i₁v̂₂ − i₂v̂₁)e₁₂, then right-multiply by v̂.",
            inputHint = "Enter e₁ coefficient of i_⊥ in A",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = e1Comp,
            unit = "A",
            hints = listOf(
                "i ∧ v̂ = ${ComplexNum.formatClean(wedgeB)}e₁₂.",
                "e₁ coefficient comes from (${ComplexNum.formatClean(wedgeB)}e₁₂)(${u2}e₂) = ${ComplexNum.formatClean(e1Comp)}e₁."
            ),
            workedSteps = listOf(
                "1. i ∧ v̂ = ${ComplexNum.formatClean(wedgeB)}e₁₂.",
                "2. i_⊥ · e₁ = ${ComplexNum.formatClean(e1Comp)} A."
            ),
            isProcedural = true,
            acContext = "Contracting (i ∧ v̂)·v̂ yields the exact harmonic/reactive current vector i_⊥ an active power filter must inject for unity pf."
        )
    }
    9 -> {
        val a11 = 2; val a12 = 1; val a21 = 1; val a22 = 3
        val x1 = listOf(-3, -2, 2, 3, 4).random()
        val x2 = listOf(-2, 1, 2, 3).random()
        val c1 = x1 * a11 + x2 * a21
        val c2 = x1 * a12 + x2 * a22
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural GA Wedge Cramer",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Solve the 2-loop AC mesh system x₁ a₁ + x₂ a₂ = c in Cl₂,₀ via x₁ = (c ∧ a₂) / (a₁ ∧ a₂). Find scalar x₁:",
            parametersLine1 = "a₁ = 2e₁ + e₂,   a₂ = e₁ + 3e₂",
            parametersLine2 = "c = ${c1}e₁ ${if (c2 >= 0) "+ ${c2}e₂" else "− ${-c2}e₂"}",
            methodTitle = "WEDGE CRAMER DRILL",
            methodNote = "Divide bivector c ∧ a₂ by a₁ ∧ a₂ = 5e₁₂.",
            inputHint = "Enter scalar x₁",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = x1.toDouble(),
            unit = "",
            hints = listOf(
                "a₁ ∧ a₂ = 5e₁₂.",
                "c ∧ a₂ = ($c1·3 − ($c2)·1)e₁₂ = ${5 * x1}e₁₂."
            ),
            workedSteps = listOf(
                "1. x₁ = ${5 * x1}e₁₂ / 5e₁₂ = $x1."
            ),
            isProcedural = true,
            acContext = "Grassmann wedge ratios solve 2-loop AC KVL mesh equations and two-port linear networks geometrically without matrix determinants."
        )
    }
    else -> {
        val vMag = listOf(25, 50, 75, 100).random()
        val sSigned = if (Random.nextBoolean()) 0.6 else -0.6
        val outComp = vMag * (2.0 * 0.8 * sSigned)
        val rStr = if (sSigned >= 0) "0.8 − 0.6e₂₃" else "0.8 + 0.6e₂₃"
        CircuitProblem(
            id = drillId,
            code = "Drill #$drillNum",
            title = "Procedural 3D Cl₃,₀ Rotor",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Phase-shift harmonic AC load vector v = 4e₁ + ${vMag}e₂ in the e₂₃ harmonic plane via 3D rotor R = $rStr (v' = R v R̃). Find e₃-component:",
            parametersLine1 = "v = 4e₁ + ${vMag}e₂,   R = $rStr",
            parametersLine2 = "Plane rotation in e₂₃ (4e₁ commutes and stays invariant)",
            methodTitle = "3D ROTOR DRILL",
            methodNote = "Compute sin θ = 2·0.8·(${sSigned}) and multiply by $vMag.",
            inputHint = "Enter e₃ coefficient",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = outComp,
            unit = "",
            hints = listOf(
                "sin θ = ${ComplexNum.formatClean(2.0 * 0.8 * sSigned)}.",
                "e₃-component = $vMag × ${ComplexNum.formatClean(2.0 * 0.8 * sSigned)}."
            ),
            workedSteps = listOf(
                "1. e₃-component = ${ComplexNum.formatClean(outComp)}."
            ),
            isProcedural = true,
            acContext = "3D rotors phase-shift specific harmonic planes (e₂₃) independently while keeping orthogonal fundamental/harmonic axes untouched."
        )
    }
}
