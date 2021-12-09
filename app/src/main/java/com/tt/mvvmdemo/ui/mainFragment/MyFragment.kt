package com.tt.mvvmdemo.ui.mainFragment

import android.content.Intent
import android.view.View
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelFragment
import com.tt.mvvmdemo.constant.Constant
import com.tt.mvvmdemo.mvvm.mainViewModel.MyViewModel
import com.tt.mvvmdemo.ui.activity.collect.MyCollectActivity
import com.tt.mvvmdemo.ui.activity.my.MyScoreActivity
import com.tt.mvvmdemo.ui.activity.share.ShareActivity
import com.tt.mvvmdemo.ui.login.LoginActivity
import com.tt.mvvmdemo.ui.view.BottomDialog
import com.tt.mvvmdemo.ui.view.MyDialog
import com.tt.mvvmdemo.utils.ImageLoader
import com.tt.mvvmdemo.utils.MyMMKV.Companion.mmkv
import com.tt.mvvmdemo.utils.SettingUtil
import com.tt.mvvmdemo.utils.captureImage
import com.tt.mvvmdemo.utils.selectImage
import kotlinx.android.synthetic.main.my_fragment.*
import kotlinx.android.synthetic.main.my_fragment.view.*
import java.io.File

class MyFragment : BaseViewModelFragment<MyViewModel>(), View.OnClickListener {

    companion object {
        fun newInstance() = MyFragment()
    }

    private lateinit var bottomDialog: BottomDialog
    private lateinit var logoutDialog: MyDialog
    private lateinit var refreshLayout: SmartRefreshLayout
    private val headType = "head"
    private val bgType = "backGround"
    private var type = ""

    override fun providerVMClass(): Class<MyViewModel>? = MyViewModel::class.java

    override fun getLayoutId(): Int = R.layout.my_fragment

    override fun initData() {
        tv_name.setOnClickListener(this)
        head_pic.setOnClickListener(this)
        iv_bg_img.setOnClickListener(this)
        ll_my_collect.setOnClickListener(this)
        ll_score.setOnClickListener(this)
        ll_my_share.setOnClickListener(this)
        ll_my_logout.setOnClickListener(this)
        iv_todo.setOnClickListener(this)
        ll_my_system.setOnClickListener(this)
        ll_my_about.setOnClickListener(this)
        ll_my_laterRead.setOnClickListener(this)
        ll_my_readRecord.setOnClickListener(this)

        LiveEventBus.get(Constant.IS_LOGIN, Boolean::class.java).observe(this, {
            if (it) {
                startHttp()
            } else {
                tv_name.text = resources.getString(R.string.my_login)
                my_rank_num.text = resources.getString(R.string.my_score)
            }
        })
        LiveEventBus.get("myBadge", Boolean::class.java).observe(this, {
            if (!SettingUtil.getIsShowBadge()!!) {
                tv_un_todo.visibility = View.GONE
                return@observe
            }
            if (it && mmkv?.decodeBool(Constant.IS_LOGIN, false)!!) {
                tv_un_todo.visibility = View.VISIBLE
            } else tv_un_todo.visibility = View.GONE
        })
        setImage(File(mmkv?.decodeString("HeadPic", "")))
        if (mmkv?.decodeString("bgHeadPic", "") != "") {
            setBgImage(File(mmkv?.decodeString("bgHeadPic", "")))
        }
    }

    override fun initView(view: View) {
        refreshLayout = swipeRefreshLayout_my
        refreshLayout.run {
            setRefreshHeader(ch_header_my)
            setOnRefreshListener { startHttp() }
        }
    }

    override fun startHttp() {
        if (mmkv?.decodeBool(Constant.IS_LOGIN, false)!!) {
            getTodoBadge()
            tv_name.text =
                mmkv?.decodeString(Constant.USERNAME_KEY, resources.getString(R.string.my_login))
            my_rank_num.text =
                "${resources.getString(R.string.my_score)}(${mmkv?.decodeString(Constant.USERNAME_COIN_COUNT)})"
            viewModel.getUserInfo().observe(this, {
                refreshLayout.finishRefresh()
                tv_name.text = it.username
                my_rank_num.text = "${resources.getString(R.string.my_score)}(${it.coinCount})"
                mmkv?.encode(Constant.USERNAME_COIN_COUNT, it.coinCount.toString())
                mmkv?.encode(Constant.USERNAME_KEY, it.username)
                mmkv?.encode(Constant.SCORE_UNM, it.coinCount)
            })
        } else {
            if (refreshLayout.isRefreshing) refreshLayout.finishRefresh()
            tv_name.text = resources.getString(R.string.my_login)
            my_rank_num.text = resources.getString(R.string.my_score)
        }
    }

    private fun getTodoBadge() {
        val map = mutableMapOf<String, Any>()
        map["status"] = 0
        viewModel.getTodoList(1, map).observe(this, {
            hideLoading()
            it.datas.let { todoList ->
                if (todoList.size > 0) {
                    tv_un_todo.visibility = View.VISIBLE
                    LiveEventBus.get("myBadge").post(true)
                } else {
                    tv_un_todo.visibility = View.GONE
                    LiveEventBus.get("myBadge").post(false)
                }

            }
        })
    }

    private fun setImage(file: File?) {
        ImageLoader.loadByNoCache(context, file, head_pic)
    }

    private fun setBgImage(file: File?) {
        ImageLoader.loadByNoCache(context, file, iv_bg_img)
    }

    override fun onClick(v: View?) {
        when (v) {
            tv_name -> {
                if (!mmkv.decodeBool(Constant.IS_LOGIN, false)) {
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
            ll_score -> {
                if (mmkv.decodeBool(Constant.IS_LOGIN, false)) {
                    startActivity(Intent(activity, MyScoreActivity::class.java))
                } else {
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
            ll_my_collect -> {
                if (mmkv.decodeBool(Constant.IS_LOGIN, false)) {
                    startActivity(Intent(activity, MyCollectActivity::class.java))
                } else {
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
            ll_my_share -> {
                if (mmkv.decodeBool(Constant.IS_LOGIN, false)) {
                    startActivity(Intent(activity, ShareActivity::class.java))
                } else {
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
            iv_todo -> {
                if (mmkv.decodeBool(Constant.IS_LOGIN, false)) {

                } else {
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
            head_pic -> showPhotoDialog(headType)
            iv_bg_img -> showPhotoDialog(bgType)
            ll_my_logout -> {

            }
        }
    }

    private fun showPhotoDialog(imgType: String) {
        type = imgType
        if (this::bottomDialog.isInitialized) bottomDialog.show()
        else {
            bottomDialog = BottomDialog(activity!!, {
                when (it.id) {
                    R.id.tv_take_photo -> {
                        if (this::bottomDialog.isInitialized && bottomDialog.isShowing) {
                            bottomDialog.dismiss()
                            captureImage(activity!!)
                        }
                    }
                    R.id.tv_from_album -> {
                        if (this::bottomDialog.isInitialized && bottomDialog.isShowing) {
                            bottomDialog.dismiss()
                            selectImage(activity!!)
                        }
                    }
                    R.id.tv_cancle -> {
                        if (this::bottomDialog.isInitialized && bottomDialog.isShowing) {
                            bottomDialog.dismiss()
                        }
                    }
                }
            })
        }
        bottomDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::bottomDialog.isInitialized && bottomDialog.isShowing) bottomDialog.dismiss()
    }
}