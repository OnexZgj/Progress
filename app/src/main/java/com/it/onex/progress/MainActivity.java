package com.it.onex.progress;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.it.onex.onexprogress.OnexHorizontalProgress;
import com.it.onex.onexprogress.OnexRoundProgress;

public class MainActivity extends AppCompatActivity {

    private OnexHorizontalProgress horPb;
    private OnexRoundProgress roundPb;
    private OnexRoundProgress roundPb2;
    private OnexRoundProgress roundPb3;

    public static final int MSG_UPDATE = 100;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            int progress = horPb.getProgress();
            int progress2 = roundPb.getProgress();
            int progress3 = roundPb2.getProgress();
            int progress4 = roundPb3.getProgress();
            roundPb.setProgress(++progress2);
            roundPb2.setProgress(++progress3);
            roundPb3.setProgress(++progress4);

            if (progress2 > 100) {
                roundPb.setProgress(0);
                roundPb2.setProgress(0);
                roundPb3.setProgress(0);
            }

            horPb.setProgress(++progress);
            if (progress > 100) {
                horPb.setProgress(0);

            }
            handler.sendEmptyMessageDelayed(MSG_UPDATE, 10);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        horPb = findViewById(R.id.horizontalProgress);
        roundPb = findViewById(R.id.onex_round);
        roundPb2 = findViewById(R.id.onex_round2);
        roundPb3 = findViewById(R.id.onex_round3);

        handler.sendEmptyMessageDelayed(MSG_UPDATE, 1000);
    }
}
