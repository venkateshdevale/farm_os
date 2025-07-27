package com.example.farmos.network

import retrofit2.http.Body
import retrofit2.http.POST

interface FarmOS {
    @POST("crop/suggest")
    suspend fun getCropSuggestion(@Body request: CropSuggestionRequest): CropSuggestionResponse
}
