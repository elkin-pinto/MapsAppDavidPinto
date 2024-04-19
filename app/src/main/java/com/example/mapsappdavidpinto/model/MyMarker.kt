package com.example.mapsappdavidpinto.model

data class MyMarker (
    var markerId: String?,
    var lat:Double,
    var lng:Double,
    var title:String,
    var snippet:String,
    var tipus:String,
    var image:String?,
    var userId:String
    ) {

    constructor(): this(null, 0.0, 0.0,"","","","","")
    constructor(lat:Double,lng:Double,title:String,snippet:String,tipus:String,image:String?,userId: String) : this() {
        this.lat = lat
        this.lng = lng
        this.title = title
        this.snippet = snippet
        this.tipus = tipus
        this.image = image
        this.userId = userId
    }
}