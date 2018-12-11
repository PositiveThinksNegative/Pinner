package com.pinner

import android.graphics.*
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main_map.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*


class MainMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var canada: List<String>
    private lateinit var usa: List<String>
    private lateinit var uk: List<String>
    private lateinit var germany: List<String>
    private lateinit var france: List<String>
    private lateinit var viewModel: MainMapViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_map)

        canada = resources.getStringArray(R.array.canada).toList()
        usa = resources.getStringArray(R.array.usa).toList()
        uk = resources.getStringArray(R.array.uk).toList()
        germany = resources.getStringArray(R.array.germany).toList()
        france = resources.getStringArray(R.array.france).toList()

        viewModel = ViewModelProviders.of(this, MapViewModelFactory(application)).get(MainMapViewModel::class.java)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        googleMap.setOnMarkerClickListener { marker ->
            viewModel.onMarkerClicked(marker)
            false
        }

        googleMap.setOnMapClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.onRegionsFetched().observe(this, Observer {
            it?.let { feeds ->
                feeds.forEach { regionObject ->
                    val pinColor = getColorFromTimezone(regionObject.city)
                    val markerOpt = MarkerOptions()
                        .icon(ColorUtil.createColoredBitmap(resources, pinColor))
                        .position(regionObject.position)

                    val marker = googleMap.addMarker(markerOpt)
                    marker.tag = regionObject
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

    private fun getColorFromTimezone(timeZone: String): Int {
        return when (timeZone) {
            in canada -> ContextCompat.getColor(this, R.color.canada)
            in usa -> ContextCompat.getColor(this, R.color.usa)
            in uk -> ContextCompat.getColor(this, R.color.uk)
            in germany -> ContextCompat.getColor(this, R.color.germany)
            in france -> ContextCompat.getColor(this, R.color.france)
            else -> ContextCompat.getColor(this, R.color.other)
        }
    }

}
