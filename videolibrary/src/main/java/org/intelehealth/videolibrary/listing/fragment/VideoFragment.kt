package org.intelehealth.videolibrary.listing.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.intelehealth.videolibrary.R
import org.intelehealth.videolibrary.callbacks.VideoClickedListener
import org.intelehealth.videolibrary.constants.Constants
import org.intelehealth.videolibrary.data.PreferenceHelper
import org.intelehealth.videolibrary.databinding.FragmentVideoCategoryBinding
import org.intelehealth.videolibrary.listing.activity.checkAndHideProgressBar
import org.intelehealth.videolibrary.listing.adapter.YoutubeListingAdapter
import org.intelehealth.videolibrary.listing.viewmodel.videos.VideoViewModelFactory
import org.intelehealth.videolibrary.listing.viewmodel.videos.YoutubeVideoViewModel
import org.intelehealth.videolibrary.model.Video
import org.intelehealth.videolibrary.player.activity.VideoPlayerActivity
import org.intelehealth.videolibrary.restapi.RetrofitProvider
import org.intelehealth.videolibrary.room.VideoLibraryDatabase

class VideoFragment : Fragment(), VideoClickedListener {

    private var binding: FragmentVideoCategoryBinding? = null
    private var viewModel: YoutubeVideoViewModel? = null

    private var auth: String? = null
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var videoList: List<Video>? = null
    private var isCallToServer: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeData()
        getDataFromBundle(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoCategoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        fetchVideosFromDb()
    }

    private fun setObservers() {
        viewModel?.tokenExpiredObserver?.observe(requireActivity()) {
            if (it) {
                requireActivity().apply {
                    setResult(Constants.JWT_TOKEN_EXPIRED)
                    finish()
                }
            }
        }

        viewModel?.emptyListObserver?.observe(requireActivity()) {
            if (it) {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.no_videos_found_on_server_for_category, categoryName),
                    Toast.LENGTH_LONG
                ).show()

                // Hiding the progress bars here is being handled by extension functions
                binding?.progressBar?.checkAndHideProgressBar()
            }
        }

        categoryId?.let { categoryId ->
            viewModel?.fetchVideosFromDb(categoryId)?.observe(requireActivity()) {
                if (it.isEmpty() && !isCallToServer) {
                    isCallToServer = false
                    binding?.progressBar?.visibility = View.VISIBLE
                    fetchVideosFromServer()
                    return@observe
                }

                if (viewModel?.areListsSame(videoList, it) == true) {
                    binding?.progressBar?.checkAndHideProgressBar()
                    return@observe
                }

                videoList = it
                initializeRecyclerView(it)

                binding?.progressBar?.checkAndHideProgressBar()
            }
        }
    }

    private fun initializeRecyclerView(videos: List<Video>) {
        val adapter = YoutubeListingAdapter(
            videoIds = videos,
            lifecycle = lifecycle,
            listener = this@VideoFragment
        )

        binding?.rvVideos?.apply {
            this.adapter = adapter
            this.addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun fetchVideosFromServer() {
        isCallToServer = true
        viewModel?.fetchCategoryVideosFromServer(
            auth = auth!!,
            categoryId = categoryId?.toString()!!
        )
    }

    private fun initializeData() {
        val helper = PreferenceHelper(requireActivity())
        auth = "Bearer ${helper.getJwtAuthToken()}"

        val database = VideoLibraryDatabase.getInstance(requireContext().applicationContext)
        val dao = database.videoDao()
        val service = RetrofitProvider.apiService

        viewModel = ViewModelProvider(
            owner = this@VideoFragment,
            factory = VideoViewModelFactory(
                service = service,
                dao = dao
            )
        )[YoutubeVideoViewModel::class.java]
    }

    private fun getDataFromBundle(bundle: Bundle?) {
        categoryId = bundle?.getInt(Constants.CATEGORY_ID_BUNDLE_ARGUMENT)
        categoryName = bundle?.getString(Constants.CATEGORY_NAME_BUNDLE_ARGUMENT)
    }

    private fun fetchVideosFromDb() {
        binding?.progressBar?.visibility = View.VISIBLE
        categoryId?.let { viewModel?.fetchVideosFromDb(it) }
    }

    override fun onVideoClicked(videoId: String) {
        val intent = Intent(requireActivity(), VideoPlayerActivity::class.java).also {
            it.putExtra(Constants.VIDEO_ID, videoId)
        }
        startActivity(intent)
    }
}