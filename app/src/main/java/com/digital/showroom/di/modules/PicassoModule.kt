package com.digital.showroom.di.modules

import android.content.Context
import com.jakewharton.picasso.OkHttp3Downloader
import com.digital.showroom.di.ApplicationScope
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Named

/**
 * Created by Krishna Upadhya on 11/08/20.
 */
@Module(includes = [OkHttpClientModule::class])
class PicassoModule {

    @ApplicationScope
    @Provides
    fun picasso(@Named("application_context")context: Context, okHttp3Downloader: OkHttp3Downloader): Picasso {
        return Picasso.Builder(context)
            .downloader(okHttp3Downloader)
            .build()
    }

    @Provides
    fun okHttp3Downloader(okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }
}