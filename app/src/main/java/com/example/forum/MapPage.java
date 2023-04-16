package com.example.forum;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPage extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap map;
    private LocationManager locationManager;

    SupportMapFragment mapFragment;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_page);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);



        /*{searchView=findViewById(R.id.sv_location);
        mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                List<Address> addressList =null;
                if(location !=null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapPage.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Address address=addressList.get(0);
                    LatLng latLng =new LatLng(address.getLongitude(),address.getLatitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });*/

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng PESSBuilding = new LatLng(52.6746, -8.5676);
        map.addMarker(new MarkerOptions().position(PESSBuilding).title("PESS Building"));

        LatLng CSBuilding = new LatLng(52.6739, -8.5756);
        map.addMarker(new MarkerOptions().position(CSBuilding).title("Computer Science Building"));

        LatLng KBBuilding = new LatLng(52.6726, -8.5768);
        map.addMarker(new MarkerOptions().position(KBBuilding).title("Kemmy Business Building"));

        LatLng SBuilding = new LatLng(52.6732, -8.5779);
        map.addMarker(new MarkerOptions().position(SBuilding).title("Robert Schuman Building"));

        LatLng ULLibrary = new LatLng(52.6733, -8.5735);
        map.addMarker(new MarkerOptions().position(ULLibrary).title("UL Library"));

        LatLng ULSport = new LatLng(52.6734, -8.5653);
        map.addMarker(new MarkerOptions().position(ULSport).title("UL Sport"));

        LatLng StablesClub = new LatLng(52.6731, -8.5709);
        map.addMarker(new MarkerOptions().position(StablesClub).title("Stables Club"));

        LatLng MainBuilding = new LatLng(52.6738, -8.5719);
        map.addMarker(new MarkerOptions().position(MainBuilding).title("Main Building"));



        // Check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        // Enable current location on map
        map.setMyLocationEnabled(true);

        // Get current location
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            map.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
        }

        // Register for location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Update map with new location
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        //map.clear();
        map.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable current location on map
                map.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
