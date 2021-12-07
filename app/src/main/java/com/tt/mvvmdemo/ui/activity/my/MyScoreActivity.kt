package com.tt.mvvmdemo.ui.activity.my

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelActivity
import com.tt.mvvmdemo.mvvm.viewModel.MyScoreActivityViewModel
import com.tt.mvvmdemo.ui.adapter.ScoreAdapter
import kotlinx.android.synthetic.main.toolbar_layout.*

class MyScoreActivity : BaseViewModelActivity<MyScoreActivityViewModel>() {

    private val mAdapter: ScoreAdapter by lazy { ScoreAdapter() }
    private val linearLayoutManager by lazy { LinearLayoutManager(this) }


    override fun getLayoutId(): Int = R.layout.activity_my_score

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData() {
        setTop("我的积分", R.drawable.paihangbang)
        toolbar_subtitle_image.setOnClickListener {  }

    }

    override fun initView() {

    }

    override fun startHttp() {

    }

    override fun providerVMClass() = MyScoreActivityViewModel::class.java
}