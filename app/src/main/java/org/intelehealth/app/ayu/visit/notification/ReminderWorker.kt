package org.intelehealth.app.ayu.visit.notification

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.intelehealth.app.activities.visit.VisitActivity
import org.intelehealth.app.app.IntelehealthApplication

class ReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private var notificationHelper: NotificationHelper? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        sendNotification()
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification() {
        notificationHelper = NotificationHelper(IntelehealthApplication.getAppContext())
        notificationHelper!!.createNotificationChannel()

        val inForeground = isAppInForeground(IntelehealthApplication.getAppContext())
        if (inForeground) {
            val localIntent = Intent("SHOW_DOCTOR_PRESCRIPTION_NOTIFICATION_FOREGROUND").apply {
                putExtra("PRESCRIPTION_NOTIFICATION_TITLE", "32 prescriptions")
                putExtra("PRESCRIPTION_NOTIFICATION_SUBTITLE", "are pending to be sent to the patient")
            }
            LocalBroadcastManager.getInstance(IntelehealthApplication.getAppContext()).sendBroadcast(localIntent)
        } else {
            notificationHelper!!.showNotification(
                "32 prescriptions",
                "are pending to be sent to the patient",
                VisitActivity::class.java
            )
        }
    }

    private fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses ?: return false

        for (processInfo in runningAppProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                processInfo.processName == context.packageName
            ) {
                return true
            }
        }
        return false
    }
}