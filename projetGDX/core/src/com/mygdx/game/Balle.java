package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Balle {

    private BodyDef bodyDef;
    private Body body;
    private Sprite sprite;
    private CircleShape shape;
    private FixtureDef fixture;
    private String texturePath;
    private boolean reinitialiserPosition;
    private final static float taille = 5f;

    public Balle(World world) {
        reinitialiserPosition = false;
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0, 0);
        texturePath = "images/Ballon.png";
        // perso shape
        shape = new CircleShape();
        shape.setRadius(taille);

        //fixture definition
        fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 1f;
        fixture.restitution = .25f;

        body = world.createBody(bodyDef);
        body.createFixture(fixture);

        sprite = new Sprite(new Texture(Gdx.files.internal(texturePath)));
        sprite.setSize(taille*2, taille*2);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        body.setUserData(sprite);

        shape.dispose();
    }

    public void render(){
        if (reinitialiserPosition){
            //Si un but a ete marque on reinitialise la position de la balle au centre du terrain avec une vitesse de O
            body.setLinearVelocity(0, 0);
            body.setTransform(0, 0, 0);
            reinitialiserPosition = false;
            System.out.println("Balle au centre.");
        }
    }

    public void reinitialiserPosition(){
        reinitialiserPosition = true;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public Body getBody() {
        return body;
    }
}
