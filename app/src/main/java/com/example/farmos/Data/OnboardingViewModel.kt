package com.example.farmos.Data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class OnboardingViewModel : ViewModel() {
    var farmName by mutableStateOf("Basappa Farm")
    var district by mutableStateOf("")
    var state by mutableStateOf("")
    var areaInAcres by mutableStateOf(0.0)
    var farmPolygon by mutableStateOf<List<LatLng>>(emptyList())
}
