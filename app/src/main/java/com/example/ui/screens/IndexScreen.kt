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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProblemBank
import com.example.model.CircuitProblem
import com.example.model.Difficulty
import com.example.model.ProblemCategory
import com.example.ui.theme.InkBlack
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeSoftBg
import com.example.ui.theme.SlateDark
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
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
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
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "Problems",
                        style = MaterialTheme.typography.displayLarge,
                        color = InkBlack
                    )
                    Text(
                        text = "${solvedIds.size}/${allProblems.size} solved  ·  ${state.progress.totalXp} XP",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = SlateMuted,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Subtle, refined Quick Drill launcher
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(SurfaceWhite)
                        .border(1.dp, OrangeAccent.copy(alpha = 0.28f), RoundedCornerShape(18.dp))
                        .clickable { onStartDrill(state.selectedCategoryFilter) }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                        .testTag("start_random_drill_card"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(OrangeSoftBg, RoundedCornerShape(11.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Bolt,
                            contentDescription = "Random Drill",
                            tint = OrangeAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when {
                                state.selectedCategoryFilter != null && state.selectedDifficultyFilter != null ->
                                    "${state.selectedDifficultyFilter.label} ${state.selectedCategoryFilter.label} Drill"
                                state.selectedCategoryFilter != null ->
                                    "${state.selectedCategoryFilter.label} Drill"
                                state.selectedDifficultyFilter != null ->
                                    "${state.selectedDifficultyFilter.label} AC Drill"
                                else -> "Quick Practice Drill"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = InkBlack
                        )
                        Text(
                            text = "Randomized values each run",
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateMuted
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = OrangeAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Category Chips
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
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quiet single-line Difficulty & Unsolved strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SubtleFilterTab(
                        label = "All Levels",
                        selected = state.selectedDifficultyFilter == null,
                        onClick = { onSelectDifficulty(null) }
                    )
                    Difficulty.entries.forEach { diff ->
                        SubtleFilterTab(
                            label = diff.label,
                            selected = state.selectedDifficultyFilter == diff,
                            onClick = { onSelectDifficulty(diff) }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .width(1.dp)
                            .height(14.dp)
                            .background(SubtleBorder)
                    )
                    SubtleFilterTab(
                        label = "Unsolved",
                        selected = state.filterUnsolvedOnly,
                        onClick = onToggleUnsolvedOnly
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
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
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "All matching problems solved",
                        style = MaterialTheme.typography.titleMedium,
                        color = InkBlack
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Try a Quick Practice Drill above or clear filters.",
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
            .background(if (selected) OrangeSoftBg else SurfaceWhite)
            .border(
                width = 1.dp,
                color = if (selected) OrangeAccent.copy(alpha = 0.5f) else SubtleBorder,
                shape = RoundedCornerShape(50)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.5.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (selected) OrangeAccent else SlateDark
        )
    }
}

@Composable
private fun SubtleFilterTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) InkBlack else SurfaceWhite.copy(alpha = 0f))
            .clickable { onClick() }
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.5.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (selected) SurfaceWhite else SlateMuted
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
            .padding(horizontal = 18.dp, vertical = 15.dp)
            .testTag("problem_card_${problem.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${problem.code}  ·  ${problem.category.label}  ·  ${problem.difficulty.label}",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Medium,
                color = if (isCurrent) OrangeAccent else SlateMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = problem.title,
                style = MaterialTheme.typography.titleMedium,
                color = InkBlack,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = problem.parametersLine1,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 11.5.sp,
                color = SlateMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        if (isSolved) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = "Solved",
                tint = SuccessGreen,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = "+${problem.difficulty.xpReward}",
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = SlateMuted
            )
        }
    }
}
