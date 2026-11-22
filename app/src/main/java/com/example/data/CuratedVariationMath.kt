package com.example.data

import com.example.model.CircuitProblem
import com.example.model.ComplexNum
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

internal fun generateMathVariation(base: CircuitProblem): CircuitProblem? = when (base.id) {
    // ── COMPLEX ANALYSIS (22) ──
    "p_600a" -> {
        val (rVal, imVal, expN) = listOf(
            Triple(1, 1, 6),
            Triple(1, -1, 4),
            Triple(1, 1, 8),
            Triple(2, 2, 3),
            Triple(1, -1, 6),
            Triple(2, 0, 5).let { Triple(1, 1, 5) }
        ).random()
        val z = ComplexNum(rVal.toDouble(), imVal.toDouble())
        val res = ComplexNum.fromPolarDeg(z.magnitude.pow(expN.toDouble()), z.angleDeg * expN)
        wrapCuratedVariation(
            base = base,
            prompt = "Evaluate z^$expN in rectangular form (A + jB) using De Moivre's theorem:",
            parametersLine1 = "z = ${z.formatRect()}",
            parametersLine2 = "Compute z^$expN = r^$expN∠${expN}θ",
            methodNote = "Convert ${z.formatRect()} to polar form r∠θ, raise magnitude to power $expN, and multiply angle by $expN.",
            hints = listOf(
                "|z| = ${ComplexNum.formatClean(z.magnitude)} and arg(z) = ${ComplexNum.formatClean(z.angleDeg)}°.",
                "Magnitude |z^$expN| = ${ComplexNum.formatClean(res.magnitude)} at angle ${ComplexNum.formatClean(res.angleDeg)}°."
            ),
            workedSteps = listOf(
                "1. z = ${z.formatPolar()}.",
                "2. z^$expN = ${res.formatPolar()} = ${res.formatRect()}."
            ),
            expectedComplex = res
        )
    }

    "p_600b" -> {
        val (r, thetaLabel, deg) = listOf(
            Triple(6, "π/6", 30.0),
            Triple(4, "π/4", 45.0),
            Triple(8, "2π/3", 120.0),
            Triple(6, "−π/3", -60.0),
            Triple(10, "−π/6", -30.0),
            Triple(2, "3π/4", 135.0)
        ).random()
        val ans = ComplexNum.fromPolarDeg(r.toDouble(), deg)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "z = $r · e^(j$thetaLabel)",
            parametersLine2 = "Apply e^(jθ) = cos(θ) + j·sin(θ)",
            methodNote = "Evaluate $r·cos(${deg.toInt()}°) + j·$r·sin(${deg.toInt()}°).",
            hints = listOf(
                "$thetaLabel radians equals ${deg.toInt()}°.",
                "cos(${deg.toInt()}°) = ${ComplexNum.formatClean(cos(Math.toRadians(deg)))}, sin(${deg.toInt()}°) = ${ComplexNum.formatClean(sin(Math.toRadians(deg)))}."
            ),
            workedSteps = listOf(
                "1. Re(z) = $r cos(${deg.toInt()}°) = ${ComplexNum.formatClean(ans.re)}.",
                "2. Im(z) = $r sin(${deg.toInt()}°) = ${ComplexNum.formatClean(ans.im)} ⇒ z = ${ans.formatRect()}."
            ),
            expectedComplex = ans
        )
    }

    "p_600c" -> {
        val (a, b) = listOf(
            5 to 2, 2 to -6, -3 to 4, 6 to 8, 4 to -7, -5 to -2
        ).random()
        val w = ComplexNum(-b.toDouble(), a.toDouble())
        val zStr = ComplexNum(a.toDouble(), b.toDouble()).formatRect()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "z = $zStr",
            parametersLine2 = "Apply w = j · ($zStr) using j² = −1",
            methodNote = "Distribute j across ($zStr) and replace j² with −1.",
            hints = listOf(
                "j · ($zStr) = ${a}j + (${b})j².",
                "Substitute j² = −1 to get real part ${-b}."
            ),
            workedSteps = listOf(
                "1. w = j($zStr) = ${a}j − ($b) = ${w.formatRect()}."
            ),
            expectedComplex = w
        )
    }

    "p_600d" -> {
        val (k, invertSign) = listOf(
            2 to false, 3 to false, 4 to false, 1 to true, 2 to true, 5 to true
        ).random()
        val prefix = if (k == 1) "" else "$k"
        val ans = if (!invertSign) ComplexNum(0.0, k.toDouble()) else ComplexNum(0.0, -k.toDouble())
        val top = if (!invertSign) "(1 + j)" else "(1 − j)"
        val bot = if (!invertSign) "(1 − j)" else "(1 + j)"
        wrapCuratedVariation(
            base = base,
            prompt = "Simplify the complex quotient w = $prefix$top / $bot into rectangular form A + jB:",
            parametersLine1 = "w = $prefix$top / $bot",
            parametersLine2 = "Multiply numerator & denominator by conjugate $top",
            methodNote = "Multiply top and bottom by $top so (1 − j)(1 + j) = 2.",
            hints = listOf(
                "$top² = ${if (!invertSign) "+2j" else "−2j"}.",
                "Denominator (1 − j)(1 + j) = 2."
            ),
            workedSteps = listOf(
                "1. w = $prefix · $top² / 2 = ${ans.formatRect()} (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_600e" -> {
        val (r1, a1, r2, a2) = listOf(
            listOf(2, 20, 5, 25),
            listOf(3, 15, 4, 45),
            listOf(5, 40, 2, -10),
            listOf(4, 30, 3, 90),
            listOf(6, 50, 2, -50),
            listOf(3, 100, 4, 35)
        ).random()
        val ans = ComplexNum.fromPolarDeg((r1 * r2).toDouble(), (a1 + a2).toDouble())
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "z₁ = $r1∠$a1°,   z₂ = $r2∠$a2°",
            parametersLine2 = "z₁ · z₂ = (r₁ · r₂)∠(θ₁ + θ₂)",
            inputHint = "Enter product (${ans.formatPolar()} or rect)",
            hints = listOf(
                "Modulus = $r1 × $r2 = ${r1 * r2}.",
                "Angle = $a1° + ($a2°) = ${a1 + a2}°."
            ),
            workedSteps = listOf(
                "1. z₁·z₂ = ${r1 * r2}∠${a1 + a2}° = ${ans.formatRect()}."
            ),
            expectedComplex = ans
        )
    }

    "p_601a" -> {
        val a = listOf(2, 3, 4, 5, -2, -3).random()
        val kExpected = (2 * a).toDouble()
        val polyStr = "$a(x² − y²)"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "f(z) = $polyStr + j(k·x·y)",
            parametersLine2 = "∂u/∂x = ∂v/∂y,  ∂u/∂y = −∂v/∂x",
            hints = listOf(
                "Compute u_x = ∂($polyStr)/∂x = ${2 * a}x and v_y = kx.",
                "Setting ${2 * a}x = kx yields k = ${2 * a}."
            ),
            workedSteps = listOf(
                "1. u_x = ${2 * a}x, v_y = kx.",
                "2. Cauchy–Riemann u_x = v_y gives k = ${2 * a}."
            ),
            expectedReal = kExpected
        )
    }

    "p_602a" -> {
        val (alpha, wd) = listOf(
            1 to 4, 3 to 4, 2 to 5, 4 to 3, 5 to 2, 1 to 2
        ).random()
        val bCoeff = 2 * alpha
        val cCoeff = alpha * alpha + wd * wd
        val ans = ComplexNum(-alpha.toDouble(), wd.toDouble())
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "P(s) = s² + ${bCoeff}s + $cCoeff = 0",
            parametersLine2 = "Solve s = (−b ± √(b² − 4ac)) / 2a",
            methodNote = "Apply the quadratic formula or complete the square: (s + $alpha)² + ${wd * wd} = 0.",
            inputHint = "Enter s₁ (e.g. ${ans.formatRect()})",
            hints = listOf(
                "Discriminant b² − 4ac = ${bCoeff * bCoeff} − ${4 * cCoeff} = −${4 * wd * wd}.",
                "s = −$alpha ± j$wd; pick Im(s₁) > 0."
            ),
            workedSteps = listOf(
                "1. Complete square: (s + $alpha)² + $wd² = 0.",
                "2. Upper-half pole: s₁ = −$alpha + j$wd rad/s."
            ),
            expectedComplex = ans
        )
    }

    "p_603a" -> {
        val (k, p, w) = listOf(
            Triple(20, 10, 10),
            Triple(60, 30, 30),
            Triple(50, 30, 40),
            Triple(100, 40, 30),
            Triple(26, 5, 12),
            Triple(30, 15, 15)
        ).random()
        val den = (p * p + w * w).toDouble()
        val ans = ComplexNum(k * p / den, -k * w / den)
        wrapCuratedVariation(
            base = base,
            prompt = "Evaluate the analytic transfer function H(s) at the imaginary-axis point s = j$w:",
            parametersLine1 = "H(s) = $k / (s + $p)",
            parametersLine2 = "Substitute s = j$w and express in A + jB",
            inputHint = "Enter H(j$w) (A + jB)",
            hints = listOf(
                "H(j$w) = $k / ($p + j$w).",
                "Multiply numerator and denominator by ($p − j$w); |$p + j$w|² = ${den.toInt()}."
            ),
            workedSteps = listOf(
                "1. H(j$w) = $k($p − j$w) / ${den.toInt()} = ${ans.formatRect()} (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_604b" -> {
        val (z0, rL, xL) = listOf(
            Triple(50, 50, 50),
            Triple(50, 100, 50),
            Triple(50, 0, 50),
            Triple(75, 75, 150),
            Triple(100, 100, -100),
            Triple(50, 25, -25)
        ).random()
        // Gamma = ((rL - z0) + j*xL) / ((rL + z0) + j*xL)
        val nRe = (rL - z0).toDouble()
        val nIm = xL.toDouble()
        val dRe = (rL + z0).toDouble()
        val dIm = xL.toDouble()
        val den = dRe * dRe + dIm * dIm
        val ans = ComplexNum((nRe * dRe + nIm * dIm) / den, (nIm * dRe - nRe * dIm) / den)
        val zLStr = ComplexNum(rL.toDouble(), xL.toDouble()).formatRect()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Z_L = ($zLStr) Ω",
            parametersLine2 = "Z₀ = $z0 Ω (characteristic impedance)",
            hints = listOf(
                "Γ = (($zLStr) − $z0) / (($zLStr) + $z0) = ${ComplexNum(nRe, nIm).formatRect()} / ${ComplexNum(dRe, dIm).formatRect()}.",
                "Rationalize by multiplying by (${ComplexNum(dRe, -dIm).formatRect()})."
            ),
            workedSteps = listOf(
                "1. Γ = ${ComplexNum(nRe, nIm).formatRect()} / ${ComplexNum(dRe, dIm).formatRect()} = ${ans.formatRect()} (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_605b" -> {
        val (a, b, rCircle) = listOf(
            Triple(3, 4, 2),
            Triple(1, -6, 3),
            Triple(4, 7, 5),
            Triple(-2, 4, 1),
            Triple(5, -5, 2)
        ).random()
        val numFactor = if (a >= 0) "(z + $a)" else "(z − ${-a})"
        val poleFactor = if (b >= 0) "(z − $b)" else "(z + ${-b})"
        wrapCuratedVariation(
            base = base,
            prompt = "Evaluate the closed CCW contour integral I = ∮ f(z) dz around the circle |z| = $rCircle:",
            parametersLine1 = "f(z) = $numFactor / $poleFactor",
            parametersLine2 = "Contour C: |z| = $rCircle (counterclockwise)",
            methodNote = "Check whether any singularities lie inside or on the contour |z| = $rCircle before integrating.",
            hints = listOf(
                "The only pole is at z = $b, where |$b| = ${abs(b)} > $rCircle.",
                "Since f(z) is analytic inside and on |z| = $rCircle, apply Cauchy–Goursat."
            ),
            workedSteps = listOf(
                "1. Singularity z = $b lies strictly outside |z| = $rCircle.",
                "2. By Cauchy–Goursat's theorem, ∮_C f(z) dz = 0."
            ),
            expectedReal = 0.0
        )
    }

    "p_606b" -> {
        val (n, a) = listOf(
            12 to 4, 20 to 5, 18 to 6, 24 to 3, 30 to 5, 14 to 7
        ).random()
        val res = -(n.toDouble() / a)
        wrapCuratedVariation(
            base = base,
            prompt = "Compute the residue Res[F(s), s = −$a] using the cover-up limit lim(s→−$a) (s + $a)·F(s):",
            parametersLine1 = "F(s) = $n / (s · (s + $a))",
            parametersLine2 = "Simple pole at s₀ = −$a",
            methodNote = "Cancel the (s + $a) factor and plug s = −$a into the remaining fraction.",
            hints = listOf(
                "(s + $a)F(s) = $n / s.",
                "Evaluate $n / (−$a)."
            ),
            workedSteps = listOf(
                "1. Res[F(s), −$a] = $n / (−$a) = ${ComplexNum.formatClean(res)}."
            ),
            expectedReal = res
        )
    }

    "p_607b" -> {
        val (k, z0, rCircle) = listOf(
            Triple(1, 2, 3),
            Triple(3, 1, 2),
            Triple(1, 1, 2),
            Triple(-2, 1, 2),
            Triple(2, -1, 2),
            Triple(1, 3, 4)
        ).random()
        val valAns = exp((k * z0).toDouble())
        val poleFactor = if (z0 >= 0) "(z − $z0)" else "(z + ${-z0})"
        wrapCuratedVariation(
            base = base,
            prompt = "Evaluate the normalized integral I = (1/j2π) ∮ (e^(${k}z) / $poleFactor) dz CCW around |z| = $rCircle:",
            parametersLine1 = "f(z) = e^(${k}z) / $poleFactor,   g(z) = e^(${k}z)",
            parametersLine2 = "Closed CCW Contour C: |z| = $rCircle",
            methodNote = "Since z₀ = $z0 lies inside |z| = $rCircle, (1/j2π) ∮ g(z)/(z − z₀) dz = g(z₀).",
            inputHint = "Enter value (e.g. e^${k * z0} or ${ComplexNum.formatClean(valAns)})",
            hints = listOf(
                "|$z0| < $rCircle, so the simple pole z₀ = $z0 is enclosed.",
                "Evaluate g($z0) = e^($k · $z0) = e^(${k * z0})."
            ),
            workedSteps = listOf(
                "1. g($z0) = e^(${k * z0}) ≈ ${ComplexNum.formatClean(valAns)}."
            ),
            expectedReal = valAns
        )
    }

    "p_608c" -> {
        val (alpha, w, c) = listOf(
            Triple(1, 2, 5),
            Triple(2, 2, 6),
            Triple(1, 3, 7),
            Triple(2, 4, 2),
            Triple(3, 2, 7)
        ).random()
        // F(s) = (s + c) / ((s + alpha)^2 + w^2) at s0 = -alpha + j*w
        // Res = (-alpha + c + j*w) / (j*2*w) = (w + j*(alpha - c)) / (2*w)
        val re = 0.5
        val im = (alpha - c) / (2.0 * w)
        val ans = ComplexNum(re, im)
        wrapCuratedVariation(
            base = base,
            prompt = "Compute the complex residue Res[F(s), s = −$alpha + j$w] for the rational function:",
            parametersLine1 = "F(s) = (s + $c) / ((s + $alpha)² + ${w * w})",
            parametersLine2 = "Evaluate at simple pole s₀ = −$alpha + j$w",
            methodNote = "Factor ((s+$alpha)² + ${w * w}) = (s + $alpha − j$w)(s + $alpha + j$w) and apply cover-up at s₀.",
            hints = listOf(
                "Res = (s₀ + $c) / (s₀ + $alpha + j$w) = (${c - alpha} + j$w) / (j${2 * w}).",
                "Divide (${c - alpha} + j$w) by j${2 * w}."
            ),
            workedSteps = listOf(
                "1. Cover-up at s₀ = −$alpha + j$w: (${c - alpha} + j$w) / (j${2 * w}).",
                "2. = ${ans.formatRect()}."
            ),
            expectedComplex = ans
        )
    }

    "p_612d" -> {
        val (c, a) = listOf(
            4 to 2, 9 to 3, 1 to 1, 2 to 2, 3 to 3, 8 to 2
        ).random()
        // F(s) = (s^2 + c) / (s * (s + a)^2) -> g(s) = s + c/s -> g'(-a) = 1 - c/a^2
        val res = 1.0 - c.toDouble() / (a * a)
        wrapCuratedVariation(
            base = base,
            prompt = "Evaluate the residue at the double pole s = −$a of F(s):",
            parametersLine1 = "F(s) = (s² + $c) / (s(s + $a)²)",
            parametersLine2 = "Order m = 2 pole at s = −$a",
            methodNote = "Use Res[F, −$a] = lim(s→−$a) d/ds [ (s + $a)² · F(s) ].",
            hints = listOf(
                "Differentiate g(s) = (s² + $c)/s = s + $c/s.",
                "g'(s) = 1 − $c/s²; evaluate at s = −$a."
            ),
            workedSteps = listOf(
                "1. g'(s) = 1 − $c/s².",
                "2. Res[F, −$a] = 1 − $c/${a * a} = ${ComplexNum.formatClean(res)}."
            ),
            expectedReal = res
        )
    }

    "p_616e" -> {
        val (c, p1, p2, rC) = listOf(
            listOf(8, 1, 4, 2),
            listOf(6, 1, 3, 2),
            listOf(10, 1, 5, 3),
            listOf(12, 2, 6, 3),
            listOf(9, 1, 3, 2),
            listOf(4, 1, 4, 2)
        ).filter { !(it[0] == 6 && it[1] == 1 && it[2] == 3) }.random()
        val res0 = c.toDouble() / (p1 * p2)
        val res1 = (p1 * p1 + c).toDouble() / (p1 * (p1 - p2))
        val ans = res0 + res1
        wrapCuratedVariation(
            base = base,
            prompt = "Evaluate the normalized counterclockwise contour integral around |z| = $rC:",
            parametersLine1 = "I = (1 / j2π) ∮ f(z) dz  along |z| = $rC",
            parametersLine2 = "f(z) = (z² + $c) / (z(z − $p1)(z − $p2))",
            methodNote = "Sum only residues of poles lying strictly inside the circle |z| = $rC.",
            hints = listOf(
                "Enclosed poles inside |z| = $rC are z = 0 and z = $p1 (z = $p2 is outside).",
                "Res(0) = ${ComplexNum.formatClean(res0)}; Res($p1) = ${ComplexNum.formatClean(res1)}."
            ),
            workedSteps = listOf(
                "1. Res[f, 0] = $c / ((−$p1)(−$p2)) = ${ComplexNum.formatClean(res0)}.",
                "2. Res[f, $p1] = (${p1 * p1} + $c) / ($p1 · ($p1 − $p2)) = ${ComplexNum.formatClean(res1)}.",
                "3. Integral = Res(0) + Res($p1) = ${ComplexNum.formatClean(ans)}."
            ),
            expectedReal = ans
        )
    }

    "p_620f" -> {
        val (a, w1, w2, tDivLabel, tVal) = listOf(
            listOf(6, 1, 2, "π/2", Math.PI / 2.0),
            listOf(8, 1, 3, "π/2", Math.PI / 2.0),
            listOf(5, 2, 3, "π/3", Math.PI / 3.0),
            listOf(6, 1, 2, "π/3", Math.PI / 3.0),
            listOf(8, 1, 3, "π/4", Math.PI / 4.0)
        ).random()
        val amp = (a as Int).toDouble() / (((w2 as Int) * w2) - ((w1 as Int) * w1))
        val ans = amp * (cos(w1 * (tVal as Double)) - cos(w2 * tVal))
        wrapCuratedVariation(
            base = base,
            prompt = "Evaluate f(t) = (1/j2π) ∫ F(s)e^(st) ds via Bromwich residues at t = $tDivLabel s:",
            parametersLine1 = "F(s) = ${a}s / ((s² + ${w1 * w1})(s² + ${w2 * w2}))",
            parametersLine2 = "Evaluate f(t) at t = $tDivLabel seconds",
            inputHint = "Enter f($tDivLabel)",
            hints = listOf(
                "Residue sum yields f(t) = ${ComplexNum.formatClean(amp)}(cos(${w1}t) − cos(${w2}t)).",
                "Substitute t = $tDivLabel into ${ComplexNum.formatClean(amp)}(cos(${w1}t) − cos(${w2}t))."
            ),
            workedSteps = listOf(
                "1. Poles at ±j$w1 and ±j$w2 give f(t) = ${ComplexNum.formatClean(amp)}(cos(${w1}t) − cos(${w2}t)).",
                "2. At t = $tDivLabel: f(t) = ${ComplexNum.formatClean(ans)}."
            ),
            expectedReal = ans
        )
    }

    "p_624g" -> {
        val p = listOf(2, 3, 4, 5).random()
        val nCcw = listOf(0, 1, 2, 3, 4).filter { it <= p && !(p == 3 && it == 2) }.random()
        val zAns = (p - nCcw).toDouble()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Open-loop RHP poles: P = $p",
            parametersLine2 = "Net CCW encirclements of (−1 + j0): N_CCW = $nCcw",
            hints = listOf(
                "Apply Z = P − N_CCW directly.",
                "Z = $p − $nCcw."
            ),
            workedSteps = listOf(
                "1. Z = P − N_CCW = $p − $nCcw = ${zAns.toInt()}."
            ),
            expectedReal = zAns
        )
    }

    "p_628h" -> {
        val (kNum, kStr) = listOf(
            0.5 to "0.5",
            2.0 to "2",
            -1.0 to "−1",
            1.5 to "1.5"
        ).random()
        val ans = exp(-kNum * Math.PI / 2.0)
        val powLabel = if (kNum == -1.0) "−j" else if (kNum == 1.0) "j" else "j·($kStr)"
        wrapCuratedVariation(
            base = base,
            prompt = "Using the principal branch Log(z) = ln|z| + j·Arg(z), evaluate the principal value of j^($powLabel):",
            parametersLine1 = "z = j = e^(jπ/2)   (Arg(j) = π/2)",
            parametersLine2 = "j^($powLabel) = exp(($powLabel) · Log(j))",
            methodNote = "Substitute Log(j) = jπ/2 into exp(($powLabel) · Log(j)).",
            inputHint = "Enter decimal (e.g. ${ComplexNum.formatClean(ans)})",
            hints = listOf(
                "Log(j) = jπ/2.",
                "($powLabel) · (jπ/2) = ${ComplexNum.formatClean(-kNum * Math.PI / 2.0)}."
            ),
            workedSteps = listOf(
                "1. exp(($powLabel)·(jπ/2)) = exp(${ComplexNum.formatClean(-kNum * Math.PI / 2.0)}) ≈ ${ComplexNum.formatClean(ans)}."
            ),
            expectedReal = ans
        )
    }

    "p_632i" -> {
        val a = listOf(2, 3, -1, -2, 6).random()
        val res = -(a * a * a) / 6.0
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "f(z) = z² · sin($a/z)",
            parametersLine2 = "Expand sin(w) = w − w³/3! + w⁵/5! − … at w = $a/z",
            methodNote = "Expand z²·sin($a/z) in powers of z and extract the a₋₁ coefficient of z⁻¹.",
            inputHint = "Enter residue (e.g. ${ComplexNum.formatClean(res)})",
            hints = listOf(
                "sin($a/z) = $a/z − ($a)³/(6z³) + …",
                "Multiply by z²: z⁻¹ coefficient is −($a)³/6."
            ),
            workedSteps = listOf(
                "1. f(z) = ${a}z − ${a * a * a}/(6z) + …",
                "2. Residue a₋₁ = −(${a}³)/6 = ${ComplexNum.formatClean(res)}."
            ),
            expectedReal = res
        )
    }

    "p_636j" -> {
        val a = listOf(1, 3, 4, 5, 10).random()
        val ans = 1.0 / a
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "I = (1/π) ∫₋∞^∞ dx / (x² + ${a * a})",
            parametersLine2 = "Upper half-plane pole at z = +j$a",
            methodNote = "Compute Res[1/(z²+${a * a}), +j$a], multiply by 2πj, then divide by π.",
            hints = listOf(
                "Only z = +j$a lies in the UHP; Res = 1/(j2·$a).",
                "2πj · (1 / (j${2 * a})) / π = 1 / $a."
            ),
            workedSteps = listOf(
                "1. I = (1/π) · 2πj · Res[1/(z²+${a * a}), j$a] = 1/$a = ${ComplexNum.formatClean(ans)}."
            ),
            expectedReal = ans
        )
    }

    "p_640k" -> {
        val w = listOf(1, 3, 4, -2, -1).random()
        val den = (1 + w * w).toDouble()
        val ans = ComplexNum((1.0 - w * w) / den, (2.0 * w) / den)
        val sLabel = if (w >= 0) "j$w" else "−j${-w}"
        val numLabel = if (w >= 0) "(1 + j$w)" else "(1 − j${-w})"
        val denLabel = if (w >= 0) "(1 − j$w)" else "(1 + j${-w})"
        wrapCuratedVariation(
            base = base,
            prompt = "Evaluate the Cayley/bilinear transform z = (1 + s)/(1 − s) along the jω-axis at s = $sLabel:",
            parametersLine1 = "Bilinear Map: z = (1 + s) / (1 − s)",
            parametersLine2 = "Analog Frequency Point: s = $sLabel",
            hints = listOf(
                "z = $numLabel / $denLabel = $numLabel² / (1² + ${w * w}).",
                "Notice |z| = 1 on the unit circle."
            ),
            workedSteps = listOf(
                "1. z = ((1 − ${w * w}) + j(${2 * w})) / ${den.toInt()} = ${ans.formatRect()}."
            ),
            expectedComplex = ans
        )
    }

    "p_644l" -> {
        val (a, nOrder) = listOf(
            2 to 2, 4 to 2, 5 to 2, 2 to 3, 3 to 3, 6 to 2
        ).random()
        val fact = if (nOrder == 2) 2.0 else 6.0
        val ans = a.toDouble().pow(nOrder) / fact
        wrapCuratedVariation(
            base = base,
            prompt = "Evaluate the normalized integral (1/j2π) ∮ (e^(${a}z) / z^${nOrder + 1}) dz positively around |z| = 2:",
            parametersLine1 = "f(z) = e^(${a}z) / z^${nOrder + 1}",
            parametersLine2 = "Closed CCW Contour C: |z| = 2",
            methodNote = "Apply (1/j2π) ∮ g(z)/z^(n+1) dz = g^(n)(0) / n! with g(z) = e^(${a}z) and n = $nOrder.",
            hints = listOf(
                "Order is n + 1 = ${nOrder + 1} ⇒ n = $nOrder.",
                "g^($nOrder)(0) = $a^$nOrder = ${a.toDouble().pow(nOrder).toInt()}; divide by $nOrder! = ${fact.toInt()}."
            ),
            workedSteps = listOf(
                "1. g^($nOrder)(0) / $nOrder! = ${a.toDouble().pow(nOrder).toInt()} / ${fact.toInt()} = ${ComplexNum.formatClean(ans)}."
            ),
            expectedReal = ans
        )
    }

    // ── GEOMETRIC ALGEBRA (19) ──
    "p_700a" -> {
        val (v1, v2, i1, i2) = listOf(
            listOf(8, 3, 5, 2),
            listOf(5, 4, 6, 3),
            listOf(7, 2, 4, 5),
            listOf(9, -2, 3, 4),
            listOf(10, 3, 2, 6)
        ).random()
        val p = (v1 * i1 + v2 * i2).toDouble()
        val v2Str = if (v2 >= 0) "+ ${v2}e₂" else "− ${-v2}e₂"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v = ${v1}e₁ $v2Str V",
            parametersLine2 = "i = ${i1}e₁ + ${i2}e₂ A",
            hints = listOf("Multiply matching components: ($v1 × $i1) + ($v2 × $i2)."),
            workedSteps = listOf("1. P = ${v1 * i1} + ${v2 * i2} = ${p.toInt()} W."),
            expectedReal = p
        )
    }

    "p_700b" -> {
        val (a1, a2, b1, b2) = listOf(
            listOf(4, 1, 2, 6),
            listOf(6, 3, 2, 5),
            listOf(3, 5, 4, 2),
            listOf(7, 2, 1, 4),
            listOf(4, -2, 3, 5)
        ).random()
        val beta = (a1 * b2 - a2 * b1).toDouble()
        val a2Str = if (a2 >= 0) "+ ${a2}e₂" else "− ${-a2}e₂"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "a = ${a1}e₁ $a2Str",
            parametersLine2 = "b = ${b1}e₁ + ${b2}e₂   (e₁∧e₁ = 0, e₂∧e₁ = −e₁₂)",
            hints = listOf("Evaluate determinant β = ($a1 × $b2) − ($a2 × $b1)."),
            workedSteps = listOf("1. a ∧ b = (${a1 * b2} − ${a2 * b1})e₁₂ = ${beta.toInt()}e₁₂."),
            expectedReal = beta
        )
    }

    "p_701a" -> {
        val k = listOf(2, 3, 4, 5).random()
        val ans = -(k * k).toDouble()
        wrapCuratedVariation(
            base = base,
            prompt = "In Euclidean Geometric Algebra Cl₂,₀ (e₁² = e₂² = 1, e₁e₂ = −e₂e₁), compute B² = (${k}e₁₂)²:",
            parametersLine1 = "B = ${k}e₁₂ = ${k}(e₁ ∧ e₂)",
            parametersLine2 = "Evaluate scalar B² = ${k}² (e₁₂)²",
            hints = listOf(
                "Since e₂e₁ = −e₁e₂, (e₁₂)² = −1.",
                "Multiply by ${k}² = ${k * k}."
            ),
            workedSteps = listOf(
                "1. B² = ${k * k}(e₁₂)² = ${k * k}(−1) = ${ans.toInt()}."
            ),
            expectedReal = ans
        )
    }

    "p_702a" -> {
        val (a, b) = listOf(
            5 to -4, 6 to -2, 3 to -7, 7 to 4, 8 to -5, 2 to 6
        ).random()
        val bStr = if (b >= 0) "+ ${b}e₂" else "− ${-b}e₂"
        val e1Comp = (-b).toDouble()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v = ${a}e₁ $bStr",
            parametersLine2 = "v' = v · e₁₂ = $a(e₁e₁₂) + ($b)(e₂e₁₂)",
            hints = listOf(
                "e₁e₁₂ = e₂ and e₂e₁₂ = −e₁.",
                "v · e₁₂ = ${a}e₂ + ($b)(−e₁) ⇒ e₁-component is ${-b}."
            ),
            workedSteps = listOf(
                "1. v·e₁₂ = (${-b})e₁ + ${a}e₂ ⇒ e₁ coefficient = ${-b}."
            ),
            expectedReal = e1Comp
        )
    }

    "p_703a" -> {
        val (r, xMag, w) = listOf(
            Triple(6, 8, 100),
            Triple(12, 5, 100),
            Triple(10, 10, 200),
            Triple(15, 20, 500),
            Triple(9, 12, 100)
        ).random()
        val lH = xMag.toDouble() / w
        val ans = ComplexNum(r.toDouble(), xMag.toDouble())
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Series RL: R = ${r}Ω, L = ${ComplexNum.formatClean(lH)} H",
            parametersLine2 = "ω = $w rad/s   (bivector e₁₂ plays role of +j)",
            hints = listOf(
                "X_L = ωL = $w × ${ComplexNum.formatClean(lH)} = $xMag Ω.",
                "Write Z = $r + ${xMag}e₁₂."
            ),
            workedSteps = listOf(
                "1. X_L = $xMag Ω ⇒ Z = ${ans.formatBivector()} Ω."
            ),
            expectedComplex = ans
        )
    }

    "p_704b" -> {
        val (a1, a2, b1, b2) = listOf(
            listOf(4, 3, 2, 5),
            listOf(5, 2, 3, 4),
            listOf(2, 6, 3, 1),
            listOf(4, -3, 2, 5),
            listOf(6, 2, 1, 3)
        ).random()
        val dot = (a1 * b1 + a2 * b2).toDouble()
        val wedge = (a1 * b2 - a2 * b1).toDouble()
        val ans = ComplexNum(dot, wedge)
        val aStr = if (a2 >= 0) "${a1}e₁ + ${a2}e₂" else "${a1}e₁ − ${-a2}e₂"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "a = $aStr",
            parametersLine2 = "b = ${b1}e₁ + ${b2}e₂",
            hints = listOf(
                "a·b = $a1($b1) + ($a2)($b2) = ${dot.toInt()}.",
                "a∧b = ($a1·$b2 − ($a2)·$b1)e₁₂ = ${wedge.toInt()}e₁₂."
            ),
            workedSteps = listOf(
                "1. a·b = ${dot.toInt()}, a∧b = ${wedge.toInt()}e₁₂.",
                "2. ab = ${ans.formatBivector()}."
            ),
            expectedComplex = ans
        )
    }

    "p_705b" -> {
        val (u1, u2, iAmp) = listOf(
            Triple(0.8, 0.6, 10),
            Triple(0.6, 0.8, 15),
            Triple(0.8, 0.6, 20),
            Triple(0.6, 0.8, 25),
            Triple(0.8, 0.6, 30)
        ).random()
        val proj = iAmp * u1
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v̂ = ${u1}e₁ + ${u2}e₂   (||v̂|| = 1)",
            parametersLine2 = "i = ${iAmp}e₁ A",
            hints = listOf("i · v̂ = $iAmp × $u1."),
            workedSteps = listOf("1. ||i_∥|| = |$iAmp × $u1| = ${ComplexNum.formatClean(proj)} A."),
            expectedReal = proj
        )
    }

    "p_706b" -> {
        val (a, b, norm) = listOf(
            Triple(3, 4, 5),
            Triple(6, 8, 10),
            Triple(8, 15, 17),
            Triple(7, 24, 25),
            Triple(9, 12, 15),
            Triple(9, 40, 41)
        ).random()
        val sign = if (Random.nextBoolean()) 1 else -1
        val bSigned = b * sign
        val bSignStr = if (bSigned >= 0) "+ ${b}e₁₂" else "− ${b}e₁₂"
        val revStr = if (bSigned >= 0) "− ${b}e₁₂" else "+ ${b}e₁₂"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "M = $a $bSignStr",
            parametersLine2 = "Reversion M̃ = $a $revStr   ((e₁₂)² = −1)",
            hints = listOf(
                "M M̃ = $a² + $b² = ${norm * norm}.",
                "||M|| = √${norm * norm}."
            ),
            workedSteps = listOf("1. ||M|| = √($a² + $b²) = $norm."),
            expectedReal = norm.toDouble()
        )
    }

    "p_707b" -> {
        val thetaDeg = listOf(90.0, 120.0, 180.0, -60.0, -90.0, 30.0).random()
        val halfRad = Math.toRadians(thetaDeg / 2.0)
        val ans = ComplexNum(cos(halfRad), -sin(halfRad))
        wrapCuratedVariation(
            base = base,
            prompt = "Build the Cl₂,₀ rotor R = cos(θ/2) − sin(θ/2)e₁₂ for a θ = ${ComplexNum.formatClean(thetaDeg)}° CCW rotation (enter α + βe12):",
            parametersLine1 = "Rotation Angle θ = ${ComplexNum.formatClean(thetaDeg)}° CCW",
            parametersLine2 = "R = cos(θ/2) − sin(θ/2)e₁₂   (unit norm R R̃ = 1)",
            methodNote = "Halve the angle to θ/2 = ${ComplexNum.formatClean(thetaDeg / 2.0)}° and evaluate cos(θ/2) − sin(θ/2)e₁₂.",
            inputHint = "Enter R (e.g. ${ans.formatBivector()})",
            hints = listOf(
                "θ/2 = ${ComplexNum.formatClean(thetaDeg / 2.0)}°.",
                "Remember the minus sign before sin(θ/2)e₁₂."
            ),
            workedSteps = listOf(
                "1. R = cos(${ComplexNum.formatClean(thetaDeg / 2.0)}°) − sin(${ComplexNum.formatClean(thetaDeg / 2.0)}°)e₁₂ = ${ans.formatBivector()}."
            ),
            expectedComplex = ans
        )
    }

    "p_708c" -> {
        val vMag = listOf(3, 4, 6, 8, 10, 12).random()
        val ccw90 = Random.nextBoolean()
        val rotLabel = if (ccw90) "+90° CCW" else "−90° CW"
        val rStr = if (ccw90) "(1 − e₁₂)/√2" else "(1 + e₁₂)/√2"
        val rTildeStr = if (ccw90) "(1 + e₁₂)/√2" else "(1 − e₁₂)/√2"
        val ans = if (ccw90) vMag.toDouble() else -vMag.toDouble()
        wrapCuratedVariation(
            base = base,
            prompt = "Rotate v = ${vMag}e₁ by $rotLabel via v' = R v R̃ in Cl₂,₀. Find the e₂-component of v':",
            parametersLine1 = "v = ${vMag}e₁,  R = $rStr",
            parametersLine2 = "R̃ = $rTildeStr  (reverse of R)",
            hints = listOf(
                "Rotating ${vMag}e₁ by $rotLabel aligns it along ${if (ccw90) "+e₂" else "−e₂"}.",
                "The e₂ coefficient is ${ans.toInt()}."
            ),
            workedSteps = listOf(
                "1. v' = R(${vMag}e₁)R̃ = ${if (ccw90) "+" else "−"}${vMag}e₂ ⇒ e₂-component = ${ans.toInt()}."
            ),
            expectedReal = ans
        )
    }

    "p_710d" -> {
        val (v1, v2, v3) = listOf(
            Triple(6, 4, 5), Triple(8, 3, 2), Triple(12, 4, 3), Triple(5, 5, 4), Triple(9, 2, 6)
        ).random()
        val (i1, i2, i3) = listOf(
            Triple(3, 5, 2), Triple(4, 2, 6), Triple(2, 6, 3), Triple(4, 3, 5), Triple(3, 4, 2)
        ).random()
        val p = (v1 * i1 + v2 * i2 + v3 * i3).toDouble()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v = ${v1}e₁ + ${v2}e₂ + ${v3}e₃ V",
            parametersLine2 = "i = ${i1}e₁ + ${i2}e₂ + ${i3}e₃ A",
            hints = listOf("Sum ($v1×$i1) + ($v2×$i2) + ($v3×$i3)."),
            workedSteps = listOf("1. P = ${v1 * i1} + ${v2 * i2} + ${v3 * i3} = ${p.toInt()} W."),
            expectedReal = p
        )
    }

    "p_711d" -> {
        val (v1, i1, i3) = listOf(
            Triple(4, 3, 5), Triple(6, 2, 4), Triple(8, 5, 3), Triple(3, 4, 7), Triple(10, 2, 6)
        ).random()
        val norm = (v1 * i3).toDouble()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v = ${v1}e₁ V",
            parametersLine2 = "i = ${i1}e₁ + ${i3}e₃ A",
            hints = listOf(
                "v ∧ i = (${v1}e₁) ∧ (${i1}e₁ + ${i3}e₃) = ${v1 * i3} e₁₃.",
                "Norm is ${v1 * i3} VA."
            ),
            workedSteps = listOf(
                "1. e₁∧e₁ = 0 ⇒ v∧i = ${v1 * i3}e₁₃ ⇒ ||v∧i|| = ${norm.toInt()} VA."
            ),
            expectedReal = norm
        )
    }

    "p_712d" -> {
        val v1 = listOf(5, 6, 8, 12, 15).random()
        val (i1, i2) = listOf(
            3 to 4, 4 to -3, 6 to 8, 5 to -12, 8 to 6
        ).random()
        val ans = ComplexNum((v1 * i1).toDouble(), (v1 * i2).toDouble())
        val iStr = if (i2 >= 0) "${i1}e₁ + ${i2}e₂" else "${i1}e₁ − ${-i2}e₂"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v = ${v1}e₁  (voltage vector)",
            parametersLine2 = "i = $iStr  (current vector)",
            methodNote = "Take the direct geometric product (${v1}e₁)($iStr) in Cl₂,₀.",
            inputHint = "Enter M (e.g. ${ans.formatBivector()})",
            hints = listOf(
                "P = $v1 × $i1 = ${v1 * i1} W.",
                "Bivector part v∧i = ${v1 * i2}e₁₂."
            ),
            workedSteps = listOf(
                "1. M = (${v1}e₁)($iStr) = ${ans.formatBivector()} VA."
            ),
            expectedComplex = ans
        )
    }

    "p_732i" -> {
        val (vScalar, r, x) = listOf(
            Triple(25, 3, 4), Triple(100, 6, -8), Triple(50, 8, 6), Triple(169, 5, 12), Triple(40, 4, -2)
        ).random()
        val magSq = (r * r + x * x).toDouble()
        val ans = ComplexNum(vScalar * r / magSq, -vScalar * x / magSq)
        val zStr = ComplexNum(r.toDouble(), x.toDouble()).formatBivector()
        val zRevStr = ComplexNum(r.toDouble(), -x.toDouble()).formatBivector()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V = $vScalar (scalar phase 0°),   Z = $zStr Ω",
            parametersLine2 = "Z⁻¹ = Z̃ / (Z Z̃)",
            methodNote = "Compute reverse Z̃ = $zRevStr, norm Z Z̃ = ${magSq.toInt()}, and multiply V·Z⁻¹.",
            inputHint = "Enter I in A (e.g. ${ans.formatBivector()})",
            hints = listOf(
                "Z Z̃ = $r² + $x² = ${magSq.toInt()}.",
                "I = $vScalar · ($zRevStr) / ${magSq.toInt()}."
            ),
            workedSteps = listOf(
                "1. Z⁻¹ = ($zRevStr) / ${magSq.toInt()}.",
                "2. I = ${ans.formatBivector()} A."
            ),
            expectedComplex = ans
        )
    }

    "p_716e" -> {
        val norms = listOf(
            listOf(1, 2, 2, 3),
            listOf(2, 4, 4, 6),
            listOf(2, 3, 6, 7),
            listOf(1, 4, 8, 9),
            listOf(2, 6, 9, 11),
            listOf(4, 4, 7, 9),
            listOf(3, 4, 12, 13)
        )
        val vPick = norms.filter { !(it[0] == 3 && it[1] == 4 && it[2] == 12) }.random()
        val iPick = norms.filter { it != vPick }.random()
        val ans = (vPick[3] * iPick[3]).toDouble()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v = ${vPick[0]}e₁ + ${vPick[1]}e₂ + ${vPick[2]}e₃",
            parametersLine2 = "i = ${iPick[0]}e₁ + ${iPick[1]}e₂ + ${iPick[2]}e₃",
            hints = listOf(
                "||v|| = ${vPick[3]} and ||i|| = ${iPick[3]}.",
                "In Cl₃,₀, ||M|| = ||v|| · ||i|| = ${vPick[3]} × ${iPick[3]}."
            ),
            workedSteps = listOf(
                "1. ||v|| = ${vPick[3]}, ||i|| = ${iPick[3]}.",
                "2. ||M|| = ${vPick[3]} × ${iPick[3]} = ${ans.toInt()} VA."
            ),
            expectedReal = ans
        )
    }

    "p_720f" -> {
        val (a, b) = listOf(
            4 to 3, 3 to -4, 6 to 8, 5 to 12, 8 to -6, 1 to 2
        ).random()
        val magSq = (a * a + b * b).toDouble()
        val ans = ComplexNum(a / magSq, -b / magSq)
        val mStr = ComplexNum(a.toDouble(), b.toDouble()).formatBivector()
        val revStr = ComplexNum(a.toDouble(), -b.toDouble()).formatBivector()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "M = $mStr",
            parametersLine2 = "Reversion M̃ = $revStr",
            inputHint = "Enter M⁻¹ (e.g. ${ans.formatBivector()})",
            hints = listOf(
                "M M̃ = $a² + $b² = ${magSq.toInt()}.",
                "M⁻¹ = ($revStr) / ${magSq.toInt()}."
            ),
            workedSteps = listOf(
                "1. M M̃ = ${magSq.toInt()}.",
                "2. M⁻¹ = ($revStr) / ${magSq.toInt()} = ${ans.formatBivector()}."
            ),
            expectedComplex = ans
        )
    }

    "p_724g" -> {
        val (u1, u2, iAmp) = listOf(
            Triple(0.8, 0.6, 10),
            Triple(0.6, 0.8, 15),
            Triple(0.8, 0.6, 20),
            Triple(0.6, 0.8, 25),
            Triple(0.8, 0.6, 30)
        ).random()
        val rej = iAmp * u2
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v̂ = ${u1}e₁ + ${u2}e₂  (||v̂|| = 1)",
            parametersLine2 = "i = ${iAmp}e₁ A",
            hints = listOf(
                "i ∧ v̂ = (${iAmp}e₁) ∧ (${u1}e₁ + ${u2}e₂) = ${ComplexNum.formatClean(rej)}e₁₂.",
                "Magnitude ||i_⊥|| = ${ComplexNum.formatClean(rej)} A."
            ),
            workedSteps = listOf(
                "1. i ∧ v̂ = (${iAmp} × ${u2})e₁₂ ⇒ ||i_⊥|| = ${ComplexNum.formatClean(rej)} A."
            ),
            expectedReal = rej
        )
    }

    "p_728h" -> {
        val k = listOf(2, 3, 4, 5).random()
        val ans = -(k * k).toDouble()
        wrapCuratedVariation(
            base = base,
            prompt = "In 3D Euclidean Geometric Algebra Cl₃,₀ (e_k² = +1, orthogonal vectors anticommute), evaluate M² for M = ${k}I = ${k}e₁e₂e₃:",
            parametersLine1 = "Scaled Pseudoscalar: M = ${k}I = ${k}(e₁ e₂ e₃)",
            parametersLine2 = "Evaluate M² = ${k}² (e₁ e₂ e₃)²",
            hints = listOf(
                "First verify I² = (e₁e₂e₃)² = −1.",
                "Multiply by ${k}² = ${k * k}."
            ),
            workedSteps = listOf(
                "1. I² = −1 ⇒ M² = ${k * k}(−1) = ${ans.toInt()}."
            ),
            expectedReal = ans
        )
    }

    "p_736j" -> {
        val (v1, b12, b13, norm) = listOf(
            listOf(3, 9, 12, 15),
            listOf(5, 15, 20, 25),
            listOf(2, 6, 8, 10),
            listOf(4, 12, 16, 20),
            listOf(5, 20, 15, 25)
        ).random()
        val i2 = b12 / v1
        val i3 = b13 / v1
        val i1 = listOf(2, 3, 5).random()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v = ${v1}e₁ V",
            parametersLine2 = "i = ${i1}e₁ + ${i2}e₂ + ${i3}e₃ A",
            methodNote = "Compute wedge product v ∧ i = ${b12}e₁₂ + ${b13}e₁₃ and take magnitude √($b12² + $b13²).",
            hints = listOf(
                "v ∧ i = ${v1}e₁ ∧ (${i2}e₂ + ${i3}e₃) = ${b12}e₁₂ + ${b13}e₁₃.",
                "Compute √($b12² + $b13²)."
            ),
            workedSteps = listOf(
                "1. v ∧ i = ${b12}e₁₂ + ${b13}e₁₃.",
                "2. ||v ∧ i|| = √(${b12 * b12} + ${b13 * b13}) = $norm VA."
            ),
            expectedReal = norm.toDouble()
        )
    }

    else -> null
}
