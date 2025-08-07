package com.vompom.typewriter.mingkwai_typewriter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 *
 * Created by @juliswang on 2025/08/05 21:18
 *
 * @Description 2 test keyboards to simulate mingkwai keyboard.
 */
@Composable
fun KeyboardView() {
    Surface(
        color = Color(0xFFF5F5F5),
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 数字键排 (远端，较小)
            KeyRow(
                keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
                yOffset = 0.dp,
                scaleFactor = 0.8f,
                horizontalPadding = 4.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 第一排字母 (Q W E R T Y U I O P)
            KeyRow(
                keys = listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
                yOffset = 10.dp,
                scaleFactor = 0.9f,
                horizontalPadding = 4.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 第二排字母 (A S D F G H J K L)
            KeyRow(
                keys = listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
                yOffset = 20.dp,
                scaleFactor = 1.0f,
                horizontalPadding = 4.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 第三排字母 (Z X C V B N M)
            KeyRow(
                keys = listOf("Z", "X", "C", "V", "B", "N", "M"),
                yOffset = 30.dp,
                scaleFactor = 1.1f,
                horizontalPadding = 4.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 底部功能键 (空格、Shift等，最大)
            KeyRow(
                keys = listOf("Shift", "Ctrl", "Alt", "Space", "Enter"),
                yOffset = 40.dp,
                scaleFactor = 1.2f,
                horizontalPadding = 8.dp,
                isBottomRow = true
            )
        }
    }
}

@Composable
fun KeyRow(
    keys: List<String>,
    yOffset: Dp,
    scaleFactor: Float,
    horizontalPadding: Dp,
    isBottomRow: Boolean = false
) {
    val keyWidth = if (isBottomRow && keys.contains("Space")) {
        // 空格键比较宽
        60.dp
    } else if (isBottomRow) {
        45.dp
    } else {
        35.dp
    }

    Row(
        modifier = Modifier
            .graphicsLayer {
                // 模拟远近：通过 translationY 和 scale 实现“斜视角”
                this.translationY = yOffset.toPx()
                this.scaleX = scaleFactor
                this.scaleY = scaleFactor
                // 可选：alpha 也可以做远近感
                // this.alpha = 0.3f + 0.7f * scaleFactor
            }
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(horizontalPadding)
    ) {
        keys.forEach { keyText ->
            val width = when {
                keyText == "Space" -> keyWidth
                isBottomRow && (keyText == "Shift" || keyText == "Ctrl" || keyText == "Alt" || keyText == "Enter") -> 50.dp
                else -> keyWidth
            }

            KeyButton(
                text = keyText,
                modifier = Modifier
                    .width(width)
                    .height(if (isBottomRow && keyText == "Space") 35.dp else 35.dp)
                    .graphicsLayer {
                        // 进一步增强立体感（可选）
                        shadowElevation = 40f
                    }
            ) {
                // 处理按键逻辑（可扩展）
                println("Pressed: $keyText")
            }
        }
    }
}


@Composable
fun KeyButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp)) // 轻微圆角，也可以为 0.dp 做直角
            .background(
                if (isPressed) Color(0xFFD5D5D5) else Color(0xFFF8F8F8) // 按下时稍微变暗
            )
            .border(
                width = 1.dp,
                color = Color(0xFF888888), // 深灰色边框，模拟塑料或金属质感
                shape = RoundedCornerShape(2.dp)
            )
            .clickable {
                isPressed = true
                onClick()
                // 按下后短暂延迟恢复

            }
            .scale(if (isPressed) 0.25f else 1f) // 按下时缩小，模拟物理按键反馈
            .padding(2.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2D2D2D), // 深色文字，类似墨水
            textAlign = TextAlign.Center,
//            fontFamily = TypewriterFontFamily, // 打字机风格字体
            modifier = Modifier.align(Alignment.Center)
        )
    }
    if (isPressed) {
        Box {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(100)
                isPressed = false
            }
        }
    }
}
