package org.intelehealth.videolibrary.restapi.response.categories

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String
)
