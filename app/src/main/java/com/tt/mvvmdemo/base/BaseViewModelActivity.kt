package com.tt.mvvmdemo.base

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tt.mvvmdemo.utils.toast
import retrofit2.HttpException
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException

abstract class BaseViewModelActivity<VM : BaseViewModel> : BaseActivity() {

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        initVM()
        super.onCreate(savedInstanceState)
        startObserve()
    }

    private fun initVM() {
        providerVMClass().let {
            viewModel = ViewModelProvider(this).get(it)
            lifecycle.addObserver(viewModel)
        }
    }

    abstract fun providerVMClass(): Class<VM>

    private fun startObserve() {
        viewModel.run {
            getError().observe(this@BaseViewModelActivity, {
                hideLoading()
                hideSearchLoading()
                requestError(it)
            })
            getFinally().observe(this@BaseViewModelActivity, {
                requestFinally(it)
            })
        }
    }

    open fun requestFinally(it: Int?) {}

    open fun requestError(it: Exception) {
        it?.run {
            when (it) {
                is CancellationException -> Log.d("${TAG}--->接口请求取消", it.message.toString())
                is TimeoutException -> toast("请求超时")
                is BaseRepository.TokenInvalidException -> toast("登录超时")
                is HttpException -> {
                    if (it.code() == 504) toast("无法连接服务器，请检查网络设置")
                    else toast(it.message.toString())
                }
                else -> toast(it.message.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }

}