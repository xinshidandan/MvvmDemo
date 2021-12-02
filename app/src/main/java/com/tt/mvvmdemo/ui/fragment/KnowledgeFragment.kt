package com.tt.mvvmdemo.ui.fragment

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelFragment
import com.tt.mvvmdemo.httpUtils.KnowledgeTreeBody
import com.tt.mvvmdemo.mvvm.viewModel.KnowledgeViewModel
import com.tt.mvvmdemo.ui.adapter.KnowledgeAdapter
import kotlinx.android.synthetic.main.knowledge_fragment.*
import java.lang.Exception

class KnowledgeFragment : BaseViewModelFragment<KnowledgeViewModel>() {

    companion object {
        fun newInstance() = KnowledgeFragment()
    }

    private val linearLayoutManager by lazy { LinearLayoutManager(activity) }
    private val knowledgeAdapter by lazy { KnowledgeAdapter() }

    override fun providerVMClass(): Class<KnowledgeViewModel>? = KnowledgeViewModel::class.java

    override fun getLayoutId(): Int = R.layout.knowledge_fragment

    override fun initData() {}

    override fun initView(view: View) {
        recyclerView_work.run {
            layoutManager = linearLayoutManager
            adapter = knowledgeAdapter
        }
        knowledgeAdapter.run {
            recyclerView = recyclerView_work
            setOnItemClickListener(object : KnowledgeAdapter.OnItemClickListener {
                override fun onClick(bean: KnowledgeTreeBody, pos: Int) {
//                    Intent(activity, )
                }

            })
        }
    }

    override fun startHttp() {
        initKnowledgeTree()
    }

    private fun initKnowledgeTree() {
        viewModel = ViewModelProvider(this).get(KnowledgeViewModel::class.java)
        viewModel.getKnowledgeTree().observe(activity!!, { list ->
            knowledgeAdapter.run {
                setList(list)
            }

        })
    }

    override fun requestError(it: Exception?) {
        super.requestError(it)
        knowledgeAdapter.loadMoreModule.loadMoreFail()
    }

}