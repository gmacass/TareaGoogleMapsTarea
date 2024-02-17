package com.example.myappgooglemapstarea;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Places.initialize(getApplicationContext(), "CLAVE_DE_API");


        placesClient = Places.createClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng quevedo = new LatLng(-1.0113558476088707, -79.46938086135764);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(quevedo, 15);
        mMap.addMarker(new MarkerOptions().position(quevedo).title("Lugar"));
        mMap.moveCamera(cameraUpdate);

        mMap.setOnMapClickListener(this);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View infoView = getLayoutInflater().inflate(R.layout.custom_info_window, null);


                String placeId = marker.getSnippet();

                if (placeId != null) {

                    List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS);
                    FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);


                    placesClient.fetchPlace(request).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FetchPlaceResponse response = task.getResult();
                            Place place = response.getPlace();


                            TextView titleTextView = infoView.findViewById(R.id.text_title);
                            TextView descriptionTextView = infoView.findViewById(R.id.text_description);

                            titleTextView.setText(place.getName());
                            descriptionTextView.setText(place.getAddress());
                        }
                    });
                }

                return infoView;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Título del Lugar"));


        marker.showInfoWindow();
    }
}