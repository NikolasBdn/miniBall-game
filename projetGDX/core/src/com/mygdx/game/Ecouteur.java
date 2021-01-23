package com.mygdx.game;

import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;

public class Ecouteur implements InputProcessor {

    private MyGdxGame game;
    private ArrayList<Joueur> joueursList;
    private  Rectangle btQuitter;

    public Ecouteur(MyGdxGame g, Rectangle btQ){
        game = g;
        joueursList = new ArrayList<Joueur>();
        btQuitter = btQ;
    }

    @Override
    public boolean keyDown(int i) {
        for (Joueur joueur: joueursList) {
        //Utilisation d'un switch impossible car les var commandeUp, ... ne sont pas des constantes
            if (i == joueur.getCommandeUp()){
               joueur.setMouvementY(joueur.getVitesse()*20);
            }else if (i == joueur.getCommandeDown()){
               joueur.setMouvementY(-joueur.getVitesse()*20);
            }else if (i == joueur.getCommandeLeft()){
               joueur.setMouvementX(-joueur.getVitesse()*20);
            }else if (i == joueur.getCommandeRight()){
               joueur.setMouvementX(joueur.getVitesse()*20);
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int i) {
        for (Joueur joueur: joueursList) {
            if (i == joueur.getCommandeUp() || i == joueur.getCommandeDown()){
                joueur.setMouvementY(0);
            }else if (i == joueur.getCommandeLeft() || i == joueur.getCommandeRight()){
                joueur.setMouvementX(0);
             }
        }
        return true;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        System.out.println("Click: " + i + ", "+i1);
        if (btQuitter.contains(i, i1)){
            System.out.println("Quitter !");
            game.setScreen(new EcranMenu(game));
        }
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

    public void addJoueur(Joueur j){
        joueursList.add(j);
    }
}
