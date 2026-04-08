package org.bssm.studentidreplica.data.preferences

import kotlinx.coroutines.flow.Flow

interface ConsentRepository {
    fun observeConsent(): Flow<Boolean>
    suspend fun acceptConsent()
}
