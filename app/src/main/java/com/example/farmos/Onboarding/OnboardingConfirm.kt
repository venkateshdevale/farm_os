package com.example.farmos.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.farmos.R
import com.example.farmos.nav

// For Lottie: import com.airbnb.lottie.compose.*

@Composable
fun OnboardingConfirmScreen(navController: NavController) {
    val dmSans = FontFamily(Font(R.font.dm_sans))
    val botName = "DhartiMitra"

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
            // --- Top bar: back btn only if you want (or remove for full immersion)
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

            // --- Animated logo or Lottie (prefer animated farm/success!)
            AnimatedVisibility(
                visible = true,
                enter = fadeIn()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splashlogo),
                    contentDescription = "Setup Complete",
                    modifier = Modifier
                        .size(140.dp)
                        .padding(top = 16.dp, bottom = 10.dp),
                    contentScale = ContentScale.Fit
                )
            }
            // If using Lottie:
            // val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success_anim))
            // LottieAnimation(composition, iterations = LottieConstants.IterateForever, modifier = Modifier.size(180.dp))

            // --- Welcome & pitch
            Text(
                text = "Welcome to FarmOS!",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = dmSans,
                color = Color(0xFF00695C),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 6.dp)
            )

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
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = buildString {
                            append("This isn’t just an app—this is your farm’s AI control tower.\n\n")
                            append("From now on, $botName works 24x7:\n")
                            append("• Raises alerts before trouble hits\n")
                            append("• Boosts your yield & mandi profits\n")
                            append("• Auto-fills all those painful government forms\n\n")
                            append("Relax, focus on your land—FarmOS & $botName have you covered. No more flying blind. No more farming alone. 🚀")
                        },
                        fontSize = 15.sp,
                        fontFamily = dmSans,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF444444),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // --- Call to action: Go! button (animated/floating)
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
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = dmSans
                )
            }

            // --- Footer empathy message (no instructions)
            Text(
                text = "FarmOS & $botName are always working for you.\nWishing you a bumper harvest and zero stress! 🌾",
                color = Color(0xFF6A5D2F),
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                fontFamily = dmSans,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 2.dp)
            )
        }
    }
}
