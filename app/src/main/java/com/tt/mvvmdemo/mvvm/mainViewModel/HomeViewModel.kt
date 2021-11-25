package com.tt.mvvmdemo.mvvm.mainViewModel

import com.tt.mvvmdemo.httpUtils.Article
import com.tt.mvvmdemo.httpUtils.ArticleResponseBody
import com.tt.mvvmdemo.httpUtils.Banner
import com.tt.mvvmdemo.httpUtils.HotSearchBean
import com.tt.mvvmdemo.mvvm.mainRepository.HomeRepository
import com.tt.mvvmdemo.mvvm.viewModel.CommonViewModel
import com.tt.mvvmdemo.utils.SingleLiveEvent

class HomeViewModel : CommonViewModel() {

    private val repository = HomeRepository()
    private val bannerDatas = SingleLiveEvent<List<Banner>>()
    private val articlesDatas = SingleLiveEvent<List<Article>>()
    private var topArticlesDatas = SingleLiveEvent<List<Article>>()
    private val searchData = SingleLiveEvent<ArticleResponseBody>()
    private val hotSearchData = SingleLiveEvent<MutableList<HotSearchBean>>()

    private var topArticlesList = mutableListOf<Article>()
    private var articlesList = mutableListOf<Article>()



}