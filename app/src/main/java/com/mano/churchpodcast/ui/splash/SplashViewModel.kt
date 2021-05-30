package com.mano.churchpodcast.ui.splash

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.mano.churchpodcast.BuildConfig
import com.mano.churchpodcast.R
import com.mano.churchpodcast.db.MediaRepository
import kotlinx.coroutines.launch

class SplashViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private const val SPLASH_TIME_MS = 700L
    }

    private val remoteConfig = remoteConfigSettings {
        minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
            30
        } else {
            1 * 60 * 60
        }
    }

    private val _isRemoteConfigLoaded = MutableLiveData<Boolean>()
    private val _isHandlerFinished = MutableLiveData<Boolean>()
    private val _isSignInAnonymously = MutableLiveData<Boolean>()
    private val _isGooglePlayServiceUpdated = MutableLiveData<Boolean>()
    private val _isRepositoryInitialized = MutableLiveData<Boolean>()

    val launchMainActivity: LiveData<Boolean> = launchMainActivity(
        _isSignInAnonymously,
        _isRemoteConfigLoaded,
        _isGooglePlayServiceUpdated,
        _isHandlerFinished,
        _isRepositoryInitialized
    )
    val isGooglePlayServiceUpdated: LiveData<Boolean>
        get() = _isGooglePlayServiceUpdated

    init {
        firebaseAuthSetup()
        firebaseRemoteConfigSetup()
        googlePlayServicesSetup(application)
        handlerSetup()
        viewModelScope.launch {
            MediaRepository(application).init()
            _isRepositoryInitialized.value = true
        }
    }

    fun googlePlayServiceUpdate() {
        _isGooglePlayServiceUpdated.value = true
    }

    private fun handlerSetup() {
        Handler(Looper.getMainLooper()).postDelayed({
            _isHandlerFinished.value = true
        }, SPLASH_TIME_MS)
    }

    private fun firebaseRemoteConfigSetup() {
        Firebase.remoteConfig.apply {
            setDefaultsAsync(R.xml.remote_config_defaults)
            setConfigSettingsAsync(remoteConfig)
            fetch().onSuccessTask {
                activate()
            }.addOnCompleteListener {
                _isRemoteConfigLoaded.value = true
            }
        }
    }

    private fun firebaseAuthSetup() {
        Firebase.auth.apply {
            if (currentUser == null) {
                signInAnonymously().addOnCompleteListener {
                    _isSignInAnonymously.value = true
                }
            } else _isSignInAnonymously.value = true
        }
    }

    private fun googlePlayServicesSetup(context: Context) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context)
        _isGooglePlayServiceUpdated.value = connectionStatusCode == ConnectionResult.SUCCESS
    }

    private fun launchMainActivity(vararg liveDataVarargs: LiveData<Boolean>)
    : LiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            liveDataVarargs.forEach { liveData ->
                addSource(liveData) {
                    value = liveDataVarargs.all { it.value ?: false }
                }
            }
        }

}