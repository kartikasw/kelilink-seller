package com.example.kelilinkseller.core.domain.use_case.seller

import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Seller
import com.example.kelilinkseller.core.domain.repository.SellerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SellerInteractor @Inject constructor(
    private val sellerRepository: SellerRepository
) : SellerUseCase {
    override fun setFcmToken(token: String) {
        sellerRepository.setFcmToken(token)
    }

    override fun getFcmToken(): String =
        sellerRepository.getFcmToken()

    override fun updatePassword(oldPassword: String, newPassword: String): Flow<Resource<Seller>> =
        sellerRepository.updatePassword(oldPassword, newPassword)
}