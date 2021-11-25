package com.tt.mvvmdemo.utils

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation







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