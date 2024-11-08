package org.intelehealth.videolibrary.listing.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.EndIconMode
import org.intelehealth.videolibrary.R
import org.intelehealth.videolibrary.callbacks.VideoClickedListener
import org.intelehealth.videolibrary.constants.Constants
import org.intelehealth.videolibrary.data.PreferenceHelper
import org.intelehealth.videolibrary.databinding.FragmentVideoCategoryBinding
import org.intelehealth.videolibrary.listing.activity.checkAndHideProgressBar
import org.intelehealth.videolibrary.listing.activity.emojiFilter
import org.intelehealth.videolibrary.listing.activity.hideKeyboard
import org.intelehealth.videolibrary.listing.adapter.YoutubeListingAdapter
import org.intelehealth.videolibrary.listing.viewmodel.videos.VideoViewModelFactory
import org.intelehealth.videolibrary.listing.viewmodel.videos.YoutubeVideoViewModel
import org.intelehealth.videolibrary.model.Video
import org.intelehealth.videolibrary.player.activity.VideoPlayerActivity
import org.intelehealth.videolibrary.restapi.RetrofitProvider
import org.intelehealth.videolibrary.room.VideoLibraryDatabase

class VideoFragment : Fragment(), VideoClickedListener {

    private var binding: FragmentVideoCategoryBinding? = null
    private val viewModel: YoutubeVideoViewModel by lazy {
        val helper = PreferenceHelper(requireActivity())
        auth = "Bearer ${helper.getJwtAuthToken()}"

        val database = VideoLibraryDatabase.getInstance(requireContext().applicationContext)
        val dao = database.videoDao()
        val service = RetrofitProvider.apiService

        ViewModelProvider(
            owner = this@VideoFragment, factory = VideoViewModelFactory(
                service = service, dao = dao
            )
        )[YoutubeVideoViewModel::class.java]
    }

    private var auth: String? = null
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var videoList: List<Video>? = null
    private var isCallToServer: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromBundle(arguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoCategoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setListeners()
        fetchVideosFromDb()
    }

    private fun setListeners() {
        binding?.tvFindVideos?.filters = arrayOf(emojiFilter)

        binding?.tilFindVideos?.endIconMode = TextInputLayout.END_ICON_NONE

        binding?.tvFindVideos?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(v.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding?.tvFindVideos?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No-op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    binding?.tilFindVideos?.endIconMode = TextInputLayout.END_ICON_NONE
                    binding?.tilFindVideos?.setEndIconOnClickListener(null)
                } else {
                    binding?.tilFindVideos?.setEndIconOnClickListener(null)
                    binding?.tilFindVideos?.endIconMode = TextInputLayout.END_ICON_CUSTOM
                    binding?.tilFindVideos?.endIconDrawable = ContextCompat.getDrawable(
                        context!!,
                        R.drawable.ic_clear
                    )

                    binding?.tilFindVideos?.setEndIconOnClickListener {
                        binding?.tvFindVideos?.text?.clear()
                        videoList?.let { it1 -> initializeRecyclerView(it1) }
                        hideKeyboard(requireActivity())
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No-op
            }
        })


    }

    private fun performSearch(searchString: String) {
        val tempList = mutableListOf<Video>()

        for (video in videoList!!) {
            if (video.title.contains(searchString, ignoreCase = true)) {
                tempList.add(video)
            }
        }

        initializeRecyclerView(tempList)
    }

    private fun setObservers() {
        viewModel.tokenExpiredObserver.observe(viewLifecycleOwner) {
            if (it) {
                requireActivity().apply {
                    setResult(Constants.JWT_TOKEN_EXPIRED)
                    finish()
                }
            }
        }

        viewModel.emptyListObserver.observe(viewLifecycleOwner) {
            // Hiding the progress bars here is being handled by extension functions
            binding?.progressBar?.checkAndHideProgressBar()
        }

        categoryId?.let {
            viewModel.fetchVideosFromDb(it).observe(viewLifecycleOwner) { list ->
                Log.d("LiveData: ", "Inside Observer")
                if (list.isEmpty() && !isCallToServer) {
                    isCallToServer = false
                    binding?.progressBar?.visibility = View.VISIBLE
                    fetchVideosFromServer()
                    return@observe
                }

                if (viewModel.areListsSame(videoList, list)) {
                    binding?.progressBar?.checkAndHideProgressBar()
                    return@observe
                }

                videoList = list
                initializeRecyclerView(list)

                binding?.progressBar?.checkAndHideProgressBar()
            }
        }
    }

    private fun initializeRecyclerView(videos: List<Video>) {
        val adapter = YoutubeListingAdapter(
            videoIds = videos, lifecycle = lifecycle, listener = this@VideoFragment
        )

        binding?.rvVideos?.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun fetchVideosFromServer() {
        isCallToServer = true
        viewModel.fetchCategoryVideosFromServer(
            auth = auth!!, categoryId = categoryId?.toString()!!
        )
    }

    private fun getDataFromBundle(bundle: Bundle?) {
        categoryId = bundle?.getInt(Constants.CATEGORY_ID_BUNDLE_ARGUMENT)
        categoryName = bundle?.getString(Constants.CATEGORY_NAME_BUNDLE_ARGUMENT)
    }

    private fun fetchVideosFromDb() {
        binding?.progressBar?.visibility = View.VISIBLE
        categoryId?.let { viewModel.fetchVideosFromDb(it) }
    }

    override fun onVideoClicked(videoId: String) {
        val intent = Intent(requireActivity(), VideoPlayerActivity::class.java).also {
            it.putExtra(Constants.VIDEO_ID, videoId)
        }
        startActivity(intent)
    }
}