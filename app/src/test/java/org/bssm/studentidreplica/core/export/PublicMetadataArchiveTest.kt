package org.bssm.studentidreplica.core.export

import org.bssm.studentidreplica.core.model.ScannedTagRecord
import org.junit.Assert.assertEquals
import org.junit.Test

class PublicMetadataArchiveTest {
    @Test
    fun `archive always includes top-level public metadata warning`() {
        val archive = PublicMetadataArchive.from(
            ScannedTagRecord(
                id = 1,
                uid = "EC:64:F6:71",
                tagType = "ISO 14443-3A, NXP - Mifare Classic 1K",
                techList = listOf("android.nfc.tech.NfcA"),
                atqa = "0x0004",
                sak = "0x08",
                scannedAt = 1_744_122_000_000,
                label = null,
            )
        )

        assertEquals(
            "This file contains only public metadata. No private or sector data is stored.",
            archive.warning
        )
    }
}
