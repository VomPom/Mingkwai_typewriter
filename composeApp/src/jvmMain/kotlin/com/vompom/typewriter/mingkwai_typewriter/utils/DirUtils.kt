package com.vompom.typewriter.mingkwai_typewriter.utils

import java.awt.Desktop
import java.io.File

/**
 *
 * Created by @juliswang on 2025/08/04 21:07
 *
 * @Description
 */
private const val APP_DIR = "mingkuai"

val snapshotDir: String = safeDir("snapshot")
val textDir: String = safeDir("text")

private fun safeDir(dirname: String): String {
    val path = getDownloadDirectory()
        .plus(File.separator)
        .plus(APP_DIR)
        .plus(File.separator)
        .plus(dirname)
    if (!File(path).exists()) {
        File(path).mkdirs()
    }
    return path
}

fun getDownloadDirectory(): String {
    val osName = System.getProperty("os.name")
    return when {
        osName.contains("Windows") -> System.getProperty("user.home") + "\\Downloads"
        osName.contains("Mac") -> System.getProperty("user.home") + "/Downloads"
        else -> {
            System.getProperty("user.home") + "/Downloads"
        }
    }
}

val isWindows = System.getProperty("os.name").startsWith("Win")

val isLinux = System.getProperty("os.name").startsWith("Linux")

val isMac = System.getProperty("os.name").startsWith("Mac")


fun browseFileDirectory(file: File) {
    if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE_FILE_DIR)) {
        Desktop.getDesktop().browseFileDirectory(file)
    } else {
        if (isWindows) {
            Runtime.getRuntime().exec("explorer /select, ${file.absolutePath}")
        } else if (isMac) {
            Runtime.getRuntime().exec("open -R ${file.absolutePath}")
        } else if (isLinux) {

        }
    }
}