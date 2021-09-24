package learn.droid.vinayak.activities

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import learn.droid.vinayak.R
import learn.droid.vinayak.database.LocationDatabase
import learn.droid.vinayak.database.entities.Location
import learn.droid.vinayak.receivers.LocationBroadcastReceiver
import learn.droid.vinayak.services.LocationService
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class CaptureDisasterActivity : AppCompatActivity(),CoroutineScope {

    private lateinit var layout:ConstraintLayout
    private lateinit var etLocation: EditText
    private lateinit var etDisaster:EditText
    private lateinit var btSubmit:Button

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

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_disaster)
        layout = findViewById(R.id.captureDisaster_cl)
        val disasterImageView = findViewById<ImageView>(R.id.disaster_preview_iv)
        etLocation = findViewById(R.id.etLocation)
        etDisaster = findViewById(R.id.etDisaster)
        btSubmit = findViewById(R.id.btSubmit)
        btSubmit.setOnClickListener {
            val locationDao = LocationDatabase.getInstance(this).locationDao()
            val disaster = etDisaster.text.toString()
            val address =  etLocation.text.toString()
            val location = Location(disaster = disaster,address = address)
            launch{
                val result:Long = locationDao.insertLocation(location)
                if(!Boolean.equals(result.compareTo(-1))){
                    Toast.makeText(applicationContext,"Disaster Reported",Toast.LENGTH_LONG).show()
                }else {
                    Toast.makeText(applicationContext,"Disaster Report Failed",Toast.LENGTH_LONG).show()
                }
                finish()
            }
        }
        launchCamera(disasterImageView)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locationBroadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        locationBroadcastReceiver.setLocationEditText(etLocation)
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