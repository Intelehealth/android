package org.intelehealth.ekalarogya.activities.questionNodeActivity;

import static org.intelehealth.ekalarogya.utilities.StringUtils.node_fetch_local_language;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;

import org.intelehealth.ekalarogya.R;
import org.intelehealth.ekalarogya.activities.pastMedicalHistoryActivity.PastMedicalHistoryActivity;
import org.intelehealth.ekalarogya.activities.physcialExamActivity.PhysicalExamActivity;
import org.intelehealth.ekalarogya.app.AppConstants;
import org.intelehealth.ekalarogya.app.IntelehealthApplication;
import org.intelehealth.ekalarogya.database.dao.EncounterDAO;
import org.intelehealth.ekalarogya.database.dao.ImagesDAO;
import org.intelehealth.ekalarogya.database.dao.ObsDAO;
import org.intelehealth.ekalarogya.database.dao.PatientsDAO;
import org.intelehealth.ekalarogya.knowledgeEngine.Node;
import org.intelehealth.ekalarogya.knowledgeEngine.ncd.NCDNodeValidationLogic;
import org.intelehealth.ekalarogya.knowledgeEngine.ncd.NCDValidationResult;
import org.intelehealth.ekalarogya.knowledgeEngine.ncd.ValidationConstants;
import org.intelehealth.ekalarogya.models.AnswerResult;
import org.intelehealth.ekalarogya.models.dto.ObsDTO;
import org.intelehealth.ekalarogya.utilities.FileUtils;
import org.intelehealth.ekalarogya.utilities.SessionManager;
import org.intelehealth.ekalarogya.utilities.StringUtils;
import org.intelehealth.ekalarogya.utilities.UuidDictionary;
import org.intelehealth.ekalarogya.utilities.exception.DAOException;
import org.intelehealth.ekalarogya.utilities.pageindicator.ScrollingPagerIndicator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;


public class QuestionNodeActivity extends AppCompatActivity implements QuestionsAdapter.FabClickListener {
    final String TAG = "Question Node Activity";
    String patientUuid;
    String visitUuid;
    String state;
    String patientName;
    String intentTag;
    String mgender;

    String imageName;
    File filePath;
    Boolean complaintConfirmed = false;
    SessionManager sessionManager = null;
    private float float_ageYear_Month;

    //    Knowledge mKnowledge; //Knowledge engine
    // ExpandableListView questionListView;
    String mFileName = "knowledge.json"; //knowledge engine file
    //    String mFileName = "DemoBrain.json";
    int complaintNumber = 0; //assuming there is at least one complaint, starting complaint number
    HashMap<String, String> complaintDetails; //temporary storage of complaint findings
    ArrayList<String> complaints; //list of complaints going to be used
    List<Node> complaintsNodes; //actual nodes to be used
    ArrayList<String> physicalExams;
    private Node mCurrentNode;
    // CustomExpandableListAdapter adapter;
    QuestionsAdapter mQuestionListingadapter;
    boolean nodeComplete = false;

    int lastExpandedPosition = -1;
    String insertion = "", insertion_REG = "";
    private SharedPreferences prefs;
    private String encounterVitals;
    private String encounterAdultIntials, EncounterAdultInitial_LatestVisit;

    private List<Node> optionsList = new ArrayList<>();
    Node assoSympNode;
    private JSONObject assoSympObj = new JSONObject();
    private JSONArray assoSympArr = new JSONArray();
    private JSONObject finalAssoSympObj = new JSONObject();
    ScrollingPagerIndicator recyclerViewIndicator;

    FloatingActionButton fab, forwardButton, backButton;
    RelativeLayout navButtonRelativeLayout;
    RecyclerView question_recyclerView;
    Context context;
    private LinearLayout mTimerLinearLayout;
    private TextView mTimerTitleTextView, mTimerTextView;

    private HorizontalScrollLockLayoutManager linearLayoutManager;
    private BroadcastReceiver mQuestionActionBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Handle the broadcast
            String action = intent.getAction();
            if (action != null && action.equals(ValidationConstants.ACTION_QUESTION_STATUS_UPDATE)) {
                // Perform your actions here
                int waitTime = intent.getIntExtra("recurring_wait_time_min", 0);
                int maxTryCount = intent.getIntExtra("recurring_max_try_count", 0);
                int currentStep = intent.getIntExtra("recurring_current_step", 0);
                String nodeText = intent.getStringExtra("node_text");
                boolean isRequiredToMoveNextQuestion = intent.getBooleanExtra("move_next", false);
                if (isRequiredToMoveNextQuestion) {
                    forwardButton.setVisibility(View.VISIBLE);
                    mTimerLinearLayout.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.please_move_to_next_question, Toast.LENGTH_SHORT).show();
                } else {
                    forwardButton.setVisibility(View.INVISIBLE);
                    mTimerLinearLayout.setVisibility(View.VISIBLE);
                    mTimerTitleTextView.setText("Waiting for " + nodeText + " - " + currentStep);
                    mCountDownTimer = new CountDownTimer(waitTime * 60 * 1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            // Used for formatting digit to be in 2 digits only
                            NumberFormat f = new DecimalFormat("00");
                            //long hour = (millisUntilFinished / 3600000) % 24;
                            long min = (millisUntilFinished / 60000) % 60;
                            long sec = (millisUntilFinished / 1000) % 60;
                            //mTimerTextView.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                            mTimerTextView.setText(f.format(min) + ":" + f.format(sec));
                        }

                        // When the task is over it will print 00:00:00 there
                        public void onFinish() {
                            mTimerTextView.setText("00:00");
                            mTimerLinearLayout.setVisibility(View.GONE);
                            // Get instance of Vibrator from current Context
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                            // Vibrate for 400 milliseconds
                            v.vibrate(400);
                            Toast.makeText(context, "Please take the reading for " + nodeText, Toast.LENGTH_SHORT).show();

                            // TODO:directly open the input box
                            mQuestionListingadapter.manualClickActionOnRecurringInput();

                        }
                    }.start();
                }

            }
        }
    };

    private CountDownTimer mCountDownTimer;

    @Override
    protected void onResume() {
        super.onResume();
        // Register the receiver
        IntentFilter filter = new IntentFilter(ValidationConstants.ACTION_QUESTION_STATUS_UPDATE);
        registerReceiver(mQuestionActionBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the receiver
        unregisterReceiver(mQuestionActionBroadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);
        String language = sessionManager.getAppLanguage();
        context = QuestionNodeActivity.this;

        //In case of crash still the app should hold the current lang fix.
        if (!language.equalsIgnoreCase("")) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
        sessionManager.setCurrentLang(getResources().getConfiguration().locale.toString());

        filePath = new File(AppConstants.IMAGE_PATH);
        Intent intent = this.getIntent(); // The intent was passed to the activity
        if (intent != null) {
            patientUuid = intent.getStringExtra("patientUuid");
            visitUuid = intent.getStringExtra("visitUuid");
            state = intent.getStringExtra("state");
            encounterVitals = intent.getStringExtra("encounterUuidVitals");
            encounterAdultIntials = intent.getStringExtra("encounterUuidAdultIntial");
            EncounterAdultInitial_LatestVisit = intent.getStringExtra("EncounterAdultInitial_LatestVisit");
            float_ageYear_Month = intent.getFloatExtra("float_ageYear_Month", 0);
            patientName = intent.getStringExtra("name");
            intentTag = intent.getStringExtra("tag");
            complaints = intent.getStringArrayListExtra("complaints");
        }
        complaintDetails = new HashMap<>();
        physicalExams = new ArrayList<>();
        complaintsNodes = new ArrayList<>();

        boolean hasLicense = false;
        if (!sessionManager.getLicenseKey().isEmpty())
            hasLicense = true;

        JSONObject currentFile = null;
        for (int i = 0; i < complaints.size(); i++) {
            if (hasLicense) {
                try {
                    String complaintsFile = FileUtils.readFile(complaints.get(i) + ".json", this);
                    if (complaintsFile != null) {
                        currentFile = new JSONObject(complaintsFile);
                    }
                } catch (JSONException e) {
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
            } else {
                String fileLocation = "engines/" + complaints.get(i) + ".json";
                currentFile = FileUtils.encodeJSON(this, fileLocation);
            }

            if (currentFile != null) {
                Node currentNode = new Node(currentFile);
                complaintsNodes.add(currentNode);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_node);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this, R.style.ToolbarTheme);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // questionListView = findViewById(R.id.complaint_question_expandable_list_view);

        fab = findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabClick();
            }
        });

        mTimerLinearLayout = findViewById(R.id.ll_timer);
        mTimerTitleTextView = findViewById(R.id.tv_timer_title);
        mTimerTextView = findViewById(R.id.tv_timer);

        navButtonRelativeLayout = findViewById(R.id.rl_nav_btn);
        forwardButton = findViewById(R.id.btn_forward);
        backButton = findViewById(R.id.btn_back);

        recyclerViewIndicator = findViewById(R.id.recyclerViewIndicator);
        question_recyclerView = findViewById(R.id.question_recyclerView);
        linearLayoutManager = new HorizontalScrollLockLayoutManager(this);
        question_recyclerView.setLayoutManager(linearLayoutManager);

        question_recyclerView.setNestedScrollingEnabled(true);
        question_recyclerView.setItemAnimator(new DefaultItemAnimator());
        PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(question_recyclerView);

        setupQuestions(complaintNumber);
        //In the event there is more than one complaint, they will be prompted one at a time.

 /*       questionListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                onListClicked(v, groupPosition, childPosition);

                return false;

            }
        });

        //Not a perfect method, but closes all other questions when a new one is clicked.
        //Expandable Lists in Android are broken, so this is a band-aid fix.
        questionListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    questionListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });*/

    }


    public void onListClicked(View v, int groupPosition, int childPosition) {
        Log.e(TAG, "CLICKED: " + mCurrentNode.getOption(groupPosition).toString());
        if ((mCurrentNode.getOption(groupPosition).getChoiceType().equals("single")) && !mCurrentNode.getOption(groupPosition).anySubSelected()) {
            Node question = mCurrentNode.getOption(groupPosition).getOption(childPosition);
            question.toggleSelected();
            if (mCurrentNode.getOption(groupPosition).anySubSelected()) {
                mCurrentNode.getOption(groupPosition).setSelected(true);
            } else {
                mCurrentNode.getOption(groupPosition).setUnselected();
            }

            if (!question.getInputType().isEmpty() && question.isSelected()) {
                if (question.getInputType().equals("camera")) {
                    if (!filePath.exists()) {
                        filePath.mkdirs();
                    }
                    imageName = UUID.randomUUID().toString();
                    Node.handleQuestion(question, QuestionNodeActivity.this, mQuestionListingadapter, filePath.toString(), imageName);
                } else {
                    Node.handleQuestion(question, QuestionNodeActivity.this, mQuestionListingadapter, null, null);
                }
            }


            if (!question.isTerminal() && question.isSelected()) {
                Node.subLevelQuestion(question, QuestionNodeActivity.this, mQuestionListingadapter, filePath.toString(), imageName);
                //If the knowledgeEngine is not terminal, that means there are more questions to be asked for this branch.
            }
        } else if ((mCurrentNode.getOption(groupPosition).getChoiceType().equals("single")) && mCurrentNode.getOption(groupPosition).anySubSelected()) {
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
            //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QuestionNodeActivity.this,R.style.AlertDialogStyle);
            alertDialogBuilder.setMessage(R.string.this_question_only_one_answer);
            alertDialogBuilder.setNeutralButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            IntelehealthApplication.setAlertDialogCustomTheme(this, alertDialog);
        } else {

            Node question = mCurrentNode.getOption(groupPosition).getOption(childPosition);
            question.toggleSelected();
            if (mCurrentNode.getOption(groupPosition).anySubSelected()) {
                mCurrentNode.getOption(groupPosition).setSelected(true);
            } else {
                mCurrentNode.getOption(groupPosition).setUnselected();
            }

            if (!mCurrentNode.findDisplay().equalsIgnoreCase("Associated Symptoms")
                    && !mCurrentNode.findDisplay().equalsIgnoreCase("जुड़े लक्षण")
                    && !mCurrentNode.findDisplay().equalsIgnoreCase("ಸಂಬಂಧಿತ ರೋಗಲಕ್ಷಣಗಳು")
                    && !mCurrentNode.findDisplay().equalsIgnoreCase("संबद्ध लक्षणे")
                    && !mCurrentNode.findDisplay().equalsIgnoreCase("ସମ୍ପର୍କିତ ଲକ୍ଷଣଗୁଡ଼ିକ")
                    && !mCurrentNode.findDisplay().equalsIgnoreCase("સંકળાયેલ લક્ષણો")
                    && !mCurrentNode.findDisplay().equalsIgnoreCase("সংশ্লিষ্ট উপসর্গ")
                    && !mCurrentNode.findDisplay().equalsIgnoreCase("সংশ্লিষ্ট লক্ষণ")) {
                //code added to handle multiple and single option selection.
                Node rootNode = mCurrentNode.getOption(groupPosition);
                if (rootNode.isMultiChoice() && !question.isExcludedFromMultiChoice()) {
                    for (int i = 0; i < rootNode.getOptionsList().size(); i++) {
                        Node childNode = rootNode.getOptionsList().get(i);
                        if (childNode.isSelected() && childNode.isExcludedFromMultiChoice()) {
                            mCurrentNode.getOption(groupPosition).getOptionsList().get(i).setUnselected();
                            mCurrentNode.getOption(groupPosition).getOptionsList().get(i).setDataCapture(false);
                        }
                    }
                }
                Log.v(TAG, "rootNode - " + new Gson().toJson(rootNode));
                if (!rootNode.isMultiChoice() || (rootNode.isMultiChoice() &&
                        question.isExcludedFromMultiChoice() && question.isSelected())) {
                    for (int i = 0; i < rootNode.getOptionsList().size(); i++) {
                        Node childNode = rootNode.getOptionsList().get(i);
                        if (!childNode.getId().equals(question.getId())) {
                            mCurrentNode.getOption(groupPosition).getOptionsList().get(i).setUnselected();
                            mCurrentNode.getOption(groupPosition).getOptionsList().get(i).setDataCapture(false);
                        }
                    }
                }
            }
            if (!question.getInputType().isEmpty() && question.isSelected()) {
                if (question.getInputType().equals("camera")) {
                    if (!filePath.exists()) {
                        filePath.mkdirs();
                    }
                    Node.handleQuestion(question, QuestionNodeActivity.this, mQuestionListingadapter, filePath.toString(), imageName);
                    return;
                } else {
                    Node.handleQuestion(question, QuestionNodeActivity.this, mQuestionListingadapter, null, null);
                    return;
                }
                //If there is an input type, then the question has a special method of data entry.
            }

            if (!question.isTerminal() && question.isSelected()) {
                Node.subLevelQuestion(question, QuestionNodeActivity.this, mQuestionListingadapter, filePath.toString(), imageName);
                //If the knowledgeEngine is not terminal, that means there are more questions to be asked for this branch.
            }
        }
        //adapter.updateNode(currentNode);
        mQuestionListingadapter.notifyDataSetChanged();

    }

    /**
     * Summarizes the information of the current complaint knowledgeEngine.
     * Then has that put into the database, and then checks to see if there are more complaint nodes.
     * If there are more, presents the user with the next set of questions.
     * All exams are also stored into a string, which will be passed through the activities to the Physical Exam Activity.
     */
    private void fabClick() {
        nodeComplete = true;

        AnswerResult answerResult = mCurrentNode.checkAllRequiredAnswered(context);
        if (!answerResult.result) {
            // show alert dialog
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
            alertDialogBuilder.setMessage(answerResult.requiredStrings);
            alertDialogBuilder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            Dialog alertDialog = alertDialogBuilder.show();
            Log.v(TAG, answerResult.requiredStrings);
            return;
        }


        if (!complaintConfirmed) {
            questionsMissing();
        } else {
            List<String> imagePathList = mCurrentNode.getImagePathList();

            if (imagePathList != null) {
                for (String imagePath : imagePathList) {
                    updateImageDatabase(imagePath);
                }
            }

            String complaintString = mCurrentNode.generateLanguage();
            String complaintString_REG = mCurrentNode.generateRegional_Language(sessionManager.getAppLanguage());
            String complaint = mCurrentNode.getText();
            String complaint_REG = "";
            if (sessionManager.getAppLanguage().equalsIgnoreCase("hi"))
                complaint_REG = mCurrentNode.getDisplay_hindi();
            else if (sessionManager.getAppLanguage().equalsIgnoreCase("bn"))
                complaint_REG = mCurrentNode.getDisplay_bengali();
            else if (sessionManager.getAppLanguage().equalsIgnoreCase("kn"))
                complaint_REG = mCurrentNode.getDisplay_kannada();
            else if (sessionManager.getAppLanguage().equalsIgnoreCase("mr"))
                complaint_REG = mCurrentNode.getDisplay_marathi();
            else if (sessionManager.getAppLanguage().equalsIgnoreCase("or"))
                complaint_REG = mCurrentNode.getDisplay_oriya();
            else if (sessionManager.getAppLanguage().equalsIgnoreCase("gu"))
                complaint_REG = mCurrentNode.getDisplay_gujarati();
            else if (sessionManager.getAppLanguage().equalsIgnoreCase("as"))
                complaint_REG = mCurrentNode.getDisplay_assamese();
            else
                complaint_REG = mCurrentNode.getDisplay();

            if (complaintString != null && !complaintString.isEmpty()) {
                insertion = insertion.concat(Node.bullet_arrow + "<b>" + complaint + "</b>" + ": " + Node.next_line + complaintString + " ");
                insertion_REG = insertion_REG.concat(Node.bullet_arrow + "<b>" + complaint_REG + "</b>" + ": " + Node.next_line + complaintString_REG + " ");
            } else {
                if (!complaint.equalsIgnoreCase(getResources().getString(R.string.associated_symptoms))) {
                    insertion = insertion.concat(Node.bullet_arrow + "<b>" + complaint + "</b>" + ": " + Node.next_line + " ");
                    insertion_REG = insertion_REG.concat(Node.bullet_arrow + "<b>" + complaint_REG + "</b>" + ": " + Node.next_line + " ");
                }
            }

            ArrayList<String> selectedAssociatedComplaintsList = mCurrentNode.getSelectedAssociations();
            if (selectedAssociatedComplaintsList != null && !selectedAssociatedComplaintsList.isEmpty()) {
                for (String associatedComplaint : selectedAssociatedComplaintsList) {
                    if (!complaints.contains(associatedComplaint)) {
                        complaints.add(associatedComplaint);
                        String fileLocation = "engines/" + associatedComplaint + ".json";
                        JSONObject currentFile = FileUtils.encodeJSON(this, fileLocation);
                        Node currentNode = new Node(currentFile);
                        complaintsNodes.add(currentNode);
                    }
                }
            }

            ArrayList<String> childNodeSelectedPhysicalExams = mCurrentNode.getPhysicalExamList();
            if (!childNodeSelectedPhysicalExams.isEmpty())
                physicalExams.addAll(childNodeSelectedPhysicalExams); //For Selected child nodes

            ArrayList<String> rootNodePhysicalExams = parseExams(mCurrentNode);
            if (rootNodePhysicalExams != null && !rootNodePhysicalExams.isEmpty())
                physicalExams.addAll(rootNodePhysicalExams); //For Root Node

            if (complaintNumber < complaints.size() - 1) {
                complaintNumber++;
                setupQuestions(complaintNumber);
                complaintConfirmed = false;
            } else if (complaints.size() >= 1 && complaintNumber == complaints.size() - 1 && !optionsList.isEmpty()) {
                complaintNumber++;
                removeDuplicateSymptoms();
                complaintConfirmed = false;
            } else {
                if (intentTag != null && intentTag.equals("edit")) {
                    Log.i(TAG, "fabClick: update" + insertion);

                    if (insertion.contains("Yes [Describe]") || insertion.contains("[Describe]") || insertion.contains("Other [Describe]")) {
                        insertion = insertion.replaceAll("Yes \\[Describe]", "")
                                .replaceAll("Other \\[Describe]", "")
                                .replaceAll("\\[Describe]", "");
                    }

                    if (insertion_REG.contains("Yes [Describe]") || insertion_REG.contains("[Describe]") || insertion_REG.contains("Other [Describe]")) {
                        insertion_REG = insertion_REG.replaceAll("Yes \\[Describe]", "")
                                .replaceAll("Other \\[Describe]", "")
                                .replaceAll("\\[Describe]", "");
                    }

                    insertion = Node.dateformate_hi_or_gu_as_en(insertion, sessionManager); // Regional to English - for doctor data
                    insertion_REG = Node.dateformat_en_hi_or_gu_as(insertion_REG, sessionManager);  // English to Regional - for HW to show in reg lang.
                    Log.v("insertion_tag", "insertion_update: " + insertion);
                    updateDatabase(insertion, UuidDictionary.CURRENT_COMPLAINT);  // updating data.

                    JSONObject object = new JSONObject();
                    try {
                        object.put("text_" + sessionManager.getAppLanguage(), insertion_REG);
                        //  object.put("text_en", insertion_REG);
                        updateDatabase(object.toString(), UuidDictionary.CC_REG_LANG_VALUE);    // updating regional data.
                        Log.v("insertion_tag", "insertion_update_regional: " + object.toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    Intent intent = new Intent(QuestionNodeActivity.this, PhysicalExamActivity.class);
                    intent.putExtra("patientUuid", patientUuid);
                    intent.putExtra("visitUuid", visitUuid);
                    intent.putExtra("encounterUuidVitals", encounterVitals);
                    intent.putExtra("encounterUuidAdultIntial", encounterAdultIntials);
                    intent.putExtra("EncounterAdultInitial_LatestVisit", EncounterAdultInitial_LatestVisit);
                    intent.putExtra("state", state);
                    intent.putExtra("name", patientName);
                    intent.putExtra("tag", intentTag);

                    Set<String> selectedExams = new LinkedHashSet<>(physicalExams);
                    sessionManager.setVisitSummary(patientUuid, selectedExams);

                    startActivity(intent);
                } else {
                    Log.i(TAG, "fabClick: " + insertion);
                    if (insertion.contains("Yes [Describe]") || insertion.contains("[Describe]") || insertion.contains("Other [Describe]")) {
                        insertion = insertion.replaceAll("Yes \\[Describe]", "")
                                .replaceAll("Other \\[Describe]", "")
                                .replaceAll("\\[Describe]", "");
                    }
                    if (insertion_REG.contains("Yes [Describe]") || insertion_REG.contains("[Describe]") || insertion_REG.contains("Other [Describe]")) {
                        insertion_REG = insertion_REG.replaceAll("Yes \\[Describe]", "")
                                .replaceAll("Other \\[Describe]", "")
                                .replaceAll("\\[Describe]", "");
                    }

                    insertion = Node.dateformate_hi_or_gu_as_en(insertion, sessionManager); // Regional to English - for doctor data
                    insertion_REG = Node.dateformat_en_hi_or_gu_as(insertion_REG, sessionManager);  // English to Regional - for HW to show in reg lang.
                    Log.v("insertion_tag", "insertion_insert: " + insertion);
                    insertDb(insertion, UuidDictionary.CURRENT_COMPLAINT);    // inserting data.

                    JSONObject object = new JSONObject();
                    try {
                        object.put("text_" + sessionManager.getAppLanguage(), insertion_REG);
                        //   object.put("text_en", insertion_REG);
                        insertDb(object.toString(), UuidDictionary.CC_REG_LANG_VALUE);    // inserting regional data.
                        Log.v("insertion_tag", "insertion_insert_regional: " + object.toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    Intent intent = new Intent
                            (QuestionNodeActivity.this, PastMedicalHistoryActivity.class);
                    intent.putExtra("patientUuid", patientUuid);
                    intent.putExtra("visitUuid", visitUuid);
                    intent.putExtra("encounterUuidVitals", encounterVitals);
                    intent.putExtra("encounterUuidAdultIntial", encounterAdultIntials);
                    intent.putExtra("EncounterAdultInitial_LatestVisit", EncounterAdultInitial_LatestVisit);
                    intent.putExtra("state", state);
                    intent.putExtra("name", patientName);
                    intent.putExtra("float_ageYear_Month", float_ageYear_Month);
                    intent.putExtra("tag", intentTag);
                    Set<String> selectedExams = new LinkedHashSet<>(physicalExams);
                    sessionManager.setVisitSummary(patientUuid, selectedExams);

                    startActivity(intent);
                }
            }
        }

        // question_recyclerView.setAdapter(adapter);

        mQuestionListingadapter.notifyDataSetChanged();
        //question_recyclerView.notifyAll();
        recyclerViewIndicator.attachToRecyclerView(question_recyclerView);

    }

    /**
     * Insert into DB could be made into a Helper Method, but isn't because there are specific concept IDs used each time.
     * Although this could also be made into a function, for now it has now been.
     *
     * @param value String to put into DB
     * @return DB Row number, never used
     */
    private boolean insertDb(String value, String conceptID) {

        Log.i(TAG, "insertDb: " + patientUuid + " " + visitUuid + " " + conceptID);
        ObsDAO obsDAO = new ObsDAO();
        ObsDTO obsDTO = new ObsDTO();
        obsDTO.setConceptuuid(conceptID);
        obsDTO.setEncounteruuid(encounterAdultIntials);
        obsDTO.setCreator(sessionManager.getCreatorID());
        obsDTO.setValue(StringUtils.getValue1(value));
        boolean isInserted = false;
        try {
            isInserted = obsDAO.insertObs(obsDTO);
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }


        return isInserted;
    }

    private void updateImageDatabase(String imagePath) {


        ImagesDAO imagesDAO = new ImagesDAO();

        try {
            imagesDAO.insertObsImageDatabase(imageName, encounterAdultIntials, "");
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    private void updateDatabase(String string, String conceptID) {
        Log.i(TAG, "updateDatabase: " + patientUuid + " " + visitUuid + " " + conceptID);

        ObsDTO obsDTO = new ObsDTO();
        ObsDAO obsDAO = new ObsDAO();
        try {
            obsDTO.setConceptuuid(conceptID);
            obsDTO.setEncounteruuid(encounterAdultIntials);
            obsDTO.setCreator(sessionManager.getCreatorID());
            obsDTO.setValue(string);
            obsDTO.setUuid(obsDAO.getObsuuid(encounterAdultIntials, conceptID));
            obsDAO.updateObs(obsDTO);
        } catch (DAOException dao) {
            FirebaseCrashlytics.getInstance().recordException(dao);
        }

        EncounterDAO encounterDAO = new EncounterDAO();
        try {
            encounterDAO.updateEncounterSync("false", encounterAdultIntials);
            encounterDAO.updateEncounterModifiedDate(encounterAdultIntials);
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    /**
     * Sets up the complaint knowledgeEngine's questions.
     *
     * @param complaintIndex Index of complaint being displayed to user.
     */
    private void setupQuestions(int complaintIndex) {
        nodeComplete = false;

        if (complaints.size() >= 1) {
            getAssociatedSymptoms(complaintIndex);
        } else {
            mCurrentNode = complaintsNodes.get(complaintIndex);
            setupUI(mCurrentNode);
        }

        mgender = PatientsDAO.fetch_gender(patientUuid);

        if (mCurrentNode != null) {
            if (mgender.equalsIgnoreCase("M")) {
                mCurrentNode.fetchItem("0");
            } else if (mgender.equalsIgnoreCase("F")) {
                mCurrentNode.fetchItem("1");
            }

            // flaoting value of age is passed to Node for comparison...
            mCurrentNode.fetchAge(float_ageYear_Month);


            mQuestionListingadapter = new QuestionsAdapter(this, mCurrentNode, question_recyclerView, this.getClass().getSimpleName(), this, false);
            question_recyclerView.setAdapter(mQuestionListingadapter);
            mQuestionListingadapter.setForNCDProtocol(mCurrentNode.getIsNcdProtocol());
            recyclerViewIndicator.attachToRecyclerView(question_recyclerView);
            setTitle(patientName + ": " + mCurrentNode.findDisplay());
            getSupportActionBar().setSubtitle(mgender + "/" + (int) float_ageYear_Month + " Yrs");
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private int mCurrentNodeIndex = 0;
    private int mTotalQuestions = 0;

    private void setupUI(Node currentNode) {
        if (currentNode != null) {
            if (currentNode.getIsNcdProtocol()) {
                recyclerViewIndicator.setVisibility(View.GONE);
                linearLayoutManager.setHorizontalScrollEnabled(false);
                navButtonRelativeLayout.setVisibility(View.VISIBLE);
                mTotalQuestions = currentNode.getOptionsList().size();
                decideToDisplayTheActionButtons();
                forwardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Node currentDisplayingNode = currentNode.getOptionsList().get(mCurrentNodeIndex);
                        if (!currentDisplayingNode.isSelected() || !currentDisplayingNode.isNestedMandatoryOptionsAnswered()) {
                            Toast.makeText(QuestionNodeActivity.this, "Please answer!", Toast.LENGTH_SHORT).show();
                        } else {

                            //mCurrentNodeIndex += 1;

                            Node pastActionNode = currentNode.getOption(mCurrentNodeIndex);
                            String popupMessage = pastActionNode.getPop_up();
                            Set<String> popSet = new HashSet<>();
                            if (popupMessage == null || popupMessage.isEmpty()) {
                                for (int i = 0; i < pastActionNode.getOptionsList().size(); i++) {
                                    Node tempNode = pastActionNode.getOptionsList().get(i);
                                    if (tempNode.isSelected() && tempNode.getPop_up() != null && !tempNode.getPop_up().isEmpty()) {
                                        popSet.add(tempNode.getPop_up());
                                    }
                                }
                            }
                            StringBuilder stringBuilder = new StringBuilder();

                            Iterator<String> setIterator = popSet.iterator();
                            while(setIterator.hasNext()){
                                if(!stringBuilder.toString().isEmpty()){
                                    stringBuilder.append("\n");
                                }
                                stringBuilder.append(setIterator.next());
                            }
                            String tempMsg = stringBuilder.toString().trim();
                            if(!tempMsg.isEmpty()){
                                popupMessage = tempMsg;
                            }

                            if (pastActionNode.isLazyPopuShow() && !popupMessage.isEmpty()) {
                                MaterialAlertDialogBuilder alertdialogBuilder = new MaterialAlertDialogBuilder(QuestionNodeActivity.this);
                                alertdialogBuilder.setMessage(popupMessage);
                                alertdialogBuilder.setPositiveButton(R.string.generic_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                //alertdialogBuilder.setNegativeButton(R.string.generic_no, null);
                                AlertDialog alertDialog = alertdialogBuilder.create();
                                alertDialog.show();
                                Button positiveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                                //Button negativeButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
                                positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                                //negativeButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                                IntelehealthApplication.setAlertDialogCustomTheme(QuestionNodeActivity.this, alertDialog);
                            }

                            NCDValidationResult ncdValidationResult = NCDNodeValidationLogic.validateAndFindNextPath(QuestionNodeActivity.this, patientUuid, currentNode, mCurrentNodeIndex, currentNode.getOption(mCurrentNodeIndex), false, null, true);
                            if (ncdValidationResult.getUpdatedNode() != null)
                                mCurrentNode = ncdValidationResult.getUpdatedNode();

                            if (ncdValidationResult.isReadyToEndTheScreening()) {
                                Toast.makeText(QuestionNodeActivity.this, "Screening done!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (ncdValidationResult.getTargetNodeID() == null && !ncdValidationResult.isReadyToEndTheScreening()) {
                                    mCurrentNodeIndex += 1;
                                    // need to check the autofill node
                                    if (mCurrentNode.getOptionsList().get(mCurrentNodeIndex).getAutoFill()) {
                                        NCDValidationResult autoFillResult = NCDNodeValidationLogic.validateAndFindNextPath(QuestionNodeActivity.this, patientUuid, currentNode, mCurrentNodeIndex, currentNode.getOption(mCurrentNodeIndex), false, null, true);
                                        mCurrentNode = autoFillResult.getUpdatedNode();
                                    }
                                    question_recyclerView.getLayoutManager().scrollToPosition(mCurrentNodeIndex);

                                    decideToDisplayTheActionButtons();
                                } else {
                                    for (int i = 0; i < mCurrentNode.getOptionsList().size(); i++) {
                                        Node tempNode = mCurrentNode.getOptionsList().get(i);
                                        if (tempNode.getId().equals(ncdValidationResult.getTargetNodeID())) {
                                            mCurrentNodeIndex = i;
                                        }
                                    }
                                    Log.v(TAG, mCurrentNode.toString());
                                    question_recyclerView.getLayoutManager().scrollToPosition(mCurrentNodeIndex);
                                    decideToDisplayTheActionButtons();
                                }
                            }
                        }
                        question_recyclerView.getAdapter().notifyItemChanged(mCurrentNodeIndex);
                    }
                });
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCurrentNodeIndex -= 1;
                        if (mCurrentNode.getOptionsList().get(mCurrentNodeIndex) != null)
                            while (mCurrentNode.getOptionsList().get(mCurrentNodeIndex).getHidden()) {
                                mCurrentNodeIndex -= 1;
                            }
                        question_recyclerView.getLayoutManager().scrollToPosition(mCurrentNodeIndex);
                        question_recyclerView.getAdapter().notifyItemChanged(mCurrentNodeIndex);
                        decideToDisplayTheActionButtons();
                    }
                });
            } else {
                recyclerViewIndicator.setVisibility(View.VISIBLE);
                linearLayoutManager.setHorizontalScrollEnabled(true);
                navButtonRelativeLayout.setVisibility(View.GONE);
            }
        }
    }

   /* private void postSubmitCheckLogic(Node rootNode, Node pastActionNode){
        NCDValidationResult ncdValidationResult = NCDNodeValidationLogic.validateAndFindNextPath(QuestionNodeActivity.this, patientUuid, rootNode, mCurrentNodeIndex, pastActionNode, false, null, true);
        if (ncdValidationResult.getUpdatedNode() != null)
            mCurrentNode = ncdValidationResult.getUpdatedNode();
        if (ncdValidationResult.isReadyToEndTheScreening()) {
            Toast.makeText(QuestionNodeActivity.this, "Screening done!", Toast.LENGTH_SHORT).show();
        } else {
            if (ncdValidationResult.getTargetNodeID() == null && !ncdValidationResult.isReadyToEndTheScreening()) {
                mCurrentNodeIndex += 1;
                // need to check the autofill node
                if (mCurrentNode.getOptionsList().get(mCurrentNodeIndex).getAutoFill()) {
                    NCDValidationResult autoFillResult = NCDNodeValidationLogic.validateAndFindNextPath(QuestionNodeActivity.this, patientUuid, rootNode, mCurrentNodeIndex, rootNode.getOption(mCurrentNodeIndex), false, null, true);
                    mCurrentNode = autoFillResult.getUpdatedNode();
                }
                question_recyclerView.getLayoutManager().scrollToPosition(mCurrentNodeIndex);

                decideToDisplayTheActionButtons();
            } else {
                for (int i = 0; i < mCurrentNode.getOptionsList().size(); i++) {
                    Node tempNode = mCurrentNode.getOptionsList().get(i);
                    if (tempNode.getId().equals(ncdValidationResult.getTargetNodeID())) {
                        mCurrentNodeIndex = i;
                    }
                }
                Log.v(TAG, mCurrentNode.toString());
                question_recyclerView.getLayoutManager().scrollToPosition(mCurrentNodeIndex);
                decideToDisplayTheActionButtons();
            }
        }
    }*/

    private void decideToDisplayTheActionButtons() {
        if (mCurrentNodeIndex == 0) {
            forwardButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.GONE);
        } else if (mCurrentNodeIndex == mTotalQuestions - 1) {
            forwardButton.setVisibility(View.GONE);
            backButton.setVisibility(View.VISIBLE);
        } else {
            forwardButton.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
        }
        fab.setVisibility(View.GONE);
    }

    private void getAssociatedSymptoms(int complaintIndex) {

        List<Node> assoComplaintsNodes = new ArrayList<>(complaintsNodes);

        if (!assoComplaintsNodes.isEmpty()) {
            for (int i = 0; i < complaintsNodes.get(complaintIndex).size(); i++) {

                if ((complaintsNodes.get(complaintIndex).getOptionsList().get(i).getText()
                        .equalsIgnoreCase("Associated symptoms"))
                        || (complaintsNodes.get(complaintIndex).getOptionsList().get(i).getText()
                        .equalsIgnoreCase("जुड़े लक्षण"))
                        || (complaintsNodes.get(complaintIndex).getOptionsList().get(i).getText()
                        .equalsIgnoreCase("ಸಂಬಂಧಿತ ರೋಗಲಕ್ಷಣಗಳು"))
                        || (complaintsNodes.get(complaintIndex).getOptionsList().get(i).getText()
                        .equalsIgnoreCase("संबद्ध लक्षणे"))
                        || (complaintsNodes.get(complaintIndex).getOptionsList().get(i).getText()
                        .equalsIgnoreCase("ସମ୍ପର୍କିତ ଲକ୍ଷଣଗୁଡ଼ିକ"))
                        || (complaintsNodes.get(complaintIndex).getOptionsList().get(i).getText()
                        .equalsIgnoreCase("સંકળાયેલ લક્ષણો"))
                        || (complaintsNodes.get(complaintIndex).getOptionsList().get(i).getText()
                        .equalsIgnoreCase("সংশ্লিষ্ট উপসর্গ"))
                        || (complaintsNodes.get(complaintIndex).getOptionsList().get(i).getText()
                        .equalsIgnoreCase("সংশ্লিষ্ট লক্ষণ"))) {

                    optionsList.addAll(complaintsNodes.get(complaintIndex).getOptionsList().get(i).getOptionsList());

                    assoComplaintsNodes.get(complaintIndex).getOptionsList().remove(i);
                    mCurrentNode = assoComplaintsNodes.get(complaintIndex);
                    //   Log.e("CurrentNode", "" + currentNode);
                    setupUI(mCurrentNode);
                } else {
                    mCurrentNode = complaintsNodes.get(complaintIndex);
                    setupUI(mCurrentNode);
                }
            }
        }
    }

    public void setRecyclerViewIndicator() {
        question_recyclerView.setAdapter(mQuestionListingadapter);
        recyclerViewIndicator.attachToRecyclerView(question_recyclerView);
    }

    private void removeDuplicateSymptoms() {

        nodeComplete = false;

        HashSet<String> hashSet = new HashSet<>();

        List<Node> finalOptionsList = new ArrayList<>(optionsList);

        if (optionsList.size() != 0) {

            for (int i = 0; i < optionsList.size(); i++) {

                if (hashSet.contains(optionsList.get(i).getText())) {

                    finalOptionsList.remove(optionsList.get(i));

                } else {
                    hashSet.add(optionsList.get(i).getText());
                }
            }

            try {
                assoSympObj.put("id", "ID_294177528");
                assoSympObj.put("text", "Associated symptoms");
                assoSympObj.put("display", "Do you have the following symptom(s)?");
                assoSympObj.put("display-hi", "क्या आपको निम्नलिखित लक्षण हैं?");
                assoSympObj.put("display-bn", "আপনার কি নিম্নলিখিত উপসর্গ(গুলি) আছে?");
                assoSympObj.put("display-kn", "ನೀವು ಈ ಕೆಳಗಿನ ರೋಗಲಕ್ಷಣಗಳನ್ನು ಹೊಂದಿದ್ದೀರಾ?");
                assoSympObj.put("display-mr", "तुम्हाला खालील लक्षण (लक्षणे) आहेत का?");
                assoSympObj.put("display-or", "ତମର ଏହି ଲକ୍ଷଣ ସବୁ ଅଛି କି?");
                assoSympObj.put("display-gu", "તમે નીચેનાં લક્ષણ(લક્ષણો) છે?");
                assoSympObj.put("display-as", "আপোনাৰ তলত দিয়া লক্ষণ(সমূহ) আছেনে?");
                assoSympObj.put("pos-condition", "c.");
                assoSympObj.put("neg-condition", "s.");
                assoSympArr.put(0, assoSympObj);
                finalAssoSympObj.put("id", "ID_844006222");
                finalAssoSympObj.put("text", "Associated symptoms");
                finalAssoSympObj.put("display-or", "ସମ୍ପର୍କିତ ଲକ୍ଷଣଗୁଡ଼ିକ");
                finalAssoSympObj.put("display-hi", "जुड़े लक्षण");
                finalAssoSympObj.put("display-bn", "সংশ্লিষ্ট উপসর্গ");
                finalAssoSympObj.put("display-kn", "ಸಂಬಂಧಿತ ರೋಗಲಕ್ಷಣಗಳು");
                finalAssoSympObj.put("display-mr", "संबद्ध लक्षणे");
                finalAssoSympObj.put("display-gu", "સંકળાયેલ લક્ષણો");
                finalAssoSympObj.put("display-as", "সংশ্লিষ্ট লক্ষণ");
                finalAssoSympObj.put("perform-physical-exam", "");
                finalAssoSympObj.put("options", assoSympArr);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            assoSympNode = new Node(finalAssoSympObj);
            assoSympNode.getOptionsList().get(0).setOptionsList(finalOptionsList);
            assoSympNode.getOptionsList().get(0).setTerminal(false);

            mCurrentNode = assoSympNode;

            mgender = PatientsDAO.fetch_gender(patientUuid);

            if (mCurrentNode != null) {
                if (mgender.equalsIgnoreCase("M")) {
                    mCurrentNode.fetchItem("0");
                } else if (mgender.equalsIgnoreCase("F")) {
                    mCurrentNode.fetchItem("1");
                }

                // flaoting value of age is passed to Node for comparison...
                mCurrentNode.fetchAge(float_ageYear_Month);

                mQuestionListingadapter = new QuestionsAdapter(this, mCurrentNode, question_recyclerView, this.getClass().getSimpleName(), this, true);
                question_recyclerView.setAdapter(mQuestionListingadapter);
                mQuestionListingadapter.setForNCDProtocol(mCurrentNode.getIsNcdProtocol());
                //setTitle(patientName + ": " + currentNode.getText());
                setTitle(patientName + ": " + mCurrentNode.findDisplay());

                getSupportActionBar().setSubtitle(mgender + "/" + (int) float_ageYear_Month + " Yrs");
            }
        }
    }

    //Dialog Alert forcing user to answer all questions.
    //Can be removed if necessary
    //TODO: Add setting to allow for all questions unrequired..addAll(Arrays.asList(splitExams))
    public void questionsMissing() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
        // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,R.style.AlertDialogStyle);

        //language ui
        SessionManager sessionManager = new SessionManager(IntelehealthApplication.getAppContext());
        String currentNodeVal = node_fetch_local_language(context, sessionManager, mCurrentNode);
        alertDialogBuilder.setMessage(Html.fromHtml(currentNodeVal));

        alertDialogBuilder.setPositiveButton(R.string.generic_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                complaintConfirmed = true;
                dialog.dismiss();
                fabClick();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.generic_back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog alertDialog = alertDialogBuilder.show();
        IntelehealthApplication.setAlertDialogCustomTheme(this, alertDialog);
        //alertDialog.show();
    }

    private ArrayList<String> parseExams(Node node) {
        ArrayList<String> examList = new ArrayList<>();
        String rawExams = node.getPhysicalExams();
        if (rawExams != null) {
            String[] splitExams = rawExams.split(";");
            examList.addAll(Arrays.asList(splitExams));
            return examList;
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Node.TAKE_IMAGE_FOR_NODE) {
            if (resultCode == RESULT_OK) {
                String mCurrentPhotoPath = data.getStringExtra("RESULT");
                mCurrentNode.setImagePath(mCurrentPhotoPath);
                mCurrentNode.displayImage(this, filePath.getAbsolutePath(), imageName);
            }
        }
    }

    @Override
    public void onBackPressed() {
    }


    public void AnimateView(View v) {

        int fadeInDuration = 500; // Configure time values here
        int timeBetween = 3000;
        int fadeOutDuration = 1000;

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(fadeInDuration);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(fadeInDuration + timeBetween);
        fadeOut.setDuration(fadeOutDuration);

        AnimationSet animation = new AnimationSet(false); // change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        animation.setRepeatCount(1);
        if (v != null) {
            v.setAnimation(animation);
        }

    }

    public void bottomUpAnimation(View v) {

        if (v != null) {
            v.setVisibility(View.VISIBLE);
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);
            v.startAnimation(bottomUp);
        }

    }

    @Override
    public void fabClickedAtEnd() {
        //currentNode = node;
        fabClick();
    }

    @Override
    public void onChildListClickEvent(int groupPos, int childPos, int physExamPos) {
        onListClicked(null, groupPos, childPos);
    }
}
