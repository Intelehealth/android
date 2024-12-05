package org.intelehealth.features.ondemand.mediator.utils

import android.content.Context
import android.content.Intent
import org.intelehealth.features.ondemand.mediator.CHAT_ROOM_IMPL_CLASS
import org.intelehealth.features.ondemand.mediator.VIDEO_CALL_IMPL_CLASS
import org.intelehealth.features.ondemand.mediator.createInstance
import org.intelehealth.features.ondemand.mediator.listener.ChatRoomMediator
import org.intelehealth.features.ondemand.mediator.listener.VideoCallMediator
import org.intelehealth.features.ondemand.mediator.model.ChatRoomConfig

/**
 * Created by Vaghela Mithun R. on 10-10-2024 - 11:37.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
object OnDemandIntentUtils {
    @JvmStatic
    fun openChatRoom(context: Context, chatRoomConfig: ChatRoomConfig) {
        val listener = createInstance<ChatRoomMediator>(CHAT_ROOM_IMPL_CLASS)
        listener?.openChatRoom(context, chatRoomConfig)
    }

    @JvmStatic
    fun initiateChatClient(context: Context) {
        val listener = createInstance<ChatRoomMediator>(CHAT_ROOM_IMPL_CLASS)
        listener?.initiateChatClient(context)
    }

    @JvmStatic
    fun startCallLog(context: Context?) {
        val listener = createInstance<VideoCallMediator>(VIDEO_CALL_IMPL_CLASS)
        listener?.startCallLogActivity(context)
    }

}