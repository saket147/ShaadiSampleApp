package com.example.shaadisampleapp.network.api

import com.example.shaadisampleapp.network.model.VideosApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/")
    suspend fun getVideos(@Query("results") results: Int?): VideosApiResponse

}