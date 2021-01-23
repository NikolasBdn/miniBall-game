package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class EcranIntro extends ScreenAdapter {

    private MyGdxGame game;
    private float seconde = 0;
    private  float periode = 3;

    public EcranIntro(MyGdxGame game){
        this.game = game;
    }

    @Override
    public void render(float delta) {
        seconde += Gdx.graphics.getRawDeltaTime();
        if (seconde > periode){
            System.out.println("PASSER INTRO");
            game.setScreen(new EcranMenu(game));
            seconde-=periode;
        }

        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.font.draw(game.batch, "MINIBALL", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight()/2);
        game.batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}
