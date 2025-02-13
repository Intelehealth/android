package org.intelehealth.app.triagingengine.model.rules

import com.google.gson.annotations.SerializedName

/**
 * Created by Lincon Pradhan on  03-02-2025.
 **/
data class Field(
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("score_value") val scoreValue: Int
)