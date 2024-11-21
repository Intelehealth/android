package org.intelehealth.feature.chat.room.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.intelehealth.core.utils.utility.DateTimeUtils
import org.intelehealth.feature.chat.model.ItemHeader

/**
 * Created by Vaghela Mithun R. on 03-07-2023 - 15:32.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@Parcelize
@Entity(tableName = "tbl_chat_message")
data class ChatMessage(
    @SerializedName("id") @ColumnInfo(name = "message_id") @PrimaryKey var messageId: Int = 0,

    @SerializedName("fromUser") @ColumnInfo(name = "sender_id")
    // Either doctorId or HW id
    var senderId: String = "",

    @ColumnInfo(name = "sender_name")
    // Either doctor name or HW name
    var senderName: String? = null,

    @SerializedName("hwPic") @ColumnInfo(name = "sender_dp")
    // Either HW or doctor profile picture
    var senderDp: String? = null,

    @SerializedName("toUser") @ColumnInfo(name = "receiver_id")
    // Either doctorId or HW id
    var receiverId: String = "",

    @SerializedName("hwName") @ColumnInfo(name = "receiver_name")
    // Either doctor name or HW name
    var receiverName: String? = null,

    @ColumnInfo(name = "receiver_dp")
    // Either HW or doctor profile picture
    var receiverDp: String? = null,

    @SerializedName("visitId") @ColumnInfo(name = "room_id")
    // visitId should be consider as roomId
    var roomId: String? = null,

    @SerializedName("patientName") @ColumnInfo(name = "room_name")
    // Patient name is a room name
    var roomName: String? = null,

    @SerializedName("patientPic") @ColumnInfo(name = "room_icon")
    // Patient profile picture is a room icon
    var roomIcon: String? = null,

    var message: String = "",

    @ColumnInfo(name = "message_status")
    /** Status is value of Enum class named MessageStatus
     * READ(3),
     * DELIVERED(2),
     * SENT(1),
     * SENDING(0)
     * FAIL(-1)
     */
    var messageStatus: Int = 0,

    @SerializedName("isRead") @Ignore var isRead: Boolean = false,

    @ColumnInfo(name = "patient_id") @SerializedName("patientId") var patientId: String? = null,

    @ColumnInfo(name = "open_mrs_id") var openMrsId: String? = null,

    @SerializedName("type") var type: String? = null,

    @SerializedName("createdAt") @ColumnInfo(name = "created_at") var createdAt: String? = null,

    @SerializedName("updatedAt") @ColumnInfo(name = "updated_at") var updatedAt: String? = null
) : ItemHeader, Parcelable {

    override fun createdDate(): String {
        return createdAt ?: DateTimeUtils.getCurrentDateWithDBFormat()
    }

    fun getMessageTime(): String? {
        val date = DateTimeUtils.parseUTCDate(createdDate(), DateTimeUtils.DB_FORMAT)
        return DateTimeUtils.formatToLocalDate(date, DateTimeUtils.MESSAGE_TIME_FORMAT)
    }

    fun getMessageDay(): String? {
        val date = DateTimeUtils.parseUTCDate(createdDate(), DateTimeUtils.DB_FORMAT)
        return DateTimeUtils.formatToLocalDate(date, DateTimeUtils.MESSAGE_DAY_FORMAT)
    }

    fun isAttachment(): Boolean {
        return if (type == null) false else type == "attachment"
    }

    fun toJson(): String? {
        return Gson().toJson(this)
    }

    override fun isHeader(): Boolean {
        return false
    }
}