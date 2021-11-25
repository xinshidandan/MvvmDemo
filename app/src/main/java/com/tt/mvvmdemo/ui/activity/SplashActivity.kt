package com.tt.mvvmdemo.ui.activity

import android.content.Intent
import com.tt.mvvmdemo.MainActivity
import com.tt.mvvmdemo.base.BaseActivity

class SplashActivity : BaseActivity() {
    override fun getLayoutId(): Int = 0

    override fun initData() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun initView() {
    }

    override fun startHttp() {
    }
}