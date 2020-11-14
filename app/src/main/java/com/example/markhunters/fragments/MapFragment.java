package com.example.markhunters.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.markhunters.R;
import com.example.markhunters.model.GPSLocation;
import com.example.markhunters.model.Mark;
import com.example.markhunters.model.MarkLocation;
import com.example.markhunters.service.rest.RestClientCallbacks;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapFragment extends MarkFragment implements OnMapReadyCallback {
    MapView mapView = null;

    GoogleMap map;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1252;

    public MapFragment () {
        super();
    }

    public void refreshMarks() {
        if (map == null) return;
        map.clear();
        if (currentLocation != null) {
            getClient().getMarksByDistance(currentLocation, 1000.4, new RestClientCallbacks.CallbackCollection<Mark>() {
                @Override
                public void onSuccess(List<Mark> marks) {
                    activity.runOnUiThread(() -> marks.forEach(m -> addMarker(m.getLatLng(), m.getTitle(), m.id)));
                }

                @Override
                public void onFailure(@Nullable String message) {
                    System.out.println(message);
                    activity.runOnUiThread(() -> toast("Ocurrió un error intentando actualizar los marcadores"));
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        if (!permissionsAllowed()) {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        Button addMarkButton = root.findViewById(R.id.addMarkButton);
        addMarkButton.setOnClickListener(new MarkButtonListener());

        Button refreshButton = root.findViewById(R.id.refreshMarksButton);
        refreshButton.setOnClickListener(view -> refreshMarks());
        return root;
    }

    private class MarkButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(!permissionsAllowed()) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
            if (currentLocation != null) {
                GPSLocation gpsLocation = new GPSLocation(currentLocation);
                MarkLocation markLocation = new MarkLocation(gpsLocation);
                goToFragment(new MarkCreationFragment(markLocation));
            }
        }
    }

    private boolean permissionsAllowed() {
        return ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private class MarkLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            currentLocation = location;
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(context);
        map = googleMap;
        initLocationServices();
        refreshMarks();
        map.setOnMarkerClickListener(mark -> {
            MarkViewFragment markViewFragment = new MarkViewFragment(String.valueOf(mark.getTag()));
            goToFragment(markViewFragment);
            return true;
        });
    }

    private void addMarker(LatLng latLng, String title, int markId) {
        map.addMarker(new MarkerOptions().position(latLng).title(title).icon(bitmapDescriptorFromVector(context, R.drawable.ic_mark_hunters_mark))).setTag(markId);
    }

    private void initLocationServices() {
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) && shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
                Toast.makeText(context, "El permiso de ubicación es necesario para obtener la ubicación actual", Toast.LENGTH_LONG).show();
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            if (map != null) {
                map.setMyLocationEnabled(true);
                locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                locationListener = new MarkLocationListener();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                        1, locationListener);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocationServices();
            } else {
                Toast.makeText(context, "Permiso de ubicación no habilitado", Toast.LENGTH_LONG).show();
            }
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorId);
        vectorDrawable.setBounds(0,0, 100, 100);
        Bitmap bitmap = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
