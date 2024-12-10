package org.intelehealth.app.utilities;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.intelehealth.app.R;
import org.intelehealth.app.activities.homeActivity.HomeScreenActivity_New;
import org.intelehealth.app.activities.patientSurveyActivity.PatientSurveyActivity_New;
import org.intelehealth.app.app.AppConstants;
import org.intelehealth.app.database.dao.EncounterDAO;
import org.intelehealth.app.database.dao.VisitsDAO;
import org.intelehealth.app.syncModule.SyncUtils;
import org.intelehealth.app.utilities.exception.DAOException;

public class VisitUtils {
    public static void endVisit(Context activityContext, String visitUUID, String patientUuid,
                                String followUpDate, String encounterVitals, String encounterUuidAdultIntial,
                                String state, String patientName, String intentTag) {
        //end visit
        if (visitUUID != null && !visitUUID.isEmpty()) {

            if (followUpDate != null && !followUpDate.equalsIgnoreCase("") && !followUpDate.equalsIgnoreCase("No")) {

                new DialogUtils().showCommonDialog(activityContext, R.drawable.ui2_ic_exit_app, activityContext.getResources().getString(R.string.alert_txt), activityContext.getString(R.string.visit_summary_follow_up_reminder) + " " + followUpDate, true, activityContext.getResources().getString(R.string.ok), activityContext.getResources().getString(R.string.cancel), new DialogUtils.CustomDialogListener() {
                    @Override
                    public void onDialogActionDone(int action) {
                        endVisitAndRedirectToHomeScreen(activityContext, visitUUID, patientUuid);
                    }
                });
            } else {
                endVisitAndRedirectToHomeScreen(activityContext, visitUUID, patientUuid);
            }
        } else {
            new DialogUtils().showCommonDialog(activityContext, 0, activityContext.getResources().getString(R.string.alert_txt), activityContext.getString(R.string.visit_summary_upload_reminder), true, activityContext.getResources().getString(R.string.ok), activityContext.getResources().getString(R.string.cancel), new DialogUtils.CustomDialogListener() {
                @Override
                public void onDialogActionDone(int action) {

                }
            });

        }
    }

    public static void endVisitAndRedirectToHomeScreen(Context activityContext, String visitUuid, String patientUuid) {
        VisitsDAO visitsDAO = new VisitsDAO();
        try {
            visitsDAO.updateVisitEnddate(visitUuid, AppConstants.dateAndTimeUtils.currentDateTime());
            NotificationSchedulerUtils.cancelNotification(visitUuid + "-" + AppConstants.FOLLOW_UP_SCHEDULE_ONE_DURATION);
            NotificationSchedulerUtils.cancelNotification(visitUuid + "-" + AppConstants.FOLLOW_UP_SCHEDULE_TWO_DURATION);

            if (NetworkConnection.isOnline(activityContext)) {
                new SyncUtils().syncForeground("survey");
            }

            SessionManager.getInstance(activityContext).removeVisitSummary(patientUuid, visitUuid);
            Intent i = new Intent(activityContext, HomeScreenActivity_New.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activityContext.startActivity(i);
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
}
