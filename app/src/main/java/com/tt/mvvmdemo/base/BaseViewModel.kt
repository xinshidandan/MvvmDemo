package com.tt.mvvmdemo.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tt.mvvmdemo.constant.HttpConstant
import com.tt.mvvmdemo.utils.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.lang.Exception

open class BaseViewModel : ViewModel(), LifecycleObserver {

    private val error by lazy { SingleLiveEvent<Exception>() }

    private val finally by lazy { SingleLiveEvent<Int>() }

    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        try {
            withTimeout(HttpConstant.DEFAULT_TIMEOUT * 1000) {
                block()
            }
        } catch (e: Exception) {
            error.value = e
        } finally {
            finally.value = 200
        }
    }

    /**
     * 请求失败，出现异常
     */
    fun getError(): LiveData<Exception> = error

    /**
     * 请求完成，在此做一些关闭操作
     */
    fun getFinally(): LiveData<Int> = finally
}