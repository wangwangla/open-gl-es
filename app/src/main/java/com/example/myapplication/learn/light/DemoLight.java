package com.example.myapplication.learn.light;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.myapplication.learn.shape.base.BaseGameScreen;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class DemoLight extends BaseGameScreen {
    private static final float r = 0.8f;

    private static final float LIGHT[] = {1.0f, 0f, 5f};
    private static final float CAMERA[] = {0.0f, 0f, 3f};

    private int vCount;
    private Context ctx;
    private FloatBuffer fbVertex;
    private FloatBuffer fbNormal;
    private FloatBuffer fbLight;
    private FloatBuffer fbCamera;
    static float[] mMMatrix = new float[16];
    int mProgram;   // 自定义渲染管线程序id

    int muMVPMatrixHandle;// 总变换矩阵引用id
    int maPositionHandle; // 顶点位置属性引用id
    int maNormalHandle; //顶点法向量属性引用
    int maLightLocationHandle;//光源位置属性引用
    int maCameraHandle;//相机位置属性引用

    public static float[] mProjMatrix = new float[16];// 4x4矩阵 投影用
    public static float[] mVMatrix = new float[16];// 摄像机位置朝向9参数矩阵
    public static float[] mMVPMatrix;// 最后起作用的总变换矩阵

    public DemoLight(Context ctx) {
        super();
        this.ctx = ctx;
    }

    @Override
    public void create() {
        onSurfaceCreated();
    }

    public void onSurfaceCreated() {
        GLES20.glClearColor(0f, 0f, 0f, 1.0f);

        initVertex();

        initShader();

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
    }

    public void onSurfaceChanged(int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 80);

        Matrix.setLookAtM(mVMatrix, 0, CAMERA[0], CAMERA[1], CAMERA[2], 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    public void onDrawFrame() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        draw();
    }

    public void draw() {
        GLES20.glUseProgram(mProgram);
        Matrix.setRotateM(mMMatrix, 0, 0, 0, 1, 0);

        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        GLES20.glUniform3fv(maLightLocationHandle, 1, fbLight);
        //TODO 添加相机位置
        GLES20.glUniform3fv(maCameraHandle, 1, fbCamera);

        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, fbVertex);
        GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, fbNormal);

        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maNormalHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
    }

    //初始化数据
    private void initVertex() {
        ArrayList<Float> alVertix = new ArrayList<Float>();
        final int angleSpan = 10;// 将球进行单位切分的角度
        for (int vAngle = -90; vAngle < 90; vAngle = vAngle + angleSpan)// 垂直方向angleSpan度一份
        {
            for (int hAngle = 0; hAngle <= 360; hAngle = hAngle + angleSpan)// 水平方向angleSpan度一份
            {
                float x0 = (float) (r * Math.cos(Math.toRadians(vAngle)) * Math.cos(Math.toRadians(hAngle)));
                float y0 = (float) (r * Math.cos(Math.toRadians(vAngle)) * Math.sin(Math.toRadians(hAngle)));
                float z0 = (float) (r * Math.sin(Math.toRadians(vAngle)));

                float x1 = (float) (r * Math.cos(Math.toRadians(vAngle)) * Math.cos(Math.toRadians(hAngle + angleSpan)));
                float y1 = (float) (r * Math.cos(Math.toRadians(vAngle)) * Math.sin(Math.toRadians(hAngle + angleSpan)));
                float z1 = (float) (r * Math.sin(Math.toRadians(vAngle)));

                float x2 = (float) (r * Math.cos(Math.toRadians(vAngle + angleSpan)) * Math.cos(Math.toRadians(hAngle + angleSpan)));
                float y2 = (float) (r * Math.cos(Math.toRadians(vAngle + angleSpan)) * Math.sin(Math.toRadians(hAngle + angleSpan)));
                float z2 = (float) (r * Math.sin(Math.toRadians(vAngle + angleSpan)));

                float x3 = (float) (r * Math.cos(Math.toRadians(vAngle + angleSpan)) * Math.cos(Math.toRadians(hAngle)));
                float y3 = (float) (r * Math.cos(Math.toRadians(vAngle + angleSpan)) * Math.sin(Math.toRadians(hAngle)));
                float z3 = (float) (r * Math.sin(Math.toRadians(vAngle + angleSpan)));

                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);
                alVertix.add(x0);
                alVertix.add(y0);
                alVertix.add(z0);

                alVertix.add(x1);
                alVertix.add(y1);
                alVertix.add(z1);
                alVertix.add(x2);
                alVertix.add(y2);
                alVertix.add(z2);
                alVertix.add(x3);
                alVertix.add(y3);
                alVertix.add(z3);

            }
        }

        vCount = alVertix.size() / 3;
        float vertices[] = new float[alVertix.size()];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = alVertix.get(i);
        }

        ByteBuffer bbv = ByteBuffer.allocateDirect(vertices.length * 4);
        bbv.order(ByteOrder.nativeOrder());
        fbVertex = bbv.asFloatBuffer();
        fbVertex.put(vertices);
        fbVertex.position(0);

        ByteBuffer bbn = ByteBuffer.allocateDirect(vertices.length * 4);
        bbn.order(ByteOrder.nativeOrder());
        fbNormal = bbn.asFloatBuffer();
        fbNormal.put(vertices);
        fbNormal.position(0);

        ByteBuffer bbl = ByteBuffer.allocateDirect(LIGHT.length * 4);
        bbl.order(ByteOrder.nativeOrder());
        fbLight = bbl.asFloatBuffer();
        fbLight.put(LIGHT);
        fbLight.position(0);

        ByteBuffer bbc = ByteBuffer.allocateDirect(CAMERA.length * 4);
        bbc.order(ByteOrder.nativeOrder());
        fbCamera = bbc.asFloatBuffer();
        fbCamera.put(CAMERA);
        fbCamera.position(0);
    }

    //初始化shader
    private void initShader() {
        String vertex = "uniform mat4 uMVPMatrix;        //总变换矩阵\n" +
                "\n" +
                "uniform vec3 uCamera;           //摄像机位置\n" +
                "uniform vec3 uLightLocation;    //光源位置\n" +
                "attribute vec3 aPosition;       //顶点位置\n" +
                "attribute vec3 aNormal;         //顶点法向量\n" +
                "\n" +
                "varying vec4 vSpecular;         //用于传递给片元着色器的镜面光分量\n" +
                "\n" +
                "void main()     \n" +
                "{\n" +
                "    float shininess=30.0;               //粗糙度，越小越光滑\n" +
                "    vec4 vAmbient = vec4(0.7, 0.7, 0.7, 1.0);       //设置镜面光强度\n" +
                "\n" +
                "    gl_Position = uMVPMatrix * vec4(aPosition,1);\n" +
                "\n" +
                "    //求出法向量\n" +
                "    vec3 normalTarget=aPosition+aNormal;\n" +
                "    vec3 newNormal=(vec4(normalTarget,1)).xyz-(vec4(aPosition,1)).xyz;\n" +
                "    newNormal=normalize(newNormal);                     //向量规格化\n" +
                "\n" +
                "    //计算从表面点到摄像机的向量\n" +
                "    vec3 eye= uCamera-(vec4(aPosition,1)).xyz;\n" +
                "    eye=normalize(eye);\n" +
                "\n" +
                "    //计算从表面点到光源位置的向量\n" +
                "    vec3 vp= normalize(uLightLocation-(vec4(aPosition,1)).xyz);\n" +
                "    vp=normalize(vp);\n" +
                "\n" +
                "    vec3 halfVector=normalize(vp+eye);  //求视线与光线的半向量\n" +
                "    float nDotViewHalfVector=dot(newNormal,halfVector);         //法线与半向量的点积\n" +
                "    float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess));\n" +
                "\n" +
                "    vSpecular=vAmbient*powerFactor;             //计算镜面光的最终强度\n" +
                "}\n";
        String ssx= "uniform mat4 uMVPMatrix;        //总变换矩阵\n" +
                "\n" +
                "uniform vec3 uCamera;           //摄像机位置\n" +
                "uniform vec3 uLightLocation;    //光源位置\n" +
                "attribute vec3 aPosition;       //顶点位置\n" +
                "attribute vec3 aNormal;         //顶点法向量\n" +
                "\n" +
                "varying vec4 vSpecular;         //用于传递给片元着色器的镜面光分量\n" +
                "varying vec4 vDiffuse;          //用于传递给片元着色器的散射光分量\n" +
                "varying vec4 vAmbient;          //用于传递给片元着色器的环境光分量\n" +
                "\n" +
                "void main()     \n" +
                "{\n" +
                "    float shininess=20.0;               //粗糙度，越小越光滑\n" +
                "    vec4 initAmbient = vec4(0.2, 0.2, 0.2, 1.0);       //设置环境光强度\n" +
                "    vec4 initDiffuse = vec4(0.8, 0.8, 0.8, 1.0);       //设置散射光强度\n" +
                "    vec4 initSpecular = vec4(0.5, 0.5, 0.5, 1.0);      //设置镜面光强度\n" +
                "\n" +
                "    vAmbient=initAmbient;\n" +
                "\n" +
                "    gl_Position = uMVPMatrix * vec4(aPosition,1);\n" +
                "\n" +
                "    //求出法向量\n" +
                "    vec3 normalTarget=aPosition+aNormal;\n" +
                "    vec3 newNormal=(vec4(normalTarget,1)).xyz-(vec4(aPosition,1)).xyz;\n" +
                "    newNormal=normalize(newNormal);                     //向量规格化\n" +
                "\n" +
                "    //计算从表面点到摄像机的向量\n" +
                "    vec3 eye= uCamera-(vec4(aPosition,1)).xyz;\n" +
                "    eye=normalize(eye);\n" +
                "\n" +
                "    //计算从表面点到光源位置的向量\n" +
                "    vec3 vp= normalize(uLightLocation-(vec4(aPosition,1)).xyz);\n" +
                "    vp=normalize(vp);\n" +
                "\n" +
                "    //计算散射光\n" +
                "    float nDotViewPosition=max(0.0, dot(newNormal,vp));\n" +
                "    vDiffuse=initDiffuse*nDotViewPosition;\n" +
                "\n" +
                "    //计算镜面光\n" +
                "    vec3 halfVector=normalize(vp+eye);  //求视线与光线的半向量\n" +
                "    float nDotViewHalfVector=dot(newNormal,halfVector);         //法线与半向量的点积\n" +
                "    float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess));\n" +
                "    vSpecular=initSpecular*powerFactor;             //计算镜面光的最终强度\n" +
                "}\n";
        String xxxc = "uniform mat4 uMVPMatrix;        //总变换矩阵\n" +
                "\n" +
                "uniform vec3 uCamera;           //摄像机位置\n" +
                "uniform vec3 uLightLocation;    //光源位置\n" +
                "attribute vec3 aPosition;       //顶点位置\n" +
                "attribute vec3 aNormal;         //顶点法向量\n" +
                "\n" +
                "varying vec4 vSpecular;         //用于传递给片元着色器的镜面光分量\n" +
                "varying vec4 vDiffuse;          //用于传递给片元着色器的散射光分量\n" +
                "varying vec4 vAmbient;          //用于传递给片元着色器的环境光分量\n" +
                "\n" +
                "void main()     \n" +
                "{\n" +
                "    float shininess=20.0;               //粗糙度，越小越光滑\n" +
                "    vec4 initAmbient = vec4(0.2, 0.2, 0.2, 1.0);       //设置环境光强度\n" +
                "    vec4 initDiffuse = vec4(0.8, 0.8, 0.8, 1.0);       //设置散射光强度\n" +
                "    vec4 initSpecular = vec4(0.5, 0.5, 0.5, 1.0);      //设置镜面光强度\n" +
                "\n" +
                "    vAmbient=initAmbient;\n" +
                "\n" +
                "    gl_Position = uMVPMatrix * vec4(aPosition,1);\n" +
                "\n" +
                "    //求出法向量\n" +
                "    vec3 normalTarget=aPosition+aNormal;\n" +
                "    vec3 newNormal=(vec4(normalTarget,1)).xyz-(vec4(aPosition,1)).xyz;\n" +
                "    newNormal=normalize(newNormal);                     //向量规格化\n" +
                "\n" +
                "    //计算从表面点到摄像机的向量\n" +
                "    vec3 eye= uCamera-(vec4(aPosition,1)).xyz;\n" +
                "    eye=normalize(eye);\n" +
                "\n" +
                "    //计算从表面点到光源位置的向量\n" +
                "    vec3 vp= normalize(uLightLocation-(vec4(aPosition,1)).xyz);\n" +
                "    vp=normalize(vp);\n" +
                "\n" +
                "    //计算散射光\n" +
                "    float nDotViewPosition=max(0.0, dot(newNormal,vp));\n" +
                "    vDiffuse=initDiffuse*nDotViewPosition;\n" +
                "\n" +
                "    //计算镜面光\n" +
                "    vec3 halfVector=normalize(vp+eye);  //求视线与光线的半向量\n" +
                "    float nDotViewHalfVector=dot(newNormal,halfVector);         //法线与半向量的点积\n" +
                "    float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess));\n" +
                "    vSpecular=initSpecular*powerFactor;             //计算镜面光的最终强度\n" +
                "}\n";
        String shader = "precision mediump float;\n" +
                "\n" +
                "varying vec4 vSpecular;                         //用于传递给片元着色器的镜面光分量\n" +
                "\n" +
                "void main()                         \n" +
                "{\n" +
                "    vec4 vFinalColor = vec4(1.0, 0.0, 1.0, 0.0);\n" +
                "\n" +
                "    gl_FragColor = vFinalColor * vSpecular;  //通过镜面光分量获得最终颜色\n" +
                "}\n";

        int verS = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        if (verS != 0) {
            GLES20.glShaderSource(verS, vertex);
            GLES20.glCompileShader(verS);
        }

        int fragS = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        if (fragS != 0) {
            GLES20.glShaderSource(fragS, shader);
            GLES20.glCompileShader(fragS);
        }
        mProgram = GLES20.glCreateProgram();
        if (mProgram != 0) {
            GLES20.glAttachShader(mProgram, verS);
            GLES20.glAttachShader(mProgram, fragS);
            GLES20.glLinkProgram(mProgram);
        }

        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        maNormalHandle= GLES20.glGetAttribLocation(mProgram, "aNormal");
        maLightLocationHandle=GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        //TODO 添加相机引用
        maCameraHandle=GLES20.glGetUniformLocation(mProgram, "uCamera");
    }

    //将sh文件加载进来
    private String loadSH(String fname) {
        String result = null;
        try {
            InputStream in = ctx.getAssets().open(fname);
            int ch = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((ch = in.read()) != -1) {
                baos.write(ch);
            }
            byte[] buff = baos.toByteArray();
            baos.close();
            in.close();
            result = new String(buff, "UTF-8");
            result = result.replaceAll("\\r\\n", "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void render() {
        onDrawFrame();
    }

    @Override
    public void surfaceChange(int width, int height) {
        onSurfaceChanged(width,height);
    }

    @Override
    public void dispose() {

    }
}