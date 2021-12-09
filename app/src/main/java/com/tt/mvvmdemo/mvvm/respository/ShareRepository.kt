package com.tt.mvvmdemo.mvvm.respository

import com.tt.mvvmdemo.httpUtils.ResponseData
import com.tt.mvvmdemo.httpUtils.RetrofitClient
import com.tt.mvvmdemo.httpUtils.ShareResponseBody

class ShareRepository : CommonRepository() {

    suspend fun shareArticle(map: MutableMap<String, Any>): ResponseData<Any> = request {
        RetrofitClient.service.shareArticle(map)
    }

    suspend fun getShareList(page: Int): ResponseData<ShareResponseBody> = request {
        RetrofitClient.service.getShareList(page)
    }

    suspend fun deleteShareArticle(id: Int): ResponseData<Any> = request {
        RetrofitClient.service.deleteShareArticle(id)
    }
}