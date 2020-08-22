package com.digital.showroom.module.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.digital.showroom.model.CarData
import com.digital.showroom.repository.DataRepository

class CarViewModel : ViewModel() {
    var carData: MutableLiveData<CarData> = MutableLiveData<CarData>()

    init {
    }


    fun getItemAtPosition(position: Int) {
        carData.value = DataRepository.getCars()?.get(position)
    }

}