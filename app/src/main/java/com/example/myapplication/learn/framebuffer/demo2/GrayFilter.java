/*
 *
 * GrayFilter.java
 * 
 * Created by Wuwang on 2016/12/14
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.example.myapplication.learn.framebuffer.demo2;

import android.content.res.Resources;

import com.example.myapplication.learn.framebuffer.demo2.AFilter;

/**
 * Description:
 */
public class GrayFilter extends AFilter {

    public GrayFilter(Resources mRes) {
        super(mRes);
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("base_vertex.sh",
            "gray_fragment.frag");
    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }
}
