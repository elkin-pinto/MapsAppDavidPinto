package com.example.mapsappdavidpinto.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.mapsappdavidpinto.model.BottomNavigationScreens

@Composable
fun MyScaffold(
    navigationCOntroller: NavController,
    bottomNavigationitems: List<BottomNavigationScreens>,
    function: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = { MyBottomBar(navigationCOntroller, bottomNavigationitems) }
    ) {
        Box (modifier = Modifier.fillMaxSize().padding(it)) {
            function()
        }
    }
}

