package org.bssm.studentidreplica.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreConsentRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : ConsentRepository {

    override fun observeConsent(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[CONSENT_ACCEPTED] ?: false
    }

    override suspend fun acceptConsent() {
        dataStore.edit { preferences ->
            preferences[CONSENT_ACCEPTED] = true
        }
    }

    private companion object {
        val CONSENT_ACCEPTED = booleanPreferencesKey("consent_accepted")
    }
}
