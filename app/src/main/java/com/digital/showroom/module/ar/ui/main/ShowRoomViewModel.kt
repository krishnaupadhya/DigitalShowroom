package com.digital.showroom.module.ar.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digital.showroom.repository.DataRepository
import com.digital.showroom.utils.Logger
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.io.File

class ShowRoomViewModel(var position: Int) : ViewModel() {

    var modelFileName = MutableLiveData<File?>()
    var model = MutableLiveData<String>()


    fun startDownloading(position: Int) {
        viewModelScope.launch {
            downloadModel(position)
        }
    }

    private fun downloadModel(position: Int) {
        val fileName: String = DataRepository.getCars()?.get(position)?.model_renderable_name!!
        val storage = FirebaseStorage.getInstance()
        val modelRef = storage.reference.child("$fileName.glb")
        try {
            val file = File.createTempFile(fileName, "glb");
            modelRef.getFile(file).addOnSuccessListener {
                Logger.log("$fileName model download success")
                modelFileName.value = file
            }.addOnFailureListener {
                Logger.log("$fileName model download failed ${it.localizedMessage}")
                modelFileName.value = null
            }
        } catch (e: Exception) {
            Logger.log("$fileName model download failed ${e.localizedMessage}")
            modelFileName.value = null
        }
    }

    fun getModelName(position: Int) {
        model.value = DataRepository.getCars()?.get(position)?.model_renderable_name

    }

    fun isColorPaletAvailable(position: Int): Boolean {
        return DataRepository.getCars()?.get(position)?.colorPallets?.isNotEmpty() ?: false
    }
}