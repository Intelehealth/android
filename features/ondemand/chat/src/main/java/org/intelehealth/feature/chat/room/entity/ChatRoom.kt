package org.intelehealth.feature.chat.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Vaghela Mithun R. on 21-11-2024 - 12:47.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Entity("tbl_chat_room")
data class ChatRoom(
    @PrimaryKey(autoGenerate = true) var chatRoomId: Int = 0,
    @ColumnInfo(name = "sender_id") var senderId: String,
    @ColumnInfo(name = "receiver_id") var receiverId: String,
    @ColumnInfo(name = "room_id") var roomId: String,
    @ColumnInfo(name = "sender_name") var senderName: String,
    @ColumnInfo(name = "receiver_name") var receiverName: String,
    @ColumnInfo(name = "room_name") var roomName: String,
    @ColumnInfo(name = "sync_status") var syncStatus: Boolean
)
