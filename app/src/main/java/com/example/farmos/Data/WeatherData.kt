package com.example.farmos.data

data class HistoricalTrends(
    val year: Int,
    val pricePerTon: Int,
    val rainfallMm: Int,
    val yieldTonPerAcre: Int,
    val disease: String
)

data class CropData(

    val historicalTrend: List<HistoricalTrends>,
    val iconUrl: String
)
val mockWeatherData = listOf(
    CropData(

        iconUrl = "https://daivikorganic.com/cdn/shop/products/4-3_9897a324-03c5-447c-9111-dc7f29f7ed40.png?v=1670567337",
        historicalTrend = listOf(
            HistoricalTrends(2019, 17000, 850, 20, "Bacterial leaf blight"),
            HistoricalTrends(2020, 16000, 780, 19, "Brown spot"),
            HistoricalTrends(2021, 18500, 820, 21, "No major outbreak"),
            HistoricalTrends(2022, 19000, 870, 22, "False smut (mild)"),
            HistoricalTrends(2023, 18000, 830, 22, "No major outbreak")
        )
    ),
    CropData(
        iconUrl = "https://5.imimg.com/data5/SELLER/Default/2023/8/334836385/UJ/RM/IO/23524314/yellow-corn-maize.jpg",
        historicalTrend = listOf(
            HistoricalTrends(2019, 3100, 850, 38, "Red rot (mild)"),
            HistoricalTrends(2020, 3000, 780, 36, "Pyrilla pest"),
            HistoricalTrends(2021, 3200, 820, 39, "No major outbreak"),
            HistoricalTrends(2022, 3300, 870, 40, "No major outbreak"),
            HistoricalTrends(2023, 3200, 830, 40, "No major outbreak")
        )
    ),
    CropData(
        iconUrl = "https://m.media-amazon.com/images/I/51wy7m+ZLOS._UF1000,1000_QL80_.jpg",
        historicalTrend = listOf(
            HistoricalTrends(2019, 55000, 850, 7, "Wilt (mild)"),
            HistoricalTrends(2020, 50000, 780, 6, "Pod borer"),
            HistoricalTrends(2021, 62000, 820, 7, "Sterility mosaic virus"),
            HistoricalTrends(2022, 58000, 870, 8, "No major outbreak"),
            HistoricalTrends(2023, 60000, 830, 8, "No major outbreak")
        )),
    CropData(
        iconUrl = "https://5.imimg.com/data5/IA/CS/MY-35371982/paddy-rice.jpg",
        historicalTrend = listOf(
            HistoricalTrends(2019, 55000, 850, 7, "Wilt (mild)"),
            HistoricalTrends(2020, 50000, 780, 6, "Pod borer"),
            HistoricalTrends(2021, 62000, 820, 7, "Sterility mosaic virus"),
            HistoricalTrends(2022, 58000, 870, 8, "No major outbreak"),
            HistoricalTrends(2023, 60000, 830, 8, "No major outbreak")
        )
    ),
CropData(

        iconUrl = "https://daivikorganic.com/cdn/shop/products/4-3_9897a324-03c5-447c-9111-dc7f29f7ed40.png?v=1670567337",
        historicalTrend = listOf(
            HistoricalTrends(2019, 17000, 850, 20, "Bacterial leaf blight"),
            HistoricalTrends(2020, 16000, 780, 19, "Brown spot"),
            HistoricalTrends(2021, 18500, 820, 21, "No major outbreak"),
            HistoricalTrends(2022, 19000, 870, 22, "False smut (mild)"),
            HistoricalTrends(2023, 18000, 830, 22, "No major outbreak")
        )
    ),
    CropData(
        iconUrl = "https://5.imimg.com/data5/SELLER/Default/2023/8/334836385/UJ/RM/IO/23524314/yellow-corn-maize.jpg",
        historicalTrend = listOf(
            HistoricalTrends(2019, 3100, 850, 38, "Red rot (mild)"),
            HistoricalTrends(2020, 3000, 780, 36, "Pyrilla pest"),
            HistoricalTrends(2021, 3200, 820, 39, "No major outbreak"),
            HistoricalTrends(2022, 3300, 870, 40, "No major outbreak"),
            HistoricalTrends(2023, 3200, 830, 40, "No major outbreak")
        )
    ),
    CropData(
        iconUrl = "https://m.media-amazon.com/images/I/51wy7m+ZLOS._UF1000,1000_QL80_.jpg",
        historicalTrend = listOf(
            HistoricalTrends(2019, 55000, 850, 7, "Wilt (mild)"),
            HistoricalTrends(2020, 50000, 780, 6, "Pod borer"),
            HistoricalTrends(2021, 62000, 820, 7, "Sterility mosaic virus"),
            HistoricalTrends(2022, 58000, 870, 8, "No major outbreak"),
            HistoricalTrends(2023, 60000, 830, 8, "No major outbreak")
        )),
    CropData(
        iconUrl = "https://5.imimg.com/data5/IA/CS/MY-35371982/paddy-rice.jpg",
        historicalTrend = listOf(
            HistoricalTrends(2019, 55000, 850, 7, "Wilt (mild)"),
            HistoricalTrends(2020, 50000, 780, 6, "Pod borer"),
            HistoricalTrends(2021, 62000, 820, 7, "Sterility mosaic virus"),
            HistoricalTrends(2022, 58000, 870, 8, "No major outbreak"),
            HistoricalTrends(2023, 60000, 830, 8, "No major outbreak")
        )
    ))
