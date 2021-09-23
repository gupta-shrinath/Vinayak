package learn.droid.vinayak

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import learn.droid.vinayak.receivers.LocationBroadcastReceiver
import learn.droid.vinayak.services.LocationService
import java.lang.Exception

class CaptureDisasterActivity : AppCompatActivity() {

    private lateinit var layout:ConstraintLayout

    private val locationBroadcastReceiver = LocationBroadcastReceiver()

    companion object {
        private val TAG = CaptureDisasterActivity::class.simpleName
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result->
        if(result) {
            Log.d(TAG,"Location permission received")
            startService(Intent(this, LocationService::class.java))
        } else{
            Log.d(TAG,"Location permission denied aborting report")
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_disaster)
        val disasterImageView = findViewById<ImageView>(R.id.disaster_preview_iv)
        layout = findViewById(R.id.captureDisaster_cl)
        launchCamera(disasterImageView)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationBroadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("learn.droid.vinayak.location")
        LocalBroadcastManager.getInstance(this).registerReceiver(locationBroadcastReceiver,intentFilter)
    }

    private fun launchCamera(imageView: ImageView) {
        val handleCaptureImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(imageBitmap)
                getLocation()

            } else {
                Log.i(TAG,"Image Capture Fail")
                finish()
            }
        }
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try{
            handleCaptureImage.launch(cameraIntent)
        }
        catch (e: Exception) {
            TODO("Catch the exception")
        }
    }

    private fun getLocation() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG,"Location permission already received")
                startService(Intent(this, LocationService::class.java))
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Log.d(TAG, "Location permission was denied show rationale")
                Snackbar.make(layout,"Location permission required to resolve disaster",Snackbar.LENGTH_INDEFINITE).setAction("OK") {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }.show()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

    }

}