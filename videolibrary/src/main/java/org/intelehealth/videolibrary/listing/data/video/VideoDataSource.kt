package org.intelehealth.videolibrary.listing.data.video

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.intelehealth.videolibrary.model.Video
import org.intelehealth.videolibrary.restapi.VideoLibraryApiClient
import org.intelehealth.videolibrary.restapi.response.videos.MainVideoResponse
import org.intelehealth.videolibrary.room.dao.VideoDao
import retrofit2.Response

class VideoDataSource(
    private val service: VideoLibraryApiClient,
    private val dao: VideoDao
) {

    fun fetchVideosFromServer(
        auth: String,
        categoryId: String
    ): Flow<Response<MainVideoResponse>> = flow {
        emit(service.fetchVideoByCategoryId(auth, categoryId))
    }

    suspend fun insertVideos(videos: List<Video>) {
        dao.insertAll(videos)
    }

    fun getVideosFromDbByCategoryId(categoryId: Int) = dao.getVideosByCategoryId(categoryId)

}