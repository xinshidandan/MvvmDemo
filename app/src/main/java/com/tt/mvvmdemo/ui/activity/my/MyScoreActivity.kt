package com.tt.mvvmdemo.ui.activity.my

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelActivity
import com.tt.mvvmdemo.constant.Constant
import com.tt.mvvmdemo.mvvm.viewModel.MyScoreActivityViewModel
import com.tt.mvvmdemo.ui.adapter.ScoreAdapter
import com.tt.mvvmdemo.utils.AnimatorUtils
import com.tt.mvvmdemo.utils.MyMMKV.Companion.mmkv
import com.tt.mvvmdemo.utils.RvAnimUtils
import com.tt.mvvmdemo.utils.SettingUtil
import kotlinx.android.synthetic.main.activity_my_score.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class MyScoreActivity : BaseViewModelActivity<MyScoreActivityViewModel>() {

    private val mAdapter: ScoreAdapter by lazy { ScoreAdapter() }
    private val linearLayoutManager by lazy { LinearLayoutManager(this) }

    override fun providerVMClass() = MyScoreActivityViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_my_score

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData() {
        setTop("我的积分", R.drawable.paihangbang)
        toolbar_subtitle_image.setOnClickListener {
            startActivity(Intent(this, RankActivity::class.java))
        }
        wave_score?.run {
            setBorder(0, ContextCompat.getColor(this@MyScoreActivity, R.color.colorPrimary))
        }
    }

    override fun initView() {
        recyclerView1_score.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
        }
        mAdapter.run {
            recyclerView = recyclerView1_score
            loadMoreModule.setOnLoadMoreListener {
                getScoreList(data.size / pageSize)
            }
        }
        RvAnimUtils.setAnim(mAdapter, SettingUtil.getListAnimal())
    }

    override fun startHttp() {
        showLoading()
        getScoreList(0)
        if (mmkv.decodeInt(Constant.SCORE_UNM, 0) == 0) {
            getUserInfo()
        } else {
            AnimatorUtils.doIntAnimator(tv_score, 0, mmkv.decodeInt(Constant.SCORE_UNM), 1000)
        }
    }

    private fun getUserInfo() {
        viewModel.getUserInfo().observe(this, {
            mmkv.encode(Constant.SCORE_UNM, it.coinCount)
            AnimatorUtils.doIntAnimator(tv_score, 0, it.coinCount, 1000)
        })
    }

    private fun getScoreList(page: Int) {
        viewModel.getScoreList(page + 1).observe(this, {
            hideLoading()
            it.datas.let { scoreList ->
                mAdapter.run {
                    if (page == 1) setList(scoreList)
                    else addData(scoreList)
                    if (it.over) loadMoreModule.loadMoreEnd()
                    else loadMoreModule.loadMoreComplete()
                }
            }
        })
    }

    override fun requestError(it: Exception) {
        super.requestError(it)
        mAdapter.loadMoreModule.loadMoreFail()
    }

    override fun onResume() {
        super.onResume()
        wave_score?.start()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onPause() {
        super.onPause()
        wave_score?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        wave_score?.end()
    }

}