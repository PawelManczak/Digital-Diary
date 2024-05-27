package com.example.digitaldiary.presentation.screencontent


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import com.example.digitaldiary.R
import com.example.digitaldiary.presentation.icon.Camera
import com.example.digitaldiary.presentation.icon.Cameraswitch
import com.example.digitaldiary.presentation.util.rotateBitmap
import com.example.digitaldiary.presentation.util.toggleCamera
import java.io.File
import java.util.UUID
import java.util.concurrent.Executor

@Composable
fun CameraContent(
    onPhotoCaptured: (Uri) -> Unit,
) {

    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(context) }

    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        ExtendedFloatingActionButton(text = { Text(text = stringResource(R.string.take_photo)) },
            onClick = { capturePhoto(context, cameraController, onPhotoCaptured) },
            icon = {
                Image(
                    imageVector = Icons.Default.Camera,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

            })
    }) { paddingValues: PaddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setBackgroundColor(Color.BLACK)
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                })
        }
        FloatingActionButton(
            onClick = { toggleCamera(cameraController) }, modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Cameraswitch,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

private fun capturePhoto(
    context: Context, cameraController: LifecycleCameraController, onPhotoCaptured: (Uri) -> Unit
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {

            val bmp: Bitmap =
                image.toBitmap().rotateBitmap(image.imageInfo.rotationDegrees.toFloat())

            val file = File(context.filesDir, "${UUID.randomUUID()}.jpg")
            file.outputStream().use { out ->
                bmp.scale(bmp.width, bmp.height).compress(Bitmap.CompressFormat.JPEG, 90, out)
                out.flush()
            }
            var scaleRatio = 0.9f
            while (file.length() > 524_288) { //512kB
                file.outputStream().use { out ->
                    bmp.scale(
                        (bmp.width * scaleRatio).toInt(), (bmp.height * scaleRatio).toInt()
                    ).compress(Bitmap.CompressFormat.JPEG, 90, out)
                    out.flush()
                }
                scaleRatio -= 0.1f
            }
            onPhotoCaptured(file.toUri())
            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            exception.printStackTrace()
        }
    })
}


@Preview
@Composable
private fun PreviewCameraContent() {
    CameraContent(onPhotoCaptured = {})
}
