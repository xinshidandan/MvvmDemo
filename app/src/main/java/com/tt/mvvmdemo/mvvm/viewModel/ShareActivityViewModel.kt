package com.tt.mvvmdemo.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.tt.mvvmdemo.httpUtils.ShareResponseBody
import com.tt.mvvmdemo.mvvm.respository.ShareRepository
import com.tt.mvvmdemo.utils.SingleLiveEvent

class ShareActivityViewModel : CommonViewModel() {

    private val repository = ShareRepository()
    private var data = SingleLiveEvent<Any>()
    private var shareList = SingleLiveEvent<ShareResponseBody>()
    private var delete = SingleLiveEvent<Any>()

    fun shareArticle(map: MutableMap<String, Any>): LiveData<Any> {
        launchUI {
            val res = repository.shareArticle(map)
            data.value = res.data
        }
        return data
    }

    fun getShareList(page: Int): LiveData<ShareResponseBody> {
        launchUI {
            val res = repository.getShareList(page)
            shareList.value = res.data
        }
        return shareList
    }

    fun deleteShareArticle(id: Int): LiveData<Any> {
        launchUI {
            val res = repository.deleteShareArticle(id)
            delete.value = res.data
        }
        return delete
    }
}