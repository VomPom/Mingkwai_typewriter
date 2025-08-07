package com.vompom.typewriter.mingkwai_typewriter.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 *
 * Created by @juliswang on 2025/08/05 19:15
 *
 * @Description 2 test keyboards to simulate mingkwai keyboard.
 */
@Composable
fun BoxScope.PerspectiveKeyboard() {
    BoxWithConstraints(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .graphicsLayer {
                rotationX = 10f // 倾斜角度
                shadowElevation = 0.1f
            }
            .fillMaxWidth(0.7f)
    ) {
        // 键盘主体
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xff5a5aa9), Color(0xFF1C1C2C))
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 25.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF606070),
                            Color(0xFF303040)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(vertical = 24.dp, horizontal = 12.dp)
        ) {

            // 顶部数字行（更小、更远）
            KeyboardRow(
                keys = listOf("`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "⌫"),
                scale = 0.85f, // 缩小尺寸
                offsetY = 10.dp // 上移
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 第一行字母键
            KeyboardRow(
                keys = listOf("⇥", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\"),
                scale = 0.92f,
                offsetY = 5.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 第二行字母键
            KeyboardRow(
                keys = listOf("⇪", "A", "S", "D", "F", "G", "H", "J", "K", "L", ";", "'", "⏎"),
                scale = 0.96f,
                offsetY = 2.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 第三行字母键（最近）
            KeyboardRow(
                keys = listOf("⇧", "Z", "X", "C", "V", "B", "N", "M", ",", ".", "/", "⇧"),
                scale = 0.96f,
                offsetY = 0.dp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 底部功能键
//            KeyboardRow(
//                keys = listOf("Ctrl", "Fn", "⌘", "⎵", "⌘", "Alt", "→"),
//                scale = 1f,
//                offsetY = 0.dp
//            )
        }

        // 键盘底部反射效果
//        Canvas(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(30.dp)
//                .align(Alignment.BottomCenter)
//                .offset(y = 10.dp)
//        ) {
//            drawRect(
//                brush = Brush.verticalGradient(
//                    colors = listOf(Color(0x55FFFFFF), Color(0x00FFFFFF)),
//                    startY = 0f,
//                    endY = size.height
//                ),
//                topLeft = Offset(0f, 0f),
//                size = Size(size.width, size.height)
//            )
//        }
    }
}

@Composable
fun KeyboardRow(
    keys: List<String>,
    scale: Float,
    offsetY: Dp,
    keyWidth: Float = 1f
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationY = offsetY.value * density
            }
            .padding(horizontal = if (scale < 1f) ((1 - scale).times(30).dp) else 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        keys.forEach { key ->
            KeyButton2(
                key = key,
                modifier = Modifier.weight(keyWidth)
            ) {}
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
fun KeyButton(
    key: String,
    modifier: Modifier = Modifier,
) {
    var isPressed by remember { mutableStateOf(false) }
    val pressProgress by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        label = "keyPress"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .height(48.dp)
            .clip(RoundedCornerShape(6.dp))
            .graphicsLayer {
//                translationZ = if (isPressed) -2f else 0f
                rotationX = if (isPressed) 5f else 0f
                shadowElevation = if (isPressed) 0f else 4f
            }
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = {

                }
            )
            .drawWithContent {
                // 按键顶部
                drawRect(
                    brush = if (isPressed) {
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF505060), Color(0xFF404050))
                        )
                    } else {
                        Brush.linearGradient(
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            colors = listOf(Color(0xFF505060), Color(0xFF303040))
                        )
                    },
                    size = size
                )

                // 按键侧面（3D效果）
                val sideHeight = 4f
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF202030), Color(0xFF303040))
                    ),
                    topLeft = Offset(0f, size.height - sideHeight),
                    size = Size(size.width, sideHeight)
                )

                // 按键顶部边框
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF9090A0), Color(0xFF606070))
                    ),
                    topLeft = Offset(0f, 0f),
                    size = Size(size.width, 1f),
                    style = Stroke(width = 1f)
                )

                // 按键按下效果
                if (isPressed) {
                    drawRect(
                        brush = SolidColor(Color.White.copy(alpha = 0.2f)),
                        size = size
                    )
                }

                // 绘制按键内容
                drawContent()
            }
            .background(Color.Transparent)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = key,
            color = Color(0xFFA0A0FF),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun KeyButton2(
    key: String,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 60.dp,
    ringWidth: Dp = 8.dp,
    onClick: () -> Unit,
) {
    // 交互状态用于按压效果
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .size(buttonSize)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // 绘制金属环
        Canvas(modifier = Modifier.matchParentSize()) {
            val ringRadius = size.minDimension / 2 - ringWidth.toPx() / 2
            val center = Offset(size.width / 2, size.height / 2)

            // 金属质感渐变（银白色）
            val metalGradient = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFE0E0E0), // 亮银
                    Color(0xFFB0B0B0), // 主银
                    Color(0xFF909090), // 阴影
                    Color(0xFFC0C0C0)  // 高光
                ),
                center = center,
                radius = ringRadius + ringWidth.toPx(),
                tileMode = TileMode.Clamp
            )

            // 绘制金属环（带描边效果）
            drawCircle(
                brush = metalGradient,
                radius = ringRadius,
                center = center,
                style = Stroke(width = ringWidth.toPx())
            )
        }

        // 绘制塑料按钮
        Canvas(modifier = Modifier.size(buttonSize - ringWidth * 2)) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2

            // 塑料质感渐变
            // 塑料质感渐变 (黑色系配置)
            val plasticGradient = Brush.radialGradient(
                colors = if (isPressed) {
                    // 按压状态下的颜色（变暗）
                    listOf(
                        Color(0xFF303030),  // 深灰黑
                        Color(0xFF202020),  // 更深的灰黑
                        Color(0xFF101010)   // 接近纯黑
                    )
//                    listOf(
//                        Color(0xFFFF5252), // 亮塑料色 - 较亮的红色作为高光
//                        Color(0xFFF44336), // 主塑料色 - 标准红色作为主体
//                        Color(0xFFD32F2F)  // 阴影 - 深红色作为阴影
//                    )
                } else {
                    // 正常状态下的颜色
                    listOf(
                        Color(0xFF505050),  // 中灰色
                        Color(0xFF303030),  // 深灰
                        Color(0xFF202020)   // 灰黑
                    )
//                    listOf(
//                        Color(0xFFF44336).copy(alpha = 0.9f), // 亮红 - 稍暗的红色
//                        Color(0xFFD32F2F), // 主红 - 深红色
//                        Color(0xFFB71C1C)  // 暗红 - 更深的红色作为阴影
//                    )
                },
                center = center,
                radius = radius,
                tileMode = TileMode.Clamp
            )

            // 绘制塑料按钮主体
            drawCircle(
                brush = plasticGradient,
                radius = radius,
                center = center
            )

            // 添加高光效果
            drawArc(
                color = Color.White.copy(alpha = 0.6f),
                startAngle = 135f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(radius * 0.15f, radius * 0.15f),
                size = Size(radius * 1.7f, radius * 1.7f),
                style = Stroke(width = 4f)
            )
        }

        // 按钮文字
        Text(
            text = key,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}

//动画按钮效果
@Composable
fun TypewriterKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // 定义动画：按下时缩放1.2倍，上移10dp
    val scale by animateFloatAsState(if (isPressed) 1.2f else 1f)
    val offsetY by animateDpAsState(if (isPressed) (-10).dp else 0.dp)
    val color by animateColorAsState(if (isPressed) Color.Yellow else Color.White)

    Box(
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale) // 缩放
            .offset(y = offsetY) // 上移
            .clickable(
                interactionSource = interactionSource,
                indication = null, // 禁用默认涟漪效果
                onClick = onClick
            )
            .background(color, RoundedCornerShape(8.dp))
    ) {
        Text(text = label, modifier = Modifier.padding(16.dp))
    }
}