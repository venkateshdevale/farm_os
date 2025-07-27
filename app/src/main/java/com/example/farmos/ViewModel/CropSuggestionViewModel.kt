package com.example.farmos.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.farmos.Data.CropSuggestionRepository
import com.example.farmos.network.CropSuggestionRequest
import com.example.farmos.network.CropSuggestionResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CropSuggestionViewModel : ViewModel() {

    private val repository = CropSuggestionRepository()

    private val _cropSuggestion = MutableStateFlow<CropSuggestionResponse?>(null)
    val cropSuggestion: StateFlow<CropSuggestionResponse?> = _cropSuggestion

    fun getCropSuggestion(location: String, soilQuality: String, date: String, farm_area: Double, previousCrops: List<String>?) {
        viewModelScope.launch {
            try {
                val request = CropSuggestionRequest(location, soilQuality,  farm_area, date, previousCrops)
                Log.d("SHABAZ", "CropSuggestionAPI Request: $request")
                _cropSuggestion.value = repository.getCropSuggestion(request)
                Log.d("SHAbaz", _cropSuggestion.value.toString())
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
                Log.e("crop/suggest", e.toString())
            }
        }
    }
}
