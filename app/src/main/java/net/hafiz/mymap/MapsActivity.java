package net.hafiz.mymap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.hafiz.mymap.databinding.ActivityMapsBinding;

import java.util.Vector;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    MarkerOptions marker;
    LatLng hazard;
    Vector<MarkerOptions> markerOptions;

    private String URL = "http://192.168.0.173/serverside/data.php";
    RequestQueue requestQueue;
    Gson gson;
    Maklumat[] maklumats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        gson = new GsonBuilder().create();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        markerOptions = new Vector<>();
        markerOptions.add(new MarkerOptions()
                .position(new LatLng(6.446898, 100.274733))
                .title("Fall and Failing Objects reported by Nor Alia")
                .snippet("Date: 12/12/21 Time :11:20 AM")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        );
        hazard = new LatLng(6.097526, 100.777269);
        marker = new MarkerOptions().position(hazard).title("Hazard : ").snippet("My Snippet"+"\n"+"1st Line Text"+"\n"+"2nd Line Text"+"\n"+"3rd Line Text");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     **/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        //mMap.addMarker(marker);

        for (MarkerOptions mark : markerOptions){
            mMap.addMarker(mark);
        }
        enableMyLocation();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hazard,8));
        sendRequest();
    }

    private void enableMyLocation(){
    String perms[] = {"android.permission.ACCESS_FINE_LOCATION", " android.permission.ACCESS_NETWORK_STATE"};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                Log.d("starxx", "permission granted");

            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission

        Log.d("starxx", "permission denied");
            ActivityCompat.requestPermissions(this, perms ,200);

        }
    }
    public void sendRequest(){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL,onSuccess, onError);
        requestQueue.add(stringRequest);
    }

    public Response.Listener<String> onSuccess = new Response.Listener<String>(){
        @Override
            public void onResponse(String response) {
            maklumats = gson.fromJson(response, Maklumat[].class);

            //this will be displayed on logcat as debug
            Log.d("Maklumat", "Number of Maklumat Data Point : " + maklumats.length);

            if (maklumats.length <1) {
                Toast.makeText(getApplicationContext(), "Problem retrieving JSON data", Toast.LENGTH_SHORT).show();
                return;
            }

            for (Maklumat info:maklumats){
                Double lat = Double.parseDouble(info.lat);
                Double lng = Double.parseDouble(info.lng);
                String title = info.hazardType;
                String snippet1 = info.hazardDate;
                String snippet3 = info.hazardReporter;
                String snippet2 = info.hazardTime;

               MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng))
                        .title(title + " reported by "+snippet3)
                        .snippet("Date: "+snippet1 + " Time: "+snippet2)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

               mMap.addMarker(marker);

            }
        }
    };
    public Response.ErrorListener onError = new Response.ErrorListener(){

        @Override
        public void onErrorResponse (VolleyError error){
            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
        }
    };
}