package org.intelehealth.feature.chat

import org.intelehealth.feature.chat.listener.ConnectionListener
import org.intelehealth.feature.chat.listener.EventCallback
import org.intelehealth.feature.chat.model.ChatMessage
import org.intelehealth.feature.chat.socket.ChatSocket
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Vaghela Mithun R. on 08-07-2023 - 12:00.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Singleton
class ChatClient @Inject constructor(
    private val chatSocket: ChatSocket,
    messageHandler: MessageHandler
) : ConnectionListener {

    init {
        chatSocket.messageListener = messageHandler
        chatSocket.conversationListener = messageHandler
        chatSocket.connectionListener = this
    }

    fun connect(socketUrl: String) = chatSocket.connect(socketUrl)

    fun isConnected() = chatSocket.isConnected()

    fun sendMessage(chatMessage: ChatMessage, callback: EventCallback<String>? = null) =
        chatSocket.sentMessage(chatMessage)

    fun ackMessageAsRead(messageId: Int) = chatSocket.ackMessageRead(messageId)

    fun ackConversationRead(senderId: String, receiverId: String) =
        chatSocket.ackConversationRead(senderId, receiverId)

    override fun onConnected() {

    }

    override fun onDisconnected() {

    }
}