package com.tt.mvvmdemo.ui.mainFragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelFragment
import com.tt.mvvmdemo.httpUtils.WXChapterBean
import com.tt.mvvmdemo.mvvm.mainViewModel.WeiXinViewModel
import com.tt.mvvmdemo.ui.fragment.KnowListFragment

class WeiXinFragment : BaseViewModelFragment<WeiXinViewModel>() {

    companion object {
        fun newInstance() = WeiXinFragment()
    }

    private val data = mutableListOf<WXChapterBean>()
    private lateinit var vp2: ViewPager2
    private lateinit var tbLayout: TabLayout

    override fun providerVMClass(): Class<WeiXinViewModel>? = WeiXinViewModel::class.java

    override fun getLayoutId(): Int = R.layout.wei_xin_fragment

    override fun initData() {

    }

    override fun initView(view: View) {

    }

    override fun startHttp() {

    }

    inner class WeiXinPagerAdapter : FragmentStateAdapter(this) {
        private val fragments = mutableListOf<Fragment>()

        init {
            fragments.clear()
//            data.forEach {
//                fragments.add()
//            }
        }

        override fun getItemCount(): Int = data.size

        override fun createFragment(position: Int): Fragment =
            KnowListFragment.newInstance(data[position].id)


    }
}