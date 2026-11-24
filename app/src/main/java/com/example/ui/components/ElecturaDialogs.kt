package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AttemptRecord
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.HairlineBorder
import com.example.ui.theme.InkBlack
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateMuted
import com.example.ui.theme.SubtleBorder
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TabularMonoStyle
import com.example.viewmodel.CircuitGameUiState

@Composable
fun AttemptHistoryDialog(
    state: CircuitGameUiState,
    onDismiss: () -> Unit,
    onSelectAttempt: (AttemptRecord) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .paperCardSurface(
                    shape = RoundedCornerShape(22.dp),
                    backgroundColor = SurfaceWhite,
                    borderColor = SubtleBorder,
                    borderWidth = 1.dp,
                    elevation = 6.dp
                )
                .padding(22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircuitTag(text = "Session Log")
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close history",
                        tint = SlateDark,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = "Recent Attempts",
                style = MaterialTheme.typography.headlineMedium,
                color = InkBlack
            )
            Spacer(modifier = Modifier.height(14.dp))

            if (state.recentAttempts.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.History,
                        contentDescription = null,
                        tint = SlateMuted,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No submissions yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = InkBlack
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Solve any problem to record your answers here.",
                        style = MaterialTheme.typography.bodySmall,
                        color = SlateMuted
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.height(280.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.recentAttempts) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .tactilePaperCard(
                                    shape = RoundedCornerShape(14.dp),
                                    backgroundColor = SurfaceWhite,
                                    borderColor = HairlineBorder,
                                    borderWidth = 0.85.dp,
                                    elevation = 1.dp,
                                    pressedScale = 0.98f,
                                    onClick = { onSelectAttempt(item) }
                                )
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(
                                        if (item.isCorrect) SuccessGreen else ErrorRed,
                                        CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${item.problemCode} · ${item.problemTitle}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = InkBlack,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = item.submittedAnswer,
                                    style = TabularMonoStyle,
                                    fontSize = 11.5.sp,
                                    color = SlateMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (item.isCorrect) "+${item.xpEarned} XP" else "Retry",
                                style = TabularMonoStyle,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (item.isCorrect) SuccessGreen else ErrorRed
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResetProgressDialog(
    onDismiss: () -> Unit,
    onConfirmReset: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceWhite,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                "Reset Progress?",
                style = MaterialTheme.typography.titleLarge,
                color = InkBlack
            )
        },
        text = {
            Text(
                "This clears your solved problems, XP, streaks, and accuracy telemetry.",
                style = MaterialTheme.typography.bodyMedium,
                color = SlateDark
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmReset) {
                Text("Reset", fontWeight = FontWeight.Bold, color = ErrorRed)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", fontWeight = FontWeight.SemiBold, color = SlateDark)
            }
        }
    )
}
