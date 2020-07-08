package com.example.myapplication;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class MySurfaceView extends GLSurfaceView implements View.OnTouchListener {
    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
