package com.example.shaadisampleapp.network.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getVideos(results: Int?) = apiService.getVideos(results)
}