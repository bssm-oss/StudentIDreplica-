package org.bssm.studentidreplica.core.model

data class TagInfo(
    val uid: String,
    val tagType: String,
    val techList: List<String>,
    val atqa: String,
    val sak: String,
    val scannedAt: Long,
    val label: String? = null,
)
