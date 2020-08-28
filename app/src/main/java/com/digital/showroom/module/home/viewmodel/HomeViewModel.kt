package com.digital.showroom.module.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.digital.showroom.model.CarData
import com.digital.showroom.repository.DataRepository
import com.digital.showroom.utils.Logger
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import org.json.JSONObject


class HomeViewModel : ViewModel() {
    var carsList: MutableLiveData<List<CarData>> = MutableLiveData<List<CarData>>()
    var carData: MutableLiveData<CarData> = MutableLiveData<CarData>()

    init {
        getVehiclesList()
    }

    fun getVehiclesList() {

        FirebaseFirestore.getInstance().collection("vehicles").get()
            .addOnSuccessListener { documentSnapshots ->
                if (documentSnapshots.isEmpty()) {
                    Logger.log("onSuccess: LIST EMPTY")
                } else {
                    val cars = ArrayList<CarData>()
                    for (document in documentSnapshots) {
                        Logger.log("${document.id} => ${document.data}")
                        val data = document.data
                        data.let {
                            Logger.log("car list get success")
                            val obj = JSONObject(data).toString()
                            val car: CarData = Gson().fromJson(obj, CarData::class.java)
                            cars.add(car)
                        }
                    }
                    DataRepository.setCarsList(cars)
                    carsList.value = cars
                }


            }
            .addOnFailureListener { exception ->
                Logger.log("car list get failed: ${exception.message}")
            }
    }

    fun getItemAtPosition(position: Int) {
        carData.value = carsList.value?.get(position)
    }

}