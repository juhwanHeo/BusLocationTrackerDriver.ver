package com.bustrackerdirver.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusLog {

    private int id;
    private int timeRowId;
    private double lat;
    private double lon;
    private double locationAccuracy;
    private double speed;
    private long time;
    private String arrivalTime;

}
