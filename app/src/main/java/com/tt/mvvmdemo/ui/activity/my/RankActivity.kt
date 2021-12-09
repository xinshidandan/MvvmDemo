package com.tt.mvvmdemo.ui.activity.my

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelActivity
import com.tt.mvvmdemo.mvvm.viewModel.MyScoreActivityViewModel
import com.tt.mvvmdemo.ui.adapter.RankAdapter
import com.tt.mvvmdemo.utils.RvAnimUtils
import com.tt.mvvmdemo.utils.SettingUtil
import com.tt.mvvmdemo.webView.WebViewActivity
import kotlinx.android.synthetic.main.activity_rank.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class RankActivity : BaseViewModelActivity<MyScoreActivityViewModel>() {

    private val mAdapter: RankAdapter by lazy { RankAdapter() }
    private var isRefresh = false
    private lateinit var refreshLayout: SmartRefreshLayout
    private val linearLayoutManager by lazy { LinearLayoutManager(this) }

    override fun providerVMClass() = MyScoreActivityViewModel::class.java

    override fun getLayoutId() = R.layout.activity_rank

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData() {
        setTop("积分排行榜", R.drawable.wenhao)
        toolbar_subtitle_image.setOnClickListener {
            val url = "https://www.wanandroid.com/blog/show/2653"
            WebViewActivity.start(this, 2653, "", url)
        }
    }

    override fun initView() {  //
        refreshLayout = swipeRefreshLayout1_integral
        refreshLayout.run {
            setRefreshHeader(ch_header_integral)
            setOnRefreshListener {
                startHttp()
            }
        }
        recyclerView1_integral.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
        }
        mAdapter.run {
            recyclerView = recyclerView1_integral
            loadMoreModule.setOnLoadMoreListener {
                isRefresh = false
                refreshLayout.finishRefresh()
                val page = mAdapter.data.size / pageSize
                getRankList(page)
            }
        }
        RvAnimUtils.setAnim(mAdapter, SettingUtil.getListAnimal())
    }

    override fun startHttp() {
        isRefresh = true
        showLoading()
        getRankList(0)
    }

    private fun getRankList(page: Int) {
        //该接口page从1开始
        viewModel.getRankList(page + 1).observe(this, {
            hideLoading()
            it.datas.let { scoreList ->
                mAdapter.run {
                    if (isRefresh) {
                        refreshLayout.finishRefresh()
                        setList(scoreList)
                        recyclerView.scrollToPosition(0)
                    } else addData(scoreList)
                    if (data.size == 0) setEmptyView(R.layout.fragment_empty_layout)
                    else if (hasEmptyView()) removeEmptyView()
                    if (it.over) loadMoreModule.loadMoreEnd(isRefresh)
                    else loadMoreModule.loadMoreComplete()
                }
            }
        })
    }

    override fun requestError(it: Exception) {
        super.requestError(it)
        mAdapter.loadMoreModule.loadMoreFail()
    }
}