package com.example.farmos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import com.example.farmos.ui.theme.AndroidAssessmentTheme
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.farmos.Data.OnboardingViewModel
import com.example.farmos.Functions.ChatScreen
import com.example.farmos.Functions.CropScanner
import com.example.farmos.Functions.Insights
import com.example.farmos.Functions.satellite
import com.example.farmos.onboarding.OnboardingCropSuggestScreen
import com.example.farmos.ui.screens.HarvestScreen
import com.example.farmos.ui.screens.MonitorPage
import com.example.farmos.ui.screens.SubsidyScreen
import com.example.farmos.ui.screens.onboarding.OnboardingConfirmScreen
import com.example.farmos.ui.screens.onboarding.OnboardingLocationScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fullscreen immersive mode
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE


        setContent {
            AndroidAssessmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   val pad = innerPadding
                   val navController = rememberNavController()
                    val onboardingViewModel = viewModel<OnboardingViewModel>() // or use viewModel()
                    NavHost(
                        navController = navController,
                        startDestination = nav.onboardingLocation
                    ) {
                        // Splash / Intro
//                        composable(nav.splash) {
//                            SplashScreen(navController)
//                        }

                        // Onboarding Flow
                        composable(nav.onboardingLocation) {
                            OnboardingLocationScreen(navController, onboardingViewModel)
                        }
                        composable(nav.onboardingCrops) {
                            OnboardingCropSuggestScreen(navController, onboardingViewModel)
                        }
                        composable(nav.onboardingConfirm) {
                            OnboardingConfirmScreen(navController)
                        }

                        // Main Monitor Page
                        composable(nav.monitor) {
                            MonitorPage(navController)
                        }

                        // Crop Scanner
                        composable(nav.harvest) {
                            HarvestScreen(navController)
                        }

                        // Crop Scanner
                        composable(nav.cropScan) {
                            CropScanner(navController)
                        }

                        // Insights & Satellite
                        composable(nav.insights) {
                            Insights(navController)
                        }
                        composable(nav.satellite) {
                            satellite(navController)
                        }

                        // AI Chat (Gemini)
                        composable(nav.chatModel) {
                            ChatScreen()
                        }

                        // Subsidy Information
                        composable(nav.subsidy) {
                            SubsidyScreen()
                        }
                    }
                }
            }
        }
    }
}
