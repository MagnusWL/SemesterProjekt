/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spilprojekt4.common.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Josan gamle stodder
 */
public class SpriteContainer {
    
    private Map<String, Animation> animationMap;
    
    public SpriteContainer()
    {
        animationMap = new HashMap<String, Animation>();
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
        addAnimation(new Animation<TextureRegion>(frameDuration, keyFrames), animationName);
    }
    public void addAnimation(Animation animation, String spriteMapKey)
    {
        animationMap.put(spriteMapKey, animation);
    }
    
}
