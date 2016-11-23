package com.oceanmanaus.lab.map;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLongClickListener {

    private static final int LOCATION_REQUEST_CODE = 1;
    private static final String LABEL = "Debug";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recupera do gerenciador de Fragments se existe o framgment de mapa
        SupportMapFragment mFirstMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);

        // Verifica se a instancia do fragment de mapa ja existe
        if (mFirstMapFragment == null) {
            mFirstMapFragment = FirstMapFragment.newInstance();
        }

        // Substitui o nosso layout para o fragment do Mapa
        getSupportFragmentManager()
                .beginTransaction().replace(R.id.map_container, mFirstMapFragment).commit();

        // Registrar escucha onMapReadyCallback
        mFirstMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Adiciona um marcador estatico
        LatLng coord = addNewMarker(3.4383, -76.5161, googleMap);

        // Personalizar a janela de informacoes do marcador
        googleMap.setInfoWindowAdapter(new MyAdapterMarker(this));

        // Move a camera do mapa para o novo marcado
        moveCamera(googleMap, coord);

        // Ativa a bussola
        googleMap.getUiSettings().setCompassEnabled(true);
        // Ativa os botoes de controle
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        // Ativa o botao da minha localizacao
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        // Icones quando voce clica em um marcador
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        // Eventos de Mapa
        googleMap.setOnMarkerClickListener(this); // Clica em um marcador
        googleMap.setOnMarkerDragListener(this); // Move um marcador
        googleMap.setOnInfoWindowClickListener(this); // Clica nas informacoes do marcador
        googleMap.setOnMapLongClickListener(this); // Longo clique no mapa

        // Verifica se o Android possui permissao para acessar localizacao GPS e NETWORK
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permissao
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }

        }else{
            // Ativa o mapa para direcionar para a localizacao atual
            googleMap.setMyLocationEnabled(true);
        }

    }

    // Move a camera do mapa para uma localizacao informada
    private void moveCamera(GoogleMap googleMap, LatLng coord) {
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(coord) // Localizacao informada
                .zoom(14) // Zoom da camera
                .build();

        // Move a camera do mapa (pode usar os metodos  - moveCamera() ou animateCamera()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private LatLng addNewMarker(double lat, double lon, GoogleMap googleMap) {

        // Cria o objeto das coordenadas
        LatLng coord = new LatLng(lat, lon);

        // Cria o marcador do mapa
        MarkerOptions markerOptions = new MarkerOptions()
                .position(coord)
                .title("Cali la Sucursal del cielo")
                .snippet("Description about Sucursal")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location))
                .rotation(0)
                .draggable(true);

        // Adiciona o marcador no mapa
        googleMap.addMarker(markerOptions);

        return coord;
    }

    // Adapter personalizado para as informacoes no marcador
    class MyAdapterMarker implements GoogleMap.InfoWindowAdapter{

        private final View view;

        public MyAdapterMarker(Activity context){
            // Cria o layout personalizado para o marcador
            view = context.getLayoutInflater().inflate(R.layout.info_map, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            // Recupera a view do nome no layout
            TextView vName = (TextView) view.findViewById(R.id.name);
            vName.setText(marker.getTitle());
            return view;
        }
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            // Verifica as permissoes permitidas
            if (permissions.length > 0 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Seta o mapa para ativar a localizacao do usuario
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Error de permissoes", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // Adiciona um marcador estatico
        LatLng cali = addNewMarker(latLng.latitude, latLng.longitude, mMap);
        // Move a camera do mapa para o novo marcador
        moveCamera(mMap, cali);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(LABEL, "Um marcador foi clicado");
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d(LABEL, "Um marcador estar sendo arrastado (onMarkerDragStart)");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d(LABEL, "Um marcador estar sendo arrastado (onMarkerDrag)");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d(LABEL, "Um marcador foi arrastado (onMarkerDragEnd)");
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d(LABEL, "Clicou na janela de informacoes do marcador");
    }
}
