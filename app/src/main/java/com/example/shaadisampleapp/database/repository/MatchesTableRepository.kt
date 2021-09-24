package com.example.shaadisampleapp.database.repository

import android.app.Application
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.shaadisampleapp.database.AppDatabase
import com.example.shaadisampleapp.database.dao.MatchesDao
import com.example.shaadisampleapp.network.model.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MatchesTableRepository(application: Application) {

    private val matchesDao: MatchesDao? = AppDatabase.getInstance(application)?.matchesDao()
    private val savedMatches: Flow<List<Results?>?>? = matchesDao?.getAllMatches()

    fun getAllMatches(): Flow<List<Results?>?>?{
        return savedMatches
    }

    suspend fun insertAllItem(results: List<Results?>?){
        withContext(Dispatchers.Default){
            matchesDao?.insertAllItem(results)
        }
    }

    suspend fun insertItem(result: Results?){
        withContext(Dispatchers.Default){
            matchesDao?.insertItem(result)
        }
    }

    suspend fun updateMatchStatus(email: String?, matchStatus: String?){
        withContext(Dispatchers.Default){
            matchesDao?.updateMatchStatus(email, matchStatus)
        }
    }

    suspend fun nukeTable(supportSQLiteQuery: SupportSQLiteQuery?){
        withContext(Dispatchers.Default){
            matchesDao?.nukeTable()
            matchesDao?.vacuumDb(supportSQLiteQuery)
        }
    }
}