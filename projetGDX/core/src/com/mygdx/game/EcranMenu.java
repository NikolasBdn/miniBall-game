package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class EcranMenu extends ScreenAdapter {
    private MyGdxGame game;
    private Texture backgroundTexture, ballonTexture, menuTexture;

    public EcranMenu(MyGdxGame game){
        this.game = game;
        backgroundTexture = new Texture("images/Terrain.png");
        ballonTexture = new Texture("images/Ballon.png");
        menuTexture = new Texture("images/Menu.jpg");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter(){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //adapte taille du menu a la taille de l'ecran
                int largeurMenu = menuTexture.getWidth()  * (Gdx.graphics.getWidth() / 640);
                int longueurMenu = menuTexture.getHeight()* (Gdx.graphics.getHeight() / 480);

                int xG = (Gdx.graphics.getWidth() - largeurMenu) / 2;
                int yG = (Gdx.graphics.getHeight() - longueurMenu) / 2;
                int xD = (Gdx.graphics.getWidth() - largeurMenu) / 2 +largeurMenu;
                int yD = (Gdx.graphics.getHeight() - longueurMenu) / 2 + longueurMenu / 4;

                Rectangle btJ1 = new Rectangle(xG, yG, xD, yD);//premier bouton de 210px hauteur

                Rectangle btJ2 = new Rectangle(xG, yG + longueurMenu / 4, xD, yD + longueurMenu / 4);//deuxieme bouton de 210px hauteur

                Rectangle btQuitter = new Rectangle(xG , yG + 3*longueurMenu/4, xD, yG + longueurMenu);//deuxieme bouton de 210px hauteur


                if (btJ1.contains(screenX, screenY)){
                    System.out.println("1 joueur");
                    game.setScreen(new EcranJeu(game, 1));

                }
                else if (btJ2.contains(screenX, screenY)){
                    System.out.println("2 joueurs");
                    game.setScreen(new EcranJeu(game, 2));
                }
                else if (btQuitter.contains(screenX, screenY)){
                    System.out.println("Quitter");
                    System.exit(-1);
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
       // game.batch.draw(backgroundTexture, -55, -90, Gdx.graphics.getWidth() + 110,  Gdx.graphics.getHeight() + 180);
        //game.batch.draw(ballonTexture, (Gdx.graphics.getWidth() - ballonTexture.getWidth()) / 2, (Gdx.graphics.getHeight() - ballonTexture.getHeight()) /2 - 70,
        //        ballonTexture.getWidth() + 10,Gdx.graphics.getHeight() + 50);
        int largeurMenu = menuTexture.getWidth()  * (Gdx.graphics.getWidth() / 640);
        int longueurMenu = menuTexture.getHeight()* (Gdx.graphics.getHeight() / 480);

        game.batch.draw(menuTexture, (Gdx.graphics.getWidth() - largeurMenu) / 2 , (Gdx.graphics.getHeight() - longueurMenu) /2,
                largeurMenu, longueurMenu );
        game.batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}


/*Gdx.graphics.getWidth() - menuTexture.getWidth()) / 2 , (Gdx.graphics.getHeight() - menuTexture.getHeight()) /2,
                        (Gdx.graphics.getWidth() - menuTexture.getWidth()) / 2 + menuTexture.getWidth(),  (Gdx.graphics.getHeight() - menuTexture.getHeight()) /2 + menuTexture.getHeight()/4  */