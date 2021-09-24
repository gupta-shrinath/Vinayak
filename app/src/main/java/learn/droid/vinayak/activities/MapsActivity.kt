package learn.droid.vinayak.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import learn.droid.vinayak.R
import learn.droid.vinayak.database.LocationDatabase
import learn.droid.vinayak.databinding.ActivityMapsBinding
import learn.droid.vinayak.utils.ReverseGeoCoder
import kotlin.coroutines.CoroutineContext

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,CoroutineScope {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val locationDao = LocationDatabase.getInstance(this).locationDao()
        launch {
            val locations = locationDao.getLocations()
            locations.onEach {
                val location =
                    it.address?.let { it1 -> ReverseGeoCoder.getLocationCoordinates(applicationContext, it1) }
                val disaster = it.disaster
                mMap.addMarker(MarkerOptions().position(location).title(disaster))
            }
        }
    }
}