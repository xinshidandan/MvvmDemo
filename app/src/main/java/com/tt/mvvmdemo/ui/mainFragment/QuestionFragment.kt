package com.tt.mvvmdemo.ui.mainFragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelFragment
import com.tt.mvvmdemo.constant.Constant
import com.tt.mvvmdemo.mvvm.mainViewModel.QuestionViewModel
import com.tt.mvvmdemo.ui.adapter.HomeAdapter
import com.tt.mvvmdemo.ui.login.LoginActivity
import com.tt.mvvmdemo.utils.MyMMKV
import com.tt.mvvmdemo.utils.RvAnimUtils
import com.tt.mvvmdemo.utils.SettingUtil
import com.tt.mvvmdemo.webView.WebViewActivity
import kotlinx.android.synthetic.main.question_fragment.*
import java.lang.Exception

class QuestionFragment : BaseViewModelFragment<QuestionViewModel>() {

    companion object {
        fun newInstance() = QuestionFragment()
    }

    private var isRefresh = true
    private lateinit var refreshLayout: SmartRefreshLayout
    private val linearLayoutManager by lazy { LinearLayoutManager(activity) }
    private val questionAdapter by lazy { HomeAdapter() }

    override fun providerVMClass(): Class<QuestionViewModel>? = QuestionViewModel::class.java

    override fun getLayoutId(): Int = R.layout.question_fragment

    override fun initData() {}

    override fun initView(view: View) {

        refreshLayout = swipeRefreshLayout1_question
        refreshLayout.run {
            setRefreshHeader(ch_header_question)
            setOnRefreshListener { startHttp() }
        }

        recyclerView1_question.run {
            layoutManager = linearLayoutManager
            adapter = questionAdapter
            itemAnimator = DefaultItemAnimator()
        }
        questionAdapter.run {
            recyclerView = recyclerView1_question
            setOnItemClickListener { adapter, view, position ->
                if (data.size != 0) {
                    val data = data[position]
                    WebViewActivity.start(activity, data.id, data.title, data.link)
                }
            }
            loadMoreModule.setOnLoadMoreListener {
                isRefresh = false
                refreshLayout.finishRefresh()
                val page = questionAdapter.data.size / pageSize
                getQuestionList(page)
            }
            addChildClickViewIds(R.id.iv_like)
            setOnItemChildClickListener { adapter, view, position ->
                if (data.size == 0) return@setOnItemChildClickListener
                val res = data[position]
                when (view.id) {
                    R.id.iv_like -> {
                        if (!MyMMKV.mmkv.decodeBool(Constant.IS_LOGIN, false)) {
                            startActivity(Intent(activity, LoginActivity::class.java))
                            return@setOnItemChildClickListener
                        }
                        val collect = res.collect
                        res.collect = !collect
                        setData(position, res)
                        if (collect) viewModel.cancelCollectArticle(res.id).observe(activity!!, {})
                        else viewModel.addCollectArticle(res.id).observe(activity!!, {})
                    }
                }
            }
        }
        RvAnimUtils.setAnim(questionAdapter, SettingUtil.getListAnimal())
        LiveEventBus.get("rv_anim").observe(this, {
            RvAnimUtils.setAnim(questionAdapter, it)
        })

    }

    override fun startHttp() {
        showLoading()
        isRefresh = true
        getQuestionList(0)
    }

    private fun getQuestionList(page: Int) {
        viewModel.getQuestionList(page).observe(activity!!, {
            it.datas.let { article ->
                hideLoading()
                questionAdapter.run {
                    if (isRefresh) {
                        refreshLayout.finishRefresh()
                        setList(article)
                        recyclerView.scrollToPosition(0)
                    } else addData(article)
                    if (data.size == 0) setEmptyView(R.layout.fragment_empty_layout)
                    else if (hasEmptyView()) removeEmptyView()
                    if (it.over) loadMoreModule.loadMoreEnd(isRefresh)
                    else loadMoreModule.loadMoreComplete()
                }
            }
        })
    }

    override fun requestError(it: Exception?) {
        super.requestError(it)
        questionAdapter.loadMoreModule.loadMoreFail()
    }
}