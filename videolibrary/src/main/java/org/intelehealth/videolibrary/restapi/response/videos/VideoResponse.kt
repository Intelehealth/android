package org.intelehealth.videolibrary.restapi.response.videos

import com.google.gson.annotations.SerializedName
import org.intelehealth.videolibrary.model.Video

/**
 * Created by Arpan Sircar. on 08-02-2024.
 * Email : arpan@intelehealth.org
 * Mob   : +919123116015
 **/

data class VideoResponse(

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("updatedAt")
    val updatedAt: String,

    @SerializedName("videos")
    val videos: List<Video>

)