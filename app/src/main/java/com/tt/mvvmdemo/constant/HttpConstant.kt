package com.tt.mvvmdemo.constant

import com.tt.mvvmdemo.utils.MyMMKV.Companion.mmkv
import java.lang.StringBuilder

object HttpConstant {

    const val DEFAULT_TIMEOUT: Long = 30
    const val SAVE_USER_LOGIN_KEY = "user/login"
    const val SAVE_USER_REGISTER_KEY = "user/register"

    const val COLLECTIONS_WEBSITE = "lg/collect"
    const val UNCOLLECTIONS_WEBSITE = "lg/uncollect"
    const val ARTICLE_WEBSITE = "article"
    const val TODO_WEBSITE = "lg/todo"
    const val COIN_WEBSITE = "lg/coin"

    const val SET_COOKIE_KEY = "set-cookie"
    const val COOKIE_NAME = "Cookie"

    const val MAX_CACHE_SIZE: Long = 1024 * 1024 * 50 //50M缓存空间


    fun encodeCookie(cookie: List<String>): String {
        val sb = StringBuilder()
        val set = HashSet<String>()
        cookie.map { cookie ->
            cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }
            .forEach {
                it.filterNot { set.contains(it) }.forEach { set.add(it) }
            }
        val ite = set.iterator()
        while (ite.hasNext()) {
            val cookie = ite.next()
            sb.append(cookie).append(";")
        }
        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }
        return sb.toString()
    }


    fun saveCookie(url: String?, domain: String?, cookies: String) {
        url ?: return
        mmkv?.encode(url, cookies)
        domain ?: return
        mmkv?.encode(domain, cookies)
    }

}