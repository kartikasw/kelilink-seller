package com.example.kelilinkseller.features.store.menu.edit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.DESCRIPTION_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.NAME_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.PRICE_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.UNIT_COLUMN
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Menu
import com.example.kelilinkseller.databinding.ActivityMenuFormBinding
import com.example.kelilinkseller.features.store.menu.MenuActivity.Companion.EXTRA_EDIT_MENU
import com.example.kelilinkseller.features.store.menu.MenuActivity.Companion.EXTRA_MENU_ID
import com.example.kelilinkseller.features.store.menu.detail.DetailMenuActivity
import com.example.kelilinkseller.util.custom_view.KelilinkLoadingDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuFormBinding

    private val editMenuViewModel: EditMenuViewModel by viewModels()

    private var menuId: String = ""

    private lateinit var loading: KelilinkLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuId = intent?.getStringExtra(EXTRA_MENU_ID).toString()

        loading = KelilinkLoadingDialog(this)

        setUpToolbar()

        showForm()

        setOnClickListener()
    }

    private fun showForm() {
        editMenuViewModel.getMenuById(menuId).observe(this) {
            when(it) {
                is Resource.Success -> {
                    setUpFormView(it.data!!)
                    showLoadingState(false)
                    showInfo(true)
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

    private fun setUpFormView(menu: Menu) {
        with(binding) {
            mfBtnAction.text = resources.getString(R.string.btn_save)

            Glide.with(mfIvMenu.context)
                .load(menu.image)
                .transform(CenterCrop(), RoundedCorners(resources.getDimensionPixelOffset(R.dimen.corner_button)))
                .into(mfIvMenu)

            mfEtName.setText(menu.name)
            mfEtPrice.setText(menu.price.toString())
            mfEtUnit.setText(menu.unit)
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.mfToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_edit_menu)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setOnClickListener() {
        with(binding) {
            mfBtnAction.setOnClickListener {
                val name = mfEtName
                val description = mfEtDescription
                val price = mfEtPrice
                val unit = mfEtUnit
                val nameData = name.text.toString()
                val descriptionData = description.text.toString()
                val priceData = price.text.toString()
                val unitData = unit.text.toString()
                val image = editMenuViewModel.uriImage.value

                if(name.error == null && price.error == null && unit.error == null && description.error == null
                    && nameData.isNotEmpty() && descriptionData.isNotEmpty() && priceData.isNotEmpty() && unitData.isNotEmpty()) {

                    if(image != null) {
                        editMenuViewModel.updateMenu(
                            menuId,
                            mutableMapOf(
                                NAME_COLUMN to nameData,
                                DESCRIPTION_COLUMN to descriptionData,
                                PRICE_COLUMN to priceData.toInt(),
                                UNIT_COLUMN to unitData
                            ),
                            image
                        ).observe(this@EditMenuActivity, ::menuResponse)
                    } else {
                        editMenuViewModel.updateMenu(
                            menuId,
                            mutableMapOf(
                                NAME_COLUMN to nameData,
                                DESCRIPTION_COLUMN to descriptionData,
                                PRICE_COLUMN to priceData.toInt(),
                                UNIT_COLUMN to unitData
                            )
                        ).observe(this@EditMenuActivity, ::menuResponse)
                    }

                } else {
                    Snackbar.make(binding.root, resources.getString(R.string.error_field), Snackbar.LENGTH_LONG)
                        .show()
                }
            }

            mfIvMenu.setOnClickListener {
                pickImage()
            }
        }
    }

    private fun menuResponse(data: Resource<Unit>) {
        when(data) {
            is Resource.Success -> {
                loading.dismiss()
                val intent = Intent(this, DetailMenuActivity::class.java)
                intent
                    .putExtra(EXTRA_MENU_ID, menuId)
                    .putExtra(EXTRA_EDIT_MENU, true)
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
        editMenuViewModel.setUriImage(it)

        Glide.with(binding.mfIvMenu.context)
            .load(it)
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimensionPixelOffset(R.dimen.corner_button))
            )
            .into(binding.mfIvMenu)
    }

    private fun showLoadingState(state: Boolean) {
        binding.mfLoading.root.isVisible = state
    }

    private fun showInfo(state: Boolean) {
        binding.mfInfo.isVisible = state
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val TAG = "EditMenuActivity"
    }
}