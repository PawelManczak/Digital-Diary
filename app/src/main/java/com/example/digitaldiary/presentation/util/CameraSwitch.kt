package com.example.digitaldiary.presentation.util

import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController

fun toggleCamera(cameraController: CameraController) {
    if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA && cameraController.hasCamera(
            CameraSelector.DEFAULT_FRONT_CAMERA
        )
    ) {
        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    } else if (cameraController.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA && cameraController.hasCamera(
            CameraSelector.DEFAULT_BACK_CAMERA
        )
    ) {
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    }
}