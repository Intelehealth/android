package org.intelehealth.app.ayu.visit.reason;

import static org.intelehealth.app.ayu.visit.common.VisitUtils.getTranslatedAssociatedSymptomQString;
import static org.intelehealth.app.ayu.visit.common.VisitUtils.getTranslatedPatientDenies;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import org.intelehealth.app.utilities.CustomLog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.intelehealth.app.R;
import org.intelehealth.app.ayu.visit.VisitCreationActionListener;
import org.intelehealth.app.ayu.visit.VisitCreationActivity;
import org.intelehealth.app.ayu.visit.common.adapter.SummaryViewAdapter;
import org.intelehealth.app.ayu.visit.model.CommonVisitData;
import org.intelehealth.app.ayu.visit.model.VisitSummaryData;
import org.intelehealth.app.knowledgeEngine.Node;
import org.intelehealth.app.syncModule.SyncUtils;
import org.intelehealth.app.utilities.NetworkConnection;
import org.intelehealth.app.utilities.SessionManager;
import org.intelehealth.config.room.entity.FeatureActiveStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private LinearLayout mAssociateSymptomsLinearLayout, mComplainSummaryLinearLayout;
    private VisitCreationActionListener mActionListener;
    SessionManager sessionManager;
    private boolean mIsEditMode = false;
    private TextView mAssociateSymptomsLabelTextView, mAssociateSymptChangeTextView;

    public VisitReasonSummaryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static VisitReasonSummaryFragment newInstance(CommonVisitData commonVisitData, String values, boolean isEditMode) {
        VisitReasonSummaryFragment fragment = new VisitReasonSummaryFragment();
        fragment.mSummaryString = values;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FeatureActiveStatus status = ((VisitCreationActivity) requireActivity()).getFeatureActiveStatus();
        int index = status.getVitalSection() ? 2 : 1;
        int total = status.getVitalSection() ? 4 : 3;
        TextView tvTitle = view.findViewById(R.id.tv_sub_title);
        tvTitle.setText(getString(R.string._visit_reason_summary, index, total));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visit_reason_summary, container, false);


        mComplainSummaryLinearLayout = view.findViewById(R.id.ll_complain_summary);
        mAssociateSymptomsLinearLayout = view.findViewById(R.id.ll_associated_sympt);
        mAssociateSymptomsLabelTextView = view.findViewById(R.id.tv_ass_complain_label);
        mAssociateSymptChangeTextView = view.findViewById(R.id.tv_change_associate_sympt);

        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsEditMode && ((VisitCreationActivity) requireActivity()).isEditTriggerFromVisitSummary()) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                } else
                    mActionListener.onFormSubmitted(VisitCreationActivity.STEP_3_PHYSICAL_EXAMINATION, mIsEditMode, null);
            }
        });
        mAssociateSymptChangeTextView.setOnClickListener(new View.OnClickListener() {
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
        String answerInLocale = mSummaryString;
        answerInLocale = answerInLocale.replaceAll("<.*?>", "");
        String[] spt = answerInLocale.split("►");
        List<String> list = new ArrayList<>();
        String associatedSymptomsString = "";
        for (String s : spt) {
            if (s.isEmpty()) continue;
            if (s.trim().contains(getTranslatedPatientDenies(sessionManager.getAppLanguage())) || s.trim().contains(getTranslatedAssociatedSymptomQString(sessionManager.getAppLanguage()))) {
                associatedSymptomsString = s;
            } else {
                list.add(s);
            }
        }

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
                        if (v.endsWith(",")) {
                            v = v.substring(0, v.length() - 1);
                        }
                        VisitSummaryData summaryData = new VisitSummaryData();
                        summaryData.setQuestion(k);
                        summaryData.setDisplayValue(v);
                        visitSummaryDataList.add(summaryData);
                    } else {


                        //String k = value.split("•")[0].trim();
                        StringBuilder stringBuilder = new StringBuilder();
                        String key = "";
                        String lastString = "";
                        for (int j = 0; j < qa.length; j++) {

                            String v1 = qa[j].trim();
                            System.out.println(v1);
                            if (lastString.equals(v1)) continue;
                            //if (!stringBuilder.toString().isEmpty()) stringBuilder.append("\n");
                            stringBuilder.append(v1);
                            lastString = v1;
                            if (j % 2 != 0) {
                                String v = qa[j].trim();
                                if (j == qa.length - 2) {
                                    v = v + Node.bullet_arrow + qa[j + 1];
                                }

                                VisitSummaryData summaryData = new VisitSummaryData();
                                if (v.endsWith(",")) {
                                    v = v.substring(0, v.length() - 1);
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

        // ASSOCIATED SYMPTOMS
        String[] tempAS = associatedSymptomsString.split("::");
        if (tempAS.length >= 2) {
            String title = tempAS[0];
            mAssociateSymptomsLabelTextView.setText(title);

            associatedSymptomsString = tempAS[1];
        }

        mAssociateSymptomsLinearLayout.removeAllViews();

        if (!associatedSymptomsString.trim().isEmpty()) {
            String[] sections = associatedSymptomsString.split(getTranslatedPatientDenies(sessionManager.getAppLanguage()));

            CustomLog.v(TAG, associatedSymptomsString);
            String[] spt1 = associatedSymptomsString.trim().split("•");
            CustomLog.e("node", associatedSymptomsString);
            CustomLog.e("node", String.valueOf(spt1.length));
            CustomLog.e("node", "sections.length - " + String.valueOf(sections.length));
            for (int i = 0; i < sections.length; i++) {
                String patientReports = sections[i]; // Patient reports & // Patient denies
                if (patientReports != null && patientReports.length() >= 2) {
                    patientReports = patientReports.substring(1);
                    patientReports = patientReports.replace("•", ", ");
                    patientReports = patientReports.replace("●", ", ");
                    View view = View.inflate(getActivity(), R.layout.ui2_summary_qa_ass_sympt_row_item_view, null);
                    TextView keyTextView = view.findViewById(R.id.tv_question_label);
                    keyTextView.setText(i == 0 ? getString(R.string.patient_reports) : getString(R.string.patient_denies));
                    TextView valueTextView = view.findViewById(R.id.tv_answer_value);
                    valueTextView.setText(patientReports.trim());
               /* if (patientReportsDenies.isEmpty()) {
                    view.findViewById(R.id.iv_blt).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.iv_blt).setVisibility(View.VISIBLE);
                }*/
                    mAssociateSymptomsLinearLayout.addView(view);
                }
            }
            mAssociateSymptomsLabelTextView.setVisibility(View.VISIBLE);
            mAssociateSymptChangeTextView.setVisibility(View.VISIBLE);
        } else {
            mAssociateSymptomsLabelTextView.setVisibility(View.INVISIBLE);
            mAssociateSymptChangeTextView.setVisibility(View.INVISIBLE);
        }


        for (int i = 0; i < mAnsweredRootNodeList.size(); i++) {
            List<VisitSummaryData> itemList = new ArrayList<VisitSummaryData>();
            for (int j = 0; j < mAnsweredRootNodeList.get(i).getOptionsList().size(); j++) {
                VisitSummaryData summaryData = new VisitSummaryData();
                summaryData.setDisplayValue(mAnsweredRootNodeList.get(i).getOptionsList().get(j).getText());
                itemList.add(summaryData);
            }
        }
    }
}