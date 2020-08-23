package com.digital.showroom.module.ar.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.digital.showroom.repository.DataRepository
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ShowRoomViewModel : ViewModel() {

    var modelFileName = MutableLiveData<File>()

    fun downLoadModel(position: Int) {

        val fileName: String = DataRepository.getCars()?.get(position)?.model_3d_name!!
        val storage = FirebaseStorage.getInstance()
        val modelRef = storage.reference.child("$fileName.glb")
        try {
            val file = File.createTempFile(fileName, "glb");
            modelRef.getFile(file).addOnSuccessListener {
                modelFileName.value = file
            }
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }
}