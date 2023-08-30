package org.intelehealth.app.activities.help.activities;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.intelehealth.app.R;
import org.intelehealth.app.activities.help.adapter.FAQExpandableAdapter;
import org.intelehealth.app.activities.help.adapter.MostSearchedVideosAdapter_New;
import org.intelehealth.app.activities.help.models.QuestionModel;
import org.intelehealth.app.activities.help.models.YoutubeVideoList;
import org.intelehealth.app.syncModule.SyncUtils;
import org.intelehealth.app.utilities.NetworkUtils;
import org.intelehealth.app.utilities.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HelpFragment_New extends Fragment implements View.OnClickListener, NetworkUtils.InternetCheckUpdateInterface {
    private static final String TAG = "HelpFragment";
    View view;
    ImageView ivInternet;
    private ObjectAnimator syncAnimator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help_ui2, container, false);
        setLocale(getContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setLocale(getContext());
        initUI();

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

    private void initUI() {
        BottomNavigationView bottomNav = getActivity().findViewById(R.id.bnvHomeScreen);
        bottomNav.getMenu().findItem(R.id.bottomNavHelp).setChecked(true);
        bottomNav.setVisibility(View.VISIBLE);
        View optionsView = view.findViewById(R.id.categoriesButtonsLayoutHelpScreen);
        TextView btnAll = optionsView.findViewById(R.id.tvAllCategoryFAQHelp);
        RecyclerView rvSearchedVideos = view.findViewById(R.id.rvMostSearchedHelpScreen);
        RecyclerView rvFaq = view.findViewById(R.id.rvFAQHelpScreen);
        TextView tvMoreVideos = view.findViewById(R.id.tvMostSearchedMoreHelpScreen);
        TextView tvMoreFaq = view.findViewById(R.id.tvFAQMoreHelpScreen);
        FloatingActionButton fabHelp = view.findViewById(R.id.fabChatHelpScreen);
        ivInternet = view.findViewById(R.id.ivInternetHelpScreen);
        ivInternet.setOnClickListener(v -> SyncUtils.syncNow(requireActivity(), ivInternet, syncAnimator));

        fabHelp.setOnClickListener(v -> {
            //Intent intent = new Intent(getActivity(), ChatSupportHelpActivity_New.class);
            //startActivity(intent);

            String phoneNumber = getString(R.string.support_mobile_no_1);
            String message = String.format(getString(R.string.help_whatsapp_string), new SessionManager(getActivity()).getChwname());
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                            String.format("https://api.whatsapp.com/send?phone=%s&text=%s",
                                    phoneNumber, message))));
        });

        tvMoreVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MostSearchedVideosActivity_New.class);
                startActivity(intent);
            }
        });
        tvMoreFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FAQActivity_New.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvSearchedVideos.setLayoutManager(layoutManager);
        MostSearchedVideosAdapter_New mostSearchedVideosAdapter_new = new MostSearchedVideosAdapter_New(getActivity(), getVideoList());
        rvSearchedVideos.setAdapter(mostSearchedVideosAdapter_new);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvFaq.setLayoutManager(linearLayoutManager);
        FAQExpandableAdapter faqExpandableAdapter = new FAQExpandableAdapter(getActivity(), getQuestionsList());
        rvFaq.setAdapter(faqExpandableAdapter);

        btnAll.setOnClickListener(this);

    }

    public List<YoutubeVideoList> getVideoList()
    {
        String[] namesArr = {"<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/TqNiRWOBNTs\" frameborder=\"0\" allowfullscreen></iframe>",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/LCG6eJ0j-Cg\" frameborder=\"0\" allowfullscreen></iframe>",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/qbDHSwMOYg4\" frameborder=\"0\" allowfullscreen></iframe>",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/E0UAHVoqcm0\" frameborder=\"0\" allowfullscreen></iframe>"};
        String[] descArr = { getResources().getString(R.string.treat_mild_fever), getResources().getString(R.string.what_is_anemia),getResources().getString(R.string.treat_cough_at_home),getResources().getString(R.string.benefits_of_walking)};


        List<YoutubeVideoList> videoList = new ArrayList<>();
        for (int i = 0; i < namesArr.length; i++) {
            YoutubeVideoList youtubeVideoList = new YoutubeVideoList(namesArr[i], descArr[i]);
            videoList.add(youtubeVideoList);
        }

        return videoList;
    }

    public List<QuestionModel> getQuestionsList() {
        String[] namesArr = {getResources().getString(R.string.how_intelehealth_work),getResources().getString(R.string.why_intelehealth_exist), getResources().getString(R.string.how_intelehealth_help), getResources().getString(R.string.how_to_register),
                getResources().getString(R.string.how_to_add_new_visit), getResources().getString(R.string.how_to_book_an_appointment)};
        String[] descArr = {getResources().getString(R.string.how_intelehealth_work_ans), getResources().getString(R.string.why_intelehealth_exist_ans), getResources().getString(R.string.how_intelehealth_help_ans),
                getResources().getString(R.string.how_to_register_ans), getResources().getString(R.string.how_to_add_new_visit_ans), getResources().getString(R.string.how_to_book_an_appointment_ans)};


        List<QuestionModel> questionsList = new ArrayList<>();
        for (int i = 0; i < namesArr.length; i++) {
            QuestionModel questionModel = new QuestionModel(namesArr[i], descArr[i]);
            questionsList.add(questionModel);
        }

        return questionsList;

    }

    @Override
    public void updateUIForInternetAvailability(boolean isInternetAvailable) {
        Log.d(TAG, "updateUIForInternetAvailability: ");
        if (isInternetAvailable) {
            ivInternet.setImageDrawable(getResources().getDrawable(R.drawable.ui2_ic_internet_available));

        } else {
            ivInternet.setImageDrawable(getResources().getDrawable(R.drawable.ui2_ic_no_internet));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAllCategoryFAQHelp:
                break;
        }
    }
}