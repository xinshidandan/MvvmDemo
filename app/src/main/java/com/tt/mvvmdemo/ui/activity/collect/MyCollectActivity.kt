package com.tt.mvvmdemo.ui.activity.collect

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelActivity
import com.tt.mvvmdemo.mvvm.viewModel.MyCollectActivityViewModel
import com.tt.mvvmdemo.ui.adapter.CollectAdapter
import com.tt.mvvmdemo.utils.isInvalidClick
import com.tt.mvvmdemo.webView.WebViewActivity
import kotlinx.android.synthetic.main.activity_my_collect.*

class MyCollectActivity : BaseViewModelActivity<MyCollectActivityViewModel>() {

    private val mAdapter by lazy { CollectAdapter() }
    private var isRefresh = false
    private lateinit var refreshLayout: SmartRefreshLayout
    private val linearLayoutManager by lazy { LinearLayoutManager(this) }

    override fun providerVMClass() = MyCollectActivityViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_my_collect

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData() {
        setTop("我的收藏")
    }

    override fun initView() {
        refreshLayout = swipeRefresh_collect
        refreshLayout.setRefreshHeader(ch_header_collect)
        refreshLayout.setOnRefreshListener {
            mAdapter.loadMoreModule.isEnableLoadMore = false
            startHttp()
        }
        recyclerView_collect.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
        }
        mAdapter.run {
            recyclerView = recyclerView_collect
            setOnItemClickListener { adapter, view, position ->
                if (data.size != 0) {
                    val data = data[position]
                    WebViewActivity.start(this@MyCollectActivity, data.id, data.title, data.link)
                }
            }
            loadMoreModule.setOnLoadMoreListener {
                isRefresh = false
                refreshLayout.finishRefresh()
                val page = mAdapter.data.size / pageSize
                getCollectList(page)
            }
            addChildClickViewIds(R.id.iv_like)

            setOnItemClickListener { adapter, view, position ->
                if (isInvalidClick(view)) return@setOnItemClickListener
                if (data.size == 0) return@setOnItemClickListener
                when (view.id) {
                    R.id.iv_like -> {
                        removeCollectArticle(data[position].id, data[position].originId)
                        mAdapter.removeAt(position)
                    }
                }
            }
        }
    }

    private fun removeCollectArticle(id: Int, originId: Int) {
        showLoading()
        viewModel.removeCollectArticle(id, originId).observe(this@MyCollectActivity, {
            hideLoading()
        })
    }

    override fun startHttp() {
        showLoading()
        isRefresh = true
        getCollectList(0)
    }

    private fun getCollectList(page: Int) {
        viewModel.getCollectList(page).observe(this, {
            it.datas.let { collectList ->
                hideLoading()
                mAdapter.run {
                    if (isRefresh) {
                        refreshLayout.finishRefresh()
                        setList(collectList)
                    } else addData(collectList)
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