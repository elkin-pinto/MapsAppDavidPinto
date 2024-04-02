package com.example.mapsappdavidpinto.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsappdavidpinto.controllers.Routes
import com.example.mapsappdavidpinto.model.MyMarker
import com.example.mapsappdavidpinto.viewModel.MainViewModel

@Composable
fun MarkerListScreen(navController: NavController, vM: MainViewModel) {
    MyDrawerMenu(vM,navController) { Screen(navController,vM) }
}

@Composable
private fun Screen(navController: NavController,vM:MainViewModel) {
    val markers by vM.markersList.observeAsState(emptyList())
    val tipus by vM.tipus.observeAsState("")
    val text by vM.searchBarMarkersList.observeAsState("")
    vM.searchMarkers(text)
    Column {
        TextField(value = text, onValueChange = {
            vM.searchBarMarkersList.value = it
            vM.searchMarkers(text)}, Modifier.fillMaxWidth())
        MyDropMenuTipus(vM,tipus)
        Spacer(Modifier.height(10.dp))
        Box(Modifier.fillMaxSize()) {
            LazyColumn (horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxWidth()){
                items(markers) {
                    markerItem(it,navController,vM)
                }
            }
            Box(
                Modifier
                    .fillMaxHeight(0.96f)
                    .fillMaxWidth(0.98f), contentAlignment = Alignment.BottomEnd) {
                AddMarkerButton(vM,navController)
            }
        }
    }



}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun markerItem(marker: MyMarker,navController: NavController,vM: MainViewModel) {
    Spacer(Modifier.height(5.dp))
    Box(modifier = Modifier
        .border(BorderStroke(2.dp, Color.Black))
        .fillMaxWidth(0.8f)
        .padding(5.dp)
        .clickable {
            vM.latPosition = marker.state.latitude
            vM.lngPosition = marker.state.longitude
            navController.navigate(Routes.MapScreen.route)
        }
    ) {
        Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(marker.title, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                Text(marker.snippet)
            }
            if (marker.image != null) {
                GlideImage(model = marker.image, contentDescription = "Marker Value", contentScale = ContentScale.Fit, modifier = Modifier.rotate(90f).size(100.dp))
            }
        }
    }
}