package learn.droid.vinayak

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource


class LocationService : Service() {

    private var cancellationTokenSource = CancellationTokenSource()

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        cancellationTokenSource.cancel()
    }

    private fun getLocationCoordinates() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token).addOnSuccessListener {
                location ->
                Log.d("Location", location?.latitude.toString()+ location?.longitude.toString())
                saveLocationCoordinates(location)
            }

        }
    }

    private fun saveLocationCoordinates(location:Location) {
        val sharedPref = getSharedPreferences("learn.droid.vinayak.CURRENTLOCATION",Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("longitude", location.longitude.toString())
            putString("latitude", location.latitude.toString())
            apply()
        }
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        getLocationCoordinates()
        return  START_STICKY
    }

}