package com.example

import com.example.data.ProblemBank
import com.example.data.ProgressCodec
import com.example.model.AttemptRecord
import com.example.model.ComplexNum
import com.example.model.Difficulty
import com.example.model.ProblemCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProgressCodecTest {

    @Test
    fun `complex num formatBivector handles zero, scalar, unit bivector, and signed mixed terms`() {
        assertEquals("0", ComplexNum(0.0, 0.0).formatBivector())
        assertEquals("18", ComplexNum(18.0, 0.0).formatBivector())
        assertEquals("e12", ComplexNum(0.0, 1.0).formatBivector())
        assertEquals("-e12", ComplexNum(0.0, -1.0).formatBivector())
        assertEquals("26 + 7e12", ComplexNum(26.0, 7.0).formatBivector())
        assertEquals("0.12 - 0.16e12", ComplexNum(0.12, -0.16).formatBivector())
    }

    @Test
    fun `progress codec round-trips curated attempts, special unicode, escaping, and 30-item bound`() {
        val sample = (1..35).map { idx ->
            AttemptRecord(
                problemId = "p_$idx",
                problemCode = "No. $idx\\A",
                problemTitle = "Quote \"Test\" $idx\nLine2",
                submittedAnswer = "10∠-36.87° − Ω $idx",
                isCorrect = idx % 2 == 0,
                xpEarned = if (idx % 2 == 0) 175 else 0,
                timestampMillis = 1_700_000_000_000L + idx,
                drillSnapshot = null
            )
        }
        val encoded = ProgressCodec.encodeAttempts(sample)
        val decoded = ProgressCodec.decodeAttempts(encoded)

        assertEquals(30, decoded.size)
        assertEquals("p_1", decoded.first().problemId)
        assertEquals("No. 1\\A", decoded.first().problemCode)
        assertEquals("Quote \"Test\" 1\nLine2", decoded.first().problemTitle)
        assertEquals("10∠-36.87° − Ω 1", decoded.first().submittedAnswer)
        assertEquals(false, decoded.first().isCorrect)
        assertEquals(0, decoded.first().xpEarned)
        assertNull(decoded.first().drillSnapshot)
        assertEquals("p_30", decoded.last().problemId)
    }

    @Test
    fun `progress codec round-trips procedural drill snapshots for reopening history`() {
        val complexDrill = ProblemBank.generateRandomDrill(preferredCategory = ProblemCategory.GEOMETRIC_ALGEBRA)
        val scalarDrill = ProblemBank.generateRandomDrill(preferredCategory = ProblemCategory.RESONANCE)

        val records = listOf(
            AttemptRecord(
                problemId = complexDrill.id,
                problemCode = complexDrill.code,
                problemTitle = complexDrill.title,
                submittedAnswer = complexDrill.expectedDisplay(),
                isCorrect = true,
                xpEarned = complexDrill.difficulty.xpReward,
                timestampMillis = 100L,
                drillSnapshot = complexDrill
            ),
            AttemptRecord(
                problemId = scalarDrill.id,
                problemCode = scalarDrill.code,
                problemTitle = scalarDrill.title,
                submittedAnswer = scalarDrill.expectedDisplay(),
                isCorrect = false,
                xpEarned = 0,
                timestampMillis = 99L,
                drillSnapshot = scalarDrill
            )
        )

        val decoded = ProgressCodec.decodeAttempts(ProgressCodec.encodeAttempts(records))
        assertEquals(2, decoded.size)

        val restoredComplex = decoded[0].drillSnapshot
        assertNotNull(restoredComplex)
        assertEquals(complexDrill, restoredComplex)

        val restoredScalar = decoded[1].drillSnapshot
        assertNotNull(restoredScalar)
        assertEquals(scalarDrill, restoredScalar)

        val baseCurated = ProblemBank.curatedProblems.first { it.id == "p_492b" }
        val curatedVariation = ProblemBank.generateVariationFor(baseCurated)
        val variationRecord = AttemptRecord(
            problemId = curatedVariation.id,
            problemCode = curatedVariation.code,
            problemTitle = curatedVariation.title,
            submittedAnswer = curatedVariation.expectedDisplay(),
            isCorrect = true,
            xpEarned = curatedVariation.difficulty.xpReward,
            timestampMillis = 101L,
            drillSnapshot = curatedVariation
        )
        val decodedVar = ProgressCodec.decodeAttempts(ProgressCodec.encodeAttempts(listOf(variationRecord)))
        assertEquals(1, decodedVar.size)
        assertEquals(curatedVariation, decodedVar[0].drillSnapshot)
        assertEquals("p_492b", decodedVar[0].drillSnapshot?.sourceProblemId)
        assertEquals("No. 492B", decodedVar[0].drillSnapshot?.sourceProblemCode)
    }

    @Test
    fun `next curated problem stays scoped within active index filters`() {
        val powerList = ProblemBank.curatedProblems.filter { it.category == ProblemCategory.POWER }
        val firstPower = powerList.first()
        val solved = setOf(firstPower.id)

        val next = ProblemBank.selectNextCuratedProblem(
            currentProblemId = firstPower.id,
            solvedIds = solved,
            categoryFilter = ProblemCategory.POWER,
            difficultyFilter = null,
            unsolvedOnly = false
        )
        assertEquals(ProblemCategory.POWER, next.category)
        assertTrue(next.id != firstPower.id)

        // When all matching problems in a scoped filter are solved, cycle cleanly inside that same filter
        val allPowerSolved = powerList.map { it.id }.toSet()
        val cycled = ProblemBank.selectNextCuratedProblem(
            currentProblemId = firstPower.id,
            solvedIds = allPowerSolved,
            categoryFilter = ProblemCategory.POWER,
            difficultyFilter = Difficulty.FOUNDATIONS,
            unsolvedOnly = true
        )
        assertEquals(ProblemCategory.POWER, cycled.category)
        assertEquals(Difficulty.FOUNDATIONS, cycled.difficulty)
    }

    @Test
    fun `starter problem and field guide catalog remain intact`() {
        assertEquals("p_492b", ProblemBank.curatedProblems.first().id)
        assertEquals("No. 492B", ProblemBank.curatedProblems.first().code)
        assertEquals(8, ProblemBank.fieldGuideEntries.size)
        assertTrue(ProblemBank.fieldGuideEntries.all { it.title.isNotBlank() && it.formula.isNotBlank() })
    }
}
