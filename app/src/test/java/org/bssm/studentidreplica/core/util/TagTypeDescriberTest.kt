package org.bssm.studentidreplica.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class TagTypeDescriberTest {
    @Test
    fun `describes mifare classic tags explicitly`() {
        val value = TagTypeDescriber.describe(
            listOf(
                "android.nfc.tech.NfcA",
                "android.nfc.tech.MifareClassic",
                "android.nfc.tech.NdefFormatable"
            )
        )

        assertEquals("ISO 14443-3A, NXP - Mifare Classic 1K", value)
    }
}
