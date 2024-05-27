package com.example.digitaldiary.presentation.util.record

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}