package com.example.mapsappdavidpinto.controllers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsappdavidpinto.ui.theme.MapsAppDavidPintoTheme
import com.example.mapsappdavidpinto.view.AddMarkerScreen
import com.example.mapsappdavidpinto.view.GalleryScreen
import com.example.mapsappdavidpinto.view.LoginScreen
import com.example.mapsappdavidpinto.view.MapScreen
import com.example.mapsappdavidpinto.view.MarkerListScreen
import com.example.mapsappdavidpinto.view.MenuScreen
import com.example.mapsappdavidpinto.view.PermissionDeclinedScreen
import com.example.mapsappdavidpinto.view.RegusterScreen
import com.example.mapsappdavidpinto.view.SplashScreen
import com.example.mapsappdavidpinto.view.TakePhotoScreen
import com.example.mapsappdavidpinto.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vM by viewModels<MainViewModel> ()
        setContent {
            MapsAppDavidPintoTheme {
                Body(vM)
            }
        }
    }
}

@Composable
fun Body(vM:MainViewModel) {
    val navigationController = rememberNavController()
    NavHost(navController = navigationController, startDestination = Routes.SplashScreen.route) {
        composable(Routes.SplashScreen.route) { SplashScreen(navigationController,vM) }
        composable(Routes.LoginScreen.route) { LoginScreen(navigationController,vM) }
        composable(Routes.MenuScreen.route) { MenuScreen(navigationController,vM) }
        composable(Routes.RegusterScreen.route) { RegusterScreen(navigationController,vM) }
        composable(Routes.MapScreen.route) { MapScreen(navigationController,vM) }
        composable(Routes.MarkerListScreen.route) { MarkerListScreen(navigationController,vM) }
        composable(Routes.AddMarkerScreen.route) { AddMarkerScreen(navigationController,vM) }
        composable(Routes.PermissionDeclinedScreen.route) { PermissionDeclinedScreen() }
        composable(Routes.TakePhotoScreen.route) { TakePhotoScreen(navigationController,vM) }
        composable(Routes.GalleryScreen.route) { GalleryScreen(vM,navigationController) }
    }
}

