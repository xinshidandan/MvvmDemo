package com.tt.mvvmdemo.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.tt.mvvmdemo.httpUtils.KnowledgeTreeBody
import com.tt.mvvmdemo.mvvm.mainRepository.SystemRepository
import com.tt.mvvmdemo.utils.SingleLiveEvent

class KnowledgeViewModel : CommonViewModel() {

    private val repository = SystemRepository()
    private val knowledgeTree = SingleLiveEvent<List<KnowledgeTreeBody>>()

    fun getKnowledgeTree(): LiveData<List<KnowledgeTreeBody>> {
        launchUI {
            val result = repository.getKnowledgeTree()
            knowledgeTree.value = result.data
        }
        return knowledgeTree
    }
}