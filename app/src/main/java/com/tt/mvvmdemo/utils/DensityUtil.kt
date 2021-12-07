package com.tt.mvvmdemo.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import com.tt.mvvmdemo.utils.MyMMKV.Companion.mmkv

object DensityUtil {

    /**
     * 根据手机分辨率从dip的单位转成px
     */
    fun dip2px(context: Context, dpValue: Int): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dpValue.toFloat() * scale + 0.5f).toInt()
    }

    /**
     * 根据手机分辨率从px转换为dp
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 很多手机都可以从屏幕旁边拖动出小窗应用
     * 所以这时候设置toolbar的高度不用计算状态栏的高度
     */
    fun isSmallWindow(activity: Activity): Boolean {
        //这个是拿到的当前activity的宽高
        val metrics = activity.resources.displayMetrics
        //保证存在的是屏幕宽度最大值
        if (metrics.heightPixels > mmkv!!.decodeInt("max_size")) {
            mmkv?.encode("max_size", (metrics.heightPixels))
        }
        //拿到的宽始终是一样的，高度也是一样，但是小屏的高度比全凭自己小，所以这里首次存一下高度，后面用高度判断
        return metrics.heightPixels < mmkv?.decodeInt("max_size", metrics.heightPixels)!!
    }

    /**
     * 修改颜色透明度
     */
    fun changeColorAlpha(color: Int, alpha: Int): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }
}