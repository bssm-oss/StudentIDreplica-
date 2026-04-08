package org.bssm.studentidreplica.core.util

object TagTypeDescriber {
    fun describe(techList: List<String>): String {
        return when {
            techList.contains("android.nfc.tech.MifareClassic") -> "ISO 14443-3A, NXP - Mifare Classic 1K"
            techList.contains("android.nfc.tech.NfcA") -> "ISO 14443-3A, NFC-A 호환 태그"
            else -> "알 수 없는 NFC 태그"
        }
    }
}
