package com.example.mapsappdavidpinto.view

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsappdavidpinto.controllers.Routes
import com.example.mapsappdavidpinto.model.MyMarker
import com.example.mapsappdavidpinto.viewModel.MainViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewMarker(vM: MainViewModel,navController: NavController,function: @Composable () -> Unit){
    val title = vM.title.observeAsState("")
    val image by vM.image.observeAsState(null)
    val description = vM.snippet.observeAsState("")
    val tipus by vM.tipus.observeAsState("")
    val isCameraPermissionGranted by vM.cameraPermissionGranted.observeAsState(false)
    val showPermissionDenied by vM.showPermissionDenied.observeAsState(false)

    val launcher = CameraOption(vM, navController)

    Column(
        Modifier
            .background(Color.White)
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        MyTextField(vM.title,title,"The marker name")
        MyTextField(vM.snippet,description,"The marker description")
        MyDropMenuTipus(vM.tipus, vM.tipusMarkerList, tipus)

        Row (Modifier.padding(5.dp)){
            Button(onClick = {
                if(!isCameraPermissionGranted) {
                    launcher.launch(Manifest.permission.CAMERA)
                }else {
                    navController.navigate(Routes.TakePhotoScreen.route)
                }
                if (showPermissionDenied) navController.navigate(Routes.PermissionDeclinedScreen.route)
            }) {
                Icon(Icons.Filled.CameraAlt, "Location Icon" )
            }
            function()
        }

        if (image != null) {
            GlideImage(model = image, contentDescription = "Marker Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(100.dp))
        }

        Button(onClick = {
            try {
                val marker = MyMarker(vM.lat.value!!.toDouble(),vM.lng.value!!.toDouble(),vM.title.value!!,vM.snippet.value!!,vM.tipus.value!!,vM.image.value,vM._userId.value!!)
                vM.newMarker(marker)
                vM.latPosition = vM.lat.value!!.toDouble()
                vM.lngPosition = vM.lng.value!!.toDouble()
                vM.show.value = false
                vM.addMarker(marker)

                navController.navigate(Routes.MapScreen.route)
            } catch (e: Exception) {
                println(e.message)
            }finally {
                vM.lat.value = 0.0
                vM.lng.value = 0.0
                vM.title.value = ""
                vM.snippet.value = ""
                vM.tipus.value = ""
                vM.image.value = null
            }
        }, Modifier.fillMaxWidth()) {
            Text("Create Marker")
        }

    }
}

@Composable
fun MyTextField (title: MutableLiveData<String>, value: State<String>, m1:String){
    TextField(value = value.value, onValueChange = { title.value = it },
        label = { Text(text = m1) },
    )
}