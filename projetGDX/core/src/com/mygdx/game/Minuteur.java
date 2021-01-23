package com.mygdx.game;

import com.badlogic.gdx.utils.Timer;

public class Minuteur extends Timer.Task {
    private final int temps;
    private int count;

    public Minuteur(int t){
        temps = t;
        count = t;
    }

    public void recommencer(){
        count = temps;
    }

    @Override
    public void run() {
        count--;
    }

    public int getCount() {
        return count;
    }

    public int getTemps() {
        return temps;
    }
}
