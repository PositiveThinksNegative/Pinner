package com.pinner.map

import android.app.Application
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.pinner.regions.MarkerDetailsUiObject
import com.pinner.regions.RegionClusterItem
import com.pinner.regions.RegionsFetcherLiveData
import java.util.*

class MapViewModel(application: Application) : ViewModel() {

    private val fetcherLiveData = RegionsFetcherLiveData(application.applicationContext)
    private val onDisplayCityDetails = MutableLiveData<MarkerDetailsUiObject>()
    private val geoCoder: Geocoder = Geocoder(application, Locale.getDefault())

    fun onRegionsFetched(): RegionsFetcherLiveData {
        return fetcherLiveData
    }

    fun onDisplayCityDetails(): LiveData<MarkerDetailsUiObject> {
        return onDisplayCityDetails
    }

    fun onMarkerClicked(marker: RegionClusterItem) {
        getAddressFromCoordinates(marker.position)?.let {
            val markerDetails = MarkerDetailsUiObject(marker.snippet, it.locality ?: it.featureName)
            onDisplayCityDetails.postValue(markerDetails)
        } ?: let {
            val markerDetails = MarkerDetailsUiObject(marker.snippet, marker.title)
            onDisplayCityDetails.postValue(markerDetails)
        }
    }

    private fun getAddressFromCoordinates(latLng: LatLng): Address? {
        geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.let {
            if (it.size > 0) return it[0]
        }
        return null
    }

}