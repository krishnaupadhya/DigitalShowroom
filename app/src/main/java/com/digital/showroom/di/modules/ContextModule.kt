package com.digital.showroom.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Krishna Upadhya on 11/08/20.
 */

@Module
class ContextModule(private var context: Context) {

    @Named("application_context")
    @Provides
    fun getContext(): Context {
        return context.applicationContext
    }
}