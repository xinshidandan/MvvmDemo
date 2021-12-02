package com.tt.mvvmdemo.mvvm.mainRepository

import com.tt.mvvmdemo.httpUtils.ResponseData
import com.tt.mvvmdemo.httpUtils.RetrofitClient
import com.tt.mvvmdemo.httpUtils.WXChapterBean
import com.tt.mvvmdemo.mvvm.respository.CommonRepository

class WeiXinRepository : CommonRepository() {

    suspend fun getWeiXin(): ResponseData<List<WXChapterBean>> = request {
        RetrofitClient.service.getWeiXin()
    }
}