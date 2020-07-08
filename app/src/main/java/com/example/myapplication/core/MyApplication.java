package com.example.myapplication.core;

import android.app.Application;
import android.opengl.GLES20;

import com.example.myapplication.AndroidGraphics;

public class MyApplication {
    public static MyApplication app;
    public static AndroidGraphics graphics;
    public MyApplication(){
        app = this;
        init();
    }

    private void init() {
        String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projTrans;\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "   v_color.a = v_color.a * (255.0/254.0);\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        String fragmentShader = "#ifdef GL_ES\n" //
                + "#define LOWP lowp\n" //
                + "precision mediump float;\n" //
                + "#else\n" //
                + "#define LOWP \n" //
                + "#endif\n" //
                + "varying LOWP vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
                + "}";
        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
    }

    public void render() {
        //绘制之前需要将背景清除  但是如果绘制可以将前一个全部覆盖可以不进行清除。
        /**
         * 可以绘制其他将屏幕覆盖
         *      绘制的效率低
         *      设置任意的位置和方向，判断清楚矩阵的大小和位置(emmmmmmmmmmmm)
         *      可以清除缓存区的东西
         *
         * 颜色存储方式
         *      直接存储在位平面内
         *      存储一个颜色索引值
         *
         * GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
         *      GLES20.GL_COLOR_BUFFER_BIT:清除缓存区（颜色）
         *
         * glClearolor 设置清除颜色   默认黑色
         *
         * glClear() 清楚指定缓存区的
         *      缓存区
         *          - 颜色缓存区（color）
         *          - 深度缓存区(depth)
         *          - 累积缓存区(accum)
         *          - 模板缓存区(stencil)
         *
         *          清楚上述的缓存区值，也可以指定各个缓存区的值
         *          - glClearColor()
         *          - glClearDepth()
         *          - glClearIndex()
         *          - glClearAccum()
         *          - glClearStanecil()
         *
         * 允许清楚多个，清楚是比骄慢的，如果不允许清除多个，那么就一个一个清除。但是一起清除会更快。
         *
         * 指定颜色
         *
         * 强制绘制
         *
         *       GLES20.glFlush();   强制客户机发送数据包（存在客户机）
         *       GLES20.glFinish();   如果写昂无论哪种情况都执行
         *
         *
         *
         */
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(1.0F,1.0F,1.0F,1.0F);



    }
}
