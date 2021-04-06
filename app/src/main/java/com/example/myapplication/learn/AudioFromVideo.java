package com.example.myapplication.learn;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class AudioFromVideo{
    private String audio,video;

    private MediaCodec amc;

    private MediaExtractor ame;

    private MediaFormat amf;

    private String amime;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public AudioFromVideo(String srcVideo, String destAudio){
        this.audio=destAudio;

        this.video=srcVideo;

        ame=new MediaExtractor();

        init();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void init(){
        try {
            ame.setDataSource(video);

            amf=ame.getTrackFormat(1);

            ame.selectTrack(1);

            amime=amf.getString(MediaFormat.KEY_MIME);

            amc=MediaCodec.createDecoderByType(amime);

            amc.configure(amf, null, null, 0);

            amc.start();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void start(){
        new AudioService(amc,ame,audio).start();

    }

    private class AudioService extends Thread{
        private MediaCodec amc;

        private MediaExtractor ame;

        private ByteBuffer[] aInputBuffers,aOutputBuffers;

        private String destFile;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @SuppressWarnings("deprecation")

        AudioService(MediaCodec amc,MediaExtractor ame,String destFile){
            this.amc=amc;

            this.ame=ame;

            this.destFile=destFile;

            aInputBuffers=amc.getInputBuffers();

            aOutputBuffers=amc.getOutputBuffers();

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @SuppressWarnings("deprecation")

        public void run(){
            try {
                OutputStream os=new FileOutputStream(new File(destFile));

                long count=0;

                while(true){
                    int inputIndex=amc.dequeueInputBuffer(0);

                    if(inputIndex==-1){
                        continue;

                    }

                    int sampleSize=ame.readSampleData(aInputBuffers[inputIndex], 0);

                    if(sampleSize==-1)break;

                    long presentationTime=ame.getSampleTime();

                    int flag=ame.getSampleFlags();

                    ame.advance();

                    amc.queueInputBuffer(inputIndex, 0, sampleSize, presentationTime, flag);

                    MediaCodec.BufferInfo info=new MediaCodec.BufferInfo();

                    int outputIndex=amc.dequeueOutputBuffer(info, 0);

                    if (outputIndex >= 0) {
                        byte[] data=new byte[info.size];

                        aOutputBuffers[outputIndex].get(data, 0, data.length);

                        aOutputBuffers[outputIndex].clear();

                        os.write(data);

                        count+=data.length;

//                        Log.i("write", ""+count);

                        amc.releaseOutputBuffer(outputIndex, false);

                    } else if (outputIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                        aOutputBuffers = amc.getOutputBuffers();

                    } else if (outputIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {}

                }

                os.flush();

                os.close();

            } catch (Exception e) {
                e.printStackTrace();

            }

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void main(String[] args) {
        String path = "C:\\Users\\28188\\Downloads\\Video\\1.mp4";
        String target = "C:\\Users\\28188\\Downloads\\Video\\";
        new AudioFromVideo(path,target);
    }
}
