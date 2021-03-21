package com.tawkto.jim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.tawkto.jim.databinding.ActivityProfileBinding
import com.tawkto.jim.model.User
import com.tawkto.jim.ui.ProfileViewModel
import timber.log.Timber

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = getUser()
        Timber.d("Profile user: $user")

        binding = ActivityProfileBinding.inflate(layoutInflater).apply {
            this.user = user
            Glide.with(this@ProfileActivity)
                .load(user.avatarUrl)
                .into(ivAvatar)

        }
        setContentView(binding.root)


    }

    private fun getUser() = intent.getSerializableExtra("user") as User
}