package org.bssm.studentidreplica.core.model

sealed interface NfcState {
    data object Idle : NfcState
    data object Reading : NfcState
    data class Success(val tagInfo: TagInfo) : NfcState
    data class Error(val message: String) : NfcState
}
