package learn.droid.vinayak.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import learn.droid.vinayak.utils.ReverseGeoCoder

class LocationBroadcastReceiver: BroadcastReceiver() {
    companion object {
        private val TAG = LocationBroadcastReceiver::class.simpleName
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        var longitude = 0.0
        var latitude = 0.0
        if (p1 != null) {
            p1.getStringExtra("longitude")?.let {
                Log.d(TAG, "Current Longitude $it")
                longitude = it.toDouble()
            }
            p1.getStringExtra("latitude")?.let {
                Log.d(TAG, "Current Latitude $it")
                latitude = it.toDouble()
            }
        }
        Log.d(TAG, "onReceive: "+ p0?.let { ReverseGeoCoder.getLocation(it,longitude,latitude) })
    }


}