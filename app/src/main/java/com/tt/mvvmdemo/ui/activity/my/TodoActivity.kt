package com.tt.mvvmdemo.ui.activity.my

import android.content.Intent
import android.os.Build
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.base.BaseViewModelActivity
import com.tt.mvvmdemo.mvvm.viewModel.TodoActivityViewModel
import com.tt.mvvmdemo.ui.adapter.TodoAdapter
import com.tt.mvvmdemo.ui.view.SwipeItemLayout
import kotlinx.android.synthetic.main.activity_todo.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class TodoActivity : BaseViewModelActivity<TodoActivityViewModel>() {

    companion object {
        const val DEAL_SUCCESS = 1
    }

    private val mAdapter by lazy { TodoAdapter() }
    private var isRefresh = false
    private lateinit var refreshLayout: SmartRefreshLayout
    private val linearLayoutManager by lazy { LinearLayoutManager(this) }
    private lateinit var dataMap: MutableMap<String, Any>
    private lateinit var popWindow: PopupWindow

    override fun providerVMClass() = TodoActivityViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_todo

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initData() {
        setTop("TODO", R.drawable.add)
        toolbar_subtitle_image.setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            intent.putExtra("type", "new")
            startActivityForResult(intent, DEAL_SUCCESS)
        }
    }

    override fun initView() {
        dataMap = mutableMapOf()
        refreshLayout = swipeRefresh_todo
        refreshLayout.run {
            setRefreshHeader(ch_header_todo)
            startHttp()
        }
        recyclerView_todo.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
            addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(this@TodoActivity))
        }
        mAdapter.run {
            recyclerView = recyclerView_todo
            //item使用的自定义的SwipeItemLayout，会导致item点击事件无效，所以在ChildClick添加item点击事件
            setOnItemClickListener { adapter, view, position -> }
            loadMoreModule.setOnLoadMoreListener {
                isRefresh = false
                refreshLayout.finishRefresh()
                val page = mAdapter.data.size / pageSize
                getTodoList(page, dataMap)
            }
            addChildClickViewIds(R.id.item_todo_done, R.id.item_todo_delete, R.id.rl_item_todo)
            setOnItemChildClickListener { adapter, view, position ->
                var status = data[position]
                when (view.id) {
                    R.id.rl_item_todo -> {
                        val intent = Intent(this@TodoActivity, AddTodoActivity::class.java)
                        intent.putExtra("id", data[position].id)
                        intent.putExtra("type", "edit")
                        intent.putExtra("dateStr", data[position].dateStr)
                        intent.putExtra("title", data[position].title)
                        intent.putExtra("priority", data[position].priority)
                        intent.putExtra("content", data[position].content)
                        startActivityForResult(intent, DEAL_SUCCESS)
                    }
                    R.id.item_todo_done -> {
                        if (data[position].status == 0) {
                            updateTodoById(data[position].id, 1)
                            if (toolbar_title.text == "TODO") {
                                status.status = 1
                                setData(position, status)
                            } else adapter.removeAt(position)
                        } else {
                            updateTodoById(data[position].id, 0)
                            if (toolbar_title.text == "TODO") {
                                status.status = 0
                                setData(position, status)
                            } else adapter.removeAt(position)
                        }
                    }
                    R.id.item_todo_delete -> {
                        deleteTodoById(data[position].id)
                        adapter.removeAt(position)
                    }
                }
            }
        }
    }

    override fun startHttp() {
        isRefresh = true
        getTodoList(0, dataMap)
    }

    private fun getTodoList(page: Int, map: MutableMap<String, Any>) {
        showLoading()
        viewModel.getTodoList(page + 1, map).observe(this, {
            hideLoading()
            it.datas.let { todoList ->
                setResult(RESULT_OK)
                mAdapter.run {
                    var hasTodo = false
                    for (i in 0 until todoList.size) {
                        if (todoList[i].status == 0) {
                            hasTodo = true
                            break
                        }
                    }
                    if (hasTodo) LiveEventBus.get("myBadge").post(true)
                    else LiveEventBus.get("myBadge").post(false)
                    if (isRefresh) {
                        refreshLayout.finishRefresh()
                        setList(todoList)
                        recyclerView.scrollToPosition(0)
                    } else addData(todoList)
                    if (data.size == 0) setEmptyView(R.layout.fragment_empty_layout)
                    else if (hasEmptyView()) removeEmptyView()
                    if (it.over) loadMoreModule.loadMoreEnd(isRefresh)
                    else loadMoreModule.loadMoreComplete()
                }
            }
        })
    }

    private fun updateTodoById(id: Int, status: Int) {
        viewModel.updateTodoById(id, status).observe(this, {
            setResult(RESULT_OK)
        })
    }

    private fun deleteTodoById(id: Int) {
        viewModel.deleteTodoById(id).observe(this, {
            setResult(RESULT_OK)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DEAL_SUCCESS && resultCode == RESULT_OK) {
            startHttp()
        }
    }

    override fun requestError(it: Exception) {
        super.requestError(it)
        mAdapter.loadMoreModule.loadMoreFail()
    }

}