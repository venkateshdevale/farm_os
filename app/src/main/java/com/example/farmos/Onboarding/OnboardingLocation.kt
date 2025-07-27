package com.example.farmos.ui.screens.onboarding

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.farmos.Data.OnboardingViewModel
import com.example.farmos.SphericalUtil
import com.example.farmos.nav
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import com.example.farmos.R

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingLocationScreen(
    navController: NavController,
    onboardingViewModel: OnboardingViewModel
) {
    val dmSans = FontFamily(Font(com.example.farmos.R.font.dm_sans))
    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current

    val defaultLatLng = LatLng(12.417616, 76.740728)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLatLng, 19f)
    }
    val polygonArr = remember { mutableStateListOf<LatLng>() }
    var areaInSqMeters by remember { mutableStateOf(0.0) }
    var areaInAcres by remember { mutableStateOf(0.0) }
    var isLoading by remember { mutableStateOf(false) }
    var districtLatLng by remember { mutableStateOf(defaultLatLng) }

    // Center camera on current location if possible
    LaunchedEffect(Unit) {
        getCurrentLocation(context) { latLng ->
            latLng?.let {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 19f)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7)) // super clean neutral
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { activity?.finish() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF2F4D1D)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mark Farm Boundary",
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    color = Color(0xFF20521F)
                )
            }

            // Instruction card (pitch + story)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp, top = 2.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE4FFD7)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Text(
                    text = if (polygonArr.size < 4)
                        "Step 1 of 3"
                    else
                        "Step 2 of 3",
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF20521F),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .padding(bottom = 2.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = if (polygonArr.size < 4)
                        "Tap the 4 corners of your field below. FarmOS + DhartiMitra need to know your real boundaries—not just for maps, but for the right advice, weather, and disease alerts. \n\nWrong tap? No worries, just tap again anywhere to restart."
                    else
                        "Perfect! You’ve drawn your boundary. Double-check the area below, then tap Confirm to let DhartiMitra start watching your farm day and night.",
                    fontFamily = dmSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            // Map and polygon marking
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 10.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = true,
                        mapType = MapType.SATELLITE
                    ),
                    onMapClick = { latLng ->
                        if (polygonArr.size < 4) {
                            polygonArr.add(latLng)
                            districtLatLng = latLng
                        } else {
                            // Reset if 4 already placed, start new
                            polygonArr.clear()
                            polygonArr.add(latLng)
                            districtLatLng = latLng
                        }
                        areaInSqMeters = SphericalUtil.computeArea(polygonArr)
                        areaInAcres = areaInSqMeters / 4046.86
                    }
                ) {
                    // Markers for corners
                    polygonArr.forEachIndexed { idx, point ->
                        Marker(
                            state = MarkerState(position = point),
                            title = "Corner ${idx + 1}"
                        )
                    }
                    // Draw polygon if 4 corners
                    if (polygonArr.size == 4) {
                        Polygon(
                            points = polygonArr,
                            fillColor = Color(0x332F4D1D),
                            strokeColor = Color(0xFF2F4D1D),
                            strokeWidth = 3f
                        )
                    }
                }
            }

            // Area display with friendly copy
            Text(
                text = if (polygonArr.size < 4)
                    "Mark all 4 corners to see your farm’s area."
                else
                    "Your farm area: ${String.format("%.2f", areaInAcres)} acres  (${String.format("%.0f", areaInSqMeters)} m²)",
                color = Color(0xFF236636),
                fontWeight = FontWeight.Bold,
                fontFamily = dmSans,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )

            // Confirm Button
            Button(
                onClick = {
                    if (polygonArr.size == 4) {
                        isLoading = true
                        CoroutineScope(Dispatchers.IO).launch {
                            getAddressFromLatLng(districtLatLng) { district: String, state: String ->
                                isLoading = false
                                onboardingViewModel.district = district
                                onboardingViewModel.state = state
                                onboardingViewModel.areaInAcres = areaInAcres
                                onboardingViewModel.farmPolygon = polygonArr.toList()
                                navController.navigate(nav.onboardingCrops)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                enabled = !isLoading && polygonArr.size == 4
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (polygonArr.size < 4) "Mark All Corners" else "Confirm Location",
                        color = Color.Black,
                        fontFamily = dmSans,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            }

            // Footer empathy message for trust
            Text(
                text = "Marking your land helps DhartiMitra bring you the right alerts, crop suggestions, and government schemes all year. Your boundary, your advantage.",
                color = Color(0xFF6A5D2F),
                fontWeight = FontWeight.SemiBold,
                fontFamily = dmSans,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 2.dp)
            )
        }
    }
}

// --- Utility functions remain the same ---

suspend fun getAddressFromLatLng(
    latLng: LatLng,
    onResult: (district: String, state: String) -> Unit
) {
    try {
        val url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=${latLng.latitude},${latLng.longitude}&key=YOUR_API_KEY"
        val request = Request.Builder().url(url).build()
        val response = OkHttpClient().newCall(request).execute()
        val body = JSONObject(response.body?.string() ?: "")

        val components = body
            .getJSONArray("results")
            .getJSONObject(0)
            .getJSONArray("address_components")

        var district = "Unknown"
        var state = "Unknown"

        for (i in 0 until components.length()) {
            val obj = components.getJSONObject(i)
            val type = obj.getJSONArray("types").getString(0)
            if (type == "administrative_area_level_2") district = obj.getString("long_name")
            if (type == "administrative_area_level_1") state = obj.getString("long_name")
        }

        withContext(Dispatchers.Main) {
            onResult(district, state)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        withContext(Dispatchers.Main) {
            onResult("Unknown", "Unknown")
        }
    }
}

@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context, onLocationFound: (LatLng?) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let { onLocationFound(LatLng(it.latitude, it.longitude)) }
            ?: onLocationFound(null)
    }
}
