package com.example.farmos.ui.screens

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import com.example.farmos.R
import com.example.farmos.nav
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MonitorPage(navController: NavController) {
    // Mock farm name and current date
    val farmName = "Basappa’s Farm"
    val currentDate = SimpleDateFormat("MMMM dd").format(Date())

    // Timeline images
    val images = listOf(
        R.drawable.farms,
        R.drawable.farm_animation_placeholder,
        R.drawable.farms,
        R.drawable.farms,
        R.drawable.farms
    )
    var currentImageIndex by remember { mutableStateOf(0) }

    // Popup & bottom sheet states
    var showVirtualFarmDialog by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showTrendsSheet by remember { mutableStateOf(false) }

    // Gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF4CAF50), Color(0xFFF5F5DC))
                )
            )
            .statusBarsPadding()
    ) {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header: Farm name + date
            Text(
                text = "$farmName – $currentDate",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        pushNotification(context = context,"Disease Alert")
                    }
            )

            // Farm Timeline
            Text(
                text = "Farm Timeline",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD4AF37),
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        currentImageIndex = (currentImageIndex + 1) % images.size
                    }
            ) {
                Image(
                    painter = painterResource(id = images[currentImageIndex]),
                    contentDescription = "Farm Timeline Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Button to open Virtual Farm
            Button(
                onClick = { showVirtualFarmDialog = true },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
            ) {
                Text("Open Your Virtual Farm", color = Color.Black, fontSize = 16.sp)
            }

            // Insights Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InsightCard("Weather", "28°C\nSunny")
                InsightCard("Harvest", "45 days")
                InsightCard("Next Manure", "7 days")
            }

            // Manure & Pesticide sections
            ScheduleSection(
                title = "🪱 Manure Schedule",
                lastDone = "2 weeks ago",
                nextDue = "In 7 days",
                recommendation = "Use NPK 20-20-20 with compost."
            )
            ScheduleSection(
                title = "🧪 Pesticide Schedule",
                lastDone = "5 days ago",
                nextDue = "In 9 days",
                recommendation = "Neem oil + Monocrotophos (1ml/lit)"
            )

            // View Trends Button
            Button(
                onClick = { showTrendsSheet = true },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("View Historical Trends", color = Color.White)
            }

            // Chat Button
            Button(
                onClick = { navController.navigate(nav.chatModel) },
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C))
            ) {
                Text("Talk to FarmAI", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(100.dp)) // space for FABs
        }

        // Bottom FABs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            FloatingActionButton(
                onClick = { /* Voice with Gemini later */ },
                containerColor = Color(0xFFD4AF37),
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Mic")
            }
            FloatingActionButton(
                onClick = { navController.navigate(nav.cropScan) },
                containerColor = Color(0xFFD4AF37),
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Scan Crop")
            }
        }
    }

    // Virtual Farm Dialog
    if (showVirtualFarmDialog) {
        AlertDialog(
            onDismissRequest = { showVirtualFarmDialog = false },
            confirmButton = {
                TextButton(onClick = { showVirtualFarmDialog = false }) {
                    Text("Close")
                }
            },
            title = {
                Text(
                    text = "Your Virtual Farm",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Track growth, weather, and alerts for your farm here.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.farm_animation_placeholder),
                        contentDescription = "Farm Animation",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        )
    }

    // Bottom Sheet for Trends
    if (showTrendsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTrendsSheet = false },
            sheetState = bottomSheetState
        ) {
            HistoricalTrendsContent()
        }
    }
}

@Composable
fun HistoricalTrendsContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Historical Trends",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        val priceData = listOf(1500f, 1800f, 1700f, 2100f, 2000f)
        val rainfallData = listOf(30f, 50f, 40f, 60f, 55f)
        val yieldData = listOf(2f, 2.5f, 2.8f, 3f, 2.9f)

        Text("Price Trend (₹/ton)", fontWeight = FontWeight.SemiBold)
        MPLineChart("Price", priceData, AndroidColor.rgb(244, 180, 0))

        Text("Rainfall Trend (mm)", fontWeight = FontWeight.SemiBold)
        MPLineChart("Rainfall", rainfallData, AndroidColor.rgb(0, 150, 136))

        Text("Yield Trend (ton/acre)", fontWeight = FontWeight.SemiBold)
        MPLineChart("Yield", yieldData, AndroidColor.rgb(76, 175, 80))
    }
}

@Composable
fun MPLineChart(title: String, data: List<Float>, color: Int) {
    val entries = data.mapIndexed { index, value -> Entry(index.toFloat() + 1, value) }
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                val dataSet = LineDataSet(entries, title).apply {
                    this.color = color
                    valueTextColor = AndroidColor.BLACK
                    lineWidth = 2f
                    setCircleColor(color)
                    circleRadius = 4f
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    setDrawFilled(true)
                    fillAlpha = 80
                    fillColor = color
                }
                this.data = LineData(dataSet)
                this.animateX(1000)
                this.description = Description().apply { text = "" }
                axisRight.isEnabled = false
                legend.isEnabled = false
                xAxis.setDrawGridLines(false)
                xAxis.textColor = AndroidColor.BLACK
                axisLeft.textColor = AndroidColor.BLACK
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        update = { chart -> chart.invalidate() }
    )
}

@Composable
fun InsightCard(title: String, value: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .width(110.dp)
            .height(90.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4CAF50))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
        }
    }
}
fun pushNotification(context: Context, title: String, message: String = "A crop disease is spreading in nearby farms. Take preventive action!") {
    val channelId = "disease_alerts"
    val notificationId = 1001

    // Create notification channel (required for API 26+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Disease Alerts"
        val descriptionText = "Notifications for nearby farm disease outbreaks"
        val importance = NotificationManager.IMPORTANCE_HIGH // Heads-up alert
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Build the notification
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.stat_sys_warning) // Replace with your drawable
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setAutoCancel(true)
        .setDefaults(NotificationCompat.DEFAULT_ALL) // Vibrate, sound, lights

    // Show it
    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}

@Composable
fun ScheduleSection(title: String, lastDone: String, nextDue: String, recommendation: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FBE7))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF33691E))
            Spacer(Modifier.height(4.dp))
            Text("Last Done: $lastDone", color = Color(0xFF558B2F))
            Text("Next Due: $nextDue", color = Color(0xFF558B2F))
            Spacer(Modifier.height(4.dp))
            Text("💡 $recommendation", fontSize = 14.sp, color = Color(0xFF33691E))
        }
    }
}
