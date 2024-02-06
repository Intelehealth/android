package org.intelehealth.app.abdm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;

import com.google.android.material.chip.Chip;

import org.intelehealth.app.R;
import org.intelehealth.app.abdm.model.EnrollSuggestionRequestBody;
import org.intelehealth.app.abdm.model.EnrollSuggestionResponse;
import org.intelehealth.app.abdm.model.OTPVerificationResponse;
import org.intelehealth.app.activities.identificationActivity.IdentificationActivity_New;
import org.intelehealth.app.app.AppConstants;
import org.intelehealth.app.databinding.ActivityAadharMobileVerificationBinding;
import org.intelehealth.app.databinding.ActivityAbhaAddressSuggestionsBinding;
import org.intelehealth.app.utilities.UrlModifiers;
import org.intelehealth.app.utilities.WindowsUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AbhaAddressSuggestionsActivity extends AppCompatActivity {
    private Context context = AbhaAddressSuggestionsActivity.this;
    public static final String TAG = AbhaAddressSuggestionsActivity.class.getSimpleName();
    ActivityAbhaAddressSuggestionsBinding binding;
    private String txnID, accessToken, selectedChip = "";
    private ArrayList<String> phrAddressList = new ArrayList<>();
    private OTPVerificationResponse otpVerificationResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAbhaAddressSuggestionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WindowsUtils.setStatusBarColor(AbhaAddressSuggestionsActivity.this);  // changing status bar color

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        otpVerificationResponse = (OTPVerificationResponse) intent.getSerializableExtra("payload");
        if (otpVerificationResponse != null) {
            txnID = otpVerificationResponse.getTxnId();     // auto-generated address from abdm end.
            /*phrAddressList = otpVerificationResponse.getABHAProfile().getPhrAddress();     // auto-generated abha preferred address from abdm end.
            Log.d(TAG, "phrAddress: " + phrAddressList.toString());*/
        }

        if (intent.hasExtra("addressList")) {
            phrAddressList = intent.getStringArrayListExtra("addressList");
            Log.d(TAG, "addressList: " + phrAddressList.toString());
            for (String phrAddressAutoGenerated : phrAddressList) {
                createDynamicChips(phrAddressAutoGenerated);
            }
        }

       /* createDynamicChips("prajwalw@sbx");
        createDynamicChips("prajuuu@sbx");
        createDynamicChips("aparna@sbx");
        createDynamicChips("kavita@sbx");
        createDynamicChips("hello@sbx");  // todo: testing -> comment later..*/

        if (binding.chipGrp.getChildCount() > 0) {
            for (int i = 0; i < binding.chipGrp.getChildCount(); i++) {
                int finalI = i;
                binding.chipGrp.getChildAt(i).setOnClickListener(v -> {
                    Chip chip = binding.chipGrp.findViewById(binding.chipGrp.getChildAt(finalI).getId());
                    chip.setChecked(true);
                    selectedChip = chip.getText().toString().trim();
                    Log.d(TAG, "ischecked: " + selectedChip);
                });
            }
        }

        binding.submitABHAAddressBtn.setOnClickListener(v -> {
            if (!selectedChip.isEmpty())    // here you set this value to the Setter of the response variable and pass it to identification screen.
                otpVerificationResponse.getABHAProfile().setAddress(selectedChip);

            Intent dataIntent = new Intent(context, IdentificationActivity_New.class);
            dataIntent.putExtra("payload", otpVerificationResponse);
            dataIntent.putExtra("accessToken", accessToken);
            startActivity(dataIntent);
            finish();
        });

        // todo: tesing -> uncomment later.
      /*  // api - start
        String url = UrlModifiers.getEnrollABHASuggestionUrl();
        EnrollSuggestionRequestBody body = new EnrollSuggestionRequestBody();
        body.setTxnId(txnID);

        Single<EnrollSuggestionResponse> enrollSuggestionResponseSingle =
                AppConstants.apiInterface.PUSH_ENROLL_ABHA_ADDRESS_SUGGESTION(url, accessToken, body);

        new Thread(new Runnable() {
            @Override
            public void run() {
                enrollSuggestionResponseSingle
                        .observeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableSingleObserver<EnrollSuggestionResponse>() {
                            @Override
                            public void onSuccess(EnrollSuggestionResponse enrollSuggestionResponse) {
                                Log.d(TAG, "onSuccess: suggestion: " + enrollSuggestionResponse);
                                if (enrollSuggestionResponse.getAbhaAddressList() != null) {

                                    for (String phrAddressAutoGenerated : phrAddressList) {
                                        createDynamicChips(phrAddressAutoGenerated);
                                    }
                                    for (String phrAddress : enrollSuggestionResponse.getAbhaAddressList()) {
                                        createDynamicChips(phrAddress);
                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: suggestion" + e.toString());
                            }
                        });
            }
        }).start();
        // api - end*/

    }

    private void createDynamicChips(String chipTitle) {
        Chip chip = new Chip(context);
        chip.setId(ViewCompat.generateViewId());
        chip.setText(chipTitle);
        chip.setCheckable(true);
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setChipStrokeColorResource(R.color.colorPrimaryDark);
        chip.setChipStrokeWidth(2);
        chip.setTextColor(getColor(R.color.colorPrimary));
        chip.isCloseIconVisible();
        chip.setCheckedIconTintResource(R.color.colorPrimary);

        if (binding.chipGrp.getChildCount() == 0) // ie. by default the auto-generated address will be selected.
            chip.setChecked(true);
        binding.chipGrp.addView(chip);
    }
}