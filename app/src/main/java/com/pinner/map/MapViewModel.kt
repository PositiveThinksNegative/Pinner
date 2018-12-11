package com.pinner.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.pinner.regions.RegionClusterItem
import com.pinner.regions.RegionsFetcherLiveData
import java.util.*

class MapViewModel(context: Context) : ViewModel() {

    private val onRegionsFetched = RegionsFetcherLiveData(context)
    private val onDisplayCityDetails = MutableLiveData<MarkerDetailsUiObject>()
    private val geoCoder: Geocoder = Geocoder(context, Locale.getDefault())

    fun onRegionsFetched(): RegionsFetcherLiveData {
        return onRegionsFetched
    }

    fun onDisplayCityDetails(): LiveData<MarkerDetailsUiObject> {
        return onDisplayCityDetails
    }

    fun onMarkerClicked(marker: RegionClusterItem) {

        //Prevent empty city name coming from JSON feed
        val title = if (marker.title.isEmpty()) {
            getAddressFromCoordinates(marker.position)?.run {
                locality ?: featureName
            } ?: run {
                ""
            }
        } else {
            marker.title
        }

        val markerDetails = MarkerDetailsUiObject(title, marker.snippet)
        onDisplayCityDetails.postValue(markerDetails)
    }

    private fun getAddressFromCoordinates(latLng: LatLng): Address? {
        geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.let {
            if (it.size > 0) return it[0]
        }
        return null
    }

}