package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          CircuitApp(modifier = Modifier.padding(innerPadding))
        }
      }
    }
  }
}

data class Problem(
  val title: String,
  val description: String,
  val correctAnswer: String,
  val hint: String
)

val problems = listOf(
  Problem(
    "LCR Series Resonance",
    "An LCR series circuit has R = 10 Ω, L = 0.1 H, and C = 10 μF. It is connected to a 100 V AC source of variable frequency. Calculate the Q-factor of the circuit at resonance.",
    "10",
    "Q = (1/R) * √(L/C)"
  ),
  Problem(
    "Complex Power",
    "A load draws a complex power of S = (800 + j600) VA at 200 V rms. What is the power factor of the load?",
    "0.8",
    "pf = P / |S|"
  ),
  Problem(
    "Contour Integration - Laplace",
    "Evaluate the inverse Laplace transform of F(s) = 1 / ((s²+1)(s²+4)) at t = π seconds using contour integration.",
    "0",
    "Use residue theorem. Poles are at ±j, ±2j."
  ),
  Problem(
    "RC Circuit Transfer Function",
    "Find the magnitude of the transfer function |H(jω)| for a simple RC low-pass filter at the cutoff frequency ωc = 1/RC.",
    "0.707",
    "1 / √2"
  ),
  Problem(
    "Nyquist Plot Stability",
    "A system has an open-loop transfer function G(s)H(s) = K / (s(s+1)(s+2)). What is the critical positive value of K for marginal stability?",
    "6",
    "Characteristic equation: s³ + 3s² + 2s + K = 0. Use Routh-Hurwitz."
  )
)

fun Modifier.brutalShadow(
    offsetDp: Float = 4f,
    cornerRadiusDp: Float = 24f,
    color: Color = Color.Black
) = this.drawBehind {
    val pxOffset = offsetDp.dp.toPx()
    val pxCorner = cornerRadiusDp.dp.toPx()
    drawRoundRect(
        color = color,
        topLeft = Offset(pxOffset, pxOffset),
        size = size,
        cornerRadius = CornerRadius(pxCorner, pxCorner)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircuitApp(modifier: Modifier = Modifier) {
  var userAnswer by remember { mutableStateOf("") }
  val interactionSource = remember { MutableInteractionSource() }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFFAFAFA))
  ) {
    // Top Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 24.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Black, modifier = Modifier.size(28.dp))
      Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Icon(Icons.Filled.History, contentDescription = "History", tint = Color.Black, modifier = Modifier.size(28.dp))
        Icon(Icons.Filled.MoreVert, contentDescription = "More", tint = Color.Black, modifier = Modifier.size(28.dp))
      }
    }

    // Expanding content area
    Column(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
      // Tags
      Box(
        modifier = Modifier
          .padding(bottom = 8.dp)
          .background(Color(0xFFFFF7ED), RoundedCornerShape(4.dp))
          .padding(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Text(
          text = "LCR NETWORK ANALYSIS",
          style = MaterialTheme.typography.labelSmall,
          color = Color(0xFFEA580C)
        )
      }

      // Heading
      Text(
        text = "Problem\nNo. 492B",
        style = MaterialTheme.typography.displayLarge,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 24.dp)
      )

      // Brutalist Card
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(bottom = 24.dp)
          .brutalShadow(offsetDp = 4f, cornerRadiusDp = 24f, color = Color.Black)
          .background(Color.White, RoundedCornerShape(24.dp))
          .border(2.dp, Color.Black, RoundedCornerShape(24.dp))
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = "Solve for current i(t) in a series circuit where:",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Medium,
          color = Color(0xFF64748B),
          modifier = Modifier.padding(bottom = 8.dp)
        )

        // Monospace code block
        Column(
          modifier = Modifier
            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            .padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(text = "L = 0.5H, C = 0.02F, R = 10Ω", fontFamily = FontFamily.Monospace, fontSize = 16.sp, color = Color.Black)
          Text(text = "E(t) = 100 sin(10t)", fontFamily = FontFamily.Monospace, fontSize = 16.sp, color = Color(0xFFEA580C))
        }

        Spacer(modifier = Modifier.padding(12.dp))

        // Time indicators
        Row(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("T = 0+", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
          Text("STEADY STATE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA580C), fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
        }
        
        Spacer(modifier = Modifier.padding(4.dp))
        Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(Color.Black, RoundedCornerShape(50)))
        
        Spacer(modifier = Modifier.padding(16.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "INTEGRATION PATH",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            letterSpacing = 1.sp
          )
          Text(
            text = "Use Bromwich contour for the inverse Laplace transform.",
            fontSize = 12.sp,
            color = Color(0xFF475569),
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
          )
        }
      }

      // Input Field
      BasicTextField(
        value = userAnswer,
        onValueChange = { userAnswer = it },
        textStyle = TextStyle(
          fontFamily = FontFamily.Monospace,
          fontSize = 18.sp,
          color = Color.Black
        ),
        singleLine = true,
        cursorBrush = SolidColor(Color(0xFFEA580C)),
        decorationBox = { innerTextField ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(56.dp)
              .background(Color.White, RoundedCornerShape(16.dp))
              .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
              .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(Modifier.weight(1f)) {
              if (userAnswer.isEmpty()) {
                Text(
                  text = "Enter complex result (A + jB)",
                  fontFamily = FontFamily.Monospace,
                  fontSize = 16.sp,
                  color = Color(0xFF94A3B8),
                  fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
              }
              innerTextField()
            }
            Icon(Icons.Filled.Functions, contentDescription = null, tint = Color(0xFFEA580C))
          }
        }
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Submit Button
      Button(
        onClick = { /* Check Answer */ },
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.Black,
          contentColor = Color.White
        )
      ) {
        Row(
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "SUBMIT SOLUTION",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
          Spacer(modifier = Modifier.width(12.dp))
          Icon(Icons.Filled.Send, contentDescription = "Send", modifier = Modifier.size(20.dp))
        }
      }
    }

    // Bottom Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .border(1.dp, Color(0xFFE2E8F0))
        .padding(vertical = 16.dp, horizontal = 24.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      BottomIcon(icon = Icons.Filled.GridView, label = "Index", isActive = false)
      BottomIcon(icon = Icons.Filled.EditNote, label = "Solve", isActive = true)
      BottomIcon(icon = Icons.Filled.Analytics, label = "Stats", isActive = false)
      BottomIcon(icon = Icons.Filled.PersonSearch, label = "Guide", isActive = false)
    }
  }
}

@Composable
fun BottomIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isActive: Boolean) {
  val color = if (isActive) Color(0xFFEA580C) else Color.Black.copy(alpha = 0.4f)
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(28.dp))
    Text(
      text = label,
      fontSize = 10.sp,
      fontWeight = FontWeight.Bold,
      color = color,
      modifier = Modifier.padding(top = 4.dp)
    )
  }
}

@Preview(showBackground = true)
@Composable
fun CircuitAppPreview() {
  MyApplicationTheme {
    CircuitApp()
  }
}
