package com.example.domain

import com.example.data.ProblemBank
import com.example.model.CircuitTopology
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SchematicAnnotationResolverTest {

    @Test
    fun resolves_every_curated_problem_and_topology_without_blank_titles() {
        val seenTopologies = mutableSetOf<CircuitTopology>()
        ProblemBank.curatedProblems.forEach { problem ->
            val resolved = SchematicAnnotationResolver.resolve(problem)
            assertTrue(
                "Expected non-blank topologyTitle for ${problem.id}",
                resolved.topologyTitle.isNotBlank()
            )
            seenTopologies.add(problem.topology)
        }
        assertEquals(CircuitTopology.entries.toSet(), seenTopologies)
    }

    @Test
    fun extracts_specific_component_values_for_rl_bridge_and_transformer() {
        val rl = ProblemBank.curatedProblems.first { it.id == "p_101a" }
        val rlAnn = SchematicAnnotationResolver.resolve(rl)
        assertEquals("R=6Ω", rlAnn.primaryR)
        assertEquals("L=0.08H", rlAnn.inductor)
        assertEquals("ω=100 rad/s", rlAnn.frequencyTag)

        val transformer = ProblemBank.curatedProblems.first { it.id == "p_146i" }
        val txAnn = SchematicAnnotationResolver.resolve(transformer)
        assertNotNull(txAnn.inductor)
        assertTrue(txAnn.inductor!!.startsWith("Z₁₁="))
        assertTrue(txAnn.secondaryBox!!.contains("ωM=5 Ω"))

        val bridge = ProblemBank.curatedProblems.first { it.id == "p_533j" }
        val bridgeAnn = SchematicAnnotationResolver.resolve(bridge)
        assertEquals(4, bridgeAnn.bridgeArms.size)
        assertTrue(bridgeAnn.bridgeArms.any { it.contains("500") })
    }

    @Test
    fun avoids_parenthetical_formulas_and_maps_three_phase_mesh_bridge_lnet_and_feedback_labels() {
        val neutral3Phase = SchematicAnnotationResolver.resolve(ProblemBank.curatedProblems.first { it.id == "p_315d" })
        assertTrue(neutral3Phase.loadZ!!.startsWith("I_a="))
        assertTrue(neutral3Phase.secondaryBox!!.startsWith("I_b="))
        assertTrue(!neutral3Phase.secondaryBox!!.contains("I_N="))

        val lineVoltage3Phase = SchematicAnnotationResolver.resolve(ProblemBank.curatedProblems.first { it.id == "p_312c" })
        assertTrue(lineVoltage3Phase.loadZ!!.startsWith("V_an="))
        assertEquals("V_ab=√3·V_an", lineVoltage3Phase.secondaryBox)

        val feederMesh = SchematicAnnotationResolver.resolve(ProblemBank.curatedProblems.first { it.id == "p_334j" })
        assertTrue(feederMesh.source!!.startsWith("V_L="))
        assertTrue(feederMesh.primaryR!!.startsWith("Z_line="))
        assertTrue(feederMesh.secondaryR!!.startsWith("I="))
        assertEquals(null, feederMesh.inductor)

        val parallelRc = SchematicAnnotationResolver.resolve(ProblemBank.curatedProblems.first { it.id == "p_108c" })
        assertEquals("R=10Ω", parallelRc.primaryR)
        assertEquals("Z_C=-j10Ω", parallelRc.capacitor)

        val parallelRl = SchematicAnnotationResolver.resolve(ProblemBank.curatedProblems.first { it.id == "p_119f" })
        assertEquals("R=10Ω", parallelRl.primaryR)
        assertEquals("Z_L=j10Ω", parallelRl.inductor)

        val schering = SchematicAnnotationResolver.resolve(ProblemBank.curatedProblems.first { it.id == "p_134j" })
        assertEquals(listOf("Z_x", "C₂=100 nF", "R₃=500 Ω", "R₄=2000 Ω"), schering.bridgeArms)

        val lNetwork = SchematicAnnotationResolver.resolve(ProblemBank.curatedProblems.first { it.id == "p_230i" })
        assertEquals("L-NET", lNetwork.secondaryBox)
        assertEquals("R_s=10 Ω", lNetwork.source)
        assertEquals("R_L=50 Ω", lNetwork.secondaryR)

        val unityPm = SchematicAnnotationResolver.resolve(ProblemBank.curatedProblems.first { it.id == "p_498h" })
        assertEquals("G(s)", unityPm.loadZ)
        assertEquals("1", unityPm.secondaryBox)

        val routh = SchematicAnnotationResolver.resolve(ProblemBank.curatedProblems.first { it.id == "p_495c" })
        assertEquals("G(s)H(s)", routh.loadZ)
        assertEquals("1", routh.secondaryBox)

        val bandpass = SchematicAnnotationResolver.resolve(ProblemBank.curatedProblems.first { it.id == "p_496e" })
        assertEquals("H(s)", bandpass.loadZ)
        assertEquals("1", bandpass.secondaryBox)
    }
}
