package com.example.app1.ui.health

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HealthViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Ваши привычки"
    }
    val text: LiveData<String> = _text
}