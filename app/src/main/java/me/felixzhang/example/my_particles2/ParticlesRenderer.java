package me.felixzhang.example.my_particles2;

import android.content.Context;
import android.graphics.Color;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.felixzhang.example.my_particles2.objects.ParticlesShooter;
import me.felixzhang.example.my_particles2.objects.ParticlsSystem;
import me.felixzhang.example.my_particles2.programs.ParticlesShaderProgram;
import me.felixzhang.example.my_particles2.util.MatrixHelper;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLSurfaceView.Renderer;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static me.felixzhang.example.my_particles2.util.Geometry.Point;
import static me.felixzhang.example.my_particles2.util.Geometry.Vector;

/**
 * Created by felix on 15/5/19.
 */
public class ParticlesRenderer implements Renderer {

    private Context context;
    private ParticlesShaderProgram particleProgram;
    private ParticlsSystem particlesSystem;

    private ParticlesShooter redParticleShooter;
    private ParticlesShooter greenParticleShooter;
    private ParticlesShooter blueParticleShooter;

    private float globalStartTime;

    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];

    public ParticlesRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        particleProgram = new ParticlesShaderProgram(context);
        particlesSystem = new ParticlsSystem(10000);
        globalStartTime = System.nanoTime();
        final Vector particleDirection = new Vector(0f, 0.5f, 0f);

        redParticleShooter = new ParticlesShooter(
                new Point(-1f, 0f, 0f),
                particleDirection,
                Color.rgb(255, 50, 5)
        );

        greenParticleShooter = new ParticlesShooter(
                new Point(0f, 0f, 0f),
                particleDirection,
                Color.rgb(25, 255, 25)
        );
        blueParticleShooter = new ParticlesShooter(
                new Point(1f, 0f, 0f),
                particleDirection,
                Color.rgb(5, 50, 255)
        );
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0, 0, width, height);


        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
                / (float) height, 1f, 10f);

        setIdentityM(viewMatrix, 0);
        translateM(viewMatrix, 0, 0f, -1.5f, -5f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0,
                viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);

        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f; //从纳秒转换为秒

        redParticleShooter.addParticles(particlesSystem, currentTime, 5);
        greenParticleShooter.addParticles(particlesSystem, currentTime, 5);
        blueParticleShooter.addParticles(particlesSystem, currentTime, 5);

        particleProgram.useProgram();
        particleProgram.setUniforms(viewProjectionMatrix,currentTime);
        particlesSystem.bindData(particleProgram);
        particlesSystem.draw();
    }
}
