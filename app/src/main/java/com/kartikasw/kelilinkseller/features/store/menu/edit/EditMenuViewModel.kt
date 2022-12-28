package com.kartikasw.kelilinkseller.features.store.menu.edit

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilinkseller.core.domain.use_case.store.StoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditMenuViewModel @Inject constructor(
    private val storeUseCase: StoreUseCase
): ViewModel() {

    private val _uriImage = MutableLiveData<Uri?>()
    val uriImage: LiveData<Uri?> = _uriImage

    fun setUriImage(uri: Uri?) {
        _uriImage.value = uri
    }

    fun getMenuById(menuId: String) =
        storeUseCase.getMenuById(menuId).asLiveData()

    fun updateMenu(menuId: String, menu: MutableMap<String, Any>, uri: Uri) =
        storeUseCase.updateMenu(menuId, menu, uri).asLiveData()

    fun updateMenu(menuId: String, menu: MutableMap<String, Any>) =
        storeUseCase.updateMenu(menuId, menu).asLiveData()

}