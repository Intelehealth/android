package org.intelehealth.videolibrary.restapi.response.categories

import com.google.gson.annotations.SerializedName

data class MainCategoryResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: List<Category>
)