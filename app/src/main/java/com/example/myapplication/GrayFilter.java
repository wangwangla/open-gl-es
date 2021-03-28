/*
 *
 * GrayFilter.java
 * 
 * Created by Wuwang on 2016/12/14
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.example.myapplication;

import android.content.res.Resources;

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
