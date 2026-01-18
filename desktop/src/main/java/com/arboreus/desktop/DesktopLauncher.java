package com.arboreus.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.arboreus.ArboreusGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        try {
            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
            config.setForegroundFPS(60);
            config.setTitle("Arboreus");
            config.setWindowedMode(1600, 900);
            config.setResizable(true); // Allow resizing per user request
            config.setWindowIcon(com.badlogic.gdx.Files.FileType.Internal, "icon.png");

            System.out.println("Starting Lwjgl3Application...");
            new Lwjgl3Application(new ArboreusGame(), config);
            System.out.println("Lwjgl3Application exited.");
        } catch (Exception e) {
            System.err.println("CRASH IN DESKTOP LAUNCHER:");
            e.printStackTrace();
        }
    }
}
