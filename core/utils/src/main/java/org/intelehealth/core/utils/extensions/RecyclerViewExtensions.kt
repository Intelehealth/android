package org.intelehealth.core.utils.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setupLinearView(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
    layoutManager = LinearLayoutManager(this.context)
    this.adapter = adapter
}

fun RecyclerView.setupChatList(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
    layoutManager = LinearLayoutManager(this.context).apply {
        stackFromEnd = true
        reverseLayout = false
    }
    this.adapter = adapter
}