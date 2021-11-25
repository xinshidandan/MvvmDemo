package com.tt.mvvmdemo.httpUtils.interceptor

import com.tt.mvvmdemo.base.BaseApplication
import com.tt.mvvmdemo.utils.NetWorkUtil
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!NetWorkUtil.isNetworkAvailable(BaseApplication.mContext)) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
        }
        val response = chain.proceed(request)
        if (NetWorkUtil.isNetworkAvailable(BaseApplication.mContext)) {
            val maxAge = 60 * 3
            response.newBuilder()
                .addHeader("Cache-Control", "public, max~age=$maxAge")
                .removeHeader("Retrofit")
                .build()
        } else {
            val maxStale = 60 * 60 * 24 * 28
            response.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("nyn")
                .build()
        }
        return response
    }
}