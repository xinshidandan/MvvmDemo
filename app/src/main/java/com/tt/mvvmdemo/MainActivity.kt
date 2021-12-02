package com.tt.mvvmdemo

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.SettingInjectorService
import android.net.Uri
import android.provider.Settings
import android.view.ViewParent
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.tt.mvvmdemo.base.BaseActivity
import com.tt.mvvmdemo.receiver.NetworkChangeReceiver
import com.tt.mvvmdemo.ui.mainFragment.HomeFragment
import com.tt.mvvmdemo.ui.mainFragment.SystemFragment
import com.tt.mvvmdemo.utils.PermissionUtils

class MainActivity : BaseActivity() {

    companion object {
        const val REQ_CODE_INIT_API_KEY = 666
    }

    /**
     * 网络状态变化广播
     */
    private var mNetworkChangeReceiver: NetworkChangeReceiver? = null

    //定义是否退出程序的标记
    private var isExit = 0L
    private lateinit var viewPager: ViewPager
    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var bottomNavigationBar: BottomNavigationBar
    private lateinit var titleList: MutableList<String>
    private var pos = 0

    /**
     * 权限申请
     */
    private val permissionArray = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    private var mPermissionList = arrayListOf<String>()
    private lateinit var mPermissionDialog: AlertDialog

    /**
     * newInstance并不能保证fragment是同一个，可能会重新new一个，所以这里设置一下
     */
    private val homeFragment = HomeFragment.newInstance()
    private val systemFragment = SystemFragment.newInstance()



    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initData() {
        initPermission()
    }

    private fun initPermission() {
        mPermissionList.clear()
        for (i in permissionArray.indices) {
            if (!PermissionUtils.checkPhonePermission(this, permissionArray[i]))
                mPermissionList.add(permissionArray[i])
        }
        if (mPermissionList.size > 0) PermissionUtils.requestPermissions(
            this, permissionArray, REQ_CODE_INIT_API_KEY
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var hasPermissionDismiss = false
        if (requestCode == REQ_CODE_INIT_API_KEY) {
            for (i in grantResults.indices) {
                if (grantResults[i] != PERMISSION_GRANTED) hasPermissionDismiss = true
            }
            if (hasPermissionDismiss) showPermissionDialog()
        }
    }

    private fun showPermissionDialog() {
        if (!this::mPermissionDialog.isInitialized) {
            mPermissionDialog = AlertDialog.Builder(this)
                .setMessage("权限申请失败")
                .setPositiveButton("去设置") { _, _ ->
                    mPermissionDialog.dismiss()
                    //跳转到应用设置
                    val packageURI = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                    startActivity(intent)
                    this.finish()
                }.setNegativeButton("取消") { _, _ ->
                    mPermissionDialog.dismiss()
                    this.finish()
                }.setCancelable(false)
                .create()
            mPermissionDialog.show()
        }
    }

    override fun initView() {

    }

    override fun startHttp() {

    }


}