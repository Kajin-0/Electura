package com.example.data

import com.example.model.AnswerKind
import com.example.model.CircuitProblem
import com.example.model.CircuitTopology
import com.example.model.ComplexNum
import com.example.model.Difficulty
import com.example.model.ProblemCategory
import kotlin.math.sqrt

internal val starterProblem492B: CircuitProblem = CircuitProblem(
    id = "p_492b",
    code = "No. 492B",
    title = "Series LCR Transient & Steady State",
    category = ProblemCategory.LAPLACE,
    difficulty = Difficulty.INTERMEDIATE,
    topology = CircuitTopology.SERIES_RLC,
    prompt = "Solve for the steady-state peak current phasor I (t ≫ τ, after transients decay) where:",
    parametersLine1 = "L = 0.5H, C = 0.02F, R = 10Ω",
    parametersLine2 = "E(t) = 100 sin(10t) V",
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
)

internal val curatedPowerLaplaceProblems: List<CircuitProblem> = listOf(
    // POWER (12)
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
        id = "p_302a",
        code = "No. 302A",
        title = "Multi-Frequency Harmonic RMS",
        category = ProblemCategory.POWER,
        difficulty = Difficulty.FOUNDATIONS,
        topology = CircuitTopology.COMPLEX_LOAD,
        prompt = "A branch carries DC plus two sinusoidal harmonics. Compute total effective RMS current I_rms (in Amps):",
        parametersLine1 = "i(t) = 3 + 4√2 cos(100t) + 12√2 sin(300t) A",
        parametersLine2 = "Orthogonal harmonics: I_rms = √(I₀² + I₁² + I₃²)",
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
        id = "p_314d",
        code = "No. 314D",
        title = "Complex Power Conjugate",
        category = ProblemCategory.POWER,
        difficulty = Difficulty.FOUNDATIONS,
        topology = CircuitTopology.COMPLEX_LOAD,
        prompt = "Compute complex power S = V · I* (in VA) from the rms phasors:",
        parametersLine1 = "V_rms = 100∠0° V",
        parametersLine2 = "I_rms = (4 − j3) A",
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
        id = "p_305b",
        code = "No. 305B",
        title = "Phasor Load Current",
        category = ProblemCategory.POWER,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.COMPLEX_LOAD,
        prompt = "Calculate the complex rms current phasor I (in Amps) drawn by load Z:",
        parametersLine1 = "V = 120∠0° V rms",
        parametersLine2 = "Z = (6 + j8) Ω",
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
        id = "p_312c",
        code = "No. 312C",
        title = "Balanced 3-Phase Line Voltage V_ab",
        category = ProblemCategory.POWER,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.COMPLEX_LOAD,
        prompt = "Given positive-sequence (a-b-c) phase voltage V_an, compute line-to-line phasor V_ab (in V rms):",
        parametersLine1 = "V_an = 200∠0° V rms  (positive sequence)",
        parametersLine2 = "V_ab = √3 · V_an ∠+30°",
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
        id = "p_318e",
        code = "No. 318E",
        title = "Power Factor Correction",
        category = ProblemCategory.POWER,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.PARALLEL_RC,
        prompt = "Find the parallel capacitance C (in μF) required to correct pf to unity (1.0):",
        parametersLine1 = "Load: P = 1.2kW, Q = 1.6kVAR lag",
        parametersLine2 = "V_rms = 200 V, ω = 100 rad/s",
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
        id = "p_326g",
        code = "No. 326G",
        title = "3-Phase Δ-to-Y Load Transform",
        category = ProblemCategory.POWER,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.COMPLEX_LOAD,
        prompt = "A balanced three-phase Δ-connected load has per-phase impedance Z_Δ. Find the equivalent Y impedance Z_Y:",
        parametersLine1 = "Z_Δ = (12 + j9) Ω per phase",
        parametersLine2 = "Balanced 3φ Δ → Y Equivalence",
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
        id = "p_310c",
        code = "No. 310C",
        title = "Parallel Load Summing",
        category = ProblemCategory.POWER,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.COMPLEX_LOAD,
        prompt = "Find total complex power S_total = P + jQ (in kVA) for two parallel loads:",
        parametersLine1 = "Load 1: 1.2kW @ 0.6 pf lag",
        parametersLine2 = "Load 2: 0.8kW @ 1.0 pf",
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
        id = "p_322f",
        code = "No. 322F",
        title = "AC Max Power Transfer",
        category = ProblemCategory.POWER,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.COMPLEX_LOAD,
        prompt = "Find maximum average power P_max (in Watts) delivered to a matched load Z_L = Z_th*:",
        parametersLine1 = "V_th = 20∠0° V rms",
        parametersLine2 = "Z_th = (4 + j3) Ω",
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
        id = "p_330h",
        code = "No. 330H",
        title = "Balanced 3-Phase Line Power",
        category = ProblemCategory.POWER,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.COMPLEX_LOAD,
        prompt = "Find total 3-phase active power P_3φ (in kW) absorbed by a balanced load:",
        parametersLine1 = "V_L = 400 V (RMS),   I_L = 20 A (RMS)",
        parametersLine2 = "Power Factor pf = cos(30°) = √3 / 2",
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
        id = "p_334j",
        code = "No. 334J",
        title = "AC Feeder Line Drop & Sending V_s",
        category = ProblemCategory.POWER,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.COMPLEX_LOAD,
        prompt = "A feeder with Z_line feeds a load at V_L = 200∠0° V drawing I = (8 − j6) A. Find sending-end V_s (in V):",
        parametersLine1 = "V_L = (200 + j0) V,   I = (8 − j6) A",
        parametersLine2 = "Z_line = (1 + j2) Ω",
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

    // LAPLACE (remaining 13; starter p_492b stays pinned at index 0 in ProblemBank)
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
        id = "p_402a",
        code = "No. 402A",
        title = "RC Filter Cutoff Gain",
        category = ProblemCategory.LAPLACE,
        difficulty = Difficulty.FOUNDATIONS,
        topology = CircuitTopology.LOW_PASS_RC,
        prompt = "Find the transfer magnitude |H(jω)| of an RC low-pass filter at cutoff:",
        parametersLine1 = "H(s) = 1 / (1 + sRC)",
        parametersLine2 = "Evaluate at ω = ω_c = 1/(RC)",
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
        id = "p_403a",
        code = "No. 403A",
        title = "First-Order RL Time Constant",
        category = ProblemCategory.LAPLACE,
        difficulty = Difficulty.FOUNDATIONS,
        topology = CircuitTopology.SERIES_RL,
        prompt = "Calculate the exponential current time constant τ = L / R (in milliseconds) for the RL branch:",
        parametersLine1 = "L = 50 mH,   R = 25 Ω",
        parametersLine2 = "τ = L / R  (natural response e^(−t/τ))",
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
        id = "p_496d",
        code = "No. 496D",
        title = "Laplace Final Value Theorem",
        category = ProblemCategory.LAPLACE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.FEEDBACK_LOOP,
        prompt = "Use the Final Value Theorem to find steady-state output y(∞) for:",
        parametersLine1 = "Y(s) = 12(s + 2) / (s(s + 3)(s + 4))",
        parametersLine2 = "y(∞) = lim(s→0) s·Y(s)",
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
        id = "p_497f",
        code = "No. 497F",
        title = "Bode Magnitude Gain in dB",
        category = ProblemCategory.LAPLACE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.LOW_PASS_RC,
        prompt = "A single-pole low-pass filter has straight-line Bode magnitude |H(jω)| = 0.1 at ω = 10ω_c. Find the gain in dB:",
        parametersLine1 = "|H(jω)| = 0.1  (one decade above ω_c)",
        parametersLine2 = "Gain_dB = 20 · log₁₀|H(jω)|",
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
        id = "p_499e",
        code = "No. 499E",
        title = "RC Step Transient Response",
        category = ProblemCategory.LAPLACE,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.SERIES_RC,
        prompt = "Uncharged C (v_C(0⁻) = 0 V) is energized at t = 0 by V_s = 100V through R. Find v_C(t) at t = τ:",
        parametersLine1 = "V_s = 100 V step, R = 10 kΩ, C = 50 μF",
        parametersLine2 = "Evaluate capacitor voltage at t = τ = RC = 0.5 s",
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
        id = "p_495c",
        code = "No. 495C",
        title = "Nyquist & Routh Stability",
        category = ProblemCategory.LAPLACE,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.FEEDBACK_LOOP,
        prompt = "Find the critical positive gain K for marginal closed-loop stability:",
        parametersLine1 = "G(s)H(s) = K / (s(s+1)(s+2))",
        parametersLine2 = "1 + G(s)H(s) = 0",
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
        id = "p_498e",
        code = "No. 498E",
        title = "Second-Order Damping Ratio",
        category = ProblemCategory.LAPLACE,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.SERIES_RLC,
        prompt = "Calculate the dimensionless damping ratio ζ of the series RLC network:",
        parametersLine1 = "R = 6Ω, L = 0.5H",
        parametersLine2 = "C = 0.02F (underdamped)",
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
        id = "p_497d",
        code = "No. 497D",
        title = "Critical Damping Resistance R_crit",
        category = ProblemCategory.LAPLACE,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.SERIES_RLC,
        prompt = "Find the series resistance R_crit (in Ohms) needed for critical damping (ζ = 1) in this RLC network:",
        parametersLine1 = "L = 25 mH,   C = 10 μF",
        parametersLine2 = "Critical damping condition: ζ = α / ω₀ = 1",
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
    )
)
