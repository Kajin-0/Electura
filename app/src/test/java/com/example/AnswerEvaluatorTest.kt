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
        org.junit.Assert.assertEquals(105, ProblemBank.curatedProblems.size)
        org.junit.Assert.assertEquals(105, ProblemBank.curatedProblems.map { it.id }.toSet().size)
        for (problem in ProblemBank.curatedProblems) {
            val input = when (problem.answerKind) {
                AnswerKind.COMPLEX_RECT_OR_POLAR -> problem.expectedComplex!!.formatRect()
                AnswerKind.REAL_SCALAR -> problem.expectedReal!!.toString()
            }
            val result = AnswerEvaluator.evaluate(problem, input, hintsUsed = 0, currentStreak = 1)
            assertTrue("Expected ${problem.id} ($input) to evaluate Correct, got $result", result is EvaluationResult.Correct)
        }

        // Verify non-starter problems progress smoothly by difficulty within each category
        val tail = ProblemBank.curatedProblems.drop(1)
        for (cat in ProblemCategory.entries) {
            val ordinals = tail.filter { it.category == cat }.map { it.difficulty.ordinal }
            assertTrue("Expected sorted difficulty progression in $cat, got $ordinals", ordinals == ordinals.sorted())
        }
    }

    @Test
    fun `procedural drills across all categories validate accurately across many iterations`() {
        val seenIds = mutableSetOf<String>()
        for (cat in ProblemCategory.entries) {
            val recentTitles = ArrayDeque<String>()
            repeat(120) {
                val drill = ProblemBank.generateRandomDrill(
                    preferredCategory = cat,
                    excludeTitles = recentTitles.toSet()
                )
                assertTrue("Expected unique drill id, got duplicate ${drill.id}", seenIds.add(drill.id))
                assertTrue("Expected non-repeating template title in $cat, got ${drill.title} from recent=$recentTitles", drill.title !in recentTitles)
                recentTitles.addLast(drill.title)
                if (recentTitles.size > 3) recentTitles.removeFirst()

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

            // Every category supports procedural drills at all 4 difficulty levels
            for (diff in com.example.model.Difficulty.entries) {
                val filteredDrill = ProblemBank.generateRandomDrill(
                    preferredCategory = cat,
                    preferredDifficulty = diff
                )
                org.junit.Assert.assertEquals(
                    "Expected $cat drill at $diff, got ${filteredDrill.title} (${filteredDrill.difficulty})",
                    diff,
                    filteredDrill.difficulty
                )
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

        val p140 = ProblemBank.curatedProblems.first { it.id == "p_140k" } // 0 + j20
        assertTrue(AnswerEvaluator.evaluate(p140, "j20", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p140, "20∠90°", 0, 0) is EvaluationResult.Correct)

        val p499e = ProblemBank.curatedProblems.first { it.id == "p_499e" } // 63.212
        assertTrue(AnswerEvaluator.evaluate(p499e, "63.2", 0, 0) is EvaluationResult.Correct)

        val p640 = ProblemBank.curatedProblems.first { it.id == "p_640k" } // -0.6 + j0.8
        assertTrue(AnswerEvaluator.evaluate(p640, "-0.6 + j0.8", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p640, "1∠126.87°", 0, 0) is EvaluationResult.Correct)

        val p600c = ProblemBank.curatedProblems.first { it.id == "p_600c" } // -4 + j3
        assertTrue(AnswerEvaluator.evaluate(p600c, "-4 + j3", 0, 0) is EvaluationResult.Correct)

        val p600d = ProblemBank.curatedProblems.first { it.id == "p_600d" } // 0 + j1
        assertTrue(AnswerEvaluator.evaluate(p600d, "j", 0, 0) is EvaluationResult.Correct)

        val p602a = ProblemBank.curatedProblems.first { it.id == "p_602a" } // -2 + j3
        assertTrue(AnswerEvaluator.evaluate(p602a, "-2 + j3", 0, 0) is EvaluationResult.Correct)

        val p600e = ProblemBank.curatedProblems.first { it.id == "p_600e" } // 0 + j12
        assertTrue(AnswerEvaluator.evaluate(p600e, "12∠90°", 0, 0) is EvaluationResult.Correct)

        val p605b = ProblemBank.curatedProblems.first { it.id == "p_605b" } // 0
        assertTrue(AnswerEvaluator.evaluate(p605b, "0", 0, 0) is EvaluationResult.Correct)

        val p607b = ProblemBank.curatedProblems.first { it.id == "p_607b" } // e^2 ≈ 7.389
        assertTrue(AnswerEvaluator.evaluate(p607b, "7.389", 0, 0) is EvaluationResult.Correct)

        val p400a = ProblemBank.curatedProblems.first { it.id == "p_400a" } // 1.0
        assertTrue(AnswerEvaluator.evaluate(p400a, "1", 0, 0) is EvaluationResult.Correct)

        val p100a = ProblemBank.curatedProblems.first { it.id == "p_100a" } // 45
        assertTrue(AnswerEvaluator.evaluate(p100a, "45", 0, 0) is EvaluationResult.Correct)

        val p106b = ProblemBank.curatedProblems.first { it.id == "p_106b" } // 6.25 - j5
        assertTrue(AnswerEvaluator.evaluate(p106b, "6.25 - j5", 0, 0) is EvaluationResult.Correct)

        val p124f = ProblemBank.curatedProblems.first { it.id == "p_124f" } // -1 + j1
        assertTrue(AnswerEvaluator.evaluate(p124f, "-1 + j1", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p124f, "1.414∠135°", 0, 0) is EvaluationResult.Correct)

        val p528i = ProblemBank.curatedProblems.first { it.id == "p_528i" } // 50
        assertTrue(AnswerEvaluator.evaluate(p528i, "50", 0, 0) is EvaluationResult.Correct)

        val p222g = ProblemBank.curatedProblems.first { it.id == "p_222g" } // 1800
        assertTrue(AnswerEvaluator.evaluate(p222g, "1800", 0, 0) is EvaluationResult.Correct)

        val p302a = ProblemBank.curatedProblems.first { it.id == "p_302a" } // 13
        assertTrue(AnswerEvaluator.evaluate(p302a, "13", 0, 0) is EvaluationResult.Correct)

        val p312c = ProblemBank.curatedProblems.first { it.id == "p_312c" } // 346.41∠30°
        assertTrue(AnswerEvaluator.evaluate(p312c, "346.41∠30°", 0, 0) is EvaluationResult.Correct)

        val p334j = ProblemBank.curatedProblems.first { it.id == "p_334j" } // 220 + j10
        assertTrue(AnswerEvaluator.evaluate(p334j, "220 + j10", 0, 0) is EvaluationResult.Correct)

        val p497d = ProblemBank.curatedProblems.first { it.id == "p_497d" } // 100
        assertTrue(AnswerEvaluator.evaluate(p497d, "100", 0, 0) is EvaluationResult.Correct)
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

        val p732 = ProblemBank.curatedProblems.first { it.id == "p_732i" } // 3 - 4e12
        assertTrue(AnswerEvaluator.evaluate(p732, "3 - 4e12", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p732, "3 - j4", 0, 0) is EvaluationResult.Correct)

        val p736 = ProblemBank.curatedProblems.first { it.id == "p_736j" } // 20
        assertTrue(AnswerEvaluator.evaluate(p736, "20", 0, 0) is EvaluationResult.Correct)

        val p700b = ProblemBank.curatedProblems.first { it.id == "p_700b" } // 18
        assertTrue(AnswerEvaluator.evaluate(p700b, "18", 0, 0) is EvaluationResult.Correct)

        val p703a = ProblemBank.curatedProblems.first { it.id == "p_703a" } // 8 + 6e12
        assertTrue(AnswerEvaluator.evaluate(p703a, "8 + 6e12", 0, 0) is EvaluationResult.Correct)

        val p707b = ProblemBank.curatedProblems.first { it.id == "p_707b" } // 0.866 - 0.5e12
        assertTrue(AnswerEvaluator.evaluate(p707b, "0.866 - 0.5e12", 0, 0) is EvaluationResult.Correct)

        val p711d = ProblemBank.curatedProblems.first { it.id == "p_711d" } // 15
        assertTrue(AnswerEvaluator.evaluate(p711d, "15", 0, 0) is EvaluationResult.Correct)
    }

    @Test
    fun `matching trailing units strip cleanly while mismatched units fail`() {
        val p100a = ProblemBank.curatedProblems.first { it.id == "p_100a" } // 45°
        assertTrue(AnswerEvaluator.evaluate(p100a, "45°", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p100a, "45 deg", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p100a, "45 degrees", 0, 0) is EvaluationResult.Correct)

        val p403a = ProblemBank.curatedProblems.first { it.id == "p_403a" } // 2.0 ms
        assertTrue(AnswerEvaluator.evaluate(p403a, "2 ms", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p403a, "2ms", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p403a, "2 s", 0, 0) is EvaluationResult.InvalidFormat)

        val p318e = ProblemBank.curatedProblems.first { it.id == "p_318e" } // 400.0 μF
        assertTrue(AnswerEvaluator.evaluate(p318e, "400 uF", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p318e, "400 μF", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p318e, "400 mF", 0, 0) is EvaluationResult.InvalidFormat)

        val p104b = ProblemBank.curatedProblems.first { it.id == "p_104b" } // 40 - j40 Ω
        assertTrue(AnswerEvaluator.evaluate(p104b, "40 - j40 Ω", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p104b, "(40 - j40) ohm", 0, 0) is EvaluationResult.Correct)
    }

    @Test
    fun `euler exponentials polar radians and compound complex arithmetic evaluate accurately`() {
        val p140k = ProblemBank.curatedProblems.first { it.id == "p_140k" } // 0 + j20
        assertTrue(AnswerEvaluator.evaluate(p140k, "20e^(j90°)", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p140k, "20*e^(j*pi/2)", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p140k, "20∠pi/2 rad", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p140k, "20 angle 90 deg", 0, 0) is EvaluationResult.Correct)

        val p101a = ProblemBank.curatedProblems.first { it.id == "p_101a" } // 6 + j8 = 10∠53.13°
        assertTrue(AnswerEvaluator.evaluate(p101a, "10e^(j53.13°)", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p101a, "10∠(53.13°)", 0, 0) is EvaluationResult.Correct)

        val p628h = ProblemBank.curatedProblems.first { it.id == "p_628h" } // e^(-pi/2)
        assertTrue(AnswerEvaluator.evaluate(p628h, "e^(-pi/2)", 0, 0) is EvaluationResult.Correct)
        assertTrue(AnswerEvaluator.evaluate(p628h, "exp(-pi/2)", 0, 0) is EvaluationResult.Correct)

        val piRect = AnswerEvaluator.parseComplex("1 + j*pi")!!
        org.junit.Assert.assertEquals(1.0, piRect.re, 1e-9)
        org.junit.Assert.assertEquals(Math.PI, piRect.im, 1e-9)

        val divComplex = AnswerEvaluator.parseComplex("10/(3 + j4)")!!
        org.junit.Assert.assertEquals(1.2, divComplex.re, 1e-9)
        org.junit.Assert.assertEquals(-1.6, divComplex.im, 1e-9)

        val scaledComplex = AnswerEvaluator.parseComplex("(6 + j8)/2")!!
        org.junit.Assert.assertEquals(3.0, scaledComplex.re, 1e-9)
        org.junit.Assert.assertEquals(4.0, scaledComplex.im, 1e-9)

        val squaredComplex = AnswerEvaluator.parseComplex("(1 + j)^2")!!
        org.junit.Assert.assertEquals(0.0, squaredComplex.re, 1e-9)
        org.junit.Assert.assertEquals(2.0, squaredComplex.im, 1e-9)

        org.junit.Assert.assertEquals(2.0 * kotlin.math.sqrt(2.0), AnswerEvaluator.parseRealExpression("2*sqrt(2)")!!, 1e-9)
        org.junit.Assert.assertEquals(2.0 * kotlin.math.sqrt(2.0), AnswerEvaluator.parseRealExpression("2√2")!!, 1e-9)
        org.junit.Assert.assertEquals(1e-3, AnswerEvaluator.parseRealExpression("1e-3")!!, 1e-12)
        org.junit.Assert.assertEquals(2.0, AnswerEvaluator.parseRealExpression("ln(e^2)")!!, 1e-9)
        org.junit.Assert.assertEquals(2.0, AnswerEvaluator.parseRealExpression("log(100)")!!, 1e-9)
    }

    @Test
    fun `invalid non-finite and whitespace-juxtaposed numbers return InvalidFormat`() {
        val p400a = ProblemBank.curatedProblems.first { it.id == "p_400a" }
        assertTrue(AnswerEvaluator.evaluate(p400a, "1/0", 0, 0) is EvaluationResult.InvalidFormat)
        assertTrue(AnswerEvaluator.evaluate(p400a, "Infinity", 0, 0) is EvaluationResult.InvalidFormat)
        assertTrue(AnswerEvaluator.evaluate(p400a, "NaN", 0, 0) is EvaluationResult.InvalidFormat)
        assertTrue(AnswerEvaluator.evaluate(p400a, "ln(0)", 0, 0) is EvaluationResult.InvalidFormat)
        assertTrue(AnswerEvaluator.evaluate(p400a, "log(-1)", 0, 0) is EvaluationResult.InvalidFormat)
        assertTrue(AnswerEvaluator.evaluate(p400a, "2 3", 0, 0) is EvaluationResult.InvalidFormat)

        val p101a = ProblemBank.curatedProblems.first { it.id == "p_101a" }
        assertTrue(AnswerEvaluator.evaluate(p101a, "1/(1 - 1)", 0, 0) is EvaluationResult.InvalidFormat)
    }

    @Test
    fun `geometric algebra formats bivectors with e12 and domain-specific diagnostics`() {
        val p704 = ProblemBank.curatedProblems.first { it.id == "p_704b" } // 26 + 7e12
        val expectedText = p704.expectedDisplay()
        assertTrue("Expected e12 display, got $expectedText", expectedText.contains("e12") && !expectedText.contains("+ j"))

        val correctGa = AnswerEvaluator.evaluate(p704, "26 + 7e12", 0, 0)
        assertTrue(correctGa is EvaluationResult.Correct && correctGa.parsedDisplay.contains("e12") && correctGa.note.contains("Cl₂,₀"))

        val signMismatchGa = AnswerEvaluator.evaluate(p704, "26 - 7e12", 0, 0)
        assertTrue(signMismatchGa is EvaluationResult.Incorrect && signMismatchGa.diagnosticTip.contains("e12"))

        val mismatchGa = AnswerEvaluator.evaluate(p704, "1 + 2e12", 0, 0)
        assertTrue(
            mismatchGa is EvaluationResult.Incorrect &&
                mismatchGa.diagnosticTip.contains("Clifford", ignoreCase = true) &&
                !mismatchGa.diagnosticTip.contains("jωL")
        )

        val p600c = ProblemBank.curatedProblems.first { it.id == "p_600c" } // COMPLEX_ANALYSIS
        val mismatchComplex = AnswerEvaluator.evaluate(p600c, "9 + j9", 0, 0)
        assertTrue(mismatchComplex is EvaluationResult.Incorrect && mismatchComplex.diagnosticTip.contains("residue", ignoreCase = true))

        val p326g = ProblemBank.curatedProblems.first { it.id == "p_326g" } // POWER
        val mismatchPower = AnswerEvaluator.evaluate(p326g, "1 + j2", 0, 0)
        assertTrue(mismatchPower is EvaluationResult.Incorrect && mismatchPower.diagnosticTip.contains("S = V·I*", ignoreCase = true))
    }

    @Test
    fun `inline polar precedence, trig functions, and complex helpers parse accurately`() {
        val addedPolar = AnswerEvaluator.parseComplex("10∠pi/4 rad + 5")!!
        val expectedRe = 5.0 + 10.0 / kotlin.math.sqrt(2.0)
        val expectedIm = 10.0 / kotlin.math.sqrt(2.0)
        org.junit.Assert.assertEquals(expectedRe, addedPolar.re, 1e-6)
        org.junit.Assert.assertEquals(expectedIm, addedPolar.im, 1e-6)

        val negPolar = AnswerEvaluator.parseComplex("10∠-pi/2 rad + 5")!!
        org.junit.Assert.assertEquals(5.0, negPolar.re, 1e-6)
        org.junit.Assert.assertEquals(-10.0, negPolar.im, 1e-6)

        val scaledPolar = AnswerEvaluator.parseComplex("10∠30° / 2")!!
        org.junit.Assert.assertEquals(5.0 * kotlin.math.cos(Math.toRadians(30.0)), scaledPolar.re, 1e-6)
        org.junit.Assert.assertEquals(5.0 * kotlin.math.sin(Math.toRadians(30.0)), scaledPolar.im, 1e-6)

        val multPolar = AnswerEvaluator.parseComplex("10∠30° * 2∠15°")!!
        org.junit.Assert.assertEquals(20.0 * kotlin.math.cos(Math.toRadians(45.0)), multPolar.re, 1e-6)
        org.junit.Assert.assertEquals(20.0 * kotlin.math.sin(Math.toRadians(45.0)), multPolar.im, 1e-6)

        val exprAnglePolar = AnswerEvaluator.parseComplex("10∠(30° + 15°)")!!
        org.junit.Assert.assertEquals(10.0 * kotlin.math.cos(Math.toRadians(45.0)), exprAnglePolar.re, 1e-6)
        org.junit.Assert.assertEquals(10.0 * kotlin.math.sin(Math.toRadians(45.0)), exprAnglePolar.im, 1e-6)

        val trigComplex = AnswerEvaluator.parseComplex("4*cos(60°) + j*4*sin(60°)")!!
        org.junit.Assert.assertEquals(2.0, trigComplex.re, 1e-6)
        org.junit.Assert.assertEquals(2.0 * kotlin.math.sqrt(3.0), trigComplex.im, 1e-6)

        val recJ = AnswerEvaluator.parseComplex("1/j10")!!
        org.junit.Assert.assertEquals(0.0, recJ.re, 1e-9)
        org.junit.Assert.assertEquals(-0.1, recJ.im, 1e-9)

        org.junit.Assert.assertEquals(1.0, AnswerEvaluator.parseRealExpression("tan(45°)")!!, 1e-9)
        org.junit.Assert.assertEquals(Math.toDegrees(kotlin.math.atan2(8.0, 6.0)), AnswerEvaluator.parseRealExpression("atan2(8, 6)*180/pi")!!, 1e-6)
        org.junit.Assert.assertEquals(5.0, AnswerEvaluator.parseRealExpression("abs(3 + j4)")!!, 1e-9)
        org.junit.Assert.assertEquals(3.0, AnswerEvaluator.parseRealExpression("re(3 + j4)")!!, 1e-9)
        org.junit.Assert.assertEquals(4.0, AnswerEvaluator.parseRealExpression("im(3 + j4)")!!, 1e-9)
        org.junit.Assert.assertEquals(45.0, AnswerEvaluator.parseRealExpression("arg(1 + j)*180/pi")!!, 1e-6)

        val conjVal = AnswerEvaluator.parseComplex("conj(4 - j3)")!!
        org.junit.Assert.assertEquals(4.0, conjVal.re, 1e-9)
        org.junit.Assert.assertEquals(3.0, conjVal.im, 1e-9)

        val p400a = ProblemBank.curatedProblems.first { it.id == "p_400a" }
        assertTrue(AnswerEvaluator.evaluate(p400a, "tan(90°)", 0, 0) is EvaluationResult.InvalidFormat)
    }

    @Test
    fun `compatible SI and angle unit mismatches emit non-punitive conversion guidance`() {
        val p318e = ProblemBank.curatedProblems.first { it.id == "p_318e" } // 400 μF
        val capRes = AnswerEvaluator.evaluate(p318e, "0.4 mF", 0, 0)
        assertTrue(capRes is EvaluationResult.InvalidFormat && capRes.reason.contains("μF") && capRes.reason.contains("matches"))

        val p403a = ProblemBank.curatedProblems.first { it.id == "p_403a" } // 2 ms
        val timeRes = AnswerEvaluator.evaluate(p403a, "0.002 s", 0, 0)
        assertTrue(timeRes is EvaluationResult.InvalidFormat && timeRes.reason.contains("ms") && timeRes.reason.contains("matches"))

        val wrongFamilyRes = AnswerEvaluator.evaluate(p403a, "10 A", 0, 0)
        assertTrue(wrongFamilyRes is EvaluationResult.InvalidFormat && wrongFamilyRes.reason.contains("mismatch", ignoreCase = true))

        val p100a = ProblemBank.curatedProblems.first { it.id == "p_100a" } // 45°
        val radRes = AnswerEvaluator.evaluate(p100a, "pi/4 rad", 0, 0)
        assertTrue(radRes is EvaluationResult.InvalidFormat && radRes.reason.contains("°") && radRes.reason.contains("matches"))
    }

    @Test
    fun `targeted diagnostics catch rad-vs-deg, factor-of-2, sqrt2, sqrt3, reciprocal, and 1000x slips`() {
        val p100a = ProblemBank.curatedProblems.first { it.id == "p_100a" } // 45°
        val radMistake = AnswerEvaluator.evaluate(p100a, "${Math.toRadians(45.0)}", 0, 0)
        assertTrue(radMistake is EvaluationResult.Incorrect && radMistake.diagnosticTip.contains("radian", ignoreCase = true))

        val p497d = ProblemBank.curatedProblems.first { it.id == "p_497d" } // expected 100.0
        val halfMistake = AnswerEvaluator.evaluate(p497d, "50", 0, 0)
        assertTrue(halfMistake is EvaluationResult.Incorrect && halfMistake.diagnosticTip.contains("factor of 2", ignoreCase = true))

        val sqrt2Mistake = AnswerEvaluator.evaluate(p497d, "${100.0 / kotlin.math.sqrt(2.0)}", 0, 0)
        assertTrue(sqrt2Mistake is EvaluationResult.Incorrect && sqrt2Mistake.diagnosticTip.contains("√2", ignoreCase = true))

        val sqrt3Mistake = AnswerEvaluator.evaluate(p497d, "${100.0 * kotlin.math.sqrt(3.0)}", 0, 0)
        assertTrue(sqrt3Mistake is EvaluationResult.Incorrect && sqrt3Mistake.diagnosticTip.contains("√3", ignoreCase = true))

        val prefixMistake = AnswerEvaluator.evaluate(p497d, "0.1", 0, 0)
        assertTrue(prefixMistake is EvaluationResult.Incorrect && prefixMistake.diagnosticTip.contains("10³", ignoreCase = true))

        val recScalarMistake = AnswerEvaluator.evaluate(p497d, "0.01", 0, 0)
        assertTrue(recScalarMistake is EvaluationResult.Incorrect && recScalarMistake.diagnosticTip.contains("reciprocal", ignoreCase = true))

        val p492b = ProblemBank.curatedProblems.first { it.id == "p_492b" } // 10 + j0
        val complexHalfMistake = AnswerEvaluator.evaluate(p492b, "5", 0, 0)
        assertTrue(complexHalfMistake is EvaluationResult.Incorrect && complexHalfMistake.diagnosticTip.contains("factor of 2", ignoreCase = true))

        val p101a = ProblemBank.curatedProblems.first { it.id == "p_101a" } // 6 + j8 -> 1/Z = 0.06 - j0.08
        val recComplexMistake = AnswerEvaluator.evaluate(p101a, "0.06 - j0.08", 0, 0)
        assertTrue(recComplexMistake is EvaluationResult.Incorrect && recComplexMistake.diagnosticTip.contains("reciprocal", ignoreCase = true))
    }

    @Test
    fun `every curated problem spawns valid, distinct, self-consistent Practice Variations`() {
        org.junit.Assert.assertEquals(105, ProblemBank.curatedProblems.size)
        for (base in ProblemBank.curatedProblems) {
            repeat(3) {
                val variation = ProblemBank.generateVariationFor(base)
                assertTrue("Variation of ${base.id} should be marked procedural", variation.isProcedural)
                org.junit.Assert.assertEquals(base.id, variation.sourceProblemId)
                org.junit.Assert.assertEquals(base.code, variation.sourceProblemCode)
                org.junit.Assert.assertEquals(base.category, variation.category)
                org.junit.Assert.assertEquals(base.unit, variation.unit)
                assertTrue("Variation hints missing for ${base.id}", variation.hints.isNotEmpty())
                assertTrue("Variation workedSteps missing for ${base.id}", variation.workedSteps.isNotEmpty())

                val differs = variation.parametersLine1 != base.parametersLine1 ||
                    variation.parametersLine2 != base.parametersLine2
                assertTrue("Variation of ${base.id} repeated parent parameters verbatim", differs)

                val input = when (variation.answerKind) {
                    AnswerKind.COMPLEX_RECT_OR_POLAR -> variation.expectedComplex!!.formatRect()
                    AnswerKind.REAL_SCALAR -> variation.expectedReal!!.toString()
                }
                val result = AnswerEvaluator.evaluate(variation, input, hintsUsed = 0, currentStreak = 1)
                assertTrue(
                    "Variation of ${base.id} ($input) failed evaluation: $result",
                    result is EvaluationResult.Correct
                )
            }
        }
    }
}
