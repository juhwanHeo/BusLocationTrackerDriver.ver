package com.bustrackerdirver.dto;

import lombok.Data;

import java.util.List;

@Data
public class TimeTable {
    private String id;
    private List<TimeRow> timeRowList;

}
