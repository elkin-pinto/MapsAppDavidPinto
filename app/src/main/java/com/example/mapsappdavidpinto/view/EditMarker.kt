package com.example.mapsappdavidpinto.view

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsappdavidpinto.controllers.Routes
import com.example.mapsappdavidpinto.viewModel.MainViewModel

@Composable
fun EditMarker(vM: MainViewModel, navController: NavController) {
    MyDrawerMenu(vM,navController) { Screen1(vM,navController) }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Screen1(vM: MainViewModel, navController: NavController) {
    val title = vM.titleEdit.observeAsState("")
    val description = vM.snippetEdit.observeAsState("")
    val image = vM.image.observeAsState("")
    val tipus = vM.tipusEdit.observeAsState("")


    val isCameraPermissionGranted by vM.cameraPermissionGranted.observeAsState(false)
    val showPermissionDenied by vM.showPermissionDenied.observeAsState(false)

    val launcher = CameraOption(vM, navController)


    Spacer(Modifier.height(15.dp))
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(Modifier.height(15.dp))
        Text("Edit Marker", fontSize = 25.sp)

        Column(
            Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {

            MyTextField(vM.titleEdit,title,"The marker name")
            MyTextField(vM.snippetEdit,description,"The marker description")
            MyDropMenuTipus(vM.tipusEdit, vM.tipusMarkerList, tipus.value)

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
            }

            if (image.value != null) {
                GlideImage(model = image.value, contentDescription = "Marker Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(100.dp))
            }

            Button(onClick = {
                try {
                    vM.actualitzarMarkerEdit()
                    val marker = vM.markerEdit
                    vM.editMarker(marker)
                } catch (e: Exception) {
                    println(e.message)
                }finally {
                    vM.image.value = null
                    navController.navigate(Routes.MarkerListScreen.route)
                }
            }, Modifier.fillMaxWidth()) {
                Text("Edit Marker")
            }

        }
    }
}