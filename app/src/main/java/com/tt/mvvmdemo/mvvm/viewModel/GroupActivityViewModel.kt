package com.tt.mvvmdemo.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.tt.mvvmdemo.httpUtils.ArticleResponseBody
import com.tt.mvvmdemo.mvvm.respository.GroupRepository
import com.tt.mvvmdemo.utils.SingleLiveEvent

class GroupActivityViewModel : CommonViewModel() {

    private val repository = GroupRepository()
    private var groupList = SingleLiveEvent<ArticleResponseBody>()

    fun getGroupList(page: Int): LiveData<ArticleResponseBody> {
        launchUI {
            val res = repository.getGroupList(page)
            groupList.value = res.data
        }
        return groupList
    }
}