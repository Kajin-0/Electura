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
    val recentDrillTitles: List<String> = emptyList(),
    val recentVariationSignatures: Map<String, List<String>> = emptyMap()
)

class CircuitGameViewModel(
    application: Application,
    private val repository: ProgressRepository
) : AndroidViewModel(application) {

    constructor(application: Application) : this(
        application,
        ProgressRepository(application.applicationContext)
    )

    private val _uiState = MutableStateFlow(CircuitGameUiState())
    val uiState: StateFlow<CircuitGameUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.progressFlow.collect { saved ->
                _uiState.update {
                    it.copy(
                        progress = saved,
                        recentAttempts = saved.recentAttempts
                    )
                }
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

    fun reopenAttempt(attempt: AttemptRecord) {
        val target = ProblemBank.curatedProblems.find { it.id == attempt.problemId }
            ?: attempt.drillSnapshot
            ?: return
        _uiState.update { it.copy(showHistorySheet = false) }
        selectProblem(target)
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

    fun startVariation(baseProblem: CircuitProblem = _uiState.value.currentProblem) {
        val currentState = _uiState.value
        val parentId = baseProblem.sourceProblemId ?: baseProblem.id
        val history = currentState.recentVariationSignatures[parentId].orEmpty()
        val variation = ProblemBank.generateVariationFor(
            problem = baseProblem,
            excludeSignatures = history.toSet()
        )
        val sig = ProblemBank.variationSignature(variation)
        val updatedHistory = (history.filter { it != sig } + sig).takeLast(8)
        _uiState.update {
            it.copy(
                activeTab = AppTab.SOLVE,
                currentProblem = variation,
                userInput = "",
                hintsRevealed = 0,
                evaluation = null,
                showSolutionSteps = false,
                recentVariationSignatures = it.recentVariationSignatures + (parentId to updatedHistory)
            )
        }
    }

    fun returnToOriginalProblem() {
        val sourceId = _uiState.value.currentProblem.sourceProblemId ?: return
        val original = ProblemBank.curatedProblems.find { it.id == sourceId } ?: return
        selectProblem(original)
    }

    fun onBackPressedInSolve() {
        _uiState.update { it.copy(activeTab = AppTab.INDEX) }
    }

    fun updateUserInput(value: String) {
        _uiState.update {
            if (value == it.userInput) {
                it
            } else {
                it.copy(
                    userInput = value,
                    evaluation = null
                )
            }
        }
    }

    fun appendSymbol(symbol: String) {
        _uiState.update { state ->
            val updated = when (symbol) {
                "CLR" -> ""
                "⌫" -> if (state.userInput.isNotEmpty()) state.userInput.dropLast(1) else ""
                else -> state.userInput + symbol
            }
            if (updated == state.userInput) {
                state
            } else {
                state.copy(
                    userInput = updated,
                    evaluation = null
                )
            }
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
        if (state.evaluation is EvaluationResult.Correct) return

        val result = AnswerEvaluator.evaluate(
            problem = state.currentProblem,
            rawInput = state.userInput,
            hintsUsed = state.hintsRevealed,
            currentStreak = state.progress.currentStreak
        )

        val drillSnapshot = if (state.currentProblem.isProcedural) state.currentProblem else null

        when (result) {
            is EvaluationResult.InvalidFormat -> {
                _uiState.update { it.copy(evaluation = result) }
            }
            is EvaluationResult.Correct -> {
                val alreadySolved = !state.currentProblem.isProcedural &&
                    state.progress.solvedIds.contains(state.currentProblem.id)
                val awardedXp = if (alreadySolved) 25 else result.xpEarned
                val displayedResult = if (awardedXp == result.xpEarned) result else result.copy(xpEarned = awardedXp)
                val record = AttemptRecord(
                    problemId = state.currentProblem.id,
                    problemCode = state.currentProblem.code,
                    problemTitle = state.currentProblem.title,
                    submittedAnswer = state.userInput.trim(),
                    isCorrect = true,
                    xpEarned = awardedXp,
                    timestampMillis = System.currentTimeMillis(),
                    drillSnapshot = drillSnapshot
                )
                _uiState.update {
                    it.copy(
                        evaluation = displayedResult,
                        showSolutionSteps = true,
                        recentAttempts = (listOf(record) + it.recentAttempts).take(30)
                    )
                }
                viewModelScope.launch {
                    repository.recordCorrectSubmission(
                        attempt = record,
                        isProcedural = state.currentProblem.isProcedural,
                        xpEarned = awardedXp
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
                    timestampMillis = System.currentTimeMillis(),
                    drillSnapshot = drillSnapshot
                )
                _uiState.update {
                    it.copy(
                        evaluation = result,
                        recentAttempts = (listOf(record) + it.recentAttempts).take(30)
                    )
                }
                viewModelScope.launch {
                    repository.recordIncorrectSubmission(record)
                }
            }
        }
    }

    fun goToNextProblem() {
        val state = _uiState.value
        if (state.currentProblem.sourceProblemId != null) {
            startVariation(state.currentProblem)
            return
        }
        if (state.currentProblem.isProcedural) {
            startRandomDrill(state.activeDrillCategory)
            return
        }
        val next = ProblemBank.selectNextCuratedProblem(
            currentProblemId = state.currentProblem.id,
            solvedIds = state.progress.solvedIds,
            categoryFilter = state.selectedCategoryFilter,
            difficultyFilter = state.selectedDifficultyFilter,
            unsolvedOnly = state.filterUnsolvedOnly
        )
        selectProblem(next)
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
                showSolutionSteps = false,
                recentVariationSignatures = emptyMap()
            )
        }
        viewModelScope.launch {
            repository.resetAllProgress()
        }
    }
}
