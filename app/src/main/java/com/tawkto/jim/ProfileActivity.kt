package com.tawkto.jim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tawkto.jim.databinding.ActivityProfileBinding
import com.tawkto.jim.model.User
import com.tawkto.jim.ui.ProfileViewModel
import com.tawkto.jim.util.DataState
import com.tawkto.jim.util.isNetworkAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater).apply {
            vm = viewModel
            lifecycleOwner = this@ProfileActivity

        }
        setContentView(binding.root)

        val user = getUser()
        fetchUserByName(user.first as Int to user.second.toString())

        setupCollection()

        checkNetStatus()
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
                                viewModel.note.value = note

                                Glide.with(this@ProfileActivity)
                                    .load(avatarUrl)
                                    .into(ivAvatar)
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

    private fun fetchUserByName(userPair: Pair<Int, String>) {
        viewModel.userByUsername(userPair)
    }

    private fun checkNetStatus() {
        binding.apply {
            if (isNetworkAvailable()) {
                tvNetStatus.visibility = View.GONE
            } else {
                tvNetStatus.visibility = View.VISIBLE
            }
        }
    }

    private fun getUser() = intent.getSerializableExtra("userPair") as Pair<*, *>
}