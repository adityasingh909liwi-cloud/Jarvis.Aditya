package com.jarvis.app

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    private val viewModel: JarvisViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, 123)
        } else {
            startService(Intent(this, TransparentOverlayService::class.java))
        }

        setContent {
            MainDashboard(viewModel)
        }
    }
}

@Composable
fun MainDashboard(viewModel: JarvisViewModel) {
    val responseText by viewModel.responseText.collectAsState()
    val isUrgent by viewModel.sentimentColorUrgent.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val context = LocalContext.current

    val arcColor = if (isUrgent) Color(0xFFFFAB00) else Color(0xFF00E5FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E17))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "J.A.R.V.I.S. HUD",
            color = Color.White,
            fontSize = 22.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.padding(top = 24.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        val infiniteTransition = rememberInfiniteTransition(label = "")
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(animation = tween(2500, easing = LinearEasing)),
            label = ""
        )

        Canvas(modifier = Modifier.size(100.dp)) {
            drawArc(
                color = arcColor,
                startAngle = rotation,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = 10f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        GlassCard(title = "STATUS", content = "Systems Nominal | Groq Brain Online")
        Spacer(modifier = Modifier.height(10.dp))
        GlassCard(title = "JARVIS RESPONSE", content = responseText)

        Spacer(modifier = Modifier.weight(1f))

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Command JARVIS...", color = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00E5FF),
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (inputText.isNotBlank()) {
                    viewModel.askJarvis(inputText, context)
                    inputText = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("SEND COMMAND", color = Color.Black, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
fun GlassCard(title: String, content: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(text = title, color = Color(0xFF00E5FF), fontSize = 12.sp, fontFamily = FontFamily.Monospace)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = content, color = Color.White, fontSize = 14.sp)
        }
    }
}
