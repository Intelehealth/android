package org.intelehealth.feature.chat.binding

import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import org.intelehealth.feature.chat.R
import org.intelehealth.feature.chat.model.MessageStatus


/**
 * Created by Vaghela Mithun R. on 14-08-2023 - 20:36.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/

@BindingAdapter("android:status")
fun setMessageStatusIcon(textView: TextView, messageStatus: Int) {
    val drawable: Int
    val statusLbl: Int
    when (messageStatus) {
        MessageStatus.READ.value -> {
            drawable = R.drawable.ic_status_msg_read
            statusLbl = R.string.read
        }

        MessageStatus.DELIVERED.value -> {
            drawable = R.drawable.ic_status_msg_delivered
            statusLbl = R.string.msg_delivered
        }

        MessageStatus.SENT.value -> {
            drawable = R.drawable.ic_status_msg_sent
            statusLbl = R.string.sent
        }

        MessageStatus.SENDING.value -> {
            drawable = R.drawable.ic_status_msg_sending
            statusLbl = R.string.sending
        }

        MessageStatus.FAIL.value -> {
            drawable = R.drawable.ic_status_msg_fail
            statusLbl = R.string.fail
        }

        else -> {
            drawable = R.drawable.ic_status_msg_read
            statusLbl = R.string.read
        }
    }
    val index = textView.tag as Int
//    textView.isVisible = index == 0
    textView.text = textView.context.getText(statusLbl)
    textView.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}