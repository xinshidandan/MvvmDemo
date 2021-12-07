package com.tt.mvvmdemo.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.tt.mvvmdemo.httpUtils.BaseListResponseBody
import com.tt.mvvmdemo.httpUtils.CoinInfoBean
import com.tt.mvvmdemo.httpUtils.UserInfoBody
import com.tt.mvvmdemo.httpUtils.UserScoreBean
import com.tt.mvvmdemo.mvvm.respository.ScoreRepository
import com.tt.mvvmdemo.utils.SingleLiveEvent

class MyScoreActivityViewModel : CommonViewModel() {
    private val repository = ScoreRepository()
    private val data = SingleLiveEvent<BaseListResponseBody<UserScoreBean>>()
    private val userInfo = SingleLiveEvent<UserInfoBody>()
    private val rankList = SingleLiveEvent<BaseListResponseBody<CoinInfoBean>>()

    fun getScoreList(page: Int): LiveData<BaseListResponseBody<UserScoreBean>> {
        launchUI {
            val result = repository.getScoreList(page)
            data.value = result.data
        }
        return data
    }

    fun getUserInfo(): LiveData<UserInfoBody> {
        launchUI {
            val res = repository.getUserInfo()
            userInfo.value = res.data
        }
        return userInfo
    }

    fun getRankList(page: Int): LiveData<BaseListResponseBody<CoinInfoBean>> {
        launchUI {
            val res = repository.getRankList(page)
            rankList.value = res.data
        }
        return rankList
    }


}