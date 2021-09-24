package learn.droid.vinayak.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import learn.droid.vinayak.database.LocationDatabase
import learn.droid.vinayak.database.entities.Location
import learn.droid.vinayak.utils.ReverseGeoCoder
import kotlin.coroutines.CoroutineContext

class LocationBroadcastReceiver() : BroadcastReceiver(), CoroutineScope {

    private lateinit var etLocation:EditText
    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object {
        private val TAG = LocationBroadcastReceiver::class.simpleName
    }

     fun setLocationEditText(editText: EditText) {
        etLocation = editText
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
        val address = p0?.let { ReverseGeoCoder.getLocation(it,longitude,latitude) }
        Log.d(TAG, "onReceive: $address")
        etLocation.setText(address)
        val locationDao = p0?.let { LocationDatabase.getInstance(it).locationDao()}
        val location = Location(longitude = longitude,latitude = latitude)
        launch{
            locationDao?.insertLocation(location)
        }
    }


}