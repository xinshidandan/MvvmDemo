package com.tt.mvvmdemo.utils

import com.tencent.mmkv.MMKV

/**
 * MMKV
 */
class MyMMKV {

    companion object {
        private const val fileName = "wan_android_mvvmdemo"

        /**
         * 初始化mmkv
         */
        val mmkv: MMKV
            get() = MMKV.mmkvWithID(fileName)


        /**
         * 删除全部数据（传了参数就是按照key删除）
         */
        fun deleteKeyOrAll(key: String? = null) {
            if (key == null) mmkv?.clearAll()
            else mmkv?.removeValueForKey(key)
        }

        /**
         * 查询某个key是否已经存在
         */
        fun contains(key: String) = mmkv?.contains(key)
    }
}