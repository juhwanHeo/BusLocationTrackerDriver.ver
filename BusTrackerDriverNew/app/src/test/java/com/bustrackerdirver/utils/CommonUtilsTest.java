package com.bustrackerdirver.utils;


import org.junit.Test;

public class CommonUtilsTest {

    @Test
    public void testGetNowDate() {
        CommonUtils.getNowDate(System.currentTimeMillis());
    }
}