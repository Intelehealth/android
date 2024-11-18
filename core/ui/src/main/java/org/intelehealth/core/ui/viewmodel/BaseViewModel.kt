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
        networkCall: suspend () -> ServiceResponse<T>
    ) = flow {
        if (isInternetAvailable()) {
            val response = networkCall()
            if (response.status == 200) {
                Timber.d("Api success")
                val result = Result.Success(response.data, "Success")
                result.message = response.message
                emit(result)
            } else {
                Timber.e("Api error ${response.message}")
                emit(Result.Error(response.message))
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
        localCall: () -> LiveData<L>?, networkCall: suspend () -> ServiceResponse<T>, saveDataCall: suspend (T?) -> L
    ) = flow {
        val data = localCall()
        data?.value?.let {
            emit(Result.Success(it, "Success"))
        } ?: if (isInternetAvailable()) {
            val response = networkCall()
            if (response.status == 200) {
                Timber.d("Api success")
                val data = saveDataCall(response.data)
                val result = Result.Success(data, "Success")
                result.message = response.message
                emit(result)
            } else {
                Timber.e("Api error ${response.message}")
                emit(Result.Error(response.message))
            }
        } else dataConnectionStatus.postValue(false)
    }.onStart {
        emit(Result.Loading("Please wait..."))
    }.flowOn(dispatcher)

    fun isInternetAvailable(): Boolean = networkHelper?.isNetworkConnected() ?: false

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