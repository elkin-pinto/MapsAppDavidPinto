package com.example.mapsappdavidpinto.view

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.mapsappdavidpinto.controllers.Routes
import com.example.mapsappdavidpinto.viewModel.MainViewModel
import java.io.File

@Composable
fun TakePhotoScreen(navController: NavController, vM: MainViewModel,) {
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            CameraController.IMAGE_CAPTURE
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(controller = controller, modifier = Modifier.fillMaxSize())
        IconButton(
            onClick = {
                controller.cameraSelector =
                   if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                       CameraSelector.DEFAULT_FRONT_CAMERA
                   }else {
                       CameraSelector.DEFAULT_BACK_CAMERA
                   }
            },
            modifier = Modifier.offset(
                16.dp,
                16.dp
            )
        ) {
            Icon(Icons.Default.Cameraswitch, contentDescription = "Swith camera")
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(contentAlignment = Alignment.BottomEnd,modifier = Modifier.fillMaxSize()) {
            Row (

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ){

                IconButton(onClick = { navController.navigate(Routes.GalleryScreen.route) }) {
                    Icon(imageVector = Icons.Default.Photo, contentDescription = "Open Gallery")
                }
                IconButton(onClick = {
                    takePhoto(vM,context,controller) {
                        takePhoto(vM,context, controller) {
                            navController.navigate()
                        }
                    }

                }) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = "Take photo")
                }
            }
        }
    }
}

private fun takePhoto(vM:MainViewModel,context: Context,
                      controller: LifecycleCameraController, onPhotoTaken: (Uri) -> Unit) {
    val outputDirectory = File(context.filesDir, "photos") // Directorio donde se guardará la imagen
    if (!outputDirectory.exists()) {
        outputDirectory.mkdirs()
    }

    val photoFile = File(outputDirectory, "${System.currentTimeMillis()}.jpg")

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                vM.uploadImage(savedUri)
                onPhotoTaken(savedUri)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("Camera", "Error al guardar la imagen", exception)
            }
        }
    )
}


@Composable
fun CameraPreview(
    controller: LifecycleCameraController, modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },modifier = modifier
    )
}