package org.bssm.studentidreplica.feature.history.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.bssm.studentidreplica.core.model.ScannedTagRecord
import org.bssm.studentidreplica.data.repository.TagRepository

@HiltViewModel
class HistoryDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    tagRepository: TagRepository,
) : ViewModel() {
    private val tagId: Long = checkNotNull(savedStateHandle.get<Long>("tagId"))

    val record: StateFlow<ScannedTagRecord?> = tagRepository.observeById(tagId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
