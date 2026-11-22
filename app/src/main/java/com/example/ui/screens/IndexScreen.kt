package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProblemBank
import com.example.model.CircuitProblem
import com.example.model.Difficulty
import com.example.model.ProblemCategory
import com.example.ui.theme.CodeSurface
import com.example.ui.theme.InkBlack
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeSoftBg
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceWhite
import com.example.viewmodel.CircuitGameUiState

@Composable
fun IndexScreen(
    state: CircuitGameUiState,
    onSelectProblem: (CircuitProblem) -> Unit,
    onStartDrill: (ProblemCategory?) -> Unit,
    onSelectCategory: (ProblemCategory?) -> Unit,
    onToggleUnsolvedOnly: () -> Unit,
    onSelectDifficulty: (Difficulty?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val allProblems = ProblemBank.curatedProblems
    val solvedIds = state.progress.solvedIds
    val filteredProblems = allProblems.filter { prob ->
        val matchesCategory = state.selectedCategoryFilter == null || prob.category == state.selectedCategoryFilter
        val matchesDifficulty = state.selectedDifficultyFilter == null || prob.difficulty == state.selectedDifficultyFilter
        val matchesUnsolved = !state.filterUnsolvedOnly || !solvedIds.contains(prob.id)
        matchesCategory && matchesDifficulty && matchesUnsolved
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PROBLEM SET",
                        style = MaterialTheme.typography.labelSmall,
                        color = OrangeAccent
                    )
                    Text(
                        text = "${solvedIds.size}/${allProblems.size} Solved  •  ${state.progress.totalXp} XP",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SlateMuted
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Circuit Index",
                    style = MaterialTheme.typography.displayLarge,
                    color = InkBlack
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Sleek Procedural Drill Launcher
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(InkBlack)
                        .clickable { onStartDrill(state.selectedCategoryFilter) }
                        .padding(horizontal = 18.dp, vertical = 16.dp)
                        .testTag("start_random_drill_card"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(OrangeAccent, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Bolt,
                            contentDescription = "Random Drill",
                            tint = SurfaceWhite,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "INFINITE DRILL",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeAccent,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = when {
                                state.selectedCategoryFilter != null && state.selectedDifficultyFilter != null ->
                                    "New ${state.selectedDifficultyFilter.label} ${state.selectedCategoryFilter.label} Drill"
                                state.selectedCategoryFilter != null ->
                                    "New ${state.selectedCategoryFilter.label} Challenge"
                                state.selectedDifficultyFilter != null ->
                                    "New ${state.selectedDifficultyFilter.label} AC Drill"
                                else -> "Generate Randomized AC Problem"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = SurfaceWhite
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = SurfaceWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Sleek Pill Filters
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChipItem(
                        label = "All",
                        selected = state.selectedCategoryFilter == null,
                        onClick = { onSelectCategory(null) }
                    )
                    ProblemCategory.entries.forEach { cat ->
                        FilterChipItem(
                            label = cat.label,
                            selected = state.selectedCategoryFilter == cat,
                            onClick = { onSelectCategory(cat) }
                        )
                    }
                    FilterChipItem(
                        label = "Unsolved",
                        selected = state.filterUnsolvedOnly,
                        onClick = onToggleUnsolvedOnly
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Difficulty Tier Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChipItem(
                        label = "All Levels",
                        selected = state.selectedDifficultyFilter == null,
                        onClick = { onSelectDifficulty(null) }
                    )
                    Difficulty.entries.forEach { diff ->
                        FilterChipItem(
                            label = diff.label,
                            selected = state.selectedDifficultyFilter == diff,
                            onClick = { onSelectDifficulty(diff) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        if (filteredProblems.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 600.dp)
                        .background(SurfaceWhite, RoundedCornerShape(18.dp))
                        .border(1.dp, SubtleBorder, RoundedCornerShape(18.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "All matching problems solved!",
                        style = MaterialTheme.typography.titleMedium,
                        color = InkBlack
                    )
                    Text(
                        text = "Try Infinite Drill above or clear filters.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateMuted
                    )
                }
            }
        } else {
            items(filteredProblems, key = { it.id }) { problem ->
                val isSolved = solvedIds.contains(problem.id)
                val isCurrent = state.currentProblem.id == problem.id
                ProblemIndexCard(
                    problem = problem,
                    isSolved = isSolved,
                    isCurrent = isCurrent,
                    onClick = { onSelectProblem(problem) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(34.dp)
            .clip(RoundedCornerShape(50))
            .background(if (selected) InkBlack else SurfaceWhite)
            .border(1.dp, if (selected) InkBlack else SubtleBorder, RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) SurfaceWhite else InkBlack
        )
    }
}

@Composable
private fun ProblemIndexCard(
    problem: CircuitProblem,
    isSolved: Boolean,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 600.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceWhite)
            .border(
                width = if (isCurrent) 1.5.dp else 1.dp,
                color = if (isCurrent) OrangeAccent else SubtleBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag("problem_card_${problem.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = problem.code,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrent) SurfaceWhite else OrangeAccent,
                    modifier = Modifier
                        .background(
                            if (isCurrent) OrangeAccent else OrangeSoftBg,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                )
                Text(
                    text = "${problem.category.label} • ${problem.difficulty.label}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = SlateMuted
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = problem.title,
                style = MaterialTheme.typography.titleMedium,
                color = InkBlack
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${problem.parametersLine1}  |  ${problem.parametersLine2}",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 11.sp,
                color = SlateMuted,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        if (isSolved) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = "Solved",
                tint = SuccessGreen,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Text(
                text = "+${problem.difficulty.xpReward}",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SlateMuted,
                modifier = Modifier
                    .background(CodeSurface, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
