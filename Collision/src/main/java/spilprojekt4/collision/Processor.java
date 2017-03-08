package spilprojekt4.collision;

import java.awt.Polygon;
import java.awt.geom.Area;
import org.openide.util.lookup.ServiceProvider;
import spilprojekt4.common.Entity;
import spilprojekt4.common.EntityType;
import spilprojekt4.common.GameData;
import spilprojekt4.common.World;
import spilprojekt4.common.services.ICollisionService;

@ServiceProvider(service = ICollisionService.class)

public class Processor implements ICollisionService {

    @Override
    public boolean isColliding(World world, GameData gameData, Entity entity, float moveX, float moveY) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        if (shapex != null && shapey != null) {
            for (int i = 0; i < shapex.length; i++) {
                int x = (int) ((entity.getX() + shapex[i] + moveX) / gameData.getTileSize());
                int y = (int) ((entity.getY() + shapey[i] + moveY) / gameData.getTileSize());
                if (x >= 0 && y >= 0 && x < gameData.getMapWidth() && y < gameData.getMapHeight()) {
                    for (Entity map : world.getEntities(EntityType.MAP)) {
                        if (map.getMap()[x][y] == 1) {
                            return true;
                        }
                    }

                } else {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isEntitiesColliding(World world, GameData gameData, Entity entity1, Entity entity2) {
        Polygon poly1 = new Polygon();
        Polygon poly2 = new Polygon();

        for (int i = 0; i < entity1.getShapeX().length; i++) {
            poly1.addPoint((int) (entity1.getShapeX()[i] + entity1.getX()), (int) (entity1.getShapeY()[i] + entity1.getY()));
        }
        for (int i = 0; i < entity2.getShapeX().length; i++) {
            poly2.addPoint((int) (entity2.getShapeX()[i] + entity2.getX()), (int) (entity2.getShapeY()[i] + entity2.getY()));
        }

        Area a = new Area(poly1);
        a.intersect(new Area(poly2));

        return !a.isEmpty();
    }
}
