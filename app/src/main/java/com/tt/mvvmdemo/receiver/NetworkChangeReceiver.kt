package com.tt.mvvmdemo.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tt.mvvmdemo.constant.Constant
import com.tt.mvvmdemo.utils.MyMMKV.Companion.mmkv
import com.tt.mvvmdemo.utils.NetWorkUtil

class NetworkChangeReceiver:BroadcastReceiver() {

    /**
     * 缓存上一次的网络状态
     */
    private var hasNetwork = mmkv?.decodeBool(Constant.HAS_NETWORK_KEY, true)
    override fun onReceive(context: Context?, intent: Intent?) {
        val isConnected = NetWorkUtil.isNetworkConnected(context!!)
        if (isConnected){
            if (!hasNetwork!!){
                LiveEventBus.get("isConnected").post(isConnected)
            }
        } else {
            if (!NetWorkUtil.isNetworkConnected(context)){
                LiveEventBus.get("isConnected").post(isConnected)
            }
        }
        hasNetwork = isConnected
    }
}