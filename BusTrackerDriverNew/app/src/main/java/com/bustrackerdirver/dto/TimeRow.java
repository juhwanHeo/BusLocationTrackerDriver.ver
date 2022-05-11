package com.bustrackerdirver.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TimeRow {
    private int id;
    private List<Time> timeList;

}
