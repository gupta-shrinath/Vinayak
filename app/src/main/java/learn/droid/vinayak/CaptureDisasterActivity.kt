package learn.droid.vinayak

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import java.lang.Exception

class CaptureDisasterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_disaster)
        val disasterImageView = findViewById<ImageView>(R.id.disaster_preview_iv)
        val handleCaptureImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                disasterImageView.setImageBitmap(imageBitmap)
            } else {
                Log.i("Camera Launch","Image Capture Fail")
                finish()
            }
        }
        launchCamera(handleCaptureImage)
    }

    private fun launchCamera(captureImage: ActivityResultLauncher<Intent>) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try{
            captureImage.launch(cameraIntent)
        }
        catch (e: Exception) {

        }
    }
}