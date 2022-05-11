package com.bus.shuttle.shuttlebus;

import android.app.Activity;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

public class realTImeThread extends Activity {
    Handler handler = new Handler();
    private TextView currentText;
    private Button currentBtn;

    public realTImeThread(){


    }

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
