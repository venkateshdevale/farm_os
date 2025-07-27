package com.example.farmos.Functions

import android.Manifest
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropScanner(navController: NavController) {
    val DM_Sans = FontFamily(Font(R.font.dm_sans, FontWeight.Normal))

    var capturedPhotos by remember { mutableStateOf<List<Pair<Bitmap?, String>>>(emptyList()) }

    // Mock previous timeline photos
    val oldPhotos = listOf(
        Pair(null, "Day 10"),
        Pair(null, "Day 30"),
        Pair(null, "Day 45")
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { imageBitmap ->
        if (imageBitmap != null) {
            capturedPhotos = listOf(Pair(imageBitmap, "Today")) + capturedPhotos
        }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) cameraLauncher.launch(null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar with Back and Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF00695C)
                    )
                }
                Text(
                    text = "Crop Health Scanner",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = DM_Sans,
                    color = Color(0xFF00695C)
                )
                Spacer(modifier = Modifier.width(48.dp)) // balance
            }

            // Tiny instruction below title
            Text(
                text = "Snap a photo of your crop leaves for instant health analysis.\nClick the camera below to start. No internet? No problem—your farm data stays safe on your phone.",
                fontSize = 11.sp,
                color = Color.Black,
                fontFamily = DM_Sans,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp, bottom = 0.dp),
                lineHeight = 13.sp,
                textAlign = TextAlign.Start
            )

            // Camera Preview
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(R.drawable.leaf),
                        contentDescription = "Camera Preview",
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                        contentScale = ContentScale.Crop
                    )
                    // Capture Button floating on preview
                    FloatingActionButton(
                        onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                        containerColor = Color(0xFFD4AF37),
                        contentColor = Color.Black,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Capture")
                    }
                }
            }

            // Tiny timeline instruction
            Text(
                text = "Your timeline: Compare today’s photo with previous days. Tap to view details and spot issues early!",
                fontSize = 11.sp,
                color = Color.Black,
                fontFamily = DM_Sans,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 13.sp,
                textAlign = TextAlign.Start
            )

            // Timeline
            val allPhotos = capturedPhotos + oldPhotos
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(allPhotos) { _, photoPair ->
                    val (bitmap, label) = photoPair
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Timeline Image",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.leaf),
                                contentDescription = "Timeline Image",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Text(
                            text = label,
                            fontSize = 13.sp,
                            color = Color.Black,
                            fontFamily = DM_Sans,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            // Tiny voice assistant instruction
            Text(
                text = "Not sure what’s wrong? Tap the mic and ask FarmAI about leaf diseases, in any language you like!",
                fontSize = 11.sp,
                color = Color.Black,
                fontFamily = DM_Sans,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 13.sp,
                textAlign = TextAlign.Start
            )

            // Voice Assistant Button
            Button(
                onClick = { /* TODO: Voice chat with Gemini AI */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .clip(RoundedCornerShape(27.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Mic")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ask About Diseases", color = Color.Black, fontWeight = FontWeight.Bold, fontFamily = DM_Sans)
            }

            // Disease Alert Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚠️ Nearby Disease Alert!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = DM_Sans,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Powdery mildew detected in nearby farms.\nConsult agriculture authorities and spray recommended fungicides.",
                        fontSize = 13.sp,
                        color = Color.Black,
                        fontFamily = DM_Sans,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}
