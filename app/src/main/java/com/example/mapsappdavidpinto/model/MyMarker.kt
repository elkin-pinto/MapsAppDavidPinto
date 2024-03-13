package com.example.mapsappdavidpinto.model

import com.google.android.gms.maps.model.LatLng

class MyMarker {
    val state:LatLng
    val title:String
    val snippet:String
    val tipus:String
    constructor(state:LatLng,title:String,snippet:String,tipus:String) {
        this.state = state
        this.title = title
        this.snippet = snippet
        this.tipus = tipus
    }
}