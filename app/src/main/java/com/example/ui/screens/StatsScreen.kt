package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProblemBank
import com.example.model.AttemptRecord
import com.example.model.ProblemCategory
import com.example.ui.components.paperCardSurface
import com.example.ui.components.rememberAnimatedInt
import com.example.ui.components.tactileClickable
import com.example.ui.components.tactilePaperCard
import com.example.ui.theme.CodeSurface
import com.example.ui.theme.CopperBorderSoft
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.HairlineBorder
import com.example.ui.theme.InkBlack
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeSoftBg
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TabularMonoStyle
import com.example.viewmodel.CircuitGameUiState

@Composable
fun StatsScreen(
    state: CircuitGameUiState,
    onRequestReset: () -> Unit,
    onSelectAttempt: (AttemptRecord) -> Unit = {},
    onScrolledChange: (Boolean) -> Unit = {},
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

    val animatedXp = rememberAnimatedInt(progress.totalXp, label = "statsXp")
    val animatedAccuracy = rememberAnimatedInt(accuracy, label = "statsAccuracy")
    val animatedStreak = rememberAnimatedInt(progress.currentStreak, label = "statsStreak")
    val animatedSolved = rememberAnimatedInt(solvedCount, label = "statsSolved")

    var metersRevealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        metersRevealed = true
    }

    val scrollState = rememberScrollState()
    val isScrolled by remember {
        derivedStateOf { scrollState.value > 8 }
    }
    LaunchedEffect(isScrolled) {
        onScrolledChange(isScrolled)
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
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
        ) {
            Text(
                text = rankTitle.uppercase(),
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

            // 2x2 Animated Metric Tiles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricBox(
                    label = "Total XP",
                    value = "$animatedXp",
                    sub = "${progress.drillsSolved} drills",
                    accent = true,
                    modifier = Modifier.weight(1f)
                )
                MetricBox(
                    label = "Accuracy",
                    value = "$animatedAccuracy%",
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
                    value = "$animatedStreak",
                    sub = "Best ${progress.bestStreak}",
                    accent = false,
                    modifier = Modifier.weight(1f)
                )
                MetricBox(
                    label = "Solved",
                    value = "$animatedSolved/$totalCurated",
                    sub = "${if (totalCurated > 0) (solvedCount * 100) / totalCurated else 0}% complete",
                    accent = false,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Topic Mastery Card with Staggered Animated Bars
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .paperCardSurface(
                        shape = RoundedCornerShape(18.dp),
                        backgroundColor = SurfaceWhite,
                        borderColor = SubtleBorder,
                        borderWidth = 1.dp,
                        elevation = 2.dp
                    )
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
                    val targetFraction = if (catProblems.isNotEmpty()) catSolved.toFloat() / catProblems.size else 0f
                    val animatedFraction by animateFloatAsState(
                        targetValue = if (metersRevealed) targetFraction else 0f,
                        animationSpec = tween(
                            durationMillis = 520,
                            delayMillis = index * 45,
                            easing = FastOutSlowInEasing
                        ),
                        label = "topicMeter_${category.name}"
                    )

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
                                style = TabularMonoStyle,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (targetFraction >= 1f) SuccessGreen else SlateMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(CodeSurface)
                        ) {
                            if (animatedFraction > 0f) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(animatedFraction.coerceIn(0f, 1f))
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(50))
                                        .background(if (targetFraction >= 1f) SuccessGreen else OrangeAccent)
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
                        .paperCardSurface(
                            shape = RoundedCornerShape(18.dp),
                            backgroundColor = SurfaceWhite,
                            borderColor = SubtleBorder,
                            borderWidth = 1.dp,
                            elevation = 2.dp
                        )
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
                            HorizontalDivider(
                                color = HairlineBorder,
                                thickness = 0.75.dp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .minimumInteractiveComponentSize()
                                .clip(RoundedCornerShape(10.dp))
                                .tactileClickable(pressedScale = 0.985f) { onSelectAttempt(attempt) }
                                .padding(horizontal = 6.dp, vertical = 6.dp)
                                .testTag("stats_attempt_row_$idx"),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(
                                        if (attempt.isCorrect) SuccessGreen else ErrorRed,
                                        CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.width(10.dp))
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
                                    style = TabularMonoStyle,
                                    fontSize = 11.5.sp,
                                    color = SlateMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (attempt.isCorrect) "+${attempt.xpEarned} XP" else "Retry",
                                style = TabularMonoStyle,
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
                    .height(48.dp)
                    .tactilePaperCard(
                        shape = RoundedCornerShape(14.dp),
                        backgroundColor = SurfaceWhite,
                        borderColor = SubtleBorder,
                        borderWidth = 1.dp,
                        elevation = 1.5.dp,
                        pressedScale = 0.98f,
                        onClick = onRequestReset
                    )
                    .testTag("reset_progress_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Reset Progress",
                    fontSize = 13.sp,
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
            .paperCardSurface(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = if (accent) OrangeSoftBg else SurfaceWhite,
                borderColor = if (accent) CopperBorderSoft else SubtleBorder,
                borderWidth = 1.dp,
                elevation = if (accent) 2.dp else 1.5.dp
            )
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
            style = TabularMonoStyle,
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
