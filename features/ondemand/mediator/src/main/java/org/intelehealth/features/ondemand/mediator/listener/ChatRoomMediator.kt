package org.intelehealth.features.ondemand.mediator.listener

import android.content.Context
import org.intelehealth.features.ondemand.mediator.model.ChatRoomConfig

/**
 * Created by Vaghela Mithun R. on 15-11-2024 - 13:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
interface ChatRoomMediator {
    fun openChatRoom(context: Context, chatRoomConfig: ChatRoomConfig)

    fun initiateChatClient(context: Context)
}