package org.intelehealth.feature.chat.ui.viewmodel

import android.content.Context
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.github.ajalt.timberkt.Timber
import kotlinx.coroutines.launch
import org.intelehealth.core.network.state.Result
import org.intelehealth.core.ui.viewmodel.BaseViewModel
import org.intelehealth.core.utils.utility.DateTimeUtils
import org.intelehealth.feature.chat.ChatClient
import org.intelehealth.feature.chat.data.ChatRepository
import org.intelehealth.feature.chat.room.entity.ChatMessage
import org.intelehealth.feature.chat.model.MessageStatus
import org.intelehealth.features.ondemand.mediator.model.ChatRoomConfig

/**
 * Created by Vaghela Mithun R. on 18-07-2023 - 23:43.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

class ChatViewModel(
    context: Context,
    private val chatClient: ChatClient = ChatClient.getInstance(context),
    private val repository: ChatRepository
) : BaseViewModel() {

    lateinit var roomConfig: ChatRoomConfig

    fun updateActiveChatRoomDetails() {
        if (::roomConfig.isInitialized) {
            chatClient.updateActiveChatRoomDetails(roomConfig.fromId, roomConfig.visitId)
        }
    }

    fun clearChatRoomSession() {
        chatClient.updateActiveChatRoomDetails(null, null)
    }

    fun sendMessage(text: String, messageId: Int) {
        viewModelScope.launch {
            var status = MessageStatus.SENDING
            val message = roomConfig.let {
                return@let ChatMessage(
                    messageId = messageId,
                    senderId = it.fromId,
                    receiverId = it.toId,
                    roomId = it.visitId,
                    message = text,
                    senderName = it.hwName,
                    roomName = it.patientName,
                    openMrsId = it.openMrsId,
                    patientId = it.patientId,
                    messageStatus = status.value,
                    type = "text",
                    createdAt = DateTimeUtils.getCurrentDateWithDBFormat(),
                    updatedAt = DateTimeUtils.getCurrentDateWithDBFormat()
                )
            }
            repository.addMessage(message)
            Timber.d { "sendMessage started" }
            executeNetworkCall {
                Timber.d { "sendMessage executeNetworkCall" }
                repository.sendMessage(message)
            }.collect {
                Timber.e { "Send Message status => ${it.status}" }
                status = when (it.status) {
                    Result.State.FAIL, Result.State.ERROR -> MessageStatus.FAIL
                    Result.State.SUCCESS -> MessageStatus.SENT
                    else -> MessageStatus.FAIL
                }
                Timber.e { "it.data => ${it.data?.toJson()}" }
                it.data?.messageId?.let { it1 ->
                    if (it1 == messageId) repository.changeMessageStatus(messageId, status)
                    else repository.updateMessageIdAndStatus(it1, status.value, message.message)
                    Timber.e { "updating local message " }
                }
            }
        }
    }

    fun ackMessageRead(messageId: Int) = viewModelScope.launch {
        repository.ackMessageRead(messageId)
    }

    fun loadConversation() = catchNetworkData({
        val patientId = roomConfig.patientId
        clearChatRoom()
        repository.getMessages(roomConfig.fromId, roomConfig.toId, patientId)
    }, { messages -> repository.saveMessages(messages) }).asLiveData()

    fun getChatRoomMessages() = repository.getChatRoomMessages(roomConfig.visitId)

    private fun clearChatRoom() {
        viewModelScope.launch { repository.clearChatRoom(roomConfig.visitId) }
    }

    fun connect(url: String) {
        if (chatClient.isConnected().not()) chatClient.connect(url)
    }
}