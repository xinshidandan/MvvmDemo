package com.tt.mvvmdemo.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.tt.mvvmdemo.httpUtils.ArticleResponseBody
import com.tt.mvvmdemo.mvvm.mainRepository.SystemRepository
import com.tt.mvvmdemo.utils.SingleLiveEvent

class KnowListViewModel : CommonViewModel() {

    private val repository = SystemRepository()
    private val articleResponse = SingleLiveEvent<ArticleResponseBody>()

    fun getKnowledgeList(page: Int, cid: Int): LiveData<ArticleResponseBody> {
        launchUI {
            val res = repository.getKnowledgeList(page, cid)
            articleResponse.value = res.data
        }
        return articleResponse
    }
}