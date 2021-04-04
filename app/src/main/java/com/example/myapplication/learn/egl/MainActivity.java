package com.example.myapplication.learn.egl;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.SurfaceView;

import com.example.myapplication.R;


public class MainActivity extends Activity {
    //渲染器 是  一个线程，它执行线程
    private EGLRenderer mGLRenderer;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egl);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.sv_main_demo);
        mGLRenderer = new EGLRenderer();
        mGLRenderer.start();
//        GLSurfaceView view = new GLSurfaceView(this);

//        view.setRenderer(new GLSurfaceView.Renderer() {
//            @Override
//            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//
//            }
//
//            @Override
//            public void onSurfaceChanged(GL10 gl, int width, int height) {
//
//            }
//
//            @Override
//            public void onDrawFrame(GL10 gl) {
//
//            }
//        });

//
//        GLSurfaceView view = new GLSurfaceView(this);
//        view.setRenderer(new GLSurfaceView.Renderer() {
//            @Override
//            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//
//            }
//
//            @Override
//            public void onSurfaceChanged(GL10 gl, int width, int height) {
//
//            }
//
//            @Override
//            public void onDrawFrame(GL10 gl) {
//
//            }
//        });
//        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder surfaceHolder) {
//                mGLRenderer.surfaceCreated(surfaceHolder.getSurface());
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
//                mGLRenderer.onDrawFrame(surfaceHolder.getSurface(), width, height);
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//
//            }
//        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGLRenderer.onRelease();
    }
}
