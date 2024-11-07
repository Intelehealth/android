package org.intelehealth.videolibrary.listing.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.intelehealth.videolibrary.constants.Constants
import org.intelehealth.videolibrary.databinding.FragmentVideoCategoryBinding
import org.intelehealth.videolibrary.listing.viewmodel.videos.VideoViewModelFactory
import org.intelehealth.videolibrary.listing.viewmodel.videos.YoutubeVideoViewModel
import org.intelehealth.videolibrary.restapi.RetrofitProvider
import org.intelehealth.videolibrary.room.VideoLibraryDatabase

class VideoFragment : Fragment() {

    private var binding: FragmentVideoCategoryBinding? = null
    private var viewModel: YoutubeVideoViewModel? = null

    private var categoryId: Int? = null
    private var categoryName: String? = null

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

    private fun initializeData() {
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
}