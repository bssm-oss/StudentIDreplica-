package org.bssm.studentidreplica.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.bssm.studentidreplica.core.model.ScannedTagRecord
import org.bssm.studentidreplica.core.model.TagInfo
import org.bssm.studentidreplica.data.local.dao.TagDao
import org.bssm.studentidreplica.data.local.entity.ScannedTagEntity

class RoomTagRepository @Inject constructor(
    private val tagDao: TagDao,
    private val archiveStore: ScanArchiveStore,
) : TagRepository {

    override fun observeAll(): Flow<List<ScannedTagRecord>> = tagDao.observeAll().map { entities ->
        entities.map(ScannedTagEntity::toRecord)
    }

    override fun observeById(id: Long): Flow<ScannedTagRecord?> = tagDao.observeById(id).map { entity ->
        entity?.toRecord()
    }

    override fun observeLatest(): Flow<ScannedTagRecord?> = tagDao.observeLatest().map { entity ->
        entity?.toRecord()
    }

    override suspend fun saveScan(tagInfo: TagInfo): Long {
        val id = tagDao.insert(
            ScannedTagEntity(
                uid = tagInfo.uid,
                tagType = tagInfo.tagType,
                techList = tagInfo.techList,
                atqa = tagInfo.atqa,
                sak = tagInfo.sak,
                scannedAt = tagInfo.scannedAt,
                label = tagInfo.label,
            )
        )
        archiveStore.write(
            ScannedTagRecord(
                id = id,
                uid = tagInfo.uid,
                tagType = tagInfo.tagType,
                techList = tagInfo.techList,
                atqa = tagInfo.atqa,
                sak = tagInfo.sak,
                scannedAt = tagInfo.scannedAt,
                label = tagInfo.label,
            )
        )
        return id
    }
}

private fun ScannedTagEntity.toRecord(): ScannedTagRecord = ScannedTagRecord(
    id = id,
    uid = uid,
    tagType = tagType,
    techList = techList,
    atqa = atqa,
    sak = sak,
    scannedAt = scannedAt,
    label = label,
)
