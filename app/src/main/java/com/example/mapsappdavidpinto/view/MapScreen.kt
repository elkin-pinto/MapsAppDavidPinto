package com.example.mapsappdavidpinto.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.mapsappdavidpinto.model.Colors
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
    MyScaffold(navController,vM.bottomNavigationItems) { Map(navController, vM) }
}
@Composable
fun Map(navController: NavController, vM: MainViewModel) {
    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        val show = vM.show.observeAsState(false)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(41.4534265,2.1837151), 10f)
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
                NewMarker(vM,show.value)

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


@Composable
fun NewMarker(vM:MainViewModel,show:Boolean){

    val title = vM.title.observeAsState("")
    val description = vM.snippet.observeAsState("")

    if (show) {
        Dialog(
            onDismissRequest = { vM.show.value = false },
        ) {
            Column(
                Modifier
                    .background(Colors.background.color)
                    .padding(24.dp)
                    .fillMaxWidth()) {

                MyTextField(vM.title,title,"The marker name")
                MyTextField(vM.snippet,description,"The marker description")
                MyDropMenuTipus(vM)



                Button(onClick = {
                    try {
                        vM.newMarker(LatLng(vM.lat.value!!.toDouble(),vM.lng.value!!.toDouble()),vM.title.value!!,vM.snippet.value!!,vM.tipus.value!!)
                        vM.show.value = false
                    } catch (e: Exception) {
                        println(e.message)
                    }finally {
                        vM.title.value = ""
                        vM.snippet.value = ""
                        vM.tipus.value = ""
                    }
                },Modifier.fillMaxWidth()) {
                    Text("Create Marker")
                }

            }
        }
    }
}

@Composable
fun MyTextField (title:MutableLiveData<String>,value:State<String>,m1:String){
    TextField(value = value.value, onValueChange = { title.value = it },
        label = { Text(text = m1) },
    )
}


