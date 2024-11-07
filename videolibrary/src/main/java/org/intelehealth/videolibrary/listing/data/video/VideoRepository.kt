package org.intelehealth.videolibrary.listing.data.video

import org.intelehealth.videolibrary.model.Video

class VideoRepository(private val source: VideoDataSource) {

    fun fetchCategoryVideosFromServer(
        auth: String,
        categoryId: String
    ) = source.fetchVideosFromServer(auth, categoryId)

    suspend fun insertVideos(videos: List<Video>) =
        source.insertVideos(videos)

    fun getVideosFromDbByCategoryId(categoryId: Int) =
        source.getVideosFromDbByCategoryId(categoryId)

}