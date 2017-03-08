package spilprojekt4.camera;

import org.openide.util.lookup.ServiceProvider;
import spilprojekt4.common.Entity;
import spilprojekt4.common.EntityType;
import spilprojekt4.common.GameData;
import spilprojekt4.common.World;
import spilprojekt4.common.services.IServiceInitializer;
import spilprojekt4.common.services.IServiceProcessor;

@ServiceProvider(service = IServiceProcessor.class)

public class Processor implements IServiceProcessor {

    @Override
    public void process(GameData gameData, World world) {
        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            gameData.setCameraX((int) (player.getX() - gameData.getDisplayWidth() / 2.0));
            gameData.setCameraY((int) (player.getY() - gameData.getDisplayHeight() / 2.0));
            checkBlankSpace(gameData);
            break;
        }
    }

    public void checkBlankSpace(GameData gameData) {

        if (gameData.getCameraX() < 0) {
            gameData.setCameraX(0);
        } else if (gameData.getCameraX() > (gameData.getMapWidth() * gameData.getTileSize()) - gameData.getDisplayWidth()) {
            gameData.setCameraX((gameData.getMapWidth() * gameData.getTileSize()) - gameData.getDisplayWidth());
        }

        if (gameData.getCameraY() > 0) {
            gameData.setCameraY(0);
        } else if (gameData.getCameraY() < (gameData.getMapHeight() * gameData.getTileSize()) - gameData.getDisplayHeight()) {
            gameData.setCameraY((gameData.getMapHeight() * gameData.getTileSize()) - gameData.getDisplayHeight());
        }
    }
}
