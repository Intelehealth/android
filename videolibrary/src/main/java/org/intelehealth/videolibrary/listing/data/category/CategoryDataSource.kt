package org.intelehealth.videolibrary.listing.data.category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.intelehealth.videolibrary.model.Category
import org.intelehealth.videolibrary.restapi.VideoLibraryApiClient
import org.intelehealth.videolibrary.restapi.response.categories.MainCategoryResponse
import org.intelehealth.videolibrary.room.dao.CategoryDao
import org.intelehealth.videolibrary.room.dao.VideoDao
import retrofit2.Response

/**
 * Created by Arpan Sircar. on 08-02-2024.
 * Email : arpan@intelehealth.org
 * Mob   : +919123116015
 **/

class CategoryDataSource(
    private val service: VideoLibraryApiClient,
    private val categoryDao: CategoryDao,
    private val videoDao: VideoDao
) {

    fun fetchAllCategoriesFromServer(auth: String): Flow<Response<MainCategoryResponse>> = flow {
        emit(service.fetchAllCategories(auth))
    }

    suspend fun insertCategories(categories: List<Category>) {
        categoryDao.deleteAll()
        categoryDao.insertAll(categories)
    }

    fun fetchCategoriesFromDb(): Flow<List<Category>> = categoryDao.getAll()

    suspend fun deleteAllCategories() {
        categoryDao.deleteAll()
    }

    suspend fun deleteAllVideos() {
        videoDao.deleteAll()
    }

    suspend fun deleteAllVideosByCategoryId(categoryId: Int) =
        videoDao.deleteVideosByCategoryId(categoryId)
}