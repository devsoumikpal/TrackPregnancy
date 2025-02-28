package com.example.trackpregnancy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vital_table")
data class Vital(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val bloodPressureSys: Int,
    val bloodPressureDia: Int,
    val weight: Float,
    val heartRate: Int,
    val babyKicks: Int,
    val timestamp: Long = System.currentTimeMillis()
)
