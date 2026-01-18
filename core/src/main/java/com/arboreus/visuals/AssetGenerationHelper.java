package com.arboreus.visuals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class AssetGenerationHelper {

    /**
     * Generates a "Cyber-Cell" style node texture.
     * Scale: 128x128 for smooth anti-aliased look.
     */
    public static Texture generateCircleTexture(int radius) {
        int size = 128; // 128x128 texture space
        int centerX = size / 2;
        int centerY = size / 2;

        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setBlending(Pixmap.Blending.None); // Important for clearing
        pixmap.setColor(0, 0, 0, 0); // Transparent
        pixmap.fill();

        pixmap.setBlending(Pixmap.Blending.SourceOver);

        // 1. Layer 3 (Glow): Soft faint outer glow
        // Since Pixmap doesn't support gradients/blur effectively, we simulate with a
        // low-alpha circle
        pixmap.setColor(0.2f, 1f, 1f, 0.15f); // Neon Cyan faint
        pixmap.fillCircle(centerX, centerY, 62);

        // 2. Layer 2 (Border): Thick Neon Yellow Ring
        pixmap.setColor(0.9f, 1.0f, 0.2f, 1f); // Neon Yellow
        pixmap.fillCircle(centerX, centerY, 58); // Outer edge

        // 3. Layer 1 (Base): Dark semi-transparent center (Cuts the hole)
        pixmap.setColor(0f, 0f, 0f, 0.6f); // Dark tint
        pixmap.fillCircle(centerX, centerY, 50); // Inner hole radius

        Texture texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();
        return texture;
    }

    /**
     * Generates a smooth BitmapFont keying off a .ttf file if present.
     * Falls back to scaled default if not found.
     */
    /**
     * Generates a "True FreeType" font at the exact requested size.
     * Sharpness is prioritized.
     */
    public static BitmapFont getFont(int size) {
        String fontPath = "font.ttf";
        if (!Gdx.files.internal(fontPath).exists()) {
            BitmapFont font = new BitmapFont();
            // Fallback: No scaling, just raw default font to avoid blurry scaling
            // font.getData().setScale(size / 16f); // REMOVED SCALING
            return font;
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;
        parameter.genMipMaps = true; // Smooth downscaling if needed
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.packer = null;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 1; // Subtle border for readability
        parameter.borderColor = Color.BLACK;
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;
        parameter.shadowColor = new Color(0, 0, 0, 0.5f);

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        // Ensure the texture uses the filters we requested (Generator params set them
        // on the data, but let's be sure)
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        return font;
    }

    /**
     * Generates a smooth BitmapFont keying off a .ttf file if present.
     * Falls back to scaled default if not found.
     */
    public static BitmapFont generateFont(int size) {
        String fontPath = "font.ttf"; // Expecting this in assets root
        if (!Gdx.files.internal(fontPath).exists()) {
            // Fallback if missing
            BitmapFont font = new BitmapFont();
            font.getData().setScale(size / 16f); // Approx scale based on 16px default
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            return font;
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.BLACK;
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;
        parameter.shadowColor = new Color(0, 0, 0, 0.5f);

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    public static BitmapFont getInfoBodyFont() {
        return getFont(22);
    }

    /**
     * Generates "Traditional" solid buttons.
     * Up: Dark Slate Gray (#2C3E50)
     * Down/Over: Steel Blue (#4682B4)
     * Border: 2px White
     */
    /**
     * Loads the custom "Gradient Rectangle" button asset.
     * Uses NinePatch to preserve corners (12px padding).
     */
    public static void loadCustomButtonSkin(Skin skin) {
        String texturePath = "button_rectangle_gradient.png";
        if (!Gdx.files.internal(texturePath).exists()) {
            System.err.println("ERROR: Missing " + texturePath + ". Falling back to standard generation.");
            generateStandardButtonSkin(skin);
            return;
        }

        Texture buttonTex = new Texture(Gdx.files.internal(texturePath));
        buttonTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        // Create NinePatch (12px splits)
        com.badlogic.gdx.graphics.g2d.NinePatch patch = new com.badlogic.gdx.graphics.g2d.NinePatch(buttonTex, 12, 12,
                12, 12);
        com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable drawableUp = new com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable(
                patch);

        skin.add("btn-custom-up", drawableUp, com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);

        // Down State (Tinted)
        com.badlogic.gdx.scenes.scene2d.utils.Drawable drawableDown = drawableUp.tint(Color.LIGHT_GRAY);
        skin.add("btn-custom-down", drawableDown, com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        // Use 'ui' font if available, or default
        if (skin.has("ui", BitmapFont.class)) {
            style.font = skin.getFont("ui");
        } else {
            style.font = skin.getFont("default");
        }
        style.fontColor = Color.WHITE;

        style.up = skin.getDrawable("btn-custom-up");
        style.down = skin.getDrawable("btn-custom-down");
        style.over = skin.getDrawable("btn-custom-down");

        // Register styles
        skin.add("cyber", style);
        skin.add("default", style);
    }

    public static void generateStandardButtonSkin(Skin skin) {
        int width = 200;
        int height = 60;
        int radius = 10;

        // Up State
        Pixmap upMap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        upMap.setColor(0.17f, 0.24f, 0.31f, 1f); // Dark Slate Gray #2C3E50
        fillRoundedRect(upMap, 0, 0, width, height, radius);
        upMap.setColor(Color.WHITE); // 2px Border
        drawRoundedRect(upMap, 0, 0, width, height, radius, 2);
        skin.add("btn-standard-up", new Texture(upMap));

        // Down/Over State
        Pixmap downMap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        downMap.setColor(0.27f, 0.51f, 0.71f, 1f); // Steel Blue #4682B4
        fillRoundedRect(downMap, 0, 0, width, height, radius);
        downMap.setColor(Color.WHITE); // 2px Border
        drawRoundedRect(downMap, 0, 0, width, height, radius, 2);
        skin.add("btn-standard-down", new Texture(downMap));

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        // Use 'ui' font if available, or default
        if (skin.has("ui", BitmapFont.class)) {
            style.font = skin.getFont("ui");
        } else {
            style.font = skin.getFont("default");
        }
        style.fontColor = Color.WHITE;
        style.up = skin.newDrawable("btn-standard-up");
        style.down = skin.newDrawable("btn-standard-down");
        style.over = skin.newDrawable("btn-standard-down");

        skin.add("cyber", style);
        skin.add("default", style);

        upMap.dispose();
        downMap.dispose();
    }

    // Draw rounded rect helper
    private static void fillRoundedRect(Pixmap p, int x, int y, int w, int h, int r) {
        p.fillRectangle(x + r, y, w - 2 * r, h);
        p.fillRectangle(x, y + r, w, h - 2 * r);
        p.fillCircle(x + r, y + r, r);
        p.fillCircle(x + w - r, y + r, r);
        p.fillCircle(x + r, y + h - r, r);
        p.fillCircle(x + w - r, y + h - r, r);
    }

    private static void drawRoundedRect(Pixmap p, int x, int y, int w, int h, int r, int thickness) {
        p.setBlending(Pixmap.Blending.SourceOver);
        for (int i = 0; i < thickness; i++) {
            drawSingleRoundedRect(p, x + i, y + i, w - 2 * i, h - 2 * i, r - i < 0 ? 0 : r - i);
        }
    }

    private static void drawSingleRoundedRect(Pixmap p, int x, int y, int w, int h, int r) {
        p.drawLine(x + r, y, x + w - r, y);
        p.drawLine(x + r, y + h - 1, x + w - r, y + h - 1);
        p.drawLine(x, y + r, x, y + h - r);
        p.drawLine(x + w - 1, y + r, x + w - 1, y + h - r);
        p.drawCircle(x + r, y + r, r);
        p.drawCircle(x + w - r, y + r, r);
        p.drawCircle(x + r, y + h - r, r);
        p.drawCircle(x + w - r, y + h - r, r);
    }

    public static com.badlogic.gdx.utils.I18NBundle i18n;

    public static void loadSystems() {
        if (i18n == null) {
            i18n = com.badlogic.gdx.utils.I18NBundle.createBundle(Gdx.files.internal("i18n/MyBundle"));
        }
    }

    public static void loadSquarePopupStyle(Skin skin) {
        String texturePath = "button_square_gradient.png";
        if (!Gdx.files.internal(texturePath).exists()) {
            System.err.println("ERROR: Missing " + texturePath + ". Skipping popup style.");
            return;
        }

        Texture popupTex = new Texture(Gdx.files.internal(texturePath));
        popupTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        com.badlogic.gdx.graphics.g2d.NinePatch patch = new com.badlogic.gdx.graphics.g2d.NinePatch(popupTex, 14, 14,
                14, 14);
        com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable drawable = new com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable(
                patch);

        skin.add("popup-bg", drawable, com.badlogic.gdx.scenes.scene2d.utils.Drawable.class);

        com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle ws = new com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle();
        ws.background = skin.getDrawable("popup-bg");
        if (skin.has("title", BitmapFont.class)) {
            ws.titleFont = skin.getFont("title");
        } else {
            ws.titleFont = getFont(32);
        }
        ws.titleFontColor = Color.CYAN;

        skin.add("square-dialog", ws);
        // Ensure default window style uses "square-dialog" explicitly or strictly use
        // square-dialog for popup
        skin.add("default", ws);
    }
}
