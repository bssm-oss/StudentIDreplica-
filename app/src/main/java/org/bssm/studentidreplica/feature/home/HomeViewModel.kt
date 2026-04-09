package org.bssm.studentidreplica.feature.home

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bssm.studentidreplica.core.model.NfcState
import org.bssm.studentidreplica.data.preferences.ConsentRepository
import org.bssm.studentidreplica.data.repository.TagRepository
import org.bssm.studentidreplica.feature.nfc.NfcReaderHelper

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val consentRepository: ConsentRepository,
    private val tagRepository: TagRepository,
    private val nfcReaderHelper: NfcReaderHelper,
) : ViewModel() {

    private val deviceState = MutableStateFlow(DeviceNfcState())
    private val nfcState = MutableStateFlow<NfcState>(NfcState.Idle)

    val uiState: StateFlow<HomeUiState> = combine(
        consentRepository.observeConsent(),
        tagRepository.observeLatest(),
        deviceState,
        nfcState,
    ) { consentAccepted, latestRecord, device, currentNfcState ->
        HomeUiState(
            consentAccepted = consentAccepted,
            nfcSupported = device.supported,
            nfcEnabled = device.enabled,
            readArmed = device.readArmed,
            nfcState = currentNfcState,
            latestRecord = latestRecord,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HomeUiState()
    )

    fun acceptConsent() {
        viewModelScope.launch {
            consentRepository.acceptConsent()
        }
    }

    fun updateNfcAvailability(supported: Boolean, enabled: Boolean) {
        deviceState.update { it.copy(supported = supported, enabled = enabled) }
    }

    fun startReading() {
        val snapshot = uiState.value
        if (!snapshot.consentAccepted) return
        if (!snapshot.nfcSupported) {
            nfcState.value = NfcState.Error("이 기기는 NFC를 지원하지 않습니다.")
            return
        }
        if (!snapshot.nfcEnabled) {
            nfcState.value = NfcState.Error("NFC가 꺼져 있습니다. 시스템 설정에서 NFC를 켜 주세요.")
            return
        }
        deviceState.update { it.copy(readArmed = true) }
        nfcState.value = NfcState.Reading
    }

    fun handleTagIntent(intent: Intent) {
        val snapshot = uiState.value
        if (!snapshot.consentAccepted || !snapshot.readArmed) return
        val tag = intent.readTag() ?: run {
            nfcState.value = NfcState.Error("태그 정보를 찾지 못했습니다.")
            return
        }
        handleDiscoveredTag(tag)
    }

    internal fun handleDiscoveredTag(tag: Tag) {
        deviceState.update { it.copy(readArmed = false) }
        viewModelScope.launch {
            runCatching {
                val tagInfo = nfcReaderHelper.extractPublicMetadata(
                    tag = tag,
                    scannedAt = System.currentTimeMillis()
                )
                tagRepository.saveScan(tagInfo)
                nfcState.value = NfcState.Success(tagInfo)
            }.onFailure {
                nfcState.value = NfcState.Error(it.message ?: "태그 정보를 읽지 못했습니다.")
            }
        }
    }

    private fun Intent.readTag(): Tag? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelableExtra(NfcAdapter.EXTRA_TAG)
        }
    }

    private data class DeviceNfcState(
        val supported: Boolean = false,
        val enabled: Boolean = false,
        val readArmed: Boolean = false,
    )
}
