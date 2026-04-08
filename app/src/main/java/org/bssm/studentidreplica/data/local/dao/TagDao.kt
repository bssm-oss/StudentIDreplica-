package org.bssm.studentidreplica.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.bssm.studentidreplica.data.local.entity.ScannedTagEntity

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ScannedTagEntity): Long

    @Query("SELECT * FROM scanned_tags ORDER BY scannedAt DESC")
    fun observeAll(): Flow<List<ScannedTagEntity>>

    @Query("SELECT * FROM scanned_tags WHERE id = :id")
    fun observeById(id: Long): Flow<ScannedTagEntity?>

    @Query("SELECT * FROM scanned_tags ORDER BY scannedAt DESC LIMIT 1")
    fun observeLatest(): Flow<ScannedTagEntity?>
}
