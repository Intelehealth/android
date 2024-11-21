package org.intelehealth.feature.chat.provider

import android.content.Context
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.intelehealth.core.utils.helper.PreferenceHelper
import org.intelehealth.app.BuildConfig
import org.intelehealth.feature.chat.restapi.AuthInterceptor
import org.intelehealth.feature.chat.restapi.ChatRestClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Vaghela Mithun R. on 16-09-2023 - 20:04.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
object RetrofitProvider {
    private var apiClient: ChatRestClient? = null

    fun getApiClient(context: Context): ChatRestClient = apiClient ?: synchronized(this) {
        apiClient ?: buildChatRestClient(context).also {
            apiClient = it
        }
    }

    private fun buildChatRestClient(context: Context): ChatRestClient {
        return provideWebRtcApiClient(
            provideRetrofitBuilder(
                provideOkHttpApiClient(
                    provideOkHttpBuilder(provideHttpLoggingInterceptor()), AuthInterceptor(PreferenceHelper(context))
                ), provideGsonConverterFactory(provideGson())
            )
        )
    }

    private fun provideOkHttpApiClient(
        okHttpBuilder: OkHttpClient.Builder, authInterceptor: AuthInterceptor
    ): OkHttpClient = if (authInterceptor.hasToken()) {
        okHttpBuilder.addInterceptor(authInterceptor).build()
    } else {
        okHttpBuilder.build()
    }

    private fun provideOkHttpRtcClient(okHttpBuilder: OkHttpClient.Builder): OkHttpClient = okHttpBuilder.build()

    private fun provideOkHttpBuilder(interceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor)

    private fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    private fun provideGson(): Gson = Gson()


    private fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    private fun provideRetrofitBuilder(
        okhttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory
    ): Retrofit.Builder = Retrofit.Builder().client(okhttpClient).addConverterFactory(gsonConverterFactory)


    private fun provideWebRtcApiClient(
        retrofitBuilder: Retrofit.Builder
    ): ChatRestClient = retrofitBuilder.baseUrl(BuildConfig.SOCKET_URL).build().create(ChatRestClient::class.java)

    fun getOkHttpClient() = provideOkHttpRtcClient(
        provideOkHttpBuilder(provideHttpLoggingInterceptor())
    )
}