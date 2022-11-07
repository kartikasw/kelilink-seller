package com.example.kelilinkseller.features.store.menu.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kelilinkseller.core.domain.model.Menu
import com.example.kelilinkseller.core.domain.use_case.store.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddMenuViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
): ViewModel() {

    private val _uriImage = MutableLiveData<Uri?>()
    val uriImage: LiveData<Uri?> = _uriImage

    fun setUriImage(uri: Uri?) {
        _uriImage.value = uri
    }

    fun addMenu(menu: Menu, uri: Uri) =
        storeUseCase.addMenu(menu, uri).asLiveData()
}