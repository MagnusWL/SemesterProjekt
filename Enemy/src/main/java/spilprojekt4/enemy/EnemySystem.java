package spilprojekt4.enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import spilprojekt4.common.Entity;
import spilprojekt4.common.EntityType;
import spilprojekt4.common.GameData;
import spilprojekt4.common.World;
import spilprojekt4.common.events.Event;
import spilprojekt4.common.events.EventType;
import spilprojekt4.common.services.IServiceInitializer;
import spilprojekt4.common.services.IServiceProcessor;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceProcessor.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class EnemySystem implements IServiceProcessor, IServiceInitializer {

    private List<Entity> enemies = new ArrayList<>();
    private final Random rand = new Random();

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(EntityType.ENEMY)) {
            for (Entity player : world.getEntities(EntityType.PLAYER)) {
                for (Entity base : world.getEntities(EntityType.BASE)) {
                    if (Math.abs(entity.getX() - player.getX()) < Math.abs(entity.getX() - base.getX())) {
                        if (player.getX() - 50 > entity.getX()) {
                            entity.setVelocity(entity.getMovementSpeed());
                        } else if (player.getX() + 50 < entity.getX()) {
                            entity.setVelocity(-entity.getMovementSpeed());
                        } else {
                            entity.setVelocity(0);
                        }
                    } else {
                        if (base.getX() - 25 > entity.getX()) {
                            entity.setVelocity(entity.getMovementSpeed());
                        }
                        if (base.getX() + 25 < entity.getX()) {
                            entity.setVelocity(entity.getMovementSpeed());
                        }
                    }
                }
            }

            if (rand.nextFloat() > 0.9f) {
                if (entity.isGrounded()) {
                    entity.setVerticalVelocity(entity.getJumpSpeed());
                }
            }

            for (Event e : gameData.getAllEvents()) {
                if (e.getType() == EventType.ENTITY_HIT && e.getEntityID().equals(entity.getID())) {

                    entity.setLife(entity.getLife() - 1);
                    if (entity.getLife() <= 0) {
                        world.removeWeapon(entity.getID());
                        world.removeEntity(entity);
                    }

                    gameData.removeEvent(e);
                }
            }
        }
    }

    @Override
    public void start(GameData gameData, World world) {
        for (int i = 0; i < 10; i++) {
            Entity enemy = createEnemy(gameData, world);
            world.addEntity(enemy);
        }
    }

    private Entity createEnemy(GameData gameData, World world) {
        Entity enemyCharacter = new Entity();

        enemyCharacter.setEntityType(EntityType.ENEMY);
        enemyCharacter.setX((int) (gameData.getDisplayWidth() * Math.random()));
        enemyCharacter.setY((int) (gameData.getDisplayHeight() * 0.8));
        enemyCharacter.setHasGravity(true);
        enemyCharacter.setMaxLife(10);
        enemyCharacter.setLife(enemyCharacter.getMaxLife());
        enemyCharacter.setJumpSpeed(300);
        enemyCharacter.setMovementSpeed(85);
        enemyCharacter.setSprite("penisenemy");
        enemyCharacter.setShapeX(new float[]{5, 25, 25, 5});
        enemyCharacter.setShapeY(new float[]{25, 25, 2, 2});
        gameData.addEvent(new Event(EventType.PICKUP_WEAPON, enemyCharacter.getID()));

        enemies.add(enemyCharacter);

        return enemyCharacter;
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity e : enemies) {
            world.removeEntity(e);
        }
    }
}
