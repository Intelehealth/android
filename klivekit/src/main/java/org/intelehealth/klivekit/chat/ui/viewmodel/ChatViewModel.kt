package org.intelehealth.klivekit.chat.ui.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModel
import io.socket.emitter.Emitter
import org.intelehealth.klivekit.socket.SocketManager
import org.intelehealth.klivekit.ui.viewmodel.VideoCallViewModel
import org.intelehealth.klivekit.utils.AwsS3Utils

/**
 * Created by Vaghela Mithun R. on 18-07-2023 - 23:43.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class ChatViewModel(private val socketManager: SocketManager) : ViewModel() {

    init {
        socketManager.emitterListener = this::emitter
    }

    private val fileUploadBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

        }
    }

    private fun emitter(event: String) = Emitter.Listener {

    }

    fun registerReceivers(context: Context) {
        IntentFilter().apply {
            addAction(AwsS3Utils.ACTION_FILE_UPLOAD_DONE)
            context.registerReceiver(fileUploadBroadcastReceiver, this)
        }
    }

    fun unregisterBroadcast(context: Context) {
        context.unregisterReceiver(fileUploadBroadcastReceiver)
    }

    fun connect(url: String) {
        if (socketManager.isConnected().not()) socketManager.connect(url)
    }

}