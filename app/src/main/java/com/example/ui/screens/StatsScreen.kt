package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import com.example.model.ProblemCategory
import com.example.ui.theme.CodeSurface
import com.example.ui.theme.ErrorRed
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
fun StatsScreen(
    state: CircuitGameUiState,
    onRequestReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = state.progress
    val totalCurated = ProblemBank.curatedProblems.size
    val solvedCount = progress.solvedIds.size
    val accuracy = if (progress.totalAttempts > 0) {
        (progress.totalCorrect * 100) / progress.totalAttempts
    } else {
        0
    }

    val rankTitle = when {
        progress.totalXp >= 2400 -> "Chief Network Architect"
        progress.totalXp >= 1200 -> "Senior Phasor Engineer"
        progress.totalXp >= 500 -> "Circuit Analyst"
        else -> "Apprentice Solver"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
        ) {
            Text(
                text = rankTitle,
                style = MaterialTheme.typography.labelSmall,
                color = OrangeAccent
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Telemetry",
                style = MaterialTheme.typography.displayLarge,
                color = InkBlack
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2x2 Refined Metric Tiles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricBox(
                    label = "Total XP",
                    value = "${progress.totalXp}",
                    sub = "${progress.drillsSolved} drills",
                    accent = true,
                    modifier = Modifier.weight(1f)
                )
                MetricBox(
                    label = "Accuracy",
                    value = "$accuracy%",
                    sub = "${progress.totalCorrect}/${progress.totalAttempts} verified",
                    accent = false,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricBox(
                    label = "Streak",
                    value = "${progress.currentStreak}",
                    sub = "Best ${progress.bestStreak}",
                    accent = false,
                    modifier = Modifier.weight(1f)
                )
                MetricBox(
                    label = "Solved",
                    value = "$solvedCount/$totalCurated",
                    sub = "${(solvedCount * 100) / totalCurated}% complete",
                    accent = false,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Topic Mastery Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite, RoundedCornerShape(18.dp))
                    .border(1.dp, SubtleBorder, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = "TOPIC MASTERY",
                    style = MaterialTheme.typography.labelSmall,
                    color = SlateMuted
                )
                Spacer(modifier = Modifier.height(14.dp))

                ProblemCategory.entries.forEachIndexed { index, category ->
                    val catProblems = ProblemBank.curatedProblems.filter { it.category == category }
                    val catSolved = catProblems.count { progress.solvedIds.contains(it.id) }
                    val fraction = if (catProblems.isNotEmpty()) catSolved.toFloat() / catProblems.size else 0f

                    Column(modifier = Modifier.padding(bottom = if (index == ProblemCategory.entries.lastIndex) 0.dp else 14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = category.label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = InkBlack
                            )
                            Text(
                                text = "$catSolved / ${catProblems.size}",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (fraction >= 1f) SuccessGreen else SlateMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(50))
                                .background(CodeSurface)
                        ) {
                            if (fraction > 0f) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(fraction)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(50))
                                        .background(if (fraction >= 1f) SuccessGreen else OrangeAccent)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (state.recentAttempts.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceWhite, RoundedCornerShape(18.dp))
                        .border(1.dp, SubtleBorder, RoundedCornerShape(18.dp))
                        .padding(18.dp)
                ) {
                    Text(
                        text = "RECENT SUBMISSIONS",
                        style = MaterialTheme.typography.labelSmall,
                        color = SlateMuted
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    state.recentAttempts.take(5).forEachIndexed { idx, attempt ->
                        if (idx > 0) {
                            HorizontalDivider(color = SubtleBorder, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${attempt.problemCode} · ${attempt.problemTitle}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = InkBlack,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = attempt.submittedAnswer,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 11.5.sp,
                                    color = SlateMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (attempt.isCorrect) "+${attempt.xpEarned} XP" else "Miss",
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (attempt.isCorrect) SuccessGreen else ErrorRed
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceWhite)
                    .border(1.dp, SubtleBorder, RoundedCornerShape(14.dp))
                    .clickable { onRequestReset() }
                    .testTag("reset_progress_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Reset Progress",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ErrorRed
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun MetricBox(
    label: String,
    value: String,
    sub: String,
    accent: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(if (accent) OrangeSoftBg else SurfaceWhite, RoundedCornerShape(16.dp))
            .border(1.dp, if (accent) OrangeAccent.copy(alpha = 0.25f) else SubtleBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (accent) OrangeAccent else SlateMuted,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = InkBlack
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = sub,
            fontSize = 11.5.sp,
            color = SlateDark
        )
    }
}
