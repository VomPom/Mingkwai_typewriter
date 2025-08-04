package com.vompom.typewriter.mingkwai_typewriter.componet

import com.vompom.typewriter.mingkwai_typewriter.data.MainViewModel.AudioType
import java.io.File
import java.util.concurrent.Executors
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

/**
 *
 * Created by @juliswang on 2025/07/31 21:17
 *
 * @Description
 */

class AudioManager {
    private val clipCache = mutableMapOf<String, Clip>()
    private val soundExecutor = Executors.newFixedThreadPool(2) // Adjust pool size as needed

    fun play(type: AudioType) {
        playSound(type.fileName)
    }

    private fun playSound(name: String) {
        soundExecutor.submit {
            try {
                val clip = clipCache.getOrPut(name) {
                    val path = "/Users/juliswang/Downloads/Resources/$name"
                    val audioInputStream = AudioSystem.getAudioInputStream(File(path))
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


}