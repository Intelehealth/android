package org.intelehealth.app.ayu.visit.notification

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import org.intelehealth.app.R


class MyNotificationManager(private val activity: Activity) {

    private var notificationView: View? = null

    fun showNotification(
        title: String,
        subtitle: String,
        onActionClick: () -> Unit
    ) {
        if (notificationView != null) return

        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)

        val container = FrameLayout(activity)
        container.setPadding(0, 50, 0, 0)
        notificationView = LayoutInflater.from(activity).inflate(
            R.layout.card_prescription_notification, rootView, false
        )
        container.addView(notificationView)

        rootView.addView(container)

        val actionButton = notificationView!!.findViewById<View>(R.id.btn_prescription_view)

        val spannable = SpannableString(title + " " + subtitle)
        spannable.setSpan(StyleSpan(Typeface.BOLD), 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(Color.BLACK), 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannable.setSpan(StyleSpan(Typeface.NORMAL), title.length + 1, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(Color.GRAY), title.length + 1, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val textView = notificationView!!.findViewById<TextView>(R.id.tv_prescription_count)
        textView.text = spannable

        actionButton.setOnClickListener {
            onActionClick()
            notificationView?.let {
                val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
                rootView.removeView(it)
                notificationView = null
            }
        }
    }
}
