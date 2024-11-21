package org.intelehealth.feature.chat.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.intelehealth.feature.chat.room.entity.ChatRoom


/**
 * Created by Vaghela Mithun R. on 04-01-2023 - 15:58.
 * Email : vaghela@codeglo.com
 * Mob   : +919727206702
 **/
@Dao
interface ChatRoomDao {

    @Query("SELECT * FROM tbl_chat_room where room_id =:roomId")
    fun getChatRoom(roomId: String): LiveData<ChatRoom>

    @Query("SELECT * FROM tbl_chat_room where room_id =:roomId AND sender_id =:senderId AND receiver_id =:receiverId")
    suspend fun getChatRoom(roomId: String, senderId: String, receiverId: String): ChatRoom

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChatRoom(chatRoom: ChatRoom)

    @Query("UPDATE tbl_chat_room SET sync_status= :status where room_id =:roomId")
    suspend fun updateChatRoomSyncStatus(roomId: Int, status: Boolean)

    @Query("UPDATE tbl_chat_room SET sync_status= :status where room_id =:roomId AND sender_id =:senderId AND receiver_id =:receiverId")
    suspend fun updateChatRoomSyncStatus(roomId: String, senderId: String, receiverId: String, status: Boolean)
}