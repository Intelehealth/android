package org.intelehealth.videolibrary.listing.viewmodel.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.intelehealth.videolibrary.restapi.VideoLibraryApiClient
import org.intelehealth.videolibrary.room.dao.CategoryDao

/**
 * Created by Arpan Sircar. on 08-02-2024.
 * Email : arpan@intelehealth.org
 * Mob   : +919123116015
 **/

@Suppress("UNCHECKED_CAST")
class CategoryViewModelFactory(
    private val service: VideoLibraryApiClient,
    private val categoryDao: CategoryDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return YoutubeCategoryViewModel(service, categoryDao) as T
    }
}