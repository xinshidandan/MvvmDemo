package com.tt.mvvmdemo.mvvm.respository

import com.tt.mvvmdemo.httpUtils.ResponseData
import com.tt.mvvmdemo.httpUtils.RetrofitClient

class TodoRepository : CommonRepository() {

    suspend fun updateTodoById(id: Int, status: Int): ResponseData<Any> = request {
        RetrofitClient.service.updateTodoById(id, status)
    }

    suspend fun deleteTodoById(page: Int): ResponseData<Any> = request {
        RetrofitClient.service.deleteTodoById(page)
    }

    suspend fun addTodo(map: MutableMap<String, Any>): ResponseData<Any> = request {
        RetrofitClient.service.addTodo(map)
    }

    suspend fun updateTodo(id: Int, map: MutableMap<String, Any>): ResponseData<Any> = request {
        RetrofitClient.service.updateTodo(id, map)
    }
}