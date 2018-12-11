package com.pinner

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import com.pinner.map.MapViewModel
import com.pinner.regions.RegionClusterItem

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class MapInteractionInstrumentedTests {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mapViewModel = MapViewModel(InstrumentationRegistry.getTargetContext())
    private val marker1 = RegionClusterItem("feed1", "city1", 2, LatLng(0.0, 0.0))
    private val marker2 = RegionClusterItem("feed2", "", 2, LatLng(45.5, -73.5))
    private val marker3 = RegionClusterItem("feed3", "", 2, LatLng(0.0, 0.0))

    @Test
    fun testFeedAndCityIsSame() {
        mapViewModel.onDisplayCityDetails().observeForever {
            assertTrue(it.feedName == marker1.snippet)
            assertTrue(it.cityName == marker1.title)
        }
        mapViewModel.onMarkerClicked(marker1)
    }

    @Test
    fun testEmptyCity() {
        mapViewModel.onDisplayCityDetails().observeForever {
            assertTrue(it.feedName == marker2.snippet)
            assertTrue(it.cityName == "Saint-Lambert")
        }
        mapViewModel.onMarkerClicked(marker2)
    }

    @Test
    fun testEmptyCityWithWrongCoordinates() {
        mapViewModel.onDisplayCityDetails().observeForever {
            assertTrue(it.feedName == marker3.snippet)
            assertTrue(it.cityName == "")
        }
        mapViewModel.onMarkerClicked(marker3)
    }

}
