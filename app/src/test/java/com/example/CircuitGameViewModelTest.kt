package com.example

import android.app.Application
import android.os.Looper
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.test.core.app.ApplicationProvider
import com.example.data.ProblemBank
import com.example.data.ProgressRepository
import com.example.domain.EvaluationResult
import com.example.model.AppTab
import com.example.model.AttemptRecord
import com.example.model.ProblemCategory
import com.example.viewmodel.CircuitGameViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class CircuitGameViewModelTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var dataStoreScope: CoroutineScope
    private lateinit var viewModel: CircuitGameViewModel

    @Before
    fun setUp() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        dataStoreScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val dataStore = PreferenceDataStoreFactory.create(
            scope = dataStoreScope,
            produceFile = { tempFolder.newFile("test_progress.preferences_pb") }
        )
        viewModel = CircuitGameViewModel(app, ProgressRepository(dataStore))
        shadowOf(Looper.getMainLooper()).idle()
    }

    @After
    fun tearDown() {
        dataStoreScope.cancel()
    }

    @Test
    fun `duplicate submit while Correct is ignored and input edits clear evaluation`() {
        viewModel.updateUserInput("10 + j0")
        viewModel.submitAnswer()

        val firstEval = viewModel.uiState.value.evaluation
        assertTrue(firstEval is EvaluationResult.Correct)
        assertEquals(1, viewModel.uiState.value.recentAttempts.size)

        // Re-submitting without changing input must not add duplicate attempts or inflate XP
        viewModel.submitAnswer()
        assertEquals(1, viewModel.uiState.value.recentAttempts.size)

        // Calling updateUserInput with identical value preserves Correct
        viewModel.updateUserInput("10 + j0")
        assertTrue(viewModel.uiState.value.evaluation is EvaluationResult.Correct)

        // Modifying input resets evaluation so user can re-check
        viewModel.updateUserInput("10 + j1")
        assertNull(viewModel.uiState.value.evaluation)

        // appendSymbol with CLR clears input & evaluation
        viewModel.appendSymbol("CLR")
        assertEquals("", viewModel.uiState.value.userInput)
        assertNull(viewModel.uiState.value.evaluation)
    }

    @Test
    fun `re-solving an already solved curated problem displays 25 practice XP`() {
        viewModel.updateUserInput("10 + j0")
        viewModel.submitAnswer()
        val initialEval = viewModel.uiState.value.evaluation as EvaluationResult.Correct
        assertEquals(175, initialEval.xpEarned)

        // Pump Main Looper so DataStore persistence updates progress.solvedIds via viewModelScope
        var attempts = 0
        while (!viewModel.uiState.value.progress.solvedIds.contains("p_492b") && attempts < 40) {
            Thread.sleep(25)
            shadowOf(Looper.getMainLooper()).idle()
            attempts++
        }
        assertTrue(viewModel.uiState.value.progress.solvedIds.contains("p_492b"))

        // Re-select starter problem and solve again
        val starter = ProblemBank.curatedProblems.first { it.id == "p_492b" }
        viewModel.selectProblem(starter)
        viewModel.updateUserInput("10 + j0")
        viewModel.submitAnswer()

        val resolveEval = viewModel.uiState.value.evaluation as EvaluationResult.Correct
        assertEquals(25, resolveEval.xpEarned)
        assertEquals(25, viewModel.uiState.value.recentAttempts.first().xpEarned)
    }

    @Test
    fun `reopenAttempt restores both curated problems and procedural drill snapshots into Solve`() {
        val drill = ProblemBank.generateRandomDrill(preferredCategory = ProblemCategory.IMPEDANCE)
        val drillAttempt = AttemptRecord(
            problemId = drill.id,
            problemCode = drill.code,
            problemTitle = drill.title,
            submittedAnswer = drill.expectedDisplay(),
            isCorrect = true,
            xpEarned = drill.difficulty.xpReward,
            timestampMillis = 200L,
            drillSnapshot = drill
        )

        viewModel.selectTab(AppTab.STATS)
        viewModel.reopenAttempt(drillAttempt)

        assertEquals(AppTab.SOLVE, viewModel.uiState.value.activeTab)
        assertEquals(drill.id, viewModel.uiState.value.currentProblem.id)
        assertEquals("", viewModel.uiState.value.userInput)
        assertNull(viewModel.uiState.value.evaluation)

        val curatedAttempt = AttemptRecord(
            problemId = "p_101a",
            problemCode = "No. 101A",
            problemTitle = "Series RL Impedance",
            submittedAnswer = "6 + j8",
            isCorrect = false,
            xpEarned = 0,
            timestampMillis = 201L,
            drillSnapshot = null
        )

        viewModel.selectTab(AppTab.STATS)
        viewModel.reopenAttempt(curatedAttempt)

        assertEquals(AppTab.SOLVE, viewModel.uiState.value.activeTab)
        assertEquals("p_101a", viewModel.uiState.value.currentProblem.id)
        assertNull(viewModel.uiState.value.activeDrillCategory)
    }

    @Test
    fun `reopening procedural drill syncs activeDrillCategory and confirmResetProgress clears session fields`() {
        viewModel.startRandomDrill(ProblemCategory.IMPEDANCE)
        assertEquals(ProblemCategory.IMPEDANCE, viewModel.uiState.value.activeDrillCategory)

        val resonanceDrill = ProblemBank.generateRandomDrill(preferredCategory = ProblemCategory.RESONANCE)
        val resonanceAttempt = AttemptRecord(
            problemId = resonanceDrill.id,
            problemCode = resonanceDrill.code,
            problemTitle = resonanceDrill.title,
            submittedAnswer = resonanceDrill.expectedDisplay(),
            isCorrect = false,
            xpEarned = 0,
            timestampMillis = 300L,
            drillSnapshot = resonanceDrill
        )

        viewModel.reopenAttempt(resonanceAttempt)
        assertEquals(ProblemCategory.RESONANCE, viewModel.uiState.value.activeDrillCategory)

        viewModel.goToNextProblem()
        assertEquals(ProblemCategory.RESONANCE, viewModel.uiState.value.currentProblem.category)

        viewModel.updateUserInput("123")
        viewModel.confirmResetProgress()

        assertEquals("", viewModel.uiState.value.userInput)
        assertNull(viewModel.uiState.value.activeDrillCategory)
        assertTrue(viewModel.uiState.value.recentDrillTitles.isEmpty())
        assertTrue(viewModel.uiState.value.recentAttempts.isEmpty())
        assertNull(viewModel.uiState.value.evaluation)
    }
}
