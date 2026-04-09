package org.bssm.studentidreplica.feature.home

import android.nfc.Tag
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.bssm.studentidreplica.core.model.NfcState
import org.bssm.studentidreplica.core.model.ScannedTagRecord
import org.bssm.studentidreplica.core.model.TagInfo
import org.bssm.studentidreplica.data.preferences.ConsentRepository
import org.bssm.studentidreplica.data.repository.TagRepository
import org.bssm.studentidreplica.feature.nfc.NfcReaderHelper
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `startReading enters reading state when consent and nfc are enabled`() = runTest(dispatcher) {
        val viewModel = HomeViewModel(
            consentRepository = FakeConsentRepository(initial = true),
            tagRepository = FakeTagRepository(),
            nfcReaderHelper = mockk(relaxed = true),
        )

        advanceUntilIdle()
        viewModel.updateNfcAvailability(supported = true, enabled = true)
        advanceUntilIdle()
        viewModel.startReading()
        advanceUntilIdle()

        assertEquals(NfcState.Reading, viewModel.uiState.value.nfcState)
        assertTrue(viewModel.uiState.value.readArmed)
    }

    @Test
    fun `handleDiscoveredTag saves metadata and publishes success state`() = runTest(dispatcher) {
        val tag = mockk<Tag>()
        val reader = mockk<NfcReaderHelper>()
        val repository = FakeTagRepository()
        val expected = TagInfo(
            uid = "EC:64:F6:71",
            tagType = "ISO 14443-3A, NXP - Mifare Classic 1K",
            techList = listOf("android.nfc.tech.NfcA", "android.nfc.tech.MifareClassic"),
            atqa = "0x0004",
            sak = "0x08",
            scannedAt = 1_700_000_000_000,
        )
        every { reader.extractPublicMetadata(tag, any()) } returns expected

        val viewModel = HomeViewModel(
            consentRepository = FakeConsentRepository(initial = true),
            tagRepository = repository,
            nfcReaderHelper = reader,
        )

        advanceUntilIdle()
        viewModel.updateNfcAvailability(supported = true, enabled = true)
        advanceUntilIdle()
        viewModel.startReading()
        advanceUntilIdle()
        viewModel.handleDiscoveredTag(tag)
        advanceUntilIdle()

        val state = viewModel.uiState.value.nfcState
        assertTrue(state is NfcState.Success)
        assertEquals(expected.uid, repository.saved.single().uid)
        coVerify { reader.extractPublicMetadata(tag, any()) }
    }

    private class FakeConsentRepository(initial: Boolean) : ConsentRepository {
        private val state = MutableStateFlow(initial)

        override fun observeConsent(): Flow<Boolean> = state

        override suspend fun acceptConsent() {
            state.value = true
        }
    }

    private class FakeTagRepository : TagRepository {
        val saved = mutableListOf<TagInfo>()
        private val latest = MutableStateFlow<ScannedTagRecord?>(null)

        override fun observeAll(): Flow<List<ScannedTagRecord>> = flowOf(emptyList())

        override fun observeById(id: Long): Flow<ScannedTagRecord?> = latest

        override fun observeLatest(): Flow<ScannedTagRecord?> = latest

        override suspend fun saveScan(tagInfo: TagInfo): Long {
            saved += tagInfo
            latest.value = ScannedTagRecord(
                id = saved.size.toLong(),
                uid = tagInfo.uid,
                tagType = tagInfo.tagType,
                techList = tagInfo.techList,
                atqa = tagInfo.atqa,
                sak = tagInfo.sak,
                scannedAt = tagInfo.scannedAt,
                label = tagInfo.label,
            )
            return saved.size.toLong()
        }
    }
}
