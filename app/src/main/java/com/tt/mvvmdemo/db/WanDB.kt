package com.tt.mvvmdemo.db

import android.content.Context
import androidx.room.Room
import com.tt.mvvmdemo.base.BaseApplication

object WanDB {
    private var context: Context? = null
    private var database: WanRoom? = null

    init {
        this.context = BaseApplication.mContext
    }

    @JvmStatic
    private fun db(dbName: String): WanRoom {
        database?.run {
            if (isOpen) {
                if (openHelper.databaseName == dbName) {
                    return this
                } else {
                    close()
                }
            }
        }
        database = Room.databaseBuilder(context!!, WanRoom::class.java, dbName).build()
        return database!!
    }

    @JvmStatic
    fun db(): WanRoom {
        return db("wan_db")
    }
}