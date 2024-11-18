package org.intelehealth.feature.chat.ui.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.emitter.Emitter
import kotlinx.coroutines.launch
import org.intelehealth.core.network.state.Result
import org.intelehealth.core.ui.viewmodel.BaseViewModel
import org.intelehealth.feature.chat.ChatClient
import org.intelehealth.feature.chat.data.ChatRepository
import org.intelehealth.feature.chat.model.ChatMessage
import org.intelehealth.feature.chat.model.MessageStatus
import org.intelehealth.features.ondemand.mediator.model.ChatRoomConfig
import javax.inject.Inject

/**
 * Created by Vaghela Mithun R. on 18-07-2023 - 23:43.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatClient: ChatClient, private val repository: ChatRepository
) : BaseViewModel() {

    lateinit var roomConfig: ChatRoomConfig

    private val fileUploadBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

        }
    }

    private fun emitter(event: String) = Emitter.Listener {

    }

    fun registerReceivers(context: Context) {
        IntentFilter().apply {
//            addAction(AwsS3Utils.ACTION_FILE_UPLOAD_DONE)
            ContextCompat.registerReceiver(
                context, fileUploadBroadcastReceiver, this, ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }

    fun unregisterBroadcast(context: Context) {
        context.unregisterReceiver(fileUploadBroadcastReceiver)
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            var status = MessageStatus.SENDING
            executeNetworkCall {

                val message = roomConfig.let {
                    return@let ChatMessage(
                        senderId = it.fromId,
                        receiverId = it.toId,
                        roomId = it.visitId,
                        message = text,
                        senderName = it.hwName,
                        roomName = it.patientName,
                        openMrsId = it.openMrsId,
                        patientId = it.patientId,
                        messageStatus = status.value
                    )
                } ?: ChatMessage()
                repository.addMessage(message)
                repository.sendMessage(message)
            }.collect {
                status = when (it.status) {
                    Result.State.FAIL, Result.State.ERROR -> MessageStatus.FAIL
                    Result.State.SUCCESS -> MessageStatus.SENT
                    else -> MessageStatus.FAIL
                }
                it.data?.get(0)?.messageId?.let { it1 -> repository.changeMessageStatus(it1, status) }
            }
        }
    }

    fun loadConversation() = catchNetworkData({
        getChatRoomMessages(roomConfig.visitId)
    }, {
        val patientId = roomConfig.patientId ?: ""
        repository.getMessages(roomConfig.fromId, roomConfig.toId, patientId)
    }, { messages -> repository.saveMessages(messages) }).asLiveData()

    private fun getChatRoomMessages(roomId: String) = repository.getChatRoomMessages(roomId)

    fun connect(url: String) {
        if (chatClient.isConnected().not()) chatClient.connect(url)
    }

}