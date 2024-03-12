package com.example.mapsappdavidpinto.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsappdavidpinto.R
import com.example.mapsappdavidpinto.model.BottomNavigationScreens
import com.example.mapsappdavidpinto.model.MyMarker
import com.google.android.gms.maps.model.LatLng

class MainViewModel:ViewModel() {
    val icon = R.drawable.splash_screen_icon

    private val _markers = MutableLiveData<List<MyMarker>>(listOf(MyMarker(LatLng(41.4534265,2.1837151),"itb","Marked at itb")))
    val markers = _markers
    var show = MutableLiveData(false)


    //Bottom Navigation
    val bottomNavigationItems = listOf(
        BottomNavigationScreens.Home,
        BottomNavigationScreens.MarkerList
    )


    // Dialog MapScreen
    var lat = MutableLiveData<String>("")
    var lng = MutableLiveData<String>("")
    var title = MutableLiveData<String>("")
    var snippet = MutableLiveData<String>("")


    fun newMarker(state: LatLng, title:String,snippet:String) {
        val newMarker = _markers.value?.toMutableList()
        newMarker?.add(MyMarker(state,title, snippet))
        _markers.value = newMarker!!
    }
}
