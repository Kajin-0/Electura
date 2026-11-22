package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PersistedProgress
import com.example.data.ProblemBank
import com.example.data.ProgressRepository
import com.example.domain.AnswerEvaluator
import com.example.domain.EvaluationResult
import com.example.model.AppTab
import com.example.model.AttemptRecord
import com.example.model.CircuitProblem
import com.example.model.Difficulty
import com.example.model.ProblemCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CircuitGameUiState(
    val activeTab: AppTab = AppTab.SOLVE,
    val currentProblem: CircuitProblem = ProblemBank.curatedProblems.first(),
    val activeDrillCategory: ProblemCategory? = null,
    val userInput: String = "",
    val hintsRevealed: Int = 0,
    val evaluation: EvaluationResult? = null,
    val showSolutionSteps: Boolean = false,
    val selectedCategoryFilter: ProblemCategory? = null,
    val selectedDifficultyFilter: Difficulty? = null,
    val filterUnsolvedOnly: Boolean = false,
    val progress: PersistedProgress = PersistedProgress(),
    val recentAttempts: List<AttemptRecord> = emptyList(),
    val showHistorySheet: Boolean = false,
    val showResetConfirmDialog: Boolean = false,
    val recentDrillTitles: List<String> = emptyList()
)

class CircuitGameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProgressRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(CircuitGameUiState())
    val uiState: StateFlow<CircuitGameUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.progressFlow.collect { saved ->
                _uiState.update { it.copy(progress = saved) }
            }
        }
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun selectProblem(problem: CircuitProblem) {
        _uiState.update {
            it.copy(
                activeTab = AppTab.SOLVE,
                currentProblem = problem,
                userInput = "",
                hintsRevealed = 0,
                evaluation = null,
                showSolutionSteps = false
            )
        }
    }

    fun startRandomDrill(category: ProblemCategory? = null) {
        val currentState = _uiState.value
        val current = currentState.currentProblem
        val drill = ProblemBank.generateRandomDrill(
            preferredCategory = category,
            excludeCategory = if (category == null && current.isProcedural) current.category else null,
            excludeTitle = if (current.isProcedural) current.title else null,
            excludeTitles = currentState.recentDrillTitles.toSet(),
            preferredDifficulty = currentState.selectedDifficultyFilter
        )
        _uiState.update {
            it.copy(
                activeTab = AppTab.SOLVE,
                currentProblem = drill,
                activeDrillCategory = category,
                userInput = "",
                hintsRevealed = 0,
                evaluation = null,
                showSolutionSteps = false,
                recentDrillTitles = (it.recentDrillTitles + drill.title).takeLast(3)
            )
        }
    }

    fun onBackPressedInSolve() {
        _uiState.update { it.copy(activeTab = AppTab.INDEX) }
    }

    fun updateUserInput(value: String) {
        _uiState.update {
            it.copy(
                userInput = value,
                evaluation = if (it.evaluation is EvaluationResult.InvalidFormat || it.evaluation is EvaluationResult.Incorrect) null else it.evaluation
            )
        }
    }

    fun appendSymbol(symbol: String) {
        _uiState.update { state ->
            val updated = when (symbol) {
                "CLR" -> ""
                "⌫" -> if (state.userInput.isNotEmpty()) state.userInput.dropLast(1) else ""
                else -> state.userInput + symbol
            }
            state.copy(
                userInput = updated,
                evaluation = if (state.evaluation is EvaluationResult.InvalidFormat || state.evaluation is EvaluationResult.Incorrect) null else state.evaluation
            )
        }
    }

    fun revealNextHint() {
        _uiState.update { state ->
            val maxHints = state.currentProblem.hints.size
            if (state.hintsRevealed < maxHints) {
                state.copy(hintsRevealed = state.hintsRevealed + 1)
            } else {
                state
            }
        }
    }

    fun toggleSolutionSteps() {
        _uiState.update { it.copy(showSolutionSteps = !it.showSolutionSteps) }
    }

    fun submitAnswer() {
        val state = _uiState.value
        val result = AnswerEvaluator.evaluate(
            problem = state.currentProblem,
            rawInput = state.userInput,
            hintsUsed = state.hintsRevealed,
            currentStreak = state.progress.currentStreak
        )

        when (result) {
            is EvaluationResult.InvalidFormat -> {
                _uiState.update { it.copy(evaluation = result) }
            }
            is EvaluationResult.Correct -> {
                val record = AttemptRecord(
                    problemId = state.currentProblem.id,
                    problemCode = state.currentProblem.code,
                    problemTitle = state.currentProblem.title,
                    submittedAnswer = state.userInput.trim(),
                    isCorrect = true,
                    xpEarned = result.xpEarned,
                    timestampMillis = System.currentTimeMillis()
                )
                _uiState.update {
                    it.copy(
                        evaluation = result,
                        showSolutionSteps = true,
                        recentAttempts = (listOf(record) + it.recentAttempts).take(30)
                    )
                }
                viewModelScope.launch {
                    repository.recordCorrectSubmission(
                        problemId = state.currentProblem.id,
                        isProcedural = state.currentProblem.isProcedural,
                        xpEarned = result.xpEarned
                    )
                }
            }
            is EvaluationResult.Incorrect -> {
                val record = AttemptRecord(
                    problemId = state.currentProblem.id,
                    problemCode = state.currentProblem.code,
                    problemTitle = state.currentProblem.title,
                    submittedAnswer = state.userInput.trim(),
                    isCorrect = false,
                    xpEarned = 0,
                    timestampMillis = System.currentTimeMillis()
                )
                _uiState.update {
                    it.copy(
                        evaluation = result,
                        recentAttempts = (listOf(record) + it.recentAttempts).take(30)
                    )
                }
                viewModelScope.launch {
                    repository.recordIncorrectSubmission()
                }
            }
        }
    }

    fun goToNextProblem() {
        val state = _uiState.value
        if (state.currentProblem.isProcedural) {
            startRandomDrill(state.activeDrillCategory)
            return
        }
        val list = ProblemBank.curatedProblems
        val currentIdx = list.indexOfFirst { it.id == state.currentProblem.id }
        val solved = state.progress.solvedIds
        val nextUnsolved = list.drop(currentIdx + 1).firstOrNull { !solved.contains(it.id) }
            ?: list.firstOrNull { !solved.contains(it.id) && it.id != state.currentProblem.id }
            ?: list[(currentIdx + 1) % list.size]
        selectProblem(nextUnsolved)
    }

    fun setCategoryFilter(category: ProblemCategory?) {
        _uiState.update { it.copy(selectedCategoryFilter = category) }
    }

    fun setDifficultyFilter(difficulty: Difficulty?) {
        _uiState.update {
            it.copy(
                selectedDifficultyFilter = if (it.selectedDifficultyFilter == difficulty) null else difficulty
            )
        }
    }

    fun toggleFilterUnsolvedOnly() {
        _uiState.update { it.copy(filterUnsolvedOnly = !it.filterUnsolvedOnly) }
    }

    fun toggleQuickKeypad() {
        val next = !_uiState.value.progress.showQuickKeypad
        viewModelScope.launch {
            repository.setShowQuickKeypad(next)
        }
    }

    fun setHistorySheetVisible(visible: Boolean) {
        _uiState.update { it.copy(showHistorySheet = visible) }
    }

    fun setResetConfirmDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(showResetConfirmDialog = visible) }
    }

    fun confirmResetProgress() {
        _uiState.update {
            it.copy(
                showResetConfirmDialog = false,
                recentAttempts = emptyList(),
                evaluation = null,
                hintsRevealed = 0,
                showSolutionSteps = false
            )
        }
        viewModelScope.launch {
            repository.resetAllProgress()
        }
    }
}
