package com.example.cahier.data;

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter;

import java.time.LocalDate;

//@ProvidedTypeConverter
//class LocalDateConverter {
//    @TypeConverter
//    fun fromTimestamp(value: Long?): LocalDate? {
//        return value?.let { LocalDate.ofEpochDay(it) }
//    }
//
//    @TypeConverter
//    fun dateToTimestamp(date: LocalDate?): Long? {
//        return date?.toEpochDay()
//    }
//}