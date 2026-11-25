package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CodeSurface
import com.example.ui.theme.HairlineBorder
import com.example.ui.theme.PaperShadowTint
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.SurfaceWhite

/**
 * Shared spring press scaling paired with Material ripple and optional light haptic feedback.
 */
fun Modifier.tactileClickable(
    enabled: Boolean = true,
    pressedScale: Float = 0.97f,
    withHaptic: Boolean = true,
    role: Role? = null,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = 0.65f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "tactileScale"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = LocalIndication.current,
            enabled = enabled,
            role = role,
            onClick = {
                if (withHaptic) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                onClick()
            }
        )
}

/**
 * Applies tactile press scaling driven by an external InteractionSource (for Button / IconButton).
 */
fun Modifier.springPressScale(
    interactionSource: MutableInteractionSource,
    pressedScale: Float = 0.97f
): Modifier = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = 0.65f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "springPressScale"
    )
    this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

/**
 * Layered Swiss paper surface with subtle warm ambient shadow and crisp hairline border.
 */
fun Modifier.paperCardSurface(
    shape: Shape = RoundedCornerShape(18.dp),
    backgroundColor: Color = SurfaceWhite,
    borderColor: Color = SubtleBorder,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 2.dp
): Modifier = this
    .shadow(
        elevation = elevation,
        shape = shape,
        clip = false,
        ambientColor = PaperShadowTint.copy(alpha = 0.18f),
        spotColor = PaperShadowTint.copy(alpha = 0.14f)
    )
    .clip(shape)
    .background(backgroundColor, shape)
    .border(borderWidth, borderColor, shape)

/**
 * Clickable layered paper card that subtly depresses its elevation and scales on press.
 */
fun Modifier.tactilePaperCard(
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = SurfaceWhite,
    borderColor: Color = SubtleBorder,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 2.dp,
    pressedScale: Float = 0.985f,
    withHaptic: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = 0.65f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "paperCardScale"
    )
    val animatedElevation by animateDpAsState(
        targetValue = if (isPressed) elevation * 0.4f else elevation,
        animationSpec = tween(120),
        label = "paperCardElevation"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .shadow(
            elevation = animatedElevation,
            shape = shape,
            clip = false,
            ambientColor = PaperShadowTint.copy(alpha = 0.18f),
            spotColor = PaperShadowTint.copy(alpha = 0.14f)
        )
        .clip(shape)
        .background(backgroundColor, shape)
        .border(borderWidth, borderColor, shape)
        .clickable(
            interactionSource = interactionSource,
            indication = LocalIndication.current,
            onClick = {
                if (withHaptic) {
                    haptic.tickLight()
                }
                onClick()
            }
        )
}

/**
 * Recessed drafting-well inset surface for formulas, given parameters, and reference trays.
 */
fun Modifier.recessedPaperWell(
    shape: Shape = RoundedCornerShape(14.dp),
    backgroundColor: Color = CodeSurface,
    borderColor: Color = HairlineBorder
): Modifier = this
    .clip(shape)
    .background(backgroundColor, shape)
    .border(0.85.dp, borderColor, shape)

/**
 * Softly fades overflowing edges of horizontally scrollable strips into the underlying surface.
 */
fun Modifier.horizontalEdgeFade(
    scrollState: ScrollState,
    fadeWidth: Dp = 16.dp
): Modifier = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        val fadeLeft = scrollState.canScrollBackward
        val fadeRight = scrollState.canScrollForward
        if (!fadeLeft && !fadeRight) return@drawWithContent
        val fadePx = fadeWidth.toPx().coerceAtMost(size.width * 0.25f)
        if (fadePx <= 0f) return@drawWithContent
        if (fadeLeft) {
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startX = 0f,
                    endX = fadePx
                ),
                topLeft = Offset.Zero,
                size = Size(fadePx, size.height),
                blendMode = BlendMode.DstIn
            )
        }
        if (fadeRight) {
            val startX = size.width - fadePx
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Black, Color.Transparent),
                    startX = startX,
                    endX = size.width
                ),
                topLeft = Offset(startX, 0f),
                size = Size(fadePx, size.height),
                blendMode = BlendMode.DstIn
            )
        }
    }

fun HapticFeedback.tickLight() {
    performHapticFeedback(HapticFeedbackType.TextHandleMove)
}

fun HapticFeedback.pulseConfirm() {
    performHapticFeedback(HapticFeedbackType.LongPress)
}

@Composable
fun rememberAnimatedInt(
    targetValue: Int,
    durationMillis: Int = 520,
    label: String = "animatedInt"
): Int {
    val animated by animateIntAsState(
        targetValue = targetValue,
        animationSpec = tween(
            durationMillis = durationMillis,
            easing = FastOutSlowInEasing
        ),
        label = label
    )
    return animated
}
