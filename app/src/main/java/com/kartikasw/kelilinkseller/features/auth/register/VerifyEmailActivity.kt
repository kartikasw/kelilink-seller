package com.kartikasw.kelilinkseller.features.auth.register

import android.content.Intent
import android.content.Intent.EXTRA_EMAIL
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import androidx.appcompat.app.AppCompatActivity
import com.kartikasw.kelilinkseller.R
import com.kartikasw.kelilinkseller.databinding.ActivityVerifyEmailBinding
import com.kartikasw.kelilinkseller.features.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class VerifyEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyEmailBinding
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent.getStringExtra(EXTRA_EMAIL)!!

        setOnClickListener()

        val text = resources.getString(R.string.content_verify_email, email)
        val styledText: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }
        binding.veTvContent.text = styledText
    }

    private fun setOnClickListener() {
        binding.veBtnMove.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java)).also {
                finish()
            }
        }
    }
}