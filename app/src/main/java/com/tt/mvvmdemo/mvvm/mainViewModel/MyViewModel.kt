package com.tt.mvvmdemo.mvvm.mainViewModel

import androidx.lifecycle.LiveData
import com.tt.mvvmdemo.httpUtils.UserInfoBody
import com.tt.mvvmdemo.mvvm.mainRepository.MyRepositiry
import com.tt.mvvmdemo.mvvm.viewModel.CommonViewModel
import com.tt.mvvmdemo.utils.SingleLiveEvent

class MyViewModel : CommonViewModel() {

    private val repository = MyRepositiry()
    private val userInfoData = SingleLiveEvent<UserInfoBody>()

    fun getUserInfo(): LiveData<UserInfoBody> {
        launchUI {
            val res = repository.getUserInfo()
            userInfoData.value = res.data
        }
        return userInfoData
    }
}