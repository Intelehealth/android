package org.intelehealth.feature.chat

import android.content.Context
import org.intelehealth.feature.chat.listener.ConnectionListener
import org.intelehealth.feature.chat.listener.EventCallback
import org.intelehealth.feature.chat.room.entity.ChatMessage
import org.intelehealth.feature.chat.socket.ChatSocket


/**
 * Created by Vaghela Mithun R. on 08-07-2023 - 12:00.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

class ChatClient(
    private val chatSocket: ChatSocket, private val messageHandler: MessageHandler
) : ConnectionListener {

    init {
        chatSocket.messageListener = messageHandler
        chatSocket.conversationListener = messageHandler
        chatSocket.connectionListener = this
    }

    fun updateActiveChatRoomDetails(loginUserId: String?, activeRoomId: String?) {
        messageHandler.activeRoomId = activeRoomId
        messageHandler.loginUserId = loginUserId
    }

    fun connect(socketUrl: String) = chatSocket.connect(socketUrl)

    fun isConnected() = chatSocket.isConnected()

    fun sendMessage(chatMessage: ChatMessage, callback: EventCallback<String>? = null) =
        chatSocket.sentMessage(chatMessage)

    fun ackMessageAsRead(messageId: Int) = chatSocket.ackMessageRead(messageId)

    fun ackConversationRead(senderId: String, receiverId: String) = chatSocket.ackConversationRead(senderId, receiverId)

    override fun onConnected() {

    }

    override fun onDisconnected() {

    }

    companion object {
        @Volatile
        private var INSTANCE: ChatClient? = null

        @JvmStatic
        fun getInstance(context: Context): ChatClient = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildChatClient(context).also {
                INSTANCE = it
            }
        }

        private fun buildChatClient(context: Context): ChatClient {
            return ChatClient(ChatSocket.getInstance(), MessageHandler.getInstance(context))
        }
    }
}