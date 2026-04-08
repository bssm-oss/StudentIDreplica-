package org.bssm.studentidreplica.feature.history.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.bssm.studentidreplica.R
import org.bssm.studentidreplica.core.model.ScannedTagRecord
import org.bssm.studentidreplica.core.util.TimeFormatter
import org.bssm.studentidreplica.feature.home.MetadataRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(record: ScannedTagRecord?) {
    val context = LocalContext.current
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.detail_title)) }) }
    ) { paddingValues ->
        if (record == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = stringResource(R.string.empty_history))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = record.uid,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        MetadataRow(stringResource(R.string.tag_type_label), record.tagType)
                        MetadataRow(stringResource(R.string.tech_list_label), record.techList.joinToString())
                        MetadataRow(stringResource(R.string.atqa_label), record.atqa)
                        MetadataRow(stringResource(R.string.sak_label), record.sak)
                        MetadataRow(stringResource(R.string.scanned_at_label), TimeFormatter.format(record.scannedAt))
                        MetadataRow(
                            stringResource(R.string.label_label),
                            record.label ?: stringResource(R.string.label_empty)
                        )
                    }
                }
                Text(
                    text = stringResource(R.string.safety_notice),
                    color = MaterialTheme.colorScheme.primary,
                )
                Button(
                    onClick = { ReportShareHelper.share(context, record) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.share_report))
                }
            }
        }
    }
}
