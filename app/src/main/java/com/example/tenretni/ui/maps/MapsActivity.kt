package com.example.tenretni.ui.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.navArgs
import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import com.example.tenretni.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.tenretni.databinding.ActivityMapsBinding
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val args: MapsActivityArgs by navArgs()

    private val locationPermissionRequest by lazy {
        permissionsBuilder(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationPermissionRequest.send()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val permissionStatus = locationPermissionRequest.checkStatus()
        if(permissionStatus.allGranted()) {
            @SuppressLint("MissingPermission")
            mMap.isMyLocationEnabled = true

        }
        val custumerPosition = MarkerOptions().position(args.latLong)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            .title(args.name)

        mMap.addMarker(custumerPosition)
        val cameraPosition = CameraPosition.Builder().target(args.latLong).zoom(15f).build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }
}