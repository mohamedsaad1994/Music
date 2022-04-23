package com.mondia.music.features.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mondia.music.R
import com.mondia.music.base.network.response.Song
import com.mondia.music.base.states.CommonStatusImp
import com.mondia.music.databinding.ActivityHomeBinding
import com.mondia.music.utils.UtilsFunctions
import com.mondia.music.utils.UtilsFunctions.hideKeyboard

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private var token = ""
    private lateinit var adapterSongs: SongsAdapter

    companion object {
        private const val TAG = "HomeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        getAuthorizationToken()
        observeData()
        actions()
    }

    private fun actions() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.etSearch.hideKeyboard(this)
                getData()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun observeData() {
        viewModel.authorizationResult.observe(this) {
            when (it.whichStatus()) {
                CommonStatusImp.ERROR -> Log.e(
                    TAG,
                    "observeData: error while getting auth token: ${it.fetchError()?.second}"
                )
                CommonStatusImp.LOADING -> showLoading()
                CommonStatusImp.SUCCESS -> {
                    it.fetchData().let { auth ->
                        token = auth!!.token
                        Log.e(TAG, "observeData: token value: $token")
                    }
                    hideLoading()
                }
            }
        }
        viewModel.dataResult.observe(this) {
            when (it.whichStatus()) {
                CommonStatusImp.ERROR -> Log.e(
                    TAG,
                    "observeData: error while getting search result: ${it.fetchError()?.second}"
                )
                CommonStatusImp.LOADING -> showLoading()
                CommonStatusImp.SUCCESS -> {
                    showResultData(it.fetchData())
                    hideLoading()
                }
            }
        }
    }

    private fun showResultData(songs: List<Song>?) {
        if (songs.isNullOrEmpty()) {
            showEmptyState()
        } else {
            hideEmptyState()
            addDataToRecycler(songs)
        }
    }

    private fun setupRecyclerView() {
        adapterSongs = SongsAdapter()
        binding.rvSearchResult.adapter = adapterSongs
    }

    private fun addDataToRecycler(songs: List<Song>) {
        adapterSongs.addData(songs)
    }

    private fun showEmptyState() {
        binding.tvEmptyState.visibility = View.VISIBLE
    }

    private fun hideEmptyState() {
        binding.tvEmptyState.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.loader.visibility = View.GONE
    }

    private fun showLoading() {
        binding.loader.visibility = View.VISIBLE
    }

    private fun getAuthorizationToken() {
        if (UtilsFunctions.isNetworkAvailable(this)) {
            viewModel.getAuthorizationToken()
        } else
            UtilsFunctions.showToast(this, getString(R.string.no_internet_connection))
    }

    private fun getData() {
        if (UtilsFunctions.isNetworkAvailable(this)) {
            viewModel.getData(token, binding.etSearch.text.toString())
        } else
            UtilsFunctions.showToast(this, getString(R.string.no_internet_connection))
    }
}