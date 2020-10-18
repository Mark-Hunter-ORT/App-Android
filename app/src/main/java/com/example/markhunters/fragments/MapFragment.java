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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.markhunters.R;
import com.example.markhunters.model.Marca;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapFragment extends MarkFragment implements OnMapReadyCallback {
    MapView mapView = null;

    GoogleMap map;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1252;
    private final String PAYLOAD_KEY = "Mark";
    private Marca mark; // Todo debería usar generics acá? MapFragment implements ModelFragment<Mark>

    public MapFragment () {
        super();
        setPayloadKey(PAYLOAD_KEY);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        initEnvironment();
        if (getArguments() != null) {
            mark = (Marca) getArguments().getSerializable(getPayloadKey());
        }
        if (!permssionsAllowed()) {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        View addMarkButton = root.findViewById(R.id.addMarkButton);
        addMarkButton.setOnClickListener(new MarkButtonListener());
        return root;
    }


    private class MarkButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(!permssionsAllowed()) {
                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
            if (currentLocation != null) {
                mark = new Marca(currentLocation, activity.getUserUid());
                activity.goToFragment(new MarkCreationFragment(), mark);
            }
        }
    }

    private boolean permssionsAllowed() {
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
        LatLng buenosaires = new LatLng (-34.6083, -58.3712);
        addMarker(buenosaires,"el capo");
        if (mark != null && mark.getImageId() != null) { // todo como distingo marca persistida? Veo el id?
            addMarker(mark.getLatLng(), mark.getText());
        }
    }

    private void addMarker(LatLng latLng, String title) {
        map.addMarker(new MarkerOptions().position(latLng).title(title).icon(bitmapDescriptorFromVector(context, R.drawable.ic_mark_hunters_mark)));
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
