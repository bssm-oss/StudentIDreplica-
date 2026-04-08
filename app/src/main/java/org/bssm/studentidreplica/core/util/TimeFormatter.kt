package org.bssm.studentidreplica.core.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeFormatter {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneId.systemDefault())

    fun format(timestamp: Long): String = formatter.format(Instant.ofEpochMilli(timestamp))
}
