package com.tt.mvvmdemo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tt.mvvmdemo.db.dao.ReadLaterDao
import com.tt.mvvmdemo.db.dao.ReadRecordDao
import com.tt.mvvmdemo.db.model.ReadLaterModel
import com.tt.mvvmdemo.db.model.ReadRecordModel

@Database(
    entities = [ReadLaterModel::class, ReadRecordModel::class],
    version = 1,
    exportSchema = false
)
abstract class WanRoom : RoomDatabase() {
    abstract fun readLaterDao(): ReadLaterDao
    abstract fun readRecordDao(): ReadRecordDao
}