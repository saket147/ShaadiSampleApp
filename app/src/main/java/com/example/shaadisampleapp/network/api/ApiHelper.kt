package com.example.shaadisampleapp.network.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getMatches(results: Int?) = apiService.getMatches(results)
}