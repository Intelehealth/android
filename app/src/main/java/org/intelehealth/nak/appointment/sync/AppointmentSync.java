package org.intelehealth.nak.appointment.sync;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import org.intelehealth.nak.BuildConfig;
import org.intelehealth.nak.app.AppConstants;
import org.intelehealth.nak.app.IntelehealthApplication;
import org.intelehealth.nak.appointment.api.ApiClientAppointment;
import org.intelehealth.nak.appointment.dao.AppointmentDAO;
import org.intelehealth.nak.appointment.model.AppointmentListingResponse;
import org.intelehealth.nak.utilities.SessionManager;
import org.intelehealth.nak.utilities.exception.DAOException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class AppointmentSync {
    private static final String TAG = "AppointmentSync";

    public static void getAppointments(Context context) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String selectedStartDate = simpleDateFormat.format(new Date());
        String selectedEndDate = simpleDateFormat.format(new Date(new Date().getTime() + 30L * 24 * 60 * 60 * 1000));

        //String baseUrl = BuildConfig.SERVER_URL + ":3004";
        String baseUrl = new SessionManager(context).getServerUrl() + ":3004";
        ApiClientAppointment.getInstance(baseUrl).getApi()
                .getSlotsAll(selectedStartDate, selectedEndDate, new SessionManager(context).getLocationUuid())

                .enqueue(new Callback<AppointmentListingResponse>() {
                    @Override
                    public void onResponse(Call<AppointmentListingResponse> call, retrofit2.Response<AppointmentListingResponse> response) {
                        if (response.body() == null) return;
                        AppointmentListingResponse slotInfoResponse = response.body();
                        AppointmentDAO appointmentDAO = new AppointmentDAO();
                        appointmentDAO.deleteAllAppointments();
                        for (int i = 0; i < slotInfoResponse.getData().size(); i++) {

                            try {
                                Log.v(TAG, "insert = "+new Gson().toJson(slotInfoResponse.getData().get(i)));
                                appointmentDAO.insert(slotInfoResponse.getData().get(i));
                            } catch (DAOException e) {
                                e.printStackTrace();
                            }
                        }

                        /*if (slotInfoResponse.getCancelledAppointments() != null) {
                            if (slotInfoResponse != null && slotInfoResponse.getCancelledAppointments().size() > 0) {
                                for (int i = 0; i < slotInfoResponse.getCancelledAppointments().size(); i++) {
                                    try {
                                        appointmentDAO.insert(slotInfoResponse.getCancelledAppointments().get(i));

                                    } catch (DAOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else {
                        }*/
                        Log.v(TAG, "getAppointments done!");
                        Intent broadcast = new Intent();
                        broadcast.putExtra("JOB", AppConstants.SYNC_APPOINTMENT_PULL_DATA_DONE);
                        broadcast.setAction(AppConstants.SYNC_NOTIFY_INTENT_ACTION);
                        context.sendBroadcast(broadcast);

                        IntelehealthApplication.getAppContext().sendBroadcast(new Intent(AppConstants.SYNC_INTENT_ACTION)
                                .putExtra(AppConstants.SYNC_INTENT_DATA_KEY, AppConstants.SYNC_APPOINTMENT_PULL_DATA_DONE));
                    }


                    @Override
                    public void onFailure(Call<AppointmentListingResponse> call, Throwable t) {
                        Log.v(TAG, t.getMessage());
                    }
                });

    }
}
