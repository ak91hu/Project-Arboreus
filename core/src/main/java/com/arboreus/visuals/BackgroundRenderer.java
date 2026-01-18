package com.arboreus.visuals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BackgroundRenderer {
    public static void drawCyberGradient(ShapeRenderer shapeRenderer) {
        // Gradient: Top (Soft Cyan: 0.2, 0.8, 0.9), Bottom (Soft Purple: 0.4, 0.2, 0.6)
        Color topColor = new Color(0.1f, 0.15f, 0.3f, 1f); // Darker Deep Blue/Cyber
        Color bottomColor = new Color(0.05f, 0.05f, 0.1f, 1f); // Almost black

        // Let's stick to the user's requested "Pastel Gradient" but maybe darker for
        // cyber
        // User requested: "Extract the 'Pastel Gradient' logic... call this helper...
        // transition is seamless."
        // Previous GameScreen had: Top (0.2, 0.8, 0.9), Bottom (0.4, 0.2, 0.6)
        Color pastelTop = new Color(0.2f, 0.8f, 0.9f, 1f);
        Color pastelBottom = new Color(0.4f, 0.2f, 0.6f, 1f);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                pastelBottom, pastelBottom, pastelTop, pastelTop);
        shapeRenderer.end();

        // Add a grid overlay for extra "Cyber" feel?
        // User didn't strictly ask, but "Cyber-Cell" look implies it.
        // Let's keep it simple for now as requested.
    }
}
