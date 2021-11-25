package com.tt.mvvmdemo.httpUtils

data class ResponseData<out T>(
    val errorCode: Int,
    val errorMsg: String,
    val data: T
)

data class LoginData(
    val chapterTops: MutableList<String>,
    val collectIds: MutableList<String>,
    val email: String,
    val icon: String,
    val id: Int,
    val password: String,
    val token: String,
    val type: String,
    val username: String
)


data class UserInfoBody(
    val coinCount: Int,
    val rank: Int,
    val userId: Int,
    val username: String
)