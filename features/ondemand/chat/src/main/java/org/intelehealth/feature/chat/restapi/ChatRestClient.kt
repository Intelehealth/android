package org.intelehealth.feature.chat.restapi

import org.intelehealth.feature.chat.room.entity.ChatMessage
import org.intelehealth.feature.chat.restapi.response.ChatResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Created by Vaghela Mithun R. on 30-08-2023 - 15:34.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
interface ChatRestClient {
    @POST("/api/messages/sendMessage")
    suspend fun sendMessage(@Body message: ChatMessage): Response<ChatResponse<ChatMessage>>

    @PUT("/api/messages/read/{messageId}")
    suspend fun ackMessageRead(@Path("messageId") messageId: Int): Response<ChatResponse<List<Int>>>

    @GET("/api/messages/{from}/{to}/{patientId}")
    suspend fun getAllMessages(
        @Path("from") from: String, @Path("to") to: String, @Path("patientId") patientId: String
    ): Response<ChatResponse<List<ChatMessage>>>
}