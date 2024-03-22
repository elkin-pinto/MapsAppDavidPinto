package com.example.mapsappdavidpinto.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavController
import com.example.mapsappdavidpinto.controllers.MainActivity
import com.example.mapsappdavidpinto.controllers.Routes
import com.example.mapsappdavidpinto.viewModel.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun AddMarkerScreen(navController: NavController, vM: MainViewModel) {
    MyDrawerMenu(vM,navController) { Screen1(vM,navController) }
}


@Composable
fun Screen1 (vM: MainViewModel,navController: NavController) {
    Spacer(Modifier.height(15.dp))
    Column (horizontalAlignment = Alignment.CenterHorizontally){
        Spacer(Modifier.height(15.dp))
        Text("Create Marker", fontSize = 25.sp)
        MarkerCreateFun(vM , navController)
        Text("Create Type", fontSize = 25.sp)
        Spacer(Modifier.height(15.dp))
        TypeCreateFun(vM)
    }

}

@Composable
fun TypeCreateFun(vM:MainViewModel) {
    var typeName by remember{ mutableStateOf("") }

    TextField(value = typeName, onValueChange = {typeName = it})
    Spacer(Modifier.height(15.dp))
    Button(onClick = { if (typeName != "") {
        vM.tipusMarkerList.add(typeName)
        typeName = ""
    } }, Modifier.fillMaxWidth(0.9f)) {
        Text("Create Type")
    }
}


@SuppressLint("MissingPermission")
@Composable
private fun MarkerCreateFun(vM:MainViewModel,navController: NavController) {
    val showMap = vM.mapDialog.observeAsState(false)
    val isCameraPermissionGranted by vM.cameraPermissionGranted.observeAsState(false)
    val showPermissionDenied by vM.showPermissionDenied.observeAsState(false)

    val launcher = CameraOption(vM, navController)
    NewMarker(vM,navController){
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
            Spacer(Modifier.width(15.dp))
            Button(onClick = {
                vM.mapDialog.value = true
                 }) {
                Icon(Icons.Filled.LocationOn, "Location Icon" )
            }
        }
    }
    if (showMap.value) {
        MapOption(vM)
    }
}


@Composable
fun CameraOption(vM:MainViewModel,navController: NavController): ManagedActivityResultLauncher<String, Boolean> {
    val context = LocalContext.current
    val shouldShowPermissionRationale by vM.shouldShowPermissionRationale.observeAsState(false)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
             if (isGranted) {
                 vM.cameraPermissionGranted.value = true
             }else {
                 vM.shouldShowPermissionRationale.value = shouldShowRequestPermissionRationale(
                     context as Activity,
                     Manifest.permission.CAMERA
                 )
                 if (!shouldShowPermissionRationale) {
                     Log.i("CameraScreen","No podemos volver a perdir permisos")
                     vM.showPermissionDenied.value = true
                 }
             }
        }
    )
    return launcher
}



@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapOption(vM:MainViewModel) {

    if (vM.mapDialog.value!!) {
        val mapLat = vM.lat.observeAsState(0.0)
        val mapLng = vM.lng.observeAsState(0.0)
        val permissionState =
            rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
        LaunchedEffect(Unit) {
            permissionState.launchPermissionRequest()
        }
        val cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(LatLng(mapLat.value,mapLng.value),18f) }
        var deviceLatLng by remember { mutableStateOf(LatLng(mapLat.value,mapLng.value)) }
        if (mapLat.value == 0.0 || mapLng.value == 0.0) {
            val context = LocalContext.current
            val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }
            var lastKnowLocation by remember { mutableStateOf<android.location.Location?>(null) }
            cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLatLng,18f)
            val locationResult = fusedLocationProviderClient.getCurrentLocation(100,null)

            locationResult.addOnCompleteListener(context as MainActivity) {task ->
                if (task.isSuccessful) {
                    lastKnowLocation = task.result
                    deviceLatLng = LatLng(lastKnowLocation!!.latitude, lastKnowLocation!!.longitude)
                    vM.lng.value = lastKnowLocation!!.longitude
                    vM.lat.value = lastKnowLocation!!.latitude
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLatLng,18f)
                }else {
                    Log.e("Error", "Exception: %$", task.exception)
                }
            }
        }



        if (permissionState.status.isGranted) {
            Dialog(onDismissRequest = { vM.mapDialog.value = false }) {
                GoogleMap (
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth(0.95f),
                    cameraPositionState = cameraPositionState,
                    onMapClick = {
                        vM.lat.value = it.latitude
                        vM.lng.value = it.longitude
                        vM.mapDialog.value = false
                    }
                )
                {
                    Marker(
                        state = MarkerState(position = deviceLatLng),
                        title = "New",
                        snippet = "New Maker"
                    )
                }
            }
        }
    }
}

