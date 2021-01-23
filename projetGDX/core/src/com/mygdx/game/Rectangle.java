package com.mygdx.game;

public class Rectangle {
    private int xGauche, yGauche, xDroite, yDroite;

    public Rectangle(int x1, int y1, int x2, int y2){
        this.xGauche = x1;
        this.yGauche = y1;
        this.xDroite = x2;
        this.yDroite = y2;
    }

    public boolean contains(int xp, int yp){
     return (xp < xDroite) && (xp > xGauche) && (yp < yDroite) && (yp > yGauche);
    }
}
