package org.bssm.studentidreplica.data.repository

import kotlinx.coroutines.flow.Flow
import org.bssm.studentidreplica.core.model.ScannedTagRecord
import org.bssm.studentidreplica.core.model.TagInfo

interface TagRepository {
    fun observeAll(): Flow<List<ScannedTagRecord>>
    fun observeById(id: Long): Flow<ScannedTagRecord?>
    fun observeLatest(): Flow<ScannedTagRecord?>
    suspend fun saveScan(tagInfo: TagInfo): Long
}
