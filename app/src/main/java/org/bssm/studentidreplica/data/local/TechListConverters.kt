package org.bssm.studentidreplica.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class TechListConverters {
    @TypeConverter
    fun fromTechList(value: List<String>): String = Json.encodeToString(value)

    @TypeConverter
    fun toTechList(value: String): List<String> = Json.decodeFromString(value)
}
