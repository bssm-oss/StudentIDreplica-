package org.bssm.studentidreplica.core.export

import kotlinx.serialization.Serializable
import org.bssm.studentidreplica.core.model.ScannedTagRecord

@Serializable
data class PublicMetadataArchive(
    val warning: String = WARNING,
    val uid: String,
    val tagType: String,
    val techList: List<String>,
    val atqa: String,
    val sak: String,
    val scannedAt: Long,
    val label: String? = null,
) {
    companion object {
        const val WARNING = "This file contains only public metadata. No private or sector data is stored."

        fun from(record: ScannedTagRecord): PublicMetadataArchive = PublicMetadataArchive(
            uid = record.uid,
            tagType = record.tagType,
            techList = record.techList,
            atqa = record.atqa,
            sak = record.sak,
            scannedAt = record.scannedAt,
            label = record.label
        )
    }
}
