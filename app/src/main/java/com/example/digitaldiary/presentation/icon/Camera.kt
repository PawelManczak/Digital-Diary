package com.example.digitaldiary.presentation.icon


import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


val Icons.Filled.Camera: ImageVector
    get() {
        if (_camera != null) {
            return _camera!!
        }
        _camera = ImageVector.Builder(
            name = "Camera",
            defaultWidth = 90.0.dp,
            defaultHeight = 90.0.dp,
            viewportWidth = 90.0F,
            viewportHeight = 90.0F,
        ).materialPath {
            verticalLineToRelative(0.0F)
            moveTo(31.66F, 49.5165F)
            curveTo(31.66F, 56.9711F, 37.5266F, 62.9612F, 44.6993F, 62.9612F)
            curveTo(51.8719F, 62.9612F, 57.7386F, 56.9711F, 57.7386F, 49.5165F)
            curveTo(57.7386F, 42.0619F, 51.8719F, 36.0717F, 44.6993F, 36.0717F)
            curveTo(37.5266F, 36.0717F, 31.66F, 42.0619F, 31.66F, 49.5165F)

            moveTo(22.6107F, 12.5367F)
            horizontalLineTo(10.1163F)
            curveTo(7.42326F, 12.5367F, 4.8469F, 13.6375F, 2.95221F, 15.5865F)
            curveTo(1.05853F, 17.5346F, 0.0F, 20.1702F, 0.0F, 22.9121F)
            verticalLineTo(76.7395F)
            curveTo(0.0F, 79.4814F, 1.05853F, 82.1171F, 2.95221F, 84.0651F)
            curveTo(4.8469F, 86.0142F, 7.42326F, 87.115F, 10.1163F, 87.115F)
            horizontalLineTo(79.8837F)
            curveTo(82.5767F, 87.115F, 85.1531F, 86.0142F, 87.0478F, 84.0651F)
            curveTo(88.9415F, 82.1171F, 90.0F, 79.4814F, 90.0F, 76.7395F)
            verticalLineTo(22.9121F)
            curveTo(90.0F, 20.1702F, 88.9415F, 17.5346F, 87.0478F, 15.5865F)
            curveTo(85.1531F, 13.6375F, 82.5767F, 12.5367F, 79.8837F, 12.5367F)
            horizontalLineTo(67.3893F)
            lineTo(59.0788F, 3.98768F)
            curveTo(58.8163F, 3.71764F, 58.4568F, 3.56543F, 58.0814F, 3.56543F)
            horizontalLineTo(31.9186F)
            curveTo(31.5432F, 3.56543F, 31.1837F, 3.71764F, 30.9212F, 3.98768F)
            lineTo(22.6107F, 12.5367F)

            moveTo(25.2606F, 49.5165F)
            curveTo(25.2606F, 38.5192F, 33.9349F, 29.5511F, 44.6993F, 29.5511F)
            curveTo(55.4637F, 29.5511F, 64.1379F, 38.5192F, 64.1379F, 49.5165F)
            curveTo(64.1379F, 60.5138F, 55.4637F, 69.4819F, 44.6993F, 69.4819F)
            curveTo(33.9349F, 69.4819F, 25.2606F, 60.5138F, 25.2606F, 49.5165F)

            close()
        }.build()
        return _camera!!
    }
private var _camera: ImageVector? = null


@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconCameraPreview() {
    Image(imageVector = Icons.Filled.Camera, contentDescription = null)
}