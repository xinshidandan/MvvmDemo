package com.tt.mvvmdemo.ui.fragment

import android.app.ActivityOptions
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelFragment
import com.tt.mvvmdemo.httpUtils.Article
import com.tt.mvvmdemo.mvvm.viewModel.NavigationViewModel
import com.tt.mvvmdemo.ui.adapter.NavigationAdapter
import com.tt.mvvmdemo.utils.RvAnimUtils
import com.tt.mvvmdemo.utils.SettingUtil
import com.tt.mvvmdemo.webView.WebViewActivity
import kotlinx.android.synthetic.main.navigation_fragment.*
import java.lang.Exception

class NavigationFragment : BaseViewModelFragment<NavigationViewModel>() {

    companion object {
        fun newInstance() = NavigationFragment()
    }

    private val linearLayoutManager by lazy { LinearLayoutManager(activity) }
    private val navigationAdapter by lazy { NavigationAdapter() }

    override fun providerVMClass(): Class<NavigationViewModel>? = NavigationViewModel::class.java

    override fun getLayoutId(): Int = R.layout.navigation_fragment

    override fun initData() {}

    override fun initView(view: View) {

        recyclerView_nav.run {
            layoutManager = linearLayoutManager
            adapter = navigationAdapter
        }
        navigationAdapter.run {
            recyclerView = recyclerView_nav
            setOnItemClickListener(object : NavigationAdapter.OnItemClickListener {
                override fun onClick(bean: Article, pos: Int) {
                    val options: ActivityOptions = ActivityOptions.makeScaleUpAnimation(
                        view,
                        view.width / 2,
                        view.height / 2,
                        0,
                        0
                    )
                    val data: Article = bean
                    WebViewActivity.start(
                        context,
                        data.id,
                        data.title,
                        data.link,
                        options.toBundle()
                    )
                }
            })
        }
        RvAnimUtils.setAnim(navigationAdapter, SettingUtil.getListAnimal()!!)
        LiveEventBus.get("rv_anim").observe(this, {
            RvAnimUtils.setAnim(navigationAdapter, it)
        })
    }

    override fun startHttp() {
        initKnowledgeTree()
    }

    private fun initKnowledgeTree() {
        viewModel = ViewModelProvider(this).get(NavigationViewModel::class.java)
        viewModel.getNavigationTree().observe(activity!!, { list ->
            navigationAdapter.run {
                setList(list)
                if (data.size == 0) setEmptyView(R.layout.fragment_empty_layout)
                else if (hasEmptyView()) removeEmptyView()
            }
        })
    }

    override fun requestError(it: Exception?) {
        super.requestError(it)
        navigationAdapter.loadMoreModule.loadMoreFail()
    }
}