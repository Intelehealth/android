package org.intelehealth.app.shared;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.github.ajalt.timberkt.Timber;
import com.google.gson.Gson;

import org.intelehealth.app.database.dao.ProviderDAO;
import org.intelehealth.app.database.dao.RTCConnectionDAO;
import org.intelehealth.app.models.dto.ProviderDTO;
import org.intelehealth.app.models.dto.RTCConnectionDTO;
import org.intelehealth.app.ui.language.activity.LanguageActivity;
import org.intelehealth.app.utilities.exception.DAOException;
import org.intelehealth.config.presenter.feature.data.FeatureActiveStatusRepository;
import org.intelehealth.config.presenter.feature.factory.FeatureActiveStatusViewModelFactory;
import org.intelehealth.config.presenter.feature.viewmodel.FeatureActiveStatusViewModel;
import org.intelehealth.config.room.ConfigDatabase;
import org.intelehealth.config.room.entity.FeatureActiveStatus;
import org.intelehealth.core.socket.SocketManager;
import org.intelehealth.installer.downloader.DynamicDeliveryCallback;
import org.intelehealth.installer.downloader.DynamicModuleDownloadManager;
import org.intelehealth.installer.utils.DynamicModules;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Vaghela Mithun R. on 03-06-2023 - 19:29.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
public class BaseActivity extends LanguageActivity implements DynamicDeliveryCallback {
    private static final String TAG = "BaseActivity";
    private FeatureActiveStatus featureActiveStatus;
    protected DynamicModuleDownloadManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = DynamicModuleDownloadManager.getInstance(this);
//        SocketManager.getInstance().setNotificationListener(this);
        loadFeatureActiveStatus();
    }

    /**
     * This method will load the active/deactivate status of overall application feature
     * like appointment = false, vital = false means as per this status we need to hide/show
     * the specific feature from the app
     */
    private void loadFeatureActiveStatus() {
        FeatureActiveStatusRepository repository = new FeatureActiveStatusRepository(ConfigDatabase.getInstance(this).featureActiveStatusDao());
        FeatureActiveStatusViewModelFactory factory = new FeatureActiveStatusViewModelFactory(repository);
        FeatureActiveStatusViewModel featureActiveStatusViewModel = new ViewModelProvider(this, factory).get(FeatureActiveStatusViewModel.class);
        featureActiveStatusViewModel.fetchFeaturesActiveStatus().observe(this, featureActiveStatus -> {
            if (featureActiveStatus != null) onFeatureActiveStatusLoaded(featureActiveStatus);
        });
    }

    private void saveChatInfoLog(String visitId, String doctorId) throws DAOException {
        RTCConnectionDTO rtcDto = new RTCConnectionDTO();
        rtcDto.setUuid(UUID.randomUUID().toString());
        rtcDto.setVisitUUID(visitId);
        rtcDto.setConnectionInfo(doctorId);
        new RTCConnectionDAO().insert(rtcDto);
    }

    protected void onFeatureActiveStatusLoaded(FeatureActiveStatus activeStatus) {
        featureActiveStatus = activeStatus;
        Timber.tag(TAG).d("Active feature status=>%s", new Gson().toJson(activeStatus));
    }

    protected void uninstallModule(FeatureActiveStatus activeStatus) {
        Timber.tag(TAG).d("uninstallModule");
        ArrayList<String> list = new ArrayList<>();
        boolean hasVideoModule = manager.isModuleDownloaded(DynamicModules.MODULE_VIDEO);
        boolean hasChatModule = manager.isModuleDownloaded(DynamicModules.MODULE_CHAT);
        if (!activeStatus.getChatSection() && !activeStatus.getVideoSection()
                && hasChatModule && hasVideoModule) {
            list.add(DynamicModules.MODULE_VIDEO);
            list.add(DynamicModules.MODULE_CHAT);
        } else if (!activeStatus.getVideoSection() && hasVideoModule) {
            list.add(DynamicModules.MODULE_VIDEO);
        } else if (!activeStatus.getChatSection() && hasChatModule) {
            list.add(DynamicModules.MODULE_CHAT);
        }
        Timber.tag(TAG).d("uninstallModule=>%s", list.toString());
        if (!list.isEmpty()) manager.requestUninstall(list);
    }

    @Override
    protected void onResume() {
//        manager.registerListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
//        manager.unregisterListener();
        super.onPause();
    }

    @Override
    public void onDownloading(int percentage) {

    }

    @Override
    public void onDownloadCompleted() {

    }

    @Override
    public void onInstallSuccess() {

    }

    @Override
    public void onFailed(@NonNull String errorMessage) {

    }

    @Override
    public void onInstalling() {

    }
}
