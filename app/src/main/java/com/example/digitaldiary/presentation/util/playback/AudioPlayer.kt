package com.example.digitaldiary.presentation.util.playback

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}