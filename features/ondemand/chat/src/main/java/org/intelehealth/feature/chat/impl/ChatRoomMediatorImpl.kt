package org.intelehealth.feature.chat.impl

import android.content.Context
import com.github.ajalt.timberkt.Timber
import org.intelehealth.feature.chat.ChatClient
import org.intelehealth.feature.chat.ui.activity.ChatRoomActivity
import org.intelehealth.features.ondemand.mediator.listener.ChatRoomMediator
import org.intelehealth.features.ondemand.mediator.model.ChatRoomConfig

/**
 * Created by Vaghela Mithun R. on 15-11-2024 - 14:00.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ChatRoomMediatorImpl : ChatRoomMediator {
    override fun openChatRoom(context: Context, chatRoomConfig: ChatRoomConfig) {
        ChatRoomActivity.startChatRoomActivity(context, chatRoomConfig)
    }

    override fun initiateChatClient(context: Context) {
        Timber.d { "ChatClient initiate" }
        ChatClient.getInstance(context)
    }
}