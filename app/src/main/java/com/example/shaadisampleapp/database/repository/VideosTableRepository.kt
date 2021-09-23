package com.example.shaadisampleapp.database.repository

import android.app.Application
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.shaadisampleapp.database.AppDatabase
import com.example.shaadisampleapp.database.dao.VideosDao
import com.example.shaadisampleapp.network.model.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VideosTableRepository(application: Application) {

    private val videosDao: VideosDao? = AppDatabase.getInstance(application)?.videosDao()
    private val savedVideos: Flow<List<Results?>?>? = videosDao?.getAllSavedVideos()

    fun getAllSavedVideos(): Flow<List<Results?>?>?{
        return savedVideos
    }

    suspend fun insertItem(result: Results?){
        withContext(Dispatchers.Default){
            videosDao?.insertItem(result)
        }
    }

    suspend fun nukeTable(supportSQLiteQuery: SupportSQLiteQuery?){
        withContext(Dispatchers.Default){
            videosDao?.nukeTable()
            videosDao?.vacuumDb(supportSQLiteQuery)
        }
    }
}