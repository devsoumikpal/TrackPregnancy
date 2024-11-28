package com.example.shayariapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "shayari_table")
data class DataEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var  id:Int,
    @ColumnInfo(name = "date")
    var date: Date,
    @ColumnInfo(name = "shayari")
    var shayari:String
){
}
