package com.tt.mvvmdemo.httpUtils

import retrofit2.http.*

interface ApiService {


    /**
     * 获取轮播图
     */
    @GET("banner/json")
    suspend fun getBanners(): ResponseData<List<Banner>>

    /**
     * 获取首页置顶文章列表
     * http://www.wanandroid.com/article/top/json
     */
    @GET("article/top/json")
    suspend fun getTopArticles(): ResponseData<MutableList<Article>>

    /**
     * 获取文章列表
     * http://www.wanandroid.com/article/list/0/json
     * @param pageNum
     */
    @GET("article/list/{pageNum}/json")
    suspend fun getArticles(@Path("pageNum") pageNum: Int): ResponseData<ArticleResponseBody>

    /**
     * 搜索热词
     * http://www.wanandroid.com/hotkey/json
     */
    @GET("hotkey/json")
    suspend fun getHotSearchData(): ResponseData<MutableList<HotSearchBean>>

    /**
     * 搜索
     * http://www.wanandroid.com/article/query/0/json
     * @param page
     * @param key
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    suspend fun queryBySearchKey(
        @Path("page") page: Int,
        @Field("k") key: String
    ): ResponseData<ArticleResponseBody>


    /**
     * 登录
     * 方法：POST
     *
     * 登录后会在cookie中返回账号密码，只要在客户端做cookie持久化存储即可自动登录验证
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ResponseData<LoginData>

    /**
     * 注册
     * 方法：POST
     *
     * 登录后会在cookie中返回账号密码，只要在客户端做cookie持久化存储即可自动登录验证
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): ResponseData<LoginData>

    /**
     * 收藏站外文章
     * 方法：POST
     * http://www.wanandroid.com/lg/collect/add/json
     */
    @FormUrlEncoded
    @POST("lg/collect/add/json")
    suspend fun addCollectOutsideArticle(
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String
    ): ResponseData<Any>


    /**
     * 退出登录
     */
    @GET("user/logout/json")
    suspend fun logout(): ResponseData<Any>

    /**
     * V2版本 ： 获取TODO列表数据
     * http://www.wanandroid.com/lg/todo/v2/list/页码/json
     * @param page 页码从1开始，拼接在url上
     * @param map
     *            status 状态， 1-完成；0-未完成；默认全部展示
     *            type 创建时传入的类型，默认全部展示
     *            priority 创建时传入的优先级；默认全部展示
     *            orderby 1-完成日期顺序；2-完成日期逆序；3-创建日期顺序；4-创建日期逆序（默认）
     */
    @GET("user/todo/v2/list/{page}/json")
    suspend fun getTodoList(
        @Path("page") page: Int,
        @QueryMap map: MutableMap<String, Any>
    ): ResponseData<TodoResponseBody>

    /**
     * 收藏站内文章
     * http://www.wanandroid.com/lg/collect/1165/json
     * @param id article id
     */
    @POST("lg/collect/{id}/json")
    suspend fun addCollectArticle(@Path("id") id: Int): ResponseData<Any>

    /**
     * 文章列表中取消收藏文章
     * http://www.wanandroid.com/lg/uncollect_originId/2333/json
     * @param id article id
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun cancelCollectArticle(@Path("id") id: Int): ResponseData<Any>


    /**
     * 收藏列表中取消收藏文章
     * 方法：POST
     * http://www.wanandroid.com/lg/uncollect/2805/json
     */
    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    suspend fun removeCollectArticle(
        @Path("id") id: Int,
        @Field("originId") originId: Int = -1
    ): ResponseData<Any>

    /**
     * 获取知识体系
     * http://www.wanandroid.com/tree/json
     */
    @GET("tree/json")
    suspend fun getKnowledgeTree(): ResponseData<List<KnowledgeTreeBody>>

    /**
     * 知识体系下的文章
     * http://www.wanandroid.com/article/list/0/json?cid=168
     * @param page
     * @param cid
     */
    @GET("article/list/{page}/json")
    suspend fun getKnowledgeList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ResponseData<ArticleResponseBody>

    /**
     * 导航数据
     * http://www.wanandroid.com/navi/json
     */
    @GET("navi/json")
    suspend fun getNavigationList(): ResponseData<List<NavigationBean>>

    /**
     * 获取公众号列表
     * http://wanandroid.com/wxarticle/chapters/json
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getWeiXin(): ResponseData<MutableList<WXChapterBean>>

    /**
     * 问答
     * pageId， 拼接在连接上，例如上面的1
     */
    @GET("wenda/list/{page}/json")
    suspend fun getQuestionList(@Path("page") page: Int): ResponseData<ArticleListBean>

    /**
     * 获取个人信息
     * https://www.wanandroid.com/lg/coin/userinfo/json
     */
    @GET("lg/coin/userinfo/json")
    suspend fun getUserInfo(): ResponseData<UserInfoBody>
}