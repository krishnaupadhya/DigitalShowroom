package com.digital.showroom.module.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.digital.showroom.model.CarData
import com.digital.showroom.repository.DataRepository
import com.digital.showroom.utils.AppConstants
import com.digital.showroom.utils.Logger
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.lang.reflect.Type


class HomeViewModel : ViewModel() {
    var carsList: MutableLiveData<List<CarData>> = MutableLiveData<List<CarData>>()
    var carData: MutableLiveData<CarData> = MutableLiveData<CarData>()

    init {
        getVehiclesList()
    }

    private fun getVehiclesList() {
        FirebaseFirestore.getInstance()
            .document("${AppConstants.KEY_VEHICLE_INFO}/${AppConstants.KEY_VEHICLES}")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val data = document.data
                    data?.let {
                        Logger.log("fetchMessagesFromFirebase get success")

                        val result = data.get(AppConstants.KEY_MODEL_INFO) as ArrayList<*>
                        // convert your list to json
                        val cars: ArrayList<CarData> = getCarListFromMap(result)
                        DataRepository.setCarsList(cars)
                        carsList.value = cars
                    }
                }
            }
            .addOnFailureListener { exception ->
                Logger.log("fetchMessagesFromFirebase get failed: ${exception.message}")
            }
    }

    private fun getCarListFromMap(
        result: ArrayList<*>
    ): ArrayList<CarData> {
        val gson = Gson()
        val jsonCartList = gson.toJson(result)
        val userListType: Type =
            object : TypeToken<ArrayList<CarData?>?>() {}.type
        return gson.fromJson(jsonCartList, userListType)
    }

    fun getItemAtPosition(position: Int) {
        carData.value = carsList.value?.get(position)
    }

}