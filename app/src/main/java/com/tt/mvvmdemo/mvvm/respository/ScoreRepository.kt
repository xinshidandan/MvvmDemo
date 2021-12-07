package com.tt.mvvmdemo.mvvm.respository

import com.tt.mvvmdemo.httpUtils.*

class ScoreRepository : CommonRepository() {

    suspend fun getScoreList(page: Int): ResponseData<BaseListResponseBody<UserScoreBean>> =
        request {
            RetrofitClient.service.getUserScoreList(page)
        }

    suspend fun getUserInfo(): ResponseData<UserInfoBody> = request {
        RetrofitClient.service.getUserInfo()
    }

    suspend fun getRankList(page: Int): ResponseData<BaseListResponseBody<CoinInfoBean>> = request {
        RetrofitClient.service.getRankList(page)
    }
}