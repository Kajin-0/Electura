package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.EvaluationResult
import com.example.ui.theme.ErrorBg
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.InkBlack
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeSoftBg
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.SuccessBg
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceWhite

@Composable
fun EvaluationFeedbackCard(
    evaluation: EvaluationResult,
    expectedAnswer: String,
    workedSteps: List<String>,
    showSteps: Boolean,
    onToggleSteps: () -> Unit,
    onNextProblem: () -> Unit,
    onPracticeVariation: () -> Unit,
    isProcedural: Boolean,
    isCuratedVariation: Boolean
) {
    val haptic = LocalHapticFeedback.current

    when (evaluation) {
        is EvaluationResult.Correct -> {
            val checkScale = remember(evaluation) { Animatable(0.55f) }
            LaunchedEffect(evaluation) {
                checkScale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SuccessBg, RoundedCornerShape(18.dp))
                    .border(1.dp, SuccessGreen.copy(alpha = 0.28f), RoundedCornerShape(18.dp))
                    .padding(18.dp)
                    .testTag("feedback_correct_card")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Verified",
                        tint = SuccessGreen,
                        modifier = Modifier
                            .size(22.dp)
                            .graphicsLayer {
                                scaleX = checkScale.value
                                scaleY = checkScale.value
                            }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Correct  ·  +${evaluation.xpEarned} XP",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp,
                            color = SuccessGreen
                        )
                        Text(
                            text = evaluation.parsedDisplay,
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 12.sp,
                            color = SlateDark
                        )
                    }
                }

                if (workedSteps.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (showSteps) "Hide derivation" else "Show derivation",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SuccessGreen,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .tactileClickable(pressedScale = 0.96f) { onToggleSteps() }
                            .padding(horizontal = 6.dp, vertical = 5.dp)
                    )
                    AnimatedVisibility(
                        visible = showSteps,
                        enter = fadeIn() + expandVertically(spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        WorkedStepsTray(workedSteps = workedSteps)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!isProcedural) {
                        val variationInteraction = remember { MutableInteractionSource() }
                        OutlinedButton(
                            onClick = {
                                haptic.tickLight()
                                onPracticeVariation()
                            },
                            interactionSource = variationInteraction,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .springPressScale(variationInteraction, pressedScale = 0.97f)
                                .testTag("feedback_variation_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.35f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = SurfaceWhite,
                                contentColor = InkBlack
                            )
                        ) {
                            Icon(
                                Icons.Filled.Refresh,
                                contentDescription = null,
                                tint = OrangeAccent,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Variation",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    val nextInteraction = remember { MutableInteractionSource() }
                    Button(
                        onClick = {
                            haptic.tickLight()
                            onNextProblem()
                        },
                        interactionSource = nextInteraction,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .springPressScale(nextInteraction, pressedScale = 0.97f)
                            .testTag("next_problem_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeAccent,
                            contentColor = SurfaceWhite
                        )
                    ) {
                        val nextLabel = when {
                            isCuratedVariation -> stringResource(R.string.action_new_variation)
                            isProcedural -> stringResource(R.string.action_new_drill)
                            else -> stringResource(R.string.action_next_problem)
                        }
                        Text(
                            text = nextLabel,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            if (isProcedural) Icons.Filled.Refresh else Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        is EvaluationResult.Incorrect -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ErrorBg, RoundedCornerShape(18.dp))
                    .border(1.dp, ErrorRed.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
                    .padding(18.dp)
                    .testTag("feedback_incorrect_card")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Warning,
                        contentDescription = "Mismatch",
                        tint = ErrorRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Not quite",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = ErrorRed
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Evaluated ${evaluation.parsedDisplay} — ${evaluation.diagnosticTip}",
                    style = MaterialTheme.typography.bodySmall,
                    color = InkBlack
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showSteps) "Expected: $expectedAnswer" else "Reveal solution",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ErrorRed,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .clip(RoundedCornerShape(6.dp))
                            .tactileClickable(pressedScale = 0.96f) { onToggleSteps() }
                            .padding(horizontal = 4.dp, vertical = 6.dp)
                    )
                    if (!isProcedural || isCuratedVariation) {
                        Text(
                            text = if (isCuratedVariation) "New variation" else "Practice variation",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OrangeAccent,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .clip(RoundedCornerShape(6.dp))
                                .tactileClickable(pressedScale = 0.96f) { onPracticeVariation() }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .testTag("feedback_variation_button")
                        )
                    }
                }
                AnimatedVisibility(
                    visible = showSteps && workedSteps.isNotEmpty(),
                    enter = fadeIn() + expandVertically(spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    WorkedStepsTray(workedSteps = workedSteps)
                }
            }
        }

        is EvaluationResult.InvalidFormat -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(OrangeSoftBg, RoundedCornerShape(14.dp))
                    .border(1.dp, OrangeAccent.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
                    .testTag("feedback_format_card"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = "Syntax note",
                    tint = OrangeAccent,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = evaluation.reason,
                    style = MaterialTheme.typography.bodySmall,
                    color = InkBlack
                )
            }
        }
    }
}

@Composable
private fun WorkedStepsTray(workedSteps: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
            .background(SurfaceWhite, RoundedCornerShape(12.dp))
            .border(1.dp, SubtleBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        workedSteps.forEach { step ->
            Text(
                text = step,
                fontFamily = JetBrainsMonoFontFamily,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = InkBlack
            )
        }
    }
}
