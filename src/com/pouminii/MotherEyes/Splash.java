package com.pouminii.MotherEyes;

import com.pouminii.MotherEyes.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
         
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
 
            @Override
            public void run() {
                finish();       // 3 초후 이미지를 닫아버림
            }
        }, 2000);
    }
}
 