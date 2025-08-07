package com.vompom.typewriter.mingkwai_typewriter.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 *
 * Created by @juliswang on 2025/08/05 21:09
 *
 * @Description
 */

fun formatTimestamp(timestamp: Long): String {
    return SimpleDateFormat("yyyy-MM-dd-HHmm", Locale.getDefault()).format(Date(timestamp))
}