/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spilprojekt4.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.util.vector.Matrix;
import org.openide.util.Lookup;
import spilprojekt4.common.Entity;
import spilprojekt4.common.EntityType;
import spilprojekt4.common.GameData;
import spilprojekt4.common.World;
import spilprojekt4.common.events.EventType;
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
        Texture tex = new Texture(Gdx.files.internal("midgårdsormen.png"));
        spriteMap.put("midgårdsormen", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("penisenemy.png"));
        spriteMap.put("penisenemy", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("base.png"));
        spriteMap.put("base", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("gun.png"));
        spriteMap.put("gun", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("bullet.png"));
        spriteMap.put("bullet", new Sprite(tex));
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
        drawMap();
        drawSprites();
        drawHealthBars();

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

    private void drawMap() {
        sr.setAutoShapeType(true);
        for (Entity map : world.getEntities(EntityType.MAP)) {
            sr.begin(ShapeType.Filled);
            for (int i = 0; i < gameData.getMapWidth(); i++) {
                for (int j = 0; j < gameData.getMapHeight(); j++) {
                    if (map.getMap()[i][j] == 0) {
                        sr.setColor(0.4f, 0.6f, 1, 1f);
                    } else if (map.getMap()[i][j] == 1) {
                        sr.setColor(0.1f, 0.6f, 0.1f, 1f);
                    }
                    sr.rect(gameData.getTileSize() * i - gameData.getCameraX(),
                            gameData.getTileSize() * j - gameData.getCameraY(),
                            gameData.getTileSize(), gameData.getTileSize());
                }
            }
            sr.end();
        }
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
