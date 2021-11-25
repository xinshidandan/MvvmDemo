package com.tt.mvvmdemo.base

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import android.os.StrictMode
import android.util.Log
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tencent.bugly.Bugly
import com.tencent.mmkv.MMKV
import com.tt.mvvmdemo.constant.Constant
import com.tt.mvvmdemo.httpUtils.UserInfoBody

class BaseApplication : Application() {

    companion object {
        var TAG = javaClass.simpleName

        //用户信息
        var userInfo: UserInfoBody? = null
        lateinit var instance: Application
        lateinit var mContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        mContext = applicationContext
        //解决7。0调用相机报错问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
        }

        //初始化MMKV
        val rootDir = MMKV.initialize(this)
        Log.d(TAG, "mmkv_root------:${rootDir}")
        lateInitSDK()
    }

    /**
     * 非必须在主线程初始化的SDK放在子线程初始化
     */
    private fun lateInitSDK() {
        Thread {
            //设置进程的优先级，不与主线程抢资源
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
            LiveEventBus.config().lifecycleObserverAlwaysActive(false).setContext(this)
            initBugly()
        }.start()
    }

    private fun initBugly() {
        Bugly.init(applicationContext, Constant.BUGLY_ID, false)
    }
}