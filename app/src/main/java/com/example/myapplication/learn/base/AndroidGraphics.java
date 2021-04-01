package com.example.myapplication.learn.base;

import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.example.myapplication.CameraGLSurfaceView;
import com.example.myapplication.MainActivity;
import com.example.myapplication.learn.WriteCame;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AndroidGraphics implements GLSurfaceView.Renderer{
    ApplicationListener applicationListener;
    private View view ;

    public AndroidGraphics(MainActivity mainActivity){
        mainActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mainActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        view = new MySurfaceView(mainActivity);
        ((GLSurfaceView)(view)).setRenderer(new WriteCame(mainActivity,null));
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
    }

    public void onResume() {
        if (view!=null)
        ((GLSurfaceView)(view)).onResume();
    }

    public View getView() {
        return view;
    }
}
