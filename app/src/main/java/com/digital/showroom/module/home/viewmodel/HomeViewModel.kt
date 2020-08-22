package com.digital.showroom.module.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.digital.showroom.model.CarData
import com.digital.showroom.repository.DataRepository
import com.digital.showroom.utils.AppConstants
import com.digital.showroom.utils.Logger
import com.google.firebase.firestore.FirebaseFirestore


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

                        val messageList = data.get(AppConstants.KEY_MODEL_INFO) as ArrayList<*>

                        val cars = mutableListOf<CarData>()
                        for (msg in messageList) {
                            val map = msg as HashMap<*, *>
                            cars.add(
                                CarData(
                                    map["model"]!! as String,
                                    map["image_url"]!! as String,
                                    map["model_3d_name"]!! as String,
                                    map["year"]!! as String
                                )
                            )
                        }
                        DataRepository.setCarsList(cars)
                        carsList.value = cars
                    }
                }
            }
            .addOnFailureListener { exception ->
                Logger.log("fetchMessagesFromFirebase get failed: ${exception.message}")
            }
    }

    fun getItemAtPosition(position: Int) {
        carData.value = carsList.value?.get(position)
    }

}