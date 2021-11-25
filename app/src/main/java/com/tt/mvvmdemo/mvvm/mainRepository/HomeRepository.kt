package com.tt.mvvmdemo.mvvm.mainRepository

import com.tt.mvvmdemo.httpUtils.*
import com.tt.mvvmdemo.mvvm.respository.CommonRepository

class HomeRepository : CommonRepository() {

    suspend fun getBanner(): ResponseData<List<Banner>> = request {
        RetrofitClient.service.getBanners()
    }


    suspend fun getArticles(page: Int): ResponseData<ArticleResponseBody> = request {
        RetrofitClient.service.getArticles(page)
    }

    suspend fun getTopArticles():ResponseData<MutableList<Article>> = request {
        RetrofitClient.service.getTopArticles()
    }

    suspend fun queryBySearchKey(page:Int, key:String):ResponseData<ArticleResponseBody> = request {
        RetrofitClient.service.queryBySearchKey(page, key)
    }

    suspend fun getHotSearchData():ResponseData<MutableList<HotSearchBean>> = request {
        RetrofitClient.service.getHotSearchData()
    }


}