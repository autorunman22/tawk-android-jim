package com.tawkto.jim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tawkto.jim.databinding.ActivityMainBinding
import com.tawkto.jim.databinding.LayoutUserBinding
import com.tawkto.jim.model.User
import com.tawkto.jim.ui.MainViewModel
import com.tawkto.jim.util.DataState
import com.tawkto.jim.util.setDivider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

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

    override fun onResume() {
        super.onResume()
        viewModel.loadUsers()
    }
}