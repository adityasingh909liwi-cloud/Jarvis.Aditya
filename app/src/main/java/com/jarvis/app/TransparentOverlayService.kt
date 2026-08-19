package com.jarvis.app

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp

class TransparentOverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: ComposeView

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val params = WindowManager.LayoutParams(
            200, 200,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 30
            y = 100
        }

        overlayView = ComposeView(this).apply {
            setContent {
                val infiniteTransition = rememberInfiniteTransition(label = "Arc")
                val rotation by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(animation = tween(2000, easing = LinearEasing)),
                    label = "rotate"
                )

                Canvas(modifier = Modifier.size(80.dp)) {
                    drawCircle(
                        color = Color(0xFF00E5FF),
                        radius = size.minDimension / 4,
                        style = Stroke(width = 6f)
                    )
                    drawArc(
                        color = Color(0xFF00E5FF),
                        startAngle = rotation,
                        sweepAngle = 240f,
                        useCenter = false,
                        style = Stroke(width = 8f)
                    )
                }
            }
        }

        windowManager.addView(overlayView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::overlayView.isInitialized) windowManager.removeView(overlayView)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
