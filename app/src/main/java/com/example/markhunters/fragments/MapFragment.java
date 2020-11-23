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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

@SuppressWarnings("FieldCanBeLocal")
public class MapFragment extends MarkFragment implements OnMapReadyCallback {
    private final float MAX_DISTANCE_ENABLED = 50f; // metros
    private final double MAX_FETCH_DISTANCE = 1000.4; // metros
    MapView mapView = null;
    GoogleMap map;
    private Location currentLocation;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1252;
    private BitmapDescriptor icon_default = null;
    private BitmapDescriptor icon_followed = null;

    public MapFragment () {
        super();
    }

    public void refreshMarks() {
        if (map == null) return;
        map.clear();
        if (currentLocation != null) {
            getClient().getMarksByDistance(currentLocation, MAX_FETCH_DISTANCE, new RestClientCallbacks.CallbackCollection<Mark>() {
                @Override
                public void onSuccess(List<Mark> marks) {
                    activity.runOnUiThread(() -> marks.forEach(m -> addMarker(m.getLatLng(), m.getTitle(), m.id, m.isByFollowed)));
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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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
        map.setOnMarkerClickListener(new MarkListener());
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    private class MarkListener implements GoogleMap.OnMarkerClickListener {
        @Override
        public boolean onMarkerClick(Marker mark) {
            if (isWithinRange(mark.getPosition())) {
                MarkViewFragment markViewFragment = new MarkViewFragment(String.valueOf(mark.getTag()));
                goToFragment(markViewFragment);
            } else {
                activity.runOnUiThread(() -> toast("Debe acercarse más al mark para ver el contenido"));
            }
            return true;
        }
        // Obtiene la distancia entre dos pares de coordenadas y la compara con la distancia max para desbloquear el mark
        private boolean isWithinRange(LatLng latLng) {
            return getDistanceFrom(latLng) <= MAX_DISTANCE_ENABLED;
        }
    }

    private float getDistanceFrom(LatLng latLng) {
        float[] results = new float[1];
        Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), latLng.latitude, latLng.longitude, results);
        return results[0];
    }

    private void addMarker(LatLng latLng, String title, int markId, boolean followed) {
        BitmapDescriptor icon = getIcon(followed);
        map.addMarker(new MarkerOptions().position(latLng).title(title).icon(icon).alpha(resolveOpacity(latLng))).setTag(markId);
    }

    private BitmapDescriptor getIcon(boolean followed) {
        return followed ? getIconFollowed() : getIconDefault();
    }

    private BitmapDescriptor getIconFollowed() {
        if (icon_followed == null) {
            icon_followed = bitmapDescriptorFromVector(context, R.drawable.ic_mark_hunters_mark_followed);
        }
        return icon_followed;
    }

    private BitmapDescriptor getIconDefault() {
        if (icon_default == null) {
            icon_default = bitmapDescriptorFromVector(context, R.drawable.ic_mark_hunters_mark_default);
        }
        return icon_default;
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
                LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                LocationListener locationListener = new MarkLocationListener();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                        1, locationListener);
                float zoomLevel = 16.0f;
                final LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocationServices();
                refreshMarks();
            } else {
                Toast.makeText(context, "Permiso de ubicación no habilitado", Toast.LENGTH_LONG).show();
            }
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorId);
        assert(vectorDrawable != null);
        vectorDrawable.setBounds(0,0, 100, 100);
        Bitmap bitmap = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private float resolveOpacity(LatLng latLng) {
        float distance = getDistanceFrom(latLng);
        if (distance <= MAX_DISTANCE_ENABLED) return 1.0f;
        if (distance >= MAX_FETCH_DISTANCE) return 0.1f;
        float division = distance / (float) MAX_FETCH_DISTANCE;
        float relativeOpacity = 1.0f - division;
        if (relativeOpacity < 0.1f) relativeOpacity = 0.1f; // opacidad minima
        else if (relativeOpacity > 0.8f) relativeOpacity = 0.8f; // opacidad máxima sin habilitar
        return relativeOpacity;
    }
}
