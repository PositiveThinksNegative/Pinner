package com.pinner.regions

data class RegionsContainer(val feeds: List<Region>)

data class Region(val id: Int, val code: String, val bounds: Bounds, val location: String, val country_code: String)

data class Bounds(val min_lat: Double, val max_lat: Double, val min_lon: Double, val max_lon: Double)