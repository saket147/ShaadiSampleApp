package com.example.shaadisampleapp.network.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shaadisampleapp.utils.DBConstants

data class VideosApiResponse(
    val results: Collection<Results?>?,
    val info: Info?
)

data class Info(
    val seed: String?,
    val results: Int?,
    val page: Int?,
    val version: Double?
)

@Entity(tableName = DBConstants.TAG_VIDEOS_TABLE)
data class Results(
    @PrimaryKey
    val email: String,
    val gender: String?,
    @Embedded(prefix = "name_")
    val name: Name?,
    @Embedded(prefix = "location_")
    val location: Location?,
    @Embedded(prefix = "login_")
    val login: Login?,
    @Embedded(prefix = "dob_")
    val dob: Dob?,
    @Embedded(prefix = "registered_")
    val registered: Registered?,
    val phone: String?,
    val cell: String?,
    @Embedded(prefix = "id_")
    val id: Id?,
    @Embedded(prefix = "picture_")
    val picture: Picture?,
    val nat: String?
)

data class Name(
    val title: String?,
    val first: String?,
    val last: String?
)

data class Location(
    @Embedded(prefix = "street_")
    val street: Street?,
    val city: String?,
    val state: String?,
    val country: String?,
    val postcode: Int?,
    @Embedded(prefix = "coordinates_")
    val coordinates: Coordinates?,
    @Embedded(prefix = "timezone_")
    val timezone: Timezone?
)

data class Login(
    val uuid: String?,
    val username: String?,
    val password: String?,
    val salt: String?,
    val md5: String?,
    val sha1: String?,
    val sha256: String?
)

data class Dob(
    val date: String?,
    val age: Int?
)

data class Registered(
    val date: String?,
    val age: Int?
)

data class Id(
    val name: String?,
    val value: String?
)

data class Picture(
    val large: String?,
    val medium: String?,
    val thumbnail: String?
)

data class Street(
    val number: Int?,
    val name: String?
)

data class Coordinates(
    val latitude: Double?,
    val longitude: Double?
)

data class Timezone(
    val offset: String?,
    val description: String?
)