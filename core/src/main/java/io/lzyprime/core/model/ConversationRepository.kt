package io.lzyprime.core.model

import io.lzyprime.core.model.api.IMConversationService
import io.lzyprime.core.model.bean.IMConversation
import io.lzyprime.core.model.modules.IODispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class ConversationRepository @Inject constructor(
    private val conversationService: IMConversationService,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    private val deleteConversationId = MutableSharedFlow<String>()

    fun getConversationList(): Flow<Collection<IMConversation>> = channelFlow {
        val cache = mutableMapOf<String, IMConversation>()

        conversationService.getConversationList().collect { list ->
            list.forEach { cache[it.conversationId] = it }
            send(cache.values)
            yield()
        }

        launch(ioDispatcher) {
            conversationService.updateConversationList.collect { updateList ->
                updateList.forEach { cache[it.conversationId] = it }
                send(cache.values)
                yield()
            }
        }

        launch(ioDispatcher) {
            deleteConversationId.collect {
                cache.remove(it)
                send(cache.values)
                yield()
            }
        }
    }

    suspend fun deleteConversation(conversationId: String) {
        if (conversationService.deleteConversation(conversationId)) {
            deleteConversationId.emit(conversationId)
        }
    }

    suspend fun pinConversation(conversationId: String, pin:Boolean){
        if(conversationService.pinConversation(conversationId, pin)) {
            conversationService.getConversation(conversationId)
        }
    }
}