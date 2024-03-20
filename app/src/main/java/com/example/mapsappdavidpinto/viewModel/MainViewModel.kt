package com.example.mapsappdavidpinto.viewModel

import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsappdavidpinto.R
import com.example.mapsappdavidpinto.model.BottomNavigationScreens
import com.example.mapsappdavidpinto.model.MyMarker
import com.google.android.gms.maps.model.LatLng
import java.util.regex.Pattern

class MainViewModel:ViewModel() {
    val icon = R.drawable.splash_screen_icon

    private val _markers = MutableLiveData<List<MyMarker>>(listOf(MyMarker(LatLng(41.4534265,2.1837151),"itb","Marker at itb","escola",R.drawable.empty_image.toDrawable().toBitmap())))
    val markers = _markers
    private val _markersList = MutableLiveData<List<MyMarker>>()
    val markersList = _markersList
    var show = MutableLiveData(false)


    //Map Position Values
    var latPosition = 41.4534265
    var lngPosition = 2.1837151

    //SearchBar
    var searchBarMarkersList = MutableLiveData("")

    //Camera
    val cameraPermissionGranted = MutableLiveData(false)
    val shouldShowPermissionRationale = MutableLiveData(false)
    val showPermissionDenied = MutableLiveData(false)

    //Map Dialog
    var mapDialog = MutableLiveData(false)

    //TipusMarkers
    val tipusMarkerList = mutableListOf("","escola")

    //Bottom Navigation
    val bottomNavigationItems = listOf(
        BottomNavigationScreens.Home,
        BottomNavigationScreens.MarkerList
    )
    //

    // Dialog MapScreen
    var lat = MutableLiveData(0.0)
    var lng = MutableLiveData(0.0)
    var title = MutableLiveData<String>("")
    var snippet = MutableLiveData<String>("")
    var tipus = MutableLiveData<String>("")

    // Marker Image
    var image = MutableLiveData<Bitmap>(R.drawable.empty_image.toDrawable().toBitmap())

    // Maker Detail
    lateinit var markerSelected:MyMarker




    fun newMarker(state: LatLng, title:String,snippet:String,tipus:String,image:Bitmap) {
        val newMarker = _markers.value?.toMutableList()
        newMarker?.add(MyMarker(state,title, snippet,tipus,image))
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
