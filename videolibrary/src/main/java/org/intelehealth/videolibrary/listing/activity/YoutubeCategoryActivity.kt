package org.intelehealth.videolibrary.listing.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import org.intelehealth.videolibrary.R
import org.intelehealth.videolibrary.callbacks.VideoClickedListener
import org.intelehealth.videolibrary.constants.Constants
import org.intelehealth.videolibrary.data.PreferenceHelper
import org.intelehealth.videolibrary.databinding.ActivityYoutubeListingBinding
import org.intelehealth.videolibrary.listing.adapter.CategoryFragmentAdapter
import org.intelehealth.videolibrary.listing.viewmodel.category.CategoryViewModelFactory
import org.intelehealth.videolibrary.listing.viewmodel.category.YoutubeCategoryViewModel
import org.intelehealth.videolibrary.model.Category
import org.intelehealth.videolibrary.player.activity.VideoPlayerActivity
import org.intelehealth.videolibrary.restapi.RetrofitProvider
import org.intelehealth.videolibrary.restapi.VideoLibraryApiClient
import org.intelehealth.videolibrary.room.VideoLibraryDatabase
import org.intelehealth.videolibrary.room.dao.CategoryDao

/**
 * Created by Arpan Sircar. on 08-02-2024.
 * Email : arpan@intelehealth.org
 * Mob   : +919123116015
 **/

class YoutubeCategoryActivity : AppCompatActivity(), VideoClickedListener {

    private var binding: ActivityYoutubeListingBinding? = null
    private var preferenceHelper: PreferenceHelper? = null
    private var viewmodel: YoutubeCategoryViewModel? = null

    private var authKey: String? = null
    private var packageName: String? = null
    private var categoryList: List<Category>? = null
    private var isCallToServer: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYoutubeListingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initializeData()
        setObservers()
        setVideoLibraryRecyclerView()
    }

    private fun setObservers() {

        // used for fetching data from the db
        viewmodel?.fetchCategoriesFromDb()?.observe(this) {

            // to fetch the videos from the server in case the db is empty
            if (it.isEmpty() && !isCallToServer) {
                isCallToServer = false
                binding?.progressBar?.visibility = View.VISIBLE
                fetchVideosFromServer()
                return@observe
            }

            // to check if the same videos are being set or not
            // this is to prevent flickering issue as Flows are constantly updating our listing
            if (viewmodel?.areListsSame(categoryList, it) == true) {
                binding?.progressBar?.checkAndHideProgressBar()
                return@observe
            }

            // caching the list of videos fetched to maintain a record and prevent constant updates
            categoryList = it
            initializeViewPager(it)

            // Hiding the progress bars here is being handled by extension functions
            binding?.progressBar?.checkAndHideProgressBar()
        }

        // used for detecting if the JWT token is expired
        viewmodel?.tokenExpiredObserver?.observe(this) {
            if (it) {
                setResult(Constants.JWT_TOKEN_EXPIRED)
                finish()
            }
        }

        // used for handling scenarios where there are no videos from the server
        viewmodel?.emptyListObserver?.observe(this) {
            if (it) {
                Toast.makeText(
                    this@YoutubeCategoryActivity,
                    getString(R.string.no_videos_found_on_server),
                    Toast.LENGTH_LONG
                ).show()

                // Hiding the progress bars here is being handled by extension functions
                binding?.progressBar?.checkAndHideProgressBar()
            }
        }
    }

    private fun initializeViewPager(categoryList: List<Category>) {
        val adapter = CategoryFragmentAdapter(
            categoryList = categoryList,
            lifecycle = lifecycle,
            manager = supportFragmentManager
        )

        binding?.vpVideos?.adapter = adapter
        TabLayoutMediator(binding?.tlVideos!!, binding?.vpVideos!!) { tab, position ->
            tab.text = categoryList[position].name
        }.attach()
    }

    private fun initializeData() {
        preferenceHelper = PreferenceHelper(applicationContext)
        authKey = "Bearer ${preferenceHelper?.getJwtAuthToken()}"
        packageName = applicationContext.packageName

        val service: VideoLibraryApiClient = RetrofitProvider.apiService
        val categoryDao: CategoryDao =
            VideoLibraryDatabase.getInstance(this@YoutubeCategoryActivity).categoryDao()

        viewmodel = ViewModelProvider(
            owner = this@YoutubeCategoryActivity, factory = CategoryViewModelFactory(
                service = service,
                categoryDao = categoryDao
            )
        )[YoutubeCategoryViewModel::class.java]

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE
    }

    private fun fetchVideosFromServer() {
        isCallToServer = true
        viewmodel?.fetchCategoriesFromServer(authKey!!)
    }

    private fun setVideoLibraryRecyclerView() {
        binding?.progressBar?.visibility = View.VISIBLE
        viewmodel?.fetchCategoriesFromDb()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId: Int = item.itemId
        if (itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onVideoClicked(videoId: String) {
        val intent = Intent(this@YoutubeCategoryActivity, VideoPlayerActivity::class.java).also {
            it.putExtra(Constants.VIDEO_ID, videoId)
        }
        startActivity(intent)
    }
}