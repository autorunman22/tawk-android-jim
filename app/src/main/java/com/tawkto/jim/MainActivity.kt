package com.tawkto.jim

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.tawkto.jim.databinding.ActivityMainBinding
import com.tawkto.jim.db.UserCacheMapper
import com.tawkto.jim.paging.adapter.UserAdapter
import com.tawkto.jim.paging.adapter.UserLoadStateAdapter
import com.tawkto.jim.ui.MainViewModel
import com.tawkto.jim.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NetCallback {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var networkUtil: NetworkUtil
    private var isInit = false

    private lateinit var userAdapter: UserAdapter
    @Inject lateinit var userCacheMapper: UserCacheMapper

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false

        super.onCreate(savedInstanceState)

        userAdapter = UserAdapter(viewModel) { user, view ->
            val options = ActivityOptions.makeSceneTransitionAnimation(this, view, "shared_image_view")
            val intent = Intent(this@MainActivity, ProfileActivity::class.java).apply {
                putExtra("userTriple",  Triple(user.id, user.username, user.avatarUrl))
            }
            startActivity(intent, options.toBundle())
        }.apply {
            withLoadStateHeaderAndFooter(
                footer = UserLoadStateAdapter(::retry),
                header = UserLoadStateAdapter(::retry)
            )
        }

        lifecycleScope.launchWhenStarted {
            userAdapter.loadStateFlow.collectLatest { state ->
                binding.apply {
                    loader.isVisible = (state.refresh is LoadState.Loading) || state.append is LoadState.Loading
                    btnRetry.isVisible = state.refresh is LoadState.Error || state.mediator?.append is LoadState.Error

                    if (state.refresh is LoadState.Error) {
                        toggleNetMode(false, state.refresh.toString())
                    }
                }
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            rvUsers.apply {
                adapter = userAdapter
                layoutManager = LinearLayoutManager(context)
                setDivider(R.drawable.rv_divider)
            }
            etSearch.setOnClickListener {
                val options = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, it, "transition_search")
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent, options.toBundle())
            }
            btnRetry.setOnClickListener {
                userAdapter.retry()
            }
        }
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            viewModel.flowPager.collect {
                userAdapter.submitData(it.map { userCacheEntity ->
                    userCacheMapper.mapFromEntity(userCacheEntity).also { user ->
                        user.hasNote = viewModel.hasNote(user.id)
                    }
                })
            }
        }

        initNetworkCallbacks()
    }

    private fun initNetworkCallbacks() {
        networkUtil = NetworkUtil(applicationContext, this).apply {
            startNetworkCallback()
        }
    }

    override fun onDestroy() {
        networkUtil.stopNetworkCallback()
        super.onDestroy()
    }

    override fun onOnline() {
        if (isInit) {
            Timber.d("Do something online")
            toggleNetMode(true)
            userAdapter.retry()
        } else isInit = true // On Activity load, let onCreate fetches the data
    }

    override fun onOffline() {
        Timber.d("Offline mode")
        toggleNetMode(false)
    }

    private fun toggleNetMode(isOnline: Boolean, errorMessage: String = getText(R.string.offline_mode).toString()) {
        Timber.d("Toggling: $isOnline")
        binding.apply {
            lifecycleScope.launch(Dispatchers.Main) {
                if (isOnline) {
                    tvNetStatus.setBackgroundResource(R.color.green)
                    tvNetStatus.text = getText(R.string.online)
                    tvNetStatus.visibility = View.VISIBLE
                    delay(2500)
                    tvNetStatus.visibility = View.GONE
                } else {
                    tvNetStatus.setBackgroundResource(R.color.red)
                    tvNetStatus.text = errorMessage
                    tvNetStatus.visibility = View.VISIBLE
                }
            }
        }
    }
}