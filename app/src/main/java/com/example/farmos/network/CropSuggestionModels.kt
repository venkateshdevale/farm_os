package com.example.farmos.network

import com.example.farmos.data.HistoricalTrend
import com.example.farmos.data.HistoricalTrends
import com.google.gson.annotations.SerializedName

data class CropSuggestionRequest(
    val location: String,
    @SerializedName("soil_quality")
    val soilQuality: String,
    val farmArea: Double, //in acres
    val date: String,//MM-DD-YYYY
    @SerializedName("previous_crops")
    val previousCrops: List<String>? = null
)
data class HistoricalTrend(
    val year: Int,
    val pricePerTon: Int,
    val rainfallMm: Int,
    val yieldTonPerAcre: Int,
    val disease: String
)

//CropRecommendation("Wheat", "2.5 tons/acre", "₹20,000", 85),
data class CropInfo (
    @SerializedName("crop_name")
    val name: String,
    val investment: String, //in rupeesz
    val profit: String, //in rupees
    val reasoning: String,
    @SerializedName("confidence_score")
    val confidenceScore: Float,
    val url: String,
    val historicalTrend: List<HistoricalTrends>

)
data class CropSuggestionResponse(
    val crops: List<CropInfo>

)
