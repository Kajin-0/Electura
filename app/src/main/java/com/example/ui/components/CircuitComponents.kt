package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.domain.SchematicAnnotationResolver
import com.example.domain.SchematicAnnotations
import com.example.model.CircuitProblem
import com.example.model.CircuitTopology
import com.example.model.ComplexNum
import com.example.ui.theme.CopperBorderSoft
import com.example.ui.theme.DraftingGridLine
import com.example.ui.theme.HairlineBorder
import com.example.ui.theme.InkBlack
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeBright
import com.example.ui.theme.OrangeSoftBg
import com.example.ui.theme.PlaceholderSlate
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TabularMonoStyle
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircuitTag(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = OrangeSoftBg,
    textColor: Color = OrangeAccent,
    borderColor: Color? = null
) {
    val shape = RoundedCornerShape(6.dp)
    Box(
        modifier = modifier
            .background(backgroundColor, shape)
            .then(if (borderColor != null) Modifier.border(1.dp, borderColor, shape) else Modifier)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            fontSize = 10.sp
        )
    }
}

@Composable
fun CircuitDiagramCanvas(
    problem: CircuitProblem,
    modifier: Modifier = Modifier,
    onExpandClick: (() -> Unit)? = null
) {
    val annotations = remember(problem) { SchematicAnnotationResolver.resolve(problem) }
    val trayShape = RoundedCornerShape(14.dp)
    val expandCd = stringResource(R.string.cd_expand_schematic)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(126.dp)
            .recessedPaperWell(
                shape = trayShape,
                backgroundColor = SurfaceElevated,
                borderColor = HairlineBorder
            )
            .then(
                if (onExpandClick != null) {
                    Modifier
                        .minimumInteractiveComponentSize()
                        .tactileClickable(
                            pressedScale = 0.985f,
                            role = Role.Button,
                            onClick = onExpandClick
                        )
                        .semantics { contentDescription = expandCd }
                        .testTag("circuit_diagram_card")
                } else {
                    Modifier
                }
            )
    ) {
        CircuitSchematicCanvas(
            problem = problem,
            annotations = annotations,
            isExpanded = false,
            modifier = Modifier.fillMaxSize()
        )

        if (onExpandClick != null) {
            Icon(
                imageVector = Icons.Filled.ZoomOutMap,
                contentDescription = null,
                tint = SlateMuted.copy(alpha = 0.72f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(14.dp)
            )
        }
    }
}

@Composable
fun CircuitSchematicCanvas(
    problem: CircuitProblem,
    annotations: SchematicAnnotations,
    isExpanded: Boolean,
    modifier: Modifier = Modifier
) {
    val topology = problem.topology
    val targetPhaseDeg = problem.expectedComplex?.angleDeg?.toFloat() ?: 45f
    val animatedPhaseDeg by animateFloatAsState(
        targetValue = targetPhaseDeg,
        animationSpec = spring(dampingRatio = 0.78f, stiffness = Spring.StiffnessLow),
        label = "geometricAngle"
    )
    val textMeasurer = rememberTextMeasurer()

    val infiniteTransition = rememberInfiniteTransition(label = "schematicMotion")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "acWavePhase"
    )

    Canvas(modifier = modifier) {
        val gridStep = if (isExpanded) 18.dp.toPx() else 12.dp.toPx()
        drawDraftingGrid(stepPx = gridStep, gridColor = DraftingGridLine.copy(alpha = 0.65f))

        val strokeW = if (isExpanded) 2.1.dp.toPx() else 1.55.dp.toPx()
        val accentW = if (isExpanded) 2.5.dp.toPx() else 1.9.dp.toPx()
        val stroke = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        val accentStroke = Stroke(width = accentW, cap = StrokeCap.Round, join = StrokeJoin.Round)
        val labelFontSize = if (isExpanded) 11.sp else 8.8.sp

        val w = size.width
        val h = size.height
        val topY = h * 0.42f
        val botY = h * 0.78f
        val leftX = w * 0.14f
        val rightX = w * 0.86f
        val midY = (topY + botY) / 2f
        val loopH = botY - topY
        val capHalfGap = if (isExpanded) 6.dp.toPx() else 4.5.dp.toPx()
        val rAmp = (loopH * 0.14f).coerceIn(5.dp.toPx(), 12.dp.toPx())
        val lArc = (loopH * 0.17f).coerceIn(6.dp.toPx(), 14.dp.toPx())
        val cHalf = (loopH * 0.24f).coerceIn(8.5.dp.toPx(), 19.dp.toPx())
        val pillHalfH = if (isExpanded) 9.5.dp.toPx() else 7.5.dp.toPx()
        val topClearance = if (isExpanded) 7.dp.toPx() else 4.dp.toPx()
        val botClearance = if (isExpanded) 8.dp.toPx() else 5.5.dp.toPx()
        val topElemLabelY = topY - cHalf - pillHalfH - topClearance
        val topWireLabelY = topY - pillHalfH - (if (isExpanded) 8.dp.toPx() else 5.dp.toPx())
        val underTopWireLabelY = topY + pillHalfH + (if (isExpanded) 8.dp.toPx() else 5.dp.toPx())
        val botLabelY = botY + pillHalfH + botClearance
        val sourceRadius = loopH * 0.28f

        // Frequency / reference corner badge (top-left, safely above topY)
        annotations.frequencyTag?.let { freqText ->
            drawAnnotationPill(
                textMeasurer = textMeasurer,
                label = freqText,
                anchor = Offset(6.dp.toPx(), if (isExpanded) 14.dp.toPx() else 10.dp.toPx()),
                textColor = SlateDark,
                borderColor = HairlineBorder,
                fontSize = if (isExpanded) 10.sp else 8.2.sp,
                anchorMode = PillAnchorMode.START_AT
            )
        }

        when (topology) {
            CircuitTopology.SERIES_RLC -> {
                drawAcSource(Offset(leftX, midY), sourceRadius, wavePhase, stroke, accentStroke)
                drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - sourceRadius), stroke.width)
                drawLine(InkBlack, Offset(leftX, midY + sourceRadius), Offset(leftX, botY), stroke.width)
                drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)
                drawLine(InkBlack, Offset(rightX, topY), Offset(rightX, botY), stroke.width)

                val rStart = w * 0.24f
                val rEnd = w * 0.40f
                val lStart = w * 0.46f
                val lEnd = w * 0.64f
                val cPos = w * 0.75f

                drawLine(InkBlack, Offset(leftX, topY), Offset(rStart, topY), stroke.width)
                drawResistor(rStart, rEnd, topY, rAmp, stroke)
                drawLine(InkBlack, Offset(rEnd, topY), Offset(lStart, topY), stroke.width)
                drawInductor(lStart, lEnd, topY, lArc, accentStroke)
                drawLine(InkBlack, Offset(lEnd, topY), Offset(cPos - capHalfGap, topY), stroke.width)
                drawCapacitor(cPos, topY, cHalf, accentStroke, capHalfGap)
                drawLine(InkBlack, Offset(cPos + capHalfGap, topY), Offset(rightX, topY), stroke.width)

                drawNodeDot(Offset(leftX, topY))
                drawNodeDot(Offset(rightX, topY))
                drawNodeDot(Offset(leftX, botY))
                drawNodeDot(Offset(rightX, botY))

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(leftX + 8.dp.toPx(), botLabelY), SlateDark, HairlineBorder, labelFontSize)
                }
                annotations.primaryR?.let {
                    drawAnnotationPill(textMeasurer, it, Offset((rStart + rEnd) / 2f, topElemLabelY), InkBlack, HairlineBorder, labelFontSize)
                }
                annotations.inductor?.let {
                    drawAnnotationPill(textMeasurer, it, Offset((lStart + lEnd) / 2f, underTopWireLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
                annotations.capacitor?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(cPos, topElemLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.SERIES_RL -> {
                drawAcSource(Offset(leftX, midY), sourceRadius, wavePhase, stroke, accentStroke)
                drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - sourceRadius), stroke.width)
                drawLine(InkBlack, Offset(leftX, midY + sourceRadius), Offset(leftX, botY), stroke.width)
                drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)
                drawLine(InkBlack, Offset(rightX, topY), Offset(rightX, botY), stroke.width)

                val rStart = w * 0.28f
                val rEnd = w * 0.48f
                val lStart = w * 0.56f
                val lEnd = w * 0.76f
                drawLine(InkBlack, Offset(leftX, topY), Offset(rStart, topY), stroke.width)
                drawResistor(rStart, rEnd, topY, rAmp, stroke)
                drawLine(InkBlack, Offset(rEnd, topY), Offset(lStart, topY), stroke.width)
                drawInductor(lStart, lEnd, topY, lArc, accentStroke)
                drawLine(InkBlack, Offset(lEnd, topY), Offset(rightX, topY), stroke.width)

                drawNodeDot(Offset(leftX, topY))
                drawNodeDot(Offset(rightX, topY))
                drawNodeDot(Offset(leftX, botY))
                drawNodeDot(Offset(rightX, botY))

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(leftX + 8.dp.toPx(), botLabelY), SlateDark, HairlineBorder, labelFontSize)
                }
                annotations.primaryR?.let {
                    drawAnnotationPill(textMeasurer, it, Offset((rStart + rEnd) / 2f, topElemLabelY), InkBlack, HairlineBorder, labelFontSize)
                }
                annotations.inductor?.let {
                    drawAnnotationPill(textMeasurer, it, Offset((lStart + lEnd) / 2f, topElemLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.SERIES_RC -> {
                drawAcSource(Offset(leftX, midY), sourceRadius, wavePhase, stroke, accentStroke)
                drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - sourceRadius), stroke.width)
                drawLine(InkBlack, Offset(leftX, midY + sourceRadius), Offset(leftX, botY), stroke.width)
                drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)
                drawLine(InkBlack, Offset(rightX, topY), Offset(rightX, botY), stroke.width)

                val rStart = w * 0.28f
                val rEnd = w * 0.50f
                val cPos = w * 0.68f
                drawLine(InkBlack, Offset(leftX, topY), Offset(rStart, topY), stroke.width)
                drawResistor(rStart, rEnd, topY, rAmp, stroke)
                drawLine(InkBlack, Offset(rEnd, topY), Offset(cPos - capHalfGap, topY), stroke.width)
                drawCapacitor(cPos, topY, cHalf, accentStroke, capHalfGap)
                drawLine(InkBlack, Offset(cPos + capHalfGap, topY), Offset(rightX, topY), stroke.width)

                drawNodeDot(Offset(leftX, topY))
                drawNodeDot(Offset(rightX, topY))
                drawNodeDot(Offset(leftX, botY))
                drawNodeDot(Offset(rightX, botY))

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(leftX + 8.dp.toPx(), botLabelY), SlateDark, HairlineBorder, labelFontSize)
                }
                annotations.primaryR?.let {
                    drawAnnotationPill(textMeasurer, it, Offset((rStart + rEnd) / 2f, topElemLabelY), InkBlack, HairlineBorder, labelFontSize)
                }
                annotations.capacitor?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(cPos, topElemLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.PARALLEL_RC -> {
                drawAcSource(Offset(leftX, midY), sourceRadius, wavePhase, stroke, accentStroke)
                drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - sourceRadius), stroke.width)
                drawLine(InkBlack, Offset(leftX, midY + sourceRadius), Offset(leftX, botY), stroke.width)
                drawLine(InkBlack, Offset(leftX, topY), Offset(rightX, topY), stroke.width)
                drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)

                val branch1X = w * 0.50f
                val rHalf = loopH * 0.27f
                drawLine(InkBlack, Offset(branch1X, topY), Offset(branch1X, midY - rHalf), stroke.width)
                drawResistorVertical(branch1X, midY - rHalf, midY + rHalf, 4.5.dp.toPx(), stroke)
                drawLine(InkBlack, Offset(branch1X, midY + rHalf), Offset(branch1X, botY), stroke.width)
                drawNodeDot(Offset(branch1X, topY))
                drawNodeDot(Offset(branch1X, botY))

                val plateHalfW = if (isExpanded) 15.dp.toPx() else 11.dp.toPx()
                drawLine(InkBlack, Offset(rightX, topY), Offset(rightX, midY - capHalfGap), stroke.width)
                drawLine(OrangeAccent, Offset(rightX - plateHalfW, midY - capHalfGap), Offset(rightX + plateHalfW, midY - capHalfGap), accentStroke.width)
                drawLine(OrangeAccent, Offset(rightX - plateHalfW, midY + capHalfGap), Offset(rightX + plateHalfW, midY + capHalfGap), accentStroke.width)
                drawLine(InkBlack, Offset(rightX, midY + capHalfGap), Offset(rightX, botY), stroke.width)

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(leftX + 8.dp.toPx(), botLabelY), SlateDark, HairlineBorder, labelFontSize)
                }
                annotations.primaryR?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(branch1X, topWireLabelY), InkBlack, HairlineBorder, labelFontSize)
                }
                annotations.capacitor?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(rightX - 10.dp.toPx(), botLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.PARALLEL_RL -> {
                val rlRightX = w * 0.80f
                drawAcSource(Offset(leftX, midY), sourceRadius, wavePhase, stroke, accentStroke)
                drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - sourceRadius), stroke.width)
                drawLine(InkBlack, Offset(leftX, midY + sourceRadius), Offset(leftX, botY), stroke.width)
                drawLine(InkBlack, Offset(leftX, topY), Offset(rlRightX, topY), stroke.width)
                drawLine(InkBlack, Offset(leftX, botY), Offset(rlRightX, botY), stroke.width)

                val branch1X = w * 0.48f
                val elemHalf = (botY - topY) * 0.27f
                drawLine(InkBlack, Offset(branch1X, topY), Offset(branch1X, midY - elemHalf), stroke.width)
                drawResistorVertical(branch1X, midY - elemHalf, midY + elemHalf, 4.5.dp.toPx(), stroke)
                drawLine(InkBlack, Offset(branch1X, midY + elemHalf), Offset(branch1X, botY), stroke.width)
                drawNodeDot(Offset(branch1X, topY))
                drawNodeDot(Offset(branch1X, botY))

                drawLine(InkBlack, Offset(rlRightX, topY), Offset(rlRightX, midY - elemHalf), stroke.width)
                drawInductorVertical(rlRightX, midY - elemHalf, midY + elemHalf, 5.5.dp.toPx(), accentStroke, bulgeRight = true)
                drawLine(InkBlack, Offset(rlRightX, midY + elemHalf), Offset(rlRightX, botY), stroke.width)

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(leftX + 8.dp.toPx(), botLabelY), SlateDark, HairlineBorder, labelFontSize)
                }
                annotations.primaryR?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(branch1X, topWireLabelY), InkBlack, HairlineBorder, labelFontSize)
                }
                annotations.inductor?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(rlRightX, botLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.PARALLEL_RLC -> {
                drawAcSource(Offset(leftX, midY), sourceRadius, wavePhase, stroke, accentStroke)
                drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - sourceRadius), stroke.width)
                drawLine(InkBlack, Offset(leftX, midY + sourceRadius), Offset(leftX, botY), stroke.width)
                drawLine(InkBlack, Offset(leftX, topY), Offset(rightX, topY), stroke.width)
                drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)

                val branch1X = w * 0.41f
                val branch2X = w * 0.63f
                val elemHalf = loopH * 0.27f

                drawLine(InkBlack, Offset(branch1X, topY), Offset(branch1X, midY - elemHalf), stroke.width)
                drawResistorVertical(branch1X, midY - elemHalf, midY + elemHalf, 4.2.dp.toPx(), stroke)
                drawLine(InkBlack, Offset(branch1X, midY + elemHalf), Offset(branch1X, botY), stroke.width)
                drawNodeDot(Offset(branch1X, topY))
                drawNodeDot(Offset(branch1X, botY))

                drawLine(InkBlack, Offset(branch2X, topY), Offset(branch2X, midY - elemHalf), stroke.width)
                drawInductorVertical(branch2X, midY - elemHalf, midY + elemHalf, 5.5.dp.toPx(), accentStroke, bulgeRight = true)
                drawLine(InkBlack, Offset(branch2X, midY + elemHalf), Offset(branch2X, botY), stroke.width)
                drawNodeDot(Offset(branch2X, topY))
                drawNodeDot(Offset(branch2X, botY))

                val plateHalfW = if (isExpanded) 13.dp.toPx() else 9.5.dp.toPx()
                drawLine(InkBlack, Offset(rightX, topY), Offset(rightX, midY - capHalfGap), stroke.width)
                drawLine(OrangeAccent, Offset(rightX - plateHalfW, midY - capHalfGap), Offset(rightX + plateHalfW, midY - capHalfGap), accentStroke.width)
                drawLine(OrangeAccent, Offset(rightX - plateHalfW, midY + capHalfGap), Offset(rightX + plateHalfW, midY + capHalfGap), accentStroke.width)
                drawLine(InkBlack, Offset(rightX, midY + capHalfGap), Offset(rightX, botY), stroke.width)

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(leftX + 8.dp.toPx(), botLabelY), SlateDark, HairlineBorder, labelFontSize)
                }
                annotations.primaryR?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(branch1X, topWireLabelY), InkBlack, HairlineBorder, labelFontSize)
                }
                annotations.inductor?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(branch2X, botLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
                annotations.capacitor?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(rightX - 12.dp.toPx(), topWireLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.COMPLEX_LOAD -> {
                val sx = leftX + 10.dp.toPx()
                val boxLeft = w * 0.60f
                val boxWidth = w * 0.21f
                val boxCenterX = boxLeft + boxWidth / 2f
                val boxHalfH = loopH * 0.30f

                drawAcSource(Offset(sx, midY), sourceRadius, wavePhase, stroke, accentStroke)
                drawLine(InkBlack, Offset(sx, topY), Offset(sx, midY - sourceRadius), stroke.width)
                drawLine(InkBlack, Offset(sx, midY + sourceRadius), Offset(sx, botY), stroke.width)
                drawLine(InkBlack, Offset(sx, topY), Offset(boxCenterX, topY), stroke.width)
                drawLine(InkBlack, Offset(sx, botY), Offset(boxCenterX, botY), stroke.width)

                val ax = w * 0.41f
                drawArrowHead(Offset(ax + 8.dp.toPx(), topY), 0f, 5.dp.toPx(), OrangeAccent, accentStroke.width)

                val pulseFrac = wavePhase / (2f * PI.toFloat())
                val pulseX = sx + (boxCenterX - sx) * pulseFrac
                drawCircle(color = OrangeAccent.copy(alpha = 0.85f), radius = 2.6.dp.toPx(), center = Offset(pulseX, topY))

                drawLine(InkBlack, Offset(boxCenterX, topY), Offset(boxCenterX, midY - boxHalfH), stroke.width)
                drawLine(InkBlack, Offset(boxCenterX, midY + boxHalfH), Offset(boxCenterX, botY), stroke.width)
                drawRoundRect(
                    color = OrangeSoftBg,
                    topLeft = Offset(boxLeft, midY - boxHalfH),
                    size = Size(boxWidth, boxHalfH * 2f),
                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                )
                drawRoundRect(
                    color = OrangeAccent,
                    topLeft = Offset(boxLeft, midY - boxHalfH),
                    size = Size(boxWidth, boxHalfH * 2f),
                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
                    style = accentStroke
                )
                drawCenterLabel(textMeasurer, "Z", Offset(boxCenterX, midY), OrangeAccent, fontSize = if (isExpanded) 13.sp else 9.5.sp)

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(sx + 8.dp.toPx(), botLabelY), SlateDark, HairlineBorder, labelFontSize)
                }
                annotations.loadZ?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(boxCenterX, botLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.LOW_PASS_RC -> {
                val rStart = w * 0.25f
                val rEnd = w * 0.48f
                val tapX = w * 0.62f
                drawLine(InkBlack, Offset(leftX + 4.dp.toPx(), topY), Offset(rStart, topY), stroke.width)
                drawResistor(rStart, rEnd, topY, rAmp, stroke)
                drawLine(InkBlack, Offset(rEnd, topY), Offset(rightX - 4.dp.toPx(), topY), stroke.width)
                drawLine(InkBlack, Offset(leftX + 4.dp.toPx(), botY), Offset(rightX - 4.dp.toPx(), botY), stroke.width)

                val plateHalfW = if (isExpanded) 14.dp.toPx() else 10.5.dp.toPx()
                drawLine(InkBlack, Offset(tapX, topY), Offset(tapX, midY - capHalfGap), stroke.width)
                drawLine(OrangeAccent, Offset(tapX - plateHalfW, midY - capHalfGap), Offset(tapX + plateHalfW, midY - capHalfGap), accentStroke.width)
                drawLine(OrangeAccent, Offset(tapX - plateHalfW, midY + capHalfGap), Offset(tapX + plateHalfW, midY + capHalfGap), accentStroke.width)
                drawLine(InkBlack, Offset(tapX, midY + capHalfGap), Offset(tapX, botY), stroke.width)

                drawNodeDot(Offset(tapX, topY))
                drawNodeDot(Offset(tapX, botY))
                drawNodeDot(Offset(leftX, topY), radius = 3.8.dp.toPx())
                drawNodeDot(Offset(leftX, botY), radius = 3.8.dp.toPx())
                drawNodeDot(Offset(rightX, topY), radius = 3.8.dp.toPx(), ringColor = OrangeAccent)
                drawNodeDot(Offset(rightX, botY), radius = 3.8.dp.toPx(), ringColor = OrangeAccent)

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(leftX, midY), SlateDark, HairlineBorder, labelFontSize)
                }
                annotations.primaryR?.let {
                    drawAnnotationPill(textMeasurer, it, Offset((rStart + rEnd) / 2f, topElemLabelY), InkBlack, HairlineBorder, labelFontSize)
                }
                annotations.capacitor?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(tapX, botLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
                annotations.loadZ?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(rightX, midY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.HIGH_PASS_RC -> {
                val cX = w * 0.36f
                val tapX = w * 0.63f
                val rHalf = loopH * 0.27f

                drawLine(InkBlack, Offset(leftX + 4.dp.toPx(), topY), Offset(cX - capHalfGap, topY), stroke.width)
                drawCapacitor(cX, topY, cHalf, accentStroke, capHalfGap)
                drawLine(InkBlack, Offset(cX + capHalfGap, topY), Offset(rightX - 4.dp.toPx(), topY), stroke.width)
                drawLine(InkBlack, Offset(leftX + 4.dp.toPx(), botY), Offset(rightX - 4.dp.toPx(), botY), stroke.width)

                drawLine(InkBlack, Offset(tapX, topY), Offset(tapX, midY - rHalf), stroke.width)
                drawResistorVertical(tapX, midY - rHalf, midY + rHalf, 4.5.dp.toPx(), stroke)
                drawLine(InkBlack, Offset(tapX, midY + rHalf), Offset(tapX, botY), stroke.width)

                drawNodeDot(Offset(tapX, topY))
                drawNodeDot(Offset(tapX, botY))
                drawNodeDot(Offset(leftX, topY), radius = 3.8.dp.toPx())
                drawNodeDot(Offset(leftX, botY), radius = 3.8.dp.toPx())
                drawNodeDot(Offset(rightX, topY), radius = 3.8.dp.toPx(), ringColor = OrangeAccent)
                drawNodeDot(Offset(rightX, botY), radius = 3.8.dp.toPx(), ringColor = OrangeAccent)

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(leftX, midY), SlateDark, HairlineBorder, labelFontSize)
                }
                annotations.capacitor?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(cX, topElemLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
                annotations.primaryR?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(tapX, botLabelY), InkBlack, HairlineBorder, labelFontSize)
                }
                annotations.loadZ?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(rightX, midY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.FEEDBACK_LOOP -> {
                val sumCenter = Offset(w * 0.24f, h * 0.40f)
                val sy = sumCenter.y
                val sumR = if (isExpanded) 13.dp.toPx() else 9.5.dp.toPx()
                drawLine(InkBlack, Offset(w * 0.10f, sy), Offset(sumCenter.x - sumR, sy), stroke.width)
                drawArrowHead(Offset(sumCenter.x - sumR, sy), 0f, 4.dp.toPx(), InkBlack, stroke.width)
                drawCircle(color = SurfaceWhite, radius = sumR, center = sumCenter)
                drawCircle(InkBlack, radius = sumR, center = sumCenter, style = stroke)
                drawLine(InkBlack, Offset(sumCenter.x - 4.dp.toPx(), sy), Offset(sumCenter.x + 4.dp.toPx(), sy), stroke.width)
                drawLine(InkBlack, Offset(sumCenter.x, sy - 4.dp.toPx()), Offset(sumCenter.x, sy + 4.dp.toPx()), stroke.width)

                val gLeft = w * 0.40f
                val gWidth = w * 0.26f
                val gHalfH = if (isExpanded) 16.dp.toPx() else 12.dp.toPx()
                drawLine(InkBlack, Offset(sumCenter.x + sumR, sy), Offset(gLeft, sy), stroke.width)
                drawArrowHead(Offset(gLeft, sy), 0f, 4.dp.toPx(), InkBlack, stroke.width)
                drawRoundRect(
                    color = OrangeSoftBg,
                    topLeft = Offset(gLeft, sy - gHalfH),
                    size = Size(gWidth, gHalfH * 2f),
                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                )
                drawRoundRect(
                    color = OrangeAccent,
                    topLeft = Offset(gLeft, sy - gHalfH),
                    size = Size(gWidth, gHalfH * 2f),
                    cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
                    style = accentStroke
                )
                drawCenterLabel(
                    textMeasurer,
                    annotations.loadZ ?: "G(s)",
                    Offset(gLeft + gWidth / 2f, sy),
                    OrangeAccent,
                    fontSize = if (isExpanded) 11.5.sp else 8.8.sp
                )

                val tapX = w * 0.78f
                val hLeft = w * 0.42f
                val hWidth = w * 0.22f
                val hHalfH = if (isExpanded) 13.dp.toPx() else 9.5.dp.toPx()
                drawLine(InkBlack, Offset(gLeft + gWidth, sy), Offset(w * 0.90f, sy), stroke.width)
                drawArrowHead(Offset(w * 0.90f, sy), 0f, 4.dp.toPx(), OrangeAccent, stroke.width)
                drawLine(InkBlack, Offset(tapX, sy), Offset(tapX, botY), stroke.width)
                drawLine(InkBlack, Offset(tapX, botY), Offset(hLeft + hWidth, botY), stroke.width)
                drawLine(InkBlack, Offset(hLeft, botY), Offset(sumCenter.x, botY), stroke.width)
                drawLine(InkBlack, Offset(sumCenter.x, botY), Offset(sumCenter.x, sy + sumR), stroke.width)
                drawArrowHead(Offset(sumCenter.x, sy + sumR), 270f, 4.dp.toPx(), OrangeAccent, stroke.width)
                drawCenterLabel(textMeasurer, "−", Offset(sumCenter.x - 10.dp.toPx(), sy + sumR + 6.dp.toPx()), OrangeAccent, fontSize = 10.sp)
                drawNodeDot(Offset(tapX, sy))

                drawRoundRect(
                    color = SurfaceWhite,
                    topLeft = Offset(hLeft, botY - hHalfH),
                    size = Size(hWidth, hHalfH * 2f),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
                drawRoundRect(
                    color = InkBlack,
                    topLeft = Offset(hLeft, botY - hHalfH),
                    size = Size(hWidth, hHalfH * 2f),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                    style = stroke
                )
                drawCenterLabel(
                    textMeasurer,
                    annotations.secondaryBox ?: "H",
                    Offset(hLeft + hWidth / 2f, botY),
                    InkBlack,
                    fontSize = if (isExpanded) 11.sp else 8.8.sp
                )
                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(w * 0.11f, sy + pillHalfH + 6.dp.toPx()), SlateDark, HairlineBorder, labelFontSize)
                }
            }

            CircuitTopology.TRANSFORMER_COUPLED -> {
                val priX = w * 0.37f
                val secX = w * 0.59f
                val coilHalf = loopH * 0.27f

                drawAcSource(Offset(leftX, midY), sourceRadius, wavePhase, stroke, accentStroke)
                drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - sourceRadius), stroke.width)
                drawLine(InkBlack, Offset(leftX, midY + sourceRadius), Offset(leftX, botY), stroke.width)
                drawLine(InkBlack, Offset(leftX, topY), Offset(priX, topY), stroke.width)
                drawLine(InkBlack, Offset(leftX, botY), Offset(priX, botY), stroke.width)

                drawLine(InkBlack, Offset(priX, topY), Offset(priX, midY - coilHalf), stroke.width)
                drawInductorVertical(priX, midY - coilHalf, midY + coilHalf, 5.2.dp.toPx(), accentStroke, bulgeRight = true)
                drawLine(InkBlack, Offset(priX, midY + coilHalf), Offset(priX, botY), stroke.width)

                val coreX1 = w * 0.46f
                val coreX2 = w * 0.50f
                drawLine(InkBlack.copy(alpha = 0.7f), Offset(coreX1, topY + 2.dp.toPx()), Offset(coreX1, botY - 2.dp.toPx()), stroke.width)
                drawLine(InkBlack.copy(alpha = 0.7f), Offset(coreX2, topY + 2.dp.toPx()), Offset(coreX2, botY - 2.dp.toPx()), stroke.width)
                drawCircle(color = OrangeAccent, radius = 2.2.dp.toPx(), center = Offset(priX - 7.dp.toPx(), midY - coilHalf))
                drawCircle(color = OrangeAccent, radius = 2.2.dp.toPx(), center = Offset(secX + 7.dp.toPx(), midY - coilHalf))

                val loadCenterX = w * 0.81f
                val boxW = w * 0.14f
                val boxHalfH = loopH * 0.28f
                drawLine(InkBlack, Offset(secX, topY), Offset(secX, midY - coilHalf), stroke.width)
                drawInductorVertical(secX, midY - coilHalf, midY + coilHalf, 5.2.dp.toPx(), accentStroke, bulgeRight = false)
                drawLine(InkBlack, Offset(secX, midY + coilHalf), Offset(secX, botY), stroke.width)
                drawLine(InkBlack, Offset(secX, topY), Offset(loadCenterX, topY), stroke.width)
                drawLine(InkBlack, Offset(secX, botY), Offset(loadCenterX, botY), stroke.width)
                drawLine(InkBlack, Offset(loadCenterX, topY), Offset(loadCenterX, midY - boxHalfH), stroke.width)
                drawLine(InkBlack, Offset(loadCenterX, midY + boxHalfH), Offset(loadCenterX, botY), stroke.width)
                drawRoundRect(
                    color = OrangeSoftBg,
                    topLeft = Offset(loadCenterX - boxW / 2f, midY - boxHalfH),
                    size = Size(boxW, boxHalfH * 2f),
                    cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
                )
                drawRoundRect(
                    color = OrangeAccent,
                    topLeft = Offset(loadCenterX - boxW / 2f, midY - boxHalfH),
                    size = Size(boxW, boxHalfH * 2f),
                    cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx()),
                    style = accentStroke
                )
                drawCenterLabel(textMeasurer, "Z", Offset(loadCenterX, midY), OrangeAccent, fontSize = if (isExpanded) 12.sp else 9.5.sp)

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(leftX + 4.dp.toPx(), botLabelY), SlateDark, HairlineBorder, labelFontSize)
                }
                annotations.inductor?.let {
                    drawAnnotationPill(
                        textMeasurer, it,
                        Offset(coreX1 - 5.dp.toPx(), topWireLabelY),
                        OrangeAccent, CopperBorderSoft, labelFontSize,
                        anchorMode = PillAnchorMode.END_AT
                    )
                }
                annotations.capacitor?.let {
                    drawAnnotationPill(
                        textMeasurer, it,
                        Offset(coreX2 + 5.dp.toPx(), topWireLabelY),
                        OrangeAccent, CopperBorderSoft, labelFontSize,
                        anchorMode = PillAnchorMode.START_AT
                    )
                }
                annotations.secondaryBox?.let {
                    drawAnnotationPill(textMeasurer, it, Offset((coreX1 + coreX2) / 2f, botLabelY), InkBlack, HairlineBorder, labelFontSize)
                }
                annotations.loadZ?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(loadCenterX - 4.dp.toPx(), botLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.OP_AMP_INVERTING -> {
                val ampBaseX = w * 0.45f
                val ampTipX = w * 0.68f
                val ampMidY = h * 0.64f
                val minusY = ampMidY - (if (isExpanded) 12.dp.toPx() else 8.dp.toPx())
                val plusY = ampMidY + (if (isExpanded) 12.dp.toPx() else 8.dp.toPx())
                val fbY = h * 0.34f
                val sumNodeX = w * 0.37f
                val outNodeX = w * 0.74f
                val rInAmp = if (isExpanded) 6.dp.toPx() else 4.dp.toPx()

                drawNodeDot(Offset(leftX, minusY), radius = 3.5.dp.toPx())
                val rInStart = w * 0.19f
                val rInEnd = w * 0.32f
                drawLine(InkBlack, Offset(leftX + 3.5.dp.toPx(), minusY), Offset(rInStart, minusY), stroke.width)
                drawResistor(rInStart, rInEnd, minusY, rInAmp, stroke)
                drawLine(InkBlack, Offset(rInEnd, minusY), Offset(ampBaseX, minusY), stroke.width)
                drawNodeDot(Offset(sumNodeX, minusY))

                val gndX = w * 0.38f
                drawLine(InkBlack, Offset(ampBaseX, plusY), Offset(gndX, plusY), stroke.width)
                drawLine(InkBlack, Offset(gndX, plusY), Offset(gndX, plusY + 8.dp.toPx()), stroke.width)
                drawGroundSymbol(Offset(gndX, plusY + 8.dp.toPx()), stroke.width)

                val triHalfH = if (isExpanded) 23.dp.toPx() else 15.5.dp.toPx()
                val triPath = Path().apply {
                    moveTo(ampBaseX, ampMidY - triHalfH)
                    lineTo(ampTipX, ampMidY)
                    lineTo(ampBaseX, ampMidY + triHalfH)
                    close()
                }
                drawPath(path = triPath, color = SurfaceWhite)
                drawPath(path = triPath, color = InkBlack, style = stroke)
                drawCenterLabel(textMeasurer, "−", Offset(ampBaseX + 7.dp.toPx(), minusY), InkBlack, fontSize = if (isExpanded) 11.sp else 9.sp)
                drawCenterLabel(textMeasurer, "+", Offset(ampBaseX + 7.dp.toPx(), plusY), InkBlack, fontSize = if (isExpanded) 11.sp else 9.sp)

                val fbBoxLeft = w * 0.44f
                val fbBoxW = w * 0.22f
                val fbHalfH = if (isExpanded) 11.dp.toPx() else 8.dp.toPx()
                drawLine(InkBlack, Offset(sumNodeX, minusY), Offset(sumNodeX, fbY), stroke.width)
                drawLine(InkBlack, Offset(sumNodeX, fbY), Offset(fbBoxLeft, fbY), stroke.width)
                drawRoundRect(
                    color = OrangeSoftBg,
                    topLeft = Offset(fbBoxLeft, fbY - fbHalfH),
                    size = Size(fbBoxW, fbHalfH * 2f),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
                drawRoundRect(
                    color = OrangeAccent,
                    topLeft = Offset(fbBoxLeft, fbY - fbHalfH),
                    size = Size(fbBoxW, fbHalfH * 2f),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                    style = accentStroke
                )
                drawCenterLabel(
                    textMeasurer,
                    "Z_f",
                    Offset(fbBoxLeft + fbBoxW / 2f, fbY),
                    OrangeAccent,
                    fontSize = if (isExpanded) 10.sp else 8.2.sp
                )
                drawLine(InkBlack, Offset(fbBoxLeft + fbBoxW, fbY), Offset(outNodeX, fbY), stroke.width)
                drawLine(InkBlack, Offset(outNodeX, fbY), Offset(outNodeX, ampMidY), stroke.width)

                drawLine(InkBlack, Offset(ampTipX, ampMidY), Offset(rightX - 3.5.dp.toPx(), ampMidY), stroke.width)
                drawNodeDot(Offset(outNodeX, ampMidY))
                drawNodeDot(Offset(rightX, ampMidY), radius = 3.5.dp.toPx(), ringColor = OrangeAccent)

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(leftX, minusY + pillHalfH + 5.5.dp.toPx()), SlateDark, HairlineBorder, labelFontSize)
                }
                annotations.primaryR?.let {
                    drawAnnotationPill(textMeasurer, it, Offset((rInStart + rInEnd) / 2f, minusY - rInAmp - pillHalfH - 4.dp.toPx()), InkBlack, HairlineBorder, labelFontSize)
                }
                annotations.loadZ?.takeIf { it != "Zf" }?.let { fbVal ->
                    drawAnnotationPill(
                        textMeasurer, fbVal,
                        Offset(fbBoxLeft + fbBoxW / 2f, fbY - fbHalfH - pillHalfH - 3.5.dp.toPx()),
                        OrangeAccent, CopperBorderSoft, labelFontSize
                    )
                }
                annotations.secondaryBox?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(rightX - 4.dp.toPx(), ampMidY + pillHalfH + 5.5.dp.toPx()), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.AC_BRIDGE -> {
                val srcX = w * 0.13f
                val bx = w * 0.60f
                val bTopY = h * 0.22f
                val bBotY = h * 0.78f
                val bMidY = (bTopY + bBotY) / 2f
                val bLeft = Offset(bx - w * 0.15f, bMidY)
                val bRight = Offset(bx + w * 0.15f, bMidY)
                val bTop = Offset(bx, bTopY)
                val bBot = Offset(bx, bBotY)
                val bridgeSourceR = (bBotY - bTopY) * 0.25f

                drawAcSource(Offset(srcX, bMidY), bridgeSourceR, wavePhase, stroke, accentStroke)
                drawLine(InkBlack, Offset(srcX, bTop.y), Offset(srcX, bMidY - bridgeSourceR), stroke.width)
                drawLine(InkBlack, Offset(srcX, bMidY + bridgeSourceR), Offset(srcX, bBot.y), stroke.width)
                drawLine(InkBlack, Offset(srcX, bTop.y), bTop, stroke.width)
                drawLine(InkBlack, Offset(srcX, bBot.y), bBot, stroke.width)

                drawLine(InkBlack, bTop, bLeft, stroke.width)
                drawLine(OrangeAccent, bTop, bRight, accentStroke.width)
                drawLine(OrangeAccent, bLeft, bBot, accentStroke.width)
                drawLine(InkBlack, bRight, bBot, stroke.width)

                listOf(
                    Offset((bTop.x + bLeft.x) / 2f, (bTop.y + bLeft.y) / 2f) to InkBlack,
                    Offset((bTop.x + bRight.x) / 2f, (bTop.y + bRight.y) / 2f) to OrangeAccent,
                    Offset((bLeft.x + bBot.x) / 2f, (bLeft.y + bBot.y) / 2f) to OrangeAccent,
                    Offset((bRight.x + bBot.x) / 2f, (bRight.y + bBot.y) / 2f) to InkBlack
                ).forEach { (pt, clr) ->
                    drawCircle(color = SurfaceWhite, radius = 3.2.dp.toPx(), center = pt)
                    drawCircle(color = clr, radius = 3.2.dp.toPx(), center = pt, style = stroke)
                }

                val detCenter = Offset(bx, bMidY)
                val detR = if (isExpanded) 11.dp.toPx() else 8.dp.toPx()
                drawLine(InkBlack, bLeft, Offset(detCenter.x - detR, bMidY), stroke.width)
                drawLine(InkBlack, Offset(detCenter.x + detR, bMidY), bRight, stroke.width)
                drawCircle(color = OrangeSoftBg, radius = detR, center = detCenter)
                drawCircle(color = OrangeAccent, radius = detR, center = detCenter, style = accentStroke)
                drawCenterLabel(textMeasurer, "∅", detCenter, OrangeAccent, fontSize = if (isExpanded) 11.5.sp else 9.sp)

                drawNodeDot(bTop)
                drawNodeDot(bBot)
                drawNodeDot(bLeft)
                drawNodeDot(bRight)

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(srcX + 8.dp.toPx(), bBotY + pillHalfH + botClearance), SlateDark, HairlineBorder, labelFontSize)
                }
                if (annotations.bridgeArms.size == 4) {
                    val leftPillX = bLeft.x - (if (isExpanded) 12.dp.toPx() else 8.dp.toPx())
                    val rightPillX = bRight.x + (if (isExpanded) 12.dp.toPx() else 8.dp.toPx())
                    val topArmY = ((bTop.y + bLeft.y) / 2f).coerceAtLeast(bTop.y + pillHalfH + 6.dp.toPx())
                    val botArmY = ((bLeft.y + bBot.y) / 2f).coerceAtMost(bBot.y - pillHalfH - 6.dp.toPx())
                    val armSpecs = listOf(
                        Triple(Offset(leftPillX, topArmY), InkBlack, PillAnchorMode.END_AT),
                        Triple(Offset(rightPillX, topArmY), OrangeAccent, PillAnchorMode.START_AT),
                        Triple(Offset(leftPillX, botArmY), OrangeAccent, PillAnchorMode.END_AT),
                        Triple(Offset(rightPillX, botArmY), InkBlack, PillAnchorMode.START_AT)
                    )
                    armSpecs.forEachIndexed { idx, (pos, clr, mode) ->
                        drawAnnotationPill(
                            textMeasurer = textMeasurer,
                            label = annotations.bridgeArms[idx],
                            anchor = pos,
                            textColor = clr,
                            borderColor = if (clr == OrangeAccent) CopperBorderSoft else HairlineBorder,
                            fontSize = if (isExpanded) 10.sp else 8.sp,
                            anchorMode = mode
                        )
                    }
                }
            }

            CircuitTopology.TWO_MESH -> {
                val midX = w * 0.50f
                val elemHalf = loopH * 0.27f

                drawAcSource(Offset(leftX, midY), sourceRadius, wavePhase, stroke, accentStroke)
                drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - sourceRadius), stroke.width)
                drawLine(InkBlack, Offset(leftX, midY + sourceRadius), Offset(leftX, botY), stroke.width)

                val r1Start = w * 0.24f
                val r1End = w * 0.40f
                drawLine(InkBlack, Offset(leftX, topY), Offset(r1Start, topY), stroke.width)
                drawResistor(r1Start, r1End, topY, rAmp, stroke)
                drawLine(InkBlack, Offset(r1End, topY), Offset(midX, topY), stroke.width)

                val r2Start = w * 0.60f
                val r2End = w * 0.76f
                drawLine(InkBlack, Offset(midX, topY), Offset(r2Start, topY), stroke.width)
                drawResistor(r2Start, r2End, topY, rAmp, stroke)
                drawLine(InkBlack, Offset(r2End, topY), Offset(rightX, topY), stroke.width)

                drawLine(InkBlack, Offset(rightX, topY), Offset(rightX, midY - elemHalf), stroke.width)
                drawResistorVertical(rightX, midY - elemHalf, midY + elemHalf, 4.2.dp.toPx(), stroke)
                drawLine(InkBlack, Offset(rightX, midY + elemHalf), Offset(rightX, botY), stroke.width)

                drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)

                drawLine(InkBlack, Offset(midX, topY), Offset(midX, midY - elemHalf), stroke.width)
                drawInductorVertical(midX, midY - elemHalf, midY + elemHalf, 5.2.dp.toPx(), accentStroke, bulgeRight = true)
                drawLine(InkBlack, Offset(midX, midY + elemHalf), Offset(midX, botY), stroke.width)

                drawNodeDot(Offset(midX, topY))
                drawNodeDot(Offset(midX, botY))
                drawNodeDot(Offset(leftX, topY))
                drawNodeDot(Offset(rightX, topY))

                val loopR = if (isExpanded) 12.dp.toPx() else 8.dp.toPx()
                val m1Center = Offset(w * 0.32f, midY)
                val m2Center = Offset(w * 0.68f, midY)
                drawArc(
                    color = OrangeAccent.copy(alpha = 0.75f),
                    startAngle = 210f,
                    sweepAngle = 250f,
                    useCenter = false,
                    topLeft = Offset(m1Center.x - loopR, m1Center.y - loopR),
                    size = Size(loopR * 2f, loopR * 2f),
                    style = Stroke(width = 1.3.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = OrangeAccent.copy(alpha = 0.75f),
                    startAngle = 210f,
                    sweepAngle = 250f,
                    useCenter = false,
                    topLeft = Offset(m2Center.x - loopR, m2Center.y - loopR),
                    size = Size(loopR * 2f, loopR * 2f),
                    style = Stroke(width = 1.3.dp.toPx(), cap = StrokeCap.Round)
                )
                if (isExpanded) {
                    drawCenterLabel(textMeasurer, "I₁", m1Center, OrangeAccent, fontSize = 10.sp)
                    drawCenterLabel(textMeasurer, "I₂", m2Center, OrangeAccent, fontSize = 10.sp)
                }

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(leftX + 8.dp.toPx(), botLabelY), SlateDark, HairlineBorder, labelFontSize)
                }
                annotations.primaryR?.let {
                    drawAnnotationPill(textMeasurer, it, Offset((r1Start + r1End) / 2f, topElemLabelY), InkBlack, HairlineBorder, labelFontSize)
                }
                annotations.secondaryR?.let {
                    drawAnnotationPill(textMeasurer, it, Offset((r2Start + r2End) / 2f, topElemLabelY), InkBlack, HairlineBorder, labelFontSize)
                }
                annotations.inductor?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(midX, botLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.THREE_PHASE_DELTA_Y -> {
                val dA = Offset(w * 0.21f, h * 0.28f)
                val dB = Offset(w * 0.21f, h * 0.72f)
                val dC = Offset(w * 0.38f, h * 0.50f)
                drawLine(InkBlack, dA, dB, stroke.width)
                drawLine(OrangeAccent, dB, dC, accentStroke.width)
                drawLine(OrangeAccent, dC, dA, accentStroke.width)
                drawNodeDot(dA, ringColor = OrangeAccent)
                drawNodeDot(dB, ringColor = OrangeAccent)
                drawNodeDot(dC, ringColor = OrangeAccent)
                drawCenterLabel(textMeasurer, "Δ", Offset(w * 0.27f, h * 0.50f), InkBlack, fontSize = if (isExpanded) 13.sp else 10.sp)

                val arrX1 = w * 0.45f
                val arrX2 = w * 0.54f
                drawLine(InkBlack.copy(alpha = 0.5f), Offset(arrX1, h * 0.50f), Offset(arrX2, h * 0.50f), stroke.width)
                drawArrowHead(Offset(arrX2, h * 0.50f), 0f, 4.dp.toPx(), OrangeAccent, accentStroke.width)

                val yCenter = Offset(w * 0.71f, h * 0.50f)
                val yA = Offset(w * 0.61f, h * 0.28f)
                val yB = Offset(w * 0.61f, h * 0.72f)
                val yC = Offset(w * 0.85f, h * 0.50f)
                drawLine(OrangeAccent, yCenter, yA, accentStroke.width)
                drawLine(OrangeAccent, yCenter, yB, accentStroke.width)
                drawLine(InkBlack, yCenter, yC, stroke.width)
                drawNodeDot(yCenter)
                drawNodeDot(yA, ringColor = OrangeAccent)
                drawNodeDot(yB, ringColor = OrangeAccent)
                drawNodeDot(yC, ringColor = OrangeAccent)
                drawCenterLabel(textMeasurer, "Y", Offset(w * 0.77f, h * 0.34f), OrangeAccent, fontSize = if (isExpanded) 13.sp else 10.sp)

                annotations.loadZ?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(w * 0.26f, h * 0.87f), InkBlack, HairlineBorder, labelFontSize)
                }
                annotations.secondaryBox?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(w * 0.73f, h * 0.87f), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.TWO_PORT_NETWORK -> {
                val boxLeft = w * 0.34f
                val boxWidth = w * 0.28f
                val loadX = w * 0.82f
                val port2TapX = w * 0.71f

                drawLine(InkBlack, Offset(leftX + 3.5.dp.toPx(), topY), Offset(boxLeft, topY), stroke.width)
                drawLine(InkBlack, Offset(leftX + 3.5.dp.toPx(), botY), Offset(boxLeft, botY), stroke.width)
                drawArrowHead(Offset(w * 0.25f, topY), 0f, 4.dp.toPx(), OrangeAccent, accentStroke.width)
                drawNodeDot(Offset(leftX, topY), radius = 3.5.dp.toPx())
                drawNodeDot(Offset(leftX, botY), radius = 3.5.dp.toPx())

                drawRoundRect(
                    color = OrangeSoftBg,
                    topLeft = Offset(boxLeft, topY - 4.dp.toPx()),
                    size = Size(boxWidth, loopH + 8.dp.toPx()),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
                drawRoundRect(
                    color = OrangeAccent,
                    topLeft = Offset(boxLeft, topY - 4.dp.toPx()),
                    size = Size(boxWidth, loopH + 8.dp.toPx()),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                    style = accentStroke
                )
                drawCenterLabel(
                    textMeasurer,
                    annotations.secondaryBox ?: "[Y]",
                    Offset(boxLeft + boxWidth / 2f, midY),
                    InkBlack,
                    fontSize = if (isExpanded) 12.sp else 9.5.sp
                )

                drawLine(InkBlack, Offset(boxLeft + boxWidth, topY), Offset(loadX, topY), stroke.width)
                drawLine(InkBlack, Offset(boxLeft + boxWidth, botY), Offset(loadX, botY), stroke.width)
                drawNodeDot(Offset(port2TapX, topY), radius = 3.2.dp.toPx(), ringColor = OrangeAccent)
                drawNodeDot(Offset(port2TapX, botY), radius = 3.2.dp.toPx(), ringColor = OrangeAccent)

                val loadHalf = loopH * 0.27f
                drawLine(InkBlack, Offset(loadX, topY), Offset(loadX, midY - loadHalf), stroke.width)
                drawResistorVertical(loadX, midY - loadHalf, midY + loadHalf, 4.2.dp.toPx(), stroke)
                drawLine(InkBlack, Offset(loadX, midY + loadHalf), Offset(loadX, botY), stroke.width)

                annotations.source?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(leftX + 12.dp.toPx(), botLabelY), SlateDark, HairlineBorder, labelFontSize)
                }
                annotations.secondaryR?.let {
                    drawAnnotationPill(textMeasurer, it, Offset(loadX - 8.dp.toPx(), botLabelY), OrangeAccent, CopperBorderSoft, labelFontSize)
                }
            }

            CircuitTopology.CONTOUR_SPLANE -> {
                val cx = w * 0.50f
                val cy = h * 0.52f
                drawLine(InkBlack.copy(alpha = 0.35f), Offset(w * 0.16f, cy), Offset(w * 0.84f, cy), 1.2.dp.toPx())
                drawLine(InkBlack.copy(alpha = 0.35f), Offset(cx, h * 0.12f), Offset(cx, h * 0.92f), 1.2.dp.toPx())

                val r = h * 0.33f
                val shift = (problem.id.hashCode() % 3) * 0.04f
                val contourCenter = Offset(cx - w * (0.04f + shift), cy)
                drawCircle(
                    color = OrangeAccent,
                    radius = r,
                    center = contourCenter,
                    style = accentStroke
                )

                val markerPos = Offset(
                    contourCenter.x + r * cos(-wavePhase),
                    contourCenter.y + r * sin(-wavePhase)
                )
                drawCircle(color = OrangeAccent, radius = 3.2.dp.toPx(), center = markerPos)

                val cross = 3.8.dp.toPx()
                val poles = when {
                    problem.id == "p_620f" || problem.id == "p_636j" -> listOf(
                        Offset(cx, cy - r * 0.45f),
                        Offset(cx, cy + r * 0.45f),
                        Offset(cx, cy - r * 0.80f),
                        Offset(cx, cy + r * 0.80f)
                    )
                    problem.id == "p_616e" -> listOf(
                        Offset(cx - w * 0.06f, cy),
                        Offset(cx + w * 0.04f, cy),
                        Offset(cx + w * 0.24f, cy)
                    )
                    else -> listOf(
                        Offset(cx - w * 0.12f, cy - r * 0.42f),
                        Offset(cx - w * 0.12f, cy + r * 0.42f)
                    )
                }
                poles.forEach { p ->
                    drawLine(InkBlack, Offset(p.x - cross, p.y - cross), Offset(p.x + cross, p.y + cross), stroke.width)
                    drawLine(InkBlack, Offset(p.x - cross, p.y + cross), Offset(p.x + cross, p.y - cross), stroke.width)
                }
                drawCenterLabel(textMeasurer, "Re", Offset(w * 0.86f, cy - 8.dp.toPx()), SlateMuted, fontSize = if (isExpanded) 10.sp else 8.sp)
                drawCenterLabel(textMeasurer, "Im", Offset(cx + 12.dp.toPx(), h * 0.14f), SlateMuted, fontSize = if (isExpanded) 10.sp else 8.sp)
            }

            CircuitTopology.GEOMETRIC_PLANE -> {
                val ox = w * 0.46f
                val oy = h * 0.56f
                drawLine(InkBlack.copy(alpha = 0.3f), Offset(w * 0.14f, oy), Offset(w * 0.86f, oy), 1.2.dp.toPx())
                drawLine(InkBlack.copy(alpha = 0.3f), Offset(ox, h * 0.12f), Offset(ox, h * 0.90f), 1.2.dp.toPx())

                val len = h * 0.33f
                val displayAngleDeg = animatedPhaseDeg.coerceIn(-179f, 179f)
                val rad = Math.toRadians(displayAngleDeg.toDouble())
                val v1 = Offset(ox + len * 1.25f, oy - h * 0.02f)
                val v2 = Offset(
                    ox + (len * cos(rad)).toFloat(),
                    (oy - (len * sin(rad)).toFloat()).coerceIn(h * 0.12f, h * 0.90f)
                )

                val area = Path().apply {
                    moveTo(ox, oy)
                    lineTo(v1.x, v1.y)
                    lineTo(v1.x + (v2.x - ox), v1.y + (v2.y - oy))
                    lineTo(v2.x, v2.y)
                    close()
                }
                drawPath(path = area, color = OrangeSoftBg)
                drawLine(InkBlack, Offset(ox, oy), v1, stroke.width)
                drawLine(OrangeAccent, Offset(ox, oy), v2, accentStroke.width)
                drawCircle(color = OrangeAccent, radius = 3.dp.toPx(), center = v2)

                if (abs(displayAngleDeg) >= 3f) {
                    val arcRadius = if (isExpanded) 22.dp.toPx() else 16.dp.toPx()
                    drawArc(
                        color = OrangeAccent,
                        startAngle = if (displayAngleDeg >= 0f) -displayAngleDeg else 0f,
                        sweepAngle = abs(displayAngleDeg),
                        useCenter = false,
                        topLeft = Offset(ox - arcRadius, oy - arcRadius),
                        size = Size(arcRadius * 2f, arcRadius * 2f),
                        style = accentStroke
                    )
                }
                drawCenterLabel(textMeasurer, "e₁", Offset(w * 0.87f, oy - 8.dp.toPx()), SlateMuted, fontSize = if (isExpanded) 10.sp else 8.sp)
                drawCenterLabel(textMeasurer, "e₂", Offset(ox + 11.dp.toPx(), h * 0.14f), SlateMuted, fontSize = if (isExpanded) 10.sp else 8.sp)
            }
        }
    }
}

@Composable
fun LivePhasorMiniPlot(
    complex: ComplexNum?,
    modifier: Modifier = Modifier
) {
    val targetAngle = complex?.angleDeg?.toFloat() ?: 0f
    val targetMag = if (complex != null && complex.magnitude > 0.0) 0.78f else 0f

    val animAngle by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessMediumLow),
        label = "phasorAngle"
    )
    val animMag by animateFloatAsState(
        targetValue = targetMag,
        animationSpec = spring(dampingRatio = 0.80f, stiffness = Spring.StiffnessMediumLow),
        label = "phasorMag"
    )

    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = size.minDimension * 0.44f

        drawCircle(
            color = SurfaceElevated,
            radius = radius,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = HairlineBorder,
            radius = radius,
            center = Offset(cx, cy),
            style = Stroke(width = 1.dp.toPx())
        )
        drawCircle(
            color = HairlineBorder.copy(alpha = 0.7f),
            radius = radius * 0.5f,
            center = Offset(cx, cy),
            style = Stroke(width = 0.8.dp.toPx())
        )

        drawLine(
            color = SlateMuted.copy(alpha = 0.35f),
            start = Offset(cx - radius, cy),
            end = Offset(cx + radius, cy),
            strokeWidth = 0.9.dp.toPx()
        )
        drawLine(
            color = SlateMuted.copy(alpha = 0.35f),
            start = Offset(cx, cy - radius),
            end = Offset(cx, cy + radius),
            strokeWidth = 0.9.dp.toPx()
        )

        if (animMag > 0.02f) {
            val rad = Math.toRadians(animAngle.toDouble())
            val tip = Offset(
                cx + (radius * animMag * cos(rad)).toFloat(),
                cy - (radius * animMag * sin(rad)).toFloat()
            )
            drawLine(
                color = OrangeAccent,
                start = Offset(cx, cy),
                end = tip,
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawCircle(
                color = OrangeAccent.copy(alpha = 0.2f),
                radius = 5.5.dp.toPx(),
                center = tip
            )
            drawCircle(
                color = OrangeAccent,
                radius = 3.dp.toPx(),
                center = tip
            )
        }
        drawCircle(
            color = InkBlack,
            radius = 2.dp.toPx(),
            center = Offset(cx, cy)
        )
    }
}

private fun DrawScope.drawDraftingGrid(stepPx: Float, gridColor: Color) {
    var x = stepPx
    while (x < size.width) {
        drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 0.6.dp.toPx())
        x += stepPx
    }
    var y = stepPx
    while (y < size.height) {
        drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 0.6.dp.toPx())
        y += stepPx
    }
}

private fun DrawScope.drawNodeDot(
    center: Offset,
    radius: Float = 2.6.dp.toPx(),
    ringColor: Color = InkBlack
) {
    drawCircle(color = SurfaceWhite, radius = radius, center = center)
    drawCircle(color = ringColor, radius = radius, center = center, style = Stroke(width = 1.4.dp.toPx()))
}

private fun DrawScope.drawAcSource(
    center: Offset,
    radius: Float,
    phase: Float,
    circleStroke: Stroke,
    waveStroke: Stroke
) {
    drawCircle(color = SurfaceWhite, radius = radius, center = center)
    drawCircle(color = InkBlack, radius = radius, center = center, style = circleStroke)
    val path = Path()
    val steps = 18
    val halfW = radius * 0.6f
    for (i in 0..steps) {
        val t = i.toFloat() / steps.toFloat()
        val x = center.x - halfW + t * (halfW * 2f)
        val y = center.y - sin(t * 2f * PI + phase).toFloat() * (radius * 0.40f)
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    drawPath(path = path, color = OrangeAccent, style = waveStroke)
}

private fun DrawScope.drawResistor(
    startX: Float,
    endX: Float,
    y: Float,
    amplitude: Float,
    stroke: Stroke
) {
    val path = Path()
    val peaks = 6
    val seg = (endX - startX) / peaks
    path.moveTo(startX, y)
    for (i in 1 until peaks) {
        val x = startX + i * seg
        val dy = if (i % 2 == 1) -amplitude else amplitude
        path.lineTo(x, y + dy)
    }
    path.lineTo(endX, y)
    drawPath(path = path, color = InkBlack, style = stroke)
}

private fun DrawScope.drawInductor(
    startX: Float,
    endX: Float,
    y: Float,
    arcHeight: Float,
    stroke: Stroke
) {
    val loops = 3
    val loopW = (endX - startX) / loops
    for (i in 0 until loops) {
        val lx = startX + i * loopW
        drawArc(
            color = OrangeAccent,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(lx, y - arcHeight),
            size = Size(loopW, arcHeight * 2f),
            style = stroke
        )
    }
}

private fun DrawScope.drawCapacitor(
    x: Float,
    y: Float,
    halfPlateHeight: Float,
    stroke: Stroke,
    gap: Float = 4.5.dp.toPx()
) {
    drawLine(OrangeAccent, Offset(x - gap, y - halfPlateHeight), Offset(x - gap, y + halfPlateHeight), stroke.width)
    drawLine(OrangeAccent, Offset(x + gap, y - halfPlateHeight), Offset(x + gap, y + halfPlateHeight), stroke.width)
}

private fun DrawScope.drawResistorVertical(
    x: Float,
    startY: Float,
    endY: Float,
    amplitude: Float,
    stroke: Stroke
) {
    val path = Path()
    val peaks = 6
    val seg = (endY - startY) / peaks
    path.moveTo(x, startY)
    for (i in 1 until peaks) {
        val y = startY + i * seg
        val dx = if (i % 2 == 1) -amplitude else amplitude
        path.lineTo(x + dx, y)
    }
    path.lineTo(x, endY)
    drawPath(path = path, color = InkBlack, style = stroke)
}

private fun DrawScope.drawInductorVertical(
    x: Float,
    startY: Float,
    endY: Float,
    arcWidth: Float,
    stroke: Stroke,
    bulgeRight: Boolean
) {
    val loops = 3
    val loopH = (endY - startY) / loops
    val startAngle = if (bulgeRight) 270f else 90f
    for (i in 0 until loops) {
        val ly = startY + i * loopH
        drawArc(
            color = OrangeAccent,
            startAngle = startAngle,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(x - arcWidth, ly),
            size = Size(arcWidth * 2f, loopH),
            style = stroke
        )
    }
}

private fun DrawScope.drawArrowHead(
    tip: Offset,
    angleDeg: Float,
    sizePx: Float,
    color: Color,
    strokeWidth: Float
) {
    val backRad = Math.toRadians(angleDeg.toDouble())
    val wingSpread = Math.toRadians(34.0)
    val leftWing = Offset(
        tip.x - (sizePx * cos(backRad - wingSpread)).toFloat(),
        tip.y - (sizePx * sin(backRad - wingSpread)).toFloat()
    )
    val rightWing = Offset(
        tip.x - (sizePx * cos(backRad + wingSpread)).toFloat(),
        tip.y - (sizePx * sin(backRad + wingSpread)).toFloat()
    )
    drawLine(color, leftWing, tip, strokeWidth = strokeWidth, cap = StrokeCap.Round)
    drawLine(color, rightWing, tip, strokeWidth = strokeWidth, cap = StrokeCap.Round)
}

private fun DrawScope.drawGroundSymbol(
    top: Offset,
    strokeWidth: Float
) {
    val bars = listOf(5.5.dp.toPx(), 3.4.dp.toPx(), 1.5.dp.toPx())
    val stepY = 2.8.dp.toPx()
    bars.forEachIndexed { idx, halfW ->
        val y = top.y + idx * stepY
        drawLine(InkBlack, Offset(top.x - halfW, y), Offset(top.x + halfW, y), strokeWidth = strokeWidth, cap = StrokeCap.Round)
    }
}

private fun DrawScope.drawCenterLabel(
    textMeasurer: TextMeasurer,
    label: String,
    center: Offset,
    color: Color,
    fontSize: TextUnit = 9.5.sp
) {
    val layout = textMeasurer.measure(
        text = AnnotatedString(label),
        style = TextStyle(
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = color
        )
    )
    drawText(
        textLayoutResult = layout,
        topLeft = Offset(
            x = (center.x - layout.size.width / 2f).coerceIn(0f, (size.width - layout.size.width).coerceAtLeast(0f)),
            y = (center.y - layout.size.height / 2f).coerceIn(0f, (size.height - layout.size.height).coerceAtLeast(0f))
        )
    )
}

private enum class PillAnchorMode {
    CENTER,
    START_AT,
    END_AT
}

private fun DrawScope.drawAnnotationPill(
    textMeasurer: TextMeasurer,
    label: String,
    anchor: Offset,
    textColor: Color,
    borderColor: Color,
    fontSize: TextUnit,
    anchorMode: PillAnchorMode = PillAnchorMode.CENTER
) {
    if (label.isBlank()) return
    val layout = textMeasurer.measure(
        text = AnnotatedString(label),
        style = TextStyle(
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    )
    val padX = 4.5.dp.toPx()
    val padY = 1.8.dp.toPx()
    val pillW = layout.size.width + padX * 2f
    val pillH = layout.size.height + padY * 2f
    val margin = 3.dp.toPx()

    val rawX = when (anchorMode) {
        PillAnchorMode.START_AT -> anchor.x
        PillAnchorMode.END_AT -> anchor.x - pillW
        PillAnchorMode.CENTER -> anchor.x - pillW / 2f
    }
    val pillX = rawX.coerceIn(margin, (size.width - pillW - margin).coerceAtLeast(margin))
    val pillY = (anchor.y - pillH / 2f).coerceIn(margin, (size.height - pillH - margin).coerceAtLeast(margin))
    val corner = 4.dp.toPx()

    drawRoundRect(
        color = SurfaceElevated.copy(alpha = 0.94f),
        topLeft = Offset(pillX, pillY),
        size = Size(pillW, pillH),
        cornerRadius = CornerRadius(corner, corner)
    )
    drawRoundRect(
        color = borderColor.copy(alpha = 0.85f),
        topLeft = Offset(pillX, pillY),
        size = Size(pillW, pillH),
        cornerRadius = CornerRadius(corner, corner),
        style = Stroke(width = 0.7.dp.toPx())
    )
    drawText(
        textLayoutResult = layout,
        topLeft = Offset(pillX + padX, pillY + padY)
    )
}
