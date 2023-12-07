package org.intelehealth.app.ayu.visit.reason;

import static org.intelehealth.app.ayu.visit.common.VisitUtils.getTranslatedAssociatedSymptomQString;
import static org.intelehealth.app.ayu.visit.common.VisitUtils.getTranslatedAssociatedSymptomQStringNew;
import static org.intelehealth.app.ayu.visit.common.VisitUtils.getTranslatedPatientDenies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.intelehealth.app.R;
import org.intelehealth.app.ayu.visit.VisitCreationActionListener;
import org.intelehealth.app.ayu.visit.VisitCreationActivity;
import org.intelehealth.app.ayu.visit.common.adapter.SummaryViewAdapter;
import org.intelehealth.app.ayu.visit.model.VisitSummaryData;
import org.intelehealth.app.knowledgeEngine.Node;
import org.intelehealth.app.syncModule.SyncUtils;
import org.intelehealth.app.utilities.NetworkConnection;
import org.intelehealth.app.utilities.SessionManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VisitReasonSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisitReasonSummaryFragment extends Fragment {
    public static final String TAG = VisitReasonSummaryFragment.class.getSimpleName();
    private List<Node> mAnsweredRootNodeList = new ArrayList<>();
    private List<List<VisitSummaryData>> mAllItemList = new ArrayList<>();
    private List<VisitSummaryData> mItemList = new ArrayList<VisitSummaryData>();
    private String mSummaryString;
    private JSONObject mSummaryStringJsonObject;
    private LinearLayout mAssociateSymptomsLinearLayout, mComplainSummaryLinearLayout;

    private View additionalView;

    private VisitCreationActionListener mActionListener;
    SessionManager sessionManager;
    private boolean mIsEditMode = false;
    private TextView mAssociateSymptomsLabelTextView;

    public VisitReasonSummaryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static VisitReasonSummaryFragment newInstance(Intent intent, String values, boolean isEditMode) {
        VisitReasonSummaryFragment fragment = new VisitReasonSummaryFragment();
        fragment.mSummaryString = values;
        try {
            fragment.mSummaryStringJsonObject = new JSONObject(values);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        fragment.mIsEditMode = isEditMode;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActionListener = (VisitCreationActionListener) context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visit_reason_summary, container, false);

        mComplainSummaryLinearLayout = view.findViewById(R.id.ll_complain_summary);
        mAssociateSymptomsLinearLayout = view.findViewById(R.id.ll_associated_sympt);
        mAssociateSymptomsLabelTextView = view.findViewById(R.id.tv_ass_complain_label);
        additionalView = view.findViewById(R.id.rlAdditionalInfo);
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsEditMode && ((VisitCreationActivity) requireActivity()).isEditTriggerFromVisitSummary()) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                } else
                    mActionListener.onFormSubmitted(VisitCreationActivity.STEP_1_VITAL, mIsEditMode, null);
            }
        });
        view.findViewById(R.id.tv_change_associate_sympt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onFormSubmitted(VisitCreationActivity.FROM_SUMMARY_RESUME_BACK_FOR_EDIT, mIsEditMode, VisitCreationActivity.STEP_2_VISIT_REASON_QUESTION_ASSOCIATE_SYMPTOMS);
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onFormSubmitted(VisitCreationActivity.FROM_SUMMARY_RESUME_BACK_FOR_EDIT, mIsEditMode, VisitCreationActivity.STEP_2_VISIT_REASON_QUESTION);
            }
        });
        view.findViewById(R.id.img_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onFormSubmitted(VisitCreationActivity.FROM_SUMMARY_RESUME_BACK_FOR_EDIT, mIsEditMode, VisitCreationActivity.STEP_2_VISIT_REASON_QUESTION);
            }
        });
        view.findViewById(R.id.imb_btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkConnection.isOnline(getActivity())) {
                    new SyncUtils().syncBackground();
//                    Toast.makeText(getActivity(), getString(R.string.sync_strated), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //prepareSummary();
        prepareSummaryV2();
        return view;
    }

    private void prepareSummaryV2() {
        try {
            String lCode = sessionManager.getAppLanguage();
            String answerInLocale = mSummaryStringJsonObject.getString("l-" + lCode);
            answerInLocale = answerInLocale.replaceAll("<.*?>", "");
            System.out.println(answerInLocale);
            Log.d(TAG, "prepareSummaryV2=>" + answerInLocale);
            String[] spt = answerInLocale.split(Node.bullet_arrow);
            Log.d(TAG, "prepareSummaryV2: " + spt.length);
            List<String> list = new ArrayList<>();
            String associatedSymptomsString = "";
            for (String s : spt) {
                if (s.isEmpty()) continue;
                //String s1 =  new String(s.getBytes(), "UTF-8");
                System.out.println("Chunk - " + s);
                Log.d(TAG, "prepareSummaryV2: value=> " + s);
                //if (s.trim().startsWith(getTranslatedAssociatedSymptomQString(lCode))) {
                //if (s.trim().contains("Patient denies -•")) {
                if (s.trim().contains(getTranslatedPatientDenies(lCode)) || s.trim().contains(getTranslatedAssociatedSymptomQString(lCode)) || s.trim().contains(getTranslatedAssociatedSymptomQStringNew(lCode))) {
                    associatedSymptomsString = s;
                    System.out.println("associatedSymptomsString - " + associatedSymptomsString);
                } else {
                    list.add(s);
                }

            }
            Log.d(TAG, "prepareSummaryV2: list=>" + list.size());
            mComplainSummaryLinearLayout.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                String complainName = "";
                List<VisitSummaryData> visitSummaryDataList = new ArrayList<>();
                String[] spt1 = list.get(i).split("●");
                for (String value : spt1) {
                    if (value.contains("::")) {
                        complainName = value.replace("::", "");
                        System.out.println(complainName);
                    } else {
                        String[] qa = value.split("•");
                        if (qa.length == 2) {
                            String k = value.split("•")[0].trim();
                            String v = value.split("•")[1].trim();
                            if(v.endsWith(",")){
                                v =  v.substring(0, v.length()-1);
                            }
                            VisitSummaryData summaryData = new VisitSummaryData();
                            summaryData.setQuestion(k);
                            summaryData.setDisplayValue(v);
                            visitSummaryDataList.add(summaryData);
                        } else {
                            StringBuilder stringBuilder = new StringBuilder();
                            String key = "";
                            String lastString = "";
                            for (int j = 0; j < qa.length; j++) {
                                String v1 = qa[j].trim();
                                System.out.println(v1);
                                if (lastString.equals(v1)) continue;
                                stringBuilder.append(v1);
                                lastString = v1;
                                if (j % 2 != 0) {
                                    String v = qa[j].trim();
                                    if (j == qa.length - 2) {
                                        v = v + Node.bullet_arrow + qa[j + 1];
                                    }
                                    VisitSummaryData summaryData = new VisitSummaryData();
                                    if(v.endsWith(",")){
                                        v =  v.substring(0, v.length()-1);
                                    }
                                    summaryData.setQuestion(key);
                                    summaryData.setDisplayValue(v);
                                    visitSummaryDataList.add(summaryData);
                                } else {
                                    key = qa[j].trim();
                                }
                            }
                        }
                    }

                }

                if (!complainName.isEmpty() && !visitSummaryDataList.isEmpty()) {
                    View view = View.inflate(getActivity(), R.layout.ui2_summary_main_row_item_view, null);
                    TextView complainLabelTextView = view.findViewById(R.id.tv_complain_label);
                    complainLabelTextView.setText(complainName);
                    view.findViewById(R.id.tv_change).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mActionListener.onFormSubmitted(VisitCreationActivity.FROM_SUMMARY_RESUME_BACK_FOR_EDIT, true, VisitCreationActivity.STEP_2_VISIT_REASON_QUESTION);
                        }
                    });

                    RecyclerView recyclerView = view.findViewById(R.id.rcv_qa);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                    SummaryViewAdapter summaryViewAdapter = new SummaryViewAdapter(recyclerView, getActivity(), visitSummaryDataList, new SummaryViewAdapter.OnItemSelection() {
                        @Override
                        public void onSelect(VisitSummaryData data) {

                        }
                    });
                    recyclerView.setAdapter(summaryViewAdapter);
                    mComplainSummaryLinearLayout.addView(view);
                }
            }

            // ASSOCIATED SYMPTOMS
            String[] tempAS = associatedSymptomsString.split("::");
            if (tempAS.length >= 2) {
                String title = tempAS[0];
                associatedSymptomsString = tempAS[1];

            }
            mAssociateSymptomsLinearLayout.removeAllViews();
            additionalView.setVisibility(!associatedSymptomsString.isEmpty() ? View.VISIBLE : View.GONE);
            View view = View.inflate(getActivity(), R.layout.ui2_summary_qa_ass_sympt_row_item_view, null);
            TextView keyTextView = view.findViewById(R.id.tv_question_label);
            keyTextView.setText(tempAS[0]);
            TextView valueTextView = view.findViewById(R.id.tv_answer_value);
            valueTextView.setText(associatedSymptomsString);
            mAssociateSymptomsLinearLayout.addView(view);


            for (int i = 0; i < mAnsweredRootNodeList.size(); i++) {
                List<VisitSummaryData> itemList = new ArrayList<VisitSummaryData>();
                for (int j = 0; j < mAnsweredRootNodeList.get(i).getOptionsList().size(); j++) {
                    VisitSummaryData summaryData = new VisitSummaryData();
                    summaryData.setDisplayValue(mAnsweredRootNodeList.get(i).getOptionsList().get(j).getText());
                    itemList.add(summaryData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* */

    /**
     * @param
     * @return
     *//*
   private String getTranslatedAssociatedSymptomQString(String localeCode) {
        if (localeCode.equalsIgnoreCase("hi")) {
            return "क्या आपको निम्न लक्षण है";
        } else if (localeCode.equalsIgnoreCase("or")) {
            return "ତମର ଏହି ଲକ୍ଷଣ ସବୁ ଅଛି କି?";
        } else {
            return "Do you have the following symptom(s)?";
        }
    }

    private String getTranslatedPatientDenies(String localeCode) {
        if (localeCode.equalsIgnoreCase("hi")) {
            return "पेशेंट ने मना कर दिया -";
        } else if (localeCode.equalsIgnoreCase("or")) {
            return "ରୋଗୀ ଅସ୍ୱୀକାର କରନ୍ତି -";
        } else {
            return "Patient denies -";
        }
    }*/
    private void prepareSummary() {
        try {
            String str = mSummaryStringJsonObject.getString("en");
            //String str = mSummaryString;//"►<b>Abdominal Pain</b>: <br/>• Site - Upper (C) - Epigastric.<br/>• Pain radiates to - Middle (R) - Right Lumbar.<br/>• Onset - Gradual.<br/>• Timing - Morning.<br/>• Character of the pain - Constant.<br/>• Severity - Mild, 1-3.<br/>• Exacerbating Factors - Hunger.<br/>• Relieving Factors - Food.<br/>• Prior treatment sought - None.<br/> ►<b>Associated symptoms</b>: <br/>• Patient reports -<br/> Anorexia <br/>• Patient denies -<br/> Diarrhea,  Constipation,  Fever<br/>";
            str = str.replaceAll("<.*?>", "");
            System.out.println(str);
            String[] spt = str.split("►");
            List<String> list = new ArrayList<>();
            String associatedSymptomsString = "";
            for (String s : spt) {
                Log.e("node", s);
                if (s.trim().startsWith("Associated symptoms:")) {
                    associatedSymptomsString = s;
                } else {
                    list.add(s);
                }

            }
            mComplainSummaryLinearLayout.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                String complainName = "";
                List<VisitSummaryData> visitSummaryDataList = new ArrayList<>();
                String[] spt1 = list.get(i).split("•");
                for (String value : spt1) {

                    if (value.contains(" - ")) {
                        String k = value.substring(0, value.indexOf(" - ")).trim();
                        String v = value.substring(value.indexOf(" - ") + 2).trim();
                        VisitSummaryData summaryData = new VisitSummaryData();
                        summaryData.setQuestion(k);
                        summaryData.setDisplayValue(v);
                        visitSummaryDataList.add(summaryData);
                    } else if (value.contains(":")) {
                        complainName = value;
                        System.out.println(complainName);
                    }

                }
                if (!complainName.isEmpty() && !visitSummaryDataList.isEmpty()) {
                    View view = View.inflate(getActivity(), R.layout.ui2_summary_main_row_item_view, null);
                    TextView complainLabelTextView = view.findViewById(R.id.tv_complain_label);
                    complainLabelTextView.setText(complainName);
                    view.findViewById(R.id.tv_change).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mActionListener.onFormSubmitted(VisitCreationActivity.FROM_SUMMARY_RESUME_BACK_FOR_EDIT, mIsEditMode, VisitCreationActivity.STEP_2_VISIT_REASON_QUESTION);
                        }
                    });

                    RecyclerView recyclerView = view.findViewById(R.id.rcv_qa);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                    SummaryViewAdapter summaryViewAdapter = new SummaryViewAdapter(recyclerView, getActivity(), visitSummaryDataList, new SummaryViewAdapter.OnItemSelection() {
                        @Override
                        public void onSelect(VisitSummaryData data) {

                        }
                    });
                    recyclerView.setAdapter(summaryViewAdapter);
                    mComplainSummaryLinearLayout.addView(view);
                }
            }
            String[] spt1 = associatedSymptomsString.trim().split("•");
            Log.e("node", associatedSymptomsString);
            Log.e("node", String.valueOf(spt1.length));
            mAssociateSymptomsLinearLayout.removeAllViews();
            for (String value : spt1) {
                Log.e("node", value);
                if (value.contains(" - ")) {
                    String k = value.substring(0, value.indexOf(" - ")).trim();
                    String v = value.substring(value.indexOf(" - ") + 2).trim();
                    View view = View.inflate(getActivity(), R.layout.ui2_summary_qa_ass_sympt_row_item_view, null);
                    TextView keyTextView = view.findViewById(R.id.tv_question_label);
                    keyTextView.setText(k);
                    TextView valueTextView = view.findViewById(R.id.tv_answer_value);
                    valueTextView.setText(v);
                    if (v.isEmpty()) {
                        view.findViewById(R.id.iv_blt).setVisibility(View.GONE);
                    } else {
                        view.findViewById(R.id.iv_blt).setVisibility(View.VISIBLE);
                    }
                    mAssociateSymptomsLinearLayout.addView(view);
                } else if (value.contains(":")) {
                    System.out.println(value);
                }

            }
            for (int i = 0; i < mAnsweredRootNodeList.size(); i++) {
                List<VisitSummaryData> itemList = new ArrayList<VisitSummaryData>();
                for (int j = 0; j < mAnsweredRootNodeList.get(i).getOptionsList().size(); j++) {
                    VisitSummaryData summaryData = new VisitSummaryData();
                    summaryData.setDisplayValue(mAnsweredRootNodeList.get(i).getOptionsList().get(j).getText());
                    itemList.add(summaryData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}