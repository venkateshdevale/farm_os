package com.example.farmos.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farmos.nav
import com.example.farmos.R

@Composable
fun OnboardingConfirmScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFEFF8E2), Color(0xFFDDE9C7))
                )
            )
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back Button
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF2F4D1D)
                    )
                }
            }

            // Confirmation Card
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splashlogo),
                        contentDescription = "Setup Complete",
                        modifier = Modifier
                            .size(180.dp)
                            .padding(top = 16.dp),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = "Setup Complete!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2F4D1D),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Your farm location and crop choices are saved. You’ll now receive tailored insights and updates for your farm’s success.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF4C4C4C),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Proceed Button
            FloatingActionButton(
                onClick = {
                    navController.navigate(nav.monitor) {
                        popUpTo(nav.onboardingLocation) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                containerColor = Color(0xFFD4AF37),
                contentColor = Color.Black
            ) {
                Text("Go", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
