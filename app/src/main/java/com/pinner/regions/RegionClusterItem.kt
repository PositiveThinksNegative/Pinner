package com.pinner.regions

import androidx.annotation.ColorInt
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class RegionClusterItem(private val feedName: String, private val city: String?, @ColorInt val colorInt: Int, private val position: LatLng) : ClusterItem {

    override fun getSnippet(): String {
        return feedName
    }

    override fun getTitle(): String {
        return city ?: ""
    }

    override fun getPosition(): LatLng {
        return position
    }

}