package com.digital.showroom.repository

import com.digital.showroom.model.CarData
import com.google.ar.sceneform.rendering.ModelRenderable

/**
 * Created by Krishna Upadhya on 19/08/20.
 */

object DataRepository {


    private var cars: List<CarData>? = null

    fun setCarsList(cars: List<CarData>) {
        this.cars = cars
    }

    fun getCars(): List<CarData>? {
        return cars
    }

     var renderableAsset: ModelRenderable?=null
        get() = field
        set(value) {
            field = value
        }


}