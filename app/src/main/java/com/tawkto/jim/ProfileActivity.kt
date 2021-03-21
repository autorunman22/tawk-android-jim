package com.tawkto.jim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tawkto.jim.databinding.ActivityProfileBinding
import com.tawkto.jim.model.User
import com.tawkto.jim.ui.ProfileViewModel
import com.tawkto.jim.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = getUser()
        Timber.d("Profile user: $user")

        binding = ActivityProfileBinding.inflate(layoutInflater).apply {
            vm = viewModel
            lifecycleOwner = this@ProfileActivity

            Glide.with(this@ProfileActivity)
                .load(user.avatarUrl)
                .into(ivAvatar)

        }
        setContentView(binding.root)

        fetchUserByName(user.username, user.id)

        setupCollection()
    }

    private fun setupCollection() {
        lifecycleScope.launchWhenStarted {
            viewModel.user.collect {
                when(it) {
                    is DataState.Success -> {
                        val profile = it.data
                        binding.apply {
                            with(profile) {
                                tvName.text = name ?: "No name"
                                tvCompany.text = company ?: ""
                                tvEmail.text = email ?: ""
                                tvBlog.text = blog ?: ""
                                tvFollowers.text = followers.toString()
                                tvFollowing.text = following.toString()
                            }
                        }
                    }
                    is DataState.Error -> {
                        Timber.d("Something went wrong: ${it.exception.message}")
                    }
                    else -> Timber.d("Handle other states")
                }

            }
        }
    }

    private fun fetchUserByName(name: String, id: Int) {
        viewModel.userByName(name, id)
    }

    private fun getUser() = intent.getSerializableExtra("user") as User
}