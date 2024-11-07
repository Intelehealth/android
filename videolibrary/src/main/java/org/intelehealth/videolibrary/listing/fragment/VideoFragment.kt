package org.intelehealth.videolibrary.listing.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.intelehealth.videolibrary.databinding.FragmentVideoCategoryBinding
import org.intelehealth.videolibrary.listing.viewmodel.videos.VideoViewModelFactory
import org.intelehealth.videolibrary.listing.viewmodel.videos.YoutubeVideoViewModel
import org.intelehealth.videolibrary.restapi.RetrofitProvider
import org.intelehealth.videolibrary.room.VideoLibraryDatabase

class VideoFragment : Fragment() {

    private var binding: FragmentVideoCategoryBinding? = null
    private var viewModel: YoutubeVideoViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoCategoryBinding.inflate(inflater, container, false)
        return binding?.root
    }
}