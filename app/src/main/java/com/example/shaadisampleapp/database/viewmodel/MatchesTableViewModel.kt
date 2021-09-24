package com.example.shaadisampleapp.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.shaadisampleapp.database.repository.MatchesTableRepository
import com.example.shaadisampleapp.network.model.Results
import kotlinx.coroutines.flow.Flow

class MatchesTableViewModel(application: Application) : AndroidViewModel(application) {

    private val matchesTableRepository: MatchesTableRepository = MatchesTableRepository(application)
    internal val savedMatches: Flow<List<Results?>?>? = matchesTableRepository.getAllMatches()

    suspend fun insertAllItem(result: List<Results?>?) {
        matchesTableRepository.insertAllItem(result)
    }

    suspend fun insertItem(result: Results?) {
        matchesTableRepository.insertItem(result)
    }

    suspend fun updateMatchStatus(email: String?, matchStatus: String?){
        matchesTableRepository.updateMatchStatus(email, matchStatus)
    }

    suspend fun nukeTable(supportSQLiteQuery: SupportSQLiteQuery?) {
        matchesTableRepository.nukeTable(supportSQLiteQuery)
    }
}