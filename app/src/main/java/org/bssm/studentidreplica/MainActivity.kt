package org.bssm.studentidreplica

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.tech.NfcA
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import org.bssm.studentidreplica.feature.home.HomeViewModel
import org.bssm.studentidreplica.navigation.StudentIdReplicaApp
import org.bssm.studentidreplica.ui.theme.StudentIdReplicaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    private val nfcAdapter: NfcAdapter? by lazy { NfcAdapter.getDefaultAdapter(this) }

    private val pendingIntent: PendingIntent by lazy {
        PendingIntent.getActivity(
            this,
            100,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    private val filters = arrayOf(IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED))
    private val techLists = arrayOf(arrayOf(NfcA::class.java.name))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
            StudentIdReplicaTheme {
                StudentIdReplicaApp(
                    homeViewModel = homeViewModel,
                    homeUiState = homeUiState
                )
            }
        }

        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.updateNfcAvailability(
            supported = nfcAdapter != null,
            enabled = nfcAdapter?.isEnabled == true
        )
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, filters, techLists)
    }

    override fun onPause() {
        nfcAdapter?.disableForegroundDispatch(this)
        super.onPause()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent == null) return
        if (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED) {
            homeViewModel.handleTagIntent(intent)
        }
    }
}
