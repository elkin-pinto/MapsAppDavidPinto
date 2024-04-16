package com.example.mapsappdavidpinto.model

import com.google.android.gms.maps.model.LatLng

data class MyMarker (
    var markerId: String?,
    var state:LatLng,
    var title:String,
    var snippet:String,
    var tipus:String,
    var image:String?,
    var userId:String
    ) {

    constructor(): this(null, LatLng(0.0, 0.0),"","","","","")
    constructor(state:LatLng,title:String,snippet:String,tipus:String,image:String?,userId: String) : this() {
        this.state = state
        this.title = title
        this.snippet = snippet
        this.tipus = tipus
        this.image = image
        this.userId = userId
    }
}