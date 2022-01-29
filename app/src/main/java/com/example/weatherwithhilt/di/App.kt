package com.example.weatherwithhilt.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App: Application() {

    @Inject lateinit var myToast: MyToast

}