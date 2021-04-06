package me.felixzhang.example.my_particles2.programs;

import android.content.Context;

import com.example.myapplication.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by felix on 15/5/19.
 */
public class ParticlesShaderProgram extends ShaderProgram {

    private final int uMatrixLocation;
    private final int uTimeLocation;

    private final int aPositionLocation;
    private final int aColorLocation;
    private final int aDirectionVectorLocation;
    private final int aParticleStartTimeLocation;


    public ParticlesShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTimeLocation = glGetUniformLocation(program, U_TIME);

        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation = glGetAttribLocation(program, A_COLOR);
        aDirectionVectorLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR);
        aParticleStartTimeLocation = glGetAttribLocation(program, A_PARTICLE_START_TIME);

    }

    public void setUniforms(float[] matrix, float elapsedTime) {
        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        glUniform1f(uTimeLocation,elapsedTime);
    }

    public int getaPositionLocation() {
        return aPositionLocation;
    }

    public int getaColorLocation() {
        return aColorLocation;
    }

    public int getaDirectionVectorLocation() {
        return aDirectionVectorLocation;
    }

    public int getaParticleStartTimeLocation() {
        return aParticleStartTimeLocation;
    }


}
