package learn.droid.vinayak.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class LocationBroadcastReceiver: BroadcastReceiver() {
    companion object {
        private val TAG = LocationBroadcastReceiver::class.simpleName
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1 != null) {
            p1.getStringExtra("longitude")?.let { Log.d(TAG, "Current Longitude $it") }
            p1.getStringExtra("latitude")?.let { Log.d(TAG, "Current Latitude $it") }
        }
    }


}