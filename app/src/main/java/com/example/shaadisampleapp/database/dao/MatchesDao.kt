package com.example.shaadisampleapp.database.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.shaadisampleapp.network.model.Results
import com.example.shaadisampleapp.utils.DBConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllItem(results: List<Results?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(result: Results?)

    @Query("SELECT * FROM ${DBConstants.TAG_MATCHES_TABLE}")
    fun getAllMatches(): Flow<List<Results?>?>?

    @Query("UPDATE ${DBConstants.TAG_MATCHES_TABLE} SET matchStatus = :matchStatus WHERE email = :email")
    fun updateMatchStatus(email: String?, matchStatus: String?)

    @Query("DELETE FROM ${DBConstants.TAG_MATCHES_TABLE}")
    fun nukeTable()

    @RawQuery
    fun vacuumDb(supportSQLiteQuery: SupportSQLiteQuery?): Int
}