package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.domain.SchematicAnnotationResolver
import com.example.model.CircuitProblem
import com.example.ui.theme.CodeSurface
import com.example.ui.theme.CopperBorderSoft
import com.example.ui.theme.HairlineBorder
import com.example.ui.theme.InkBlack
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.OrangeSoftBg
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TabularMonoStyle
import java.util.Locale

private const val MIN_ZOOM = 1.0f
private const val MAX_ZOOM = 3.2f
private const val ZOOM_STEP = 0.5f

@Composable
fun ExpandedSchematicDialog(
    problem: CircuitProblem,
    onDismiss: () -> Unit
) {
    val annotations = remember(problem) { SchematicAnnotationResolver.resolve(problem) }
    var targetScale by remember(problem.id) { mutableFloatStateOf(1f) }
    var targetOffset by remember(problem.id) { mutableStateOf(Offset.Zero) }
    var viewportSize by remember { mutableStateOf(IntSize.Zero) }

    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMedium),
        label = "expandedSchematicScale"
    )
    val animatedOffset by animateOffsetAsState(
        targetValue = targetOffset,
        animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMedium),
        label = "expandedSchematicOffset"
    )

    fun clampOffset(raw: Offset, scale: Float): Offset {
        if (scale <= 1.01f || viewportSize.width == 0 || viewportSize.height == 0) {
            return Offset.Zero
        }
        val maxX = (viewportSize.width * (scale - 1f)) / 2f
        val maxY = (viewportSize.height * (scale - 1f)) / 2f
        return Offset(
            x = raw.x.coerceIn(-maxX, maxX),
            y = raw.y.coerceIn(-maxY, maxY)
        )
    }

    fun applyScale(nextScale: Float) {
        val clamped = nextScale.coerceIn(MIN_ZOOM, MAX_ZOOM)
        targetScale = clamped
        targetOffset = clampOffset(targetOffset, clamped)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 720.dp)
                    .paperCardSurface(
                        shape = RoundedCornerShape(22.dp),
                        backgroundColor = SurfaceWhite,
                        borderColor = SubtleBorder,
                        borderWidth = 1.dp,
                        elevation = 8.dp
                    )
                    .padding(18.dp)
                    .testTag("expanded_schematic_dialog")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircuitTag(text = annotations.topologyTitle)
                        Text(
                            text = if (problem.isProcedural) "Drill" else problem.code,
                            style = TabularMonoStyle,
                            fontSize = 11.sp,
                            color = SlateMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("expanded_schematic_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.cd_close_schematic),
                            tint = SlateDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Text(
                    text = problem.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = InkBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Zoomable & pannable drafting viewport
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(276.dp)
                        .recessedPaperWell(
                            shape = RoundedCornerShape(16.dp),
                            backgroundColor = SurfaceElevated,
                            borderColor = HairlineBorder
                        )
                        .clipToBounds()
                        .onSizeChanged { viewportSize = it }
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                val nextScale = (targetScale * zoom).coerceIn(MIN_ZOOM, MAX_ZOOM)
                                targetScale = nextScale
                                targetOffset = clampOffset(targetOffset + pan, nextScale)
                            }
                        }
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    if (targetScale > 1.25f) {
                                        targetScale = MIN_ZOOM
                                        targetOffset = Offset.Zero
                                    } else {
                                        targetScale = 2.0f
                                        targetOffset = Offset.Zero
                                    }
                                }
                            )
                        }
                ) {
                    CircuitSchematicCanvas(
                        problem = problem,
                        annotations = annotations,
                        isExpanded = true,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = animatedScale
                                scaleY = animatedScale
                                translationX = animatedOffset.x
                                translationY = animatedOffset.y
                            }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Accessible zoom toolbar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.label_zoom_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { applyScale(targetScale - ZOOM_STEP) },
                            enabled = targetScale > MIN_ZOOM + 0.01f,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .background(CodeSurface, RoundedCornerShape(10.dp))
                                .border(0.85.dp, HairlineBorder, RoundedCornerShape(10.dp))
                                .testTag("schematic_zoom_out_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Remove,
                                contentDescription = stringResource(R.string.cd_zoom_out),
                                tint = if (targetScale > MIN_ZOOM + 0.01f) InkBlack else SlateMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .background(OrangeSoftBg, RoundedCornerShape(10.dp))
                                .border(1.dp, CopperBorderSoft, RoundedCornerShape(10.dp))
                                .tactileClickable(
                                    pressedScale = 0.96f,
                                    role = Role.Button,
                                    onClick = {
                                        targetScale = MIN_ZOOM
                                        targetOffset = Offset.Zero
                                    }
                                )
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                .testTag("schematic_zoom_reset_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = String.format(Locale.US, "%.1f×", targetScale),
                                style = TabularMonoStyle,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = OrangeAccent
                            )
                        }

                        IconButton(
                            onClick = { applyScale(targetScale + ZOOM_STEP) },
                            enabled = targetScale < MAX_ZOOM - 0.01f,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .background(CodeSurface, RoundedCornerShape(10.dp))
                                .border(0.85.dp, HairlineBorder, RoundedCornerShape(10.dp))
                                .testTag("schematic_zoom_in_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = stringResource(R.string.cd_zoom_in),
                                tint = if (targetScale < MAX_ZOOM - 0.01f) InkBlack else SlateMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Full Given-Parameters Legend
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .recessedPaperWell(
                            shape = RoundedCornerShape(12.dp),
                            backgroundColor = CodeSurface,
                            borderColor = HairlineBorder
                        )
                        .padding(horizontal = 12.dp, vertical = 9.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = problem.parametersLine1,
                        style = TabularMonoStyle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = InkBlack
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = problem.parametersLine2,
                        style = TabularMonoStyle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeAccent
                    )
                }
            }
        }
    }
}
