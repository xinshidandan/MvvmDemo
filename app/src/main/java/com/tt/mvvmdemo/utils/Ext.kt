package com.tt.mvvmdemo.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.webkit.WebView
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.constant.Constant
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 得到一个自定义的AgentWebView
 */
fun getAgentWebView(
    url: String,
    activity: Activity,
    webContent: ViewGroup,
    layoutParams: ViewGroup.LayoutParams,
    webView: WebView,
    webViewClient: com.just.agentweb.WebViewClient?,
    webChromeClient: com.just.agentweb.WebChromeClient?,
    indicatorColor: Int
): AgentWeb {
    return AgentWeb.with(activity)
        .setAgentWebParent(webContent, 1, layoutParams)
        .useDefaultIndicator(indicatorColor, 2)
        .setWebView(webView)
        .setWebViewClient(webViewClient)
        .setWebChromeClient(webChromeClient)
        .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
        .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
        .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
        .interceptUnkownUrl()
        .createAgentWeb()
        .ready()
        .go(url)
}

/**
 * 防止快速点击导致打开多个相同页面
 * @param v 防止多次点击的view
 * @param defaultTime 超时时间，默认300毫秒
 */
fun isInvalidClick(v: View, defaultTime: Long = 300): Boolean {
    val curTimeStamp = System.currentTimeMillis()
    val lastClickTimeStamp: Long
    val o = v.getTag(R.id.invalid_click)
    if (o == null) {
        v.setTag(R.id.invalid_click, curTimeStamp)
        return false
    }
    lastClickTimeStamp = o as Long
    val isInvalid = curTimeStamp - lastClickTimeStamp < defaultTime
    if (!isInvalid) v.setTag(R.id.invalid_click, curTimeStamp)
    return isInvalid
}


/**
 * 获取旋转动画
 */
fun getRotateAnimation(fromDegress: Float, toDegress: Float): Animation {
    val rotateAnimation = RotateAnimation(
        fromDegress,
        toDegress,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    )
    rotateAnimation.fillAfter = true
    rotateAnimation.duration = 2000
    rotateAnimation.repeatCount = Animation.INFINITE
    rotateAnimation.repeatMode = Animation.RESTART
    rotateAnimation.interpolator = LinearInterpolator()
    return rotateAnimation

}

/**
 * 拍照
 */
fun captureImage(activity: Activity) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val file =
        File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), Constant.HEAD_PIC_PATH)
    //7.0版本以下才能使用file://, 7.0以上需要用共享文件的形式：content://URI
    val uri = Uri.fromFile(file)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    activity.startActivityForResult(intent, Constant.IMAGE_CAPTURE)
}

/**
 * 从图库中选取图片
 *
 */
fun selectImage(activity: Activity) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_PICK
    activity.startActivityForResult(intent, Constant.IMAGE_SELECT)
}

/**
 * 格式化当前日期
 *
 */
fun formatCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(Date())
}

/**
 * String 转 Calendar
 */
fun String.stringToCalendar(): Calendar {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val date = sdf.parse(this)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar
}