package org.bssm.studentidreplica.feature.history.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.bssm.studentidreplica.R
import org.bssm.studentidreplica.core.model.ScannedTagRecord
import org.bssm.studentidreplica.core.util.TimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryListScreen(
    records: List<ScannedTagRecord>,
    onOpenDetail: (Long) -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.history_title)) }) }
    ) { paddingValues ->
        if (records.isEmpty()) {
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(records, key = { it.id }) { record ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenDetail(record.id) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(record.uid, fontWeight = FontWeight.SemiBold)
                            Text(record.tagType, style = MaterialTheme.typography.bodyMedium)
                            Text(TimeFormatter.format(record.scannedAt), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
