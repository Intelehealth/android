package org.intelehealth.klivekit.call

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.hasKeyWithValueOfType
import com.google.gson.Gson
import org.intelehealth.klivekit.call.utils.IntentUtils
import org.intelehealth.klivekit.model.RtcArgs


/**
 * Created by Vaghela Mithun R. on 12-09-2024 - 12:43.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class CallServiceWorker(private val context: Context, private val workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        if (inputData.hasKeyWithValueOfType<String>(CALL_SERVICE_ARGS)) {
            val args = inputData.getString(CALL_SERVICE_ARGS)
            val data: RtcArgs = Gson().fromJson(args, RtcArgs::class.java)
            IntentUtils.getHeadsUpNotificationServiceIntent(data, context).also {
                ContextCompat.startForegroundService(context, it)
            }
        }
        return Result.success()
    }

    companion object {
        private const val CALL_SERVICE_TAG = "call_start_service"
        private const val CALL_SERVICE_ARGS = "call_service_args"
        fun startCallServiceWorker(messageBody: RtcArgs, context: Context) {
            Data.Builder().apply {
                val args = Gson().toJson(messageBody)
                putString(CALL_SERVICE_ARGS, args)
            }.also {
                OneTimeWorkRequest.Builder(CallServiceWorker::class.java).addTag(CALL_SERVICE_TAG)
                    .setInputData(it.build()).build().apply {
                        WorkManager.getInstance(context).enqueue(this)
                    }
            }
        }
    }
}