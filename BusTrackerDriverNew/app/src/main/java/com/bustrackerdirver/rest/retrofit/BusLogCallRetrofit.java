package com.bustrackerdirver.rest.retrofit;

import android.util.Log;
import com.bustrackerdirver.dto.BusLog;
import com.bustrackerdirver.rest.client.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusLogCallRetrofit {
    /*
     * TODO: 2022/05/11
     * Test 진행
     */
    public void callPostBusLog(BusLog busLog) {
        Call<BusLog> call = RetrofitClient.getBusLogApi().postBusLog(busLog);

        call.enqueue(new Callback<BusLog>() {
            @Override
            public void onResponse(Call<BusLog> call, Response<BusLog> response) {
                Log.d("Connection Success", response.message());
            }

            @Override
            public void onFailure(Call<BusLog> call, Throwable t) {
                Log.e("Connection Failed", t.getMessage());
                /*
                 * TODO: 2022/05/11
                 * 전송 실패 시
                 * 스낵바 or 토스트 메세지 or 다이아로그 같은
                 * 에러 메세지를 안내할 수단 출력
                 */
            }
        });
    }
}
