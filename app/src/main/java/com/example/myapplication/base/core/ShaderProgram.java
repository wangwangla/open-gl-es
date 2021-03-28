package com.example.myapplication.base.core;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class ShaderProgram {
    public static final String POSITION_ATTRIBUTE = "a_position";
    /** default name for normal attributes **/
    public static final String NORMAL_ATTRIBUTE = "a_normal";
    /** default name for color attributes **/
    public static final String COLOR_ATTRIBUTE = "a_color";
    /** default name for texcoords attributes, append texture unit number **/
    public static final String TEXCOORD_ATTRIBUTE = "a_texCoord";
    /** default name for tangent attribute **/
    public static final String TANGENT_ATTRIBUTE = "a_tangent";
    /** default name for binormal attribute **/
    public static final String BINORMAL_ATTRIBUTE = "a_binormal";
    /** default name for boneweight attribute **/
    public static final String BONEWEIGHT_ATTRIBUTE = "a_boneWeight";

    public ShaderProgram (String vertexShader, String fragmentShader) {
        if (vertexShader == null) throw new IllegalArgumentException("vertex shader must not be null");
        if (fragmentShader == null) throw new IllegalArgumentException("fragment shader must not be null");

        compileShaders(vertexShader, fragmentShader);
     }

    int program;
    int vertexShaderHandle;
    int fragmentShaderHandle;

    private void compileShaders (String vertexShader, String fragmentShader) {
        vertexShaderHandle = loadShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        fragmentShaderHandle = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
        program = linkProgram(GLES20.glCreateProgram());
    }

    private int linkProgram(int program) {
        GLES20.glAttachShader(program, vertexShaderHandle);
        GLES20.glAttachShader(program, fragmentShaderHandle);
        GLES20.glLinkProgram(program);

        ByteBuffer tmp = ByteBuffer.allocateDirect(4);
        tmp.order(ByteOrder.nativeOrder());
        IntBuffer intbuf = tmp.asIntBuffer();

        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, intbuf);
        int linked = intbuf.get(0);
        return program;
    }

    private int loadShader (int type, String source) {

        ByteBuffer buffer = ByteBuffer.allocateDirect(4);
        buffer.order(ByteOrder.nativeOrder());


        IntBuffer intbuf = buffer.asIntBuffer();

        int shader = GLES20.glCreateShader(type);
        if (shader == 0) return -1;

        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, intbuf);
        return shader;
    }

}
