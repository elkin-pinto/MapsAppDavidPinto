package com.example.mapsappdavidpinto.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsappdavidpinto.R
import com.example.mapsappdavidpinto.controllers.Routes
import com.example.mapsappdavidpinto.model.FirebaseRep
import com.example.mapsappdavidpinto.model.MyMarker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern

class MainViewModel:ViewModel() {
    val icon = R.drawable.splash_screen_icon

    private val _markers = MutableLiveData(emptyList<MyMarker>())
    val markers = _markers
    private val _markersList = MutableLiveData<List<MyMarker>>()
    val markersList = _markersList
    var show = MutableLiveData(false)


    //Routes
    var currentRoute = MutableLiveData<String>(Routes.MapScreen.route)

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
    val tipusSelected = MutableLiveData<String>("")

    // Dialog MapScreen
    var lat = MutableLiveData(0.0)
    var lng = MutableLiveData(0.0)
    var title = MutableLiveData<String>("")
    var snippet = MutableLiveData<String>("")
    var tipus = MutableLiveData<String>("")

    // Marker Image
    var image = MutableLiveData<String?>()

    // Maker Detail

    fun newMarker(marker: MyMarker) {
        val newMarker = _markers.value?.toMutableList()
        newMarker?.add(MyMarker(marker.lat,marker.lng,marker.title, marker.snippet,marker.tipus,marker.image,marker.userId))
        _markers.value = newMarker!!
    }

    fun searchMarkers(value:String) {
        val patter = Pattern.compile("${value.toLowerCase()}.*")
        try {
            val listmarkers = mutableListOf<MyMarker>()
            for(i in markers.value!!){
                if ((tipusSelected.value == "" || i.tipus == tipusSelected.value) && patter.matcher(i.title).matches()) {
                    listmarkers.add(i)
                }
            }
            _markersList.value = listmarkers

        } catch (_:Exception) {}
    }

    //Firebase
    private val firebaseRep = FirebaseRep()

    val actualMarker = MutableLiveData<MyMarker?>()

    private fun getMarkers() {
        firebaseRep.getMarkers().whereEqualTo("userId",_userId.value).addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore.error", error.message.toString())
                return@addSnapshotListener
            }
            val tempList = mutableListOf<MyMarker>()
            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    val newMarker = dc.document.toObject(MyMarker::class.java)
                    newMarker.markerId = dc.document.id
                    tempList.add(newMarker)
                }
            }
            _markers.value = tempList
        }
    }

    fun getMarker(markerId:String) {
        firebaseRep.getMarker(markerId).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("UserRepository","Listen failed", error)
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                val marker = value.toObject(MyMarker::class.java)
                if (marker != null) {
                    marker.markerId = markerId
                    if(marker.tipus in tipusMarkerList) tipusMarkerList.add(marker.tipus)
                }
                actualMarker.value = marker
                title.value = actualMarker.value!!.title
                lat.value = actualMarker.value!!.lat
                lng.value = actualMarker.value!!.lng
                snippet.value = actualMarker.value!!.snippet
                image.value = actualMarker.value!!.image
                tipus.value = actualMarker.value!!.tipus
            } else {
                Log.e("UserRepository", "Current data: null")
            }
        }
    }
    fun addMarker(marker:MyMarker) {
        this.addMarkerRep(marker)
        this.getMarkers()
    }
    private fun addMarkerRep(marker:MyMarker) = firebaseRep.addMarker(marker)

    fun editMarker(marker:MyMarker) {
        this.editMarkerRep(marker)
        this.getMarkers()
    }
    private fun editMarkerRep(marker:MyMarker) = firebaseRep.editMarker(marker)

    fun deleteMarker(markerId:String) {
        this.deleteMarkerRep(markerId)
        this.getMarkers()
    }
    private fun deleteMarkerRep(markerId:String) = firebaseRep.deleteMarker(markerId)


    // Authentication
    private val auth = FirebaseAuth.getInstance()

    private val _goToNext = MutableLiveData<Boolean>()
    val userLoggingComplete = MutableLiveData(false)

    private fun modifiyProcessing() {
        userLoggingComplete.value = true
    }
    fun register(username: String, password: String) {
        auth.createUserWithEmailAndPassword(username,password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    _goToNext.value = false
                    this.getMarkers()
                    modifiyProcessing()
                } else {
                    Log.d("Error","Error creating user ${task.exception}")
                }

            }
    }

    val _userId = MutableLiveData<String>()
    val _loggedUser = MutableLiveData<String>()

    fun login(username:String?, password: String?) {
        auth.signInWithEmailAndPassword(username!!,password!!)
            .addOnSuccessListener {task ->
                _userId.value = task.user?.uid
                _loggedUser.value = task.user?.email?.split("@")?.get(0)
                _goToNext.value = true
                this.getMarkers()
                modifiyProcessing()
            }.addOnFailureListener {
                _goToNext.value = false
                Log.d("Error","Error signing in ${it.message}")
            }
    }

    fun logOut() {
        auth.signOut()
    }


//Upload Image

    fun uploadImage(imageUri: Uri) {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("images/$fileName")
        storage.putFile(imageUri)
            .addOnSuccessListener {
                Log.i("IMAGE UPLOAD", "Image uploaded successfully")
                storage.downloadUrl.addOnSuccessListener {
                    Log.i("IMAGE", it.toString())
                    image.value = it.toString()
                }
            }
            .addOnFailureListener {
                Log.i("IMAGE UPLOAD", "Image upload failed")
            }
    }
}
