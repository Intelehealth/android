package org.intelehealth.app.triagingengine.network

/**
 * Created by Lincon Pradhan on  03-02-2025.
 **/
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}