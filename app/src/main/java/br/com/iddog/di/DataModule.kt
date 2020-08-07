package br.com.iddog.di

import br.com.iddog.data.store.Store
import br.com.iddog.data.store.UserStore
import br.com.iddog.repository.DogsRepository
import br.com.iddog.repository.DogsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindDogsRepositoryImpl(
        repository: DogsRepositoryImpl
    ): DogsRepository

    @Singleton
    @Binds
    abstract fun bindUserStore(
        userStore: UserStore
    ): Store
}

