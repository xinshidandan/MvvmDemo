package com.tt.mvvmdemo

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.ashokvarma.bottomnavigation.ShapeBadgeItem
import com.ashokvarma.bottomnavigation.TextBadgeItem
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tt.mvvmdemo.base.BaseActivity
import com.tt.mvvmdemo.constant.Constant
import com.tt.mvvmdemo.receiver.NetworkChangeReceiver
import com.tt.mvvmdemo.ui.mainFragment.*
import com.tt.mvvmdemo.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.android.synthetic.main.toolbar_layout_search.view.*
import okhttp3.internal.waitMillis

class MainActivity : BaseActivity(), BottomNavigationBar.OnTabSelectedListener {

    companion object {
        const val REQ_CODE_INIT_API_KEY = 666
    }

    /**
     * 网络状态变化广播
     */
    private var mNetworkChangeReceiver: NetworkChangeReceiver? = null

    //定义是否退出程序的标记
    private var isExit = 0L
    private lateinit var viewPager: ViewPager
    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var bottomNavigationBar: BottomNavigationBar
    private lateinit var titleList: MutableList<String>
    private var pos = 0

    /**
     * 权限申请
     */
    private val permissionArray = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    private var mPermissionList = arrayListOf<String>()
    private lateinit var mPermissionDialog: AlertDialog

    /**
     * newInstance并不能保证fragment是同一个，可能会重新new一个，所以这里设置一下
     */
    private val homeFragment = HomeFragment.newInstance()
    private val systemFragment = SystemFragment.newInstance()
    private val weiXinFragment = WeiXinFragment.newInstance()
    private val questionFragment = QuestionFragment.newInstance()
    private val myFragment = MyFragment.newInstance()


    override fun getLayoutId(): Int = R.layout.activity_main

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
        setTop(SettingUtil.getDefaultPage(), R.drawable.search, {})
        toolbar_title.setTextColor(ContextCompat.getColor(this, R.color.white))
        toolbar_left_image_back.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.group))


    }
    override fun initData() {
        initPermission()
    }

    private fun initPermission() {
        mPermissionList.clear()
        for (i in permissionArray.indices) {
            if (!PermissionUtils.checkPhonePermission(this, permissionArray[i]))
                mPermissionList.add(permissionArray[i])
        }
        if (mPermissionList.size > 0) PermissionUtils.requestPermissions(
            this, permissionArray, REQ_CODE_INIT_API_KEY
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var hasPermissionDismiss = false
        if (requestCode == REQ_CODE_INIT_API_KEY) {
            for (i in grantResults.indices) {
                if (grantResults[i] != PERMISSION_GRANTED) hasPermissionDismiss = true
            }
            if (hasPermissionDismiss) showPermissionDialog()
        }
    }

    private fun showPermissionDialog() {
        if (!this::mPermissionDialog.isInitialized) {
            mPermissionDialog = AlertDialog.Builder(this)
                .setMessage("权限申请失败")
                .setPositiveButton("去设置") { _, _ ->
                    mPermissionDialog.dismiss()
                    //跳转到应用设置
                    val packageURI = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                    startActivity(intent)
                    this.finish()
                }.setNegativeButton("取消") { _, _ ->
                    mPermissionDialog.dismiss()
                    this.finish()
                }.setCancelable(false)
                .create()
            mPermissionDialog.show()
        }
    }

    override fun initView() {
        initViewPager()
        bottomNavigationBar = bottom_bar
        val homeBadge =
            TextBadgeItem().setBorderWidth(1).setText(resources.getString(R.string.zero))
        homeBadge.hide()
        LiveEventBus.get("homeBadge", Int::class.java).observe(this, {
            if (!SettingUtil.getIsShowBadge()!!) {
                homeBadge.hide()
                return@observe
            }
            when {
                it == 0 -> homeBadge.hide()
                it > 99 -> {
                    homeBadge.show()
                    homeBadge.setText(resources.getString(R.string.exceed_99))
                }
                else -> {
                    homeBadge.show()
                    homeBadge.setText(it.toString())
                }
            }
        })

        val myBadge = ShapeBadgeItem().setShape(ShapeBadgeItem.SHAPE_OVAL).setSizeInDp(this, 10, 10)
        myBadge.hide()
        LiveEventBus.get("myBadge", Boolean::class.java).observe(this, {
            if (!SettingUtil.getIsShowBadge()!!) {
                myBadge.hide()
                return@observe
            }
            if (it && MyMMKV.mmkv.encode(
                    Constant.IS_LOGIN,
                    true
                )
            ) myBadge.show() else myBadge.hide()
        })
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED)
            .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
            .addItem(BottomNavigationItem(R.drawable.home, titleList[0]).setBadgeItem(homeBadge))
            .addItem(BottomNavigationItem(R.drawable.work, titleList[1]))
            .addItem(BottomNavigationItem(R.drawable.weixin, titleList[2]))
            .addItem(BottomNavigationItem(R.drawable.msg, titleList[3]))
            .addItem(BottomNavigationItem(R.drawable.my, titleList[4]).setBadgeItem(myBadge))
            .setActiveColor(R.color.Light_Blue)//图标和文本被激活或选中的颜色，默认颜色为Color.LTGRAY
            .setInActiveColor(R.color.grey_search)//图标和文本被激活或选中的颜色，默认颜色为Theme's Primary Color
            .setBarBackgroundColor(R.color.white)//整个控件的背景色，默认颜色为Color.WHITE
            .setTabSelectedListener(this)//回调方法
            .setFirstSelectedPosition(SettingUtil.getDefaultPage(this))
            .initialise()
        setBottomNavigationItem(bottomNavigationBar, 2, 22, 12)
        viewPager.currentItem = bottomNavigationBar.currentSelectedPosition
    }

    private fun initViewPager() {
        titleList = mutableListOf()
        titleList.run {
            add(resources.getString(R.string.home))
            add(resources.getString(R.string.system))
            add(resources.getString(R.string.weixin))
            add(resources.getString(R.string.question))
            add(resources.getString(R.string.my))
        }

        fragments = ArrayList()
        fragments.run {
            add(homeFragment)
            add(systemFragment)
            add(weiXinFragment)
            add(questionFragment)
            add(myFragment)
        }
        //初始化Viewpager，这是只能用Viewpager，不能用Viewpager2，2会滑动冲突，不好解决
        viewPager = vp2_fragment
        viewPager.run {
            adapter = MainPagerAdapter()
            //解决滑动页面后再回来会刷新页面的问题，因为viewpager默认只预加载一页，多的页面会回收
            offscreenPageLimit = fragments.size
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    pos = position
                    if (position == fragments.size - 1) toolbar.visibility =
                        View.GONE else toolbar.visibility = View.VISIBLE
                    toolbar_title.text = titleList[position]
                    bottom_bar.selectTab(position)
                }

                override fun onPageScrollStateChanged(state: Int) {}

            })
        }
    }

    inner class MainPagerAdapter : FragmentPagerAdapter(supportFragmentManager) {
        override fun getCount(): Int = fragments.size

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> homeFragment
                1 -> systemFragment
                2 -> weiXinFragment
                3 -> questionFragment
                4 -> myFragment
                else -> homeFragment
            }
        }

    }

    override fun onResume() {
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        mNetworkChangeReceiver = NetworkChangeReceiver()
        registerReceiver(mNetworkChangeReceiver, filter)
        super.onResume()
    }

    override fun onPause() {
        if (mNetworkChangeReceiver != null) {
            unregisterReceiver(mNetworkChangeReceiver)
            mNetworkChangeReceiver = null
        }
        super.onPause()
    }

    override fun startHttp() {}

    override fun onTabSelected(position: Int) {
        viewPager.currentItem = position
    }

    override fun onTabUnselected(position: Int) {}

    override fun onTabReselected(position: Int) {}

    /**
     * @param bottomNavigationBar, 需要修改的 BottomNavigationBar
     * @param space 图片与文字之间的间距
     * @param imgLen 单位：dp， 图片大小，应 <= 36dp
     * @param textSize 单位：dp， 文字大小， 应 <= 20dp
     *
     * 使用方法：直接调用setBottomNavigationItem(bottomNavigationBar, 6, 26, 10);
     * 代表将bottomNavigationBar的文字大小设置为10dp，图片大小为26dp，二者间距为6dp
     */
    private fun setBottomNavigationItem(
        bottomNavigationBar: BottomNavigationBar,
        space: Int,
        imgLen: Int,
        textSize: Int
    ) {
        val barClass: Class<*> = bottomNavigationBar.javaClass
        val fields = barClass.declaredFields
        for (i in fields.indices) {
            val field = fields[i]
            field.isAccessible = true
            if (field.name == "mTabContainer") {
                try {
                    //反射得到 mTabContainer
                    val mTabContainer = field[bottomNavigationBar] as LinearLayout
                    for (j in 0 until mTabContainer.childCount) {
                        val view = mTabContainer.getChildAt(j)
                        var params = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            DensityUtil.dip2px(this, 56)
                        )
                        val container =
                            view.findViewById<View>(R.id.fixed_bottom_navigation_container) as FrameLayout
                        container.layoutParams = params
                        container.setPadding(
                            DensityUtil.dip2px(this, 0),
                            DensityUtil.dip2px(this, 0),
                            DensityUtil.dip2px(this, 0),
                            DensityUtil.dip2px(this, 0)
                        )
                        val labelView =
                            view.findViewById<View>(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title) as TextView
                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize.toFloat())
                        labelView.includeFontPadding = false
                        labelView.setPadding(
                            0,
                            0,
                            0,
                            DensityUtil.dip2px(this, 25 - textSize - space / 2)
                        )
                        val iconView =
                            view.findViewById<View>(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon) as ImageView
                        params = FrameLayout.LayoutParams(
                            DensityUtil.dip2px(this, imgLen),
                            DensityUtil.dip2px(this, imgLen)
                        )
                        params.setMargins(0, 0, 0, space / 2)
                        params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                        iconView.layoutParams = params
                    }
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.IMAGE_CAPTURE || requestCode == Constant.IMAGE_SELECT || requestCode == Constant.FROM_TODO) {
            myFragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - isExit <= 2000) {
                this.finish()
            } else {
                isExit = System.currentTimeMillis()
                toast("再按一次退出应用")
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}