package com.example.farmos.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
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
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
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
    val DMSans = FontFamily(Font(R.font.dm_sans))

    val farmName = "Basappa’s Farm"
    val currentDate = SimpleDateFormat("MMMM dd").format(Date())
    val images = listOf(
        R.drawable.one, R.drawable.two, R.drawable.three,
        R.drawable.four, R.drawable.five
    )
    var currentImageIndex by remember { mutableStateOf(0) }

    var showVirtualFarmDialog by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showTrendsSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header
            Text(
                text = "$farmName • $currentDate",
                fontFamily = DMSans,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B4332),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        pushNotification(context = context, title = "Disease Alert")
                    }
            )
            // Small, black instruction
            Text(
                text = "Your digital farm dashboard: always-on, no-nonsense, and friendlier than Basappa’s neighbor.",
                fontFamily = DMSans,
                fontSize = 12.sp,
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            // Farm Timeline title
            Text(
                text = "Farm Timeline (tap to browse stages)",
                fontFamily = DMSans,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF388E3C),
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
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
            // Small, black caption
            Text(
                text = "Each photo is a memory, each day is a chapter in your farm's story.",
                fontFamily = DMSans,
                fontSize = 11.sp,
                color = Color.Black,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            // Virtual Farm button
            Button(
                onClick = { showVirtualFarmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(27.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
            ) {
                Text(
                    "Open Your Virtual Farm",
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = DMSans
                )
            }

            // Insights Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InsightCard("Weather", "28°C\nSunny", DMSans)
                InsightCard("Harvest", "45 days", DMSans)
                InsightCard("Next Manure", "7 days", DMSans)
            }

            // Schedules
            ScheduleSection(
                title = "🪱 Manure Schedule",
                lastDone = "2 weeks ago",
                nextDue = "In 7 days",
                recommendation = "Feed the soil: NPK 20-20-20 plus compost for lush, happy roots.",
                fontFamily = DMSans
            )
            ScheduleSection(
                title = "🦟 Pesticide Schedule",
                lastDone = "5 days ago",
                nextDue = "In 9 days",
                recommendation = "Neem oil + Monocrotophos (1ml/lit) = bugs go packing, plants stay smiling.",
                fontFamily = DMSans
            )

            // Trends Button
            Button(
                onClick = { showTrendsSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(27.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
            ) {
                Text("View Historical Trends", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = DMSans)
            }

            // AI Chat Button
            Button(
                onClick = { navController.navigate(nav.chatModel) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(27.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C))
            ) {
                Text("Talk to FarmAI", color = Color.White, fontWeight = FontWeight.Bold, fontFamily = DMSans)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Bottom FABs — now TRIPLE: Voice, Camera, Harvest!
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FloatingActionButton(
                onClick = { /* Voice with Gemini coming soon */ },
                containerColor = Color(0xFFD4AF37),
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Voice Chat")
            }
            FloatingActionButton(
                onClick = { navController.navigate(nav.cropScan) },
                containerColor = Color(0xFFD4AF37),
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Scan Crop")
            }
            FloatingActionButton(
                onClick = { navController.navigate(nav.harvest) }, // <-- Add this route!
                containerColor = Color(0xFFD4AF37),
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Money, contentDescription = "Harvest")
            }
        }
    }

    // Virtual Farm Dialog
    if (showVirtualFarmDialog) {
        AlertDialog(
            onDismissRequest = { showVirtualFarmDialog = false },
            confirmButton = {
                TextButton(onClick = { showVirtualFarmDialog = false }) {
                    Text("Close", fontFamily = DMSans)
                }
            },
            title = {
                Text(
                    text = "Your Virtual Farm",
                    fontFamily = DMSans,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Here you’ll find a smart snapshot of your farm’s health, growth, and upcoming actions. FarmOS keeps you two steps ahead—no crystal ball needed!",
                        fontFamily = DMSans,
                        fontSize = 12.sp,
                        color = Color.Black,
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
            HistoricalTrendsContent(DMSans)
        }
    }
}

// ========== Reusable Components (Styled for DM Sans) ==========

@Composable
fun HistoricalTrendsContent(fontFamily: FontFamily) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Historical Trends",
            fontFamily = fontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        val priceData = listOf(1500f, 1800f, 1700f, 2100f, 2000f)
        val rainfallData = listOf(30f, 50f, 40f, 60f, 55f)
        val yieldData = listOf(2f, 2.5f, 2.8f, 3f, 2.9f)

        Text("Price Trend (₹/ton)", fontWeight = FontWeight.SemiBold, fontFamily = fontFamily, fontSize = 14.sp)
        MPLineChart("Price", priceData, AndroidColor.rgb(244, 180, 0))

        Text("Rainfall Trend (mm)", fontWeight = FontWeight.SemiBold, fontFamily = fontFamily, fontSize = 14.sp)
        MPLineChart("Rainfall", rainfallData, AndroidColor.rgb(0, 150, 136))

        Text("Yield Trend (ton/acre)", fontWeight = FontWeight.SemiBold, fontFamily = fontFamily, fontSize = 14.sp)
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
fun InsightCard(title: String, value: String, fontFamily: FontFamily) {
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
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4CAF50), fontFamily = fontFamily)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32), fontFamily = fontFamily)
        }
    }
}

fun pushNotification(context: Context, title: String, message: String = "A crop disease is spreading in nearby farms. Take preventive action!") {
    val channelId = "disease_alerts"
    val notificationId = 1001

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Disease Alerts"
        val descriptionText = "Notifications for nearby farm disease outbreaks"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.stat_sys_warning)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setAutoCancel(true)
        .setDefaults(NotificationCompat.DEFAULT_ALL)

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, builder.build())
    }
}

@Composable
fun ScheduleSection(
    title: String,
    lastDone: String,
    nextDue: String,
    recommendation: String,
    fontFamily: FontFamily
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FBE7))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF33691E), fontFamily = fontFamily)
            Spacer(Modifier.height(4.dp))
            Text("Last Done: $lastDone", color = Color(0xFF558B2F), fontSize = 12.sp, fontFamily = fontFamily)
            Text("Next Due: $nextDue", color = Color(0xFF558B2F), fontSize = 12.sp, fontFamily = fontFamily)
            Spacer(Modifier.height(4.dp))
            Text("💡 $recommendation", fontSize = 13.sp, color = Color(0xFF33691E), fontFamily = fontFamily)
        }
    }
}
