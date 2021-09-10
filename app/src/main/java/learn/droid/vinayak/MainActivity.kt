package learn.droid.vinayak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reportButton = findViewById<Button>(R.id.report_btn)
        reportButton.setOnClickListener {
            startActivity(Intent(this,CaptureDisasterActivity::class.java))
        }

    }
}