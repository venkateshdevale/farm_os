package com.example.farmos.Data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class OnboardingViewModel : ViewModel() {

    var farmName by mutableStateOf("Basappa Farm")
    var district by mutableStateOf("Kolar")
    var state by mutableStateOf("Karnataka")
    var areaInAcres by mutableStateOf(0.25)


}
