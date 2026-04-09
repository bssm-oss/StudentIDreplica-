package org.bssm.studentidreplica.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.bssm.studentidreplica.data.local.entity.ScannedTagEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TagDaoInstrumentedTest {
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndObserveLatest_returnsPersistedRecord() = runBlocking {
        database.tagDao().insert(
            ScannedTagEntity(
                uid = "EC:64:F6:71",
                tagType = "ISO 14443-3A, NXP - Mifare Classic 1K",
                techList = listOf("android.nfc.tech.NfcA", "android.nfc.tech.MifareClassic"),
                atqa = "0x0004",
                sak = "0x08",
                scannedAt = 1_744_122_000_000,
                label = null,
            )
        )

        val latest = database.tagDao().observeLatest().first()

        requireNotNull(latest)
        assertEquals("EC:64:F6:71", latest.uid)
        assertEquals("0x0004", latest.atqa)
        assertEquals("0x08", latest.sak)
    }
}
