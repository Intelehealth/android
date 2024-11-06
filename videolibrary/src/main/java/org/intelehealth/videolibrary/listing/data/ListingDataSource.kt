package org.intelehealth.videolibrary.listing.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.intelehealth.videolibrary.model.Video
import org.intelehealth.videolibrary.restapi.VideoLibraryApiClient
import org.intelehealth.videolibrary.restapi.response.categories.MainCategoryResponse
import org.intelehealth.videolibrary.restapi.response.videos.MainVideoResponse
import org.intelehealth.videolibrary.room.dao.CategoryDao
import org.intelehealth.videolibrary.room.dao.LibraryDao
import retrofit2.Response

/**
 * Created by Arpan Sircar. on 08-02-2024.
 * Email : arpan@intelehealth.org
 * Mob   : +919123116015
 **/

class ListingDataSource(
    private val service: VideoLibraryApiClient,
    private val libraryDao: LibraryDao,
    private val categoryDao: CategoryDao
) {

    fun fetchAllCategoriesFromServer(auth: String): Flow<Response<MainCategoryResponse>> = flow {
        emit(service.fetchAllCategories(auth))
    }

    suspend fun insertVideosToDb(videos: List<Video>) {
        libraryDao.deleteAll()
        libraryDao.insertAll(videos)
    }

    fun fetchVideosFromDb(categoryId: Int): Flow<List<Video>> =
        libraryDao.getVideosByCategoryId(categoryId)

    suspend fun deleteAll() {
        libraryDao.deleteAll()
    }
}