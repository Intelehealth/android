package org.intelehealth.klivekit.call.utils

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import com.github.ajalt.timberkt.Timber
import org.intelehealth.klivekit.call.notification.CallReceiver
import org.intelehealth.klivekit.call.notification.HeadsUpNotificationService
import org.intelehealth.klivekit.call.utils.CallConstants.MAX_INT
import org.intelehealth.klivekit.model.RtcArgs
import org.intelehealth.klivekit.call.ui.activity.VideoCallActivity
import org.intelehealth.klivekit.utils.RTC_ARGS
import kotlin.random.Random

/**
 * Created by Vaghela Mithun R. on 8/30/2021.
 * vaghela@codeglo.com
 */
object IntentUtils {

    private const val REQUEST_CODE = 10001
    private const val CALL_RECEIVER_ACTION = "CALL_RECEIVER_ACTION"

    /**
     * @param context Context of current scope
     * @param messageBody an instance of CallNotificationMessageBody to send with intent
     * @return Intent ChatCallActivity intent to start
     */
    fun getCallActivityIntent(messageBody: RtcArgs, context: Context): Intent? {
        return messageBody.callCallName?.let {
            val callClass: Class<*> = Class.forName(it)
            return@let Intent(context, callClass).apply {
                putExtra(RTC_ARGS, messageBody)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }

    /**
     * @param context Context of current scope
     * @param messageBody an instance of RtcArgs to send with intent
     * @return Intent ChatCallActivity intent to start
     */
    fun getHeadsUpNotificationServiceIntent(messageBody: RtcArgs, context: Context): Intent {
        return Intent(context, HeadsUpNotificationService::class.java).apply {
            putExtra(RTC_ARGS, messageBody)
        }
    }

    /**
     * @param context Context of current scope
     * @param messageBody an instance of RtcArgs to send with intent
     * @return Intent ChatViewActivity intent to start
     */
    private fun getChatViewActivityIntent(
        messageBody: RtcArgs,
        context: Context
    ): Intent {
        return messageBody.callCallName?.let {
            val callClass: Class<*> = Class.forName(it)
            return@let Intent(context, callClass).apply {
                putExtra(RTC_ARGS, messageBody)
            }
        } ?: Intent(context, VideoCallActivity::class.java).apply {
            putExtra(RTC_ARGS, messageBody)
        }
    }

    /**
     * @param context Context of current scope
     * @param messageBody an instance of RtcArgs to send with intent
     * @return ChatCallBroadCastReceiver intent
     */
    private fun getCallBroadcastIntent(
        messageBody: RtcArgs,
        context: Context
    ): Intent {
        Timber.d { "getCallBroadcastIntent: ${messageBody.toJson()}" }
        return Intent(context, CallReceiver::class.java).apply {
            this.putExtra(RTC_ARGS, messageBody)
            this.action = getCallReceiverAction(context)
        }
    }

    /**
     * An specific action of notification will perform on pending intent
     * @param context Context of current scope
     * @param messageBody an instance of RtcArgs to send with intent
     * @return PendingIntent type of ChatCallBroadCastReceiver intent
     */
    private fun getActionPendingBroadCastIntent(
        context: Context,
        messageBody: RtcArgs
    ) = PendingIntent.getBroadcast(
        context,
        Random.nextInt(0, MAX_INT),
        getCallBroadcastIntent(messageBody, context),
        getPendingIntentFlag()
    )

    /**
     * An accept action of incoming call notification
     * @param context Context of current scope
     * @param messageBody an instance of RtcArgs to send with intent
     * @return PendingIntent type of ChatCallBroadCastReceiver intent
     */
    fun getAcceptPendingBroadCastIntent(
        context: Context,
        messageBody: RtcArgs
    ): PendingIntent {
        return getActionPendingBroadCastIntent(context, messageBody)
    }


    /**
     * A decline action of incoming call notification to notify sender that receiver is busy right now
     * @param context Context of current scope
     * @param messageBody an instance of RtcArgs to send with intent
     * @return PendingIntent type of ChatCallBroadCastReceiver intent
     */
    fun getDeclinePendingBroadCastIntent(
        context: Context,
        messageBody: RtcArgs
    ): PendingIntent {
        return getActionPendingBroadCastIntent(context, messageBody)
    }


    fun getBroadCastIntent(
        context: Context,
        messageBody: RtcArgs
    ): PendingIntent {
        return getActionPendingBroadCastIntent(context, messageBody)
    }


    /**
     * A general action of notification to perform task
     * @param context Context of current scope
     * @param messageBody an instance of RtcArgs to send with intent
     * @return PendingIntent type of ChatCallBroadCastReceiver intent
     */
    fun getPendingBroadCastIntent(
        context: Context,
        messageBody: RtcArgs
    ): PendingIntent = PendingIntent.getBroadcast(
        context,
        Random.nextInt(0, MAX_INT),
        getCallBroadcastIntent(messageBody, context),
        getPendingIntentFlag()
    )


    fun getPendingActivityIntent(
        context: Context,
        messageBody: RtcArgs
    ): PendingIntent {
        Timber.d { "getPendingActivityIntent -> url = ${messageBody.url}" }
//        return getCallActivityIntent(messageBody, context)?.let {
//            return@let getPendingIntentWithParentStack(context, it)
//        } ?:
        return PendingIntent.getActivity(
            context, Random.nextInt(0, MAX_INT),
            getCallActivityIntent(messageBody, context),
            getPendingIntentFlag()
        )
    }

    fun getChatViewPendingActivityIntent(
        context: Context,
        messageBody: RtcArgs
    ): PendingIntent = PendingIntent.getActivity(
        context, Random.nextInt(0, MAX_INT),
        getChatViewActivityIntent(messageBody, context),
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    )

    fun getOutGoingCallIntent(
        context: Context,
        messageBody: RtcArgs
    ): PendingIntent = PendingIntent.getActivity(
        context, Random.nextInt(0, MAX_INT),
        getCallActivityIntent(messageBody.apply {
//            callStatus = OUTGOING_CALL
//            val recId = receiverId
//            receiverId = msgFrom
//            msgFrom = recId

        }, context),
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    )


    /**
     * To get the value of chat view model
     * @return [ChatActivityIntent] model
     *
     **/

//    fun getChatActivityModel(messageBody: RtcArgs) = ChatActivityIntent(
//        receiverId = messageBody.msgFrom ?: "",
//        name1 = "${messageBody.receiverId}-${messageBody.msgFrom}",
//        name2 = "${messageBody.msgFrom}-${messageBody.receiverId}",
//        receiverName = messageBody.username ?: "",
//        age = messageBody.age.toString(),
//        profilePicture = messageBody.profilePicture ?: "",
//        matchScreen = false,
//        searchScreen = false
//    )

    private fun getPendingIntentWithParentStack(context: Context, intent: Intent): PendingIntent {
        val taskStackBuilder = TaskStackBuilder.create(context)
        taskStackBuilder.addNextIntentWithParentStack(intent)

        return taskStackBuilder.getPendingIntent(
            REQUEST_CODE,
            getPendingIntentFlag()
        )
    }

    private fun getPendingIntentFlag() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    fun getCallReceiverAction(context: Context) =
        "${context.applicationContext.packageName}.$CALL_RECEIVER_ACTION"
}