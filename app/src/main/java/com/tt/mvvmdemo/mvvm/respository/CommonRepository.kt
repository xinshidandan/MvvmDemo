package com.tt.mvvmdemo.mvvm.respository

import com.tt.mvvmdemo.base.BaseRepository
import com.tt.mvvmdemo.httpUtils.LoginData
import com.tt.mvvmdemo.httpUtils.ResponseData
import com.tt.mvvmdemo.httpUtils.RetrofitClient

open class CommonRepository : BaseRepository() {

    suspend fun login(name:String, psw:String):ResponseData<LoginData> = request {
        RetrofitClient.service.login(name, psw)
    }




}