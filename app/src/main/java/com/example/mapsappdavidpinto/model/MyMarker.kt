package com.example.mapsappdavidpinto.model

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

class MyMarker {
    val state:LatLng
    val title:String
    val snippet:String
    val tipus:String
    val image:Bitmap
    constructor(state:LatLng,title:String,snippet:String,tipus:String,image:Bitmap) {
        this.state = state
        this.title = title
        this.snippet = snippet
        this.tipus = tipus
        this.image = image
    }
}