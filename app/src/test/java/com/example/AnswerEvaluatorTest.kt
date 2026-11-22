package com.example

import com.example.data.ProblemBank
import com.example.domain.AnswerEvaluator
import com.example.domain.EvaluationResult
import com.example.model.AnswerKind
import com.example.model.ProblemCategory
import org.junit.Assert.assertTrue
import org.junit.Test

class AnswerEvaluatorTest {

    @Test
    fun `curated complex problems accept both rectangular and polar answers`() {
        val p101 = ProblemBank.curatedProblems.first { it.id == "p_101a" }
        val rectRes = AnswerEvaluator.evaluate(p101, "6 + j8", hintsUsed = 0, currentStreak = 0)
        assertTrue(rectRes is EvaluationResult.Correct)

        val polarRes = AnswerEvaluator.evaluate(p101, "10∠53.13°", hintsUsed = 0, currentStreak = 0)
        assertTrue(polarRes is EvaluationResult.Correct)
    }

    @Test
    fun `all curated problems validate against their own expected values`() {
        org.junit.Assert.assertEquals(55, ProblemBank.curatedProblems.size)
        org.junit.Assert.assertEquals(55, ProblemBank.curatedProblems.map { it.id }.toSet().size)
        for (problem in ProblemBank.curatedProblems) {
            val input = when (problem.answerKind) {
                AnswerKind.COMPLEX_RECT_OR_POLAR -> problem.expectedComplex!!.formatRect()
                AnswerKind.REAL_SCALAR -> problem.expectedReal!!.toString()
            }
            val result = AnswerEvaluator.evaluate(problem, input, hintsUsed = 0, currentStreak = 1)
            assertTrue("Expected ${problem.id} ($input) to evaluate Correct, got $result", result is EvaluationResult.Correct)
        }
    }

    @Test
    fun `procedural drills across all categories validate accurately across many iterations`() {
        val seenIds = mutableSetOf<String>()
        for (cat in ProblemCategory.entries) {
            var prevTitle: String? = null
            repeat(50) {
                val drill = ProblemBank.generateRandomDrill(preferredCategory = cat, excludeTitle = prevTitle)
                assertTrue("Expected unique drill id, got duplicate ${drill.id}", seenIds.add(drill.id))
                assertTrue("Expected non-repeating template title in $cat, got $prevTitle twice", drill.title != prevTitle)
                prevTitle = drill.title

                val input = when (drill.answerKind) {
                    AnswerKind.COMPLEX_RECT_OR_POLAR -> drill.expectedComplex!!.formatPolar()
                    AnswerKind.REAL_SCALAR -> drill.expectedReal!!.toString()
                }
                val result = AnswerEvaluator.evaluate(drill, input, hintsUsed = 0, currentStreak = 0)
                assertTrue("Expected drill ${drill.title} ($cat, $input) to evaluate Correct, got $result", result is EvaluationResult.Correct)

                if (drill.answerKind == AnswerKind.COMPLEX_RECT_OR_POLAR) {
                    val rectInput = drill.expectedComplex!!.formatRect()
                    val rectResult = AnswerEvaluator.evaluate(drill, rectInput, hintsUsed = 0, currentStreak = 0)
                    assertTrue("Expected drill ${drill.title} rect ($rectInput) to evaluate Correct, got $rectResult", rectResult is EvaluationResult.Correct)
                }
            }
        }

        // Mixed drill category rotation avoids repeating the exact same category back-to-back
        var prevCategory: ProblemCategory? = null
        repeat(20) {
            val mixed = ProblemBank.generateRandomDrill(preferredCategory = null, excludeCategory = prevCategory)
            assertTrue("Expected mixed drill to switch category from $prevCategory", mixed.category != prevCategory)
            prevCategory = mixed.category
        }
    }

    @Test
    fun `diagnostic tip detects sign error on complex impedance`() {
        val p104 = ProblemBank.curatedProblems.first { it.id == "p_104b" } // 40 - j40
        val res = AnswerEvaluator.evaluate(p104, "40 + j40", hintsUsed = 0, currentStreak = 0)
        assertTrue(res is EvaluationResult.Incorrect && res.diagnosticTip.contains("sign", ignoreCase = true))
    }

    @Test
    fun `real expression parser handles sqrt fractions and new curated problems`() {
        val p402 = ProblemBank.curatedProblems.first { it.id == "p_402a" } // 1/sqrt(2)
        val res = AnswerEvaluator.evaluate(p402, "1/sqrt(2)", hintsUsed = 0, currentStreak = 0)
        assertTrue(res is EvaluationResult.Correct)

        val p632 = ProblemBank.curatedProblems.first { it.id == "p_632i" } // -1/6
        assertTrue(AnswerEvaluator.evaluate(p632, "-1/6", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p632, "-0.167", 0, 0) is EvaluationResult.Correct)

        val p628 = ProblemBank.curatedProblems.first { it.id == "p_628h" } // e^(-pi/2)
        assertTrue(AnswerEvaluator.evaluate(p628, "0.208", 0, 0) is EvaluationResult.Correct)

        val p124 = ProblemBank.curatedProblems.first { it.id == "p_124g" } // 4 + j0
        assertTrue(AnswerEvaluator.evaluate(p124, "4", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p124, "4∠0°", 0, 0) is EvaluationResult.Correct)
    }

    @Test
    fun `admittance and dissipation factor enforce tight proportional tolerance`() {
        val p504 = ProblemBank.curatedProblems.first { it.id == "p_504b" } // 0.05 + j0.05
        assertTrue(AnswerEvaluator.evaluate(p504, "0.0707∠45°", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p504, "0.06 + j0.05", 0, 0) is EvaluationResult.Incorrect)

        val p518 = ProblemBank.curatedProblems.first { it.id == "p_518f" } // 0.02
        assertTrue(AnswerEvaluator.evaluate(p518, "0.02", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p518, "0.03", 0, 0) is EvaluationResult.Incorrect)
    }

    @Test
    fun `geometric algebra e12 multivector notation parses identically to j`() {
        val p704 = ProblemBank.curatedProblems.first { it.id == "p_704b" } // 26 + 7e12
        assertTrue(AnswerEvaluator.evaluate(p704, "26 + 7e12", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p704, "26 + j7", 0, 0) is EvaluationResult.Correct)

        val p720 = ProblemBank.curatedProblems.first { it.id == "p_720f" } // 0.12 - 0.16e12
        assertTrue(AnswerEvaluator.evaluate(p720, "0.12 - 0.16e12", 0, 0) is EvaluationResult.Correct)
    }
}
