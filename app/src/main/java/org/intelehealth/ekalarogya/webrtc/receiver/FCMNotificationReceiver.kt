package org.intelehealth.ekalarogya.webrtc.receiver

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.github.ajalt.timberkt.Timber
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import org.intelehealth.ekalarogya.R
import org.intelehealth.ekalarogya.activities.homeActivity.HomeActivity
import org.intelehealth.ekalarogya.app.AppConstants
import org.intelehealth.ekalarogya.database.dao.PatientsDAO
import org.intelehealth.ekalarogya.database.dao.ProviderDAO
import org.intelehealth.ekalarogya.utilities.NotificationUtils
import org.intelehealth.ekalarogya.utilities.OfflineLogin
import org.intelehealth.ekalarogya.utilities.SessionManager
import org.intelehealth.ekalarogya.utilities.exception.DAOException
import org.intelehealth.ekalarogya.webrtc.activity.EkalChatActivity
import org.intelehealth.ekalarogya.webrtc.activity.EkalVideoActivity
import org.intelehealth.ekalarogya.webrtc.notification.AppNotification
import org.intelehealth.fcm.FcmBroadcastReceiver
import org.intelehealth.fcm.FcmNotification
import org.intelehealth.fcm.utils.FcmConstants
import org.intelehealth.klivekit.call.utils.CallHandlerUtils
import org.intelehealth.klivekit.call.utils.CallMode
import org.intelehealth.klivekit.call.utils.CallType
import org.intelehealth.klivekit.call.utils.IntentUtils
import org.intelehealth.klivekit.model.ChatMessage
import org.intelehealth.klivekit.model.RtcArgs
import org.intelehealth.klivekit.utils.Constants
import org.intelehealth.klivekit.utils.extensions.fromJson

/**
 * Created by Vaghela Mithun R. on 18-09-2023 - 10:14.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class FCMNotificationReceiver : FcmBroadcastReceiver() {
    override fun onMessageReceived(
        context: Context?,
        notification: RemoteMessage.Notification?,
        data: HashMap<String, String>
    ) {
        Timber.tag(TAG).d("onMessageReceived: $data ")
        val sessionManager = SessionManager(context)
        if (sessionManager.isLogout) return
        context?.let {
            if (data.containsKey("type") && data["type"].equals("video_call")) {

                Gson().fromJson<RtcArgs>(Gson().toJson(data)).apply {
                    nurseName = sessionManager.chwname
                    callType = CallType.VIDEO
                    url = "wss://" + sessionManager.serverUrl + ":9090"
                    socketUrl = Constants.BASE_URL + "?userId=" + nurseId + "&name=" + nurseName
                    PatientsDAO().getPatientName(roomId).apply {
                        patientName = get(0).name
                    }
                }.also { arg ->
                    if (isAppInForeground()) {
                        arg.callMode = CallMode.INCOMING
                        arg.className = EkalVideoActivity::class.java.name
                        CallHandlerUtils.saveIncomingCall(context, arg)
                        context.startActivity(IntentUtils.getCallActivityIntent(arg, context))
                    } else {
                        CallHandlerUtils.operateIncomingCall(it, arg, EkalVideoActivity::class.java)
                    }
                }
            } else if (data.containsKey("type") && data["type"].equals("text")) {
                showChatNotification(context, data)
            } else {
                parseMessage(notification, context, data)
            }
        }
    }

    private fun parseMessage(
        notification: RemoteMessage.Notification?,
        context: Context,
        data: HashMap<String, String>
    ) {
        Timber.e { "parseMessage Called" }
        notification?.let {
            when (notification.body) {
                "INVALIDATE_OFFLINE_LOGIN" -> {
                    //Invalidating Offline credentials
                    OfflineLogin.getOfflineLogin().invalidateLoginCredentials()
                }

                "UPDATE_MIND_MAPS" -> {
                    run {}
                    //Calling method to generate notification
                    sendNotification(notification, context, data)
                }

                else -> sendNotification(notification, context, data)
            }
        }
    }


    //This method is only generating push notification
    //It is same as we did in earlier posts
    private fun sendNotification(
        notification: RemoteMessage.Notification?,
        context: Context,
        data: HashMap<String, String>
    ) {
        Timber.e { "Webrtc: sendNotification" }
        val messageTitle = notification!!.title
        val messageBody = notification.body
        val clickAction: String? = data[FcmConstants.INTENT_CLICK_ACTION]
        val notificationIntent = Intent(context, HomeActivity::class.java).also {
            when (clickAction) {
                FcmConstants.FCM_PLUGIN_HOME_ACTIVITY -> {
                    it.putExtra(FcmConstants.INTENT_CLICK_ACTION, clickAction)
                    it.putExtra(
                        AppConstants.INTENT_VISIT_UUID,
                        data[AppConstants.INTENT_VISIT_UUID]
                    )
                    it.putExtra(
                        AppConstants.INTENT_PATIENT_ID,
                        data[AppConstants.INTENT_PATIENT_ID]
                    )
                }
            }
        }

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            NotificationUtils.getPendingIntentFlag()
        )

        FcmNotification.Builder(context)
            .channelName("EKAL")
            .title(messageTitle ?: "Ekal")
            .content(messageBody ?: "")
            .smallIcon(R.mipmap.ic_launcher)
            .contentIntent(pendingIntent)
            .build().startNotify()

//        val channelId = "CHANNEL_ID"
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder: NotificationCompat.Builder =
//            NotificationCompat.Builder(context, channelId)
//                .setSmallIcon(R.mipmap.ic_launcher) //.setContentTitle("Firebase Push Notification")
//                .setContentTitle(messageTitle)
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent)
//                .setColor(context.getResources().getColor(R.color.white))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        /*NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);*/
//        val notificationManager = NotificationManagerCompat.from(context)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name: CharSequence = "Default Channel"
//            val description = "Default Channel description"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(channelId, name, importance)
//            channel.description = description
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            notificationManager.createNotificationChannel(channel)
//        }
//        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun showChatNotification(context: Context?, data: HashMap<String, String>) {
        val args = RtcArgs()
        args.patientName = data["patientName"]
        args.patientId = data["patientId"]
        args.visitId = data["visitId"]
        args.nurseId = data["toUser"]
        args.doctorUuid = data["fromUser"]
        try {
            val title = ProviderDAO().getProviderName(args.doctorUuid)
            AppNotification.Builder(context)
                .title(title)
                .body(data["message"])
                .pendingIntent(EkalChatActivity.getPendingIntent(context, args))
                .send()
        } catch (e: DAOException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        const val TAG = "FCMNotificationReceiver"
    }
}