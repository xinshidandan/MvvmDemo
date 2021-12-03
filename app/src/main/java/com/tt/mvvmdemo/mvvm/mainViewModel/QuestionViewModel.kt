package com.tt.mvvmdemo.mvvm.mainViewModel

import androidx.lifecycle.LiveData
import com.tt.mvvmdemo.httpUtils.ArticleListBean
import com.tt.mvvmdemo.mvvm.mainRepository.QuestionRepository
import com.tt.mvvmdemo.mvvm.viewModel.CommonViewModel
import com.tt.mvvmdemo.utils.SingleLiveEvent

class QuestionViewModel : CommonViewModel() {

    private val repository = QuestionRepository()
    private val questionData = SingleLiveEvent<ArticleListBean>()

    fun getQuestionList(page:Int):LiveData<ArticleListBean>{
        launchUI {
            val res = repository.getQuestionList(page)
            questionData.value = res.data
        }
        return questionData
    }
}