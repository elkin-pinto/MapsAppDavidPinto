package com.example.mapsappdavidpinto.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.mapsappdavidpinto.controllers.Routes
import com.example.mapsappdavidpinto.viewModel.MainViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun NewMarker(vM: MainViewModel,navController: NavController,function: @Composable () -> Unit){
    val title = vM.title.observeAsState("")
    val description = vM.snippet.observeAsState("")
    Column(
        Modifier
            .background(Color.Transparent)
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        MyTextField(vM.title,title,"The marker name")
        MyTextField(vM.snippet,description,"The marker description")
        MyDropMenuTipus(vM)

        function()

        Button(onClick = {
            try {
                vM.newMarker(LatLng(vM.lat.value!!.toDouble(),vM.lng.value!!.toDouble()),vM.title.value!!,vM.snippet.value!!,vM.tipus.value!!)
                vM.latPosition = vM.lat.value!!.toDouble()
                vM.lngPosition = vM.lng.value!!.toDouble()
                vM.show.value = false
                navController.navigate(Routes.MapScreen.route)
            } catch (e: Exception) {
                println(e.message)
            }finally {
                vM.lat.value = ""
                vM.lng.value = ""
                vM.title.value = ""
                vM.snippet.value = ""
                vM.tipus.value = ""
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