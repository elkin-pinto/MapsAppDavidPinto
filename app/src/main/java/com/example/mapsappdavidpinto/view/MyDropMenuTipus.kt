package com.example.mapsappdavidpinto.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mapsappdavidpinto.viewModel.MainViewModel

@Composable
fun MyDropMenuTipus (vM:MainViewModel) {
    var expanded by remember { mutableStateOf(false) }
    Column(Modifier.padding(20.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.CenterEnd) {
            TextField(
                value = vM.tipus.value!!,
                onValueChange = { vM.tipus.value = it },
                enabled = false,
                readOnly = true,
                modifier = Modifier
                    .clickable { expanded = true },
                colors = TextFieldDefaults.colors(disabledTextColor = Color.Black)

            )
            Icon(Icons.Filled.ArrowDropDown,  "Arrow Down")
        }


        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, Modifier.weight(1f)) {
            vM.tipusMarkerList.forEach {
                DropdownMenuItem(text = { Text(it) }, onClick = {
                    vM.tipus.value = it
                    expanded = false
                },modifier = Modifier.fillMaxWidth())
            }
        }
    }
}