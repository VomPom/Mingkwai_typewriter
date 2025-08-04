package com.vompom.typewriter.mingkwai_typewriter.ext

import androidx.compose.runtime.MutableState

inline fun <T> MutableState<T>.update(
    transform: (T) -> T
): T = run {
    transform(this.value).also { this.value = it }
}

inline fun MutableState<String>.plus(
    transform: (String) -> String
): String = run {
    transform(this.value).also {
        if (it.isNotBlank() && it != "\n") {
            this.value = this.value.plus("\n").plus(it)
        }
    }
}
