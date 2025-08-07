package com.vompom.typewriter.mingkwai_typewriter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.vompom.typewriter.mingkwai_typewriter.componet.APP_TITLE
import com.vompom.typewriter.mingkwai_typewriter.componet.AudioManager
import com.vompom.typewriter.mingkwai_typewriter.componet.FONT_NAME_LIST
import com.vompom.typewriter.mingkwai_typewriter.componet.FONT_SIZE_LIST
import com.vompom.typewriter.mingkwai_typewriter.componet.TEXT_ALIGN_LIST
import com.vompom.typewriter.mingkwai_typewriter.componet.WINDOW_SIZE
import com.vompom.typewriter.mingkwai_typewriter.data.MainViewModel
import com.vompom.typewriter.mingkwai_typewriter.data.SnapshotState
import com.vompom.typewriter.mingkwai_typewriter.utils.openWebBrowser
import kotlinx.coroutines.Dispatchers
import java.io.File

@OptIn(ExperimentalSettingsApi::class)
fun main() = application {
    var windowState = rememberWindowState(
        size = DpSize(WINDOW_SIZE.width.dp, WINDOW_SIZE.height.dp),
        position = WindowPosition.Aligned(alignment = Alignment.Center)
    )

    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = APP_TITLE,
    ) {
        val viewModel = viewModel {
            MainViewModel(
                settings = PreferencesSettings.Factory().create(APP_TITLE).toFlowSettings(
                    Dispatchers.IO
                ),
                audioManager = AudioManager()
            )
        }
        SystemMenu(viewModel)
        App(viewModel)
    }
}


@Composable
fun FrameWindowScope.SystemMenu(viewModel: MainViewModel) {
    val userData by viewModel.userData.collectAsState()
    var enableAudio by mutableStateOf(userData.audio)
    var showStats by mutableStateOf(userData.showStats)
    var fontSize by mutableStateOf(userData.fontSize)
    var fontName by mutableStateOf(userData.fontName)
    var textAlign by mutableStateOf(userData.textAlign)

    MenuBar {
        Menu("File") {
            Item(
                "Export as Image",
                shortcut = KeyShortcut(Key.E, meta = true),
                onClick = { viewModel.makeSnapshot(SnapshotState.TODO) })
            Item(
                "Export as File",
                shortcut = KeyShortcut(Key.F, meta = true),
                onClick = {
                    viewModel.makeTextFile()
                })
        }
        Menu("Format") {
            Menu("Font Size") {
                FONT_SIZE_LIST.forEach {
                    CheckboxItem(text = it.toString(), checked = fontSize == it) { isChecked ->
                        viewModel.updateFont(fontSize = it)
                    }
                }
            }
            Menu("Text Align") {
                TEXT_ALIGN_LIST.forEach {
                    CheckboxItem(
                        text = it.toString(),
                        checked = textAlign.toString() == it.toString()
                    ) { isChecked ->
                        viewModel.updateTextAlign(textAlign = it)
                    }
                }
            }
            Menu("Font Style") {
                FONT_NAME_LIST.forEach {
                    CheckboxItem(text = File(it).name, checked = fontName == it) { isChecked ->
                        viewModel.updateFont(fontName = it)
                    }
                }
            }
        }
        Menu("Settings") {
            CheckboxItem(
                text = "Enable Audio",
                checked = enableAudio,
                shortcut = KeyShortcut(Key.M, meta = true)
            ) { isChecked ->
                viewModel.updateAudio(isChecked)
            }
            CheckboxItem(
                text = "Show Stats",
                checked = showStats,
                shortcut = KeyShortcut(Key.T, meta = true)
            ) { isChecked ->
                viewModel.updateShowStats(isChecked)
            }
        }

        Menu("Help") {
            Item(
                "About",
                onClick = {
                    openWebBrowser("https://github.com/VomPom/Mingkwai_typewriter")
                })
        }
    }
}