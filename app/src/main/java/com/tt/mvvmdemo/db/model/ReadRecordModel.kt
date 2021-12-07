package com.tt.mvvmdemo.db.model

import androidx.room.Entity

@Entity(primaryKeys = ["link"])
data class ReadRecordModel(
    val link: String,
    val title: String,
    val time: Long
)