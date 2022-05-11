package com.bustrackerdirver.rest.retrofit;

import com.bustrackerdirver.dto.BusLog;
import com.bustrackerdirver.dto.TimeRow;
import com.bustrackerdirver.rest.client.RetrofitClient;
import retrofit2.Call;

public class TimeRowCallRetrofit {
    /*
     * TODO: 2022/05/11
     * Test 진행
     */
    public void callGetTimeRow() {
        Call<TimeRow> call = RetrofitClient.getTimeRowApi().getTimeRow(null);

//        call.enqueue(new Callback<BusLog>() {
//            @Override
//            public void onResponse(Call<BusLog> call, Response<BusLog> response) {
//                Log.d("Connection Success", response.message());
//            }
//
//            @Override
//            public void onFailure(Call<BusLog> call, Throwable t) {
//                Log.e("Connection Failed", t.getMessage());
//                /*
//                 * TODO: 2022/05/11
//                 * 전송 실패 시
//                 * 스낵바 or 토스트 메세지 or 다이아로그 같은
//                 * 에러 메세지를 안내할 수단 출력
//                 */
//            }
//        });
    }
}
