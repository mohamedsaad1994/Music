package com.mondia.music.base.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mondia.music.base.states.IResult
import com.mondia.music.base.states.ResponseResult

abstract class BaseViewModel : ViewModel() {

    fun <T> MutableLiveData<IResult<T>>.initialize() {
        this.value = ResponseResult.loading()
    }
}