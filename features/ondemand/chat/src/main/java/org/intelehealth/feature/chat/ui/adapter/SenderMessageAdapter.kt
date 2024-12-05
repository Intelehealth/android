package org.intelehealth.feature.chat.ui.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.ajalt.timberkt.Timber
import org.intelehealth.feature.chat.databinding.RowMsgItemSenderBinding
import org.intelehealth.feature.chat.room.entity.ChatMessage
import org.intelehealth.feature.chat.model.ItemHeader
import org.intelehealth.feature.chat.ui.adapter.viewholder.SenderViewHolder

/**
 * Created by Vaghela Mithun R. on 14-08-2023 - 18:52.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class SenderMessageAdapter(context: Context, list: MutableList<ItemHeader>) : DayHeaderAdapter(context, list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SENDER_MESSAGE) {
            val binding = RowMsgItemSenderBinding.inflate(inflater, parent, false)
            SenderViewHolder(binding)
        } else super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItem(position) is ChatMessage) {
            val message = getItem(position) as ChatMessage
            if (holder is SenderViewHolder) {
                holder.bind(message)
                holder.setStatusVisibility(position == getList().size - 1)
            } else super.onBindViewHolder(holder, position)
        } else super.onBindViewHolder(holder, position)
    }

    companion object {
        const val SENDER_MESSAGE = 1200
    }
}