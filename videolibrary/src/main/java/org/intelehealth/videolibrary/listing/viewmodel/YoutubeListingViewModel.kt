package org.intelehealth.videolibrary.listing.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.intelehealth.videolibrary.listing.data.ListingDataSource
import org.intelehealth.videolibrary.listing.data.ListingRepository
import org.intelehealth.videolibrary.restapi.VideoLibraryApiClient
import org.intelehealth.videolibrary.restapi.response.VideoLibraryResponse
import org.intelehealth.videolibrary.room.dao.LibraryDao
import org.intelehealth.videolibrary.utils.ResponseChecker
import retrofit2.Response

class YoutubeListingViewModel(service: VideoLibraryApiClient, dao: LibraryDao) : ViewModel() {

    private var repository: ListingRepository

    private var _tokenExpiredObserver: MutableLiveData<Boolean> = MutableLiveData(false)
    var tokenExpiredObserver: LiveData<Boolean> = _tokenExpiredObserver

    init {
        val dataSource = ListingDataSource(service, dao)
        repository = ListingRepository(dataSource)
    }

    fun fetchVideos(packageName: String, auth: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchVideos(packageName, auth)
                .collect { response ->
                    handleResponses(response)
                }
        }
    }

    private fun handleResponses(response: Response<VideoLibraryResponse?>) {
        val responseChecker = ResponseChecker(response)
        if (responseChecker.isNotAuthorized) {
            _tokenExpiredObserver.postValue(true)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                response.body()?.projectLibraryData?.videos?.let {
                    repository.insertVideos(it)
                }
            }
        }
    }

    fun fetchVideosFromDb() = repository.fetchVideosFromDb().asLiveData()

}