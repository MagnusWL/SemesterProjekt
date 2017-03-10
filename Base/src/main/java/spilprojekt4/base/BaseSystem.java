package spilprojekt4.base;

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

public class BaseSystem implements IServiceProcessor, IServiceInitializer {

    private Entity base;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity entity : world.getEntities(EntityType.BASE)) {
            for (Event e : gameData.getAllEvents()) {
                if (e.getType() == EventType.ENTITY_HIT && e.getEntityID().equals(entity.getID())) {

                    entity.setLife(entity.getLife() - 1);
                    if (entity.getLife() <= 0) {
                        world.removeEntity(entity);
                    }

                    gameData.removeEvent(e);
                }
            }
        }
    }

    @Override
    public void start(GameData gameData, World world) {
        base = createBase(gameData, world);
        world.addEntity(base);
    }

    private Entity createBase(GameData gameData, World world) {
        Entity base = new Entity();

        base.setEntityType(EntityType.BASE);
        base.setX((int) (gameData.getDisplayWidth() * 0.2));
        base.setY((int) (gameData.getDisplayHeight() * 0.13));
        base.setMaxLife(50);
        base.setLife(base.getMaxLife());
        base.setHasGravity(false);
        base.setSprite("base");

        base.setShapeX(new float[]{
            20,
            20,
            80,
            80});
        base.setShapeY(new float[]{
            0,
            100,
            100,
            0});

        return base;
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(base);
    }
}
