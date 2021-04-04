package me.felixzhang.example.my_particles2.objects;

import static me.felixzhang.example.my_particles2.util.Geometry.Vector;
import static me.felixzhang.example.my_particles2.util.Geometry.Point;

/**
 * Created by felix on 15/5/19.
 */
public class ParticlesShooter {

    //确定粒子发射器的位置，方向和颜色
    private final Point position;
    private final Vector direction;
    private final int color;

    public ParticlesShooter(Point position, Vector direction, int color) {
        this.position = position;
        this.direction = direction;
        this.color = color;
    }

    public void addParticles(ParticlsSystem particlsSystem, float currentTime, int count) {

        for (int i = 0; i < count; i++) {
            particlsSystem.addParticles(position,color,direction,currentTime);
        }
    }
}
