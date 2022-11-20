package com.example.kelilinkseller.features.store.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Menu
import com.example.kelilinkseller.core.ui.MenuAdapter
import com.example.kelilinkseller.databinding.ActivityMenuBinding
import com.example.kelilinkseller.features.store.menu.add.AddMenuActivity
import com.example.kelilinkseller.features.store.menu.detail.DetailMenuActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    private val viewModel: MenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        setOnClickListener()

        showMenu()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.mToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_menu)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun showMenu() {
        viewModel.getMyMenu().observe(this) {
            when(it) {
                is Resource.Success -> {
                    if(it.data != null) {
                        showEmptyState(false)
                        setUpMenuView(it.data)
                        showLoadingState(false)
                    } else {
                        showLoadingState(false)
                        showEmptyState(true)
                    }
                }
                is Resource.Loading -> {
                    showEmptyState(false)
                    showLoadingState(true)
                }
                is Resource.Error -> {
                    showLoadingState(false)
                    showEmptyState(false)
                    Log.e(TAG, it.message.toString())
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setUpMenuView(data: List<Menu>?) {
        val menuAdapter = MenuAdapter()

        menuAdapter.apply {
            onItemClick = {
                val intent = Intent(this@MenuActivity, DetailMenuActivity::class.java)
                intent.putExtra(EXTRA_MENU_ID, it.id)
                startActivity(intent)
            }
        }

        menuAdapter.setItems(data!!)

        with(binding.mRvMenu) {
            layoutManager = LinearLayoutManager(this@MenuActivity)
            adapter = menuAdapter
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.mLoading.root.isVisible = state
    }

    private fun showEmptyState(state: Boolean) {
        binding.mEmpty.root.isVisible = state
    }

    private fun setOnClickListener() {
        with(binding) {
            mFabAdd.setOnClickListener {
                startActivity(Intent(this@MenuActivity, AddMenuActivity::class.java))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val TAG = "MenuActivity"
        const val EXTRA_MENU_ID = "menu_id"
        const val EXTRA_EDIT_MENU = "edit_menu"
    }
}