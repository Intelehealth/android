package org.intelehealth.feature.chat.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.ajalt.timberkt.Timber
import org.intelehealth.feature.chat.databinding.RowMsgItemReceiverBinding
import org.intelehealth.feature.chat.databinding.RowMsgItemSenderBinding
import org.intelehealth.feature.chat.model.ChatMessage
import org.intelehealth.feature.chat.model.ItemHeader
import org.intelehealth.feature.chat.model.MessageStatus
import org.intelehealth.feature.chat.ui.adapter.viewholder.ReceiverViewHolder
import org.intelehealth.feature.chat.ui.adapter.viewholder.SenderViewHolder
import org.intelehealth.feature.chat.utils.LEFT_ITEM_DOCT
import org.intelehealth.feature.chat.utils.RIGHT_ITEM_HW

/**
 * Created by Vaghela Mithun R. on 14-08-2023 - 18:52.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ChatMessageAdapter(context: Context, list: MutableList<ItemHeader>) : ReceiverMessageAdapter(context, list) {
    var loginUserId: String? = null

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isHeader()) DateHeaderAdapter.DATE_HEADER
        else if (getItem(position) is ChatMessage) {
            val message = getItem(position) as ChatMessage
            if (message.senderId == loginUserId) SENDER_MESSAGE
            else RECEIVER_MESSAGE
        } else RECEIVER_MESSAGE
    }


//    fun markMessageAsRead(id: Int) {
//        for (i in items.indices) {
//            if (items.get(i) is ChatMessage) {
//                val chatMessage = items.get(i) as ChatMessage
//                //                if (id == chatMessage.getId()) {
//                chatMessage.isRead = true
//                chatMessage.messageStatus = MessageStatus.READ.value
//                notifyItemChanged(i)
//                //                    break;
////                }
//            }
//        }
//    }
//
//    fun markMessageAsDelivered(id: Int) {
//        for (i in items.indices) {
//            if (items.get(i) is ChatMessage) {
//                val chatMessage = items.get(i) as ChatMessage
//                if (id == chatMessage.messageId) {
//                    chatMessage.isRead = false
//                    chatMessage.messageStatus = MessageStatus.DELIVERED.value
//                    Timber.e { "markMessageAsDelivered: " + chatMessage.message }
//                    notifyItemChanged(i)
//                    break
//                }
//            }
//        }
//    }
//
//    fun updatedMessage(message: ChatMessage) {
//        for (i in items.indices) {
//            if (items.get(i) is ChatMessage) {
//                val chatMessage = items.get(i) as ChatMessage
//                if (message.message == chatMessage.message) {
//                    chatMessage.messageId = message.messageId
//                    chatMessage.messageStatus = message.messageStatus
//                    notifyItemChanged(i)
//                    break
//                }
//            }
//        }
//    }
}