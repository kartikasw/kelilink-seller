package com.example.kelilinkseller.features.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.kelilinkseller.features.MainActivity
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.databinding.FragmentLoginBinding
import com.example.kelilinkseller.features.auth.reset_password.ResetPasswordActivity
import com.example.kelilinkseller.util.custom_view.KelilinkLoadingDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var loading: KelilinkLoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading = KelilinkLoadingDialog(requireContext())

        setOnClickListener()
    }

    private fun setOnClickListener() {
        with(binding) {
            lBtnRegister.setOnClickListener {
                it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            lBtnLogin.setOnClickListener {
                login()
            }

            lBtnForgotPassword.setOnClickListener {
                startActivity(
                    Intent(requireContext(), ResetPasswordActivity::class.java)
                )
            }
        }
    }

    private fun login() {
        val email = binding.lEtEmail
        val password = binding.lEtPassword
        val emailData = email.text.toString()
        val passwordData = password.text.toString()

        Log.d(TAG, emailData)

        if(email.error == null && password.error == null
            && emailData.isNotEmpty() && passwordData.isNotEmpty()) {
            val token = loginViewModel.getFcmToken()
            loginViewModel.logIn(emailData, passwordData, token).observe(viewLifecycleOwner, ::loginResponse)
        }
    }

    private fun loginResponse(data: Resource<Unit>) {
        when(data) {
            is Resource.Success -> {
                loading.dismiss()
                startActivity(Intent(requireContext(), MainActivity::class.java)).also {
                    requireActivity().finish()
                }
            }
            is Resource.Loading -> {
                loading.show()
            }
            is Resource.Error -> {
                loading.dismiss()
                Log.e(TAG, data.message.toString())
                Snackbar.make(binding.root, data.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "LoginFragment"
    }
}