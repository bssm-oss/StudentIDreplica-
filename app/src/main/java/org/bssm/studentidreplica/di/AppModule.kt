package org.bssm.studentidreplica.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.bssm.studentidreplica.data.local.AppDatabase
import org.bssm.studentidreplica.data.local.dao.TagDao
import org.bssm.studentidreplica.data.preferences.ConsentRepository
import org.bssm.studentidreplica.data.preferences.DataStoreConsentRepository
import org.bssm.studentidreplica.data.repository.RoomTagRepository
import org.bssm.studentidreplica.data.repository.TagRepository

@Module
@InstallIn(SingletonComponent::class)
object AppProvidesModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "student-id-replica.db"
    ).build()

    @Provides
    fun provideTagDao(database: AppDatabase): TagDao = database.tagDao()

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("student_id_replica.preferences_pb") }
        )
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindsModule {
    @Binds
    abstract fun bindTagRepository(impl: RoomTagRepository): TagRepository

    @Binds
    abstract fun bindConsentRepository(impl: DataStoreConsentRepository): ConsentRepository
}
