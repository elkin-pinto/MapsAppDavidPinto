package com.example.mapsappdavidpinto.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mapsappdavidpinto.controllers.Routes
import com.example.mapsappdavidpinto.viewModel.MainViewModel


@Composable
fun LoginScreen(navController: NavController, vM: MainViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val userLoggedSuccesful by vM.userLoggingComplete.observeAsState(false)

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(text = "GeoApp", fontSize = 30.sp, fontFamily = FontFamily.Monospace)
        Spacer(modifier = Modifier.height(20.dp))
        TextField(value = email, onValueChange = { email  = it },  keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ) )
        Spacer(modifier = Modifier.height(15.dp))
        TextField(value = password, onValueChange = { password  = it },  visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.height(25.dp))

        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            OutlinedButton(onClick = {
                vM.login(email, password)

            },
                border = BorderStroke(2.dp, Color.Red),
                modifier = Modifier.padding(8.dp) ) {
                Text(text = "Sign in")
            }
            Spacer(modifier = Modifier.width(25.dp))
            OutlinedButton(onClick = {
                        vM.register(email,password)
            },
                border = BorderStroke(2.dp, Color.Red),
                modifier = Modifier.padding(8.dp)) {
                Text(text = "Register")
            }
            if(userLoggedSuccesful == true) navController.navigate(Routes.MapScreen.route)
        }
    }
}