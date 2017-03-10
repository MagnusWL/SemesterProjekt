package spilprojekt4.enemy;

import java.util.ArrayList;
import java.util.List;
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

@ServiceProviders(value={
        @ServiceProvider(service=IServiceProcessor.class),
        @ServiceProvider(service=IServiceInitializer.class)})

public class EnemySystem implements IServiceProcessor, IServiceInitializer {

    private List<Entity> enemies = new ArrayList<>();

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(EntityType.ENEMY)) {
            for (Entity player : world.getEntities(EntityType.PLAYER)) {
                if (player.getX() > entity.getX()) {
                    entity.setVelocity(entity.getMovementSpeed());
                } else {
                    entity.setVelocity(-entity.getMovementSpeed());
                }
            }

            for (Event e : gameData.getAllEvents()) {
                if (e.getType() == EventType.ENTITY_HIT && e.getEntityID().equals(entity.getID())) {

                    entity.setLife(entity.getLife() - 1);
                    if (entity.getLife() <= 0) {
                        world.removeEntity(entity);
                        world.removeWeapon(entity.getID());
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
