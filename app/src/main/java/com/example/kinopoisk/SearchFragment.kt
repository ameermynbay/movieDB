package com.example.kinopoisk

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.search_fragment.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import kotlin.math.log


class SearchFragment : Fragment(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var googleMap: GoogleMap
    var longitude = 0.0
    var latitude = 1.0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

        getLastKnownLocation()
        Log.d("searchFragment", "Coordinates $longitude, $latitude")
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(mapView != null){
            mapView.onCreate(null)
            mapView.onResume()
            mapView.getMapAsync(this)
        }
    }



    override fun onMapReady(p0: GoogleMap?) {
        MapsInitializer.initialize(context)
        googleMap = p0!!
        val almaty = LatLng(43.2, 76.8)
        val lumiera = LatLng(43.2620175, 76.9412188)
        val megapark = LatLng(43.264051, 76.928773)
        val megaAlmaty = LatLng(43.203302, 76.892672)
        val arman = LatLng(43.242230, 76.957546)
        val cinemax = LatLng(43.233342, 76.955667)
        val silkway = LatLng(43.254671, 76.937278)
        val cesar = LatLng(43.260940, 76.946242)
        val forum = LatLng(43.234063, 76.935823)
        val moskva = LatLng(43.226841, 76.864126)
        val esentai = LatLng(43.218272, 76.927565)
        googleMap.addMarker(MarkerOptions().position(lumiera).title("Lumiera Cinema"))
        googleMap.addMarker(MarkerOptions().position(megapark).title("Chaplin Cinemas"))
        googleMap.addMarker(MarkerOptions().position(megaAlmaty).title("Chaplin Cinemas"))
        googleMap.addMarker(MarkerOptions().position(arman).title("Arman 3D"))
        googleMap.addMarker(MarkerOptions().position(cinemax).title("Cinemax Dostyk Multiplex"))
        googleMap.addMarker(MarkerOptions().position(silkway).title("Silkway Cinema"))
        googleMap.addMarker(MarkerOptions().position(cesar).title("Cesar Cinema"))
        googleMap.addMarker(MarkerOptions().position(forum).title("Kinopark 16 Forum"))
        googleMap.addMarker(MarkerOptions().position(moskva).title("Kinopark 8 Moskva"))
        googleMap.addMarker(MarkerOptions().position(esentai).title("Kinopark 11 Esentai"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(almaty))
        val url = getDirectionsUrl(esentai, megapark)
        GetDirection(url).execute()
    }

    fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location->
                    if (location != null) {
                        latitude =  location?.latitude
                        longitude = location?.longitude
                    }

                }

    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        val sensor = "sensor=false"
        val mode = "mode=driving"
        val parameters = "$str_origin&$str_dest&$sensor&$mode"
        val output = "json"
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=AIzaSyBW_85euYYXrFvw0QA4YgfAFUDYjdXQ5f8"
    }

    inner class GetDirection(val url: String): AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg p0: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body().toString()
            val result = ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data, GoogleMapDTO::class.java)
                val path = ArrayList<LatLng>()
                for(i in 0..(respObj.routes[0].legs[0].steps.size-1)){
                    val startLatLng = LatLng(respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble()
                    , respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble())
                    path.add(startLatLng)
                    val endLatLng = LatLng(respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble()
                            , respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble() )
                    path.add(endLatLng)
                }
                result.add(path)
            } catch (e:Exception){
                e.printStackTrace()
                Log.d("searchFragment", "not succesfull")
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.BLUE)
                lineoption.geodesic(true)
            }
            googleMap.addPolyline(lineoption)
        }

    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

}
