package com.example.data

import com.example.model.AnswerKind
import com.example.model.CircuitProblem
import com.example.model.CircuitTopology
import com.example.model.ComplexNum
import com.example.model.Difficulty
import com.example.model.ProblemCategory
import kotlin.math.sqrt

internal val curatedComplexGaProblems: List<CircuitProblem> = listOf(
    // COMPLEX ANALYSIS (22)
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
        id = "p_600c",
        code = "No. 600C",
        title = "Complex Plane 90° Operator j",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.FOUNDATIONS,
        topology = CircuitTopology.CONTOUR_SPLANE,
        prompt = "Multiplying by j = e^(jπ/2) rotates a phasor +90° CCW. Evaluate w = j · z in rectangular form:",
        parametersLine1 = "z = 3 + j4",
        parametersLine2 = "Apply w = j · (3 + j4) using j² = −1",
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
        id = "p_600e",
        code = "No. 600E",
        title = "Polar Phasor Product z₁ · z₂",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.FOUNDATIONS,
        topology = CircuitTopology.CONTOUR_SPLANE,
        prompt = "Multiply two complex phasors in polar form (multiply moduli, add angles) and give rectangular A + jB:",
        parametersLine1 = "z₁ = 3∠25°,   z₂ = 4∠65°",
        parametersLine2 = "z₁ · z₂ = (r₁ · r₂)∠(θ₁ + θ₂)",
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
        id = "p_601a",
        code = "No. 601A",
        title = "Cauchy-Riemann Analyticity",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.CONTOUR_SPLANE,
        prompt = "Determine the real constant k so that f(z) is entire (analytic everywhere in ℂ):",
        parametersLine1 = "f(z) = (x² − y²) + j(k·x·y)",
        parametersLine2 = "∂u/∂x = ∂v/∂y,  ∂u/∂y = −∂v/∂x",
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
        id = "p_602a",
        code = "No. 602A",
        title = "Complex Conjugate Roots in s-Plane",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.CONTOUR_SPLANE,
        prompt = "Find the upper-half s-plane pole s₁ (with Im(s₁) > 0) of the damped RLC polynomial:",
        parametersLine1 = "P(s) = s² + 4s + 13 = 0",
        parametersLine2 = "Solve s = (−b ± √(b² − 4ac)) / 2a",
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
        id = "p_604b",
        code = "No. 604B",
        title = "Möbius Smith Chart Reflection Γ",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.COMPLEX_LOAD,
        prompt = "Map load Z_L onto the unit disk via the Möbius transform Γ = (Z_L − Z₀)/(Z_L + Z₀):",
        parametersLine1 = "Z_L = (50 + j100) Ω",
        parametersLine2 = "Z₀ = 50 Ω (characteristic impedance)",
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
        id = "p_605b",
        code = "No. 605B",
        title = "Cauchy–Goursat Analytic Contour",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.CONTOUR_SPLANE,
        prompt = "Evaluate the closed CCW contour integral I = ∮ f(z) dz around the unit circle |z| = 1:",
        parametersLine1 = "f(z) = (z + 2) / (z − 5)",
        parametersLine2 = "Contour C: |z| = 1 (counterclockwise)",
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
        id = "p_606b",
        code = "No. 606B",
        title = "Simple Real-Pole Cover-Up Residue",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.CONTOUR_SPLANE,
        prompt = "Compute the residue Res[F(s), s = −3] using the cover-up limit lim(s→−3) (s + 3)·F(s):",
        parametersLine1 = "F(s) = 15 / (s · (s + 3))",
        parametersLine2 = "Simple pole at s₀ = −3",
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
        id = "p_607b",
        code = "No. 607B",
        title = "Cauchy Integral Formula (Simple Pole)",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.CONTOUR_SPLANE,
        prompt = "Evaluate the normalized integral I = (1/j2π) ∮ (e^(2z) / (z − 1)) dz CCW around |z| = 2:",
        parametersLine1 = "f(z) = e^(2z) / (z − 1),   g(z) = e^(2z)",
        parametersLine2 = "Closed CCW Contour C: |z| = 2",
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
        id = "p_608c",
        code = "No. 608C",
        title = "Complex Simple Pole Residue",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.CONTOUR_SPLANE,
        prompt = "Compute the complex residue Res[F(s), s = −1 + j2] for the rational function:",
        parametersLine1 = "F(s) = (s + 3) / ((s + 1)² + 4)",
        parametersLine2 = "Evaluate at simple pole s₀ = −1 + j2",
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
        id = "p_628h",
        code = "No. 628H",
        title = "Principal Value of j^j",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.CONTOUR_SPLANE,
        prompt = "Using the principal branch Log(z) = ln|z| + j·Arg(z), evaluate the principal value of j^j (to 3 decimals):",
        parametersLine1 = "z = j = e^(jπ/2)   (Arg(j) = π/2)",
        parametersLine2 = "j^j = exp(j · Log(j))",
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
        id = "p_640k",
        code = "No. 640K",
        title = "Bilinear s-to-z Filter Transform",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.CONTOUR_SPLANE,
        prompt = "Evaluate the Cayley/bilinear transform z = (1 + s)/(1 − s) along the jω-axis at s = j2:",
        parametersLine1 = "Bilinear Map: z = (1 + s) / (1 − s)",
        parametersLine2 = "Analog Frequency Point: s = j2",
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
        id = "p_609c",
        code = "No. 609C",
        title = "Harmonic Conjugate Construction",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.CONTOUR_SPLANE,
        prompt = "Given harmonic real part u(x, y) = x² − y² + 3x with v(0, 0) = 0, construct harmonic conjugate v(x, y) and evaluate v(2, 1):",
        parametersLine1 = "u(x, y) = x² − y² + 3x,   v(0, 0) = 0",
        parametersLine2 = "Cauchy–Riemann: ∂v/∂y = ∂u/∂x,   ∂v/∂x = −∂u/∂y",
        methodTitle = "CAUCHY–RIEMANN INTEGRATION",
        methodNote = "Integrate ∂v/∂y = ∂u/∂x with respect to y, then match ∂v/∂x = −∂u/∂y to fix the x-only function.",
        inputHint = "Enter scalar v(2, 1)",
        answerKind = AnswerKind.REAL_SCALAR,
        expectedReal = 7.0,
        unit = "",
        hints = listOf(
            "∂v/∂y = 2x + 3 ⇒ v(x, y) = 2xy + 3y + φ(x).",
            "Since ∂v/∂x = 2y + φ'(x) = −(−2y) = 2y, φ(x) = 0 ⇒ evaluate 2xy + 3y at (2, 1)."
        ),
        workedSteps = listOf(
            "1. Integrate ∂v/∂y = 2x + 3: v(x, y) = 2xy + 3y + φ(x).",
            "2. Match ∂v/∂x = 2y + φ'(x) = −∂u/∂y = 2y ⇒ φ'(x) = 0 and v(0, 0) = 0 gives φ = 0.",
            "3. v(2, 1) = 2(2)(1) + 3(1) = 7 (notice f(z) = z² + 3z)."
        )
    ),
    CircuitProblem(
        id = "p_610d",
        code = "No. 610D",
        title = "Inverse Smith Chart Möbius Map",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.CONTOUR_SPLANE,
        prompt = "A VNA measures complex reflection Γ on a Z₀ = 50 Ω line. Invert the Möbius map to recover load impedance Z_L (in Ω):",
        parametersLine1 = "Γ = 0.2 − j0.4,   Z₀ = 50 Ω",
        parametersLine2 = "Inverse Möbius: Z_L = Z₀ · (1 + Γ) / (1 − Γ)",
        methodTitle = "MÖBIUS INVERSION",
        methodNote = "Evaluate normalized impedance z_L = (1 + Γ)/(1 − Γ) via conjugate division, then multiply by Z₀.",
        inputHint = "Enter Z_L in Ω (R + jX)",
        answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
        expectedComplex = ComplexNum(50.0, -50.0),
        unit = "Ω",
        hints = listOf(
            "1 + Γ = 1.2 − j0.4 and 1 − Γ = 0.8 + j0.4 (factor ratio (3 − j1)/(2 + j1)).",
            "(3 − j1)(2 − j1) / (2² + 1²) = (5 − j5) / 5 = 1 − j1; multiply by 50 Ω."
        ),
        workedSteps = listOf(
            "1. z_L = (1.2 − j0.4) / (0.8 + j0.4) = (3 − j1) / (2 + j1).",
            "2. Multiply by (2 − j1): z_L = (6 − 1 − j5) / 5 = 1 − j1.",
            "3. Z_L = 50(1 − j1) = 50 − j50 Ω (70.71∠−45° Ω)."
        )
    ),
    CircuitProblem(
        id = "p_622f",
        code = "No. 622F",
        title = "s-Plane Pole-Zero Geometric Gain",
        category = ProblemCategory.COMPLEX_ANALYSIS,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.CONTOUR_SPLANE,
        prompt = "Compute Bode magnitude |H(j12)| via s-plane vector lengths from zeros z_i and poles p_k to s = j12:",
        parametersLine1 = "H(s) = 300(s + 5) / ((s + 9)(s + 16))",
        parametersLine2 = "Evaluate |H(jω)| = K · ∏|jω − z_i| / ∏|jω − p_k| at ω = 12",
        methodTitle = "POLE-ZERO VECTOR LENGTHS",
        methodNote = "Each factor (jω + a) forms a vector from −a to jω in the complex s-plane with length √(a² + ω²).",
        inputHint = "Enter magnitude |H(j12)|",
        answerKind = AnswerKind.REAL_SCALAR,
        expectedReal = 13.0,
        unit = "",
        hints = listOf(
            "Zero vector length |5 + j12| = 13.",
            "Pole vector lengths |9 + j12| = 15 and |16 + j12| = 20 ⇒ 300 × 13 / (15 × 20)."
        ),
        workedSteps = listOf(
            "1. Zero distance: |j12 − (−5)| = √(5² + 12²) = 13.",
            "2. Pole distances: |j12 − (−9)| = √(9² + 12²) = 15, |j12 − (−16)| = √(16² + 12²) = 20.",
            "3. |H(j12)| = 300 × 13 / (15 × 20) = 13."
        )
    ),

    // GEOMETRIC ALGEBRA (19)
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
        id = "p_701a",
        code = "No. 701A",
        title = "Cl₂,₀ Pseudoscalar Bivector Square",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.FOUNDATIONS,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "In Euclidean Geometric Algebra Cl₂,₀ (e₁² = e₂² = 1, e₁e₂ = −e₂e₁), compute (e₁₂)²:",
        parametersLine1 = "e₁₂ = e₁ ∧ e₂ = e₁e₂",
        parametersLine2 = "Evaluate scalar (e₁₂)² = e₁e₂e₁e₂",
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
        id = "p_702a",
        code = "No. 702A",
        title = "Right-Multiply by e₁₂ (+90° Turn)",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.FOUNDATIONS,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "Right-multiplying by e₁₂ rotates any Cl₂,₀ vector +90° (e₁e₁₂ = e₂, e₂e₁₂ = −e₁). Find e₁-part of v·e₁₂:",
        parametersLine1 = "v = 4e₁ − 3e₂",
        parametersLine2 = "v' = v · e₁₂ = 4(e₁e₁₂) − 3(e₂e₁₂)",
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
        id = "p_704b",
        code = "No. 704B",
        title = "Geometric Product a·b + a∧b",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "Compute the geometric product ab = (a·b) + (a∧b) in Cl₂,₀ (enter α + βe12 or α + jβ):",
        parametersLine1 = "a = 3e₁ + 4e₂",
        parametersLine2 = "b = 2e₁ + 5e₂",
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
        id = "p_705b",
        code = "No. 705B",
        title = "GA Vector Parallel Projection",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "Given unit voltage direction v̂ and current i in Cl₂,₀, find active projected magnitude ||i_∥|| = |i · v̂|:",
        parametersLine1 = "v̂ = 0.6e₁ + 0.8e₂   (||v̂|| = 1)",
        parametersLine2 = "i = 10e₁ A",
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
        id = "p_706b",
        code = "No. 706B",
        title = "Cl₂,₀ Reversion Norm ||M||",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "Compute the multivector magnitude ||M|| = √(M M̃) using reversion M̃ = α − βe₁₂ in Cl₂,₀:",
        parametersLine1 = "M = 5 + 12e₁₂",
        parametersLine2 = "Reversion M̃ = 5 − 12e₁₂   ((e₁₂)² = −1)",
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
        id = "p_707b",
        code = "No. 707B",
        title = "Half-Angle Rotor Construction",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "Build the Cl₂,₀ rotor R = cos(θ/2) − sin(θ/2)e₁₂ for a θ = +60° CCW rotation (enter α + βe12):",
        parametersLine1 = "Rotation Angle θ = +60° CCW",
        parametersLine2 = "R = cos(θ/2) − sin(θ/2)e₁₂   (unit norm R R̃ = 1)",
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
        id = "p_708c",
        code = "No. 708C",
        title = "Rotor Sandwich Rotation",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "Rotate v = 5e₁ by +90° CCW via v' = R v R̃ in Cl₂,₀. Find the e₂-component of v':",
        parametersLine1 = "v = 5e₁,  R = (1 − e₁₂)/√2",
        parametersLine2 = "R̃ = (1 + e₁₂)/√2  (reverse of R)",
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
        id = "p_710d",
        code = "No. 710D",
        title = "Cl₃,₀ Harmonic Active Power",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "In 3-harmonic Euclidean space Cl₃,₀ (e_j·e_k = δ_jk), compute total active power P = v · i:",
        parametersLine1 = "v = 10e₁ + 5e₂ + 4e₃ V",
        parametersLine2 = "i = 4e₁ + 2e₂ + 5e₃ A",
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
        id = "p_712d",
        code = "No. 712D",
        title = "2D Geometric Power Multivector",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "Compute the geometric power M = v·i + v∧i = P + B·e₁₂ (enter P + Be12 or P + jB):",
        parametersLine1 = "v = 10e₁  (voltage vector)",
        parametersLine2 = "i = 8e₁ − 6e₂  (current vector)",
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
        id = "p_732i",
        code = "No. 732I",
        title = "Cl₂,₀ Ohm's Law (I = V Z⁻¹)",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.MASTERY,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "Solve I = V·Z⁻¹ in Cl₂,₀ using geometric multivector inversion (enter A + Be12):",
        parametersLine1 = "V = 50 (scalar phase 0°),   Z = 6 + 8e₁₂ Ω",
        parametersLine2 = "Z⁻¹ = Z̃ / (Z Z̃)",
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
        id = "p_716e",
        code = "No. 716E",
        title = "3D Non-Sinusoidal Power Norm Cl₃,₀",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "In harmonic basis Cl₃,₀, find apparent geometric power ||M|| = √(P² + ||v∧i||²) for:",
        parametersLine1 = "v = 3e₁ + 4e₂ + 12e₃",
        parametersLine2 = "i = 2e₁ + 3e₂ + 6e₃",
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
        id = "p_724g",
        code = "No. 724G",
        title = "GA Fryze Orthogonal Current",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "Given unit voltage direction v̂ and line current i in Cl₂,₀, find the non-active orthogonal current magnitude ||i_⊥|| = ||i ∧ v̂||:",
        parametersLine1 = "v̂ = 0.6e₁ + 0.8e₂  (||v̂|| = 1)",
        parametersLine2 = "i = 10e₁ A",
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
        id = "p_736j",
        code = "No. 736J",
        title = "Cl₃,₀ Distortion Bivector Norm",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "In harmonic Cl₃,₀ space, find the non-active bivector norm ||v ∧ i|| (in VA) for:",
        parametersLine1 = "v = 4e₁ V",
        parametersLine2 = "i = 3e₁ + 4e₂ + 3e₃ A",
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
        id = "p_709c",
        code = "No. 709C",
        title = "Cl₂,₀ Line Reflection û v û",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.INTERMEDIATE,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "Reflect vector v = 25e₁ across unit axis û via sandwich v' = û v û in Cl₂,₀. Find the e₂-component of v':",
        parametersLine1 = "v = 25e₁,   û = 0.6e₁ + 0.8e₂  (||û|| = 1)",
        parametersLine2 = "Reflection law: v' = û v û = 2(v·û)û − v",
        methodTitle = "VERSOR REFLECTION",
        methodNote = "In Clifford algebra, reflecting a vector across line û reverses its orthogonal part while keeping its û-projection intact.",
        inputHint = "Enter e₂ coefficient",
        answerKind = AnswerKind.REAL_SCALAR,
        expectedReal = 24.0,
        unit = "",
        hints = listOf(
            "Inner projection v·û = 25 × 0.6 = 15.",
            "v' = 2(15)(0.6e₁ + 0.8e₂) − 25e₁ = −7e₁ + 24e₂."
        ),
        workedSteps = listOf(
            "1. Compute û v = (0.6e₁ + 0.8e₂)(25e₁) = 15 − 20e₁₂.",
            "2. Multiply on right by û: (15 − 20e₁₂)(0.6e₁ + 0.8e₂) = (9 − 16)e₁ + (12 + 12)e₂ = −7e₁ + 24e₂.",
            "3. Notice ||v'|| = √(7² + 24²) = 25, with e₂-component = 24."
        )
    ),
    CircuitProblem(
        id = "p_726h",
        code = "No. 726H",
        title = "Minimal Alignment Rotor R",
        category = ProblemCategory.GEOMETRIC_ALGEBRA,
        difficulty = Difficulty.ADVANCED,
        topology = CircuitTopology.GEOMETRIC_PLANE,
        prompt = "Build the minimal Cl₂,₀ rotor R = (1 + b̂ â) / ||1 + b̂ â|| that rotates â onto b̂ via b̂ = R â R̃ (enter α + βe12):",
        parametersLine1 = "â = e₁,   b̂ = 0.28e₁ + 0.96e₂  (||b̂|| = 1)",
        parametersLine2 = "b̂ â = 0.28 − 0.96e₁₂  ⇒  R = α + βe₁₂",
        methodTitle = "VECTOR-TO-VECTOR ROTOR",
        methodNote = "Bisect the angle between â and b̂ by normalizing 1 + b̂ â = (1 + b̂·â) + (b̂ ∧ â).",
        inputHint = "Enter R (e.g. 0.8 - 0.6e12)",
        answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
        expectedComplex = ComplexNum(0.8, -0.6),
        unit = "",
        hints = listOf(
            "1 + b̂ â = 1.28 − 0.96e₁₂.",
            "Normalize ||1.28 − 0.96e₁₂|| = √(1.28² + 0.96²) = 1.6, then divide."
        ),
        workedSteps = listOf(
            "1. Compute geometric product b̂ â = (0.28e₁ + 0.96e₂)e₁ = 0.28 − 0.96e₁₂.",
            "2. Bisector multivector: 1 + b̂ â = 1.28 − 0.96e₁₂, norm = √(1.6384 + 0.9216) = 1.6.",
            "3. Minimal unit rotor R = (1.28 − 0.96e₁₂) / 1.6 = 0.8 − 0.6e₁₂ (check R e₁ R̃ = 0.28e₁ + 0.96e₂)."
        )
    )
)
