/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spilprojekt4.weaponcontroller;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import spilprojekt4.common.Entity;
import spilprojekt4.common.EntityType;
import spilprojekt4.common.GameData;
import spilprojekt4.common.GameKeys;
import spilprojekt4.common.World;
import spilprojekt4.common.events.Event;
import spilprojekt4.common.events.EventType;
import spilprojekt4.common.services.ICollisionService;
import spilprojekt4.common.services.IServiceInitializer;
import spilprojekt4.common.services.IServiceProcessor;

@ServiceProviders(value={
        @ServiceProvider(service=IServiceProcessor.class),
        @ServiceProvider(service=IServiceInitializer.class)})

public class WeaponSystem implements IServiceProcessor, IServiceInitializer {

    @Override
    public void process(GameData gameData, World world) {
        for(Event e: gameData.getAllEvents())
            if(e.getType() == EventType.PICKUP_WEAPON)
            {
                createGun(gameData, world, world.getEntity(e.getEntityID()));
                gameData.removeEvent(e);
            }
        
        for (Entry e : world.getWeapons().entrySet()) {
            
            Entity carrier = world.getEntity((String) e.getKey());
            Entity gun = (Entity) e.getValue();
            gun.setX(carrier.getX());
            gun.setY(carrier.getY());
            gun.setVelocity(carrier.getVelocity());

            if (carrier.getEntityType() == EntityType.PLAYER && gameData.getKeys().isDown(GameKeys.S)) {
                gameData.addEvent(new Event(EventType.PLAYER_SHOOT, gun.getID()));
            }

            if (carrier.getEntityType() == EntityType.ENEMY) {
                gameData.addEvent(new Event(EventType.ENEMY_SHOOT, gun.getID()));
            }
        }
    }

    public void createGun(GameData gameData, World world, Entity e) {
        Entity weapon = new Entity();
        weapon.setEntityType(EntityType.WEAPON);
        weapon.setSprite("gun");
        world.getWeapons().put(e.getID(), weapon);
        world.addEntity(weapon);
    }

    @Override
    public void start(GameData gameData, World world) {
        
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Entity e : world.getWeapons().values()) {
            world.removeEntity(e);
        }
    }
}