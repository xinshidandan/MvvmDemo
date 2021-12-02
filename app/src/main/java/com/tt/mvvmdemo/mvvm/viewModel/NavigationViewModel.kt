package com.tt.mvvmdemo.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.tt.mvvmdemo.httpUtils.NavigationBean
import com.tt.mvvmdemo.mvvm.mainRepository.SystemRepository
import com.tt.mvvmdemo.utils.SingleLiveEvent

class NavigationViewModel : CommonViewModel() {
    private val repository = SystemRepository()
    private val navigationTree = SingleLiveEvent<List<NavigationBean>>()

    fun getNavigationTree(): LiveData<List<NavigationBean>> {
        launchUI {
            val res = repository.getNavigationTree()
            navigationTree.value = res.data
        }
        return navigationTree
    }
}