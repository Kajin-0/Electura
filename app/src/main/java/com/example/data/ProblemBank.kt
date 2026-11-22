package com.example.data

import com.example.model.AnswerKind
import com.example.model.CircuitProblem
import com.example.model.CircuitTopology
import com.example.model.ComplexNum
import com.example.model.Difficulty
import com.example.model.FieldGuideEntry
import com.example.model.ProblemCategory
import kotlin.math.sqrt
import kotlin.random.Random

object ProblemBank {

    private val rawCuratedProblems: List<CircuitProblem> = listOf(
        CircuitProblem(
            id = "p_492b",
            code = "No. 492B",
            title = "Series LCR Transient & Steady State",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Solve for the steady-state peak current phasor I (t ≫ τ, after transients decay) where:",
            parametersLine1 = "L = 0.5H, C = 0.02F, R = 10Ω",
            parametersLine2 = "E(t) = 100 sin(10t) V",
            leftMarker = "TRANSIENT DECAYED",
            rightMarker = "STEADY STATE",
            methodTitle = "INTEGRATION PATH",
            methodNote = "Use Bromwich contour or steady-state jω impedance at ω = 10 rad/s.",
            inputHint = "Enter complex result (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(10.0, 0.0),
            unit = "A",
            hints = listOf(
                "Compute reactances at ω = 10 rad/s: X_L = ωL and X_C = 1/(ωC).",
                "Notice X_L = 5Ω equals X_C = 5Ω, so reactive terms cancel at resonance: Z = 10 + j0 Ω."
            ),
            workedSteps = listOf(
                "1. Source angular frequency is ω = 10 rad/s.",
                "2. X_L = ωL = 10 × 0.5 = 5 Ω; X_C = 1/(ωC) = 1/(10 × 0.02) = 5 Ω.",
                "3. Z = R + j(X_L − X_C) = 10 + j(5 − 5) = 10 Ω (circuit is at resonance).",
                "4. Steady-state current phasor I = E / Z = 100∠0° / 10 = 10 + j0 A."
            )
        ),
        CircuitProblem(
            id = "p_101a",
            code = "No. 101A",
            title = "Series RL Impedance",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.SERIES_RL,
            prompt = "Determine the total complex impedance Z = R + jX_L in Ohms where:",
            parametersLine1 = "R = 6Ω, L = 0.08H",
            parametersLine2 = "ω = 100 rad/s",
            leftMarker = "REAL (R)",
            rightMarker = "INDUCTIVE (+jX)",
            methodTitle = "PHASOR IMPEDANCE",
            methodNote = "Convert inductance L to inductive reactance X_L = ωL before summing.",
            inputHint = "Enter complex result (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(6.0, 8.0),
            unit = "Ω",
            hints = listOf(
                "Inductive impedance is Z_L = jωL.",
                "X_L = 100 × 0.08 = 8Ω, so Z = 6 + j8 Ω (or 10∠53.13° Ω)."
            ),
            workedSteps = listOf(
                "1. Inductive reactance X_L = ωL = 100 × 0.08 = 8 Ω.",
                "2. Series sum: Z = R + jX_L = 6 + j8 Ω.",
                "3. In polar form: |Z| = √(6² + 8²) = 10 Ω, θ = atan2(8, 6) = 53.13°."
            )
        ),
        CircuitProblem(
            id = "p_104b",
            code = "No. 104B",
            title = "Series RC Reactance",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.SERIES_RC,
            prompt = "Find the total complex impedance Z (in Ohms) for the RC branch:",
            parametersLine1 = "R = 40Ω, C = 250μF",
            parametersLine2 = "ω = 100 rad/s",
            leftMarker = "LEADING CURRENT",
            rightMarker = "CAPACITIVE (-jX)",
            methodTitle = "CAPACITIVE PHASE",
            methodNote = "Remember Z_C = 1/(jωC) = -j/(ωC) carries a negative imaginary sign.",
            inputHint = "Enter complex result (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(40.0, -40.0),
            unit = "Ω",
            hints = listOf(
                "Convert 250 μF = 250 × 10⁻⁶ F = 0.00025 F.",
                "X_C = 1 / (100 × 0.00025) = 1 / 0.025 = 40 Ω, giving Z = 40 - j40 Ω."
            ),
            workedSteps = listOf(
                "1. ωC = 100 × 250×10⁻⁶ = 0.025 S.",
                "2. X_C = 1 / 0.025 = 40 Ω.",
                "3. Z = R − jX_C = 40 − j40 Ω (56.57∠−45° Ω)."
            )
        ),
        CircuitProblem(
            id = "p_108c",
            code = "No. 108C",
            title = "Parallel RC Equivalent",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "Compute the equivalent parallel impedance Z_eq (in Ohms) of:",
            parametersLine1 = "R = 10Ω || Z_C = -j10Ω",
            parametersLine2 = "Y = 1/R + j/X_C",
            leftMarker = "ADMITTANCE Y",
            rightMarker = "IMPEDANCE Z = 1/Y",
            methodTitle = "COMPLEX CONJUGATE",
            methodNote = "Use Z_eq = (Z₁·Z₂)/(Z₁ + Z₂) and rationalize the denominator.",
            inputHint = "Enter complex result (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(5.0, -5.0),
            unit = "Ω",
            hints = listOf(
                "Z_eq = (10 × -j10) / (10 - j10) = -j100 / (10 - j10).",
                "Multiply numerator and denominator by (1 + j) to rationalize."
            ),
            workedSteps = listOf(
                "1. Admittance Y = 1/10 + 1/(-j10) = 0.1 + j0.1 S.",
                "2. Z_eq = 1 / (0.1 + j0.1) = 10 / (1 + j).",
                "3. Multiply by (1 − j)/(1 − j): Z_eq = 10(1 − j)/2 = 5 − j5 Ω."
            )
        ),
        CircuitProblem(
            id = "p_112d",
            code = "No. 112D",
            title = "Series RLC Net Impedance",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Find total series impedance Z = R + j(X_L − X_C) in Ohms for:",
            parametersLine1 = "R = 12Ω, L = 0.2H, C = 4mF",
            parametersLine2 = "ω = 50 rad/s",
            leftMarker = "X_L > X_C",
            rightMarker = "NET INDUCTIVE",
            methodTitle = "NET REACTANCE",
            methodNote = "Subtract capacitive reactance X_C from inductive reactance X_L.",
            inputHint = "Enter complex result (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(12.0, 5.0),
            unit = "Ω",
            hints = listOf(
                "X_L = 50 × 0.2 = 10 Ω; C = 4 mF = 0.004 F.",
                "X_C = 1 / (50 × 0.004) = 5 Ω, so net reactance is +(10 − 5) = +5 Ω."
            ),
            workedSteps = listOf(
                "1. X_L = ωL = 50 × 0.2 = 10 Ω.",
                "2. X_C = 1/(ωC) = 1/(50 × 0.004) = 5 Ω.",
                "3. Z = 12 + j(10 − 5) = 12 + j5 Ω (13∠22.62° Ω)."
            )
        ),
        CircuitProblem(
            id = "p_115e",
            code = "No. 115E",
            title = "Complex Voltage Divider",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.SERIES_RL,
            prompt = "Find the complex voltage phasor V_L across the inductor in Volts:",
            parametersLine1 = "V_in = 20∠0° V, R = 3Ω",
            parametersLine2 = "Z_L = j4Ω (series with R)",
            leftMarker = "SERIES LOOP",
            rightMarker = "DIVIDER RULE",
            methodTitle = "PHASOR DIVISION",
            methodNote = "Apply V_L = V_in · Z_L / (R + Z_L) and rationalize the denominator.",
            inputHint = "Enter V_L (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(12.8, 9.6),
            unit = "V",
            hints = listOf(
                "V_L = 20 × (j4) / (3 + j4) = j80 / (3 + j4).",
                "Multiply numerator & denominator by (3 − j4); |3 + j4|² = 25."
            ),
            workedSteps = listOf(
                "1. V_L = 20 · (j4) / (3 + j4) = j80 / (3 + j4).",
                "2. Rationalize: j80(3 − j4) / 25 = (320 + j240) / 25.",
                "3. V_L = 12.8 + j9.6 V (or 16∠36.87° V)."
            )
        ),
        CircuitProblem(
            id = "p_119f",
            code = "No. 119F",
            title = "Parallel RL Equivalent",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "Determine the equivalent impedance Z_eq (in Ohms) for the parallel pair:",
            parametersLine1 = "R = 10Ω || Z_L = j10Ω",
            parametersLine2 = "Y = 1/10 − j/10 S",
            leftMarker = "ADMITTANCE Y",
            rightMarker = "LAGGING PHASE",
            methodTitle = "PARALLEL INVERSION",
            methodNote = "Invert total admittance Y = G − jB using its complex conjugate.",
            inputHint = "Enter Z_eq (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(5.0, 5.0),
            unit = "Ω",
            hints = listOf(
                "Z_eq = (10 × j10) / (10 + j10) = j10 / (1 + j).",
                "Multiply by (1 − j)/(1 − j) to simplify."
            ),
            workedSteps = listOf(
                "1. Z_eq = (10 · j10) / (10 + j10) = j10 / (1 + j).",
                "2. Multiply by (1 − j)/(1 − j): j10(1 − j)/2 = (10 + j10)/2.",
                "3. Z_eq = 5 + j5 Ω (7.07∠45° Ω)."
            )
        ),
        CircuitProblem(
            id = "p_201a",
            code = "No. 201A",
            title = "Series Resonance Q-Factor",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Calculate the dimensionless quality factor Q at series resonance:",
            parametersLine1 = "R = 10Ω, L = 0.1H, C = 10μF",
            parametersLine2 = "V_s = 100 V rms (variable ω)",
            leftMarker = "ω = ω₀",
            rightMarker = "PEAK SELECTIVITY",
            methodTitle = "ENERGY RATIO",
            methodNote = "At resonance, Q equals inductive reactance divided by resistance.",
            inputHint = "Enter scalar value (e.g. 10)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 10.0,
            unit = "",
            hints = listOf(
                "Use the series Q formula: Q = (1/R) × √(L/C).",
                "L/C = 0.1 / 10⁻⁵ = 10,000, whose square root is 100."
            ),
            workedSteps = listOf(
                "1. Compute L/C = 0.1 / (10 × 10⁻⁶) = 10⁴.",
                "2. √(L/C) = 100 Ω.",
                "3. Q = (1/R)√(L/C) = 100 / 10 = 10."
            )
        ),
        CircuitProblem(
            id = "p_204b",
            code = "No. 204B",
            title = "Resonant Angular Frequency",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Find the resonant angular frequency ω₀ (in rad/s) for the tank:",
            parametersLine1 = "L = 25mH, C = 10μF",
            parametersLine2 = "R = 5Ω (series)",
            leftMarker = "X_L = X_C",
            rightMarker = "ZERO PHASE",
            methodTitle = "NATURAL FREQUENCY",
            methodNote = "Equate ω₀L = 1/(ω₀C) to solve for ω₀ in radians per second.",
            inputHint = "Enter frequency in rad/s",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 2000.0,
            unit = "rad/s",
            hints = listOf(
                "ω₀ = 1 / √(L·C). Convert 25 mH = 0.025 H and 10 μF = 10⁻⁵ F.",
                "L·C = 2.5 × 10⁻⁷ = 25 × 10⁻⁸, so √(L·C) = 5 × 10⁻⁴."
            ),
            workedSteps = listOf(
                "1. L·C = (25 × 10⁻³) × (10 × 10⁻⁶) = 25 × 10⁻⁸ s².",
                "2. √(L·C) = 5 × 10⁻⁴ s.",
                "3. ω₀ = 1 / (5 × 10⁻⁴) = 2000 rad/s."
            )
        ),
        CircuitProblem(
            id = "p_209c",
            code = "No. 209C",
            title = "Parallel Tank Bandwidth",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.PARALLEL_RLC,
            prompt = "Determine the 3-dB half-power bandwidth BW (in rad/s) of the parallel RLC:",
            parametersLine1 = "R = 500Ω, L = 10mH",
            parametersLine2 = "C = 100nF (parallel tank)",
            leftMarker = "ω₁ (-3dB)",
            rightMarker = "ω₂ (-3dB)",
            methodTitle = "DAMPING FACTOR",
            methodNote = "For a parallel RLC tank, bandwidth depends strictly on R and C.",
            inputHint = "Enter bandwidth in rad/s",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 20000.0,
            unit = "rad/s",
            hints = listOf(
                "In a parallel RLC circuit, BW = 2α = 1 / (R·C).",
                "R·C = 500 × 100×10⁻⁹ = 5 × 10⁻⁵ s."
            ),
            workedSteps = listOf(
                "1. Parallel damping factor α = 1 / (2RC).",
                "2. Half-power bandwidth BW = 2α = 1 / (RC).",
                "3. BW = 1 / (500 × 100×10⁻⁹) = 1 / (5×10⁻⁵) = 20000 rad/s."
            )
        ),
        CircuitProblem(
            id = "p_212d",
            code = "No. 212D",
            title = "Filter Selectivity Bandwidth",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Find the 3-dB passband width BW (in Hz) of a tuned resonant filter:",
            parametersLine1 = "f₀ = 1000 Hz",
            parametersLine2 = "Quality Factor Q = 25",
            leftMarker = "f₁ (-3dB)",
            rightMarker = "f₂ (-3dB)",
            methodTitle = "SELECTIVITY",
            methodNote = "Bandwidth narrows inversely with quality factor: BW = f₀ / Q.",
            inputHint = "Enter bandwidth in Hz",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 40.0,
            unit = "Hz",
            hints = listOf(
                "Quality factor is defined as Q = f₀ / BW.",
                "Rearrange: BW = 1000 / 25."
            ),
            workedSteps = listOf(
                "1. BW = f₀ / Q.",
                "2. BW = 1000 Hz / 25 = 40 Hz."
            )
        ),
        CircuitProblem(
            id = "p_215e",
            code = "No. 215E",
            title = "Resonant Voltage Gain",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Find the capacitor rms voltage magnitude |V_C| (in Volts) at series resonance:",
            parametersLine1 = "V_s = 12 V rms, R = 4Ω",
            parametersLine2 = "X_L = X_C = 60Ω at ω₀",
            leftMarker = "Q = X_L / R",
            rightMarker = "|V_C| = Q·V_s",
            methodTitle = "VOLTAGE MAGNIFICATION",
            methodNote = "At resonance, current is V_s / R and multiplies across reactance X_C.",
            inputHint = "Enter |V_C| in Volts",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 180.0,
            unit = "V",
            hints = listOf(
                "Resonant current I = V_s / R = 12 / 4 = 3 A.",
                "|V_C| = I × X_C = 3 × 60 (or Q × V_s with Q = 15)."
            ),
            workedSteps = listOf(
                "1. At resonance, X_L and X_C cancel, so I = V_s / R = 12 / 4 = 3 A.",
                "2. |V_C| = I · X_C = 3 × 60 = 180 V."
            )
        ),
        CircuitProblem(
            id = "p_219f",
            code = "No. 219F",
            title = "Parallel Tank Q-Factor",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.PARALLEL_RLC,
            prompt = "Compute the quality factor Q_p of a parallel RLC tank circuit:",
            parametersLine1 = "R = 2kΩ, L = 10mH",
            parametersLine2 = "C = 250nF (parallel)",
            leftMarker = "PARALLEL TANK",
            rightMarker = "Q = R√(C/L)",
            methodTitle = "PARALLEL Q LAW",
            methodNote = "Unlike series RLC, parallel Q scales directly with R: Q_p = R√(C/L).",
            inputHint = "Enter Q_p value",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 10.0,
            unit = "",
            hints = listOf(
                "Compute C / L = (250 × 10⁻⁹) / (10 × 10⁻³) = 25 × 10⁻⁶.",
                "√(25 × 10⁻⁶) = 5 × 10⁻³ = 0.005; multiply by R = 2000 Ω."
            ),
            workedSteps = listOf(
                "1. C / L = (2.5 × 10⁻⁷) / (10⁻²) = 25 × 10⁻⁶.",
                "2. √(C / L) = 5 × 10⁻³.",
                "3. Q_p = R√(C/L) = 2000 × 0.005 = 10."
            )
        ),
        CircuitProblem(
            id = "p_301a",
            code = "No. 301A",
            title = "Complex Power Factor",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "A single-phase AC load draws complex power S. Find its power factor:",
            parametersLine1 = "S = (800 + j600) VA",
            parametersLine2 = "V_rms = 200 V",
            leftMarker = "REAL P = 800W",
            rightMarker = "REACTIVE Q = 600VAR",
            methodTitle = "POWER TRIANGLE",
            methodNote = "Compute apparent power |S| = √(P² + Q²) and take the ratio P / |S|.",
            inputHint = "Enter power factor (0 to 1)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 0.8,
            unit = "",
            hints = listOf(
                "Apparent power |S| = √(800² + 600²).",
                "|S| = 1000 VA, so pf = P / |S| = 800 / 1000."
            ),
            workedSteps = listOf(
                "1. Real power P = 800 W, reactive power Q = 600 VAR.",
                "2. Apparent power |S| = √(800² + 600²) = 1000 VA.",
                "3. Power factor pf = cos(θ) = P / |S| = 800 / 1000 = 0.8 (lagging)."
            )
        ),
        CircuitProblem(
            id = "p_305b",
            code = "No. 305B",
            title = "Phasor Load Current",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "Calculate the complex rms current phasor I (in Amps) drawn by load Z:",
            parametersLine1 = "V = 120∠0° V rms",
            parametersLine2 = "Z = (6 + j8) Ω",
            leftMarker = "V REF (0°)",
            rightMarker = "LAGGING I",
            methodTitle = "OHM'S LAW PHASOR",
            methodNote = "Divide V by Z using polar magnitudes/angles or complex conjugate multiplication.",
            inputHint = "Enter complex current (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(7.2, -9.6),
            unit = "A",
            hints = listOf(
                "I = 120 / (6 + j8). Multiply numerator and denominator by (6 - j8).",
                "|Z|² = 6² + 8² = 100, so I = 1.2 × (6 - j8)."
            ),
            workedSteps = listOf(
                "1. I = V / Z = 120 / (6 + j8).",
                "2. Rationalize: I = 120(6 − j8) / (36 + 64) = 1.2(6 − j8).",
                "3. I = 7.2 − j9.6 A (or 12∠−53.13° A)."
            )
        ),
        CircuitProblem(
            id = "p_310c",
            code = "No. 310C",
            title = "Parallel Load Summing",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "Find total complex power S_total = P + jQ (in kVA) for two parallel loads:",
            parametersLine1 = "Load 1: 1.2kW @ 0.6 pf lag",
            parametersLine2 = "Load 2: 0.8kW @ 1.0 pf",
            leftMarker = "LOAD 1 + LOAD 2",
            rightMarker = "CONSERVATION OF S",
            methodTitle = "BOUCHEROT'S THEOREM",
            methodNote = "Sum real powers P and reactive powers Q independently across parallel branches.",
            inputHint = "Enter S_total in kVA (P + jQ)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(2.0, 1.6),
            unit = "kVA",
            hints = listOf(
                "For Load 1: |S₁| = P₁ / pf₁ = 1.2 / 0.6 = 2.0 kVA.",
                "Q₁ = √(|S₁|² - P₁²) = √(4 - 1.44) = 1.6 kVAR; Load 2 has Q₂ = 0."
            ),
            workedSteps = listOf(
                "1. Load 1 apparent power |S₁| = 1.2 / 0.6 = 2.0 kVA.",
                "2. Load 1 reactive power Q₁ = +√(2.0² − 1.2²) = +1.6 kVAR (lagging).",
                "3. Load 2 has pf = 1.0, so P₂ = 0.8 kW and Q₂ = 0 kVAR.",
                "4. S_total = (1.2 + 0.8) + j(1.6 + 0) = 2.0 + j1.6 kVA."
            )
        ),
        CircuitProblem(
            id = "p_314d",
            code = "No. 314D",
            title = "Complex Power Conjugate",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "Compute complex power S = V · I* (in VA) from the rms phasors:",
            parametersLine1 = "V_rms = 100∠0° V",
            parametersLine2 = "I_rms = (4 − j3) A",
            leftMarker = "CONJUGATE I*",
            rightMarker = "S = P + jQ",
            methodTitle = "CONJUGATE PRODUCT",
            methodNote = "Flip the imaginary sign of current I to form I* before multiplying by V.",
            inputHint = "Enter S in VA (P + jQ)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(400.0, 300.0),
            unit = "VA",
            hints = listOf(
                "The complex conjugate of (4 − j3) is I* = 4 + j3.",
                "Multiply V = 100 + j0 by I* = 4 + j3."
            ),
            workedSteps = listOf(
                "1. Take conjugate: I* = 4 + j3 A.",
                "2. S = V · I* = 100 · (4 + j3) = 400 + j300 VA (500∠36.87° VA)."
            )
        ),
        CircuitProblem(
            id = "p_318e",
            code = "No. 318E",
            title = "Power Factor Correction",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "Find the parallel capacitance C (in μF) required to correct pf to unity (1.0):",
            parametersLine1 = "Load: P = 1.2kW, Q = 1.6kVAR lag",
            parametersLine2 = "V_rms = 200 V, ω = 100 rad/s",
            leftMarker = "CANCEL +Q_L",
            rightMarker = "UNITY PF (1.0)",
            methodTitle = "REACTIVE COMPENSATION",
            methodNote = "Set capacitor reactive power ω·C·V_rms² equal to inductive Q.",
            inputHint = "Enter capacitance in μF",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 400.0,
            unit = "μF",
            hints = listOf(
                "To reach unity pf, the capacitor must supply Q_C = 1600 VAR.",
                "Solve C = Q_C / (ω · V_rms²) = 1600 / (100 × 200²), then convert to μF."
            ),
            workedSteps = listOf(
                "1. Required compensation Q_C = 1600 VAR.",
                "2. C = Q_C / (ω · V_rms²) = 1600 / (100 × 40000) = 4 × 10⁻⁴ F.",
                "3. Convert to microfarads: C = 400 μF."
            )
        ),
        CircuitProblem(
            id = "p_322f",
            code = "No. 322F",
            title = "AC Max Power Transfer",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "Find maximum average power P_max (in Watts) delivered to a matched load Z_L = Z_th*:",
            parametersLine1 = "V_th = 20∠0° V rms",
            parametersLine2 = "Z_th = (4 + j3) Ω",
            leftMarker = "CONJUGATE MATCH",
            rightMarker = "Z_L = 4 − j3 Ω",
            methodTitle = "MAXIMUM POWER THEOREM",
            methodNote = "At conjugate match, reactive parts cancel and P_max = |V_th|² / (4 · R_th).",
            inputHint = "Enter P_max in Watts",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 25.0,
            unit = "W",
            hints = listOf(
                "With Z_L = 4 − j3 Ω, total loop impedance is Z_th + Z_L = 8 Ω.",
                "Current |I| = 20 / 8 = 2.5 A; compute P_max = |I|² · R_L."
            ),
            workedSteps = listOf(
                "1. Optimal load is Z_L = Z_th* = 4 − j3 Ω.",
                "2. Total series impedance Z_tot = (4 + j3) + (4 − j3) = 8 Ω.",
                "3. Current I = 20 / 8 = 2.5 A rms.",
                "4. P_max = I² · R_L = (2.5)² × 4 = 25 W."
            )
        ),
        CircuitProblem(
            id = "p_402a",
            code = "No. 402A",
            title = "RC Filter Cutoff Gain",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.LOW_PASS_RC,
            prompt = "Find the transfer magnitude |H(jω)| of an RC low-pass filter at cutoff:",
            parametersLine1 = "H(s) = 1 / (1 + sRC)",
            parametersLine2 = "Evaluate at ω = ω_c = 1/(RC)",
            leftMarker = "PASSBAND (1.0)",
            rightMarker = "-3dB CORNER",
            methodTitle = "FREQUENCY RESPONSE",
            methodNote = "Substitute s = jω_c into H(s) and take the complex modulus |H(jω_c)|.",
            inputHint = "Enter magnitude (e.g. 0.707)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 1.0 / sqrt(2.0),
            unit = "",
            hints = listOf(
                "At ω_c = 1/RC, the product ω_c·RC equals 1.",
                "H(jω_c) = 1 / (1 + j1), so |H| = 1 / √(1² + 1²) = 1/√2."
            ),
            workedSteps = listOf(
                "1. Substitute s = jω_c = j/(RC) into H(s) = 1/(1 + sRC).",
                "2. H(jω_c) = 1 / (1 + j).",
                "3. |H(jω_c)| = 1 / √(1² + 1²) = 1/√2 ≈ 0.707."
            )
        ),
        CircuitProblem(
            id = "p_495c",
            code = "No. 495C",
            title = "Nyquist & Routh Stability",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.FEEDBACK_LOOP,
            prompt = "Find the critical positive gain K for marginal closed-loop stability:",
            parametersLine1 = "G(s)H(s) = K / (s(s+1)(s+2))",
            parametersLine2 = "1 + G(s)H(s) = 0",
            leftMarker = "LHP STABLE",
            rightMarker = "jω CROSSING",
            methodTitle = "ROUTH-HURWITZ CRITERION",
            methodNote = "Expand characteristic polynomial s³ + 3s² + 2s + K = 0 and check row s¹.",
            inputHint = "Enter critical gain K",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 6.0,
            unit = "",
            hints = listOf(
                "Characteristic equation: s(s² + 3s + 2) + K = s³ + 3s² + 2s + K = 0.",
                "Routh condition for a cubic s³ + a₂s² + a₁s + a₀ = 0 requires a₂·a₁ > a₀."
            ),
            workedSteps = listOf(
                "1. Expand: s(s + 1)(s + 2) + K = s³ + 3s² + 2s + K = 0.",
                "2. Routh array s¹ entry is (3×2 − 1×K)/3 = (6 − K)/3.",
                "3. Marginal stability occurs at 6 − K = 0, so K_crit = 6."
            )
        ),
        CircuitProblem(
            id = "p_496d",
            code = "No. 496D",
            title = "Laplace Final Value Theorem",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.FEEDBACK_LOOP,
            prompt = "Use the Final Value Theorem to find steady-state output y(∞) for:",
            parametersLine1 = "Y(s) = 12(s + 2) / (s(s + 3)(s + 4))",
            parametersLine2 = "y(∞) = lim(s→0) s·Y(s)",
            leftMarker = "LHP POLES (-3,-4)",
            rightMarker = "t → ∞",
            methodTitle = "ASYMPTOTIC LIMIT",
            methodNote = "Cancel the s factor and evaluate the remaining rational function at s = 0.",
            inputHint = "Enter steady-state value y(∞)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 2.0,
            unit = "",
            hints = listOf(
                "Multiply Y(s) by s to cancel the pole at the origin.",
                "Evaluate 12(0 + 2) / ((0 + 3)(0 + 4))."
            ),
            workedSteps = listOf(
                "1. Nonzero poles s = −3, −4 lie strictly in the left-half plane, so FVT applies.",
                "2. s·Y(s) = 12(s + 2) / ((s + 3)(s + 4)).",
                "3. Evaluate at s = 0: 24 / (3 × 4) = 2."
            )
        ),
        CircuitProblem(
            id = "p_498e",
            code = "No. 498E",
            title = "Second-Order Damping Ratio",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Calculate the dimensionless damping ratio ζ of the series RLC network:",
            parametersLine1 = "R = 6Ω, L = 0.5H",
            parametersLine2 = "C = 0.02F (underdamped)",
            leftMarker = "α = R/(2L)",
            rightMarker = "ζ = α / ω₀",
            methodTitle = "POLE DAMPING",
            methodNote = "Find attenuation α = R/(2L) and natural frequency ω₀ = 1/√(LC).",
            inputHint = "Enter damping ratio ζ",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 0.6,
            unit = "",
            hints = listOf(
                "ω₀ = 1 / √(0.5 × 0.02) = 1 / √0.01 = 10 rad/s.",
                "α = 6 / (2 × 0.5) = 6 s⁻¹, so ζ = α / ω₀."
            ),
            workedSteps = listOf(
                "1. Natural frequency ω₀ = 1/√(0.5 × 0.02) = 10 rad/s.",
                "2. Neper frequency α = R/(2L) = 6 / 1.0 = 6 s⁻¹.",
                "3. Damping ratio ζ = α / ω₀ = 6 / 10 = 0.6."
            )
        ),
        CircuitProblem(
            id = "p_499d",
            code = "No. 499D",
            title = "Off-Resonance RLC Phasor",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Solve for the steady-state current phasor I (in Amps) in the series loop:",
            parametersLine1 = "R = 8Ω, L = 0.2H, C = 5mF",
            parametersLine2 = "E = 100∠0° V, ω = 20 rad/s",
            leftMarker = "CAPACITIVE NET",
            rightMarker = "LEADING CURRENT",
            methodTitle = "STEADY-STATE PHASOR",
            methodNote = "Compute net reactance X = ωL − 1/(ωC), then evaluate I = E / (R + jX).",
            inputHint = "Enter complex current (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(8.0, 6.0),
            unit = "A",
            hints = listOf(
                "X_L = 20 × 0.2 = 4 Ω; X_C = 1 / (20 × 0.005) = 10 Ω.",
                "Z = 8 + j(4 - 10) = 8 - j6 Ω; divide 100∠0° by (8 - j6)."
            ),
            workedSteps = listOf(
                "1. X_L = ωL = 4 Ω; X_C = 1/(ωC) = 10 Ω.",
                "2. Net impedance Z = 8 + j(4 − 10) = 8 − j6 Ω.",
                "3. I = 100 / (8 − j6) = 100(8 + j6) / (64 + 36) = 8 + j6 A (10∠36.87° A)."
            )
        ),
        CircuitProblem(
            id = "p_501a",
            code = "No. 501A",
            title = "Complex Admittance Y = G + jB",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.SERIES_RC,
            prompt = "Convert the series impedance Z to complex admittance Y = 1/Z (in Siemens):",
            parametersLine1 = "Z = (3 − j4) Ω",
            parametersLine2 = "Y = G + jB = 1 / Z",
            leftMarker = "CONDUCTANCE G",
            rightMarker = "SUSCEPTANCE +jB",
            methodTitle = "COMPLEX INVERSION",
            methodNote = "Rationalize 1 / (3 − j4) by multiplying by the conjugate (3 + j4).",
            inputHint = "Enter admittance Y (G + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(0.12, 0.16),
            unit = "S",
            hints = listOf(
                "|Z|² = 3² + (−4)² = 25 Ω².",
                "Y = (3 + j4) / 25 = 0.12 + j0.16 S."
            ),
            workedSteps = listOf(
                "1. Y = 1 / (3 − j4) = (3 + j4) / (3² + 4²).",
                "2. Y = (3 + j4) / 25 = 0.12 + j0.16 S (0.2∠53.13° S)."
            )
        ),
        CircuitProblem(
            id = "p_504b",
            code = "No. 504B",
            title = "Parallel Conductance & Susceptance",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "Find total parallel admittance Y = G + jB_C (in Siemens) for:",
            parametersLine1 = "R = 20Ω || C = 50μF",
            parametersLine2 = "ω = 1000 rad/s",
            leftMarker = "G = 1/R",
            rightMarker = "B_C = ωC",
            methodTitle = "PARALLEL ADMITTANCE",
            methodNote = "Sum conductance G = 1/R and capacitive susceptance +jωC directly.",
            inputHint = "Enter admittance Y (G + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(0.05, 0.05),
            unit = "S",
            hints = listOf(
                "G = 1 / 20 = 0.05 S.",
                "B_C = ω·C = 1000 × 50×10⁻⁶ = 0.05 S."
            ),
            workedSteps = listOf(
                "1. Conductance G = 1/R = 1/20 = 0.05 S.",
                "2. Capacitive susceptance B_C = ωC = 1000 × 50×10⁻⁶ = 0.05 S.",
                "3. Y = 0.05 + j0.05 S (0.0707∠45° S)."
            )
        ),
        CircuitProblem(
            id = "p_507c",
            code = "No. 507C",
            title = "Inductive Susceptance B_L",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.PARALLEL_RLC,
            prompt = "Find the signed inductive susceptance B_L (in Siemens, where Y_L = jB_L) for:",
            parametersLine1 = "L = 25 mH (ideal coil)",
            parametersLine2 = "ω = 400 rad/s",
            leftMarker = "Y_L = 1/(jωL)",
            rightMarker = "B_L = −1/(ωL)",
            methodTitle = "SUSCEPTANCE SIGN",
            methodNote = "Since 1/j = −j, inductive susceptance B_L carries a negative sign.",
            inputHint = "Enter signed B_L (e.g. -0.1)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = -0.1,
            unit = "S",
            hints = listOf(
                "X_L = ω·L = 400 × 0.025 = 10 Ω.",
                "Y_L = 1/(j10) = −j0.1 S, so B_L = −0.1 S."
            ),
            workedSteps = listOf(
                "1. Inductive reactance X_L = ωL = 400 × 0.025 = 10 Ω.",
                "2. Admittance Y_L = 1/(j10) = −j0.1 S.",
                "3. Therefore signed susceptance B_L = −0.1 S."
            )
        ),
        CircuitProblem(
            id = "p_510d",
            code = "No. 510D",
            title = "LCR Meter Series Resistance",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.SERIES_RL,
            prompt = "An LCR meter in Z-θ mode reads the following for a coil. Find series resistance R_s:",
            parametersLine1 = "|Z| = 50 Ω,  θ = +53.13°",
            parametersLine2 = "f_test = 159.15 Hz (ω = 1000 rad/s)",
            leftMarker = "POLAR READOUT",
            rightMarker = "R_s = |Z| cos θ",
            methodTitle = "LCR METER EXTRACTION",
            methodNote = "Project impedance magnitude onto the real axis: R_s = |Z|·cos(θ).",
            inputHint = "Enter R_s in Ohms",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 30.0,
            unit = "Ω",
            hints = listOf(
                "cos(53.13°) ≈ 0.6 (a 3-4-5 right triangle).",
                "R_s = 50 × 0.6 = 30 Ω."
            ),
            workedSteps = listOf(
                "1. Series resistance R_s = |Z| cos(θ) = 50 × cos(53.13°).",
                "2. R_s = 50 × 0.6 = 30 Ω (with X_s = 40 Ω)."
            )
        ),
        CircuitProblem(
            id = "p_514e",
            code = "No. 514E",
            title = "LCR Meter Inductance Extraction",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RL,
            prompt = "An LCR meter measures a coil's complex impedance in series mode. Find inductance L_s (in mH):",
            parametersLine1 = "Z = (12 + j45) Ω",
            parametersLine2 = "ω = 1000 rad/s",
            leftMarker = "X_s = 45 Ω",
            rightMarker = "L_s = X_s / ω",
            methodTitle = "REACTANCE TO INDUCTANCE",
            methodNote = "Extract imaginary reactance X_s = Im(Z) and divide by angular frequency ω.",
            inputHint = "Enter L_s in mH",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 45.0,
            unit = "mH",
            hints = listOf(
                "Inductive reactance is X_s = 45 Ω = ω·L_s.",
                "L_s = 45 / 1000 = 0.045 H; convert to millihenries (×1000)."
            ),
            workedSteps = listOf(
                "1. From Z = R_s + jX_s, reactance X_s = 45 Ω.",
                "2. L_s = X_s / ω = 45 / 1000 = 0.045 H = 45 mH."
            )
        ),
        CircuitProblem(
            id = "p_518f",
            code = "No. 518F",
            title = "LCR Meter Dissipation Factor D",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RC,
            prompt = "A capacitor tested on an LCR meter exhibits ESR and reactance X_C. Find dissipation factor D:",
            parametersLine1 = "ESR (R_s) = 2 Ω",
            parametersLine2 = "|X_C| = 100 Ω at test frequency",
            leftMarker = "LOSS TANGENT",
            rightMarker = "D = tan δ = 1/Q",
            methodTitle = "CAPACITOR LOSS FACTOR",
            methodNote = "Dissipation factor D equals series resistance divided by capacitive reactance.",
            inputHint = "Enter dissipation factor D",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 0.02,
            unit = "",
            hints = listOf(
                "D = tan(δ) = ESR / |X_C|.",
                "Compute 2 / 100."
            ),
            workedSteps = listOf(
                "1. D = ESR / |X_C| = 2 / 100 = 0.02.",
                "2. Equivalently, Q = 1/D = 50."
            )
        ),
        CircuitProblem(
            id = "p_522g",
            code = "No. 522G",
            title = "LCR Series-to-Parallel R_p",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "An LCR meter in series mode reads R_s and X_s. Convert to equivalent parallel resistance R_p:",
            parametersLine1 = "R_s = 10 Ω,  X_s = 30 Ω",
            parametersLine2 = "Quality Factor Q = X_s / R_s = 3",
            leftMarker = "SERIES MODE",
            rightMarker = "PARALLEL R_p",
            methodTitle = "EQUIVALENT MODEL",
            methodNote = "Use R_p = R_s · (1 + Q²) or invert Z to find G = Re(1/Z) and take R_p = 1/G.",
            inputHint = "Enter R_p in Ohms",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 100.0,
            unit = "Ω",
            hints = listOf(
                "With Q = 30 / 10 = 3, apply R_p = R_s × (1 + Q²).",
                "R_p = 10 × (1 + 9) = 100 Ω."
            ),
            workedSteps = listOf(
                "1. Quality factor Q = X_s / R_s = 30 / 10 = 3.",
                "2. R_p = R_s(1 + Q²) = 10(1 + 9) = 100 Ω.",
                "3. Check via admittance: Y = (10 − j30)/1000 = 0.01 − j0.03 S → R_p = 1/0.01 = 100 Ω."
            )
        ),
        CircuitProblem(
            id = "p_526h",
            code = "No. 526H",
            title = "Net Admittance to Impedance",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.PARALLEL_RLC,
            prompt = "A parallel network has conductance G and susceptances B_C, B_L. Find total impedance Z = 1/Y:",
            parametersLine1 = "G = 0.3 S,  B_C = +0.6 S",
            parametersLine2 = "B_L = −0.2 S",
            leftMarker = "Y = G + j(B_C+B_L)",
            rightMarker = "Z = 1 / Y",
            methodTitle = "ADMITTANCE INVERSION",
            methodNote = "Sum net susceptance B = B_C + B_L, then invert Y = G + jB to obtain Z.",
            inputHint = "Enter Z in Ohms (R + jX)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(1.2, -1.6),
            unit = "Ω",
            hints = listOf(
                "Net admittance Y = 0.3 + j(0.6 − 0.2) = 0.3 + j0.4 S.",
                "|Y|² = 0.3² + 0.4² = 0.25, so Z = (0.3 − j0.4) / 0.25."
            ),
            workedSteps = listOf(
                "1. Net susceptance B = 0.6 − 0.2 = +0.4 S → Y = 0.3 + j0.4 S.",
                "2. Z = 1 / (0.3 + j0.4) = (0.3 − j0.4) / (0.09 + 0.16).",
                "3. Z = (0.3 − j0.4) / 0.25 = 1.2 − j1.6 Ω (2∠−53.13° Ω)."
            )
        ),
        CircuitProblem(
            id = "p_601a",
            code = "No. 601A",
            title = "Cauchy-Riemann Analyticity",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Determine the real constant k so that f(z) is entire (analytic everywhere in ℂ):",
            parametersLine1 = "f(z) = (x² − y²) + j(k·x·y)",
            parametersLine2 = "∂u/∂x = ∂v/∂y,  ∂u/∂y = −∂v/∂x",
            leftMarker = "HOLOMORPHIC",
            rightMarker = "CAUCHY-RIEMANN",
            methodTitle = "ANALYTICITY CONDITION",
            methodNote = "Equate partial derivatives u_x = v_y and u_y = −v_x to solve for k.",
            inputHint = "Enter constant k",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 2.0,
            unit = "",
            hints = listOf(
                "Compute u_x = ∂(x² − y²)/∂x = 2x and v_y = ∂(kxy)/∂y = kx.",
                "Setting 2x = kx yields k = 2 (recovering f(z) = z²)."
            ),
            workedSteps = listOf(
                "1. u(x,y) = x² − y² → u_x = 2x, u_y = −2y.",
                "2. v(x,y) = kxy → v_x = ky, v_y = kx.",
                "3. Cauchy-Riemann requires 2x = kx and −2y = −ky → k = 2."
            )
        ),
        CircuitProblem(
            id = "p_604b",
            code = "No. 604B",
            title = "Möbius Smith Chart Reflection Γ",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "Map load Z_L onto the unit disk via the Möbius transform Γ = (Z_L − Z₀)/(Z_L + Z₀):",
            parametersLine1 = "Z_L = (50 + j100) Ω",
            parametersLine2 = "Z₀ = 50 Ω (characteristic impedance)",
            leftMarker = "RHP IMPEDANCE",
            rightMarker = "UNIT DISK |Γ| < 1",
            methodTitle = "CONFORMAL MAPPING",
            methodNote = "Substitute Z_L and Z₀ into Γ and rationalize the complex quotient.",
            inputHint = "Enter complex Γ (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(0.5, 0.5),
            unit = "",
            hints = listOf(
                "Γ = (50 + j100 − 50) / (50 + j100 + 50) = j100 / (100 + j100) = j / (1 + j).",
                "Multiply j / (1 + j) by (1 − j)/(1 − j)."
            ),
            workedSteps = listOf(
                "1. Γ = (Z_L − Z₀)/(Z_L + Z₀) = j100 / (100 + j100) = j / (1 + j).",
                "2. Rationalize: j(1 − j)/2 = (1 + j)/2 = 0.5 + j0.5 (0.707∠45°)."
            )
        ),
        CircuitProblem(
            id = "p_608c",
            code = "No. 608C",
            title = "Complex Simple Pole Residue",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Compute the complex residue Res[F(s), s = −1 + j2] for the rational function:",
            parametersLine1 = "F(s) = (s + 3) / ((s + 1)² + 4)",
            parametersLine2 = "Evaluate at simple pole s₀ = −1 + j2",
            leftMarker = "RESIDUE CALCULUS",
            rightMarker = "SIMPLE POLE",
            methodTitle = "LAURENT COEFFICIENT a₋₁",
            methodNote = "Factor ((s+1)² + 4) = (s + 1 − j2)(s + 1 + j2) and apply cover-up at s₀.",
            inputHint = "Enter complex residue (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(0.5, -0.5),
            unit = "",
            hints = listOf(
                "Res = lim(s→s₀) (s − s₀)F(s) = (s₀ + 3) / (s₀ + 1 + j2).",
                "Substitute s₀ = −1 + j2 to get (2 + j2) / (j4)."
            ),
            workedSteps = listOf(
                "1. Factor denominator: (s + 1 − j2)(s + 1 + j2).",
                "2. Res[F, −1 + j2] = (−1 + j2 + 3) / (−1 + j2 + 1 + j2) = (2 + j2) / (j4).",
                "3. Divide by j4 (multiply by −j): (2 − j2)/4 = 0.5 − j0.5."
            )
        ),
        CircuitProblem(
            id = "p_612d",
            code = "No. 612D",
            title = "Second-Order Pole Residue",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Evaluate the residue at the double pole s = −2 of F(s):",
            parametersLine1 = "F(s) = (s² + 1) / (s(s + 2)²)",
            parametersLine2 = "Order m = 2 pole at s = −2",
            leftMarker = "DOUBLE POLE",
            rightMarker = "d/ds [(s+2)² F(s)]",
            methodTitle = "HIGHER-ORDER RESIDUE",
            methodNote = "Use Res[F, −2] = lim(s→−2) d/ds [ (s + 2)² · F(s) ].",
            inputHint = "Enter residue value",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 0.75,
            unit = "",
            hints = listOf(
                "After multiplying by (s + 2)², differentiate g(s) = (s² + 1)/s = s + 1/s.",
                "g'(s) = 1 − 1/s²; evaluate at s = −2."
            ),
            workedSteps = listOf(
                "1. g(s) = (s + 2)² F(s) = (s² + 1)/s = s + s⁻¹.",
                "2. g'(s) = 1 − s⁻².",
                "3. Res[F, −2] = g'(−2) = 1 − 1/4 = 0.75."
            )
        ),
        CircuitProblem(
            id = "p_616e",
            code = "No. 616E",
            title = "Cauchy Residue Contour Integral",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Evaluate the normalized counterclockwise contour integral around |z| = 2:",
            parametersLine1 = "I = (1 / j2π) ∮ f(z) dz  along |z| = 2",
            parametersLine2 = "f(z) = (z² + 6) / (z(z − 1)(z − 3))",
            leftMarker = "ENCLOSED: 0, 1",
            rightMarker = "EXCLUDED: 3",
            methodTitle = "CAUCHY RESIDUE THEOREM",
            methodNote = "Sum only residues of poles lying strictly inside the circle |z| = 2.",
            inputHint = "Enter contour integral value",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = -1.5,
            unit = "",
            hints = listOf(
                "Poles are at z = 0, z = 1 (inside |z| = 2) and z = 3 (outside |z| = 2).",
                "Res(0) = 6 / ((−1)(−3)) = 2; Res(1) = 7 / (1·(−2)) = −3.5."
            ),
            workedSteps = listOf(
                "1. Identify enclosed poles inside |z| = 2: z = 0 and z = 1 (z = 3 is outside).",
                "2. Res[f, 0] = 6 / ((−1)(−3)) = 2.",
                "3. Res[f, 1] = (1 + 6) / (1 · (1 − 3)) = −3.5.",
                "4. (1/j2π) ∮ f(z)dz = Res(0) + Res(1) = 2 − 3.5 = −1.5."
            )
        ),
        CircuitProblem(
            id = "p_620f",
            code = "No. 620F",
            title = "Bromwich Contour Inverse Laplace",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Evaluate f(t) = (1/j2π) ∫ F(s)e^(st) ds via Bromwich residues at t = π/3 s:",
            parametersLine1 = "F(s) = 4s / ((s² + 1)(s² + 9))",
            parametersLine2 = "Evaluate f(t) at t = π/3 seconds",
            leftMarker = "POLES ±j, ±j3",
            rightMarker = "RESIDUE SUM",
            methodTitle = "BROMWICH INVERSION",
            methodNote = "Close the Bromwich line in the LHP and sum the four imaginary-axis pole residues.",
            inputHint = "Enter f(π/3)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 0.75,
            unit = "",
            hints = listOf(
                "Sum residues at ±j (giving 0.5 cos t) and ±j3 (giving −0.5 cos 3t).",
                "f(t) = 0.5(cos t − cos 3t); evaluate at t = π/3 where cos(π/3) = 0.5 and cos(π) = −1."
            ),
            workedSteps = listOf(
                "1. Res at s = ±j gives (4j e^(jt))/(2j·8) + conj = 0.5 cos(t).",
                "2. Res at s = ±j3 gives (12j e^(j3t))/(−8·6j) + conj = −0.5 cos(3t).",
                "3. f(π/3) = 0.5(cos(π/3) − cos(π)) = 0.5(0.5 − (−1)) = 0.75."
            )
        ),
        CircuitProblem(
            id = "p_624g",
            code = "No. 624G",
            title = "Nyquist Argument Principle",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.FEEDBACK_LOOP,
            prompt = "Apply Cauchy's Argument Principle (Z = P − N_CCW) to find closed-loop RHP poles Z:",
            parametersLine1 = "Open-loop RHP poles: P = 3",
            parametersLine2 = "Net CCW encirclements of (−1 + j0): N_CCW = 2",
            leftMarker = "ARGUMENT PRINCIPLE",
            rightMarker = "WINDING NUMBER",
            methodTitle = "NYQUIST ENCIRCLEMENT",
            methodNote = "Each CCW encirclement of −1 cancels one open-loop RHP pole in 1 + G(s)H(s).",
            inputHint = "Enter unstable RHP pole count Z",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 1.0,
            unit = "",
            hints = listOf(
                "Use Z = P − N_CCW directly.",
                "Z = 3 − 2 = 1 unstable closed-loop pole (system remains unstable)."
            ),
            workedSteps = listOf(
                "1. Argument Principle on 1 + G(s)H(s): N_CW = Z − P, so Z = P − N_CCW.",
                "2. Z = 3 − 2 = 1 closed-loop pole in the right-half plane."
            )
        ),
        CircuitProblem(
            id = "p_701a",
            code = "No. 701A",
            title = "Cl₂,₀ Pseudoscalar Bivector Square",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "In Euclidean Geometric Algebra Cl₂,₀ (e₁² = e₂² = 1, e₁e₂ = −e₂e₁), compute (e₁₂)²:",
            parametersLine1 = "e₁₂ = e₁ ∧ e₂ = e₁e₂",
            parametersLine2 = "Evaluate scalar (e₁₂)² = e₁e₂e₁e₂",
            leftMarker = "UNIT BIVECTOR",
            rightMarker = "COMPLEX i ISOMORPH",
            methodTitle = "CLIFFORD ANTICOMMUTATION",
            methodNote = "Swap adjacent orthogonal basis vectors using e₂e₁ = −e₁e₂.",
            inputHint = "Enter scalar value",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = -1.0,
            unit = "",
            hints = listOf(
                "Write (e₁e₂)(e₁e₂) = e₁(e₂e₁)e₂.",
                "Substitute e₂e₁ = −e₁e₂ to get −e₁²e₂² = −1."
            ),
            workedSteps = listOf(
                "1. (e₁₂)² = e₁e₂e₁e₂ = e₁(−e₁e₂)e₂.",
                "2. = −(e₁²)(e₂²) = −(1)(1) = −1 (showing why Cl₂,₀⁺ ≅ ℂ)."
            )
        ),
        CircuitProblem(
            id = "p_704b",
            code = "No. 704B",
            title = "Geometric Product a·b + a∧b",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Compute the geometric product ab = (a·b) + (a∧b) in Cl₂,₀ (enter α + βe12 or α + jβ):",
            parametersLine1 = "a = 3e₁ + 4e₂",
            parametersLine2 = "b = 2e₁ + 5e₂",
            leftMarker = "INNER a·b",
            rightMarker = "OUTER a∧b",
            methodTitle = "MULTIVECTOR PRODUCT",
            methodNote = "Scalar part is a₁b₁ + a₂b₂; bivector coefficient is (a₁b₂ − a₂b₁)e₁₂.",
            inputHint = "Enter α + βe12 (or α + jβ)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(26.0, 7.0),
            unit = "",
            hints = listOf(
                "Dot product a·b = 3×2 + 4×5 = 26.",
                "Wedge product a∧b = (3×5 − 4×2)e₁₂ = 7e₁₂."
            ),
            workedSteps = listOf(
                "1. Inner product a·b = 3(2) + 4(5) = 26.",
                "2. Outer product a∧b = (3·5 − 4·2)e₁₂ = 7e₁₂.",
                "3. Geometric product ab = 26 + 7e₁₂."
            )
        ),
        CircuitProblem(
            id = "p_708c",
            code = "No. 708C",
            title = "Rotor Sandwich Rotation",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Rotate v = 5e₁ by +90° CCW via v' = R v R̃ in Cl₂,₀. Find the e₂-component of v':",
            parametersLine1 = "v = 5e₁,  R = (1 − e₁₂)/√2",
            parametersLine2 = "R̃ = (1 + e₁₂)/√2  (reverse of R)",
            leftMarker = "HALF-ANGLE θ/2 = 45°",
            rightMarker = "SANDWICH R v R̃",
            methodTitle = "ROTOR ACTION",
            methodNote = "Use e₁₂e₁ = −e₂, e₁e₁₂ = +e₂, and e₂e₁₂ = −e₁ when expanding R v R̃.",
            inputHint = "Enter e₂ coefficient",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 5.0,
            unit = "",
            hints = listOf(
                "Compute R e₁ = (e₁ − e₁₂e₁)/√2 = (e₁ + e₂)/√2.",
                "Multiply on right by R̃ = (1 + e₁₂)/√2; the e₁ terms cancel leaving +5e₂."
            ),
            workedSteps = listOf(
                "1. R e₁ = (1/√2)(e₁ − e₁₂e₁) = (1/√2)(e₁ + e₂).",
                "2. (R e₁)R̃ = (1/2)(e₁ + e₂)(1 + e₁₂) = (1/2)(e₁ + e₂ + e₂ − e₁) = +e₂.",
                "3. Scaling by 5 gives v' = +5e₂, so the e₂-component is 5."
            )
        ),
        CircuitProblem(
            id = "p_712d",
            code = "No. 712D",
            title = "2D Geometric Power Multivector",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Compute the geometric power M = v·i + v∧i = P + B·e₁₂ (enter P + Be12 or P + jB):",
            parametersLine1 = "v = 10e₁  (voltage vector)",
            parametersLine2 = "i = 8e₁ − 6e₂  (current vector)",
            leftMarker = "ACTIVE P = v·i",
            rightMarker = "REACTIVE B = v∧i",
            methodTitle = "GA POWER THEORY",
            methodNote = "Take the direct geometric product (10e₁)(8e₁ − 6e₂) in Cl₂,₀.",
            inputHint = "Enter M (e.g. 80 - 60e12)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(80.0, -60.0),
            unit = "VA",
            hints = listOf(
                "Active power P = 10 × 8 = 80 W.",
                "Bivector reactive part v∧i = 10e₁ ∧ (−6e₂) = −60e₁₂."
            ),
            workedSteps = listOf(
                "1. M = (10e₁)(8e₁ − 6e₂) = 80 e₁² − 60 e₁e₂.",
                "2. M = 80 − 60e₁₂ VA (apparent norm ||M|| = 100 VA)."
            )
        ),
        CircuitProblem(
            id = "p_716e",
            code = "No. 716E",
            title = "3D Non-Sinusoidal Power Norm Cl₃,₀",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "In harmonic basis Cl₃,₀, find apparent geometric power ||M|| = √(P² + ||v∧i||²) for:",
            parametersLine1 = "v = 3e₁ + 4e₂ + 12e₃",
            parametersLine2 = "i = 2e₁ + 3e₂ + 6e₃",
            leftMarker = "P = v·i = 90",
            rightMarker = "||B_Q|| = ||v∧i||",
            methodTitle = "CLIFFORD NORM MULTIPLICATIVITY",
            methodNote = "Use the identity ||v i|| = ||v|| · ||i|| or sum P² + B₁₂² + B₂₃² + B₃₁².",
            inputHint = "Enter apparent norm ||M||",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 91.0,
            unit = "VA",
            hints = listOf(
                "Compute ||v|| = √(3² + 4² + 12²) = 13 and ||i|| = √(2² + 3² + 6²) = 7.",
                "In Cl₃,₀, ||M|| = ||v|| · ||i|| = 13 × 7 (or verify √(90² + 1² + 12² + 6²))."
            ),
            workedSteps = listOf(
                "1. ||v|| = √(9 + 16 + 144) = 13; ||i|| = √(4 + 9 + 36) = 7.",
                "2. Active P = 90; distortion bivector B_Q = 1e₁₂ − 12e₂₃ + 6e₃₁ (||B_Q||² = 181).",
                "3. ||M|| = √(90² + 181) = √8281 = 91 VA (= 13 × 7)."
            )
        ),
        CircuitProblem(
            id = "p_720f",
            code = "No. 720F",
            title = "Cl₂,₀ Multivector Inverse",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Compute the multivector inverse M⁻¹ = M̃ / (M M̃) in Cl₂,₀ (enter α + βe12 or α + jβ):",
            parametersLine1 = "M = 3 + 4e₁₂",
            parametersLine2 = "Reversion M̃ = 3 − 4e₁₂",
            leftMarker = "NORM² = M M̃",
            rightMarker = "M⁻¹ = M̃ / 25",
            methodTitle = "MULTIVECTOR INVERSION",
            methodNote = "Compute the scalar product M M̃ using (e₁₂)² = −1, then divide M̃ by M M̃.",
            inputHint = "Enter M⁻¹ (e.g. 0.12 - 0.16e12)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(0.12, -0.16),
            unit = "",
            hints = listOf(
                "M M̃ = (3 + 4e₁₂)(3 − 4e₁₂) = 9 − 16(−1) = 25.",
                "M⁻¹ = (3 − 4e₁₂) / 25 = 0.12 − 0.16e₁₂."
            ),
            workedSteps = listOf(
                "1. M M̃ = 3² − 4²(e₁₂)² = 9 + 16 = 25.",
                "2. M⁻¹ = (3 − 4e₁₂) / 25 = 0.12 − 0.16e₁₂."
            )
        ),
        CircuitProblem(
            id = "p_124g",
            code = "No. 124G",
            title = "AC Thévenin Conjugate Parallel",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "Find the open-circuit Thévenin impedance Z_th (in Ohms) of two parallel conjugate branches:",
            parametersLine1 = "Z₁ = (4 + j4) Ω   ||   Z₂ = (4 − j4) Ω",
            parametersLine2 = "Z_th = (Z₁ · Z₂) / (Z₁ + Z₂)",
            leftMarker = "Z₁ ∥ Z₂",
            rightMarker = "REAL Z_th",
            methodTitle = "CONJUGATE PARALLEL REDUCTION",
            methodNote = "Multiply the complex conjugates (a+jb)(a−jb) = a²+b², then divide by the purely real sum.",
            inputHint = "Enter Z_th in Ω (e.g. 4 or 4 + j0)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(4.0, 0.0),
            unit = "Ω",
            hints = listOf(
                "Z₁ · Z₂ = (4 + j4)(4 − j4) = 16 + 16 = 32 Ω².",
                "Z₁ + Z₂ = (4 + j4) + (4 − j4) = 8 Ω."
            ),
            workedSteps = listOf(
                "1. Product: (4 + j4)(4 − j4) = 4² + 4² = 32.",
                "2. Sum: (4 + j4) + (4 − j4) = 8.",
                "3. Z_th = 32 / 8 = 4 Ω (purely resistive)."
            )
        ),
        CircuitProblem(
            id = "p_128h",
            code = "No. 128H",
            title = "Ideal Transformer Reflected Impedance",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "An ideal transformer with turns ratio N₁:N₂ = 2:1 feeds load Z_L. Find primary input impedance Z_in:",
            parametersLine1 = "N₁ : N₂ = 2 : 1  (a = N₁/N₂ = 2)",
            parametersLine2 = "Z_L = (5 − j3) Ω",
            leftMarker = "PRIMARY N₁",
            rightMarker = "a² Z_L REFLECT",
            methodTitle = "IMPEDANCE REFLECTION",
            methodNote = "Multiply secondary load impedance by the squared turns ratio a² = (N₁/N₂)².",
            inputHint = "Enter Z_in in Ω (e.g. 20 - j12)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(20.0, -12.0),
            unit = "Ω",
            hints = listOf(
                "Turns ratio a = N₁/N₂ = 2, so impedance scales by a² = 4.",
                "Z_in = 4 × (5 − j3) Ω."
            ),
            workedSteps = listOf(
                "1. Scaling factor a² = (2 / 1)² = 4.",
                "2. Z_in = 4(5 − j3) = 20 − j12 Ω."
            )
        ),
        CircuitProblem(
            id = "p_326g",
            code = "No. 326G",
            title = "3-Phase Δ-to-Y Load Transform",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "A balanced three-phase Δ-connected load has per-phase impedance Z_Δ. Find the equivalent Y impedance Z_Y:",
            parametersLine1 = "Z_Δ = (12 + j9) Ω per phase",
            parametersLine2 = "Balanced 3φ Δ → Y Equivalence",
            leftMarker = "DELTA Z_Δ",
            rightMarker = "WYE Z_Δ / 3",
            methodTitle = "THREE-PHASE Δ–Y TRANSFORM",
            methodNote = "For identical impedances in a balanced Delta network, Z_Y = Z_Δ / 3.",
            inputHint = "Enter Z_Y in Ω (e.g. 4 + j3)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(4.0, 3.0),
            unit = "Ω",
            hints = listOf(
                "For equal delta legs, dividing Z_Δ by 3 yields the equivalent wye leg.",
                "Z_Y = (12 + j9) / 3."
            ),
            workedSteps = listOf(
                "1. Balanced Δ–Y formula: Z_Y = Z_Δ / 3.",
                "2. Z_Y = (12 + j9) / 3 = 4 + j3 Ω."
            )
        ),
        CircuitProblem(
            id = "p_497f",
            code = "No. 497F",
            title = "Bode Magnitude Gain in dB",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.LOW_PASS_RC,
            prompt = "A single-pole low-pass filter has straight-line Bode magnitude |H(jω)| = 0.1 at ω = 10ω_c. Find the gain in dB:",
            parametersLine1 = "|H(jω)| = 0.1  (one decade above ω_c)",
            parametersLine2 = "Gain_dB = 20 · log₁₀|H(jω)|",
            leftMarker = "ω = 10ω_c",
            rightMarker = "−20 dB/DEC",
            methodTitle = "DECIBEL BODE SCALE",
            methodNote = "Evaluate 20·log₁₀(0.1) for power/voltage transfer magnitude in decibels.",
            inputHint = "Enter gain in dB (e.g. -20)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = -20.0,
            unit = "dB",
            hints = listOf(
                "log₁₀(0.1) = log₁₀(10⁻¹) = −1.",
                "Multiply by 20 for voltage/transfer ratio dB."
            ),
            workedSteps = listOf(
                "1. log₁₀(0.1) = −1.",
                "2. 20 · log₁₀(0.1) = 20 × (−1) = −20 dB."
            )
        ),
        CircuitProblem(
            id = "p_529i",
            code = "No. 529I",
            title = "LCR Meter Parallel C_p Readout",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "An LCR meter in C_p–G parallel mode measures complex admittance Y at ω = 1000 rad/s. Find parallel capacitance C_p (in μF):",
            parametersLine1 = "Y = (0.02 + j0.05) S",
            parametersLine2 = "Test Frequency ω = 1000 rad/s",
            leftMarker = "G_p = 0.02S",
            rightMarker = "B_p = ωC_p",
            methodTitle = "LCR PARALLEL SUSCEPTANCE",
            methodNote = "Equate the positive capacitive susceptance B_p = Im(Y) to ωC_p and convert Farads to μF (×10⁶).",
            inputHint = "Enter C_p in μF",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 50.0,
            unit = "μF",
            hints = listOf(
                "B_p = Im(Y) = 0.05 S = ω C_p.",
                "C_p = 0.05 / 1000 = 0.00005 F = 50 μF."
            ),
            workedSteps = listOf(
                "1. Extract parallel susceptance B_p = 0.05 S.",
                "2. C_p = B_p / ω = 0.05 / 1000 = 5×10⁻⁵ F = 50 μF."
            )
        ),
        CircuitProblem(
            id = "p_628h",
            code = "No. 628H",
            title = "Principal Value of j^j",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Using the principal branch Log(z) = ln|z| + j·Arg(z), evaluate the principal value of j^j (to 3 decimals):",
            parametersLine1 = "z = j = e^(jπ/2)   (Arg(j) = π/2)",
            parametersLine2 = "j^j = exp(j · Log(j))",
            leftMarker = "BRANCH CUT",
            rightMarker = "REAL VALUE",
            methodTitle = "PRINCIPAL COMPLEX POWER",
            methodNote = "Substitute Log(j) = jπ/2 into exp(j · Log(j)) = e^(−π/2).",
            inputHint = "Enter decimal (e.g. 0.208)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 0.20788,
            tolerance = 0.02,
            unit = "",
            hints = listOf(
                "Log(j) = ln(1) + j(π/2) = jπ/2.",
                "j · (jπ/2) = −π/2 ≈ −1.5708, so evaluate e^(−π/2)."
            ),
            workedSteps = listOf(
                "1. On the principal branch, j = exp(jπ/2), so Log(j) = jπ/2.",
                "2. j^j = exp(j · jπ/2) = exp(−π/2) ≈ 0.2079 (purely real)."
            )
        ),
        CircuitProblem(
            id = "p_632i",
            code = "No. 632I",
            title = "Essential Singularity Laurent Residue",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Compute the residue Res[f(z), z = 0] at the essential singularity z = 0 via Laurent series expansion:",
            parametersLine1 = "f(z) = z² · sin(1/z)",
            parametersLine2 = "Expand sin(w) = w − w³/3! + w⁵/5! − … at w = 1/z",
            leftMarker = "ESSENTIAL POLE",
            rightMarker = "COEFF OF z⁻¹",
            methodTitle = "LAURENT SERIES RESIDUE",
            methodNote = "Expand z²·sin(1/z) in powers of z and extract the a₋₁ coefficient of z⁻¹.",
            inputHint = "Enter residue (e.g. -1/6 or -0.167)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = -1.0 / 6.0,
            unit = "",
            hints = listOf(
                "sin(1/z) = 1/z − 1/(6z³) + 1/(120z⁵) − …",
                "Multiply by z²: z²·sin(1/z) = z − 1/(6z) + …"
            ),
            workedSteps = listOf(
                "1. Laurent series of sin(1/z) = 1/z − 1/(3! z³) + …",
                "2. Multiply by z²: f(z) = z − 1/(6z) + 1/(120z³) − …",
                "3. The residue a₋₁ (coefficient of 1/z) is −1/6 ≈ −0.167."
            )
        ),
        CircuitProblem(
            id = "p_636j",
            code = "No. 636J",
            title = "Real Improper Integral via Contour",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Evaluate the normalized real improper integral by closing the contour in the upper half-plane (Im z > 0):",
            parametersLine1 = "I = (1/π) ∫₋∞^∞ dx / (x² + 4)",
            parametersLine2 = "Upper half-plane pole at z = +j2",
            leftMarker = "UHP SEMICIRCLE",
            rightMarker = "2πj·Res / π",
            methodTitle = "CONTOUR INTEGRATION",
            methodNote = "Compute Res[1/(z²+4), +j2], multiply by 2πj for the infinite integral, then divide by π.",
            inputHint = "Enter integral value I",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 0.5,
            unit = "",
            hints = listOf(
                "Factor z² + 4 = (z − j2)(z + j2); only z = +j2 lies in the UHP.",
                "Res[1/(z²+4), +j2] = 1/(j2 + j2) = 1/(j4)."
            ),
            workedSteps = listOf(
                "1. By Jordan's lemma on the upper semicircle, arc contribution → 0.",
                "2. ∫₋∞^∞ dx/(x²+4) = 2πj · Res[1/(z²+4), j2] = 2πj / (j4) = π/2.",
                "3. Normalize: I = (1/π)(π/2) = 0.5."
            )
        ),
        CircuitProblem(
            id = "p_724g",
            code = "No. 724G",
            title = "GA Fryze Orthogonal Current",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Given unit voltage direction v̂ and line current i in Cl₂,₀, find the non-active orthogonal current magnitude ||i_⊥|| = ||i ∧ v̂||:",
            parametersLine1 = "v̂ = 0.6e₁ + 0.8e₂  (||v̂|| = 1)",
            parametersLine2 = "i = 10e₁ A",
            leftMarker = "ACTIVE i·v̂",
            rightMarker = "REACTIVE ||i∧v̂||",
            methodTitle = "FRYZE BIVECTOR REJECTION",
            methodNote = "Compute the wedge product i ∧ v̂ = (i₁v̂₂ − i₂v̂₁)e₁₂ and take its magnitude.",
            inputHint = "Enter ||i_⊥|| in A",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 8.0,
            unit = "A",
            hints = listOf(
                "i ∧ v̂ = (10e₁) ∧ (0.6e₁ + 0.8e₂) = (10 × 0.8 − 0) e₁₂.",
                "Magnitude of 8e₁₂ is 8 A (active part is 6 A; 6² + 8² = 10²)."
            ),
            workedSteps = listOf(
                "1. Active projection: i_∥ = (i · v̂)v̂ has ||i_∥|| = 10 × 0.6 = 6 A.",
                "2. Non-active bivector rejection: i ∧ v̂ = 8e₁₂ ⇒ ||i_⊥|| = 8 A."
            )
        ),
        CircuitProblem(
            id = "p_728h",
            code = "No. 728H",
            title = "Cl₃,₀ Pseudoscalar Square",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "In 3D Euclidean Geometric Algebra Cl₃,₀ (e_k² = +1, orthogonal basis vectors anticommute), evaluate the pseudoscalar square I²:",
            parametersLine1 = "Unit Trivector (Pseudoscalar): I = e₁ e₂ e₃",
            parametersLine2 = "Evaluate I² = (e₁ e₂ e₃)(e₁ e₂ e₃)",
            leftMarker = "GRADE-3 TRIVECTOR",
            rightMarker = "DUALITY OP",
            methodTitle = "CLIFFORD PSEUDOSCALAR",
            methodNote = "Count the adjacent swaps needed to pair e₁²e₂²e₃² using e_i e_j = −e_j e_i (for i ≠ j).",
            inputHint = "Enter scalar value I²",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = -1.0,
            unit = "",
            hints = listOf(
                "Move the second e₁ past e₂ and e₃ (2 swaps ⇒ (−1)² = +1), giving e₁² e₂ e₃ e₂ e₃.",
                "Swap the inner e₃ e₂ to −e₂ e₃, yielding −e₂² e₃² = −1."
            ),
            workedSteps = listOf(
                "1. I² = e₁e₂e₃e₁e₂e₃ = (+1) e₁² e₂e₃e₂e₃ = e₂e₃e₂e₃.",
                "2. Swap e₃ and e₂: = −e₂² e₃² = −(1)(1) = −1."
            )
        ),
        CircuitProblem(
            id = "p_132i",
            code = "No. 132I",
            title = "Series-Opposing Coupled Inductors",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.SERIES_RL,
            prompt = "Two magnetically coupled coils (dots opposing) sit in series with R. Find Z at ω = 1000 rad/s:",
            parametersLine1 = "R = 30Ω, L₁ = 40mH, L₂ = 30mH",
            parametersLine2 = "M = 15mH (series opposing), ω = 1000 rad/s",
            leftMarker = "L_eq = L₁+L₂−2M",
            rightMarker = "MUTUAL COUPLING",
            methodTitle = "COUPLED INDUCTANCE",
            methodNote = "For series-opposing windings, mutual flux subtracts twice: L_eq = L₁ + L₂ − 2M.",
            inputHint = "Enter Z in Ω (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(30.0, 40.0),
            unit = "Ω",
            hints = listOf(
                "Compute equivalent inductance L_eq = 40mH + 30mH − 2(15mH) = 40mH.",
                "Reactance X_L = ωL_eq = 1000 × 0.04 = 40 Ω."
            ),
            workedSteps = listOf(
                "1. L_eq = 40 + 30 − 2(15) = 40 mH = 0.04 H.",
                "2. X_L = ωL_eq = 1000 × 0.04 = 40 Ω.",
                "3. Z = R + jX_L = 30 + j40 Ω (50∠53.13° Ω)."
            )
        ),
        CircuitProblem(
            id = "p_136j",
            code = "No. 136J",
            title = "AC Norton Equivalent Current",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "Convert the AC Thévenin source into its Norton equivalent short-circuit current phasor I_N:",
            parametersLine1 = "V_th = 50∠0° V, Z_th = (3 + j4) Ω",
            parametersLine2 = "Solve I_N = V_th / Z_th",
            leftMarker = "THÉVENIN PORT",
            rightMarker = "NORTON I_N",
            methodTitle = "SOURCE TRANSFORMATION",
            methodNote = "Divide open-circuit phasor V_th by complex Thévenin impedance Z_th.",
            inputHint = "Enter I_N in A (A + jB or polar)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(6.0, -8.0),
            unit = "A",
            hints = listOf(
                "Multiply numerator and denominator by Z_th* = 3 − j4.",
                "Denominator |Z_th|² = 3² + 4² = 25."
            ),
            workedSteps = listOf(
                "1. I_N = 50 / (3 + j4) = 50(3 − j4) / (9 + 16).",
                "2. I_N = 2(3 − j4) = 6 − j8 A (10∠−53.13° A)."
            )
        ),
        CircuitProblem(
            id = "p_140k",
            code = "No. 140K",
            title = "Two-Source AC Nodal Analysis",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "Find node phasor voltage V_A driven by V₁ through R = 4Ω plus current source I_s entering node A:",
            parametersLine1 = "V₁ = 20∠0° V via R = 4Ω to A",
            parametersLine2 = "I_s = j5 A entering A; Z_L = j4Ω (A to GND)",
            leftMarker = "KCL AT NODE A",
            rightMarker = "TWO SOURCES",
            methodTitle = "PHASOR NODAL KCL",
            methodNote = "Write KCL leaving node A: (V_A − V₁)/R + V_A/Z_L = I_s.",
            inputHint = "Enter V_A in V (A + jB or jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(0.0, 20.0),
            unit = "V",
            hints = listOf(
                "(V_A − 20)/4 + V_A/(j4) = j5.",
                "Multiply by 4: V_A(1 − j) = 20 + j20."
            ),
            workedSteps = listOf(
                "1. KCL at node A: (V_A − 20)/4 + V_A/(j4) = j5.",
                "2. Note 1/j = −j, so V_A(1 − j) = 20(1 + j).",
                "3. V_A = 20(1 + j)/(1 − j) = 20(j) = 0 + j20 V (20∠90° V)."
            )
        ),
        CircuitProblem(
            id = "p_224g",
            code = "No. 224G",
            title = "Lossy Parallel Tank Resonance",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.PARALLEL_RLC,
            prompt = "A practical coil (R_s = 5Ω, X_L = 15Ω) is tuned to unity pf by a lossless parallel C. Find tank Z_tank:",
            parametersLine1 = "Coil Branch: R_s = 5Ω, X_L = 15Ω",
            parametersLine2 = "Shunted by C for Parallel Resonance (B_net = 0)",
            leftMarker = "Q_coil = 3",
            rightMarker = "R_p = R_s(1+Q²)",
            methodTitle = "LOSSY TANK EQUIVALENT",
            methodNote = "Convert the series lossy coil into its equivalent parallel resistance R_p = R_s(1 + Q_coil²).",
            inputHint = "Enter Z_tank in Ω",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 50.0,
            unit = "Ω",
            hints = listOf(
                "Compute coil quality factor Q_coil = X_L / R_s = 15 / 5 = 3.",
                "At unity power factor, C cancels coil susceptance leaving Z_tank = R_p = R_s(1 + Q_coil²)."
            ),
            workedSteps = listOf(
                "1. Q_coil = X_L / R_s = 15 / 5 = 3.",
                "2. Parallel coil admittance Y_coil = 1/(5 + j15) = (5 − j15)/250 = 0.02 − j0.06 S.",
                "3. Parallel C cancels −j0.06 S, so Z_tank = 1/0.02 = 5(1 + 3²) = 50 Ω."
            )
        ),
        CircuitProblem(
            id = "p_330h",
            code = "No. 330H",
            title = "Balanced 3-Phase Line Power",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "Find total 3-phase active power P_3φ (in kW) absorbed by a balanced load:",
            parametersLine1 = "V_L = 400 V (RMS),   I_L = 20 A (RMS)",
            parametersLine2 = "Power Factor pf = cos(30°) = √3 / 2",
            leftMarker = "BALANCED 3φ",
            rightMarker = "P_3φ = √3 V_L I_L pf",
            methodTitle = "THREE-PHASE POWER",
            methodNote = "Multiply line quantities by √3·pf and divide by 1000 for kilowatts.",
            inputHint = "Enter P_3φ in kW",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 12.0,
            unit = "kW",
            hints = listOf(
                "P_3φ = √3 × V_L × I_L × pf.",
                "Notice √3 × (√3 / 2) = 3/2 = 1.5."
            ),
            workedSteps = listOf(
                "1. P_3φ = √3 × 400 × 20 × (√3 / 2).",
                "2. = 1.5 × 8000 = 12000 W = 12 kW."
            )
        ),
        CircuitProblem(
            id = "p_334i",
            code = "No. 334I",
            title = "Two-Wattmeter Phase Angle",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "A balanced 3-phase inductive load is measured with two wattmeters. Find load phase angle φ (in deg):",
            parametersLine1 = "W₁ = 4000 W,   W₂ = 2000 W",
            parametersLine2 = "tan(φ) = √3 (W₁ − W₂) / (W₁ + W₂)",
            leftMarker = "TWO-WATTMETER",
            rightMarker = "ANGLE φ (deg)",
            methodTitle = "TWO-WATTMETER ANGLE",
            methodNote = "Compute tan(φ) = √3(W₁ − W₂)/(W₁ + W₂) and take arctangent in degrees.",
            inputHint = "Enter φ in degrees",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 30.0,
            unit = "deg",
            hints = listOf(
                "W₁ − W₂ = 2000 W and W₁ + W₂ = 6000 W.",
                "tan(φ) = √3 × (1/3) = 1/√3."
            ),
            workedSteps = listOf(
                "1. tan(φ) = √3 × (4000 − 2000) / (4000 + 2000) = √3 / 3 = 1/√3.",
                "2. φ = arctan(1/√3) = 30°."
            )
        ),
        CircuitProblem(
            id = "p_499e",
            code = "No. 499E",
            title = "RC Step Transient Response",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RC,
            prompt = "Uncharged C (v_C(0⁻) = 0 V) is energized at t = 0 by V_s = 100V through R. Find v_C(t) at t = τ:",
            parametersLine1 = "V_s = 100 V step, R = 10 kΩ, C = 50 μF",
            parametersLine2 = "Evaluate capacitor voltage at t = τ = RC = 0.5 s",
            leftMarker = "v_C(0⁻) = 0",
            rightMarker = "t = τ = RC",
            methodTitle = "FIRST-ORDER TRANSIENT",
            methodNote = "Apply v_C(t) = V_s(1 − e^(−t/τ)) at t = τ.",
            inputHint = "Enter v_C(τ) in Volts",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 100.0 * (1.0 - kotlin.math.exp(-1.0)),
            tolerance = 0.005,
            unit = "V",
            hints = listOf(
                "At t = τ, the exponential factor e^(−t/τ) becomes e⁻¹ ≈ 0.3679.",
                "v_C(τ) = 100 × (1 − 0.3679) V."
            ),
            workedSteps = listOf(
                "1. Time constant τ = R·C = 10×10³ × 50×10⁻⁶ = 0.5 s.",
                "2. v_C(τ) = V_s(1 − e⁻¹) = 100 × 0.63212 ≈ 63.212 V."
            )
        ),
        CircuitProblem(
            id = "p_499f",
            code = "No. 499F",
            title = "Pre-Charged RLC Initial Slope",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "A series RLC with i(0⁻) = 0 and v_C(0⁻) = 4V is switched onto V_s = 24V at t = 0. Find di/dt(0⁺):",
            parametersLine1 = "V_s = 24 V step, R = 10Ω, L = 0.2 H",
            parametersLine2 = "Initial state: i(0⁻) = 0 A, v_C(0⁻) = 4 V",
            leftMarker = "i(0⁺) = 0",
            rightMarker = "di/dt(0⁺)",
            methodTitle = "INITIAL CONDITIONS KVL",
            methodNote = "Use state continuity (i(0⁺) = i(0⁻) and v_C(0⁺) = v_C(0⁻)) in KVL at t = 0⁺.",
            inputHint = "Enter di/dt(0⁺) in A/s",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 100.0,
            unit = "A/s",
            hints = listOf(
                "At t = 0⁺, i(0⁺) = 0 so resistor drop i(0⁺)R = 0 V.",
                "KVL gives L·di/dt(0⁺) = V_s − v_C(0⁺) = 24 − 4 = 20 V."
            ),
            workedSteps = listOf(
                "1. Continuity of inductor current and capacitor voltage gives i(0⁺) = 0 A, v_C(0⁺) = 4 V.",
                "2. KVL at t = 0⁺: 0·R + 0.2·di/dt(0⁺) + 4 = 24 ⇒ di/dt(0⁺) = 20 / 0.2 = 100 A/s."
            )
        ),
        CircuitProblem(
            id = "p_533j",
            code = "No. 533J",
            title = "Maxwell Inductance-Capacitance Bridge",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "A Maxwell AC bridge balances unknown series coil L_x against parallel R₁∥C₁. Find L_x (in mH):",
            parametersLine1 = "Arm 1: R₁ = 500Ω ∥ C₁ = 0.4 μF",
            parametersLine2 = "Bridge Arms: R₂ = 200Ω, R₃ = 1000Ω",
            leftMarker = "Z_x Y₁ = R₂/R₃",
            rightMarker = "L_x = R₂ R₃ C₁",
            methodTitle = "MAXWELL BRIDGE BALANCE",
            methodNote = "At Maxwell bridge balance, Z_x = R₂·R₃·Y₁ = R₂·R₃·(1/R₁ + jωC₁), yielding L_x = R₂·R₃·C₁.",
            inputHint = "Enter L_x in mH",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 80.0,
            unit = "mH",
            hints = listOf(
                "At bridge balance, Z_x / R₂ = R₃ · Y₁ ⇒ R_x + jωL_x = R₂R₃/R₁ + jωR₂R₃C₁.",
                "Match imaginary parts: L_x = R₂ · R₃ · C₁."
            ),
            workedSteps = listOf(
                "1. Bridge balance requires Z_x = R₂ R₃ (1/R₁ + jωC₁).",
                "2. Equating reactive terms: L_x = R₂ · R₃ · C₁ = 200 × 1000 × 0.4×10⁻⁶ = 0.08 H = 80 mH."
            )
        ),
        CircuitProblem(
            id = "p_600a",
            code = "No. 600A",
            title = "De Moivre Complex Power",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Evaluate z⁴ in rectangular form (A + jB) using De Moivre's theorem:",
            parametersLine1 = "z = 1 + j",
            parametersLine2 = "Compute z⁴ = r⁴∠4θ",
            leftMarker = "r = √2",
            rightMarker = "θ = 45°",
            methodTitle = "DE MOIVRE ROTATION",
            methodNote = "Convert 1 + j to polar form r∠θ, raise magnitude to the 4th power, and multiply angle by 4.",
            inputHint = "Enter complex value (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(-4.0, 0.0),
            unit = "",
            hints = listOf(
                "|1 + j| = √2 and arg(1 + j) = 45°.",
                "(√2)⁴ = 4 and 4 × 45° = 180°."
            ),
            workedSteps = listOf(
                "1. z = √2∠45°.",
                "2. z⁴ = (√2)⁴∠180° = 4∠180° = −4 + j0."
            )
        ),
        CircuitProblem(
            id = "p_600b",
            code = "No. 600B",
            title = "Euler Polar-to-Rectangular Map",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Convert the Euler exponential complex number into rectangular form A + jB:",
            parametersLine1 = "z = 4 · e^(jπ/3)",
            parametersLine2 = "Apply e^(jθ) = cos(θ) + j·sin(θ)",
            leftMarker = "MODULUS 4",
            rightMarker = "PHASE π/3",
            methodTitle = "EULER IDENTITY",
            methodNote = "Evaluate 4·cos(60°) + j·4·sin(60°).",
            inputHint = "Enter complex value (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(2.0, 2.0 * sqrt(3.0)),
            unit = "",
            hints = listOf(
                "π/3 radians equals 60°.",
                "cos(60°) = 0.5 and sin(60°) = √3 / 2 ≈ 0.866."
            ),
            workedSteps = listOf(
                "1. Re(z) = 4 cos(π/3) = 4 × 0.5 = 2.",
                "2. Im(z) = 4 sin(π/3) = 4 × (√3 / 2) = 2√3 ≈ 3.464 ⇒ z = 2 + j3.464."
            )
        ),
        CircuitProblem(
            id = "p_640k",
            code = "No. 640K",
            title = "Bilinear s-to-z Filter Transform",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Evaluate the Cayley/bilinear transform z = (1 + s)/(1 − s) along the jω-axis at s = j2:",
            parametersLine1 = "Bilinear Map: z = (1 + s) / (1 − s)",
            parametersLine2 = "Analog Frequency Point: s = j2",
            leftMarker = "jω-AXIS",
            rightMarker = "UNIT CIRCLE |z|=1",
            methodTitle = "MÖBIUS DIGITAL MAP",
            methodNote = "Substitute s = j2 and rationalize the denominator by multiplying by (1 + j2).",
            inputHint = "Enter z (A + jB or polar)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(-0.6, 0.8),
            unit = "",
            hints = listOf(
                "z = (1 + j2)/(1 − j2) = (1 + j2)² / (1² + 2²).",
                "(1 + j2)² = (1 − 4) + j4 = −3 + j4."
            ),
            workedSteps = listOf(
                "1. z = (1 + j2)² / (1 + 4) = (−3 + j4) / 5.",
                "2. z = −0.6 + j0.8 (note |z| = √(0.36 + 0.64) = 1 on the unit circle)."
            )
        ),
        CircuitProblem(
            id = "p_644l",
            code = "No. 644L",
            title = "Cauchy Higher-Order Pole",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Evaluate the normalized integral (1/j2π) ∮ (e^(3z) / z³) dz positively around |z| = 2:",
            parametersLine1 = "f(z) = e^(3z) / z³",
            parametersLine2 = "Closed CCW Contour C: |z| = 2",
            leftMarker = "ORDER-3 POLE",
            rightMarker = "f''(0) / 2!",
            methodTitle = "CAUCHY DERIVATIVE FORMULA",
            methodNote = "Apply (1/j2π) ∮ g(z)/z^(n+1) dz = g^(n)(0) / n! with g(z) = e^(3z) and n = 2.",
            inputHint = "Enter scalar value",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 4.5,
            unit = "",
            hints = listOf(
                "Identify g(z) = e^(3z) and n + 1 = 3 ⇒ n = 2.",
                "Compute second derivative g''(z) = 9 e^(3z) at z = 0, then divide by 2!."
            ),
            workedSteps = listOf(
                "1. Pole z = 0 of order 3 is inside |z| = 2, so integral = g''(0) / 2!.",
                "2. g''(0) = 9·e⁰ = 9 ⇒ 9 / 2 = 4.5."
            )
        ),
        CircuitProblem(
            id = "p_732i",
            code = "No. 732I",
            title = "Cl₂,₀ Ohm's Law (I = V Z⁻¹)",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Solve I = V·Z⁻¹ in Cl₂,₀ using geometric multivector inversion (enter A + Be12):",
            parametersLine1 = "V = 50 (scalar phase 0°),   Z = 6 + 8e₁₂ Ω",
            parametersLine2 = "Z⁻¹ = Z̃ / (Z Z̃)",
            leftMarker = "IMPEDANCE Z",
            rightMarker = "CURRENT I",
            methodTitle = "CL₂,₀ MULTIVECTOR INVERSE",
            methodNote = "Compute reverse Z̃ = 6 − 8e₁₂, norm Z Z̃ = 36 + 64 = 100, and multiply V·Z⁻¹.",
            inputHint = "Enter I in A (e.g. 3 - 4e12)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(3.0, -4.0),
            unit = "A",
            hints = listOf(
                "Because e₁₂² = −1, Z Z̃ = (6 + 8e₁₂)(6 − 8e₁₂) = 36 − 64e₁₂² = 100.",
                "I = 50 · (6 − 8e₁₂) / 100."
            ),
            workedSteps = listOf(
                "1. Z̃ = 6 − 8e₁₂ and Z Z̃ = 6² + 8² = 100.",
                "2. Z⁻¹ = 0.06 − 0.08e₁₂ S.",
                "3. I = 50(0.06 − 0.08e₁₂) = 3 − 4e₁₂ A."
            )
        ),
        CircuitProblem(
            id = "p_736j",
            code = "No. 736J",
            title = "Cl₃,₀ Distortion Bivector Norm",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "In harmonic Cl₃,₀ space, find the non-active bivector norm ||v ∧ i|| (in VA) for:",
            parametersLine1 = "v = 4e₁ V",
            parametersLine2 = "i = 3e₁ + 4e₂ + 3e₃ A",
            leftMarker = "BIVECTOR v∧i",
            rightMarker = "NORM ||Q_G||",
            methodTitle = "GEOMETRIC DISTORTION NORM",
            methodNote = "Compute wedge product v ∧ i = 16e₁₂ + 12e₁₃ and take Euclidean magnitude √(Q₁₂² + Q₁₃²).",
            inputHint = "Enter ||v ∧ i|| in VA",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 20.0,
            unit = "VA",
            hints = listOf(
                "e₁ ∧ e₁ = 0, so v ∧ i = 4e₁ ∧ (4e₂ + 3e₃) = 16e₁₂ + 12e₁₃.",
                "Compute √(16² + 12²)."
            ),
            workedSteps = listOf(
                "1. v ∧ i = (4)(4)e₁₂ + (4)(3)e₁₃ = 16e₁₂ + 12e₁₃.",
                "2. ||v ∧ i|| = √(16² + 12²) = √400 = 20 VA (combines reactive + harmonic distortion)."
            )
        ),
        CircuitProblem(
            id = "p_600c",
            code = "No. 600C",
            title = "Complex Plane 90° Operator j",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Multiplying by j = e^(jπ/2) rotates a phasor +90° CCW. Evaluate w = j · z in rectangular form:",
            parametersLine1 = "z = 3 + j4",
            parametersLine2 = "Apply w = j · (3 + j4) using j² = −1",
            leftMarker = "PHASOR z",
            rightMarker = "+90° ROTATION j·z",
            methodTitle = "ORTHOGONAL OPERATOR j",
            methodNote = "Distribute j across (3 + j4) and replace j² with −1.",
            inputHint = "Enter w (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(-4.0, 3.0),
            unit = "",
            hints = listOf(
                "j · (3 + j4) = 3j + 4j².",
                "Substitute j² = −1 to put the real term first."
            ),
            workedSteps = listOf(
                "1. w = j(3 + j4) = 3j + 4j² = 3j + 4(−1).",
                "2. w = −4 + j3 (magnitude stays 5; angle rotates 53.13° → 143.13°)."
            )
        ),
        CircuitProblem(
            id = "p_600d",
            code = "No. 600D",
            title = "Conjugate Quotient in ℂ",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Simplify the complex quotient w = (1 + j) / (1 − j) into rectangular form A + jB:",
            parametersLine1 = "w = (1 + j) / (1 − j)",
            parametersLine2 = "Multiply numerator & denominator by conjugate (1 + j)",
            leftMarker = "CONJUGATE (1+j)",
            rightMarker = "REAL DENOM 2",
            methodTitle = "COMPLEX RATIONALIZATION",
            methodNote = "Multiply top and bottom by (1 + j) so (1 − j)(1 + j) = 1² + 1² = 2.",
            inputHint = "Enter w (e.g. j1 or 0 + j1)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(0.0, 1.0),
            unit = "",
            hints = listOf(
                "Numerator (1 + j)² = 1 + 2j + j² = 2j.",
                "Denominator (1 − j)(1 + j) = 2."
            ),
            workedSteps = listOf(
                "1. w = (1 + j)² / ((1 − j)(1 + j)) = (1 + 2j − 1) / (1 + 1).",
                "2. w = 2j / 2 = 0 + j1 (1∠90°)."
            )
        ),
        CircuitProblem(
            id = "p_602a",
            code = "No. 602A",
            title = "Complex Conjugate Roots in s-Plane",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Find the upper-half s-plane pole s₁ (with Im(s₁) > 0) of the damped RLC polynomial:",
            parametersLine1 = "P(s) = s² + 4s + 13 = 0",
            parametersLine2 = "Solve s = (−b ± √(b² − 4ac)) / 2a",
            leftMarker = "DAMPING σ = −2",
            rightMarker = "FREQ ω_d = 3",
            methodTitle = "QUADRATIC S-PLANE POLES",
            methodNote = "Apply the quadratic formula or complete the square: (s + 2)² + 9 = 0.",
            inputHint = "Enter s₁ (e.g. -2 + j3)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(-2.0, 3.0),
            unit = "rad/s",
            hints = listOf(
                "Discriminant b² − 4ac = 16 − 52 = −36 ⇒ √(−36) = j6.",
                "s = (−4 ± j6) / 2 = −2 ± j3; pick Im(s₁) > 0."
            ),
            workedSteps = listOf(
                "1. Complete the square: (s + 2)² + 3² = 0.",
                "2. Upper-half pole: s₁ = −2 + j3 rad/s (conjugate pair with −2 − j3)."
            )
        ),
        CircuitProblem(
            id = "p_603a",
            code = "No. 603A",
            title = "Analytic Transfer H(s) on jω-Axis",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.LOW_PASS_RC,
            prompt = "Evaluate the analytic transfer function H(s) at the imaginary-axis point s = j20:",
            parametersLine1 = "H(s) = 40 / (s + 20)",
            parametersLine2 = "Substitute s = j20 and express in A + jB",
            leftMarker = "POLE s = −20",
            rightMarker = "EVAL s = j20",
            methodTitle = "CONTOUR EVALUATION s = jω",
            methodNote = "Evaluating H(s) along the jω-axis connects s-plane complex functions directly to AC phasors.",
            inputHint = "Enter H(j20) (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(1.0, -1.0),
            unit = "",
            hints = listOf(
                "H(j20) = 40 / (20 + j20) = 2 / (1 + j).",
                "Multiply numerator and denominator by (1 − j)."
            ),
            workedSteps = listOf(
                "1. H(j20) = 40 / (20 + j20) = 2 / (1 + j).",
                "2. Rationalize: 2(1 − j) / (1 + 1) = 1 − j1 (1.414∠−45°)."
            )
        ),
        CircuitProblem(
            id = "p_606b",
            code = "No. 606B",
            title = "Simple Real-Pole Cover-Up Residue",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Compute the residue Res[F(s), s = −3] using the cover-up limit lim(s→−3) (s + 3)·F(s):",
            parametersLine1 = "F(s) = 15 / (s · (s + 3))",
            parametersLine2 = "Simple pole at s₀ = −3",
            leftMarker = "COVER-UP (s+3)",
            rightMarker = "RESIDUE a₋₁",
            methodTitle = "SIMPLE POLE COVER-UP",
            methodNote = "Cancel the (s + 3) factor and plug s = −3 into the remaining fraction.",
            inputHint = "Enter scalar residue",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = -5.0,
            unit = "",
            hints = listOf(
                "Multiply by (s + 3): (s + 3)F(s) = 15 / s.",
                "Evaluate 15 / (−3)."
            ),
            workedSteps = listOf(
                "1. Cancel (s + 3) from denominator: remaining expression is 15 / s.",
                "2. Res[F(s), −3] = 15 / (−3) = −5."
            )
        ),
        CircuitProblem(
            id = "p_700a",
            code = "No. 700A",
            title = "Cl₂,₀ Inner Product & Active Power",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "In Cl₂,₀ (e₁·e₁ = e₂·e₂ = 1, e₁·e₂ = 0), compute the scalar inner product P = v · i:",
            parametersLine1 = "v = 6e₁ + 2e₂ V",
            parametersLine2 = "i = 4e₁ + 3e₂ A",
            leftMarker = "GRADE-0 SCALAR",
            rightMarker = "ACTIVE POWER P",
            methodTitle = "CLIFFORD INNER PRODUCT",
            methodNote = "Like-basis terms multiply to +1 and cross terms vanish: v·i = v₁i₁ + v₂i₂.",
            inputHint = "Enter P in Watts",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 30.0,
            unit = "W",
            hints = listOf(
                "Multiply matching basis components: (6 × 4) + (2 × 3).",
                "Cross terms e₁·e₂ and e₂·e₁ equal 0."
            ),
            workedSteps = listOf(
                "1. v · i = 6(4)(e₁·e₁) + 2(3)(e₂·e₂).",
                "2. P = 24 + 6 = 30 W."
            )
        ),
        CircuitProblem(
            id = "p_700b",
            code = "No. 700B",
            title = "Cl₂,₀ Outer Wedge Area a ∧ b",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Compute the oriented bivector coefficient β in the outer product a ∧ b = β·e₁₂ in Cl₂,₀:",
            parametersLine1 = "a = 5e₁ + 2e₂",
            parametersLine2 = "b = 1e₁ + 4e₂   (e₁∧e₁ = 0, e₂∧e₁ = −e₁₂)",
            leftMarker = "OUTER PRODUCT a∧b",
            rightMarker = "2D BIVECTOR β·e₁₂",
            methodTitle = "WEDGE DETERMINANT",
            methodNote = "Evaluate β = a₁b₂ − a₂b₁ using antisymmetry e₂ ∧ e₁ = −e₁ ∧ e₂.",
            inputHint = "Enter coefficient β",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 18.0,
            unit = "",
            hints = listOf(
                "Parallel components wedge to zero: e₁∧e₁ = 0 and e₂∧e₂ = 0.",
                "Cross terms give (5 × 4)e₁₂ + (2 × 1)e₂₁ = (20 − 2)e₁₂."
            ),
            workedSteps = listOf(
                "1. a ∧ b = (5·4 − 2·1) e₁₂.",
                "2. = 18 e₁₂, so coefficient β = 18."
            )
        ),
        CircuitProblem(
            id = "p_702a",
            code = "No. 702A",
            title = "Right-Multiply by e₁₂ (+90° Turn)",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Right-multiplying by e₁₂ rotates any Cl₂,₀ vector +90° (e₁e₁₂ = e₂, e₂e₁₂ = −e₁). Find e₁-part of v·e₁₂:",
            parametersLine1 = "v = 4e₁ − 3e₂",
            parametersLine2 = "v' = v · e₁₂ = 4(e₁e₁₂) − 3(e₂e₁₂)",
            leftMarker = "e₁e₁₂ = +e₂",
            rightMarker = "e₂e₁₂ = −e₁",
            methodTitle = "UNIT BIVECTOR ROTATION",
            methodNote = "Substitute e₁e₁₂ = e₂ and e₂e₁₂ = −e₁ directly, just like multiplying a phasor by j.",
            inputHint = "Enter e₁ coefficient",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 3.0,
            unit = "",
            hints = listOf(
                "v · e₁₂ = 4(e₂) − 3(−e₁).",
                "Collect the e₁ term: (−3)(−e₁) = +3e₁."
            ),
            workedSteps = listOf(
                "1. e₁e₁₂ = e₁(e₁e₂) = e₂; e₂e₁₂ = e₂(e₁e₂) = −e₁.",
                "2. v·e₁₂ = 4e₂ − 3(−e₁) = 3e₁ + 4e₂ ⇒ e₁ coefficient is 3."
            )
        ),
        CircuitProblem(
            id = "p_703a",
            code = "No. 703A",
            title = "Cl₂,₀ Impedance Multivector Z",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Express a series RL impedance as an even Cl₂,₀ multivector Z = R + X_L·e₁₂ (enter A + Be12):",
            parametersLine1 = "Series RL: R = 8Ω, L = 0.06 H",
            parametersLine2 = "ω = 100 rad/s   (bivector e₁₂ plays role of +j)",
            leftMarker = "SCALAR R",
            rightMarker = "BIVECTOR X_L e₁₂",
            methodTitle = "EVEN SUBALGEBRA IMPEDANCE",
            methodNote = "Compute X_L = ωL and write Z = R + X_L e₁₂ inside Cl₂,₀⁺.",
            inputHint = "Enter Z in Ω (e.g. 8 + 6e12)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(8.0, 6.0),
            unit = "Ω",
            hints = listOf(
                "X_L = ωL = 100 × 0.06 = 6 Ω.",
                "Combine resistance R = 8 and inductive bivector +6e₁₂."
            ),
            workedSteps = listOf(
                "1. Compute inductive reactance: X_L = 100 × 0.06 = 6 Ω.",
                "2. In Cl₂,₀, Z = 8 + 6e₁₂ Ω."
            )
        ),
        CircuitProblem(
            id = "p_706b",
            code = "No. 706B",
            title = "Cl₂,₀ Reversion Norm ||M||",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Compute the multivector magnitude ||M|| = √(M M̃) using reversion M̃ = α − βe₁₂ in Cl₂,₀:",
            parametersLine1 = "M = 5 + 12e₁₂",
            parametersLine2 = "Reversion M̃ = 5 − 12e₁₂   ((e₁₂)² = −1)",
            leftMarker = "REVERSE M̃",
            rightMarker = "NORM ||M||",
            methodTitle = "MULTIVECTOR MAGNITUDE",
            methodNote = "Evaluate M M̃ = α² − β²(e₁₂)² = α² + β² and take the square root before dividing for M⁻¹.",
            inputHint = "Enter scalar magnitude ||M||",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 13.0,
            unit = "",
            hints = listOf(
                "M M̃ = (5 + 12e₁₂)(5 − 12e₁₂) = 25 − 144(−1) = 169.",
                "Take √169."
            ),
            workedSteps = listOf(
                "1. M M̃ = 5² + 12² = 25 + 144 = 169.",
                "2. ||M|| = √169 = 13."
            )
        ),
        CircuitProblem(
            id = "p_600e",
            code = "No. 600E",
            title = "Polar Phasor Product z₁ · z₂",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Multiply two complex phasors in polar form (multiply moduli, add angles) and give rectangular A + jB:",
            parametersLine1 = "z₁ = 3∠25°,   z₂ = 4∠65°",
            parametersLine2 = "z₁ · z₂ = (r₁ · r₂)∠(θ₁ + θ₂)",
            leftMarker = "r = 3 × 4 = 12",
            rightMarker = "θ = 25° + 65°",
            methodTitle = "POLAR MULTIPLICATION",
            methodNote = "In ℂ, products scale magnitudes and sum phase angles: r₁r₂ e^(j(θ₁+θ₂)).",
            inputHint = "Enter product (e.g. j12 or 12∠90°)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(0.0, 12.0),
            unit = "",
            hints = listOf(
                "Modulus is 3 × 4 = 12.",
                "Phase is 25° + 65° = 90°, which lies along the +j axis."
            ),
            workedSteps = listOf(
                "1. r = 3 × 4 = 12; θ = 25° + 65° = 90°.",
                "2. z₁·z₂ = 12∠90° = 0 + j12."
            )
        ),
        CircuitProblem(
            id = "p_605b",
            code = "No. 605B",
            title = "Cauchy–Goursat Analytic Contour",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Evaluate the closed CCW contour integral I = ∮ f(z) dz around the unit circle |z| = 1:",
            parametersLine1 = "f(z) = (z + 2) / (z − 5)",
            parametersLine2 = "Contour C: |z| = 1 (counterclockwise)",
            leftMarker = "POLE z = 5 OUTSIDE",
            rightMarker = "ANALYTIC ON |z|≤1",
            methodTitle = "CAUCHY–GOURSAT THEOREM",
            methodNote = "Check whether any singularities lie inside or on the contour |z| = 1 before integrating.",
            inputHint = "Enter contour integral I",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 0.0,
            unit = "",
            hints = listOf(
                "The only pole is at z = 5, where |5| = 5 > 1.",
                "Since f(z) is analytic everywhere inside and on |z| = 1, Cauchy–Goursat gives 0."
            ),
            workedSteps = listOf(
                "1. Singularity z = 5 sits outside the contour |z| = 1.",
                "2. By Cauchy–Goursat's theorem, ∮_C f(z) dz = 0."
            )
        ),
        CircuitProblem(
            id = "p_607b",
            code = "No. 607B",
            title = "Cauchy Integral Formula (Simple Pole)",
            category = ProblemCategory.COMPLEX_ANALYSIS,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.CONTOUR_SPLANE,
            prompt = "Evaluate the normalized integral I = (1/j2π) ∮ (e^(2z) / (z − 1)) dz CCW around |z| = 2:",
            parametersLine1 = "f(z) = e^(2z) / (z − 1),   g(z) = e^(2z)",
            parametersLine2 = "Closed CCW Contour C: |z| = 2",
            leftMarker = "ENCLOSED z₀ = 1",
            rightMarker = "I = g(z₀)",
            methodTitle = "CAUCHY INTEGRAL FORMULA",
            methodNote = "For analytic g(z) inside C and z₀ = 1 inside |z| = 2, (1/j2π) ∮ g(z)/(z − z₀) dz = g(z₀).",
            inputHint = "Enter numeric value (e.g. 7.389)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = kotlin.math.exp(2.0),
            tolerance = 0.005,
            unit = "",
            hints = listOf(
                "Since |1| < 2, the simple pole z₀ = 1 is enclosed.",
                "Evaluate g(1) = e^(2·1) = e² ≈ 7.389."
            ),
            workedSteps = listOf(
                "1. g(z) = e^(2z) is entire and z₀ = 1 lies inside |z| = 2.",
                "2. By Cauchy's Integral Formula, I = g(1) = e² ≈ 7.389."
            )
        ),
        CircuitProblem(
            id = "p_705b",
            code = "No. 705B",
            title = "GA Vector Parallel Projection",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Given unit voltage direction v̂ and current i in Cl₂,₀, find active projected magnitude ||i_∥|| = |i · v̂|:",
            parametersLine1 = "v̂ = 0.6e₁ + 0.8e₂   (||v̂|| = 1)",
            parametersLine2 = "i = 10e₁ A",
            leftMarker = "PROJECTION (i·v̂)v̂",
            rightMarker = "ACTIVE ||i_∥||",
            methodTitle = "INNER PROJECTION",
            methodNote = "Project current onto unit voltage direction: i_∥ = (i · v̂) v̂, so ||i_∥|| = |i · v̂|.",
            inputHint = "Enter ||i_∥|| in A",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 6.0,
            unit = "A",
            hints = listOf(
                "Compute inner product i · v̂ = 10(0.6) + 0(0.8).",
                "Notice active 6 A and Fryze rejection 8 A satisfy 6² + 8² = 10²."
            ),
            workedSteps = listOf(
                "1. i · v̂ = (10)(0.6) + (0)(0.8) = 6 A.",
                "2. i_∥ = 6(0.6e₁ + 0.8e₂) = 3.6e₁ + 4.8e₂ A ⇒ ||i_∥|| = 6 A."
            )
        ),
        CircuitProblem(
            id = "p_707b",
            code = "No. 707B",
            title = "Half-Angle Rotor Construction",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "Build the Cl₂,₀ rotor R = cos(θ/2) − sin(θ/2)e₁₂ for a θ = +60° CCW rotation (enter α + βe12):",
            parametersLine1 = "Rotation Angle θ = +60° CCW",
            parametersLine2 = "R = cos(θ/2) − sin(θ/2)e₁₂   (unit norm R R̃ = 1)",
            leftMarker = "HALF-ANGLE 30°",
            rightMarker = "ROTOR BUILDER",
            methodTitle = "EXPONENTIAL ROTOR",
            methodNote = "Halve the angle to θ/2 = 30° and evaluate R = cos(30°) − sin(30°)e₁₂.",
            inputHint = "Enter R (e.g. 0.866 - 0.5e12)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(sqrt(3.0) / 2.0, -0.5),
            unit = "",
            hints = listOf(
                "Half-angle is θ/2 = 60° / 2 = 30°.",
                "cos(30°) = √3 / 2 ≈ 0.866 and sin(30°) = 0.5."
            ),
            workedSteps = listOf(
                "1. θ/2 = 30° ⇒ cos(30°) = √3/2 ≈ 0.866, sin(30°) = 0.5.",
                "2. R = 0.866 − 0.5e₁₂ (R R̃ = 0.75 + 0.25 = 1)."
            )
        ),
        CircuitProblem(
            id = "p_710d",
            code = "No. 710D",
            title = "Cl₃,₀ Harmonic Active Power",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "In 3-harmonic Euclidean space Cl₃,₀ (e_j·e_k = δ_jk), compute total active power P = v · i:",
            parametersLine1 = "v = 10e₁ + 5e₂ + 4e₃ V",
            parametersLine2 = "i = 4e₁ + 2e₂ + 5e₃ A",
            leftMarker = "3D HARMONICS",
            rightMarker = "SCALAR DOT P = v·i",
            methodTitle = "CL₃,₀ INNER PRODUCT",
            methodNote = "Sum matching harmonic products across all three orthogonal axes: v₁i₁ + v₂i₂ + v₃i₃.",
            inputHint = "Enter P in Watts",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 70.0,
            unit = "W",
            hints = listOf(
                "Compute term-by-term: (10 × 4) + (5 × 2) + (4 × 5).",
                "Cross-harmonic terms e_j·e_k (j ≠ k) vanish."
            ),
            workedSteps = listOf(
                "1. v · i = 10(4) + 5(2) + 4(5) = 40 + 10 + 20.",
                "2. P = 70 W."
            )
        ),
        CircuitProblem(
            id = "p_711d",
            code = "No. 711D",
            title = "Cl₃,₀ Two-Axis Harmonic Wedge",
            category = ProblemCategory.GEOMETRIC_ALGEBRA,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.GEOMETRIC_PLANE,
            prompt = "In Cl₃,₀ harmonic space, voltage v lies along e₁ while distorted i includes e₃. Find ||v ∧ i|| (in VA):",
            parametersLine1 = "v = 5e₁ V",
            parametersLine2 = "i = 2e₁ + 3e₃ A",
            leftMarker = "HARMONIC PLANE e₁₃",
            rightMarker = "DISTORTION ||v∧i||",
            methodTitle = "CL₃,₀ WEDGE INTRO",
            methodNote = "Because e₁ ∧ e₁ = 0, only the orthogonal e₁ ∧ e₃ bivector component survives.",
            inputHint = "Enter ||v ∧ i|| in VA",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 15.0,
            unit = "VA",
            hints = listOf(
                "v ∧ i = (5e₁) ∧ (2e₁ + 3e₃) = 15 e₁₃.",
                "Magnitude of a pure single-plane bivector 15e₁₃ is 15 VA."
            ),
            workedSteps = listOf(
                "1. v ∧ i = 10(e₁∧e₁) + 15(e₁∧e₃) = 15 e₁₃.",
                "2. ||v ∧ i|| = 15 VA."
            )
        ),
        CircuitProblem(
            id = "p_400a",
            code = "No. 400A",
            title = "First-Order RC Time Constant",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.SERIES_RC,
            prompt = "Calculate the exponential charging time constant τ = R · C (in seconds) for the RC network:",
            parametersLine1 = "R = 25 kΩ,   C = 40 μF",
            parametersLine2 = "τ = R · C  (exponential time e^(−t/τ))",
            leftMarker = "R = 25×10³ Ω",
            rightMarker = "C = 40×10⁻⁶ F",
            methodTitle = "RC TIME CONSTANT",
            methodNote = "Convert kΩ to Ω (×10³) and μF to F (×10⁻⁶) before multiplying R·C.",
            inputHint = "Enter τ in seconds",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 1.0,
            unit = "s",
            hints = listOf(
                "τ = (25 × 10³) × (40 × 10⁻⁶) seconds.",
                "25 × 40 × 10⁻³ = 1000 × 10⁻³ = 1 s."
            ),
            workedSteps = listOf(
                "1. τ = R·C = (25,000 Ω) × (40 × 10⁻⁶ F).",
                "2. τ = 1.0 s (cutoff ω_c = 1/τ = 1 rad/s)."
            )
        ),
        CircuitProblem(
            id = "p_401a",
            code = "No. 401A",
            title = "First-Order s-Plane Pole",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.LOW_PASS_RC,
            prompt = "Identify the s-plane pole location s₀ (in rad/s) of the first-order transfer function:",
            parametersLine1 = "H(s) = 10 / (s + 50)",
            parametersLine2 = "Set denominator D(s) = 0 to locate pole",
            leftMarker = "LEFT-HALF PLANE",
            rightMarker = "POLE s₀ = −ω_c",
            methodTitle = "TRANSFER FUNCTION POLE",
            methodNote = "Solve s + 50 = 0 for the root where H(s) → ∞.",
            inputHint = "Enter pole s₀ in rad/s (signed)",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = -50.0,
            unit = "rad/s",
            hints = listOf(
                "Poles are roots of the denominator polynomial s + 50 = 0.",
                "Remember the negative sign placing it in the stable left-half plane."
            ),
            workedSteps = listOf(
                "1. Set denominator s + 50 = 0.",
                "2. Pole sits at s₀ = −50 rad/s (LHP exponential decay e^(−50t))."
            )
        ),
        CircuitProblem(
            id = "p_228h",
            code = "No. 228H",
            title = "Source-Loaded Tank Q_L",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.PARALLEL_RLC,
            prompt = "A parallel tank (R_tank = 10 kΩ, unloaded Q₀ = 20) is driven by a source with R_s = 10 kΩ. Find loaded Q_L:",
            parametersLine1 = "R_tank = 10 kΩ (unloaded Q₀ = 20)",
            parametersLine2 = "Norton Source R_s = 10 kΩ across tank",
            leftMarker = "R_eq = R_s ∥ R_tank",
            rightMarker = "LOADED Q_L",
            methodTitle = "LOADED QUALITY FACTOR",
            methodNote = "Parallel source resistance lowers effective tank resistance R_eq = R_s∥R_tank, scaling Q_L = Q₀(R_eq/R_tank).",
            inputHint = "Enter loaded Q_L",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 10.0,
            unit = "",
            hints = listOf(
                "Compute parallel resistance R_eq = 10 kΩ ∥ 10 kΩ = 5 kΩ.",
                "Q_L = Q₀ × (R_eq / R_tank) = 20 × (5 / 10)."
            ),
            workedSteps = listOf(
                "1. Combined parallel resistance R_eq = (10k × 10k) / (10k + 10k) = 5 kΩ.",
                "2. Since parallel Q ∝ R_eq, loaded Q_L = 20 × (5k / 10k) = 10."
            )
        ),
        CircuitProblem(
            id = "p_100a",
            code = "No. 100A",
            title = "Sinusoidal Phase Alignment",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.SERIES_RL,
            prompt = "Align v(t) and i(t) to a common cosine reference and find impedance phase θ = θ_v − θ_i (in degrees):",
            parametersLine1 = "v(t) = 100 cos(500t + 20°) V",
            parametersLine2 = "i(t) = 4 sin(500t + 65°) A",
            leftMarker = "sin α = cos(α−90°)",
            rightMarker = "θ = θ_v − θ_i",
            methodTitle = "COSINE PHASOR REFERENCE",
            methodNote = "Convert sine to cosine via sin(ωt + φ) = cos(ωt + φ − 90°) before subtracting angles.",
            inputHint = "Enter angle θ in degrees",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 45.0,
            unit = "°",
            hints = listOf(
                "sin(500t + 65°) = cos(500t + 65° − 90°) = cos(500t − 25°), so θ_i = −25°.",
                "θ = θ_v − θ_i = 20° − (−25°) = +45°."
            ),
            workedSteps = listOf(
                "1. Convert i(t) to cosine: sin(500t + 65°) = cos(500t − 25°) ⇒ θ_i = −25°.",
                "2. Impedance phase θ = θ_v − θ_i = 20° − (−25°) = 45° (voltage leads current, inductive)."
            )
        ),
        CircuitProblem(
            id = "p_102b",
            code = "No. 102B",
            title = "Series-Aiding Coupled Inductors",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.SERIES_RL,
            prompt = "Find the total equivalent inductance L_eq (in mH) of two mutually coupled coils wired series-aiding:",
            parametersLine1 = "L₁ = 40 mH,   L₂ = 60 mH",
            parametersLine2 = "Mutual M = 15 mH (series-aiding dots)",
            leftMarker = "AIDING FLUX (+2M)",
            rightMarker = "L_eq = L₁+L₂+2M",
            methodTitle = "COUPLED INDUCTANCE",
            methodNote = "When current enters both dotted terminals in sequence, mutual inductance adds: L_eq = L₁ + L₂ + 2M.",
            inputHint = "Enter L_eq in mH",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 130.0,
            unit = "mH",
            hints = listOf(
                "For series-aiding coils, mutual flux reinforces self-flux by +2M.",
                "L_eq = 40 + 60 + 2(15) mH."
            ),
            workedSteps = listOf(
                "1. L_eq = L₁ + L₂ + 2M = 40 + 60 + 2(15).",
                "2. L_eq = 130 mH."
            )
        ),
        CircuitProblem(
            id = "p_106b",
            code = "No. 106B",
            title = "Complex Current Divider",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "Source current I_s splits between parallel branches Z₁ and Z₂. Find complex branch current I₁ (in A):",
            parametersLine1 = "I_s = 10∠0° A,   Z₁ = (3 + j4) Ω",
            parametersLine2 = "Z₂ = (5 − j4) Ω  (parallel with Z₁)",
            leftMarker = "Z₁ + Z₂ = 8 Ω",
            rightMarker = "I₁ = I_s·Z₂/(Z₁+Z₂)",
            methodTitle = "PHASOR CURRENT DIVIDER",
            methodNote = "By the two-branch current divider, multiply source I_s by opposite branch Z₂ / (Z₁ + Z₂).",
            inputHint = "Enter I₁ in A (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(6.25, -5.0),
            unit = "A",
            hints = listOf(
                "Sum the two impedances: Z₁ + Z₂ = (3 + 5) + j(4 − 4) = 8 Ω.",
                "I₁ = 10 × (5 − j4) / 8 = 1.25 × (5 − j4)."
            ),
            workedSteps = listOf(
                "1. Z₁ + Z₂ = (3 + j4) + (5 − j4) = 8 + j0 Ω.",
                "2. I₁ = 10 · (5 − j4) / 8 = 6.25 − j5 A (8.004∠−38.66° A)."
            )
        ),
        CircuitProblem(
            id = "p_124f",
            code = "No. 124F",
            title = "Inverting Op-Amp Active AC Gain",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.MASTERY,
            topology = CircuitTopology.FEEDBACK_LOOP,
            prompt = "An ideal inverting op-amp has input R_in and parallel R_f ∥ C_f feedback. Find complex gain A_v = −Z_f / R_in:",
            parametersLine1 = "R_in = 10 kΩ,   R_f = 20 kΩ ∥ C_f = 100 nF",
            parametersLine2 = "ω = 500 rad/s",
            leftMarker = "ACTIVE FILTER",
            rightMarker = "A_v = −Z_f / R_in",
            methodTitle = "ACTIVE AC GAIN",
            methodNote = "Compute capacitive reactance |X_Cf| = 1/(ωC_f), parallel Z_f = R_f ∥ (−jX_Cf), then A_v = −Z_f / R_in.",
            inputHint = "Enter A_v (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(-1.0, 1.0),
            unit = "V/V",
            hints = listOf(
                "X_Cf = 1 / (500 × 100×10⁻⁹) = 20 kΩ, so Z_Cf = −j20 kΩ.",
                "Z_f = 20k ∥ (−j20k) = 10 − j10 kΩ; divide by −R_in = −10 kΩ."
            ),
            workedSteps = listOf(
                "1. X_Cf = 1 / (500 × 10⁻⁷) = 20 kΩ ⇒ Z_Cf = −j20 kΩ.",
                "2. Z_f = (20k · −j20k) / (20k − j20k) = 10 − j10 kΩ.",
                "3. A_v = −Z_f / R_in = −(10 − j10) / 10 = −1 + j1 V/V (1.414∠135°)."
            )
        ),
        CircuitProblem(
            id = "p_144h",
            code = "No. 144H",
            title = "AC Thévenin with Dependent Source",
            category = ProblemCategory.IMPEDANCE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.FEEDBACK_LOOP,
            prompt = "A test source V_t drives series R = 6Ω, Z_C = −j8Ω, and dependent drop V_d = 2V_C in loop KVL. Find Z_th = V_t/I_t:",
            parametersLine1 = "R = 6Ω,   Z_C = −j8Ω,   V_C = I_t·Z_C",
            parametersLine2 = "Loop KVL: V_t = I_t·R + V_C + 2V_C",
            leftMarker = "TEST DRIVE V_t/I_t",
            rightMarker = "DEPENDENT 2V_C",
            methodTitle = "TEST-SOURCE METHOD",
            methodNote = "Substitute V_C = I_t(−j8) into the KVL equation and factor out I_t to obtain Z_th = V_t / I_t.",
            inputHint = "Enter Z_th in Ω (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(6.0, -24.0),
            unit = "Ω",
            hints = listOf(
                "Combine V_C + 2V_C = 3V_C = 3(−j8 I_t) = −j24 I_t.",
                "V_t = I_t(6 − j24), so Z_th = V_t / I_t = 6 − j24 Ω."
            ),
            workedSteps = listOf(
                "1. Capacitor voltage is V_C = I_t(−j8).",
                "2. KVL: V_t = 6 I_t + 3 V_C = (6 − j24) I_t.",
                "3. Z_th = V_t / I_t = 6 − j24 Ω."
            )
        ),
        CircuitProblem(
            id = "p_508c",
            code = "No. 508C",
            title = "Wien Bridge Zero-Phase Frequency",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.PARALLEL_RC,
            prompt = "Find the zero-phase angular null frequency ω₀ = 1/(RC) (in rad/s) of a balanced Wien RC bridge:",
            parametersLine1 = "R₁ = R₂ = 5 kΩ",
            parametersLine2 = "C₁ = C₂ = 200 nF  (series & parallel arms)",
            leftMarker = "ZERO PHASE SHIFT",
            rightMarker = "ω₀ = 1 / (R·C)",
            methodTitle = "WIEN BRIDGE BALANCE",
            methodNote = "At ω₀ = 1/(RC), the series-RC and parallel-RC arms match ratio V_out/V_in = 1/3 at 0°.",
            inputHint = "Enter ω₀ in rad/s",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 1000.0,
            unit = "rad/s",
            hints = listOf(
                "Convert R = 5000 Ω and C = 200 × 10⁻⁹ F.",
                "R·C = 5000 × 2×10⁻⁷ = 10⁻³ s, so ω₀ = 1 / 10⁻³."
            ),
            workedSteps = listOf(
                "1. R·C = (5 × 10³) × (200 × 10⁻⁹) = 1.0 × 10⁻³ s.",
                "2. ω₀ = 1 / (R·C) = 1000 rad/s (159.15 Hz)."
            )
        ),
        CircuitProblem(
            id = "p_528i",
            code = "No. 528I",
            title = "Capacitor Self-Resonant Frequency",
            category = ProblemCategory.ADMITTANCE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "A capacitor has parasitic ESL = 40 nH in series with C = 10 nF and ESR = 0.05 Ω. Find ω_SRF (in Mrad/s):",
            parametersLine1 = "C = 10 nF,   ESL (L_s) = 40 nH",
            parametersLine2 = "ESR = 0.05 Ω  (series parasitic model)",
            leftMarker = "CAPACITIVE < SRF",
            rightMarker = "INDUCTIVE > SRF",
            methodTitle = "PARASITIC RESONANCE",
            methodNote = "Self-resonance occurs when ω·L_s = 1/(ω·C): solve ω_SRF = 1/√(L_s C) and convert to Mrad/s (10⁶).",
            inputHint = "Enter ω_SRF in Mrad/s",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 50.0,
            unit = "Mrad/s",
            hints = listOf(
                "L_s·C = (40 × 10⁻⁹) × (10 × 10⁻⁹) = 4 × 10⁻¹⁶ s².",
                "√(4 × 10⁻¹⁶) = 2 × 10⁻⁸ s ⇒ 1 / (2×10⁻⁸) = 50 × 10⁶ rad/s."
            ),
            workedSteps = listOf(
                "1. L_s·C = 400 × 10⁻¹⁸ = 4 × 10⁻¹⁶ s² ⇒ √(L_s·C) = 2 × 10⁻⁸ s.",
                "2. ω_SRF = 1 / (2×10⁻⁸) = 5 × 10⁷ rad/s = 50 Mrad/s (7.96 MHz)."
            )
        ),
        CircuitProblem(
            id = "p_222g",
            code = "No. 222G",
            title = "Upper Half-Power Frequency ω₂",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "An RLC bandpass filter has ω₀ = 1200 rad/s and exact −3 dB bandwidth BW = 1000 rad/s. Find upper corner ω₂:",
            parametersLine1 = "ω₀ = 1200 rad/s,   BW = ω₂ − ω₁ = 1000 rad/s",
            parametersLine2 = "Exact geometric symmetry: ω₁ · ω₂ = ω₀²",
            leftMarker = "ω₁ = 800 rad/s",
            rightMarker = "ω₂ = BW/2 + √...",
            methodTitle = "EXACT CUTOFF ROOTS",
            methodNote = "Solve ω₂ = (BW / 2) + √((BW / 2)² + ω₀²) so ω₂ − ω₁ = BW and √(ω₁ω₂) = ω₀.",
            inputHint = "Enter ω₂ in rad/s",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 1800.0,
            unit = "rad/s",
            hints = listOf(
                "BW / 2 = 500 rad/s.",
                "Notice (500, 1200, 1300) is a 5-12-13 right triangle: 500 + 1300."
            ),
            workedSteps = listOf(
                "1. BW / 2 = 1000 / 2 = 500 rad/s.",
                "2. √(500² + 1200²) = 1300 rad/s.",
                "3. ω₂ = 500 + 1300 = 1800 rad/s (and ω₁ = 800 rad/s, check √(800×1800) = 1200)."
            )
        ),
        CircuitProblem(
            id = "p_230i",
            code = "No. 230I",
            title = "L-Network Resonant Impedance Match",
            category = ProblemCategory.RESONANCE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.PARALLEL_RLC,
            prompt = "Match R_s = 10Ω up to R_L = 50Ω using series +jX_L and shunt −jX_C across R_L. Find series X_L (in Ω):",
            parametersLine1 = "Source R_s = 10 Ω  →  Load R_L = 50 Ω",
            parametersLine2 = "Q = √(R_L / R_s − 1),   X_L = Q · R_s",
            leftMarker = "STEP-UP RATIO 5:1",
            rightMarker = "MATCHED 10 + j0 Ω",
            methodTitle = "RF L-NETWORK MATCH",
            methodNote = "Compute transformation Q = √(R_L/R_s − 1), then series reactance X_L = Q·R_s cancels shunt reactive part.",
            inputHint = "Enter X_L in Ω",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 20.0,
            unit = "Ω",
            hints = listOf(
                "Q = √(50 / 10 − 1) = √4 = 2.",
                "X_L = Q × R_s = 2 × 10 Ω (with shunt X_C = R_L / Q = 25 Ω)."
            ),
            workedSteps = listOf(
                "1. Required network Q = √(50/10 − 1) = √4 = 2.",
                "2. Series reactance X_L = Q · R_s = 2 × 10 = 20 Ω (shunt 50∥−j25 = 10−j20 cancels +j20)."
            )
        ),
        CircuitProblem(
            id = "p_302a",
            code = "No. 302A",
            title = "Multi-Frequency Harmonic RMS",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "A branch carries DC plus two sinusoidal harmonics. Compute total effective RMS current I_rms (in Amps):",
            parametersLine1 = "i(t) = 3 + 4√2 cos(100t) + 12√2 sin(300t) A",
            parametersLine2 = "Orthogonal harmonics: I_rms = √(I₀² + I₁² + I₃²)",
            leftMarker = "I₀=3, I₁=4, I₃=12",
            rightMarker = "ORTHOGONAL RMS",
            methodTitle = "HARMONIC SUPERPOSITION",
            methodNote = "Convert AC peaks V_m to RMS (V_m / √2) and take the root-sum-square across distinct frequencies.",
            inputHint = "Enter total I_rms in A",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 13.0,
            unit = "A",
            hints = listOf(
                "RMS components are I₀ = 3 A, I₁ = (4√2)/√2 = 4 A, and I₃ = 12 A.",
                "Compute √(3² + 4² + 12²) = √(25 + 144)."
            ),
            workedSteps = listOf(
                "1. Individual RMS values: I₀ = 3 A, I₁ = 4 A, I₃ = 12 A.",
                "2. I_rms = √(3² + 4² + 12²) = √169 = 13 A."
            )
        ),
        CircuitProblem(
            id = "p_312c",
            code = "No. 312C",
            title = "Balanced 3-Phase Line Voltage V_ab",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "Given positive-sequence (a-b-c) phase voltage V_an, compute line-to-line phasor V_ab (in V rms):",
            parametersLine1 = "V_an = 200∠0° V rms  (positive sequence)",
            parametersLine2 = "V_ab = √3 · V_an ∠+30°",
            leftMarker = "SCALE BY √3",
            rightMarker = "LEAD +30°",
            methodTitle = "THREE-PHASE LINE PHASOR",
            methodNote = "In positive abc sequence, line voltage V_ab leads phase voltage V_an by +30° with magnitude √3·|V_an|.",
            inputHint = "Enter V_ab in V (A + jB or r∠θ)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(300.0, 100.0 * sqrt(3.0)),
            unit = "V",
            hints = listOf(
                "Magnitude |V_ab| = 200√3 ≈ 346.41 V at angle +30°.",
                "Rectangular: 346.41 cos(30°) = 300 V, 346.41 sin(30°) = 100√3 ≈ 173.21 V."
            ),
            workedSteps = listOf(
                "1. V_ab = 200√3 ∠30° V ≈ 346.41∠30° V.",
                "2. In rectangular form: 300 + j173.21 V."
            )
        ),
        CircuitProblem(
            id = "p_334j",
            code = "No. 334J",
            title = "AC Feeder Line Drop & Sending V_s",
            category = ProblemCategory.POWER,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.COMPLEX_LOAD,
            prompt = "A feeder with Z_line feeds a load at V_L = 200∠0° V drawing I = (8 − j6) A. Find sending-end V_s (in V):",
            parametersLine1 = "V_L = (200 + j0) V,   I = (8 − j6) A",
            parametersLine2 = "Z_line = (1 + j2) Ω",
            leftMarker = "DROP I · Z_line",
            rightMarker = "V_s = V_L + I·Z_line",
            methodTitle = "FEEDER VOLTAGE DROP",
            methodNote = "Multiply phasor current I by line impedance Z_line to find line drop, then add V_L.",
            inputHint = "Enter V_s in V (A + jB)",
            answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
            expectedComplex = ComplexNum(220.0, 10.0),
            unit = "V",
            hints = listOf(
                "I · Z_line = (8 − j6)(1 + j2) = (8 + 12) + j(16 − 6) = 20 + j10 V.",
                "Add V_L = 200 + j0 V."
            ),
            workedSteps = listOf(
                "1. Line drop ΔV = (8 − j6)(1 + j2) = 8 + j16 − j6 − j²12 = 20 + j10 V.",
                "2. Sending voltage V_s = 200 + (20 + j10) = 220 + j10 V (220.23∠2.60° V)."
            )
        ),
        CircuitProblem(
            id = "p_403a",
            code = "No. 403A",
            title = "First-Order RL Time Constant",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.FOUNDATIONS,
            topology = CircuitTopology.SERIES_RL,
            prompt = "Calculate the exponential current time constant τ = L / R (in milliseconds) for the RL branch:",
            parametersLine1 = "L = 50 mH,   R = 25 Ω",
            parametersLine2 = "τ = L / R  (natural response e^(−t/τ))",
            leftMarker = "INDUCTANCE L",
            rightMarker = "τ = L / R (ms)",
            methodTitle = "RL TIME CONSTANT",
            methodNote = "Divide inductance L by series resistance R; dividing mH by Ω yields milliseconds directly.",
            inputHint = "Enter τ in ms",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 2.0,
            unit = "ms",
            hints = listOf(
                "τ = 0.050 H / 25 Ω = 0.002 s.",
                "Convert seconds to milliseconds (×1000)."
            ),
            workedSteps = listOf(
                "1. τ = L / R = 0.050 / 25 = 0.002 s.",
                "2. Convert to ms: τ = 2 ms."
            )
        ),
        CircuitProblem(
            id = "p_407b",
            code = "No. 407B",
            title = "High-Pass RC Filter Phase Lead",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.LOW_PASS_RC,
            prompt = "Evaluate the output phase angle ∠H(jω) (in degrees) of the first-order RC high-pass filter at corner ω_c:",
            parametersLine1 = "H(s) = sRC / (1 + sRC)",
            parametersLine2 = "Evaluate at ω = ω_c = 1/(RC) = 500 rad/s",
            leftMarker = "OUTPUT LEADS",
            rightMarker = "+3dB CORNER PHASE",
            methodTitle = "HIGH-PASS PHASE",
            methodNote = "Substitute s = jω_c so ω_c RC = 1, giving H(jω_c) = j / (1 + j).",
            inputHint = "Enter angle in degrees",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 45.0,
            unit = "°",
            hints = listOf(
                "Numerator angle ∠j = +90°; denominator angle ∠(1 + j1) = +45°.",
                "Subtract angles: 90° − 45°."
            ),
            workedSteps = listOf(
                "1. H(jω_c) = j1 / (1 + j1) = (1∠90°) / (√2∠45°) = 0.707∠+45°.",
                "2. Phase lead at cutoff is +45°."
            )
        ),
        CircuitProblem(
            id = "p_497d",
            code = "No. 497D",
            title = "Critical Damping Resistance R_crit",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.ADVANCED,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Find the series resistance R_crit (in Ohms) needed for critical damping (ζ = 1) in this RLC network:",
            parametersLine1 = "L = 25 mH,   C = 10 μF",
            parametersLine2 = "Critical damping condition: ζ = α / ω₀ = 1",
            leftMarker = "REPEATED LHP POLE",
            rightMarker = "R_crit = 2√(L/C)",
            methodTitle = "CRITICAL DAMPING",
            methodNote = "Set α = R/(2L) equal to ω₀ = 1/√(LC) to solve R_crit = 2√(L / C).",
            inputHint = "Enter R_crit in Ω",
            answerKind = AnswerKind.REAL_SCALAR,
            expectedReal = 100.0,
            unit = "Ω",
            hints = listOf(
                "L / C = (25 × 10⁻³) / (10 × 10⁻⁶) = 2500.",
                "R_crit = 2 × √2500 = 2 × 50."
            ),
            workedSteps = listOf(
                "1. L / C = 0.025 / 10⁻⁵ = 2500 ⇒ √(L/C) = 50 Ω.",
                "2. R_crit = 2√(L/C) = 2 × 50 = 100 Ω."
            )
        )
    )

    val curatedProblems: List<CircuitProblem> = rawCuratedProblems.let { list ->
        listOf(list.first()) + list.drop(1).sortedWith(
            compareBy({ it.category.ordinal }, { it.difficulty.ordinal }, { it.code })
        )
    }

    val fieldGuideEntries: List<FieldGuideEntry> = listOf(
        FieldGuideEntry(
            id = "fg_imp",
            tag = "IMPEDANCE",
            title = "Phasor Reactance Laws",
            formula = "Z_R = R   |   Z_L = jωL   |   Z_C = −j / (ωC)",
            explanation = "In sinusoidal steady state (s = jω), inductors shift voltage +90° ahead of current while capacitors lag voltage −90° behind current."
        ),
        FieldGuideEntry(
            id = "fg_adm",
            tag = "ADMITTANCE & LCR METERS",
            title = "Admittance, Susceptance & Loss Factor",
            formula = "Y = 1/Z = G + jB   |   D = ESR/|X_C| = 1/Q   |   R_p = R_s(1 + Q²)",
            explanation = "Conductance G = Re(Y) and susceptance B = Im(Y) simplify parallel networks (B_C > 0, B_L < 0). LCR meters measure series/parallel R, X, Q, and dissipation factor D = tan(δ)."
        ),
        FieldGuideEntry(
            id = "fg_euler",
            tag = "COMPLEX ALGEBRA",
            title = "Rectangular ↔ Polar Form",
            formula = "A + jB = r∠θ   where   r = √(A² + B²),  θ = atan2(B, A)",
            explanation = "Add/subtract impedances in rectangular form; multiply/divide phasors in polar form by multiplying magnitudes and adding angles."
        ),
        FieldGuideEntry(
            id = "fg_res",
            tag = "RESONANCE",
            title = "Resonance & Quality Factor",
            formula = "ω₀ = 1 / √(LC)   |   Q_series = (1/R)√(L/C)   |   BW = ω₀ / Q",
            explanation = "At ω₀, inductive and capacitive reactances cancel completely (X_L = X_C), yielding purely real impedance and unity power factor."
        ),
        FieldGuideEntry(
            id = "fg_pwr",
            tag = "POWER TRIANGLE",
            title = "Complex Power S = P + jQ",
            formula = "S = V_rms · I_rms* = P + jQ   |   pf = P / |S| = cos(θ_v − θ_i)",
            explanation = "Real power P (Watts) performs work; reactive power Q (VAR) oscillates in fields. Inductive loads have +Q (lagging pf)."
        ),
        FieldGuideEntry(
            id = "fg_lap",
            tag = "LAPLACE & CONTROL",
            title = "Residue Theorem & Routh-Hurwitz",
            formula = "f(t) = Σ Res[ F(s)e^(st), s_k ]   |   s³+a₂s²+a₁s+a₀: a₂a₁ > a₀",
            explanation = "Poles in the open left-half s-plane decay exponentially (stable). Right-half poles grow without bound; jω-axis poles oscillate."
        ),
        FieldGuideEntry(
            id = "fg_ca",
            tag = "COMPLEX ANALYSIS",
            title = "Cauchy Residues, Bromwich & Möbius Γ",
            formula = "∮ f(z)dz = j2π Σ Res[f, z_k]   |   Γ = (Z_L − Z₀)/(Z_L + Z₀)",
            explanation = "Holomorphic functions satisfy Cauchy-Riemann (u_x = v_y, u_y = −v_x). Contour integrals count enclosed residues, and Nyquist plots apply Cauchy's Argument Principle (Z = P − N_CCW)."
        ),
        FieldGuideEntry(
            id = "fg_ga",
            tag = "GEOMETRIC ALGEBRA",
            title = "Clifford Product, Rotors & Power Norm",
            formula = "ab = a·b + a∧b   |   e₁₂² = −1   |   v' = R v R̃   |   ||M|| = ||v||·||i||",
            explanation = "In Cl₂,₀, the unit bivector e₁₂ squares to −1 and generates rotors R = cos(θ/2) − sin(θ/2)e₁₂. In Cl₃,₀, geometric power M = v·i + v∧i unifies active, reactive, and harmonic distortion power."
        )
    )

    private var drillSequence = 0

    private fun pickTemplateIndex(
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

    fun generateRandomDrill(
        preferredCategory: ProblemCategory? = null,
        excludeCategory: ProblemCategory? = null,
        excludeTitle: String? = null,
        excludeTitles: Set<String> = emptySet(),
        preferredDifficulty: Difficulty? = null
    ): CircuitProblem {
        val category = preferredCategory ?: run {
            val pool = ProblemCategory.entries.filter { it != excludeCategory }
            (if (pool.isNotEmpty()) pool else ProblemCategory.entries).random()
        }
        val excluded = if (excludeTitle != null) excludeTitles + excludeTitle else excludeTitles
        val drillNum = Random.nextInt(500, 999)
        val drillId = "drill_${drillNum}_${++drillSequence}"
        return when (category) {
            ProblemCategory.COMPLEX_ANALYSIS -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural Simple Pole Residue" to Difficulty.ADVANCED,
                        "Procedural Smith Chart Γ" to Difficulty.ADVANCED,
                        "Procedural Cauchy-Riemann Constant" to Difficulty.MASTERY,
                        "Procedural Closed Contour Integral" to Difficulty.ADVANCED,
                        "Procedural Bilinear Map s → z" to Difficulty.ADVANCED,
                        "Procedural Euler Polar-to-Rect" to Difficulty.FOUNDATIONS,
                        "Procedural Real-Pole Cover-Up" to Difficulty.INTERMEDIATE
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
                        prompt = "Compute the residue Res[f(z), z = $targetPole] for the meromorphic function:",
                        parametersLine1 = "f(z) = $numStr / ((z $pAStr)(z $pBStr))",
                        parametersLine2 = "Evaluate at simple pole z₀ = $targetPole",
                        leftMarker = "COVER-UP RULE",
                        rightMarker = "SIMPLE POLE",
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
                        isProcedural = true
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
                        leftMarker = "MÖBIUS MAP",
                        rightMarker = "|Γ| < 1 PASSIVE",
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
                        isProcedural = true
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
                        prompt = "Find constant k so f(z) = u(x,y) + j·v(x,y) is everywhere holomorphic:",
                        parametersLine1 = uLine,
                        parametersLine2 = vLine,
                        leftMarker = "u_x = v_y",
                        rightMarker = "u_y = −v_x",
                        methodTitle = "CAUCHY-RIEMANN DRILL",
                        methodNote = "Differentiate u and v partially and equate ∂u/∂x = ∂v/∂y and ∂u/∂y = −∂v/∂x.",
                        inputHint = "Enter constant k",
                        answerKind = AnswerKind.REAL_SCALAR,
                        expectedReal = kTarget.toDouble(),
                        unit = "",
                        hints = listOf(hint1, "Solve for real constant k."),
                        workedSteps = listOf("1. $step1"),
                        isProcedural = true
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
                        prompt = "Evaluate I = (1/j2π) ∮ f(z) dz positively around the circle |z| = $radius:",
                        parametersLine1 = "f(z) = $numStr / ((z $f1)(z $f2))",
                        parametersLine2 = "Contour C: |z| = $radius (CCW)",
                        leftMarker = "|$z1| < $radius INSIDE",
                        rightMarker = if (bothInside) "|$z2| < $radius INSIDE" else "|$z2| > $radius OUTSIDE",
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
                        isProcedural = true
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
                        leftMarker = "jω-AXIS",
                        rightMarker = "UNIT CIRCLE",
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
                        isProcedural = true
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
                        prompt = "Convert Euler exponential z = $r·e^(j$radLabel) into rectangular form A + jB:",
                        parametersLine1 = "z = $r · e^(j$radLabel)   (θ = $deg°)",
                        parametersLine2 = "z = r·cos(θ) + j·r·sin(θ)",
                        leftMarker = "MODULUS r = $r",
                        rightMarker = "PHASE $deg°",
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
                        isProcedural = true
                    )
                }
                else -> {
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
                        prompt = "Use the cover-up rule to compute Res[F(s), s = $targetPole] for:",
                        parametersLine1 = "F(s) = $k / (s · (s + $p))",
                        parametersLine2 = "Evaluate at simple real pole s₀ = $targetPole",
                        leftMarker = "COVER-UP RULE",
                        rightMarker = "s₀ = $targetPole",
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
                        isProcedural = true
                    )
                }
            }
            ProblemCategory.GEOMETRIC_ALGEBRA -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural Cl₂,₀ Geometric Product" to Difficulty.MASTERY,
                        "Procedural Cl₂,₀ Multivector Inverse" to Difficulty.ADVANCED,
                        "Procedural Cl₃,₀ Power Norm" to Difficulty.ADVANCED,
                        "Procedural Fryze Orthogonal Current" to Difficulty.ADVANCED,
                        "Procedural Cl₂,₀ Outer Wedge a ∧ b" to Difficulty.FOUNDATIONS,
                        "Procedural Cl₂,₀ Inner Product P" to Difficulty.FOUNDATIONS,
                        "Procedural Cl₂,₀ Impedance Z" to Difficulty.INTERMEDIATE
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
                        prompt = "Compute ab = a·b + a∧b in Cl₂,₀ (enter α + βe12 or α + jβ):",
                        parametersLine1 = "a = ${fmtVec(a1, a2)}",
                        parametersLine2 = "b = ${fmtVec(b1, b2)}",
                        leftMarker = "SCALAR a·b",
                        rightMarker = "BIVECTOR a∧b",
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
                            "2. ab = ${ComplexNum(dot, wedge).formatRect()}."
                        ),
                        isProcedural = true
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
                        prompt = "Find the multivector inverse M⁻¹ = M̃ / (M M̃) in Cl₂,₀ (enter α + βe12 or α + jβ):",
                        parametersLine1 = "M = ${a.toInt()} $mSign",
                        parametersLine2 = "Reversion M̃ = ${a.toInt()} $conjSign",
                        leftMarker = "M M̃ = ${normSq.toInt()}",
                        rightMarker = "(e₁₂)² = −1",
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
                            "2. M⁻¹ = (${a.toInt()} $conjSign) / ${normSq.toInt()} = ${ComplexNum(invRe, invIm).formatRect()}."
                        ),
                        isProcedural = true
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
                        leftMarker = "||v|| = ${vNorm}V",
                        rightMarker = "||i|| = ${iNorm}A",
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
                        isProcedural = true
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
                        leftMarker = "ACTIVE i·v̂",
                        rightMarker = "ORTHO ||i∧v̂||",
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
                        isProcedural = true
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
                        prompt = "Find the oriented bivector coefficient β in a ∧ b = β·e₁₂ in Cl₂,₀:",
                        parametersLine1 = "a = ${fmtVec(a1, a2)}",
                        parametersLine2 = "b = ${fmtVec(b1, b2)}",
                        leftMarker = "WEDGE a∧b",
                        rightMarker = "e₂∧e₁ = −e₁₂",
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
                        isProcedural = true
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
                        leftMarker = "GRADE-0 SCALAR",
                        rightMarker = "P = v₁i₁ + v₂i₂",
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
                        isProcedural = true
                    )
                }
                else -> {
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
                        leftMarker = "SCALAR R",
                        rightMarker = "BIVECTOR X_L e₁₂",
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
                        isProcedural = true
                    )
                }
            }
            ProblemCategory.ADMITTANCE -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural Complex Admittance" to Difficulty.INTERMEDIATE,
                        "Procedural LCR Loss Factor D" to Difficulty.INTERMEDIATE,
                        "Procedural LCR Series → Parallel R_p" to Difficulty.MASTERY,
                        "Procedural LCR Parallel C_p" to Difficulty.MASTERY,
                        "Procedural Parallel G + jB" to Difficulty.FOUNDATIONS,
                        "Procedural Capacitor SRF" to Difficulty.ADVANCED
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
                        leftMarker = "|Z|² = ${magSq.toInt()}",
                        rightMarker = if (isCapacitive) "CAPACITIVE +jB" else "INDUCTIVE −jB",
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
                            leftMarker = "LOSS TANGENT",
                            rightMarker = "D = 1 / Q",
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
                            leftMarker = "LOSS TANGENT D",
                            rightMarker = "SERIES ESR (Ω)",
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
                                leftMarker = "INVERSE LOSS",
                                rightMarker = "QUALITY FACTOR Q",
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
                        leftMarker = "SERIES R_s = ${rs}Ω",
                        rightMarker = "PARALLEL R_p",
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
                        leftMarker = "G_p = ${ComplexNum.formatClean(gP)}S",
                        rightMarker = "B_p = ωC_p",
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
                        leftMarker = "G = 1/R",
                        rightMarker = "B_C = ωC",
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
                else -> {
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
                        leftMarker = "SERIES $lUnit + $cUnit",
                        rightMarker = "ω_SRF (Mrad/s)",
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
            }
            ProblemCategory.IMPEDANCE -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural RL Impedance" to Difficulty.INTERMEDIATE,
                        "Procedural RC Impedance" to Difficulty.INTERMEDIATE,
                        "Procedural Series RLC Impedance" to Difficulty.INTERMEDIATE,
                        "Procedural Transformer Impedance" to Difficulty.MASTERY,
                        "Procedural Coupled Inductors" to Difficulty.ADVANCED,
                        "Procedural Sine–Cosine Phase" to Difficulty.FOUNDATIONS,
                        "Procedural Complex Current Divider" to Difficulty.INTERMEDIATE
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
                        leftMarker = "R = ${r.toInt()}Ω",
                        rightMarker = "|Z| = ${hyp}Ω",
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
                        leftMarker = "R = ${r.toInt()}Ω",
                        rightMarker = "|Z| = ${hyp}Ω",
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
                        leftMarker = "X_L = ${xl}Ω",
                        rightMarker = "X_C = ${xc}Ω",
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
                            leftMarker = "a² = ${ComplexNum.formatClean(aSq)}",
                            rightMarker = "Z_L = Z_in / a²",
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
                            leftMarker = "a = ${ratio.first}/${ratio.second}",
                            rightMarker = "Z_in = a² Z_L",
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
                        leftMarker = "L_eq = L₁+L₂${signSym}2M",
                        rightMarker = "ω = $omega",
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
                        leftMarker = "sin φ = cos(φ−90°)",
                        rightMarker = "θ = θ_v − θ_i",
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
                else -> {
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
                        leftMarker = "Z₁ + Z₂ = 10 Ω",
                        rightMarker = "I₁ = I_s·Z₂/(Z₁+Z₂)",
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
            }
            ProblemCategory.RESONANCE -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural Resonance Q" to Difficulty.INTERMEDIATE,
                        "Procedural Resonant Frequency ω₀" to Difficulty.FOUNDATIONS,
                        "Procedural −3 dB Bandwidth" to Difficulty.INTERMEDIATE,
                        "Procedural Resonant Voltage Gain" to Difficulty.MASTERY,
                        "Procedural Lossy Tank Z_tank" to Difficulty.ADVANCED,
                        "Procedural L-Match Network" to Difficulty.ADVANCED
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
                        leftMarker = "ω₀ = $omega0",
                        rightMarker = "RESONANCE",
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
                        leftMarker = "X_L = X_C",
                        rightMarker = "ω₀ rad/s",
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
                            leftMarker = "−3 dB POINTS",
                            rightMarker = "BW = f₀ / Q",
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
                            leftMarker = "SELECTIVITY",
                            rightMarker = "Q = f₀ / BW",
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
                            leftMarker = "Q = f₀ / BW",
                            rightMarker = "f₀ = BW · Q",
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
                        leftMarker = "I = V_s / R",
                        rightMarker = "|V_C| = Q · V_s",
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
                        leftMarker = "Q_coil = $qCoil",
                        rightMarker = "R_p = R_s(1+Q²)",
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
                else -> {
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
                            leftMarker = "Q = $q",
                            rightMarker = "X_C = R_L / Q",
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
                            leftMarker = "Q = $q",
                            rightMarker = "X_L = Q · R_s",
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
            }
            ProblemCategory.POWER -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural Power Factor" to Difficulty.FOUNDATIONS,
                        "Procedural Complex Power S = V·I*" to Difficulty.INTERMEDIATE,
                        "Procedural Max Power Transfer" to Difficulty.MASTERY,
                        "Procedural 3-Phase Δ ↔ Y" to Difficulty.INTERMEDIATE,
                        "Procedural 3-Phase Line Power" to Difficulty.ADVANCED,
                        "Procedural Multi-Freq RMS" to Difficulty.FOUNDATIONS
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
                        leftMarker = "P = ${p}W",
                        rightMarker = "Q = ${q}VAR",
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
                        leftMarker = "CONJUGATE I*",
                        rightMarker = "S = P + jQ",
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
                        leftMarker = "X_L = −X_th",
                        rightMarker = "P_max = V²/(4R)",
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
                            leftMarker = "WYE Z_Y",
                            rightMarker = "DELTA 3 · Z_Y",
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
                            leftMarker = "DELTA Z_Δ",
                            rightMarker = "WYE Z_Δ / 3",
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
                        leftMarker = "BALANCED 3φ",
                        rightMarker = "P_3φ (kW)",
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
                else -> {
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
                        leftMarker = "I₀=$i0, I₁=$i1, I₃=$i2",
                        rightMarker = "TOTAL I_rms",
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
            }
            ProblemCategory.LAPLACE -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural Routh Margin" to Difficulty.MASTERY,
                        "Procedural Final Value Theorem" to Difficulty.INTERMEDIATE,
                        "Procedural Damping Ratio ζ" to Difficulty.INTERMEDIATE,
                        "Procedural Initial Value Theorem" to Difficulty.INTERMEDIATE,
                        "Procedural RLC Initial Slope" to Difficulty.ADVANCED,
                        "Procedural RC Time Constant τ" to Difficulty.FOUNDATIONS,
                        "Procedural Critical Damping R" to Difficulty.ADVANCED
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
                        prompt = "Find the critical positive gain K for marginal stability of:",
                        parametersLine1 = "1 + G(s)H(s) = 0",
                        parametersLine2 = "s³ + ${a2}s² + ${a1}s + K = 0",
                        leftMarker = "LHP STABLE",
                        rightMarker = "jω CROSSING",
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
                        isProcedural = true
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
                        prompt = "Using the Laplace Final Value Theorem, find steady-state y(∞) = lim(s→0) s·Y(s):",
                        parametersLine1 = "Y(s) = $k(s + $z) / [ s(s + $p1)(s + $p2) ]",
                        parametersLine2 = "All non-zero poles in open LHP",
                        leftMarker = "STEP INPUT",
                        rightMarker = "s → 0 LIMIT",
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
                        isProcedural = true
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
                        prompt = "Find the dimensionless damping ratio ζ = α/ω₀ for the characteristic polynomial:",
                        parametersLine1 = "s² + ${2 * alpha}s + ${omega0 * omega0} = 0",
                        parametersLine2 = "Standard Form: s² + 2ζω₀s + ω₀² = 0",
                        leftMarker = "α = $alpha",
                        rightMarker = "ω₀ = $omega0",
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
                        isProcedural = true
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
                        prompt = "Using the Laplace Initial Value Theorem, find f(0⁺) = lim(s→∞) s·F(s):",
                        parametersLine1 = "F(s) = $k(s + $z) / ((s + $p1)(s + $p2))",
                        parametersLine2 = "Strictly proper rational transform",
                        leftMarker = "t = 0+",
                        rightMarker = "s → ∞ LIMIT",
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
                        isProcedural = true
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
                        leftMarker = "i(0⁺) = 0",
                        rightMarker = "di/dt(0⁺)",
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
                        isProcedural = true
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
                            leftMarker = "R = ${rK}×10³ Ω",
                            rightMarker = "C = ${cUf}×10⁻⁶ F",
                            methodTitle = "TIME CONSTANT DRILL",
                            methodNote = "Multiply R (×10³ Ω) by C (×10⁻⁶ F); kΩ × μF yields milliseconds (×10⁻³ s).",
                            inputHint = "Enter τ in seconds",
                            answerKind = AnswerKind.REAL_SCALAR,
                            expectedReal = tauSec,
                            unit = "s",
                            hints = listOf("($rK × 10³) × ($cUf × 10⁻⁶) = $tauMs × 10⁻³ s.", "Divide $tauMs by 1000."),
                            workedSteps = listOf("1. τ = ($rK×10³) × ($cUf×10⁻⁶) = ${ComplexNum.formatClean(tauSec)} s."),
                            isProcedural = true
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
                            leftMarker = "τ = $tauMs ms",
                            rightMarker = "SOLVE FOR C (μF)",
                            methodTitle = "CAPACITANCE FROM τ",
                            methodNote = "Divide time constant τ (ms) by resistance R (kΩ) — units ms / kΩ equal μF directly!",
                            inputHint = "Enter C in μF",
                            answerKind = AnswerKind.REAL_SCALAR,
                            expectedReal = cUf.toDouble(),
                            unit = "μF",
                            hints = listOf("τ = ${ComplexNum.formatClean(tauSec)} s and R = ${rK * 1000} Ω.", "C = $tauMs ms / $rK kΩ = $cUf μF."),
                            workedSteps = listOf("1. C = τ / R = $tauMs ms / $rK kΩ = $cUf μF."),
                            isProcedural = true
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
                            leftMarker = "τ = ${ComplexNum.formatClean(tauSec)} s",
                            rightMarker = "ω_c = 1 / τ",
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
                            isProcedural = true
                        )
                    }
                }
                else -> {
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
                        leftMarker = "√(L/C) = ${rootZ0}Ω",
                        rightMarker = "R_crit = 2√(L/C)",
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
                        isProcedural = true
                    )
                }
            }
        }
    }
}
