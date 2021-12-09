package com.tt.mvvmdemo.ui.activity.my

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.tt.mvvmdemo.BuildConfig
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseActivity
import com.tt.mvvmdemo.webView.WebViewActivity
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : BaseActivity(), View.OnClickListener {

    override fun getLayoutId() = R.layout.activity_about

    override fun initData() {}

    override fun startHttp() {}

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initView() {
        setTop("关于")
        tv_version_name.text = "Version " + BuildConfig.VERSION_NAME
        ll_wan_web.setOnClickListener(this)
        ll_web_content.setOnClickListener(this)
        ll_source_code.setOnClickListener(this)
        ll_statement.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            ll_wan_web -> WebViewActivity.start(this, "https://www.wanandroid.com")
            ll_web_content -> WebViewActivity.start(this, "https://www.wanandroid.com/about")
            ll_source_code -> WebViewActivity.start(this, "https://github.com/jzh1996/mvvm")
            ll_statement -> {
                AlertDialog.Builder(this)
                    .setTitle("版权声明")
                    .setMessage(resources.getString(R.string.copyright_content))
                    .setCancelable(true)
                    .show()
            }
        }
    }


}