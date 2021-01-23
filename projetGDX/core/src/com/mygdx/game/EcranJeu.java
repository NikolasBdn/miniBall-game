package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

public class EcranJeu extends ScreenAdapter {
    private MyGdxGame game;
    private int nombreJoueur;

    private int scoreJoueur1 = 0;
    private int scoreJoueur2 = 0;

    private World world;
    private Body bodyBorders, bodyBut1, bodyBut2;
    private Texture backgroundTexture;
    private Texture padTexture;
    private Texture butTexture;

    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch spriteBatch;

    private Array<Body> tmpBodies = new Array<Body>();

    private final float TIMESTEP = 1 / 60f; //60fps
    private final int VELOCITYITERATION = 8, POSITIONITERATIONS = 3;

    private final int zoom = 1;
    private final int espacePad = 200;
    private Joueur joueur1, joueur2;
    private Balle balle;
    private InputMultiplexer mesEcouteur;

    private int widht = 848;
    private int height = 480;

    private int reinitialiserPositions = 0;//1 si but de j1, 2 si but de j2

    private FreeTypeFontGenerator fGen;
    private FreeTypeFontGenerator.FreeTypeFontParameter fParams;
    private BitmapFont police;

    private Minuteur minuteur;
    private long tDeb;

    private Sound shootSon;
    private Sound goalSon;

    public EcranJeu(MyGdxGame game, int nbJoueurs){
        this.game = game;
        nombreJoueur = nbJoueurs;

        world = new World(new Vector2(0, 0), true);//vector est la gravite ici 0 car vue du dessu

        //Creation joueurs avec images, position de depart et touches de commandes
        balle = new Balle(world);
        joueur1 = new Joueur(world, "images/JoueurDroite.png", 200, 0);
        joueur1.setCommandes(new int[]{19, 20, 21, 22});//ZSQD
        int[] commandesJ2 = {54, 47, 45, 32};
        if (nbJoueurs == 1) {
            joueur2 = new Bot(world, "images/JoueurGauche.png", -200, 0, balle);
        }else{
            joueur2 = new Joueur(world, "images/JoueurGauche.png", -200, 0);
        }
        joueur2.setCommandes(new int[]{54, 47, 45, 32});//Fleches directionnelles
        //Creation balle

        minuteur = new Minuteur(20);
        Timer.schedule(minuteur, 1f, 1f);
        tDeb = 0;

        backgroundTexture = new Texture(Gdx.files.internal("images/Terrain.png"));
        padTexture = new Texture(Gdx.files.internal("images/Pad.png"));
        butTexture = new Texture(Gdx.files.internal("images/But.bmp"));

        //Police d'ecriture
        fGen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Comic_Sans_MS_Bold.ttf"));
        fParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fParams.size = 35;
        fParams.color = new Color(1f, 1f, 0f,0.75f );
        fParams.borderColor  = Color.BLACK;
        fParams.borderWidth = 1;

        //Sons
        shootSon =  Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.ogg"));
        goalSon =  Gdx.audio.newSound(Gdx.files.internal("sounds/goal.ogg"));

        police = fGen.generateFont(fParams);
        fGen.dispose();
    }

    @Override
    public void render(float delta) {
        balle.getBody().setActive(true);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();//DEBUT DESSINS
        world.getBodies(tmpBodies);
        //Ajout de la texture du terrain
        spriteBatch.draw(backgroundTexture, -(camera.viewportWidth - (espacePad) / zoom) / 2,-camera.viewportHeight / 2, camera.viewportWidth - (espacePad / zoom),camera.viewportHeight);
        //Ajout de la texture des pads
        spriteBatch.draw(padTexture, (camera.viewportWidth - (espacePad)/ zoom) / 2, -10 / 2, 100, 100);
        spriteBatch.draw(padTexture, -camera.viewportWidth / 2, -10 / 2, 100, 100);

        for (Body body: tmpBodies){
            if (body.getUserData() != null && body.getUserData() instanceof Sprite){
                Sprite sprite = (Sprite) body.getUserData();
                sprite.setPosition(body.getPosition().x- sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
                sprite.setRotation(body.getAngle()* MathUtils.radiansToDegrees);
                sprite.draw(spriteBatch);
            }
        }
        police.draw(spriteBatch, scoreJoueur2+"", -150, camera.viewportHeight/2 - 20);
        police.draw(spriteBatch, scoreJoueur1+"", 150, camera.viewportHeight/2 - 20);
        police.draw(spriteBatch, minuteur.getCount()+"", -25, camera.viewportHeight/2 - 20);

        if (reinitialiserPositions != 0){
            //Je desactive le body de la balle si un but a ete marque pour eviter de le compter plusieurs fois, elle est reactivé au debut du render
            balle.getBody().setActive(false);
            if (reinitialiserPositions == 1){
                scoreJoueur1++;
                spriteBatch.draw(butTexture, -150, -100, 400, 400);
            }else if (reinitialiserPositions == 2){
                scoreJoueur2++;
                spriteBatch.draw(butTexture, -150, -100, 400, 400);
            }
            reinitialiserPositions = 0;


            System.out.println("Score: Joueur 1 = "+ scoreJoueur1+" - Joueur 2 = "+scoreJoueur2);
            balle.reinitialiserPosition();
            joueur1.reinitialiserPosition();
            joueur2.reinitialiserPosition();
        }

        if (minuteur.getCount() <= 0){
            //Bloquer les joueur pendant l'affichage des resultats
            joueur1.reinitialiserPosition();
            joueur2.reinitialiserPosition();
            balle.reinitialiserPosition();
            Timer.instance().stop();
            if (scoreJoueur1 > scoreJoueur2){
                police.draw(spriteBatch, "Victoire du Joueur de droite !\n", -255,  45);
            }else if (scoreJoueur2 > scoreJoueur1){
                police.draw(spriteBatch, "Victoire du Joueur de gauche !\n", -255,  45);
            }else {
                police.draw(spriteBatch, "Match nul !\n", -110,  45);
            }

            police.draw(spriteBatch, +scoreJoueur2+" - "+scoreJoueur1, -45,  10);

            if (tDeb == 0){
                tDeb = System.currentTimeMillis();

            }else if (System.currentTimeMillis() - tDeb > 3000){
                nouvellePartie();
            }
        }

        spriteBatch.end();
        debugRenderer.render(world, camera.combined);

        world.step(TIMESTEP, VELOCITYITERATION, POSITIONITERATIONS);
        joueur1.render();
        joueur2.render();
        balle.render();

        spriteBatch.setProjectionMatrix(camera.combined);
    }

    private void nouvellePartie(){
        System.out.println("Nouvelle Partie");
        scoreJoueur1 = 0;
        scoreJoueur2 = 0;
        minuteur.recommencer();
        Timer.instance().start();
        tDeb = 0;
        reinitialiserPositions = 3;
    }
    public void show() {
        debugRenderer = new Box2DDebugRenderer();

        spriteBatch = new SpriteBatch();

        camera = new OrthographicCamera( widht / zoom,  height / zoom);

        //Mise en place des bodies de bordure du terrain et des cages
        setTerrain();

        mesEcouteur = new InputMultiplexer();
        //Ajout ecouteur du clavier et souris
        System.out.println("quitter: "+(Gdx.graphics.getWidth() / 2 - 40) + ", "+(Gdx.graphics.getHeight() -100));
        Rectangle btQuitter = new Rectangle(Gdx.graphics.getWidth() / 2 - 40, Gdx.graphics.getHeight() - 100,Gdx.graphics.getWidth() / 2 + 40, Gdx.graphics.getHeight() );//premier bouton de 210px hauteur

        Ecouteur ecouteur = new Ecouteur(game, btQuitter);
        ecouteur.addJoueur(joueur1);
        if (nombreJoueur == 2){
            ecouteur.addJoueur(joueur2);
        }
        mesEcouteur.addProcessor(ecouteur);

        //Ajout ecouteur des gestes sur l'ecran tactile
        //Creation zones de controles
        System.out.println("Zone controle: "+((camera.viewportWidth-padTexture.getWidth()) * (Gdx.graphics.getWidth() / widht))+", "+ 0+", "+ Gdx.graphics.getWidth()+", "+ Gdx.graphics.getHeight());
        System.out.println("Pad texture: "+padTexture.getWidth());

        Rectangle zoneControle1 = new Rectangle((widht-padTexture.getWidth()) * (Gdx.graphics.getWidth() / widht), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        joueur1.setZonePad(zoneControle1);

        Rectangle zoneControle2 = new Rectangle(0, 0, (padTexture.getWidth()) * (Gdx.graphics.getWidth() / widht), Gdx.graphics.getHeight());
        joueur2.setZonePad(zoneControle2);

        EcouteurGeste ecouteurGeste = new EcouteurGeste();
        ecouteurGeste.addJoueur(joueur1);
        if (nombreJoueur == 2) {
            ecouteurGeste.addJoueur(joueur2);
        }
        mesEcouteur.addProcessor(new GestureDetector(ecouteurGeste));
        Gdx.input.setInputProcessor(mesEcouteur);
    }

    public void setTerrain(){
        //BORDERS
        //body definition
        BodyDef bordersBody = new BodyDef();
        bordersBody.type = BodyDef.BodyType.StaticBody;
        bordersBody.position.set(-(camera.viewportWidth - (espacePad) / zoom) / 2,- camera.viewportHeight / 2);

        // borders shape
        FileHandle handle = Gdx.files.internal("models/terrain.txt");
        String text = handle.readString();

        String linesArray[] = text.split("\\r?\\n");
        float[] coordBorders = new float[linesArray.length * 2];
        int j = 0;

        for (String word: linesArray) {
            String tmpArray[] = word.split("  *");
            float f =  Float.parseFloat(tmpArray[0]);
            coordBorders[j] = Float.parseFloat(tmpArray[0]);
            coordBorders[++j] = Float.parseFloat(tmpArray[1]);
            j++;
        }

        for (int i = 0; i < coordBorders.length ; i+=2)
        {
            coordBorders[i] = (coordBorders[i] / 100 )* (camera.viewportWidth - (espacePad) / zoom);
        }
        for (int i = 1; i < coordBorders.length ; i+=2)
        {
            coordBorders[i] = (coordBorders[i] / 100) * camera.viewportHeight;
        }


        ChainShape bordersShape = new ChainShape();
        bordersShape.createLoop(coordBorders);

        System.out.println("Screen size: " + Gdx.graphics.getWidth() + " x " + Gdx.graphics.getHeight());

        //fixture definition
        FixtureDef fixtureBordersDef = new FixtureDef();
        fixtureBordersDef.shape = bordersShape;

        bodyBorders = world.createBody(bordersBody);
        bodyBorders.createFixture(fixtureBordersDef);
        bodyBorders.setUserData("Terrain");
        bordersShape.dispose();

        //BUTS
        //Droite
        BodyDef but1BodyDef = new BodyDef();
        but1BodyDef.type = BodyDef.BodyType.StaticBody;
        but1BodyDef.position.set(-(camera.viewportWidth - (espacePad) / zoom) / 2,- camera.viewportHeight / 2);

        //Gauche
        BodyDef but2BodyDef = new BodyDef();
        but2BodyDef.type = BodyDef.BodyType.StaticBody;
        but2BodyDef.position.set(-(camera.viewportWidth - (espacePad) / zoom) / 2,- camera.viewportHeight / 2);

        // Buts shape
        //Droite
        ChainShape but1Shape = new ChainShape();
        float xBut1 = (float)(92.3828125 / 100) * (camera.viewportWidth - (espacePad) / zoom);//Utilisation donnée du terrain
        float y1But1 = (float)(41.015625 / 100) * (camera.viewportHeight);//Utilisation donnée du terrain
        float y2But1 = (float)(58.59375/ 100) * (camera.viewportHeight);//Utilisation donnée du terrain
        System.out.println(y1But1);
        but1Shape.createChain(new float[]{xBut1, y1But1, xBut1, y2But1});
        FixtureDef fixtureBute1Def = new FixtureDef();
        fixtureBute1Def.shape = but1Shape;

        bodyBut1 = world.createBody(but1BodyDef);
        bodyBut1.createFixture(fixtureBute1Def);
        bodyBut1.setUserData("butsDroite");
        but1Shape.dispose();

        //Gauche
        ChainShape but2Shape = new ChainShape();
        float xBut2 = (float)(7.6171875 / 100) * (camera.viewportWidth - (espacePad) / zoom);//Utilisation donnée du terrain
        float y1But2 = (float)(41.015625 / 100) * (camera.viewportHeight);//Utilisation donnée du terrain
        float y2But2 = (float)(58.59375/ 100) * (camera.viewportHeight);//Utilisation donnée du terrain

        but2Shape.createChain(new float[]{xBut2, y1But2, xBut2, y2But2});
        FixtureDef fixtureBute2Def = new FixtureDef();
        fixtureBute2Def.shape = but2Shape;

        bodyBut2 = world.createBody(but2BodyDef);
        bodyBut2.createFixture(fixtureBute2Def);
        bodyBut2.setUserData("butsGauche");
        but2Shape.dispose();


        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                int hashCodeBut1 = -1755509749;
                int hashCodeBut2 = -1685149545;
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if ((fixtureA.getBody().getUserData().equals("butsDroite") || fixtureA.getBody().getUserData().equals("butsGauche")) && fixtureB.getBody() != null) {
                    contact.setEnabled(false);//La ligne des buts ne bloque pas les bodies

                    if (fixtureB.getBody().getUserData() instanceof Sprite) {
                        Sprite s = (Sprite) fixtureB.getBody().getUserData();

                        //Si le sprite a pour texture la texture de la balle
                        if (s.getTexture().getTextureData().toString().equals(balle.getTexturePath())) {
                            if (fixtureA.getBody().getUserData().equals("butsDroite")) {
                                System.out.println("But pour le joueur Gauche !");
                                reinitialiserPositions = 2;
                                goalSon.play();
                            } else if (fixtureA.getBody().getUserData().equals("butsGauche")) {
                                System.out.println("But pour le joueur Droit !");
                                reinitialiserPositions = 1;
                                goalSon.play();
                            }
                        }

                    }
                }

                if (fixtureB.getBody().getUserData() instanceof Sprite && fixtureA.getBody().getUserData() instanceof Sprite) {

                    Sprite fa = (Sprite) fixtureA.getBody().getUserData();
                    Sprite fb = (Sprite) fixtureB.getBody().getUserData();

                    boolean estJoueur = fb.getTexture().getTextureData().toString().equals(joueur1.getTexturePath()) || fb.getTexture().getTextureData().toString().equals(joueur2.getTexturePath());
                    boolean estBalle = (fa.getTexture().getTextureData().toString().equals(balle.getTexturePath()) || fa.getTexture().getTextureData().toString().equals(balle.getTexturePath()));
                    System.out.println("estBalle: "+estBalle+", estJoueur: "+estJoueur);

                    if ( estBalle && estJoueur ) {
                        shootSon.play();
                    }

                }
            }


            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                contact.setEnabled(true);
            }
        });

    }



    @Override
    public void hide(){
        dispose();
    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        joueur1.getSprite().getTexture().dispose();
        joueur2.getSprite().getTexture().dispose();
        balle.getSprite().getTexture().dispose();
    }

}
