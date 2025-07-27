package com.example.farmos.data

data class HistoricalTrend(
    val year: Int,
    val pricePerTon: Int,
    val rainfallMm: Int,
    val yieldTonPerAcre: Int,
    val disease: String
)

data class CropSuggestion(
    val name: String,
    val expectedYieldTonPerAcre: Int,
    val expectedPricePerTon: Int,
    val reason: String,
    val historicalTrend: List<HistoricalTrend>,
    val iconUrl: String
)

val mockCropSuggestions = listOf(
    CropSuggestion(
        name = "Paddy (Rice)",
        expectedYieldTonPerAcre = 22,
        expectedPricePerTon = 18000,
        reason = "High water availability in Lucknow makes Paddy ideal for Kharif season with stable market demand.",
        iconUrl = "https://cdn-icons-png.flaticon.com/512/765/765514.png",
        historicalTrend = listOf(
            HistoricalTrend(2019, 17000, 850, 20, "Bacterial leaf blight"),
            HistoricalTrend(2020, 16000, 780, 19, "Brown spot"),
            HistoricalTrend(2021, 18500, 820, 21, "No major outbreak"),
            HistoricalTrend(2022, 19000, 870, 22, "False smut (mild)"),
            HistoricalTrend(2023, 18000, 830, 22, "No major outbreak")
        )
    ),
    CropSuggestion(
        name = "Sugarcane",
        expectedYieldTonPerAcre = 40,
        expectedPricePerTon = 3200,
        reason = "Sugar mills in the region ensure easy market access, and irrigation supports year-long growth.",
        iconUrl = "https://cdn-icons-png.flaticon.com/512/4252/4252921.png",
        historicalTrend = listOf(
            HistoricalTrend(2019, 3100, 850, 38, "Red rot (mild)"),
            HistoricalTrend(2020, 3000, 780, 36, "Pyrilla pest"),
            HistoricalTrend(2021, 3200, 820, 39, "No major outbreak"),
            HistoricalTrend(2022, 3300, 870, 40, "No major outbreak"),
            HistoricalTrend(2023, 3200, 830, 40, "No major outbreak")
        )
    ),
    CropSuggestion(
        name = "Pigeon Pea (Arhar)",
        expectedYieldTonPerAcre = 8,
        expectedPricePerTon = 60000,
        reason = "High protein demand and soil enrichment benefits make Arhar a profitable pulse crop for farmers.",
        iconUrl = "https://cdn-icons-png.flaticon.com/512/415/415733.png",
        historicalTrend = listOf(
            HistoricalTrend(2019, 55000, 850, 7, "Wilt (mild)"),
            HistoricalTrend(2020, 50000, 780, 6, "Pod borer"),
            HistoricalTrend(2021, 62000, 820, 7, "Sterility mosaic virus"),
            HistoricalTrend(2022, 58000, 870, 8, "No major outbreak"),
            HistoricalTrend(2023, 60000, 830, 8, "No major outbreak")
        )
    )
)
