package com.pinner.regions

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.pinner.R
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*


class RegionsFetcherLiveData : LiveData<List<RegionUiObject>>() {

    private val gson = Gson()
    private val okHttpClient = OkHttpClient()
    private val feedUrl = "https://api.transitapp.com/v3/feeds"

    override fun onActive() {
        super.onActive()
        fetchRegions()
    }

    private fun fetchRegions() {
        val request = Request.Builder()
            .url(feedUrl)
            .build()

        AsyncTask.execute {
            val response = okHttpClient.newCall(request).execute()
            response.body()?.string()?.let {
                val regionsContainer = gson.fromJson(it, RegionsContainer::class.java)
                val regionUiObjects = convertRegionsToUiObjects(regionsContainer.feeds)

                postValue(regionUiObjects)
            }
        }
    }

    private fun convertRegionsToUiObjects(regions: List<Region>): List<RegionUiObject> {
        val regionUiObjects = ArrayList<RegionUiObject>()

        regions.forEach {
            val latitude = (it.bounds.min_lat + it.bounds.max_lat) / 2
            val longitude = (it.bounds.min_lon + it.bounds.max_lon) / 2
            val regionUiObject = RegionUiObject(it.code, it.timezone, R.color.canada, LatLng(latitude, longitude))

            regionUiObjects.add(regionUiObject)
        }

        return regionUiObjects
    }

}