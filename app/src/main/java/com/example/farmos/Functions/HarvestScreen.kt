package com.example.farmos.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farmos.R
import java.text.SimpleDateFormat
import java.util.*

val dmSans = FontFamily(
    Font(R.font.dm_sans, FontWeight.Normal)
)

@Composable
fun HarvestScreen(navController: NavController) {
    val context = LocalContext.current
    val farmName = "Basappa’s Farm"
    val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

    // Simulated checklist state (could be driven by backend)
    var checklist by remember {
        mutableStateOf(
            listOf(
                ChecklistItem("Tools & machinery ready", true),
                ChecklistItem("Labor arranged", false),
                ChecklistItem("Weather okay for harvest", true),
                ChecklistItem("Transport booked", false),
                ChecklistItem("Bags/Baskets clean", true)
            )
        )
    }

    val marketPrice = 2380 // ₹/quintal
    val marketTrend = "+6% this week"
    val bestMarket = "APMC Kolar"
    val lastYearDropMsg = "Last year, prices dipped 2 days after harvest."

    val yieldEstimate = 14.3 // quintals
    val yieldChange = "+8% over last year"
    val aiAdvice = "Hold for 2 days (expected +₹90/quintal)"
    val instantCashMsg = "Need instant cash? Selling now is safe—your call."
    val subsidyAlert = "🎉 You’re eligible for ₹2,000 PM-Kisan bonus. Tap to claim."
    val riskDetected = true
    val riskMsg = "⚠️ Powdery mildew risk high in 3 days due to dew. Consider early harvest or treat now."
    val summaryExport = """
        Farm: $farmName
        Date: $today
        
        ✔ Tools & machinery ready
        ✗ Labor arranged
        ✔ Weather okay for harvest
        ✗ Transport booked
        ✔ Bags/Baskets clean

        Market Price: ₹$marketPrice ($marketTrend)
        Best Market: $bestMarket
        Yield Estimate: $yieldEstimate quintals ($yieldChange)
        Subsidy: ₹2,000 PM-Kisan available
        AI Advice: $aiAdvice
        Risk: ${if (riskDetected) riskMsg else "None"}
    """.trimIndent()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FBE7))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "Harvest Command Center",
            fontFamily = dmSans,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color(0xFF2F4D1D),
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = "$farmName • $today",
            fontFamily = dmSans,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color(0xFF757575),
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = "Get ready to reap maximum profits with zero guesswork. DhartiMitra checks everything, so you focus on the harvest, not the headache.",
            fontFamily = dmSans,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )

        // Checklist Card
        CardSection {
            Text(
                text = "Are You Harvest-Ready?",
                fontFamily = dmSans,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF2F4D1D)
            )
            checklist.forEachIndexed { idx, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = if (item.checked) Icons.Filled.CheckCircle else Icons.Filled.ErrorOutline,
                        contentDescription = null,
                        tint = if (item.checked) Color(0xFF43A047) else Color(0xFFD84315),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = item.label,
                        fontFamily = dmSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
            Text(
                text = "Tip: Tap any checklist item to mark as done.",
                fontFamily = dmSans,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = Color.Black
            )
        }

        // Market Price Dashboard
        CardSection {
            Text(
                text = "Live Market Price",
                fontFamily = dmSans,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF2F4D1D)
            )
            Text(
                text = "₹$marketPrice / quintal",
                fontFamily = dmSans,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color(0xFFD4AF37)
            )
            Text(
                text = "$bestMarket  •  $marketTrend",
                fontFamily = dmSans,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xFF757575)
            )
            Text(
                text = lastYearDropMsg,
                fontFamily = dmSans,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = Color.Black
            )
        }

        // Yield Estimator
        CardSection {
            Text(
                text = "Harvest Yield Estimator",
                fontFamily = dmSans,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF2F4D1D)
            )
            Text(
                text = "$yieldEstimate quintals",
                fontFamily = dmSans,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = Color(0xFF388E3C)
            )
            Text(
                text = yieldChange,
                fontFamily = dmSans,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = Color.Black
            )
            Text(
                text = "Powered by AI and satellite data.",
                fontFamily = dmSans,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = Color.Black
            )
        }

        // Subsidy & Bonus Alerts
        CardSection {
            Text(
                text = "Subsidy & Bonus Alerts",
                fontFamily = dmSans,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF2F4D1D)
            )
            Text(
                text = subsidyAlert,
                fontFamily = dmSans,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color(0xFF01579B),
                modifier = Modifier.padding(top = 2.dp, bottom = 6.dp)
            )
            Button(
                onClick = { /* TODO: Implement subsidy claim logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Money, contentDescription = null, tint = Color.Black)
                Spacer(Modifier.width(8.dp))
                Text("Claim Now", color = Color.Black, fontFamily = dmSans, fontWeight = FontWeight.Bold)
            }
        }

        // AI Advisor Card
        CardSection {
            Text(
                text = "“Sell Now or Wait?” AI Advisor",
                fontFamily = dmSans,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF2F4D1D)
            )
            Text(
                text = aiAdvice,
                fontFamily = dmSans,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF8D6E63),
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(
                text = instantCashMsg,
                fontFamily = dmSans,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color.Black
            )
            Text(
                text = "DhartiMitra runs the numbers. You reap the rewards.",
                fontFamily = dmSans,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = Color.Black
            )
        }

        // Disease/Risk Warnings (conditional)
        if (riskDetected) {
            CardSection(
                color = Color(0xFFFFE9E4),
                borderColor = Color(0xFFD84315)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.ErrorOutline,
                        contentDescription = "Warning",
                        tint = Color(0xFFD84315),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Disease/Risk Alert",
                        fontFamily = dmSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFFD84315)
                    )
                }
                Text(
                    text = riskMsg,
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = "We worry about disease. You focus on harvest.",
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = Color.Black
                )
            }
        }

        // WhatsApp Export Button
        Button(
            onClick = {
                shareViaWhatsApp(context, summaryExport)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
        ) {
            Icon(Icons.Filled.Share, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Share Harvest Plan", color = Color.White, fontFamily = dmSans, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

// Helper composable for a uniform card look
@Composable
fun CardSection(
    color: Color = Color.White,
    borderColor: Color = Color(0xFFBDBDBD),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            content = content
        )
    }
}

// Checklist item data class
data class ChecklistItem(val label: String, val checked: Boolean)

// WhatsApp sharing intent
fun shareViaWhatsApp(context: Context, message: String) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
        setPackage("com.whatsapp")
    }
    // Try to launch WhatsApp, else fallback to chooser
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // WhatsApp not installed, open chooser
        val chooserIntent = Intent.createChooser(intent, "Share via")
        context.startActivity(chooserIntent)
    }
}
