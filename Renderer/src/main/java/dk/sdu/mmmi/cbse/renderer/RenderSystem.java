package dk.sdu.mmmi.cbse.renderer;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import spilprojekt4.common.Entity;
import spilprojekt4.common.EntityType;
import spilprojekt4.common.GameData;
import spilprojekt4.common.World;
import spilprojekt4.common.services.IServiceInitializer;
import spilprojekt4.common.services.IServiceProcessor;

@ServiceProviders(value = {
    @ServiceProvider(service = IServiceProcessor.class),
    @ServiceProvider(service = IServiceInitializer.class)})

public class RenderSystem implements IServiceInitializer, IServiceProcessor {

    private Map<String, Animation> animations;
    private Map<String, Sprite> images;

    public RenderSystem() {
    }

    @Override
    public void start(GameData gameData, World world) {
        //Load images
    }

    @Override
    public void stop(GameData gameData, World world) {
        //delete images

    }

    @Override
    public void process(GameData gameData, World world) {
        //Draw shit
    }
}
