package com.kartikasw.kelilinkseller.core.domain.use_case.seller

import com.kartikasw.kelilinkseller.core.domain.Resource
import com.kartikasw.kelilinkseller.core.domain.model.Seller
import com.kartikasw.kelilinkseller.core.domain.repository.SellerRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SellerInteractor @Inject constructor(
    private val sellerRepository: SellerRepository
) : SellerUseCase {
    override fun getUser(): FirebaseUser =
        sellerRepository.getUser()

    override fun setFcmToken(token: String) {
        sellerRepository.setFcmToken(token)
    }

    override fun getFcmToken(): String =
        sellerRepository.getFcmToken()

    override fun updatePassword(oldPassword: String, newPassword: String): Flow<Resource<Seller>> =
        sellerRepository.updatePassword(oldPassword, newPassword)
}