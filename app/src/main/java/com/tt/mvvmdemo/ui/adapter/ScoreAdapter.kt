package com.tt.mvvmdemo.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tt.mvvmdemo.R
import com.tt.mvvmdemo.httpUtils.UserScoreBean

class ScoreAdapter : BaseQuickAdapter<UserScoreBean, BaseViewHolder>(R.layout.item_score_record),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: UserScoreBean) {
        holder.setText(R.id.tv_score_title, item.reason)
        holder.setText(R.id.tv_score_time, item.desc)
        holder.setText(R.id.tv_score_count, "+${item.coinCount}")
    }
}