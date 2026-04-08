package org.bssm.studentidreplica.feature.history.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.bssm.studentidreplica.core.model.ScannedTagRecord
import org.bssm.studentidreplica.data.repository.TagRepository

@HiltViewModel
class HistoryListViewModel @Inject constructor(
    tagRepository: TagRepository,
) : ViewModel() {
    val records: StateFlow<List<ScannedTagRecord>> = tagRepository.observeAll()
        .map { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
