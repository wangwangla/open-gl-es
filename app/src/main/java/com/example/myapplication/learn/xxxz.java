package com.example.myapplication.learn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class xxxz {
    public static List<String> L=new ArrayList<>();
    //获取一个文件夹路径dir下所有的子目录地址（子目录不是文件夹形式）
    public static void listDirectory(File dir) {
        if (!dir.exists()) {
            throw new IllegalArgumentException("目录" + dir + "不存在");
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + "不是目录");
        }

        File[] files = dir.listFiles();// 返回的是直接子目录的抽象
        if (files != null && files.length > 0) {
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    listDirectory(file1);
                } else {
                    L.add(file1.toString());
                }
            }
        }
    }

    private static boolean processMP3(String path) {
        String voiceName = path.substring(path.lastIndexOf("\\") + 1,
                path.lastIndexOf("\\") + 3);
        System.out.println(voiceName);
        switch (voiceName) {
            case "愤怒":
                ffmpeg_process(voiceName, path);
                break;
            case "伤心":
                ffmpeg_process(voiceName, path);
                break;
            case "惊讶":
                ffmpeg_process(voiceName, path);
                break;
            case "快乐":
                ffmpeg_process(voiceName, path);
                break;
            case "恐惧":
                ffmpeg_process(voiceName, path);
                break;
            case "厌恶":
                ffmpeg_process(voiceName, path);
                break;
            case "自然":
                ffmpeg_process(voiceName, path);
                break;
            default:
                break;
        }

        return true;
    }

    public static boolean ffmpeg_process(String Name, String path) {
        String voiceNameNum = path.substring(path.lastIndexOf("\\") + 1,
                path.indexOf("."));
        String voiceName = path.substring(path.lastIndexOf("\\") + 1,
                path.lastIndexOf("\\") +3);
        String number = voiceNameNum.replaceAll(voiceName, "");
        System.out.println(number);
        int num = Integer.parseInt(number);
        System.out.println(num);
        List<String> commend = new java.util.ArrayList<String>();
        commend.add("ffmpeg.exe");
        commend.add("-i");
        commend.add(path);
        commend.add("-vn");
        commend.add("D:\\迅雷下载\\多模态资源\\视频中截取的音频" +"\\"+ Name + "\\" + Name + num
                + ".mp3");
        System.out.println(commend);
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            builder.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        listDirectory(new File("D:\\迅雷下载\\多模态资源\\人脸表情"));
        System.out.println(L.size());
        for (int i = 0; i < L.size(); i++) {
            System.out.println(L.get(i));
            processMP3(L.get(i));
        }
//      processMP3("D:\\迅雷下载\\多模态资源\\人脸表情\\伤心\\伤心10.avi");
    }

    public void Mp4(){

    }
}
