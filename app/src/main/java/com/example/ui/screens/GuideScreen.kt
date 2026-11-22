package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProblemBank
import com.example.domain.AnswerEvaluator
import com.example.model.ComplexNum
import com.example.ui.theme.CodeSurface
import com.example.ui.theme.InkBlack
import com.example.ui.theme.InkCharcoal
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeBright
import com.example.ui.theme.PlaceholderSlate
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.SurfaceWhite

@Composable
fun GuideScreen(modifier: Modifier = Modifier) {
    var converterInput by remember { mutableStateOf("6 + j8") }
    val parsedComplex = remember(converterInput) { AnswerEvaluator.parseComplex(converterInput) }

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
                text = "REFERENCE",
                style = MaterialTheme.typography.labelSmall,
                color = OrangeAccent
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Field Manual",
                style = MaterialTheme.typography.displayLarge,
                color = InkBlack
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Refined Graphite Phasor Converter
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(InkCharcoal, RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = "LIVE PHASOR CONVERTER",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OrangeBright,
                    letterSpacing = 0.9.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                BasicTextField(
                    value = converterInput,
                    onValueChange = { converterInput = it },
                    textStyle = TextStyle(
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = InkBlack
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(OrangeAccent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("phasor_converter_input"),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .background(SurfaceWhite, RoundedCornerShape(12.dp))
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

                Spacer(modifier = Modifier.height(14.dp))

                if (parsedComplex != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "RECTANGULAR",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PlaceholderSlate
                            )
                            Text(
                                text = parsedComplex.formatRect(),
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = SurfaceWhite
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "POLAR",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PlaceholderSlate
                            )
                            Text(
                                text = parsedComplex.formatPolar(),
                                fontFamily = JetBrainsMonoFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = OrangeBright
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "|Z| = ${ComplexNum.formatClean(parsedComplex.magnitude)}  ·  θ = ${ComplexNum.formatClean(parsedComplex.angleDeg)}°",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontSize = 11.sp,
                        color = PlaceholderSlate
                    )
                } else {
                    Text(
                        text = "Enter A + jB, R∠θ, or R·e^(jθ)",
                        fontSize = 12.sp,
                        color = PlaceholderSlate,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Clean Formula Reference Cards
            ProblemBank.fieldGuideEntries.forEach { entry ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .background(SurfaceWhite, RoundedCornerShape(18.dp))
                        .border(1.dp, SubtleBorder, RoundedCornerShape(18.dp))
                        .padding(18.dp)
                ) {
                    Text(
                        text = entry.tag,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OrangeAccent,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = entry.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = InkBlack
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CodeSurface, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = entry.formula,
                            fontFamily = JetBrainsMonoFontFamily,
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
