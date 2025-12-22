package org.example.project.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        WavyGradientBackground()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            EunoraLogo(modifier = Modifier.size(120.dp))

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Eunora",
                fontSize = 56.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF134E4A)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Let your\nfeelings breathe.",
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF134E4A),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) {
                 Box(
                     modifier = Modifier
                         .size(10.dp)
                         .background(Color(0xFFFEF3C7), shape = androidx.compose.foundation.shape.CircleShape) // Pale yellow dots
                 )
            }
        }
    }
}

@Composable
fun WavyGradientBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF80CBC4),
                    Color(0xFFFFCC80)
                )
            )
        )

        val path1 = Path().apply {
            moveTo(0f, height * 0.3f)
            cubicTo(
                width * 0.4f, height * 0.2f,
                width * 0.6f, height * 0.5f,
                width, height * 0.4f
            )
            lineTo(width, 0f)
            lineTo(0f, 0f)
            close()
        }
        drawPath(path1, color = Color(0xFF4DB6AC).copy(alpha = 0.3f))

        val path2 = Path().apply {
            moveTo(0f, height)
            cubicTo(
                width * 0.3f, height * 0.7f,
                width * 0.7f, height * 0.8f,
                width, height * 0.6f
            )
            lineTo(width, height)
            close()
        }
        drawPath(path2, color = Color(0xFFFFB74D).copy(alpha = 0.3f))
        
        val path3 = Path().apply {
            moveTo(0f, 0f)
            lineTo(width, 0f)
            lineTo(width, height)
            cubicTo(
                 width * 0.5f, height * 0.8f,
                 0f, height * 0.5f,
                 0f, height
            )
            close()
        }
    }
}

@Composable
fun EunoraLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        
        drawPath(
            path = Path().apply {
                moveTo(w * 0.5f, h * 0.8f) // Base
                quadraticBezierTo(w * 0.1f, h * 0.5f, w * 0.3f, h * 0.2f)
                quadraticBezierTo(w * 0.6f, h * 0.3f, w * 0.5f, h * 0.8f)
            },
            color = Color(0xFF26A69A)
        )
        
        drawPath(
            path = Path().apply {
                moveTo(w * 0.5f, h * 0.8f) 
                quadraticBezierTo(w * 0.4f, h * 0.3f, w * 0.5f, h * 0.1f)
                quadraticBezierTo(w * 0.6f, h * 0.3f, w * 0.5f, h * 0.8f)
            },
            color = Color(0xFF4DB6AC)
        )
        
        drawPath(
            path = Path().apply {
                moveTo(w * 0.5f, h * 0.8f) 
                quadraticBezierTo(w * 0.9f, h * 0.6f, w * 0.8f, h * 0.3f)
                quadraticBezierTo(w * 0.6f, h * 0.5f, w * 0.5f, h * 0.8f)
            },
            color = Color(0xFFFFCC80)
        )
    }
}
