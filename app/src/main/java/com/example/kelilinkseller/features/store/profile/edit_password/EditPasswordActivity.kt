package com.example.kelilinkseller.features.store.profile.edit_password

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.activity.viewModels
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Seller
import com.example.kelilinkseller.databinding.ActivityEditPasswordBinding
import com.example.kelilinkseller.databinding.ActivityEditProfileBinding
import com.example.kelilinkseller.features.store.profile.ProfileActivity
import com.example.kelilinkseller.util.custom_view.KelilinkLoadingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPasswordBinding

    private val editPasswordViewModel: EditPasswordViewModel by viewModels()

    private lateinit var loading: KelilinkLoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = KelilinkLoadingDialog(this)

        setUpToolbar()

        setUpView()

        setOnClickListener()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.epToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_edit_password)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setUpView() {
        with(binding) {
            epEtField1.hint = resources.getString(R.string.hint_password_old)
            epEtField1.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            epEtField1.transformationMethod = PasswordTransformationMethod.getInstance()
            epEtField2.hint = resources.getString(R.string.hint_password_new)
            epEtField2.transformationMethod = PasswordTransformationMethod.getInstance()
            epEtField2.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    private fun setOnClickListener() {
        binding.lBtnSave.setOnClickListener {
            with(binding) {
                val oldPassword = epEtField1
                val newPassword = epEtField2
                val oldPasswordData = oldPassword.text.toString()
                val newPasswordData = newPassword.text.toString()

                if(oldPassword.error == null && newPassword.error == null
                    && oldPasswordData.isNotEmpty() && newPasswordData.isNotEmpty()) {
                    editPasswordViewModel.updatePassword(oldPasswordData, newPasswordData)
                        .observe(this@EditPasswordActivity, ::response)
                } else {
                    Toast.makeText(this@EditPasswordActivity, resources.getString(R.string.error_field), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun response(resource: Resource<Seller>) {
        when(resource) {
            is Resource.Success -> {
                loading.dismiss()
                if(resource.data == null) {
                    Toast.makeText(this, "Gagal ganti kata sandi", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
            }
            is Resource.Loading -> {
                loading.show()
            }
            is Resource.Error -> {
                loading.dismiss()
                Toast.makeText(this, resource.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}