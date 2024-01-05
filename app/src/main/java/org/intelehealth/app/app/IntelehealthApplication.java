package org.intelehealth.app.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.github.ajalt.timberkt.Timber;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.parse.Parse;
import com.rt.printerlibrary.printer.RTPrinter;

import org.intelehealth.app.BuildConfig;
import org.intelehealth.app.R;
import org.intelehealth.app.database.InteleHealthDatabaseHelper;
import org.intelehealth.app.utilities.BaseEnum;
import org.intelehealth.app.utilities.SessionManager;
import org.intelehealth.app.webrtc.activity.SilaCallLogActivity;
import org.intelehealth.app.webrtc.activity.SilaChatActivity;
import org.intelehealth.app.webrtc.activity.SilaVideoActivity;
import org.intelehealth.klivekit.RtcEngine;
import org.intelehealth.klivekit.socket.SocketManager;
import org.intelehealth.klivekit.utils.DateTimeResource;
import org.intelehealth.klivekit.utils.Manager;

import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

//Extend Application class with MultiDexApplication for multidex support
public class IntelehealthApplication extends MultiDexApplication {

    @BaseEnum.CmdType
    private static int currentCmdType = BaseEnum.CMD_PIN;
    private static RTPrinter rtPrinter;

    private static final String TAG = IntelehealthApplication.class.getSimpleName();
    private static Context mContext;
    private static String androidId;
    private Activity currentActivity;
    SessionManager sessionManager;

    public static Context getAppContext() {
        return mContext;
    }

    public static String getAndroidId() {
        return androidId;
    }

    private static IntelehealthApplication sIntelehealthApplication;
    public String refreshedFCMTokenID = "";
    public String webrtcTempCallId = "";

    private final SocketManager socketManager = SocketManager.getInstance();

    public static IntelehealthApplication getInstance() {
        return sIntelehealthApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sIntelehealthApplication = this;
        //For Vector Drawables Backward Compatibility(<API 21)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mContext = getApplicationContext();
        sessionManager = new SessionManager(this);
        // keeping the base url in one singleton object for using in apprtc module

        // Initialize
        //EzdxBT.initialize(getApplicationContext());
        configureCrashReporting();

        RxJavaPlugins.setErrorHandler(throwable -> {
            FirebaseCrashlytics.getInstance().recordException(throwable);
        });
        androidId = String
                .format("%16s", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))
                .replace(' ', '0');

//        String url = sessionManager.getServerUrl();
//        if (url == null) {
//            Log.i(TAG, "onCreate: Parse not init");
//        } else {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(1);
        dispatcher.setMaxRequests(4);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.dispatcher(dispatcher);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .clientBuilder(builder)
                .applicationId(AppConstants.IMAGE_APP_ID)
                .server(BuildConfig.SERVER_URL + ":1337/parse/")
                .build()
        );
        Log.i(TAG, "onCreate: Parse init");

        InteleHealthDatabaseHelper mDbHelper = new InteleHealthDatabaseHelper(this);
        SQLiteDatabase localdb = mDbHelper.getWritableDatabase();
        mDbHelper.onCreate(localdb);
//        }

        Timber.plant(Timber.DebugTree());
        initSocketConnection();
    }

    private void configureCrashReporting() {
//        CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder()
//                //.disabled(BuildConfig.DEBUG) // comment by Venu as per intelesafe
//                .build();
//        Fabric.with(this, new Crashlytics.Builder().core(crashlyticsCore).build());

//        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG);
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }


    /**
     * for setting the Alert Dialog Custom Font.
     *
     * @param context
     * @param builderDialog
     */
    public static void setAlertDialogCustomTheme(Context context, Dialog builderDialog) {
        // Getting the view elements
        TextView textView = (TextView) builderDialog.getWindow().findViewById(android.R.id.message);
        TextView alertTitle = (TextView) builderDialog.getWindow().findViewById(R.id.alertTitle);
        Button button1 = (Button) builderDialog.getWindow().findViewById(android.R.id.button1);
        Button button2 = (Button) builderDialog.getWindow().findViewById(android.R.id.button2);
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.lato_regular));
        alertTitle.setTypeface(ResourcesCompat.getFont(context, R.font.lato_bold));
        button1.setTypeface(ResourcesCompat.getFont(context, R.font.lato_bold));
        button2.setTypeface(ResourcesCompat.getFont(context, R.font.lato_bold));
    }


    @BaseEnum.CmdType
    public int getCurrentCmdType() {
        return currentCmdType;
    }

    public void setCurrentCmdType(@BaseEnum.CmdType int currentCmdType) {
        this.currentCmdType = currentCmdType;
    }

    public RTPrinter getRtPrinter() {
        return rtPrinter;
    }

    public void setRtPrinter(RTPrinter rtPrinter) {
        this.rtPrinter = rtPrinter;
    }

    /**
     * Socket should be open and close app level,
     * so when app create open it and close on app terminate
     */
    public void initSocketConnection() {
        DateTimeResource.build(this);
        Log.d(TAG, "initSocketConnection: ");
        if (sessionManager.getProviderID() != null && !sessionManager.getProviderID().isEmpty()) {
            Manager.getInstance().setBaseUrl(BuildConfig.SERVER_URL);
            String socketUrl = BuildConfig.SERVER_URL + ":3004" + "?userId="
                    + sessionManager.getProviderID()
                    + "&name=" + sessionManager.getChwname();
            if (!socketManager.isConnected()) socketManager.connect(socketUrl);
            initRtcConfig();
        }
    }

    private void initRtcConfig() {
        new RtcEngine.Builder()
                .callUrl(BuildConfig.LIVE_KIT_URL)
                .socketUrl(BuildConfig.SOCKET_URL + "?userId="
                        + sessionManager.getProviderID()
                        + "&name=" + sessionManager.getChwname())
                .callIntentClass(SilaVideoActivity.class)
                .chatIntentClass(SilaChatActivity.class)
                .callLogIntentClass(SilaCallLogActivity.class)
                .build().saveConfig(this);
    }

    @Override
    public void onTerminate() {
        Timber.tag("APP").d("onTerminate");
        disconnectSocket();
        super.onTerminate();
    }

    public void disconnectSocket() {
        socketManager.disconnect();
    }
}
