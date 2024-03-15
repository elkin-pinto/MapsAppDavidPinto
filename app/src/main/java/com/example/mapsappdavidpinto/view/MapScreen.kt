package com.example.mapsappdavidpinto.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.mapsappdavidpinto.viewModel.MainViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(navController: NavController, vM: MainViewModel) {
    vM.title.value = ""
    vM.snippet.value = ""
    vM.tipus.value = ""
    MyScaffold(navController,vM.bottomNavigationItems) { Screen(navController, vM) }
}
@Composable
private fun Screen(navController: NavController, vM: MainViewModel) {
    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        val show = vM.show.observeAsState(false)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(vM.latPosition,vM.lngPosition), 10f)
        }
        Box(Modifier.fillMaxSize()) {
            GoogleMap (
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLongClick = {
                    vM.lat.value = it.latitude.toString()
                    vM.lng.value = it.longitude.toString()
                    vM.show.value = true
                }
            )
            {
                vM.markers.value?.forEach {
                    Marker(
                        state = MarkerState(position = it.state),
                        title = it.title,
                        snippet = it.snippet
                    )
                }

                if (show.value) {
                    Dialog(
                        onDismissRequest = { vM.show.value = false },
                    ) {
                        NewMarker(vM,navController){}
                    }
                }


            }
            Box(
                Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth(0.98f), contentAlignment = Alignment.BottomEnd) {
                AddMarkerButton(navController)
            }
        }

    }
}





