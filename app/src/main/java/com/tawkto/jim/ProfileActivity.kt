package com.tawkto.jim

import android.R
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.tawkto.jim.databinding.ActivityProfileBinding
import com.tawkto.jim.ui.ProfileViewModel
import com.tawkto.jim.util.DataState
import com.tawkto.jim.util.isNetworkAvailable
import com.tawkto.jim.util.snack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        initAnimation()

        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater).apply {
            vm = viewModel
            lifecycleOwner = this@ProfileActivity

        }

        setContentView(binding.root)

        val user = getUser()
        fetchUserByName(user.first as Int to user.second.toString())

        Glide.with(this)
            .load(user.third)
            .into(binding.ivAvatar)

        setupCollection()

        checkNetStatus()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    private fun setupCollection() {
        // Collect profile info
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

        // Collect save event
        lifecycleScope.launchWhenStarted {
            viewModel.saveEvent.collect {
                binding.root.snack("Note saved successfully")
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

    private fun getUser() = intent.getSerializableExtra("userTriple") as Triple<*, *, *>

    private fun initAnimation() {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        findViewById<View>(android.R.id.content).transitionName = "shared_image_view"
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())

        // Set this Activity’s enter and return transition to a MaterialContainerTransform
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 300L
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 250L
        }
    }
}