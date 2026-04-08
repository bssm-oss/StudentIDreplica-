package org.bssm.studentidreplica.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bssm.studentidreplica.core.export.PublicMetadataArchive
import org.bssm.studentidreplica.core.model.ScannedTagRecord

class ScanArchiveStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val json = Json { prettyPrint = true; encodeDefaults = true }

    suspend fun write(record: ScannedTagRecord) {
        val exportDir = File(context.filesDir, "exports").apply { mkdirs() }
        val archiveFile = File(exportDir, "scan-${record.id}.json")
        archiveFile.writeText(json.encodeToString(PublicMetadataArchive.from(record)))
    }
}
