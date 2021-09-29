package com.example.go4lunch.ui.MainActivity2;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.go4lunch.DI.DI;
import com.example.go4lunch.R;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.service.ApiService;
import com.example.go4lunch.ui.RestaurantDetail.RestaurantDetail;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {

    LatLng latLng;
    private final double lat1 = 48.8675;
    private final double lng1 = 2.6901;
    private final double lat2 = 48.8653;
    private final double lng2 = 2.6910;
    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private ImageButton mapBtn;
    private final ApiService mApiService = DI.getASIService();

    public MapFragment() {
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);
        Mapbox.getInstance(view.getContext(), "pk.eyJ1IjoibmV4aXNsdWNpcyIsImEiOiJja3MzMzkyNWsyOXIxMm9uOG05NjlnZTB3In0.C0x6L93b14FZC6oKQfcBrQ");

        mapView = view.findViewById(R.id.mapview);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapBtn = view.findViewById(R.id.mapBtn);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        MainActivity2.toolbarTitle = "I'm hungry!";

        return mapView;

    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.map = mapboxMap;
        LatLng pointlocation = new LatLng();
        mapboxMap.addOnCameraMoveListener(() -> {
            mapBtn.setVisibility(View.VISIBLE);
            mapBtn.setOnClickListener(v -> enableLocationComponent(mapboxMap.getStyleUrl()));
        });
        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                style -> enableLocationComponent(style));

        getMarkers();
    }


    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull String loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this.getContext())) {

// Get an instance of the component
            LocationComponent locationComponent = map.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    getApplicationContext());

// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this.getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this.getContext(), "location permission needed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            map.setStyle(Style.MAPBOX_STREETS, style -> enableLocationComponent(style));

        } else {
            Toast.makeText(this.getContext(), "location permission not granted", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void getMarkers() {
        for (Restaurant restaurant : mApiService.getRestaurants()) {
            latLng = new LatLng(restaurant.getLat(), restaurant.getLng());
            Marker marker = map.addMarker(new MarkerOptions().setPosition(latLng));
            map.setOnMarkerClickListener(marker1 -> {
                Intent intent = new Intent(getContext(), RestaurantDetail.class);
                intent.putExtra(RestaurantDetail.KEY_RESTAURANT, restaurant);
                startActivity(intent);
                return false;
            });
        }
    }
}
