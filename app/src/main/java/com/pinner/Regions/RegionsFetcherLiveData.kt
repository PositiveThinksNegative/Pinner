package com.pinner.Regions

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.ZoneId
import java.util.*


class RegionsFetcherLiveData : LiveData<List<Region>>() {

    private val gson = Gson()
    private val okHttpClient = OkHttpClient()
    private val feedUrl = "https://api.transitapp.com/v3/feeds"

    override fun onActive() {
        super.onActive()

        val request = Request.Builder()
            .url(feedUrl)
            .build()

        AsyncTask.execute {
            val response = okHttpClient.newCall(request).execute()
            response.body()?.string()?.let {
                val regionsContainer = gson.fromJson(it, RegionsContainer::class.java)
                postValue(regionsContainer?.feeds)
            }
        }

    }

}