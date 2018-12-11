package com.pinner.map

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.ClusterManager
import com.pinner.R
import com.pinner.regions.RegionClusterItem
import kotlinx.android.synthetic.main.activity_main_map.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var viewModel: MapViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_map)

        viewModel = ViewModelProviders.of(this, MapViewModelFactory(application)).get(MapViewModel::class.java)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val clusterManager = ClusterManager<RegionClusterItem>(this, googleMap)
        googleMap.setOnMarkerClickListener(clusterManager)
        googleMap.setOnCameraIdleListener(clusterManager)

        val mapClusterRenderer = MapClusterRenderer(this, googleMap, clusterManager)
        mapClusterRenderer.minClusterSize = MIN_CLUSTER_SIZE
        clusterManager.renderer = mapClusterRenderer

        clusterManager.setOnClusterItemClickListener { marker ->
            viewModel.onMarkerClicked(marker)

            val cameraUpdate = CameraUpdateFactory.newLatLng(marker.position)
            googleMap.animateCamera(cameraUpdate, CAMERA_SPEED, null)
            true
        }

        clusterManager.setOnClusterClickListener {
            val zoom = Math.floor(googleMap.cameraPosition.zoom.toDouble() + 1).toFloat()
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(it.position, zoom)

            googleMap.animateCamera(cameraUpdate, CAMERA_SPEED, null)
            true
        }

        googleMap.setOnMapClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.onRegionsFetched().observe(this, Observer {
            it?.let { feeds ->
                clusterManager.clearItems()
                feeds.forEach { regionObject ->
                    clusterManager.addItem(regionObject)
                    clusterManager.cluster()
                }
            }
        })

        viewModel.onDisplayCityDetails().observe(this, Observer {
            it?.let { markerDetail ->
                bottomSheet.cityName.text = markerDetail.cityName
                bottomSheet.feedName.text = markerDetail.feedName
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        })
    }

    companion object {

        const val CAMERA_SPEED = 300
        const val MIN_CLUSTER_SIZE = 9

    }

}
