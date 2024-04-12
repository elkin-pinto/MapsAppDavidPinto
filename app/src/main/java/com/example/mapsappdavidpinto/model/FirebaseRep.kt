package com.example.mapsappdavidpinto.model

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRep {
    private val database = FirebaseFirestore.getInstance()

    fun addMarker(marker:MyMarker) {
        database.collection("markers").add(
            hashMapOf(
                "title" to marker.title,
                "image" to marker.image,
                "snippet" to marker.snippet,
                "lat" to marker.state.latitude,
                "lng" to marker.state.longitude,
                "tipus" to marker.tipus
            )
        )
    }

    fun editMarker(editedMarker:MyMarker) {
        database.collection("markers").document(editedMarker.markerId!!).set(
            hashMapOf(
                "title" to editedMarker.title,
                "image" to editedMarker.image,
                "snippet" to editedMarker.snippet,
                "lat" to editedMarker.state.latitude,
                "lng" to editedMarker.state.longitude,
                "tipus" to editedMarker.tipus
            )
        )
    }

    fun deleteMarker(markerId: String) {
        database.collection("markers").document(markerId).delete()
    }

    fun getMarkers(): CollectionReference {
        return database.collection("markers")
    }

    fun getMarker(markerId:String): DocumentReference {
        return database.collection("markers").document(markerId)
    }
}