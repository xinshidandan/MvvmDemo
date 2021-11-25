package com.tt.mvvmdemo.httpUtils

import android.util.Log
import com.tt.mvvmdemo.base.BaseApplication
import com.tt.mvvmdemo.constant.HttpConstant
import com.tt.mvvmdemo.httpUtils.interceptor.CacheInterceptor
import com.tt.mvvmdemo.httpUtils.interceptor.HeaderInterceptor
import com.tt.mvvmdemo.httpUtils.interceptor.SaveCookieInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit


object RetrofitClient {

    //请求地址
    val BASE_URL = "https://wanandroid.com"

    //retrofit对象
    private var retrofit: Retrofit? = null

    //请求的api，可以根据不同场景设置多个
    val service: ApiService by lazy {
        getRetrofit().create(ApiService::class.java)
    }

    private fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        val loggingInterceptor: HttpLoggingInterceptor
        loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("接口请求log------>", message)
            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val cacheFile = File(BaseApplication.mContext.cacheDir, "cache")
        val cache = Cache(cacheFile, HttpConstant.MAX_CACHE_SIZE)

        builder.run {
            addInterceptor(loggingInterceptor)
            addInterceptor(HeaderInterceptor())
            addInterceptor(SaveCookieInterceptor())
            addInterceptor(CacheInterceptor())
            cache(cache)
            connectTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(HttpConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)//错误重连
        }
        return builder.build()
    }
}