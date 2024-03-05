package com.example.mapsappdavidpinto.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mapsappdavidpinto.viewModel.MainViewModel

@Composable
fun MapScreen(navController: NavController, vM: MainViewModel) {
    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        val itb = LatLng(41.45)
    }
}