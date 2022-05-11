package com.bustrackerdirver.rest.api;

import com.bustrackerdirver.dto.TimeRow;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

/*
* Retrofit 참고
* 1, https://morm.tistory.com/296
* 2, https://velog.io/@plz_no_anr/Android-REST-API
*/
public interface TimeRowRetrofitAPI {
    @GET("api/time-row")
    Call<TimeRow> getTimeRow(@Body TimeRow timeRow);

}
