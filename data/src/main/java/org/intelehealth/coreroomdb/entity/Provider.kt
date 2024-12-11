package org.intelehealth.coreroomdb.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_provider")
data class Provider(
    @PrimaryKey @SerializedName(value = "uuid", alternate = arrayOf("providerid")) var uuid: String,
    @SerializedName("identifier") var identifier: String? = null,
    @ColumnInfo("given_name") @SerializedName("given_name") var givenName: String? = null,
    @ColumnInfo("family_name") @SerializedName("family_name") var familyName: String? = null,
    @ColumnInfo("middle_name") @SerializedName("middle_name") var middleName: String? = null,
    @SerializedName("emailId") var emailId: String,
    @ColumnInfo("telephone_number")
    @SerializedName("telephoneNumber") var telephoneNumber: String? = null,
    @ColumnInfo("date_of_birth") @SerializedName("dateofbirth") var dateOfBirth: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("imagePath") var imagePath: String? = null,
    @ColumnInfo("country_code") @SerializedName("countryCode") var countryCode: String? = null,
    var voided: Int? = null,
    var role: String? = null,
    var providerId: Int? = null,
    @ColumnInfo("user_uuid") @SerializedName("useruuid") var userUuid: String? = null,
    @ColumnInfo("modified_date") @SerializedName("modified_date") val modifiedDate: String? = null,
    var synced: Boolean = false,
) : Parcelable

