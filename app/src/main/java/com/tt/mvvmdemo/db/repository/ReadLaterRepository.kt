package com.tt.mvvmdemo.db.repository

import com.tt.mvvmdemo.db.WanDB.db
import com.tt.mvvmdemo.db.model.ReadLaterModel

class ReadLaterRepository : BaseDBRepository() {

    fun add(link: String, title: String, success: ((t: Any) -> Unit)? = null) {
        execute({
            val model = ReadLaterModel(link, title, System.currentTimeMillis())
            db().readLaterDao().insert(model)
        }, success)
    }

    fun remove(link: String, success: ((t: Unit) -> Unit)? = null) {
        execute({
            db().readLaterDao().delete(link)
        }, success)
    }

    fun removeAll(success: ((t: Unit) -> Unit)? = null) {
        execute({
            db().readLaterDao().deleteAll()
        }, success)
    }

    fun getList(from: Int, count: Int, success: ((t: List<ReadLaterModel>) -> Unit)? = null) {
        execute({
            db().readLaterDao().findAll(from, count)
        }, success)
    }
}