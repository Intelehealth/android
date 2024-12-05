package org.intelehealth.feature.chat.ui.adapter

import android.content.Context
import org.intelehealth.feature.chat.room.entity.ChatMessage
import org.intelehealth.feature.chat.model.ItemHeader

/**
 * Created by Vaghela Mithun R. on 14-08-2023 - 18:52.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ChatMessageAdapter(
    context: Context, list: MutableList<ItemHeader>
) : ReceiverMessageAdapter(context, list) {
    var loginUserId: String? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isHeader()) DateHeaderAdapter.DATE_HEADER
        else if (getItem(position) is ChatMessage) {
            val message = getItem(position) as ChatMessage
            if (message.senderId == loginUserId) SENDER_MESSAGE
            else RECEIVER_MESSAGE
        } else RECEIVER_MESSAGE
    }

    fun getLastMessageId() = (getItem(getList().size - 1) as ChatMessage).messageId
}