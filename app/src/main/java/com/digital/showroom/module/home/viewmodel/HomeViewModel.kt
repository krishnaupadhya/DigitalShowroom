package com.digital.showroom.module.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.digital.showroom.model.CarData
import com.digital.showroom.repository.DataRepository
import com.digital.showroom.utils.Logger
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import org.json.JSONObject
import java.io.File


class HomeViewModel : ViewModel() {
    var carsList: MutableLiveData<List<CarData>> = MutableLiveData<List<CarData>>()
    var carData: MutableLiveData<CarData> = MutableLiveData<CarData>()
    var modelFileName = MutableLiveData<File?>()

    init {
//        getVehiclesList()
        getDemoVehiclesList()
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

    private fun getDemoVehiclesList() {
        val cars = ArrayList<CarData>()
        cars.add(
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
        cars.add(
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
        cars.add(
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
        cars.add(
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
        cars.add(
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
        cars.add(
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
        DataRepository.setCarsList(cars)
        carsList.value = cars
    }

}