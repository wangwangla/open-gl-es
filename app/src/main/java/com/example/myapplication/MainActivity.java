package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.learn.base.AndroidGraphics;

public class MainActivity extends Activity {
    protected AndroidGraphics graphics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        graphics = new AndroidGraphics(this);
        setContentView(graphics.getView());
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (graphics !=null){
            graphics.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (graphics !=null){
            graphics.onResume();
        }
    }
}
