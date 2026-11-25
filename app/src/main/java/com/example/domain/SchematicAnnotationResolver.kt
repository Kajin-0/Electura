package com.example.domain

import com.example.model.CircuitProblem
import com.example.model.CircuitTopology
import com.example.model.ComplexNum
import kotlin.math.abs

data class SchematicAnnotations(
    val topologyTitle: String,
    val source: String? = null,
    val primaryR: String? = null,
    val secondaryR: String? = null,
    val inductor: String? = null,
    val capacitor: String? = null,
    val loadZ: String? = null,
    val secondaryBox: String? = null,
    val frequencyTag: String? = null,
    val bridgeArms: List<String> = emptyList()
)

object SchematicAnnotationResolver {

    fun resolve(problem: CircuitProblem): SchematicAnnotations {
        val combined = "${problem.parametersLine1} | ${problem.parametersLine2}"
        val promptAndParams = "$combined | ${problem.prompt}"

        val vSource = extractAssignment(combined, listOf("V_s", "V_th", "V_in", "V₁", "E", "v\\(t\\)"))
        val iSource = extractAssignment(combined, listOf("I_s", "i\\(t\\)"))
        val source = when {
            vSource != null -> vSource
            iSource != null -> iSource
            problem.topology == CircuitTopology.LOW_PASS_RC ||
                problem.topology == CircuitTopology.HIGH_PASS_RC ||
                problem.topology == CircuitTopology.OP_AMP_INVERTING -> "V_in"
            problem.topology == CircuitTopology.TWO_PORT_NETWORK -> "V₁"
            else -> null
        }

        val freqRaw = extractAssignment(
            promptAndParams,
            listOf("ω₀", "ω_0", "ω_c", "ω_gc", "ω_pc", "ω_d", "ω", "f₀", "f_0", "f_s", "f_test", "f_c", "f")
        )
        val frequencyTag = freqRaw?.let { compactValue(it, maxLen = 16) }

        val rPrimary = extractAssignment(
            combined,
            listOf("R_in", "R_s", "ESR \\(R_s\\)", "ESR", "R₁", "R_p", "R", "G_p", "G")
        )
        val rSecondary = extractAssignment(
            combined,
            listOf("R₂", "R_L", "R₃", "Z₂₂")
        )
        val lVal = extractAssignment(
            combined,
            listOf("ESL \\(L_s\\)", "L_s", "L₁", "L_eq", "L", "X_L0", "X_L", "B_L")
        )
        val cVal = extractAssignment(
            combined,
            listOf("C_p", "C_m", "C₁", "C₀", "C", "X_C0", "X_C", "\\|X_C\\|", "B_C", "B_p")
        )
        val zLoad = extractAssignment(
            combined,
            listOf("Z_L", "Z_th", "Z_in", "Z₂", "Z₁", "Z_f", "Z_C", "Z", "Y_L", "Y_in", "Y", "S")
        )
        val mutualOrRatio = extractAssignment(
            combined,
            listOf("N₁\\s*:\\s*N₂", "Turns Ratio N₁\\s*:\\s*N₂", "Mutual reactance ωM", "ωM", "Mutual M", "M")
        )

        val title = topologyDisplayTitle(problem.topology)

        return when (problem.topology) {
            CircuitTopology.SERIES_RLC -> SchematicAnnotations(
                topologyTitle = title,
                source = compactOrDefault(source, "V_s"),
                primaryR = compactOrDefault(rPrimary, "R"),
                inductor = compactOrDefault(lVal, "L"),
                capacitor = compactOrDefault(cVal, "C"),
                frequencyTag = frequencyTag
            )

            CircuitTopology.SERIES_RL -> SchematicAnnotations(
                topologyTitle = title,
                source = compactOrDefault(source, "V_s"),
                primaryR = compactOrDefault(rPrimary, "R"),
                inductor = compactOrDefault(lVal, "L"),
                frequencyTag = frequencyTag
            )

            CircuitTopology.SERIES_RC -> SchematicAnnotations(
                topologyTitle = title,
                source = compactOrDefault(source, "V_s"),
                primaryR = compactOrDefault(rPrimary, "R"),
                capacitor = compactOrDefault(cVal, "C"),
                frequencyTag = frequencyTag
            )

            CircuitTopology.PARALLEL_RC -> SchematicAnnotations(
                topologyTitle = title,
                source = compactOrDefault(source, "I_s"),
                primaryR = compactOrDefault(rPrimary, "R"),
                capacitor = compactOrDefault(cVal, "C"),
                frequencyTag = frequencyTag
            )

            CircuitTopology.PARALLEL_RL -> SchematicAnnotations(
                topologyTitle = title,
                source = compactOrDefault(source, "I_s"),
                primaryR = compactOrDefault(rPrimary, "R"),
                inductor = compactOrDefault(lVal, "L"),
                frequencyTag = frequencyTag
            )

            CircuitTopology.PARALLEL_RLC -> SchematicAnnotations(
                topologyTitle = title,
                source = compactOrDefault(source, "I_s"),
                primaryR = compactOrDefault(rPrimary, "R"),
                inductor = compactOrDefault(lVal, "L"),
                capacitor = compactOrDefault(cVal, "C"),
                frequencyTag = frequencyTag
            )

            CircuitTopology.COMPLEX_LOAD -> SchematicAnnotations(
                topologyTitle = title,
                source = compactOrDefault(source, "V_s"),
                loadZ = compactOrDefault(zLoad, "Z"),
                frequencyTag = frequencyTag
            )

            CircuitTopology.LOW_PASS_RC -> SchematicAnnotations(
                topologyTitle = title,
                source = compactOrDefault(source, "V_in"),
                primaryR = compactOrDefault(rPrimary, "R"),
                capacitor = compactOrDefault(cVal, "C"),
                loadZ = "V_out",
                frequencyTag = frequencyTag
            )

            CircuitTopology.HIGH_PASS_RC -> SchematicAnnotations(
                topologyTitle = title,
                source = compactOrDefault(source, "V_in"),
                capacitor = compactOrDefault(cVal, "C"),
                primaryR = compactOrDefault(rPrimary, "R"),
                loadZ = "V_out",
                frequencyTag = frequencyTag
            )

            CircuitTopology.FEEDBACK_LOOP -> {
                val gBlock = extractTransferSymbol(combined, listOf("G\\(s\\)H\\(s\\)", "G\\(s\\)", "H\\(s\\)", "Y\\(s\\)", "F\\(s\\)"))
                SchematicAnnotations(
                    topologyTitle = title,
                    source = "R(s)",
                    loadZ = gBlock ?: "G(s)",
                    secondaryBox = if (combined.contains("H(s)")) "H(s)" else "H",
                    frequencyTag = frequencyTag ?: extractGainOrPoleTag(combined)
                )
            }

            CircuitTopology.TRANSFORMER_COUPLED -> {
                val l1 = extractAssignment(combined, listOf("L₁", "Z₁₁"))
                val l2 = extractAssignment(combined, listOf("L₂", "Z₂₂"))
                SchematicAnnotations(
                    topologyTitle = title,
                    source = compactOrDefault(source, "V₁"),
                    inductor = compactValue(l1 ?: "N₁", 12),
                    capacitor = compactValue(l2 ?: "N₂", 12),
                    secondaryBox = compactValue(mutualOrRatio ?: "M", 16),
                    loadZ = compactOrDefault(zLoad, "Z_L"),
                    frequencyTag = frequencyTag
                )
            }

            CircuitTopology.OP_AMP_INVERTING -> {
                val rf = extractAssignment(combined, listOf("R_f", "Z_f"))
                val cf = extractAssignment(combined, listOf("C_f"))
                val fbLabel = when {
                    rf != null && cf != null -> "${compactValue(rf, 10)} ∥ ${compactValue(cf, 10)}"
                    rf != null -> compactValue(rf, 14)
                    cf != null -> compactValue(cf, 14)
                    else -> "Zf"
                }
                SchematicAnnotations(
                    topologyTitle = title,
                    source = "V_in",
                    primaryR = compactOrDefault(rPrimary, "R_in"),
                    loadZ = fbLabel,
                    secondaryBox = "V_out",
                    frequencyTag = frequencyTag
                )
            }

            CircuitTopology.AC_BRIDGE -> {
                val arm1 = extractAssignment(combined, listOf("R₁", "C₁", "Arm 1"))
                val arm2 = extractAssignment(combined, listOf("C₂", "R₂", "Arm 2"))
                val arm3 = extractAssignment(combined, listOf("R₃", "Arm 3"))
                val arm4 = extractAssignment(combined, listOf("R₄", "C₄", "Arm 4"))
                val arms = listOf(
                    compactValue(arm1 ?: "Z₁", 11),
                    compactValue(arm2 ?: "Z₂", 11),
                    compactValue(arm3 ?: "Z₃", 11),
                    compactValue(arm4 ?: "Z_x", 11)
                )
                SchematicAnnotations(
                    topologyTitle = title,
                    source = compactOrDefault(source, "V_s"),
                    bridgeArms = arms,
                    frequencyTag = frequencyTag
                )
            }

            CircuitTopology.TWO_MESH -> {
                val zm = extractAssignment(combined, listOf("Z_m", "shared Z_m"))
                SchematicAnnotations(
                    topologyTitle = title,
                    source = compactOrDefault(source, "V₁"),
                    primaryR = compactOrDefault(rPrimary, "R₁"),
                    secondaryR = compactOrDefault(rSecondary, "R₂"),
                    inductor = compactOrDefault(zm ?: lVal, "Z_m"),
                    frequencyTag = frequencyTag
                )
            }

            CircuitTopology.THREE_PHASE_DELTA_Y -> {
                val zDelta = extractAssignment(combined, listOf("Z_Δ", "Z_ab", "W₁"))
                val zWye = extractAssignment(combined, listOf("Z_Y", "Z_bc", "W₂"))
                val lineV = extractAssignment(combined, listOf("V_L", "I_a"))
                val lineI = extractAssignment(combined, listOf("I_L", "I_N"))
                SchematicAnnotations(
                    topologyTitle = title,
                    loadZ = compactValue(zDelta ?: lineV ?: "Δ", 14),
                    secondaryBox = compactValue(zWye ?: lineI ?: "Y", 14),
                    frequencyTag = frequencyTag
                )
            }

            CircuitTopology.TWO_PORT_NETWORK -> {
                val y21 = extractAssignment(combined, listOf("y₂₁"))
                val load = extractAssignment(combined, listOf("Y_L", "R_L", "Load R_L", "Load Y_L"))
                val boxTitle = when {
                    combined.contains("L-Match", ignoreCase = true) ||
                        problem.title.contains("L-Match", ignoreCase = true) -> "L-NET"
                    y21 != null -> "[Y]"
                    else -> "[2P]"
                }
                SchematicAnnotations(
                    topologyTitle = title,
                    source = compactOrDefault(extractAssignment(combined, listOf("R_s")) ?: source, "V₁"),
                    secondaryBox = boxTitle,
                    secondaryR = compactOrDefault(load, "Z_L"),
                    frequencyTag = frequencyTag
                )
            }

            CircuitTopology.CONTOUR_SPLANE -> {
                val radiusLabel = extractRegexMatch(promptAndParams, """\|[sz]\|\s*=\s*[0-9.]+""")
                    ?: extractRegexMatch(promptAndParams, """C:\s*\|[sz][^,|)]*""")
                SchematicAnnotations(
                    topologyTitle = title,
                    frequencyTag = radiusLabel?.let { compactValue(it, 16) } ?: "Contour C"
                )
            }

            CircuitTopology.GEOMETRIC_PLANE -> {
                val angleDeg = problem.expectedComplex?.angleDeg
                val angleLabel = if (angleDeg != null && abs(angleDeg) >= 0.5) {
                    "θ = ${ComplexNum.formatClean(angleDeg)}°"
                } else {
                    null
                }
                SchematicAnnotations(
                    topologyTitle = title,
                    frequencyTag = angleLabel ?: "e₁ ∧ e₂"
                )
            }
        }
    }

    private fun topologyDisplayTitle(topology: CircuitTopology): String = when (topology) {
        CircuitTopology.SERIES_RLC -> "Series RLC Circuit"
        CircuitTopology.SERIES_RL -> "Series RL Circuit"
        CircuitTopology.SERIES_RC -> "Series RC Circuit"
        CircuitTopology.PARALLEL_RC -> "Parallel RC Network"
        CircuitTopology.PARALLEL_RL -> "Parallel RL Network"
        CircuitTopology.PARALLEL_RLC -> "Parallel RLC Tank"
        CircuitTopology.COMPLEX_LOAD -> "AC Source & Complex Load"
        CircuitTopology.LOW_PASS_RC -> "First-Order Low-Pass RC"
        CircuitTopology.HIGH_PASS_RC -> "First-Order High-Pass RC"
        CircuitTopology.FEEDBACK_LOOP -> "Closed-Loop Feedback System"
        CircuitTopology.TRANSFORMER_COUPLED -> "Magnetically Coupled Transformer"
        CircuitTopology.OP_AMP_INVERTING -> "Inverting Op-Amp Stage"
        CircuitTopology.AC_BRIDGE -> "Four-Arm AC Bridge"
        CircuitTopology.TWO_MESH -> "Two-Mesh Coupled AC Network"
        CircuitTopology.THREE_PHASE_DELTA_Y -> "Three-Phase Δ–Y Network"
        CircuitTopology.TWO_PORT_NETWORK -> "Two-Port Network & Load"
        CircuitTopology.CONTOUR_SPLANE -> "Complex s/z-Plane Contour"
        CircuitTopology.GEOMETRIC_PLANE -> "Geometric Plane Cl₂,₀"
    }

    private fun extractAssignment(text: String, keys: List<String>): String? {
        for (key in keys) {
            val regex = Regex("""(?<![A-Za-z0-9_])($key)\s*[:=]\s*([^,;|]+)""")
            val match = regex.find(text)
            if (match != null) {
                val lhs = match.groupValues[1].replace("\\", "")
                val rhsRaw = match.groupValues[2].trim()
                val rhs = cleanRhsValue(rhsRaw)
                if (rhs.isNotEmpty()) {
                    val cleanLhs = when {
                        lhs.startsWith("ESR") -> "ESR"
                        lhs.startsWith("ESL") -> "ESL"
                        lhs.startsWith("Mutual reactance ") -> lhs.removePrefix("Mutual reactance ")
                        lhs.startsWith("Mutual ") -> "M"
                        lhs.startsWith("Turns Ratio ") -> "N₁:N₂"
                        lhs.startsWith("Load ") -> lhs.removePrefix("Load ")
                        lhs.startsWith("shared ") -> lhs.removePrefix("shared ")
                        else -> lhs
                    }
                    return compactValue("$cleanLhs=$rhs", maxLen = 16)
                }
            }
        }
        return null
    }

    private fun extractTransferSymbol(text: String, keys: List<String>): String? {
        for (key in keys) {
            val regex = Regex("""($key)\s*=""")
            val match = regex.find(text)
            if (match != null) return match.groupValues[1]
        }
        return null
    }

    private fun extractGainOrPoleTag(text: String): String? {
        val match = Regex("""ω_[a-z]+\s*=\s*[0-9.]+\s*[A-Za-z/]*""").find(text)
        return match?.value?.let { compactValue(it, 16) }
    }

    private fun extractRegexMatch(text: String, pattern: String): String? =
        Regex(pattern).find(text)?.value?.trim()

    private fun cleanRhsValue(raw: String): String {
        var trimmed = raw.trim()
        val parenNoteIdx = trimmed.indexOf(" (")
        if (parenNoteIdx > 0) {
            trimmed = trimmed.substring(0, parenNoteIdx)
        }
        val stopMarkers = listOf(" at ", " via ", " with ", " for ", " in ", " and ", " → ", " ⇒ ", " ∥ ", " || ", "∥")
        for (marker in stopMarkers) {
            val idx = trimmed.indexOf(marker)
            if (idx > 0) trimmed = trimmed.substring(0, idx)
        }
        if (trimmed.contains("=")) {
            trimmed = trimmed.substringAfterLast("=").trim()
        }
        if (trimmed.startsWith("(")) {
            val closeIdx = trimmed.indexOf(')')
            if (closeIdx > 1) {
                val inner = trimmed.substring(1, closeIdx).trim()
                val tail = trimmed.substring(closeIdx + 1).trim()
                trimmed = if (tail.isNotEmpty()) "$inner $tail" else inner
            }
        }
        return trimmed
            .trim()
            .trimEnd('.', ')', ']', ';', ',')
            .replace("  ", " ")
    }

    private fun compactOrDefault(value: String?, fallback: String): String =
        if (value.isNullOrBlank()) fallback else compactValue(value, maxLen = 15)

    private fun compactValue(raw: String, maxLen: Int): String {
        val collapsed = raw.replace(" = ", "=").replace(" : ", ":").trim()
        return if (collapsed.length <= maxLen) collapsed else collapsed.take(maxLen - 1) + "…"
    }
}
