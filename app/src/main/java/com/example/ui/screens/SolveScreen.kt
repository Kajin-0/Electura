package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.EvaluationResult
import com.example.model.ProblemCategory
import com.example.ui.components.CircuitDiagramCanvas
import com.example.ui.theme.CodeSurface
import com.example.ui.theme.ErrorBg
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.InkBlack
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeSoftBg
import com.example.ui.theme.PlaceholderSlate
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.SuccessBg
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceWhite
import com.example.viewmodel.CircuitGameUiState

@Composable
fun SolveScreen(
    state: CircuitGameUiState,
    onInputChange: (String) -> Unit,
    onAppendSymbol: (String) -> Unit,
    onSubmit: () -> Unit,
    onRevealHint: () -> Unit,
    onToggleSteps: () -> Unit,
    onNextProblem: () -> Unit,
    onNewDrill: () -> Unit,
    modifier: Modifier = Modifier
) {
    val problem = state.currentProblem
    val isSolved = state.progress.solvedIds.contains(problem.id)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 600.dp)
        ) {
            // Minimal metadata breadcrumb
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${if (problem.isProcedural) "Drill" else problem.code}  ·  ${problem.category.label}  ·  +${problem.difficulty.xpReward} XP",
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = OrangeAccent
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isSolved) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Solved",
                            tint = SuccessGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    if (state.progress.currentStreak > 0) {
                        Text(
                            text = "${state.progress.currentStreak}x streak",
                            fontFamily = JetBrainsMonoFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OrangeAccent,
                            modifier = Modifier
                                .background(OrangeSoftBg, RoundedCornerShape(50))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Clear Problem Title as primary heading
            Text(
                text = problem.title,
                style = MaterialTheme.typography.headlineMedium,
                color = InkBlack,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Unified Clean Problem Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite, RoundedCornerShape(20.dp))
                    .border(1.dp, SubtleBorder, RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = problem.prompt,
                    style = MaterialTheme.typography.bodyLarge,
                    color = InkBlack,
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                // Clean Vector Schematic
                CircuitDiagramCanvas(problem = problem)

                Spacer(modifier = Modifier.height(12.dp))

                // Soft Inset Given Parameters
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CodeSurface, RoundedCornerShape(14.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = problem.parametersLine1,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.5.sp,
                        color = InkBlack
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = problem.parametersLine2,
                        fontFamily = JetBrainsMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = OrangeAccent
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = SubtleBorder, thickness = 1.dp)
                Spacer(modifier = Modifier.height(14.dp))

                // Method Note & Minimal Hint Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = problem.methodTitle,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SlateMuted,
                            letterSpacing = 0.9.sp
                        )
                        Text(
                            text = problem.methodNote,
                            style = MaterialTheme.typography.bodySmall,
                            color = SlateDark,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    if (state.hintsRevealed < problem.hints.size) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(OrangeSoftBg)
                                .clickable { onRevealHint() }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                                .testTag("reveal_hint_button"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Lightbulb,
                                contentDescription = stringResource(R.string.action_hint),
                                tint = OrangeAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Hint ${state.hintsRevealed + 1}/${problem.hints.size}",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OrangeAccent
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = state.hintsRevealed > 0,
                    enter = fadeIn() + expandVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        problem.hints.take(state.hintsRevealed).forEachIndexed { idx, hint ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(OrangeSoftBg, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = "Hint ${idx + 1}: $hint",
                                    fontSize = 12.5.sp,
                                    lineHeight = 18.sp,
                                    color = InkBlack
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Refined Answer Field
            BasicTextField(
                value = state.userInput,
                onValueChange = onInputChange,
                textStyle = TextStyle(
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = InkBlack
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                cursorBrush = SolidColor(OrangeAccent),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("answer_input_field"),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .background(SurfaceWhite, RoundedCornerShape(14.dp))
                            .border(
                                width = 1.dp,
                                color = if (state.userInput.isNotEmpty()) OrangeAccent.copy(alpha = 0.65f) else SubtleBorder,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.weight(1f)) {
                            if (state.userInput.isEmpty()) {
                                Text(
                                    text = problem.inputHint,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 13.5.sp,
                                    color = PlaceholderSlate,
                                    fontStyle = FontStyle.Italic
                                )
                            }
                            innerTextField()
                        }
                        if (problem.unit.isNotBlank()) {
                            Text(
                                text = problem.unit,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = SlateMuted
                            )
                        }
                    }
                }
            )

            // Calm Math Symbol Strip
            if (state.progress.showQuickKeypad) {
                Spacer(modifier = Modifier.height(10.dp))
                val symbols = if (problem.category == ProblemCategory.GEOMETRIC_ALGEBRA) {
                    listOf("e12", "j", "+", "-", ".", "/", "√(", "⌫", "CLR")
                } else {
                    listOf("j", "∠", "+", "-", ".", "/", "√(", "⌫", "CLR")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    symbols.forEach { sym ->
                        val isAction = sym == "CLR" || sym == "⌫"
                        val isMathAccent = sym == "j" || sym == "∠" || sym == "e12"
                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .widthIn(min = 42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    when {
                                        isMathAccent -> OrangeSoftBg
                                        isAction -> CodeSurface
                                        else -> SurfaceWhite
                                    }
                                )
                                .border(1.dp, SubtleBorder, RoundedCornerShape(10.dp))
                                .clickable { onAppendSymbol(sym) }
                                .padding(horizontal = 11.dp)
                                .testTag("symbol_key_$sym"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = sym,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = if (isMathAccent) OrangeAccent else InkBlack
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Concise Primary Action
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_solution_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = InkBlack,
                    contentColor = SurfaceWhite
                )
            ) {
                Text(
                    text = stringResource(R.string.action_submit_solution),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Evaluation Feedback
            AnimatedVisibility(
                visible = state.evaluation != null,
                enter = fadeIn() + expandVertically()
            ) {
                state.evaluation?.let { eval ->
                    Spacer(modifier = Modifier.height(14.dp))
                    EvaluationFeedbackCard(
                        evaluation = eval,
                        expectedAnswer = problem.expectedDisplay(),
                        workedSteps = problem.workedSteps,
                        showSteps = state.showSolutionSteps,
                        onToggleSteps = onToggleSteps,
                        onNextProblem = onNextProblem,
                        isProcedural = problem.isProcedural
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EvaluationFeedbackCard(
    evaluation: EvaluationResult,
    expectedAnswer: String,
    workedSteps: List<String>,
    showSteps: Boolean,
    onToggleSteps: () -> Unit,
    onNextProblem: () -> Unit,
    isProcedural: Boolean
) {
    when (evaluation) {
        is EvaluationResult.Correct -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SuccessBg, RoundedCornerShape(18.dp))
                    .border(1.dp, SuccessGreen.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
                    .padding(18.dp)
                    .testTag("feedback_correct_card")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Verified",
                        tint = SuccessGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Correct  ·  +${evaluation.xpEarned} XP",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
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
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onToggleSteps() }
                            .padding(vertical = 4.dp)
                    )
                    if (showSteps) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                                .background(SurfaceWhite, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            workedSteps.forEach { step ->
                                Text(
                                    text = step,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 12.sp,
                                    color = InkBlack
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onNextProblem,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("next_problem_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeAccent,
                        contentColor = SurfaceWhite
                    )
                ) {
                    Text(
                        text = if (isProcedural) stringResource(R.string.action_new_drill) else stringResource(R.string.action_next_problem),
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
                Text(
                    text = if (showSteps) "Expected: $expectedAnswer" else "Reveal solution",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ErrorRed,
                    modifier = Modifier
                        .clickable { onToggleSteps() }
                        .padding(vertical = 4.dp)
                )
                if (showSteps && workedSteps.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                            .background(SurfaceWhite, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        workedSteps.forEach { step ->
                            Text(
                                text = step,
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 12.sp,
                                color = InkBlack
                            )
                        }
                    }
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
