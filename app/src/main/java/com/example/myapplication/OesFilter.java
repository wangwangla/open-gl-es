package com.example.myapplication;

import android.content.res.Resources;

import com.example.myapplication.learn.framebuffer.demo2.AFilter;


import java.util.Arrays;

import android.content.res.Resources;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

/**
 * Description:
 */
public class OesFilter extends AFilter{

    private int mHCoordMatrix;
    private float[] mCoordMatrix= Arrays.copyOf(OM,16);

    public OesFilter(Resources mRes) {
        super(mRes);
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("shader/oes.sh","shader/oes_f.sh");
        mHCoordMatrix=GLES20.glGetUniformLocation(mProgram,"vCoordMatrix");
    }

    public void setCoordMatrix(float[] matrix){
        this.mCoordMatrix=matrix;
    }

    @Override
    protected void onSetExpandData() {
        super.onSetExpandData();
        GLES20.glUniformMatrix4fv(mHCoordMatrix,1,false,mCoordMatrix,0);
    }

    @Override
    protected void onBindTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+getTextureType());
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,getTextureId());
        GLES20.glUniform1i(mHTexture,getTextureType());
    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }

}
