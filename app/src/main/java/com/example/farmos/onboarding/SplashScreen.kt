package com.example.farmos.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.farmos.R
import com.example.farmos.nav

@Composable
fun SplashScreen(navController: NavController) {
    // Earth-tone gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFEDF3E2), // Light soil green
                        Color(0xFFCCE3A1), // Leafy green
                        Color(0xFFB2D49C)  // Olive
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(40.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Welcome to",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF4B5D2A), // Earthy brown-green
                    fontSize = 18.sp
                )

                Text(
                    text = "FarmOS",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2F4D1D) // Rich forest green
                )

                Image(
                    painter = painterResource(id = R.drawable.splashlogo), // or R.drawable.your_updated_image
                    contentDescription = "FarmOS Logo",
                    modifier = Modifier
                        .height(280.dp)
                        .padding(top = 8.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "Your AI-powered companion for healthy, profitable farming.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF4B5D2A).copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Button(
                onClick = { navController.navigate(nav.onboardingLocation) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(32.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
            ) {
                Text("Get Started", fontWeight = FontWeight.Bold, color = Color.Black)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
