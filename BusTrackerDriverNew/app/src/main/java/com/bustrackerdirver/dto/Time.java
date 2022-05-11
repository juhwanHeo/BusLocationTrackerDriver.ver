package com.bustrackerdirver.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Time {
    private int id;
    private int order;
    private Station station;
    private long time;
    private String arrivalTime;

}
