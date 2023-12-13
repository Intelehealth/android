package org.intelehealth.nak.webrtc.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ajalt.timberkt.Timber;

import org.intelehealth.klivekit.model.ChatMessage;
import org.intelehealth.klivekit.model.RtcArgs;
import org.intelehealth.klivekit.socket.SocketManager;
import org.intelehealth.nak.database.dao.ProviderDAO;
import org.intelehealth.nak.database.dao.RTCConnectionDAO;
import org.intelehealth.nak.models.dto.RTCConnectionDTO;
import org.intelehealth.nak.utilities.exception.DAOException;
import org.intelehealth.nak.webrtc.notification.AppNotification;

import java.util.UUID;

/**
 * Created by Vaghela Mithun R. on 03-06-2023 - 19:29.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
public class BaseActivity extends AppCompatActivity implements SocketManager.NotificationListener {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SocketManager.getInstance().setNotificationListener(this);
    }

    @Override
    public void showNotification(@NonNull ChatMessage chatMessage) {
        RtcArgs args = new RtcArgs();
        args.setPatientName(chatMessage.getPatientName());
        args.setPatientId(chatMessage.getPatientId());
        args.setVisitId(chatMessage.getVisitId());
        args.setNurseId(chatMessage.getToUser());
        args.setDoctorUuid(chatMessage.getFromUser());
        try {
            String title = new ProviderDAO().getProviderName(args.getDoctorUuid());
            new AppNotification.Builder(this)
                    .title(title)
                    .body(chatMessage.getMessage())
                    .pendingIntent(IDAChatActivity.getPendingIntent(this, args))
                    .send();

            saveChatInfoLog(args.getVisitId(), args.getDoctorUuid());
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveChatInfoLog(String visitId, String doctorId) throws DAOException {
        RTCConnectionDTO rtcDto = new RTCConnectionDTO();
        rtcDto.setUuid(UUID.randomUUID().toString());
        rtcDto.setVisitUUID(visitId);
        rtcDto.setConnectionInfo(doctorId);
        new RTCConnectionDAO().insert(rtcDto);
    }

    @Override
    public void saveTheDoctor(@NonNull ChatMessage chatMessage) {
        try {
            saveChatInfoLog(chatMessage.getVisitId(), chatMessage.getFromUser());
        } catch (DAOException e) {
            Timber.tag(TAG).e(e.getThwStack(), "saveTheDoctor: ");
        }
    }
}
