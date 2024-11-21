package org.intelehealth.feature.chat.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.feature.chat.databinding.RowMsgItemReceiverBinding
import org.intelehealth.feature.chat.room.entity.ChatMessage
import org.intelehealth.feature.chat.model.ItemHeader
import org.intelehealth.feature.chat.ui.adapter.viewholder.ReceiverViewHolder

/**
 * Created by Vaghela Mithun R. on 14-08-2023 - 18:52.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class ReceiverMessageAdapter(context: Context, list: MutableList<ItemHeader>) :
    SenderMessageAdapter(context, list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == RECEIVER_MESSAGE) {
            val binding = RowMsgItemReceiverBinding.inflate(inflater, parent, false)
            ReceiverViewHolder(binding)
        } else super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItem(position) is ChatMessage) {
            val message = getItem(position) as ChatMessage
            if (holder is ReceiverViewHolder) holder.bind(message)
            else super.onBindViewHolder(holder, position)
        } else super.onBindViewHolder(holder, position)
    }

    companion object {
        const val RECEIVER_MESSAGE = 1100
    }
}