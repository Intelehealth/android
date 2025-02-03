package org.intelehealth.app.utilities

import android.content.Context
import android.content.Intent
import org.intelehealth.app.BuildConfig
import org.intelehealth.app.activities.onboarding.PersonalConsentActivity
import org.intelehealth.app.activities.onboarding.PrivacyPolicyActivity_New
import org.intelehealth.app.ui.patient.activity.PatientRegistrationActivity
import org.intelehealth.config.room.entity.PatientRegistrationFields
import java.lang.ref.WeakReference

class AddPatientUtils {
    companion object {

        @JvmStatic
        fun navigate(context: Context, regFields: List<PatientRegistrationFields>) {
            val weakContext = WeakReference(context)
            weakContext.get()?.let { ctx ->
                //2 = means two consents need to show
                if (regFields.size == 2) {
                    ctx.startActivity(Intent(
                        ctx, PrivacyPolicyActivity_New::class.java
                    ).apply {
                        putExtra(IntentKeys.INTENT_TYPE, IntentKeys.NAVIGATE_FURTHER)
                        putExtra(IntentKeys.ADD_PATIENT, IntentKeys.ADD_PATIENT)
                        putExtra(IntentKeys.IS_PERSONAL_CONSENT_REQUIRED, true)
                    })
                }
                //checking only telemedicine consent is required or not
                else if (regFields.size < 2 && (regFields.find { it.idKey == PatientRegConfigKeys.TELEMEDICINE_CONSENT_POLICY } != null)) {
                    ctx.startActivity(Intent(ctx, PersonalConsentActivity::class.java))
                }
                //checking privacy policy is required or not
                else if (regFields.size < 2 && (regFields.find { it.idKey == PatientRegConfigKeys.DATA_PRIVACY_POLICY } != null)) {
                    ctx.startActivity(Intent(
                        ctx, PrivacyPolicyActivity_New::class.java
                    ).apply {
                        putExtra(IntentKeys.INTENT_TYPE, IntentKeys.NAVIGATE_FURTHER)
                        putExtra(IntentKeys.ADD_PATIENT, IntentKeys.ADD_PATIENT)
                        putExtra(IntentKeys.IS_PERSONAL_CONSENT_REQUIRED, false)
                    })
                }
                //if no consents are required then we are redirecting to registration screen
                else {
                    PatientRegistrationActivity.startPatientRegistration(context)
                }

            }
        }
    }
}