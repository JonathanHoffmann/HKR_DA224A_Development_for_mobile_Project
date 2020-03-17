package com.blue_red.bensinpriser.detail

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blue_red.bensinpriser.R
import com.blue_red.bensinpriser.model.FuelStation
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng

import com.google.android.gms.maps.model.MarkerOptions



class DetailViewActivity : AppCompatActivity(), OnMapReadyCallback {

    private var fuelStation: FuelStation? = null

    private lateinit var detailMapView: MapView

    private lateinit var imageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var subtitleTextView: TextView

    private lateinit var bensin98TextView: TextView
    private lateinit var bensin95TextView: TextView
    private lateinit var dieselTextView: TextView
    private lateinit var ethanol85TextView: TextView

    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_view)
        setupUI()
        loadMaps(savedInstanceState)
        setupComponents()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        detailMapView.onSaveInstanceState(mapViewBundle)
    }


    private fun setupComponents() {
        imageView = findViewById(R.id.logoImageView)
        Glide.with(this).load(fuelStation?.imageUrl).into(imageView)
    }

    private fun loadMaps(savedInstanceState: Bundle?) {
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        detailMapView = findViewById<View>(R.id.detailMapView) as MapView
        detailMapView.onCreate(mapViewBundle)

        detailMapView.getMapAsync(this)
    }

    private fun setupUI() {
        val intent = this.intent
        val bundle = intent.extras
        fuelStation = bundle?.getSerializable("Station") as FuelStation?

        titleTextView = findViewById(R.id.TVtitle)
        titleTextView.text = fuelStation?.title

        subtitleTextView = findViewById(R.id.companyName)
        subtitleTextView.text = fuelStation?.getmCompanyName()

        bensin95TextView = findViewById(R.id.textView11)
        bensin95TextView.text = "Bensin95 price: " + formatForNull(fuelStation?.getmBensin95())

        bensin98TextView = findViewById(R.id.textView18)
        bensin98TextView.text = "Bensin98 price: " + formatForNull(fuelStation?.getmBensin98())

        dieselTextView = findViewById(R.id.textView12)
        dieselTextView.text = "Diesel price: " + fuelStation?.getmDiesel() + "SEK"

        ethanol85TextView = findViewById(R.id.textView13)
        ethanol85TextView.text = "Ethanol price: " + fuelStation?.getmEthanol85() + "SEK"
    }

    private fun formatForNull(price: Double?): String {
        if (price != null) {
            if (price == -1.0) {
                return "not available"
            }
            return price.toString() + " SEK"
        }
        return "not available"
    }

    override fun onMapReady(p0: GoogleMap?) {

        // if lat/lang not available, return
        val (getmLatitude, getmLongitutde) =
            guardLet(fuelStation?.getmLatitude(),fuelStation?.getmLongitude()) { return }

        val latLang = LatLng(getmLatitude, getmLongitutde)
        p0?.addMarker(MarkerOptions().position(latLang).title(fuelStation?.title))
        val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLng(latLang)
        p0?.animateCamera(cameraUpdate)
        p0?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, 12.0f))
    }

    override fun onResume() {
        super.onResume()
        detailMapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        detailMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        detailMapView.onStop()
    }

    override fun onPause() {
        detailMapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        detailMapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        detailMapView.onLowMemory()
    }

    private inline fun <T: Any> guardLet(vararg elements: T?, closure: () -> Nothing): List<T> {
        return if (elements.all { it != null }) {
            elements.filterNotNull()
        } else {
            closure()
        }
    }

//    private inline fun <T: Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
//        if (elements.all { it != null }) {
//            closure(elements.filterNotNull())
//        }
//    }
}