package com.tt.mvvmdemo.mvvm.respository

import com.tt.mvvmdemo.httpUtils.BaseListResponseBody
import com.tt.mvvmdemo.httpUtils.CollectionArticle
import com.tt.mvvmdemo.httpUtils.ResponseData
import com.tt.mvvmdemo.httpUtils.RetrofitClient

class CollectRepository : CommonRepository() {

    suspend fun getCollectList(page: Int): ResponseData<BaseListResponseBody<CollectionArticle>> =
        request {
            RetrofitClient.service.getCollectList(page)
        }
}