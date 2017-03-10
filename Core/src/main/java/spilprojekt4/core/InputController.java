package spilprojekt4.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import spilprojekt4.common.GameData;
import spilprojekt4.common.GameKeys;

public class InputController extends InputAdapter {

    private final GameData gameData;

    public InputController(GameData gameData) {
        this.gameData = gameData;
    }

    @Override
    public boolean keyDown(int k) {
        if (k == Input.Keys.W) {
            gameData.getKeys().setKey(GameKeys.W, true);
        }
        if (k == Input.Keys.A) {
            gameData.getKeys().setKey(GameKeys.A, true);
        }
        if (k == Input.Keys.S) {
            gameData.getKeys().setKey(GameKeys.S, true);
        }
        if (k == Input.Keys.D) {
            gameData.getKeys().setKey(GameKeys.D, true);
        }
        if (k == Input.Keys.ENTER) {
            gameData.getKeys().setKey(GameKeys.ENTER, true);
        }
        if (k == Input.Keys.ESCAPE) {
            gameData.getKeys().setKey(GameKeys.ESCAPE, true);
        }
        if (k == Input.Keys.SPACE) {
            gameData.getKeys().setKey(GameKeys.SPACE, true);
        }
        if (k == Input.Keys.SHIFT_LEFT || k == Input.Keys.SHIFT_RIGHT) {
            gameData.getKeys().setKey(GameKeys.SHIFT, true);
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            gameData.getKeys().setKey(GameKeys.MOUSE0, true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int k) {
        if (k == Input.Keys.W) {
            gameData.getKeys().setKey(GameKeys.W, false);
        }
        if (k == Input.Keys.A) {
            gameData.getKeys().setKey(GameKeys.A, false);
        }
        if (k == Input.Keys.S) {
            gameData.getKeys().setKey(GameKeys.S, false);
        }
        if (k == Input.Keys.D) {
            gameData.getKeys().setKey(GameKeys.D, false);
        }
        if (k == Input.Keys.ENTER) {
            gameData.getKeys().setKey(GameKeys.ENTER, false);
        }
        if (k == Input.Keys.ESCAPE) {
            gameData.getKeys().setKey(GameKeys.ESCAPE, false);
        }
        if (k == Input.Keys.SPACE) {
            gameData.getKeys().setKey(GameKeys.SPACE, false);
        }
        if (k == Input.Keys.SHIFT_LEFT || k == Input.Keys.SHIFT_RIGHT) {
            gameData.getKeys().setKey(GameKeys.SHIFT, false);
        }
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            gameData.getKeys().setKey(GameKeys.MOUSE0, false);
        }
        return true;
    }

}
