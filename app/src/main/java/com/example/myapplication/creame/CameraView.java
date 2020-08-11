package com.example.myapplication.creame;


import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraView extends GLSurfaceView implements GLSurfaceView.Renderer {
    private int cameraId=1;

    private Runnable mRunnable;

    public CameraView(Context context) {
        this(context,null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    public void switchCamera(){
        mRunnable=new Runnable() {
            @Override
            public void run() {
                cameraId=cameraId==1?0:1;
            }
        };
        onPause();
        onResume();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
