package com.rioramdani0034.mobpro1.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rioramdani0034.mobpro1.model.Art
import com.rioramdani0034.mobpro1.network.ApiStatus
import com.rioramdani0034.mobpro1.network.ArtApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Art>())
        private set
    var status = MutableStateFlow(ApiStatus.LOADING)
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set
    var deleteStatus = mutableStateOf<String?>(null)
        private set

    fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                val response = ArtApi.service.getArt()
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                parser.timeZone = TimeZone.getTimeZone("UTC")

                val sorted = response.sortedByDescending { art ->
                    try {
                        parser.parse(art.createdAt)?.time ?: 0L
                    } catch (e: Exception) {
                        0L
                    }
                }

                data.value = sorted
                status.value = ApiStatus.SUCCESS
                Log.d("MainViewModel", "Loaded ${sorted.size} artworks")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Failure: ${e.message}", e)
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(
        title: String,
        description: String,
        category: String,
        origin: String,
        artist: String,
        bitmap: Bitmap
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                ArtApi.service.postArt(
                    title.toRequestBody("text/plain".toMediaTypeOrNull()),
                    description.toRequestBody("text/plain".toMediaTypeOrNull()),
                    category.toRequestBody("text/plain".toMediaTypeOrNull()),
                    origin.toRequestBody("text/plain".toMediaTypeOrNull()),
                    artist.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )
                retrieveData()
            } catch (e: Exception) {
                Log.d("MainViewModel", "Save failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun updateData(
        id: String,
        title: String,
        description: String,
        category: String,
        origin: String,
        artist: String,
        bitmap: Bitmap?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imagePart = bitmap?.toMultipartBody()
                ArtApi.service.updateArt(
                    id,
                    title.toRequestBody("text/plain".toMediaTypeOrNull()),
                    description.toRequestBody("text/plain".toMediaTypeOrNull()),
                    category.toRequestBody("text/plain".toMediaTypeOrNull()),
                    origin.toRequestBody("text/plain".toMediaTypeOrNull()),
                    artist.toRequestBody("text/plain".toMediaTypeOrNull()),
                    imagePart
                )
                retrieveData()
            } catch (e: Exception) {
                Log.d("MainViewModel", "Update failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteData(artworkId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = ArtApi.service.deleteArt(artworkId)
                deleteStatus.value = result.message
                retrieveData()
            } catch (e: Exception) {
                Log.d("MainViewModel", "Delete failure: ${e.message}")
                deleteStatus.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }

    fun clearDeleteStatus() {
        deleteStatus.value = null
    }

    fun clearMessage() {
        errorMessage.value = null
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpeg".toMediaTypeOrNull(), 0, byteArray.size
        )
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody
        )
    }
}
