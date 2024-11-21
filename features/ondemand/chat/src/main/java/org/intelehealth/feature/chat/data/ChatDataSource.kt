package org.intelehealth.feature.chat.data

import org.intelehealth.feature.chat.room.entity.ChatMessage
import org.intelehealth.feature.chat.restapi.ChatRestClient

/**
 * Created by Vaghela Mithun R. on 03-07-2023 - 16:22.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ChatDataSource(private val restClient: ChatRestClient) {
    suspend fun sendMessage(message: ChatMessage) = restClient.sendMessage(message)

    suspend fun ackMessageRead(messageId: Int) = restClient.ackMessageRead(messageId)

    suspend fun getMessages(
        from: String, to: String, patientId: String
    ) = restClient.getAllMessages(from, to, patientId)
}