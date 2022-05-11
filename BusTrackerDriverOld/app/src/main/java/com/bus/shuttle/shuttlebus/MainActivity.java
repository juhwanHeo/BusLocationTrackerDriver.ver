package com.bus.shuttle.shuttlebus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity implements LocationListener {

    Handler handler = new Handler();
    int time = 0;
    int value = 0;
    double currentLat;
    double currentLon;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 3;

    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
    // 1000 = 1초
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    final GpsDistance gd = new GpsDistance();
    private Button btnShowLocation;
    private TextView currentText;
    private Button moveMapBtn;
    private TextView txtLocation;
    private TextView txtAll;
    private TextView txtCurrent;
    private TextView currentValue;
    private TextView changeValue;
    private TextView curDistance;

    public double curLat;
    public double curLot;


    private final int MY_PERMISSION_STORAGE = 1111;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    private BackPressCloseHandler backPressCloseHandler;
    private GpsInfo gps;
    private DB_Manager dbManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        callPermission();


        // 최소 GPS 정보 업데이트 거리 10미터
        // 지금 1미터 x
        backPressCloseHandler = new BackPressCloseHandler(this);


        currentText = (TextView) findViewById(R.id.currentText);
        moveMapBtn = (Button) findViewById(R.id.move_map_btn);
        btnShowLocation = (Button) findViewById(R.id.btn1);
        txtLocation = (TextView) findViewById(R.id.clickLocation);
        txtCurrent = (TextView) findViewById(R.id.stationDistance);
        txtAll = (TextView) findViewById(R.id.allDistance);
        currentValue = (TextView) findViewById(R.id.currentValue);
        changeValue = (TextView) findViewById(R.id.changeValue);
        curDistance = (TextView) findViewById(R.id.curDistance);
//        floatingBar = (Button) findViewById(R.id.fab);



        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
        Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

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

        // onLocationChanged 를 불러온다
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);

        gd.setGps();

        // GPS 정보를 보여주기 위한 이벤트 클래스 등록
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                time = 0;
                Thread.interrupted();
                // 권한 요청을 해야 함
                
                if (!isPermission) {
                    callPermission();
                    return;
                }

                gps = new GpsInfo(MainActivity.this);
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    double currentGps = gd.toDistance(latitude,longitude);

                    txtLocation.setText(String.format("위도: %f\n경도: %f",latitude,longitude));
                    txtCurrent.setText(String.format(
                            "마석역까지의 거리 \n: [%.2f] Km",(currentGps*0.001)));
//                    Toast.makeText(
//                            getApplicationContext(),
//                            "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
//                            Toast.LENGTH_LONG).show();
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
                new Thread(new Runnable(){
                    public void run(){
                        boolean running;
                        running = true;
                        while(running){
                            handler.post(new Runnable(){
                            @Override
                            public void run() {
//                                double curSpeed = gps.getSpeed();;
                                currentValue.setText("진행 시간: "+time + "초");
                                time++;
                            }
                            });
                            try{
                                Thread.sleep(1000);

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                    }
                }).start();
            }
        });

        callPermission();  // 권한 요청을 해야 함

        txtAll.setText(String.format("총 거리 : [%.2f]Km",(gd.allDistance()*0.001)));

        moveMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        getApplicationContext(),
                        Map.class
                );
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        value++;

        double currentLat = location.getLatitude();
        double currentLon = location.getLongitude();
        double currentDistance = gd.toDistance(currentLat,currentLon);
        curLat = currentLat;
        curLot = currentLon;

        dbManager = new DB_Manager();



        SharedPreferences prefs = getSharedPreferences("login",0);
        String login = prefs.getString("USER_LOGIN","LOGOUT");
        String user_id = "기모찌";

        if(login.equals("LOGIN")){
            user_id = prefs.getString("USER_ID","NULL");
        }
        final String str_user_id = user_id;
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String str_datetime = sdfNow.format(date);


        String str_latitude = String.valueOf(curLat);
        String str_longitude = String.valueOf(curLot);

        Toast.makeText(getApplicationContext(),"DB 전송 시작",Toast.LENGTH_SHORT).show();
        dbManager.riding_user_information(str_user_id,str_datetime,str_latitude,str_longitude);
        Toast.makeText(getApplicationContext(),"DB 전송 완료",Toast.LENGTH_SHORT).show();



        currentText.setText("실시간"+"\n경도: "+currentLat+"\n위도: "+currentLon);
        changeValue.setText("changeValue: "+value);
        curDistance.setText(String.format("마석역까지 거리: [%.2f]Km",currentDistance*0.001));

    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
        switch (requestCode){
            case MY_PERMISSION_STORAGE:
                for(int i = 0 ;i < grantResults.length; i++){
                    if(grantResults[i] < 0){
                        Toast.makeText(getApplicationContext(),"해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;

        }
    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
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



    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
//        super.onBackPressed();
    }


}
class BackPressCloseHandler{
    private long backKeyPressedTime = 0;
    private Toast toast;
    private Activity activity;
    private final long REMAINING_TIME = 2000;



    // 생성자로부터 어느 화면인지를 받아온다.
    public BackPressCloseHandler(Activity context){
        this.activity = context;

    }
    public void onBackPressed(){
        if(System.currentTimeMillis() > backKeyPressedTime + REMAINING_TIME){
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }

        if(System.currentTimeMillis() <= backKeyPressedTime + REMAINING_TIME){
            activity.finish();
            toast.cancel();
        }


    }

    private void showGuide() {
        toast = Toast.makeText(activity,"\'뒤로\' 버튼을 한번더 누루시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }


}


