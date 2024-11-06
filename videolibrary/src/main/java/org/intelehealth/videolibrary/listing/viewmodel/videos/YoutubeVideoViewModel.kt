package org.intelehealth.videolibrary.listing.viewmodel.videos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.intelehealth.videolibrary.listing.data.video.VideoDataSource
import org.intelehealth.videolibrary.listing.data.video.VideoRepository
import org.intelehealth.videolibrary.restapi.VideoLibraryApiClient
import org.intelehealth.videolibrary.restapi.response.videos.MainVideoResponse
import org.intelehealth.videolibrary.room.dao.VideoDao
import org.intelehealth.videolibrary.utils.ResponseChecker
import retrofit2.Response

/**
 * Created by Arpan Sircar. on 08-02-2024.
 * Email : arpan@intelehealth.org
 * Mob   : +919123116015
 **/

class YoutubeVideoViewModel(
    service: VideoLibraryApiClient,
    dao: VideoDao
) : ViewModel() {

    private var repository: VideoRepository

    private var _tokenExpiredObserver: MutableLiveData<Boolean> = MutableLiveData(false)
    var tokenExpiredObserver: LiveData<Boolean> = _tokenExpiredObserver

    private var _emptyListObserver: MutableLiveData<Boolean> = MutableLiveData(false)
    var emptyListObserver: LiveData<Boolean> = _emptyListObserver

    init {
        val dataSource = VideoDataSource(service, dao)
        repository = VideoRepository(dataSource)
    }

    fun fetchCategoryVideosFromServer(auth: String, categoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository
                .fetchCategoryVideosFromServer(auth, categoryId)
                .collect { response ->
                    handleVideoResponse(response)
                }
        }
    }

    private fun handleVideoResponse(response: Response<MainVideoResponse>) {
        val responseChecker = ResponseChecker(response)
        if (responseChecker.isNotAuthorized) {
            _tokenExpiredObserver.postValue(true)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                response.body()?.videoResponse?.videos?.let {
                    _emptyListObserver.postValue(it.isEmpty())
                    repository.insertVideos(it)
                }
            }
        }
    }

    fun fetchVideosFromDb(categoryId: Int) =
        repository.getVideosFromDbByCategoryId(categoryId).asLiveData()

    fun deleteAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllVideos()
        }
    }

    fun deleteVideosByCategoryId(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllVideosByCategoryId(categoryId)
        }
    }
}