package com.example.kelilinkseller.features.store.profile.edit_profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.children
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.CATEGORY_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.NAME_COLUMN
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Store
import com.example.kelilinkseller.databinding.ActivityEditProfileBinding
import com.example.kelilinkseller.features.store.profile.ProfileActivity
import com.example.kelilinkseller.util.custom_view.KelilinkLoadingDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    private val editProfileViewModel: EditProfileViewModel by viewModels()

    private lateinit var categoryChipGroup: ChipGroup
    private var categoryMap = mutableMapOf<Int, String>()

    private lateinit var loading: KelilinkLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = KelilinkLoadingDialog(this)

        setUpCategoryView()

        setUpToolbar()

        showForm()

        setOnClickListener()
    }

    private fun setUpCategoryView() {
        categoryChipGroup = binding.epCgCategory
        val categories = resources.getStringArray(R.array.category_name)
        for (category in categories.indices) {
            categoryChipGroup.addView(
                Chip(this).apply {
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


    private fun setUpToolbar() {
        setSupportActionBar(binding.epToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_edit_store_profile)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun showForm() {
        val storeId = Firebase.auth.currentUser!!.uid
        editProfileViewModel.getStoreById(storeId).observe(this) {
            when(it) {
                is Resource.Success -> {
                    setUpFormView(it.data!!)
                    showInfo(true)
                    showLoadingState(false)
                }
                is Resource.Loading -> {
                    showInfo(false)
                    showLoadingState(true)
                }
                is Resource.Error -> {
                    showInfo(false)
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                }
            }
        }
    }

    private fun setUpFormView(data: Store) {
        with(binding) {
            Glide.with(epIvStoreImage.context)
                .load(data.image)
                .transform(CenterCrop(), RoundedCorners(resources.getDimensionPixelOffset(R.dimen.corner_button)))
                .into(epIvStoreImage)

            epEtStoreName.setText(data.name)
        }
    }

    private fun showInfo(state: Boolean) {
        binding.epInfo.isVisible = state
    }

    private fun showLoadingState(state: Boolean) {
        binding.epLoading.root.isVisible = state
    }

    private fun setOnClickListener() {
        with(binding) {
            epBtnSave.setOnClickListener {
                val categoryList: MutableList<String> = mutableListOf()

                val storeName = epEtStoreName
                val storeNameData = storeName.text.toString()
                val image = editProfileViewModel.uriImage.value
                val selectedCategory = categoryChipGroup.checkedChipIds

                if(selectedCategory.isNotEmpty()) {
                    for(categoryIndex in selectedCategory) {
                        categoryList.add(categoryMap[categoryIndex]!!)
                    }
                }

                if(storeName.error == null && storeNameData.isNotEmpty()
                    && selectedCategory.isNotEmpty()
                ) {

                    val data = mutableMapOf(
                        NAME_COLUMN to storeNameData,
                        CATEGORY_COLUMN to categoryList
                    )

                    if(image != null) {
                        editProfileViewModel.updateMyStore(data, image)
                            .observe(this@EditProfileActivity, ::storeResponse)
                    } else {
                        editProfileViewModel.updateMyStore(data)
                            .observe(this@EditProfileActivity, ::storeResponse)
                    }

                } else {
                    Snackbar.make(binding.root, resources.getString(R.string.error_field), Snackbar.LENGTH_LONG)
                        .show()
                }
            }

            epIvStoreImage.setOnClickListener { pickImage() }
        }
    }


    private fun storeResponse(data: Resource<Unit>) {
        when(data) {
            is Resource.Success -> {
                loading.dismiss()
                val intent = Intent(
                    this@EditProfileActivity,
                    ProfileActivity::class.java
                )
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            is Resource.Loading -> {
                loading.show()
            }
            is Resource.Error -> {
                loading.dismiss()
                Log.e(TAG, data.message.toString())
            }
        }
    }

    private fun pickImage() {
        pickImage.launch("image/*")
    }

    private var pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        editProfileViewModel.setUriImage(it)

        Glide.with(binding.epIvStoreImage.context)
            .load(it)
            .placeholder(R.drawable.placeholder_add_image)
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimensionPixelOffset(R.dimen.corner_button))
            )
            .into(binding.epIvStoreImage)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val TAG = "EditProfileActivity"
    }
}