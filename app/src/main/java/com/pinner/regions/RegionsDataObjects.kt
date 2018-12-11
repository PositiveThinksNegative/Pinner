package com.pinner.regions

import androidx.annotation.ColorRes
import com.google.android.gms.maps.model.LatLng

data class RegionsContainer(val feeds: List<Region>)

data class Region(val id: Int, val code: String, val timezone: String, val bounds: Bounds)

data class Bounds(val min_lat: Double, val max_lat: Double, val min_lon: Double, val max_lon: Double)

data class RegionUiObject(val feedName: String, val city: String, @ColorRes val colorRes: Int, val position: LatLng)

data class MarkerDetailsUiObject(val feedName: String, val cityName: String)
