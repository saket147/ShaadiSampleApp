package com.example.shaadisampleapp.network.repository

import com.example.shaadisampleapp.network.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getVideos(results: Int?) = apiHelper.getVideos(results)
}