package com.tt.mvvmdemo.mvvm.mainRepository

import com.tt.mvvmdemo.httpUtils.ResponseData
import com.tt.mvvmdemo.httpUtils.RetrofitClient
import com.tt.mvvmdemo.httpUtils.UserInfoBody
import com.tt.mvvmdemo.mvvm.respository.CommonRepository

class MyRepositiry : CommonRepository() {

    suspend fun getUserInfo(): ResponseData<UserInfoBody> = request {
        RetrofitClient.service.getUserInfo()
    }
}