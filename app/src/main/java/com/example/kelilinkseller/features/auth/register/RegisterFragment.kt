package com.example.kelilinkseller.features.auth.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Seller
import com.example.kelilinkseller.core.domain.model.Store
import com.example.kelilinkseller.databinding.FragmentRegisterBinding
import com.example.kelilinkseller.util.custom_view.KelilinkLoadingDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {


    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels()

    private lateinit var categoryChipGroup: ChipGroup

    private lateinit var loading: KelilinkLoadingDialog

    private var categoryMap = mutableMapOf<Int, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading = KelilinkLoadingDialog(requireContext())

        setOnClickListener()

        setUpCategoryView()
    }

    private fun register() {
        val categoryList: MutableList<String> = mutableListOf()

        val storeName = binding.rEtStoreName
        val email = binding.rEtEmail
        val password = binding.rEtPassword
        val storeNameData = storeName.text.toString()
        val emailData = email.text.toString()
        val passwordData = password.text.toString()
        val selectedCategory = categoryChipGroup.checkedChipIds
        val image = registerViewModel.uriImage.value

        if(selectedCategory.isNotEmpty()) {
            for(categoryIndex in selectedCategory) {
                categoryList.add(categoryMap[categoryIndex]!!)
            }
        }

        if(
            selectedCategory.isNotEmpty() && storeName.error == null && email.error == null
            && password.error == null && storeNameData.isNotEmpty() && emailData.isNotEmpty()
            && passwordData.isNotEmpty() && image != null
        ) {
            val seller = Seller(
                uid = "",
                email = emailData
            )

            val store = Store(
                categories = categoryList,
                name = storeNameData
            )

            registerViewModel
                .register(emailData, passwordData, seller, store, image)
                .observe(viewLifecycleOwner, ::registerResponse)
        } else {
            Snackbar.make(binding.root, resources.getString(R.string.error_field), Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private fun registerResponse(data: Resource<Unit>) {
        when(data) {
            is Resource.Success -> {
                loading.dismiss()
                (binding.root).findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
            is Resource.Loading -> {
                loading.show()
            }
            is Resource.Error -> {
                loading.dismiss()
                Log.e(TAG, data.message.toString())
                Snackbar.make(binding.root,data.message.toString(), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setUpCategoryView() {
        categoryChipGroup = binding.rCgCategory
        val categories = resources.getStringArray(R.array.category_name)
        for (category in categories.indices) {
            categoryChipGroup.addView(
                Chip(requireContext()).apply {
                    id = category
                    text = categories[category]
                }
            )
            categoryMap[category] = categories[category]
        }
        categoryChipGroup.setOnCheckedStateChangeListener { group, _ ->
            if(group.checkedChipIds.size > 2) {
                categoryChipGroup.setChildrenEnabled(false)
            } else if(group.checkedChipIds.size > 1) {
                categoryChipGroup.setChildrenEnabled(true)
            }
        }

    }

    private fun ChipGroup.setChildrenEnabled(enable: Boolean) {
        children
            .toList()
            .filter { !(it as Chip).isChecked }
            .forEach { it.isEnabled = enable }
    }

    private fun setOnClickListener() {
        with(binding) {
            rBtnLogin.setOnClickListener {
                it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }

            rBtnRegister.setOnClickListener { register() }

            rIvStoreImage.setOnClickListener { pickImage() }
        }
    }

    private fun pickImage() {
        pickImage.launch("image/*")
    }

    private var pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        registerViewModel.setUriImage(it)

        Glide.with(binding.rIvStoreImage.context)
            .load(it)
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimensionPixelOffset(R.dimen.corner_button))
            )
            .into(binding.rIvStoreImage)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "RegisterFragment"
    }


}