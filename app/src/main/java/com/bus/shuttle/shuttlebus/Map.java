package com.bus.shuttle.shuttlebus;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.*;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.PendingIntent.getActivity;


public class Map extends Activity implements LocationListener ,Station , MapView.CurrentLocationEventListener {
    public static final String DAUM_MAPS_API_KEY = "d8ff7acf10464dd7861661aa208a44ff";
    private double currentLat;
    private double currentLot;
    private boolean isBtn = false;
    public double mySpeed;
    public double maxSpeed;



    // 1분
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 3;

    private Button btnTracking;
    private TextView textSpeed;

    private MapView mMapView;
    private GpsInfo gps;
    private LocationManager manager;
    private MapPoint mapPoint;
    private DB_Manager dbManager;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        setMap();
        setMarker();

        btnTracking = (Button) findViewById(R.id.btnTracking);
        btnTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBtn){
                    offTracking();
                    btnTracking.setText("On The Tracking");
                    Toast.makeText(getApplicationContext(),"추적 비활성화",Toast.LENGTH_SHORT).show();
                    isBtn = false;
                }
                else{
                    onTracking();
                    Toast.makeText(getApplicationContext(), "추적 활성화", Toast.LENGTH_SHORT).show();
                    btnTracking.setText("Off The Tracking");
                    isBtn = true;
                }
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
//        handler = new Handler(){
//            @Override
//            public void handleMessage (Message msg){
//                SharedPreferences prefs = getSharedPreferences("login",0);
//                String login = prefs.getString("USER_LOGIN","LOGOUT");
//                String user_id = "";
//
//                if(login.equals("LOGIN")){
//                    user_id = prefs.getString("USER_ID","NULL");
//                }
//                final String str_user_id = user_id;
//                long now = System.currentTimeMillis();
//                Date date = new Date(now);
//                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//                String str_datetime = sdfNow.format(date);
//
//                double lat = gps.getLatitude();
//                double lon = gps.getLongitude();
//
//                String str_latitude = String.valueOf(lat);
//                String str_longitude = String.valueOf(lon);
//
//                dbManager.riding_user_information(str_user_id,str_datetime,str_latitude,str_longitude);
//            }
//        };

        textSpeed = (TextView) findViewById(R.id.text_speed);

        if (location != null) {
            mySpeed = location.getSpeed();
            if (mySpeed > maxSpeed) {
                maxSpeed = mySpeed;
            }
        }

        textSpeed.setText(String.format("속도: [%.0f]Km/h",mySpeed));
        Toast.makeText(this,"GPS Change",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }
    public void onTracking(){

        // 트래킹 모드 활성화
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        mMapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.ic_launcher_round, new MapPOIItem.ImageOffset(10,0));
        mMapView.setShowCurrentLocationMarker(true);

    }

    public void offTracking(){

        // 트래킹 모드 비활성화
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff );
        mMapView.setShowCurrentLocationMarker(false);

    }

    public void setMarker(){

        MapPOIItem apart = new MapPOIItem();
        MapPOIItem stationExit1 = new MapPOIItem();
        MapPOIItem stationExit2 = new MapPOIItem();
        MapPOIItem underBridge = new MapPOIItem();
        MapPOIItem simSchool = new MapPOIItem();
        MapPOIItem maOffice = new MapPOIItem();
        MapPOIItem songSchool = new MapPOIItem();
        MapPOIItem maSchoolExit2 = new MapPOIItem();
        MapPOIItem maHiSchool = new MapPOIItem();

        MapPoint apartPoint = MapPoint.mapPointWithGeoCoord(apartmentLat, apartmentLon);
        MapPoint exit1Point = MapPoint.mapPointWithGeoCoord(mStationExit1Lat,mStationExit1Lon);
        MapPoint exit2Point = MapPoint.mapPointWithGeoCoord(mStationExit2Lat,mStationExit2Lon);
        MapPoint bridge = MapPoint.mapPointWithGeoCoord(underBridgeLat,underBridgeLon);
        MapPoint simSchoolPoint = MapPoint.mapPointWithGeoCoord(simSchoolLat,simSchoolLon);
        MapPoint maOfficePoint = MapPoint.mapPointWithGeoCoord(maOfficeLat,maOfficeLon);
        MapPoint songSchoolPoint = MapPoint.mapPointWithGeoCoord(songSchoolLat,songSchoolLon);
        MapPoint maSchoolExit2Point = MapPoint.mapPointWithGeoCoord(maSchoolBackLat,maSchoolBackLon);
        MapPoint maHiSchoolExitPoint = MapPoint.mapPointWithGeoCoord(maHiSchoolLat,maHiSchoolLon);

        apart.setItemName("아파트");
        stationExit1.setItemName("마석역 1번출구");
        stationExit2.setItemName("마석역 2번출구");
        underBridge.setItemName("다리 및");
        simSchool.setItemName("심석중,고등학교");
        maOffice.setItemName("화도읍사무소");
        songSchool.setItemName("송라초,중학교");
        maSchoolExit2.setItemName("마석초등학교");
        maHiSchool.setItemName("마석고등학교");

        mMapView.selectPOIItem(apart, true);
        mMapView.selectPOIItem(stationExit1, true);
        mMapView.selectPOIItem(stationExit2, true);
        mMapView.selectPOIItem(underBridge, true);
        mMapView.selectPOIItem(simSchool, true);
        mMapView.selectPOIItem(maOffice, true);
        mMapView.selectPOIItem(songSchool, true);
        mMapView.selectPOIItem(maSchoolExit2, true);
        mMapView.selectPOIItem(maHiSchool, true);

        apart.setMapPoint(apartPoint);
        stationExit1.setMapPoint(exit1Point);
        stationExit2.setMapPoint(exit2Point);
        underBridge.setMapPoint(bridge);
        simSchool.setMapPoint(simSchoolPoint);
        maOffice.setMapPoint(maOfficePoint);
        songSchool.setMapPoint(songSchoolPoint);
        maSchoolExit2.setMapPoint(maSchoolExit2Point);
        maHiSchool.setMapPoint(maHiSchoolExitPoint);

        mMapView.addPOIItem(apart);
        mMapView.addPOIItem(stationExit1);
        mMapView.addPOIItem(stationExit2);
        mMapView.addPOIItem(underBridge);
        mMapView.addPOIItem(simSchool);
        mMapView.addPOIItem(maOffice);
        mMapView.addPOIItem(songSchool);
        mMapView.addPOIItem(maSchoolExit2);
        mMapView.addPOIItem(maHiSchool);

    }

    public void setMap() {

        gps = new GpsInfo(this);
        mMapView = new MapView(this);
        mMapView.setDaumMapApiKey(DAUM_MAPS_API_KEY);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mMapView);


        currentLat = gps.getLatitude();
        currentLot = gps.getLongitude();

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapPoint = MapPoint.mapPointWithGeoCoord(currentLat, currentLot);

        //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
        mMapView.setMapCenterPoint(mapPoint, true);

        // 줌 인
        mMapView.zoomIn(true);
        // 줌 아웃
        mMapView.zoomOut(true);

        // onLocationChange를 불러온다
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }
}


