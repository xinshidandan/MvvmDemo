package com.tt.mvvmdemo.ui.activity.share

import android.os.Build
import androidx.annotation.RequiresApi
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelActivity
import com.tt.mvvmdemo.mvvm.viewModel.ShareActivityViewModel
import com.tt.mvvmdemo.utils.toast
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class ShareActivity : BaseViewModelActivity<ShareActivityViewModel>() {

    override fun providerVMClass() = ShareActivityViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_share

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData() {
        setTop("分享文章", "分享")
        toolbar_subtitle.setOnClickListener { shareArticle() }
    }

    override fun initView() {}

    override fun startHttp() {}

    private fun shareArticle() {
        val title = et_article_title.text.toString().trim()
        val link = et_article_link.text.toString().trim()
        if (title.isEmpty()) {
            toast("文章标题不能为空")
            return
        }
        if (link.isEmpty()) {
            toast("文章连接不能为空")
            return
        }
        val map = mutableMapOf<String, Any>()
        map["title"] = title
        map["link"] = link
        viewModel.shareArticle(map).observe(this, {
            toast("提交成功")
            setResult(RESULT_OK)
            this@ShareActivity.finish()
        })
    }

}