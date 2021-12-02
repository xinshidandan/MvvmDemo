package com.tt.mvvmdemo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelFragment
import com.tt.mvvmdemo.constant.Constant
import com.tt.mvvmdemo.mvvm.viewModel.KnowListViewModel
import com.tt.mvvmdemo.ui.adapter.KnowledgeListAdapter

class KnowListFragment : BaseViewModelFragment<KnowListViewModel>() {

    companion object {
        fun newInstance(id: Int): KnowListFragment {
            val fragment = KnowListFragment()
            var args = Bundle()
            args.putInt(Constant.CONTENT_CID_KEY, id)
            fragment.arguments = args
            return fragment
        }
    }

    private var cid = 0
    private val knowLedgeListAdapter by lazy { KnowledgeListAdapter() }
    private var isRefresh = true
    private lateinit var refreshLayout: SmartRefreshLayout
    private val linearLayoutManager by lazy { LinearLayoutManager(activity) }

    override fun providerVMClass(): Class<KnowListViewModel>? = KnowListViewModel::class.java

    override fun getLayoutId(): Int = R.layout.know_list_fragment

    override fun initData() {
        cid = arguments?.getInt(Constant.CONTENT_CID_KEY) ?: 0
    }

    override fun initView(view: View) {

    }

    override fun startHttp() {

    }
}