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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CircuitProblem
import com.example.model.CircuitTopology
import com.example.model.ComplexNum
import com.example.ui.theme.DraftingGridLine
import com.example.ui.theme.HairlineBorder
import com.example.ui.theme.InkBlack
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeBright
import com.example.ui.theme.OrangeSoftBg
import com.example.ui.theme.PlaceholderSlate
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceWhite
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

    val trayShape = RoundedCornerShape(14.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(86.dp)
            .recessedPaperWell(
                shape = trayShape,
                backgroundColor = SurfaceElevated,
                borderColor = HairlineBorder
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawDraftingGrid(stepPx = 12.dp.toPx(), gridColor = DraftingGridLine.copy(alpha = 0.65f))

            val stroke = Stroke(width = 1.55.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            val accentStroke = Stroke(width = 1.9.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            val w = size.width
            val h = size.height
            val topY = h * 0.25f
            val botY = h * 0.79f
            val leftX = w * 0.14f
            val rightX = w * 0.86f
            val midY = (topY + botY) / 2f
            val capHalfGap = 4.5.dp.toPx()

            when (topology) {
                CircuitTopology.SERIES_RLC -> {
                    drawAcSource(Offset(leftX, midY), h * 0.155f, wavePhase, stroke, accentStroke)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - h * 0.155f), stroke.width)
                    drawLine(InkBlack, Offset(leftX, midY + h * 0.155f), Offset(leftX, botY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)
                    drawLine(InkBlack, Offset(rightX, topY), Offset(rightX, botY), stroke.width)

                    val rStart = w * 0.24f
                    val rEnd = w * 0.40f
                    val lStart = w * 0.46f
                    val lEnd = w * 0.64f
                    val cPos = w * 0.75f

                    drawLine(InkBlack, Offset(leftX, topY), Offset(rStart, topY), stroke.width)
                    drawResistor(rStart, rEnd, topY, h * 0.08f, stroke)
                    drawLine(InkBlack, Offset(rEnd, topY), Offset(lStart, topY), stroke.width)
                    drawInductor(lStart, lEnd, topY, h * 0.095f, accentStroke)
                    drawLine(InkBlack, Offset(lEnd, topY), Offset(cPos - capHalfGap, topY), stroke.width)
                    drawCapacitor(cPos, topY, h * 0.135f, accentStroke)
                    drawLine(InkBlack, Offset(cPos + capHalfGap, topY), Offset(rightX, topY), stroke.width)

                    drawNodeDot(Offset(leftX, topY))
                    drawNodeDot(Offset(rightX, topY))
                    drawNodeDot(Offset(leftX, botY))
                    drawNodeDot(Offset(rightX, botY))
                }

                CircuitTopology.SERIES_RL -> {
                    drawAcSource(Offset(leftX, midY), h * 0.155f, wavePhase, stroke, accentStroke)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - h * 0.155f), stroke.width)
                    drawLine(InkBlack, Offset(leftX, midY + h * 0.155f), Offset(leftX, botY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)
                    drawLine(InkBlack, Offset(rightX, topY), Offset(rightX, botY), stroke.width)

                    val rStart = w * 0.28f
                    val rEnd = w * 0.48f
                    val lStart = w * 0.56f
                    val lEnd = w * 0.76f
                    drawLine(InkBlack, Offset(leftX, topY), Offset(rStart, topY), stroke.width)
                    drawResistor(rStart, rEnd, topY, h * 0.085f, stroke)
                    drawLine(InkBlack, Offset(rEnd, topY), Offset(lStart, topY), stroke.width)
                    drawInductor(lStart, lEnd, topY, h * 0.105f, accentStroke)
                    drawLine(InkBlack, Offset(lEnd, topY), Offset(rightX, topY), stroke.width)

                    drawNodeDot(Offset(leftX, topY))
                    drawNodeDot(Offset(rightX, topY))
                    drawNodeDot(Offset(leftX, botY))
                    drawNodeDot(Offset(rightX, botY))
                }

                CircuitTopology.SERIES_RC -> {
                    drawAcSource(Offset(leftX, midY), h * 0.155f, wavePhase, stroke, accentStroke)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - h * 0.155f), stroke.width)
                    drawLine(InkBlack, Offset(leftX, midY + h * 0.155f), Offset(leftX, botY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)
                    drawLine(InkBlack, Offset(rightX, topY), Offset(rightX, botY), stroke.width)

                    val rStart = w * 0.28f
                    val rEnd = w * 0.50f
                    val cPos = w * 0.68f
                    drawLine(InkBlack, Offset(leftX, topY), Offset(rStart, topY), stroke.width)
                    drawResistor(rStart, rEnd, topY, h * 0.085f, stroke)
                    drawLine(InkBlack, Offset(rEnd, topY), Offset(cPos - capHalfGap, topY), stroke.width)
                    drawCapacitor(cPos, topY, h * 0.135f, accentStroke)
                    drawLine(InkBlack, Offset(cPos + capHalfGap, topY), Offset(rightX, topY), stroke.width)

                    drawNodeDot(Offset(leftX, topY))
                    drawNodeDot(Offset(rightX, topY))
                    drawNodeDot(Offset(leftX, botY))
                    drawNodeDot(Offset(rightX, botY))
                }

                CircuitTopology.PARALLEL_RC -> {
                    drawAcSource(Offset(leftX, midY), h * 0.155f, wavePhase, stroke, accentStroke)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - h * 0.155f), stroke.width)
                    drawLine(InkBlack, Offset(leftX, midY + h * 0.155f), Offset(leftX, botY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(rightX, topY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)

                    val branch1X = w * 0.50f
                    val rHalf = 12.dp.toPx()
                    drawLine(InkBlack, Offset(branch1X, topY), Offset(branch1X, midY - rHalf), stroke.width)
                    drawResistorVertical(branch1X, midY - rHalf, midY + rHalf, 4.5.dp.toPx(), stroke)
                    drawLine(InkBlack, Offset(branch1X, midY + rHalf), Offset(branch1X, botY), stroke.width)
                    drawNodeDot(Offset(branch1X, topY))
                    drawNodeDot(Offset(branch1X, botY))

                    drawLine(InkBlack, Offset(rightX, topY), Offset(rightX, midY - capHalfGap), stroke.width)
                    drawLine(OrangeAccent, Offset(rightX - 11.dp.toPx(), midY - capHalfGap), Offset(rightX + 11.dp.toPx(), midY - capHalfGap), accentStroke.width)
                    drawLine(OrangeAccent, Offset(rightX - 11.dp.toPx(), midY + capHalfGap), Offset(rightX + 11.dp.toPx(), midY + capHalfGap), accentStroke.width)
                    drawLine(InkBlack, Offset(rightX, midY + capHalfGap), Offset(rightX, botY), stroke.width)
                }

                CircuitTopology.PARALLEL_RL -> {
                    val rlRightX = w * 0.82f
                    drawAcSource(Offset(leftX, midY), h * 0.155f, wavePhase, stroke, accentStroke)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - h * 0.155f), stroke.width)
                    drawLine(InkBlack, Offset(leftX, midY + h * 0.155f), Offset(leftX, botY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(rlRightX, topY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, botY), Offset(rlRightX, botY), stroke.width)

                    val branch1X = w * 0.48f
                    val elemHalf = 12.5.dp.toPx()
                    drawLine(InkBlack, Offset(branch1X, topY), Offset(branch1X, midY - elemHalf), stroke.width)
                    drawResistorVertical(branch1X, midY - elemHalf, midY + elemHalf, 4.5.dp.toPx(), stroke)
                    drawLine(InkBlack, Offset(branch1X, midY + elemHalf), Offset(branch1X, botY), stroke.width)
                    drawNodeDot(Offset(branch1X, topY))
                    drawNodeDot(Offset(branch1X, botY))

                    drawLine(InkBlack, Offset(rlRightX, topY), Offset(rlRightX, midY - elemHalf), stroke.width)
                    drawInductorVertical(rlRightX, midY - elemHalf, midY + elemHalf, 5.5.dp.toPx(), accentStroke, bulgeRight = true)
                    drawLine(InkBlack, Offset(rlRightX, midY + elemHalf), Offset(rlRightX, botY), stroke.width)
                }

                CircuitTopology.PARALLEL_RLC -> {
                    drawAcSource(Offset(leftX, midY), h * 0.155f, wavePhase, stroke, accentStroke)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - h * 0.155f), stroke.width)
                    drawLine(InkBlack, Offset(leftX, midY + h * 0.155f), Offset(leftX, botY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(rightX, topY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)

                    val branch1X = w * 0.42f
                    val branch2X = w * 0.64f
                    val elemHalf = 12.5.dp.toPx()

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

                    drawLine(InkBlack, Offset(rightX, topY), Offset(rightX, midY - capHalfGap), stroke.width)
                    drawLine(OrangeAccent, Offset(rightX - 10.dp.toPx(), midY - capHalfGap), Offset(rightX + 10.dp.toPx(), midY - capHalfGap), accentStroke.width)
                    drawLine(OrangeAccent, Offset(rightX - 10.dp.toPx(), midY + capHalfGap), Offset(rightX + 10.dp.toPx(), midY + capHalfGap), accentStroke.width)
                    drawLine(InkBlack, Offset(rightX, midY + capHalfGap), Offset(rightX, botY), stroke.width)
                }

                CircuitTopology.COMPLEX_LOAD -> {
                    val sx = leftX + 10.dp.toPx()
                    val boxLeft = w * 0.60f
                    val boxWidth = w * 0.21f
                    val boxCenterX = boxLeft + boxWidth / 2f

                    drawAcSource(Offset(sx, midY), h * 0.155f, wavePhase, stroke, accentStroke)
                    drawLine(InkBlack, Offset(sx, topY), Offset(sx, midY - h * 0.155f), stroke.width)
                    drawLine(InkBlack, Offset(sx, midY + h * 0.155f), Offset(sx, botY), stroke.width)
                    drawLine(InkBlack, Offset(sx, topY), Offset(boxCenterX, topY), stroke.width)
                    drawLine(InkBlack, Offset(sx, botY), Offset(boxCenterX, botY), stroke.width)

                    val ax = w * 0.41f
                    drawArrowHead(Offset(ax + 8.dp.toPx(), topY), 0f, 5.dp.toPx(), OrangeAccent, accentStroke.width)

                    // Subtle traveling signal pulse dot along top feed wire
                    val pulseFrac = wavePhase / (2f * PI.toFloat())
                    val pulseX = sx + (boxCenterX - sx) * pulseFrac
                    drawCircle(color = OrangeAccent.copy(alpha = 0.85f), radius = 2.6.dp.toPx(), center = Offset(pulseX, topY))

                    drawLine(InkBlack, Offset(boxCenterX, topY), Offset(boxCenterX, midY - 15.dp.toPx()), stroke.width)
                    drawLine(InkBlack, Offset(boxCenterX, midY + 15.dp.toPx()), Offset(boxCenterX, botY), stroke.width)
                    drawRoundRect(
                        color = OrangeSoftBg,
                        topLeft = Offset(boxLeft, midY - 15.dp.toPx()),
                        size = Size(boxWidth, 30.dp.toPx()),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )
                    drawRoundRect(
                        color = OrangeAccent,
                        topLeft = Offset(boxLeft, midY - 15.dp.toPx()),
                        size = Size(boxWidth, 30.dp.toPx()),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
                        style = accentStroke
                    )
                    drawCenterLabel(textMeasurer, "Z", Offset(boxCenterX, midY), OrangeAccent)
                }

                CircuitTopology.LOW_PASS_RC -> {
                    val rStart = w * 0.25f
                    val rEnd = w * 0.48f
                    val tapX = w * 0.62f
                    drawLine(InkBlack, Offset(leftX + 4.dp.toPx(), topY), Offset(rStart, topY), stroke.width)
                    drawResistor(rStart, rEnd, topY, h * 0.085f, stroke)
                    drawLine(InkBlack, Offset(rEnd, topY), Offset(rightX - 4.dp.toPx(), topY), stroke.width)
                    drawLine(InkBlack, Offset(leftX + 4.dp.toPx(), botY), Offset(rightX - 4.dp.toPx(), botY), stroke.width)

                    drawLine(InkBlack, Offset(tapX, topY), Offset(tapX, midY - capHalfGap), stroke.width)
                    drawLine(OrangeAccent, Offset(tapX - 11.dp.toPx(), midY - capHalfGap), Offset(tapX + 11.dp.toPx(), midY - capHalfGap), accentStroke.width)
                    drawLine(OrangeAccent, Offset(tapX - 11.dp.toPx(), midY + capHalfGap), Offset(tapX + 11.dp.toPx(), midY + capHalfGap), accentStroke.width)
                    drawLine(InkBlack, Offset(tapX, midY + capHalfGap), Offset(tapX, botY), stroke.width)

                    drawNodeDot(Offset(tapX, topY))
                    drawNodeDot(Offset(tapX, botY))
                    drawNodeDot(Offset(leftX, topY), radius = 3.8.dp.toPx())
                    drawNodeDot(Offset(leftX, botY), radius = 3.8.dp.toPx())
                    drawNodeDot(Offset(rightX, topY), radius = 3.8.dp.toPx(), ringColor = OrangeAccent)
                    drawNodeDot(Offset(rightX, botY), radius = 3.8.dp.toPx(), ringColor = OrangeAccent)
                }

                CircuitTopology.HIGH_PASS_RC -> {
                    val cX = w * 0.36f
                    val tapX = w * 0.63f
                    val rHalf = 12.dp.toPx()

                    drawLine(InkBlack, Offset(leftX + 4.dp.toPx(), topY), Offset(cX - capHalfGap, topY), stroke.width)
                    drawCapacitor(cX, topY, h * 0.135f, accentStroke)
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
                }

                CircuitTopology.FEEDBACK_LOOP -> {
                    val sumCenter = Offset(w * 0.24f, topY + 10.dp.toPx())
                    val sy = sumCenter.y
                    val sumR = 10.dp.toPx()
                    drawLine(InkBlack, Offset(w * 0.10f, sy), Offset(sumCenter.x - sumR, sy), stroke.width)
                    drawArrowHead(Offset(sumCenter.x - sumR, sy), 0f, 4.dp.toPx(), InkBlack, stroke.width)
                    drawCircle(color = SurfaceWhite, radius = sumR, center = sumCenter)
                    drawCircle(InkBlack, radius = sumR, center = sumCenter, style = stroke)
                    drawLine(InkBlack, Offset(sumCenter.x - 4.dp.toPx(), sy), Offset(sumCenter.x + 4.dp.toPx(), sy), stroke.width)
                    drawLine(InkBlack, Offset(sumCenter.x, sy - 4.dp.toPx()), Offset(sumCenter.x, sy + 4.dp.toPx()), stroke.width)

                    val gLeft = w * 0.42f
                    val gWidth = w * 0.22f
                    drawLine(InkBlack, Offset(sumCenter.x + sumR, sy), Offset(gLeft, sy), stroke.width)
                    drawArrowHead(Offset(gLeft, sy), 0f, 4.dp.toPx(), InkBlack, stroke.width)
                    drawRoundRect(
                        color = OrangeSoftBg,
                        topLeft = Offset(gLeft, sy - 13.dp.toPx()),
                        size = Size(gWidth, 26.dp.toPx()),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )
                    drawRoundRect(
                        color = OrangeAccent,
                        topLeft = Offset(gLeft, sy - 13.dp.toPx()),
                        size = Size(gWidth, 26.dp.toPx()),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
                        style = accentStroke
                    )
                    drawCenterLabel(textMeasurer, "G", Offset(gLeft + gWidth / 2f, sy), OrangeAccent)

                    val tapX = w * 0.78f
                    val hLeft = w * 0.43f
                    val hWidth = w * 0.20f
                    drawLine(InkBlack, Offset(gLeft + gWidth, sy), Offset(w * 0.90f, sy), stroke.width)
                    drawArrowHead(Offset(w * 0.90f, sy), 0f, 4.dp.toPx(), OrangeAccent, stroke.width)
                    drawLine(InkBlack, Offset(tapX, sy), Offset(tapX, botY), stroke.width)
                    drawLine(InkBlack, Offset(tapX, botY), Offset(hLeft + hWidth, botY), stroke.width)
                    drawLine(InkBlack, Offset(hLeft, botY), Offset(sumCenter.x, botY), stroke.width)
                    drawLine(InkBlack, Offset(sumCenter.x, botY), Offset(sumCenter.x, sy + sumR), stroke.width)
                    drawArrowHead(Offset(sumCenter.x, sy + sumR), 270f, 4.dp.toPx(), OrangeAccent, stroke.width)
                    drawCenterLabel(textMeasurer, "−", Offset(sumCenter.x - 10.dp.toPx(), sy + sumR + 6.dp.toPx()), OrangeAccent)
                    drawNodeDot(Offset(tapX, sy))

                    drawRoundRect(
                        color = SurfaceWhite,
                        topLeft = Offset(hLeft, botY - 10.dp.toPx()),
                        size = Size(hWidth, 20.dp.toPx()),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )
                    drawRoundRect(
                        color = InkBlack,
                        topLeft = Offset(hLeft, botY - 10.dp.toPx()),
                        size = Size(hWidth, 20.dp.toPx()),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                        style = stroke
                    )
                    drawCenterLabel(textMeasurer, "H", Offset(hLeft + hWidth / 2f, botY), InkBlack)
                }

                CircuitTopology.TRANSFORMER_COUPLED -> {
                    val priX = w * 0.39f
                    val secX = w * 0.56f
                    val coilHalf = 13.dp.toPx()

                    // Primary winding loop + AC source
                    drawAcSource(Offset(leftX, midY), h * 0.155f, wavePhase, stroke, accentStroke)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - h * 0.155f), stroke.width)
                    drawLine(InkBlack, Offset(leftX, midY + h * 0.155f), Offset(leftX, botY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(priX, topY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, botY), Offset(priX, botY), stroke.width)

                    drawLine(InkBlack, Offset(priX, topY), Offset(priX, midY - coilHalf), stroke.width)
                    drawInductorVertical(priX, midY - coilHalf, midY + coilHalf, 5.2.dp.toPx(), accentStroke, bulgeRight = true)
                    drawLine(InkBlack, Offset(priX, midY + coilHalf), Offset(priX, botY), stroke.width)

                    // Iron core magnetic coupling bars & polarity dots
                    val coreX1 = w * 0.46f
                    val coreX2 = w * 0.49f
                    drawLine(InkBlack.copy(alpha = 0.7f), Offset(coreX1, topY + 2.dp.toPx()), Offset(coreX1, botY - 2.dp.toPx()), stroke.width)
                    drawLine(InkBlack.copy(alpha = 0.7f), Offset(coreX2, topY + 2.dp.toPx()), Offset(coreX2, botY - 2.dp.toPx()), stroke.width)
                    drawCircle(color = OrangeAccent, radius = 2.2.dp.toPx(), center = Offset(priX - 7.dp.toPx(), midY - coilHalf))
                    drawCircle(color = OrangeAccent, radius = 2.2.dp.toPx(), center = Offset(secX + 7.dp.toPx(), midY - coilHalf))

                    // Secondary winding loop + load Z
                    val loadCenterX = w * 0.80f
                    val boxW = w * 0.14f
                    drawLine(InkBlack, Offset(secX, topY), Offset(secX, midY - coilHalf), stroke.width)
                    drawInductorVertical(secX, midY - coilHalf, midY + coilHalf, 5.2.dp.toPx(), accentStroke, bulgeRight = false)
                    drawLine(InkBlack, Offset(secX, midY + coilHalf), Offset(secX, botY), stroke.width)
                    drawLine(InkBlack, Offset(secX, topY), Offset(loadCenterX, topY), stroke.width)
                    drawLine(InkBlack, Offset(secX, botY), Offset(loadCenterX, botY), stroke.width)
                    drawLine(InkBlack, Offset(loadCenterX, topY), Offset(loadCenterX, midY - 14.dp.toPx()), stroke.width)
                    drawLine(InkBlack, Offset(loadCenterX, midY + 14.dp.toPx()), Offset(loadCenterX, botY), stroke.width)
                    drawRoundRect(
                        color = OrangeSoftBg,
                        topLeft = Offset(loadCenterX - boxW / 2f, midY - 14.dp.toPx()),
                        size = Size(boxW, 28.dp.toPx()),
                        cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
                    )
                    drawRoundRect(
                        color = OrangeAccent,
                        topLeft = Offset(loadCenterX - boxW / 2f, midY - 14.dp.toPx()),
                        size = Size(boxW, 28.dp.toPx()),
                        cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx()),
                        style = accentStroke
                    )
                    drawCenterLabel(textMeasurer, "Z", Offset(loadCenterX, midY), OrangeAccent)
                }

                CircuitTopology.OP_AMP_INVERTING -> {
                    val ampBaseX = w * 0.45f
                    val ampTipX = w * 0.68f
                    val ampMidY = h * 0.56f
                    val minusY = ampMidY - 8.dp.toPx()
                    val plusY = ampMidY + 8.dp.toPx()
                    val fbY = h * 0.21f
                    val sumNodeX = w * 0.37f
                    val outNodeX = w * 0.74f

                    // Input port + R_in to inverting (-) pin
                    drawNodeDot(Offset(leftX, minusY), radius = 3.5.dp.toPx())
                    val rInStart = w * 0.19f
                    val rInEnd = w * 0.33f
                    drawLine(InkBlack, Offset(leftX + 3.5.dp.toPx(), minusY), Offset(rInStart, minusY), stroke.width)
                    drawResistor(rInStart, rInEnd, minusY, 4.dp.toPx(), stroke)
                    drawLine(InkBlack, Offset(rInEnd, minusY), Offset(ampBaseX, minusY), stroke.width)
                    drawNodeDot(Offset(sumNodeX, minusY))

                    // Non-inverting (+) pin to ground
                    val gndX = w * 0.38f
                    drawLine(InkBlack, Offset(ampBaseX, plusY), Offset(gndX, plusY), stroke.width)
                    drawLine(InkBlack, Offset(gndX, plusY), Offset(gndX, plusY + 9.dp.toPx()), stroke.width)
                    drawGroundSymbol(Offset(gndX, plusY + 9.dp.toPx()), stroke.width)

                    // Op-amp triangle
                    val triPath = Path().apply {
                        moveTo(ampBaseX, ampMidY - 16.dp.toPx())
                        lineTo(ampTipX, ampMidY)
                        lineTo(ampBaseX, ampMidY + 16.dp.toPx())
                        close()
                    }
                    drawPath(path = triPath, color = SurfaceWhite)
                    drawPath(path = triPath, color = InkBlack, style = stroke)
                    drawCenterLabel(textMeasurer, "−", Offset(ampBaseX + 6.5.dp.toPx(), minusY), InkBlack)
                    drawCenterLabel(textMeasurer, "+", Offset(ampBaseX + 6.5.dp.toPx(), plusY), InkBlack)

                    // Feedback path R_f || C_f box
                    val fbBoxLeft = w * 0.44f
                    val fbBoxW = w * 0.22f
                    drawLine(InkBlack, Offset(sumNodeX, minusY), Offset(sumNodeX, fbY), stroke.width)
                    drawLine(InkBlack, Offset(sumNodeX, fbY), Offset(fbBoxLeft, fbY), stroke.width)
                    drawRoundRect(
                        color = OrangeSoftBg,
                        topLeft = Offset(fbBoxLeft, fbY - 9.dp.toPx()),
                        size = Size(fbBoxW, 18.dp.toPx()),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )
                    drawRoundRect(
                        color = OrangeAccent,
                        topLeft = Offset(fbBoxLeft, fbY - 9.dp.toPx()),
                        size = Size(fbBoxW, 18.dp.toPx()),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                        style = accentStroke
                    )
                    drawCenterLabel(textMeasurer, "Zf", Offset(fbBoxLeft + fbBoxW / 2f, fbY), OrangeAccent)
                    drawLine(InkBlack, Offset(fbBoxLeft + fbBoxW, fbY), Offset(outNodeX, fbY), stroke.width)
                    drawLine(InkBlack, Offset(outNodeX, fbY), Offset(outNodeX, ampMidY), stroke.width)

                    // Output line & port
                    drawLine(InkBlack, Offset(ampTipX, ampMidY), Offset(rightX - 3.5.dp.toPx(), ampMidY), stroke.width)
                    drawNodeDot(Offset(outNodeX, ampMidY))
                    drawNodeDot(Offset(rightX, ampMidY), radius = 3.5.dp.toPx(), ringColor = OrangeAccent)
                }

                CircuitTopology.AC_BRIDGE -> {
                    val srcX = w * 0.16f
                    val bx = w * 0.56f
                    val bLeft = Offset(bx - w * 0.18f, midY)
                    val bRight = Offset(bx + w * 0.18f, midY)
                    val bTop = Offset(bx, h * 0.16f)
                    val bBot = Offset(bx, h * 0.84f)

                    // Excitation rail from AC source to top & bottom bridge vertices
                    drawAcSource(Offset(srcX, midY), h * 0.155f, wavePhase, stroke, accentStroke)
                    drawLine(InkBlack, Offset(srcX, bTop.y), Offset(srcX, midY - h * 0.155f), stroke.width)
                    drawLine(InkBlack, Offset(srcX, midY + h * 0.155f), Offset(srcX, bBot.y), stroke.width)
                    drawLine(InkBlack, Offset(srcX, bTop.y), bTop, stroke.width)
                    drawLine(InkBlack, Offset(srcX, bBot.y), bBot, stroke.width)

                    // Diamond 4 arms
                    drawLine(InkBlack, bTop, bLeft, stroke.width)
                    drawLine(OrangeAccent, bTop, bRight, accentStroke.width)
                    drawLine(OrangeAccent, bLeft, bBot, accentStroke.width)
                    drawLine(InkBlack, bRight, bBot, stroke.width)

                    // Arm mid-markers (component pads on the 4 diamond arms)
                    listOf(
                        Offset((bTop.x + bLeft.x) / 2f, (bTop.y + bLeft.y) / 2f) to InkBlack,
                        Offset((bTop.x + bRight.x) / 2f, (bTop.y + bRight.y) / 2f) to OrangeAccent,
                        Offset((bLeft.x + bBot.x) / 2f, (bLeft.y + bBot.y) / 2f) to OrangeAccent,
                        Offset((bRight.x + bBot.x) / 2f, (bRight.y + bBot.y) / 2f) to InkBlack
                    ).forEach { (pt, clr) ->
                        drawCircle(color = SurfaceWhite, radius = 3.4.dp.toPx(), center = pt)
                        drawCircle(color = clr, radius = 3.4.dp.toPx(), center = pt, style = stroke)
                    }

                    // Null detector branch across diagonal
                    val detCenter = Offset(bx, midY)
                    val detR = 8.5.dp.toPx()
                    drawLine(InkBlack, bLeft, Offset(detCenter.x - detR, midY), stroke.width)
                    drawLine(InkBlack, Offset(detCenter.x + detR, midY), bRight, stroke.width)
                    drawCircle(color = OrangeSoftBg, radius = detR, center = detCenter)
                    drawCircle(color = OrangeAccent, radius = detR, center = detCenter, style = accentStroke)
                    drawCenterLabel(textMeasurer, "∅", detCenter, OrangeAccent)

                    drawNodeDot(bTop)
                    drawNodeDot(bBot)
                    drawNodeDot(bLeft)
                    drawNodeDot(bRight)
                }

                CircuitTopology.TWO_MESH -> {
                    val midX = w * 0.50f
                    val elemHalf = 12.5.dp.toPx()

                    drawAcSource(Offset(leftX, midY), h * 0.155f, wavePhase, stroke, accentStroke)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - h * 0.155f), stroke.width)
                    drawLine(InkBlack, Offset(leftX, midY + h * 0.155f), Offset(leftX, botY), stroke.width)

                    // Left loop top R1
                    val r1Start = w * 0.24f
                    val r1End = w * 0.40f
                    drawLine(InkBlack, Offset(leftX, topY), Offset(r1Start, topY), stroke.width)
                    drawResistor(r1Start, r1End, topY, h * 0.08f, stroke)
                    drawLine(InkBlack, Offset(r1End, topY), Offset(midX, topY), stroke.width)

                    // Right loop top R2 & vertical R3
                    val r2Start = w * 0.60f
                    val r2End = w * 0.76f
                    drawLine(InkBlack, Offset(midX, topY), Offset(r2Start, topY), stroke.width)
                    drawResistor(r2Start, r2End, topY, h * 0.08f, stroke)
                    drawLine(InkBlack, Offset(r2End, topY), Offset(rightX, topY), stroke.width)

                    drawLine(InkBlack, Offset(rightX, topY), Offset(rightX, midY - elemHalf), stroke.width)
                    drawResistorVertical(rightX, midY - elemHalf, midY + elemHalf, 4.2.dp.toPx(), stroke)
                    drawLine(InkBlack, Offset(rightX, midY + elemHalf), Offset(rightX, botY), stroke.width)

                    // Bottom rail
                    drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)

                    // Shared central inductor branch
                    drawLine(InkBlack, Offset(midX, topY), Offset(midX, midY - elemHalf), stroke.width)
                    drawInductorVertical(midX, midY - elemHalf, midY + elemHalf, 5.2.dp.toPx(), accentStroke, bulgeRight = true)
                    drawLine(InkBlack, Offset(midX, midY + elemHalf), Offset(midX, botY), stroke.width)

                    drawNodeDot(Offset(midX, topY))
                    drawNodeDot(Offset(midX, botY))
                    drawNodeDot(Offset(leftX, topY))
                    drawNodeDot(Offset(rightX, topY))

                    // Subtle mesh current arc indicators in Loop 1 and Loop 2
                    val loopR = 8.dp.toPx()
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
                }

                CircuitTopology.THREE_PHASE_DELTA_Y -> {
                    // Left Delta (Δ) loop
                    val dA = Offset(w * 0.21f, h * 0.22f)
                    val dB = Offset(w * 0.21f, h * 0.78f)
                    val dC = Offset(w * 0.38f, h * 0.50f)
                    drawLine(InkBlack, dA, dB, stroke.width)
                    drawLine(OrangeAccent, dB, dC, accentStroke.width)
                    drawLine(OrangeAccent, dC, dA, accentStroke.width)
                    drawNodeDot(dA, ringColor = OrangeAccent)
                    drawNodeDot(dB, ringColor = OrangeAccent)
                    drawNodeDot(dC, ringColor = OrangeAccent)
                    drawCenterLabel(textMeasurer, "Δ", Offset(w * 0.27f, h * 0.50f), InkBlack)

                    // Center equivalence arrow / transfer link
                    val arrX1 = w * 0.45f
                    val arrX2 = w * 0.54f
                    drawLine(InkBlack.copy(alpha = 0.5f), Offset(arrX1, h * 0.50f), Offset(arrX2, h * 0.50f), stroke.width)
                    drawArrowHead(Offset(arrX2, h * 0.50f), 0f, 4.dp.toPx(), OrangeAccent, accentStroke.width)

                    // Right Wye (Y) star
                    val yCenter = Offset(w * 0.71f, h * 0.50f)
                    val yA = Offset(w * 0.61f, h * 0.22f)
                    val yB = Offset(w * 0.61f, h * 0.78f)
                    val yC = Offset(w * 0.85f, h * 0.50f)
                    drawLine(OrangeAccent, yCenter, yA, accentStroke.width)
                    drawLine(OrangeAccent, yCenter, yB, accentStroke.width)
                    drawLine(InkBlack, yCenter, yC, stroke.width)
                    drawNodeDot(yCenter)
                    drawNodeDot(yA, ringColor = OrangeAccent)
                    drawNodeDot(yB, ringColor = OrangeAccent)
                    drawNodeDot(yC, ringColor = OrangeAccent)
                    drawCenterLabel(textMeasurer, "Y", Offset(w * 0.77f, h * 0.30f), OrangeAccent)
                }

                CircuitTopology.TWO_PORT_NETWORK -> {
                    val boxLeft = w * 0.34f
                    val boxWidth = w * 0.28f
                    val loadX = w * 0.82f
                    val port2TapX = w * 0.71f

                    // Port 1 terminals & leads
                    drawLine(InkBlack, Offset(leftX + 3.5.dp.toPx(), topY), Offset(boxLeft, topY), stroke.width)
                    drawLine(InkBlack, Offset(leftX + 3.5.dp.toPx(), botY), Offset(boxLeft, botY), stroke.width)
                    drawArrowHead(Offset(w * 0.25f, topY), 0f, 4.dp.toPx(), OrangeAccent, accentStroke.width)
                    drawNodeDot(Offset(leftX, topY), radius = 3.5.dp.toPx())
                    drawNodeDot(Offset(leftX, botY), radius = 3.5.dp.toPx())

                    // Central [Y] / 2-Port block
                    drawRoundRect(
                        color = OrangeSoftBg,
                        topLeft = Offset(boxLeft, topY - 4.dp.toPx()),
                        size = Size(boxWidth, (botY - topY) + 8.dp.toPx()),
                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                    )
                    drawRoundRect(
                        color = OrangeAccent,
                        topLeft = Offset(boxLeft, topY - 4.dp.toPx()),
                        size = Size(boxWidth, (botY - topY) + 8.dp.toPx()),
                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                        style = accentStroke
                    )
                    drawCenterLabel(textMeasurer, "[Y]", Offset(boxLeft + boxWidth / 2f, midY), InkBlack)

                    // Port 2 terminals feeding shunt load
                    drawLine(InkBlack, Offset(boxLeft + boxWidth, topY), Offset(loadX, topY), stroke.width)
                    drawLine(InkBlack, Offset(boxLeft + boxWidth, botY), Offset(loadX, botY), stroke.width)
                    drawNodeDot(Offset(port2TapX, topY), radius = 3.2.dp.toPx(), ringColor = OrangeAccent)
                    drawNodeDot(Offset(port2TapX, botY), radius = 3.2.dp.toPx(), ringColor = OrangeAccent)

                    val loadHalf = 12.dp.toPx()
                    drawLine(InkBlack, Offset(loadX, topY), Offset(loadX, midY - loadHalf), stroke.width)
                    drawResistorVertical(loadX, midY - loadHalf, midY + loadHalf, 4.2.dp.toPx(), stroke)
                    drawLine(InkBlack, Offset(loadX, midY + loadHalf), Offset(loadX, botY), stroke.width)
                }

                CircuitTopology.CONTOUR_SPLANE -> {
                    val cx = w * 0.50f
                    val cy = h * 0.50f
                    drawLine(InkBlack.copy(alpha = 0.35f), Offset(w * 0.16f, cy), Offset(w * 0.84f, cy), 1.2.dp.toPx())
                    drawLine(InkBlack.copy(alpha = 0.35f), Offset(cx, h * 0.08f), Offset(cx, h * 0.92f), 1.2.dp.toPx())

                    val r = h * 0.36f
                    val shift = (problem.id.hashCode() % 3) * 0.04f
                    val contourCenter = Offset(cx - w * (0.04f + shift), cy)
                    drawCircle(
                        color = OrangeAccent,
                        radius = r,
                        center = contourCenter,
                        style = accentStroke
                    )

                    // Orbiting contour traversal dot
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
                }

                CircuitTopology.GEOMETRIC_PLANE -> {
                    val ox = w * 0.46f
                    val oy = h * 0.54f
                    drawLine(InkBlack.copy(alpha = 0.3f), Offset(w * 0.14f, oy), Offset(w * 0.86f, oy), 1.2.dp.toPx())
                    drawLine(InkBlack.copy(alpha = 0.3f), Offset(ox, h * 0.10f), Offset(ox, h * 0.90f), 1.2.dp.toPx())

                    val len = h * 0.36f
                    val displayAngleDeg = animatedPhaseDeg.coerceIn(-179f, 179f)
                    val rad = Math.toRadians(displayAngleDeg.toDouble())
                    val v1 = Offset(ox + len * 1.25f, oy - h * 0.02f)
                    val v2 = Offset(
                        ox + (len * cos(rad)).toFloat(),
                        (oy - (len * sin(rad)).toFloat()).coerceIn(h * 0.10f, h * 0.90f)
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
                        val arcRadius = 16.dp.toPx()
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
                }
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
            color = Color.White.copy(alpha = 0.06f),
            radius = radius,
            center = Offset(cx, cy)
        )
        drawCircle(
            color = PlaceholderSlate.copy(alpha = 0.35f),
            radius = radius,
            center = Offset(cx, cy),
            style = Stroke(width = 1.dp.toPx())
        )
        drawCircle(
            color = PlaceholderSlate.copy(alpha = 0.18f),
            radius = radius * 0.5f,
            center = Offset(cx, cy),
            style = Stroke(width = 0.8.dp.toPx())
        )

        // Axes
        drawLine(
            color = PlaceholderSlate.copy(alpha = 0.32f),
            start = Offset(cx - radius, cy),
            end = Offset(cx + radius, cy),
            strokeWidth = 0.9.dp.toPx()
        )
        drawLine(
            color = PlaceholderSlate.copy(alpha = 0.32f),
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
                color = OrangeBright,
                start = Offset(cx, cy),
                end = tip,
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawCircle(
                color = OrangeBright.copy(alpha = 0.25f),
                radius = 5.5.dp.toPx(),
                center = tip
            )
            drawCircle(
                color = OrangeBright,
                radius = 3.dp.toPx(),
                center = tip
            )
        }
        drawCircle(
            color = Color.White,
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
    stroke: Stroke
) {
    val gap = 4.5.dp.toPx()
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
    color: Color
) {
    val layout = textMeasurer.measure(
        text = AnnotatedString(label),
        style = TextStyle(
            fontFamily = JetBrainsMonoFontFamily,
            fontSize = 9.5.sp,
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
