package com.example.healthapp;

import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.ImageHolder;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;

public class ChayBoActivity extends AppCompatActivity {
    private MapView mapView;
    private FloatingActionButton fabLocation;
    private ImageView image_back;
    private TextView batDau, hoanThanh, countdownTimer;
    private TextView thoiGian, tocDo, quangDuong, kcal;

    private CardView cardView;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chay_bo);

        mapView= findViewById(R.id.mapView);
        fabLocation = findViewById(R.id.myLocation);
        image_back = findViewById(R.id.image_back);
        batDau = findViewById(R.id.btBatDau);
        hoanThanh = findViewById(R.id.btHoanThanh);
        cardView = findViewById(R.id.cardView);

        thoiGian = findViewById(R.id.thoiGian);
        tocDo = findViewById(R.id.tocDo);
        quangDuong = findViewById(R.id.quangDuong);
        kcal = findViewById(R.id.kcal);

        countdownTimer = findViewById(R.id.countdownTimer);

        batDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "True", Toast.LENGTH_SHORT).show();
                batDau.setVisibility(View.GONE);
                mapView.setVisibility(View.GONE);
                countdownTimer.setVisibility(View.VISIBLE);
                hoanThanh.setVisibility(View.VISIBLE);

                new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        countdownTimer.setText(String.valueOf((millisUntilFinished / 1000)+1));
                    }

                    public void onFinish() {
                        countdownTimer.setText("Go!");

                        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) mapView.getLayoutParams();
                        params1.addRule(RelativeLayout.ABOVE, R.id.cardView);
                        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) fabLocation.getLayoutParams();
                        params2.addRule(RelativeLayout.ABOVE, R.id.cardView);


                        mapView.setLayoutParams(params1);
                        fabLocation.setLayoutParams(params2);
                        cardView.setVisibility(View.VISIBLE);
                        mapView.setVisibility(View.VISIBLE);

//                        Intent serviceIntent = new Intent(getApplicationContext(), TrackingService.class);
//                        startService(serviceIntent);

                    }
                }.start();
            }
        });
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChayBoActivity.this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
        if(ActivityCompat.checkSelfPermission(ChayBoActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        mapView.getMapboxMap().loadStyleUri("mapbox://styles/thachphan/clvvmp6gs025g01qp4xdnfnzm", new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(20.0).build());

                LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
                locationComponentPlugin.setEnabled(true);
                LocationPuck2D locationPuck2D = new LocationPuck2D();

                locationPuck2D.setBearingImage(ImageHolder.from(R.drawable.circle1));

                locationComponentPlugin.setLocationPuck(locationPuck2D);

                locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);

                getGestures(mapView).addOnMoveListener(onMoveListener);

                fabLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);

                        getGestures(mapView).addOnMoveListener(onMoveListener);
                        fabLocation.hide();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter("tracking_update"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result){
                Toast.makeText(ChayBoActivity.this,"Cho phep", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(ChayBoActivity.this,"Khong cho phep", Toast.LENGTH_SHORT).show();
            }
        }
    });
    private final OnIndicatorBearingChangedListener onIndicatorBearingChangedListener = new OnIndicatorBearingChangedListener() {
        @Override
        public void onIndicatorBearingChanged(double v) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().bearing(v).build());
        }
    };
    private final OnIndicatorPositionChangedListener onIndicatorPositionChangedListener = new OnIndicatorPositionChangedListener() {
        @Override
        public void onIndicatorPositionChanged(@NonNull Point point) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().center(point).zoom(20.0).build());
            getGestures(mapView).setFocalPoint(mapView.getMapboxMap().pixelForCoordinate(point));
        }
    };
    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
            getLocationComponent(mapView).removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
            getLocationComponent(mapView).removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
            getGestures(mapView).removeOnMoveListener(onMoveListener);
            fabLocation.show();
        }

        @Override
        public boolean onMove(@NonNull MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {

        }
    };
}