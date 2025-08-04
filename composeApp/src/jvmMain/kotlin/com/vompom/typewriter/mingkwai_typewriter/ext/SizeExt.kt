package com.vompom.typewriter.mingkwai_typewriter.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 *
 * Created by @juliswang on 2025/07/31 22:52
 *
 * @Description
 */

@Composable
fun Int.pxToDp(): Dp {
    val density = LocalDensity.current
    return with(density) { this@pxToDp.toDp() }
}

@Composable
fun Float.pxToDp(): Dp {
    val density = LocalDensity.current
    return with(density) { this@pxToDp.toDp() }
}

@Composable
fun Dp.toPx(): Float {
    return with(LocalDensity.current) { this@toPx.toPx() }
}

@Composable
fun Int.toPx(): Float {
    return this.dp.toPx()
}

@Composable
fun Float.toPx(): Float {
    return this.dp.toPx()
}