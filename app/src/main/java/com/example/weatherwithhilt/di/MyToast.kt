package com.example.weatherwithhilt.di

import android.content.Context
import android.widget.Toast
import com.example.weatherwithhilt.R
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
class MyToast (context: Context?) : Toast(context){

    override fun show() {
        duration = LENGTH_LONG
        setText(R.string.error)
        super.show()
    }
}