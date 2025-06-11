package com.rioramdani0034.mobpro1.network

import com.rioramdani0034.mobpro1.model.Art
import com.rioramdani0034.mobpro1.model.DeleteResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

private const val BASE_URL = "https://art-api.sendiko.my.id/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface HewanApiService {
    @GET("artworks")
    suspend fun getHewan(): List<Art>

    @Multipart
    @POST("artworks")
    suspend fun postHewan(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part("origin") origin: RequestBody,
        @Part("artist") artist: RequestBody,
        @Part image: MultipartBody.Part
    ): Art

    @Multipart
    @PUT("artworks/{id}")
    suspend fun updateHewan(
        @Path("id") id: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part("origin") origin: RequestBody,
        @Part("artist") artist: RequestBody,
        @Part image: MultipartBody.Part?
    ): Art

    @DELETE("artworks/{id}")
    suspend fun deleteHewan(
        @Path("id") id: String
    ): DeleteResponse
}

object HewanApi {
    val service: HewanApiService by lazy {
        retrofit.create(HewanApiService::class.java)
    }

    fun getHewanUrl(imageUrl: String): String {
        return imageUrl
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED}