package org.bssm.studentidreplica.data.repository

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.io.File
import kotlinx.coroutines.runBlocking
import org.bssm.studentidreplica.core.model.ScannedTagRecord
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScanArchiveStoreInstrumentedTest {
    @Test
    fun write_createsJsonFileWithTopLevelWarning() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        val store = ScanArchiveStore(context)
        val record = ScannedTagRecord(
            id = 99,
            uid = "EC:64:F6:71",
            tagType = "ISO 14443-3A, NXP - Mifare Classic 1K",
            techList = listOf("android.nfc.tech.NfcA", "android.nfc.tech.MifareClassic"),
            atqa = "0x0004",
            sak = "0x08",
            scannedAt = 1_744_122_000_000,
            label = null,
        )

        store.write(record)

        val archive = File(context.filesDir, "exports/scan-99.json")
        val contents = archive.readText()

        assertTrue(contents.contains("\"warning\": \"This file contains only public metadata. No private or sector data is stored.\""))
        assertTrue(contents.contains("\"uid\": \"EC:64:F6:71\""))
    }
}
