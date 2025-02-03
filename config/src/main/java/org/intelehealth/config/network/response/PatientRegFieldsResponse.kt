package org.intelehealth.config.network.response

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class PatientRegFieldsResponse(
    val name: String,
    @SerializedName("key")
    val idKey: String,
    @SerializedName("is_mandatory")
    val isMandatory: Boolean,
    @SerializedName("is_editable")
    val isEditable: Boolean,
    @SerializedName("is_enabled")
    var isEnabled: Boolean,
    val validations: Validations?,
)

data class Validations(
    val maxLength: Int?,
    val minLength: Int?
)
