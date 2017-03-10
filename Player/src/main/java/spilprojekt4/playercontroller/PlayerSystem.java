package spilprojekt4.playercontroller;

import spilprojekt4.common.Entity;
import spilprojekt4.common.EntityType;
import spilprojekt4.common.GameData;
import spilprojekt4.common.GameKeys;
import spilprojekt4.common.World;
import spilprojekt4.common.events.Event;
import spilprojekt4.common.events.EventType;
import spilprojekt4.common.services.IServiceInitializer;
import spilprojekt4.common.services.IServiceProcessor;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceProcessor.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class PlayerSystem implements IServiceProcessor, IServiceInitializer {

    private Entity player;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(EntityType.PLAYER)) {

            if (gameData.getKeys().isDown(GameKeys.A)) {
                //left
                entity.setVelocity(-entity.getMovementSpeed());
            }
            if (gameData.getKeys().isDown(GameKeys.D)) {
                //right
                entity.setVelocity(entity.getMovementSpeed());
            }

            if (gameData.getKeys().isDown(GameKeys.SPACE)) {
                if (entity.isGrounded()) {
                    entity.setVerticalVelocity(entity.getJumpSpeed());
                }
            }

            if (!gameData.getKeys().isDown(GameKeys.A) && !gameData.getKeys().isDown(GameKeys.D)) {
                entity.setVelocity(0);
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
        player = createPlayer(gameData, world);
        world.addEntity(player);
    }

    private Entity createPlayer(GameData gameData, World world) {
        Entity playerCharacter = new Entity();

        playerCharacter.setEntityType(EntityType.PLAYER);
        playerCharacter.setX((int) (gameData.getDisplayWidth() * 0.5));
        playerCharacter.setY((int) (gameData.getDisplayHeight() * 0.8));
        playerCharacter.setHasGravity(true);
        playerCharacter.setMaxLife(100);
        playerCharacter.setLife(playerCharacter.getMaxLife());
        playerCharacter.setJumpSpeed(400);
        playerCharacter.setMovementSpeed(150);
        playerCharacter.setSprite("midgårdsormen");
        playerCharacter.setShapeX(new float[]{5, 25, 25, 5});
        playerCharacter.setShapeY(new float[]{25, 25, 2, 2});
        gameData.addEvent(new Event(EventType.PICKUP_WEAPON, playerCharacter.getID()));

        return playerCharacter;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(player);
    }
}
