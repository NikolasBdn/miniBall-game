package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Joueur {
    private BodyDef bodyDef;
    private Body body;
    private Sprite sprite;
    private CircleShape shape;
    private FixtureDef fixture;
    private String texturePath;
    private Vector2 mouvement = new Vector2();
    private final static float vitesse = 1500;
    private final static float taille = 10f;

    private int initX;
    private int initY;

    private int commandeUp;
    private int commandeDown;
    private int commandeLeft;
    private int commandeRight;

    private Rectangle zonePad;

    private boolean reinitialiserPosition;

    public String getTexturePath() {
        return texturePath;
    }

    public Joueur(World world, String tp, int xp, int yp) {
        texturePath = tp;
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        initX = xp;
        initY = yp;
        bodyDef.position.set(initX, initY);

        // perso shape
        shape = new CircleShape();
        shape.setRadius(taille);

        //fixture definition
        fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 1f;
        //fixture.friction = .75f;
        fixture.restitution = .25f;

        body = world.createBody(bodyDef);
        body.createFixture(fixture);

        sprite = new Sprite(new Texture(Gdx.files.internal(texturePath)));
        sprite.setSize(taille*2, taille*2);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        body.setUserData(sprite);

        shape.dispose();
    }

    public void render()
    {
        if (reinitialiserPosition){
            //Si un but a ete marque on reinitialise la position des deux joueur
            body.setLinearVelocity(0,0 );
            body.setTransform(initX, initY, 0);
            reinitialiserPosition = false;
        }
        body.applyForceToCenter(mouvement, true);
    }

    public float getMouvementX() {
        return mouvement.x;
    }

    public float getMouvementY() {
        return mouvement.y;
    }

    public void setMouvementX(float x) {
        this.mouvement.x = x;
    }

    public void setMouvementY(float y) {
        this.mouvement.y = y;
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getMouvement() {
        return mouvement;
    }

    public float getVitesse() {
        return vitesse;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getCommandeUp() {
        return commandeUp;
    }

    public int getCommandeDown() {
        return commandeDown;
    }

    public int getCommandeLeft() {
        return commandeLeft;
    }

    public int getCommandeRight() {
        return commandeRight;
    }

    public Rectangle getZonePad() {
        return zonePad;
    }

    public void setZonePad(Rectangle zonePad) {
        this.zonePad = zonePad;
    }

    public void setCommandes(int[] commandes) {
        if (commandes.length == 4){
            this.commandeUp = commandes[0];
            this.commandeDown = commandes[1];
            this.commandeLeft = commandes[2];
            this.commandeRight = commandes[3];
        }
    }

    public void reinitialiserPosition(){
        reinitialiserPosition = true;
    }

}
