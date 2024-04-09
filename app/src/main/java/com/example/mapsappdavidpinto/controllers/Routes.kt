package com.example.mapsappdavidpinto.controllers

sealed class Routes (val route: String) {
    object SplashScreen:Routes("SplashScreen")
    object LoginScreen:Routes("LoginScreen")
    object MenuScreen:Routes("MenuScreen")
    object MapScreen:Routes("MapScreen")
    object MarkerListScreen:Routes("MarkerListScreen")
    object AddMarkerScreen:Routes("AddMarkerListScreen")
    object PermissionDeclinedScreen:Routes("PermissionDeclinedScreen")
    object TakePhotoScreen:Routes("TakePhotoScreen")
    object GalleryScreen:Routes("GalleryScreen")
}