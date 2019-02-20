package com.bus.shuttle.shuttlebus;

import android.app.Activity;
import android.widget.Toast;



public class BusRun extends Activity {
    private Time time;
    private BusStation busStation;
    private GpsDistance gpsDistance;

    private boolean isBusRun;
    private boolean isBusArrive;
    private boolean isBusEnd;





    public void checkRun(){
        time = new Time();
        gpsDistance = new GpsDistance();



        // 버스운행이 종료됫나?
        while(!isBusEnd){
            // 아파트 반경 8m 보다 작거나 같다면
            if((gpsDistance.getDistance(gpsDistance.apartment,gpsDistance.curLocation) * 0.01) <= 8){
                isBusArrive = true;

                if(isBusArrive){
                    if(time.hour == 6 && time.min == 45){
                        isBusRun = true;
                        isBusArrive = false;
                    }
                    else if(time.hour == 7 && time.min == 5){
                        isBusRun = true;
                        isBusArrive = false;
                    }
                    else if(time.hour == 7 && time.min == 25){
                        isBusRun = true;
                        isBusArrive = false;
                    }

                    else if(time.hour == 8 && time.min == 20){
                        isBusRun = true;
                        isBusArrive = false;

                    }
                    else{
                        /*
                        운행시간이 아닙니다. 텍스트 만들기
                         */
                    }



                }

            } //   end of if distance

        /*
        남은 시간 측정
         */

        }// end of while


    }



    public void run(){
        busStation = new BusStation();
        time = new Time();


    }

}
