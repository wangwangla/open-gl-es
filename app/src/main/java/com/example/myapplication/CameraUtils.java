package com.example.myapplication;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraUtils {
    public Camera camera;
    public static int cameraId = 0;
    public static int orientation = 0;
    public SurfaceView surfaceView;
    public int fitWidth;
    public int fitHeight;

    public CameraUtils(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }

    public void startPreview(SurfaceTexture texture0) {
        try {
            camera.setPreviewTexture(texture0);
            camera.startPreview();
        } catch (IOException e) {
            Log.v("glcamera",e.getMessage());
        }
    }


    /**
     * 打开相机
     */
    public void openCamera(int mCameraId0){
        try {
            cameraId = mCameraId0;
            camera = Camera.open(mCameraId0);
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.getSupportedFocusModes().contains(
                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            //1.设置预览尺寸，防止预览画面变形
            List<Camera.Size> sizes1 = parameters.getSupportedPreviewSizes(); //得到的比例，宽是大头
            int[] result1 = getOptimalSize(sizes1, surfaceView.getWidth(), surfaceView.getHeight());
            parameters.setPreviewSize(result1[0], result1[1]);
            fitWidth = result1[0];
            fitHeight = result1[1];
            //2.设置拍照取得的图片尺寸
            List<Camera.Size> sizes2 = parameters.getSupportedPictureSizes();
            int[] result2 = getOptimalSize(sizes2,surfaceView.getWidth(),surfaceView.getHeight());
            parameters.setPictureSize(result2[0],result2[1]);
            //3.得到video尺寸，传给mediarecorder
            camera.setParameters(parameters);
            //设置相机方向
            setCameraDisplayOrientation(cameraId);
        }catch (Exception e){
            Log.v("aaaaa",e.getMessage());
        }
    }

    public void startPreview(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放相机
     */
    public void releaseCamera() {
        if (camera !=null){
            camera.stopPreview();
            //释放向机
            camera.release();
            camera = null;
        }
    }

    /**
     * *找出最接近的尺寸，以保证图像不会被拉伸
     * @param sizes
     * @param currentWidth
     * @param currentHeight
     * @return
     */
    private int[] getOptimalSize(List<Camera.Size> sizes, int currentWidth, int currentHeight) {
        int i = 1;
        //大头
        int bestWidth = sizes.get(0).width;
        //小头
        int bestHeight = sizes.get(0).height;
        //很重要，第一项一定是高/宽
        float min = Math.abs((float) bestHeight / (float) bestWidth - (float) currentWidth / (float) currentHeight);
        while (i < sizes.size()) {
            float current = Math.abs((float) sizes.get(i).height / (float) sizes.get(i).width - (float) currentWidth / (float) currentHeight);
            if (current < min) {
                min = current;
                bestWidth = sizes.get(i).width;
                bestHeight = sizes.get(i).height;
            }
            i++;
        }
        int[] result = new int[2];
        result[0] = bestWidth;
        result[1] = bestHeight;
        Log.v("glcamera", bestWidth + "//" + bestHeight);
        return result;
    }

    /**
     * 根据手机屏幕以及前后摄来调整相机角度
     *
     * @param cameraId
     */
    private void setCameraDisplayOrientation(int cameraId) {
        Activity targetActivity = (Activity) surfaceView.getContext();
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = targetActivity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
        orientation = result;
    }
}
