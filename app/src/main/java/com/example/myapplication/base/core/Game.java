package com.example.myapplication.base.core;

import com.example.myapplication.learn.base.ApplicationListener;

import javax.microedition.khronos.opengles.GL10;

public abstract class Game implements ApplicationListener {
    protected Screen screen;

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render(GL10 gl) {
        screen.render(1);
    }

    @Override
    public void pause() {
        screen.pause();
    }

    @Override
    public void resume() {
        screen.resume();
    }

    public void setScreen(Screen screen) {
        if (this.screen != null)this.screen.hide();
        this.screen = screen;
        screen.show();
    }
}
