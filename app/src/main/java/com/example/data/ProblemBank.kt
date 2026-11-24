package com.example.data

import com.example.model.CircuitProblem
import com.example.model.Difficulty
import com.example.model.FieldGuideEntry
import com.example.model.ProblemCategory
import kotlin.random.Random

object ProblemBank {

    val curatedProblems: List<CircuitProblem> =
        listOf(starterProblem492B) +
            (curatedAcCircuitProblems + curatedPowerLaplaceProblems + curatedComplexGaProblems)
                .sortedWith(compareBy({ it.category.ordinal }, { it.difficulty.ordinal }, { it.code }))

    val fieldGuideEntries: List<FieldGuideEntry> = fieldGuideEntriesCatalog

    fun generateRandomDrill(
        preferredCategory: ProblemCategory? = null,
        excludeCategory: ProblemCategory? = null,
        excludeTitle: String? = null,
        excludeTitles: Set<String> = emptySet(),
        preferredDifficulty: Difficulty? = null
    ): CircuitProblem {
        val category = preferredCategory ?: run {
            val pool = ProblemCategory.entries.filter { it != excludeCategory }
            (if (pool.isNotEmpty()) pool else ProblemCategory.entries).random()
        }
        val excluded = if (excludeTitle != null) excludeTitles + excludeTitle else excludeTitles
        val drillNum = Random.nextInt(500, 999)
        val drillId = "drill_${drillNum}_${nextDrillSequence()}"
        return when (category) {
            ProblemCategory.COMPLEX_ANALYSIS ->
                generateComplexAnalysisDrill(drillId, drillNum, preferredDifficulty, excluded)
            ProblemCategory.GEOMETRIC_ALGEBRA ->
                generateGeometricAlgebraDrill(drillId, drillNum, preferredDifficulty, excluded)
            ProblemCategory.ADMITTANCE ->
                generateAdmittanceDrill(drillId, drillNum, preferredDifficulty, excluded)
            ProblemCategory.IMPEDANCE ->
                generateImpedanceDrill(drillId, drillNum, preferredDifficulty, excluded)
            ProblemCategory.RESONANCE ->
                generateResonanceDrill(drillId, drillNum, preferredDifficulty, excluded)
            ProblemCategory.POWER ->
                generatePowerDrill(drillId, drillNum, preferredDifficulty, excluded)
            ProblemCategory.LAPLACE ->
                generateLaplaceDrill(drillId, drillNum, preferredDifficulty, excluded)
        }
    }

    fun variationSignature(problem: CircuitProblem): String =
        "${problem.prompt}|${problem.parametersLine1}|${problem.parametersLine2}"

    fun generateVariationFor(
        problem: CircuitProblem,
        excludeSignatures: Set<String> = emptySet()
    ): CircuitProblem {
        val parent = problem.sourceProblemId?.let { srcId ->
            curatedProblems.find { it.id == srcId }
        } ?: problem
        val parentSig = variationSignature(parent)
        val currentSig = variationSignature(problem)
        var nonRepeatingFallback: CircuitProblem? = null

        repeat(24) {
            val candidate = generateAcVariation(parent)
                ?: generatePowerLaplaceVariation(parent)
                ?: generateMathVariation(parent)
            if (candidate != null) {
                val candSig = variationSignature(candidate)
                if (candSig != parentSig && candSig != currentSig) {
                    if (nonRepeatingFallback == null) {
                        nonRepeatingFallback = candidate
                    }
                    if (candSig !in excludeSignatures) {
                        return candidate
                    }
                }
            }
        }

        return nonRepeatingFallback
            ?: generateAcVariation(parent)
            ?: generatePowerLaplaceVariation(parent)
            ?: generateMathVariation(parent)
            ?: generateRandomDrill(
                preferredCategory = parent.category,
                preferredDifficulty = parent.difficulty
            ).copy(
                sourceProblemId = parent.id,
                sourceProblemCode = parent.code
            )
    }

    internal fun selectNextCuratedProblem(
        currentProblemId: String,
        solvedIds: Set<String>,
        categoryFilter: ProblemCategory?,
        difficultyFilter: Difficulty?,
        unsolvedOnly: Boolean
    ): CircuitProblem {
        val scoped = curatedProblems.filter { p ->
            (categoryFilter == null || p.category == categoryFilter) &&
                (difficultyFilter == null || p.difficulty == difficultyFilter)
        }
        val pool = scoped.ifEmpty { curatedProblems }
        val currentIdx = pool.indexOfFirst { it.id == currentProblemId }

        val candidateAfterCurrent = if (currentIdx >= 0) {
            pool.drop(currentIdx + 1).firstOrNull { it.id !in solvedIds }
        } else {
            pool.firstOrNull { it.id !in solvedIds }
        }
        if (candidateAfterCurrent != null) return candidateAfterCurrent

        val anyUnsolvedInPool = pool.firstOrNull { it.id !in solvedIds && it.id != currentProblemId }
        if (anyUnsolvedInPool != null) return anyUnsolvedInPool

        if (unsolvedOnly && categoryFilter == null && difficultyFilter == null) {
            val globalUnsolved = curatedProblems.firstOrNull { it.id !in solvedIds && it.id != currentProblemId }
            if (globalUnsolved != null) return globalUnsolved
        }

        return if (currentIdx >= 0) {
            pool[(currentIdx + 1) % pool.size]
        } else {
            pool.first()
        }
    }
}
