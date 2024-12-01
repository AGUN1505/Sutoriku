package com.dicoding.sutoriku.ui.addSutori

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.*
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dicoding.sutoriku.R
import com.dicoding.sutoriku.databinding.FragmentDashboardBinding
import com.dicoding.sutoriku.ui.camera.CameraActivity
import com.dicoding.sutoriku.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class addSutoriFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val addStoryViewModel: addSutoriViewModel by viewModels {
        addSutoriViewModelFactory.getInstance(requireContext())
    }

    private val appExecutors: AppExecutors by lazy { AppExecutors() }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            addStoryViewModel.setSelectImageUri(uri)
            showImage()
        } else {
            Log.d(getString(R.string.photo_picker_title), getString(R.string.no_photo_selected))
        }
    }

    private val launchCameraActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.getStringExtra("image_uri")?.let { Uri.parse(it) }
            uri?.let {
                addStoryViewModel.setSelectImageUri(it)
                showImage()
            }
        } else {
            Log.d(getString(R.string.camera_title), getString(R.string.failed_take_photo))
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(getString(R.string.photo_picker_title), getString(R.string.acces_granted))
            showSnackBar(getString(R.string.acces_granted))
        } else {
            Log.d(getString(R.string.photo_picker_title), getString(R.string.acces_denied))
            showSnackBar(getString(R.string.acces_denied))
        }
    }

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permission: Boolean ->
        if (permission) {
            getLocation()
        } else {
            showSnackBar(getString(R.string.location_permission_denied))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        buttonClickListener()
        setupObserver()

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamerax.setOnClickListener { startCameraX() }

        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getLocation()
            } else {
                addStoryViewModel.setLocation(null, null)
            }
        }

        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (addStoryViewModel.uploadSuccess.value == true) {
                        toHomeFragment()
                    } else {
                        findNavController().popBackStack()
                    }
                }
            }
        )
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                addStoryViewModel.setLocation(location.latitude, location.longitude)
            } else {
                showSnackBar(getString(R.string.location_not_found))
            }
        }
    }

    private fun buttonClickListener() {
        binding.buttonAdd.setOnClickListener {
            val currentImageUri = addStoryViewModel.selectUriImage.value
            if (currentImageUri != null) {
                showLoading(true)

                appExecutors.diskIO.execute {
                    try {
                        val imageFile = uriToFile(currentImageUri, requireContext())
                        val reduceImageFile = imageFile.reduceFileImage()
                        val description = binding.edAddDescription.text.toString()
                        val requestBody = description.toRequestBody("text/plain".toMediaType())
                        val requestImageFile = reduceImageFile.asRequestBody("image/jpeg".toMediaType())
                        val multiPart = MultipartBody.Part.createFormData(
                            "photo", reduceImageFile.name, requestImageFile
                        )
                        val location = addStoryViewModel.location.value
                        val lat = location?.first?.toString()?.toRequestBody("text/plain".toMediaType())
                        val lon = location?.second?.toString()?.toRequestBody("text/plain".toMediaType())
                        appExecutors.mainThread.execute {
                            addStoryViewModel.uploadImage(multiPart, requestBody, lat, lon)
                        }
                    } catch (e: Exception) {
                        appExecutors.mainThread.execute {
                            showSnackBar(getString(R.string.add_story_failed_dialog, e.message))
                            showLoading(false)
                        }
                    }
                }
            } else {
                showSnackBar(getString(R.string.pick_or_capture_photo_dialog))
            }
        }
    }

    private fun setupObserver() {
        addStoryViewModel.selectUriImage.observe(viewLifecycleOwner) {
            showImage()
        }

        addStoryViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        addStoryViewModel.snackBar.observe(viewLifecycleOwner) {
            it?.let {
                showSnackBar(it)
            }
        }

        addStoryViewModel.uploadSuccess.observe(viewLifecycleOwner) {
            if (it) {
                resetFragment()
                toHomeFragment()
            }
        }
    }

    private fun toHomeFragment() {
        findNavController().navigate(
            R.id.action_navigation_dashboard_to_navigation_home,
            null,
            NavOptions.Builder().setPopUpTo(R.id.navigation_dashboard, true).build()
        )
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun startGallery() {
        launchGallery.launch("image/*")
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launchCameraActivity.launch(intent)
    }

    private fun showImage() {
        addStoryViewModel.selectUriImage.value?.let {
            binding.pvImage.setImageURI(it)
        }
    }

    private fun resetFragment() {
        addStoryViewModel.setSelectImageUri(null)
        binding.edAddDescription.text?.clear()
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}