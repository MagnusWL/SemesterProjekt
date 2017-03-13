/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spilprojekt4.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.util.Lookup;
import spilprojekt4.common.Entity;
import spilprojekt4.common.EntityType;
import spilprojekt4.common.GameData;
import spilprojekt4.common.World;
import spilprojekt4.common.services.IServiceInitializer;
import spilprojekt4.common.services.IServiceProcessor;

/**
 *
 * @author Magnus
 */
public class Game implements ApplicationListener {

    private World world;
    private GameData gameData;
    private OrthographicCamera cam;
    private List<IServiceProcessor> processorList;

    public Game() {

    }

    SpriteBatch batch;
    Map<String, Sprite> spriteMap = new HashMap<>();
    ShapeRenderer sr;

    @Override
    public void create() {
        processorList = new ArrayList<>();
        world = new World();
        gameData = new GameData();
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        gameData.setTileSize(16);
        gameData.setMapWidth(gameData.getDisplayWidth() / gameData.getTileSize() * 2);
        gameData.setMapHeight(gameData.getDisplayHeight() / gameData.getTileSize());

        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        for (IServiceInitializer i : Lookup.getDefault().lookupAll(IServiceInitializer.class)) {
            i.start(gameData, world);
        }

        Gdx.input.setInputProcessor(
                new InputController(gameData)
        );
        batch = new SpriteBatch();

        

        sr = new ShapeRenderer();
    }

    @Override
    public void render() {

        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());
        for (IServiceProcessor e : Lookup.getDefault().lookupAll(IServiceProcessor.class)) {
            e.process(gameData, world);
        }

        update();
        drawBackground();
//        drawMap();
        drawSprites();
        drawHealthBars();
        drawForeground();
        gameData.getKeys().update();
    }

    private void drawHealthBars() {
        sr.setAutoShapeType(true);
        sr.begin(ShapeType.Filled);
        int healthOffset;
        int healthWidth;

        for (Entity entity : world.getAllEntities()) {
            if (entity.getMaxLife() != 0) {
                healthOffset = (int) spriteMap.get(entity.getSprite()).getHeight() + 5;
                healthWidth = (int) spriteMap.get(entity.getSprite()).getWidth();
                sr.setColor(1f, 0f, 0, 1f);
                sr.rect(entity.getX() - gameData.getCameraX(), entity.getY() - gameData.getCameraY() + healthOffset, healthWidth, 5);
                sr.setColor(0.0f, 1f, 0, 1f);
                sr.rect(entity.getX() - gameData.getCameraX(), entity.getY() - gameData.getCameraY() + healthOffset, ((float) entity.getLife() / (float) entity.getMaxLife()) * healthWidth, 5);
            }
        }
        sr.end();
    }

    private void drawSprites() {
        batch.begin();
        for (Entity entity : world.getEntities(EntityType.BASE)) {
            drawSprite(entity, spriteMap.get(entity.getSprite()), false);
        }

        for (Entity entity : world.getEntities(EntityType.ENEMY)) {
            drawSprite(entity, spriteMap.get(entity.getSprite()), true);
        }

        for (Entity entity : world.getEntities(EntityType.PLAYER)) {
            drawSprite(entity, spriteMap.get(entity.getSprite()), true);
        }

        for (Entity entity : world.getEntities(EntityType.WEAPON)) {
            drawSprite(entity, spriteMap.get(entity.getSprite()), true);
        }

        for (Entity entity : world.getEntities(EntityType.PROJECTILE)) {
            drawSprite(entity, spriteMap.get(entity.getSprite()), false);
        }

        batch.end();
    }

    private void drawSprite(Entity entity, Sprite sprite, boolean flip) {
        if (flip) {
            if ((entity.getVelocity() < 0 && !sprite.isFlipX()) || (entity.getVelocity() > 0 && sprite.isFlipX())) {
                sprite.flip(true, false);
            }
        }

        sprite.setX(entity.getX() - gameData.getCameraX());
        sprite.setY(entity.getY() - gameData.getCameraY());
        sprite.draw(batch);
    }

    float back1m = 2f;
    float back2m = 1f;
    float back3m = 0.5f;
    float back4m = 0.25f;

    private void drawBackground() {
        sr.begin(ShapeType.Filled);
        sr.setColor(new Color(0, 138f / 255f, 1f, 1));
        sr.rect(0, 0, gameData.getDisplayWidth(), gameData.getDisplayWidth());
        sr.end();
        batch.begin();
        drawBackground(spriteMap.get("back4"), back4m);
        drawBackground(spriteMap.get("back3"), back3m);
        drawBackground(spriteMap.get("back2"), back2m);
        batch.end();
    }

    private void drawForeground() {
        batch.begin();
        drawBackground(spriteMap.get("back1"), back1m);
        batch.end();
    }

    private float playerX = 0;

    private void drawBackground(Sprite sprite, float mov) {
        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            playerX = player.getX();
        }

        int repeats = 2 + (int) (((gameData.getTileSize() * gameData.getMapWidth()) / sprite.getWidth()) * mov);
        for (int i = -1; i < repeats; i++) {
            sprite.setX(i * sprite.getWidth() - gameData.getCameraX() * mov);
            sprite.draw(batch);
        }

    }

    private void drawMap() {
        batch.begin();
        for (Entity map : world.getEntities(EntityType.MAP)) {
            for (int i = 0; i < gameData.getMapWidth(); i++) {
                for (int j = 0; j < gameData.getMapHeight(); j++) {
                    if (map.getMap()[i][j] == 0) {
                        spriteMap.get("sky").setX(gameData.getTileSize() * i - gameData.getCameraX());
                        spriteMap.get("sky").setY(gameData.getTileSize() * j - gameData.getCameraY());
                        spriteMap.get("sky").draw(batch);
                    } else if (map.getMap()[i][j] == 1) {
                        spriteMap.get("grass").setX(gameData.getTileSize() * i - gameData.getCameraX());
                        spriteMap.get("grass").setY(gameData.getTileSize() * j - gameData.getCameraY());
                        spriteMap.get("grass").draw(batch);
                    }
                }
            }
        }
        batch.end();
    }

    private void update() {
        gameData.setMouseX(Gdx.input.getX());
        gameData.setMouseY(gameData.getDisplayHeight() - Gdx.input.getY());
        
        for (IServiceProcessor e : processorList) {
            e.process(gameData, world);
        }
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
