package org.intelehealth.app.activities.visit;

import static org.intelehealth.app.database.dao.VisitsDAO.allNotEndedVisits;
import static org.intelehealth.app.database.dao.VisitsDAO.thisMonths_NotEndedVisits;
import static org.intelehealth.app.database.dao.VisitsDAO.olderNotEndedVisits;
import static org.intelehealth.app.database.dao.VisitsDAO.recentNotEndedVisits;
import static org.intelehealth.app.syncModule.SyncUtils.syncNow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.intelehealth.app.R;
import org.intelehealth.app.app.AppConstants;
import org.intelehealth.app.models.PrescriptionModel;
import org.intelehealth.app.utilities.NetworkUtils;
import org.intelehealth.app.utilities.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EndVisitActivity extends AppCompatActivity implements NetworkUtils.InternetCheckUpdateInterface {
    RecyclerView recycler_recent, recycler_older, recycler_month;
    NestedScrollView nestedscrollview;
    private static SQLiteDatabase db;
    private int total_counts = 0, todays_count = 0, weeks_count = 0, months_count = 0;
    private ImageButton backArrow, refresh;
    TextView recent_nodata, older_nodata, month_nodata;
    private NetworkUtils networkUtils;
    private ObjectAnimator syncAnimator;
    private int recentLimit = 15, olderLimit = 15;
    private int recentStart = 0, recentEnd = recentStart + recentLimit;
    private boolean isRecentFullyLoaded = false;

    private int olderStart = 0, olderEnd = olderStart + olderLimit;
    private boolean isolderFullyLoaded = false;
    private List<PrescriptionModel> recentCloseVisitsList, olderCloseVisitsList;
    private EndVisitAdapter recentVisitsAdapter, olderVisitsAdapter;
    private androidx.appcompat.widget.SearchView searchview_received;
    private ImageView closeButton;
   // int totalCounts_recent = 0, totalCounts_older = 0;

    private Context context = EndVisitActivity.this;
    private RelativeLayout no_patient_found_block, main_block;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_visit);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.WHITE);
        }

        db = AppConstants.inteleHealthDatabaseHelper.getWriteDb();
        networkUtils = new NetworkUtils(this, this);
        initViews();
        endVisits_data();

        refresh.setOnClickListener(v -> {
            syncNow(EndVisitActivity.this, refresh, syncAnimator);
        });
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

    private void initViews() {
        recycler_recent = findViewById(R.id.recycler_recent);
        LinearLayoutManager reLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_recent.setLayoutManager(reLayoutManager);

        recycler_older = findViewById(R.id.recycler_older);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_older.setLayoutManager(layoutManager);

        searchview_received = findViewById(R.id.searchview_received);
        closeButton = searchview_received.findViewById(R.id.search_close_btn);
        no_patient_found_block = findViewById(R.id.no_patient_found_block);
        main_block = findViewById(R.id.main_block);

        nestedscrollview = findViewById(R.id.nestedscrollview);
        nestedscrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (v.getChildAt(v.getChildCount() - 1) != null) {
                // Scroll Down
                if (scrollY > oldScrollY) {
                    // update recent data as it will not go at very bottom of list.
                    if (recentCloseVisitsList != null && recentCloseVisitsList.size() == 0) {
                        isRecentFullyLoaded = true;
                    }
                    if (!isRecentFullyLoaded)
                        setRecentMoreDataIntoRecyclerView();

                    // Last Item Scroll Down.
                    if (scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) {
                        // update older data as it will not go at very bottom of list.
                        if (olderCloseVisitsList != null && olderCloseVisitsList.size() == 0) {
                            isolderFullyLoaded = true;
                            return;
                        }
                        if (!isolderFullyLoaded) {
                            Toast.makeText(EndVisitActivity.this, getString(R.string.loading_more), Toast.LENGTH_SHORT).show();
                            setOlderMoreDataIntoRecyclerView();
                        }
                    }
                }
            }
        });


        recycler_month = findViewById(R.id.recycler_month);
        recent_nodata = findViewById(R.id.recent_nodata);
        older_nodata = findViewById(R.id.older_nodata);
        month_nodata = findViewById(R.id.month_nodata);
        backArrow = findViewById(R.id.backArrow);
        refresh = findViewById(R.id.refresh);

        backArrow.setOnClickListener(v -> {
            finish();
        });

        // Search - start
        searchview_received.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchOperation(query);
                return false;   // setting to false will close the keyboard when clicked on search btn.
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equalsIgnoreCase("")) {
                    searchview_received.setBackground(getResources().getDrawable(R.drawable.blue_border_bg));
                } else {
                    searchview_received.setBackground(getResources().getDrawable(R.drawable.ui2_common_input_bg));
                }
                return false;
            }
        });

        closeButton.setOnClickListener(v -> {
            no_patient_found_block.setVisibility(View.GONE);
            main_block.setVisibility(View.VISIBLE);
            resetData();
            searchview_received.setQuery("", false);
        });
        // Search - end
    }

    private void resetData() {
        recent_older_visibility(recentCloseVisitsList, olderCloseVisitsList);
        Log.d("TAG", "resetData: " + recentCloseVisitsList.size() + ", " + olderCloseVisitsList.size());

        recentVisitsAdapter = new EndVisitAdapter(this, recentCloseVisitsList);
        recycler_recent.setNestedScrollingEnabled(false); // Note: use NestedScrollView in xml and in xml add nestedscrolling to false as well as in java for Recyclerview in case you are recyclerview and scrollview together.
        recycler_recent.setAdapter(recentVisitsAdapter);

        olderVisitsAdapter = new EndVisitAdapter(this, olderCloseVisitsList);
        recycler_older.setNestedScrollingEnabled(false);
        recycler_older.setAdapter(olderVisitsAdapter);
    }

    private void endVisits_data() {
      //  initLimits();
        recentCloseVisits();
        olderCloseVisits();
    }

    private void initLimits() {
        recentLimit = 15;
        olderLimit = 15;
        recentStart = 0;
        recentEnd = recentStart + recentLimit;
        olderStart = 0;
        olderEnd = olderStart + olderLimit;
    }

    private void recentCloseVisits() {
        recentCloseVisitsList = recentNotEndedVisits(recentLimit, recentStart);
        recentVisitsAdapter = new EndVisitAdapter(this, recentCloseVisitsList);
        recycler_recent.setNestedScrollingEnabled(false); // Note: use NestedScrollView in xml and in xml add nestedscrolling to false as well as in java for Recyclerview in case you are recyclerview and scrollview together.
        recycler_recent.setAdapter(recentVisitsAdapter);

        recentStart = recentEnd;
        recentEnd += recentLimit;

        todays_count = recentCloseVisitsList.size();
        if (todays_count == 0 || todays_count < 0)
            recent_nodata.setVisibility(View.VISIBLE);
        else
            recent_nodata.setVisibility(View.GONE);
    }

    private void olderCloseVisits() {
        olderCloseVisitsList = olderNotEndedVisits(olderLimit, olderStart);
        olderVisitsAdapter = new EndVisitAdapter(this, olderCloseVisitsList);
        recycler_older.setNestedScrollingEnabled(false);
        recycler_older.setAdapter(olderVisitsAdapter);

        olderStart = olderEnd;
        olderEnd += olderLimit;

        weeks_count = olderCloseVisitsList.size();
        if (weeks_count == 0 || weeks_count < 0)
            older_nodata.setVisibility(View.VISIBLE);
        else
            older_nodata.setVisibility(View.GONE);
    }

    // This method will be accessed every time the person scrolls the recyclerView further.
    private void setRecentMoreDataIntoRecyclerView() {
        if (recentCloseVisitsList != null && recentCloseVisitsList.size() == 0) {
            isRecentFullyLoaded = true;
            return;
        }

        recentCloseVisitsList = recentNotEndedVisits(recentLimit, recentStart); // for n iteration limit be fixed == 15 and start - offset will keep skipping each records.
        Log.d("TAG", "setRecentMoreDataIntoRecyclerView: " + recentCloseVisitsList.size());
        recentVisitsAdapter.arrayList.addAll(recentCloseVisitsList);
        recentVisitsAdapter.notifyDataSetChanged();
        recentStart = recentEnd;
        recentEnd += recentLimit;
    }

    private void setOlderMoreDataIntoRecyclerView() {
        if (olderCloseVisitsList != null && olderCloseVisitsList.size() == 0) {
            isolderFullyLoaded = true;
            return;
        }

        olderCloseVisitsList = olderNotEndedVisits(olderLimit, olderStart); // for n iteration limit be fixed == 15 and start - offset will keep skipping each records.
        Log.d("TAG", "setOlderMoreDataIntoRecyclerView: " + olderCloseVisitsList.size());
        olderVisitsAdapter.arrayList.addAll(olderCloseVisitsList);
        olderVisitsAdapter.notifyDataSetChanged();
        olderStart = olderEnd;
        olderEnd += olderLimit;
    }

    private void thisMonths_EndVisits() {
        List<PrescriptionModel> arrayList = thisMonths_NotEndedVisits();
        EndVisitAdapter adapter_new = new EndVisitAdapter(this, arrayList);
        recycler_month.setNestedScrollingEnabled(false);
        recycler_month.setAdapter(adapter_new);
        months_count = arrayList.size();
        if (months_count == 0 || months_count < 0)
            month_nodata.setVisibility(View.VISIBLE);
        else
            month_nodata.setVisibility(View.GONE);
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

    @Override
    public void onStart() {
        super.onStart();
        //register receiver for internet check
        networkUtils.callBroadcastReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            //unregister receiver for internet check
            networkUtils.unregisterNetworkReceiver();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


  /*  @Override
    public int getTotalCounts() {
        total_counts = todays_count + weeks_count + months_count;
        return total_counts;
    }*/

    private void searchOperation(String query) {
        Log.v("Search", "Search Word: " + query);
        query = query.toLowerCase().trim();
        query = query.replaceAll(" {2}", " ");
        Log.d("TAG", "searchOperation: " + query);

        List<PrescriptionModel> recent = new ArrayList<>();
        List<PrescriptionModel> older = new ArrayList<>();

        String finalQuery = query;
        new Thread(new Runnable() {
            @Override
            public void run() {
              //  List<PrescriptionModel> allCloseList = allNotEndedVisits();
                List<PrescriptionModel> allRecentList = recentNotEndedVisits();
                List<PrescriptionModel> allOlderList = olderNotEndedVisits();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!finalQuery.isEmpty()) {
                            // recent- start
                            recent.clear();
                            older.clear();

                            if (allRecentList.size() > 0) {
                                for (PrescriptionModel model : allRecentList) {
                                    String firstName = model.getFirst_name().toLowerCase();
                                    String lastName = model.getLast_name().toLowerCase();
                                    String fullName = firstName + " " + lastName;

                                    if (firstName.contains(finalQuery) || lastName.contains(finalQuery) || fullName.equalsIgnoreCase(finalQuery)) {
                                        recent.add(model);
                                    } else {
                                        // dont add in list value.
                                    }
                                }
                            }

                            if (allOlderList.size() > 0) {
                                for (PrescriptionModel model : allOlderList) {
                                    String firstName = model.getFirst_name().toLowerCase();
                                    String lastName = model.getLast_name().toLowerCase();
                                    String fullName = firstName + " " + lastName;

                                    if (firstName.contains(finalQuery) || lastName.contains(finalQuery) || fullName.equalsIgnoreCase(finalQuery)) {
                                        older.add(model);
                                    } else {
                                        // dont add in list value.
                                    }
                                }
                            }

                            recentVisitsAdapter = new EndVisitAdapter(context, recent);
                            recycler_recent.setNestedScrollingEnabled(false);
                            recycler_recent.setAdapter(recentVisitsAdapter);

                            olderVisitsAdapter = new EndVisitAdapter(context, older);
                            recycler_older.setNestedScrollingEnabled(false);
                            recycler_older.setAdapter(olderVisitsAdapter);

                            /**
                             * Checking here the query that is entered and it is not empty so check the size of all of these
                             * arraylists; if there size is 0 than show the no patient found view.
                             */
                            int allCount = recent.size() + older.size();
                            allCountVisibility(allCount);
                            recent_older_visibility(recent, older);
                        }
                    }
                });
            }
        }).start();

    }

    private void recent_older_visibility(List<PrescriptionModel> recent, List<PrescriptionModel> older) {
        if (recent.size() == 0 || recent.size() < 0)
            recent_nodata.setVisibility(View.VISIBLE);
        else
            recent_nodata.setVisibility(View.GONE);

        if (older.size() == 0 || older.size() < 0)
            older_nodata.setVisibility(View.VISIBLE);
        else
            older_nodata.setVisibility(View.GONE);
    }

    private void allCountVisibility(int allCount) {
        if (allCount == 0 || allCount < 0) {
            no_patient_found_block.setVisibility(View.VISIBLE);
            main_block.setVisibility(View.GONE);
        } else {
            no_patient_found_block.setVisibility(View.GONE);
            main_block.setVisibility(View.VISIBLE);
        }
    }
}