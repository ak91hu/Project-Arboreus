package com.arboreus.screens;

import com.arboreus.ArboreusGame;
import com.arboreus.visuals.AssetGenerationHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.arboreus.visuals.BackgroundRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private ShapeRenderer shapeRenderer;
    private com.badlogic.gdx.audio.Sound clickSound;

    public MainMenuScreen() {
        System.out.println("MainMenuScreen: initializing...");
        stage = new Stage(new ScreenViewport());
        shapeRenderer = new ShapeRenderer();
        skin = new Skin();

        clickSound = Gdx.audio.newSound(Gdx.files.internal("click-a.ogg"));

        // 1. Generate Fonts (True FreeType - Exact Sizes)
        // Title Font (Big, Sharp)
        BitmapFont titleFont = AssetGenerationHelper.getFont(72);
        skin.add("title", titleFont);

        // UI Font (Regular, Sharp) - Used for buttons/HUD
        BitmapFont uiFont = AssetGenerationHelper.getFont(32);
        skin.add("ui", uiFont);

        // 2. Label Styles
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = skin.getFont("title");
        titleStyle.fontColor = Color.CYAN; // Cyber Cyan
        skin.add("title", titleStyle);

        // 3. Button Styles (Custom Image)
        AssetGenerationHelper.loadCustomButtonSkin(skin);
        // Note: "cyber" text button style is added by this helper

        buildStage();
    }

    private void buildStage() {
        Table table = new Table();
        table.setFillParent(true);
        table.top(); // Start from top

        Label titleLabel = new Label(AssetGenerationHelper.i18n.get("app_title"), skin, "title");

        // Use "cyber" style (which maps to Standard now)
        TextButton bstButton = new TextButton(AssetGenerationHelper.i18n.get("menu_bst"), skin, "cyber");

        bstButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(1.0f);
                ((ArboreusGame) Gdx.app.getApplicationListener()).switchToGameScreen("BST");
            }
        });

        // Top 1/3 approx positions with padding fixed
        table.add(titleLabel).padTop(Gdx.graphics.getHeight() * 0.33f).padBottom(50).row();
        // Button centered, 400x80
        // Button centered, 400x80
        table.add(bstButton).size(400, 80).padTop(0).row(); // Reduced top pad since Title has bottom pad

        TextButton rbtButton = new TextButton(AssetGenerationHelper.i18n.get("menu_rbt"), skin, "cyber");
        rbtButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(1.0f);
                ((ArboreusGame) Gdx.app.getApplicationListener()).switchToGameScreen("RBT");
            }
        });
        table.add(rbtButton).size(400, 80).padTop(20).row();

        TextButton exitButton = new TextButton(AssetGenerationHelper.i18n.get("menu_exit"), skin, "cyber");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.play(1.0f);
                Gdx.app.exit();
            }
        });
        table.add(exitButton).size(400, 80).padTop(20).row();

        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Unified Background
        // Helper takes ShapeRenderer
        BackgroundRenderer.drawCyberGradient(shapeRenderer);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        // Helper might need resize notification if it cached anything, but it doesn't.
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        shapeRenderer.dispose();
        clickSound.dispose();
    }
}
