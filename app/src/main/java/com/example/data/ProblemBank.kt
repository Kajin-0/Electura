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

    val curatedProblems: List<CircuitProblem> = listOf(
        CircuitProblem(
            id = "p_492b",
            code = "No. 492B",
            title = "Series LCR Transient & Steady State",
            category = ProblemCategory.LAPLACE,
            difficulty = Difficulty.INTERMEDIATE,
            topology = CircuitTopology.SERIES_RLC,
            prompt = "Solve for the steady-state current phasor I in a series circuit where:",
            parametersLine1 = "L = 0.5H, C = 0.02F, R = 10Ω",
            parametersLine2 = "E(t) = 100 sin(10t) V",
            leftMarker = "t = 0+",
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
        )
    )

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

    private fun pickTemplateIndex(titles: List<String>, excludeTitle: String?): Int {
        val candidates = titles.indices.filter { titles[it] != excludeTitle }
        return if (candidates.isNotEmpty()) candidates.random() else Random.nextInt(titles.size)
    }

    fun generateRandomDrill(
        preferredCategory: ProblemCategory? = null,
        excludeCategory: ProblemCategory? = null,
        excludeTitle: String? = null
    ): CircuitProblem {
        val category = preferredCategory ?: run {
            val pool = ProblemCategory.entries.filter { it != excludeCategory }
            (if (pool.isNotEmpty()) pool else ProblemCategory.entries).random()
        }
        val drillNum = Random.nextInt(500, 999)
        val drillId = "drill_${drillNum}_${++drillSequence}"
        return when (category) {
            ProblemCategory.COMPLEX_ANALYSIS -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural Simple Pole Residue",
                        "Procedural Smith Chart Γ",
                        "Procedural Cauchy-Riemann Constant",
                        "Procedural Closed Contour Integral"
                    ),
                    excludeTitle
                )
            ) {
                0 -> {
                    val pA = listOf(1, 2, 3, 4).random()
                    val pB = listOf(-1, -2, -3).random()
                    val evalAtFirst = Random.nextBoolean()
                    val targetPole = if (evalAtFirst) pA else pB
                    val otherPole = if (evalAtFirst) pB else pA
                    val c = listOf(-6, -4, 2, 4, 6, 8).random()
                    val res = c.toDouble() / (targetPole - otherPole).toDouble()
                    val pAStr = if (pA >= 0) "− $pA" else "+ ${-pA}"
                    val pBStr = if (pB >= 0) "− $pB" else "+ ${-pB}"
                    CircuitProblem(
                        id = drillId,
                        code = "Drill #$drillNum",
                        title = "Procedural Simple Pole Residue",
                        category = ProblemCategory.COMPLEX_ANALYSIS,
                        difficulty = Difficulty.ADVANCED,
                        topology = CircuitTopology.CONTOUR_SPLANE,
                        prompt = "Compute the residue Res[f(z), z = $targetPole] for the meromorphic function:",
                        parametersLine1 = "f(z) = $c / ((z $pAStr)(z $pBStr))",
                        parametersLine2 = "Evaluate at simple pole z₀ = $targetPole",
                        leftMarker = "COVER-UP RULE",
                        rightMarker = "SIMPLE POLE",
                        methodTitle = "RESIDUE DRILL",
                        methodNote = "Cancel the pole factor at z = $targetPole and evaluate the remaining factor at z = $targetPole.",
                        inputHint = "Enter residue",
                        answerKind = AnswerKind.REAL_SCALAR,
                        expectedReal = res,
                        unit = "",
                        hints = listOf(
                            "Res[f, $targetPole] = $c / ($targetPole − ($otherPole)).",
                            "Compute $c / ${targetPole - otherPole}."
                        ),
                        workedSteps = listOf(
                            "1. Res[f, $targetPole] = lim(z→$targetPole) (z − ($targetPole))f(z) = $c / ($targetPole − ($otherPole)).",
                            "2. = $c / ${targetPole - otherPole} = ${ComplexNum.formatClean(res)}."
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
                    val kTarget = 2 * a
                    CircuitProblem(
                        id = drillId,
                        code = "Drill #$drillNum",
                        title = "Procedural Cauchy-Riemann Constant",
                        category = ProblemCategory.COMPLEX_ANALYSIS,
                        difficulty = Difficulty.MASTERY,
                        topology = CircuitTopology.CONTOUR_SPLANE,
                        prompt = "Find constant k so f(z) = u(x,y) + j·v(x,y) is everywhere holomorphic:",
                        parametersLine1 = "u(x,y) = ${a}x² − ${a}y² + ${b}x",
                        parametersLine2 = "v(x,y) = k·xy + ${b}y",
                        leftMarker = "u_x = v_y",
                        rightMarker = "u_y = −v_x",
                        methodTitle = "CAUCHY-RIEMANN DRILL",
                        methodNote = "Differentiate u and v partially and equate ∂u/∂x = ∂v/∂y.",
                        inputHint = "Enter constant k",
                        answerKind = AnswerKind.REAL_SCALAR,
                        expectedReal = kTarget.toDouble(),
                        unit = "",
                        hints = listOf(
                            "∂u/∂x = ${2 * a}x + $b and ∂v/∂y = kx + $b.",
                            "Match coefficients of x to solve for k."
                        ),
                        workedSteps = listOf(
                            "1. ∂u/∂x = ${2 * a}x + $b;  ∂v/∂y = kx + $b.",
                            "2. Setting ∂u/∂x = ∂v/∂y gives k = $kTarget (and ∂u/∂y = −${2 * a}y = −∂v/∂x holds)."
                        ),
                        isProcedural = true
                    )
                }
                else -> {
                    val zIn = listOf(-2, -1, 1, 2).random()
                    val zOut = listOf(-5, -4, 4, 5).random()
                    val c = listOf(3, 6, 12, -6).random()
                    val integralVal = c.toDouble() / (zIn - zOut).toDouble()
                    val fIn = if (zIn >= 0) "− $zIn" else "+ ${-zIn}"
                    val fOut = if (zOut >= 0) "− $zOut" else "+ ${-zOut}"
                    CircuitProblem(
                        id = drillId,
                        code = "Drill #$drillNum",
                        title = "Procedural Closed Contour Integral",
                        category = ProblemCategory.COMPLEX_ANALYSIS,
                        difficulty = Difficulty.ADVANCED,
                        topology = CircuitTopology.CONTOUR_SPLANE,
                        prompt = "Evaluate I = (1/j2π) ∮ f(z) dz positively around the circle |z| = 3:",
                        parametersLine1 = "f(z) = $c / ((z $fIn)(z $fOut))",
                        parametersLine2 = "Contour C: |z| = 3 (CCW)",
                        leftMarker = "|$zIn| < 3 INSIDE",
                        rightMarker = "|$zOut| > 3 OUTSIDE",
                        methodTitle = "CAUCHY RESIDUE ENCLOSURE",
                        methodNote = "Only poles strictly inside |z| = 3 contribute to (1/j2π) ∮ f(z) dz = Σ Res_inside.",
                        inputHint = "Enter contour integral value",
                        answerKind = AnswerKind.REAL_SCALAR,
                        expectedReal = integralVal,
                        unit = "",
                        hints = listOf(
                            "Pole at z = $zIn has |$zIn| < 3 (enclosed); pole at z = $zOut lies outside.",
                            "Compute Res[f, $zIn] = $c / ($zIn − ($zOut))."
                        ),
                        workedSteps = listOf(
                            "1. Only z = $zIn lies inside |z| = 3.",
                            "2. I = Res[f, $zIn] = $c / ($zIn − ($zOut)) = ${ComplexNum.formatClean(integralVal)}."
                        ),
                        isProcedural = true
                    )
                }
            }
            ProblemCategory.GEOMETRIC_ALGEBRA -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural Cl₂,₀ Geometric Product",
                        "Procedural Cl₂,₀ Multivector Inverse",
                        "Procedural Cl₃,₀ Power Norm",
                        "Procedural Fryze Orthogonal Current"
                    ),
                    excludeTitle
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
                    val triples = listOf(Triple(3, 4, 5), Triple(4, 3, 5), Triple(6, 8, 10), Triple(5, 12, 13))
                    val t = triples.random()
                    val bSign = if (Random.nextBoolean()) 1 else -1
                    val a = t.first.toDouble()
                    val b = (t.second * bSign).toDouble()
                    val normSq = (t.third * t.third).toDouble()
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
                            "M M̃ = ${a.toInt()}² + ${t.second}² = ${normSq.toInt()}.",
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
                    val quadsV = listOf(listOf(10, 20, 20, 30), listOf(20, 30, 60, 70), listOf(10, 40, 80, 90))
                    val quadsI = listOf(listOf(1, 2, 2, 3), listOf(2, 3, 6, 7))
                    val qv = quadsV.random()
                    val qi = quadsI.random()
                    val normM = (qv[3] * qi[3]).toDouble()
                    CircuitProblem(
                        id = drillId,
                        code = "Drill #$drillNum",
                        title = "Procedural Cl₃,₀ Power Norm",
                        category = ProblemCategory.GEOMETRIC_ALGEBRA,
                        difficulty = Difficulty.ADVANCED,
                        topology = CircuitTopology.GEOMETRIC_PLANE,
                        prompt = "Compute the apparent geometric power norm ||M|| = ||v||·||i|| in Cl₃,₀ (in VA):",
                        parametersLine1 = "v = ${qv[0]}e₁ + ${qv[1]}e₂ + ${qv[2]}e₃ V",
                        parametersLine2 = "i = ${qi[0]}e₁ + ${qi[1]}e₂ + ${qi[2]}e₃ A",
                        leftMarker = "||v|| = ${qv[3]}V",
                        rightMarker = "||i|| = ${qi[3]}A",
                        methodTitle = "MULTIHARMONIC POWER DRILL",
                        methodNote = "In Clifford power theory, ||M|| = √(P² + ||Q||²) equals the product ||v||·||i||.",
                        inputHint = "Enter ||M|| in VA",
                        answerKind = AnswerKind.REAL_SCALAR,
                        expectedReal = normM,
                        unit = "VA",
                        hints = listOf(
                            "||v|| = √(${qv[0]}² + ${qv[1]}² + ${qv[2]}²) = ${qv[3]} V.",
                            "||i|| = √(${qi[0]}² + ${qi[1]}² + ${qi[2]}²) = ${qi[3]} A."
                        ),
                        workedSteps = listOf(
                            "1. Compute Euclidean norms: ||v|| = ${qv[3]} V, ||i|| = ${qi[3]} A.",
                            "2. ||M|| = ||v|| · ||i|| = ${qv[3]} × ${qi[3]} = ${normM.toInt()} VA."
                        ),
                        isProcedural = true
                    )
                }
                else -> {
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
            }
            ProblemCategory.ADMITTANCE -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural Complex Admittance",
                        "Procedural LCR Loss Factor D",
                        "Procedural LCR Series → Parallel R_p",
                        "Procedural LCR Parallel C_p"
                    ),
                    excludeTitle
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
                    val xc = listOf(100, 200, 250, 400, 500).random()
                    val dTarget = listOf(0.01, 0.02, 0.04, 0.05, 0.08).random()
                    val esr = xc * dTarget
                    val esrStr = ComplexNum.formatClean(esr)
                    CircuitProblem(
                        id = drillId,
                        code = "Drill #$drillNum",
                        title = "Procedural LCR Loss Factor D",
                        category = ProblemCategory.ADMITTANCE,
                        difficulty = Difficulty.INTERMEDIATE,
                        topology = CircuitTopology.SERIES_RC,
                        prompt = "An LCR meter measures a capacitor's ESR and reactance |X_C|. Compute dissipation factor D:",
                        parametersLine1 = "ESR = ${esrStr} Ω,   |X_C| = $xc Ω",
                        parametersLine2 = "D = tan(δ) = ESR / |X_C|",
                        leftMarker = "LOSS TANGENT",
                        rightMarker = "D = 1 / Q",
                        methodTitle = "LCR LOSS DRILL",
                        methodNote = "Divide equivalent series resistance ESR by capacitive reactance magnitude |X_C|.",
                        inputHint = "Enter D (e.g. 0.02)",
                        answerKind = AnswerKind.REAL_SCALAR,
                        expectedReal = dTarget,
                        unit = "",
                        hints = listOf(
                            "D = ESR / |X_C| = $esrStr / $xc.",
                            "Express as a dimensionless decimal."
                        ),
                        workedSteps = listOf(
                            "1. D = ESR / |X_C| = $esrStr / $xc = ${ComplexNum.formatClean(dTarget)}."
                        ),
                        isProcedural = true
                    )
                }
                2 -> {
                    val rs = listOf(2, 4, 5, 10).random()
                    val q = listOf(3, 4, 5, 8, 10).random()
                    val rp = rs * (1 + q * q).toDouble()
                    CircuitProblem(
                        id = drillId,
                        code = "Drill #$drillNum",
                        title = "Procedural LCR Series → Parallel R_p",
                        category = ProblemCategory.ADMITTANCE,
                        difficulty = Difficulty.MASTERY,
                        topology = CircuitTopology.PARALLEL_RC,
                        prompt = "Convert a lossy inductor's LCR series readout (R_s, Q) to equivalent parallel resistance R_p (in Ω):",
                        parametersLine1 = "R_s = $rs Ω,   Quality Factor Q = $q",
                        parametersLine2 = "R_p = R_s · (1 + Q²)",
                        leftMarker = "SERIES R_s",
                        rightMarker = "PARALLEL R_p",
                        methodTitle = "LCR EQUIVALENCE DRILL",
                        methodNote = "Apply the series-to-parallel transformation R_p = R_s(1 + Q²).",
                        inputHint = "Enter R_p in Ω",
                        answerKind = AnswerKind.REAL_SCALAR,
                        expectedReal = rp,
                        unit = "Ω",
                        hints = listOf(
                            "1 + Q² = 1 + $q² = ${1 + q * q}.",
                            "Multiply $rs × ${1 + q * q}."
                        ),
                        workedSteps = listOf(
                            "1. 1 + Q² = 1 + ${q * q} = ${1 + q * q}.",
                            "2. R_p = $rs × ${1 + q * q} = ${rp.toInt()} Ω."
                        ),
                        isProcedural = true
                    )
                }
                else -> {
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
            }
            ProblemCategory.IMPEDANCE -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural RL Impedance",
                        "Procedural RC Impedance",
                        "Procedural Series RLC Impedance",
                        "Procedural Transformer Impedance"
                    ),
                    excludeTitle
                )
            ) {
                0 -> {
                    val pyth = listOf(Triple(3, 4, 5), Triple(6, 8, 10), Triple(5, 12, 13), Triple(8, 15, 17)).random()
                    val omega = listOf(50, 100, 200).random()
                    val r = pyth.first.toDouble()
                    val x = pyth.second.toDouble()
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
                        rightMarker = "|Z| = ${pyth.third}Ω",
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
                            "2. Z = ${r.toInt()} + j${x.toInt()} Ω (|Z| = ${pyth.third} Ω)."
                        ),
                        isProcedural = true
                    )
                }
                1 -> {
                    val pyth = listOf(Triple(3, 4, 5), Triple(6, 8, 10), Triple(5, 12, 13), Triple(8, 15, 17)).random()
                    val omega = listOf(50, 100, 200).random()
                    val r = pyth.first.toDouble()
                    val x = pyth.second.toDouble()
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
                        rightMarker = "|Z| = ${pyth.third}Ω",
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
                            "2. Z = ${r.toInt()} − j${x.toInt()} Ω (|Z| = ${pyth.third} Ω)."
                        ),
                        isProcedural = true
                    )
                }
                2 -> {
                    val r = listOf(10, 15, 20, 30).random()
                    val omega = 100
                    val xl = listOf(30, 40, 50, 60).random()
                    val xc = listOf(10, 20, 25, 80).filter { it != xl }.random()
                    val lMh = (xl / omega.toDouble()) * 1000.0
                    val cUf = 1_000_000.0 / (omega * xc)
                    val xNet = (xl - xc).toDouble()
                    CircuitProblem(
                        id = drillId,
                        code = "Drill #$drillNum",
                        title = "Procedural Series RLC Impedance",
                        category = ProblemCategory.IMPEDANCE,
                        difficulty = Difficulty.INTERMEDIATE,
                        topology = CircuitTopology.SERIES_RLC,
                        prompt = "Find net series RLC impedance Z = R + j(X_L − X_C) (in Ohms) at ω = $omega rad/s:",
                        parametersLine1 = "R = ${r}Ω, L = ${lMh.toInt()}mH, C = ${cUf.toInt()}μF",
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
                            "X_L = $omega × ${lMh.toInt()}mH = $xl Ω; X_C = 1 / ($omega × ${cUf.toInt()}μF) = $xc Ω.",
                            "Net reactance X = X_L − X_C = ${xNet.toInt()} Ω."
                        ),
                        workedSteps = listOf(
                            "1. X_L = $xl Ω, X_C = $xc Ω ⇒ X_net = ${xNet.toInt()} Ω.",
                            "2. Z = ${ComplexNum(r.toDouble(), xNet).formatRect()} Ω."
                        ),
                        isProcedural = true
                    )
                }
                else -> {
                    val ratios = listOf(Pair(2, 1), Pair(3, 1), Pair(1, 2))
                    val ratio = ratios.random()
                    val aSq = (ratio.first.toDouble() / ratio.second.toDouble()).let { it * it }
                    val rL = if (ratio.first < ratio.second) listOf(4, 8, 12, 16).random() else listOf(2, 4, 5, 6).random()
                    val xMag = if (ratio.first < ratio.second) listOf(4, 8, 12, 16).random() else listOf(2, 3, 4, 5).random()
                    val xSignMul = if (Random.nextBoolean()) 1 else -1
                    val xL = xMag * xSignMul
                    val zInRe = aSq * rL
                    val zInIm = aSq * xL
                    val xLStr = if (xL >= 0) "+ j$xL" else "− j${-xL}"
                    CircuitProblem(
                        id = drillId,
                        code = "Drill #$drillNum",
                        title = "Procedural Transformer Impedance",
                        category = ProblemCategory.IMPEDANCE,
                        difficulty = Difficulty.MASTERY,
                        topology = CircuitTopology.COMPLEX_LOAD,
                        prompt = "An ideal transformer with turns ratio N₁:N₂ = ${ratio.first}:${ratio.second} feeds Z_L. Find reflected Z_in:",
                        parametersLine1 = "Turns Ratio N₁ : N₂ = ${ratio.first} : ${ratio.second}",
                        parametersLine2 = "Secondary Load Z_L = ($rL $xLStr) Ω",
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
                            "Z_in = ${ComplexNum.formatClean(aSq)} × ($rL $xLStr) Ω."
                        ),
                        workedSteps = listOf(
                            "1. Impedance scaling factor a² = (${ratio.first}/${ratio.second})² = ${ComplexNum.formatClean(aSq)}.",
                            "2. Z_in = ${ComplexNum(zInRe, zInIm).formatRect()} Ω."
                        ),
                        isProcedural = true
                    )
                }
            }
            ProblemCategory.RESONANCE -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural Resonance Q",
                        "Procedural Resonant Frequency ω₀",
                        "Procedural −3 dB Bandwidth",
                        "Procedural Resonant Voltage Gain"
                    ),
                    excludeTitle
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
                    val f0 = listOf(1000, 2000, 5000, 10000).random()
                    val q = listOf(5, 10, 20, 25, 50).random()
                    val bw = f0.toDouble() / q.toDouble()
                    CircuitProblem(
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
                        expectedReal = bw,
                        unit = "Hz",
                        hints = listOf(
                            "Half-power bandwidth BW = f₂ − f₁ = f₀ / Q.",
                            "Compute $f0 / $q."
                        ),
                        workedSteps = listOf(
                            "1. BW = f₀ / Q = $f0 / $q = ${ComplexNum.formatClean(bw)} Hz."
                        ),
                        isProcedural = true
                    )
                }
                else -> {
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
            }
            ProblemCategory.POWER -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural Power Factor",
                        "Procedural Complex Power S = V·I*",
                        "Procedural Max Power Transfer",
                        "Procedural 3-Phase Δ-to-Y"
                    ),
                    excludeTitle
                )
            ) {
                0 -> {
                    val triples = listOf(Triple(3, 4, 5), Triple(4, 3, 5), Triple(6, 8, 10), Triple(8, 6, 10), Triple(5, 12, 13))
                    val t = triples.random()
                    val scale = listOf(100, 200).random()
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
                    val vRms = listOf(100, 120, 200, 240).random()
                    val iRe = listOf(2, 3, 4, 5).random()
                    val iIm = listOf(-3, -2, -1, 1, 2).random()
                    val p = (vRms * iRe).toDouble()
                    val q = (vRms * (-iIm)).toDouble()
                    val iSign = if (iIm >= 0) "+ j$iIm" else "− j${-iIm}"
                    CircuitProblem(
                        id = drillId,
                        code = "Drill #$drillNum",
                        title = "Procedural Complex Power S = V·I*",
                        category = ProblemCategory.POWER,
                        difficulty = Difficulty.INTERMEDIATE,
                        topology = CircuitTopology.COMPLEX_LOAD,
                        prompt = "Compute complex power S = V_rms · I_rms* (in VA) for the RMS phasors:",
                        parametersLine1 = "V_rms = ($vRms + j0) V",
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
                            "I_rms* = $iRe ${if (-iIm >= 0) "+ j${-iIm}" else "− j$iIm"} A.",
                            "Multiply $vRms × I_rms*."
                        ),
                        workedSteps = listOf(
                            "1. Take complex conjugate: I_rms* = ($iRe ${if (-iIm >= 0) "+ j${-iIm}" else "− j$iIm"}) A.",
                            "2. S = $vRms · I_rms* = ${ComplexNum(p, q).formatRect()} VA."
                        ),
                        isProcedural = true
                    )
                }
                2 -> {
                    val pairs = listOf(Pair(40, 10), Pair(60, 5), Pair(100, 25), Pair(120, 20), Pair(200, 25))
                    val pick = pairs.random()
                    val vTh = pick.first
                    val rTh = pick.second
                    val xTh = listOf(4, 8, 12, 15).random()
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
                else -> {
                    val rY = listOf(3, 4, 5, 6, 8).random()
                    val xSign = if (Random.nextBoolean()) 1 else -1
                    val xY = listOf(2, 3, 4, 6).random() * xSign
                    val rD = 3 * rY
                    val xD = 3 * xY
                    val dStr = if (xD >= 0) "+ j$xD" else "− j${-xD}"
                    CircuitProblem(
                        id = drillId,
                        code = "Drill #$drillNum",
                        title = "Procedural 3-Phase Δ-to-Y",
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
                            "Divide real $rD and imaginary ${xD} by 3."
                        ),
                        workedSteps = listOf(
                            "1. Z_Y = Z_Δ / 3 = ($rD $dStr) / 3.",
                            "2. Z_Y = ${ComplexNum(rY.toDouble(), xY.toDouble()).formatRect()} Ω."
                        ),
                        isProcedural = true
                    )
                }
            }
            ProblemCategory.LAPLACE -> when (
                pickTemplateIndex(
                    listOf(
                        "Procedural Routh Margin",
                        "Procedural Final Value Theorem",
                        "Procedural Damping Ratio ζ",
                        "Procedural Initial Value Theorem"
                    ),
                    excludeTitle
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
                else -> {
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
            }
        }
    }
}
