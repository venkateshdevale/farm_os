package com.example.farmos.AiService

import android.content.Context
import com.google.auth.oauth2.ServiceAccountCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.InputStream

class VertexService(val context: Context) {

    private val client = OkHttpClient()
    private val projectId = "your-project-id"
    private val location = "us-central1"
    private val modelId = "gemini-1.5-pro"

    private suspend fun getToken() : String = withContext(Dispatchers.IO){
        val inputStream: InputStream = context.assets.open("service-account-key.json")

        val credentials = ServiceAccountCredentials.fromStream(inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/cloud-platform"))
        credentials.refreshAccessToken().tokenValue
    }

    suspend fun textGeneration(prompt : String) : Result<String> = withContext(Dispatchers.IO){
        try {
            val accessToken = getToken()
            val endpoint = "https://$location-aiplatform.googleapis.com/v1/projects/$projectId/locations/$location/publishers/google/models/$modelId:generateContent"

            val requestBody = JSONObject().apply {
                put("contents", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", org.json.JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.7)
                    put("topP", 0.8)
                    put("topK", 40)
                    put("maxOutputTokens", 1024)
                })
            }

        val request = Request.Builder()
            .url(endpoint)
            .addHeader("Authorization", "Bearer $accessToken")
            .addHeader("Content-Type", "application/json")
            .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            val jsonResponse = JSONObject(responseBody ?: "")
            val candidates = jsonResponse.optJSONArray("candidates")

            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.getJSONObject("content")
                val parts = content.getJSONArray("parts")
                val text = parts.getJSONObject(0).getString("text")

                Result.success(text)
            } else {
                Result.failure(Exception("No response generated"))
            }
        } else {
            Result.failure(Exception("API call failed: ${response.code}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
}