package org.intelehealth.feature.chat.data

import org.intelehealth.feature.chat.room.entity.ChatMessage
import org.intelehealth.feature.chat.model.MessageStatus
import org.intelehealth.feature.chat.room.dao.ChatDao
import org.intelehealth.feature.chat.room.dao.ChatRoomDao
import org.intelehealth.feature.chat.room.entity.ChatRoom

/**
 * Created by Vaghela Mithun R. on 03-07-2023 - 16:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ChatRepository(
    private val chatDao: ChatDao, private val chatRoomDao: ChatRoomDao, private val dataSource: ChatDataSource
) {

    suspend fun addMessage(message: ChatMessage) = chatDao.addMessage(message)

    suspend fun addMessages(messages: List<ChatMessage>) = chatDao.insertAll(messages)

    fun getChatRoomMessages(roomId: String) = chatDao.getChatRoomMessages(roomId)

    suspend fun changeMessageStatus(messageId: Int, messageStatus: MessageStatus) =
        chatDao.changeMessageStatus(messageId, messageStatus.value)

    suspend fun updateMessage(message: ChatMessage) = chatDao.updateMessage(message)

    suspend fun updateMessageIdAndStatus(messageId: Int, status: Int, message: String) =
        chatDao.updateMessageIdAndStatus(messageId, status, message)

    suspend fun changeMessageStatus(messageIds: List<Int>, messageStatus: MessageStatus) =
        chatDao.changeMessageStatus(messageIds, messageStatus.value)

    suspend fun sendMessage(message: ChatMessage) = dataSource.sendMessage(message)

    suspend fun ackMessageRead(messageId: Int) = dataSource.ackMessageRead(messageId)

    suspend fun getMessages(
        from: String, to: String, patientId: String
    ) = dataSource.getMessages(from, to, patientId)

    suspend fun saveMessages(messages: List<ChatMessage>?): List<ChatMessage> {
        com.github.ajalt.timberkt.Timber.d { "saveMessages" }
        return messages?.let {
            chatDao.insertAll(it)
            messages
        } ?: emptyList()
    }

    suspend fun clearChatRoom(roomId: String) = chatDao.clearChatRoom(roomId)

    suspend fun getChatRoom(roomId: String, senderId: String, receiverId: String) =
        chatRoomDao.getChatRoom(roomId, senderId, receiverId)

    fun getChatRoom(roomId: String) = chatRoomDao.getChatRoom(roomId)

    suspend fun addChatRoom(chatRoom: ChatRoom) = chatRoomDao.addChatRoom(chatRoom)

    suspend fun updateChatRoomSyncStatus(roomId: String, senderId: String, receiverId: String, status: Boolean) =
        chatRoomDao.updateChatRoomSyncStatus(roomId, senderId, receiverId, status)
}