package ipvc.estg.pm_trab

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ipvc.estg.pm_trab.api.EndPoints
import ipvc.estg.pm_trab.api.ServiceBuilder
import ipvc.estg.pm_trab.api.Ticket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private var LOCATION_PERMISSION_REQUEST_CODE=1
    lateinit var longitude:String
    lateinit var latitude:String

    override fun onCreate(savedInstanceState: Bundle?) {
        longitude = ""
        latitude = ""

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getMarkers()
        var position: LatLng

        call.enqueue(object : Callback<List<Ticket>> {
            override fun onResponse(call: Call<List<Ticket>>, response: Response<List<Ticket>>) {
                if (response.isSuccessful) {
                    val c = response.body()!!

                    for (note in c) {
                        position = LatLng(note.lat.toDouble(), note.lon.toDouble())
                        var marker = mMap.addMarker(MarkerOptions().position(position))
                        marker.tag = note.id
                    }
                }
            }

            override fun onFailure(call: Call<List<Ticket>>, t: Throwable) {
            }
        })
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
        setUpMap()
        mMap.setOnMarkerClickListener( object: GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(p0: Marker): Boolean {

                val intent = Intent(this@MapActivity, ticket::class.java)
                setUpMap()
                intent.putExtra(ticket.EXTRA_LAT, latitude)
                intent.putExtra(ticket.EXTRA_LON, longitude)
                intent.putExtra(ticket.EXTRA_MSG, p0.tag.toString())
                startActivity(intent)
                return false
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_map, menu)
        return true
    }
    fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }else {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()


                }
            }}}
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_marker_btn -> {
                setUpMap()
                val intent = Intent(this, addmarker::class.java)
                intent.putExtra(addmarker.EXTRA_LAT, latitude)
                intent.putExtra(addmarker.EXTRA_LON, longitude)
                startActivity(intent)
                true
            }
            R.id.logout_btn -> {
                val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putBoolean(getString(R.string.automatic_login_check), false)
                    putString(getString(R.string.automatic_login_username), null)
                    commit()
                }

                val intent = Intent(this@MapActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
