package com.example.shayariapp

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    public fun toTimestamp(date: Date):Long?{
        return date?.time
    }

    @TypeConverter
    public fun toDate(long: Long): Date{
        return long.let { Date(it) }
    }
}