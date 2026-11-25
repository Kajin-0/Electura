package com.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import com.example.data.ProblemBank
import com.example.model.CircuitTopology
import com.example.ui.components.CircuitDiagramCanvas
import com.example.ui.theme.MyApplicationTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], qualifiers = "w411dp-h915dp")
class GreetingScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun solve_and_navigate_tabs_cuj() {
        composeTestRule.setContent {
            MyApplicationTheme {
                CircuitAppRoot()
            }
        }

        // Verify app opens on Index browser tab
        composeTestRule.onNodeWithTag("start_random_drill_card").assertIsDisplayed()

        // Navigate to Solve tab (defaults to Problem No. 492B) and verify AC Context callout
        composeTestRule.onNodeWithTag("nav_tab_solve").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("ac_context_callout").assertIsDisplayed()
        composeTestRule.onNodeWithTag("submit_solution_button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("answer_input_field").performTextInput("10 + j0")
        composeTestRule.onNodeWithTag("submit_solution_button").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("feedback_correct_card").assertIsDisplayed()

        // Navigate to Index, Stats, Guide tabs
        composeTestRule.onNodeWithTag("nav_tab_index").performClick()
        composeTestRule.onNodeWithTag("start_random_drill_card").assertIsDisplayed()

        composeTestRule.onNodeWithTag("nav_tab_stats").performClick()
        composeTestRule.onNodeWithText("TOPIC MASTERY").assertIsDisplayed()

        composeTestRule.onNodeWithTag("nav_tab_guide").performClick()
        composeTestRule.onNodeWithTag("phasor_converter_input").assertIsDisplayed()
    }

    @Test
    fun stats_shows_retry_badge_reopens_solve_and_negative_angle_ga_diagram_renders() {
        composeTestRule.setContent {
            MyApplicationTheme {
                CircuitAppRoot()
            }
        }

        // Switch from initial Index tab to Solve and submit an incorrect answer
        composeTestRule.onNodeWithTag("nav_tab_solve").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("answer_input_field").performTextInput("99")
        composeTestRule.onNodeWithTag("submit_solution_button").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("nav_tab_stats").performClick()
        composeTestRule.onNodeWithTag("stats_attempt_row_0").performScrollTo()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
        composeTestRule.onNodeWithTag("stats_attempt_row_0").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("submit_solution_button").performScrollTo().assertIsDisplayed()

        // Navigate to Index -> Geometric Algebra and open a negative-angle GA problem (p_707b)
        composeTestRule.onNodeWithTag("nav_tab_index").performClick()
        composeTestRule.onNodeWithText("Geometric Algebra").performScrollTo().performClick()
        composeTestRule.onNodeWithTag("index_problem_list")
            .performScrollToNode(hasTestTag("problem_card_p_707b"))
        composeTestRule.onNodeWithTag("problem_card_p_707b").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("ac_context_callout").assertIsDisplayed()
    }

    @Test
    fun multistep_guided_solve_cuj() {
        composeTestRule.setContent {
            MyApplicationTheme {
                CircuitAppRoot()
            }
        }

        // Open multi-step mastery problem p_124f directly from the Index launch screen
        composeTestRule.onNodeWithTag("index_problem_list")
            .performScrollToNode(hasTestTag("problem_card_p_124f"))
        composeTestRule.onNodeWithTag("problem_card_p_124f").performClick()
        composeTestRule.waitForIdle()

        // Step 1 of 3
        composeTestRule.onNodeWithTag("step_progress_header").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("STEP 1 OF 3").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("active_step_prompt").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("answer_input_field").performScrollTo().performTextInput("20")
        composeTestRule.onNodeWithTag("submit_solution_button").performScrollTo().performClick()
        composeTestRule.waitForIdle()

        // Step 2 of 3
        composeTestRule.onNodeWithTag("step_advance_banner").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("verified_step_0").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("STEP 2 OF 3").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("answer_input_field").performScrollTo().performTextInput("10 - j10")
        composeTestRule.onNodeWithTag("submit_solution_button").performScrollTo().performClick()
        composeTestRule.waitForIdle()

        // Step 3 of 3
        composeTestRule.onNodeWithTag("verified_step_1").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithText("STEP 3 OF 3").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag("answer_input_field").performScrollTo().performTextInput("-1 + j1")
        composeTestRule.onNodeWithTag("submit_solution_button").performScrollTo().performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("feedback_correct_card").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun all_circuit_topologies_render_cleanly() {
        val sampleByTopology = CircuitTopology.entries.map { topology ->
            topology to (
                ProblemBank.curatedProblems.firstOrNull { it.topology == topology }
                    ?: ProblemBank.curatedProblems.first().copy(topology = topology)
                )
        }
        val usedTopologies = ProblemBank.curatedProblems.map { it.topology }.toSet()
        CircuitTopology.entries.forEach { topology ->
            assertTrue(
                "Expected curatedProblems to include $topology",
                topology in usedTopologies
            )
        }

        composeTestRule.setContent {
            MyApplicationTheme {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    sampleByTopology.forEach { (topology, problem) ->
                        CircuitDiagramCanvas(
                            problem = problem,
                            modifier = Modifier.testTag("diagram_${topology.name}")
                        )
                    }
                }
            }
        }

        composeTestRule.waitForIdle()
        CircuitTopology.entries.forEach { topology ->
            composeTestRule.onNodeWithTag("diagram_${topology.name}")
                .performScrollTo()
                .assertIsDisplayed()
        }
    }

    @Test
    fun tap_schematic_opens_zoom_inspector_and_controls_work() {
        composeTestRule.setContent {
            MyApplicationTheme {
                CircuitAppRoot()
            }
        }

        // Open Solve screen and tap the annotated schematic card
        composeTestRule.onNodeWithTag("nav_tab_solve").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("circuit_diagram_card").performScrollTo().performClick()
        composeTestRule.waitForIdle()

        // Verify expanded dialog, zoom controls, and close action
        composeTestRule.onNodeWithTag("expanded_schematic_dialog").assertIsDisplayed()
        composeTestRule.onNodeWithText("1.0×").assertIsDisplayed()

        composeTestRule.onNodeWithTag("schematic_zoom_in_button").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("1.5×").assertIsDisplayed()

        composeTestRule.onNodeWithTag("schematic_zoom_out_button").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("1.0×").assertIsDisplayed()

        composeTestRule.onNodeWithTag("schematic_zoom_in_button").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("schematic_zoom_reset_button").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("1.0×").assertIsDisplayed()

        composeTestRule.onNodeWithTag("expanded_schematic_close_button").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("expanded_schematic_dialog").assertDoesNotExist()
    }
}
