package org.bssm.studentidreplica.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scanned_tags")
data class ScannedTagEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uid: String,
    val tagType: String,
    val techList: List<String>,
    val atqa: String,
    val sak: String,
    val scannedAt: Long,
    val label: String? = null,
)
