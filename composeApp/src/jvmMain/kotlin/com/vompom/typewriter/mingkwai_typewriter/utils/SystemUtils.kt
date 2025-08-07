package com.vompom.typewriter.mingkwai_typewriter.utils

/**
 *
 * Created by @juliswang on 2025/08/07 21:34
 *
 * @Description
 */

val isWindows = System.getProperty("os.name").startsWith("Win")

val isLinux = System.getProperty("os.name").startsWith("Linux")

val isMac = System.getProperty("os.name").startsWith("Mac")

fun openWebBrowser(url: String) {
    val runtime = Runtime.getRuntime()
    if (isMac) {
        runtime.exec("open $url")
    } else if (isWindows) {
        runtime.exec("xdg-open $url")
    } else {
        // no-op
    }
}