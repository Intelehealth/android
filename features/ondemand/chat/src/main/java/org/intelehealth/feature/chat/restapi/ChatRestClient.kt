package org.intelehealth.feature.chat.restapi

import org.intelehealth.feature.chat.model.ChatMessage
import org.intelehealth.feature.chat.restapi.response.ChatResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by Vaghela Mithun R. on 30-08-2023 - 15:34.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
interface ChatRestClient {
    @POST("/api/messages/sendMessage")
    suspend fun sendMessage(message: ChatMessage): ChatResponse<List<ChatMessage>>

    @GET("/api/messages/{from}/{to}/{patientId}")
    suspend fun getAllMessages(
        @Path("from") from: String, @Path("to") to: String, @Path("patientId") patientId: String
    ): ChatResponse<List<ChatMessage>>
}