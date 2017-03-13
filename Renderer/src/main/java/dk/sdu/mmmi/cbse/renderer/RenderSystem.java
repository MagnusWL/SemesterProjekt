package dk.sdu.mmmi.cbse.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
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

        public void makeAnimation(String animationName, Texture spriteSheet, int spriteSizeX, int spriteSizeY, int frameDuration)
    {
        Array<TextureRegion> keyFrames = new Array<TextureRegion>();
        int numberOfSprites =  (int) (spriteSheet.getWidth() / spriteSizeX);
        for (int i = 0; i < numberOfSprites; i++) {
            TextureRegion sprite = new TextureRegion(spriteSheet);
            sprite.setRegion(i*spriteSizeX, 0, spriteSizeX, spriteSizeY);
            keyFrames.add(sprite);
        }
        
        animations.put(animationName, new Animation<TextureRegion>(frameDuration, keyFrames));
    }
  
    @Override
    public void start(GameData gameData, World world) {
        
        Texture tex = new Texture(Gdx.files.internal("Player.png"));
        images.put("Player", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("Enemy.png"));
        images.put("Enemy", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("base.png"));
        images.put("base", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("gun.png"));
        images.put("gun", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("bullet.png"));
        images.put("bullet", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("sky.png"));
        images.put("sky", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("grass.png"));
        images.put("grass", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("back1.png"));
        images.put("back1", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("back2.png"));
        images.put("back2", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("back3.png"));
        images.put("back3", new Sprite(tex));
        tex = new Texture(Gdx.files.internal("back4.png"));
        images.put("back4", new Sprite(tex));
        
        makeAnimation("player_run", new Texture(Gdx.files.internal("player_run.png")), 75, 80, 2);
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
