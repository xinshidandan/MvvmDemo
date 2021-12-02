package com.tt.mvvmdemo.ui.mainFragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelFragment
import com.tt.mvvmdemo.mvvm.mainViewModel.SystemViewModel
import com.tt.mvvmdemo.ui.fragment.KnowledgeFragment
import com.tt.mvvmdemo.ui.fragment.NavigationFragment
import kotlinx.android.synthetic.main.work_fragment.*

class SystemFragment : BaseViewModelFragment<SystemViewModel>() {

    companion object {
        fun newInstance() = SystemFragment()
    }

    private lateinit var vp2: ViewPager2
    private lateinit var tbLayout: TabLayout
    private val titleList = mutableListOf<String>()
    private val fragmentList = mutableListOf<Fragment>()
    private val systemPagerAdapter by lazy { SystemPagerAdapter() }

    override fun providerVMClass(): Class<SystemViewModel>? = SystemViewModel::class.java

    override fun getLayoutId(): Int = R.layout.work_fragment

    override fun initData() {}

    override fun initView(view: View) {
        vp2 = vp2_system
        tbLayout = tl_system
        titleList.add(resources.getString(R.string.system))
        titleList.add(resources.getString(R.string.navigation))
        fragmentList.add(KnowledgeFragment.newInstance())
        fragmentList.add(NavigationFragment.newInstance())
        vp2.adapter = systemPagerAdapter
        tbLayout.run {
            addOnTabSelectedListener(onTabSelectedListener)
        }
        TabLayoutMediator(tbLayout, vp2) { tab, position ->
            tab.text = titleList[position]
        }.attach()
    }

    override fun startHttp() {}

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

    inner class SystemPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = fragmentList.size
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> KnowledgeFragment.newInstance()
                1 -> NavigationFragment.newInstance()
                else -> KnowledgeFragment.newInstance()
            }
        }
    }
}