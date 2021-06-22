package com.example.mapmarkers

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapdemo.PermissionUtils
import com.example.mapmarkers.room.AppDatabase
import com.example.mapmarkers.room.MarkerEntity
import com.example.mapmarkers.room.MarkerRepository
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMapClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: MapsActivityViewModel
    private lateinit var map: FragmentContainerView
    private lateinit var rv: RecyclerView
    private lateinit var adapter: MarkersListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val markerRepository = MarkerRepository(AppDatabase.getInstance(this).markerDao())
        val factory = MapsActivityViewModelFactory(markerRepository)
        viewModel = ViewModelProvider(this, factory).get(MapsActivityViewModel::class.java)

        initViews()

        // Initialised View Model
        viewModel.allMarkers.observe(this, { markers ->
            markers?.let {
                adapter.submitList(it)
                showMarkers(it)
            }
        })
    }

    private fun initViews() {
        map = findViewById(R.id.map)
        rv = findViewById(R.id.rv_markers)

        findViewById<FloatingActionButton>(R.id.fab_markers).setOnClickListener {
            if (map.visibility == View.VISIBLE) {
                map.visibility = View.GONE
                rv.visibility = View.VISIBLE
            } else {
                map.visibility = View.VISIBLE
                rv.visibility = View.GONE
            }
        }

        adapter = MarkersListAdapter(
            {mark -> viewModel.update(mark)},
            {mark -> viewModel.delete(mark)}
        )
        rv.setHasFixedSize(true)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        enableMyLocation()
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.setMyLocationEnabled(true)
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(
                this, LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (PermissionUtils.isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else
        {
            // Permission was denied. Display an error message
            Toast.makeText(this, R.string.permission_required, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapClick(p0: LatLng) {
        mMap.addMarker(MarkerOptions().position(p0).title("New Marker"))
        viewModel.insert(MarkerEntity(null, p0.latitude, p0.longitude, "New Marker", null))
    }

    private fun showMarkers(markers: List<MarkerEntity>) {
        mMap.clear()
        for (mark in markers) {
            mMap.addMarker(MarkerOptions().position(LatLng(mark.latitude, mark.longitude)).title(mark.name))
        }
    }
}