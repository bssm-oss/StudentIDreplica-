package org.bssm.studentidreplica.core.export

import org.bssm.studentidreplica.core.model.ScannedTagRecord
import org.junit.Assert.assertTrue
import org.junit.Test

class TagReportBuilderTest {
    @Test
    fun `builds report with warning and public metadata only`() {
        val report = TagReportBuilder.build(
            record = ScannedTagRecord(
                id = 1,
                uid = "EC:64:F6:71",
                tagType = "ISO 14443-3A, NXP - Mifare Classic 1K",
                techList = listOf(
                    "android.nfc.tech.NfcA",
                    "android.nfc.tech.MifareClassic",
                    "android.nfc.tech.NdefFormatable"
                ),
                atqa = "0x0004",
                sak = "0x08",
                scannedAt = 1_744_122_000_000,
                label = null,
            ),
            appVersion = "1.0.0",
        )

        assertTrue(report.contains("경고: 이 파일은 공개 메타데이터만 포함합니다."))
        assertTrue(report.contains("UID (시리얼 번호): EC:64:F6:71"))
        assertTrue(report.contains("ATQA: 0x0004"))
        assertTrue(report.contains("SAK: 0x08"))
        assertTrue(report.contains("MIFARE Classic 1K 호환 태그로 추정됩니다."))
    }
}
