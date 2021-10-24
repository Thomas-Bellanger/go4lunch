package com.example.go4lunch.ui.MainActivity2;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.DI.DI;
import com.example.go4lunch.R;
import com.example.go4lunch.manager.GoogleManager;
import com.example.go4lunch.model.Restaurant;
import com.example.go4lunch.service.ApiService;
import com.example.go4lunch.ui.RestaurantDetail.RestaurantDetail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.CompassListener;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {

    LatLng latLng;
    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private final ApiService mApiService = DI.getASIService();
    private MapViewModel mMapViewModel = MapViewModel.getInstance();
    private LatLng userLocation;
    private LatLng lastLocation;
    private FloatingActionButton mapBtn;
    private GoogleManager mGoogleManager = GoogleManager.getInstance();
    private MutableLiveData<Location> liveLocation= new MutableLiveData<>();

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
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
            MainActivity2.toolbarTitle = "I'm hungry!";
            mapBtn = view.findViewById(R.id.btn);
            mapBtn.setVisibility(View.GONE);
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this.getActivity());

        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
            this.map = mapboxMap;
            map.addOnCameraMoveListener(() -> mapBtn.setVisibility(View.VISIBLE));
            enableLocationComponent(Style.MAPBOX_STREETS);
            mapBtn.setOnClickListener(v -> {
                if(userLocation.equals(lastLocation)) {
                }
                else{
                    getLocation();
                }
                getMarkers(mApiService.getFilteredRestaurants());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(userLocation).zoom(12).build();
                map.setCameraPosition(cameraPosition);
                mapBtn.setVisibility(View.GONE);
            });
            getLocation();
    }

    @SuppressLint("MissingPermission")
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

            if(locationComponent.getLastKnownLocation() != null) {
                userLocation = new LatLng(locationComponent.getLastKnownLocation().getLatitude(), locationComponent.getLastKnownLocation().getLongitude());
            }
                 else{
                    userLocation = new LatLng(map.getCameraPosition().target.getLatitude(), map.getCameraPosition().target.getLongitude());
                }
        }
        else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this.getActivity());
            Log.e("Nok", "Nok");
        }
    }

    private void getLocation(){
        LocationComponent locationComponent = map.getLocationComponent();
        if(locationComponent.getLastKnownLocation()!=null) {
            lastLocation = new LatLng(locationComponent.getLastKnownLocation().getLatitude(), locationComponent.getLastKnownLocation().getLongitude());
           if (userLocation != null) {
               if (userLocation.equals(lastLocation)) {
               } else {
                   userLocation = lastLocation;
                   checkNearbyRestaurant();
                   getMarkers(mApiService.getFilteredRestaurants());
               }
           }
           else {
                userLocation = lastLocation;
                }
            liveLocation.setValue(locationComponent.getLastKnownLocation());
        }
        else{
            userLocation = new LatLng(map.getCameraPosition().target.getLatitude(), map.getCameraPosition().target.getLongitude());
        }
        getMarkers(mApiService.getFilteredRestaurants());
    }

    public void checkNearbyRestaurant() {
            mMapViewModel.setLocation(userLocation);
            mApiService.populateRestaurant();
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            Log.e("permission", "granted");
            enableLocationComponent(Style.MAPBOX_STREETS);


        } else {
            Log.e("permission", "Not granted");
            Toast.makeText(this.getContext(), "location permission not granted", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
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

    public void getMarkers(List<Restaurant> restaurants) {
        List<Marker> markerList = new ArrayList<>();
        IconFactory mIconFactory = IconFactory.getInstance(getContext());
        Icon pin = mIconFactory.fromResource(R.drawable.baseline_person_pin_circle_white_24);
        Double distance;
        map.clear();
        for (Restaurant restaurant : restaurants) {
            latLng = new LatLng(restaurant.getLat(), restaurant.getLng());
            Marker marker = map.addMarker(new MarkerOptions().setPosition(latLng));

            if(restaurant.getJoiners().size()>0) {
               marker.setIcon(pin);
            }
            markerList.add(marker);
            distance = marker.getPosition().distanceTo(userLocation);
                String[] distances = distance.toString().split("\\.");
                restaurant.setDistance(distances[0] + "m");
                mApiService.getLiveDistance().setValue(distance.toString());
                for (Marker markers : markerList) {
                    markers.setPosition(markers.getPosition());
                    map.setOnMarkerClickListener(marker1 -> {
                        Intent intent = new Intent(getContext(), RestaurantDetail.class);
                        intent.putExtra(RestaurantDetail.KEY_RESTAURANT, mApiService.getRestaurants().get(markerList.indexOf(marker1)));
                        startActivity(intent);

                        return false;
                    });
                }
            }
        }
}
