package io.lzyprime.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.lzyprime.core.model.ConversationRepository
import io.lzyprime.core.model.bean.IMConversation
import io.lzyprime.core.model.modules.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ConversationListViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val conversationList = conversationRepository.getConversationList()
        .map { it.sortedByDescending(IMConversation::orderKey) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(STOP_DELAY),
            emptyList(),
        )

    fun deleteConversation(conversationId: String) = viewModelScope.launch(ioDispatcher) {
        conversationRepository.deleteConversation(conversationId)
    }

    fun pinConversation(conversation: IMConversation) = viewModelScope.launch(ioDispatcher) {
        conversationRepository.pinConversation(conversation.conversationId, !conversation.isPinned)
    }

    companion object {
        const val STOP_DELAY = 500L
    }
}