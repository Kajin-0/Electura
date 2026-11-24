package com.example.data

import com.example.model.FieldGuideEntry

internal val fieldGuideEntriesCatalog: List<FieldGuideEntry> = listOf(
    FieldGuideEntry(
        id = "fg_imp",
        tag = "IMPEDANCE",
        title = "Phasor Reactance, Mesh & Δ↔Y Laws",
        formula = "Z_L = jωL  |  Z_C = −j/(ωC)  |  Z₁ = Z_ab·Z_ca / ΣZ_Δ",
        explanation = "Inductors lead voltage +90° and capacitors lag −90°. Coupled two-loop meshes share off-diagonal −Z_m, and complex Δ-to-Y reduction divides adjacent branch products by the Delta loop sum ΣZ_Δ."
    ),
    FieldGuideEntry(
        id = "fg_adm",
        tag = "ADMITTANCE & LCR METERS",
        title = "Admittance, Shunt Match & Two-Port [Y]",
        formula = "Y = 1/Z = G + jB   |   B_shunt = −B_L   |   A_v = −y₂₁/(y₂₂ + Y_L)",
        explanation = "Parallel susceptances add directly (B_C > 0, B_L < 0), letting a cancelling shunt cancel reactive load current. LCR meters link ESR and Q via R_p = R_s(1 + Q²), while two-port [Y] models compute loaded gain A_v."
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
        title = "Resonance, Q-Factor & Crystal Antiresonance",
        formula = "f₀ = √(f₁f₂)   |   Q_series = (1/R)√(L/C)   |   f_p = f_s√(1 + C_m/C₀)",
        explanation = "At ω₀ = 1/√(LC), reactances cancel completely and half-power edges obey geometric symmetry (f₁f₂ = f₀²). Quartz crystals also exhibit parallel antiresonance f_p above f_s due to shunt holder C₀."
    ),
    FieldGuideEntry(
        id = "fg_pwr",
        tag = "POWER TRIANGLE",
        title = "Complex Power, Two-Wattmeter & IEEE 1459",
        formula = "S = V·I* = P + jQ   |   Q = √3(W₁−W₂)   |   S² = P² + Q₁² + D²",
        explanation = "Active power P (W) performs work and reactive power Q (VAR) oscillates in fields. Two wattmeters directly measure 3-phase Q = √3(W₁−W₂), while IEEE 1459 isolates harmonic distortion power D."
    ),
    FieldGuideEntry(
        id = "fg_lap",
        tag = "LAPLACE & CONTROL",
        title = "Stability, Steady-State Error & Margins",
        formula = "e_ss = 1/(1+K_p) or 1/K_v   |   GM_dB = −20 log₁₀|G(jω_pc)|",
        explanation = "LHP poles decay while RHP poles diverge (Routh cubic: a₂a₁ > K). Closed-loop accuracy depends on system Type (K_p, K_v), and Gain/Phase margins measure Bode distance from −1 at ω_pc and ω_gc."
    ),
    FieldGuideEntry(
        id = "fg_ca",
        tag = "COMPLEX ANALYSIS",
        title = "Residues, z-Mapping & Unit-Circle Integrals",
        formula = "∮ f dz = j2πΣRes  |  z = e^(sT)  |  ∫₀²π dθ/(a+b cosθ) = 2π/√(a²−b²)",
        explanation = "Holomorphic maps satisfy Cauchy-Riemann, Nyquist counts encirclements via Z = P − N_CCW, unit-circle residues evaluate trig integrals, and z = e^(sT) maps LHP stability inside |z| < 1."
    ),
    FieldGuideEntry(
        id = "fg_ga",
        tag = "GEOMETRIC ALGEBRA",
        title = "Clifford Wedge Solver, 3D Rotors & Power Norm",
        formula = "ab = a·b + a∧b  |  x₁ = (c∧a₂)/(a₁∧a₂)  |  v' = R v R̃  |  ||M|| = ||v||·||i||",
        explanation = "Outer products (∧) solve 2D linear systems coordinate-free (Cramer's wedge rule). In Cl₃,₀, bivector plane rotors R = cos(θ/2)−sin(θ/2)B̂ rotate planes while leaving orthogonal axes invariant."
    )
)
