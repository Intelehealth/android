package org.intelehealth.app.ayu.visit.pastmedicalhist;

import static org.intelehealth.app.syncModule.SyncUtils.syncNow;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.intelehealth.app.R;
import org.intelehealth.app.ayu.visit.VisitCreationActionListener;
import org.intelehealth.app.ayu.visit.VisitCreationActivity;
import org.intelehealth.app.ayu.visit.common.adapter.SummarySingleViewAdapter;
import org.intelehealth.app.ayu.visit.common.adapter.SummaryViewAdapter;
import org.intelehealth.app.ayu.visit.model.VisitSummaryData;
import org.intelehealth.app.knowledgeEngine.Node;
import org.intelehealth.app.utilities.NetworkConnection;
import org.intelehealth.app.utilities.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedicalHistorySummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicalHistorySummaryFragment extends Fragment {

    private List<Node> mAnsweredRootNodeList = new ArrayList<>();
    private List<List<VisitSummaryData>> mAllItemList = new ArrayList<>();
    private List<VisitSummaryData> mItemList = new ArrayList<VisitSummaryData>();
    private VisitCreationActionListener mActionListener;
    SessionManager sessionManager;
    private String mSummaryStringPastHistory, mSummaryStringFamilyHistory;
    private LinearLayout mSummaryLinearLayout;
    private ObjectAnimator syncAnimator;
    private boolean mIsEditMode = false;

    public MedicalHistorySummaryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MedicalHistorySummaryFragment newInstance(Intent intent, String patientHistory, String familyHistory, boolean isEditMode) {
        MedicalHistorySummaryFragment fragment = new MedicalHistorySummaryFragment();
        fragment.mSummaryStringPastHistory = patientHistory;
        fragment.mSummaryStringFamilyHistory = familyHistory;
        fragment.mIsEditMode = isEditMode;
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActionListener = (VisitCreationActionListener) context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medical_history_summary, container, false);
        mSummaryLinearLayout = view.findViewById(R.id.ll_summary);
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsEditMode) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                } else
                    mActionListener.onFormSubmitted(VisitCreationActivity.STEP_6_VISIT_SUMMARY, mIsEditMode, null);
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onFormSubmitted(VisitCreationActivity.FROM_SUMMARY_RESUME_BACK_FOR_EDIT, mIsEditMode, VisitCreationActivity.STEP_4_PAST_MEDICAL_HISTORY);
            }
        });
        view.findViewById(R.id.img_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionListener.onFormSubmitted(VisitCreationActivity.FROM_SUMMARY_RESUME_BACK_FOR_EDIT, mIsEditMode, VisitCreationActivity.STEP_4_PAST_MEDICAL_HISTORY);
            }
        });
        ImageButton refresh = view.findViewById(R.id.imb_btn_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkConnection.isOnline(getActivity())) {
                    syncNow(getActivity(), refresh, syncAnimator);
                }
            }
        });
        //prepareSummary();
        prepareSummaryV2();

        return view;
    }

    private void prepareSummaryV2() {
        mSummaryLinearLayout.removeAllViews();
        String str = mSummaryStringPastHistory;//"►<b>Abdominal Pain</b>: <br/>• Site - Upper (C) - Epigastric.<br/>• Pain radiates to - Middle (R) - Right Lumbar.<br/>• Onset - Gradual.<br/>• Timing - Morning.<br/>• Character of the pain - Constant.<br/>• Severity - Mild, 1-3.<br/>• Exacerbating Factors - Hunger.<br/>• Relieving Factors - Food.<br/>• Prior treatment sought - None.<br/> ►<b>Associated symptoms</b>: <br/>• Patient reports -<br/> Anorexia <br/>• Patient denies -<br/> Diarrhea,  Constipation,  Fever<br/>";
        String str1 = mSummaryStringFamilyHistory;//"►<b>Abdominal Pain</b>: <br/>• Site - Upper (C) - Epigastric.<br/>• Pain radiates to - Middle (R) - Right Lumbar.<br/>• Onset - Gradual.<br/>• Timing - Morning.<br/>• Character of the pain - Constant.<br/>• Severity - Mild, 1-3.<br/>• Exacerbating Factors - Hunger.<br/>• Relieving Factors - Food.<br/>• Prior treatment sought - None.<br/> ►<b>Associated symptoms</b>: <br/>• Patient reports -<br/> Anorexia <br/>• Patient denies -<br/> Diarrhea,  Constipation,  Fever<br/>";
        str = str.replaceAll("<.*?>", "");
        str1 = str1.replaceAll("<.*?>", "");
        System.out.println("mSummaryStringPastHistory - " + str);
        System.out.println("mSummaryStringFamilyHistory - " + str1);
        String[] spt = str.split("●");
        String[] spt1 = str1.split("●");
        List<String> list = new ArrayList<>();
        TreeMap<String, List<String>> mapData = new TreeMap<>(Collections.reverseOrder());
        mapData.put("Patient history", new ArrayList<>());
        mapData.put("Family history", new ArrayList<>());
        for (String s : spt) {
            System.out.println(s);
            if (!s.trim().isEmpty())
                mapData.get("Patient history").add(s.trim());


        }
        for (String s : spt1) {
            System.out.println(s);
            if (!s.trim().isEmpty())
                mapData.get("Family history").add(s.trim());


        }

        System.out.println(mapData);
        for (String key : mapData.keySet()) {

            String _complain = key.equalsIgnoreCase("Patient history") ? getString(R.string.title_activity_get_patient_history) : getString(R.string.title_activity_family_history);
            List<String> _list = mapData.get(key);

            if (!_complain.isEmpty() && !_list.isEmpty()) {
                View view = View.inflate(getActivity(), R.layout.ui2_summary_main_row_item_view, null);
                TextView complainLabelTextView = view.findViewById(R.id.tv_complain_label);
                complainLabelTextView.setText(_complain);
                view.findViewById(R.id.tv_change).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (key.equalsIgnoreCase("Patient history")) {
                            mActionListener.onFormSubmitted(VisitCreationActivity.FROM_SUMMARY_RESUME_BACK_FOR_EDIT, mIsEditMode, VisitCreationActivity.STEP_4_PAST_MEDICAL_HISTORY);
                        } else if (key.equalsIgnoreCase("Family history")) {
                            mActionListener.onFormSubmitted(VisitCreationActivity.FROM_SUMMARY_RESUME_BACK_FOR_EDIT, mIsEditMode, VisitCreationActivity.STEP_5_FAMILY_HISTORY);
                        }

                    }
                });
                RecyclerView recyclerView = view.findViewById(R.id.rcv_qa);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                List<VisitSummaryData> visitSummaryDataList = new ArrayList<>();
                for (int i = 0; i < _list.size(); i++) {
                    Log.v("K", "_list.get(i) - " + _list.get(i));
                    String[] qa = _list.get(i).split("•");
                    if (qa.length == 2) {
                        String k = qa[0].trim();
                        String v = qa[1].trim();
                        Log.v("K", "k - " + k);
                        Log.v("V", "V - " + v);
                        if (v.contains(":") && v.split(":").length > 1) {
                            v = v.split(":")[1];
                        }
                        VisitSummaryData summaryData = new VisitSummaryData();
                        summaryData.setQuestion(k.isEmpty() ? v : k);
                        summaryData.setDisplayValue(k.isEmpty() ? "" : v);
                        visitSummaryDataList.add(summaryData);
                    } else {
                        //String k = value.split("•")[0].trim();
                        StringBuilder stringBuilder = new StringBuilder();
                        String k1 = "";
                        String lastString = "";
                        if (key.equalsIgnoreCase("Patient history")) {
                            for (int j = 0; j < qa.length; j++) {
                                String v1 = qa[j];
                                Log.v("V", v1);
                                if (lastString.equals(v1)) continue;
                                //if (!stringBuilder.toString().isEmpty()) stringBuilder.append("\n");
                                stringBuilder.append(v1);
                                lastString = v1;
                                if (j % 2 != 0) {
                                    String v = qa[j].trim();
                                    if (v.contains(":") && v.split(":").length > 1) {
                                        v = v.split(":")[1];
                                    }
                                    VisitSummaryData summaryData = new VisitSummaryData();
                                    summaryData.setQuestion(k1);
                                    summaryData.setDisplayValue(v);
                                    visitSummaryDataList.add(summaryData);

                                } else {
                                    k1 = qa[j].trim();
                                }
                            }
                        } else {
                            for (int j = 0; j < qa.length; j++) {
                                Log.v("QA", "qa - " + qa[j]);
                                if (j == 0) {
                                    k1 = qa[j];
                                } else {
                                    if (!stringBuilder.toString().isEmpty())
                                        stringBuilder.append(Node.bullet_arrow);
                                    stringBuilder.append(qa[j]);
                                }

                            }
                            VisitSummaryData summaryData = new VisitSummaryData();
                            summaryData.setQuestion(k1);
                            summaryData.setDisplayValue(stringBuilder.toString());
                            visitSummaryDataList.add(summaryData);
                        }

                    }


                }

                SummaryViewAdapter summaryViewAdapter = new SummaryViewAdapter(recyclerView, getActivity(), visitSummaryDataList, new SummaryViewAdapter.OnItemSelection() {

                    @Override
                    public void onSelect(VisitSummaryData data) {

                    }
                });
                recyclerView.setAdapter(summaryViewAdapter);
                mSummaryLinearLayout.addView(view);
            }
        }

    }

    private void prepareSummary() {
        mSummaryLinearLayout.removeAllViews();
        String str = mSummaryStringPastHistory;//"►<b>Abdominal Pain</b>: <br/>• Site - Upper (C) - Epigastric.<br/>• Pain radiates to - Middle (R) - Right Lumbar.<br/>• Onset - Gradual.<br/>• Timing - Morning.<br/>• Character of the pain - Constant.<br/>• Severity - Mild, 1-3.<br/>• Exacerbating Factors - Hunger.<br/>• Relieving Factors - Food.<br/>• Prior treatment sought - None.<br/> ►<b>Associated symptoms</b>: <br/>• Patient reports -<br/> Anorexia <br/>• Patient denies -<br/> Diarrhea,  Constipation,  Fever<br/>";
        String str1 = mSummaryStringFamilyHistory;//"►<b>Abdominal Pain</b>: <br/>• Site - Upper (C) - Epigastric.<br/>• Pain radiates to - Middle (R) - Right Lumbar.<br/>• Onset - Gradual.<br/>• Timing - Morning.<br/>• Character of the pain - Constant.<br/>• Severity - Mild, 1-3.<br/>• Exacerbating Factors - Hunger.<br/>• Relieving Factors - Food.<br/>• Prior treatment sought - None.<br/> ►<b>Associated symptoms</b>: <br/>• Patient reports -<br/> Anorexia <br/>• Patient denies -<br/> Diarrhea,  Constipation,  Fever<br/>";
        str = str.replaceAll("<.*?>", "");
        str1 = str1.replaceAll("<.*?>", "");
        System.out.println("mSummaryStringPastHistory - " + str);
        System.out.println("mSummaryStringFamilyHistory - " + str1);
        String[] spt = str.split("•");
        String[] spt1 = str1.split("•");
        List<String> list = new ArrayList<>();
        TreeMap<String, List<String>> mapData = new TreeMap<>(Collections.reverseOrder());
        mapData.put("Patient history", new ArrayList<>());
        mapData.put("Family history", new ArrayList<>());
        for (String s : spt) {
            System.out.println(s);
            if (!s.trim().isEmpty())
                mapData.get("Patient history").add(s.trim());


        }
        for (String s : spt1) {
            System.out.println(s);
            if (!s.trim().isEmpty())
                mapData.get("Family history").add(s.trim());


        }

        System.out.println(mapData);
        for (String key : mapData.keySet()) {

            String _complain = key;
            List<String> _list = mapData.get(key);

            if (!_complain.isEmpty() && !_list.isEmpty()) {
                View view = View.inflate(getActivity(), R.layout.ui2_summary_main_row_item_view, null);
                TextView complainLabelTextView = view.findViewById(R.id.tv_complain_label);
                complainLabelTextView.setText(_complain);
                view.findViewById(R.id.tv_change).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (key.equalsIgnoreCase("Patient history")) {
                            mActionListener.onFormSubmitted(VisitCreationActivity.FROM_SUMMARY_RESUME_BACK_FOR_EDIT, mIsEditMode, VisitCreationActivity.STEP_4_PAST_MEDICAL_HISTORY);
                        } else if (key.equalsIgnoreCase("Family history")) {
                            mActionListener.onFormSubmitted(VisitCreationActivity.FROM_SUMMARY_RESUME_BACK_FOR_EDIT, mIsEditMode, VisitCreationActivity.STEP_5_FAMILY_HISTORY);
                        }

                    }
                });
                RecyclerView recyclerView = view.findViewById(R.id.rcv_qa);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                SummarySingleViewAdapter summaryViewAdapter = new SummarySingleViewAdapter(recyclerView, getActivity(), _list, new SummarySingleViewAdapter.OnItemSelection() {
                    @Override
                    public void onSelect(String data) {

                    }
                });
                recyclerView.setAdapter(summaryViewAdapter);
                mSummaryLinearLayout.addView(view);
            }
        }

    }
}