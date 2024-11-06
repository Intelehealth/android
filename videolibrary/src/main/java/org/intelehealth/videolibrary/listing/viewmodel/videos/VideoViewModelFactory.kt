package org.intelehealth.videolibrary.listing.viewmodel.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.intelehealth.videolibrary.restapi.VideoLibraryApiClient
import org.intelehealth.videolibrary.room.dao.CategoryDao
import org.intelehealth.videolibrary.room.dao.VideoDao

/**
 * Created by Arpan Sircar. on 08-02-2024.
 * Email : arpan@intelehealth.org
 * Mob   : +919123116015
 **/

@Suppress("UNCHECKED_CAST")
class VideoViewModelFactory(
    private val service: VideoLibraryApiClient,
    private val dao: VideoDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return YoutubeVideoViewModel(service, dao) as T
    }
}