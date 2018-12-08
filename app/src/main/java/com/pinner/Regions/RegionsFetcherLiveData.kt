package com.pinner.Regions

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request


class RegionsFetcherLiveData: LiveData<RegionsContainer>() {

    private val gson = Gson()
    private val okHttpClient = OkHttpClient()

    override fun onActive() {
        super.onActive()

        val request = Request.Builder()
            .url("https://api.transitapp.com/v3/feeds")
            .build()

        AsyncTask.execute {
            val response = okHttpClient.newCall(request).execute()
            response.body()?.string()?.let {
                val feeds = gson.fromJson(it, RegionsContainer::class.java)
                postValue(feeds)
            }
        }

    }

    override fun onInactive() {
        super.onInactive()
    }

}