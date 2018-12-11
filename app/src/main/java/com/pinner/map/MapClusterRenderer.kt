package com.pinner.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.pinner.ColorUtil
import com.pinner.regions.RegionClusterItem


class MapClusterRenderer(private val context: Context, map: GoogleMap, clusterManager: ClusterManager<RegionClusterItem>) : DefaultClusterRenderer<RegionClusterItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: RegionClusterItem, markerOptions: MarkerOptions) {
        val coloredPin = ColorUtil.createColoredPin(context.resources, item.colorInt)
        markerOptions.icon(coloredPin)
    }

}