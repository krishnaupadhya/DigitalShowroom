package com.digital.showroom.di

import com.digital.showroom.di.modules.PicassoModule
import com.digital.showroom.di.modules.RetrofitModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Krishna Upadhya on 14/08/20.
 */

@Singleton
@Component(modules = [RetrofitModule::class, PicassoModule::class])
interface ParkingComponent {

}