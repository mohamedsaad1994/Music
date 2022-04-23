package com.mondia.music.features.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mondia.music.base.network.response.Authorization
import com.mondia.music.base.network.response.Song
import com.mondia.music.base.states.IResult
import com.mondia.music.base.viewModel.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: HomeRepo = HomeRepo()) : BaseViewModel() {
    private var _authorizationResult: MutableLiveData<IResult<Authorization>> = MutableLiveData()
    val authorizationResult: MutableLiveData<IResult<Authorization>>
        get() = _authorizationResult

    private var _dataResult: MutableLiveData<IResult<List<Song>>> = MutableLiveData()
    val dataResult: MutableLiveData<IResult<List<Song>>>
        get() = _dataResult

    fun getAuthorizationToken(
    ) {
        _authorizationResult.initialize()
        viewModelScope.launch {
            _authorizationResult.value = repo.authorizeClient()
        }
    }

    fun getData(token: String, query: String) {
        _dataResult.initialize()
        viewModelScope.launch {
            _dataResult.value = repo.searchFor(token, query)
        }
    }
}