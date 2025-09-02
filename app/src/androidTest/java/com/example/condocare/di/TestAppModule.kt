package com.example.condocare.di

import android.content.Context
import androidx.room.Room
import com.example.condocare.data.CommunicationRepository
import com.example.condocare.data.local.AppDatabase
import com.example.condocare.data.local.CommunicationDao
import com.example.condocare.data.remote.ApiService
import com.example.condocare.data.remote.RemoteDataSource
import com.example.condocare.util.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideInMemoryDb(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): CommunicationDao {
        return db.communicationDao()
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        // This will be overridden in tests that need a mock server
        // For now, provide a basic retrofit instance pointing to localhost
        return Retrofit.Builder()
            .baseUrl("http://localhost:8080/") // Default for MockWebServer
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource {
        return RemoteDataSource(apiService)
    }

    @Singleton
    @Provides
    fun provideCommunicationRepository(
        remoteDataSource: RemoteDataSource,
        communicationDao: CommunicationDao
    ): CommunicationRepository {
        return CommunicationRepository(remoteDataSource, communicationDao)
    }
}
