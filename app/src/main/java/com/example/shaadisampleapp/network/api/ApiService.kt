package com.example.shaadisampleapp.network.api

import com.example.shaadisampleapp.network.model.MatchesApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/")
    suspend fun getMatches(@Query("results") results: Int?): MatchesApiResponse

}