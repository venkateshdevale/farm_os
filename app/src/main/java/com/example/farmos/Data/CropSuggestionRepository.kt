package com.example.farmos.Data

import android.util.Log
import com.example.farmos.network.CropSuggestionRequest
import com.example.farmos.network.CropSuggestionResponse
import com.example.farmos.network.RetrofitClient

class CropSuggestionRepository {
    private val cropSuggestionService = RetrofitClient.instance

    suspend fun getCropSuggestion(request: CropSuggestionRequest): CropSuggestionResponse {
        Log.d("SHabaz", request.toString())
        return cropSuggestionService.getCropSuggestion(request)
    }
}
