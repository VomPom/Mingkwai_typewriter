package com.vompom.typewriter.mingkwai_typewriter.data

import kotlinx.serialization.Serializable

/**
 *
 * Created by @juliswang on 2025/07/31 22:12
 *
 * @Description
 */

@Serializable
data class UserData(
    val audio: Boolean,
    val showStats: Boolean,
    val fontSize: Int,
    val fontName: String,
    val editText: String,
    val textAlign: String
)