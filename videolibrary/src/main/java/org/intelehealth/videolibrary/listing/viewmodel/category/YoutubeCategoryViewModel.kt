package org.intelehealth.videolibrary.listing.viewmodel.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.intelehealth.videolibrary.listing.data.category.CategoryDataSource
import org.intelehealth.videolibrary.listing.data.category.CategoryRepository
import org.intelehealth.videolibrary.model.Category
import org.intelehealth.videolibrary.model.Video
import org.intelehealth.videolibrary.restapi.VideoLibraryApiClient
import org.intelehealth.videolibrary.restapi.response.categories.MainCategoryResponse
import org.intelehealth.videolibrary.room.dao.CategoryDao
import org.intelehealth.videolibrary.utils.ResponseChecker
import retrofit2.Response

/**
 * Created by Arpan Sircar. on 08-02-2024.
 * Email : arpan@intelehealth.org
 * Mob   : +919123116015
 **/

class YoutubeCategoryViewModel(
    service: VideoLibraryApiClient,
    categoryDao: CategoryDao
) : ViewModel() {

    private var repository: CategoryRepository

    private var _tokenExpiredObserver: MutableLiveData<Boolean> = MutableLiveData(false)
    var tokenExpiredObserver: LiveData<Boolean> = _tokenExpiredObserver

    private var _emptyListObserver: MutableLiveData<Boolean> = MutableLiveData(false)
    var emptyListObserver: LiveData<Boolean> = _emptyListObserver

    init {
        val dataSource = CategoryDataSource(service, categoryDao)
        repository = CategoryRepository(dataSource)
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
            viewModelScope.launch(Dispatchers.IO) {
                response.body()?.data?.let {
                    _emptyListObserver.postValue(it.isEmpty())
                    repository.insertCategories(it)
                }
            }
        }
    }

    fun fetchCategoriesFromDb() = repository.fetchAllCategoriesFromDb().asLiveData()

    fun deleteAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllCategories()
        }
    }

    fun areListsSame(list1: List<Category>?, list2: List<Category>?) = list1 == list2

}