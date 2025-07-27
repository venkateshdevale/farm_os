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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farmos.nav
import com.example.farmos.R

@Composable
fun OnboardingConfirmScreen(navController: NavController) {
    val dmSans = FontFamily(Font(R.font.dm_sans))
    val botName = "DhartiMitra" // Name your AI bot here!

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back button top bar
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF00695C)
                    )
                }
            }

            // Step info (micro-instruction)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, top = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE4FFD7)),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Text(
                    text = "Step 3 of 3\n\nYou did it! Your farm is now protected and guided by FarmOS and $botName.",
                    color = Color(0xFF20521F),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    fontFamily = dmSans,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }

            // Confirmation card with all info
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.padding(18.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splashlogo),
                        contentDescription = "Setup Complete",
                        modifier = Modifier
                            .size(140.dp)
                            .padding(top = 8.dp),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = "Welcome to FarmOS!",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = dmSans,
                        color = Color(0xFF00695C),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = buildString {
                            append("Your journey just got easier. ")
                            append("$botName, your AI farm guardian, is now actively watching your fields—day and night.\n\n")
                            append("$botName will:\n")
                            append("• Alert you about weather & disease risks\n")
                            append("• Suggest ways to boost harvests & profit\n")
                            append("• Auto-fill subsidy forms & reminders\n\n")
                            append("Relax and focus on your land—FarmOS and $botName have your back. You’re never alone in the field again.")
                        },
                        fontSize = 14.sp,
                        fontFamily = dmSans,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF444444),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Go to app (floating gold button)
            FloatingActionButton(
                onClick = {
                    navController.navigate(nav.monitor) {
                        popUpTo(nav.onboardingLocation) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .padding(top = 10.dp),
                containerColor = Color(0xFFD4AF37),
                contentColor = Color.Black
            ) {
                Text(
                    "Go!",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dmSans
                )
            }

            // Footer empathy message
            Text(
                text = "FarmOS and $botName are always working for you. \uD83C\uDF31\nWishing you a happy, prosperous season!",
                color = Color(0xFF6A5D2F),
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                fontFamily = dmSans,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 2.dp)
            )
        }
    }
}
