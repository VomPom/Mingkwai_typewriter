package com.vompom.typewriter.mingkwai_typewriter.componet

import com.vompom.typewriter.mingkwai_typewriter.data.MainViewModel.AudioType
import java.io.IOException
import java.util.concurrent.Executors
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import kotlin.random.Random

/**
 *
 * Created by @juliswang on 2025/07/31 21:17
 *
 * @Description
 */

class AudioManager {
    companion object {
        const val AUDIO_PRE = "key_"
    }

    private val clipCache = mutableMapOf<String, Clip>()
    private val soundExecutor = Executors.newFixedThreadPool(2) // Adjust pool size as needed

    fun play(type: AudioType) {
        playSound(
            if (type.fileName.startsWith(AUDIO_PRE))
                randomKeyAudio()
            else
                type.fileName
        )
    }

    private fun playSound(name: String) {
        soundExecutor.submit {
            try {
                val clip = clipCache.getOrPut(name) {
                    val resourceUrl = javaClass.getResource("/audio/$name.wav")
                    if (resourceUrl == null) {
                        throw IOException("Audio resource not found: $name")
                    }
                    val audioInputStream = AudioSystem.getAudioInputStream(resourceUrl)
                    val newClip = AudioSystem.getClip()

                    newClip.open(audioInputStream)
                    newClip
                }
                // Stop the clip if it's currently playing to avoid overlapping sounds
                if (clip.isRunning) {
                    clip.stop()
                }
                clip.framePosition = 0 // Rewind to start
                clip.start()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun randomKeyAudio(): String {
        val maxSize = 7
        val index = Random.nextInt(1, maxSize + 1)
        return "$AUDIO_PRE$index"
    }

}