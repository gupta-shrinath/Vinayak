package learn.droid.vinayak.utils

import android.content.Context
import android.location.Geocoder
import android.util.Log

class ReverseGeoCoder {
    companion object {
        private const val TAG = "ReverseGeoCoder"
        fun getLocation(context: Context, longitude:Double, latitude:Double): String {
            val geoCoder = Geocoder(context)
            val address = geoCoder.getFromLocation(latitude,longitude, 1)[0].getAddressLine(0)
            Log.d(TAG, "getLocation: $address")
            return address
        }
    }
}