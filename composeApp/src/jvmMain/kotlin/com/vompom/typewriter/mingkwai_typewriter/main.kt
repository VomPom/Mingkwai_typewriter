package com.vompom.typewriter.mingkwai_typewriter

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import com.vompom.typewriter.mingkwai_typewriter.componet.AudioManager
import com.vompom.typewriter.mingkwai_typewriter.data.MainViewModel
import com.vompom.typewriter.mingkwai_typewriter.data.SnapshotState
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalSettingsApi::class)
fun main() = application {
    var windowState = rememberWindowState(
        size = DpSize(1280.dp, 600.dp),
        position = WindowPosition.Aligned(alignment = Alignment.Center)
    )

    Window(
        state = windowState,
        onCloseRequest = ::exitApplication,
        title = "mingkuai",
    ) {
        val viewModel = viewModel {
            MainViewModel(
                settings = PreferencesSettings.Factory().create("MingKuai").toFlowSettings(
                    Dispatchers.IO
                ),
                audioManager = AudioManager()
            )
        }
        val userData by viewModel.userData.collectAsState()
        var enableAudio by mutableStateOf(userData.audio)
        var fontSize by mutableStateOf(userData.fontSize)

        MenuBar {
            Menu("File") {
                Item("Export as Image", onClick = { viewModel.makeSnapshot(SnapshotState.TODO) })
                Item("Export as File", onClick = {
                    viewModel.makeTextFile()
                })
            }
            Menu("Settings") {
                CheckboxItem(
                    text = "Enable Audio",
                    checked = enableAudio,
                    shortcut = KeyShortcut(Key.M, meta = true)
                ) { isChecked ->
                    viewModel.updateAudio(isChecked)
                }
                Menu("Font Size") {
                    listOf<Int>(10, 12, 14, 15, 16, 18, 20, 25, 30, 35, 40).forEach {
                        CheckboxItem(text = it.toString(), checked = fontSize == it) { isChecked ->
                            viewModel.updateFont(it)
                        }
                    }
                }
                Menu("Font Style") {
                    listOf<Int>(10, 12, 14, 15, 16, 18, 20, 25, 30, 35, 40).forEach {
                        CheckboxItem(text = it.toString(), checked = fontSize == it) { isChecked ->
                            viewModel.updateFont(it)
                        }
                    }
                }
                Separator()
            }
        }
        App(viewModel)
    }
}

