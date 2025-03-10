package org.intelehealth.app.activities.patientDetailActivity;

import static org.intelehealth.app.abdm.activity.AadharMobileVerificationActivity.BEARER_AUTH;
import static org.intelehealth.app.abdm.utils.ABDMConstant.*;
import static org.intelehealth.app.activities.identificationActivity.IdentificationActivity_New.MOBILE_PAYLOAD;
import static org.intelehealth.app.activities.identificationActivity.IdentificationActivity_New.PAYLOAD;
import static org.intelehealth.app.utilities.DialogUtils.patientRegistrationDialog;
import static org.intelehealth.app.utilities.StringUtils.en__as_dob;
import static org.intelehealth.app.utilities.StringUtils.en__bn_dob;
import static org.intelehealth.app.utilities.StringUtils.en__gu_dob;
import static org.intelehealth.app.utilities.StringUtils.en__hi_dob;
import static org.intelehealth.app.utilities.StringUtils.en__kn_dob;
import static org.intelehealth.app.utilities.StringUtils.en__ml_dob;
import static org.intelehealth.app.utilities.StringUtils.en__mr_dob;
import static org.intelehealth.app.utilities.StringUtils.en__or_dob;
import static org.intelehealth.app.utilities.StringUtils.en__ru_dob;
import static org.intelehealth.app.utilities.StringUtils.en__ta_dob;
import static org.intelehealth.app.utilities.StringUtils.en__te_dob;
import static org.intelehealth.app.utilities.StringUtils.switch_as_caste_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_as_economic_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_as_education_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_bn_caste_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_bn_economic_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_bn_education_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_gu_caste_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_gu_economic_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_gu_education_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_hi_caste_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_hi_economic_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_hi_education_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_kn_caste_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_kn_economic_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_kn_education_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_ml_caste_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_ml_economic_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_ml_education_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_mr_caste_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_mr_economic_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_mr_education_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_or_caste_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_or_economic_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_or_education_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_ru_caste_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_ru_economic_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_ru_education_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_ta_caste_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_ta_economic_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_ta_education_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_te_caste_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_te_economic_edit;
import static org.intelehealth.app.utilities.StringUtils.switch_te_education_edit;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;

import org.intelehealth.app.R;
import org.intelehealth.app.abdm.activity.AadharMobileVerificationActivity;
import org.intelehealth.app.abdm.activity.AbhaCardActivity;
import org.intelehealth.app.abdm.model.AbhaCardResponseBody;
import org.intelehealth.app.abdm.model.AbhaProfileRequestBody;
import org.intelehealth.app.abdm.model.AbhaProfileResponse;
import org.intelehealth.app.abdm.model.MobileLoginOnOTPVerifiedResponse;
import org.intelehealth.app.abdm.model.OTPVerificationResponse;
import org.intelehealth.app.abdm.utils.ABDMConstant;
import org.intelehealth.app.abdm.utils.ABDMUtils;
import org.intelehealth.app.activities.homeActivity.HomeScreenActivity_New;
import org.intelehealth.app.activities.identificationActivity.IdentificationActivity_New;
import org.intelehealth.app.activities.identificationActivity.model.DistData;
import org.intelehealth.app.activities.identificationActivity.model.StateDistMaster;
import org.intelehealth.app.activities.searchPatientActivity.SearchPatientActivity_New;
import org.intelehealth.app.activities.visit.adapter.PastVisitListingAdapter;
import org.intelehealth.app.activities.visit.model.PastVisitData;
import org.intelehealth.app.activities.visitSummaryActivity.VisitSummaryActivity_New;
import org.intelehealth.app.app.AppConstants;
import org.intelehealth.app.app.IntelehealthApplication;
import org.intelehealth.app.ayu.visit.VisitCreationActivity;
import org.intelehealth.app.ayu.visit.common.VisitUtils;
import org.intelehealth.app.database.InteleHealthDatabaseHelper;
import org.intelehealth.app.database.dao.EncounterDAO;
import org.intelehealth.app.database.dao.ImagesDAO;
import org.intelehealth.app.database.dao.ObsDAO;
import org.intelehealth.app.database.dao.PatientsDAO;
import org.intelehealth.app.database.dao.VisitsDAO;
import org.intelehealth.app.knowledgeEngine.Node;
import org.intelehealth.app.models.dto.EncounterDTO;
import org.intelehealth.app.models.dto.PatientDTO;
import org.intelehealth.app.models.dto.VisitDTO;
import org.intelehealth.app.shared.BaseActivity;
import org.intelehealth.app.syncModule.SyncUtils;
import org.intelehealth.app.utilities.CameraUtils;
import org.intelehealth.app.utilities.DateAndTimeUtils;
import org.intelehealth.app.utilities.DialogUtils;
import org.intelehealth.app.utilities.DownloadFilesUtils;
import org.intelehealth.app.utilities.FileUtils;
import org.intelehealth.app.utilities.Logger;
import org.intelehealth.app.utilities.NetworkConnection;
import org.intelehealth.app.utilities.NetworkUtils;
import org.intelehealth.app.utilities.SessionManager;
import org.intelehealth.app.utilities.StringUtils;
import org.intelehealth.app.utilities.UrlModifiers;
import org.intelehealth.app.utilities.UuidDictionary;
import org.intelehealth.app.utilities.exception.DAOException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import timber.log.Timber;

public class PatientDetailActivity2 extends BaseActivity implements NetworkUtils.InternetCheckUpdateInterface {
    private static final String TAG = PatientDetailActivity2.class.getSimpleName();
    TextView name_txtview, openmrsID_txt, patientname, gender, patientdob, patientage, phone,
            postalcode, patientcountry, patientstate, patientdistrict, village, address1, addr2View,
            son_daughter_wife, patientoccupation, patientcaste, patienteducation, patienteconomicstatus, patientNationalID,
            patientAbhaNumber, patientAbhaAddress;
    SessionManager sessionManager = null;
    //    Patient patientDTO = new Patient();
    PatientsDAO patientsDAO = new PatientsDAO();
    private ProgressBar openMrsProgress;
    private boolean hasLicense = false;
    //SQLiteDatabase db = null;
    private PatientDTO patientDTO;
    String profileImage = "";
    String profileImage1 = "";
    Context context;
    String patientName, mGender;
    ImagesDAO imagesDAO = new ImagesDAO();
    float float_ageYear_Month;
    ImageView profile_image;
    LinearLayout personal_edit, address_edit, others_edit;
    //    Myreceiver reMyreceive;
    IntentFilter filter;
    Button startVisitBtn, btnViewAbhaCard;


    String privacy_value_selected;
    String phistory = "";

    private String encounterVitals = "";
    private String encounterAdultIntials = "";
    private boolean returning;
    private ImageView refresh, cancelbtn;
    private NetworkUtils networkUtils;
    String tag = "";
    private TableRow trAddress2;
    private OTPVerificationResponse otpVerificationResponse;
    private AbhaProfileResponse abhaProfileResponse;
    private String accessToken, xToken, txnId, SCOPE;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToHomeScreen();
    }

    public void goToHomeScreen() {
        Intent i = new Intent(PatientDetailActivity2.this, HomeScreenActivity_New.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail2);
        sessionManager = new SessionManager(this);
        String language = sessionManager.getAppLanguage();
        context = PatientDetailActivity2.this;


        networkUtils = new NetworkUtils(this, this);
        //In case of crash still the org should hold the current lang fix.
        if (!language.equalsIgnoreCase("")) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

        //db = IntelehealthApplication.inteleHealthDatabaseHelper.getWritableDatabase();
        filter = new IntentFilter("OpenmrsID");
//        reMyreceive = new Myreceiver();


        // changing status bar color
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.WHITE);
        }

        Intent intent = getIntent();
        if (intent != null) {
            tag = intent.getStringExtra("tag");
            if (intent.hasExtra("BUNDLE")) {
                Bundle args = intent.getBundleExtra("BUNDLE");
                patientDTO = (PatientDTO) args.getSerializable("patientDTO");
                accessToken = args.getString("accessToken");
                xToken = args.getString("xToken");
                txnId = args.getString("txnId");
            } else {
                patientDTO = new PatientDTO();
                patientDTO.setUuid(intent.getStringExtra("patientUuid"));
                accessToken = intent.getStringExtra("accessToken");
                xToken = intent.getStringExtra("xToken");
                txnId = intent.getStringExtra("txnId");
            }
            privacy_value_selected = intent.getStringExtra("privacy"); //intent value from IdentificationActivity.

            // abdm - start
            if (intent.hasExtra("BUNDLE")) {
                Bundle args = intent.getBundleExtra("BUNDLE");
                if (args.containsKey(PAYLOAD)) {
                    otpVerificationResponse = (OTPVerificationResponse) args.getSerializable(PAYLOAD);
                    if (otpVerificationResponse != null) {
                        //  setAutoFillValuesViaAadhar(otpVerificationResponse);
                    }
                } else if (args.containsKey(MOBILE_PAYLOAD)) {
                    SCOPE = SCOPE_MOBILE;
                    abhaProfileResponse = (AbhaProfileResponse) args.getSerializable(MOBILE_PAYLOAD);
                    if (abhaProfileResponse != null) {
                        //  setAutoFillValuesViaMobile(abhaProfileResponse);
                    }
                }
            }
            // abdm - end
        }

        initUI();


        personal_edit.setOnClickListener(v -> {
            Intent intent2 = new Intent(PatientDetailActivity2.this, IdentificationActivity_New.class);
            intent2.putExtra("patientUuid", patientDTO.getUuid());
            intent2.putExtra("ScreenEdit", "personal_edit");
            intent2.putExtra("patient_detail", true);
            Bundle args = new Bundle();
            args.putSerializable("patientDTO", (Serializable) patientDTO);
            // abha - start
            if (abhaProfileResponse != null)
                args.putSerializable(MOBILE_PAYLOAD, abhaProfileResponse);
            if (otpVerificationResponse != null)
                args.putSerializable(PAYLOAD, otpVerificationResponse);
            // abha - end
            intent2.putExtra("BUNDLE", args);
            startActivity(intent2);
            finish();
        });

        address_edit.setOnClickListener(v -> {
            Intent intent2 = new Intent(PatientDetailActivity2.this, IdentificationActivity_New.class);
            intent2.putExtra("patientUuid", patientDTO.getUuid());
            intent2.putExtra("ScreenEdit", "address_edit");
            intent2.putExtra("patient_detail", true);
            Bundle args = new Bundle();
            args.putSerializable("patientDTO", (Serializable) patientDTO);
            // abha - start
            if (abhaProfileResponse != null)
                args.putSerializable(MOBILE_PAYLOAD, abhaProfileResponse);
            if (otpVerificationResponse != null)
                args.putSerializable(PAYLOAD, otpVerificationResponse);
            // abha - end
            intent2.putExtra("BUNDLE", args);
            startActivity(intent2);
            finish();
        });

        others_edit.setOnClickListener(v -> {
            Intent intent2 = new Intent(PatientDetailActivity2.this, IdentificationActivity_New.class);
            intent2.putExtra("patientUuid", patientDTO.getUuid());
            intent2.putExtra("ScreenEdit", "others_edit");
            intent2.putExtra("patient_detail", true);
            Bundle args = new Bundle();
            args.putSerializable("patientDTO", (Serializable) patientDTO);
            // abha - start
            if (abhaProfileResponse != null)
                args.putSerializable(MOBILE_PAYLOAD, abhaProfileResponse);
            if (otpVerificationResponse != null)
                args.putSerializable(PAYLOAD, otpVerificationResponse);
            // abha - end
            intent2.putExtra("BUNDLE", args);
            startActivity(intent2);
            finish();
        });

        cancelbtn.setOnClickListener(v -> {
            goToHomeScreen();
        });

        btnViewAbhaCard.setOnClickListener(v -> {
            viewDownloadABHACard();
        });

        startVisitBtn.setOnClickListener(v -> {
            patientRegistrationDialog(context,
                    getResources().getDrawable(R.drawable.dialog_icon_complete),
                    getResources().getString(R.string.patient_registered),
                    getResources().getString(R.string.does_patient_start_visit_now),
                    getResources().getString(R.string.button_continue),
                    getResources().getString(R.string.cancel), new DialogUtils.CustomDialogListener() {
                        @Override
                        public void onDialogActionDone(int action) {
                            if (action == DialogUtils.CustomDialogListener.POSITIVE_CLICK) {
                                startVisit();
                            }
                        }
                    });
          /*  startVisitDialog(PatientDetailActivity2.this, getResources().getDrawable(R.drawable.dialog_visit_sent_success_icon),
                    "ssss", "swwwww", "yes", "no");*/ // todo: added jsut for testing purposes...
        });

        mPersonalHeaderRelativeLayout = findViewById(R.id.relative_personal_header);
        mAddressHeaderRelativeLayout = findViewById(R.id.relative_address_header);
        mOthersHeaderRelativeLayout = findViewById(R.id.relative_others_header);
        mPersonalHeaderRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout extraRelativeLayout = findViewById(R.id.ll_personal_extra);
                ImageView imageView = findViewById(R.id.iv_personal_drop);
                if (extraRelativeLayout.getVisibility() == View.VISIBLE) {
                    extraRelativeLayout.setVisibility(View.GONE);
                    imageView.setRotation(180);
                } else {
                    extraRelativeLayout.setVisibility(View.VISIBLE);
                    imageView.setRotation(0);
                }
            }
        });
        mAddressHeaderRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout extraRelativeLayout = findViewById(R.id.ll_address_extra);
                ImageView imageView = findViewById(R.id.iv_address_drop);
                if (extraRelativeLayout.getVisibility() == View.VISIBLE) {
                    extraRelativeLayout.setVisibility(View.GONE);
                    imageView.setRotation(180);
                } else {
                    extraRelativeLayout.setVisibility(View.VISIBLE);
                    imageView.setRotation(0);
                }
            }
        });
        mOthersHeaderRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout extraRelativeLayout = findViewById(R.id.ll_others_extra);
                ImageView imageView = findViewById(R.id.iv_others_drop);
                if (extraRelativeLayout.getVisibility() == View.VISIBLE) {
                    extraRelativeLayout.setVisibility(View.GONE);
                    imageView.setRotation(180);
                } else {
                    extraRelativeLayout.setVisibility(View.VISIBLE);
                    imageView.setRotation(0);
                }
            }
        });
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Toast.makeText(context, getString(R.string.sync_completed), Toast.LENGTH_SHORT).show();
                Timber.tag(TAG).v("Sync Done!");
                refresh.clearAnimation();
                syncAnimator.cancel();
                setDisplay(patientDTO.getUuid());
            }
        };
        IntentFilter filterSend = new IntentFilter();
        filterSend.addAction(AppConstants.SYNC_NOTIFY_INTENT_ACTION);
//        registerReceiver(mBroadcastReceiver, filterSend);
        ContextCompat.registerReceiver(this, mBroadcastReceiver, filterSend, ContextCompat.RECEIVER_NOT_EXPORTED);

        syncAnimator = ObjectAnimator.ofFloat(refresh, View.ROTATION, 0f, 359f).setDuration(1200);
        syncAnimator.setRepeatCount(ValueAnimator.INFINITE);
        syncAnimator.setInterpolator(new LinearInterpolator());

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void viewDownloadABHACard() {
        if (otpVerificationResponse != null && patientAbhaNumber != null && !patientAbhaNumber.getText().toString().isEmpty()) {
            String X_TOKEN = BEARER_AUTH + otpVerificationResponse.getTokens().getToken();
            callGETAbhaCardApi(X_TOKEN, accessToken, patientAbhaNumber.getText().toString());
            Timber.tag(TAG).d("viewDownloadABHACard: %s", X_TOKEN + " and " + patientAbhaNumber);
        } else if (abhaProfileResponse != null && abhaProfileResponse.getToken() != null && !abhaProfileResponse.getToken().isEmpty() &&
                patientAbhaNumber != null && !patientAbhaNumber.getText().toString().isEmpty()) {
            callGETAbhaCardApi(BEARER + abhaProfileResponse.getToken(), accessToken, patientAbhaNumber.getText().toString());
        } else if (xToken != null && !xToken.isEmpty() && patientAbhaNumber != null && !patientAbhaNumber.getText().toString().isEmpty()) {
            callGETAbhaCardApi(xToken, accessToken, patientAbhaNumber.getText().toString());
            Timber.tag(TAG).d("viewDownloadABHACard: %s", xToken + " and " + patientAbhaNumber);
        } else {  // ie. if token if expired or not available than go through the verification flow.
            Intent i = new Intent(context, AadharMobileVerificationActivity.class);
            i.putExtra("hasABHA", true);
            i.putExtra("abhaCard", true);
            startActivity(i);
        }
    }

    private void callGETAbhaCardApi(String xToken, String accessToken, String patientAbhaNumber) {
        Log.d(TAG, "callGETAbhaCardApi: " + accessToken + " : " + xToken);
        String url = UrlModifiers.getABHACardUrl();
        Single<AbhaCardResponseBody> responseBodySingle;

        if (sessionManager.getAbhaLoginType().equalsIgnoreCase(SessionManager.MOBILE_LOGIN)) {
            responseBodySingle = AppConstants.apiInterface.GET_ABHA_CARD(url, accessToken, SCOPE_MOBILE, xToken);
        } else if (sessionManager.getAbhaLoginType().equalsIgnoreCase(SessionManager.ABHA_LOGIN)) {
            responseBodySingle = AppConstants.apiInterface.GET_ABHA_CARD(url, accessToken, SCOPE_ABHA_ADDRESS, xToken);
        } else {
            responseBodySingle = AppConstants.apiInterface.GET_ABHA_CARD(url, accessToken, null, xToken);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                responseBodySingle
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableSingleObserver<AbhaCardResponseBody>() {
                            @Override
                            public void onSuccess(AbhaCardResponseBody abhaCardResponseBody) {
                                if (abhaCardResponseBody != null) {
                                    Log.d("callGETAbhaCardApi", "onSuccess: " + abhaCardResponseBody.toString());
                                    // TODO: here it will return base64 encoded image.
                                    Intent intent = new Intent(context, AbhaCardActivity.class);
                                    intent.putExtra("payload", abhaCardResponseBody);
                                    intent.putExtra("patientAbhaNumber", patientAbhaNumber);
                                    startActivity(intent);
                                    //  finish();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("callGETAbhaCardApi", "onError: " + e.toString());
                                Toast.makeText(PatientDetailActivity2.this, getString(R.string.session_expired_please_try_again), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(context, AadharMobileVerificationActivity.class);
                                i.putExtra("hasABHA", true);
                                i.putExtra("abhaCard", true);
                                startActivity(i);

                               /* cpd.dismiss();
                                Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                binding.sendOtpBtn.setEnabled(true);
                                binding.sendOtpBtn.setText(R.string.send_otp);  // Send otp.
                                binding.otpBox.setText("");
                                cancelResendAndHideView();*/    // todo: uncomment
                            }
                        });
            }
        }).start();
    }


    private BroadcastReceiver mBroadcastReceiver;
    private ObjectAnimator syncAnimator;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(setLocale(newBase));
    }

    public Context setLocale(Context context) {
        SessionManager sessionManager1 = new SessionManager(context);
        String appLanguage = sessionManager1.getAppLanguage();
        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();
        Locale locale = new Locale(appLanguage);
        Locale.setDefault(locale);
        conf.setLocale(locale);
        context.createConfigurationContext(conf);
        DisplayMetrics dm = res.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            conf.setLocales(new LocaleList(locale));
        } else {
            conf.locale = locale;
        }
        res.updateConfiguration(conf, dm);
        return context;
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            IntelehealthApplication.getInstance().isSync.observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isSync) {
                    if (isSync) {
                        Logger.logD("Update data",isSync.toString());
                        setDisplay(patientDTO.getUuid());
                        IntelehealthApplication.getInstance().isSync.postValue(false);
                    }
                }
            });
            setDisplay(patientDTO.getUuid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RelativeLayout mPersonalHeaderRelativeLayout, mAddressHeaderRelativeLayout, mOthersHeaderRelativeLayout;

    private void startVisit() {
        // before starting, we determine if it is new visit for a returning patient
        // extract both FH and PMH
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
        Date todayDate = new Date();
        String thisDate = currentDate.format(todayDate);


        String uuid = UUID.randomUUID().toString();
        EncounterDAO encounterDAO = new EncounterDAO();
        EncounterDTO encounterDTO = new EncounterDTO();
        encounterDTO.setUuid(UUID.randomUUID().toString());
        encounterDTO.setEncounterTypeUuid(encounterDAO.getEncounterTypeUuid("ENCOUNTER_VITALS"));
        encounterDTO.setEncounterTime(thisDate);
        encounterDTO.setVisituuid(uuid);
        encounterDTO.setSyncd(false);
        encounterDTO.setProvideruuid(sessionManager.getProviderID());
        Log.d("DTO", "DTO:detail " + encounterDTO.getProvideruuid());
        encounterDTO.setVoided(0);
        encounterDTO.setPrivacynotice_value(privacy_value_selected);//privacy value added.

        try {
            encounterDAO.createEncountersToDB(encounterDTO);
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        InteleHealthDatabaseHelper mDatabaseHelper = new InteleHealthDatabaseHelper(PatientDetailActivity2.this);
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getReadableDatabase();

        String CREATOR_ID = sessionManager.getCreatorID();
        returning = false;
        sessionManager.setReturning(returning);

        String[] cols = {"value"};
        Cursor cursor = sqLiteDatabase.query("tbl_obs", cols, "encounteruuid=? and conceptuuid=?",// querying for PMH (Past Medical History)
                new String[]{encounterAdultIntials, UuidDictionary.RHK_MEDICAL_HISTORY_BLURB},
                null, null, null);

        if (cursor.moveToFirst()) {
            // rows present
            do {
                // so that null data is not appended
                phistory = phistory + cursor.getString(0);

            }
            while (cursor.moveToNext());
            returning = true;
            sessionManager.setReturning(returning);
        }
        cursor.close();

        Intent intent2 = new Intent(PatientDetailActivity2.this, VisitCreationActivity.class);
        String fullName = patientDTO.getFirstname() + " " + patientDTO.getLastname();
        String patientUuid = patientDTO.getUuid();
        intent2.putExtra("patientUuid", patientUuid);
        VisitDTO visitDTO = new VisitDTO();
        visitDTO.setUuid(uuid);
        visitDTO.setPatientuuid(patientDTO.getUuid());
        visitDTO.setStartdate(thisDate);
        visitDTO.setVisitTypeUuid(UuidDictionary.VISIT_TELEMEDICINE);
        visitDTO.setLocationuuid(sessionManager.getLocationUuid());
        visitDTO.setSyncd(false);
        visitDTO.setCreatoruuid(sessionManager.getCreatorID());//static
        VisitsDAO visitsDAO = new VisitsDAO();

        try {
            visitsDAO.insertPatientToDB(visitDTO);
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        // visitUuid = String.valueOf(visitLong);
//                localdb.close();
        intent2.putExtra("patientUuid", patientUuid);
        intent2.putExtra("visitUuid", uuid);
        intent2.putExtra("encounterUuidVitals", encounterDTO.getUuid());
        intent2.putExtra("encounterUuidAdultIntial", "");
        intent2.putExtra("EncounterAdultInitial_LatestVisit", encounterAdultIntials);
        intent2.putExtra("name", fullName);
        intent2.putExtra("gender", mGender);
        intent2.putExtra("tag", "new");
        intent2.putExtra("float_ageYear_Month", float_ageYear_Month);
        startActivity(intent2);
        finish();
    }


    private void initUI() {
        refresh = findViewById(R.id.refresh);
        cancelbtn = findViewById(R.id.cancelbtn);

        profile_image = findViewById(R.id.profile_image);
        name_txtview = findViewById(R.id.name_txtview);
        openmrsID_txt = findViewById(R.id.openmrsID_txt);
        openMrsProgress = findViewById(R.id.openMrsProgress);

        patientname = findViewById(R.id.name);
        gender = findViewById(R.id.gender);
        patientdob = findViewById(R.id.dob);
        patientage = findViewById(R.id.age);
        phone = findViewById(R.id.phone);

        postalcode = findViewById(R.id.postalcode);
        patientcountry = findViewById(R.id.country);
        patientstate = findViewById(R.id.state);
        patientdistrict = findViewById(R.id.district);
        village = findViewById(R.id.village);
        address1 = findViewById(R.id.address1);
        trAddress2 = findViewById(R.id.tr_address_2);
        addr2View = findViewById(R.id.addr2View);

        son_daughter_wife = findViewById(R.id.son_daughter_wife);
        patientNationalID = findViewById(R.id.national_ID);
        patientAbhaNumber = findViewById(R.id.abhaNo);
        patientAbhaAddress = findViewById(R.id.abhaAddress);
        patientoccupation = findViewById(R.id.occupation);
        patientcaste = findViewById(R.id.caste);
        patienteducation = findViewById(R.id.education);
        patienteconomicstatus = findViewById(R.id.economicstatus);

        personal_edit = findViewById(R.id.personal_edit);
        address_edit = findViewById(R.id.address_edit);
        others_edit = findViewById(R.id.others_edit);
        cancelbtn = findViewById(R.id.cancelbtn);

        startVisitBtn = findViewById(R.id.startVisitBtn);
        btnViewAbhaCard = findViewById(R.id.btnViewAbhaCard);

        mCurrentVisitsRecyclerView = findViewById(R.id.rcv_open_visits);
        mCurrentVisitsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        mPastVisitsRecyclerView = findViewById(R.id.rcv_past_visits);
        mPastVisitsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        initForOpenVisit();
        initForPastVisit();
    }

    private RecyclerView mPastVisitsRecyclerView;
    private List<PastVisitData> mPastVisitDataList = new ArrayList<PastVisitData>();

    private RecyclerView mCurrentVisitsRecyclerView;
    private List<PastVisitData> mCurrentVisitDataList = new ArrayList<PastVisitData>();

    private void initForOpenVisit() {
        if (patientDTO == null || patientDTO.getUuid() == null) {
            return;
        }
        mCurrentVisitDataList.clear();
        SQLiteDatabase db = IntelehealthApplication.inteleHealthDatabaseHelper.getWritableDatabase();
        String visitSelection = "patientuuid = ?";
        String[] visitArgs = {patientDTO.getUuid()};
        String[] visitColumns = {"uuid", "startdate", "enddate"};
        String visitOrderBy = "startdate";
        Cursor visitCursor = db.query("tbl_visit", visitColumns, visitSelection, visitArgs, null, null, visitOrderBy);
        //if (visitCursor == null || visitCursor.getCount() <= 0) {
        //     findViewById(R.id.cv_open_visits).setVisibility(View.GONE);
        //    startVisitBtn.setVisibility(View.VISIBLE);
        //} else {
        //   findViewById(R.id.cv_open_visits).setVisibility(View.VISIBLE);
        //   startVisitBtn.setVisibility(View.GONE);
        if (visitCursor.moveToLast()) {
            do {
                EncounterDAO encounterDAO = new EncounterDAO();
                String date = visitCursor.getString(visitCursor.getColumnIndexOrThrow("startdate"));
                String end_date = visitCursor.getString(visitCursor.getColumnIndexOrThrow("enddate"));
                String visit_id = visitCursor.getString(visitCursor.getColumnIndexOrThrow("uuid"));

                boolean isCompletedExitedSurvey = false;
                try {
                    isCompletedExitedSurvey = new EncounterDAO().isCompletedExitedSurvey(visit_id);
                } catch (DAOException e) {
                    e.printStackTrace();
                }
                if (!isCompletedExitedSurvey) {

                    String encounterlocalAdultintial = "";
                    String encountervitalsLocal = null;
                    String encounterIDSelection = "visituuid = ?";

                    String[] encounterIDArgs = {visit_id};

                    Cursor encounterCursor = db.query("tbl_encounter", null, encounterIDSelection, encounterIDArgs, null, null, null);
                    if (encounterCursor != null && encounterCursor.moveToFirst()) {
                        do {
                            if (encounterDAO.getEncounterTypeUuid("ENCOUNTER_VITALS").equalsIgnoreCase(encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("encounter_type_uuid")))) {
                                encountervitalsLocal = encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("uuid"));
                            }
                            if (encounterDAO.getEncounterTypeUuid("ENCOUNTER_ADULTINITIAL").equalsIgnoreCase(encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("encounter_type_uuid")))) {
                                encounterlocalAdultintial = encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("uuid"));
                            }

                        } while (encounterCursor.moveToNext());
                    }
                    encounterCursor.close();

                    String previsitSelection = "encounteruuid = ? AND conceptuuid = ? and voided !='1'";
                    String[] previsitArgs = {encounterlocalAdultintial, UuidDictionary.CURRENT_COMPLAINT};
                    String[] previsitColumms = {"value", " conceptuuid", "encounteruuid"};
                    Cursor previsitCursor = db.query("tbl_obs", previsitColumms, previsitSelection, previsitArgs, null, null, null);
                    if (previsitCursor != null && previsitCursor.moveToLast()) {

                        String visitValue = previsitCursor.getString(previsitCursor.getColumnIndexOrThrow("value"));
                        boolean needToShowCoreValue = false;
                        if (visitValue.startsWith("{") && visitValue.endsWith("}")) {
                            try {
                                // isInOldFormat = false;
                                JSONObject jsonObject = new JSONObject(visitValue);
                                if (jsonObject.has("l-" + sessionManager.getAppLanguage())) {
                                    visitValue = jsonObject.getString("l-" + sessionManager.getAppLanguage());
                                    needToShowCoreValue = false;
                                } else {
                                    needToShowCoreValue = true;
                                    visitValue = jsonObject.getString("en");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            needToShowCoreValue = true;
                        }

                        if (visitValue != null && !visitValue.isEmpty()) {

                            if (needToShowCoreValue) {

                                visitValue = visitValue.replace("?<b>", Node.bullet_arrow);

                                String[] complaints = org.apache.commons.lang3.StringUtils.split(visitValue, Node.bullet_arrow);

                                visitValue = "";
                                String colon = ":";
                                if (complaints != null) {
                                    for (String comp : complaints) {
                                        if (!comp.trim().isEmpty()) {
                                            visitValue = visitValue + Node.bullet_arrow + comp.substring(0, comp.indexOf(colon)) + "<br/>";

                                        }
                                    }
                                    if (!visitValue.isEmpty()) {
                                        visitValue = visitValue.replaceAll(Node.bullet_arrow, "");
                                        visitValue = visitValue.replaceAll("<br/>", ", ");
                                        visitValue = visitValue.replaceAll(Node.ASSOCIATE_SYMPTOMS, "");
                                        //visitValue = visitValue.substring(0, visitValue.length() - 2);
                                        visitValue = visitValue.replaceAll("<b>", "");
                                        visitValue = visitValue.replaceAll("</b>", "");
                                        visitValue = visitValue.trim();
                                        while (visitValue.endsWith(",")) {
                                            visitValue = visitValue.substring(0, visitValue.length() - 1).trim();
                                        }
                                    }
                                }
                            } else {
                                String chiefComplain = "";
                                visitValue = visitValue.replaceAll("<.*?>", "");
                                System.out.println(visitValue);
                                Log.v(TAG, visitValue);
                                //►दस्त::● आपको ये लक्षण कब से है• 6 घंटे● दस्त शुरू कैसे हुए?•धीरे धीरे● २४ घंटे में कितनी बार दस्त हुए?•३ से कम बार● दस्त किस प्रकार के है?•पक्का● क्या आपको पिछले महीनो में दस्त शुरू होने से पहले किसी असामान्य भोजन/तरल पदार्थ से अपच महसूस हुआ है•नहीं● क्या आपने आज यहां आने से पहले इस समस्या के लिए कोई उपचार (स्व-दवा या घरेलू उपचार सहित) लिया है या किसी स्वास्थ्य प्रदाता को दिखाया है?•कोई नहीं● अतिरिक्त जानकारी•bsbdbd►क्या आपको निम्न लक्षण है::•उल्टीPatient denies -•दस्त के साथ पेट दर्द•सुजन•मल में खून•बुखार•अन्य [वर्णन करे]

                                String[] spt = visitValue.split("►");
                                List<String> list = new ArrayList<>();

                                StringBuilder stringBuilder = new StringBuilder();
                                for (String s : spt) {
                                    String complainName = "";
                                    if (s.isEmpty()) continue;
                                    //String s1 =  new String(s.getBytes(), "UTF-8");
                                    System.out.println(s);
                                    String[] spt1 = s.split("::●");
                                    complainName = spt1[0];

                                    //if (s.trim().startsWith(getTranslatedAssociatedSymptomQString(lCode))) {
                                    if (!complainName.trim().contains(VisitUtils.getTranslatedPatientDenies(sessionManager.getAppLanguage()))) {
                                        System.out.println(complainName);
                                        if (!stringBuilder.toString().isEmpty())
                                            stringBuilder.append(", ");
                                        stringBuilder.append(complainName);
                                    }

                                }
                                /*StringBuilder stringBuilder = new StringBuilder();
                                int size = list.size() == 1 ? list.size() : list.size() - 1;
                                for (int i = 0; i < size; i++) {
                                    String complainName = "";
                                    List<VisitSummaryData> visitSummaryDataList = new ArrayList<>();
                                    String[] spt1 = list.get(i).split("●");
                                    for (String value : spt1) {
                                        if (value.contains("::")) {
                                            if (!stringBuilder.toString().isEmpty())
                                                stringBuilder.append(",");
                                            complainName = value.replace("::", "");
                                            System.out.println(complainName);
                                            stringBuilder.append(complainName);
                                        }
                                    }*/
                                visitValue = stringBuilder.toString();

                            }
                            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            try {

                                Date formatted = currentDate.parse(date);
                                String visitDate = currentDate.format(formatted);
                                //createOldVisit(visitDate, visit_id, end_date, visitValue, encountervitalsLocal, encounterlocalAdultintial);
                                PastVisitData pastVisitData = new PastVisitData();
                                pastVisitData.setVisitDate(visitDate);
                                pastVisitData.setVisitUUID(visit_id);
                                pastVisitData.setChiefComplain(visitValue);
                                pastVisitData.setEncounterVitals(encountervitalsLocal);
                                pastVisitData.setEncounterAdultInitial(encounterlocalAdultintial);
                                mCurrentVisitDataList.add(pastVisitData);
                                Log.v(TAG, new Gson().toJson(mCurrentVisitDataList));

                            } catch (ParseException e) {
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }
                        }
                    }


                }
            } while (visitCursor.moveToPrevious());
        }
        Log.v(TAG, "initForOpenVisit - " + new Gson().toJson(mCurrentVisitDataList));
        if (!mCurrentVisitDataList.isEmpty()) {
            PastVisitListingAdapter pastVisitListingAdapter = new PastVisitListingAdapter(mCurrentVisitsRecyclerView, PatientDetailActivity2.this, mCurrentVisitDataList, new PastVisitListingAdapter.OnItemSelected() {
                @Override
                public void onItemSelected(PastVisitData pastVisitData) {
                    intentForVisitDetails(pastVisitData);
                }
            });
            mCurrentVisitsRecyclerView.setAdapter(pastVisitListingAdapter);
        }

        if (mCurrentVisitDataList.isEmpty()) {
            findViewById(R.id.cv_open_visits).setVisibility(View.GONE);
            startVisitBtn.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.cv_open_visits).setVisibility(View.VISIBLE);
            startVisitBtn.setVisibility(View.GONE);
        }
        // }
    }

    /**
     * @param pastVisitData
     */
    private void intentForVisitDetails(PastVisitData pastVisitData) {
        Intent in = new Intent(PatientDetailActivity2.this, VisitSummaryActivity_New.class);
        in.putExtra("patientUuid", patientDTO.getUuid());
        in.putExtra("visitUuid", pastVisitData.getVisitUUID());
        in.putExtra("gender", mGender);
        in.putExtra("name", patientName);
        in.putExtra("encounterUuidVitals", pastVisitData.getEncounterVitals());
        in.putExtra("encounterUuidAdultIntial", pastVisitData.getEncounterAdultInitial());
        in.putExtra("float_ageYear_Month", float_ageYear_Month);
        in.putExtra("tag", "VisitDetailsActivity");
        startActivity(in);
    }

    public void setDisplay(String dataString) {
        SQLiteDatabase db = IntelehealthApplication.inteleHealthDatabaseHelper.getReadableDatabase();
        patientDTO = new PatientDTO();
        String patientSelection = "uuid = ?";
        String[] patientArgs = {dataString};
        String[] patientColumns = {"uuid", "openmrs_id", "first_name", "middle_name", "last_name", "gender",
                "date_of_birth", "address1", "address2", "city_village", "state_province",
                "postal_code", "country", "phone_number", "gender", "sdw",
                "patient_photo", "abha_number", "abha_address"};
        Cursor idCursor = db.query("tbl_patient", patientColumns, patientSelection, patientArgs, null, null, null);
        if (idCursor.moveToFirst()) {
            do {
                patientDTO.setUuid(idCursor.getString(idCursor.getColumnIndexOrThrow("uuid")));
                patientDTO.setOpenmrsId(idCursor.getString(idCursor.getColumnIndexOrThrow("openmrs_id")));
                patientDTO.setFirstname(idCursor.getString(idCursor.getColumnIndexOrThrow("first_name")));
                patientDTO.setMiddlename(idCursor.getString(idCursor.getColumnIndexOrThrow("middle_name")));
                patientDTO.setLastname(idCursor.getString(idCursor.getColumnIndexOrThrow("last_name")));
                patientDTO.setGender(idCursor.getString(idCursor.getColumnIndexOrThrow("gender")));
                patientDTO.setDateofbirth(idCursor.getString(idCursor.getColumnIndexOrThrow("date_of_birth")));
                patientDTO.setAddress1(idCursor.getString(idCursor.getColumnIndexOrThrow("address1")));
                patientDTO.setAddress2(idCursor.getString(idCursor.getColumnIndexOrThrow("address2")));
                patientDTO.setCityvillage(idCursor.getString(idCursor.getColumnIndexOrThrow("city_village")));
                patientDTO.setStateprovince(idCursor.getString(idCursor.getColumnIndexOrThrow("state_province")));
                patientDTO.setPostalcode(idCursor.getString(idCursor.getColumnIndexOrThrow("postal_code")));
                patientDTO.setCountry(idCursor.getString(idCursor.getColumnIndexOrThrow("country")));
                patientDTO.setPhonenumber(idCursor.getString(idCursor.getColumnIndexOrThrow("phone_number")));
                patientDTO.setGender(idCursor.getString(idCursor.getColumnIndexOrThrow("gender")));
                patientDTO.setPatientPhoto(idCursor.getString(idCursor.getColumnIndexOrThrow("patient_photo")));
                patientDTO.setAbhaNumber(idCursor.getString(idCursor.getColumnIndexOrThrow("abha_number")));
                patientDTO.setAbhaAddress(idCursor.getString(idCursor.getColumnIndexOrThrow("abha_address")));

            } while (idCursor.moveToNext());
        }
        idCursor.close();

        String patientSelection1 = "patientuuid = ?";
        String[] patientArgs1 = {dataString};
        String[] patientColumns1 = {"value", "person_attribute_type_uuid"};
        Cursor idCursor1 = db.query("tbl_patient_attribute", patientColumns1, patientSelection1, patientArgs1, null, null, null);
        String name = "";
        if (idCursor1.moveToFirst()) {
            do {
                try {
                    name = patientsDAO.getAttributesName(idCursor1.getString(idCursor1.getColumnIndexOrThrow("person_attribute_type_uuid")));
                } catch (DAOException e) {
                    FirebaseCrashlytics.getInstance().recordException(e);
                }

                if (name.equalsIgnoreCase("caste")) {
                    patientDTO.setCaste(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Telephone Number")) {
                    patientDTO.setPhonenumber(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Education Level")) {
                    patientDTO.setEducation(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Economic Status")) {
                    patientDTO.setEconomic(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("occupation")) {
                    patientDTO.setOccupation(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Son/wife/daughter")) {
                    patientDTO.setSon_dau_wife(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("NationalID")) {
                    patientDTO.setNationalID(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("ProfileImageTimestamp")) {
                    profileImage1 = idCursor1.getString(idCursor1.getColumnIndexOrThrow("value"));
                }
                if (name.equalsIgnoreCase("createdDate")) {
                    patientDTO.setCreatedDate(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("providerUUID")) {
                    patientDTO.setProviderUUID(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }

            } while (idCursor1.moveToNext());
        }
        idCursor1.close();

        if (!sessionManager.getLicenseKey().isEmpty()) {
            hasLicense = true;
        }


        //changing patient to patientDTO object
        if (patientDTO.getMiddlename() == null) {
            patientName = patientDTO.getFirstname() + " " + patientDTO.getLastname();
        } else {
            patientName = patientDTO.getFirstname() + " " + patientDTO.getMiddlename() + " " + patientDTO.getLastname();
        }

        // setting patient name to the name textviews.
        name_txtview.setText(patientName);
        patientname.setText(patientName);


        // setting profile image of patient
        try {
            profileImage = imagesDAO.getPatientProfileChangeTime(patientDTO.getUuid());
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        if (patientDTO.getPatientPhoto() == null || patientDTO.getPatientPhoto().equalsIgnoreCase("")) {
            if (NetworkConnection.isOnline(getApplication())) {
                profilePicDownloaded();
            }
        }
        if (!profileImage.equalsIgnoreCase(profileImage1)) {
            if (NetworkConnection.isOnline(getApplication())) {
                profilePicDownloaded();
            }
        }
        Glide.with(this)
                .load(patientDTO.getPatientPhoto())
                .thumbnail(0.3f)
                .centerCrop()
                .error(R.drawable.avatar1)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(profile_image);

        // setting openmrs id
        if (patientDTO.getOpenmrsId() != null && !patientDTO.getOpenmrsId().isEmpty() && !patientDTO.getOpenmrsId().equalsIgnoreCase("NA")) {
            openmrsID_txt.setText(patientDTO.getOpenmrsId());
            openmrsID_txt.setVisibility(View.VISIBLE);
            openMrsProgress.setVisibility(View.GONE);

        }

        // setTitle(patientDTO.getOpenmrs_id());

        Log.e(TAG, "patientDTO - " + new Gson().toJson(patientDTO));
        // setting age
        String[] ymdData = DateAndTimeUtils.getAgeInYearMonth(patientDTO.getDateofbirth()).split(" ");
        int mAgeYears = Integer.parseInt(ymdData[0]);
        int mAgeMonths = Integer.parseInt(ymdData[1]);
        int mAgeDays = Integer.parseInt(ymdData[2]);
        String age = DateAndTimeUtils.formatAgeInYearsMonthsDate(this, mAgeYears, mAgeMonths, mAgeDays).replace("-", "");
        patientage.setText(age);
        float_ageYear_Month = DateAndTimeUtils.getFloat_Age_Year_Month(patientDTO.getDateofbirth());

        // setting date of birth
        String dob = DateAndTimeUtils.getFormatedDateOfBirthAsView(patientDTO.getDateofbirth());
        if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
            String dob_text = en__hi_dob(dob); //to show text of English into Hindi...
            patientdob.setText(dob_text);
        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
            String dob_text = en__or_dob(dob); //to show text of English into Odiya...
            patientdob.setText(dob_text);
        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("bn")) {
            String dob_text = en__bn_dob(dob); //to show text of English into Odiya...
            patientdob.setText(dob_text);
        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("gu")) {
            String dob_text = en__gu_dob(dob); //to show text of English into Gujarati...
            patientdob.setText(dob_text);
        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("te")) {
            String dob_text = en__te_dob(dob); //to show text of English into telugu...
            patientdob.setText(dob_text);
        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
            String dob_text = en__mr_dob(dob); //to show text of English into telugu...
            patientdob.setText(dob_text);
        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
            String dob_text = en__as_dob(dob); //to show text of English into telugu...
            patientdob.setText(dob_text);
        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ml")) {
            String dob_text = en__ml_dob(dob); //to show text of English into telugu...
            patientdob.setText(dob_text);
        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("kn")) {
            String dob_text = en__kn_dob(dob); //to show text of English into telugu...
            patientdob.setText(dob_text);
        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ru")) {
            String dob_text = en__ru_dob(dob); //to show text of English into Russian...
            patientdob.setText(dob_text);
        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ta")) {
            String dob_text = en__ta_dob(dob); //to show text of English into Tamil...
            patientdob.setText(dob_text);
        } else {
            patientdob.setText(dob);
        }

        // setting gender
        mGender = patientDTO.getGender();
        if (patientDTO.getGender() == null || patientDTO.getGender().equals("")) {
            gender.setVisibility(View.GONE);
        } else {
            if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                if (patientDTO.getGender().equalsIgnoreCase("M")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_male));
                } else if (patientDTO.getGender().equalsIgnoreCase("F")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_female));
                } else if (patientDTO.getGender().equalsIgnoreCase("O")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_other));
                } else {
                    gender.setText(patientDTO.getGender());
                }
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                if (patientDTO.getGender().equalsIgnoreCase("M")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_male));
                } else if (patientDTO.getGender().equalsIgnoreCase("F")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_female));
                } else if (patientDTO.getGender().equalsIgnoreCase("O")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_other));
                } else {
                    gender.setText(patientDTO.getGender());
                }
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("te")) {
                if (patientDTO.getGender().equalsIgnoreCase("M")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_male));
                } else if (patientDTO.getGender().equalsIgnoreCase("F")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_female));
                } else if (patientDTO.getGender().equalsIgnoreCase("O")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_other));
                } else {
                    gender.setText(patientDTO.getGender());
                }
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ml")) {
                if (patientDTO.getGender().equalsIgnoreCase("M")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_male));
                } else if (patientDTO.getGender().equalsIgnoreCase("F")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_female));
                } else if (patientDTO.getGender().equalsIgnoreCase("O")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_other));
                } else {
                    gender.setText(patientDTO.getGender());
                }
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
                if (patientDTO.getGender().equalsIgnoreCase("M")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_male));
                } else if (patientDTO.getGender().equalsIgnoreCase("F")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_female));
                } else if (patientDTO.getGender().equalsIgnoreCase("O")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_other));
                } else {
                    gender.setText(patientDTO.getGender());
                }
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
                if (patientDTO.getGender().equalsIgnoreCase("M")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_male));
                } else if (patientDTO.getGender().equalsIgnoreCase("F")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_female));
                } else if (patientDTO.getGender().equalsIgnoreCase("O")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_other));
                } else {
                    gender.setText(patientDTO.getGender());
                }
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("kn")) {
                if (patientDTO.getGender().equalsIgnoreCase("M")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_male));
                } else if (patientDTO.getGender().equalsIgnoreCase("F")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_female));
                } else if (patientDTO.getGender().equalsIgnoreCase("Other")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_other));
                } else {
                    gender.setText(patientDTO.getGender());
                }
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ru")) {
                if (patientDTO.getGender().equalsIgnoreCase("M")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_male));
                } else if (patientDTO.getGender().equalsIgnoreCase("F")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_female));
                } else if (patientDTO.getGender().equalsIgnoreCase("O")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_other));
                } else {
                    gender.setText(patientDTO.getGender());
                }
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("gu")) {
                if (patientDTO.getGender().equalsIgnoreCase("M")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_male));
                } else if (patientDTO.getGender().equalsIgnoreCase("F")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_female));
                } else if (patientDTO.getGender().equalsIgnoreCase("O")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_other));
                } else {
                    gender.setText(patientDTO.getGender());
                }
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("bn")) {
                if (patientDTO.getGender().equalsIgnoreCase("M")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_male));
                } else if (patientDTO.getGender().equalsIgnoreCase("F")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_female));
                } else if (patientDTO.getGender().equalsIgnoreCase("O")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_other));
                } else {
                    gender.setText(patientDTO.getGender());
                }
            } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ta")) {
                if (patientDTO.getGender().equalsIgnoreCase("M")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_male));
                } else if (patientDTO.getGender().equalsIgnoreCase("F")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_female));
                } else if (patientDTO.getGender().equalsIgnoreCase("O")) {
                    gender.setText(getResources().getString(R.string.identification_screen_checkbox_other));
                } else {
                    gender.setText(patientDTO.getGender());
                }
            } else {
                gender.setText(patientDTO.getGender());
            }
        }

        // setting address1
        if (patientDTO.getAddress1() == null || patientDTO.getAddress1().equals("")) {
            //  address1.setVisibility(View.GONE);
            address1.setText(getResources().getString(R.string.no_address_added));
        } else {
            address1.setText(patientDTO.getAddress1());
        }

        if (patientDTO.getAddress2() == null || patientDTO.getAddress2().equals("")) { //
            trAddress2.setVisibility(View.GONE);
        } else {
            addr2View.setText(patientDTO.getAddress2());
        }

        // setting country
        String country;
        if (patientDTO.getCountry() != null) {
            country = patientDTO.getCountry().trim();
        } else {
            country = getResources().getString(R.string.no_country_added);
        }
        patientcountry.setText(StringUtils.switch_hi_en_country(country, sessionManager.getAppLanguage())); // empty in case of abha.

        // setting state
        String state;
        if (patientDTO.getStateprovince() != null) {
            state = patientDTO.getStateprovince().trim();
        } else {
            state = getResources().getString(R.string.no_state_added);
        }
        patientstate.setText(getStateTranslated(state, sessionManager.getAppLanguage()));

        // setting district and city
        String[] district_city = null;
        if (!TextUtils.isEmpty(patientDTO.getCityvillage())) {
            district_city = patientDTO.getCityvillage().trim().split(":");
        }
        String district = null;
        String city_village = null;
        if (district_city != null && district_city.length == 2) {
            district = district_city[0];
            city_village = district_city[1];
        }

        // district = start
        if (district != null) {
            try {
                patientdistrict.setText(getDistrictTranslated(state, district, sessionManager.getAppLanguage()));
            } catch (Exception e) {
                // if (otpVerificationResponse != null || abhaProfileResponse != null) {
                if (district != null)
                    patientdistrict.setText(district);
                else
                    patientdistrict.setText(getResources().getString(R.string.no_district_added));
                //  }
            }

        } else {
            patientdistrict.setText(getResources().getString(R.string.no_district_added));
        }
        // district = end

        // city - start
        if (city_village != null) {
            village.setText(city_village);
        } else {
            village.setText(getResources().getString(R.string.no_city_added));
        }
        // end - city and district

        // setting postal code
        if (patientDTO.getPostalcode() != null) {
            postalcode.setText(patientDTO.getPostalcode());
        } else {
            postalcode.setText(getResources().getString(R.string.no_postal_code_added));
        }

        // setting phone number
        if (patientDTO.getPhonenumber() != null && !patientDTO.getPhonenumber().isEmpty()) {
            phone.setText(patientDTO.getPhonenumber());
        } else {
            phone.setText(getResources().getString(R.string.no_mobile_number_added));
        }

        // setting education status
        if (patientDTO.getEducation() != null) {
            if (patientDTO.getEducation().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                patienteducation.setText("नहीं दिया गया");
            } else if (patientDTO.getEducation().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                patienteducation.setText("ଦିଅ ଯାଇ ନାହିଁ");
            } else if (patientDTO.getEducation().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("gu")) {
                patienteducation.setText("પૂરી પાડવામાં આવેલ નથી");
            } else if (patientDTO.getEducation().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("te")) {
                patienteducation.setText("సమకూర్చబడలేదు");
            } else if (patientDTO.getEducation().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
                patienteducation.setText("झाले नाही");
            } else if (patientDTO.getEducation().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
                patienteducation.setText("প্ৰদান কৰা হোৱা নাই");
            } else if (patientDTO.getEducation().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("ml")) {
                patienteducation.setText("നൽകിയിട്ടില്ല");
            } else if (patientDTO.getEducation().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("kn")) {
                patienteducation.setText("ಒದಗಿಸಲಾಗಿಲ್ಲ");
            } else if (patientDTO.getEducation().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("ru")) {
                patienteducation.setText("Не предоставлен");
            } else if (patientDTO.getEducation().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("bn")) {
                patienteducation.setText("সরবরাহ করা হয়নি");
            } else if (patientDTO.getEducation().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("ta")) {
                patienteducation.setText("வழங்கப்படவில்லை");
            } else {
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    String education = switch_hi_education_edit(patientDTO.getEducation());
                    patienteducation.setText(education);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                    String education = switch_or_education_edit(patientDTO.getEducation());
                    patienteducation.setText(education);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ta")) {
                    String education = switch_ta_education_edit(patientDTO.getEducation());
                    patienteducation.setText(education);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("te")) {
                    String education = switch_te_education_edit(patientDTO.getEducation());
                    patienteducation.setText(education);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
                    String education = switch_mr_education_edit(patientDTO.getEducation());
                    patienteducation.setText(education);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
                    String education = switch_as_education_edit(patientDTO.getEducation());
                    patienteducation.setText(education);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ml")) {
                    String education = switch_ml_education_edit(patientDTO.getEducation());
                    patienteducation.setText(education);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("kn")) {
                    String education = switch_kn_education_edit(patientDTO.getEducation());
                    patienteducation.setText(education);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ru")) {
                    String education = switch_ru_education_edit(patientDTO.getEducation());
                    patienteducation.setText(education);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("gu")) {
                    String education = switch_gu_education_edit(patientDTO.getEducation());
                    patienteducation.setText(education);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("bn")) {
                    String education = switch_bn_education_edit(patientDTO.getEducation());
                    patienteducation.setText(education);
                } else {
                    patienteducation.setText(patientDTO.getEducation());
                }
            }
        }

        // setting economic status
        if (patientDTO.getEconomic() != null) {
            if (patientDTO.getEconomic().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                patienteconomicstatus.setText("नहीं दिया गया");
            } else if (patientDTO.getEconomic().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                patienteconomicstatus.setText("ଦିଅ ଯାଇ ନାହିଁ");
            } else if (patientDTO.getEconomic().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("ta")) {
                patienteconomicstatus.setText("வழங்கப்படவில்லை");
            } else if (patientDTO.getEconomic().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("gu")) {
                patienteconomicstatus.setText("પૂરી પાડવામાં આવેલ નથી");
            } else if (patientDTO.getEconomic().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("te")) {
                patienteconomicstatus.setText("సమకూర్చబడలేదు");
            } else if (patientDTO.getEconomic().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
                patienteconomicstatus.setText("झाले नाही");
            } else if (patientDTO.getEconomic().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
                patienteconomicstatus.setText("প্ৰদান কৰা হোৱা নাই");
            } else if (patientDTO.getEconomic().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("ml")) {
                patienteconomicstatus.setText("നൽകിയിട്ടില്ല");
            } else if (patientDTO.getEconomic().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("kn")) {
                patienteconomicstatus.setText("ಒದಗಿಸಲಾಗಿಲ್ಲ");
            } else if (patientDTO.getEconomic().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("ru")) {
                patienteconomicstatus.setText("Не предоставлен");
            } else if (patientDTO.getEconomic().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("bn")) {
                patienteconomicstatus.setText("সরবরাহ করা হয়নি");
            } else {
                patienteconomicstatus.setText(patientDTO.getEconomic());
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    String economic = switch_hi_economic_edit(patientDTO.getEconomic());
                    patienteconomicstatus.setText(economic);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                    String economic = switch_or_economic_edit(patientDTO.getEconomic());
                    patienteconomicstatus.setText(economic);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ta")) {
                    String economic = switch_ta_economic_edit(patientDTO.getEconomic());
                    patienteconomicstatus.setText(economic);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("bn")) {
                    String economic = switch_bn_economic_edit(patientDTO.getEconomic());
                    patienteconomicstatus.setText(economic);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("gu")) {
                    String economic = switch_gu_economic_edit(patientDTO.getEconomic());
                    patienteconomicstatus.setText(economic);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("te")) {
                    String economic = switch_te_economic_edit(patientDTO.getEconomic());
                    patienteconomicstatus.setText(economic);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
                    String economic = switch_mr_economic_edit(patientDTO.getEconomic());
                    patienteconomicstatus.setText(economic);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
                    String economic = switch_as_economic_edit(patientDTO.getEconomic());
                    patienteconomicstatus.setText(economic);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ml")) {
                    String economic = switch_ml_economic_edit(patientDTO.getEconomic());
                    patienteconomicstatus.setText(economic);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("kn")) {
                    String economic = switch_kn_economic_edit(patientDTO.getEconomic());
                    patienteconomicstatus.setText(economic);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ru")) {
                    String economic = switch_ru_economic_edit(patientDTO.getEconomic());
                    patienteconomicstatus.setText(economic);
                } else {
                    patienteconomicstatus.setText(patientDTO.getEconomic());
                }
            }
        }

        // setting caste value
        if (patientDTO.getCaste() != null) {
            if (patientDTO.getCaste().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                patientcaste.setText("नहीं दिया गया");
            } else if (patientDTO.getCaste().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                patientcaste.setText("ଦିଅ ଯାଇ ନାହିଁ");
            } else if (patientDTO.getCaste().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("te")) {
                patientcaste.setText("సమకూర్చబడలేదు");
            } else if (patientDTO.getCaste().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
                patientcaste.setText("झाले नाही");
            } else if (patientDTO.getCaste().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
                patientcaste.setText("প্ৰদান কৰা হোৱা নাই");
            } else if (patientDTO.getCaste().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("ml")) {
                patientcaste.setText("നൽകിയിട്ടില്ല");
            } else if (patientDTO.getCaste().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("kn")) {
                patientcaste.setText("ಒದಗಿಸಲಾಗಿಲ್ಲ");
            } else if (patientDTO.getCaste().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("ru")) {
                patientcaste.setText("Не предоставлен");
            } else if (patientDTO.getCaste().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("gu")) {
                patientcaste.setText("પૂરી પાડવામાં આવેલ નથી");
            } else if (patientDTO.getCaste().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("bn")) {
                patientcaste.setText("সরবরাহ করা হয়নি");
            } else if (patientDTO.getCaste().equalsIgnoreCase("Not provided") &&
                    sessionManager.getAppLanguage().equalsIgnoreCase("ta")) {
                patientcaste.setText("வழங்கப்படவில்லை");
            } else {
                patientcaste.setText(patientDTO.getCaste());
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                    String caste = switch_hi_caste_edit(patientDTO.getCaste());
                    patientcaste.setText(caste);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                    String caste = switch_or_caste_edit(patientDTO.getCaste());
                    patientcaste.setText(caste);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("gu")) {
                    String caste = switch_gu_caste_edit(patientDTO.getCaste());
                    patientcaste.setText(caste);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("te")) {
                    String caste = switch_te_caste_edit(patientDTO.getCaste());
                    patientcaste.setText(caste);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
                    String caste = switch_mr_caste_edit(patientDTO.getCaste());
                    patientcaste.setText(caste);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
                    String caste = switch_as_caste_edit(patientDTO.getCaste());
                    patientcaste.setText(caste);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ml")) {
                    String caste = switch_ml_caste_edit(patientDTO.getCaste());
                    patientcaste.setText(caste);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("kn")) {
                    String caste = switch_kn_caste_edit(patientDTO.getCaste());
                    patientcaste.setText(caste);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ru")) {
                    String caste = switch_ru_caste_edit(patientDTO.getCaste());
                    patientcaste.setText(caste);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("bn")) {
                    String caste = switch_bn_caste_edit(patientDTO.getCaste());
                    patientcaste.setText(caste);
                } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ta")) {
                    String caste = switch_ta_caste_edit(patientDTO.getCaste());
                    patientcaste.setText(caste);
                } else {
                    patientcaste.setText(patientDTO.getCaste());
                }
            }
        }

        // setting son/daughet_wife value
        if (patientDTO.getSon_dau_wife() != null && !patientDTO.getSon_dau_wife().equals("")) {
            son_daughter_wife.setText(patientDTO.getSon_dau_wife());
        } else {
            son_daughter_wife.setVisibility(View.GONE);
        }

        // setting national ID value
        if (patientDTO.getNationalID() != null && !patientDTO.getNationalID().equals("")) {
            patientNationalID.setText(patientDTO.getNationalID());
        } else {
            patientNationalID.setText(getResources().getString(R.string.not_provided));
        }

        // setting abha number value
        if (patientDTO.getAbhaNumber() != null && !patientDTO.getAbhaNumber().equals("")) {
            patientAbhaNumber.setText(patientDTO.getAbhaNumber());
        } else {
            patientAbhaNumber.setText(getResources().getString(R.string.not_provided));
        }

        // setting abha address value
        if (patientDTO.getAbhaAddress() != null && !patientDTO.getAbhaAddress().equals("")) {
            patientAbhaAddress.setText(patientDTO.getAbhaAddress());
        } else {
            patientAbhaAddress.setText(getResources().getString(R.string.not_provided));
        }

        // setting occupation value
        if (patientDTO.getOccupation() != null && !patientDTO.getOccupation().equals("")) {
            patientoccupation.setText(patientDTO.getOccupation());
        } else {
            patientoccupation.setText(getString(R.string.not_provided));
        }
    }

    private String getStateTranslated(String state, String language) {
        String desiredVal = state;
        JSONObject jsonObject = FileUtils.encodeJSON(PatientDetailActivity2.this, "state_district_tehsil.json");
        if (jsonObject != null) {
            StateDistMaster mStateDistMaster = new Gson().fromJson(jsonObject.toString(), StateDistMaster.
                    class);
            for (int i = 0; i < mStateDistMaster.getStateDataList().size(); i++) {
                String sName = mStateDistMaster.getStateDataList().get(i).getState();
                if (sName.equalsIgnoreCase(state)) {
                    if (language.equalsIgnoreCase("hi"))
                        desiredVal = mStateDistMaster.getStateDataList().get(i).getStateHindi();
                    else if (language.equalsIgnoreCase("en"))
                        desiredVal = mStateDistMaster.getStateDataList().get(i).getState();
                    break;
                }
            }
        }

        return desiredVal;
    }

    private String getDistrictTranslated(String state, String district, String language) {
        StateDistMaster mStateDistMaster = new Gson().fromJson(FileUtils.encodeJSON(PatientDetailActivity2.this, "state_district_tehsil.json").toString(), StateDistMaster.class);
        List<DistData> distDataList = new ArrayList<>();
        String desiredVal = district;

        for (int i = 0; i < mStateDistMaster.getStateDataList().size(); i++) {
            String sName = mStateDistMaster.getStateDataList().get(i).getState();
            if (sName.equalsIgnoreCase(state)) {
                distDataList = mStateDistMaster.getStateDataList().get(i).getDistDataList();
                break;
            }
        }

        for (int i = 0; i <= distDataList.size(); i++) {
            if (distDataList.get(i).getName().equalsIgnoreCase(district)) {
                if (language.equalsIgnoreCase("hi"))
                    desiredVal = distDataList.get(i).getNameHindi();
                else if (language.equalsIgnoreCase("en"))
                    desiredVal = distDataList.get(i).getName();
                break;
            }
        }

        return desiredVal;
    }

    // profile pic download
    public void profilePicDownloaded() {
        UrlModifiers urlModifiers = new UrlModifiers();
        String url = urlModifiers.patientProfileImageUrl(patientDTO.getUuid());
        Logger.logD(TAG, "profileimage url" + url);
        Observable<ResponseBody> profilePicDownload = AppConstants.apiInterface.PERSON_PROFILE_PIC_DOWNLOAD(url, "Basic " + sessionManager.getEncoded());
        profilePicDownload.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody file) {
                        DownloadFilesUtils downloadFilesUtils = new DownloadFilesUtils();
                        downloadFilesUtils.saveToDisk(file, patientDTO.getUuid());
                        Logger.logD(TAG, file.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.logD(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Logger.logD(TAG, "complete" + patientDTO.getPatientPhoto());
                        PatientsDAO patientsDAO = new PatientsDAO();
                        boolean updated = false;
                        try {
                            updated = patientsDAO.updatePatientPhoto(patientDTO.getUuid(), AppConstants.IMAGE_PATH + patientDTO.getUuid() + ".jpg");
                        } catch (DAOException e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                        if (updated) {
                            Glide.with(PatientDetailActivity2.this)
                                    .load(AppConstants.IMAGE_PATH + patientDTO.getUuid() + ".jpg")
                                    .thumbnail(0.3f)
                                    .centerCrop()
                                    .error(R.drawable.avatar1)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(profile_image);
                        }
                        ImagesDAO imagesDAO = new ImagesDAO();
                        boolean isImageDownloaded = false;
                        try {
                            isImageDownloaded = imagesDAO.insertPatientProfileImages(AppConstants.IMAGE_PATH +
                                    patientDTO.getUuid() + ".jpg", patientDTO.getUuid());
                        } catch (DAOException e) {
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                });
    }

    public void backPress(View view) {
        Intent intent = new Intent(this, SearchPatientActivity_New.class);
        startActivity(intent);
        finish();
    }

    public void syncNow(View view) {
        if (NetworkConnection.isOnline(this)) {
            refresh.clearAnimation();
            syncAnimator.start();
            new SyncUtils().syncBackground();
            //Toast.makeText(this, getString(R.string.sync_strated), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        registerReceiver(reMyreceive, filter);
//        ContextCompat.registerReceiver(this, reMyreceive, filter, ContextCompat.RECEIVER_NOT_EXPORTED);
        //register receiver for internet check
        networkUtils.callBroadcastReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
//        unregisterReceiver(reMyreceive);
        try {
            //unregister receiver for internet check
            networkUtils.unregisterNetworkReceiver();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateUIForInternetAvailability(boolean isInternetAvailable) {
        Log.d("TAG", "updateUIForInternetAvailability: ");
        if (isInternetAvailable) {
            refresh.setImageDrawable(getResources().getDrawable(R.drawable.ui2_ic_internet_available));
        } else {
            refresh.setImageDrawable(getResources().getDrawable(R.drawable.ui2_ic_no_internet));
        }
    }

    private void initForPastVisit() {
        mPastVisitDataList.clear();
        SQLiteDatabase db = IntelehealthApplication.inteleHealthDatabaseHelper.getWritableDatabase();
        String visitSelection = "patientuuid = ? and enddate IS NOT NULL and enddate != ''";
        String[] visitArgs = {patientDTO.getUuid()};
        String[] visitColumns = {"uuid", "startdate", "enddate"};
        String visitOrderBy = "startdate";
        Cursor visitCursor = db.query("tbl_visit", visitColumns, visitSelection, visitArgs, null, null, visitOrderBy);
        if (visitCursor == null || visitCursor.getCount() <= 0) {
            findViewById(R.id.cv_past_visits).setVisibility(View.GONE);
        } else {
            findViewById(R.id.cv_past_visits).setVisibility(View.VISIBLE);
            if (visitCursor.moveToLast()) {
                do {
                    EncounterDAO encounterDAO = new EncounterDAO();
                    String date = visitCursor.getString(visitCursor.getColumnIndexOrThrow("startdate"));
                    String end_date = visitCursor.getString(visitCursor.getColumnIndexOrThrow("enddate"));
                    String visit_id = visitCursor.getString(visitCursor.getColumnIndexOrThrow("uuid"));

                    boolean isCompletedExitedSurvey = false;
                    try {
                        isCompletedExitedSurvey = new EncounterDAO().isCompletedExitedSurvey(visit_id);
                    } catch (DAOException e) {
                        e.printStackTrace();
                    }
                    if (isCompletedExitedSurvey) {

                        String encounterlocalAdultintial = "";
                        String encountervitalsLocal = null;
                        String encounterIDSelection = "visituuid = ?";

                        String[] encounterIDArgs = {visit_id};

                        Cursor encounterCursor = db.query("tbl_encounter", null, encounterIDSelection, encounterIDArgs, null, null, null);
                        if (encounterCursor != null && encounterCursor.moveToFirst()) {
                            do {
                                if (encounterDAO.getEncounterTypeUuid("ENCOUNTER_VITALS").equalsIgnoreCase(encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("encounter_type_uuid")))) {
                                    encountervitalsLocal = encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("uuid"));
                                }
                                if (encounterDAO.getEncounterTypeUuid("ENCOUNTER_ADULTINITIAL").equalsIgnoreCase(encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("encounter_type_uuid")))) {
                                    encounterlocalAdultintial = encounterCursor.getString(encounterCursor.getColumnIndexOrThrow("uuid"));
                                }

                            } while (encounterCursor.moveToNext());
                        }
                        encounterCursor.close();

                        String previsitSelection = "encounteruuid = ? AND conceptuuid = ? and voided !='1'";
                        String[] previsitArgs = {encounterlocalAdultintial, UuidDictionary.CURRENT_COMPLAINT};
                        String[] previsitColumms = {"value", " conceptuuid", "encounteruuid"};
                        Cursor previsitCursor = db.query("tbl_obs", previsitColumms, previsitSelection, previsitArgs, null, null, null);
                        if (previsitCursor != null && previsitCursor.moveToLast()) {

                            String visitValue = previsitCursor.getString(previsitCursor.getColumnIndexOrThrow("value"));
                            boolean needToShowCoreValue = false;
                            if (visitValue.startsWith("{") && visitValue.endsWith("}")) {
                                try {
                                    // isInOldFormat = false;
                                    JSONObject jsonObject = new JSONObject(visitValue);
                                    if (jsonObject.has("l-" + sessionManager.getAppLanguage())) {
                                        visitValue = jsonObject.getString("l-" + sessionManager.getAppLanguage());
                                        needToShowCoreValue = false;
                                    } else {
                                        needToShowCoreValue = true;
                                        visitValue = jsonObject.getString("en");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                needToShowCoreValue = true;
                            }
                            if (visitValue != null && !visitValue.isEmpty()) {

                                if (needToShowCoreValue) {

                                    visitValue = visitValue.replace("?<b>", Node.bullet_arrow);

                                    String[] complaints = org.apache.commons.lang3.StringUtils.split(visitValue, Node.bullet_arrow);

                                    visitValue = "";
                                    String colon = ":";
                                    if (complaints != null) {
                                        for (String comp : complaints) {
                                            if (!comp.trim().isEmpty()) {
                                                visitValue = visitValue + Node.bullet_arrow + comp.substring(0, comp.indexOf(colon)) + "<br/>";

                                            }
                                        }
                                        if (!visitValue.isEmpty()) {
                                            visitValue = visitValue.replaceAll(Node.bullet_arrow, "");
                                            visitValue = visitValue.replaceAll("<br/>", ", ");
                                            visitValue = visitValue.replaceAll(Node.ASSOCIATE_SYMPTOMS, "");
                                            //visitValue = visitValue.substring(0, visitValue.length() - 2);
                                            visitValue = visitValue.replaceAll("<b>", "");
                                            visitValue = visitValue.replaceAll("</b>", "");
                                            visitValue = visitValue.trim();
                                            while (visitValue.endsWith(",")) {
                                                visitValue = visitValue.substring(0, visitValue.length() - 1).trim();
                                            }
                                        }
                                    }
                                } else {
                                    String chiefComplain = "";
                                    visitValue = visitValue.replaceAll("<.*?>", "");
                                    System.out.println(visitValue);
                                    Log.v(TAG, visitValue);
                                    //►दस्त::● आपको ये लक्षण कब से है• 6 घंटे● दस्त शुरू कैसे हुए?•धीरे धीरे● २४ घंटे में कितनी बार दस्त हुए?•३ से कम बार● दस्त किस प्रकार के है?•पक्का● क्या आपको पिछले महीनो में दस्त शुरू होने से पहले किसी असामान्य भोजन/तरल पदार्थ से अपच महसूस हुआ है•नहीं● क्या आपने आज यहां आने से पहले इस समस्या के लिए कोई उपचार (स्व-दवा या घरेलू उपचार सहित) लिया है या किसी स्वास्थ्य प्रदाता को दिखाया है?•कोई नहीं● अतिरिक्त जानकारी•bsbdbd►क्या आपको निम्न लक्षण है::•उल्टीPatient denies -•दस्त के साथ पेट दर्द•सुजन•मल में खून•बुखार•अन्य [वर्णन करे]

                                    String[] spt = visitValue.split("►");
                                    List<String> list = new ArrayList<>();

                                    StringBuilder stringBuilder = new StringBuilder();
                                    for (String s : spt) {
                                        String complainName = "";
                                        if (s.isEmpty()) continue;
                                        //String s1 =  new String(s.getBytes(), "UTF-8");
                                        System.out.println(s);
                                        String[] spt1 = s.split("::●");
                                        complainName = spt1[0];

                                        //if (s.trim().startsWith(getTranslatedAssociatedSymptomQString(lCode))) {
                                        if (!complainName.trim().contains(VisitUtils.getTranslatedPatientDenies(sessionManager.getAppLanguage()))) {
                                            System.out.println(complainName);
                                            if (!stringBuilder.toString().isEmpty())
                                                stringBuilder.append(", ");
                                            stringBuilder.append(complainName);
                                        }

                                    }
                                /*StringBuilder stringBuilder = new StringBuilder();
                                int size = list.size() == 1 ? list.size() : list.size() - 1;
                                for (int i = 0; i < size; i++) {
                                    String complainName = "";
                                    List<VisitSummaryData> visitSummaryDataList = new ArrayList<>();
                                    String[] spt1 = list.get(i).split("●");
                                    for (String value : spt1) {
                                        if (value.contains("::")) {
                                            if (!stringBuilder.toString().isEmpty())
                                                stringBuilder.append(",");
                                            complainName = value.replace("::", "");
                                            System.out.println(complainName);
                                            stringBuilder.append(complainName);
                                        }
                                    }*/
                                    visitValue = stringBuilder.toString();

                                }
                                SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                try {

                                    Date formatted = currentDate.parse(date);
                                    String visitDate = currentDate.format(formatted);
                                    //createOldVisit(visitDate, visit_id, end_date, visitValue, encountervitalsLocal, encounterlocalAdultintial);
                                    PastVisitData pastVisitData = new PastVisitData();
                                    pastVisitData.setVisitDate(visitDate);
                                    pastVisitData.setVisitUUID(visit_id);
                                    pastVisitData.setChiefComplain(visitValue);
                                    pastVisitData.setEncounterVitals(encountervitalsLocal);
                                    pastVisitData.setEncounterAdultInitial(encounterlocalAdultintial);
                                    mPastVisitDataList.add(pastVisitData);
                                    Log.v(TAG, new Gson().toJson(mPastVisitDataList));

                                } catch (ParseException e) {
                                    FirebaseCrashlytics.getInstance().recordException(e);
                                }
                            }
                        }


                    }
                } while (visitCursor.moveToPrevious());
            }

            if (!mPastVisitDataList.isEmpty()) {
                findViewById(R.id.cv_past_visits).setVisibility(View.VISIBLE);
                PastVisitListingAdapter pastVisitListingAdapter = new PastVisitListingAdapter(mPastVisitsRecyclerView, PatientDetailActivity2.this, mPastVisitDataList, new PastVisitListingAdapter.OnItemSelected() {
                    @Override
                    public void onItemSelected(PastVisitData pastVisitData) {
                        intentForVisitDetails(pastVisitData);
                    }
                });
                mPastVisitsRecyclerView.setAdapter(pastVisitListingAdapter);
            } else {
                findViewById(R.id.cv_past_visits).setVisibility(View.GONE);
            }
        }
    }


}