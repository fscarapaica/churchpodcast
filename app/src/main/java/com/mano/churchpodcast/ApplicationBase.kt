package com.mano.churchpodcast

import android.app.Application
import com.google.firebase.FirebaseApp
import com.mano.churchpodcast.db.MediaRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ApplicationBase: Application() {

    override fun onCreate() {
        super.onCreate()

        MainScope().launch {
            MediaRepository(applicationContext).init()
        }
        FirebaseApp.initializeApp(this)
    }

}