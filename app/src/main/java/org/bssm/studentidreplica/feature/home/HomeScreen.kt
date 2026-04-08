package org.bssm.studentidreplica.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.bssm.studentidreplica.R
import org.bssm.studentidreplica.core.model.NfcState
import org.bssm.studentidreplica.core.util.TimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onStartReading: () -> Unit,
    onOpenHistory: () -> Unit,
    onAcceptConsent: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.home_title)) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                StatusCard(state = state)
            }
            item {
                LatestRecordCard(state = state)
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onStartReading,
                        modifier = Modifier.weight(1f),
                        enabled = state.consentAccepted
                    ) {
                        Text(stringResource(R.string.read_tag))
                    }
                    OutlinedButton(
                        onClick = onOpenHistory,
                        modifier = Modifier.weight(1f),
                        enabled = state.consentAccepted
                    ) {
                        Text(stringResource(R.string.saved_records))
                    }
                }
            }
        }
    }

    if (!state.consentAccepted) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(stringResource(R.string.consent_title)) },
            text = { Text(stringResource(R.string.consent_body)) },
            confirmButton = {
                Button(onClick = onAcceptConsent) {
                    Text(stringResource(R.string.consent_confirm))
                }
            }
        )
    }
}

@Composable
private fun StatusCard(state: HomeUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = when {
                    !state.nfcSupported -> stringResource(R.string.nfc_unsupported)
                    !state.nfcEnabled -> stringResource(R.string.nfc_disabled)
                    else -> stringResource(R.string.nfc_supported)
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when (val value = state.nfcState) {
                    NfcState.Idle -> stringResource(R.string.nfc_idle)
                    NfcState.Reading -> stringResource(R.string.nfc_reading)
                    is NfcState.Success -> stringResource(R.string.nfc_success)
                    is NfcState.Error -> value.message
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.safety_notice),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun LatestRecordCard(state: HomeUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.last_scan_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            val record = state.latestRecord
            if (record == null) {
                Text(stringResource(R.string.latest_scan_empty))
            } else {
                MetadataRow(label = stringResource(R.string.uid_label), value = record.uid)
                MetadataRow(label = stringResource(R.string.tag_type_label), value = record.tagType)
                MetadataRow(label = stringResource(R.string.atqa_label), value = record.atqa)
                MetadataRow(label = stringResource(R.string.sak_label), value = record.sak)
                MetadataRow(
                    label = stringResource(R.string.scanned_at_label),
                    value = TimeFormatter.format(record.scannedAt)
                )
            }
        }
    }
}

@Composable
fun MetadataRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
