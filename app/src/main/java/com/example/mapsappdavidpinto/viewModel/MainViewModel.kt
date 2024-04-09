package com.example.mapsappdavidpinto.viewModel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsappdavidpinto.R
import com.example.mapsappdavidpinto.controllers.Routes
import com.example.mapsappdavidpinto.model.BottomNavigationScreens
import com.example.mapsappdavidpinto.model.FirebaseRep
import com.example.mapsappdavidpinto.model.MyMarker
import com.example.mapsappdavidpinto.model.User
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class MainViewModel:ViewModel() {
    val icon = R.drawable.splash_screen_icon

    private val _markers = MutableLiveData<List<MyMarker>>(listOf(MyMarker(LatLng(41.4534265,2.1837151),"itb","Marker at itb","escola",null)))
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
    var image = MutableLiveData<Bitmap>()

    // Maker Detail
    lateinit var markerSelected:MyMarker

    fun newMarker(marker: MyMarker) {
        val newMarker = _markers.value?.toMutableList()
        newMarker?.add(MyMarker(marker.state,marker.title, marker.snippet,marker.tipus,marker.image))
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

    //Firebase
    private val firebaseRep = FirebaseRep()
    private val database = FirebaseFirestore.getInstance()

    val userList = MutableLiveData<List<User>>()
    val actualUser = MutableLiveData<User?>()
    val userName = MutableLiveData<String>()
    val age = MutableLiveData<String>()

    fun getUsers() {
        firebaseRep.getUsers().addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore.error", error.message.toString())
                return@addSnapshotListener
            }
            val tempList = mutableListOf<User>()
            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    val newUser = dc.document.toObject(User::class.java)
                    newUser.userId = dc.document.id
                    tempList.add(newUser)
                }
            }
            userList.value = tempList
        }
    }

    fun getUser(userId:String) {
        firebaseRep.getUser(userId).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("UserRepository","Lsiten failed", error)
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                val user = value.toObject(User::class.java)
                if (user != null) {
                    user.userId = userId
                }
                actualUser.value = user
                userName.value = actualUser.value!!.userName
                age.value = actualUser.value!!.age.toString()
            } else {
                Log.e("UserRepository", "Current data: null")
            }
        }
    }

    fun addUser(user:User) = firebaseRep.addUser(user)

    fun editUser(user:User) = firebaseRep.addUser(user)

    fun deleteUser(userId:String) = firebaseRep.deleteUser(userId)
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
                } else {
                    Log.d("Error","Error creating user ${task.result}")
                }
                modifiyProcessing()
            }
    }

    val _userId = MutableLiveData<String>()
    val _loggedUser = MutableLiveData<String>()

    fun login(username:String?, password: String?) {
        auth.signInWithEmailAndPassword(username!!,password!!)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    _userId.value = task.result.user?.uid
                    _loggedUser.value = task.result.user?.email?.split("@")?.get(0)
                    _goToNext.value = true
                } else {
                    _goToNext.value = false
                    Log.d("Error","Error signing in ${task.result}")
                }
                modifiyProcessing()
            }
    }

    fun logOut() {
        auth.signOut()
    }

    fun insertMarker(marker:MyMarker) {
        database.collection("markers")
            .add(
                hashMapOf(
                    "tittle" to marker.title,
                    "snippet" to marker.snippet,
                    "image" to marker.image
                )
            )
    }

}
