package com.pinner

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.pinner.Regions.MarkerDetailsUiObject
import com.pinner.Regions.RegionUiObject
import com.pinner.Regions.RegionsFetcherLiveData

class MainMapViewModel(private val geocoder: Geocoder) : ViewModel() {

    private val fetcherLiveData = RegionsFetcherLiveData()
    private val onDisplayCityDetails = MutableLiveData<MarkerDetailsUiObject>()

    fun onRegionsFetched(): RegionsFetcherLiveData {
        return fetcherLiveData
    }

    fun onDisplayCityDetails(): LiveData<MarkerDetailsUiObject> {
        return onDisplayCityDetails
    }

    fun onMarkerClicked(marker: Marker) {
        if (marker.tag is RegionUiObject) {
            val regionUiObject = marker.tag as RegionUiObject

            getAddressFromCoordinates(regionUiObject.position)?.let {
                val markerDetails = MarkerDetailsUiObject(regionUiObject.feedName, it.locality ?: it.featureName)
                onDisplayCityDetails.postValue(markerDetails)
            } ?: let {
                val markerDetails = MarkerDetailsUiObject(regionUiObject.feedName, regionUiObject.city)
                onDisplayCityDetails.postValue(markerDetails)
            }
        }
    }

    private fun getAddressFromCoordinates(latLng: LatLng): Address? {
        geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.let {
            if (it.size > 0) return it[0]
        }
        return null
    }

}