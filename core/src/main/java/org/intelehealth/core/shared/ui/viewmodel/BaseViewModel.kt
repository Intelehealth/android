package org.intelehealth.core.shared.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeglo.coyamore.data.PreferenceHelper
import org.intelehealth.core.network.helper.NetworkHelper
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import org.intelehealth.core.network.state.Result
import retrofit2.Response

open class BaseViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val networkHelper: NetworkHelper? = null,
    private val preferenceHelper: PreferenceHelper? = null
) : ViewModel() {
    private val loadingData = MutableLiveData<Boolean>()

    var dataConnectionStatus = MutableLiveData<Boolean>(true)

    @JvmField
    var loading: LiveData<Boolean> = loadingData

    private val failResult = MutableLiveData<String>()

    @JvmField
    var failDataResult: LiveData<String> = failResult

    private val errorResult = MutableLiveData<Throwable>()

    @JvmField
    var errorDataResult: LiveData<Throwable> = errorResult


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

    fun executeLocalInsertUpdateQuery(
        queryCall: () -> Boolean
    ) = flow {
        val status = queryCall.invoke()
        if (status) emit(Result.Success(true, ""))
        else emit(Result.Error<Boolean>("Failed"))
    }.onStart {
        emit(Result.Loading<Boolean>("Please wait..."))
    }.flowOn(dispatcher)

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

    fun <T, L> getLocalFirstThenNetwork(
        networkCall: suspend () -> Response<T>,
        localCall: suspend () -> L?,
        saveDataCall: suspend (T?) -> L
    ) = flow {
        val localData = localCall.invoke()
        localData?.let { emit(Result.Success(localData, "Success")) } ?: kotlin.run {
            if (isInternetAvailable()) {
                val response = networkCall()
                if (response.code() == 200) {
                    val data = saveDataCall(response.body())
                    val result = Result.Success(data, "Success")
                    result.message = response.message()
                    emit(result)
                } else {
                    emit(Result.Error<L>(response.message()))
                }
            } else dataConnectionStatus.postValue(false)
        }
    }.onStart {
        emit(Result.Loading<L>("Loading..."))
    }.flowOn(dispatcher)

    fun isInternetAvailable(): Boolean = networkHelper?.isNetworkConnected() ?: false

    companion object {
        private const val TAG = "BaseViewModel"
    }
}