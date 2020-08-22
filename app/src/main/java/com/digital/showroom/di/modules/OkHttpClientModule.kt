package com.digital.showroom.di.modules

import android.content.Context
import android.util.Log
import com.digital.showroom.di.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import javax.inject.Named

/**
 * Created by Krishna Upadhya on 11/08/20.
 */

@Module(includes = [ContextModule::class])
class OkHttpClientModule {

    @ApplicationScope
    @Provides
    fun okHttpClient(cache: Cache, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(loggingInterceptor).cache(cache).build()
    }

    @Provides
    fun cache(cacheFile: File): Cache {
        return Cache(cacheFile, 10 * 1024 * 1024)//10 MB
    }

    @Provides
    fun file(@Named("application_context")context: Context): File {
        val file = File(context.filesDir, "HttpCache")
        file.mkdir()
        return file
    }

    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {

            Log.d("OKHTTP", it)

        })
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return httpLoggingInterceptor
    }


}