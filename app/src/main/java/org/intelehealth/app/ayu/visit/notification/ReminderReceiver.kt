package org.intelehealth.app.ayu.visit.notification

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.intelehealth.app.R
import org.intelehealth.app.activities.visit.VisitActivity
import org.intelehealth.app.app.AppConstants
import org.intelehealth.app.app.IntelehealthApplication


class ReminderReceiver : BroadcastReceiver() {
    private var notificationHelper: NotificationHelper? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        notificationHelper = NotificationHelper(context)
        notificationHelper!!.createNotificationChannel()

        val mSharedPreference : SharedPreferences = IntelehealthApplication.getAppContext().getSharedPreferences(
            IntelehealthApplication.getAppContext().getString(R.string.prescription_share_key),
            Context.MODE_PRIVATE
        )
        val sharedAnyPrescription: Boolean = mSharedPreference.getBoolean(AppConstants.SHARED_ANY_PRESCRIPTION, false)

        if(!sharedAnyPrescription){
            var upc = 0
            val prescriptionListJson = mSharedPreference.getString(AppConstants.PRESCRIPTION_DATA_LIST, "")
            val secondNotificationFired: Boolean = mSharedPreference.getBoolean(AppConstants.SECOND_NOTIFICATION_FIRED, false)

            if (prescriptionListJson!!.isNotEmpty()) {
                val gson = Gson()
                val type = object : TypeToken<List<LocalPrescriptionInfo?>?>() {}.type
                val prescriptionDataList = gson.fromJson<List<LocalPrescriptionInfo>>(prescriptionListJson, type)
                for (lpi : LocalPrescriptionInfo in prescriptionDataList) {
                    if (!lpi.shareStatus) {
                        upc++
                    }
                }
            }
            if(upc > 0){
                val inForeground = isAppInForeground(context)
                if (inForeground) {
                    val localIntent = Intent("SHOW_DOCTOR_PRESCRIPTION_NOTIFICATION_FOREGROUND").apply {
                        putExtra("PRESCRIPTION_NOTIFICATION_TITLE", "$upc prescriptions")
                        putExtra("PRESCRIPTION_NOTIFICATION_SUBTITLE", "are pending to be sent to the patient")
                    }
                    LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent)
                } else {
                    notificationHelper!!.showNotification(
                        "$upc prescriptions",
                        "are pending to be sent to the patient",
                        VisitActivity::class.java
                    )
                }
                if(!secondNotificationFired){
                    scheduleNotification(mSharedPreference)
                    mSharedPreference.edit().putBoolean(AppConstants.SECOND_NOTIFICATION_FIRED, true).apply()
                }
            }
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

    private fun scheduleNotification(mSharedPreference: SharedPreferences) {
        val alarmManager = IntelehealthApplication.getAppContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(IntelehealthApplication.getAppContext(), ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            IntelehealthApplication.getAppContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val triggerTime = System.currentTimeMillis() + 2 * 60 * 60 * 1000
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
        mSharedPreference.edit().putBoolean(AppConstants.SHARED_ANY_PRESCRIPTION, false).apply()
    }
}
