package com.tt.mvvmdemo.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {

    /**
     * 申请权限
     */
    fun requestPermissions(activity: Activity, permissionName: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissionName, requestCode)
    }

    /**
     * 检查单个权限是否申请
     */
    fun checkPhonePermission(context: Context, permissionName: String): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            ContextCompat.checkSelfPermission(
                context,
                permissionName
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }
}