package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.EvaluationResult
import com.example.model.ProblemCategory
import com.example.ui.components.AcContextCallout
import com.example.ui.components.CircuitDiagramCanvas
import com.example.ui.components.EvaluationFeedbackCard
import com.example.ui.components.horizontalEdgeFade
import com.example.ui.components.paperCardSurface
import com.example.ui.components.pulseConfirm
import com.example.ui.components.recessedPaperWell
import com.example.ui.components.springPressScale
import com.example.ui.components.tactileClickable
import com.example.ui.components.tickLight
import com.example.ui.theme.CodeSurface
import com.example.ui.theme.CopperBorderSoft
import com.example.ui.theme.CopperKeycapRim
import com.example.ui.theme.HairlineBorder
import com.example.ui.theme.InkBlack
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.KeycapRim
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeSoftBg
import com.example.ui.theme.PaperShadowTint
import com.example.ui.theme.PlaceholderSlate
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TabularMonoStyle
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
    onPracticeVariation: () -> Unit = {},
    onReturnToOriginal: () -> Unit = {},
    onScrolledChange: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val problem = state.currentProblem
    val isCuratedVariation = problem.sourceProblemId != null
    val isSolved = !problem.isProcedural && state.progress.solvedIds.contains(problem.id)
    val scrollState = rememberScrollState()
    val haptic = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var isInputFocused by remember { mutableStateOf(false) }
    val isVerified = state.evaluation is EvaluationResult.Correct
    val problemTransitionKey = "${problem.id}_${problem.sourceProblemId ?: "orig"}_${problem.parametersLine1}_${problem.parametersLine2}"

    val isScrolled by remember {
        derivedStateOf { scrollState.value > 8 }
    }
    LaunchedEffect(isScrolled) {
        onScrolledChange(isScrolled)
    }

    LaunchedEffect(problemTransitionKey) {
        scrollState.scrollTo(0)
    }

    // Error / format shake animation & semantic evaluation haptics
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(state.evaluation) {
        when (state.evaluation) {
            is EvaluationResult.Correct -> {
                haptic.pulseConfirm()
                kotlinx.coroutines.delay(180)
                scrollState.animateScrollTo(scrollState.maxValue)
            }
            is EvaluationResult.Incorrect, is EvaluationResult.InvalidFormat -> {
                haptic.tickLight()
                val amplitudes = listOf(-12f, 12f, -8f, 8f, -4f, 0f)
                amplitudes.forEach { target ->
                    shakeOffset.animateTo(target, animationSpec = tween(42))
                }
                kotlinx.coroutines.delay(120)
                scrollState.animateScrollTo(scrollState.maxValue)
            }
            null -> Unit
        }
    }

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
            AnimatedContent(
                targetState = problem,
                contentKey = { target ->
                    "${target.id}_${target.sourceProblemId ?: "orig"}_${target.parametersLine1}_${target.parametersLine2}"
                },
                transitionSpec = {
                    (fadeIn(tween(220)) + slideInVertically(tween(240)) { it / 28 })
                        .togetherWith(fadeOut(tween(140)))
                },
                label = "problemTransition"
            ) { targetProblem ->
                val targetIsCuratedVariation = targetProblem.sourceProblemId != null
                val targetIsSolved = !targetProblem.isProcedural && state.progress.solvedIds.contains(targetProblem.id)
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Minimal metadata breadcrumb
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val badgeCode = when {
                            targetProblem.sourceProblemCode != null -> "Variation of ${targetProblem.sourceProblemCode}"
                            targetProblem.isProcedural -> "Drill"
                            else -> targetProblem.code
                        }
                        Text(
                            text = "$badgeCode  ·  ${targetProblem.category.label}  ·  +${targetProblem.difficulty.xpReward} XP",
                            style = TabularMonoStyle,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = OrangeAccent,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (targetIsSolved) {
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
                                    style = TabularMonoStyle,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OrangeAccent,
                                    modifier = Modifier
                                        .background(OrangeSoftBg, RoundedCornerShape(50))
                                        .border(1.dp, CopperBorderSoft, RoundedCornerShape(50))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // Clear Problem Title + concise variation controls
                    Text(
                        text = targetProblem.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = InkBlack,
                        modifier = Modifier.padding(bottom = if (!targetProblem.isProcedural || targetIsCuratedVariation) 10.dp else 16.dp)
                    )

                    if (!targetProblem.isProcedural || targetIsCuratedVariation) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 14.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (targetIsCuratedVariation) {
                                Row(
                                    modifier = Modifier
                                        .minimumInteractiveComponentSize()
                                        .clip(RoundedCornerShape(50))
                                        .background(SurfaceWhite)
                                        .border(1.dp, SubtleBorder, RoundedCornerShape(50))
                                        .tactileClickable(pressedScale = 0.96f) { onReturnToOriginal() }
                                        .padding(horizontal = 12.dp, vertical = 7.dp)
                                        .testTag("return_original_button"),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.action_original_problem),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = SlateDark
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .clip(RoundedCornerShape(50))
                                    .background(OrangeSoftBg)
                                    .border(1.dp, CopperBorderSoft, RoundedCornerShape(50))
                                    .tactileClickable(pressedScale = 0.96f) { onPracticeVariation() }
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                                    .testTag("practice_variation_button"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.Refresh,
                                    contentDescription = null,
                                    tint = OrangeAccent,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = if (targetIsCuratedVariation) {
                                        stringResource(R.string.action_new_variation)
                                    } else {
                                        stringResource(R.string.action_practice_variation)
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OrangeAccent
                                )
                            }
                        }
                    }

                    // Unified Layered Paper Problem Card
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .paperCardSurface(
                                shape = RoundedCornerShape(20.dp),
                                backgroundColor = SurfaceWhite,
                                borderColor = SubtleBorder,
                                borderWidth = 1.dp,
                                elevation = 2.5.dp
                            )
                            .padding(horizontal = 18.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = targetProblem.prompt,
                            style = MaterialTheme.typography.bodyLarge,
                            color = InkBlack,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Drafting-Paper Vector Schematic
                        CircuitDiagramCanvas(problem = targetProblem)

                        Spacer(modifier = Modifier.height(10.dp))

                        // Recessed Inset Given Parameters Well
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .recessedPaperWell(
                                    shape = RoundedCornerShape(14.dp),
                                    backgroundColor = CodeSurface,
                                    borderColor = HairlineBorder
                                )
                                .padding(horizontal = 14.dp, vertical = 11.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = targetProblem.parametersLine1,
                                style = TabularMonoStyle,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.5.sp,
                                color = InkBlack
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = targetProblem.parametersLine2,
                                style = TabularMonoStyle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = OrangeAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = SubtleBorder, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Method Note & Minimal Hint Pill
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = targetProblem.methodTitle,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SlateMuted,
                                    letterSpacing = 0.9.sp
                                )
                                Text(
                                    text = targetProblem.methodNote,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SlateDark,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            if (state.hintsRevealed < targetProblem.hints.size) {
                                Spacer(modifier = Modifier.width(12.dp))
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .background(OrangeSoftBg)
                                        .border(1.dp, CopperBorderSoft, RoundedCornerShape(50))
                                        .tactileClickable(pressedScale = 0.95f) { onRevealHint() }
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
                                        text = "Hint ${state.hintsRevealed + 1}/${targetProblem.hints.size}",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = OrangeAccent
                                    )
                                }
                            }
                        }

                        if (targetProblem.acContext.isNotBlank()) {
                            AcContextCallout(
                                acContext = targetProblem.acContext,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        AnimatedVisibility(
                            visible = state.hintsRevealed > 0,
                            enter = fadeIn() + expandVertically(spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                targetProblem.hints.take(state.hintsRevealed).forEachIndexed { idx, hint ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(OrangeSoftBg)
                                            .border(1.dp, CopperBorderSoft.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                            .padding(horizontal = 12.dp, vertical = 10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(2.5.dp)
                                                .height(16.dp)
                                                .background(OrangeAccent, RoundedCornerShape(2.dp))
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
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
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val borderColor by animateColorAsState(
                targetValue = when {
                    isInputFocused -> OrangeAccent
                    state.userInput.isNotEmpty() -> OrangeAccent.copy(alpha = 0.65f)
                    else -> SubtleBorder
                },
                animationSpec = tween(180),
                label = "inputBorderColor"
            )
            val borderWidth by animateDpAsState(
                targetValue = if (isInputFocused) 1.5.dp else 1.dp,
                animationSpec = tween(180),
                label = "inputBorderWidth"
            )

            // Refined Answer Field with Focus Depth & Shake Physics
            val inputElevation by animateDpAsState(
                targetValue = if (isInputFocused) 3.5.dp else 1.dp,
                animationSpec = tween(180),
                label = "inputElevation"
            )
            BasicTextField(
                value = state.userInput,
                onValueChange = onInputChange,
                textStyle = TextStyle(
                    fontFamily = JetBrainsMonoFontFamily,
                    fontFeatureSettings = "tnum",
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = InkBlack
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    if (!isVerified) {
                        onSubmit()
                    }
                }),
                cursorBrush = SolidColor(OrangeAccent),
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { translationX = shakeOffset.value }
                    .onFocusChanged { isInputFocused = it.isFocused }
                    .testTag("answer_input_field"),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .shadow(
                                elevation = inputElevation,
                                shape = RoundedCornerShape(14.dp),
                                clip = false,
                                ambientColor = if (isInputFocused) OrangeAccent.copy(alpha = 0.25f) else PaperShadowTint.copy(alpha = 0.16f),
                                spotColor = if (isInputFocused) OrangeAccent.copy(alpha = 0.22f) else PaperShadowTint.copy(alpha = 0.12f)
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .background(SurfaceWhite, RoundedCornerShape(14.dp))
                            .border(
                                width = borderWidth,
                                color = borderColor,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.weight(1f)) {
                            if (state.userInput.isEmpty()) {
                                Text(
                                    text = problem.inputHint,
                                    style = TabularMonoStyle,
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
                                style = TabularMonoStyle,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = SlateMuted
                            )
                        }
                    }
                }
            )

            // Mechanical Tactile Symbol Keycaps
            if (state.progress.showQuickKeypad) {
                Spacer(modifier = Modifier.height(8.dp))
                val keypadScrollState = rememberScrollState()
                val symbols = if (problem.category == ProblemCategory.GEOMETRIC_ALGEBRA) {
                    listOf("e12", "j", "+", "-", ".", "/", "√(", "⌫", "CLR")
                } else {
                    listOf("j", "∠", "+", "-", ".", "/", "√(", "⌫", "CLR")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalEdgeFade(keypadScrollState)
                        .horizontalScroll(keypadScrollState),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    symbols.forEach { sym ->
                        val isAction = sym == "CLR" || sym == "⌫"
                        val isMathAccent = sym == "j" || sym == "∠" || sym == "e12"
                        val keyShape = RoundedCornerShape(10.dp)
                        val rimColor = if (isMathAccent) CopperKeycapRim else KeycapRim
                        Box(
                            modifier = Modifier
                                .height(36.dp)
                                .widthIn(min = 42.dp)
                                .shadow(
                                    elevation = 1.dp,
                                    shape = keyShape,
                                    clip = false,
                                    ambientColor = PaperShadowTint.copy(alpha = 0.14f),
                                    spotColor = PaperShadowTint.copy(alpha = 0.10f)
                                )
                                .clip(keyShape)
                                .background(
                                    when {
                                        isMathAccent -> OrangeSoftBg
                                        isAction -> CodeSurface
                                        else -> SurfaceWhite
                                    }
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isMathAccent) CopperBorderSoft else SubtleBorder,
                                    shape = keyShape
                                )
                                .drawBehind {
                                    val rimH = 2.dp.toPx()
                                    drawRect(
                                        color = rimColor,
                                        topLeft = Offset(0f, size.height - rimH),
                                        size = androidx.compose.ui.geometry.Size(size.width, rimH)
                                    )
                                }
                                .tactileClickable(pressedScale = 0.92f) { onAppendSymbol(sym) }
                                .padding(horizontal = 11.dp)
                                .testTag("symbol_key_$sym"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = sym,
                                style = TabularMonoStyle,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = if (isMathAccent) OrangeAccent else InkBlack
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Action with Spring Press Physics
            val submitInteraction = remember { MutableInteractionSource() }
            Button(
                onClick = {
                    haptic.tickLight()
                    keyboardController?.hide()
                    onSubmit()
                },
                enabled = !isVerified,
                interactionSource = submitInteraction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .springPressScale(submitInteraction, pressedScale = if (isVerified) 1f else 0.98f)
                    .testTag("submit_solution_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = InkBlack,
                    contentColor = SurfaceWhite,
                    disabledContainerColor = InkBlack.copy(alpha = 0.08f),
                    disabledContentColor = SlateMuted
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
                enter = fadeIn(tween(200)) + expandVertically(spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)),
                exit = fadeOut(tween(150)) + shrinkVertically()
            ) {
                state.evaluation?.let { eval ->
                    Column {
                        Spacer(modifier = Modifier.height(14.dp))
                        EvaluationFeedbackCard(
                            evaluation = eval,
                            expectedAnswer = problem.expectedDisplay(),
                            workedSteps = problem.workedSteps,
                            showSteps = state.showSolutionSteps,
                            onToggleSteps = onToggleSteps,
                            onNextProblem = onNextProblem,
                            onPracticeVariation = onPracticeVariation,
                            isProcedural = problem.isProcedural,
                            isCuratedVariation = isCuratedVariation
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
