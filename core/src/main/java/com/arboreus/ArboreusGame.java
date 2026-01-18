package com.arboreus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class ArboreusGame extends Game {

    @Override
    public void create() {
        com.arboreus.visuals.AssetGenerationHelper.loadSystems();
        setScreen(new com.arboreus.screens.MainMenuScreen());
    }

    public void switchToGameScreen(String gameMode) {
        if ("BST".equals(gameMode)) {
            setScreen(new com.arboreus.visuals.GameScreen());
        }
        // Future modes can be added here
    }
}
