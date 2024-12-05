package org.intelehealth.core.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import org.intelehealth.core.network.helper.NetworkHelper
import org.intelehealth.core.network.service.ServiceResponse
import org.intelehealth.core.utils.helper.PreferenceHelper
import org.intelehealth.core.network.state.Result
import retrofit2.Response
import timber.log.Timber

open class BaseViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val networkHelper: NetworkHelper? = null,
    private val preferenceHelper: PreferenceHelper? = null
) : ViewModel() {
    private val loadingData = MutableLiveData<Boolean>()

    @JvmField
    var loading: LiveData<Boolean> = loadingData

    private val failResult = MutableLiveData<String>()

    @JvmField
    var failDataResult: LiveData<String> = failResult

    private val errorResult = MutableLiveData<Throwable>()

    @JvmField
    var errorDataResult: LiveData<Throwable> = errorResult

    var dataConnectionStatus = MutableLiveData<Boolean>(true)


    fun <L> executeLocalQuery(
        queryCall: () -> L?
    ) = flow {
        val localData = queryCall.invoke()
        localData?.let { emit(Result.Success(localData, "")) } ?: kotlin.run {
            emit(Result.Error<L>("No record found"))
        }
    }.onStart {
        emit(Result.Loading<L>("Please wait..."))
    }.flowOn(dispatcher)

    fun <T> executeNetworkCall(
        networkCall: suspend () -> Response<out ServiceResponse<T>>
    ) = flow {
        if (isInternetAvailable()) {
            com.github.ajalt.timberkt.Timber.d { "network call started" }
            val response = networkCall()
            com.github.ajalt.timberkt.Timber.d { "response.status => ${response.code()}" }
            if (response.code() == 200) {
                Timber.d("Api success")
                val result = Result.Success(response.body()?.data, "Success")
                result.message = response.body()?.message
                emit(result)
            } else {
                Timber.e("Api error ${response.body()?.message}")
                emit(Result.Error(response.body()?.message))
            }
        } else dataConnectionStatus.postValue(false)
    }.onStart {
        emit(Result.Loading("Please wait..."))
    }.flowOn(dispatcher)

    fun executeLocalInsertUpdateQuery(
        queryCall: () -> Boolean
    ) = flow {
        val status = queryCall.invoke()
        if (status) emit(Result.Success(true, ""))
        else emit(Result.Error<Boolean>("Failed"))
    }.onStart {
        emit(Result.Loading<Boolean>("Please wait..."))
    }.flowOn(dispatcher)

    fun <L, T> catchNetworkData(
        networkCall: suspend () -> Response<out ServiceResponse<T>>, saveDataCall: suspend (T?) -> L
    ) = flow {
        com.github.ajalt.timberkt.Timber.d { "catchNetworkData" }
        if (isInternetAvailable()) {
            com.github.ajalt.timberkt.Timber.d { "catchNetworkData api calling" }
            val response = networkCall()
            if (response.code() == 200) {
                Timber.d("Api success")
                val savedData = saveDataCall(response.body()?.data)
                val result = Result.Success(savedData, "Success")
                result.message = response.body()?.message
                emit(result)
            } else {
                Timber.e("Api error ${response.body()?.message}")
                emit(Result.Error(response.body()?.message))
            }
        } else dataConnectionStatus.postValue(false)
    }.onStart {
        emit(Result.Loading("Please wait..."))
    }.flowOn(dispatcher)

    fun isInternetAvailable(): Boolean = true //networkHelper?.isNetworkConnected() ?: false

    /**
     * Handle response here in base with loading and error message
     *
     */
    fun <T> handleResponse(it: Result<T>, callback: (data: T) -> Unit) {
        println("handleResponse status ${it.status} ${it.message}")
        when (it.status) {
            Result.State.LOADING -> {
                loadingData.postValue(true)
            }

            Result.State.FAIL -> {
                loadingData.postValue(false)
                failResult.postValue("")
            }

            Result.State.SUCCESS -> {
                loadingData.postValue(false)
                it.data?.let { data ->
                    println("data ${Gson().toJson(data)}")
                    callback(data)
                } ?: failResult.postValue(it.message ?: "")
            }

            Result.State.ERROR -> {
                println("ERROR ${it.message}")
                loadingData.postValue(false)
                errorResult.postValue(Throwable(it.message))
            }
        }
    }

    fun updateFailResult(message: String) {
        failResult.postValue(message)
    }

    companion object {
        private const val TAG = "BaseViewModel"
    }
}