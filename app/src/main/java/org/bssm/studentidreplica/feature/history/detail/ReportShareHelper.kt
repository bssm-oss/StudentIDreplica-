package org.bssm.studentidreplica.feature.history.detail

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import org.bssm.studentidreplica.BuildConfig
import org.bssm.studentidreplica.core.export.TagReportBuilder
import org.bssm.studentidreplica.core.model.ScannedTagRecord

object ReportShareHelper {
    fun share(context: Context, record: ScannedTagRecord) {
        val sharedDir = File(context.cacheDir, "shared").apply { mkdirs() }
        val reportFile = File(sharedDir, "scan-${record.id}.txt")
        reportFile.writeText(TagReportBuilder.build(record, BuildConfig.VERSION_NAME))
        val uri = FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            reportFile
        )
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "분석 보고서 공유"))
    }
}
