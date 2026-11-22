package com.example.data

import com.example.model.FieldGuideEntry

internal val fieldGuideEntriesCatalog: List<FieldGuideEntry> = listOf(
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
