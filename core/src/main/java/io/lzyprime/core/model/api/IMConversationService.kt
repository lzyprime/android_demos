package io.lzyprime.core.model.api

import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.imsdk.v2.V2TIMConversationListener
import com.tencent.imsdk.v2.V2TIMConversationManager
import com.tencent.imsdk.v2.V2TIMConversationResult
import io.lzyprime.core.model.bean.ConversationSyncState
import io.lzyprime.core.model.bean.IMConversation
import io.lzyprime.core.utils.V2TIMCallback
import io.lzyprime.core.utils.V2TIMValueCallback
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

interface IMConversationService {
    val conversationSyncState: SharedFlow<ConversationSyncState>
    val updateConversationList: SharedFlow<List<IMConversation>>

    fun getConversationList(): Flow<List<IMConversation>>
    suspend fun deleteConversation(conversationId: String): Boolean
    suspend fun pinConversation(conversationId: String, pin: Boolean): Boolean
    suspend fun getConversation(conversationId: String): IMConversation

    companion object {
        private const val LIST_PAGE_SIZE = 100 // 会话列表分页，每页个数
        private const val LIST_REQUEST_DURATION = 500L // 会话列表每页请求间隔，ms

        operator fun invoke(
            externalScope: CoroutineScope,
            ioDispatcher: CoroutineDispatcher,
            conversationManager: V2TIMConversationManager,
        ): IMConversationService = object : IMConversationService {
            private val mutableConversationSyncEvent = MutableSharedFlow<ConversationSyncState>()
            override val conversationSyncState = mutableConversationSyncEvent.asSharedFlow()


            private val mutableUpdateConversationList = MutableSharedFlow<List<IMConversation>>()
            override val updateConversationList = mutableUpdateConversationList.asSharedFlow()

            init {
                conversationManager.setConversationListener(object : V2TIMConversationListener() {
                    override fun onSyncServerStart() {
                        externalScope.launch(ioDispatcher) {
                            mutableConversationSyncEvent.emit(ConversationSyncState.SyncServerStart)
                        }
                    }

                    override fun onSyncServerFinish() {
                        externalScope.launch(ioDispatcher) {
                            mutableConversationSyncEvent.emit(ConversationSyncState.SyncServerFinish)
                        }
                    }

                    override fun onSyncServerFailed() {
                        externalScope.launch(ioDispatcher) {
                            mutableConversationSyncEvent.emit(ConversationSyncState.SyncServerFailed)
                        }
                    }

                    override fun onNewConversation(conversationList: MutableList<V2TIMConversation>) {
                        externalScope.launch(ioDispatcher) {
                            mutableUpdateConversationList.emit(conversationList.map {
                                IMConversation(
                                    it
                                )
                            })
                        }
                    }

                    override fun onConversationChanged(conversationList: MutableList<V2TIMConversation>) {
                        externalScope.launch(ioDispatcher) {
                            mutableUpdateConversationList.emit(conversationList.map { IMConversation(it) })
                        }
                    }
                })
            }

            private suspend fun getConversationList(nextSeq: Long): V2TIMConversationResult =
                suspendCancellableCoroutine { ctn ->
                    conversationManager.getConversationList(
                        nextSeq,
                        LIST_PAGE_SIZE,
                        V2TIMValueCallback({
                            ctn.resume(it)
                        }, {
                            ctn.resumeWithException(it)
                        })
                    )
                }

            override fun getConversationList(): Flow<List<IMConversation>> = flow {
                var nextSeq = 0L
                var finish = false

                while (!finish) {
                    val res = getConversationList(nextSeq)
                    nextSeq = res.nextSeq
                    finish = res.isFinished
                    emit(res.conversationList.map { IMConversation(it) })
                    delay(LIST_REQUEST_DURATION)
                }
            }

            override suspend fun deleteConversation(conversationId: String): Boolean =
                suspendCancellableCoroutine { ctn ->
                    conversationManager.deleteConversation(conversationId, V2TIMCallback(
                        { ctn.resume(true) },
                        { ctn.resume(false) }
                    ))
                }

            override suspend fun pinConversation(conversationId: String, pin: Boolean): Boolean =
                suspendCancellableCoroutine { ctn ->
                    conversationManager.pinConversation(conversationId, pin, V2TIMCallback(
                        { ctn.resume(true) },
                        { ctn.resume(false) }
                    ))
                }

            override suspend fun getConversation(conversationId: String): IMConversation =
                suspendCancellableCoroutine { ctn ->
                    conversationManager.getConversation(conversationId, V2TIMValueCallback(
                        { res ->
                            ctn.resume(IMConversation(res).also {
                                externalScope.launch(ioDispatcher) {
                                    mutableUpdateConversationList.emit(listOf(it))
                                }
                            })
                        },
                        { ctn.resumeWithException(it) }
                    ))
                }
        }
    }
}