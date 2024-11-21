package org.intelehealth.feature.chat.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.intelehealth.feature.chat.room.entity.ChatMessage


/**
 * Created by Vaghela Mithun R. on 04-01-2023 - 15:58.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
@Dao
interface ChatDao {
    @Query("SELECT * FROM tbl_chat_message")
    fun getAll(): Flow<List<ChatMessage>>

    @Query("SELECT * FROM tbl_chat_message where room_id =:roomId")
    fun getChatRoomMessages(roomId: String): LiveData<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<ChatMessage>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessage(message: ChatMessage)

    @Query("UPDATE tbl_chat_message SET message_status= :status where message_id =:messageId")
    suspend fun changeMessageStatus(messageId: Int, status: Int)

    @Update
    suspend fun updateMessage(message: ChatMessage)

    @Query("UPDATE tbl_chat_message SET message_status= :status, message_id =:messageId " + "where message =:message")
    suspend fun updateMessageIdAndStatus(messageId: Int, status: Int, message: String)

    @Query("UPDATE tbl_chat_message SET message_status= :status where message_id IN (:messageIds)")
    suspend fun changeMessageStatus(messageIds: List<Int>, status: Int)

    @Query("DELETE FROM tbl_chat_message")
    suspend fun deleteAll()

    @Query("DELETE FROM tbl_chat_message WHERE room_id =:roomId")
    suspend fun clearChatRoom(roomId: String)
}