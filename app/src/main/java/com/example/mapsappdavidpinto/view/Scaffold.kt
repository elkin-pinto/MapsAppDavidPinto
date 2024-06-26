package com.example.mapsappdavidpinto.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffold(function: @Composable () -> Unit,state:DrawerState) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("GeoMap") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { state.open() } }) {
                        Icon(Icons.Filled.Menu,"Menu")
                    }
                }
            )
        }
    ) {
        Box (modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            function()
        }
    }
}

