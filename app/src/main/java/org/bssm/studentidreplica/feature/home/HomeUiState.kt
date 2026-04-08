package org.bssm.studentidreplica.feature.home

import org.bssm.studentidreplica.core.model.NfcState
import org.bssm.studentidreplica.core.model.ScannedTagRecord

data class HomeUiState(
    val consentAccepted: Boolean = false,
    val nfcSupported: Boolean = false,
    val nfcEnabled: Boolean = false,
    val readArmed: Boolean = false,
    val nfcState: NfcState = NfcState.Idle,
    val latestRecord: ScannedTagRecord? = null,
)
