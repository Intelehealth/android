package org.intelehealth.app.triagingengine.model.rules

import com.google.gson.annotations.SerializedName

/**
 * Created by Lincon Pradhan on  03-02-2025.
 **/
data class Formula(
    @SerializedName("expression") val expression: String,
    @SerializedName("name") val name: String,
    @SerializedName("result") val result: Int
)
