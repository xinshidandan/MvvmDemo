package com.tt.mvvmdemo.ui.mainFragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelFragment
import com.tt.mvvmdemo.httpUtils.WXChapterBean
import com.tt.mvvmdemo.mvvm.mainViewModel.WeiXinViewModel
import com.tt.mvvmdemo.ui.fragment.KnowListFragment
import kotlinx.android.synthetic.main.wei_xin_fragment.*

class WeiXinFragment : BaseViewModelFragment<WeiXinViewModel>() {

    companion object {
        fun newInstance() = WeiXinFragment()
    }

    private val data = mutableListOf<WXChapterBean>()
    private lateinit var vp2: ViewPager2
    private lateinit var tbLayout: TabLayout
    private val viewPagerAdapter by lazy { WeiXinPagerAdapter() }

    override fun providerVMClass(): Class<WeiXinViewModel>? = WeiXinViewModel::class.java

    override fun getLayoutId(): Int = R.layout.wei_xin_fragment

    override fun initData() {

    }

    override fun initView(view: View) {
        vp2 = vp2_weixin
        tbLayout = tl_weixin
        tbLayout.run {
            addOnTabSelectedListener(onTabSelectedListener)
        }
    }

    override fun startHttp() {
        getWeiXin()
    }

    private fun getWeiXin() {
        viewModel.getWeiXin().observe(activity!!, {
            data.addAll(it)
            vp2.run {
                adapter = viewPagerAdapter
                offscreenPageLimit = data.size
            }
            TabLayoutMediator(tbLayout, vp2) { tab, position ->
                tab.text = data[position].name
            }.attach()
        })
    }

    inner class WeiXinPagerAdapter : FragmentStateAdapter(this) {
        private val fragments = mutableListOf<Fragment>()

        init {
            fragments.clear()
            data.forEach {
                fragments.add(KnowListFragment.newInstance(it.id))
            }
        }

        override fun getItemCount(): Int = data.size

        override fun createFragment(position: Int): Fragment =
            KnowListFragment.newInstance(data[position].id)

    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let {
                vp2.currentItem = it.position
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }

    }
}