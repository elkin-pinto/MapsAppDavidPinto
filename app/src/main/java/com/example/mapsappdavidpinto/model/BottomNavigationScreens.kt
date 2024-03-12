package com.example.mapsappdavidpinto.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mapsappdavidpinto.controllers.Routes

sealed class BottomNavigationScreens(val route:String,val icon:ImageVector, val label:String) {
    object Home:BottomNavigationScreens(Routes.MapScreen.route, Icons.Filled.LocationOn,"Map")
    object MarkerList:BottomNavigationScreens(Routes.MarkerListScreen.route, Icons.Filled.List,"Markers List")
}