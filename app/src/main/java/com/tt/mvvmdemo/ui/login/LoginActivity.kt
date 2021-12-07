package com.tt.mvvmdemo.ui.login

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelActivity
import com.tt.mvvmdemo.constant.Constant
import com.tt.mvvmdemo.mvvm.viewModel.LoginViewModel
import com.tt.mvvmdemo.utils.MyMMKV.Companion.mmkv
import com.tt.mvvmdemo.utils.toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseViewModelActivity<LoginViewModel>() {
    override fun getLayoutId(): Int = R.layout.activity_login

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData() {
        setTop("登录")
        btn_login.setOnClickListener { login() }
        tv_register.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
    }

    override fun initView() {
    }

    override fun startHttp() {
    }

    override fun providerVMClass() = LoginViewModel::class.java

    private fun login() {
        val enterPriseName = et_login_id.text.toString().trim()
        val password = et_login_password.text.toString().trim()

        when {
            enterPriseName == "" -> toast("账号不能为空")
            password == "" -> toast("密码不能为空")
            else -> {
                viewModel.login(enterPriseName, password).observe(this, {
                    mmkv?.encode(Constant.IS_LOGIN, true)
                    LiveEventBus.get(Constant.IS_LOGIN).post(true)
                    finish()
                })
            }
        }
    }
}