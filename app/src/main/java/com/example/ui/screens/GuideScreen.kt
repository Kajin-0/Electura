package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProblemBank
import com.example.domain.AnswerEvaluator
import com.example.model.ComplexNum
import com.example.ui.components.LivePhasorMiniPlot
import com.example.ui.components.horizontalEdgeFade
import com.example.ui.components.paperCardSurface
import com.example.ui.components.recessedPaperWell
import com.example.ui.components.tactileClickable
import com.example.ui.theme.CodeSurface
import com.example.ui.theme.CopperBorderSoft
import com.example.ui.theme.HairlineBorder
import com.example.ui.theme.InkBlack
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeSoftBg
import com.example.ui.theme.PlaceholderSlate
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TabularMonoStyle

@Composable
fun GuideScreen(
    onScrolledChange: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var converterInput by remember { mutableStateOf("6 + j8") }
    val parsedComplex = remember(converterInput) { AnswerEvaluator.parseComplex(converterInput) }
    val presets = remember { listOf("6 + j8", "10∠-30°", "5e^(j90°)", "-4 + j3") }
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val isScrolled by remember {
        derivedStateOf { scrollState.value > 8 }
    }
    LaunchedEffect(isScrolled) {
        onScrolledChange(isScrolled)
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
            Text(
                text = "Field Manual",
                style = MaterialTheme.typography.displayLarge,
                color = InkBlack
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Paper-matched Phasor Converter with Recessed Input & One-Tap Presets
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .paperCardSurface(
                        shape = RoundedCornerShape(20.dp),
                        backgroundColor = SurfaceWhite,
                        borderColor = SubtleBorder,
                        borderWidth = 1.dp,
                        elevation = 2.dp
                    )
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE PHASOR CONVERTER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OrangeAccent,
                        letterSpacing = 0.9.sp
                    )
                    Text(
                        text = "RECT ↔ POLAR",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 9.5.sp,
                        color = SlateMuted
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                BasicTextField(
                    value = converterInput,
                    onValueChange = { converterInput = it },
                    textStyle = TabularMonoStyle.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = InkBlack
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    cursorBrush = SolidColor(OrangeAccent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("phasor_converter_input"),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .recessedPaperWell(
                                    shape = RoundedCornerShape(12.dp),
                                    backgroundColor = CodeSurface,
                                    borderColor = HairlineBorder
                                )
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (converterInput.isEmpty()) {
                                Text(
                                    text = "6 + j8, 10∠53.13°, or 10e^(j90°)",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    fontSize = 12.5.sp,
                                    color = PlaceholderSlate,
                                    fontStyle = FontStyle.Italic
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Quick-tap presets with edge fade
                val presetScrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalEdgeFade(presetScrollState)
                        .horizontalScroll(presetScrollState),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presets.forEach { preset ->
                        val active = converterInput.trim() == preset
                        Box(
                            modifier = Modifier
                                .tactileClickable(pressedScale = 0.94f) {
                                    converterInput = preset
                                }
                                .minimumInteractiveComponentSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (active) OrangeSoftBg else CodeSurface
                                    )
                                    .border(
                                        1.dp,
                                        if (active) CopperBorderSoft else HairlineBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = preset,
                                    style = TabularMonoStyle,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (active) OrangeAccent else SlateDark
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        if (parsedComplex != null) {
                            Text(
                                text = "RECTANGULAR",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SlateMuted
                            )
                            Text(
                                text = parsedComplex.formatRect(),
                                style = TabularMonoStyle,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = InkBlack
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "POLAR",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SlateMuted
                            )
                            Text(
                                text = parsedComplex.formatPolar(),
                                style = TabularMonoStyle,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = OrangeAccent
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "|Z| = ${ComplexNum.formatClean(parsedComplex.magnitude)}  ·  θ = ${ComplexNum.formatClean(parsedComplex.angleDeg)}°",
                                style = TabularMonoStyle,
                                fontSize = 11.sp,
                                color = SlateMuted
                            )
                        } else {
                            Text(
                                text = "Enter A + jB, R∠θ, or R·e^(jθ)",
                                fontSize = 12.sp,
                                color = SlateMuted,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    LivePhasorMiniPlot(
                        complex = parsedComplex,
                        modifier = Modifier.size(86.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Layered Paper Formula Reference Cards
            ProblemBank.fieldGuideEntries.forEach { entry ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .paperCardSurface(
                            shape = RoundedCornerShape(18.dp),
                            backgroundColor = SurfaceWhite,
                            borderColor = SubtleBorder,
                            borderWidth = 1.dp,
                            elevation = 2.dp
                        )
                        .padding(18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(12.dp)
                                .background(OrangeAccent, RoundedCornerShape(50))
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = entry.tag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OrangeAccent,
                            letterSpacing = 0.8.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = entry.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = InkBlack
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .recessedPaperWell(
                                shape = RoundedCornerShape(10.dp),
                                backgroundColor = CodeSurface,
                                borderColor = HairlineBorder
                            )
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = entry.formula,
                            style = TabularMonoStyle,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = InkBlack
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = entry.explanation,
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
