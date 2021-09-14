package learn.droid.vinayak

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

class CaptureDisasterActivity : AppCompatActivity() {
    private lateinit var layout:ConstraintLayout
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result->
        if(result) {
            Log.d("Location","Location permission received")
        } else{
            Log.d("Location","Location permission denied aborting report")
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

    private fun launchCamera(imageView: ImageView) {
        val handleCaptureImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                imageView.setImageBitmap(imageBitmap)
                getLocation()

            } else {
                Log.i("Camera Launch","Image Capture Fail")
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
                Log.d("Location","Location permission already received")
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                Log.d("Location", "Location permission was denied show rationale")
                Snackbar.make(layout,"Location permission required to resolve disaster",Snackbar.LENGTH_INDEFINITE).setAction("OK") {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }.show()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

    }


}