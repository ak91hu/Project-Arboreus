package com.arboreus.views;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends ScreenAdapter {

    @Override
    public void render(float delta) {
        // Clear screen to a different color to verify this screen is active
        ScreenUtils.clear(Color.NAVY);
    }
}
