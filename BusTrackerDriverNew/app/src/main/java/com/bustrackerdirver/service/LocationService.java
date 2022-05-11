package com.bustrackerdirver.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bustrackerdirver.dto.BusLog;
import com.bustrackerdirver.rest.retrofit.BusLogCallRetrofit;
import com.bustrackerdirver.utils.CommonUtils;

public class LocationService extends Service implements LocationListener {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        long time = System.currentTimeMillis();
        String date = CommonUtils.getNowDate(time);

        BusLog busLog = BusLog.builder()
                .time(time)
                .lat(location.getLatitude())
                .lon(location.getLongitude())
                .locationAccuracy(location.getAccuracy())
                .speed(location.getSpeed())
                .arrivalTime(date)
                .build();

        BusLogCallRetrofit busLogCallRetrofit = new BusLogCallRetrofit();

        busLogCallRetrofit.callPostBusLog(busLog);
    }

}
