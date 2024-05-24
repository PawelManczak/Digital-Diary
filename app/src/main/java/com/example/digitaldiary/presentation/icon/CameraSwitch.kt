package com.example.digitaldiary.presentation.icon


import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Icons.Cameraswitch: ImageVector
    get() {
        if (_cameraswitch != null) {
            return _cameraswitch!!
        }
        _cameraswitch = ImageVector.Builder(
            name = "Cameraswitch",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 24.0F,
            viewportHeight = 24.0F,
        ).materialPath {
            moveTo(16.0F, 7.0F)
            horizontalLineToRelative(-1.0F)
            lineToRelative(-1.0F, -1.0F)
            horizontalLineToRelative(-4.0F)
            lineTo(9.0F, 7.0F)
            horizontalLineTo(8.0F)
            curveTo(6.9F, 7.0F, 6.0F, 7.9F, 6.0F, 9.0F)
            verticalLineToRelative(6.0F)
            curveToRelative(0.0F, 1.1F, 0.9F, 2.0F, 2.0F, 2.0F)
            horizontalLineToRelative(8.0F)
            curveToRelative(1.1F, 0.0F, 2.0F, -0.9F, 2.0F, -2.0F)
            verticalLineTo(9.0F)
            curveTo(18.0F, 7.9F, 17.1F, 7.0F, 16.0F, 7.0F)

            moveTo(12.0F, 14.0F)
            curveToRelative(-1.1F, 0.0F, -2.0F, -0.9F, -2.0F, -2.0F)
            curveToRelative(0.0F, -1.1F, 0.9F, -2.0F, 2.0F, -2.0F)
            reflectiveCurveToRelative(2.0F, 0.9F, 2.0F, 2.0F)
            curveTo(14.0F, 13.1F, 13.1F, 14.0F, 12.0F, 14.0F)

            moveTo(8.57F, 0.51F)
            lineToRelative(4.48F, 4.48F)
            verticalLineTo(2.04F)
            curveToRelative(4.72F, 0.47F, 8.48F, 4.23F, 8.95F, 8.95F)
            curveToRelative(0.0F, 0.0F, 2.0F, 0.0F, 2.0F, 0.0F)
            curveTo(23.34F, 3.02F, 15.49F, -1.59F, 8.57F, 0.51F)

            moveTo(10.95F, 21.96F)
            curveTo(6.23F, 21.49F, 2.47F, 17.73F, 2.0F, 13.01F)
            curveToRelative(0.0F, 0.0F, -2.0F, 0.0F, -2.0F, 0.0F)
            curveToRelative(0.66F, 7.97F, 8.51F, 12.58F, 15.43F, 10.48F)
            lineToRelative(-4.48F, -4.48F)
            verticalLineTo(21.96F)

            close()
        }.build()
        return _cameraswitch!!
    }
private var _cameraswitch: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconCameraswitchPreview() {
    Image(imageVector = Icons.Cameraswitch, contentDescription = null)
}