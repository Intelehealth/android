package org.intelehealth.feature.chat

import android.content.Context
import com.github.ajalt.timberkt.Timber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.database.dao.ProviderDAO
import org.intelehealth.app.models.dto.ProviderDTO
import org.intelehealth.app.webrtc.notification.AppNotification
import org.intelehealth.feature.chat.data.ChatDataSource
import org.intelehealth.feature.chat.data.ChatRepository
import org.intelehealth.feature.chat.listener.ConversationListener
import org.intelehealth.feature.chat.listener.MessageListener
import org.intelehealth.feature.chat.model.MessageStatus
import org.intelehealth.feature.chat.provider.RetrofitProvider
import org.intelehealth.feature.chat.room.ChatDatabase
import org.intelehealth.feature.chat.room.entity.ChatMessage
import org.intelehealth.feature.chat.room.entity.ChatRoom
import org.intelehealth.feature.chat.ui.activity.ChatRoomActivity

/**
 * Created by Vaghela Mithun R. on 03-07-2023 - 16:21.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

class MessageHandler(
    private val chatRepository: ChatRepository
) : MessageListener, ConversationListener {

    var activeRoomId: String? = null
    var loginUserId: String? = null

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onConversationUpdate() {

    }

    override fun onConversationRead(senderId: String?, receiverId: String?) {

    }

    override fun onMessageReceived(messages: MutableList<ChatMessage>?) {
        scope.launch {
            Timber.d { "onMessageReceived => ${messages.toString()}" }
            messages?.let {
                val chatRoom = messages[0].roomId?.let { roomId ->
                    async {
                        chatRepository.getChatRoom(
                            roomId,
                            messages[0].senderId,
                            messages[0].receiverId,
                        )
                    }.await()
                }

                chatRoom?.let { room ->
                    if (room.syncStatus) chatRepository.addMessages(it)
                    else syncChatRoom(messages[0])
                } ?: createChatRoom(messages[0])

                ackMessageReadIfRoomActive(messages[0])
            }
        }
    }

    private fun createChatRoom(chatMessage: ChatMessage) {
        scope.launch {
            ChatRoom(
                senderId = chatMessage.senderId,
                receiverId = chatMessage.receiverId,
                roomId = chatMessage.roomId!!,
                roomName = chatMessage.roomName!!,
                receiverName = chatMessage.receiverName ?: "HW",
                senderName = "Doctor",
                syncStatus = true
            ).apply {
                chatRepository.addChatRoom(this)
                syncChatRoom(chatMessage)
            }
        }
    }

    private fun syncChatRoom(chatMessage: ChatMessage) {
        scope.launch {
            chatMessage.patientId?.let {
                val response = async {
                    chatRepository.getMessages(chatMessage.senderId, chatMessage.receiverId, it)
                }.await()

                if (response.code() == 200 && response.body() != null) {
                    response.body()?.data?.let {
                        chatRepository.addMessages(it)
                        chatMessage.roomId?.let { it1 ->
                            chatRepository.updateChatRoomSyncStatus(
                                it1, chatMessage.senderId, chatMessage.receiverId, true
                            )
                        }
                    }
                }
            }
        }
    }

    private fun ackMessageReadIfRoomActive(message: ChatMessage) {
        if (activeRoomId != null && loginUserId != null && activeRoomId == message.roomId && loginUserId == message.receiverId) {
            scope.launch {
                chatRepository.ackMessageRead(message.messageId)
            }
        } else showNotification(message)
    }

    private fun showNotification(message: ChatMessage) {
        val title = ProviderDAO().getProviderName(message.senderId, ProviderDTO.Columns.USER_UUID.value)
        val context = IntelehealthApplication.getAppContext()
        AppNotification.Builder(context).title(title).body(message.message)
            .pendingIntent(ChatRoomActivity.getPendingIntent(context, message.toChatConfig())).send();
    }

    override fun onCmdMessageReceived(messages: MutableList<ChatMessage>?) {

    }

    override fun onMessageDelivered(messages: MutableList<ChatMessage>?) {
        scope.launch {
            Timber.d { "onMessageDelivered => ${messages.toString()} " }
            messages?.let {
                chatRepository.changeMessageStatus(it.map { it.messageId }, MessageStatus.DELIVERED)
            }
        }
    }

    override fun onMessageRead(messages: MutableList<ChatMessage>?) {
        scope.launch {
            Timber.d { "onMessageRead => ${messages.toString()}" }
            messages?.let {
                chatRepository.changeMessageStatus(it.map { it.messageId }, MessageStatus.READ)
            }
        }
    }

    fun cancel() {
        if (scope.isActive) scope.cancel()
    }

    companion object {
        @Volatile
        private var INSTANCE: MessageHandler? = null

        @JvmStatic
        fun getInstance(context: Context): MessageHandler = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildChatClient(context).also {
                INSTANCE = it
            }
        }

        private fun buildChatClient(context: Context): MessageHandler {
            return MessageHandler(
                ChatRepository(
                    ChatDatabase.getInstance(context).chatDao(),
                    ChatDatabase.getInstance(context).chatRoomDao(),
                    ChatDataSource(RetrofitProvider.getApiClient(context))
                )
            )
        }
    }
}