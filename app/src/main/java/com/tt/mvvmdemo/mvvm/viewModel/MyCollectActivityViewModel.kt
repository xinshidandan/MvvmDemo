package com.tt.mvvmdemo.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.tt.mvvmdemo.httpUtils.BaseListResponseBody
import com.tt.mvvmdemo.httpUtils.CollectionArticle
import com.tt.mvvmdemo.mvvm.respository.CollectRepository
import com.tt.mvvmdemo.utils.SingleLiveEvent

class MyCollectActivityViewModel : CommonViewModel() {

    private val repository = CollectRepository()
    private val collectList = SingleLiveEvent<BaseListResponseBody<CollectionArticle>>()

    fun getCollectList(page: Int): LiveData<BaseListResponseBody<CollectionArticle>> {
        launchUI {
            val res = repository.getCollectList(page)
            collectList.value = res.data
        }
        return collectList
    }


}