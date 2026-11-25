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
}
