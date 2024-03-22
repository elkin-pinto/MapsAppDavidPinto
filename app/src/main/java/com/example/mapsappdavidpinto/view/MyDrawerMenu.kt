package com.example.mapsappdavidpinto.view

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.example.mapsappdavidpinto.controllers.Routes
import com.example.mapsappdavidpinto.viewModel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MyDrawerMenu(vM: MainViewModel,navigationController: NavController,function: @Composable () -> Unit) {
    val currentRoute = navigationController.currentDestination?.route
    val scope = rememberCoroutineScope()
    val state = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(drawerState = state, gesturesEnabled = false, drawerContent = {
        ModalDrawerSheet {
            NavigationDrawerItem(label = { Text("Map")}, selected = currentRoute == Routes.MapScreen.route, onClick = {
                scope.launch { state.close() }
                vM.currentRoute.value = Routes.MapScreen.route
                navigationController.navigate(Routes.MapScreen.route)
            })
            HorizontalDivider()
            NavigationDrawerItem(label = { Text("Markers")}, selected = currentRoute == Routes.MarkerListScreen.route, onClick = {
                scope.launch { state.close() }
                vM.currentRoute.value = Routes.MarkerListScreen.route
                navigationController.navigate(Routes.MarkerListScreen.route)
            })
            HorizontalDivider()
            NavigationDrawerItem(label = { Text("Create Marker")}, selected = currentRoute == Routes.AddMarkerScreen.route, onClick = {
                scope.launch { state.close() }
                vM.currentRoute.value = Routes.AddMarkerScreen.route
                navigationController.navigate(Routes.AddMarkerScreen.route)
            })
            HorizontalDivider()
            NavigationDrawerItem(label = { Text("Exit")}, selected = false, onClick = {
                scope.launch { state.close() }
            })
        }
    }) {
        MyScaffold(function = {function() }, state )
    }
}