package com.vompom.typewriter.mingkwai_typewriter.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import com.vompom.typewriter.mingkwai_typewriter.componet.AudioManager
import com.vompom.typewriter.mingkwai_typewriter.ext.update
import com.vompom.typewriter.mingkwai_typewriter.utils.browseFileDirectory
import com.vompom.typewriter.mingkwai_typewriter.utils.formatTimestamp
import com.vompom.typewriter.mingkwai_typewriter.utils.textDir
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

/**
 *
 * Created by @juliswang on 2025/07/31 21:03
 *
 * @Description
 */
class MainViewModel @OptIn(ExperimentalSettingsApi::class) constructor(
    settings: FlowSettings,
    private val audioManager: AudioManager
) : ViewModel() {
    @OptIn(ExperimentalSettingsApi::class)
    private val preferences = PreferencesDataSource(settings)

    val userData = preferences.userData.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = PreferencesDataSource.DEFAULT_USER_DATA
    )

    private val _makeSnapshot = mutableStateOf<SnapshotState>(SnapshotState.IDLE)
    val makeSnapshot by _makeSnapshot
    private val _editText = mutableStateOf<String>("")

    private fun updateUserData(block: UserData.() -> UserData) {
        viewModelScope.launch {
            val newData = userData.value.block() // 调用传入的 lambda 修改 UserData
            preferences.saveData(newData)
        }
    }

    fun updateAudio(audioEnable: Boolean) = updateUserData { copy(audio = audioEnable) }
    fun updateShowStats(showStats: Boolean) = updateUserData { copy(showStats = showStats) }
    fun updateTextAlign(textAlign: TextAlign) = updateUserData { copy(textAlign = textAlign.toString()) }

    fun updateFont(fontSize: Int = -1, fontName: String = "") {
        updateUserData {
            copy(
                fontSize = if (fontSize == -1) userData.value.fontSize else fontSize,
                fontName = if (fontName == "") userData.value.fontName else fontName
            )
        }
    }


    fun updateText(text: String) = _editText.update { text }

    fun playAudio(type: AudioType) = if (userData.value.audio) {
        audioManager.play(type)
    } else {
    }

    fun makeSnapshot(state: SnapshotState) {
        _makeSnapshot.update { state }
        if (state is SnapshotState.Done) {
            val targetFile = File(state.result as String)
            browseFileDirectory(targetFile)
        }
    }

    fun makeTextFile() {
        viewModelScope.launch {
            try {
                val fileDir = textDir
                val fileName = "mk_${formatTimestamp(System.currentTimeMillis())}.txt"
                val file = File(fileDir, fileName)
                file.writeText(_editText.value)
                browseFileDirectory(file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun showStatusUI(): Boolean = this.userData.value.showStats

    fun textAlign(): TextAlign {
        return when (this.userData.value.textAlign) {
            TextAlign.Left.toString() -> TextAlign.Left
            TextAlign.Center.toString() -> TextAlign.Center
            TextAlign.Right.toString() -> TextAlign.Right
            else -> {
                TextAlign.Left
            }
        }
    }

    fun typeFontFamily(): FontFamily {
        return FontFamily(
            Font(
                "font/${this.userData.value.fontName}",
                FontWeight.Normal,
                FontStyle.Normal
            )
        )
    }

    enum class AudioType(val fileName: String) {
        CLICK("key_1"),
        NEWLINE("typewriter_return")
    }
}


sealed interface UIState {
    data object WAIT : UIState
    data object Loading : UIState
    data class Success(val result: Any) : UIState
}

sealed interface SnapshotState {
    data object IDLE : SnapshotState
    data object TODO : SnapshotState
    data class Done(val result: Any) : SnapshotState
}
