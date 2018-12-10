package com.pinner

import android.graphics.*
import android.os.Bundle
import android.widget.FrameLayout
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


class MainMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var viewModel: MainMapViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_map)

        viewModel = ViewModelProviders.of(this, MapViewModelFactory(application)).get(MainMapViewModel::class.java)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        googleMap.setOnMarkerClickListener { marker ->
            viewModel.onMarkerClicked(marker)
            false
        }

        viewModel.onRegionsFetched().observe(this, Observer {
            it?.let { feeds ->
                feeds.forEach { regionObject ->

                    val paint = Paint()
                    paint.colorFilter = PorterDuffColorFilter(
                        ContextCompat.getColor(this, regionObject.colorRes),
                        PorterDuff.Mode.SRC_IN
                    )

                    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.pin)
                    val bitmapResult = Bitmap.createBitmap(bitmap.width / 2, bitmap.height / 2, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmapResult)
                    val matrix = Matrix()
                    matrix.setScale(0.5f, 0.5f)

                    canvas.drawBitmap(bitmap, matrix, paint)

                    val pin = BitmapDescriptorFactory.fromBitmap(bitmapResult)

                    val markerOpt = MarkerOptions()
                        .icon(pin)
                        .position(regionObject.position)

                    val marker = googleMap.addMarker(markerOpt)
                    marker.tag = regionObject
                }
            }
        })

        viewModel.onDisplayCityDetails().observe(this, Observer {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        })
    }

}
