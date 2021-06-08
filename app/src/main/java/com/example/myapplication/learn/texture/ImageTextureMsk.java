package com.example.myapplication.learn.texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.example.myapplication.learn.shape.base.BaseGameScreen;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 四宫格
 */
public class ImageTextureMsk extends BaseGameScreen {
    private int mProgram;
    private int glHPosition;
    private int glHTexture;
    private int glHCoordinate;
    private int vMatrix;
    private Bitmap mBitmap;
    private FloatBuffer bPos;
    private FloatBuffer bCoord;
    //相机位置
    private float[] mViewMatrix=new float[16];
    //透视
    private float[] mProjectMatrix=new float[16];
    //变换矩阵
    private float[] mMVPMatrix=new float[16];

    private final float[] sPos={
            -1.0f,1.0f,
            -1.0f,-1.0f,
            1.0f,1.0f,
            1.0f,-1.0f
    };
    static float yy = 1f;
    static float sCoord[] = {
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f,
    };
//    private final float[] sCoord={
//            0.0f,0.0f,
//            0.0f,1.0f,
//            1.0f,0.0f,
//            1.0f,1.0f,
//    };
    private String vertexShaderCode =
            "attribute vec4 vPosition;\n" +      //位置
                    "attribute vec2 vCoordinate;\n" +    // 纹理
                    "varying vec2 aCoordinate;\n" +
                    "uniform mat4 vMatrix;" +      //  传递纹理   片段着色器
                    "void main(){\n" +
                    "    gl_Position=vPosition * vMatrix;\n" +
                    "    aCoordinate=vCoordinate;\n" +
                    "}";
    /**
     * 灰色
     */
    private String fragmentShaderCode1 =
            "precision mediump float;\n" +
            "uniform sampler2D vTexture;\n" +
            "varying vec2 aCoordinate;\n" +
                    "vec3 W = vec3(0.2125,0.7154,0.0721);" +
            "void main(){\n" +
                    "vec2 uv = aCoordinate;" +
            "       if(uv.x <= 0.5){" +
                    "   uv.x =uv.x * 2.0;" +
                    "}else{" +
                    "   uv.x = (uv.x - 0.5)*2.0;" +
                    "}" +
                    "if(uv.y <= 0.5){" +
                    "   uv.y = uv.y * 2.0;" +
                    "}else {" +
                    "   uv.y = (uv.y - 0.5) * 2.0;" +
                    "}" +
                    "vec4 mask = texture2D(vTexture,uv);" +
                    "float luminance = dot(mask.rgb,W);" +
//                    "float c = (nColor.r * 299 + nColor.g * 587 + nColor.b * 114 + 500) / 1000;" +
//                "float c = (nColor.r + nColor.g + nColor.b) / 3.0F;" +
            "    gl_FragColor=vec4(vec3(luminance),0.1);" +
            "}";

    private String fragmentShaderCode2 ="precision highp float;\n" +
            "uniform sampler2D vTexture;\n" +
            "const vec2 TextSize = vec2(400.0,400.0);\n" +
            "const vec2 MosaicSize = vec2(10.0,10.0);\n" +
            "varying vec2 aCoordinate;\n" +
            "" +
//            "uniform sampler2D vTexture;\n" +
//            "varying vec2 aCoordinate;\n" +
            "\n" +
            "void main (void) {\n" +
            "    vec2 intXY = vec2(aCoordinate.x * TextSize.x , aCoordinate.y * TextSize.y);\n" +
            "    vec2 XYMosaic = vec2(floor(intXY.x/MosaicSize.x) * MosaicSize.x , " +
            "                           floor(intXY.y/MosaicSize.y) * MosaicSize.y);\n" +
            "    vec2 UVMosaic = vec2(XYMosaic.x / TextSize.x , XYMosaic.y / TextSize.y);\n" +
            "    vec4 mask = texture2D(vTexture, UVMosaic);\n" +
            "    gl_FragColor = mask;\n" +
            "}";

    private String fragmentShaderCode = "precision highp float;\n" +
            "uniform sampler2D vTexture;\n" +
            "varying vec2 aCoordinate;\n" +
            "const float mosaicSize = 0.03;\n" +
            "\n" +
            "void main (void) {\n" +
            "    float length = mosaicSize;\n" +
            "    const float PI6 = 0.523599;\n" +
            "    float TR = 0.866025;\n" +
            "    float TB = 1.5;\n" +
            "    \n" +
            "    float x = aCoordinate.x;\n" +
            "    float y = aCoordinate.y;\n" +
            "    \n" +
            "    int wx = int(x / TB / length);\n" +
            "    int wy = int(y / TR / length);\n" +
            "    vec2 v1, v2, vn;\n" +
            "    \n" +
            "    if (wx/2 * 2 == wx) {\n" +
            "        if (wy/2 * 2 == wy) {\n" +
            "            //(0,0),(1,1)\n" +
            "            v1 = vec2(length * 1.5 * float(wx), length * TR * float(wy));\n" +
            "            v2 = vec2(length * 1.5 * float(wx + 1), length * TR * float(wy + 1));\n" +
            "        } else {\n" +
            "            //(0,1),(1,0)\n" +
            "            v1 = vec2(length * 1.5 * float(wx), length * TR * float(wy + 1));\n" +
            "            v2 = vec2(length * 1.5 * float(wx + 1), length * TR * float(wy));\n" +
            "        }\n" +
            "    }else {\n" +
            "        if (wy/2 * 2 == wy) {\n" +
            "            //(0,1),(1,0)\n" +
            "            v1 = vec2(length * 1.5 * float(wx), length * TR * float(wy + 1));\n" +
            "            v2 = vec2(length * 1.5 * float(wx + 1), length * TR * float(wy));\n" +
            "        } else {\n" +
            "            //(0,0),(1,1)\n" +
            "            v1 = vec2(length * 1.5 * float(wx), length * TR * float(wy));\n" +
            "            v2 = vec2(length * 1.5 * float(wx + 1), length * TR * float(wy + 1));\n" +
            "        }\n" +
            "    }\n" +
            "    \n" +
            "    float s1 = sqrt(pow(v1.x - x, 2.0) + pow(v1.y - y, 2.0));\n" +
            "    float s2 = sqrt(pow(v2.x - x, 2.0) + pow(v2.y - y, 2.0));\n" +
            "    if (s1 < s2) {\n" +
            "        vn = v1;\n" +
            "    } else {\n" +
            "        vn = v2;\n" +
            "    }\n" +
            "    \n" +
            "//    vec4 mid = texture2D(Texture, vn);\n" +
            "    float a = atan((x - vn.x)/(y - vn.y));\n" +
            "    vec2 area1 = vec2(vn.x, vn.y - mosaicSize * TR / 2.0);\n" +
            "    vec2 area2 = vec2(vn.x + mosaicSize / 2.0, vn.y - mosaicSize * TR / 2.0);\n" +
            "    vec2 area3 = vec2(vn.x + mosaicSize / 2.0, vn.y + mosaicSize * TR / 2.0);\n" +
            "    vec2 area4 = vec2(vn.x, vn.y + mosaicSize * TR / 2.0);\n" +
            "    vec2 area5 = vec2(vn.x - mosaicSize / 2.0, vn.y + mosaicSize * TR / 2.0);\n" +
            "    vec2 area6 = vec2(vn.x - mosaicSize / 2.0, vn.y - mosaicSize * TR / 2.0);\n" +
            "    \n" +
            "    if (a >= PI6 && a < PI6 * 3.0) {\n" +
            "        vn = area1;\n" +
            "    } else if (a >= PI6 * 3.0 && a < PI6 * 5.0) {\n" +
            "        vn = area2;\n" +
            "    } else if ((a >= PI6 * 5.0 && a <= PI6 * 6.0)|| (a<-PI6 * 5.0 && a>-PI6*6.0)) {\n" +
            "        vn = area3;\n" +
            "    } else if (a < -PI6 * 3.0 && a >= -PI6 * 5.0) {\n" +
            "        vn = area4;\n" +
            "    } else if(a <= -PI6 && a> -PI6 * 3.0) {\n" +
            "        vn = area5;\n" +
            "    } else if (a > -PI6 && a < PI6)\n" +
            "    {\n" +
            "        vn = area6;\n" +
            "    }\n" +
            "      \n" +
            "    vec4 color = texture2D(vTexture, vn);\n" +
            "    gl_FragColor = color;\n" +
            "}";
//            "precision highp float;\n" +
//            "uniform sampler2D vTexture;\n" +
//            "varying vec2 aCoordinate;\n" +
//            "const float mosaicSize = 0.03;\n" +
//            "\n" +
//            "void main (void) {\n" +
//            "    float length = mosaicSize;\n" +
//            "    float TR = 0.866025;\n" +
//            "    float TB = 1.5;\n" +
//            "    \n" +
//            "    float x = aCoordinate.x;\n" +
//            "    float y = aCoordinate.y;\n" +
//            "    \n" +
//            "    int wx = int(x / TB / length);\n" +
//            "    int wy = int(y / TR / length);\n" +
//            "    vec2 v1, v2, vn;\n" +
//            "    \n" +
//            "    if (wx/2 * 2 == wx) {\n" +
//            "           if (wy/2 * 2 == wy) {\n" +
//            "               //(0,0),(1,1)\n" +
//            "               v1 = vec2(length * 1.5 * float(wx), length * TR * float(wy));\n" +
//            "               v2 = vec2(length * 1.5 * float(wx + 1), length * TR * float(wy + 1));\n" +
//            "           } else {\n" +
//            "               //(0,1),(1,0)\n" +
//            "               v1 = vec2(length * 1.5 * float(wx), length * TR * float(wy + 1));\n" +
//            "               v2 = vec2(length * 1.5 * float(wx + 1), length * TR * float(wy));\n" +
//            "           }\n" +
//            "       }else {\n" +
//            "           if (wy/2 * 2 == wy) {\n" +
//            "               //(0,1),(1,0)\n" +
//            "               v1 = vec2(length * 1.5 * float(wx), length * TR * float(wy + 1));\n" +
//            "               v2 = vec2(length * 1.5 * float(wx + 1), length * TR * float(wy));\n" +
//            "           } else {\n" +
//            "               //(0,0),(1,1)\n" +
//            "               v1 = vec2(length * 1.5 * float(wx), length * TR * float(wy));\n" +
//            "               v2 = vec2(length * 1.5 * float(wx + 1), length * TR * float(wy + 1));\n" +
//            "           }\n" +
//            "       }\n" +
//            "       \n" +
//            "       float s1 = sqrt(pow(v1.x - x, 2.0) + pow(v1.y - y, 2.0));\n" +
//            "       float s2 = sqrt(pow(v2.x - x, 2.0) + pow(v2.y - y, 2.0));\n" +
//            "       if (s1 < s2) {\n" +
//            "           vn = v1;\n" +
//            "       } else {\n" +
//            "           vn = v2;\n" +
//            "       }\n" +
//            "       vec4 color = texture2D(vTexture, vn);\n" +
//            "       \n" +
//            "       gl_FragColor = color;\n" +
//            "}";


    /*uniform sampler2D Texture;
const vec2 TextSize = vec2(400.0,400.0);
const vec2 MosaicSize = vec2(10.0,10.0);
varying vec2 TextureCoordsVarying;

void main (void) {
    vec2 intXY = vec2(TextureCoordsVarying.x * TextSize.x , TextureCoordsVarying.y * TextSize.y);
    vec2 XYMosaic = vec2(floor(intXY.x/MosaicSize.x) * MosaicSize.x , floor(intXY.y/MosaicSize.y) * MosaicSize.y);
    vec2 UVMosaic = vec2(XYMosaic.x / TextSize.x , XYMosaic.y / TextSize.y);
    vec4 mask = texture2D(Texture, UVMosaic);
    gl_FragColor = mask;
}*/
    private Context context;
    public ImageTextureMsk(Context context){
        this.context = context;
        ByteBuffer bb=ByteBuffer.allocateDirect(sPos.length*4);
        bb.order(ByteOrder.nativeOrder());
        bPos=bb.asFloatBuffer();
        bPos.put(sPos);
        bPos.position(0);
        ByteBuffer cc=ByteBuffer.allocateDirect(sCoord.length*4);
        cc.order(ByteOrder.nativeOrder());
        bCoord=cc.asFloatBuffer();
        bCoord.put(sCoord);
        bCoord.position(0);
    }

//    private int vChangeColor;

    public void preProgram(){
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    private int createTexture(){
        int[] texture=new int[1];
        if(mBitmap!=null&&!mBitmap.isRecycled()){
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0];
        }
        return 0;
    }

    @Override
    public void render() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);

//        GLES20.glUniform4fv(vChangeColor,1,new float[]{1,1,1,1},0);

        GLES20.glEnableVertexAttribArray(glHPosition);
        GLES20.glEnableVertexAttribArray(glHCoordinate);
        GLES20.glUniform1i(glHTexture, 0);
        createTexture();
        GLES20.glVertexAttribPointer(glHPosition,2,GLES20.GL_FLOAT,false,0,bPos);
        GLES20.glVertexAttribPointer(glHCoordinate,2,GLES20.GL_FLOAT,false,0,bCoord);
        GLES20.glUniformMatrix4fv(vMatrix,1,false,mMVPMatrix,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4);
    }

    @Override
    public void create() {
        GLES20.glClearColor(1.0f,1.0f,1.0f,1.0f);
        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        preProgram();
        glHPosition=GLES20.glGetAttribLocation(mProgram,"vPosition");
        glHCoordinate=GLES20.glGetAttribLocation(mProgram,"vCoordinate");
        glHTexture=GLES20.glGetUniformLocation(mProgram,"vTexture");
        vMatrix = GLES20.glGetUniformLocation(mProgram,"vMatrix");
        try {
            mBitmap= BitmapFactory.decodeStream(context.getAssets().open("texture/fengj.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChange(int width, int height) {
        float ratio=(float)width/height;
//        设置相机类型
        Matrix.frustumM(mProjectMatrix,0,-ratio,ratio,-1,1,3,7);
//        设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0,
                0, 0, 7.0f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix,0,mProjectMatrix,0,mViewMatrix,0);
    }

    @Override
    public void dispose() {

    }
}
