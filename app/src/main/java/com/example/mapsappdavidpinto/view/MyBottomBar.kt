package com.example.mapsappdavidpinto.view


import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mapsappdavidpinto.model.BottomNavigationScreens

@Composable
fun MyBottomBar(
    navigationCOntroller: NavController,
    bottomNavigationitems: List<BottomNavigationScreens>
) {
    BottomNavigation(backgroundColor = Color.Red) {
        val navBackEntry by navigationCOntroller.currentBackStackEntryAsState()
        val currentRoute = navBackEntry?.destination?.route
        bottomNavigationitems.forEach {
            BottomNavigationItem (
                icon = { Icon(it.icon, it.label) },
                label = { Text(it.label) },
                selected = currentRoute == it.route,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Black,
                alwaysShowLabel = false,
                onClick = {
                    if (currentRoute != it.route) navigationCOntroller.navigate(it.route)
                }
            )
        }
    }
}