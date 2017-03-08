package spilprojekt4.common.services;

import spilprojekt4.common.GameData;
import spilprojekt4.common.World;

public interface IServiceInitializer {
    public void start(GameData gameData, World world);
    public void stop(GameData gameData, World world);
}
