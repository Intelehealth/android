package org.intelehealth.feature.chat.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.intelehealth.app.BuildConfig
import org.intelehealth.core.network.service.AuthInterceptor
import org.intelehealth.core.socket.SocketManager
import org.intelehealth.core.utils.helper.PreferenceHelper
import org.intelehealth.feature.chat.restapi.ChatRestClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideAuthInterceptor(preferenceHelper: PreferenceHelper) = AuthInterceptor(preferenceHelper)

    @Singleton
    @Provides
    fun providePreferenceHelper(@ApplicationContext context: Context) = PreferenceHelper(context)

    @Singleton
    @Provides
    fun provideSocketManager(@ApplicationContext context: Context) = SocketManager.instance

    @Singleton
    @Provides
    fun provideOkHttpClient(
        okHttpBuilder: OkHttpClient.Builder, authInterceptor: AuthInterceptor
    ): OkHttpClient = if (authInterceptor.hasToken()) {
        okHttpBuilder.addInterceptor(authInterceptor).build()
    } else {
        okHttpBuilder.build()
    }

    @Singleton
    @Provides
    fun provideOkHttpBuilder(interceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder().retryOnConnectionFailure(true).connectTimeout(180, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS).writeTimeout(180, TimeUnit.SECONDS).addInterceptor(interceptor)

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    fun provideWebRtcApiClient(
        okhttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory
    ) = Retrofit.Builder().baseUrl(BuildConfig.SERVER_URL).client(okhttpClient)
        .addConverterFactory(gsonConverterFactory).build().create(ChatRestClient::class.java)
}