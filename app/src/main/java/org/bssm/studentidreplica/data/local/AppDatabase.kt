package org.bssm.studentidreplica.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.bssm.studentidreplica.data.local.dao.TagDao
import org.bssm.studentidreplica.data.local.entity.ScannedTagEntity

@Database(entities = [ScannedTagEntity::class], version = 1, exportSchema = true)
@TypeConverters(TechListConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tagDao(): TagDao
}
