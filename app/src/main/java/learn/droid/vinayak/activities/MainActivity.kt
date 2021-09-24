package learn.droid.vinayak.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import learn.droid.vinayak.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reportButton = findViewById<Button>(R.id.btnReport)
        reportButton.setOnClickListener {
            startActivity(Intent(this, CaptureDisasterActivity::class.java))
        }

        val viewButton = findViewById<Button>(R.id.btnView)
        viewButton.setOnClickListener {
            startActivity(Intent(this,MapsActivity::class.java))
        }

    }
}