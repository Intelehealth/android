package org.intelehealth.app.triagingengine.model.rules

import com.google.gson.annotations.SerializedName

/**
 * Created by Lincon Pradhan on  03-02-2025.
 **/
data class SymptomDuration(
    @SerializedName("mm_name") val mmName: String,
    @SerializedName("duration") val duration: Int
)