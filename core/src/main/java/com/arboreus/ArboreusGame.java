package com.arboreus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class ArboreusGame extends Game {

    public enum GameMode {
        BST, RBT
    }

    public GameMode currentMode = GameMode.BST;

    @Override
    public void create() {
        com.arboreus.visuals.AssetGenerationHelper.loadSystems();
        setScreen(new com.arboreus.screens.MainMenuScreen());
    }

    public void setGameMode(String modeType) {
        if ("RBT".equals(modeType)) {
            currentMode = GameMode.RBT;
        } else {
            currentMode = GameMode.BST;
        }
    }

    public void switchToGameScreen(String gameMode) {
        setGameMode(gameMode);
        setScreen(new com.arboreus.visuals.GameScreen());
    }
}
