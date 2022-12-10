package com.example.kelilinkseller.features.store

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.ADDRESS_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.LATITUDE_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.LONGITUDE_COLUMN
import com.example.kelilinkseller.core.data.helper.Constants.DatabaseColumn.OPERATING_STATUS_COLUMN
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.ui.StoreMenu
import com.example.kelilinkseller.core.ui.StoreMenuAdapter
import com.example.kelilinkseller.databinding.FragmentStoreBinding
import com.example.kelilinkseller.features.store.menu.MenuActivity
import com.example.kelilinkseller.features.store.profile.ProfileActivity
import com.example.kelilinkseller.features.store.stock.StockActivity
import com.example.kelilinkseller.util.custom_view.KelilinkLoadingDialog
import com.google.android.gms.location.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class StoreFragment : Fragment() {

    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!

    private lateinit var loading: KelilinkLoadingDialog

    private val storeViewModel: StoreViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var address: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geocoder = Geocoder(requireContext(), Locale.getDefault())
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading = KelilinkLoadingDialog(requireContext())

        createLocationRequest()
        createLocationCallback()

        setUpView()

        showStoreMenu()

        setOnClickLister()
    }

    private fun setUpView() {
        with(storeViewModel) {
            val storeId = Firebase.auth.currentUser!!.uid
            getStoreById(storeId).observe(viewLifecycleOwner) {
                Log.d(TAG, "nama = ${it.data?.name}")
                when(it) {
                    is Resource.Success -> {
                        if(it.data!!.operating_status) {
                            showButtonOpen(false)
                        } else {
                            showButtonOpen(true)
                        }
                        showLoadingState(false)
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showButtonOpen(state: Boolean) {
        with(binding) {
            sBtnWorkClose.isVisible = !state
            sBtnWorkOpen.isVisible = state
            sTvState.text = if(state) {
                resources.getString(R.string.state_operating_closed)
            } else {
                resources.getString(R.string.state_operating_open)
            }
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.sLoading.root.isVisible = state
    }

    private fun showStoreMenu() {
        val storeMenuAdapter = StoreMenuAdapter()

        storeMenuAdapter.onItemClick = {
            when(it.name) {
                requireContext().resources.getStringArray(R.array.menu_name)[0] -> {
                    startActivity(Intent(requireContext(), ProfileActivity::class.java))
                }
                requireContext().resources.getStringArray(R.array.menu_name)[1] -> {
                    startActivity(Intent(requireContext(), MenuActivity::class.java))
                }
                requireContext().resources.getStringArray(R.array.menu_name)[2] -> {
                    startActivity(Intent(requireContext(), StockActivity::class.java))
                }
            }
        }

        storeMenuAdapter.setItems(listCategories)
        with(binding.sRvStoreMenu) {
            layoutManager = GridLayoutManager(activity, 4)
            adapter = storeMenuAdapter
        }
    }

    private val listCategories: ArrayList<StoreMenu>
        get() {
            val name = resources.getStringArray(R.array.menu_name)
            val icon = resources.obtainTypedArray(R.array.menu_icon)
            val listCategory = ArrayList<StoreMenu>()
            for (i in name.indices) {
                val category = StoreMenu(name[i], icon.getResourceId(i, -1))
                listCategory.add(category)
            }
            icon.recycle()
            return listCategory
        }

    private fun setOnClickLister() {
        with(binding) {
            sBtnWorkClose.setOnClickListener {
                loading.show()
                updateStoreOperatingStatus(
                    false,
                    mutableMapOf(OPERATING_STATUS_COLUMN to false)
                )
            }

            sBtnWorkOpen.setOnClickListener {
                loading.show()
                startLocationUpdates()
                getMyLastLocation()

                storeViewModel.locationState.observe(viewLifecycleOwner) {
                    if (it) {
                        updateStoreOperatingStatus(
                            true,
                            mutableMapOf(
                                ADDRESS_COLUMN to address,
                                LATITUDE_COLUMN to latitude,
                                LONGITUDE_COLUMN to longitude,
                                OPERATING_STATUS_COLUMN to true
                            )
                        )
                    }
                }
            }
        }
    }

    private fun updateStoreOperatingStatus(state: Boolean, data: MutableMap<String, Any>) {
        storeViewModel.updateUserInfo(data).observe(viewLifecycleOwner) {
            updateResponse(it, state)
        }
    }

    private fun updateResponse(data: Resource<Unit>, state: Boolean) {
        when(data) {
            is Resource.Success -> {
                loading.dismiss()
                showButtonOpen(!state)
                stopLocationUpdates()
            }
            is Resource.Error -> {
                loading.dismiss()
                Log.e(TAG, data.message.toString())
            }
            else -> {}
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "StoreFragment"
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude

                    address = geocoder.getFromLocation(latitude, longitude, 1)[0]
                        .getAddressLine(0)

                    storeViewModel.updateLocationState(true)
                } else {
                    loading.dismiss()
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.state_no_location),
                        Toast.LENGTH_SHORT
                    ).show()
                    storeViewModel.updateLocationState(false)
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(requireActivity())
        client.checkLocationSettings(builder.build())
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.d(TAG, "onLocationResult: " + location.latitude + ", " + location.longitude)
                }
            }
        }
    }

    private fun startLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (exception: SecurityException) {
            Log.e(TAG, "Error : " + exception.message)
        }
    }


    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}