package org.intelehealth.videolibrary.restapi

import org.intelehealth.videolibrary.restapi.response.categories.MainCategoryResponse
import org.intelehealth.videolibrary.restapi.response.videos.MainVideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * Created by Arpan Sircar. on 08-02-2024.
 * Email : arpan@intelehealth.org
 * Mob   : +919123116015
 **/

interface VideoLibraryApiClient {

    @GET("api/video-library/getAllCategories")
    suspend fun fetchAllCategories(
        @Header("Authorization") auth: String
    ): Response<MainCategoryResponse>

    @GET("api/video-library/getVideosByCategoryId/{category_id}")
    suspend fun fetchVideoByCategoryId(
        @Header("Authorization") auth: String,
        @Path("category_id") categoryId: String
    ): Response<MainVideoResponse>
}