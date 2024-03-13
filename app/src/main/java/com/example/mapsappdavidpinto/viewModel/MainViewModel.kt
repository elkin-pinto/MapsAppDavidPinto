package com.example.mapsappdavidpinto.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsappdavidpinto.R
import com.example.mapsappdavidpinto.model.BottomNavigationScreens
import com.example.mapsappdavidpinto.model.MyMarker
import com.google.android.gms.maps.model.LatLng
import java.util.regex.Pattern

class MainViewModel:ViewModel() {
    val icon = R.drawable.splash_screen_icon

    private val _markers = MutableLiveData<List<MyMarker>>(listOf(MyMarker(LatLng(41.4534265,2.1837151),"itb","Marker at itb","escola")))
    val markers = _markers
    private val _markersList = MutableLiveData<List<MyMarker>>()
    val markersList = _markersList
    var show = MutableLiveData(false)

    //SearchBar
    var searchBarMarkersList = MutableLiveData("")


    //TipusMarkers
    val tipusMarkerList = listOf("","escola")

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
    var tipus = MutableLiveData<String>("")


    fun newMarker(state: LatLng, title:String,snippet:String,tipus:String) {
        val newMarker = _markers.value?.toMutableList()
        newMarker?.add(MyMarker(state,title, snippet,tipus))
        _markers.value = newMarker!!
    }

    fun searchMarkers(value:String) {
        val patter = Pattern.compile("${value.toLowerCase()}.*")
        try {
            val listmarkers = mutableListOf<MyMarker>()
            for(i in markers.value!!){
                if ((tipus.value == "" || i.tipus == tipus.value) && patter.matcher(i.title).matches()) {
                    listmarkers.add(i)
                }
            }
            _markersList.value = listmarkers

        } catch (_:Exception) {}
    }


}
