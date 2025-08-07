package com.vompom.typewriter.mingkwai_typewriter.data

import androidx.compose.ui.text.style.TextAlign
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import com.vompom.typewriter.mingkwai_typewriter.componet.FONT_NAME_LIST
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 *
 * Created by @juliswang on 2025/07/31 22:52
 *
 * @Description
 */
class PreferencesDataSource @OptIn(ExperimentalSettingsApi::class) constructor(private val settings: FlowSettings) {

    companion object {
        private const val USER_DATA = "user_data"
        val DEFAULT_USER_DATA = UserData(
            audio = true,
            fontSize = 25,
            fontName = FONT_NAME_LIST.first(),
            showStats = false,
            editText = "",
            textAlign = TextAlign.Left.toString(),
        )
    }

    @OptIn(ExperimentalSettingsApi::class)
    val userData = settings.getStringOrNullFlow(USER_DATA).map { string ->
        string?.let {
            Json.decodeFromString<UserData>(string)
        } ?: let {
            DEFAULT_USER_DATA
        }
    }

    @OptIn(ExperimentalSettingsApi::class)
    suspend fun saveData(userData: UserData) {
        val string = Json.encodeToString(userData)
        settings.putString(USER_DATA, string)
        println("save settings:$string")
    }
}
