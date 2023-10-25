package com.example.livedataexample

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class MainViewModel: ViewModel() {
    var liveData: MediatorLiveData<String> = MediatorLiveData("AAA")

    fun changeData(string: String) {
        liveData.value = string
    }

    fun changeDataWorkThread(string: String) {
        liveData.postValue(string)
    }
}