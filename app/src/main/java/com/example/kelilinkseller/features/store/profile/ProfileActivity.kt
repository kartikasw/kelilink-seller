package com.example.kelilinkseller.features.store.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Store
import com.example.kelilinkseller.databinding.ActivityProfileBinding
import com.example.kelilinkseller.features.auth.AuthActivity
import com.example.kelilinkseller.features.store.profile.edit_password.EditPasswordActivity
import com.example.kelilinkseller.features.store.profile.edit_profile.EditProfileActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        setOnClickListener()

        showDetailStore()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.pToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_profile)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setOnClickListener() {
        with(binding) {
            pBtnEditProfile.setOnClickListener {
                startActivity(Intent(this@ProfileActivity, EditProfileActivity::class.java))
            }

            pBtnEditPassword.setOnClickListener {
                startActivity(Intent(this@ProfileActivity, EditPasswordActivity::class.java))
            }

            pBtnLogout.setOnClickListener {
                MaterialAlertDialogBuilder(this@ProfileActivity)
                    .setTitle(resources.getString(R.string.title_logout))
                    .setMessage(resources.getString(R.string.content_logout))
                    .setNegativeButton(resources.getString(R.string.btn_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(resources.getString(R.string.btn_logout)) { _, _ ->
                        profileViewModel.logout()
                        startActivity(Intent(this@ProfileActivity, AuthActivity::class.java))
                        finish()
                    }
                    .show()
            }
        }
    }

    private fun showDetailStore() {
        val storeId = Firebase.auth.currentUser!!.uid
        profileViewModel.getStoreById(storeId).observe(this) {
            when(it) {
                is Resource.Success -> {
                    if(it.data != null) {
                        setUpDetailStoreView(it.data)
                        showDetailInfo(true)
                        showLoadingState(false)
                    } else {
                        onBackPressed()
                    }
                }
                is Resource.Loading -> {
                    showDetailInfo(false)
                    showLoadingState(true)
                }
                is Resource.Error -> {
                    showDetailInfo(false)
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                }
            }
        }
    }

    private fun setUpDetailStoreView(data: Store) {
        with(binding) {
            var category = ""
            for(text in data.categories) {
                category = if(data.categories[data.categories.lastIndex] == text) {
                    "$category$text"
                } else {
                    "$category$text, "
                }
            }

            Glide.with(pIvStoreImage.context)
                .load(data.image)
                .placeholder(R.drawable.shimmer_placeholder_rectangle)
                .into(pIvStoreImage)

            pTvStoreName.text = data.name
            pTvCategory.text = category
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.pLoading.root.isVisible = state
    }

    private fun showDetailInfo(state: Boolean) {
        binding.pLayoutProfile.isVisible = state
    }

    companion object {
        const val TAG = "ProfileActivity"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}