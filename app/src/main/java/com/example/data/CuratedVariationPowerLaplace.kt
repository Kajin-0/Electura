package com.example.data

import com.example.model.CircuitProblem
import com.example.model.ComplexNum
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.exp
import kotlin.math.sqrt
import kotlin.random.Random

internal fun generatePowerLaplaceVariation(base: CircuitProblem): CircuitProblem? = when (base.id) {
    // ── POWER (16) ──
    "p_301a" -> {
        val (bp, bq, bs) = listOf(
            Triple(3, 4, 5), Triple(4, 3, 5),
            Triple(5, 12, 13), Triple(12, 5, 13),
            Triple(8, 15, 17), Triple(15, 8, 17),
            Triple(7, 24, 25), Triple(24, 7, 25)
        ).random()
        val scale = listOf(50, 100, 150, 200, 250).random()
        val p = bp * scale
        val qAbs = bq * scale
        val s = bs * scale
        val isLag = Random.nextBoolean()
        val qSignStr = if (isLag) "+" else "−"
        val pfTag = if (isLag) "lagging" else "leading"
        val vRms = listOf(100, 120, 200, 220, 240).random()
        val pf = p.toDouble() / s
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "S = ($p $qSignStr j$qAbs) VA",
            parametersLine2 = "V_rms = $vRms V",
            hints = listOf(
                "Apparent power |S| = √($p² + $qAbs²) = $s VA.",
                "pf = P / |S| = $p / $s."
            ),
            workedSteps = listOf(
                "1. |S| = √($p² + $qAbs²) = $s VA.",
                "2. pf = $p / $s = ${ComplexNum.formatClean(pf)} ($pfTag)."
            ),
            expectedReal = pf
        )
    }

    "p_302a" -> {
        val baseQuad = listOf(
            listOf(1, 2, 2, 3),
            listOf(2, 3, 6, 7),
            listOf(1, 4, 8, 9),
            listOf(2, 6, 9, 11),
            listOf(4, 4, 7, 9),
            listOf(3, 4, 12, 13),
            listOf(2, 5, 14, 15),
            listOf(6, 6, 7, 11)
        ).random()
        val scale = listOf(1, 2, 3).random()
        val i0 = baseQuad[0] * scale
        val swapAc = Random.nextBoolean()
        val i1 = (if (swapAc) baseQuad[2] else baseQuad[1]) * scale
        val i3 = (if (swapAc) baseQuad[1] else baseQuad[2]) * scale
        val ans = baseQuad[3] * scale
        val harm = listOf(3, 5, 7).random()
        val w1 = listOf(100, 200, 300).random()
        val wave1 = if (Random.nextBoolean()) "cos(${w1}t)" else "sin(${w1}t)"
        val wave2 = if (Random.nextBoolean()) "sin(${harm * w1}t)" else "cos(${harm * w1}t)"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "i(t) = $i0 + ${i1}√2 $wave1 + ${i3}√2 $wave2 A",
            parametersLine2 = "Orthogonal harmonics: I_rms = √(I₀² + I₁² + I_$harm²)",
            hints = listOf(
                "RMS components are I₀ = $i0 A, I₁ = $i1 A, I_$harm = $i3 A.",
                "Compute √($i0² + $i1² + $i3²)."
            ),
            workedSteps = listOf(
                "1. I₀ = $i0 A, I₁ = $i1 A, I_$harm = $i3 A.",
                "2. I_rms = √(${i0 * i0} + ${i1 * i1} + ${i3 * i3}) = $ans A."
            ),
            expectedReal = ans.toDouble()
        )
    }

    "p_314d" -> {
        val vMag = listOf(50, 100, 120, 150, 200, 220, 240, 250).random()
        val iRe = listOf(2, 3, 4, 5, 6, 8, 10).random()
        val iIm = listOf(-8, -6, -5, -4, -3, -2, 2, 3, 4, 5, 6, 8).random()
        val s = ComplexNum(vMag * iRe.toDouble(), -vMag * iIm.toDouble())
        val iStr = ComplexNum(iRe.toDouble(), iIm.toDouble()).formatRect()
        val iConjStr = ComplexNum(iRe.toDouble(), -iIm.toDouble()).formatRect()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_rms = $vMag∠0° V",
            parametersLine2 = "I_rms = ($iStr) A",
            methodNote = "Flip the imaginary sign of current I to form I* before multiplying by V.",
            hints = listOf(
                "The complex conjugate of ($iStr) is I* = $iConjStr.",
                "Multiply V = $vMag by I* = ($iConjStr)."
            ),
            workedSteps = listOf(
                "1. I* = $iConjStr A.",
                "2. S = $vMag · ($iConjStr) = ${s.formatRect()} VA (${s.formatPolar()})."
            ),
            expectedComplex = s
        )
    }

    "p_305b" -> {
        val (br, bx, bz) = listOf(
            Triple(3, 4, 5), Triple(4, 3, 5),
            Triple(5, 12, 13), Triple(12, 5, 13),
            Triple(8, 15, 17), Triple(15, 8, 17)
        ).random()
        val zScale = listOf(1, 2, 3, 4, 5).random()
        val xSign = if (Random.nextBoolean()) 1 else -1
        val r = br * zScale
        val x = bx * zScale * xSign
        val zMag = bz * zScale
        val vMult = listOf(2, 5, 10, 15, 20).random()
        val vMag = zMag * vMult
        val magSq = (r * r + x * x).toDouble()
        val ans = ComplexNum(vMag * r / magSq, -vMag * x / magSq)
        val zStr = ComplexNum(r.toDouble(), x.toDouble()).formatRect()
        val zConjStr = ComplexNum(r.toDouble(), -x.toDouble()).formatRect()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V = $vMag∠0° V rms",
            parametersLine2 = "Z = ($zStr) Ω",
            hints = listOf(
                "I = $vMag / ($zStr). Multiply by conjugate ($zConjStr).",
                "|Z|² = ${magSq.toInt()}."
            ),
            workedSteps = listOf(
                "1. I = $vMag($zConjStr) / ${magSq.toInt()}.",
                "2. I = ${ans.formatRect()} A (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_312c" -> {
        val vMag = listOf(100, 110, 120, 127, 150, 200, 220, 240, 277).random()
        val phi = listOf(-60, -45, -30, -15, 0, 15, 30, 45, 60)
            .filter { !(vMag == 200 && it == 0) }
            .random()
        val isPosSeq = Random.nextBoolean()
        val seqShift = if (isPosSeq) 30 else -30
        val seqLabel = if (isPosSeq) "positive sequence ABC" else "negative sequence ACB"
        val shiftStr = if (isPosSeq) "+30°" else "−30°"
        val vAb = ComplexNum.fromPolarDeg(vMag * sqrt(3.0), (phi + seqShift).toDouble())
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_an = $vMag∠${phi}° V rms  ($seqLabel)",
            parametersLine2 = "V_ab = √3 · V_an ∠$shiftStr",
            hints = listOf(
                "Magnitude |V_ab| = ${vMag}√3 ≈ ${ComplexNum.formatClean(vMag * sqrt(3.0))} V.",
                "In $seqLabel, V_ab shifts by $shiftStr: ${phi}° + ($shiftStr) = ${phi + seqShift}°."
            ),
            workedSteps = listOf(
                "1. V_ab = ${vMag}√3 ∠${phi + seqShift}° V = ${vAb.formatPolar()} V.",
                "2. Rectangular form: ${vAb.formatRect()} V."
            ),
            expectedComplex = vAb
        )
    }

    "p_318e" -> {
        val valid = listOf(100, 200, 250, 400, 500).flatMap { vRms ->
            listOf(100, 150, 200, 250, 400, 500).flatMap { omega ->
                listOf(50, 80, 100, 125, 150, 200, 250, 300, 400, 500).mapNotNull { cUf ->
                    val qKvar = (omega.toDouble() * vRms * vRms * cUf) * 1e-9
                    val tenths = qKvar * 10.0
                    if (qKvar in 0.8..9.6 && kotlin.math.abs(tenths - kotlin.math.round(tenths)) < 1e-9) {
                        Triple(qKvar, vRms, omega)
                    } else null
                }
            }
        }
        val (qKvar, vRms, omega) = valid.random()
        val qVar = qKvar * 1000.0
        val cUf = (qVar / (omega * vRms.toDouble() * vRms)) * 1_000_000.0
        val pKw = ComplexNum.formatClean(qKvar * listOf(0.75, 1.0, 1.25).random())
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Load: P = ${pKw}kW, Q = ${ComplexNum.formatClean(qKvar)}kVAR lag",
            parametersLine2 = "V_rms = $vRms V, ω = $omega rad/s",
            hints = listOf(
                "Capacitor supplies Q_C = ${qVar.toInt()} VAR.",
                "Solve C = ${qVar.toInt()} / ($omega × $vRms²) in Farads, then ×10⁶ for μF."
            ),
            workedSteps = listOf(
                "1. Q_C = ${qVar.toInt()} VAR.",
                "2. C = ${qVar.toInt()} / ($omega × ${vRms * vRms}) F = ${ComplexNum.formatClean(cUf)} μF."
            ),
            expectedReal = cUf
        )
    }

    "p_326g" -> {
        val rY = listOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 15).random()
        val xMag = listOf(2, 3, 4, 5, 6, 8, 9, 10, 12).random()
        val xY = if (Random.nextBoolean()) xMag else -xMag
        val rD = 3 * rY
        val xD = 3 * xY
        val zDelta = ComplexNum(rD.toDouble(), xD.toDouble())
        val ans = ComplexNum(rY.toDouble(), xY.toDouble())
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Z_Δ = (${zDelta.formatRect()}) Ω per phase",
            parametersLine2 = "Balanced 3φ Δ → Y Equivalence",
            inputHint = "Enter Z_Y in Ω (e.g. ${ans.formatRect()})",
            hints = listOf(
                "For equal delta legs, divide Z_Δ by 3.",
                "Z_Y = (${zDelta.formatRect()}) / 3."
            ),
            workedSteps = listOf(
                "1. Z_Y = Z_Δ / 3 = (${zDelta.formatRect()}) / 3 = ${ans.formatRect()} Ω."
            ),
            expectedComplex = ans
        )
    }

    "p_310c" -> {
        val (pf1, tanFactor) = listOf(0.6 to (4.0 / 3.0), 0.8 to 0.75).random()
        val scale = listOf(1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 6.0).random()
        val p1 = scale * pf1 * 2.0 // clean terminating kW
        val q1Abs = p1 * tanFactor
        val isLag = Random.nextBoolean()
        val pfType = if (isLag) "lag" else "lead"
        val q1 = if (isLag) q1Abs else -q1Abs
        val p2 = listOf(0.5, 0.8, 1.0, 1.2, 1.5, 2.0, 2.5, 3.0).random()
        val ans = ComplexNum(p1 + p2, q1)
        val p1Str = ComplexNum.formatClean(p1)
        val p2Str = ComplexNum.formatClean(p2)
        val qAbsStr = ComplexNum.formatClean(q1Abs)
        val qSignedStr = if (isLag) "+$qAbsStr" else "−$qAbsStr"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Load 1: ${p1Str}kW @ $pf1 pf $pfType",
            parametersLine2 = "Load 2: ${p2Str}kW @ 1.0 pf",
            hints = listOf(
                "Load 1 |S₁| = $p1Str / $pf1 = ${ComplexNum.formatClean(p1 / pf1)} kVA.",
                "Q₁ = $qSignedStr kVAR ($pfType); Load 2 has Q₂ = 0."
            ),
            workedSteps = listOf(
                "1. P_tot = $p1Str + $p2Str = ${ComplexNum.formatClean(p1 + p2)} kW.",
                "2. Q_tot = $qSignedStr kVAR ⇒ S_total = ${ans.formatRect()} kVA."
            ),
            expectedComplex = ans
        )
    }

    "p_322f" -> {
        val rTh = listOf(2, 3, 4, 5, 6, 8, 10, 12, 15, 20).random()
        val xTh = listOf(-12, -8, -6, -4, -3, 3, 4, 5, 6, 8, 10, 12).random()
        val vTh = listOf(20, 24, 30, 40, 48, 50, 60, 80, 100, 120).random()
        val pMax = (vTh * vTh) / (4.0 * rTh)
        val zThStr = ComplexNum(rTh.toDouble(), xTh.toDouble()).formatRect()
        val zOptStr = ComplexNum(rTh.toDouble(), -xTh.toDouble()).formatRect()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_th = $vTh∠0° V rms",
            parametersLine2 = "Z_th = ($zThStr) Ω",
            hints = listOf(
                "Optimal load Z_L = Z_th* = $zOptStr Ω makes total impedance ${2 * rTh} Ω.",
                "P_max = |V_th|² / (4 · $rTh)."
            ),
            workedSteps = listOf(
                "1. Z_tot = ($zThStr) + ($zOptStr) = ${2 * rTh} Ω.",
                "2. P_max = $vTh² / (4 × $rTh) = ${ComplexNum.formatClean(pMax)} W."
            ),
            expectedReal = pMax
        )
    }

    "p_330h" -> {
        val vLine = listOf(200, 208, 240, 300, 380, 400, 440, 480, 600).random()
        val iLine = listOf(10, 12, 15, 20, 25, 30, 40, 50).random()
        val (pfTag, pfNum) = listOf(
            "√3 / 2" to (sqrt(3.0) / 2.0),
            "1 / √3" to (1.0 / sqrt(3.0)),
            "0.8" to 0.8,
            "0.6" to 0.6
        ).random()
        val pKw = (sqrt(3.0) * vLine * iLine * pfNum) / 1000.0
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_L = $vLine V (RMS),   I_L = $iLine A (RMS)",
            parametersLine2 = "Power Factor pf = $pfTag",
            hints = listOf(
                "P_3φ = √3 × V_L × I_L × pf.",
                "Divide by 1000 to convert Watts to kW."
            ),
            workedSteps = listOf(
                "1. P_3φ = √3 × $vLine × $iLine × ($pfTag) = ${ComplexNum.formatClean(pKw * 1000)} W.",
                "2. = ${ComplexNum.formatClean(pKw)} kW."
            ),
            expectedReal = pKw
        )
    }

    "p_334i" -> {
        val w1 = listOf(2000, 3000, 4000, 5000, 6000, 8000).random()
        val w2 = listOf(-1000, 0, 1000, 1500, 2000, 3000, 4000, 5000, 6000)
            .filter { (w1 + it) >= 2000 }
            .random()
        val phiDeg = Math.toDegrees(atan(sqrt(3.0) * (w1 - w2) / (w1 + w2).toDouble()))
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "W₁ = $w1 W,   W₂ = $w2 W",
            parametersLine2 = "tan(φ) = √3 (W₁ − W₂) / (W₁ + W₂)",
            hints = listOf(
                "W₁ − W₂ = ${w1 - w2} W and W₁ + W₂ = ${w1 + w2} W.",
                "φ = atan(√3 × ${w1 - w2} / ${w1 + w2}) in degrees."
            ),
            workedSteps = listOf(
                "1. tan(φ) = √3(${w1 - w2}) / (${w1 + w2}) = ${ComplexNum.formatClean(sqrt(3.0) * (w1 - w2) / (w1 + w2).toDouble())}.",
                "2. φ = ${ComplexNum.formatClean(phiDeg)}°."
            ),
            expectedReal = phiDeg
        )
    }

    "p_334j" -> {
        val vL = listOf(120, 200, 220, 240, 400, 480).random()
        val iRe = listOf(4, 5, 6, 8, 10, 12).random()
        val iIm = listOf(-6, -5, -4, -3, -2, 2, 3, 4).random()
        val (rLine, xLine) = listOf(1 to 2, 2 to 3, 1 to 3, 2 to 4, 3 to 4).random()
        val dvRe = iRe * rLine - iIm * xLine
        val dvIm = iRe * xLine + iIm * rLine
        val vs = ComplexNum((vL + dvRe).toDouble(), dvIm.toDouble())
        val iStr = ComplexNum(iRe.toDouble(), iIm.toDouble()).formatRect()
        val dvStr = ComplexNum(dvRe.toDouble(), dvIm.toDouble()).formatRect()
        wrapCuratedVariation(
            base = base,
            prompt = "A feeder with Z_line feeds a load at V_L = $vL∠0° V drawing I = ($iStr) A. Find sending-end V_s (in V):",
            parametersLine1 = "V_L = ($vL + j0) V,   I = ($iStr) A",
            parametersLine2 = "Z_line = ($rLine + j$xLine) Ω",
            hints = listOf(
                "Line drop ΔV = I · Z_line = ($iStr)($rLine + j$xLine) = $dvStr V.",
                "Add V_L = $vL V."
            ),
            workedSteps = listOf(
                "1. ΔV = ($iStr)($rLine + j$xLine) = $dvStr V.",
                "2. V_s = $vL + ($dvStr) = ${vs.formatRect()} V (${vs.formatPolar()})."
            ),
            expectedComplex = vs
        )
    }

    "p_316e" -> {
        val v0i = listOf(10, 15, 20, 25, 30, 40, 50).random()
        val i0i = listOf(2, 3, 4, 5, 6, 8).random()
        val v1i = listOf(40, 50, 60, 80, 100, 120).random()
        val i1i = listOf(4, 6, 8, 10, 12).random()
        val v3i = listOf(10, 20, 25, 30, 40, 50).random()
        val i3i = listOf(2, 4, 5, 6, 8).random()
        val matched = Random.nextBoolean()
        val sign60 = if (Random.nextBoolean()) "− 60°" else "+ 60°"
        val p0 = v0i * i0i
        val p1 = (v1i * i1i) / 2
        val p3 = if (matched) v3i * i3i else 0
        val totalP = (p0 + p1 + p3).toDouble()
        val i3Wave = if (matched) "sin(3ωt)" else "cos(3ωt)"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v(t) = $v0i + ${v1i}√2 cos(ωt) + ${v3i}√2 sin(3ωt) V",
            parametersLine2 = "i(t) = $i0i + ${i1i}√2 cos(ωt $sign60) + ${i3i}√2 $i3Wave A",
            hints = listOf(
                "DC power = $v0i × $i0i = $p0 W; fundamental = $v1i × $i1i × cos(60°) = $p1 W.",
                if (matched) "Both 3rd harmonics are sin(3ωt), so P₃ = $v3i × $i3i = $p3 W." else "sin(3ωt) and cos(3ωt) are quadrature (90° apart), so P₃ = 0 W."
            ),
            workedSteps = listOf(
                "1. P₀ = $p0 W, P₁ = $v1i × $i1i × 0.5 = $p1 W.",
                "2. P₃ = $p3 W ⇒ P_total = ${totalP.toInt()} W."
            ),
            expectedReal = totalP
        )
    }

    "p_319f" -> {
        val pKw = listOf(12, 24, 36, 48, 60, 72, 84, 96, 120).random()
        val (srcPf, dstPf) = listOf(
            Triple("0.6", "4/3", 4.0 / 3.0) to Triple("0.8", "3/4", 0.75),
            Triple("0.6", "4/3", 4.0 / 3.0) to Triple("1.0", "0", 0.0),
            Triple("0.8", "3/4", 0.75) to Triple("1.0", "0", 0.0)
        ).random()
        val (pf1, t1Label, tan1) = srcPf
        val (pf2, t2Label, tan2) = dstPf
        val q1 = pKw * tan1
        val q2 = pKw * tan2
        val qC = q1 - q2
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Load P = $pKw kW,   pf₁ = $pf1 lag → pf₂ = $pf2 ${if (pf2 == "1.0") "" else "lag"}".trim(),
            parametersLine2 = "Q_C = P · (tan θ₁ − tan θ₂)",
            hints = listOf(
                "tan θ₁ = $t1Label and tan θ₂ = $t2Label.",
                "Q_C = $pKw × ($t1Label − $t2Label)."
            ),
            workedSteps = listOf(
                "1. Q₁ = $pKw($t1Label) = ${ComplexNum.formatClean(q1)} kVAR, Q₂ = ${ComplexNum.formatClean(q2)} kVAR.",
                "2. Q_C = ${ComplexNum.formatClean(qC)} kVAR."
            ),
            expectedReal = qC
        )
    }

    "p_324g" -> {
        val (br, bx, bz) = listOf(
            Triple(3, 4, 5), Triple(4, 3, 5),
            Triple(5, 12, 13), Triple(12, 5, 13),
            Triple(8, 15, 17), Triple(15, 8, 17)
        ).random()
        val scale = listOf(1, 2, 3, 4, 5).random()
        val rTh = br * scale
        val xTh = bx * scale
        val zMag = bz * scale
        val vTh = listOf(40, 50, 80, 100, 120, 200).random()
        val xSign = if (Random.nextBoolean()) "+ j$xTh" else "− j$xTh"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_th = $vTh∠0° V rms,   Z_th = ($rTh $xSign) Ω",
            parametersLine2 = "Load constrained to pure R_L ∈ ℝ⁺  (X_L = 0)",
            hints = listOf(
                "Purely resistive optimal match is R_L = |Z_th|, not R_th.",
                "|Z_th| = √($rTh² + $xTh²)."
            ),
            workedSteps = listOf(
                "1. R_L,opt = |Z_th| = √(${rTh * rTh} + ${xTh * xTh}) = $zMag Ω."
            ),
            expectedReal = zMag.toDouble()
        )
    }

    "p_315d" -> {
        val (xBase, yBase, iNBase) = listOf(
            Triple(1, 4, 7),
            Triple(11, 4, 13),
            Triple(13, 8, 19),
            Triple(2, 8, 14),
            Triple(22, 8, 26),
            Triple(3, 12, 21)
        ).random()
        val xSigned = if (Random.nextBoolean()) xBase else -xBase
        val ySigned = if (Random.nextBoolean()) yBase else -yBase
        val kShift = listOf(6, 8, 10, 12, 14).random()
        // Choose b2 = kShift + ySigned, c2 = kShift -> b2 - c2 = ySigned; a - (b2 + c2) = xSigned
        val b2 = kShift + ySigned
        val c2 = kShift
        val iB = 2 * b2
        val iC = 2 * c2
        val iA = b2 + c2 + xSigned
        val validA = if (iA >= 4) iA else {
            val bump = (4 - iA + 1) / 2
            val adjA = iA + 2 * bump
            adjA
        }
        val extra = (validA - (b2 + c2 + xSigned)) / 2
        val finalB = iB + 2 * extra
        val finalC = iC + 2 * extra
        val reN = validA - 0.5 * (finalB + finalC)
        val imCoeff = -0.5 * (finalB - finalC)
        val imSign = if (imCoeff >= 0) "+" else "−"
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "I_a = ${validA}∠0° A,   I_b = ${finalB}∠−120° A",
            parametersLine2 = "I_c = ${finalC}∠+120° A   (I_N = I_a + I_b + I_c)",
            hints = listOf(
                "I_b = −${finalB / 2} − j${finalB / 2}√3 A and I_c = −${finalC / 2} + j${finalC / 2}√3 A.",
                "I_N = ${ComplexNum.formatClean(reN)} $imSign j${ComplexNum.formatClean(kotlin.math.abs(imCoeff))}√3 A."
            ),
            workedSteps = listOf(
                "1. I_N = ${ComplexNum.formatClean(reN)} $imSign j${ComplexNum.formatClean(kotlin.math.abs(imCoeff))}√3 A.",
                "2. |I_N| = √(${ComplexNum.formatClean(reN)}² + 3×${ComplexNum.formatClean(kotlin.math.abs(imCoeff))}²) = $iNBase A."
            ),
            expectedReal = iNBase.toDouble()
        )
    }

    "p_328h" -> {
        val (b1, b3, bRms) = listOf(
            Triple(3, 4, 5), Triple(4, 3, 5),
            Triple(5, 12, 13), Triple(12, 5, 13),
            Triple(8, 15, 17), Triple(15, 8, 17)
        ).random()
        val scale = listOf(1, 2, 4, 5).random()
        val i1 = (b1 * scale).toDouble()
        val i3 = (b3 * scale).toDouble()
        val iRms = (bRms * scale).toDouble()
        val (cosTh, thDeg) = listOf(0.8 to 36.87, 0.6 to 53.13, 0.5 to 60.0).random()
        val thSignStr = if (Random.nextBoolean()) "− $thDeg°" else "+ $thDeg°"
        val vRms = listOf(100, 120, 200, 240).random()
        val pfTrue = (i1 / iRms) * cosTh
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "v(t) = $vRms√2 cos(ωt) V,   i(t) = ${i1.toInt()}√2 cos(ωt $thSignStr) + ${i3.toInt()}√2 cos(3ωt) A",
            parametersLine2 = "cos($thDeg°) = $cosTh   (pf_true = (I₁ / I_rms) · cos θ₁)",
            hints = listOf(
                "I_rms = √(${i1.toInt()}² + ${i3.toInt()}²) = ${iRms.toInt()} A.",
                "pf_true = (${i1.toInt()}/${iRms.toInt()}) × $cosTh."
            ),
            workedSteps = listOf(
                "1. Distortion factor I₁/I_rms = ${i1.toInt()}/${iRms.toInt()} = ${ComplexNum.formatClean(i1 / iRms)}.",
                "2. True pf = ${ComplexNum.formatClean(i1 / iRms)} × $cosTh = ${ComplexNum.formatClean(pfTrue)}."
            ),
            expectedReal = pfTrue
        )
    }

    // ── LAPLACE (19, including No. 492B starter!) ──
    "p_492b" -> {
        val (lH, cF, w) = listOf(
            Triple(0.5, 0.005, 20),
            Triple(0.25, 0.01, 20),
            Triple(1.0, 0.01, 10),
            Triple(0.2, 0.008, 25),
            Triple(0.4, 0.001, 50),
            Triple(0.25, 0.0016, 50),
            Triple(0.1, 0.004, 50),
            Triple(0.04, 0.01, 50),
            Triple(0.05, 0.002, 100),
            Triple(0.1, 0.001, 100),
            Triple(2.0, 0.005, 10)
        ).random()
        val rOhm = listOf(4, 5, 8, 10, 12, 15, 20).random()
        val ePk = listOf(40, 60, 80, 100, 120, 150, 180, 200, 240).random()
        val xRes = w * lH
        val iPeak = ComplexNum(ePk.toDouble() / rOhm, 0.0)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "L = ${lH}H, C = ${cF}F, R = ${rOhm}Ω",
            parametersLine2 = "E(t) = $ePk sin(${w}t) V",
            methodNote = "Use Bromwich contour or steady-state jω impedance at ω = $w rad/s.",
            hints = listOf(
                "Compute reactances at ω = $w rad/s: X_L = ωL and X_C = 1/(ωC).",
                "Notice X_L = ${ComplexNum.formatClean(xRes)} Ω equals X_C = ${ComplexNum.formatClean(xRes)} Ω, so Z = ${rOhm} + j0 Ω."
            ),
            workedSteps = listOf(
                "1. ω = $w rad/s ⇒ X_L = X_C = ${ComplexNum.formatClean(xRes)} Ω.",
                "2. Z = $rOhm + j0 Ω (series resonance).",
                "3. I = $ePk∠0° / $rOhm = ${iPeak.formatRect()} A."
            ),
            expectedComplex = iPeak
        )
    }

    "p_400a" -> {
        val rK = listOf(2, 4, 5, 10, 20, 25, 40, 50, 80, 100).random()
        val cUf = listOf(5, 10, 20, 25, 40, 50, 80, 100, 200).random()
        val tau = (rK * cUf) / 1000.0
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = $rK kΩ,   C = $cUf μF",
            parametersLine2 = "τ = R · C  (exponential time e^(−t/τ))",
            hints = listOf(
                "τ = ($rK × 10³) × ($cUf × 10⁻⁶) s.",
                "Product equals ${rK * cUf} × 10⁻³ = ${ComplexNum.formatClean(tau)} s."
            ),
            workedSteps = listOf(
                "1. τ = ($rK,000 Ω) × ($cUf × 10⁻⁶ F) = ${ComplexNum.formatClean(tau)} s."
            ),
            expectedReal = tau
        )
    }

    "p_401a" -> {
        val k = listOf(2, 5, 8, 10, 12, 15, 20, 25, 40, 50).random()
        val p = listOf(5, 10, 15, 20, 25, 30, 40, 50, 60, 75, 80, 100, 120).random()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "H(s) = $k / (s + $p)",
            parametersLine2 = "Set denominator D(s) = 0 to locate pole",
            methodNote = "Solve s + $p = 0 for the root where H(s) → ∞.",
            hints = listOf(
                "Poles satisfy s + $p = 0.",
                "Include the negative LHP sign."
            ),
            workedSteps = listOf(
                "1. Set s + $p = 0 ⇒ s₀ = −$p rad/s."
            ),
            expectedReal = -p.toDouble()
        )
    }

    "p_402a" -> {
        val (wRatioLabel, kVal, kSq) = listOf(
            Triple("2/(RC) = 2ω_c", 2.0, "4"),
            Triple("3/(RC) = 3ω_c", 3.0, "9"),
            Triple("4/(RC) = 4ω_c", 4.0, "16"),
            Triple("√3/(RC) = √3 ω_c", sqrt(3.0), "3"),
            Triple("1/(√3 RC) = ω_c / √3", 1.0 / sqrt(3.0), "1/3"),
            Triple("0.5/(RC) = ω_c / 2", 0.5, "0.25"),
            Triple("3/(4RC) = (3/4)ω_c", 0.75, "9/16"),
            Triple("4/(3RC) = (4/3)ω_c", 4.0 / 3.0, "16/9")
        ).random()
        val mag = 1.0 / sqrt(1.0 + kVal * kVal)
        wrapCuratedVariation(
            base = base,
            prompt = "Find the transfer magnitude |H(jω)| of an RC low-pass filter at ω = ${wRatioLabel.substringAfter("= ")}:",
            parametersLine1 = "H(s) = 1 / (1 + sRC)",
            parametersLine2 = "Evaluate at ω = $wRatioLabel",
            inputHint = "Enter magnitude (e.g. ${ComplexNum.formatClean(mag)})",
            hints = listOf(
                "Substitute sRC = j(ω/ω_c) ⇒ H = 1 / (1 + j${ComplexNum.formatClean(kVal)}).",
                "|H| = 1 / √(1 + $kSq)."
            ),
            workedSteps = listOf(
                "1. ωRC = ${ComplexNum.formatClean(kVal)}.",
                "2. |H| = 1 / √(1 + $kSq) ≈ ${ComplexNum.formatClean(mag)}."
            ),
            expectedReal = mag
        )
    }

    "p_403a" -> {
        val lMh = listOf(10, 20, 25, 30, 40, 50, 60, 75, 80, 100, 120, 150, 200).random()
        val rOhm = listOf(5, 8, 10, 12, 15, 20, 25, 30, 40, 50).random()
        val tauMs = lMh.toDouble() / rOhm
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "L = $lMh mH,   R = $rOhm Ω",
            parametersLine2 = "τ = L / R  (natural response e^(−t/τ))",
            hints = listOf(
                "τ = ${ComplexNum.formatClean(lMh / 1000.0)} H / $rOhm Ω.",
                "Dividing mH by Ω yields milliseconds directly."
            ),
            workedSteps = listOf(
                "1. τ = $lMh mH / $rOhm Ω = ${ComplexNum.formatClean(tauMs)} ms."
            ),
            expectedReal = tauMs
        )
    }

    "p_407b" -> {
        val (label, ratio) = listOf(
            "ω = 3ω_c" to 3.0,
            "ω = 2ω_c" to 2.0,
            "ω = √3 ω_c" to sqrt(3.0),
            "ω = (4/3) ω_c" to (4.0 / 3.0),
            "ω = ω_c" to 1.0,
            "ω = (3/4) ω_c" to 0.75,
            "ω = ω_c / √3" to (1.0 / sqrt(3.0)),
            "ω = ω_c / 2" to 0.5,
            "ω = ω_c / 3" to (1.0 / 3.0)
        ).random()
        val phaseDeg = 90.0 - Math.toDegrees(atan(ratio))
        wrapCuratedVariation(
            base = base,
            prompt = "Evaluate the output phase angle ∠H(jω) (in degrees) of the first-order RC high-pass filter at $label:",
            parametersLine1 = "H(s) = sRC / (1 + sRC)",
            parametersLine2 = "Evaluate at $label  (ω_c = 1/(RC))",
            methodNote = "Substitute sRC = j${ComplexNum.formatClean(ratio)}, giving ∠H = 90° − atan(ω/ω_c).",
            hints = listOf(
                "Numerator angle ∠j = +90°; denominator angle atan(${ComplexNum.formatClean(ratio)}).",
                "Subtract denominator phase from 90°."
            ),
            workedSteps = listOf(
                "1. ∠H = 90° − atan(${ComplexNum.formatClean(ratio)}) = 90° − ${ComplexNum.formatClean(90.0 - phaseDeg)}° = ${ComplexNum.formatClean(phaseDeg)}°."
            ),
            expectedReal = phaseDeg
        )
    }

    "p_496d" -> {
        val k = listOf(6, 8, 10, 12, 15, 18, 20, 24, 30, 36).random()
        val p1 = listOf(1, 2, 3, 4, 5).random()
        val p2 = listOf(2, 3, 4, 5, 6, 8).random()
        val z = listOf(1, 2, 3, 4, 5, 6).filter { it != p1 && it != p2 }.randomOrNull() ?: 7
        val yInf = (k * z).toDouble() / (p1 * p2)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Y(s) = $k(s + $z) / (s(s + $p1)(s + $p2))",
            parametersLine2 = "y(∞) = lim(s→0) s·Y(s)",
            hints = listOf(
                "Cancel s: s·Y(s) = $k(s + $z) / ((s + $p1)(s + $p2)).",
                "Evaluate at s = 0: ($k × $z) / ($p1 × $p2)."
            ),
            workedSteps = listOf(
                "1. y(∞) = ($k × $z) / ($p1 × $p2) = ${ComplexNum.formatClean(yInf)}."
            ),
            expectedReal = yInf
        )
    }

    "p_497f" -> {
        val (magStr, magNum) = listOf(
            "0.001" to 0.001, "0.005" to 0.005, "0.01" to 0.01, "0.02" to 0.02,
            "0.05" to 0.05, "0.1" to 0.1, "0.2" to 0.2, "0.25" to 0.25, "0.5" to 0.5,
            "2" to 2.0, "4" to 4.0, "5" to 5.0, "10" to 10.0, "20" to 20.0,
            "50" to 50.0, "100" to 100.0, "200" to 200.0, "1000" to 1000.0
        ).random()
        val dbVal = 20.0 * kotlin.math.log10(magNum)
        wrapCuratedVariation(
            base = base,
            prompt = "An amplifier/filter has transfer magnitude |H(jω)| = $magStr. Find the voltage gain in dB:",
            parametersLine1 = "|H(jω)| = $magStr",
            parametersLine2 = "Gain_dB = 20 · log₁₀|H(jω)|",
            inputHint = "Enter gain in dB (e.g. ${ComplexNum.formatClean(dbVal)})",
            hints = listOf(
                "Evaluate log₁₀($magStr).",
                "Multiply by 20."
            ),
            workedSteps = listOf(
                "1. 20 · log₁₀($magStr) = ${ComplexNum.formatClean(dbVal)} dB."
            ),
            expectedReal = dbVal
        )
    }

    "p_499e" -> {
        val vStep = listOf(20, 24, 30, 40, 50, 60, 80, 100, 120, 150).random()
        val rK = listOf(5, 10, 20, 25, 40, 50).random()
        val cUf = listOf(10, 20, 25, 40, 50, 100).random()
        val k = listOf(0.5, 1.0, 1.5, 2.0, 3.0).random()
        val tauSec = rK * cUf / 1000.0
        val tSec = k * tauSec
        val vOut = vStep * (1.0 - exp(-k))
        wrapCuratedVariation(
            base = base,
            prompt = "Uncharged C (v_C(0⁻) = 0 V) is energized at t = 0 by V_s = ${vStep}V through R. Find v_C(t) at t = ${ComplexNum.formatClean(k)}τ:",
            parametersLine1 = "V_s = $vStep V step, R = $rK kΩ, C = $cUf μF",
            parametersLine2 = "Evaluate at t = ${ComplexNum.formatClean(k)}τ = ${ComplexNum.formatClean(tSec)} s (τ = ${ComplexNum.formatClean(tauSec)} s)",
            hints = listOf(
                "At t = ${ComplexNum.formatClean(k)}τ, e^(−t/τ) = e^(−${ComplexNum.formatClean(k)}) ≈ ${ComplexNum.formatClean(exp(-k))}.",
                "v_C = $vStep × (1 − ${ComplexNum.formatClean(exp(-k))})."
            ),
            workedSteps = listOf(
                "1. τ = $rK kΩ × $cUf μF = ${ComplexNum.formatClean(tauSec)} s.",
                "2. v_C = $vStep(1 − e^(−${ComplexNum.formatClean(k)})) ≈ ${ComplexNum.formatClean(vOut)} V."
            ),
            expectedReal = vOut
        )
    }

    "p_495c" -> {
        val p1 = (1..5).random()
        val p2 = (2..8).random()
        val a2 = p1 + p2
        val a1 = p1 * p2
        val kCrit = (a2 * a1).toDouble()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "G(s)H(s) = K / (s(s+$p1)(s+$p2))",
            parametersLine2 = "1 + G(s)H(s) = 0",
            methodNote = "Expand characteristic polynomial s³ + ${a2}s² + ${a1}s + K = 0 and check row s¹.",
            hints = listOf(
                "Characteristic equation: s³ + ${a2}s² + ${a1}s + K = 0.",
                "Routh condition requires a₂·a₁ > K, so K_crit = $a2 × $a1."
            ),
            workedSteps = listOf(
                "1. s(s + $p1)(s + $p2) + K = s³ + ${a2}s² + ${a1}s + K = 0.",
                "2. K_crit = $a2 × $a1 = ${kCrit.toInt()}."
            ),
            expectedReal = kCrit
        )
    }

    "p_498e" -> {
        val (lH, cF) = listOf(
            0.5 to 0.02,
            0.25 to 0.01,
            1.0 to 0.01,
            0.2 to 0.008,
            0.4 to 0.001,
            0.04 to 0.01,
            0.1 to 0.001
        ).random()
        val r = listOf(2, 4, 5, 6, 8, 10, 12, 16, 20).random()
        val w0 = 1.0 / sqrt(lH * cF)
        val alpha = r / (2.0 * lH)
        val zeta = alpha / w0
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω, L = ${lH}H",
            parametersLine2 = "C = ${cF}F",
            hints = listOf(
                "ω₀ = 1 / √($lH × $cF) = ${ComplexNum.formatClean(w0)} rad/s.",
                "α = $r / (2 × $lH) = ${ComplexNum.formatClean(alpha)} s⁻¹, so ζ = α / ω₀."
            ),
            workedSteps = listOf(
                "1. ω₀ = ${ComplexNum.formatClean(w0)} rad/s, α = ${ComplexNum.formatClean(alpha)} s⁻¹.",
                "2. ζ = ${ComplexNum.formatClean(alpha)} / ${ComplexNum.formatClean(w0)} = ${ComplexNum.formatClean(zeta)}."
            ),
            expectedReal = zeta
        )
    }

    "p_499d" -> {
        val (br, bx) = listOf(3 to 4, 4 to 3, 5 to 12, 12 to 5, 6 to 8, 8 to 6).random()
        val xNetSign = if (Random.nextBoolean()) 1 else -1
        val r = br
        val xNetSigned = bx * xNetSign
        val w = listOf(10, 20, 25, 40, 50).random()
        val xL: Int
        val xC: Int
        if (xNetSigned > 0) {
            xC = listOf(2, 4, 5, 8, 10).random()
            xL = xNetSigned + xC
        } else {
            xL = listOf(2, 4, 5, 6, 8, 10).random()
            xC = (-xNetSigned) + xL
        }
        val lH = xL.toDouble() / w
        val cMf = 1000.0 / (w * xC)
        val eMag = listOf(25, 50, 100, 130, 169, 200).random()
        val xNet = xNetSigned.toDouble()
        val zMagSq = r * r + xNet * xNet
        val ans = ComplexNum(eMag * r / zMagSq, -eMag * xNet / zMagSq)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω, L = ${ComplexNum.formatClean(lH)}H, C = ${ComplexNum.formatClean(cMf)}mF",
            parametersLine2 = "E = $eMag∠0° V, ω = $w rad/s",
            hints = listOf(
                "X_L = $xL Ω; X_C = $xC Ω ⇒ Z = ${ComplexNum(r.toDouble(), xNet).formatRect()} Ω.",
                "Compute I = $eMag / Z."
            ),
            workedSteps = listOf(
                "1. Z = ${ComplexNum(r.toDouble(), xNet).formatRect()} Ω (|Z|² = ${zMagSq.toInt()}).",
                "2. I = $eMag / Z = ${ans.formatRect()} A (${ans.formatPolar()})."
            ),
            expectedComplex = ans
        )
    }

    "p_497d" -> {
        val candidates = listOf(5, 10, 15, 20, 25, 30, 40, 50, 60).flatMap { z0 ->
            listOf(10, 20, 25, 40, 50, 100, 200).mapNotNull { cUf ->
                val prod = z0 * z0 * cUf
                if (prod % 1000 == 0 && (prod / 1000) in 5..360) {
                    Triple(prod / 1000, cUf, z0)
                } else null
            }
        }
        val (lMh, cUf, rootZ0) = candidates.random()
        val rCrit = (2 * rootZ0).toDouble()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "L = $lMh mH,   C = $cUf μF",
            parametersLine2 = "Critical damping condition: ζ = α / ω₀ = 1",
            hints = listOf(
                "√(L/C) = √(${lMh}×10⁻³ / ${cUf}×10⁻⁶) = $rootZ0 Ω.",
                "R_crit = 2 × √(L/C) = 2 × $rootZ0."
            ),
            workedSteps = listOf(
                "1. √(L/C) = $rootZ0 Ω.",
                "2. R_crit = 2 × $rootZ0 = ${rCrit.toInt()} Ω."
            ),
            expectedReal = rCrit
        )
    }

    "p_499f" -> {
        val vs = listOf(12, 18, 24, 30, 36, 48, 60).random()
        val r = listOf(4, 5, 8, 10, 12, 15).random()
        val lH = listOf(0.1, 0.2, 0.25, 0.4, 0.5, 0.8).random()
        val vc0 = listOf(0, 2, 4, 6, 8, 10, 12, 20, 30, 40)
            .filter { it != vs && !(vs == 24 && it == 4 && lH == 0.2) }
            .random()
        val slope = (vs - vc0) / lH
        wrapCuratedVariation(
            base = base,
            prompt = "A series RLC with i(0⁻) = 0 and v_C(0⁻) = ${vc0}V is switched onto V_s = ${vs}V at t = 0. Find di/dt(0⁺):",
            parametersLine1 = "V_s = $vs V step, R = ${r}Ω, L = $lH H",
            parametersLine2 = "Initial state: i(0⁻) = 0 A, v_C(0⁻) = $vc0 V",
            hints = listOf(
                "At t = 0⁺, i(0⁺) = 0 ⇒ i(0⁺)R = 0 V.",
                "L·di/dt(0⁺) = $vs − $vc0 = ${vs - vc0} V."
            ),
            workedSteps = listOf(
                "1. Continuity: i(0⁺) = 0 A, v_C(0⁺) = $vc0 V.",
                "2. di/dt(0⁺) = ($vs − $vc0) / $lH = ${ComplexNum.formatClean(slope)} A/s."
            ),
            expectedReal = slope
        )
    }

    "p_405b" -> {
        val k = (2..12).random()
        val c1 = (4..18).random()
        val c0 = (1..12).random()
        val p1 = (1..5).random()
        val p2 = (2..7).random()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Y(s) = (${k}s² + ${c1}s + $c0) / (s(s + $p1)(s + $p2))",
            parametersLine2 = "y(0⁺) = lim(s→∞) s · Y(s)",
            hints = listOf(
                "s·Y(s) = (${k}s² + ${c1}s + $c0) / (s² + ${p1 + p2}s + ${p1 * p2}).",
                "Leading s² ratio is $k / 1."
            ),
            workedSteps = listOf(
                "1. lim(s→∞) s·Y(s) = $k / 1 = $k."
            ),
            expectedReal = k.toDouble()
        )
    }

    "p_498f" -> {
        val v0 = listOf(0, 10, 12, 20, 25, 30, 40, 50, 60, 80).random()
        val vInf = listOf(0, 10, 15, 20, 30, 40, 50).filter { it != v0 }.random()
        val kTau = listOf(0.5, 1.0, 2.0, 3.0).random()
        val vOut = vInf + (v0 - vInf) * exp(-kTau)
        val kStr = ComplexNum.formatClean(kTau)
        val tLabel = if (kTau == 1.0) "τ" else "${kStr}τ"
        wrapCuratedVariation(
            base = base,
            prompt = "A capacitor pre-charged to v_C(0⁻) = $v0 V is switched onto step source V_s = $vInf V at t = 0 through R. Find v_C(t) at t = $tLabel:",
            parametersLine1 = "v_C(0⁻) = $v0 V,   V_s = $vInf V step at t = 0",
            parametersLine2 = "Evaluate at t = $tLabel = ${if (kTau == 1.0) "" else "$kStr·"}R·C   (V_∞ = $vInf V)",
            hints = listOf(
                "v_C($tLabel) = $vInf + ($v0 − $vInf) e^(−$kStr).",
                "e^(−$kStr) ≈ ${ComplexNum.formatClean(exp(-kTau))}."
            ),
            workedSteps = listOf(
                "1. v_C(t) = $vInf + (${v0 - vInf})e^(−t/τ).",
                "2. At t = $tLabel: v_C ≈ ${ComplexNum.formatClean(vOut)} V."
            ),
            expectedReal = vOut
        )
    }

    "p_498g" -> {
        val w0 = listOf(25, 50, 100, 125, 150, 200, 250).random()
        val (zeta, rootComp) = listOf(
            0.28 to 0.96,
            0.6 to 0.8,
            0.8 to 0.6,
            0.96 to 0.28
        ).random()
        val wd = w0 * rootComp
        val tpMs = 1000.0 * Math.PI / wd
        wrapCuratedVariation(
            base = base,
            prompt = "An underdamped 2nd-order RLC step response has natural frequency ω₀ = $w0 rad/s and damping ratio ζ = $zeta. Find peak overshoot time t_p (in ms):",
            parametersLine1 = "ω₀ = $w0 rad/s,   ζ = $zeta  (underdamped)",
            parametersLine2 = "ω_d = ω₀√(1 − ζ²),   t_p = π / ω_d",
            hints = listOf(
                "√(1 − $zeta²) = $rootComp ⇒ ω_d = $w0 × $rootComp = ${ComplexNum.formatClean(wd)} rad/s.",
                "t_p = π / ${ComplexNum.formatClean(wd)} s × 1000 ms/s."
            ),
            workedSteps = listOf(
                "1. ω_d = ${ComplexNum.formatClean(wd)} rad/s.",
                "2. t_p = π / ${ComplexNum.formatClean(wd)} s ≈ ${ComplexNum.formatClean(tpMs)} ms."
            ),
            expectedReal = tpMs
        )
    }

    "p_496e" -> {
        val qVal = listOf(3, 4, 5, 6, 8, 10, 12).random()
        val bw = listOf(4, 5, 8, 10, 12, 15, 20, 25).random()
        val w0 = qVal * bw
        val kGain = listOf(1, 2, 3, 4, 5).random()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "H(s) = ${kGain * bw}s / (s² + ${bw}s + ${w0 * w0})",
            parametersLine2 = "Canonical Form: H(s) = K·(ω₀/Q)s / (s² + (ω₀/Q)s + ω₀²)",
            hints = listOf(
                "ω₀ = √${w0 * w0} = $w0 rad/s.",
                "BW = ω₀/Q = $bw rad/s ⇒ Q = $w0 / $bw."
            ),
            workedSteps = listOf(
                "1. ω₀ = $w0 rad/s, BW = $bw rad/s.",
                "2. Q = $w0 / $bw = $qVal."
            ),
            expectedReal = qVal.toDouble()
        )
    }

    "p_498h" -> {
        val (bwGc, ba, bHyp) = listOf(
            Triple(3, 4, 5), Triple(4, 3, 5),
            Triple(5, 12, 13), Triple(12, 5, 13),
            Triple(8, 15, 17), Triple(15, 8, 17),
            Triple(7, 24, 25), Triple(24, 7, 25)
        ).random()
        val scale = listOf(1, 2, 3, 4).random()
        val wGc = bwGc * scale
        val a = ba * scale
        val hyp = bHyp * scale
        val kGain = wGc * hyp
        val pmDeg = Math.toDegrees(atan2(a.toDouble(), wGc.toDouble()))
        val poleAng = Math.toDegrees(atan2(wGc.toDouble(), a.toDouble()))
        wrapCuratedVariation(
            base = base,
            prompt = "A unity-feedback open-loop system G(s) has gain crossover frequency ω_gc = $wGc rad/s (where |G(jω_gc)| = 1). Find its phase margin PM (in degrees):",
            parametersLine1 = "G(s) = $kGain / (s · (s + $a))",
            parametersLine2 = "|G(j$wGc)| = $kGain / ($wGc · √($wGc² + $a²)) = 1.0,   PM = 180° + ∠G(jω_gc)",
            inputHint = "Enter PM in degrees (e.g. ${ComplexNum.formatClean(pmDeg)})",
            hints = listOf(
                "∠G(j$wGc) = −90° − atan($wGc/$a) = −90° − ${ComplexNum.formatClean(poleAng)}°.",
                "PM = 180° + ∠G(j$wGc) = 90° − ${ComplexNum.formatClean(poleAng)}°."
            ),
            workedSteps = listOf(
                "1. ∠G(j$wGc) = −90° − ${ComplexNum.formatClean(poleAng)}° = −${ComplexNum.formatClean(90.0 + poleAng)}°.",
                "2. PM = 180° − ${ComplexNum.formatClean(90.0 + poleAng)}° = ${ComplexNum.formatClean(pmDeg)}°."
            ),
            expectedReal = pmDeg
        )
    }

    else -> null
}
