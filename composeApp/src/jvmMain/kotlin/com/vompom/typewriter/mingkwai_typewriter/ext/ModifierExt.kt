package com.vompom.typewriter.mingkwai_typewriter.ext

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asComposeCanvas
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorInfo
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import org.jetbrains.skia.Picture
import org.jetbrains.skia.PictureRecorder
import org.jetbrains.skia.Rect
import java.io.File

/**
 *
 * Created by @juliswang on 2024/12/27 17:47
 *
 * @Description
 */
/**
 * 虚实分割线
 * 实线虚线长度都为10f 根据实际需求更改下方PathEffect里面的float参数数据
 **/
internal fun Modifier.dashedDivider(strokeWidth: Dp, color: Color) = drawBehind {
    drawIntoCanvas {
        val paint = Paint()
            .apply {
                this.strokeWidth = strokeWidth.toPx()
                this.color = color
                style = PaintingStyle.Stroke
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            }
        it.drawLine(
            Offset(0f, size.height / 2),
            Offset(size.width, size.height / 2),
            paint
        )
    }
}


/**
 * 虚线边框
 * @param width 虚线宽度
 * @param radius 边框角度
 * @param color 边框颜色
 * 虚实间隔也是写死的10f
 **/
internal fun Modifier.dashedBorder(width: Dp, radius: Dp, color: Color) =
    drawBehind {
        drawIntoCanvas {
            val paint = Paint()
                .apply {
                    strokeWidth = width.toPx()
                    this.color = color
                    style = PaintingStyle.Stroke
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                }
            it.drawRoundRect(
                width.toPx(),
                width.toPx(),
                size.width - width.toPx(),
                size.height - width.toPx(),
                radius.toPx(),
                radius.toPx(),
                paint
            )
        }
    }

@Composable
fun Modifier.backgroundInPadding(color: Color): Modifier =
    this.background(color)

@Composable
fun Modifier.shimmer(offsetValue: Float = 1000f, durationMillis: Int = 1000): Modifier =
    this.background(shimmerBrush(offsetValue, durationMillis))

@Composable
private fun shimmerBrush(offsetValue: Float = 1000f, durationMillis: Int = 1000): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.5f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.5f),
    )

    val transition = rememberInfiniteTransition(label = "Shimmer Effect Transition")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = offsetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis), repeatMode = RepeatMode.Restart
        ),
        label = "Shimmer Effect Translation"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )
}

inline fun Modifier.snapshot(
    pictureRecorder: PictureRecorder,
    isNeedMake: Boolean,
    saveDir: String,
    crossinline onSuccess: (String) -> Unit
): Modifier {
    return this.drawWithCache {
        val width = this.size.width
        val height = this.size.height
        onDrawWithContent {
            val pictureCanvas =
                pictureRecorder.beginRecording(Rect.makeWH(width, height))
                    .asComposeCanvas()
            draw(this, this.layoutDirection, pictureCanvas, this.size) {
                this@onDrawWithContent.drawContent()
            }
            val picture = pictureRecorder.finishRecordingAsPicture()
            drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }

            if (isNeedMake) {
                makeSnapshot(width, height, picture, saveDir, onSuccess)
            }
        }
    }
}

inline fun makeSnapshot(
    width: Float,
    height: Float,
    picture: Picture,
    saveDir: String,
    crossinline onSuccess: (String) -> Unit
) {
    val bitmap = Bitmap()
    val ci = ColorInfo(
        ColorType.BGRA_8888, ColorAlphaType.OPAQUE, ColorSpace.sRGB,
    )
    bitmap.setImageInfo(ImageInfo(ci, width.toInt(), height.toInt()))
    bitmap.allocN32Pixels(width.toInt(), height.toInt())
    val canvas = Canvas(bitmap)
    canvas.drawPicture(picture)
    bitmap.setImmutable()
    val data = Image.makeFromBitmap(bitmap).use { image ->
        image.encodeToData(EncodedImageFormat.PNG)!!
    }
    val savePath = createSnapshotPath(saveDir)
    val out = File(savePath)
    out.delete()
    out.writeBytes(data.bytes)
    onSuccess(savePath)
}

fun createSnapshotPath(saveDir: String): String = saveDir
    .plus(File.separator)
    .plus(System.currentTimeMillis())
    .plus(".png")
