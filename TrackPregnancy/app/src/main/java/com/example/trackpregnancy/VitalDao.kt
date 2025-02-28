package com.example.trackpregnancy

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VitalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVital(vital: Vital): Long

    @Query("DELETE FROM vital_table")
    suspend fun clearAllVitals(): Int

    @Query("SELECT * FROM vital_table ORDER BY timestamp DESC")
    fun getAllVitals(): Flow<List<Vital>>
}