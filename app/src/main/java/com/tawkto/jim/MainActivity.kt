package com.tawkto.jim

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
                    else -> Timber.d("success: ${it}")
                }
            }
        }
    }

    private fun initUsersList(users: List<User>) {
        binding.epoxyUsers.apply {
            withModels {
                for (user in users) {
                    layoutUser {
                        id(user.id)
                        user(user)
                        vm(viewModel)
                        onBind { _, view, _ ->
                            (view.dataBinding as LayoutUserBinding).apply {
                                Glide.with(context)
                                    .load(user.avatarUrl)
                                    .placeholder(R.drawable.tawk)
                                    .into(ivAvatar)
                            }
                        }
                    }
                }
            }
            requestModelBuild()
            layoutManager = LinearLayoutManager(context)
            setDivider(R.drawable.rv_divider)
        }
    }
}