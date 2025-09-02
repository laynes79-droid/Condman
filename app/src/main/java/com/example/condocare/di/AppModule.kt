package com.example.condocare.di

import android.content.Context
import com.example.condocare.data.CommunicationRepository
import com.example.condocare.data.local.AppDatabase
import com.example.condocare.data.local.CommunicationDao
import com.example.condocare.data.remote.ApiService
import com.example.condocare.data.remote.RemoteDataSource
import com.example.condocare.util.SessionManager
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // A BASE_URL será substituída pelo IP local correto durante os testes
    private const val BASE_URL = "http://10.0.2.2:8000/api/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideCommunicationDao(appDatabase: AppDatabase): CommunicationDao {
        return appDatabase.communicationDao()
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
