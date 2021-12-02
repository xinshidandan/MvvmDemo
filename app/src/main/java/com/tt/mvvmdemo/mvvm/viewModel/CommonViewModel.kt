package com.tt.mvvmdemo.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.tt.mvvmdemo.base.BaseViewModel
import com.tt.mvvmdemo.httpUtils.LoginData
import com.tt.mvvmdemo.httpUtils.TodoResponseBody
import com.tt.mvvmdemo.mvvm.respository.CommonRepository
import com.tt.mvvmdemo.utils.SingleLiveEvent

/**
 * 通用的ViewModel，如收藏， 登录等接口很多页面都用
 */
open class CommonViewModel : BaseViewModel() {

    private val repository = CommonRepository()
    private val addCollect = SingleLiveEvent<Any>()
    private val addCollectOutside = SingleLiveEvent<Any>()
    private val cancelCollect = SingleLiveEvent<Any>()
    private val removeCollect = SingleLiveEvent<Any>()
    private val loginData = SingleLiveEvent<LoginData>()
    private val logoutData = SingleLiveEvent<Any>()
    private val registerData = SingleLiveEvent<LoginData>()
    private val data = SingleLiveEvent<TodoResponseBody>()

    /**
     * 如果需要对返回的数据进行操作后再返回给调用者
     * 可以使用Transformations.map(data) {}或 Transformations.switchMap(data){} 对data进行处理
     * 详见 https://developer.android.google.cn/topic/libraries/architecture/livedata
     */
    fun getTodoList(page: Int, map: MutableMap<String, Any>): LiveData<TodoResponseBody> {
        launchUI {
            val res = repository.getTodoList(page, map)
            data.value = res.data
        }
        return data
    }

    fun addCollectArticle(id: Int): LiveData<Any> {
        launchUI {
            val res = repository.addCollectArticle(id)
            addCollect.value = res
        }
        return addCollect
    }

    fun addCollectOutsideArticle(title: String, author: String, link: String): LiveData<Any> {
        launchUI {
            val res = repository.addCollectOutsideArticle(title, author, link)
            addCollectOutside.value = res
        }
        return addCollectOutside
    }

    fun cancelCollectArticle(id: Int): LiveData<Any> {
        launchUI {
            val res = repository.cancelCollectArticle(id)
            cancelCollect.value = res
        }
        return cancelCollect
    }

    fun removeCollectArticle(page: Int, originId: Int): LiveData<Any> {
        launchUI {
            val res = repository.removeCollectArticle(page, originId)
            removeCollect.value = res
        }
        return removeCollect
    }

    fun login(name: String, psw: String): LiveData<LoginData> {
        launchUI {
            val res = repository.login(name, psw)
            loginData.value = res.data
        }
        return loginData
    }

    fun logout(): LiveData<Any> {
        launchUI {
            val res = repository.logout()
            logoutData.value = res
        }
        return logoutData
    }

    fun register(name: String, psw: String, rePws: String): LiveData<LoginData> {
        launchUI {
            val res = repository.register(name, psw, rePws)
            registerData.value = res.data
        }
        return registerData
    }

}