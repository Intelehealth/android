package org.intelehealth.app.utilities

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.intelehealth.app.app.AppConstants
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Vaghela Mithun R. on 26-11-2024 - 14:50.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
object FirebaseUtils {
    val TAG: String = FirebaseUtils::class.java.name

    fun saveToken(context: Context?, userUUID: String?, fcmToken: String?, lang: String?) {
        Log.v(TAG, userUUID!!)
        Log.v(TAG, fcmToken!!)
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        // Start the queue
        requestQueue.start()
        try {
            if (userUUID == null || userUUID.isEmpty() || fcmToken == null || fcmToken.isEmpty()) {
                return
            }
            val inputJsonObject = JSONObject()
            inputJsonObject.put("user_uuid", userUUID)
            inputJsonObject.put("data", JSONObject().put("device_reg_token", fcmToken))
            inputJsonObject.put("locale", lang)

            val url: String = AppConstants.SAVE_FCM_TOKEN_URL
            Log.v(TAG, url)
            Log.v(TAG, inputJsonObject.toString())
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.PUT,
                url,
                inputJsonObject, { response -> Log.v(TAG, "saveToken -response - $response") },
                { error -> Log.v(TAG, "saveToken - onErrorResponse - " + Gson().toJson(error)) })
            jsonObjectRequest.setRetryPolicy(
                DefaultRetryPolicy(
                    7 * 1000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
            )
            requestQueue.add<JSONObject>(jsonObjectRequest)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}