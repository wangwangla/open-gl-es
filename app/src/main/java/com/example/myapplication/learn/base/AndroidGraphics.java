package com.example.myapplication.learn.base;

import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.myapplication.CameraGLSurfaceView;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.learn.MyRenderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AndroidGraphics implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener{
    ApplicationListener applicationListener;
    private GLSurfaceView view ;

    public AndroidGraphics(MainActivity mainActivity){
        mainActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mainActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        view = mainActivity.findViewById(R.id.surface);
//        view = new MySurfaceView(mainActivity);

        view.setEGLContextClientVersion(2);
//        view.setRenderer(new MyRenderer(mainActivity));
        view.setRenderer(this);
        applicationListener = new MyGame(mainActivity);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        applicationListener.create();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0,0,width,height);
        applicationListener.surfaceChanage(width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        applicationListener.render();
    }

    public void onPause() {
        ((GLSurfaceView)(view)).onPause();
        applicationListener.pause();
    }

    public void onResume() {
        if (view!=null)
        ((GLSurfaceView)(view)).onResume();
        applicationListener.resume();
    }

    public View getView() {
        return view;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        view.setRenderer(this);
    }
}
