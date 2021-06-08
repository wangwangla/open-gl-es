package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.myapplication.learn.base.AndroidGraphics;

public class MainActivity extends Activity {
    protected AndroidGraphics graphics;
    private GLSurfaceView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        graphics = new AndroidGraphics(this);
//        setContentView(graphics.getView());
        setContentView(R.layout.activity_main);
        graphics = new AndroidGraphics(this);

//        GLSurfaceView view = findViewById(R.id.surface);
//        view = new MySurfaceView(this);
//        view.setEGLContextClientVersion(2);
//        view.setRenderer(new ParticlesRenderer(this));
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

    class MyRen extends SurfaceView implements SurfaceHolder.Callback {
        SurfaceHolder holder=null;
        public MyRen(Context context) {
            super(context);
            holder = getHolder();
            holder.addCallback(this);
        }

        public MyRen(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyRen(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }
}
