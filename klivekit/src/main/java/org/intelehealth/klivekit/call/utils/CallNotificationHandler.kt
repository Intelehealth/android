package org.intelehealth.klivekit.call.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import org.intelehealth.klivekit.R
import org.intelehealth.klivekit.call.utils.CallConstants.ACTION_ACCEPT
import org.intelehealth.klivekit.call.utils.CallConstants.ACTION_DECLINE
import org.intelehealth.klivekit.call.utils.CallConstants.ACTION_HANG_UP
import org.intelehealth.klivekit.call.utils.CallConstants.MAX_INT
import org.intelehealth.klivekit.model.RtcArgs
import org.intelehealth.klivekit.utils.extensions.span
import org.intelehealth.klivekit.utils.getApplicationName
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


/**
 * Created by Vaghela Mithun R. on 8/28/2021.
 * vaghela@codeglo.com
 */
object CallNotificationHandler {

    private const val NOTIFICATION_CHANNEL_ID = "call_channel_id"
    private const val NOTIFICATION_CHANNEL_NAME = "call_channel"

    /**
     * Retrieve NotificationManager instance
     * @param context service context
     * @return NotificationManager instance
     */
    private fun getNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /**
     * Cancel running notification
     * @param notificationId cancelable notification id
     * @param context service context
     */
    fun cancelNotification(notificationId: Int, context: Context) {
        getNotificationManager(context).cancel(notificationId)
    }

    /**
     * An decline action for incoming call
     * @param context Context of current scope
     * @param messageBody an instance of CallNotificationMessageBody to send with intent
     * @return PendingIntent type of ChatCallBroadCastReceiver intent
     */
    private fun getDeclineAction(
        context: Context,
        messageBody: RtcArgs
    ) = NotificationCompat.Action.Builder(
        android.R.drawable.ic_menu_call,
        ACTION_DECLINE.span(android.R.color.holo_red_light, context),
        IntentUtils.getPendingBroadCastIntent(context, messageBody.apply {
            callAction = CallAction.DECLINE
        })
    ).build()

    /**
     * An accept action for incoming call
     * @param context Context of current scope
     * @param messageBody an instance of CallNotificationMessageBody to send with intent
     * @return PendingIntent type of ChatCallBroadCastReceiver intent
     */
    private fun getAcceptAction(
        context: Context,
        messageBody: RtcArgs
    ) = NotificationCompat.Action.Builder(
        android.R.drawable.ic_menu_call,
        ACTION_ACCEPT.span(android.R.color.holo_green_dark, context),
        IntentUtils.getPendingBroadCastIntent(context, messageBody.apply {
            callAction = CallAction.ACCEPT
        })
    ).build()

    /**
     * hangup the call
     * @param [context] service context
     *
     * */
    private fun getHangUpAction(
        context: Context,
        messageBody: RtcArgs
    ): NotificationCompat.Action = NotificationCompat.Action.Builder(
        android.R.drawable.ic_menu_call,
        ACTION_HANG_UP,
        IntentUtils.getPendingBroadCastIntent(context, messageBody.apply {
            callAction = CallAction.HANG_UP
            callStatus = CallStatus.ON_GOING
        })
    ).build()


    /**
     * Build call notification while user trying to connect
     * @param context Context of current scope
     * @param messageBody an instance of CallNotificationMessageBody to send with intent
     * @return NotificationCompat.Builder
     */
    fun outGoingCallNotificationBuilder(
        messageBody: RtcArgs,
        context: Context
    ): NotificationCompat.Builder {

        return NotificationCompat.Builder(context, getChannelId(context))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentTitle(messageBody.doctorName)
            .setContentText("Calling")
            .setSilent(true)
            .setColor(ContextCompat.getColor(context, R.color.blue_1))
            .setSmallIcon(messageBody.notificationIcon)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .addAction(getHangUpAction(context, messageBody.apply {
                callAction = CallAction.HANG_UP
            }))
    }

    /**
     * Build incoming call notification with accept and decline action when received
     * incoming call
     * @param context Context of current scope
     * @param messageBody an instance of CallNotificationMessageBody to send with intent
     * @return NotificationCompat.Builder
     */
    fun getIncomingNotificationBuilder(
        context: Context,
        messageBody: RtcArgs
    ): NotificationCompat.Builder {
        com.github.ajalt.timberkt.Timber.d { "getIncomingNotificationBuilder -> url = ${messageBody.url}" }
        val lockScreenIntent = IntentUtils.getPendingActivityIntent(context, messageBody)

//        val notificationIntent = IntentUtils.getPendingBroadCastIntent(context, messageBody)

        return NotificationCompat.Builder(context, getChannelId(context))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(getApplicationName(context))
            .setContentText("Incoming call from ${messageBody.doctorName ?: "unknown"}")
            .setColor(ContextCompat.getColor(context, R.color.blue_1))
            .setSmallIcon(messageBody.notificationIcon)
            .setSound(getDefaultRingtoneUrl())
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setContentIntent(lockScreenIntent)
            .setFullScreenIntent(lockScreenIntent, true)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .addAction(getAcceptAction(context, messageBody))
            .addAction(getDeclineAction(context, messageBody))
    }

    /**
     * Build on going call notification when user accept or attend any incoming call
     * @param context Context of current scope
     * @param messageBody an instance of CallNotificationMessageBody to send with intent
     * @return NotificationCompat.Builder
     */
    fun getAttendedCallNotificationBuilder(
        context: Context,
        messageBody: RtcArgs
    ): NotificationCompat.Builder {

        messageBody.notificationTime = SystemClock.elapsedRealtime().toString()
//        messageBody.callStatus = CallStatus.ON_GOING
        val notificationIntent = IntentUtils.getPendingActivityIntent(context, messageBody)
//        val notificationIntent = IntentUtils.getPendingBroadCastIntent(context, messageBody)

        Timber.d("Local time date ***** ${messageBody.notificationTime}")

        return NotificationCompat.Builder(context, getChannelId(context))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle("Ongoing call with ${messageBody.doctorName ?: "unknown"}")
//            .setContentText("Ongoing call with ${messageBody.doctorName ?: "unknown"}")
            .setColor(ContextCompat.getColor(context, R.color.blue_1))
            .setSmallIcon(messageBody.notificationIcon)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setContentIntent(notificationIntent)
            .setUsesChronometer(true)
            .setSilent(true)
            .addAction(getHangUpAction(context, messageBody.apply {
                callAction = CallAction.HANG_UP
            }))
    }

    /**
     * Generate NotificationChannel with priority and display setting
     * @param priority notification display priority setting
     * @return NotificationChannel
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getNotificationChannel(context: Context, priority: Int): NotificationChannel {

        return NotificationChannel(
            getChannelId(context),
            getChannelName(context),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableLights(true)
            enableVibration(false)
            setShowBadge(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            setBypassDnd(true)
            if (priority == 1) {
                setSound(getDefaultRingtoneUrl(), CallNotificationHandler.getAudioAttributes())
            }
        }
    }

    /**
     * Build missed call notification when user accept or attend any incoming call
     * @param context Context of current scope
     * @param messageBody an instance of CallNotificationMessageBody to send with intent
     * @return NotificationCompat.Builder
     */
    private fun buildMissedCallNotification(
        context: Context,
        messageBody: RtcArgs
    ): NotificationCompat.Builder {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        messageBody.notificationTime = sdf.format(Date())
        messageBody.callStatus = CallStatus.MISSED
        return NotificationCompat.Builder(context, getChannelId(context))
            .setPriority(NotificationCompat.PRIORITY_LOW)
//            .setContentTitle(getApplicationName(context))
            .setContentText("Missed call from ${messageBody.doctorName}")
            .setColor(ContextCompat.getColor(context, R.color.red))
            .setSmallIcon(messageBody.notificationIcon)
            .setCategory(NotificationCompat.CATEGORY_MISSED_CALL)
            .setAutoCancel(true)
            .setContentIntent(IntentUtils.getCallLogPendingIntent(context, messageBody))
            .setSilent(true)
//            .addAction(getCallAction(context, messageBody))
    }

    fun notifyMissedCall(context: Context, messageBody: RtcArgs) {

        val notificationManager = getNotificationManager(context)
        messageBody.notificationId = Random(System.currentTimeMillis()).nextInt(MAX_INT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(getNotificationChannel(context, 2))
        }

        notificationManager.notify(
            messageBody.notificationId,
            buildMissedCallNotification(context, messageBody).build()
        )
    }


    /**
     * Build AudioAttributes
     * @return AudioAttributes for notification ringtone
     */
    private fun getAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .build()
    }

    /**
     * To get default device ringtone uri using RingtoneManager
     * @return URI with default ringtone uri
     */
    private fun getDefaultRingtoneUrl() =
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

    private fun getChannelId(context: Context): String =
        context.applicationContext.packageName.apply {
            "${this}.$NOTIFICATION_CHANNEL_ID"
        }

    private fun getChannelName(context: Context): String =
        context.applicationContext.packageName.apply {
            "${this}.$NOTIFICATION_CHANNEL_NAME"
        }
}