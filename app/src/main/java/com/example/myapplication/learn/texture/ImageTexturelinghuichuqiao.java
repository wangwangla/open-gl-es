package com.example.myapplication.learn.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.example.myapplication.learn.shape.base.BaseGameScreen;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 绘制灰色
 */
public class ImageTexturelinghuichuqiao extends BaseGameScreen {
    private int mProgram;
    private int glHPosition;
    private int glHTexture;
    private int glHCoordinate;
    private int time;
    private Bitmap mBitmap;
    private FloatBuffer bPos;
    private FloatBuffer bCoord;

    private final float[] sPos = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f
    };

    private final float[] sCoord = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };
    private String vertexShaderCode =
            "attribute vec4 vPosition;\n" +      //位置
                    "attribute vec2 vCoordinate;\n" +    // 纹理
                    "varying vec2 aCoordinate;\n" +

                    "const float PI = 3.1415926;" +      //  传递纹理   片段着色器
                    "void main(){\n" +
                    "gl_Position = vPosition;" +
                    "aCoordinate=vCoordinate;" +
                    "}";
    private String fragmentShaderCode =
            "precision mediump float;\n" +
                    "uniform sampler2D vTexture;\n" +
                    "varying vec2 aCoordinate;\n" +
                    "uniform float time;" +
                    "void main(){\n" +
                    "    // 一次灵魂出窍效果的时长 0.7\n" +
                    "    float duration = 0.7;\n" +
                    "    // 透明度上限\n" +
                    "    float maxAlpha = 0.4;\n" +
                    "    // 放大图片上限\n" +
                    "    float maxScale = 1.8;\n" +
                    "\n" +
                    "    // 进度值[0,1]\n" +
                    "    float progress = mod(time, duration) / duration; // 0~1\n" +
                    "    // 透明度[0,0.4]\n" +
                    "    float alpha = maxAlpha * (1.0 - progress);\n" +
                    "    // 缩放比例[1.0,1.8]\n" +
                    "    float scale = 1.0 + (maxScale - 1.0) * progress;\n" +
                    "\n" +
                    "    // 放大纹理坐标\n" +
                    "    // 根据放大比例，得到放大纹理坐标 [0,0],[0,1],[1,1],[1,0]\n" +
                    "    float weakX = 0.5 + (aCoordinate.x - 0.5) / scale;\n" +
                    "    float weakY = 0.5 + (aCoordinate.y - 0.5) / scale;\n" +
                    "    // 放大纹理坐标\n" +
                    "    vec2 weakTextureCoords = vec2(weakX, weakY);\n" +
                    "\n" +
                    "    // 获取对应放大纹理坐标下的纹素(颜色值rgba)\n" +
                    "    vec4 weakMask = texture2D(vTexture, weakTextureCoords);\n" +
                    "\n" +
                    "    // 原始的纹理坐标下的纹素(颜色值rgba)\n" +
                    "    vec4 mask = texture2D(vTexture, aCoordinate);\n" +
                    "\n" +
                    "    // 颜色混合 默认颜色混合方程式 = mask * (1.0-alpha) + weakMask * alpha;\n" +
                    "    gl_FragColor = mask * (1.0 - alpha) + weakMask * alpha;" +

                    "}";
//


    private Context context;

    public ImageTexturelinghuichuqiao(Context context) {
        this.context = context;
        ByteBuffer bb = ByteBuffer.allocateDirect(sPos.length * 4);
        bb.order(ByteOrder.nativeOrder());
        bPos = bb.asFloatBuffer();
        bPos.put(sPos);
        bPos.position(0);
        ByteBuffer cc = ByteBuffer.allocateDirect(sCoord.length * 4);
        cc.order(ByteOrder.nativeOrder());
        bCoord = cc.asFloatBuffer();
        bCoord.put(sCoord);
        bCoord.position(0);
    }

//    private int vChangeColor;

    public void preProgram() {

    }


    float dela = 0;
    int ii;
    @Override
    public void render() {
        dela+=0.05F;
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        GLES20.glEnableVertexAttribArray(glHPosition);
        GLES20.glUniform1f(time,dela);

        GLES20.glEnableVertexAttribArray(glHCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,ii);
        GLES20.glUniform1i(glHTexture, 0);
        GLES20.glVertexAttribPointer(glHPosition, 2, GLES20.GL_FLOAT, false, 0, bPos);
        GLES20.glVertexAttribPointer(glHCoordinate, 2, GLES20.GL_FLOAT, false, 0, bCoord);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
    }

    @Override
    public void create() {
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
        glHPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        glHCoordinate = GLES20.glGetAttribLocation(mProgram, "vCoordinate");
        glHTexture = GLES20.glGetUniformLocation(mProgram, "vTexture");
        time = GLES20.glGetUniformLocation(mProgram, "time");
        createTexture();
    }

    @Override
    public void surfaceChange(int width, int height) {
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void dispose() {

    }

    private int createTexture() {
        try {
//            mBitmap = BitmapFactory.decodeStream(context.getAssets().open("texture/fengj.png"));
            mBitmap = BitmapFactory.decodeStream(context.getAssets().open("texture/fengj.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[] texture = new int[1];
        if (mBitmap != null && !mBitmap.isRecycled()) {
            //生成纹理
            GLES20.glGenTextures(1, texture, 0);
            //绑定
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
//            根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            ii = texture[0];
            return texture[0];
        }
        return 0;
    }
}
