package com.tt.mvvmdemo.mvvm.respository

import com.tt.mvvmdemo.httpUtils.ArticleResponseBody
import com.tt.mvvmdemo.httpUtils.ResponseData
import com.tt.mvvmdemo.httpUtils.RetrofitClient

class GroupRepository:CommonRepository() {

    suspend fun getGroupList(page:Int):ResponseData<ArticleResponseBody> = request {
        RetrofitClient.service.getGroupList(page)
    }
}