package com.shuttleBus.driver;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;


import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GpsInfo extends Service implements LocationListener {
    private final Context mContext;

    // 현재 GPS 사용유무
    private boolean isGPSEnabled = false;

    // 네트워크 사용유무
    private boolean isNetworkEnabled = false;

    // GPS 상태값
    private boolean isGetLocation = false;


    //    private Location locationSpeed;
    private double lat; // 위도
    private double lon; // 경도
    private int speed;
    private double mySpeed;
    private double maxSpeed;
    private int changeValue;
    private final int MINIMUM_ERROR_RANGE = 15;

//    // 최소 GPS 정보 업데이트 거리 10미터
//    // 지금 1미터 x
//    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
//
//    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
//    // 1000 = 1초
//    private static final long MIN_TIME_BW_UPDATES = 1000 * 1 * 1;

    private Location location;
    protected LocationManager locationManager;

    private DB_PushData pushData;
    private Distance distance;

    public GpsInfo(Context context) {
        this.mContext = context;
//        getLocation();
        checkPermission();
    }

    public void setChangeValue(int value){
        changeValue = value;
    }
    public int getChangeValue(){
        return changeValue;
    }


    @TargetApi(23)
    public Location getLocation() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(
                        mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                        mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // GPS 정보 가져오기
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // 현재 네트워크 상태 값 알아오기
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // GPS 와 네트워크사용이 가능하지 않을때 소스 구현
            } else {
                this.isGetLocation = true;
                // 네트워크 정보로 부터 위치값 가져오기
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            0,
                            0, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            // 위도 경도 저장
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            mySpeed = location.getSpeed();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                0,
                                0, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                                mySpeed = location.getSpeed();
                            }
                        }
                    }
                }


//                Log.e("hasSpeed>> "+ locationSpeed.hasSpeed(),locationSpeed.toString());
//                Log.e("getSpeed>> " + locationSpeed.getSpeed() , locationSpeed.getProvider());

//                Log.e("hasSpeed: "+ location.hasSpeed(),location.toString());
//                Log.e("getSpeed1: " + location.getSpeed() , location.getProvider());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("[isGPSEnabled]: "+ isGPSEnabled ,location.toString());
        Log.e("[this.location]: " + location.getProvider() , location.toString());

        return location;
    }

    /**
     * GPS 종료
     * */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GpsInfo.this);
        }
    }

    /**
     * 위도값을 가져옵니다.
     * */
    public double getLatitude() {
        if (location != null) {
            lat = location.getLatitude();
        }
        return lat;
    }

    /**
     * 경도값을 가져옵니다.
     * */
    public double getLongitude() {
        if (location != null) {
            lon = location.getLongitude();
        }
        return lon;
    }

    /**
     * GPS 나 wife 정보가 켜져있는지 확인합니다.
     * */
    public boolean isGetLocation() {
        return this.isGetLocation;
    }

    /**
     * GPS 정보를 가져오지 못했을때
     * 설정값으로 갈지 물어보는 alert 창
     * */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다. \n 설정창으로 가시겠습니까?");

        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    private void checkPermission(){
        PermissionListener permissionListener = new PermissionListener() {
            // 허락을 한 경우 실행할 부분
            @Override
            public void onPermissionGranted() {
                // PERMISSIONS_ACCESS_FINE_LOCATION 권한
                // PERMISSIONS_ACCESS_COARSE_LOCATION 권한

                getLocation(); // 위치 정보 가져온다.
            }

            // 거절을 한 경우 실행할 부분
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(),"해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(mContext)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("GPS Setting\n\n"
                        + "[설정]버튼을 눌러주시고 [권한]에서 모든 권한을 활성화를 해야만 이 어플을 정상적으로 사용 하실 수 있습니다."
                        + "\n\n설정창으로 가시겠습니까?")

                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    public void setGPSEnabled(boolean isGetLocation){
        this.isGPSEnabled = isGetLocation;
    }

    private long beforeTime;
    public long lateTime(int speed, double distance){
        //  distance/speed = 1시간 = 60분 0.5 30분
        long time = (long)((distance/speed)*60);
//        Log.e("스피드: " + speed , location.toString());
//        Log.e("distance: " + distance , location.toString());
//        Log.e("남은시간: " + distance/speed , location.toString());
//        Log.e("time: " + time , location.toString());
//        long time = (long)(50/1.33);

        if(speed == 0){
            return (long) distance / 50;
        }
        else if(speed > 10){
//            Log.e("speed: " +speed , location.toString());
            beforeTime = time;

            return time;
        }
        else if(beforeTime != 0){
            return beforeTime;
        }
        return -1;
    }

    public void onLocationChanged(Location location) {
//        getLocation();

        Log.e("[isGPSEnabled]: "+ isGPSEnabled ,toString());
        distance = new Distance();
        MainActivity.changeValue.setText("Change Value: " + (++changeValue));

//        Log.e("[location] Speed: " + location.getSpeed(), location.toString());
//        Log.e("[this] locationSpeed: " + this.location.getSpeed(), this.location.toString());
//        if(this.location.hasSpeed()) mySpeed = Double.parseDouble(String.format("%.1f", (this.location.getSpeedAccuracyMetersPerSecond() * 3.6)));

        if(mySpeed > maxSpeed) maxSpeed = mySpeed;
//        Log.e("[this] mySpeed: " + mySpeed, this.location.toString());
//        Log.e("[this] AccuracySpeed: " +this.location.getSpeedAccuracyMetersPerSecond()*3.6, this.location.toString());
//        Log.e("[this] hasSpeed: " + this.location.hasSpeed(), this.location.toString());
//        Log.e("[this] 속도: " + String.format("%.1f",(mySpeed)*3.6) + "Km/h",this.location.toString());
//        Log.e("[this] Provider" + this.location.getProvider(),this.location.toString());
//        Log.e("[location] "+ location.getProvider() , location.toString());

        int accuracy = (int)location.getAccuracy();

        if(this.location.hasSpeed()) speed = (int)(this.location.getSpeed()*3.6);
        else speed = 50;

        double dis = (distance.toDistance(location.getLatitude(),location.getLongitude()) *0.001);
        long time= lateTime(speed, dis);

        Log.e("Accuracy: " + accuracy, location.toString());
        MainActivity.checkLocation_tv.setText("위도: " + location.getLatitude() + "\n경도: " + location.getLongitude()
                + "\n정확도: " + accuracy + "\n속도: " + mySpeed +"Km/h"
                +"\n거리: "+ dis +"Km\n남은 시간: 약 " + time +"분" + "\nhasSpeed: " +location.hasSpeed());

        MainActivity.speed_tv.setText("[Gps] 속도: " + mySpeed + "Km/h" + "\n[Gps] MAX Speed: " + maxSpeed + "Km/h");
        //  거리

        if(isGPSEnabled) {
            try {
                // 오차범위 15
                if(accuracy >= MINIMUM_ERROR_RANGE) {
                    pushData = new DB_PushData();

                    String user_id = "SeoHee Bus";
                    final String str_user_id = user_id;

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String str_datetime = sdfNow.format(date);

                    double currentLat = location.getLatitude();
                    double currentLon = location.getLongitude();
                    String str_latitude = String.valueOf(currentLat);
                    String str_longitude = String.valueOf(currentLon);
                    Log.e("[pushData]: ", location.toString());
                    pushData.riding_user_information(str_user_id, str_datetime, str_latitude, str_longitude);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        // 위치정보 업데이트
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100,1, this);

    }

    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }
}