package com.mygdx.game;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class EcouteurGeste implements GestureDetector.GestureListener {

    private ArrayList<Joueur> joueursList;

    public EcouteurGeste(){
        joueursList = new ArrayList<Joueur>();
    }

    @Override
    public boolean touchDown(float v, float v1, int i, int i1) {
        return false;
    }

    @Override
    public boolean tap(float v, float v1, int i, int i1) {
        return false;
    }

    @Override
    public boolean longPress(float v, float v1) {
        return false;
    }

    @Override
    public boolean fling(float v, float v1, int i) {

        return true;
    }

    @Override
    public boolean pan(float v, float v1, float v2, float v3) {
        for (Joueur joueur: joueursList){
            if (joueur.getZonePad().contains((int)v,(int) v1) ){
                joueur.setMouvementX(v2 * (joueur.getVitesse() * 5));
                joueur.setMouvementY(-v3 * (joueur.getVitesse() * 5));
            }
        }
        return false;
    }

    @Override
    public boolean panStop(float v, float v1, int i, int i1) {
        for (Joueur joueur: joueursList){
            joueur.setMouvementX(0);
            joueur.setMouvementY(0);
        }
        return false;
    }

    @Override
    public boolean zoom(float v, float v1) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 vector2, Vector2 vector21, Vector2 vector22, Vector2 vector23) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    public void addJoueur(Joueur j){
        System.out.println("ADD JOUEUR GESTES !");

        joueursList.add(j);
    }
}
