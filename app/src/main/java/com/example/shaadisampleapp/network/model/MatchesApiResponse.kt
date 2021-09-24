package com.example.shaadisampleapp.network.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shaadisampleapp.utils.DBConstants

data class MatchesApiResponse(
    var results: MutableList<Results?>?,
)

@Entity(tableName = DBConstants.TAG_MATCHES_TABLE)
data class Results(
    @PrimaryKey
    val email: String,
    val gender: String?,
    @Embedded(prefix = "name_")
    val name: Name?,
    @Embedded(prefix = "location_")
    val location: Location?,
    @Embedded(prefix = "dob_")
    val dob: Dob?,
    @Embedded(prefix = "picture_")
    val picture: Picture?,
    var matchStatus: String?
)

data class Name(
    val title: String?,
    val first: String?,
    val last: String?
)

data class Location(
    val city: String?,
    val country: String?
)

data class Dob(
    val date: String?,
    val age: Int?
)

data class Picture(
    val large: String?,
    val medium: String?,
    val thumbnail: String?
)

