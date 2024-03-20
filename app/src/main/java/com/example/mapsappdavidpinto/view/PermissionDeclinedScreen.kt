package com.example.mapsappdavidpinto.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PermissionDeclinedScreen() {
    val context = LocalContext.current
    Column (
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ,modifier = Modifier.fillMaxSize()
    )
    {
        Text("Permission required", fontWeight = FontWeight.Bold)
        Text("This app needs acess to the camera to take photos")
        Button(onClick = {
            openAppSettings(context as Activity)
        }) {
            Text("Accept")
        }
    }
}

fun openAppSettings(activity:Activity) {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package",activity.packageName,null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    activity.startActivity(intent)
}