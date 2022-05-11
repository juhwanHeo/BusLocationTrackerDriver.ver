package com.bustrackerdirver.dto;

public class Bus {

    public enum Status {
        // 대기
        STAND_BY,

        // 운행 중
        IN_PROGRESS,

        // 운행 종료
        COMPLETE,

        // 지연
        DELAY
    }
}
