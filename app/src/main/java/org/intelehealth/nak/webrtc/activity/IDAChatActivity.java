package org.intelehealth.nak.webrtc.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.intelehealth.fcm.utils.NotificationHandler;
import org.intelehealth.klivekit.chat.ui.activity.ChatActivity;
import org.intelehealth.klivekit.model.RtcArgs;
import org.intelehealth.nak.R;
import org.intelehealth.nak.database.dao.PatientsDAO;
import org.intelehealth.nak.utilities.SessionManager;
import org.intelehealth.nak.utilities.exception.DAOException;

/**
 * Created by Vaghela Mithun R. on 25-08-2023 - 16:43.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
public class IDAChatActivity extends ChatActivity {
    public static void startChatActivity(Context context, RtcArgs args) {
        Intent chatIntent = new Intent(context, IDAChatActivity.class);
        context.startActivity(buildExtra(chatIntent, args, context));
    }

    public static PendingIntent getPendingIntent(Context context, RtcArgs args) {
        Intent chatIntent = new Intent(context, IDAChatActivity.class);
        chatIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(context, 0, buildExtra(chatIntent, args, context),
                NotificationHandler.getPendingIntentFlag());
    }

    private static Intent buildExtra(Intent chatIntent, RtcArgs args, Context context) {
        try {
            chatIntent.putExtra("patientName", args.getPatientName());
            chatIntent.putExtra("visitUuid", args.getVisitId());
            chatIntent.putExtra("patientUuid", args.getPatientId());
            chatIntent.putExtra("fromUuid", args.getNurseId()); // provider uuid
            chatIntent.putExtra("isForVideo", false);
            chatIntent.putExtra("toUuid", args.getDoctorUuid());
            chatIntent.putExtra("hwName", new SessionManager(context).getChwname());
            chatIntent.putExtra("openMrsId", new PatientsDAO().getOpenmrsId(args.getPatientId()));
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return chatIntent;
    }

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_chat_ekal;
    }

    @Override
    protected void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        super.setupActionBar();
        toolbar.setNavigationOnClickListener(v -> finishAfterTransition());
    }

    @Override
    protected void initiateView() {
        mEmptyTextView = findViewById(R.id.empty_tv);
        mMessageEditText = findViewById(R.id.etMessageInput);
        mLoadingLinearLayout = findViewById(R.id.loading_layout);
        mEmptyLinearLayout = findViewById(R.id.empty_view);
        mRecyclerView = findViewById(R.id.rvConversation);
        mLayoutManager = new LinearLayoutManager(IDAChatActivity.this, LinearLayoutManager.VERTICAL, true);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}
