package com.example.data

import com.example.model.AnswerKind
import com.example.model.CircuitProblem
import com.example.model.CircuitTopology
import com.example.model.ComplexNum
import com.example.model.Difficulty
import com.example.model.ProblemCategory

internal val curatedAcCircuitProblems: List<CircuitProblem> = listOf(
    // IMPEDANCE (16)
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
        id = "p_101a",
        code = "No. 101A",
        title = "Series RL Impedance",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.FOUNDATIONS,
        topology = CircuitTopology.SERIES_RL,
        prompt = "Determine the total complex impedance Z = R + jX_L in Ohms where:",
        parametersLine1 = "R = 6Ω, L = 0.08H",
        parametersLine2 = "ω = 100 rad/s",
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
        id = "p_102b",
        code = "No. 102B",
        title = "Series-Aiding Coupled Inductors",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.FOUNDATIONS,
        topology = CircuitTopology.SERIES_RL,
        prompt = "Find the total equivalent inductance L_eq (in mH) of two mutually coupled coils wired series-aiding:",
        parametersLine1 = "L₁ = 40 mH,   L₂ = 60 mH",
        parametersLine2 = "Mutual M = 15 mH (series-aiding dots)",
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
        id = "p_104b",
        code = "No. 104B",
        title = "Series RC Reactance",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.FOUNDATIONS,
        topology = CircuitTopology.SERIES_RC,
        prompt = "Find the total complex impedance Z (in Ohms) for the RC branch:",
        parametersLine1 = "R = 40Ω, C = 250μF",
        parametersLine2 = "ω = 100 rad/s",
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
        id = "p_106b",
        code = "No. 106B",
        title = "Complex Current Divider",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.PARALLEL_RC,
        prompt = "Source current I_s splits between parallel branches Z₁ and Z₂. Find complex branch current I₁ (in A):",
        parametersLine1 = "I_s = 10∠0° A,   Z₁ = (3 + j4) Ω",
        parametersLine2 = "Z₂ = (5 − j4) Ω  (parallel with Z₁)",
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
        id = "p_108c",
        code = "No. 108C",
        title = "Parallel RC Equivalent",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.PARALLEL_RC,
        prompt = "Compute the equivalent parallel impedance Z_eq (in Ohms) of:",
        parametersLine1 = "R = 10Ω || Z_C = -j10Ω",
        parametersLine2 = "Y = 1/R + j/X_C",
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
        id = "p_124g",
        code = "No. 124G",
        title = "AC Thévenin Conjugate Parallel",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.PARALLEL_RC,
        prompt = "Find the open-circuit Thévenin impedance Z_th (in Ohms) of two parallel conjugate branches:",
        parametersLine1 = "Z₁ = (4 + j4) Ω   ||   Z₂ = (4 − j4) Ω",
        parametersLine2 = "Z_th = (Z₁ · Z₂) / (Z₁ + Z₂)",
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
        id = "p_115e",
        code = "No. 115E",
        title = "Complex Voltage Divider",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.SERIES_RL,
        prompt = "Find the complex voltage phasor V_L across the inductor in Volts:",
        parametersLine1 = "V_in = 20∠0° V, R = 3Ω",
        parametersLine2 = "Z_L = j4Ω (series with R)",
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
        id = "p_124f",
        code = "No. 124F",
        title = "Inverting Op-Amp Active AC Gain",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.FEEDBACK_LOOP,
        prompt = "An ideal inverting op-amp has input R_in and parallel R_f ∥ C_f feedback. Find complex gain A_v = −Z_f / R_in:",
        parametersLine1 = "R_in = 10 kΩ,   R_f = 20 kΩ ∥ C_f = 100 nF",
        parametersLine2 = "ω = 500 rad/s",
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
        id = "p_128h",
        code = "No. 128H",
        title = "Ideal Transformer Reflected Impedance",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.COMPLEX_LOAD,
        prompt = "An ideal transformer with turns ratio N₁:N₂ = 2:1 feeds load Z_L. Find primary input impedance Z_in:",
        parametersLine1 = "N₁ : N₂ = 2 : 1  (a = N₁/N₂ = 2)",
        parametersLine2 = "Z_L = (5 − j3) Ω",
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
        id = "p_140k",
        code = "No. 140K",
        title = "Two-Source AC Nodal Analysis",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.COMPLEX_LOAD,
        prompt = "Find node phasor voltage V_A driven by V₁ through R = 4Ω plus current source I_s entering node A:",
        parametersLine1 = "V₁ = 20∠0° V via R = 4Ω to A",
        parametersLine2 = "I_s = j5 A entering A; Z_L = j4Ω (A to GND)",
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
        id = "p_132i",
        code = "No. 132I",
        title = "Series-Opposing Coupled Inductors",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.SERIES_RL,
        prompt = "Two magnetically coupled coils (dots opposing) sit in series with R. Find Z at ω = 1000 rad/s:",
        parametersLine1 = "R = 30Ω, L₁ = 40mH, L₂ = 30mH",
        parametersLine2 = "M = 15mH (series opposing), ω = 1000 rad/s",
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
        id = "p_144h",
        code = "No. 144H",
        title = "AC Thévenin with Dependent Source",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.FEEDBACK_LOOP,
        prompt = "A test source V_t drives series R = 6Ω, Z_C = −j8Ω, and dependent drop V_d = 2V_C in loop KVL. Find Z_th = V_t/I_t:",
        parametersLine1 = "R = 6Ω,   Z_C = −j8Ω,   V_C = I_t·Z_C",
        parametersLine2 = "Loop KVL: V_t = I_t·R + V_C + 2V_C",
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
        id = "p_116f",
        code = "No. 116F",
        title = "AC Thévenin Open-Circuit Voltage",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.COMPLEX_LOAD,
        prompt = "Find the open-circuit AC Thévenin voltage V_th (in Volts) across the shunt capacitor branch:",
        parametersLine1 = "V_s = 20∠0° V,   Z₁ = R = 6 Ω",
        parametersLine2 = "Z₂ = −j8 Ω (open-circuit shunt C)",
        methodTitle = "THÉVENIN VOLTAGE DIVIDER",
        methodNote = "Apply the complex voltage divider V_th = V_s · Z₂ / (Z₁ + Z₂) with no load connected.",
        inputHint = "Enter V_th in V (A + jB or polar)",
        answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
        expectedComplex = ComplexNum(12.8, -9.6),
        unit = "V",
        hints = listOf(
            "V_th = 20 × (−j8) / (6 − j8) = −j160 / (6 − j8).",
            "Multiply numerator and denominator by (6 + j8); |6 − j8|² = 100."
        ),
        workedSteps = listOf(
            "1. Open-circuit divider: V_th = 20(−j8) / (6 − j8) = −j160 / (6 − j8).",
            "2. Rationalize: V_th = −j160(6 + j8) / (36 + 64) = (1280 − j960) / 100.",
            "3. V_th = 12.8 − j9.6 V (16∠−36.87° V)."
        )
    ),
    CircuitProblem(
        id = "p_146i",
        code = "No. 146I",
        title = "Coupled-Coil Reflected Impedance",
        category = ProblemCategory.IMPEDANCE,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.FEEDBACK_LOOP,
        prompt = "Two magnetically coupled loops have primary Z₁₁, secondary Z₂₂, and mutual reactance ωM. Find total primary input impedance Z_in:",
        parametersLine1 = "Z₁₁ = (4 + j10) Ω,   Z₂₂ = (3 + j4) Ω",
        parametersLine2 = "Mutual reactance ωM = 5 Ω  (Z_ref = (ωM)² / Z₂₂)",
        methodTitle = "REFLECTED IMPEDANCE",
        methodNote = "Compute the impedance reflected from the secondary loop Z_ref = (ωM)² / Z₂₂, then add Z₁₁.",
        inputHint = "Enter Z_in in Ω (A + jB)",
        answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
        expectedComplex = ComplexNum(7.0, 6.0),
        unit = "Ω",
        hints = listOf(
            "(ωM)² = 25 Ω², so reflected impedance is Z_ref = 25 / (3 + j4).",
            "25(3 − j4) / (9 + 16) = 3 − j4 Ω; add Z₁₁ = 4 + j10 Ω."
        ),
        workedSteps = listOf(
            "1. Secondary reflected impedance: Z_ref = (ωM)² / Z₂₂ = 25 / (3 + j4).",
            "2. Rationalize: Z_ref = 25(3 − j4) / 25 = 3 − j4 Ω (inductive loop reflects capacitive reactance!).",
            "3. Total input impedance: Z_in = (4 + j10) + (3 − j4) = 7 + j6 Ω."
        )
    ),

    // ADMITTANCE (12)
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
        id = "p_510d",
        code = "No. 510D",
        title = "LCR Meter Series Resistance",
        category = ProblemCategory.ADMITTANCE,
        difficulty = Difficulty.FOUNDATIONS,
        topology = CircuitTopology.SERIES_RL,
        prompt = "An LCR meter in Z-θ mode reads the following for a coil. Find series resistance R_s:",
        parametersLine1 = "|Z| = 50 Ω,  θ = +53.13°",
        parametersLine2 = "f_test = 159.15 Hz (ω = 1000 rad/s)",
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
        id = "p_507c",
        code = "No. 507C",
        title = "Inductive Susceptance B_L",
        category = ProblemCategory.ADMITTANCE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.PARALLEL_RLC,
        prompt = "Find the signed inductive susceptance B_L (in Siemens, where Y_L = jB_L) for:",
        parametersLine1 = "L = 25 mH (ideal coil)",
        parametersLine2 = "ω = 400 rad/s",
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
        id = "p_508c",
        code = "No. 508C",
        title = "Wien Bridge Zero-Phase Frequency",
        category = ProblemCategory.ADMITTANCE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.PARALLEL_RC,
        prompt = "Find the zero-phase angular null frequency ω₀ = 1/(RC) (in rad/s) of a balanced Wien RC bridge:",
        parametersLine1 = "R₁ = R₂ = 5 kΩ",
        parametersLine2 = "C₁ = C₂ = 200 nF  (series & parallel arms)",
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
        id = "p_514e",
        code = "No. 514E",
        title = "LCR Meter Inductance Extraction",
        category = ProblemCategory.ADMITTANCE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.SERIES_RL,
        prompt = "An LCR meter measures a coil's complex impedance in series mode. Find inductance L_s (in mH):",
        parametersLine1 = "Z = (12 + j45) Ω",
        parametersLine2 = "ω = 1000 rad/s",
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
        id = "p_529i",
        code = "No. 529I",
        title = "LCR Meter Parallel C_p Readout",
        category = ProblemCategory.ADMITTANCE,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.PARALLEL_RC,
        prompt = "An LCR meter in C_p–G parallel mode measures complex admittance Y at ω = 1000 rad/s. Find parallel capacitance C_p (in μF):",
        parametersLine1 = "Y = (0.02 + j0.05) S",
        parametersLine2 = "Test Frequency ω = 1000 rad/s",
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
        id = "p_528i",
        code = "No. 528I",
        title = "Capacitor Self-Resonant Frequency",
        category = ProblemCategory.ADMITTANCE,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.SERIES_RLC,
        prompt = "A capacitor has parasitic ESL = 40 nH in series with C = 10 nF and ESR = 0.05 Ω. Find ω_SRF (in Mrad/s):",
        parametersLine1 = "C = 10 nF,   ESL (L_s) = 40 nH",
        parametersLine2 = "ESR = 0.05 Ω  (series parasitic model)",
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
        id = "p_533j",
        code = "No. 533J",
        title = "Maxwell Inductance-Capacitance Bridge",
        category = ProblemCategory.ADMITTANCE,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.PARALLEL_RC,
        prompt = "A Maxwell AC bridge balances unknown series coil L_x against parallel R₁∥C₁. Find L_x (in mH):",
        parametersLine1 = "Arm 1: R₁ = 500Ω ∥ C₁ = 0.4 μF",
        parametersLine2 = "Bridge Arms: R₂ = 200Ω, R₃ = 1000Ω",
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
        id = "p_523g",
        code = "No. 523G",
        title = "LCR Series-to-Parallel L_p",
        category = ProblemCategory.ADMITTANCE,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.PARALLEL_RLC,
        prompt = "An LCR meter measures a low-Q coil in series mode (R_s, L_s). Convert to equivalent parallel inductance L_p (in mH):",
        parametersLine1 = "Series mode: R_s = 15 Ω,   L_s = 40 mH",
        parametersLine2 = "Quality factor Q = X_s / R_s = 2  (L_p = L_s(1 + 1/Q²))",
        methodTitle = "PARALLEL INDUCTANCE CONVERSION",
        methodNote = "Unlike high-Q coils where L_p ≈ L_s, finite Q scales parallel inductance by L_p = L_s(1 + 1/Q²).",
        inputHint = "Enter L_p in mH",
        answerKind = AnswerKind.REAL_SCALAR,
        expectedReal = 50.0,
        unit = "mH",
        hints = listOf(
            "Check Q² = 2² = 4, so 1 + 1/Q² = 1 + 0.25 = 1.25.",
            "Multiply L_p = 40 mH × 1.25."
        ),
        workedSteps = listOf(
            "1. Reactance factor: 1 + 1/Q² = 1 + 1/4 = 1.25.",
            "2. Equivalent parallel inductance: L_p = 40 × 1.25 = 50 mH."
        )
    ),
    CircuitProblem(
        id = "p_535k",
        code = "No. 535K",
        title = "Two-Port Y-Parameter Input Admittance",
        category = ProblemCategory.ADMITTANCE,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.FEEDBACK_LOOP,
        prompt = "A linear two-port network terminated in load admittance Y_L has [y] matrix parameters (in mS). Find input admittance Y_in:",
        parametersLine1 = "y₁₁ = 4 + j2 mS,   y₂₂ = 3 − j1 mS,   y₁₂ = −1 mS,   y₂₁ = 5 mS",
        parametersLine2 = "Load Y_L = 1 + j3 mS   (Y_in = y₁₁ − y₁₂y₂₁ / (y₂₂ + Y_L))",
        methodTitle = "TWO-PORT Y REDUCTION",
        methodNote = "Sum output admittance y₂₂ + Y_L, divide y₁₂·y₂₁ by (y₂₂ + Y_L), and subtract from y₁₁.",
        inputHint = "Enter Y_in in mS (e.g. 5 + j1.5)",
        answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
        expectedComplex = ComplexNum(5.0, 1.5),
        unit = "mS",
        hints = listOf(
            "y₂₂ + Y_L = (3 − j1) + (1 + j3) = 4 + j2 mS.",
            "y₁₂y₂₁ / (y₂₂ + Y_L) = −5 / (4 + j2) = −5(4 − j2) / 20 = −1 + j0.5 mS."
        ),
        workedSteps = listOf(
            "1. Combined port-2 admittance: y₂₂ + Y_L = (3 + 1) + j(−1 + 3) = 4 + j2 mS.",
            "2. Feedback term: y₁₂y₂₁ / (y₂₂ + Y_L) = −5 / (4 + j2) = −5(4 − j2)/20 = −1 + j0.5 mS.",
            "3. Y_in = (4 + j2) − (−1 + j0.5) = 5 + j1.5 mS."
        )
    ),

    // RESONANCE (10)
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
        id = "p_212d",
        code = "No. 212D",
        title = "Filter Selectivity Bandwidth",
        category = ProblemCategory.RESONANCE,
        difficulty = Difficulty.FOUNDATIONS,
        topology = CircuitTopology.SERIES_RLC,
        prompt = "Find the 3-dB passband width BW (in Hz) of a tuned resonant filter:",
        parametersLine1 = "f₀ = 1000 Hz",
        parametersLine2 = "Quality Factor Q = 25",
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
        id = "p_204b",
        code = "No. 204B",
        title = "Resonant Angular Frequency",
        category = ProblemCategory.RESONANCE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.SERIES_RLC,
        prompt = "Find the resonant angular frequency ω₀ (in rad/s) for the tank:",
        parametersLine1 = "L = 25mH, C = 10μF",
        parametersLine2 = "R = 5Ω (series)",
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
        id = "p_215e",
        code = "No. 215E",
        title = "Resonant Voltage Gain",
        category = ProblemCategory.RESONANCE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.SERIES_RLC,
        prompt = "Find the capacitor rms voltage magnitude |V_C| (in Volts) at series resonance:",
        parametersLine1 = "V_s = 12 V rms, R = 4Ω",
        parametersLine2 = "X_L = X_C = 60Ω at ω₀",
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
        id = "p_222g",
        code = "No. 222G",
        title = "Upper Half-Power Frequency ω₂",
        category = ProblemCategory.RESONANCE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.SERIES_RLC,
        prompt = "An RLC bandpass filter has ω₀ = 1200 rad/s and exact −3 dB bandwidth BW = 1000 rad/s. Find upper corner ω₂:",
        parametersLine1 = "ω₀ = 1200 rad/s,   BW = ω₂ − ω₁ = 1000 rad/s",
        parametersLine2 = "Exact geometric symmetry: ω₁ · ω₂ = ω₀²",
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
        id = "p_209c",
        code = "No. 209C",
        title = "Parallel Tank Bandwidth",
        category = ProblemCategory.RESONANCE,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.PARALLEL_RLC,
        prompt = "Determine the 3-dB half-power bandwidth BW (in rad/s) of the parallel RLC:",
        parametersLine1 = "R = 500Ω, L = 10mH",
        parametersLine2 = "C = 100nF (parallel tank)",
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
        id = "p_219f",
        code = "No. 219F",
        title = "Parallel Tank Q-Factor",
        category = ProblemCategory.RESONANCE,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.PARALLEL_RLC,
        prompt = "Compute the quality factor Q_p of a parallel RLC tank circuit:",
        parametersLine1 = "R = 2kΩ, L = 10mH",
        parametersLine2 = "C = 250nF (parallel)",
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
        id = "p_224g",
        code = "No. 224G",
        title = "Lossy Parallel Tank Resonance",
        category = ProblemCategory.RESONANCE,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.PARALLEL_RLC,
        prompt = "A practical coil (R_s = 5Ω, X_L = 15Ω) is tuned to unity pf by a lossless parallel C. Find tank Z_tank:",
        parametersLine1 = "Coil Branch: R_s = 5Ω, X_L = 15Ω",
        parametersLine2 = "Shunted by C for Parallel Resonance (B_net = 0)",
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
        id = "p_228h",
        code = "No. 228H",
        title = "Source-Loaded Tank Q_L",
        category = ProblemCategory.RESONANCE,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.PARALLEL_RLC,
        prompt = "A parallel tank (R_tank = 10 kΩ, unloaded Q₀ = 20) is driven by a source with R_s = 10 kΩ. Find loaded Q_L:",
        parametersLine1 = "R_tank = 10 kΩ (unloaded Q₀ = 20)",
        parametersLine2 = "Norton Source R_s = 10 kΩ across tank",
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
        id = "p_230i",
        code = "No. 230I",
        title = "L-Network Resonant Impedance Match",
        category = ProblemCategory.RESONANCE,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.PARALLEL_RLC,
        prompt = "Match R_s = 10Ω up to R_L = 50Ω using series +jX_L and shunt −jX_C across R_L. Find series X_L (in Ω):",
        parametersLine1 = "Source R_s = 10 Ω  →  Load R_L = 50 Ω",
        parametersLine2 = "Q = √(R_L / R_s − 1),   X_L = Q · R_s",
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
        id = "p_216e",
        code = "No. 216E",
        title = "Parallel Tank Current Magnification",
        category = ProblemCategory.RESONANCE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.PARALLEL_RLC,
        prompt = "A parallel RLC tank at resonance ω₀ is driven by current source I_s. Find the circulating inductor current magnitude |I_L| (in Amps):",
        parametersLine1 = "I_s = 2 A rms,   R = 600 Ω",
        parametersLine2 = "X_L = X_C = 40 Ω at resonance ω₀",
        methodTitle = "CURRENT MAGNIFICATION",
        methodNote = "At parallel resonance, reactive currents cancel at the source so V_tank = I_s·R, magnifying |I_L| = Q_p·I_s.",
        inputHint = "Enter |I_L| in Amps",
        answerKind = AnswerKind.REAL_SCALAR,
        expectedReal = 30.0,
        unit = "A",
        hints = listOf(
            "At ω₀, tank voltage is V_tank = I_s · R = 2 × 600 = 1200 V (or Q_p = R / X_L = 15).",
            "|I_L| = V_tank / X_L = 1200 / 40 = 30 A."
        ),
        workedSteps = listOf(
            "1. At resonance, I_C and I_L sum to zero, so all source current flows through R: V_tank = 2 × 600 = 1200 V.",
            "2. Circulating branch current |I_L| = V_tank / X_L = 1200 / 40 = 30 A (= Q_p · I_s)."
        )
    ),
    CircuitProblem(
        id = "p_232j",
        code = "No. 232J",
        title = "Critically Coupled Tuned Stages",
        category = ProblemCategory.RESONANCE,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.FEEDBACK_LOOP,
        prompt = "Two tuned resonant tank circuits (Q₁, Q₂) are magnetically coupled for maximally flat bandpass response. Find critical coupling k_c:",
        parametersLine1 = "Primary Tank Q₁ = 25,   Secondary Tank Q₂ = 16",
        parametersLine2 = "Critical coupling condition: k_c = 1 / √(Q₁ · Q₂)",
        methodTitle = "CRITICAL COUPLING",
        methodNote = "Critical coupling marks the transition before passband double-peaking occurs: k_c = 1 / √(Q₁Q₂).",
        inputHint = "Enter coupling factor k_c",
        answerKind = AnswerKind.REAL_SCALAR,
        expectedReal = 0.05,
        unit = "",
        hints = listOf(
            "Compute Q₁ · Q₂ = 25 × 16 = 400.",
            "Take the square root √400 = 20, then k_c = 1 / 20."
        ),
        workedSteps = listOf(
            "1. Geometric mean Q = √(25 × 16) = √400 = 20.",
            "2. Critical coupling coefficient k_c = 1 / 20 = 0.05."
        )
    )
)
