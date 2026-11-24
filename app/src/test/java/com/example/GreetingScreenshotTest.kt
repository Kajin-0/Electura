package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.ui.theme.MyApplicationTheme
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

        // Verify initial Solve screen displays Problem No. 492B
        composeTestRule.onNodeWithTag("submit_solution_button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("answer_input_field").performTextInput("10 + j0")
        composeTestRule.onNodeWithTag("submit_solution_button").performClick()
        composeTestRule.onNodeWithTag("feedback_correct_card").assertIsDisplayed()

        // Navigate to Index, Stats, Guide tabs
        composeTestRule.onNodeWithTag("nav_tab_index").performClick()
        composeTestRule.onNodeWithTag("start_random_drill_card").assertIsDisplayed()

        composeTestRule.onNodeWithTag("nav_tab_stats").performClick()
        composeTestRule.onNodeWithText("TOPIC MASTERY").assertIsDisplayed()

        composeTestRule.onNodeWithTag("nav_tab_guide").performClick()
        composeTestRule.onNodeWithTag("phasor_converter_input").assertIsDisplayed()
    }
}
