package com.example.mapsappdavidpinto.view

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.mapsappdavidpinto.controllers.MainActivity
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
    MyScaffold(navController, vM.bottomNavigationItems ) { Screen(vM,navController) }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Screen(vM:MainViewModel,navController: NavController) {
    var mapDialog by remember { mutableStateOf(false) }

    NewMarker(vM,navController){
        Row (Modifier.padding(5.dp)){
            Button(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.CameraAlt, "Location Icon" )
            }
            Spacer(Modifier.width(15.dp))
            Button(onClick = {
                mapDialog = true
                 }) {
                Icon(Icons.Filled.LocationOn, "Location Icon" )
            }
        }
    }
    if (mapDialog) {
        var mapPermission by remember { mutableStateOf(false) }
        val permissionState =
            rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
        LaunchedEffect(Unit) {
            permissionState.launchPermissionRequest()
        }
        mapPermission = permissionState.status.isGranted
        var cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(vM.latPosition,vM.lngPosition), 10f)
        }

        if (vM.lat.value == "" || vM.lng.value == "") {
            val context = LocalContext.current
            val fusedLocationProviderClient = remember { LocationServices.getFusedLocationProviderClient(context) }
            var lastKnowLocation by remember { mutableStateOf<android.location.Location?>(null) }
            var deviceLatLng by remember { mutableStateOf(LatLng(0.0,0.0)) }
            cameraPositionState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(deviceLatLng,18f) }
            val locationResult = fusedLocationProviderClient.getCurrentLocation(100,null)

            locationResult.addOnCompleteListener(context as MainActivity) {task ->
                if (task.isSuccessful) {
                    lastKnowLocation = task.result
                    deviceLatLng = LatLng(lastKnowLocation!!.latitude, lastKnowLocation!!.longitude)
                    vM.lng.value = lastKnowLocation!!.longitude.toString()
                    vM.lat.value = lastKnowLocation!!.latitude.toString()
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLatLng,18f)
                }else {
                    Log.e("Error", "Exception: %$", task.exception)
                }
            }
        }


        Dialog(onDismissRequest = { mapDialog = false }) {
            GoogleMap (
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth(0.95f),
                cameraPositionState = cameraPositionState,
                onMapClick = {
                    vM.lat.value = it.latitude.toString()
                    vM.lng.value = it.longitude.toString()
                    mapDialog = false
                }
            )
            {

                Marker(
                    state = MarkerState(position = LatLng(vM.lat.value?.toDouble()!!,vM.lng.value?.toDouble()!!)),
                    title = "You",
                    snippet = "Actual Position"
                )
            }
        }
    }
}

