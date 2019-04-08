package com.shuttleBus.driver;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static TextView checkLocation_tv;
    public static TextView changeValue;
    public static TextView speed_tv;

    private TextView checkBus_tv;
    private Button get_location_btn;
    private Button startBus_btn;
    private Button endBus_btn;
    private final Context mContext = this;
    private GpsInfo gps = null;
//    private GpsSpeed gpsSpeed = null;
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        backPressCloseHandler = new BackPressCloseHandler((Activity) mContext);
        speed_tv = (TextView) findViewById(R.id.speed_tv);
        changeValue = (TextView) findViewById(R.id.changeValue);
        checkLocation_tv = (TextView) findViewById(R.id.checkLocation_tv);
        checkBus_tv = (TextView) findViewById(R.id.checkBus_tv);
        get_location_btn = (Button) findViewById(R.id.get_location_btn);
        startBus_btn = (Button) findViewById(R.id.startBus_btn);
        endBus_btn = (Button) findViewById(R.id.endBus_btn);

        startBus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gps == null) {
                    checkBus_tv.setText("운행 중");
                    gps = new GpsInfo(mContext);
                    gps.setGPSEnabled(true);
                }
                else {
                    Snackbar.make(v, "이미 운행중입니다.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        endBus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gps != null) {
                    // gps 객체가 있다면.
                    checkBus_tv.setText("운행 종료");
                    gps.setChangeValue(0);
                    gps.setGPSEnabled(false);
                    gps.stopUsingGPS();
                    gps = null;
                }
                else{
                    // gps 객체가 없다면.
                    Snackbar.make(v, "운행을 시작하십시오.", Snackbar.LENGTH_LONG).show();
                }

            }
        });

        get_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gps != null) {
                    // gps 객체가 있다면.
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    checkLocation_tv.setText(String.format("위도: %f\n경도: %f", latitude, longitude));
                } else {
                    // gps 객체가 없다면.
                    checkLocation_tv.setText(String.format("위도: %d\n경도: %d", -1, -1));
                }
            }
        });

    }


    @Override
    public void onBackPressed(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog, (ViewGroup) findViewById(R.id.popupView));

        alertDialog.setMessage("\n");
        alertDialog.setView(layout);


        alertDialog.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finishAffinity();
            }
        });

        alertDialog.setTitle("종료");
        final AlertDialog alert = alertDialog.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        alert.show();

//        backPressCloseHandler.onBackPressed();
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
