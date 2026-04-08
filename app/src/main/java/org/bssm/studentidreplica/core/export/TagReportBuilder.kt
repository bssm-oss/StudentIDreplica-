package org.bssm.studentidreplica.core.export

import org.bssm.studentidreplica.core.model.ScannedTagRecord
import org.bssm.studentidreplica.core.util.TimeFormatter

object TagReportBuilder {
    fun build(record: ScannedTagRecord, appVersion: String): String = buildString {
        appendLine("===== NFC 공개 정보 분석 보고서 =====")
        appendLine("파일 생성일: ${TimeFormatter.format(record.scannedAt)}")
        appendLine("앱 버전: $appVersion (분석 전용 모드)")
        appendLine("경고: 이 파일은 공개 메타데이터만 포함합니다.")
        appendLine("----------------------------------------")
        appendLine("UID (시리얼 번호): ${record.uid}")
        appendLine("기술 목록: ${record.techList.joinToString()}")
        appendLine("ATQA: ${record.atqa}")
        appendLine("SAK: ${record.sak}")
        appendLine("설명: ${descriptionFor(record)}")
        appendLine("========================================")
    }

    private fun descriptionFor(record: ScannedTagRecord): String {
        return if (record.techList.contains("android.nfc.tech.MifareClassic")) {
            "MIFARE Classic 1K 호환 태그로 추정됩니다."
        } else {
            "NFC-A 호환 태그로 추정됩니다."
        }
    }
}
