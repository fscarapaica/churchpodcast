package com.mano.churchpodcast

import android.app.Application
import com.google.firebase.FirebaseApp

class ApplicationBase: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

}