package org.bssm.studentidreplica.core.model

data class ScannedTagRecord(
    val id: Long,
    val uid: String,
    val tagType: String,
    val techList: List<String>,
    val atqa: String,
    val sak: String,
    val scannedAt: Long,
    val label: String?,
)
