package com.tt.mvvmdemo.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.tt.mvvmdemo.mvvm.respository.TodoRepository
import com.tt.mvvmdemo.utils.SingleLiveEvent

class TodoActivityViewModel : CommonViewModel() {

    private val repository = TodoRepository()
    private var updateTodoById = SingleLiveEvent<Any>()
    private var deleteTodoById = SingleLiveEvent<Any>()
    private var addTodo = SingleLiveEvent<Any>()
    private var updateTodo = SingleLiveEvent<Any>()


    fun updateTodoById(id: Int, status: Int): LiveData<Any> {
        launchUI {
            val res = repository.updateTodoById(id, status)
            updateTodoById.value = res.data
        }
        return updateTodoById
    }

    fun deleteTodoById(id: Int): LiveData<Any> {
        launchUI {
            val res = repository.deleteTodoById(id)
            deleteTodoById.value = res.data
        }
        return deleteTodoById
    }

    fun addTodo(map: MutableMap<String, Any>): LiveData<Any> {
        launchUI {
            val res = repository.addTodo(map)
            addTodo.value = res.data
        }
        return addTodo
    }

    fun updateTodo(id: Int, map: MutableMap<String, Any>): LiveData<Any> {
        launchUI {
            val res = repository.updateTodo(id, map)
            updateTodo.value = res.data
        }
        return updateTodo
    }


}