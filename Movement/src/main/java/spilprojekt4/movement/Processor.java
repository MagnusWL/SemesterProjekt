package spilprojekt4.movement;

import spilprojekt4.common.Entity;
import spilprojekt4.common.EntityType;
import spilprojekt4.common.GameData;
import spilprojekt4.common.World;
import spilprojekt4.common.events.Event;
import spilprojekt4.common.events.EventType;
import spilprojekt4.common.services.ICollisionService;
import spilprojekt4.common.services.IServiceProcessor;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service=IServiceProcessor.class)

public class Processor implements IServiceProcessor {

    float steps = 10.0f;

    @Override
    public void process(GameData gameData, World world) {
        for (ICollisionService e : Lookup.getDefault().lookupAll(ICollisionService.class)) {         
            for (Entity entity : world.getEntities(EntityType.PLAYER, EntityType.ENEMY, EntityType.PROJECTILE)) {
                for (int i = 0; i < steps; i++) {
                    if (!e.isColliding(world, gameData, entity, entity.getVelocity() * gameData.getDelta() * (1.0f / steps), 0)) {
                        entity.setX(entity.getX() + entity.getVelocity() * gameData.getDelta() * (1.0f / steps));
                    } else if (entity.getEntityType() == EntityType.PROJECTILE) {
                        entity.setVelocity(0);
                        world.removeEntity(entity);
                    }

                    if (!e.isColliding(world, gameData, entity, 0, entity.getVerticalVelocity() * gameData.getDelta() * (1.0f / steps))) {
                        entity.setY(entity.getY() + entity.getVerticalVelocity() * gameData.getDelta() * (1.0f / steps));
                    } else {
                        entity.setVerticalVelocity(0);

                        if (entity.getEntityType() == EntityType.PROJECTILE) {
                            world.removeEntity(entity);
                        }
                    }
                }
                
                if(entity.getEntityType() == EntityType.PROJECTILE)
                {
                    for (Entity entityHit : world.getEntities(EntityType.PLAYER, EntityType.ENEMY)) {                    
                        if(e.isEntitiesColliding(world, gameData, entity, entityHit))
                        {
                            gameData.addEvent(new Event(EventType.ENTITY_HIT, entityHit.getID()));
                            world.removeEntity(entity);
                            break;
                        }
                    }
                }
            }

            break;
        }
    }
}
