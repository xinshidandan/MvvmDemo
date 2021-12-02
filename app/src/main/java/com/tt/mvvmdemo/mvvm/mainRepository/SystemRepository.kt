package com.tt.mvvmdemo.mvvm.mainRepository

import com.tt.mvvmdemo.httpUtils.*
import com.tt.mvvmdemo.mvvm.respository.CommonRepository

class SystemRepository : CommonRepository() {

    suspend fun getKnowledgeTree(): ResponseData<List<KnowledgeTreeBody>> = request {
        RetrofitClient.service.getKnowledgeTree()
    }

    suspend fun getNavigationTree(): ResponseData<List<NavigationBean>> = request {
        RetrofitClient.service.getNavigationList()
    }

    suspend fun getKnowledgeList(page: Int, cid: Int): ResponseData<ArticleResponseBody> = request {
        RetrofitClient.service.getKnowledgeList(page, cid)
    }
}