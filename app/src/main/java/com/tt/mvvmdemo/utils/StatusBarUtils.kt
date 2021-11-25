package com.tt.mvvmdemo.utils

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.view.Window
import android.view.WindowManager
import com.tt.mvvmdemo.R
import java.lang.Exception

/**
 * 状态栏相关工具类
 */
object StatusBarUtils {
    //设置Activity对应的顶部状态栏的背景颜色
    fun setWindowStatusBarColor(activity: Activity, colorResId: Int) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = activity.window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = activity.resources.getColor(colorResId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //设置状态栏透明
    fun setWindowStatusTransparent(activity: Activity) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = activity.window
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = activity.resources.getColor(R.color.transparent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //5.0以上版本去掉状态栏阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val decorViewClazz = Class.forName("com.android.internal.policy.DecorView")
                val field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor")
                field.isAccessible = true
                field.setInt(activity.window.decorView, Color.TRANSPARENT)//改为透明
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(activity: Activity): Int {
        val resources: Resources = activity.resources
        val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
}