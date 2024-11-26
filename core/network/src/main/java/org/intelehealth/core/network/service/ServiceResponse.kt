package org.intelehealth.core.network.service

import com.google.gson.annotations.SerializedName

open class ServiceResponse<T>(
    @SerializedName("status") val status: String = "Failed",
    @SerializedName("statusCode") val statusCode: Int = 500,
    @SerializedName("data") val data: T? = null,
    @SerializedName("message") val message: String? = null,
)