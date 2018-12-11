package com.pinner

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*

class MapViewModelFactory(application: Application) : ViewModelProvider.Factory {

    private val geoCoder: Geocoder = Geocoder(application, Locale.getDefault())

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(geoCoder) as T
    }

}