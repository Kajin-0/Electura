package com.example.data

import com.example.model.CircuitProblem
import com.example.model.ComplexNum
import kotlin.math.atan
import kotlin.math.exp
import kotlin.math.sqrt
import kotlin.random.Random

internal fun generatePowerLaplaceVariation(base: CircuitProblem): CircuitProblem? = when (base.id) {
    // ── POWER (12) ──
    "p_301a" -> {
        val (p, q, s) = listOf(
            Triple(600, 800, 1000),
            Triple(300, 400, 500),
            Triple(1200, 500, 1300),
            Triple(500, 1200, 1300),
            Triple(1500, 800, 1700),
            Triple(900, 1200, 1500)
        ).random()
        val pf = p.toDouble() / s
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "S = ($p + j$q) VA",
            parametersLine2 = "V_rms = 200 V",
            hints = listOf(
                "Apparent power |S| = √($p² + $q²) = $s VA.",
                "pf = P / |S| = $p / $s."
            ),
            workedSteps = listOf(
                "1. |S| = √($p² + $q²) = $s VA.",
                "2. pf = $p / $s = ${ComplexNum.formatClean(pf)} (lagging)."
            ),
            expectedReal = pf
        )
    }

    "p_302a" -> {
        val (i0, i1, i3, ans) = listOf(
            listOf(2, 3, 6, 7),
            listOf(1, 4, 8, 9),
            listOf(2, 6, 9, 11),
            listOf(4, 4, 7, 9),
            listOf(4, 8, 8, 12),
            listOf(6, 6, 7, 11)
        ).random()
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "i(t) = $i0 + $i1√2 cos(100t) + $i3√2 sin(300t) A",
            parametersLine2 = "Orthogonal harmonics: I_rms = √(I₀² + I₁² + I₃²)",
            hints = listOf(
                "RMS components are I₀ = $i0 A, I₁ = $i1 A, I₃ = $i3 A.",
                "Compute √($i0² + $i1² + $i3²)."
            ),
            workedSteps = listOf(
                "1. I₀ = $i0 A, I₁ = $i1 A, I₃ = $i3 A.",
                "2. I_rms = √(${i0 * i0} + ${i1 * i1} + ${i3 * i3}) = $ans A."
            ),
            expectedReal = ans.toDouble()
        )
    }

    "p_314d" -> {
        val vMag = listOf(50, 120, 200, 250).random()
        val (iRe, iIm) = listOf(
            3 to -4, 4 to 3, 6 to -8, 8 to 6, 5 to -2, 3 to 5
        ).random()
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
        val (vMag, r, x) = listOf(
            Triple(100, 6, 8), Triple(50, 3, 4), Triple(150, 8, 6), Triple(169, 5, 12), Triple(200, 8, -6)
        ).random()
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
        val vMag = listOf(100, 120, 150, 240, 277).random()
        val phi = listOf(-30, 0, 15, 30, 45).filter { !(vMag == 200 && it == 0) }.random()
        val vAb = ComplexNum.fromPolarDeg(vMag * sqrt(3.0), phi + 30.0)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_an = $vMag∠${phi}° V rms  (positive sequence)",
            parametersLine2 = "V_ab = √3 · V_an ∠+30°",
            hints = listOf(
                "Magnitude |V_ab| = ${vMag}√3 ≈ ${ComplexNum.formatClean(vMag * sqrt(3.0))} V.",
                "Angle adds +30°: ${phi}° + 30° = ${phi + 30}°."
            ),
            workedSteps = listOf(
                "1. V_ab = ${vMag}√3 ∠${phi + 30}° V = ${vAb.formatPolar()} V.",
                "2. Rectangular form: ${vAb.formatRect()} V."
            ),
            expectedComplex = vAb
        )
    }

    "p_318e" -> {
        val (qKvar, vRms, omega) = listOf(
            Triple(2.0, 200, 100),
            Triple(3.2, 200, 200),
            Triple(1.8, 100, 100),
            Triple(4.0, 200, 250),
            Triple(0.9, 100, 200),
            Triple(2.4, 200, 150)
        ).random()
        val qVar = qKvar * 1000.0
        val cUf = (qVar / (omega * vRms.toDouble() * vRms)) * 1_000_000.0
        val pKw = ComplexNum.formatClean(qKvar * 0.75)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Load: P = ${pKw}kW, Q = ${qKvar}kVAR lag",
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
        val (rY, xMag) = listOf(
            5 to 4, 6 to 2, 3 to 5, 8 to 6, 7 to 2, 10 to 5
        ).random()
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
        val (p1, pf1, p2) = listOf(
            Triple(1.5, 0.6, 1.0),
            Triple(2.4, 0.8, 0.6),
            Triple(3.0, 0.6, 1.2),
            Triple(4.0, 0.8, 1.5),
            Triple(0.9, 0.6, 1.1)
        ).random()
        val s1Mag = p1 / pf1
        val q1 = sqrt(s1Mag * s1Mag - p1 * p1)
        val ans = ComplexNum(p1 + p2, q1)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "Load 1: ${p1}kW @ $pf1 pf lag",
            parametersLine2 = "Load 2: ${p2}kW @ 1.0 pf",
            hints = listOf(
                "Load 1 |S₁| = $p1 / $pf1 = ${ComplexNum.formatClean(s1Mag)} kVA.",
                "Q₁ = √(${ComplexNum.formatClean(s1Mag)}² − $p1²) = ${ComplexNum.formatClean(q1)} kVAR; Load 2 has Q₂ = 0."
            ),
            workedSteps = listOf(
                "1. P_tot = $p1 + $p2 = ${ComplexNum.formatClean(p1 + p2)} kW.",
                "2. Q_tot = +${ComplexNum.formatClean(q1)} kVAR ⇒ S_total = ${ans.formatRect()} kVA."
            ),
            expectedComplex = ans
        )
    }

    "p_322f" -> {
        val (vTh, rTh, xTh) = listOf(
            Triple(40, 5, 4), Triple(30, 3, 4), Triple(24, 6, 8), Triple(50, 10, 5), Triple(60, 15, 10)
        ).random()
        val pMax = (vTh * vTh) / (4.0 * rTh)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "V_th = $vTh∠0° V rms",
            parametersLine2 = "Z_th = ($rTh + j$xTh) Ω",
            hints = listOf(
                "Optimal load Z_L = Z_th* = $rTh − j$xTh Ω makes total impedance ${2 * rTh} Ω.",
                "P_max = |V_th|² / (4 · $rTh)."
            ),
            workedSteps = listOf(
                "1. Z_tot = ($rTh + j$xTh) + ($rTh − j$xTh) = ${2 * rTh} Ω.",
                "2. P_max = $vTh² / (4 × $rTh) = ${ComplexNum.formatClean(pMax)} W."
            ),
            expectedReal = pMax
        )
    }

    "p_330h" -> {
        val (vL, iL, pfTag, pfVal) = listOf(
            listOf(400, 15, "√3 / 2", sqrt(3.0) / 2.0),
            listOf(480, 25, "√3 / 2", sqrt(3.0) / 2.0),
            listOf(200, 30, "√3 / 2", sqrt(3.0) / 2.0),
            listOf(600, 20, "√3 / 2", sqrt(3.0) / 2.0),
            listOf(400, 25, "1 / √3", 1.0 / sqrt(3.0)),
            listOf(300, 20, "1 / √3", 1.0 / sqrt(3.0))
        ).random()
        val vLine = vL as Int
        val iLine = iL as Int
        val pfNum = pfVal as Double
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
        val (w1, w2) = listOf(
            6000 to 3000,
            5000 to 0,
            6000 to 2000,
            5000 to 1000,
            3000 to 3000,
            4000 to -1000
        ).random()
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
        val vL = listOf(120, 220, 240, 400).random()
        val (iRe, iIm) = listOf(
            6 to -4, 10 to -5, 8 to -4, 5 to 3, 10 to -2
        ).random()
        val (rLine, xLine) = listOf(1 to 2, 2 to 3, 1 to 3, 2 to 4).random()
        // I * Z_line = (iRe + j*iIm)*(rLine + j*xLine)
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

    // ── LAPLACE (14, including No. 492B starter!) ──
    "p_492b" -> {
        val (l, c, omega, r, ePeak) = listOf(
            listOf(0.5, 0.005, 20, 5, 100),
            listOf(0.25, 0.01, 20, 10, 120),
            listOf(0.2, 0.005, 31.623, 8, 80), // use exact rational omega below instead!
            listOf(0.4, 0.01, 15.811, 10, 100)
        ).let {
            listOf(
                listOf(0.5, 0.005, 20, 5, 100),
                listOf(0.25, 0.01, 20, 10, 120),
                listOf(0.2, 0.02, 15.811, 8, 80) // prefer exact integer tuples:
            )
            listOf(
                listOf(0.5, 0.005, 20, 5, 100),
                listOf(0.25, 0.01, 20, 10, 120),
                listOf(0.25, 0.004, 100, 15, 150).let { listOf(0.25, 0.004, 31.623, 15, 150) }, // skip non-integer
                listOf(1.0, 0.01, 10, 20, 200),
                listOf(0.1, 0.04, 15.811, 5, 50)
            )
            // Exact clean resonant tuples (L in H, C in F, omega, R, E_peak):
            // 1/(L*C) = omega^2:
            listOf(
                listOf(0.5, 0.005, 20, 5, 100),     // X_L=10, X_C=10 -> I=20
                listOf(0.25, 0.01, 20, 10, 120),    // X_L=5, X_C=5 -> I=12
                listOf(1.0, 0.01, 10, 20, 200),     // X_L=10, X_C=10 -> I=10
                listOf(0.2, 0.005, 25, 8, 120).let { listOf(0.2, 0.008, 25, 8, 120) }, // 0.2*0.008=0.0016, sqrt=0.04 -> omega=25, X_L=5, X_C=5 -> I=15
                listOf(0.4, 0.001, 50, 10, 150),    // 0.4*0.001=0.0004, sqrt=0.02 -> omega=50, X_L=20, X_C=20 -> I=15
                listOf(0.25, 0.0016, 50, 12, 180)   // 0.25*0.0016=0.0004, sqrt=0.02 -> omega=50, X_L=12.5, X_C=12.5 -> I=15
            ).random()
        }
        val lH = l as Double
        val cF = c as Double
        val w = omega as Int
        val rOhm = r as Int
        val ePk = ePeak as Int
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
        val (rK, cUf) = listOf(
            20 to 50, 10 to 40, 50 to 10, 40 to 25, 5 to 80, 100 to 25
        ).random()
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
        val k = listOf(5, 12, 15, 20, 25).random()
        val p = listOf(10, 20, 25, 40, 75, 80, 100).random()
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
            Triple("√3/(RC) = √3 ω_c", sqrt(3.0), "3"),
            Triple("0.5/(RC) = ω_c / 2", 0.5, "0.25"),
            Triple("1/(2RC) = ω_c / 2", 0.5, "0.25")
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
        val (lMh, rOhm) = listOf(
            40 to 10, 60 to 15, 75 to 25, 100 to 20, 80 to 40, 120 to 30
        ).random()
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
        val (label, ratio, phaseDeg) = listOf(
            Triple("ω = 2ω_c", 2.0, 90.0 - Math.toDegrees(atan(2.0))),
            Triple("ω = ω_c / 2", 0.5, 90.0 - Math.toDegrees(atan(0.5))),
            Triple("ω = √3 ω_c", sqrt(3.0), 30.0),
            Triple("ω = ω_c / √3", 1.0 / sqrt(3.0), 60.0),
            Triple("ω = (3/4) ω_c", 0.75, 53.1301)
        ).random()
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
        val (k, z, p1, p2) = listOf(
            listOf(10, 3, 2, 5),
            listOf(18, 2, 3, 4),
            listOf(24, 5, 3, 4),
            listOf(15, 4, 2, 5),
            listOf(20, 3, 2, 6)
        ).random()
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
        val (magStr, dbVal) = listOf(
            "0.01" to -40.0,
            "0.001" to -60.0,
            "10" to 20.0,
            "100" to 40.0,
            "0.5" to -6.0206,
            "2" to 6.0206
        ).random()
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
        val (vs, rK, cUf, kTau) = listOf(
            listOf(50, 10, 40, 1.0),
            listOf(120, 20, 25, 1.0),
            listOf(80, 10, 50, 2.0),
            listOf(100, 10, 50, 2.0),
            listOf(60, 25, 20, 1.0)
        ).random()
        val vStep = vs as Int
        val k = kTau as Double
        val tauSec = (rK as Int) * (cUf as Int) / 1000.0
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
        val (p1, p2) = listOf(
            1 to 3, 2 to 3, 1 to 4, 2 to 4, 3 to 4, 1 to 5
        ).random()
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
        val (r, lH, cF) = listOf(
            Triple(4, 0.5, 0.02),
            Triple(8, 0.5, 0.02),
            Triple(6, 0.25, 0.01),
            Triple(10, 1.0, 0.01),
            Triple(12, 0.5, 0.02)
        ).random()
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
        val (r, xL, xC, w, eMag) = listOf(
            listOf(6, 12, 4, 20, 100),
            listOf(4, 3, 6, 25, 50),
            listOf(3, 8, 4, 20, 50),
            listOf(8, 10, 4, 25, 100),
            listOf(12, 5, 10, 20, 169)
        ).random()
        val lH = xL.toDouble() / w
        val cMf = 1000.0 / (w * xC)
        val xNet = (xL - xC).toDouble()
        val zMagSq = r * r + xNet * xNet
        val ans = ComplexNum(eMag * r / zMagSq, -eMag * xNet / zMagSq)
        wrapCuratedVariation(
            base = base,
            parametersLine1 = "R = ${r}Ω, L = ${ComplexNum.formatClean(lH)}H, C = ${ComplexNum.formatClean(cMf)}mF",
            parametersLine2 = "E = $eMag∠0° V, ω = $w rad/s",
            hints = listOf(
                "X_L = $xL Ω; X_C = $xC Ω ⇒ Z = $r + j(${xNet.toInt()}) Ω.",
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
        val (lMh, cUf, rootZ0) = listOf(
            Triple(40, 100, 20),
            Triple(10, 100, 10),
            Triple(90, 100, 30),
            Triple(160, 100, 40),
            Triple(100, 40, 50),
            Triple(45, 50, 30)
        ).random()
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
        val vs = listOf(18, 24, 36, 48).random()
        val r = listOf(5, 8, 12, 15).random()
        val lH = listOf(0.1, 0.25, 0.4, 0.5).random()
        val vc0 = listOf(0, 2, 6, 8).filter { !(vs == 24 && it == 4 && lH == 0.2) }.random()
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

    else -> null
}
