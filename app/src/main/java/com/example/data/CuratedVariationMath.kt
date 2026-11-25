package com.example.data

import com.example.model.AnswerKind
import com.example.model.CircuitProblem
import com.example.model.ComplexNum
import com.example.model.ProblemStep
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.hypot
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

internal fun generateMathVariation(base: CircuitProblem): CircuitProblem? = when (base.id) {
    // ── COMPLEX ANALYSIS (27) ──
    "p_600a" -> {
        val (rVal, imVal) = listOf(
            1 to 1, 1 to -1, -1 to 1, -1 to -1, 2 to 2, 2 to -2
        ).random()
        val expN = if (abs(rVal) == 2) listOf(3, 4).random() else (3..8).random()
        val z = ComplexNum(rVal.toDouble(), imVal.toDouble())
        val res = ComplexNum.fromPolarDeg(z.magnitude.pow(expN.toDouble()), z.angleDeg * expN)
        wrapCuratedVariation(
            base = base,
            prompt = "An $expN-stage cascaded AC filter multiplies phasor gain z = ${z.formatRect()} per stage. Evaluate total complex gain z^$expN (A + jB):",
            parametersLine1 = "Per-stage AC gain: z = ${z.formatRect()}",
            parametersLine2 = "Total $expN-stage gain: z^$expN = r^$expN∠${expN}θ",
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
        val r = listOf(2, 4, 6, 8, 10, 12).random()
        val (thetaLabel, deg) = listOf(
            "π/6" to 30.0, "π/4" to 45.0, "π/3" to 60.0,
            "2π/3" to 120.0, "3π/4" to 135.0, "5π/6" to 150.0,
            "−π/6" to -30.0, "−π/4" to -45.0, "−π/3" to -60.0,
            "−2π/3" to -120.0, "−3π/4" to -135.0
        ).random()
        val ans = ComplexNum.fromPolarDeg(r.toDouble(), deg)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "z = $r · e^(j$thetaLabel)   (magnitude $r, phase $thetaLabel rad)",
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
        val a = listOf(-6, -5, -4, -3, -2, 2, 3, 4, 5, 6, 7, 8).random()
        val b = listOf(-7, -6, -5, -4, -3, -2, 2, 3, 4, 5, 6, 7).random()
        val ccw = Random.nextBoolean()
        val w = if (ccw) ComplexNum(-b.toDouble(), a.toDouble()) else ComplexNum(b.toDouble(), -a.toDouble())
        val opLabel = if (ccw) "j" else "−j"
        val zStr = ComplexNum(a.toDouble(), b.toDouble()).formatRect()
        wrapCuratedVariation(
            base = base,
            prompt = "An ideal reactance ($opLabel) shifts AC phasor z = $zStr by ${if (ccw) "+90° CCW" else "−90° CW"} via w = $opLabel · z. Evaluate w in rectangular form:",
            parametersLine1 = "AC phasor z = $zStr",
            parametersLine2 = "Apply w = $opLabel · ($zStr) using j² = −1",
            methodNote = "Distribute $opLabel across ($zStr) and replace j² with −1.",
            hints = listOf(
                "$opLabel · ($zStr) = ${if (ccw) "${a}j + (${b})j²" else "−${a}j − (${b})j²"}.",
                "Substitute j² = −1 to get real part ${w.re.toInt()}."
            ),
            workedSteps = listOf(
                "1. w = $opLabel($zStr) = ${w.formatRect()}."
            ),
            expectedComplex = w
        )
    }

    "p_600d" -> {
        val k = (1..6).random()
        val a = (1..4).random()
        val invertSign = Random.nextBoolean()
        val prefix = if (k == 1) "" else "$k"
        val ans = if (!invertSign) ComplexNum(0.0, k.toDouble()) else ComplexNum(0.0, -k.toDouble())
        val top = if (!invertSign) "($a + j$a)" else "($a − j$a)"
        val bot = if (!invertSign) "($a − j$a)" else "($a + j$a)"
        wrapCuratedVariation(
            base = base,
            prompt = "Simplify the AC impedance ratio w = $prefix$top / $bot (e.g., RL-to-RC bridge branch) into rectangular form A + jB:",
            parametersLine1 = "w = $prefix$top / $bot",
            parametersLine2 = "Multiply numerator & denominator by conjugate $top",
            methodNote = "Multiply top and bottom by $top so ($a − j$a)($a + j$a) = ${2 * a * a}.",
            hints = listOf(
                "$top² = ${if (!invertSign) "+${2 * a * a}j" else "−${2 * a * a}j"}.",
                "Denominator ($a − j$a)($a + j$a) = ${2 * a * a}."
            ),
            workedSteps = listOf(
                "1. w = $prefix · $top² / ${2 * a * a} = ${ans.formatRect()} (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_600e" -> {
        val isDiv = Random.nextBoolean()
        val r2 = listOf(2, 3, 4, 5).random()
        val r1 = if (isDiv) r2 * (2..6).random() else listOf(2, 3, 4, 5, 6).random()
        val a1 = listOf(-60, -45, -30, 15, 20, 30, 40, 50, 100).random()
        val a2 = listOf(-50, -30, -10, 15, 25, 35, 45, 90).random()
        val ans = if (isDiv) {
            ComplexNum.fromPolarDeg(r1.toDouble() / r2, (a1 - a2).toDouble())
        } else {
            ComplexNum.fromPolarDeg((r1 * r2).toDouble(), (a1 + a2).toDouble())
        }
        val opPrompt = if (isDiv) "Apply AC Ohm's Law I = V / Z in polar form: compute w = z₁ / z₂ (enter R∠θ° or A + jB):" else "Apply AC Ohm's Law V = I · Z in polar form: compute w = z₁ · z₂ (enter R∠θ° or A + jB):"
        val ruleStr = if (isDiv) "z₁ / z₂ = (r₁ / r₂)∠(θ₁ − θ₂)" else "z₁ · z₂ = (r₁ · r₂)∠(θ₁ + θ₂)"
        val outMag = if (isDiv) r1 / r2 else r1 * r2
        val outAng = if (isDiv) a1 - a2 else a1 + a2
        wrapCuratedVariation(
            base = base,
            prompt = opPrompt,
            parametersLine1 = "${if (isDiv) "V = z₁" else "I = z₁"} = $r1∠$a1°,   Z = z₂ = $r2∠$a2°",
            parametersLine2 = ruleStr,
            inputHint = "Enter result (${ans.formatPolar()} or rect)",
            hints = listOf(
                if (isDiv) "Modulus = $r1 / $r2 = $outMag." else "Modulus = $r1 × $r2 = $outMag.",
                if (isDiv) "Angle = $a1° − ($a2°) = $outAng°." else "Angle = $a1° + ($a2°) = $outAng°."
            ),
            workedSteps = listOf(
                "1. w = $outMag∠$outAng° = ${ans.formatRect()}."
            ),
            expectedComplex = ans
        )
    }

    "p_601a" -> {
        val a = listOf(-5, -4, -3, -2, 2, 3, 4, 5, 6).random()
        val bLin = listOf(0, 2, 3, -2, -3).random()
        val kExpected = (2 * a).toDouble()
        val polyStr = "$a(x² − y²)" + when {
            bLin > 0 -> " + ${bLin}x"
            bLin < 0 -> " − ${-bLin}x"
            else -> ""
        }
        val vLinStr = when {
            bLin > 0 -> " + ${bLin}y"
            bLin < 0 -> " − ${-bLin}y"
            else -> ""
        }
        val dUdx = "${2 * a}x" + when {
            bLin > 0 -> " + $bLin"
            bLin < 0 -> " − ${-bLin}"
            else -> ""
        }
        val dVdy = "kx" + when {
            bLin > 0 -> " + $bLin"
            bLin < 0 -> " − ${-bLin}"
            else -> ""
        }
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "f(z) = $polyStr + j(k·x·y$vLinStr)",
            parametersLine2 = "∂u/∂x = ∂v/∂y,  ∂u/∂y = −∂v/∂x",
            hints = listOf(
                "Compute u_x = ∂u/∂x = $dUdx and v_y = $dVdy.",
                "Setting $dUdx = $dVdy yields k = ${2 * a}."
            ),
            workedSteps = listOf(
                "1. u_x = $dUdx, v_y = $dVdy.",
                "2. Cauchy–Riemann u_x = v_y gives k = ${2 * a}."
            ),
            expectedReal = kExpected
        )
    }

    "p_602a" -> {
        val alpha = (1..7).random()
        val wd = (1..6).random()
        val upperHalf = Random.nextBoolean()
        val bCoeff = 2 * alpha
        val cCoeff = alpha * alpha + wd * wd
        val ans = if (upperHalf) ComplexNum(-alpha.toDouble(), wd.toDouble()) else ComplexNum(-alpha.toDouble(), -wd.toDouble())
        val poleRegion = if (upperHalf) "upper half-plane (Im(s₁) > 0)" else "lower half-plane (Im(s₁) < 0)"
        val cond = if (upperHalf) "Im(s₁) > 0" else "Im(s₁) < 0"
        wrapCuratedVariation(
            base = base,
            prompt = "Find the $poleRegion s-plane pole s₁ of the damped RLC polynomial P(s) = 0:",
            parametersLine1 = "P(s) = s² + ${bCoeff}s + $cCoeff = 0",
            parametersLine2 = "Solve s = (−b ± √(b² − 4ac)) / 2a   ($cond)",
            methodNote = "Apply the quadratic formula or complete the square: (s + $alpha)² + ${wd * wd} = 0.",
            inputHint = "Enter s₁ (e.g. ${ans.formatRect()})",
            hints = listOf(
                "Discriminant b² − 4ac = ${bCoeff * bCoeff} − ${4 * cCoeff} = −${4 * wd * wd}.",
                "s = −$alpha ± j$wd; pick $cond."
            ),
            workedSteps = listOf(
                "1. Complete square: (s + $alpha)² + $wd² = 0.",
                "2. Selected pole: s₁ = ${ans.formatRect()} rad/s."
            ),
            expectedComplex = ans
        )
    }

    "p_603a" -> {
        val (bp, bw, bDen) = listOf(
            Triple(1, 1, 2), Triple(3, 4, 25), Triple(4, 3, 25),
            Triple(5, 12, 169), Triple(12, 5, 169), Triple(8, 15, 289), Triple(15, 8, 289)
        ).random()
        val scale = if (bp == 1) listOf(5, 10, 15, 20, 30).random() else listOf(1, 2, 5, 10).random()
        val p = bp * scale
        val w = bw * scale
        val den = (p * p + w * w).toDouble()
        val kMult = (1..3).random()
        val k = bDen * scale * kMult
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
        val z0 = listOf(50, 75, 100).random()
        val rL = listOf(0, 25, 50, 75, 100, 150).random()
        val xL = listOf(-150, -100, -50, -25, 25, 50, 100, 150).random()
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
        val rCircle = (1..3).random()
        val a = listOf(-5, -3, -2, 1, 2, 3, 4, 5).random()
        val b = listOf(-7, -6, -5, -4, 4, 5, 6, 7).random()
        val dir = if (Random.nextBoolean()) "CCW" else "CW"
        val dirWord = if (dir == "CCW") "counterclockwise" else "clockwise"
        val numFactor = if (a >= 0) "(z + $a)" else "(z − ${-a})"
        val poleFactor = if (b >= 0) "(z − $b)" else "(z + ${-b})"
        wrapCuratedVariation(
            base = base,
            prompt = "Evaluate the closed $dir contour integral I = ∮ f(z) dz around the stability circle |z| = $rCircle for an AC filter whose pole sits outside at z = $b:",
            parametersLine1 = "f(z) = $numFactor / $poleFactor",
            parametersLine2 = "Contour C: |z| = $rCircle ($dirWord)",
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
        val a = (2..10).random()
        val q = (2..8).random()
        val n = a * q
        val negPole = Random.nextBoolean()
        val s0 = if (negPole) -a else a
        val s0Str = if (negPole) "−$a" else "+$a"
        val factorStr = if (negPole) "(s + $a)" else "(s − $a)"
        val res = n.toDouble() / s0
        wrapCuratedVariation(
            base = base,
            prompt = "Extract the exponential modal weight Res[F(s), s = $s0Str] for an RC/RL network response F(s) via cover-up:",
            parametersLine1 = "F(s) = $n / (s · $factorStr)",
            parametersLine2 = "Simple pole at s₀ = $s0Str",
            methodNote = "Cancel the $factorStr factor and plug s = $s0Str into the remaining fraction.",
            hints = listOf(
                "$factorStr·F(s) = $n / s.",
                "Evaluate $n / ($s0Str)."
            ),
            workedSteps = listOf(
                "1. Res[F(s), $s0Str] = $n / ($s0Str) = ${ComplexNum.formatClean(res)}."
            ),
            expectedReal = res
        )
    }

    "p_607b" -> {
        val k = listOf(-2, -1, 1, 2, 3).random()
        val z0 = listOf(-2, -1, 1, 2, 3).random()
        val rCircle = abs(z0) + (1..2).random()
        val isCcw = Random.nextBoolean()
        val dir = if (isCcw) "CCW" else "CW"
        val dirWord = if (isCcw) "CCW" else "CW (negative sign)"
        val signMul = if (isCcw) 1.0 else -1.0
        val valAns = signMul * exp((k * z0).toDouble())
        val poleFactor = if (z0 >= 0) "(z − $z0)" else "(z + ${-z0})"
        wrapCuratedVariation(
            base = base,
            prompt = "Extract the single-mode AC transient weight I = (1/j2π) ∮ (e^(${k}z) / $poleFactor) dz $dir around |z| = $rCircle enclosing z₀ = $z0:",
            parametersLine1 = "f(z) = e^(${k}z) / $poleFactor,   g(z) = e^(${k}z)",
            parametersLine2 = "Closed $dirWord Contour C: |z| = $rCircle",
            methodNote = "Since z₀ = $z0 lies inside |z| = $rCircle, (1/j2π) ∮_CCW g(z)/(z − z₀) dz = g(z₀)" + if (isCcw) "." else "; reverse sign for CW.",
            inputHint = "Enter value (e.g. ${if (isCcw) "" else "−"}e^${k * z0} or ${ComplexNum.formatClean(valAns)})",
            hints = listOf(
                "|$z0| < $rCircle, so the simple pole z₀ = $z0 is enclosed.",
                (if (isCcw) "Evaluate g($z0) = e^($k · $z0) = e^(${k * z0})." else "Clockwise contour contributes −1: −g($z0) = −e^(${k * z0}).")
            ),
            workedSteps = listOf(
                "1. ${if (isCcw) "g($z0) = e^(${k * z0})" else "−g($z0) = −e^(${k * z0})"} ≈ ${ComplexNum.formatClean(valAns)}."
            ),
            expectedReal = valAns
        )
    }

    "p_608c" -> {
        val alpha = (1..5).random()
        val w = (1..5).random()
        val c = (-4..9).filter { it != alpha }.random()
        val upperPole = Random.nextBoolean()
        val re = 0.5
        val im = if (upperPole) (alpha - c) / (2.0 * w) else (c - alpha) / (2.0 * w)
        val ans = ComplexNum(re, im)
        val poleStr = if (upperPole) "−$alpha + j$w" else "−$alpha − j$w"
        val otherFactor = if (upperPole) "(s₀ + $alpha + j$w)" else "(s₀ + $alpha − j$w)"
        val denTerm = if (upperPole) "j${2 * w}" else "−j${2 * w}"
        val numIm = if (upperPole) "+ j$w" else "− j$w"
        wrapCuratedVariation(
            base = base,
            prompt = "Compute the complex modal residue Res[F(s), s = $poleStr] for an underdamped RLC tank response F(s):",
            parametersLine1 = "F(s) = (s + $c) / ((s + $alpha)² + ${w * w})",
            parametersLine2 = "Evaluate at simple pole s₀ = $poleStr",
            methodNote = "Factor ((s+$alpha)² + ${w * w}) = (s + $alpha − j$w)(s + $alpha + j$w) and apply cover-up at s₀ = $poleStr.",
            hints = listOf(
                "Res = (s₀ + $c) / $otherFactor = (${c - alpha} $numIm) / ($denTerm).",
                "Divide (${c - alpha} $numIm) by ($denTerm)."
            ),
            workedSteps = listOf(
                "1. Cover-up at s₀ = $poleStr: (${c - alpha} $numIm) / ($denTerm).",
                "2. = ${ans.formatRect()}."
            ),
            expectedComplex = ans
        )
    }

    "p_612d" -> {
        val a = (1..6).random()
        val c = listOf(1, 2, 3, 4, 5, 6, 8, 9, 12, 16).random()
        val plusSign = Random.nextBoolean()
        val numStr = if (plusSign) "(s² + $c)" else "(s² − $c)"
        val res = if (plusSign) 1.0 - c.toDouble() / (a * a) else 1.0 + c.toDouble() / (a * a)
        val gStr = if (plusSign) "s + $c/s" else "s − $c/s"
        val gPrimeStr = if (plusSign) "1 − $c/s²" else "1 + $c/s²"
        val subStr = if (plusSign) "1 − $c/${a * a}" else "1 + $c/${a * a}"
        wrapCuratedVariation(
            base = base,
            prompt = "Compute the transient modal residue at the critically damped double pole s = −$a of an RC/RLC stage F(s):",
            parametersLine1 = "F(s) = $numStr / (s(s + $a)²)",
            parametersLine2 = "Order m = 2 pole at s = −$a",
            methodNote = "Use Res[F, −$a] = lim(s→−$a) d/ds [ (s + $a)² · F(s) ].",
            hints = listOf(
                "Differentiate g(s) = $numStr/s = $gStr.",
                "g'(s) = $gPrimeStr; evaluate at s = −$a."
            ),
            workedSteps = listOf(
                "1. g'(s) = $gPrimeStr.",
                "2. Res[F, −$a] = $subStr = ${ComplexNum.formatClean(res)}."
            ),
            expectedReal = res
        )
    }

    "p_616e" -> {
        val p1 = listOf(1, 2).random()
        val p2 = listOf(3, 4, 5, 6).random()
        val rC = p1 + 1
        val c = listOf(2, 4, 6, 8, 9, 10, 12, 15).filter { !(it == 6 && p1 == 1 && p2 == 3) }.random()
        val isCcw = Random.nextBoolean()
        val signMul = if (isCcw) 1.0 else -1.0
        val dirWord = if (isCcw) "counterclockwise" else "clockwise"
        val res0 = c.toDouble() / (p1 * p2)
        val res1 = (p1 * p1 + c).toDouble() / (p1 * (p1 - p2))
        val ans = signMul * (res0 + res1)
        val res0Str = ComplexNum.formatClean(res0)
        val res1Str = ComplexNum.formatClean(res1)
        wrapCuratedVariation(
            base = base,
            prompt = "Sum the enclosed AC/digital filter pole residues via normalized $dirWord contour integral around |z| = $rC:",
            parametersLine1 = "I = (1 / j2π) ∮ f(z) dz  along |z| = $rC ($dirWord)",
            parametersLine2 = "f(z) = (z² + $c) / (z(z − $p1)(z − $p2))",
            methodNote = "Sum residues of poles strictly inside |z| = $rC" + if (isCcw) "." else ", then negate for clockwise traversal.",
            hints = listOf(
                "Enclosed poles inside |z| = $rC are z = 0 and z = $p1 (z = $p2 is outside).",
                "Res(0) = $res0Str; Res($p1) = $res1Str."
            ),
            workedSteps = listOf(
                "1. Res[f, 0] = $c / ((−$p1)(−$p2)) = $res0Str.",
                "2. Res[f, $p1] = (${p1 * p1} + $c) / ($p1 · ($p1 − $p2)) = $res1Str.",
                "3. Integral = ${if (isCcw) "" else "−("}Res(0) + Res($p1)${if (isCcw) "" else ")"} = ${ComplexNum.formatClean(ans)}."
            ),
            expectedReal = ans,
            steps = listOf(
                ProblemStep(
                    title = "Residue at z = 0",
                    prompt = "Step 1: Evaluate the simple-pole residue Res[f(z), z = 0]:",
                    methodNote = "Cover up z and evaluate (z² + $c) / ((z − $p1)(z − $p2)) at z = 0.",
                    inputHint = "Enter Res[f, 0]",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = res0,
                    unit = "",
                    hints = listOf(
                        "Res[f, 0] = $c / ((−$p1)(−$p2)) = $res0Str."
                    ),
                    summaryText = "Res[f, 0] = $res0Str"
                ),
                ProblemStep(
                    title = "Residue at z = $p1",
                    prompt = "Step 2: Evaluate the second enclosed pole residue Res[f(z), z = $p1]:",
                    methodNote = "Cover up (z − $p1) and evaluate (z² + $c) / (z(z − $p2)) at z = $p1.",
                    inputHint = "Enter Res[f, $p1]",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = res1,
                    unit = "",
                    hints = listOf(
                        "Res[f, $p1] = (${p1 * p1} + $c) / ($p1 · (${p1 - p2})) = $res1Str."
                    ),
                    summaryText = "Res[f, $p1] = $res1Str"
                )
            )
        )
    }

    "p_620f" -> {
        val a = listOf(4, 5, 6, 8, 10, 12).random()
        val (w1, w2) = listOf(1 to 2, 1 to 3, 2 to 3, 1 to 4, 2 to 4).random()
        val (tDivLabel, tVal) = listOf(
            "π/2" to (Math.PI / 2.0),
            "π/3" to (Math.PI / 3.0),
            "π/4" to (Math.PI / 4.0),
            "π/6" to (Math.PI / 6.0),
            "π" to Math.PI
        ).random()
        val amp = a.toDouble() / ((w2 * w2) - (w1 * w1))
        val ampStr = ComplexNum.formatClean(amp)
        val ans = amp * (cos(w1 * tVal) - cos(w2 * tVal))
        wrapCuratedVariation(
            base = base,
            prompt = "Recover a coupled dual-tank LC transient f(t) = (1/j2π) ∫ F(s)e^(st) ds via Bromwich residues at t = $tDivLabel s:",
            parametersLine1 = "F(s) = ${a}s / ((s² + ${w1 * w1})(s² + ${w2 * w2}))",
            parametersLine2 = "Evaluate f(t) at t = $tDivLabel seconds",
            inputHint = "Enter f($tDivLabel)",
            hints = listOf(
                "Residue sum yields f(t) = $ampStr(cos(${w1}t) − cos(${w2}t)).",
                "Substitute t = $tDivLabel into $ampStr(cos(${w1}t) − cos(${w2}t))."
            ),
            workedSteps = listOf(
                "1. Poles at ±j$w1 and ±j$w2 give f(t) = $ampStr(cos(${w1}t) − cos(${w2}t)).",
                "2. At t = $tDivLabel: f(t) = ${ComplexNum.formatClean(ans)}."
            ),
            expectedReal = ans,
            steps = listOf(
                ProblemStep(
                    title = "Modal Amplitude A",
                    prompt = "Step 1: Find the shared cosine amplitude A = a / (ω₂² − ω₁²) in f(t) = A(cos(${w1}t) − cos(${w2}t)):",
                    methodNote = "Compute A = $a / (${w2 * w2} − ${w1 * w1}).",
                    inputHint = "Enter modal amplitude A",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = amp,
                    unit = "",
                    hints = listOf(
                        "A = $a / ${w2 * w2 - w1 * w1} = $ampStr."
                    ),
                    summaryText = "A = $ampStr ⇒ f(t) = $ampStr(cos(${w1}t) − cos(${w2}t))"
                )
            )
        )
    }

    "p_624g" -> {
        val p = (1..6).random()
        val nCcw = (-2..5).filter { it <= p && !(p == 3 && it == 2) }.random()
        val zAns = (p - nCcw).toDouble()
        val encDesc = if (nCcw >= 0) "Net CCW encirclements of (−1 + j0): N_CCW = $nCcw" else "Net CW encirclements of (−1 + j0): ${-nCcw} CW (N_CCW = $nCcw)"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Open-loop RHP poles: P = $p",
            parametersLine2 = encDesc,
            hints = listOf(
                "Apply Z = P − N_CCW directly.",
                "Z = $p − ($nCcw)."
            ),
            workedSteps = listOf(
                "1. Z = P − N_CCW = $p − ($nCcw) = ${zAns.toInt()}."
            ),
            expectedReal = zAns
        )
    }

    "p_628h" -> {
        val (kNum, kStr) = listOf(
            -2.0 to "−2", -1.5 to "−1.5", -1.0 to "−1", -0.5 to "−0.5",
            0.5 to "0.5", 1.5 to "1.5", 2.0 to "2", 2.5 to "2.5", 3.0 to "3"
        ).random()
        val ans = exp(-kNum * Math.PI / 2.0)
        val powLabel = if (kNum == -1.0) "−j" else if (kNum == 1.0) "j" else "j·($kStr)"
        wrapCuratedVariation(
            base = base,
            prompt = "In AC wave-propagation and transmission-line attenuation models using principal Log(z) = ln|z| + j·Arg(z), evaluate j^($powLabel):",
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
        val a = listOf(-6, -4, -3, -2, -1, 1, 2, 3, 4, 6).random()
        val useSin = Random.nextBoolean()
        if (useSin) {
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
        } else {
            val aEven = listOf(-6, -4, -2, 2, 4, 6).random()
            val res = -(aEven * aEven) / 2.0
            wrapCuratedVariation(
                base = base,
                parametersLine1 = "f(z) = z · cos($aEven/z)",
                parametersLine2 = "Expand cos(w) = 1 − w²/2! + w⁴/4! − … at w = $aEven/z",
                methodNote = "Expand z·cos($aEven/z) in powers of z and extract the a₋₁ coefficient of z⁻¹.",
                inputHint = "Enter residue (e.g. ${ComplexNum.formatClean(res)})",
                hints = listOf(
                    "cos($aEven/z) = 1 − ($aEven)²/(2z²) + …",
                    "Multiply by z: z⁻¹ coefficient is −($aEven)²/2."
                ),
                workedSteps = listOf(
                    "1. f(z) = z − ${aEven * aEven}/(2z) + …",
                    "2. Residue a₋₁ = −(${aEven}²)/2 = ${ComplexNum.formatClean(res)}."
                ),
                expectedReal = res
            )
        }
    }

    "p_636j" -> {
        val k = (1..5).random()
        val a = listOf(1, 2, 3, 4, 5, 6, 8, 10, 12).random()
        val ans = k.toDouble() / a
        val numLabel = if (k == 1) "dx" else "$k dx"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "I = (1/π) ∫₋∞^∞ $numLabel / (x² + ${a * a})",
            parametersLine2 = "Upper half-plane pole at z = +j$a",
            methodNote = "Compute Res[$k/(z²+${a * a}), +j$a], multiply by 2πj, then divide by π.",
            hints = listOf(
                "Only z = +j$a lies in the UHP; Res = $k/(j2·$a).",
                "2πj · ($k / (j${2 * a})) / π = $k / $a."
            ),
            workedSteps = listOf(
                "1. I = (1/π) · 2πj · Res[$k/(z²+${a * a}), j$a] = $k/$a = ${ComplexNum.formatClean(ans)}."
            ),
            expectedReal = ans
        )
    }

    "p_640k" -> {
        val w = listOf(-5, -4, -3, -2, -1, 1, 2, 3, 4, 5).random()
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
        val a = listOf(-3, -2, -1, 1, 2, 3, 4, 5, 6).random()
        val nOrder = listOf(1, 2, 3).random()
        val fact = when (nOrder) {
            1 -> 1.0
            2 -> 2.0
            else -> 6.0
        }
        val ans = a.toDouble().pow(nOrder) / fact
        val denStr = "z^${nOrder + 1}"
        wrapCuratedVariation(
            base = base,
            prompt = "Extract the order-${nOrder + 1} repeated-pole transient weight (1/j2π) ∮ (e^(${a}z) / $denStr) dz positively around |z| = 2:",
            parametersLine1 = "f(z) = e^(${a}z) / $denStr",
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

    "p_609c" -> {
        val a = listOf(-3, -2, -1, 1, 2, 3).random()
        val b = listOf(-4, -2, 1, 2, 3, 4, 5, 6).random()
        val x0 = (1..4).random()
        val y0 = (1..4).random()
        val vVal = (2 * a * x0 * y0 + b * y0).toDouble()
        val aStr = if (a == 1) "" else if (a == -1) "−" else "$a"
        val bStr = if (b >= 0) "+ ${b}x" else "− ${-b}x"
        wrapCuratedVariation(
            base = base,
            prompt = "Given 2D AC conductor equipotential u(x, y) = ${aStr}(x² − y²) $bStr with v(0, 0) = 0, construct orthogonal flux conjugate v(x, y) and evaluate v($x0, $y0):",
            parametersLine1 = "u(x, y) = ${aStr}(x² − y²) $bStr,   v(0, 0) = 0",
            parametersLine2 = "Cauchy–Riemann: ∂v/∂y = ∂u/∂x,   ∂v/∂x = −∂u/∂y",
            hints = listOf(
                "∂v/∂y = ${2 * a}x + $b ⇒ v(x, y) = ${2 * a}xy + ${b}y.",
                "Plug in (x, y) = ($x0, $y0)."
            ),
            workedSteps = listOf(
                "1. v(x, y) = ${2 * a}xy + ${b}y.",
                "2. v($x0, $y0) = ${2 * a * x0 * y0} + ${b * y0} = ${vVal.toInt()}."
            ),
            expectedReal = vVal
        )
    }

    "p_610d" -> {
        val pick = listOf(
            listOf(0.2, 0.4, 1.0, 1.0),
            listOf(0.5, 0.5, 1.0, 2.0),
            listOf(-0.5, 0.5, 0.2, 0.4),
            listOf(-0.2, 0.4, 0.5, 0.5),
            listOf(0.6, 0.0, 4.0, 0.0),
            listOf(-0.6, 0.0, 0.25, 0.0),
            listOf(0.2, -0.4, 1.0, -1.0),
            listOf(0.5, -0.5, 1.0, -2.0),
            listOf(-0.5, -0.5, 0.2, -0.4),
            listOf(-0.2, -0.4, 0.5, -0.5)
        ).random()
        val gRe = pick[0]; val gIm = pick[1]; val zNormRe = pick[2]; val zNormIm = pick[3]
        val z0 = listOf(25, 50, 75, 100, 150).random()
        val gamma = ComplexNum(gRe, gIm)
        val zL = ComplexNum(zNormRe * z0, zNormIm * z0)
        val zNorm = ComplexNum(zNormRe, zNormIm)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Γ = ${gamma.formatRect()},   Z₀ = $z0 Ω",
            parametersLine2 = "Inverse Möbius: Z_L = Z₀ · (1 + Γ) / (1 − Γ)",
            hints = listOf(
                "(1 + Γ) / (1 − Γ) = ${zNorm.formatRect()}.",
                "Multiply by Z₀ = $z0 Ω."
            ),
            workedSteps = listOf(
                "1. z_L = (1 + (${gamma.formatRect()})) / (1 − (${gamma.formatRect()})) = ${zNorm.formatRect()}.",
                "2. Z_L = $z0 × (${zNorm.formatRect()}) = ${zL.formatRect()} Ω."
            ),
            expectedComplex = zL,
            steps = listOf(
                ProblemStep(
                    title = "Normalized Impedance z_L",
                    prompt = "Step 1: Compute the normalized load impedance z_L = (1 + Γ) / (1 − Γ):",
                    methodNote = "Divide (1 + Γ) by (1 − Γ) using complex conjugate rationalization.",
                    inputHint = "Enter normalized z_L (r + jx)",
                    answerKind = AnswerKind.COMPLEX_RECT_OR_POLAR,
                    expectedComplex = zNorm,
                    unit = "",
                    hints = listOf(
                        "(1 + Γ) / (1 − Γ) = ${zNorm.formatRect()}."
                    ),
                    summaryText = "z_L = ${zNorm.formatRect()}"
                )
            )
        )
    }

    "p_622f" -> {
        val pick = listOf(
            listOf(12, 9, 15, 5, 13, 16, 20, 260, 15),
            listOf(12, 16, 20, 5, 13, 9, 15, 195, 20),
            listOf(24, 7, 25, 10, 26, 18, 30, 780, 25),
            listOf(24, 18, 30, 7, 25, 10, 26, 650, 30),
            listOf(15, 8, 17, 0, 15, 20, 25, 375, 17)
        ).random()
        val freqScale = listOf(1, 2).random()
        val gainMult = listOf(1, 2, 3).random()
        val w = pick[0] * freqScale
        val zA = pick[1] * freqScale; val zDist = pick[2] * freqScale
        val p1A = pick[3] * freqScale; val p1Dist = pick[4] * freqScale
        val p2A = pick[5] * freqScale; val p2Dist = pick[6] * freqScale
        val kGain = pick[7] * freqScale * gainMult
        val outVal = pick[8] * gainMult
        val p1Str = if (p1A == 0) "s" else "(s + $p1A)"
        wrapCuratedVariation(
            base = base,
            prompt = "Compute Bode magnitude |H(j$w)| via s-plane vector lengths from zeros z_i and poles p_k to s = j$w:",
            parametersLine1 = "H(s) = $kGain(s + $zA) / ($p1Str(s + $p2A))",
            parametersLine2 = "Evaluate |H(jω)| = K · ∏|jω − z_i| / ∏|jω − p_k| at ω = $w",
            inputHint = "Enter magnitude |H(j$w)|",
            hints = listOf(
                "Zero length |$zA + j$w| = $zDist.",
                "Pole lengths |$p1A + j$w| = $p1Dist and |$p2A + j$w| = $p2Dist."
            ),
            workedSteps = listOf(
                "1. Zero distance = $zDist; pole distances = $p1Dist, $p2Dist.",
                "2. |H(j$w)| = $kGain × $zDist / ($p1Dist × $p2Dist) = $outVal."
            ),
            expectedReal = outVal.toDouble()
        )
    }

    "p_603b" -> {
        val pick = listOf(
            listOf(3, 2, 180, "−8 = 8∠180°"),
            listOf(3, 4, 90, "j64 = 64∠90°"),
            listOf(3, 4, 180, "−64 = 64∠180°"),
            listOf(4, 3, 180, "−81 = 81∠180°"),
            listOf(2, 5, 120, "25∠120°"),
            listOf(3, 3, -90, "−j27 = 27∠−90°"),
            listOf(4, 2, 120, "16∠120°")
        ).random()
        val nRoot = pick[0] as Int
        val rOut = pick[1] as Int
        val thetaDeg = pick[2] as Int
        val zLabel = pick[3] as String
        val kBranch = listOf(0, 1).random()
        val outDeg = (thetaDeg + 360.0 * kBranch) / nRoot
        val ans = ComplexNum.fromPolarDeg(rOut.toDouble(), outDeg)
        val branchWord = if (kBranch == 0) "principal $nRoot-th root w₀" else "second-branch $nRoot-th root w₁ (k = 1)"
        wrapCuratedVariation(
            base = base,
            prompt = "In $nRoot-stage AC phase-splitter synthesis where z = w^$nRoot, find the $branchWord = z^(1/$nRoot) (enter A + jB or R∠θ°):",
            parametersLine1 = "z = $zLabel",
            parametersLine2 = "De Moivre Roots: w_k = r^(1/$nRoot) ∠( (θ + 360°k) / $nRoot ),  k = $kBranch",
            inputHint = "Enter w_$kBranch (e.g. ${rOut}∠${ComplexNum.formatClean(outDeg)}°)",
            hints = listOf(
                "Modulus: r^(1/$nRoot) = $rOut.",
                "Argument for k = $kBranch: ($thetaDeg° + ${360 * kBranch}°) / $nRoot = ${ComplexNum.formatClean(outDeg)}°."
            ),
            workedSteps = listOf(
                "1. w_$kBranch = ${rOut}∠${ComplexNum.formatClean(outDeg)}° = ${ans.formatRect()}."
            ),
            expectedComplex = ans
        )
    }

    "p_621f" -> {
        val gainMult = (1..5).random()
        val a = (1..5).random()
        val wd = listOf(2, 3, 4, 5, 6, 8).random()
        val num = gainMult * wd
        val tLabel = "π / ${2 * wd}"
        val expArg = a * Math.PI / (2.0 * wd)
        val ans = gainMult * exp(-expArg)
        wrapCuratedVariation(
            base = base,
            prompt = "Recover an underdamped RLC impulse response f(t) via Bromwich residues at conjugate LHP poles s = −$a ± j$wd, then evaluate f(t) at t = $tLabel s:",
            parametersLine1 = "F(s) = $num / ((s + $a)² + ${wd * wd})",
            parametersLine2 = "Evaluate f(t) = L⁻¹{F(s)} at t = $tLabel s   (ω_d·t = π/2)",
            methodNote = "Sum conjugate residues at s = −$a ± j$wd to obtain f(t) = ${gainMult}e^(−${a}t)sin(${wd}t), then evaluate at t = $tLabel.",
            inputHint = "Enter f($tLabel) (e.g. ${ComplexNum.formatClean(ans)})",
            hints = listOf(
                "Numerator $num = $gainMult × $wd ⇒ f(t) = ${gainMult}e^(−${a}t)sin(${wd}t).",
                "At t = $tLabel, ω_d·t = π/2 so sin(π/2) = 1."
            ),
            workedSteps = listOf(
                "1. Sum conjugate residues: f(t) = ${gainMult}e^(−${a}t)sin(${wd}t).",
                "2. f($tLabel) = ${gainMult}e^(−$a·π/${2 * wd}) ≈ ${ComplexNum.formatClean(ans)}."
            ),
            expectedReal = ans
        )
    }

    "p_622g" -> {
        val (omegaExpr, tVal, degVal) = listOf(
            Triple("5π", 0.1, 90),
            Triple("2.5π", 0.1, 45),
            Triple("5π/3", 0.2, 60),
            Triple("5π", 0.05, 45),
            Triple("10π/3", 0.1, 60),
            Triple("10π/3", 0.2, 120),
            Triple("−5π", 0.1, -90),
            Triple("−2.5π", 0.1, -45)
        ).random()
        val sigma = listOf(0, 1, 2, 3, 4, 5).random()
        val sigmaT = sigma * tVal
        val magZ = kotlin.math.exp(-sigmaT)
        val ans = ComplexNum.fromPolarDeg(magZ, degVal.toDouble())
        val poleStr = if (omegaExpr.startsWith("−")) {
            if (sigma == 0) "j(${omegaExpr})" else "−$sigma − j${omegaExpr.drop(1)}"
        } else {
            if (sigma == 0) "+j$omegaExpr" else "−$sigma + j$omegaExpr"
        }
        val stabLabel = if (sigma == 0) "|z| = 1 (unit circle)" else "|z| = e^(−${ComplexNum.formatClean(sigmaT)}) < 1"
        wrapCuratedVariation(
            base = base,
            prompt = "Map a continuous s-plane pole s = $poleStr to the discrete z-plane via z = e^(sT) with sample period T = $tVal s. Enter z (rect or polar):",
            parametersLine1 = "Continuous pole: s = $poleStr rad/s",
            parametersLine2 = "Sample period T = $tVal s   (z = e^(−σT) ∠ ωT)",
            hints = listOf(
                "Radius: $stabLabel ≈ ${ComplexNum.formatClean(magZ)}.",
                "Angle: ωT rad = $degVal°."
            ),
            workedSteps = listOf(
                "1. |z| = e^(−$sigma × $tVal) = ${ComplexNum.formatClean(magZ)}.",
                "2. θ = ωT = $degVal° ⇒ z = ${ComplexNum.formatClean(magZ)}∠$degVal° (${ans.formatRect()})."
            ),
            expectedComplex = ans
        )
    }

    "p_623h" -> {
        val (aVal, bVal, cRoot) = listOf(
            Triple(5, 3, 4), Triple(5, 4, 3), Triple(10, 6, 8),
            Triple(13, 5, 12), Triple(13, 12, 5), Triple(17, 8, 15),
            Triple(17, 15, 8), Triple(25, 7, 24)
        ).random()
        val kNum = listOf(1, 2, 3, 4, 6).random()
        val isCos = Random.nextBoolean()
        val signChar = if (Random.nextBoolean()) "+" else "−"
        val trigStr = if (isCos) "cos θ" else "sin θ"
        val integralVal = (2.0 * Math.PI * kNum) / cRoot
        wrapCuratedVariation(
            base = base,
            prompt = "Evaluate the periodic AC cycle integral I = ∫₀²π $kNum dθ / ($aVal $signChar $bVal $trigStr) (cycle-averaged admittance) via z = e^(jθ) on |z| = 1:",
            parametersLine1 = "I = ∫₀²π $kNum dθ / ($aVal $signChar $bVal $trigStr)",
            parametersLine2 = "Substitute z = e^(jθ), dθ = dz/(jz) around unit circle |z| = 1",
            hints = listOf(
                "The unique simple pole inside |z| = 1 yields I = $kNum · (2π / √($aVal² − $bVal²)).",
                "Notice √($aVal² − $bVal²) = √${aVal * aVal - bVal * bVal} = $cRoot."
            ),
            workedSteps = listOf(
                "1. Applying residue theorem on |z| = 1 for a > |b| gives I = $kNum · 2π / √($aVal² − $bVal²).",
                "2. √($aVal² − $bVal²) = $cRoot ⇒ I = ${2 * kNum}π / $cRoot ≈ ${ComplexNum.formatClean(integralVal)}."
            ),
            expectedReal = integralVal
        )
    }

    // ── GEOMETRIC ALGEBRA (23) ──
    "p_700a" -> {
        val v1 = (3..12).random()
        val v2 = listOf(-4, -3, -2, 1, 2, 3, 4, 5).random()
        val i1 = (2..8).random()
        val i2 = listOf(-3, -2, 1, 2, 3, 4, 5, 6).random()
        val p = (v1 * i1 + v2 * i2).toDouble()
        val v2Str = if (v2 >= 0) "+ ${v2}e₂" else "− ${-v2}e₂"
        val i2Str = if (i2 >= 0) "+ ${i2}e₂" else "− ${-i2}e₂"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v = ${v1}e₁ $v2Str V",
            parametersLine2 = "i = ${i1}e₁ $i2Str A",
            hints = listOf("Multiply matching components: ($v1 × $i1) + ($v2 × $i2)."),
            workedSteps = listOf("1. P = ${v1 * i1} + (${v2 * i2}) = ${p.toInt()} W."),
            expectedReal = p
        )
    }

    "p_700b" -> {
        val a1 = (2..9).random()
        val a2 = listOf(-4, -3, -2, 1, 2, 3, 4, 5).random()
        val b1 = (1..6).random()
        val b2 = listOf(-4, -2, 2, 3, 4, 5, 6).random()
        val beta = (a1 * b2 - a2 * b1).toDouble()
        val a2Str = if (a2 >= 0) "+ ${a2}e₂" else "− ${-a2}e₂"
        val b2Str = if (b2 >= 0) "+ ${b2}e₂" else "− ${-b2}e₂"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "a = ${a1}e₁ $a2Str  (AC voltage vector)",
            parametersLine2 = "b = ${b1}e₁ $b2Str  (AC current vector; e₂∧e₁ = −e₁₂)",
            hints = listOf("Evaluate determinant β = ($a1 × $b2) − ($a2 × $b1)."),
            workedSteps = listOf("1. a ∧ b = (${a1 * b2} − (${a2 * b1}))e₁₂ = ${beta.toInt()}e₁₂."),
            expectedReal = beta
        )
    }

    "p_701a" -> {
        val k = listOf(-6, -5, -4, -3, -2, 2, 3, 4, 5, 6, 7, 8).random()
        val ans = -(k * k).toDouble()
        val kLabel = if (k < 0) "(−${-k}e₁₂)" else "${k}e₁₂"
        wrapCuratedVariation(
            base = base,
            prompt = "Verify how bivector B = $kLabel in Cl₂,₀ scales the +90° inductive AC operator by computing scalar B²:",
            parametersLine1 = "B = ${if (k < 0) "−${-k}" else "$k"}e₁₂ = ${if (k < 0) "−${-k}" else "$k"}(e₁ ∧ e₂)",
            parametersLine2 = "Evaluate scalar B² = ($k)² (e₁₂)²",
            hints = listOf(
                "Since e₂e₁ = −e₁e₂, (e₁₂)² = −1.",
                "Multiply by ($k)² = ${k * k}."
            ),
            workedSteps = listOf(
                "1. B² = ${k * k}(e₁₂)² = ${k * k}(−1) = ${ans.toInt()}."
            ),
            expectedReal = ans
        )
    }

    "p_702a" -> {
        val a = listOf(-6, -4, -3, 2, 3, 4, 5, 6, 7, 8).random()
        val b = listOf(-7, -5, -4, -2, 2, 3, 4, 5, 6, 7).random()
        val askE1 = Random.nextBoolean()
        val bStr = if (b >= 0) "+ ${b}e₂" else "− ${-b}e₂"
        val ans = if (askE1) (-b).toDouble() else a.toDouble()
        val axisLabel = if (askE1) "e₁-component" else "e₂-component"
        wrapCuratedVariation(
            base = base,
            prompt = "Right-multiplying AC phasor vector v by e₁₂ applies a +90° inductive shift (v·e₁₂). Find the $axisLabel of v' = v · e₁₂:",
            parametersLine1 = "v = ${a}e₁ $bStr  (AC phasor vector)",
            parametersLine2 = "v' = v · e₁₂ = $a(e₁e₁₂) + ($b)(e₂e₁₂)",
            hints = listOf(
                "e₁e₁₂ = e₂ and e₂e₁₂ = −e₁.",
                "v · e₁₂ = ${-b}e₁ + (${a})e₂ ⇒ $axisLabel is ${ans.toInt()}."
            ),
            workedSteps = listOf(
                "1. v·e₁₂ = (${-b})e₁ + (${a})e₂ ⇒ $axisLabel = ${ans.toInt()}."
            ),
            expectedReal = ans
        )
    }

    "p_703a" -> {
        val r = listOf(3, 4, 5, 6, 8, 9, 10, 12, 15, 20).random()
        val isInductive = Random.nextBoolean()
        val w = listOf(100, 200, 250, 400, 500, 1000).random()
        if (isInductive) {
            val xMag = listOf(4, 5, 6, 8, 10, 12, 15, 20).random()
            val lH = xMag.toDouble() / w
            val ans = ComplexNum(r.toDouble(), xMag.toDouble())
            wrapCuratedVariation(
                base = base,
                prompt = "Represent the series RL impedance Z = R + ωL e₁₂ in Cl₂,₀ bivector form (enter R + Xe12):",
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
        } else {
            val xMag = listOf(4, 5, 8, 10, 20, 25).random()
            val cMf = 1000.0 / (w * xMag)
            val ans = ComplexNum(r.toDouble(), -xMag.toDouble())
            wrapCuratedVariation(
                base = base,
                prompt = "Represent the series RC impedance Z = R − (1/ωC) e₁₂ in Cl₂,₀ bivector form (enter R - Xe12):",
                parametersLine1 = "Series RC: R = ${r}Ω, C = ${ComplexNum.formatClean(cMf)} mF",
                parametersLine2 = "ω = $w rad/s   (capacitive −e₁₂ opposes +jωL)",
                hints = listOf(
                    "X_C = 1/(ωC) = 1000 / ($w × ${ComplexNum.formatClean(cMf)}) = $xMag Ω.",
                    "Write Z = $r − ${xMag}e₁₂."
                ),
                workedSteps = listOf(
                    "1. X_C = $xMag Ω ⇒ Z = ${ans.formatBivector()} Ω."
                ),
                expectedComplex = ans
            )
        }
    }

    "p_704b" -> {
        val a1 = (2..8).random()
        val a2 = listOf(-5, -4, -3, -2, 2, 3, 4, 5, 6).random()
        val b1 = (1..6).random()
        val b2 = listOf(-4, -3, 1, 2, 3, 4, 5).random()
        val dot = (a1 * b1 + a2 * b2).toDouble()
        val wedge = (a1 * b2 - a2 * b1).toDouble()
        val ans = ComplexNum(dot, wedge)
        val aStr = if (a2 >= 0) "${a1}e₁ + ${a2}e₂" else "${a1}e₁ − ${-a2}e₂"
        val bStr = if (b2 >= 0) "${b1}e₁ + ${b2}e₂" else "${b1}e₁ − ${-b2}e₂"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "a = $aStr  (voltage vector)",
            parametersLine2 = "b = $bStr  (current vector)",
            hints = listOf(
                "a·b = $a1($b1) + ($a2)($b2) = ${dot.toInt()}.",
                "a∧b = ($a1·($b2) − ($a2)·$b1)e₁₂ = ${wedge.toInt()}e₁₂."
            ),
            workedSteps = listOf(
                "1. a·b = ${dot.toInt()}, a∧b = ${wedge.toInt()}e₁₂.",
                "2. ab = ${ans.formatBivector()}."
            ),
            expectedComplex = ans
        )
    }

    "p_705b" -> {
        val (u1, u2) = listOf(0.8 to 0.6, 0.6 to 0.8, 0.8 to -0.6, -0.6 to 0.8).random()
        val iAmp = listOf(5, 10, 15, 20, 25, 30, 40, 50).random()
        val alongE1 = Random.nextBoolean()
        val uSel = if (alongE1) u1 else u2
        val proj = iAmp * kotlin.math.abs(uSel)
        val vHatStr = if (u2 >= 0) "${u1}e₁ + ${u2}e₂" else "${u1}e₁ − ${-u2}e₂"
        val iStr = if (alongE1) "${iAmp}e₁" else "${iAmp}e₂"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v̂ = $vHatStr   (||v̂|| = 1)",
            parametersLine2 = "i = $iStr A",
            hints = listOf("i · v̂ = $iAmp × ($uSel)."),
            workedSteps = listOf("1. ||i_∥|| = |$iAmp × ($uSel)| = ${ComplexNum.formatClean(proj)} A."),
            expectedReal = proj
        )
    }

    "p_706b" -> {
        val (ba, bb, bNorm) = listOf(
            Triple(3, 4, 5), Triple(4, 3, 5),
            Triple(5, 12, 13), Triple(8, 15, 17),
            Triple(7, 24, 25), Triple(9, 40, 41)
        ).random()
        val scale = (1..3).random()
        val a = ba * scale * (if (Random.nextBoolean()) 1 else -1)
        val b = bb * scale
        val norm = bNorm * scale
        val sign = if (Random.nextBoolean()) 1 else -1
        val bSigned = b * sign
        val bSignStr = if (bSigned >= 0) "+ ${b}e₁₂" else "− ${b}e₁₂"
        val revStr = if (bSigned >= 0) "− ${b}e₁₂" else "+ ${b}e₁₂"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "M = $a $bSignStr  (e.g., Z or S multivector)",
            parametersLine2 = "Reversion M̃ = $a $revStr   ((e₁₂)² = −1)",
            hints = listOf(
                "M M̃ = ($a)² + $b² = ${norm * norm}.",
                "||M|| = √${norm * norm}."
            ),
            workedSteps = listOf("1. ||M|| = √(($a)² + $b²) = $norm."),
            expectedReal = norm.toDouble()
        )
    }

    "p_707b" -> {
        val thetaDeg = listOf(
            30.0, 45.0, 60.0, 90.0, 120.0, 135.0, 150.0, 180.0,
            -30.0, -45.0, -60.0, -90.0, -120.0
        ).random()
        val halfRad = Math.toRadians(thetaDeg / 2.0)
        val ans = ComplexNum(cos(halfRad), -sin(halfRad))
        wrapCuratedVariation(
            base = base,
            prompt = "Build the lossless AC phase-shifter rotor R = cos(θ/2) − sin(θ/2)e₁₂ for a θ = ${ComplexNum.formatClean(thetaDeg)}° CCW phasor shift (enter α + βe12):",
            parametersLine1 = "Phase-Shift Angle θ = ${ComplexNum.formatClean(thetaDeg)}° CCW",
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
        val vMag = listOf(2, 3, 4, 5, 6, 8, 10, 12, 15).random()
        val ccw90 = Random.nextBoolean()
        val startE1 = Random.nextBoolean()
        val rotLabel = if (ccw90) "+90° CCW" else "−90° CW"
        val rStr = if (ccw90) "(1 − e₁₂)/√2" else "(1 + e₁₂)/√2"
        val rTildeStr = if (ccw90) "(1 + e₁₂)/√2" else "(1 − e₁₂)/√2"
        val startStr = if (startE1) "${vMag}e₁" else "${vMag}e₂"
        val targetAxis = if (startE1) "e₂" else "e₁"
        val ans = when {
            startE1 && ccw90 -> vMag.toDouble()
            startE1 && !ccw90 -> -vMag.toDouble()
            !startE1 && ccw90 -> -vMag.toDouble()
            else -> vMag.toDouble()
        }
        val outVecLabel = "${if (ans > 0) "+" else "−"}${vMag}$targetAxis"
        wrapCuratedVariation(
            base = base,
            prompt = "Phase-shift AC voltage vector v = $startStr by $rotLabel via rotor sandwich v' = R v R̃ in Cl₂,₀. Find the $targetAxis-component of v':",
            parametersLine1 = "v = $startStr,  R = $rStr",
            parametersLine2 = "R̃ = $rTildeStr  (reverse of R)",
            hints = listOf(
                "Rotating $startStr by $rotLabel aligns it along $outVecLabel.",
                "The $targetAxis coefficient is ${ans.toInt()}."
            ),
            workedSteps = listOf(
                "1. v' = R($startStr)R̃ = $outVecLabel ⇒ $targetAxis-component = ${ans.toInt()}."
            ),
            expectedReal = ans
        )
    }

    "p_710d" -> {
        val v1 = (3..12).random()
        val v2 = listOf(-4, -3, 2, 3, 4, 5, 6).random()
        val v3 = (2..8).random()
        val i1 = (2..7).random()
        val i2 = (2..7).random()
        val i3 = listOf(-3, -2, 2, 3, 4, 5, 6).random()
        val p = (v1 * i1 + v2 * i2 + v3 * i3).toDouble()
        val v2Str = if (v2 >= 0) "+ ${v2}e₂" else "− ${-v2}e₂"
        val i3Str = if (i3 >= 0) "+ ${i3}e₃" else "− ${-i3}e₃"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v = ${v1}e₁ $v2Str + ${v3}e₃ V",
            parametersLine2 = "i = ${i1}e₁ + ${i2}e₂ $i3Str A",
            hints = listOf("Sum ($v1×$i1) + ($v2×$i2) + ($v3×$i3)."),
            workedSteps = listOf("1. P = ${v1 * i1} + (${v2 * i2}) + (${v3 * i3}) = ${p.toInt()} W."),
            expectedReal = p
        )
    }

    "p_711d" -> {
        val v1 = (2..12).random()
        val i1 = (1..8).random()
        val iOrth = (2..10).random()
        val useE3 = Random.nextBoolean()
        val orthAxis = if (useE3) "e₃" else "e₂"
        val planeName = if (useE3) "e₁₃" else "e₁₂"
        val norm = (v1 * iOrth).toDouble()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v = ${v1}e₁ V",
            parametersLine2 = "i = ${i1}e₁ + ${iOrth}$orthAxis A",
            hints = listOf(
                "v ∧ i = (${v1}e₁) ∧ (${i1}e₁ + ${iOrth}$orthAxis) = ${v1 * iOrth} $planeName.",
                "Norm is ${v1 * iOrth} VA."
            ),
            workedSteps = listOf(
                "1. e₁∧e₁ = 0 ⇒ v∧i = ${v1 * iOrth}$planeName ⇒ ||v∧i|| = ${norm.toInt()} VA."
            ),
            expectedReal = norm
        )
    }

    "p_712d" -> {
        val v1 = listOf(3, 4, 5, 6, 8, 10, 12, 15).random()
        val i1 = (2..8).random()
        val i2 = listOf(-12, -8, -6, -4, -3, 3, 4, 6, 8, 12).random()
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
        val (br, bx, bz) = listOf(
            Triple(3, 4, 5), Triple(4, 3, 5),
            Triple(5, 12, 13), Triple(12, 5, 13),
            Triple(4, 2, 0) // 4^2+2^2=20
        ).random()
        val scale = if (br == 4 && bx == 2) 1 else (1..2).random()
        val r = br * scale
        val x = bx * scale * (if (Random.nextBoolean()) 1 else -1)
        val magSqInt = r * r + x * x
        val vMult = (1..4).random()
        val vScalar = magSqInt * vMult
        val magSq = magSqInt.toDouble()
        val ans = ComplexNum(vScalar * r / magSq, -vScalar * x / magSq)
        val zStr = ComplexNum(r.toDouble(), x.toDouble()).formatBivector()
        val zRevStr = ComplexNum(r.toDouble(), -x.toDouble()).formatBivector()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V = $vScalar (scalar phase 0°),   Z = $zStr Ω",
            parametersLine2 = "Z⁻¹ = Z̃ / (Z Z̃)",
            methodNote = "Compute reverse Z̃ = $zRevStr, norm Z Z̃ = $magSqInt, and multiply V·Z⁻¹.",
            inputHint = "Enter I in A (e.g. ${ans.formatBivector()})",
            hints = listOf(
                "Z Z̃ = $r² + ($x)² = $magSqInt.",
                "I = $vScalar · ($zRevStr) / $magSqInt."
            ),
            workedSteps = listOf(
                "1. Z⁻¹ = ($zRevStr) / $magSqInt.",
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
            listOf(3, 4, 12, 13),
            listOf(4, 6, 12, 14),
            listOf(2, 8, 16, 18)
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
            expectedReal = ans,
            steps = listOf(
                ProblemStep(
                    title = "Voltage Vector Norm",
                    prompt = "Step 1: Compute the Euclidean RMS norm of the voltage vector ||v|| = √(v₁² + v₂² + v₃²):",
                    methodNote = "Take the square root of the sum of squared voltage components.",
                    inputHint = "Enter scalar ||v||",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = vPick[3].toDouble(),
                    unit = "",
                    hints = listOf(
                        "√(${vPick[0]}² + ${vPick[1]}² + ${vPick[2]}²) = ${vPick[3]}."
                    ),
                    summaryText = "||v|| = ${vPick[3]}"
                ),
                ProblemStep(
                    title = "Current Vector Norm",
                    prompt = "Step 2: Compute the Euclidean RMS norm of the current vector ||i|| = √(i₁² + i₂² + i₃²):",
                    methodNote = "Take the square root of the sum of squared current components.",
                    inputHint = "Enter scalar ||i||",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = iPick[3].toDouble(),
                    unit = "",
                    hints = listOf(
                        "√(${iPick[0]}² + ${iPick[1]}² + ${iPick[2]}²) = ${iPick[3]}."
                    ),
                    summaryText = "||i|| = ${iPick[3]}"
                )
            )
        )
    }

    "p_720f" -> {
        val (ba, bb) = listOf(
            3 to 4, 4 to 3, 5 to 12, 12 to 5, 1 to 2, 2 to 1
        ).random()
        val scale = if (ba <= 2) 1 else (1..2).random()
        val a = ba * scale * (if (Random.nextBoolean()) 1 else -1)
        val b = bb * scale * (if (Random.nextBoolean()) 1 else -1)
        val magSq = (a * a + b * b).toDouble()
        val ans = ComplexNum(a / magSq, -b / magSq)
        val mStr = ComplexNum(a.toDouble(), b.toDouble()).formatBivector()
        val revStr = ComplexNum(a.toDouble(), -b.toDouble()).formatBivector()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Impedance M = $mStr",
            parametersLine2 = "Reversion M̃ = $revStr",
            inputHint = "Enter M⁻¹ (e.g. ${ans.formatBivector()})",
            hints = listOf(
                "M M̃ = ($a)² + ($b)² = ${magSq.toInt()}.",
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
        val (u1, u2) = listOf(0.8 to 0.6, 0.6 to 0.8, 0.28 to 0.96, 0.96 to 0.28).random()
        val iAmp = listOf(10, 15, 20, 25, 30, 40, 50).random()
        val alongE1 = Random.nextBoolean()
        val rej = iAmp * (if (alongE1) u2 else u1)
        val iStr = if (alongE1) "${iAmp}e₁" else "${iAmp}e₂"
        val wedgeTerm = if (alongE1) "$iAmp × $u2" else "−$iAmp × $u1"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v̂ = ${u1}e₁ + ${u2}e₂  (||v̂|| = 1)",
            parametersLine2 = "i = $iStr A",
            hints = listOf(
                "i ∧ v̂ = ($wedgeTerm)e₁₂.",
                "Magnitude ||i_⊥|| = ${ComplexNum.formatClean(rej)} A."
            ),
            workedSteps = listOf(
                "1. ||i ∧ v̂|| = ${ComplexNum.formatClean(rej)} ⇒ ||i_⊥|| = ${ComplexNum.formatClean(rej)} A."
            ),
            expectedReal = rej
        )
    }

    "p_728h" -> {
        val k = listOf(-6, -5, -4, -3, -2, 2, 3, 4, 5, 6, 7, 8).random()
        val ans = -(k * k).toDouble()
        val kDisp = if (k < 0) "−${-k}" else "$k"
        wrapCuratedVariation(
            base = base,
            prompt = "In 3-harmonic AC space Cl₃,₀ (e_k² = +1, orthogonal harmonic axes anticommute), evaluate M² for M = ${kDisp}I = ${kDisp}e₁e₂e₃:",
            parametersLine1 = "Scaled Pseudoscalar: M = ${kDisp}I = ${kDisp}(e₁ e₂ e₃)",
            parametersLine2 = "Evaluate M² = ($k)² (e₁ e₂ e₃)²",
            hints = listOf(
                "First verify I² = (e₁e₂e₃)² = −1.",
                "Multiply by ($k)² = ${k * k}."
            ),
            workedSteps = listOf(
                "1. I² = −1 ⇒ M² = ${k * k}(−1) = ${ans.toInt()}."
            ),
            expectedReal = ans
        )
    }

    "p_736j" -> {
        val (b12, b13, bNorm) = listOf(
            Triple(3, 4, 5), Triple(4, 3, 5),
            Triple(5, 12, 13), Triple(12, 5, 13),
            Triple(8, 15, 17)
        ).random()
        val v1 = (2..5).random()
        val iScale = (1..3).random()
        val i2 = b12 * iScale
        val i3 = b13 * iScale
        val i1 = (1..6).random()
        val wedge12 = v1 * i2
        val wedge13 = v1 * i3
        val norm = v1 * bNorm * iScale
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v = ${v1}e₁ V",
            parametersLine2 = "i = ${i1}e₁ + ${i2}e₂ + ${i3}e₃ A",
            methodNote = "Compute wedge product v ∧ i = ${wedge12}e₁₂ + ${wedge13}e₁₃ and take magnitude √($wedge12² + $wedge13²).",
            hints = listOf(
                "v ∧ i = ${v1}e₁ ∧ (${i2}e₂ + ${i3}e₃) = ${wedge12}e₁₂ + ${wedge13}e₁₃.",
                "Compute √($wedge12² + $wedge13²)."
            ),
            workedSteps = listOf(
                "1. v ∧ i = ${wedge12}e₁₂ + ${wedge13}e₁₃.",
                "2. ||v ∧ i|| = √(${wedge12 * wedge12} + ${wedge13 * wedge13}) = $norm VA."
            ),
            expectedReal = norm.toDouble()
        )
    }

    "p_709c" -> {
        val (u1, u2) = listOf(0.8 to 0.6, 0.6 to 0.8, 0.8 to -0.6, 0.6 to -0.8).random()
        val vAmp = listOf(25, 50, 75, 100).random()
        val startE1 = Random.nextBoolean()
        val vVecStr = if (startE1) "${vAmp}e₁" else "${vAmp}e₂"
        val targetAxis = if (startE1) "e₂" else "e₁"
        val vDotU = vAmp * (if (startE1) u1 else u2)
        val compOut = 2.0 * vAmp * u1 * u2
        val uStr = if (u2 >= 0) "${u1}e₁ + ${u2}e₂" else "${u1}e₁ − ${-u2}e₂"
        wrapCuratedVariation(
            base = base,
            prompt = "Apply phase-conjugate line reflection v' = û v û of AC phasor v = $vVecStr across unit axis û in Cl₂,₀. Find the $targetAxis-component of v':",
            parametersLine1 = "v = $vVecStr,   û = $uStr  (||û|| = 1)",
            parametersLine2 = "Reflection law: v' = û v û = 2(v·û)û − v",
            hints = listOf(
                "v·û = $vAmp × (${if (startE1) u1 else u2}) = ${ComplexNum.formatClean(vDotU)}.",
                "$targetAxis-component = 2 × ${ComplexNum.formatClean(vDotU)} × (${if (startE1) u2 else u1})."
            ),
            workedSteps = listOf(
                "1. v' = 2(${ComplexNum.formatClean(vDotU)})($uStr) − $vVecStr.",
                "2. $targetAxis-component = ${ComplexNum.formatClean(compOut)}."
            ),
            expectedReal = compOut
        )
    }

    "p_726h" -> {
        val pick = listOf(
            listOf(-0.28, 0.96, 0.6, -0.8),
            listOf(0.28, -0.96, 0.8, 0.6),
            listOf(-0.28, -0.96, 0.6, 0.8),
            listOf(0.28, 0.96, 0.8, -0.6),
            listOf(0.8432, 0.5376, 0.96, -0.28),
            listOf(0.8432, -0.5376, 0.96, 0.28),
            listOf(-0.8432, 0.5376, 0.28, -0.96),
            listOf(-0.8432, -0.5376, 0.28, 0.96)
        ).random()
        val b1 = pick[0]; val b2 = pick[1]; val rScalar = pick[2]; val rBiv = pick[3]
        val ans = ComplexNum(rScalar, rBiv)
        val bVecStr = if (b2 >= 0) "${b1}e₁ + ${b2}e₂" else "${b1}e₁ − ${-b2}e₂"
        val baStr = ComplexNum(b1, -b2).formatBivector()
        val onePlusBaStr = ComplexNum(1.0 + b1, -b2).formatBivector()
        val normBisector = hypot(1.0 + b1, -b2)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "â = e₁,   b̂ = $bVecStr  (||b̂|| = 1)",
            parametersLine2 = "b̂ â = $baStr  ⇒  R = α + βe₁₂",
            inputHint = "Enter R (e.g. ${ans.formatBivector()})",
            hints = listOf(
                "1 + b̂ â = $onePlusBaStr.",
                "Normalize by dividing by ||1 + b̂ â|| = ${ComplexNum.formatClean(normBisector)}."
            ),
            workedSteps = listOf(
                "1. ||1 + b̂ â|| = ${ComplexNum.formatClean(normBisector)}.",
                "2. R = ($onePlusBaStr) / ${ComplexNum.formatClean(normBisector)} = ${ans.formatBivector()}."
            ),
            expectedComplex = ans,
            steps = listOf(
                ProblemStep(
                    title = "Bisector Norm",
                    prompt = "Step 1: Find the multivector norm ||1 + b̂ â|| = √((1 + b₁)² + (−b₂)²):",
                    methodNote = "Form 1 + b̂ â = $onePlusBaStr and compute its Euclidean multivector norm.",
                    inputHint = "Enter scalar norm ||1 + b̂ â||",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = normBisector,
                    unit = "",
                    hints = listOf(
                        "||$onePlusBaStr|| = ${ComplexNum.formatClean(normBisector)}."
                    ),
                    summaryText = "||1 + b̂ â|| = ${ComplexNum.formatClean(normBisector)}"
                )
            )
        )
    }

    "p_714d" -> {
        val (v1, v2, vNorm) = listOf(
            Triple(3, 4, 5), Triple(6, 8, 10),
            Triple(5, 12, 13), Triple(8, 15, 17), Triple(9, 12, 15)
        ).random()
        val i1 = (2..6).random()
        val i2 = (2..6).random()
        val i3 = (2..5).random()
        val bCrossNorm = (vNorm * i3).toDouble()
        val b13 = v1 * i3; val b23 = v2 * i3
        val b12 = v1 * i2 - v2 * i1
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v = ${v1}e₁ + ${v2}e₂ V   (pure fundamental in e₁–e₂ plane)",
            parametersLine2 = "i = ${i1}e₁ + ${i2}e₂ + ${i3}e₃ A   (includes 3rd harmonic along e₃)",
            hints = listOf(
                "Fundamental B₁₂ = ${b12}e₁₂ is excluded from B_cross.",
                "B_cross = ${b13}e₁₃ + ${b23}e₂₃ ⇒ √($b13² + $b23²)."
            ),
            workedSteps = listOf(
                "1. B_cross = (${v1}e₁ + ${v2}e₂) ∧ (${i3}e₃) = ${b13}e₁₃ + ${b23}e₂₃.",
                "2. ||B_cross|| = √(${b13 * b13} + ${b23 * b23}) = ${bCrossNorm.toInt()} VA."
            ),
            expectedReal = bCrossNorm
        )
    }

    "p_730i" -> {
        val (u1, u2) = listOf(0.6 to 0.8, 0.8 to 0.6, 0.8 to -0.6, 0.6 to -0.8).random()
        val i1 = listOf(10.0, 15.0, 20.0, 25.0, 30.0).random()
        val i2 = listOf(-10.0, -5.0, 0.0, 5.0, 10.0).random()
        val askE1 = Random.nextBoolean()
        val wedgeB = i1 * u2 - i2 * u1
        val e1Comp = wedgeB * u2
        val e2Comp = -wedgeB * u1
        val expectedVal = if (askE1) e1Comp else e2Comp
        val axisLabel = if (askE1) "e₁-component" else "e₂-component"
        val uStr = if (u2 >= 0) "${u1}e₁ + ${u2}e₂" else "${u1}e₁ − ${-u2}e₂"
        val iStr = when {
            i2 == 0.0 -> "${i1.toInt()}e₁"
            i2 > 0.0 -> "${i1.toInt()}e₁ + ${i2.toInt()}e₂"
            else -> "${i1.toInt()}e₁ − ${(-i2).toInt()}e₂"
        }
        val betaStr = ComplexNum.formatClean(wedgeB)
        wrapCuratedVariation(
            base = base,
            prompt = "Compute Fryze orthogonal reactive current i_⊥ = (i ∧ v̂)v̂ in Cl₂,₀ (unit voltage v̂, ||v̂|| = 1). Find the $axisLabel of i_⊥ (in A):",
            parametersLine1 = "v̂ = $uStr   (||v̂|| = 1)",
            parametersLine2 = "i = $iStr A   (e₁₂e₁ = −e₂, e₁₂e₂ = +e₁)",
            hints = listOf(
                "i ∧ v̂ = ${betaStr}e₁₂.",
                "(${betaStr}e₁₂)($uStr) = ${ComplexNum.formatClean(e1Comp)}e₁ + (${ComplexNum.formatClean(e2Comp)})e₂."
            ),
            workedSteps = listOf(
                "1. i ∧ v̂ = (${i1.toInt()}·($u2) − (${i2.toInt()})·$u1)e₁₂ = ${betaStr}e₁₂.",
                "2. i_⊥ = ${ComplexNum.formatClean(e1Comp)}e₁ + (${ComplexNum.formatClean(e2Comp)})e₂ ⇒ $axisLabel = ${ComplexNum.formatClean(expectedVal)} A."
            ),
            expectedReal = expectedVal,
            steps = listOf(
                ProblemStep(
                    title = "Wedge Bivector i ∧ v̂",
                    prompt = "Step 1: Compute the scalar coefficient β of the bivector rejection i ∧ v̂ = β·e₁₂:",
                    methodNote = "Use β = i₁·v̂₂ − i₂·v̂₁ from the exterior wedge product.",
                    inputHint = "Enter scalar β",
                    answerKind = AnswerKind.REAL_SCALAR,
                    expectedReal = wedgeB,
                    unit = "",
                    hints = listOf(
                        "β = ${i1.toInt()}·($u2) − (${i2.toInt()})·$u1 = $betaStr."
                    ),
                    summaryText = "i ∧ v̂ = ${betaStr}e₁₂"
                )
            )
        )
    }

    "p_737k" -> {
        val (a11, a12, a21, a22) = listOf(
            listOf(2, 1, 1, 3),
            listOf(3, 1, 1, 2),
            listOf(2, -1, 1, 2),
            listOf(3, 2, 1, 2),
            listOf(4, 1, 1, 2),
            listOf(1, 3, -1, 2)
        ).random()
        val coords = listOf(-4, -3, -2, 2, 3, 4, 5)
        val x1 = coords.random()
        val x2 = coords.random()
        val c1 = x1 * a11 + x2 * a21
        val c2 = x1 * a12 + x2 * a22
        val det = a11 * a22 - a12 * a21
        val askX1 = Random.nextBoolean()
        fun fmtV(x: Int, y: Int): String = if (y >= 0) "${x}e₁ + ${y}e₂" else "${x}e₁ − ${-y}e₂"
        val targetSym = if (askX1) "x₁" else "x₂"
        val identityStr = if (askX1) "x₁ = (c ∧ a₂) / (a₁ ∧ a₂)" else "x₂ = (a₁ ∧ c) / (a₁ ∧ a₂)"
        val numWedge = if (askX1) c1 * a22 - c2 * a21 else a11 * c2 - a12 * c1
        val expected = if (askX1) x1.toDouble() else x2.toDouble()
        wrapCuratedVariation(
            base = base,
            prompt = "Solve the 2-loop AC mesh system x₁ a₁ + x₂ a₂ = c in Cl₂,₀ without matrices using $identityStr. Find scalar $targetSym:",
            parametersLine1 = "a₁ = ${fmtV(a11, a12)},   a₂ = ${fmtV(a21, a22)}",
            parametersLine2 = "Source vector c = ${fmtV(c1, c2)}   (a ∧ a = 0)",
            hints = listOf(
                "Denominator wedge: a₁ ∧ a₂ = ($a11·($a22) − ($a12)·$a21)e₁₂ = ${det}e₁₂.",
                "Numerator wedge = ${numWedge}e₁₂ ⇒ divide by ${det}e₁₂."
            ),
            workedSteps = listOf(
                "1. a₁ ∧ a₂ = ${det}e₁₂; ${if (askX1) "c ∧ a₂" else "a₁ ∧ c"} = ${numWedge}e₁₂.",
                "2. $targetSym = $numWedge / $det = ${expected.toInt()}."
            ),
            expectedReal = expected
        )
    }

    "p_738l" -> {
        val (planeName, invAxis, fromAxis, toAxis) = listOf(
            listOf("e₂₃", "e₁", "e₂", "e₃"),
            listOf("e₃₁", "e₂", "e₃", "e₁"),
            listOf("e₁₂", "e₃", "e₁", "e₂")
        ).random()
        val (cHalf, sBase) = listOf(0.8 to 0.6, 0.6 to 0.8).random()
        val sSigned = if (Random.nextBoolean()) sBase else -sBase
        val vOrtho = listOf(2, 3, 4, 5, 6).random()
        val vMag = listOf(25, 50, 75, 100).random()
        val sinTheta = 2.0 * cHalf * sSigned
        val outComp = vMag * sinTheta
        val rSign = if (sSigned >= 0) "− ${sSigned}$planeName" else "+ ${-sSigned}$planeName"
        val rtSign = if (sSigned >= 0) "+ ${sSigned}$planeName" else "− ${-sSigned}$planeName"
        wrapCuratedVariation(
            base = base,
            prompt = "Phase-shift harmonic AC load vector v = ${vOrtho}$invAxis + ${vMag}$fromAxis in the $planeName harmonic plane via 3D rotor R = $cHalf $rSign (R̃ = $cHalf $rtSign). Find the $toAxis-component of v' = R v R̃:",
            parametersLine1 = "v = ${vOrtho}$invAxis + ${vMag}$fromAxis,   R = $cHalf $rSign   (||R|| = 1)",
            parametersLine2 = "v' = R v R̃   (${planeName}${fromAxis} = −$toAxis, ${fromAxis}${planeName} = +$toAxis)",
            hints = listOf(
                "${vOrtho}$invAxis commutes with $planeName and stays invariant.",
                "For $fromAxis rotated in $planeName: sin θ = 2($cHalf)($sSigned) = ${ComplexNum.formatClean(sinTheta)}."
            ),
            workedSteps = listOf(
                "1. $invAxis component stays invariant; $fromAxis rotates by sin θ = ${ComplexNum.formatClean(sinTheta)} toward $toAxis.",
                "2. $toAxis-component = $vMag × ${ComplexNum.formatClean(sinTheta)} = ${ComplexNum.formatClean(outComp)}."
            ),
            expectedReal = outComp
        )
    }

    else -> null
}
