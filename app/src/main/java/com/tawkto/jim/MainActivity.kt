package com.tawkto.jim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tawkto.jim.databinding.ActivityMainBinding
import com.tawkto.jim.model.User
import com.tawkto.jim.ui.MainViewModel
import com.tawkto.jim.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NetCallback {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var networkUtil: NetworkUtil
    private var isInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            viewModel.users.collect {
                when (it) {
                    is DataState.Success -> {
                        initUsersList(it.data)
                    }
                    is DataState.Error -> {
                        Timber.d("Error occurred. Please try again.")
                    }
                    is DataState.Loading -> {
                        Timber.d("Loading..")
                    }
                    is DataState.Initial -> {
                        Timber.d("Do nothing")
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.navToProfile.collect {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java).apply {
                    putExtra("user", it)
                }
                startActivity(intent)
            }
        }

        viewModel.loadUsers()

        initNetworkCallbacks()
    }

    // Update the RecyclerView for each collection
    private fun initUsersList(users: List<User>) {
        binding.epoxyUsers.apply {
            withModels {
                users.forEachIndexed { index, user ->
                    layoutUser {
                        id(index)
                        index(index)
                        user(user)
                        vm(viewModel)
                    }
                }
            }
            requestModelBuild()
            layoutManager = LinearLayoutManager(context)
            setDivider(R.drawable.rv_divider)
        }
    }

    private fun initNetworkCallbacks() {
        networkUtil = NetworkUtil(applicationContext, this).apply {
            startNetworkCallback()
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUsers()
    }

    override fun onDestroy() {
        networkUtil.stopNetworkCallback()
        super.onDestroy()
    }

    override fun onOnline() {
        if (isInit) {
            Timber.d("Do something online")
            toggleNetMode(true)
            viewModel.loadUsers()
        } else isInit = true // On Activity load, let onCreate fetches the data
    }

    override fun onOffline() {
        Timber.d("Offline mode")
        toggleNetMode(false)
    }

    private fun toggleNetMode(isOnline: Boolean) {
        Timber.d("Toggleing: $isOnline")
        binding.apply {
            lifecycleScope.launch(Dispatchers.Main) {
                if (isOnline) {
                    Timber.d("are we even?")
                    tvNetStatus.setBackgroundResource(R.color.green)
                    tvNetStatus.text = getText(R.string.online)
                    tvNetStatus.visibility = View.VISIBLE
                    delay(2500)
                    Timber.d("are we even?")
                    tvNetStatus.visibility = View.GONE
                } else {
                    tvNetStatus.setBackgroundResource(R.color.red)
                    tvNetStatus.text = getText(R.string.offline_mode)
                    tvNetStatus.visibility = View.VISIBLE
                }
            }
        }
    }
}