package com.example.myapplication;

import android.app.Activity;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLDisplay;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.example.myapplication.learn.base.AndroidGraphics;

import javax.microedition.khronos.egl.EGL;

public class MainActivity extends Activity {
    protected AndroidGraphics graphics;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        graphics = new AndroidGraphics(this);
        setContentView(graphics.getView());

//        EGLDisplay display = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
//        if (display == EGL14.EGL_NO_DISPLAY) {
//            System.out.println("unable");
//        }
//        int arr[] = new int[1];
//        int arr2[] = new int[1];
//        if (!EGL14.eglInitialize(display,arr,0,arr2,0)){
//            System.out.println("unable chu shi hua");
//        }
//        EGLConfig con[] = new EGLConfig[10];
//        int xx [] = new int[1];
//        boolean b = EGL14.eglGetConfigs(display, con, 0, 10, xx, 0);
//        System.out.println(b);
//        System.out.println("=>>>......");
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
