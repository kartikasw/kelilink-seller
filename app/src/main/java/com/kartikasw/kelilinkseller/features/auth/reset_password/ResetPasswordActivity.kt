package com.kartikasw.kelilinkseller.features.auth.reset_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.kartikasw.kelilinkseller.R
import com.kartikasw.kelilinkseller.core.domain.Resource
import com.kartikasw.kelilinkseller.databinding.ActivityResetPasswordBinding
import com.kartikasw.kelilinkseller.features.auth.AuthActivity
import com.kartikasw.kelilinkseller.util.custom_view.KelilinkLoadingDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding

    private val resetPasswordViewModel: ResetPasswordViewModel by viewModels()

    private lateinit var loading: KelilinkLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = KelilinkLoadingDialog(this)

        setUpToolbar()

        setOnClickListener()

    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.rpToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_forgot_password)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setOnClickListener() {
        binding.lBtnSend.setOnClickListener {
            val email = binding.rpEtEmail
            val emailData = email.text.toString()

            if(email.error == null && emailData.isNotEmpty()) {
                resetPasswordViewModel.resetPassword(emailData).observe(this) {
                    when(it) {
                        is Resource.Success -> {
                            loading.dismiss()
                            Toast.makeText(this, "Link berhasil terkirim", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, AuthActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                        }
                        is Resource.Loading -> {
                            loading.show()
                        }
                        is Resource.Error -> {
                            loading.dismiss()
                            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                            Log.e(TAG, it.message.toString())
                        }
                    }
                }
            } else {
                Snackbar.make(binding.root, resources.getString(R.string.error_field), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    companion object {
        const val TAG = "ResetPasswordActivity"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}