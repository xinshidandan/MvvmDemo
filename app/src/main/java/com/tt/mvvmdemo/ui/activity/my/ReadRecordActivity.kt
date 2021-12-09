package com.tt.mvvmdemo.ui.activity.my

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelActivity
import com.tt.mvvmdemo.mvvm.viewModel.RoomViewModel
import com.tt.mvvmdemo.ui.adapter.ReadRecordAdapter
import com.tt.mvvmdemo.ui.view.MyDialog
import com.tt.mvvmdemo.ui.view.SwipeItemLayout
import com.tt.mvvmdemo.utils.RvAnimUtils
import com.tt.mvvmdemo.utils.SettingUtil
import com.tt.mvvmdemo.webView.WebViewActivity
import kotlinx.android.synthetic.main.activity_later_read.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class ReadRecordActivity : BaseViewModelActivity<RoomViewModel>() {

    private val mAdapter by lazy { ReadRecordAdapter() }
    private var isRefresh = false
    private lateinit var refreshLayout: SmartRefreshLayout
    private val linearLayoutManager by lazy { LinearLayoutManager(this) }
    private lateinit var clearDialog: MyDialog

    override fun providerVMClass() = RoomViewModel::class.java

    override fun getLayoutId() = R.layout.activity_later_read

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData() {
        setTop("阅读历史", "清空")
        toolbar_subtitle.setOnClickListener {
            if (!this::clearDialog.isInitialized) {
                clearDialog = MyDialog(this)
                clearDialog.run {
                    setDialogText("清空后无法还原,确认全部删除吗")
                    setClickListener { v ->
                        when (v.id) {
                            R.id.tv_dialog_sure -> {
                                if (clearDialog.isShowing) clearDialog.dismiss()
                                viewModel.removeAllRecord().observe(this@ReadRecordActivity) {
                                    mAdapter.setList(mutableListOf())
                                    mAdapter.setEmptyView(R.layout.fragment_empty_layout)
                                }
                            }
                            R.id.tv_dialog_cancle -> if (clearDialog.isShowing) clearDialog.dismiss()
                        }
                    }
                    show()
                }
            } else clearDialog.show()
        }
    }

    override fun initView() {
        refreshLayout = swipeRefreshLayout_later_read
        refreshLayout.run {
            setRefreshHeader(ch_header_later_read)
            setOnRefreshListener {
                startHttp()
            }
        }
        recyclerView_later_read.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
            addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(this@ReadRecordActivity))
        }
        mAdapter.run {
            recyclerView = recyclerView_later_read
            loadMoreModule.setOnLoadMoreListener {
                isRefresh = false
                refreshLayout.finishRefresh()
                val page = mAdapter.data.size / pageSize
                getReadRecordList(page)
            }
            addChildClickViewIds(R.id.rl_read, R.id.btn_delete_read)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.rl_read -> WebViewActivity.start(
                        this@ReadRecordActivity,
                        data[position].link
                    )
                    R.id.btn_delete_read -> {
                        viewModel.removeRecord(data[position].link)
                            .observe(this@ReadRecordActivity) {
                                adapter.removeAt(position)
                            }
                    }
                }
            }
        }
        RvAnimUtils.setAnim(mAdapter, SettingUtil.getListAnimal())
    }

    override fun startHttp() {
        showLoading()
        isRefresh = true
        getReadRecordList(0)
    }

    private fun getReadRecordList(page: Int) {
        val from = if (page == 0) 0
        else page * pageSize
        viewModel.getRecordList(from, pageSize).observe(this) {
            hideLoading()
            it.let { laterList ->
                mAdapter.run {
                    if (isRefresh) {
                        refreshLayout.finishRefresh()
                        setList(laterList)
                        recyclerView.scrollToPosition(0)
                    } else addData(laterList)
                    if (data.size == 0) setEmptyView(R.layout.fragment_empty_layout)
                    else if (hasEmptyView()) removeEmptyView()
                    if (laterList.size < pageSize) loadMoreModule.loadMoreEnd(isRefresh)
                    else loadMoreModule.loadMoreComplete()
                }
            }
        }
    }
}