package com.kartikasw.kelilinkseller.features.store.menu.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.kartikasw.kelilinkseller.R
import com.kartikasw.kelilinkseller.core.domain.Resource
import com.kartikasw.kelilinkseller.core.domain.model.Menu
import com.kartikasw.kelilinkseller.databinding.ActivityMenuFormBinding
import com.kartikasw.kelilinkseller.features.store.menu.MenuActivity
import com.kartikasw.kelilinkseller.util.custom_view.KelilinkLoadingDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuFormBinding

    private val viewModel: AddMenuViewModel by viewModels()

    private lateinit var loading: KelilinkLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = KelilinkLoadingDialog(this)

        setUpToolbar()

        showLoadingState(false)
        binding.mfBtnAction.text = resources.getString(R.string.btn_add)

        setOnClickListener()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.mfToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_add_menu)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setOnClickListener() {
        with(binding) {
            mfIvMenu.setOnClickListener {
                pickImage()
            }

            mfBtnAction.setOnClickListener {
                val menuName = binding.mfEtName
                val description = binding.mfEtDescription
                val price = binding.mfEtPrice
                val unit = binding.mfEtUnit
                val menuNameData = menuName.text.toString()
                val descriptionData = description.text.toString()
                val priceData = price.text.toString()
                val unitData = unit.text.toString()
                val image = viewModel.uriImage.value

                if(menuName.error == null && price.error == null && unit.error == null && image != null
                    && description.error == null && menuNameData.isNotEmpty() && descriptionData.isNotEmpty()
                    && priceData.isNotEmpty() && unitData.isNotEmpty()) {
                    val menu = Menu(
                        description = descriptionData,
                        name = menuNameData,
                        price = priceData.toInt(),
                        unit = unitData
                    )

                    viewModel.addMenu(menu, image).observe(this@AddMenuActivity, ::menuResponse)
                } else {
                    Snackbar.make(binding.root, resources.getString(R.string.error_field), Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun menuResponse(data: Resource<Unit>) {
        when(data) {
            is Resource.Success -> {
                loading.dismiss()
                val intent = Intent(this, MenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
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

    private fun pickImage() {
        pickImage.launch("image/*")
    }

    private var pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        viewModel.setUriImage(it)

        Glide.with(binding.mfIvMenu.context)
            .load(
                it ?: ContextCompat.getDrawable(this, R.drawable.placeholder_add_image)
            )
            .transform(
                CenterCrop(),
                RoundedCorners(resources.getDimensionPixelOffset(R.dimen.corner_button))
            )
            .into(binding.mfIvMenu)
    }

    private fun showLoadingState(state: Boolean) {
        binding.mfLoading.root.isVisible = state
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val TAG = "AddMenuActivity"
    }

}