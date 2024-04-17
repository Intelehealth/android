package org.intelehealth.config.room.entity

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Vaghela Mithun R. on 17-04-2024 - 18:21.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
data class PatientRegistrationFields(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val groupName: String,
    val name: String,
    @SerializedName("is_mandatory")
    val isMandatory: Boolean,
    @SerializedName("is_editable")
    val isEditable: Boolean,
    @SerializedName("is_enabled")
    val isEnabled: Boolean
)