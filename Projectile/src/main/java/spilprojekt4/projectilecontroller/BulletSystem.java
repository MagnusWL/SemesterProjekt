/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spilprojekt4.projectilecontroller;

import java.util.ArrayList;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import spilprojekt4.common.GameData;
import spilprojekt4.common.World;
import spilprojekt4.common.services.IServiceInitializer;
import spilprojekt4.common.services.IServiceProcessor;
import spilprojekt4.common.Entity;
import spilprojekt4.common.EntityType;
import spilprojekt4.common.events.Event;
import spilprojekt4.common.events.EventType;

/**
 *
 * @author burno
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IServiceProcessor.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class BulletSystem implements IServiceProcessor, IServiceInitializer {

    private ArrayList<Entity> bullets = new ArrayList<>();

    private Entity createBullet(Entity entity, GameData gameData, World world, float angle) {
        Entity bullet = new Entity();
        bullet.setEntityType(EntityType.PROJECTILE);
        bullet.setVelocity((float) (350 * Math.cos(angle)));
        bullet.setVerticalVelocity((float) (350 * Math.sin(angle)));
        bullet.setSprite("bullet");
        bullet.setX(entity.getX() + 15 + ((float) Math.cos(angle) * 15));
        bullet.setY(entity.getY() + 15 + ((float) Math.sin(angle) * 15));
        bullet.setShapeX(new float[]{0, 2, 2, 0});
        bullet.setShapeY(new float[]{2, 2, 0, 0});

        bullets.add(bullet);
        return bullet;
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Event e : gameData.getAllEvents()) {
            if (e.getType() == EventType.PLAYER_SHOOT) {
                Entity player = world.getEntity(e.getEntityID());
                float angle = (float) Math.atan2(gameData.getMouseY() - (player.getY() + 15 - gameData.getCameraY()), gameData.getMouseX() - (player.getX() + 15 - gameData.getCameraX()));
                world.addEntity(createBullet(player, gameData, world, angle));
                gameData.removeEvent(e);
            }

            if (e.getType() == EventType.ENEMY_SHOOT) {
                Entity enemy = world.getEntity(e.getEntityID());
                float distancePlayer = Float.MAX_VALUE;
                float distanceBase = Float.MAX_VALUE;
                for (Entity player : world.getEntities(EntityType.PLAYER)) {
                    distancePlayer = Math.abs(player.getX() - enemy.getX());
                }
                for (Entity base : world.getEntities(EntityType.BASE)) {
                    distanceBase = Math.abs(base.getX() - enemy.getX());
                }

                if (distancePlayer > distanceBase) {
                    shootDecision(enemy, EntityType.BASE, world, gameData);
                    gameData.removeEvent(e);
                } else {
                    shootDecision(enemy, EntityType.PLAYER, world, gameData);
                    gameData.removeEvent(e);
                }

            }
        }
    }

    @Override
    public void start(GameData gameData, World world) {

    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity e : bullets) {
            world.removeEntity(e);
        }
    }

    private void shootDecision(Entity enemy, EntityType entity, World world, GameData gameData) {
        for (Entity target : world.getEntities(entity)) {
            if (entity == EntityType.PLAYER) {
                float angle = (float) Math.atan2((target.getY() + 15) - (enemy.getY() + 15), (target.getX() + 15) - (enemy.getX() + 15));
                world.addEntity(createBullet(enemy, gameData, world, angle));
            } else {
                float angle = (float) Math.atan2(target.getY() + 50 - (enemy.getY() + 15), (target.getX() + 50) - (enemy.getX() + 15));
                world.addEntity(createBullet(enemy, gameData, world, angle));
            }

        }
    }
}
