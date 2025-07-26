package com.example.farmos.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.farmos.data.dummySubsidies
import com.example.farmos.R

@Composable
fun SubsidyScreen() {
    val context = LocalContext.current

    // Start with dummy data
    var subsidies by remember { mutableStateOf(dummySubsidies) }

    var cropQuery by remember { mutableStateOf(TextFieldValue("")) }
    var stateQuery by remember { mutableStateOf(TextFieldValue("")) }

    val filtered = subsidies.filter {
        (cropQuery.text.isEmpty() || it.crop.contains(cropQuery.text, ignoreCase = true)) &&
                (stateQuery.text.isEmpty() || it.state.contains(stateQuery.text, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search fields
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = cropQuery,
                onValueChange = { cropQuery = it },
                label = { Text("Search by Crop") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = stateQuery,
                onValueChange = { stateQuery = it },
                label = { Text("Search by State") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Subsidy list
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { subsidy ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = subsidy.schemeName,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = subsidy.benefit,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(subsidy.applicationLink))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.padding(top = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Apply Now")
                        }
                    }
                }
            }
        }
    }
}
