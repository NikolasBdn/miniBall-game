package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.World;

public class Bot extends Joueur {
    private Balle balle;

    public Bot(World world, String tp, int xp, int yp, Balle b) {
        super(world, tp, xp, yp);
        balle = b;
    }

    @Override
    public void render() {
        super.render();

    }
}
