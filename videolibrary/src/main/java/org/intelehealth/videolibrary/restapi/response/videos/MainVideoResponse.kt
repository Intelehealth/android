package org.intelehealth.videolibrary.restapi.response.videos

import com.google.gson.annotations.SerializedName

/**
 * Created by Arpan Sircar. on 08-02-2024.
 * Email : arpan@intelehealth.org
 * Mob   : +919123116015
 **/

data class MainVideoResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val videoResponse: VideoResponse
)