package com.example.farmos.Functions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.farmos.nav
import com.example.farmos.R

@Composable
fun Insights(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Back Button
        Row(Modifier.fillMaxWidth()) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Text("Insights", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(
            "Farmer Tip: Check weather and yield forecast to plan irrigation and harvest better.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Weather Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5DC))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Tomorrow’s Weather", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Icon(
                        painterResource(R.drawable.baseline_cloudy_snowing_24),
                        contentDescription = "Weather",
                        modifier = Modifier.size(100.dp)
                    )
                    Text("Snow Showers", fontSize = 28.sp)
                    Text("High: 28°C   Low: 21°C")
                }
            }

            // Yield Card
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5DC))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Yield Prediction", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Text("Good", fontSize = 24.sp)
                    Text("Price projection likely to rise", fontSize = 16.sp)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        FloatingActionButton(
            onClick = { navController.navigate(nav.subsidy) },
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            containerColor = Color(0xFFD4AF37),
            contentColor = Color.Black
        ) {
            Icon(painterResource(R.drawable.baseline_mic_24), contentDescription = "Subsidy")
        }
    }
}
