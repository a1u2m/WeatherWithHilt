package com.example.weatherwithhilt.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MyToastModule {

    @Provides
    fun provideMyToast(app: Application): MyToast {
        return MyToast(app)
    }

}