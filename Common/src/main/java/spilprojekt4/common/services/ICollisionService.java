/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spilprojekt4.common.services;

import spilprojekt4.common.Entity;
import spilprojekt4.common.GameData;
import spilprojekt4.common.World;

/**
 *
 * @author Magnus
 */
public interface ICollisionService {
    public boolean isColliding(World world, GameData gameData, Entity entity, float moveX, float moveY);
    public boolean isEntitiesColliding(World world, GameData gameData, Entity entity1, Entity entity2);
}
