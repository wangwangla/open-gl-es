package com.example.myapplication.learn;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Drawwe {
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +

                    "attribute vec2 inputTextureCoordinate;" +
                    "varying vec2 textureCoordinate;" +
                    "void main()" +
                    "{" +
                    "gl_Position = vPosition;" +
                    "textureCoordinate = inputTextureCoordinate;" +
                    "}";

    /**
     * 相机显示   将坐标系发生了变换,比如xx.x>0.4世界上是y上发生了变化,
     *
     *
     * 下图方式
     * y---------|
     *          |
     *          |
     *          |
     *          x
     */
    private final String fragmentShaderCode =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;" +
                    "varying vec2 textureCoordinate;\n" +
                    "uniform samplerExternalOES s_texture;\n" +
                    "void main() {" +
                    "vec2 tex = textureCoordinate;" +
                    "if((tex.x-0.5)*(tex.x-0.5)+(tex.y-0.5)*(tex.y-0.5)<=0.25){" +
                    "gl_FragColor =texture2D( s_texture, textureCoordinate);" +
                    "}else{" +
                    "gl_FragColor = vec4(1.0,1.0,1.0,1.0);" +
                    "}"+





// step1:
//                    "vec2 xx = textureCoordinate;"+
//                    "if(xx.y>0.4){" +
//                    "gl_FragColor =texture2D( s_texture, textureCoordinate);" +
//                    "}else{" +
//                    "vec4 vCameraColor  = texture2D( s_texture, textureCoordinate);" +
//                    //黑白滤镜
//                    "float fGrayColor = (0.299*vCameraColor.r + 0.587*vCameraColor.g + 0.114*vCameraColor.b);"+
//                    "gl_FragColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);"+
//                    "}"+

//step2:
//                    "gl_FragColor = vec4(1.0,0.0,1.0,1.0);"+
//
//step3:
//                    正确de
//                    "vec4 vCameraColor  =texture2D( s_texture, textureCoordinate );" +
//                    //黑白滤镜
//                    "float fGrayColor = (0.299*vCameraColor.r + 0.587*vCameraColor.g + 0.114*vCameraColor.b);"+
//                    "gl_FragColor = vec4(fGrayColor, fGrayColor, fGrayColor, 1.0);"+
                    "}";
//   "gl_FragColor=texture2D( s_texture, textureCoordinate );" +
    private FloatBuffer vertexBuffer, textureVerticesBuffer;
    private ShortBuffer drawListBuffer;
    int mProgram;
    private int mPositionHandle;
    private int mTextureCoordHandle;

    private short drawOrder[] = {0, 1, 2, 0, 2, 3};

    private static final int COORDS_PER_VERTEX = 2;

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    static float squareCoords[] = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f,
    };

    static float textureVertices[] = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
    };

    private int texture;

    public Drawwe(int texture) {
        this.texture = texture;
        //顶点坐标
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);
        //顶点绘制顺序
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
        //纹理坐标
        ByteBuffer bb2 = ByteBuffer.allocateDirect(textureVertices.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureVerticesBuffer = bb2.asFloatBuffer();
        textureVerticesBuffer.put(textureVertices);
        textureVerticesBuffer.position(0);
        //编译着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    public void draw() {
        GLES20.glUseProgram(mProgram);
        //使用纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture);
        //顶点位置
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        //纹理坐标
        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, textureVerticesBuffer);
        //绘制
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        //结束
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordHandle);
    }

    //编译着色器
    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}