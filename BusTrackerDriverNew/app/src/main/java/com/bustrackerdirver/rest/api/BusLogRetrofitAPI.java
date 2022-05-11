package com.bustrackerdirver.rest.api;

import com.bustrackerdirver.dto.BusLog;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/*
* Retrofit 참고
* 1, https://morm.tistory.com/296
* 2, https://velog.io/@plz_no_anr/Android-REST-API
*/
public interface BusLogRetrofitAPI {
    @POST("api/bus-logs")
    Call<BusLog> postBusLog(@Body BusLog busLog);

}
