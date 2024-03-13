package com.example.mapsappdavidpinto.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mapsappdavidpinto.controllers.Routes

@Composable
fun AddMarkerButton(navHost:NavController) {
    Box(contentAlignment = Alignment.Center) {
        Icon(Icons.Default.AddCircle, "Add Marker Cirlce" , Modifier.size(60.dp).clickable {  navHost.navigate(Routes.AddMarkerListScreen.route) },Color.Green)
        Icon(Icons.Default.Add, "Add Marker Cross" , Modifier.size(45.dp).clickable {  navHost.navigate(Routes.AddMarkerListScreen.route) },Color.White)

    }
}