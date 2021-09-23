package learn.droid.vinayak.services

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource


class LocationService : Service() {

    private var cancellationTokenSource = CancellationTokenSource()

    companion object {
        private val TAG = LocationService::class.simpleName
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    super.onStartCommand(intent, flags, startId)
    getLocationCoordinates()
    return  START_STICKY
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
                Log.d(TAG, "longitude"+location?.longitude.toString()+"latitude"+location?.latitude.toString())
                broadcastLocationCoordinates(location)
            }

        }
    }

    private fun broadcastLocationCoordinates(location:Location) {
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        val intent = Intent("learn.droid.vinayak.location")
        intent.putExtra("longitude",location.longitude.toString())
        intent.putExtra("latitude",location.latitude.toString())
        localBroadcastManager.sendBroadcast(intent)
        stopSelf()
    }

}