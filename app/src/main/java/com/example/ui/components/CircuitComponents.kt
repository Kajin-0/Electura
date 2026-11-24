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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CircuitProblem
import com.example.model.CircuitTopology
import com.example.model.ComplexNum
import com.example.ui.theme.DraftingGridLine
import com.example.ui.theme.HairlineBorder
import com.example.ui.theme.InkBlack
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeBright
import com.example.ui.theme.OrangeSoftBg
import com.example.ui.theme.PlaceholderSlate
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceWhite
import kotlin.math.PI
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
                    drawLine(InkBlack, Offset(lEnd, topY), Offset(cPos - 5.dp.toPx(), topY), stroke.width)
                    drawCapacitor(cPos, topY, h * 0.135f, accentStroke)
                    drawLine(InkBlack, Offset(cPos + 5.dp.toPx(), topY), Offset(rightX, topY), stroke.width)

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
                    drawLine(InkBlack, Offset(rEnd, topY), Offset(cPos - 5.dp.toPx(), topY), stroke.width)
                    drawCapacitor(cPos, topY, h * 0.135f, accentStroke)
                    drawLine(InkBlack, Offset(cPos + 5.dp.toPx(), topY), Offset(rightX, topY), stroke.width)

                    drawNodeDot(Offset(leftX, topY))
                    drawNodeDot(Offset(rightX, topY))
                }

                CircuitTopology.PARALLEL_RC, CircuitTopology.PARALLEL_RLC -> {
                    drawAcSource(Offset(leftX, midY), h * 0.155f, wavePhase, stroke, accentStroke)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(leftX, midY - h * 0.155f), stroke.width)
                    drawLine(InkBlack, Offset(leftX, midY + h * 0.155f), Offset(leftX, botY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, topY), Offset(rightX, topY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)

                    val branch1X = if (topology == CircuitTopology.PARALLEL_RLC) w * 0.42f else w * 0.50f
                    val branch2X = if (topology == CircuitTopology.PARALLEL_RLC) w * 0.64f else rightX
                    drawLine(InkBlack, Offset(branch1X, topY), Offset(branch1X, midY - 14.dp.toPx()), stroke.width)
                    drawRoundRect(
                        color = SurfaceWhite,
                        topLeft = Offset(branch1X - 7.dp.toPx(), midY - 14.dp.toPx()),
                        size = Size(14.dp.toPx(), 28.dp.toPx()),
                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                    )
                    drawRoundRect(
                        color = InkBlack,
                        topLeft = Offset(branch1X - 7.dp.toPx(), midY - 14.dp.toPx()),
                        size = Size(14.dp.toPx(), 28.dp.toPx()),
                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
                        style = stroke
                    )
                    drawLine(InkBlack, Offset(branch1X, midY + 14.dp.toPx()), Offset(branch1X, botY), stroke.width)
                    drawNodeDot(Offset(branch1X, topY))
                    drawNodeDot(Offset(branch1X, botY))

                    if (topology == CircuitTopology.PARALLEL_RLC) {
                        drawLine(InkBlack, Offset(branch2X, topY), Offset(branch2X, midY - 14.dp.toPx()), stroke.width)
                        drawCircle(color = SurfaceWhite, radius = 12.dp.toPx(), center = Offset(branch2X, midY))
                        drawCircle(
                            color = OrangeAccent,
                            radius = 12.dp.toPx(),
                            center = Offset(branch2X, midY),
                            style = accentStroke
                        )
                        drawLine(InkBlack, Offset(branch2X, midY + 14.dp.toPx()), Offset(branch2X, botY), stroke.width)
                        drawNodeDot(Offset(branch2X, topY))
                        drawNodeDot(Offset(branch2X, botY))
                    }

                    drawLine(InkBlack, Offset(rightX, topY), Offset(rightX, midY - 5.dp.toPx()), stroke.width)
                    drawLine(OrangeAccent, Offset(rightX - 11.dp.toPx(), midY - 5.dp.toPx()), Offset(rightX + 11.dp.toPx(), midY - 5.dp.toPx()), accentStroke.width)
                    drawLine(OrangeAccent, Offset(rightX - 11.dp.toPx(), midY + 5.dp.toPx()), Offset(rightX + 11.dp.toPx(), midY + 5.dp.toPx()), accentStroke.width)
                    drawLine(InkBlack, Offset(rightX, midY + 5.dp.toPx()), Offset(rightX, botY), stroke.width)
                }

                CircuitTopology.COMPLEX_LOAD -> {
                    val sx = leftX + 12.dp.toPx()
                    drawAcSource(Offset(sx, midY), h * 0.155f, wavePhase, stroke, accentStroke)
                    drawLine(InkBlack, Offset(sx, topY), Offset(sx, midY - h * 0.155f), stroke.width)
                    drawLine(InkBlack, Offset(sx, midY + h * 0.155f), Offset(sx, botY), stroke.width)
                    drawLine(InkBlack, Offset(sx, topY), Offset(w * 0.60f, topY), stroke.width)
                    drawLine(InkBlack, Offset(sx, botY), Offset(w * 0.60f, botY), stroke.width)

                    val ax = w * 0.42f
                    drawLine(OrangeAccent, Offset(ax - 12.dp.toPx(), topY), Offset(ax + 10.dp.toPx(), topY), accentStroke.width)
                    drawLine(OrangeAccent, Offset(ax + 3.dp.toPx(), topY - 5.dp.toPx()), Offset(ax + 10.dp.toPx(), topY), accentStroke.width)
                    drawLine(OrangeAccent, Offset(ax + 3.dp.toPx(), topY + 5.dp.toPx()), Offset(ax + 10.dp.toPx(), topY), accentStroke.width)

                    // Subtle traveling signal pulse dot along top feed wire
                    val pulseFrac = wavePhase / (2f * PI.toFloat())
                    val pulseX = sx + (w * 0.60f - sx) * pulseFrac
                    drawCircle(color = OrangeAccent.copy(alpha = 0.85f), radius = 2.8.dp.toPx(), center = Offset(pulseX, topY))

                    val boxLeft = w * 0.60f
                    val boxWidth = w * 0.22f
                    drawLine(InkBlack, Offset(boxLeft + boxWidth / 2f, topY), Offset(boxLeft + boxWidth / 2f, midY - 16.dp.toPx()), stroke.width)
                    drawLine(InkBlack, Offset(boxLeft + boxWidth / 2f, midY + 16.dp.toPx()), Offset(boxLeft + boxWidth / 2f, botY), stroke.width)
                    drawRoundRect(
                        color = OrangeSoftBg,
                        topLeft = Offset(boxLeft, midY - 16.dp.toPx()),
                        size = Size(boxWidth, 32.dp.toPx()),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )
                    drawRoundRect(
                        color = OrangeAccent,
                        topLeft = Offset(boxLeft, midY - 16.dp.toPx()),
                        size = Size(boxWidth, 32.dp.toPx()),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
                        style = accentStroke
                    )
                }

                CircuitTopology.LOW_PASS_RC -> {
                    val rStart = w * 0.25f
                    val rEnd = w * 0.48f
                    val tapX = w * 0.62f
                    drawLine(InkBlack, Offset(leftX + 4.dp.toPx(), topY), Offset(rStart, topY), stroke.width)
                    drawResistor(rStart, rEnd, topY, h * 0.085f, stroke)
                    drawLine(InkBlack, Offset(rEnd, topY), Offset(rightX - 4.dp.toPx(), topY), stroke.width)
                    drawNodeDot(Offset(leftX, topY), radius = 4.dp.toPx())
                    drawNodeDot(Offset(rightX, topY), radius = 4.dp.toPx(), ringColor = OrangeAccent)

                    drawLine(InkBlack, Offset(tapX, topY), Offset(tapX, midY - 5.dp.toPx()), stroke.width)
                    drawLine(OrangeAccent, Offset(tapX - 11.dp.toPx(), midY - 5.dp.toPx()), Offset(tapX + 11.dp.toPx(), midY - 5.dp.toPx()), accentStroke.width)
                    drawLine(OrangeAccent, Offset(tapX - 11.dp.toPx(), midY + 5.dp.toPx()), Offset(tapX + 11.dp.toPx(), midY + 5.dp.toPx()), accentStroke.width)
                    drawLine(InkBlack, Offset(tapX, midY + 5.dp.toPx()), Offset(tapX, botY), stroke.width)
                    drawLine(InkBlack, Offset(leftX, botY), Offset(rightX, botY), stroke.width)
                    drawNodeDot(Offset(tapX, topY))
                    drawNodeDot(Offset(tapX, botY))
                }

                CircuitTopology.FEEDBACK_LOOP -> {
                    val sumCenter = Offset(w * 0.24f, topY + 10.dp.toPx())
                    val sy = sumCenter.y
                    drawLine(InkBlack, Offset(w * 0.10f, sy), Offset(sumCenter.x - 10.dp.toPx(), sy), stroke.width)
                    drawCircle(color = SurfaceWhite, radius = 10.dp.toPx(), center = sumCenter)
                    drawCircle(InkBlack, radius = 10.dp.toPx(), center = sumCenter, style = stroke)
                    drawLine(InkBlack, Offset(sumCenter.x - 4.dp.toPx(), sy), Offset(sumCenter.x + 4.dp.toPx(), sy), stroke.width)
                    drawLine(InkBlack, Offset(sumCenter.x, sy - 4.dp.toPx()), Offset(sumCenter.x, sy + 4.dp.toPx()), stroke.width)

                    val gLeft = w * 0.42f
                    val gWidth = w * 0.24f
                    drawLine(InkBlack, Offset(sumCenter.x + 10.dp.toPx(), sy), Offset(gLeft, sy), stroke.width)
                    drawRoundRect(
                        color = OrangeSoftBg,
                        topLeft = Offset(gLeft, sy - 14.dp.toPx()),
                        size = Size(gWidth, 28.dp.toPx()),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )
                    drawRoundRect(
                        color = OrangeAccent,
                        topLeft = Offset(gLeft, sy - 14.dp.toPx()),
                        size = Size(gWidth, 28.dp.toPx()),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
                        style = accentStroke
                    )

                    val tapX = w * 0.78f
                    drawLine(InkBlack, Offset(gLeft + gWidth, sy), Offset(w * 0.90f, sy), stroke.width)
                    drawLine(InkBlack, Offset(tapX, sy), Offset(tapX, botY), stroke.width)
                    drawLine(InkBlack, Offset(tapX, botY), Offset(sumCenter.x, botY), stroke.width)
                    drawLine(InkBlack, Offset(sumCenter.x, botY), Offset(sumCenter.x, sy + 10.dp.toPx()), stroke.width)
                    drawNodeDot(Offset(tapX, sy))

                    val hLeft = w * 0.44f
                    val hWidth = w * 0.20f
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
                    val ox = w * 0.36f
                    val oy = h * 0.72f
                    drawLine(InkBlack.copy(alpha = 0.3f), Offset(w * 0.14f, oy), Offset(w * 0.86f, oy), 1.2.dp.toPx())
                    drawLine(InkBlack.copy(alpha = 0.3f), Offset(ox, h * 0.10f), Offset(ox, h * 0.90f), 1.2.dp.toPx())

                    val len = h * 0.54f
                    val rad = Math.toRadians(animatedPhaseDeg.toDouble().coerceIn(-75.0, 80.0))
                    val v1 = Offset(ox + len * 1.15f, oy - h * 0.04f)
                    val v2 = Offset(
                        ox + (len * cos(rad)).toFloat(),
                        oy - (len * sin(rad)).toFloat().coerceAtLeast(-h * 0.16f)
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

                    val sweep = animatedPhaseDeg.coerceIn(15f, 80f)
                    drawArc(
                        color = OrangeAccent,
                        startAngle = -sweep,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(ox - 20.dp.toPx(), oy - 20.dp.toPx()),
                        size = Size(40.dp.toPx(), 40.dp.toPx()),
                        style = accentStroke
                    )
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
