package org.intelehealth.app.activities.identificationActivity;

import static android.app.Activity.RESULT_OK;
import static org.intelehealth.app.activities.identificationActivity.IdentificationActivity_New.MOBILE_PAYLOAD;
import static org.intelehealth.app.activities.identificationActivity.IdentificationActivity_New.PAYLOAD;
import static org.intelehealth.app.utilities.StringUtils.en_hi_dob_updated;
import static org.intelehealth.app.utilities.StringUtils.inputFilter_Others;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.ajalt.timberkt.Timber;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.hbb20.CountryCodePicker;

import org.intelehealth.app.R;
import org.intelehealth.app.abdm.model.AbhaProfileResponse;
import org.intelehealth.app.abdm.model.OTPVerificationResponse;
import org.intelehealth.app.activities.patientDetailActivity.PatientDetailActivity2;
import org.intelehealth.app.app.AppConstants;
import org.intelehealth.app.app.IntelehealthApplication;
import org.intelehealth.app.database.dao.ImagesDAO;
import org.intelehealth.app.database.dao.ImagesPushDAO;
import org.intelehealth.app.database.dao.PatientsDAO;
import org.intelehealth.app.database.dao.SyncDAO;
import org.intelehealth.app.models.dto.PatientAttributesDTO;
import org.intelehealth.app.models.dto.PatientDTO;
import org.intelehealth.app.ui2.calendarviewcustom.CustomCalendarViewUI2;
import org.intelehealth.app.ui2.calendarviewcustom.SendSelectedDateInterface;
import org.intelehealth.app.utilities.CameraUtils;
import org.intelehealth.app.utilities.DateAndTimeUtils;
import org.intelehealth.app.utilities.EditTextUtils;
import org.intelehealth.app.utilities.IReturnValues;
import org.intelehealth.app.utilities.Logger;
import org.intelehealth.app.utilities.NetworkConnection;
import org.intelehealth.app.utilities.SessionManager;
import org.intelehealth.app.utilities.StringUtils;
import org.intelehealth.app.utilities.exception.DAOException;
import org.intelehealth.ihutils.ui.CameraActivity;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Fragment_FirstScreen extends Fragment implements SendSelectedDateInterface {
    private View view;
    private String patientUuid = "";
    private ImageView patient_imgview;
    private Button frag1_nxt_btn_main;
    SessionManager sessionManager = null;
    private ImageView personal_icon, address_icon, other_icon;
    EditText mFirstNameEditText, mMiddleNameEditText, mLastNameEditText, mDOBEditText, mAgeEditText, mPhoneNumberEditText;
    private Fragment_SecondScreen fragment_secondScreen;
    PatientDTO patientdto = new PatientDTO();
    private String mGender;
    String uuid = "", dobToDb;
    private String mCurrentPhotoPath;
    RadioButton mGenderMaleRadioButton, mGenderFemaleRadioButton, mGenderOthersRadioButton;
    private static final String TAG = Fragment_FirstScreen.class.getSimpleName();
    private DatePickerDialog mDOBPicker;
    private MaterialAlertDialogBuilder mAgePicker;
    Calendar dob = Calendar.getInstance();
    Calendar today = Calendar.getInstance();
    private CountryCodePicker mCountryCodePicker;
    TextView mFirstNameErrorTextView, mMiddleNameErrorTextView, mLastNameErrorTextView, mGenderErrorTextView, mDOBErrorTextView, mAgeErrorTextView, mPhoneNumberErrorTextView;
    private int mDOBYear, mDOBMonth, mDOBDay, mAgeYears = 0, mAgeMonths = 0, mAgeDays = 0;
    int dob_indexValue = 15;
    //random value assigned to check while editing. If user didnt updated the dob and just clicked on fab
    //in that case, the edit() will get the dob_indexValue as 15 and we  will check if the
    //dob_indexValue == 15 then just get the dob_edittext editText value and add in the db.
    boolean fromSecondScreen = false;
    String patientID_edit;
    boolean patient_detail = false;
    private static final int GROUP_PERMISSION_REQUEST = 1000;
    private OTPVerificationResponse otpVerificationResponse;
    private AbhaProfileResponse abhaProfileResponse;
    private String accessToken, xToken, txnId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registration_firstscreen, container, false);
        setLocale(getContext());
        return view;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(getActivity());
        patient_imgview = view.findViewById(R.id.patient_imgview);
        frag1_nxt_btn_main = view.findViewById(R.id.frag1_nxt_btn_main);
        personal_icon = getActivity().findViewById(R.id.addpatient_icon);
        address_icon = getActivity().findViewById(R.id.addresslocation_icon);
        other_icon = getActivity().findViewById(R.id.other_icon);
        mFirstNameEditText = view.findViewById(R.id.firstname_edittext);
        mFirstNameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25), inputFilter_Others}); //maxlength 25  // IDA4-1344
        mMiddleNameEditText = view.findViewById(R.id.middlename_edittext);
        mMiddleNameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25), inputFilter_Others}); //maxlength 25  // IDA4-1344
        mLastNameEditText = view.findViewById(R.id.lastname_edittext);
        mLastNameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25), inputFilter_Others}); //maxlength 25  // IDA4-1344
        mGenderMaleRadioButton = view.findViewById(R.id.gender_male);
        mGenderFemaleRadioButton = view.findViewById(R.id.gender_female);
        mGenderOthersRadioButton = view.findViewById(R.id.gender_other);
        mDOBEditText = view.findViewById(R.id.dob_edittext);
        mAgeEditText = view.findViewById(R.id.age_edittext);
        mCountryCodePicker = view.findViewById(R.id.countrycode_spinner);
        mPhoneNumberEditText = view.findViewById(R.id.phoneno_edittext);
        Timber.tag("phone").v("phone value: %s", mCountryCodePicker.getSelectedCountryCode());
        mCountryCodePicker.registerCarrierNumberEditText(mPhoneNumberEditText); // attaches the ccp spinner with the edittext
        mCountryCodePicker.setNumberAutoFormattingEnabled(false);

        mFirstNameErrorTextView = view.findViewById(R.id.firstname_error);
        mMiddleNameErrorTextView = view.findViewById(R.id.middlename_error);
        mLastNameErrorTextView = view.findViewById(R.id.lastname_error);
        mGenderErrorTextView = view.findViewById(R.id.gender_error);
        mDOBErrorTextView = view.findViewById(R.id.dob_error);
        mAgeErrorTextView = view.findViewById(R.id.age_error);
        mPhoneNumberErrorTextView = view.findViewById(R.id.phone_error);

        mFirstNameEditText.addTextChangedListener(new MyTextWatcher(mFirstNameEditText));
        mMiddleNameEditText.addTextChangedListener(new MyTextWatcher(mMiddleNameEditText));
        mLastNameEditText.addTextChangedListener(new MyTextWatcher(mLastNameEditText));
        mDOBEditText.addTextChangedListener(new MyTextWatcher(mDOBEditText));
        mAgeEditText.addTextChangedListener(new MyTextWatcher(mAgeEditText));
        //mPhoneNumberEditText.addTextChangedListener(new MyTextWatcher(mPhoneNumberEditText));

        fragment_secondScreen = new Fragment_SecondScreen();
        if (getArguments() != null) {
            if (getArguments().containsKey("patientDTO"))
                patientdto = (PatientDTO) getArguments().getSerializable("patientDTO");
            Log.d(TAG, "firstscreen: " + patientdto.getAddress1());

            if (getArguments().containsKey("patient_detail"))
                patient_detail = getArguments().getBoolean("patient_detail");
            if (getArguments().containsKey("accessToken"))
                accessToken = getArguments().getString("accessToken");
            if (getArguments().containsKey("xToken"))
                xToken = getArguments().getString("xToken");
            if (getArguments().containsKey("txnId"))
                txnId = getArguments().getString("txnId");
            if (getArguments().containsKey("fromSecondScreen"))
                fromSecondScreen = getArguments().getBoolean("fromSecondScreen");

            if (getArguments().containsKey(PAYLOAD)) {
                otpVerificationResponse = (OTPVerificationResponse) getArguments().getSerializable(PAYLOAD);
                if (otpVerificationResponse != null) {
                    setAutoFillValuesViaAadhar(otpVerificationResponse);
                    patientdto.setOpenmrsId(otpVerificationResponse.getOpenMrsId());
                    patientdto.setUuid(otpVerificationResponse.getUuID());
                }
            } else if (getArguments().containsKey(MOBILE_PAYLOAD)) {
                abhaProfileResponse = (AbhaProfileResponse) getArguments().getSerializable(MOBILE_PAYLOAD);
                if (abhaProfileResponse != null) {
                    setAutoFillValuesViaMobile(abhaProfileResponse);
                    patientdto.setOpenmrsId(abhaProfileResponse.getOpenMrsId());
                    patientdto.setUuid(abhaProfileResponse.getUuiD());
//                    patientdto.getAbhaAddress();
//                    patientdto.getAddress1();

                }
            }
        }

       /* // todo: uncomment later - start
        AbhaProfileResponse abhaProfileResponse = new AbhaProfileResponse();
        abhaProfileResponse.setFirstName("Prajwal");
        abhaProfileResponse.setMiddleName("Maruti");
        abhaProfileResponse.setLastName("Waingankar");
        abhaProfileResponse.setGender("M");
        abhaProfileResponse.setDayOfBirth("01");
        abhaProfileResponse.setMonthOfBirth("07");
        abhaProfileResponse.setYearOfBirth("1998");
        abhaProfileResponse.setMobile("7304154312");

        setAutoFillValuesViaMobile(abhaProfileResponse);
        // todo: uncomment later - end*/


        if (patient_detail)
            frag1_nxt_btn_main.setText(getString(R.string.save));

        // Setting up the screen when user came from Second screen.
        if (fromSecondScreen) {
            mFirstNameEditText.setText(patientdto.getFirstname());
            mMiddleNameEditText.setText(patientdto.getMiddlename());
            mLastNameEditText.setText(patientdto.getLastname());
            dobToDb = patientdto.getDateofbirth();

            mDOBEditText.setText(DateAndTimeUtils.getDisplayDateForApp(dobToDb));

            if (sessionManager.getAppLanguage().equalsIgnoreCase("hi"))
                mDOBEditText.setText(en_hi_dob_updated(DateAndTimeUtils.getDisplayDateForApp(dobToDb)));

            // dob_edittext.setText(DateAndTimeUtils.getFormatedDateOfBirthAsView(patient1.getDate_of_birth()));
            //get year month days
            String yrMoDays = DateAndTimeUtils.getAgeInYearMonth(patientdto.getDateofbirth(), getActivity());
            String[] ymdData = DateAndTimeUtils.getAgeInYearMonth(patientdto.getDateofbirth()).split(" ");
            mAgeYears = Integer.parseInt(ymdData[0]);
            mAgeMonths = Integer.parseInt(ymdData[1]);
            mAgeDays = Integer.parseInt(ymdData[2]);
            String age = DateAndTimeUtils.formatAgeInYearsMonthsDate(getContext(), mAgeYears, mAgeMonths, mAgeDays);
            mAgeEditText.setText(age);

            mCountryCodePicker.setFullNumber(patientdto.getPhonenumber()); // automatically assigns cc to spinner and number to edittext field.
            //   phoneno_edittext.setText(patientdto.getPhonenumber());

            // Gender edit
            if (patientdto.getGender().equals("M")) {
                mGenderMaleRadioButton.setChecked(true);
                if (mGenderFemaleRadioButton.isChecked())
                    mGenderFemaleRadioButton.setChecked(false);
                if (mGenderOthersRadioButton.isChecked())
                    mGenderOthersRadioButton.setChecked(false);
                Log.v(TAG, "yes");
            } else if (patientdto.getGender().equals("F")) {
                mGenderFemaleRadioButton.setChecked(true);
                if (mGenderMaleRadioButton.isChecked())
                    mGenderMaleRadioButton.setChecked(false);
                if (mGenderOthersRadioButton.isChecked())
                    mGenderOthersRadioButton.setChecked(false);
                Log.v(TAG, "yes");
            } else {
                mGenderOthersRadioButton.setChecked(true);
                if (mGenderMaleRadioButton.isChecked())
                    mGenderMaleRadioButton.setChecked(false);
                if (mGenderFemaleRadioButton.isChecked())
                    mGenderFemaleRadioButton.setChecked(false);
            }

            if (mGenderMaleRadioButton.isChecked()) {
                mGender = "M";
            } else if (mGenderFemaleRadioButton.isChecked()) {
                mGender = "F";
            } else {
                mGender = "O";
            }

            // profile image edit
            if (patientdto.getPatientPhoto() != null && !patientdto.getPatientPhoto().trim().isEmpty()) {
                //  patient_imgview.setImageBitmap(BitmapFactory.decodeFile(patientdto.getPatientPhoto()));
                Glide.with(getActivity())
                        .load(new File(patientdto.getPatientPhoto()))
                        .thumbnail(0.25f)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(patient_imgview);

            }
        }

        setMobileNumberLimit();


    }

    private void setAutoFillValuesViaAadhar(OTPVerificationResponse otpVerificationResponse) {
        Log.d(TAG, "setAutoFillValuesViaAadhar: " + otpVerificationResponse.toString());

        String profileImage = otpVerificationResponse.getABHAProfile().getPhoto();
        if (profileImage != null) {
            displayAbhaProfilePhotoAndStoreInFile(profileImage);
        }

        mFirstNameEditText.setText(otpVerificationResponse.getABHAProfile().getFirstName());
        mMiddleNameEditText.setText(otpVerificationResponse.getABHAProfile().getMiddleName());
        mLastNameEditText.setText(otpVerificationResponse.getABHAProfile().getLastName());

        if (otpVerificationResponse.getABHAProfile().getGender().equalsIgnoreCase("M"))
            mGenderMaleRadioButton.setChecked(true);
        else if (otpVerificationResponse.getABHAProfile().getGender().equalsIgnoreCase("F"))
            mGenderFemaleRadioButton.setChecked(true);
        else
            mGenderOthersRadioButton.setChecked(true);

        // dob and age
        setAbhaDOBToEditText(otpVerificationResponse.getABHAProfile().getDob());    // aadhar -> 25-08-1978

        // mobile no.
        if (otpVerificationResponse.getABHAProfile().getMobile() != null)
            mPhoneNumberEditText.setText(otpVerificationResponse.getABHAProfile().getMobile());
    }

    private void setAutoFillValuesViaMobile(AbhaProfileResponse abhaProfileResponse) {
        Log.d(TAG, "setAutoFillValuesViaMobile: " + abhaProfileResponse.toString());

        String profileImage = abhaProfileResponse.getProfilePhoto();
        if (profileImage != null) {
            displayAbhaProfilePhotoAndStoreInFile(profileImage);
        }

        mFirstNameEditText.setText(abhaProfileResponse.getFirstName());
        mMiddleNameEditText.setText(abhaProfileResponse.getMiddleName());
        mLastNameEditText.setText(abhaProfileResponse.getLastName());

        if (abhaProfileResponse.getGender().equalsIgnoreCase("M"))
            mGenderMaleRadioButton.setChecked(true);
        else if (abhaProfileResponse.getGender().equalsIgnoreCase("F"))
            mGenderFemaleRadioButton.setChecked(true);
        else
            mGenderOthersRadioButton.setChecked(true);

        // dob and age
        setAbhaDOBToEditText(abhaProfileResponse.getDayOfBirth(), abhaProfileResponse.getMonthOfBirth(), abhaProfileResponse.getYearOfBirth());

        // mobile no.
        if (abhaProfileResponse.getMobile() != null)
            mPhoneNumberEditText.setText(abhaProfileResponse.getMobile());
    }

    private void displayAbhaProfilePhotoAndStoreInFile(String profileImage) {
        byte[] decodedString = Base64.decode(profileImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        String patientTemp = "";
        if (patientUuid.equalsIgnoreCase("")) {
            patientTemp = patientdto.getUuid();
        } else {
            patientTemp = patientUuid;
        }

        File filePath = new File(AppConstants.IMAGE_PATH + patientTemp);
        if (!filePath.exists()) {
            filePath.mkdir();
        }

        CameraUtils cameraUtils = new CameraUtils(getActivity(), patientTemp, filePath.toString());
        mCurrentPhotoPath = cameraUtils.compressImageAndSave(decodedByte);

        patient_imgview.setImageBitmap(decodedByte);
    }

    private void setAbhaAgeToEditText(String dob) {
        String[] ymdData = DateAndTimeUtils.getAgeInYearMonth(dob).split(" ");
        int mAgeYears = Integer.parseInt(ymdData[0]);
        int mAgeMonths = Integer.parseInt(ymdData[1]);
        int mAgeDays = Integer.parseInt(ymdData[2]);
        String age = DateAndTimeUtils.formatAgeInYearsMonthsDate(getContext(), mAgeYears, mAgeMonths, mAgeDays);
        mAgeEditText.setText(age);
    }

    private void setAbhaDOBToEditText(String day, String month, String year) {
        dobToDb = year + "-" + month + "-" + day;
        Log.d(TAG, "setAutoFillValuesViaMobile: " + dobToDb);   // 1998-07-01
        mDOBEditText.setText(DateAndTimeUtils.getDisplayDateForApp(dobToDb));

        // age
        setAbhaAgeToEditText(dobToDb);
    }

    private void setAbhaDOBToEditText(String dob) { // aadhar -> 01-07-1998
        String[] dateSplit = dob.split("-");
        String day = dateSplit[0];
        String month = dateSplit[1];
        String year = dateSplit[2];

        dob = year + "-" + month + "-" + day;
        dobToDb = dob;
        Log.d(TAG, "setAutoFillValuesViaAadhar: " + dobToDb);   // 1998-07-01
        mDOBEditText.setText(DateAndTimeUtils.getDisplayDateForApp(dobToDb));

        // age
        setAbhaAgeToEditText(dobToDb);
    }

    private int mSelectedMobileNumberValidationLength = 0;
    private String mSelectedCountryCode = "";

    private void setMobileNumberLimit() {
        mSelectedCountryCode = mCountryCodePicker.getSelectedCountryCode();
        if (mSelectedCountryCode.equals("91")) {
            mSelectedMobileNumberValidationLength = 10;
        }
        mPhoneNumberEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                return null;
            }
        };

        mPhoneNumberEditText.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(mSelectedMobileNumberValidationLength)});
        // hide the validation fields ...
        mPhoneNumberErrorTextView.setVisibility(View.GONE);
        mPhoneNumberEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
    }

    @Override
    public void getSelectedDate(String selectedDate, String whichDate) {
        Log.d(TAG, "getSelectedDate: selectedDate from interface : " + selectedDate);
        if (selectedDate != null) {
            try {
                Date sourceDate = new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate);
                Date nowDate = new Date();
                if (sourceDate.after(nowDate)) {
                    mAgeEditText.setText("");
                    mDOBEditText.setText("");
                    Toast.makeText(getActivity(), getString(R.string.valid_dob_msg), Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String dateToshow1 = DateAndTimeUtils.getDateWithDayAndMonthFromDDMMFormat(selectedDate);
        if (!selectedDate.isEmpty()) {
            dobToDb = DateAndTimeUtils.convertDateToYyyyMMddFormat(selectedDate);
//            String age = DateAndTimeUtils.getAge_FollowUp(DateAndTimeUtils.convertDateToYyyyMMddFormat(selectedDate), getActivity());
            //for age
            String[] ymdData = DateAndTimeUtils.getAgeInYearMonth(dobToDb).split(" ");
            mAgeYears = Integer.parseInt(ymdData[0]);
            mAgeMonths = Integer.parseInt(ymdData[1]);
            mAgeDays = Integer.parseInt(ymdData[2]);

            String age = DateAndTimeUtils.formatAgeInYearsMonthsDate(getContext(), mAgeYears, mAgeMonths, mAgeDays);
            String[] splitedDate = selectedDate.split("/");
            if (age != null && !age.isEmpty()) {
                mAgeEditText.setText(age);
                mDOBEditText.setText(dateToshow1 + ", " + splitedDate[2]);
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi"))
                    mDOBEditText.setText(en_hi_dob_updated(dateToshow1) + ", " + splitedDate[2]);
                Log.d(TAG, "getSelectedDate: " + dateToshow1 + ", " + splitedDate[2]);
            } else {
                mAgeEditText.setText("");
                mDOBEditText.setText("");
            }
        } else {
            Log.d(TAG, "onClick: date empty");
        }
    }

    class MyTextWatcher implements TextWatcher {
        EditText editText;

        MyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String val = editable.toString().trim();
            if (this.editText.getId() == R.id.firstname_edittext) {
                if (val.isEmpty()) {
                    mFirstNameErrorTextView.setVisibility(View.VISIBLE);
                    mFirstNameErrorTextView.setText(getString(R.string.error_field_required));
                    mFirstNameEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
                } else {
                    mFirstNameErrorTextView.setVisibility(View.GONE);
                    mFirstNameEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
                }
            } else if (this.editText.getId() == R.id.middlename_edittext) {
               /* if (val.isEmpty()) {
                    mMiddleNameErrorTextView.setVisibility(View.VISIBLE);
                    mMiddleNameErrorTextView.setText(getString(R.string.error_field_required));
                    mMiddleNameEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
                } else {
                    mMiddleNameErrorTextView.setVisibility(View.GONE);
                    mMiddleNameEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
                }*/
            } else if (this.editText.getId() == R.id.lastname_edittext) {
                if (val.isEmpty()) {
                    mLastNameErrorTextView.setVisibility(View.VISIBLE);
                    mLastNameErrorTextView.setText(getString(R.string.error_field_required));
                    mLastNameEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
                } else {
                    mLastNameErrorTextView.setVisibility(View.GONE);
                    mLastNameEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
                }
            } else if (this.editText.getId() == R.id.dob_edittext) {
                if (val.isEmpty()) {
                    mDOBErrorTextView.setVisibility(View.VISIBLE);
                    mDOBErrorTextView.setText(getString(R.string.error_field_required));
                    mDOBEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
                } else {
                    mDOBErrorTextView.setVisibility(View.GONE);
                    mDOBEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
                }
            } else if (this.editText.getId() == R.id.age_edittext) {
                if (val.isEmpty()) {
                    mAgeErrorTextView.setVisibility(View.VISIBLE);
                    mAgeErrorTextView.setText(getString(R.string.error_field_required));
                    mAgeEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
                } else {
                    mAgeErrorTextView.setVisibility(View.GONE);
                    mAgeEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
                }
            } else if (this.editText.getId() == R.id.phoneno_edittext) {
                if (val.isEmpty()) {
                    mPhoneNumberErrorTextView.setVisibility(View.VISIBLE);
                    mPhoneNumberErrorTextView.setText(getString(R.string.error_field_required));
                    mPhoneNumberEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
                } else {
                    mPhoneNumberErrorTextView.setVisibility(View.GONE);
                    mPhoneNumberEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        personal_icon.setImageDrawable(getResources().getDrawable(R.drawable.addpatient_icon));
        address_icon.setImageDrawable(getResources().getDrawable(R.drawable.addresslocation_icon_unselected));
        other_icon.setImageDrawable(getResources().getDrawable(R.drawable.other_icon_unselected));


        // next btn click
        frag1_nxt_btn_main.setOnClickListener(v -> {
            onPatientCreateClicked();
        });


        // Gender - start
        if (mGenderMaleRadioButton.isChecked()) {
            mGender = "M";
        } else if (mGenderFemaleRadioButton.isChecked()) {
            mGender = "F";
        } else {
            mGender = "O";
        }

        // setting patient profile
        patient_imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkPerm();
            }
        });


        mGenderFemaleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });

        mGenderMaleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });
        mGenderOthersRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(v);
            }
        });

        mDOBEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mDOBPicker.show();
//                CustomCalendarViewUI2 customCalendarViewUI2 = new CustomCalendarViewUI2(getActivity(), Fragment_FirstScreen.this);
//                customCalendarViewUI2.showDatePicker(getActivity(), null);
            }
        });
        // DOB - end

        // Age - start
        mAgeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openAgeSelectionDialog();
//                IntelehealthApplication.setAlertDialogCustomTheme(getActivity(), alertDialog);
            }
        });

        // Age - end
    }

    private void openAgeSelectionDialog() {
        mAgePicker = new MaterialAlertDialogBuilder(getActivity(), R.style.AlertDialogStyle);
        mAgePicker.setTitle(R.string.identification_screen_prompt_age);

        final LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.dialog_2_numbers_picker, null);
        mAgePicker.setView(convertView);
        NumberPicker yearPicker = convertView.findViewById(R.id.dialog_2_numbers_quantity);
        NumberPicker monthPicker = convertView.findViewById(R.id.dialog_2_numbers_unit);
        NumberPicker dayPicker = convertView.findViewById(R.id.dialog_3_numbers_unit);
        dayPicker.setVisibility(View.VISIBLE);
        Button okButton = convertView.findViewById(R.id.button_ok_picker);
        Button cancelButton = convertView.findViewById(R.id.btn_cancel_picker);
        final TextView middleText = convertView.findViewById(R.id.dialog_2_numbers_text);
        final TextView endText = convertView.findViewById(R.id.dialog_2_numbers_text_2);
        final TextView dayTv = convertView.findViewById(R.id.dialog_2_numbers_text_3);
        dayPicker.setVisibility(View.VISIBLE);

        dayTv.setText(getString(R.string.days));
        middleText.setText(getString(R.string.identification_screen_picker_years));
        endText.setText(getString(R.string.identification_screen_picker_months));

        yearPicker.setMinValue(0);
        yearPicker.setMaxValue(100);
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(12);

        dayPicker.setMinValue(0);
        dayPicker.setMaxValue(31);

        EditText yearText = yearPicker.findViewById(Resources.getSystem().getIdentifier("numberpicker_input", "id", "android"));
        EditText monthText = monthPicker.findViewById(Resources.getSystem().getIdentifier("numberpicker_input", "id", "android"));
        EditText dayText = dayPicker.findViewById(Resources.getSystem().getIdentifier("numberpicker_input", "id", "android"));

        yearPicker.setValue(mAgeYears);
        monthPicker.setValue(mAgeMonths);
        dayPicker.setValue(mAgeDays);

        AlertDialog alertDialog = mAgePicker.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.ui2_rounded_corners_dialog_bg);
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        int width = getContext().getResources().getDimensionPixelSize(R.dimen.internet_dialog_width);
        alertDialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        //year
        EditTextUtils.returnEditextValues(new IReturnValues() {
            @Override
            public void onReturnValue(String value) {
                mAgeYears = Integer.valueOf(value);
            }
        }, yearText);

        //month
        EditTextUtils.returnEditextValues(new IReturnValues() {
            @Override
            public void onReturnValue(String value) {
                mAgeMonths = Integer.valueOf(value);
            }
        }, monthText);

        //day
        EditTextUtils.returnEditextValues(new IReturnValues() {
            @Override
            public void onReturnValue(String value) {
                mAgeDays = Integer.valueOf(value);
            }
        }, dayText);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ageString = DateAndTimeUtils.formatAgeInYearsMonthsDate(getContext(), mAgeYears, mAgeMonths, mAgeDays);
                mAgeEditText.setText(ageString);

                mDOBErrorTextView.setVisibility(View.GONE);
                mDOBEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);

                mAgeErrorTextView.setVisibility(View.GONE);
                mAgeEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -mAgeDays);
                calendar.add(Calendar.MONTH, -mAgeMonths);
                calendar.add(Calendar.YEAR, -mAgeYears);

                mDOBYear = calendar.get(Calendar.YEAR);
                mDOBMonth = calendar.get(Calendar.MONTH);
                mDOBDay = calendar.get(Calendar.DAY_OF_MONTH);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",
                        Locale.ENGLISH);
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy",
                        Locale.ENGLISH);
                dob.set(mDOBYear, mDOBMonth, mDOBDay);
                String dobString = simpleDateFormat.format(dob.getTime());
                dobToDb = DateAndTimeUtils.convertDateToYyyyMMddFormat(simpleDateFormat1.format(dob.getTime()));
                mDOBEditText.setText(DateAndTimeUtils.getDisplayDateForApp(dobString));
                if (sessionManager.getAppLanguage().equalsIgnoreCase("hi"))
                    mDOBEditText.setText(en_hi_dob_updated(DateAndTimeUtils.getDisplayDateForApp(dobString)));
                alertDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void checkPerm() {
        if (checkAndRequestPermissions()) {
            takePicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GROUP_PERMISSION_REQUEST) {
            boolean allGranted = grantResults.length != 0;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                checkPerm();
            } else {
                showPermissionDeniedAlert(permissions);
            }

        }
    }

    private void showPermissionDeniedAlert(String[] permissions) {
        MaterialAlertDialogBuilder alertdialogBuilder = new MaterialAlertDialogBuilder(getActivity());

        // AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        alertdialogBuilder.setMessage(R.string.reject_permission_results);
        alertdialogBuilder.setPositiveButton(R.string.retry_again, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkPerm();
            }
        });
        alertdialogBuilder.setNegativeButton(R.string.ok_close_now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        });

        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);

        positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        //positiveButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        negativeButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        //negativeButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        IntelehealthApplication.setAlertDialogCustomTheme(getActivity(), alertDialog);
    }

    private boolean checkAndRequestPermissions() {
        int cameraPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), GROUP_PERMISSION_REQUEST);
            return false;
        }
        return true;
    }

    private void takePicture() {
        String patientTemp = "";
        if (patientUuid.equalsIgnoreCase("")) {
            patientTemp = patientdto.getUuid();
        } else {
            patientTemp = patientUuid;
        }

        File filePath = new File(AppConstants.IMAGE_PATH + patientTemp);
        if (!filePath.exists()) {
            filePath.mkdir();
        }

        Intent cameraIntent = new Intent(getActivity(), CameraActivity.class);
        cameraIntent.putExtra(CameraActivity.SET_IMAGE_NAME, patientTemp);
        cameraIntent.putExtra(CameraActivity.SET_IMAGE_PATH, filePath.toString());
        startActivityForResult(cameraIntent, CameraActivity.TAKE_IMAGE);
    }

    // gender
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.gender_male:
                if (checked)
                    mGender = "M";
                Log.v(TAG, "gender:" + mGender);
                break;
            case R.id.gender_female:
                if (checked)
                    mGender = "F";
                Log.v(TAG, "gender:" + mGender);
                break;
            case R.id.gender_other:
                if (checked)
                    mGender = "O";
                Log.v(TAG, "gender: " + mGender);
                break;
        }
        mGenderErrorTextView.setVisibility(View.GONE);
    }

    // DOB
    public String getYear(int syear, int smonth, int sday, int eyear, int emonth, int eday) {
        LocalDate birthdate = new LocalDate(syear, smonth + 1, sday);
        LocalDate now = new LocalDate();
        Period p = new Period(birthdate, now, PeriodType.yearMonthDay());
        return p.getYears() + " years  - " + p.getMonths() + " month - " + p.getDays() + " days";
    }


    private void onPatientCreateClicked() {
        uuid = UUID.randomUUID().toString();
        Log.v(TAG, "reltion: " + patientID_edit + ", " + patientdto.toString());
        if (!patient_detail && TextUtils.isEmpty(patientdto.getUuid())) {
            patientdto.setUuid(uuid);
        }else {
            patientdto.setIsExist(true);
        }

        if (mFirstNameEditText.getText().toString().equals("")) {
            mFirstNameErrorTextView.setVisibility(View.VISIBLE);
            mFirstNameErrorTextView.setText(getString(R.string.error_field_required));
            mFirstNameEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
            mFirstNameEditText.requestFocus();
            return;
        } else {
            mFirstNameErrorTextView.setVisibility(View.GONE);
            mFirstNameEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
        }

        if (mLastNameEditText.getText().toString().equals("")) {
            mLastNameErrorTextView.setVisibility(View.VISIBLE);
            mLastNameErrorTextView.setText(getString(R.string.error_field_required));
            mLastNameEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
            mLastNameEditText.requestFocus();
            return;
        } else {
            mLastNameErrorTextView.setVisibility(View.GONE);
            mLastNameEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
        }
        // gender valid - start
        if (!mGenderFemaleRadioButton.isChecked() && !mGenderMaleRadioButton.isChecked() && !mGenderOthersRadioButton.isChecked()) {
            mGenderErrorTextView.setVisibility(View.VISIBLE);
            return;
        } else {
            mGenderErrorTextView.setVisibility(View.GONE);
        }

        if (mDOBEditText.getText().toString().equals("")) {
            mDOBErrorTextView.setVisibility(View.VISIBLE);
            mDOBErrorTextView.setText(getString(R.string.error_field_required));
            mDOBEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
            mDOBEditText.requestFocus();
            return;
        } else {
            mDOBErrorTextView.setVisibility(View.GONE);
            mDOBEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
        }

        if (mAgeEditText.getText().toString().equals("")) {
            mAgeErrorTextView.setVisibility(View.VISIBLE);
            mAgeErrorTextView.setText(getString(R.string.error_field_required));
            mAgeEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
            mAgeEditText.requestFocus();
            return;
        } else {
            mAgeErrorTextView.setVisibility(View.GONE);
            mAgeEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
        }

        if (!mPhoneNumberEditText.getText().toString().equals("")) {
            String s = mPhoneNumberEditText.getText().toString().replaceAll("\\s+", "");
            Log.v("phone", "phone: " + s);
            if (s.length() < mSelectedMobileNumberValidationLength) {
                mPhoneNumberErrorTextView.setVisibility(View.VISIBLE);
                mPhoneNumberErrorTextView.setText(getString(R.string.enter_10_digits));
                mPhoneNumberEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
                mPhoneNumberEditText.requestFocus();
                return;
            }
            if (!mCountryCodePicker.getSelectedCountryCode().equalsIgnoreCase("91") && s.length() < 15) {
                mPhoneNumberErrorTextView.setVisibility(View.VISIBLE);
                mPhoneNumberErrorTextView.setText(getString(R.string.enter_15_digits));
                mPhoneNumberEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
                mPhoneNumberEditText.requestFocus();
                return;
            } else {
                mPhoneNumberErrorTextView.setVisibility(View.GONE);
                mPhoneNumberEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
            }

            if (mCountryCodePicker.getSelectedCountryCode().equalsIgnoreCase("91")
                    && s.length() != mSelectedMobileNumberValidationLength) {
                mPhoneNumberErrorTextView.setVisibility(View.VISIBLE);
                mPhoneNumberErrorTextView.setText(R.string.invalid_mobile_no);
                mPhoneNumberEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
                mPhoneNumberEditText.requestFocus();
                return;
            } else {
                mPhoneNumberErrorTextView.setVisibility(View.GONE);
                mPhoneNumberEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
            }
        }

        if (mCurrentPhotoPath != null)
            patientdto.setPatientPhoto(mCurrentPhotoPath);
        else
            patientdto.setPatientPhoto(patientdto.getPatientPhoto());

        patientdto.setFirstname(mFirstNameEditText.getText().toString());
        patientdto.setMiddlename(mMiddleNameEditText.getText().toString());
        patientdto.setLastname(mLastNameEditText.getText().toString());
        patientdto.setGender(StringUtils.getValue(mGender));
        if (!mPhoneNumberEditText.getText().toString().trim().equals(""))
            patientdto.setPhonenumber(StringUtils.getValue(mCountryCodePicker.getFullNumberWithPlus())); // automatically combines both cc and number togther.
        else
            patientdto.setPhonenumber("");
        patientdto.setDateofbirth(dobToDb);


        try {
            Logger.logD(TAG, "insertpatinet");
            boolean isPatientInserted = false;
            boolean isPatientImageInserted = false;
            PatientsDAO patientsDAO = new PatientsDAO();
            PatientAttributesDTO patientAttributesDTO = new PatientAttributesDTO();
            List<PatientAttributesDTO> patientAttributesDTOList = new ArrayList<>();

            if (patientdto.getPhonenumber() != null) {
                // mobile no adding in patient attributes.
                patientAttributesDTO = new PatientAttributesDTO();
                patientAttributesDTO.setUuid(UUID.randomUUID().toString());
                patientAttributesDTO.setPatientuuid(patientdto.getUuid());
                patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Telephone Number"));
                patientAttributesDTO.setValue(StringUtils.getValue(patientdto.getPhonenumber()));
                patientAttributesDTOList.add(patientAttributesDTO);
            }
            patientdto.setPatientAttributesDTOList(patientAttributesDTOList);
            patientdto.setSyncd(false);


            ImagesDAO imagesDAO = new ImagesDAO();

            if (patient_detail) {
                isPatientInserted = patientsDAO.updatePatientToDB_PatientDTO(patientdto, patientdto.getUuid(), patientAttributesDTOList);
                isPatientImageInserted = imagesDAO.updatePatientProfileImages(patientdto.getPatientPhoto(), patientdto.getUuid());
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("patientDTO", (Serializable) patientdto);
                bundle.putBoolean("fromFirstScreen", true);
                bundle.putBoolean("patient_detail", patient_detail);

                if (otpVerificationResponse != null)
                    bundle.putSerializable(PAYLOAD, otpVerificationResponse);

                if (abhaProfileResponse != null)
                    bundle.putSerializable(MOBILE_PAYLOAD, abhaProfileResponse);

                bundle.putString("accessToken", accessToken);
                bundle.putString("xToken", xToken);
                bundle.putString("txnId", txnId);
                fragment_secondScreen.setArguments(bundle); // passing data to Fragment

                Log.d(TAG, "onPatientCreateClicked: " + patientdto.toString());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_firstscreen, fragment_secondScreen)
                        .commit();
            }

            if (NetworkConnection.isOnline(getActivity().getApplication())) { // todo: uncomment later jsut for testing added.
                SyncDAO syncDAO = new SyncDAO();
                ImagesPushDAO imagesPushDAO = new ImagesPushDAO();
                boolean push = syncDAO.pushDataApi();
                boolean pushImage = imagesPushDAO.patientProfileImagesPush();
            }

            if (isPatientInserted && isPatientImageInserted) {
                Logger.logD(TAG, "inserted");
                Intent intent = new Intent(getActivity().getApplication(), PatientDetailActivity2.class);
                intent.putExtra("patientUuid", patientdto.getUuid());
                intent.putExtra("patientName", patientdto.getFirstname() + " " + patientdto.getLastname());
                intent.putExtra("tag", "newPatient");
                intent.putExtra("hasPrescription", "false");
                Bundle args = new Bundle();
                args.putSerializable("patientDTO", (Serializable) patientdto);
                // abha - start
                if (abhaProfileResponse != null)
                    args.putSerializable(MOBILE_PAYLOAD, abhaProfileResponse);
                if (otpVerificationResponse != null)
                    args.putSerializable(PAYLOAD, otpVerificationResponse);
                // abha - end

                args.putString("accessToken", accessToken);
                args.putString("xToken", xToken);
                args.putString("txnId", txnId);
                intent.putExtra("BUNDLE", args);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "Result Received");
        if (requestCode == CameraActivity.TAKE_IMAGE) {
            Log.v(TAG, "Request Code " + CameraActivity.TAKE_IMAGE);
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Result OK");
                mCurrentPhotoPath = data.getStringExtra("RESULT");
                Log.v("IdentificationActivity", mCurrentPhotoPath);

                Glide.with(getActivity())
                        .load(new File(mCurrentPhotoPath))
                        .thumbnail(0.25f)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(patient_imgview);
            }
        }
    }

}