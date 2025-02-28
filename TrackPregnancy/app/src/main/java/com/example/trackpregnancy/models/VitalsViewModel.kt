package com.example.trackpregnancy.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackpregnancy.Vital
import com.example.trackpregnancy.VitalsDatabase
import com.example.trackpregnancy.VitalsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class VitalsViewModel(application: Application): AndroidViewModel(application ) {
    private val repository: VitalsRepository
    private val _vitalList= MutableStateFlow<List<Vital>>(emptyList())
    val vitalsList:StateFlow<List<Vital>> = _vitalList.asStateFlow()

    init {
        val dao = VitalsDatabase.getDatabase(application).vitalDao()
        repository= VitalsRepository(dao)

        viewModelScope.launch {
            repository.allVitals.collect{vitals->
                _vitalList.value = vitals
            }
        }
    }

    fun addvital(vital: Vital) = viewModelScope.launch {
        repository.insertVital(vital)
        _vitalList.value= repository.allVitals.first()
    }
}