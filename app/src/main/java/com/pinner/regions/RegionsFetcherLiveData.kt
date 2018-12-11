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
    private val canada = context.resources.getStringArray(R.array.canada).toList()
    private val usa = context.resources.getStringArray(R.array.usa).toList()
    private val uk = context.resources.getStringArray(R.array.uk).toList()
    private val germany = context.resources.getStringArray(R.array.germany).toList()
    private val france = context.resources.getStringArray(R.array.france).toList()

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
                it.timezone,
                getColorFromTimezone(it.timezone),
                LatLng(latitude, longitude)
            )

            regionUiObjects.add(regionUiObject)
        }

        return regionUiObjects
    }

    private fun getColorFromTimezone(timeZone: String): Int {
        return when (timeZone) {
            in canada -> ContextCompat.getColor(context, R.color.canada)
            in usa -> ContextCompat.getColor(context, R.color.usa)
            in uk -> ContextCompat.getColor(context, R.color.uk)
            in germany -> ContextCompat.getColor(context, R.color.germany)
            in france -> ContextCompat.getColor(context, R.color.france)
            else -> ContextCompat.getColor(context, R.color.other)
        }
    }

}