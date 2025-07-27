package com.example.farmos.ui.screens.onboarding

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.util.Log
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.farmos.Data.OnboardingViewModel
import com.example.farmos.SphericalUtil
import com.example.farmos.nav
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

@SuppressLint("MissingPermission", "ContextCastToActivity")
@Composable
fun OnboardingLocationScreen(navController: NavController, onboardingViewModel: OnboardingViewModel) {
    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current
    var mapRef by remember { mutableStateOf<GoogleMap?>(null) }
    var areainSqMeters by remember { mutableStateOf(10000.0) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    val defaultLatLng = LatLng(12.417616, 76.740728)
    var districtLatLng by remember { mutableStateOf(defaultLatLng) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLatLng, 19f)
    }
    val polygonArr = remember { mutableStateListOf<LatLng>() }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        getCurrentLocation(context) { latLng ->
            latLng?.let {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 19f)
                selectedLocation = it
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFFEDF3E2), Color(0xFFCCE3A1)))
            )
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
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
                    text = "Set Farm Location",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2F4D1D)
                )
            }

            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 16.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = true,
                        mapType = MapType.SATELLITE
                    ),
                    onMapLoaded = { polygonArr.clear() },
                    onMapClick = { latLng ->
                        polygonArr.add(latLng)
                        districtLatLng = latLng
                        mapRef?.apply {
                            addMarker(MarkerOptions().position(latLng).title("Farm Marker"))
                            val rectOptions = PolygonOptions()
                                .addAll(polygonArr)
                                .fillColor(Color.Red.copy(alpha = 0.5f).toArgb())
                                .strokeColor(Color.Red.toArgb())
                            addPolygon(rectOptions)
                        }
                        areainSqMeters = SphericalUtil.computeArea(polygonArr)
                        Log.d("Area", "Area in m²: $areainSqMeters")
                    }
                ) {
                    selectedLocation?.let {
                        Marker(state = MarkerState(position = it), title = "Farm Location")
                    }
                    MapEffect { map ->
                        if (mapRef == null) {
                            mapRef = map
                        }
                    }
                }
            }

            Button(
                onClick = {
                    selectedLocation?.let {
                        isLoading = true
                        CoroutineScope(Dispatchers.IO).launch {
                            getAddressFromLatLng(districtLatLng) { district: String, state : String ->
                                Log.d("SHABAZ", district + ", "+state)
                                isLoading = false
                                onboardingViewModel.district = district
                                onboardingViewModel.state = state
                                onboardingViewModel.areaInAcres = areainSqMeters / 4046.86
                                navController.navigate(nav.onboardingCrops)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(32.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Confirm Location", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Suspend function for address resolution
suspend fun getAddressFromLatLng(
    latLng: LatLng,
    onResult: (district: String, state: String) -> Unit
) {
    try {
        val url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=${latLng.latitude},${latLng.longitude}&key=AIzaSyA9wjcdsuYT7YpM5ucShYqHuEh-qzlr6gs"
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
            onResult("Sitapur", "Uttar Pradesh")
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
