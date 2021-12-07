package com.tt.mvvmdemo.db.repository

import android.widget.Toast
import com.tt.mvvmdemo.base.BaseApplication
import kotlinx.coroutines.*

open class BaseDBRepository : CoroutineScope by MainScope() {

    fun <T> execute(
        runnable: suspend () -> T,
        success: ((t: T) -> Unit)? = null,
        error: ((e: Throwable) -> Unit)? = null
    ) {
        launch(CoroutineExceptionHandler { _, _ -> }) {
            val result: T = withContext(Dispatchers.IO) {
                runnable.invoke()
            }
            success?.invoke(result)
        }.invokeOnCompletion {
            if (it != null) {
                Toast.makeText(
                    BaseApplication.mContext,
                    it.message ?: "本地数据库异常",
                    Toast.LENGTH_SHORT
                ).show()
            }
            it?.let {
                error?.invoke(it)
            }
        }
    }
}