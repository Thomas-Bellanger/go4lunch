package com.example.go4lunch.ui.MainActivity2;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String PERMS = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int RC_LOCATION = 100;
    private final ApiService mApiService = DI.getAPIService();
    private final MapViewModel mMapViewModel = MapViewModel.getInstance();
    private final GoogleManager mGoogleManager = GoogleManager.getInstance();
    private final MutableLiveData<Location> liveLocation = new MutableLiveData<>();
    LatLng latLng;
    private MapView mapView;
    private MapboxMap map;
    private LatLng userLocation;
    private LatLng lastLocation;
    private FloatingActionButton mapBtn;
    private TextView dl;

    public MapFragment() {
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);
        Mapbox.getInstance(view.getContext(), "pk.eyJ1IjoibmV4aXNsdWNpcyIsImEiOiJja3MzMzkyNWsyOXIxMm9uOG05NjlnZTB3In0.C0x6L93b14FZC6oKQfcBrQ");
        dl = view.findViewById(R.id.dl);
        mapView = view.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        MainActivity2.toolbarTitle = "I'm hungry!";
        mapBtn = view.findViewById(R.id.btn);
        mapBtn.setVisibility(View.GONE);

        return view;
    }
    //map
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.map = mapboxMap;
        acceptPermission();
        map.addOnCameraMoveListener(() -> mapBtn.setVisibility(View.VISIBLE));
        mapBtn.setOnClickListener(v ->reCenter());
        mApiService.getLiveDistance().observe(this.getActivity(), this::startCheckingNearby);
        mMapViewModel.liveRestaurantsCall.observe(getActivity(), this::getMarkers);
    }

    //recenter camera on user and check nearby restaurant if user has moved
    @SuppressLint("MissingPermission")
    public void reCenter(){
        lastLocation = new LatLng(map.getLocationComponent().getLastKnownLocation().getLatitude(), map.getLocationComponent().getLastKnownLocation().getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lastLocation))
                .zoom(10)
                .tilt(20)
                .build();
        map.setCameraPosition(cameraPosition);
        if (!userLocation.equals(lastLocation)) {
            userLocation = lastLocation;
            checkNearbyRestaurant();
        }
    }
    //firstcheck on google place API
    @SuppressLint("MissingPermission")
    private void startCheckingNearby(String s) {
        if (mMapViewModel.restaurantList.isEmpty()) {
            userLocation = new LatLng(map.getLocationComponent().getLastKnownLocation().getLatitude(), map.getLocationComponent().getLastKnownLocation().getLongitude());
            checkNearbyRestaurant();
            Log.e("search", "liste  "+mMapViewModel.restaurantList.size());
        }
    }
    //enable location
    @SuppressLint("MissingPermission")
    private void enableLocationComponent(@NonNull String loadedMapStyle) {
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
        locationComponent.addCompassListener(new CompassListener() {
            @Override
            public void onCompassChanged(float userHeading) {
                //get user's location
                if (locationComponent.getLastKnownLocation() != null) {
                    if (!mApiService.getLiveDistance().equals(locationComponent.getLastKnownLocation())) {
                        mApiService.getLiveDistance().setValue(locationComponent.getLastKnownLocation().toString());
                    }
                }
            }
            @Override
            public void onCompassAccuracyChange(int compassStatus) {
            }
        });
        mMapViewModel.liveRestaurantsCall.observe(getActivity(), this::getMarkers);
    }
    //call for API and update ui
    public void checkNearbyRestaurant() {
        updateUiWhenDlStart();
        mMapViewModel.setLocation(userLocation);
    }
    //permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void acceptPermission() {
        if (!EasyPermissions.hasPermissions(this.getContext(), PERMS)) {
            EasyPermissions.requestPermissions(this, "Need the permission to start!", RC_LOCATION, PERMS);
            return;
        }
        enableLocationComponent(Style.MAPBOX_STREETS);
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
    //update ui if loading is started/finished
    public void updateUiWhenDlIsFinished() {
        dl.setVisibility(View.GONE);
    }

    public void updateUiWhenDlStart() {
        dl.setVisibility(View.VISIBLE);
    }

    //add marker on the map and create the intent when clicking on it
    public void getMarkers(List<Restaurant> restaurants) {
        updateUiWhenDlStart();
        List<Marker> markerList = new ArrayList<>();
        IconFactory mIconFactory = IconFactory.getInstance(getContext());
        Icon pin = mIconFactory.fromResource(R.drawable.baseline_person_pin_circle_blue_500_36dp);
        Double meter;
        map.clear();
        for (Restaurant restaurant : restaurants) {
            latLng = new LatLng(restaurant.getLat(), restaurant.getLng());
            Marker marker = map.addMarker(new MarkerOptions().setPosition(latLng));

            if (restaurant.getJoiners().size() > 0) {
                marker.setIcon(pin);
            }
            markerList.add(marker);
            //get distance for restaurant
            meter = marker.getPosition().distanceTo(userLocation);
            int distance = meter.intValue();
            //refresh restaurants "distance" in the list
            restaurant.setDistance(distance);
            mApiService.getLiveDistance().setValue(distance + "m");
            for (Marker markers : markerList) {
                markers.setPosition(markers.getPosition());
                map.setOnMarkerClickListener(marker1 -> {
                    Intent intent = new Intent(getContext(), RestaurantDetail.class);
                    intent.putExtra(RestaurantDetail.KEY_RESTAURANT, mMapViewModel.liveRestaurantsCall.getValue().get(markerList.indexOf(marker1)));
                    startActivity(intent);

                    return false;
                });
            }
        }
        updateUiWhenDlIsFinished();
    }
}
