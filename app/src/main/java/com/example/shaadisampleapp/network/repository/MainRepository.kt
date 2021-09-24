package com.example.shaadisampleapp.network.repository

import com.example.shaadisampleapp.network.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getMatches(results: Int?) = apiHelper.getMatches(results)
}