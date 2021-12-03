package com.tt.mvvmdemo.mvvm.mainRepository

import com.tt.mvvmdemo.httpUtils.ArticleListBean
import com.tt.mvvmdemo.httpUtils.ResponseData
import com.tt.mvvmdemo.httpUtils.RetrofitClient
import com.tt.mvvmdemo.mvvm.respository.CommonRepository

class QuestionRepository : CommonRepository() {
    suspend fun getQuestionList(page: Int): ResponseData<ArticleListBean> = request {
        RetrofitClient.service.getQuestionList(page)
    }
}