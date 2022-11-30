package com.example.kelilinkseller.features.store.menu.detail

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Menu
import com.example.kelilinkseller.databinding.ActivityDetailMenuBinding
import com.example.kelilinkseller.features.store.menu.MenuActivity
import com.example.kelilinkseller.features.store.menu.MenuActivity.Companion.EXTRA_EDIT_MENU
import com.example.kelilinkseller.features.store.menu.MenuActivity.Companion.EXTRA_MENU_ID
import com.example.kelilinkseller.features.store.menu.edit.EditMenuActivity
import com.example.kelilinkseller.util.custom_view.KelilinkLoadingDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMenuBinding

    private val viewModel: DetailMenuViewModel by viewModels()

    private var menuId: String = ""
    private var editMenu: Boolean = false

    private lateinit var loading: KelilinkLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = KelilinkLoadingDialog(this)

        menuId = intent?.getStringExtra(EXTRA_MENU_ID).toString()
        editMenu = intent.getBooleanExtra(EXTRA_EDIT_MENU, false)

        setUpToolbar()

        showDetailMenuView()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.dmToolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.delete -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(resources.getString(R.string.title_delete))
                    .setMessage(resources.getString(R.string.content_delete_menu))
                    .setNegativeButton(resources.getString(R.string.btn_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(resources.getString(R.string.btn_delete)) { dialog, _ ->
                        deleteMenu(dialog)
                    }
                    .show()
            }
            R.id.edit -> {
                val intent = Intent(this, EditMenuActivity::class.java)
                intent.putExtra(EXTRA_MENU_ID, menuId)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteMenu(dialog: DialogInterface) {
        viewModel.deleteMenu(menuId).observe(this) {
            when(it) {
                is Resource.Success -> {
                    loading.dismiss()
                    dialog.dismiss()
                    val intent = Intent(this, MenuActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
                is Resource.Loading -> {
                    loading.show()
                }
                is Resource.Error -> {
                    loading.dismiss()
                    Log.e(TAG, it.message.toString())
                }
            }
        }
    }

    private fun showDetailMenuView() {
        viewModel.getMenuById(menuId).observe(this) {
            Log.d(TAG, it.data.toString())
            when(it) {
                is Resource.Success -> {
                    setUpDetailMenuView(it.data)
                    showLoadingState(false)
                    showInfo(true)
                }
                is Resource.Loading -> {
                    showInfo(false)
                    showLoadingState(true)
                }
                is Resource.Error -> {
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpDetailMenuView(data: Menu?) {
        with(binding) {
            Glide.with(dmIvMenuImage.context)
                .load(data!!.image)
                .transform(
                    CenterCrop(),
                    RoundedCorners(resources.getDimensionPixelOffset(R.dimen.corner_button))
                )
                .into(dmIvMenuImage)

            dmTvName.text = data.name
            dmTvPriceAndAmount.text = "${data.price}/${data.unit}"
            dmTvDescription.text = data.description
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.dmLoading.root.isVisible = state
    }

    private fun showInfo(state: Boolean) {
        binding.dmLayoutInfo.isVisible = state
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if(editMenu) {
            val intent = Intent(this, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } else {
            return super.onBackPressed()
        }
    }


    companion object {
        const val TAG = "DetailMenuActivity"
    }
}