package com.bus.shuttle.shuttlebus;

import android.os.Handler;

public class ThreadTest {
    Handler handler = new Handler();

    // 실시간 값 전송 가능
    public void thread(){

        new Thread(new Runnable(){
            public void run(){
                boolean running = false;
                running = true;
                while(running){
                    handler.post(new Runnable(){
                        @Override
                        public void run() {
                            //textView.setText("값 :" + value);

                        }
                    });
                }
            }
        });
    }
}
