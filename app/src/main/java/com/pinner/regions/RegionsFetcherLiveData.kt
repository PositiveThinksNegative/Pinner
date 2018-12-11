package com.pinner.regions

import android.content.Context
import android.os.AsyncTask
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.pinner.R
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*


class RegionsFetcherLiveData(private val context: Context) : LiveData<List<RegionClusterItem>>() {

    private val gson = Gson()
    private val okHttpClient = OkHttpClient()
    private val feedUrl = context.resources.getString(R.string.feed_url)

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
                val regionUiObjects = convertRegionsToClusterItems(regionsContainer.feeds)

                postValue(regionUiObjects)
            }
        }
    }

    private fun convertRegionsToClusterItems(regions: List<Region>): List<RegionClusterItem> {
        val regionUiObjects = ArrayList<RegionClusterItem>()

        regions.forEach {
            val latitude = (it.bounds.min_lat + it.bounds.max_lat) / 2
            val longitude = (it.bounds.min_lon + it.bounds.max_lon) / 2
            val regionUiObject = RegionClusterItem(
                it.code,
                it.location,
                getColorForCountry(it.country_code),
                LatLng(latitude, longitude)
            )

            regionUiObjects.add(regionUiObject)
        }

        return regionUiObjects
    }

    private fun getColorForCountry(country: String): Int {
        val canada = context.resources.getString(R.string.canada)
        val usa = context.resources.getString(R.string.usa)
        val uk = context.resources.getString(R.string.uk)
        val germany = context.resources.getString(R.string.germany)
        val france = context.resources.getString(R.string.france)

        return when (country) {
            canada -> ContextCompat.getColor(context, R.color.canada)
            usa -> ContextCompat.getColor(context, R.color.usa)
            uk -> ContextCompat.getColor(context, R.color.uk)
            germany -> ContextCompat.getColor(context, R.color.germany)
            france -> ContextCompat.getColor(context, R.color.france)
            else -> ContextCompat.getColor(context, R.color.other)
        }
    }

}