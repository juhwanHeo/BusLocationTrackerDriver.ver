package com.bus.shuttle.shuttlebus;

import java.util.Calendar;

public class Time {

    private Calendar cal;

    public int year;
    public int month;
    public int date;
    public int hour;
    public boolean am;
    public int min;
    public int sec;

    public Time(){
        getTime();
    }




    private void getTime(){
        int amPm;
        cal = Calendar.getInstance();
        year = cal.get(cal.YEAR);           // 년
        month = cal.get(cal.MONTH) + 1;     // 월
        date  = cal.get(cal.DATE);          // 일
        hour = cal.get(cal.HOUR_OF_DAY);    // 24시 기준 시
        amPm = cal.get(cal.AM_PM);          // 0 = 오전, 1 = 오후
        min  = cal.get(cal.MINUTE);         // 분
        sec = cal.get(cal.SECOND);          // 초

        if(amPm == 0){
            am = true;
        }
        else{
            am = false;
        }


    }


}
