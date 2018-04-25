package com.sub6resources.vampir.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.sub6resources.utilities.switchMap
import com.sub6resources.vampir.models.EncryptedCredentials
import com.sub6resources.vampir.repository.PredictionRepository

class PredictionViewModel(private val predictionRepository: PredictionRepository): ViewModel() {
    private val encryptedCredentials = MutableLiveData<EncryptedCredentials>()
    val predictedData = encryptedCredentials.switchMap { predictionRepository.predictBloodSugar(it) }

    fun predict(encryptedCred: EncryptedCredentials) {
        encryptedCredentials.value = encryptedCred
    }

//    fun predictFromValue(value: Float, hour: Int) {
//        //TODO
//    }
}