package org.intelehealth.videolibrary.listing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.intelehealth.videolibrary.listing.data.ListingDataSource
import org.intelehealth.videolibrary.listing.data.ListingRepository
import org.intelehealth.videolibrary.model.Video
import org.intelehealth.videolibrary.restapi.VideoLibraryApiClient
import org.intelehealth.videolibrary.restapi.response.categories.MainCategoryResponse
import org.intelehealth.videolibrary.restapi.response.videos.MainVideoResponse
import org.intelehealth.videolibrary.room.dao.CategoryDao
import org.intelehealth.videolibrary.room.dao.LibraryDao
import org.intelehealth.videolibrary.utils.ResponseChecker
import retrofit2.Response
import kotlin.math.truncate

/**
 * Created by Arpan Sircar. on 08-02-2024.
 * Email : arpan@intelehealth.org
 * Mob   : +919123116015
 **/

class YoutubeListingViewModel(
    service: VideoLibraryApiClient,
    libraryDao: LibraryDao,
    categoryDao: CategoryDao
) : ViewModel() {

    private var repository: ListingRepository

    private var _tokenExpiredObserver: MutableLiveData<Boolean> = MutableLiveData(false)
    var tokenExpiredObserver: LiveData<Boolean> = _tokenExpiredObserver

    private var _emptyListObserver: MutableLiveData<Boolean> = MutableLiveData(false)
    var emptyListObserver: LiveData<Boolean> = _emptyListObserver

    init {
        val dataSource = ListingDataSource(service, libraryDao, categoryDao)
        repository = ListingRepository(dataSource)
    }

    fun fetchCategoriesFromServer(auth: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository
                .fetchAllCategoriesFromServer(auth)
                .collect { response ->
                    handleCategoryResponse(response)
                }
        }
    }

    private fun handleCategoryResponse(response: Response<MainCategoryResponse>) {
        val responseChecker = ResponseChecker(response)
        if (responseChecker.isNotAuthorized) {
            _tokenExpiredObserver.postValue(true)
        } else {

        }
    }

    fun fetchVideosFromDb(categoryId: Int) = repository.fetchVideosFromDb(categoryId).asLiveData()

    fun areListsSame(list1: List<Video>?, list2: List<Video>?) = list1 == list2

    fun deleteAllVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }
}