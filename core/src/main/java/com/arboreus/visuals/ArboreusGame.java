package com.arboreus.visuals;

import com.badlogic.gdx.Game;

public class ArboreusGame extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
