package com.tt.mvvmdemo.mvvm.mainViewModel

import androidx.lifecycle.LiveData
import com.tt.mvvmdemo.httpUtils.WXChapterBean
import com.tt.mvvmdemo.mvvm.mainRepository.WeiXinRepository
import com.tt.mvvmdemo.mvvm.viewModel.CommonViewModel
import com.tt.mvvmdemo.utils.SingleLiveEvent

class WeiXinViewModel : CommonViewModel() {

    private val repository = WeiXinRepository()
    private val weiXinData = SingleLiveEvent<List<WXChapterBean>>()

    fun getWeiXin(): LiveData<List<WXChapterBean>> {
        launchUI {
            val res = repository.getWeiXin()
            weiXinData.value = res.data
        }
        return weiXinData
    }
}