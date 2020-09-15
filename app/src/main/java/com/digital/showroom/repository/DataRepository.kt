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
        return getDemoVehiclesList()
    }

    var renderableAsset: ModelRenderable? = null
        get() = field
        set(value) {
            field = value
        }

    var renderableBlueAsset: ModelRenderable? = null
        get() = field
        set(value) {
            field = value
        }

    var renderableRedAsset: ModelRenderable? = null
        get() = field
        set(value) {
            field = value
        }

    private fun getDemoVehiclesList(): List<CarData>? {
        if (cars.isNullOrEmpty()) {
            val carList = ArrayList<CarData>()
            carList.add(
                CarData(
                    "1001",
                    "NSX",
                    "2020",
                    "https://nsx.acura.com/assets/2020/explorer/assets/desktop/chapters/1-NSX/01_Ratings-Section/NSX-Indy-Yellow-Pearl-front-view-driving-large2x.jpg",
                    "civic.glb",
                    4,
                    "Acura",
                    "2977",
                    "Automatic",
                    "12"
                )
            )
            carList.add(
                CarData(
                    "1002",
                    "TLX",
                    "2019",
                    "https://cf.ignitionone.com/wp/abd5312e075b9646ca3dc38ce6783995.png",
                    "civic.glb",
                    5,
                    "Acura",
                    "3500",
                    "Automatic",
                    "10.2"
                )
            )
            carList.add(
                CarData(
                    "1003",
                    "ILX",
                    "2019",
                    "https://images4.alphacoders.com/965/thumb-1920-965323.jpg",
                    "civic.glb",
                    4,
                    "Acura",
                    "2977",
                    "Automatic",
                    "12"
                )
            )
            carList.add(
                CarData(
                    "1004",
                    "HR-V",
                    "2020",
                    "https://media.zigcdn.com/media/model/2019/Sep/honda-hr-v_600x400.jpg",
                    "civic.glb",
                    4,
                    "Honda",
                    "1800",
                    "Manual",
                    "16"
                )
            )
            carList.add(
                CarData(
                    "1005",
                    "Accord",
                    "2018",
                    "https://cdn.jdpower.com/JDPA_2020%20Honda%20Accord%20Hybrid%20Blue%20Front%20Quarter%20View.jpg",
                    "civic.glb",
                    4,
                    "Honda",
                    "3741",
                    "Automatic",
                    "23"
                )
            )
            carList.add(
                CarData(
                    "1006",
                    "CIVIC",
                    "2019",
                    "https://di-uploads-pod1.dealerinspire.com/hondaoflincoln/uploads/2016/01/16_Civic_Sedan_135.jpg",
                    "civic.glb",
                    4,
                    "Honda",
                    "1800",
                    "Automatic",
                    "20"
                )
            )
            carList.add(
                CarData(
                    "1007",
                    "Integra-Type-R",
                    "1995",
                    "https://www.wsupercars.com/wallpapers/Honda/1998-Honda-Integra-Type-R-001-1080.jpg",
                    "honda_integra_white.glb",
                    4,
                    "Acura",
                    "1500",
                    "manual",
                    "14.6",
                    mutableListOf("red", "blue", "white")
                )
            )
            cars = carList
        }
        return cars
    }


}