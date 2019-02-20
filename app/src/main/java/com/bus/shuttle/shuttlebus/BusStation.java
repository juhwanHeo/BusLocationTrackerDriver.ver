package com.bus.shuttle.shuttlebus;

public class BusStation {
    private Time time;
    private GpsDistance gpsDistance;

    private boolean isTimeAm;


    public void schoolSchedule(){

        time = new Time();
        isTimeAm = time.am;
        if(isTimeAm){
            // 오전

        }
        else{
            // 오후

        }
    }



    /*
        아파트 -> 마석역 1번출구 -> 마석역 2번출구 -> 아파트
        1번출구 -> 다리및 -> 2번출구
     */
    public void nomalSchedule(){
        double distance = 0;
        gpsDistance = new GpsDistance();
        gpsDistance.setGps();


        distance = gpsDistance.getDistance(gpsDistance.apartment,gpsDistance.mStationExit1) +
                gpsDistance.getDistance(gpsDistance.mStationExit1,gpsDistance.underBridge) +
                gpsDistance.getDistance(gpsDistance.underBridge, gpsDistance.mStationExit2)+
                gpsDistance.getDistance(gpsDistance.mStationExit2,gpsDistance.apartment);
    }




}
