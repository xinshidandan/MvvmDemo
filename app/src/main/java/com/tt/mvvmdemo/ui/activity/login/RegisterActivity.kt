package com.tt.mvvmdemo.ui.activity.login

import android.os.Build
import androidx.annotation.RequiresApi
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelActivity
import com.tt.mvvmdemo.mvvm.viewModel.LoginViewModel
import com.tt.mvvmdemo.utils.toast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseViewModelActivity<LoginViewModel>() {
    override fun getLayoutId(): Int = R.layout.activity_register

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData() {
        setTop("注册")
        btn_register.setOnClickListener { register() }
    }

    override fun initView() {
    }

    override fun startHttp() {}

    override fun providerVMClass() = LoginViewModel::class.java

    private fun register() {
        val enterPriseName = et_login_id.text.toString().trim()
        val password1 = et_login_password1.text.toString().trim()
        val password2 = et_login_password2.text.toString().trim()
        when {
            enterPriseName == "" -> toast("账号不能为空")
            password1 == "" -> toast("密码不能为空")
            password2 == "" -> toast("两次密码不一致")
            else -> {
                viewModel.register(enterPriseName, password1, password2).observe(this, {
                    finish()
                })
            }
        }
    }
}