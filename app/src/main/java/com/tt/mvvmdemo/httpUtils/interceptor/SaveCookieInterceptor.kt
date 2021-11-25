package com.tt.mvvmdemo.httpUtils.interceptor

import com.tt.mvvmdemo.constant.HttpConstant
import okhttp3.Interceptor
import okhttp3.Response

class SaveCookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requestUrl = request.url.toString()
        val domain = request.url.host
        if ((requestUrl.contains(HttpConstant.SAVE_USER_LOGIN_KEY)
            || requestUrl.contains(HttpConstant.SAVE_USER_REGISTER_KEY)
        )
                && response.headers(HttpConstant.SET_COOKIE_KEY).isNotEmpty()){
            val cookies = response.headers(HttpConstant.SET_COOKIE_KEY)
            val cookie = HttpConstant.encodeCookie(cookies)
            HttpConstant.saveCookie(requestUrl, domain, cookie)
        }
        return response
    }
}