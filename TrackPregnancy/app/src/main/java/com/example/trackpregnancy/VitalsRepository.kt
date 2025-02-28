package com.example.trackpregnancy

import kotlinx.coroutines.flow.Flow

class VitalsRepository(private val vitalDao: VitalDao) {
    val allVitals: Flow<List<Vital>> = vitalDao.getAllVitals()

    suspend fun insertVital(vital: Vital){
        vitalDao.insertVital(vital)
    }

//    suspend fun clearVital(){
//        vitalDao.clearAllVitals()
//    }
}