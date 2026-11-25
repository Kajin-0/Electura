package com.example.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.InkBlack
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SurfaceWhite

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
