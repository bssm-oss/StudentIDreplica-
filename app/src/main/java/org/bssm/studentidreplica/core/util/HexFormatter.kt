package org.bssm.studentidreplica.core.util

object HexFormatter {
    fun toUidString(bytes: ByteArray?): String {
        if (bytes == null || bytes.isEmpty()) return "알 수 없음"
        return bytes.joinToString(separator = ":") { "%02X".format(it) }
    }

    fun toShortHex(bytes: ByteArray?): String {
        if (bytes == null || bytes.isEmpty()) return "알 수 없음"
        return "0x" + bytes.joinToString(separator = "") { "%02X".format(it) }
    }

    fun toShortHex(value: Short): String = "0x%02X".format(value.toInt() and 0xFF)
}
