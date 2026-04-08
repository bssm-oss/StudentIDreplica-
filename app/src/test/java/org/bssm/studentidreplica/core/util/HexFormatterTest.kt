package org.bssm.studentidreplica.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class HexFormatterTest {
    @Test
    fun `formats uid bytes as uppercase colon separated hex`() {
        val value = HexFormatter.toUidString(byteArrayOf(0xEC.toByte(), 0x64, 0xF6.toByte(), 0x71))

        assertEquals("EC:64:F6:71", value)
    }

    @Test
    fun `formats atqa bytes as short hex string`() {
        val value = HexFormatter.toShortHex(byteArrayOf(0x00, 0x04))

        assertEquals("0x0004", value)
    }
}
