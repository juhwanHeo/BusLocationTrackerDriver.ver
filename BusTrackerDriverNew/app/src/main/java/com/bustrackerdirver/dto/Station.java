package com.bustrackerdirver.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Station {
    private int id;
    private String stationName;
    private double lat;
    private double lon;
}
