package com.oceanmanaus.lab.map;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FirstMapFragment mFirstMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirstMapFragment = FirstMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map_container, mFirstMapFragment)
                .commit();

        // Registrar escucha onMapReadyCallback
        mFirstMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng cali = new LatLng(3.4383, -76.5161);
        googleMap.addMarker(new MarkerOptions()
                .position(cali)
                .title("Cali la Sucursal del cielo"));

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(cali)
                .zoom(10)
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
